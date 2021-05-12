package com.definesys.dsgc.service.utils;
/**
 * byte字节数组操作工具类
 * @author Administrator
 *
 */
public class ByteUtil {

	/**
	 * byte数组截取(按长度)
	 * 
	 * @param bytes
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subbytes(byte[] bytes, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++) {
			bs[i - begin] = bytes[i];
		}
		return bs;
	}

	/**
	 * byte数组截取(按位置)
	 * 
	 * @param bytes
	 * @param begin
	 * @param end
	 * @return
	 */
	public static byte[] subbytes(byte[] bytes, int begin) {
		byte[] bs = new byte[bytes.length - begin];
		for (int i = begin; i < bytes.length; i++) {
			bs[i - begin] = bytes[i];
		}
		return bs;
	}

	/**
	 * byte数组拼接
	 * 
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static byte[] appbytes(byte[] byte_1, byte[] byte_2) {
		if(byte_1 == null || byte_1.length == 0){
			if(byte_2 == null || byte_2.length == 0){
				return byte_1;
			}else{
				return byte_2;
			}
		}else{
			if(byte_2 == null || byte_2.length == 0){
				return byte_1;
			}else{
				byte[] byte_3 = new byte[byte_1.length + byte_2.length];
				for (int i = 0; i < byte_1.length; i++) {
					byte_3[i] = byte_1[i];
				}
				for (int i = 0; i < byte_2.length; i++) {
					byte_3[i + byte_1.length] = byte_2[i];
				}
				// System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
				// System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
				return byte_3;
			}
		}
	}

	/**
	 * int转byte数组
	 * 
	 * @param num
	 * @param len
	 * @return
	 */
	public static byte[] intToByte(int num, int len) {
		byte[] result = new byte[len];
		for (int i = 0; (i < 4) && (i < len); i++) {
			result[i] = (byte) (num >> 8 * i & 0xFF);
		}
		return result;
	}

	/**
	 * byte数组转int
	 * 
	 * @param bs
	 * @return
	 */
	public static int byteToInt(byte[] bs) {
		int count = 0;
		byte result;
		for (int i = 0; i < bs.length; i++) {
			result = bs[i];
			count += (result & 0xFF) << (8 * i);
		}
		return count;
	}
}