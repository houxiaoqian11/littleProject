package com.sumavision.tvpay.connector.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.sumavision.tvpay.commons.ftp.FtpUtils;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class CommonUtil {
	
	final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS"); // 精确到微秒
	
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS"); // 精确到微秒
	
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static SimpleDateFormat sdf5 = new SimpleDateFormat("HHmmssSSS"); // 精确到毫秒
	
	public static SimpleDateFormat sdf6 = new SimpleDateFormat("HHmmss"); // 精确到秒

	
	/**
	 * 判断主机是否可达
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static boolean isAliveHost(String ip) throws Exception{
		
		InetAddress address = InetAddress.getByName(ip);
		return address.isReachable(2000);
	}
	/**
	 * 写Smb共享文件
	 * @param fileName 文件名
	 * @param result 内容
	 * @param ppc 配置读取
	 * @throws Exception
	 */
	public static void processSmbFile(String fileName, String result, ReloadablePlaceHolderResourceBundleMessageSource ppc ) {
		String users = ppc.getMessage("shareUsers");
		String pwds = ppc.getMessage("sharePsws");
		String shareDictions = ppc.getMessage("shareDictions");
		int length = users.split(",").length;
		for(int i=0;i<length;i++){
			String user = users.split(",")[i];
			String pwd = pwds.split(",")[i];
			String shareDiction = shareDictions.split(",")[i];
			String remoteUrl = "smb://" + user + ":" + pwd + "@" + shareDiction;
			String hostIp = shareDiction.substring(0,shareDiction.indexOf("/"));
			try {
				if (CommonUtil.isAliveHost(hostIp)) {
					MessageToRemoteFile.writeRemoteFile(remoteUrl, result,fileName);
				} else {
					logger.error("写入共享文件[ " + fileName + "  ]失败，原因:主机不可达");
				}
			} catch (Exception e) {
				logger.error("写入共享文件[ " + fileName + "  ]出现异常",e);
			}
		}
	}
	
	/**
	 * 写本地同步请求文件
	 * @param fileName 文件名
	 * @param result 内容
	 * @param ppc 配置读取
	 * @throws Exception
	 */
	public static void processLocalFile(String fileName, String result, ReloadablePlaceHolderResourceBundleMessageSource ppc ) {
		FileOutputStream f = null;
		try {
			String localPath = ppc.getMessage("shareFilePath");
			localPath = localPath.endsWith(File.separator)?localPath : File.separator + localPath;
			f = new FileOutputStream(localPath + fileName);
			f.write(result.getBytes());
		} catch (IOException e) {
			logger.error("写入本地文件[ " + fileName + "  ]出现异常",e);
		}finally{
			try {
				if(f!= null)
					f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取文件内容
	 * 
	 * @param path
	 * @param fname
	 * @return
	 * @throws Exception
	 */
	public static String readFileContent(String path, String fname)
			throws Exception {
		FileInputStream f = new FileInputStream(path + fname);
		ByteArrayOutputStream ns = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int a = 0;
		while ((a = f.read(b)) != -1) {
			ns.write(b, 0, a);
		}
		f.close();
		ns.close();
		return ns.toString();

	}
	
	/**
	 * 写本地文件
	 * @param fname 文件名
	 * @param fcontent 文件内容
	 * @return 是否写成功
	 */
	public static boolean recordFileContent(String fname, String fcontent){
		boolean ret = true;
		FileOutputStream f = null;
		try {
			File file = new File(fname);
			if(!file.exists())
				file.createNewFile();
			f = new FileOutputStream(fname);
			f.write(fcontent.getBytes());
		} catch (IOException e) {
			logger.error("写入本地文件[ " + fname + "  ]出现异常");
			ret = false;
		}finally{
			try {
				if(f!= null)
					f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;

	}
	
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
		// TODO Auto-generated method stub
		BigDecimal bg = new BigDecimal(amount);
		bg = bg.divide(new BigDecimal(100));
		
		return bg;
	}
	
	/**计算摘要值
	 * 
	 * @param src
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMd5( String src) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
			byte [] dg = md.digest();
			return toHex(dg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return src;
	}
	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}
	
	/**
	 * 上传认证结果到Ftp
	 * @param fileName 卡信息
	 * @param md5FileName 文件名
	 * @param bindId 绑定ID
	 * @param ppc
	 */
	public static void uploadAuth2Ftp(String fileName, String md5FileName,
			String bindId, ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		String ftpFilePath = ppc.getMessage("ftpFilePath");
		String ftpServerIp = ppc.getMessage("ftpServerIp");
		String ftpUser = ppc.getMessage("ftpUser");
		String ftpPasswd = ppc.getMessage("ftpPasswd");
		String localFilePath = ppc.getMessage("localFilePath");
		int ftpServerPort = Integer.parseInt(ppc.getMessage("ftpServerPort"));
		localFilePath = localFilePath.endsWith("/")?localFilePath :localFilePath +  File.separator;
		ftpFilePath = ftpFilePath.endsWith("/")?ftpFilePath : (ftpFilePath + "/");
		String localFileName = localFilePath + md5FileName;
		String ftpFileName = ftpFilePath + md5FileName;
		boolean beal = CommonUtil.recordFileContent(localFileName, fileName + "," + bindId);
		if(beal)
			try{
				FtpUtils.uploadFile(localFileName, ftpFileName, ftpServerIp, ftpServerPort, ftpUser, ftpPasswd);
				logger.info("Ftp上传认证文件成功");
			}catch (Exception e) {
				logger.info("Ftp上传认证文件出现异常" ,e);
			}
	}
	
	public static String getMerchantId(RedisLock redisLock,RedisTemplate redisTemplate) throws Exception{
		
		if(redisLock.lock()){
			//拿到redis锁
			//获取商户信息列表
			ListOperations<String, String> vo = redisTemplate.opsForList();
			List<String> merchantInfoJson = vo.range("LIST_MERCHANT_INFO", 0, -1);
			System.out.println(merchantInfoJson);
			
			redisLock.unlock();
		}else{
			System.out.println("获取redis锁失败");
		}
		
		return null;
	}
	

}
