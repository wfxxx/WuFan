package com.definesys.dsgc.service.ystar.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import com.definesys.dsgc.service.ystar.file.bean.ResultEntity;
import com.definesys.dsgc.service.ystar.file.bean.ScpConnectEntity;
import com.jcraft.jsch.*;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static String saveFile(String path, String content) {
        String result = "E";
        FileWriter writer = null;
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            result = "S";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean createFile(String fileNameWithPath, String fileContent) {
        String res = saveFile(replaceFileSeparator(fileNameWithPath), fileContent);
        if ("S".equals(res)) {
            return true;
        } else {
            return false;
        }
    }


    public static void createDirectory(String filePath) {
        File file = new File(replaceFileSeparator(filePath));
        if (!file.exists()) {//如果文件夹不存在
            file.getParentFile().mkdirs();
            file.mkdir();//创建文件夹
        }
    }

    public static String replaceFileSeparator(String path) {
        if (path != null && path.length() > 0) {
            if ("\\".equals(File.separator)) {
                path = path.replaceAll("/", "\\\\");
            } else {
                path = path.replaceAll("\\\\", "/");
            }
        }
        return path;
    }

    /**
     * 根据文件名获取文件后缀
     *
     * @param fileNamePath
     * @return
     */
    public static String getFileSuffixInFileName(String fileNamePath) {
        String suffix = "";
        if (fileNamePath != null) {
            int idx = fileNamePath.indexOf(".");
            if (idx != -1) {
                suffix = fileNamePath.substring(idx);
            }
        }
        return suffix;
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest) throws IOException {
        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
            dest.createNewFile();
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }


    public static void remoteUploadFile(ScpConnectEntity scpConnectEntity, File file,
                                        String remoteFileName) throws JSchException, IOException {
        Connection connection = null;
        Session session = null;
        SCPOutputStream scpOs = null;
        FileInputStream fis = null;
        try {
            createDir(scpConnectEntity);
        } catch (JSchException e) {
            throw e;
        }
        try {
            connection = new Connection(scpConnectEntity.getTrgIp());
            connection.connect();
            if (!connection.authenticateWithPassword(scpConnectEntity.getUserName(), scpConnectEntity.getPassWord())) {
                throw new RuntimeException("SSH连接服务器失败");
            }
            session = connection.openSession();
            SCPClient scpClient = connection.createSCPClient();

            scpOs = scpClient.put(remoteFileName, file.length(), scpConnectEntity.getTrgPath(), "0666");
            fis = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int hasMore = fis.read(buf);

            while (hasMore != -1) {
                scpOs.write(buf);
                hasMore = fis.read(buf);
            }
        } catch (IOException e) {
            throw new IOException("SSH上传文件至服务器出错" + e.getMessage());
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != scpOs) {
                try {
                    scpOs.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != session) {
                session.close();
            }
            if (null != connection) {
                connection.close();
            }
        }
    }

    private static boolean createDir(ScpConnectEntity scpConnectEntity) throws JSchException {
        JSch jsch = new JSch();
        com.jcraft.jsch.Session sshSession = null;
        Channel channel = null;
        try {
            sshSession = jsch.getSession(scpConnectEntity.getUserName(), scpConnectEntity.getTrgIp(), 22);
            sshSession.setPassword(scpConnectEntity.getPassWord());
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            throw new JSchException("SFTP连接服务器失败" + e.getMessage());
        }
        ChannelSftp channelSftp = (ChannelSftp) channel;
        if (isDirExist(scpConnectEntity.getTrgPath(), channelSftp)) {
            channel.disconnect();
            channelSftp.disconnect();
            sshSession.disconnect();
            return true;
        } else {
            String pathArray[] = scpConnectEntity.getTrgPath().split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArray) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                try {
                    if (isDirExist(filePath.toString(), channelSftp)) {
                        channelSftp.cd(filePath.toString());
                    } else {
                        // 建立目录
                        channelSftp.mkdir(filePath.toString());
                        // 进入并设置为当前目录
                        channelSftp.cd(filePath.toString());
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                    throw new JSchException("SFTP无法正常操作服务器" + e.getMessage());
                }
            }
        }
        channel.disconnect();
        channelSftp.disconnect();
        sshSession.disconnect();
        return true;
    }

    private static boolean isDirExist(String directory, ChannelSftp channelSftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    public static void main(String[] args) throws IOException {

    }
}

