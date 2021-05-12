package com.definesys.dsgc.service.ystar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 操作props文件
 *
 * @author afan
 * @version 1.0
 * @date 2020/10/6 16:52
 */
public class PropsUtils {

     private File prosFile;

     public PropsUtils(String prosPath){
         this.prosFile = new File(prosPath);
     }



    // 获取value
    public String getValue(String key)  {
         try {
             FileInputStream fis = new FileInputStream(prosFile);
             Properties properties = new Properties();
             properties.load(fis);
             return properties.getProperty(key);
         }catch (Exception e){
             e.printStackTrace();
         }
         return null;

    }

    /**
     * 新增配置
     * @param key
     * @param value
     */
    public void setValue(String key,String value){
        FileInputStream fis = null;
        FileOutputStream fos = null;
         try {
             fis = new FileInputStream(prosFile);
             Properties properties = new Properties();
             properties.load(fis);
             properties.setProperty(key, value);
             fos = new FileOutputStream(prosFile);
             properties.store(fos,"");
         }catch (Exception e){
             e.printStackTrace();
         }finally {
             try {
                 if(fos!=null){
                     fos.close();
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }

         }
    }

    /**
     * 新增属性
     * @param map
     */
    public void setValue(Map<String,String> map){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(prosFile);
            Properties properties = new Properties();
            properties.load(fis);
            for (Map.Entry<String, String> param : map.entrySet()) {
                properties.setProperty(param.getKey(),param.getValue());
            }
            fos = new FileOutputStream(prosFile);
            properties.store(fos,"");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null){
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
