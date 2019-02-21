package com.sumavision.tvpay.connector.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class CommonUtil {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS"); // 精确到微秒

	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS"); // 精确到微秒

	public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");

	public static SimpleDateFormat sdf5 = new SimpleDateFormat("HHmmssSSS"); // 精确到毫秒

	public static SimpleDateFormat sdf6 = new SimpleDateFormat("HHmmss"); // 精确到秒

	/**
	 * 获取指定日期的后一天
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getNextDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		String dayAfter = CommonUtil.sdf.format(c.getTime());
		return dayAfter;
	}

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(CommonUtil.getNextDay(date));
	}

	/**
	 * 元为单位的BigDecimal转为分为单位的字符串
	 * 
	 * @param bg
	 *            BigDecimal
	 * @return String
	 */
	public static String yuan2fenAsString(BigDecimal bg) {
		bg.multiply(new BigDecimal(100));
		DecimalFormat df = new DecimalFormat("#");
		String result = df.format(bg.multiply(new BigDecimal(100)));
		return result;
	}

	/**
	 * 分为单位的字符串转为分为单位的BigDecimal
	 * 
	 * @param String
	 * @return BigDecimal
	 */
	public static BigDecimal fen2YuanAsBigdecimal(String amount) {
		// TODO Auto-generated method stub
		BigDecimal bg = new BigDecimal(amount);
		bg = bg.divide(new BigDecimal(100));

		return bg;
	}

	public static String getMerchantId(RedisLock redisLock, RedisTemplate redisTemplate) throws Exception {

		if (redisLock.lock()) {
			// 拿到redis锁
			// 获取商户信息列表
			ListOperations<String, String> vo = redisTemplate.opsForList();
			List<String> merchantInfoJson = vo.range("LIST_MERCHANT_INFO", 0, -1);
			System.out.println(merchantInfoJson);

			redisLock.unlock();
		} else {
			System.out.println("获取redis锁失败");
		}

		return null;
	}

	/*
	 * public static void unrar(File srcRarPath, File dstDiretory) { Archive a =
	 * null; try { a = new Archive(srcRarPath); if (a != null) { //
	 * a.getMainHeader().print(); // 打印文件信息. FileHeader fh = a.nextFileHeader();
	 * while (fh != null) { // 防止文件名中文乱码问题的处理
	 * 
	 * String fileName = fh.getFileNameW().isEmpty() ? fh .getFileNameString() :
	 * fh.getFileNameW(); if (fh.isDirectory()) { // 文件夹 File fol = new
	 * File(dstDiretory.getAbsoluteFile() + File.separator + fileName);
	 * fol.mkdirs(); } else { // 文件 File out = new
	 * File(dstDiretory.getAbsoluteFile() + File.separator + fileName.trim());
	 * try { if (!out.exists()) { if (!out.getParentFile().exists()) {//
	 * 相对路径可能多级，可能需要创建父目录. out.getParentFile().mkdirs(); } out.createNewFile();
	 * } FileOutputStream os = new FileOutputStream(out); a.extractFile(fh, os);
	 * os.close(); } catch (Exception ex) { ex.printStackTrace(); } } fh =
	 * a.nextFileHeader(); } a.close(); } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

}
