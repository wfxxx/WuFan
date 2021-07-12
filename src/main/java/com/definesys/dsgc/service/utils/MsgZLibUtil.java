package com.definesys.dsgc.service.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
public class MsgZLibUtil {

    public static void main(String[] args) {
        String str = null;
        str = "<?xml version=\"1.0\"?><DATA><HEAD><BIZ_CODE>DMS-DCS-S50</BIZ_CODE><MESSAGE_FROM>DMS</MESSAGE_FROM><MESSAGE_TO>DCS</MESSAGE_TO></HEAD><BODY><TRepContract><Code>DJSA030RO1609230001</Code><Corpcode>DJSA030</Corpcode><OBJID>{3D5F04E4-22C7-DB23-E050-12AC24426542}</OBJID></TRepContract></BODY></DATA>";
        str = compress(str);
        System.out.println(str);

        str = "JgEAAHicVZBNq8IwEEX/iriPmaapIowjaSb6FEqg6UY3ItqFoFaqiCDvv1s/8GM559zhDoPDy27bOpf1cVPtB+2oA+0hIZvCEP45w4TpZL6wnh1xFgTbIEICKN8UMxeCGbvFKPfZPYPyh7x94anZ/thmRvmq8DwjLPLyYKv9qV6uToS2WpfE02AghtxHXeirGAAilA/T+Pqw+src+YugT6cTpmvMyQi000Ip2xOcqlg4SEBEylilteomWv2jfKZR/vbL51Hy8YobcPdTVQ==";
        try {
            str = decompress(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }

    /***
     * 针对HMS接口的压缩
     *
     * @param msg
     * @return
     */
    public static String compress(String msg) {
        byte[] xmlByte = null;
        byte[] lenByte = null;

        try {
            // 根据utf-8获取byte数组
            xmlByte = msg.getBytes("UTF-8");
            //System.out.println("msg的length：" + msg.length());
            //System.out.println("msg对应的byte数组length：" + xmlByte.length);
            // 获取msg以utf-8编码方式获取的byte数组的长度
            int len = xmlByte.length;
            // 将长度转byte
            lenByte = ByteUtil.intToByte(len, 4);
            //System.out.println("长度存储byte数组存储的value：" + ByteUtil.byteToInt(lenByte));
            // 压缩msg对应的byte数组
            xmlByte = ZlibUtils.compress(xmlByte);
            // 合并长度byte数组和内容byte数组
            xmlByte = ByteUtil.appbytes(lenByte, xmlByte);
            // Base64转码
            return new String(Base64Util.encryptBASE64(xmlByte), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /***
     * 针对HMS接口的解压
     *
     * @param msg
     * @return
     */
    public static String decompress(String msg) throws Exception {
        byte[] xmlByte = null;
        // Base64解码
        xmlByte = Base64Util.decryptBASE64(msg.getBytes("UTF-8"));
        // 截取4位之后的byte(就是xml报文的压缩形式)
        xmlByte = ByteUtil.subbytes(xmlByte, 4, xmlByte.length - 4);
        // 解压缩
        xmlByte = ZlibUtils.decompress(xmlByte);
        return new String(xmlByte, "UTF-8");
    }
}
