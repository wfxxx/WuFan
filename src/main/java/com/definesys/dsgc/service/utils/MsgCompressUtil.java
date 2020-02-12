package com.definesys.dsgc.service.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MsgCompressUtil {
    public MsgCompressUtil() {
        super();
    }

    public static String compress(String msg) {
        try {
            byte[] data = compressInByte(msg.getBytes("UTF-8"));
            return new BASE64Encoder().encodeBuffer(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String deCompress(String msg) {
        if (msg == null  ||  "".equals(msg)) {
            return msg;
        }
        try {

            return MsgZLibUtil.decompress(msg);
        } catch (Exception e) {
//      e.printStackTrace();
            try {
                byte[] data = deCompressInByte(new BASE64Decoder().decodeBuffer(msg));
                return new String(data, "UTF-8");
            } catch (Exception e1) {
                e1.printStackTrace();
                return msg;
            }
        }
    }

    private static byte[] compressInByte(byte[] data) {
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

    private static byte[] deCompressInByte(byte[] data) {
        byte[] outdata = null;
        Inflater decompresser = null;
        ByteArrayOutputStream bos = null;
        try {
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
        } catch (Exception e) {
            outdata = data;
            e.printStackTrace();
        } finally {
            decompresser.end();
            try {
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