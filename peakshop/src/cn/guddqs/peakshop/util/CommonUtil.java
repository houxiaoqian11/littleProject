package cn.guddqs.peakshop.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CommonUtil {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS"); // 精确到微秒
	
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS"); // 精确到微秒
	
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static SimpleDateFormat sdf5 = new SimpleDateFormat("HHmmssSSS"); // 精确到毫秒
	
	public static SimpleDateFormat sdf6 = new SimpleDateFormat("HHmmss"); // 精确到秒
	
	public static SimpleDateFormat sdf7 = new SimpleDateFormat("ddHHmmSSs");
	
	public static SimpleDateFormat sdf8 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 元为单位的BigDecimal转为分为单位的字符串
	 * @param bg BigDecimal
	 * @return  String
	 */
	public static String yuan2fenAsString(BigDecimal bg){
		bg.multiply(new BigDecimal(100));
		DecimalFormat df = new DecimalFormat("#");
		String result = df.format(bg.multiply(new BigDecimal(100)));
		return result;
	}
	/**
	 * 分为单位的字符串转为分为单位的BigDecimal
	 * @param String 
	 * @return BigDecimal
	 */
	public static BigDecimal fen2YuanAsBigdecimal(String amount) {
		BigDecimal bg = new BigDecimal(amount);
		bg = bg.divide(new BigDecimal(100));
		return bg;
	}
	
}
