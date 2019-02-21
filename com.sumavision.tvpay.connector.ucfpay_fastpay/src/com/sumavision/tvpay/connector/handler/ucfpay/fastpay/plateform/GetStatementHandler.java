//package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.sumavision.tvpay.commons.ftp.FtpUtils;
//import com.sumavision.tvpay.connector.Handler;
//import com.sumavision.tvpay.connector.HandlerAutowired;
//import com.sumavision.tvpay.connector.HandlerExtends;
//import com.sumavision.tvpay.connector.util.CommonUtil;
//import com.sumavision.tvpay.connector.util.Constant;
//import com.sumavision.tvpay.connector.vo.BankRecord;
//import com.sumavision.tvpay.exception.business.ConnectorException;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//
//@HandlerExtends(method = "PLA-CON-020", 
//				code = 19L, 
//				validate = "off", 
//				description = "银行交易查询接口（各种交易），生成对账单")
//public class GetStatementHandler implements Handler {
//
//	private String method;
//
//	private long code;
//
//	@HandlerAutowired(springName = "propertyConfigurer")
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//	
//	// log
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	public void setMethod(String method) {
//		this.method = method;
//	}
//
//	public String getMethod() {
//		return method;
//	}
//	
//	public void setCode(long code){
//		this.code = code;
//	}
//	
//	public long getCode(){
//		return this.code;
//	}
//
//	
//	@Override
//	public Map<String, Object> process(Map<String, Object> param) {
//        logger.info("厦门民生获取对账单，平台传入参数为：" + param);
//        Map<String,Object> returnMap = new HashMap<String,Object>();
//      //起始日期
//    	Date startTime = (Date)(param.get(Constant.START_TIME));
//    	int succedNum = 0;
//    	List<BankRecord> recordList = new ArrayList<BankRecord>();
//		try{
//			List<BankRecord> fastPayList = getFastPayRecord(startTime);
//			recordList.addAll(fastPayList);
//			logger.info("获取厦门民生快捷支付对账单成功!");
//			succedNum ++;
//		}catch (Exception e) {
//			logger.error("获取厦门民生快捷支付对账单异常,原因:",e);
////			returnMap.put(Constant.ACTION_RESULT,Constant.FAILED);
//		}
//		try{
//			List<BankRecord> refundList = getRefundRecord(startTime);
//			recordList.addAll(refundList);
//			logger.info("获取厦门民生快捷退款对账单成功!");
//			succedNum ++;
//		}catch (Exception e) {
//			logger.error("获取厦门民生快捷退款对账单异常,原因:",e);
//		}
//		String allMustBeSure = ppc.getMessage("allMustBeSure");
//		if("0".equals(allMustBeSure)){
//			if(succedNum >0){
//				returnMap.put(Constant.ACTION_RESULT,Constant.SUCCEED);
//				logger.error("获取厦门民生快捷获取对账单部分成功，返回对账成功");
//			}else{
//				returnMap.put(Constant.ACTION_RESULT,Constant.FAILED);
//				logger.error("获取厦门民生快捷获取对账单全部失败");
//			}
//		}else{
//			if(succedNum ==2){
//				returnMap.put(Constant.ACTION_RESULT,Constant.SUCCEED);
//				logger.error("获取厦门民生快捷获取对账单全部成功，返回对账成功");
//			}else{
//				returnMap.put(Constant.ACTION_RESULT,Constant.FAILED);
//				logger.error("获取厦门民生快捷获取对账单部分成功，返回对账失败");
//			}
//		}
//			
//		
//		returnMap.put(Constant.STATMENT,recordList);
//		logger.info("厦门民生获取对账单返回交易总条数" + recordList.size());
//		return returnMap;
//	}
//	/**
//	 * 快捷对账单
//	 * @param startTime 对账日期
//	 * @param startReal 厦门民生文件日期
//	 * @return
//	 */
//	private List<BankRecord> getFastPayRecord(Date startTime) {
//		List<BankRecord> array_list = new ArrayList<BankRecord>();
//		String localFilePath = ppc.getMessage("statementLocalFilePath");
//		String statementFtpFilePath = ppc.getMessage("statementFtpFilePath");
//		String ftpServerIp = ppc.getMessage("statementFtpServerIp");
//		int ftpServerPort = Integer.parseInt(ppc.getMessage("statementFtpServerPort"));
//		String ftpUser = ppc.getMessage("statementFtpUser");
//		String ftpPasswd = ppc.getMessage("statementFtpPasswd");
//		String statementSufFileName = ppc.getMessage("statementSufFileName");
//		String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
//		localFilePath = localFilePath.endsWith("/") ? localFilePath :  localFilePath + File.separator ;
//		statementFtpFilePath = statementFtpFilePath.endsWith("/")?statementFtpFilePath : statementFtpFilePath + File.separator;
//		InputStream fs = null;
//		BufferedReader bs = null;
//		try {
//			String payStatementPreFileName = ppc.getMessage("payStatementPreFileName");
//			String date = String.valueOf(CommonUtil.sdf.format(startTime));
//			String filepath = localFilePath + payStatementPreFileName + date + statementSufFileName;
//			String ftpFile = statementFtpFilePath + payStatementPreFileName + date + statementSufFileName;
//			FtpUtils.downloadFile(ftpFile, filepath, ftpServerIp,ftpServerPort, ftpUser, ftpPasswd);
//			// 解析对账单
//			fs = new FileInputStream(filepath);// 加载文件
//			bs = new BufferedReader(new InputStreamReader(fs, Constant.FAST_PAY_ENCODING));
//			String msg = "";
//			while ((msg = bs.readLine()) != null) {
//				if (!msg.endsWith("##") && msg.startsWith(fastPayMerchantId)) {
//					String[] linestr = msg.split("\\|");
//					if("S".equalsIgnoreCase(linestr[5])){
//						BankRecord bankRecord = new BankRecord();
//						String[] r = new String[6];
//						r[0] = "0";
//						r[1] = linestr[3].trim().substring(0,8);// 交易日期
//						String amou = linestr[16].trim();
//						amou = CommonUtil.fen2YuanAsBigdecimal(amou).toString();
//						r[2] = amou;// 金额
//						r[3] = linestr[2].trim();// 订单号
//						r[4] = linestr[4].trim();//银行票号
//		
//						bankRecord.setRecord(r);
//						bankRecord.setRawContent(msg);
//						bankRecord.setQueryDate(startTime);
//						array_list.add(bankRecord);
//					}
//				}
//			}
//			logger.info("获取快捷支付对账单正常,成功交易条数为: " + array_list.size());
//		} catch (Exception e) {
//			throw new ConnectorException("310050505", "获取厦门民生快捷对账单异常,原因:"
//					+ e.getMessage(), e);
//		}finally{
//			try {
//				if(bs!=null)
//					bs.close();
//				if(fs!=null)
//					fs.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		
//		}
//		
//		return array_list;
//	}
//	
//	/**
//	 * 退款对账单
//	 * @param startTime 对账日期
//	 * @param startReal 厦门民生文件日期
//	 * @return
//	 */
//	private List<BankRecord> getRefundRecord(Date startTime) {
//		List<BankRecord> array_list = new ArrayList<BankRecord>();
//		String localFilePath = ppc.getMessage("statementLocalFilePath");
//		String statementFtpFilePath = ppc.getMessage("statementFtpFilePath");
//		String ftpServerIp = ppc.getMessage("statementFtpServerIp");
//		int ftpServerPort = Integer.parseInt(ppc.getMessage("statementFtpServerPort"));
//		String ftpUser = ppc.getMessage("statementFtpUser");
//		String ftpPasswd = ppc.getMessage("statementFtpPasswd");
//		String statementSufFileName = ppc.getMessage("statementSufFileName");
//		String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
//		localFilePath = localFilePath.endsWith("/") ? localFilePath :  localFilePath + File.separator ;
//		statementFtpFilePath = statementFtpFilePath.endsWith("/")?statementFtpFilePath : statementFtpFilePath + File.separator;
//		InputStream fs = null;
//		BufferedReader bs = null;
//		
//		try {
//			String refundStatementPreFileName = ppc.getMessage("refundStatementPreFileName");
//			String date = String.valueOf(CommonUtil.sdf.format(startTime));
//			String filepath = localFilePath + refundStatementPreFileName + date + statementSufFileName;
//			String ftpFile = statementFtpFilePath + refundStatementPreFileName + date + statementSufFileName;
//			FtpUtils.downloadFile(ftpFile, filepath, ftpServerIp,ftpServerPort, ftpUser, ftpPasswd);
//			// 解析对账单
//			fs = new FileInputStream(filepath);// 加载文件
//			bs = new BufferedReader(new InputStreamReader(fs, Constant.FAST_PAY_ENCODING));
//			String msg = "";
//			while ((msg = bs.readLine()) != null) {
//				if (!msg.endsWith("##") && msg.startsWith(fastPayMerchantId)) {
//					String[] linestr = msg.split("\\|");
//					if("S".equalsIgnoreCase(linestr[5])){
//						BankRecord bankRecord = new BankRecord();
//						String[] r = new String[6];
//						r[0] = "2";
//						r[1] = linestr[3].trim().substring(0,8);// 交易日期
//						String amou = linestr[11].trim();
//						amou = CommonUtil.fen2YuanAsBigdecimal(amou).toString();
//						r[2] = amou;// 金额
//						r[3] = linestr[2].trim();// 订单号
//						r[4] = linestr[4].trim();//银行票号
//		
//						bankRecord.setRecord(r);
//						bankRecord.setRawContent(msg);
//						bankRecord.setQueryDate(startTime);
//						array_list.add(bankRecord);
//					}
//				}
//			}
//			logger.info("获取快捷支退款账单正常,退款成功交易条数为: " + array_list.size());
//		} catch (Exception e) {
//			throw new ConnectorException("310050505", "获取厦门民生快捷对账单异常,原因:"
//					+ e.getMessage(), e);
//		}finally{
//			try {
//				if(bs!=null)
//					bs.close();
//				if(fs!=null)
//					fs.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		
//		}
//		return array_list;
//	}
//}