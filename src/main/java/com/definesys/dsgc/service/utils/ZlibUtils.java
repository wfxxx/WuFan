package com.definesys.dsgc.service.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * ZLib压缩工具
 */
public abstract class ZlibUtils {

    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @return byte[] 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        byte[] outdata = null;
        Deflater compresser = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream(data.length);
            compresser = new Deflater();
            compresser.reset();
            compresser.setInput(data);
            compresser.finish();
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            outdata = bos.toByteArray();
        } catch (Exception e) {
            outdata = data;
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                compresser.end();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outdata;
    }

    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @param os   输出流
     */
    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = null;
        try {
            dos = new DeflaterOutputStream(os);
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩
     *
     * @param data 压缩的数据
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(byte[] data) throws Exception {
        byte[] outdata = null;
        Inflater decompresser = null;
        ByteArrayOutputStream bos = null;

        decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        while (!decompresser.finished()) {
            int i = decompresser.inflate(buf);
            bos.write(buf, 0, i);
        }
        outdata = bos.toByteArray();

        decompresser.end();
        try {
            if (bos != null) {
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outdata;
    }

    /**
     * 解压缩
     *
     * @param is 输入流
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = null;
        ByteArrayOutputStream bos = null;
        byte[] outdata = null;
        int length = 0;
        try {
            iis = new InflaterInputStream(is);
            bos = new ByteArrayOutputStream(1024);
            byte[] buf = new byte[1024];
            while ((length = iis.read(buf, 0, length)) > 0) {
                bos.write(buf, 0, length);
            }
            outdata = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            outdata = "".getBytes();
        } finally {
            try {
                if (iis != null) {
                    iis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outdata;
    }
}
