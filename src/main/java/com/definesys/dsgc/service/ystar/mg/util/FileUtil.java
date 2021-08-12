package com.definesys.dsgc.service.ystar.mg.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class FileUtil {

    public static String getPrjFileContent(String filePath) {
        InputStream inputStream = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            File f = new File(filePath);
            inputStream = new FileInputStream(f);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getMuleTemplateFileContent(String type, String name) {
        String lastSub = "xml".equals(type.toLowerCase()) ? ".xml" : ".properties";
        String fileName = "mule-template/" + name + lastSub;
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        String path = classPathResource.getClassLoader().getResource(fileName).getPath();
        return getPrjFileContent(path);
    }

    public static boolean createFile(String fileNameWithPath, String fileContent) {
        String res = saveFile(replaceFileSeparator(fileNameWithPath), fileContent);
        if ("S".equals(res)) {
            return true;
        } else {
            return false;
        }
    }

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
     * 根据key=value修改配置文件内容
     *
     * @param removeFlag 是否删除
     * @param path
     * @param key
     * @param value
     */
    public static void removeOrReplaceFileContent(boolean removeFlag, String path, String key, String value) {
        File file = new File(path);
        BufferedReader br = null;
        FileReader in = null;
        // 内存流, 作为临时流
        CharArrayWriter tempStream = null;
        FileWriter out = null;
        try {
            in = new FileReader(file);
            br = new BufferedReader(in);
            // 内存流, 作为临时流
            tempStream = new CharArrayWriter();
            // 替换
            String line = null;
            boolean hasWrite = false;
            while ((line = br.readLine()) != null) {
                if (line.contains(key + "=")) {
                    if (!removeFlag) {
                        line = key + "=" + value;
                    } else {
                        line = "";
                    }
                    hasWrite = true;
                }
                // 将该行写入内存
                tempStream.write(line);
                tempStream.append(System.getProperty("line.separator"));// 添加换行符
            }
            if (!hasWrite && !removeFlag) {
                tempStream.write(key + "=" + value);
                tempStream.append(System.getProperty("line.separator"));// 添加换行符
            }
            // 将内存中的流 写入 文件
            out = new FileWriter(path);
            tempStream.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(tempStream);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(in);
        }
    }

    public static void createDirectory(String filePath) {
        File file = new File(replaceFileSeparator(filePath));
        if (!file.exists()) {//如果文件夹不存在
            file.getParentFile().mkdirs();
            file.mkdir();//创建文件夹
        }
    }


}
