package com.definesys.dsgc.service.ystar.utils;

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

    public static void main(String[] args) throws IOException {
        String fileNameWithPath = replaceFileSeparator("/Users/ystar/mywords/ZhenYi/Git/ESB_DEV_Demo/zy-demo-proj/aaaa/test.xml");
        String fileContent = "<text>1234444</text>";
        createFile(fileNameWithPath, fileContent);


//        String fileName = "test.txt";
//        System.out.println("File.separator:" + File.separator);
//        File testFile = new File("D:" + File.separator + "filepath" + File.separator + "test" + File.separator + fileName);
//        File fileParent = testFile.getParentFile();//返回的是File类型,可以调用exsit()等方法
//        String fileParentPath = testFile.getParent();//返回的是String类型
//        System.out.println("fileParent:" + fileParent);
//        System.out.println("fileParentPath:" + fileParentPath);
//        if (!fileParent.exists()) {
//            fileParent.mkdirs();// 能创建多级目录
//        }
//        if (!testFile.exists())
//            testFile.createNewFile();//有路径才能创建文件
//        System.out.println(testFile);
//
//        String path = testFile.getPath();
//        String absolutePath = testFile.getAbsolutePath();//得到文件/文件夹的绝对路径
//        String getFileName = testFile.getName();//得到文件/文件夹的名字
//        System.out.println("path:" + path);
//        System.out.println("absolutePath:" + absolutePath);
//        System.out.println("getFileName:" + getFileName);
    }
}

