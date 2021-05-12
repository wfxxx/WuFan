package com.definesys.dsgc.service.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Util {

	public static void main(String[] args) throws Exception {
		StringBuffer message = new StringBuffer("");
		message.append("<DATA><HEAD><BIZID>HMS.DMS_HD01_20160712233059106</BIZID><COUNT>3</COUNT><CONSUMER>HMS</CONSUMER><SRVLEVEL>1</SRVLEVEL><ACCOUNT></ACCOUNT><PASSWORD></PASSWORD></HEAD><BODY><ITEM><DEALER_CODE>DGDR010</DEALER_CODE><DEALER_NAME>清远纬信汽车贸易有限公司</DEALER_NAME><DEALER_TYPE>90101001</DEALER_TYPE><DEALER_SHORTNAME></DEALER_SHORTNAME><STATUS>10011001</STATUS><DEALER_LEVEL>11011001</DEALER_LEVEL><PARENT_DEALER_CODE></PARENT_DEALER_CODE><ORG_CODE>GD2</ORG_CODE><PROVINCE_NAME>440000</PROVINCE_NAME><CITY_NAME>441800</CITY_NAME><ZIP_CODE>511517</ZIP_CODE><ADDRESS>清远市新城西百加格塘清广大道旁</ADDRESS><PHONE>0763-3665333</PHONE><FAX_NO></FAX_NO><LINK_MAN>贺庆锋</LINK_MAN><LINK_MOBILE>18024848889</LINK_MOBILE><EMAIL>N/A</EMAIL><BEGIN_BANK>中国工商银行股份有限公司清远新城支行-2018020609200222008</BEGIN_BANK><BANK_CODE>2018020609200222008</BANK_CODE><TAX_NO>441804086828270</TAX_NO><FOUND_DATE>2014-03-31 11:07:56</FOUND_DATE><SHOP_LEVEL></SHOP_LEVEL><SERVICE_LEVEL></SERVICE_LEVEL><FIXED_ASSETS></FIXED_ASSETS><CURRENT_ASSETS></CURRENT_ASSETS><OPEN_DATE>2014-03-31 11:07:56</OPEN_DATE><ONLINE_STATUS>12781001</ONLINE_STATUS><BUSINESS_STATUS>12341002</BUSINESS_STATUS><REVOKE_DATE></REVOKE_DATE><HOTLINK_PHONE>18024848889</HOTLINK_PHONE><BESPEAK_PHONE></BESPEAK_PHONE><DMSONLINE_DATE>2014-03-31 11:07:56</DMSONLINE_DATE><SERVICE_NUM>18024848889</SERVICE_NUM><RESCUING_NUM></RESCUING_NUM><SALES_NUM>18024848889</SALES_NUM><REMARK></REMARK></ITEM><ITEM><DEALER_CODE>DGDL030</DEALER_CODE><DEALER_NAME>惠州市润睿汽车贸易有限公司</DEALER_NAME><DEALER_TYPE>90101001</DEALER_TYPE><DEALER_SHORTNAME></DEALER_SHORTNAME><STATUS>10011001</STATUS><DEALER_LEVEL>11011001</DEALER_LEVEL><PARENT_DEALER_CODE></PARENT_DEALER_CODE><ORG_CODE>GD2</ORG_CODE><PROVINCE_NAME>440000</PROVINCE_NAME><CITY_NAME>441300</CITY_NAME><ZIP_CODE>516001</ZIP_CODE><ADDRESS>惠州市惠阳区淡水桥背村永兴路</ADDRESS><PHONE>0752-3768836</PHONE><FAX_NO></FAX_NO><LINK_MAN>吴县辉</LINK_MAN><LINK_MOBILE>13809837388</LINK_MOBILE><EMAIL>N/A</EMAIL><BEGIN_BANK>中国建设银行股份有限公司惠州排坊支行-44050171714200000038</BEGIN_BANK><BANK_CODE>44050171714200000038</BANK_CODE><TAX_NO>91441303MA4UM3PRX6</TAX_NO><FOUND_DATE>2016-07-06 11:00:37</FOUND_DATE><SHOP_LEVEL></SHOP_LEVEL><SERVICE_LEVEL></SERVICE_LEVEL><FIXED_ASSETS></FIXED_ASSETS><CURRENT_ASSETS></CURRENT_ASSETS><OPEN_DATE>2016-07-06 11:00:37</OPEN_DATE><ONLINE_STATUS>12781001</ONLINE_STATUS><BUSINESS_STATUS>12341002</BUSINESS_STATUS><REVOKE_DATE></REVOKE_DATE><HOTLINK_PHONE>13809837388</HOTLINK_PHONE><BESPEAK_PHONE></BESPEAK_PHONE><DMSONLINE_DATE>2016-07-06 11:00:37</DMSONLINE_DATE><SERVICE_NUM>13809837388</SERVICE_NUM><RESCUING_NUM></RESCUING_NUM><SALES_NUM>13809837388</SALES_NUM><REMARK></REMARK></ITEM></BODY></DATA>");
		
		String str = message.toString();
		
		str = encryptBASE64(str);
		System.out.println(str);
		
		str = decryptBASE64(str);
		System.out.println(str);

	}

	/**
	 * BASE64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptBASE64(final byte[] data) {
		try{
			if (data == null || data.length == 0) {
				return data;
			}
			return new BASE64Encoder().encodeBuffer(data).replaceAll("[\\s*\t\n\r]", "").getBytes();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * BASE64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(final String str) {
		try{
			if (str == null || "".equals(str)) {
				return "";
			}
			return new String(encryptBASE64(str.getBytes()));
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * BASE64解码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(final byte[] data) {
		if (data == null || data.length == 0) {
			return data;
		}

		InputStream is = null;
		try {
			is = new ByteArrayInputStream(data);
			return new BASE64Decoder().decodeBuffer(is);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * BASE64解码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decryptBASE64(final String str) {
		try{
			if (str == null || "".equals(str)) {
				return "";
			}
			return new String(decryptBASE64(str.getBytes()));
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
