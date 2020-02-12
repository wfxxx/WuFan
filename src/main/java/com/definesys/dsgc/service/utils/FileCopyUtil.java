package com.definesys.dsgc.service.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/5/29 上午9:53
 * @history: 1.2019/5/29 created by biao.luo
 */
public  class FileCopyUtil {
   private List<String> filelist = new ArrayList<>();

    public FileCopyUtil(){

    }

    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("请输入源目录：");
//        String sourcePath = sc.nextLine();
//        System.out.println("请输入新目录：");
//        String path = sc.nextLine();

      //  String sourcePath = "/Users/mac/Public/old";
        String path = "/Users/mac/Desktop/Public/svndir";
       // System.out.println(sourcePath + "   " + isEmptyDir(new File(sourcePath)));
        FileCopyUtil fileCopyUtil = new FileCopyUtil();
        List<String> stringList = fileCopyUtil.getFileList(path);
        //List<String> stringList = FileCopyUtil.getChildDir(path);
        //List<String> stringList = getChildDir(path);
        if (stringList.size() > 0) {
            System.out.println("[  size ======" + stringList.size());
            for (String ss : stringList) {
                System.out.println("ssss  ======" + ss);
            }
        }

        //copyDir(sourcePath, path);
    }

    /**
     * 复制目录，如果该目录下有文件，同时复制文件
     *
     * @param sourcePath
     * @param newPath
     * @throws IOException
     */
    public static void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        File newFilePath = new File(newPath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            newFilePath.mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + File.separator + filePath[i])).isDirectory()
                    && ! (new File(sourcePath + File.separator + filePath[i]).getAbsolutePath().contains(".svn") ) ){
                copyDir(sourcePath + File.separator + filePath[i], newFilePath + File.separator + filePath[i]);
            }

            if (new File(sourcePath + File.separator + filePath[i]).isFile()) {
                copyFile(sourcePath + File.separator + filePath[i], newFilePath + File.separator + filePath[i]);
            }

        }
    }


    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(file);
            byte[] buffer = new byte[2097152];
            int readByte = 0;
            while ((readByte = in.read(buffer)) != -1) {
                out.write(buffer, 0, readByte);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    /**
     * 递归获取文件目录与文件 的绝对路径
     *
     * @param strPath
     * @return
     */
    public  List<String> getFileList(String strPath) {

        File dir = new File(strPath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    if( !fileName.contains(".svn") && ! fileName.contains(".DS_Store") ){
                        filelist.add(files[i].toString());
                        // 获取文件绝对路径
                        getFileList(files[i].getAbsolutePath());
                    }
                } else {
                    // 判断文件名是否以.avi结尾
                    //String strFileName = files[i].getAbsolutePath();
                    //System.out.println("---" + strFileName);
                    if( !fileName.contains(".svn") && ! fileName.contains(".DS_Store") ){
                        filelist.add(files[i].getAbsolutePath());
                        continue;
                    }

                }
            }

        }
        return filelist;
    }

    /**
     * 获取当前目录下的子文件，不包含子文件的子文件
     *
     * @param strPath
     * @return
     */
    public static List<String> getChildDir(String strPath) throws IOException {
        List<String> childList = new ArrayList<>();
        File dir = null;
        dir = new File(strPath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getAbsolutePath();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
//                    if( !fileName.contains(".svn") && ! fileName.contains(".DS_Store") ){
                    if( !fileName.contains(".") ){
                        childList.add(fileName);
                    }

                }
            }

        }

        return childList;


    }


    /**
     * 判断文件是否是空目录
     *
     * @param strPath
     * @return
     */
    public static boolean isEmptyDir(File strPath) {
        // 该文件目录下文件全部放入数组
        File[] files = strPath.listFiles();
        return files == null || files.length == 0 ? true : false;
    }


    /**
     * 批量删除文件，传入文件绝对路径
     * @param strPath
     * @return
     */
    public static boolean batchDelFile(List<String> strPath){
        try{
            for(String str : strPath){
                File file = new File(str);
                if(file.exists()){
                    file.delete();

                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
