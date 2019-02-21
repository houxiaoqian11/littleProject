//package com.sumavision.tvpay.connector.handler.ucfpay.plateform;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.XMLGregorianCalendar;
//
//import mtc.wsdl_to_java_obj.statement.StatementItem;
//import mtc.wsdl_to_java_obj.statement.StatementRequest;
//import mtc.wsdl_to_java_obj.statement.StatementResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.sumavision.tvpay.connector.Handler;
//import com.sumavision.tvpay.connector.HandlerAutowired;
//import com.sumavision.tvpay.connector.HandlerExtends;
//import com.sumavision.tvpay.connector.util.CommonUtil;
//import com.sumavision.tvpay.connector.util.Constant;
//import com.sumavision.tvpay.connector.util.MD5;
//import com.sumavision.tvpay.connector.util.QueryStatementUtil;
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
//			logger.info("获取对账单，平台传入参数为：" + param);
//        Map<String,Object> returnMap = new HashMap<String,Object>();
//        List<BankRecord> recordList = new ArrayList<BankRecord>();
//        StringBuffer sb = new StringBuffer();
//		StatementRequest request = new StatementRequest();
//		//密钥
//		String md5key = ppc.getMessage("ucfpay.bank.privateKey");
//		//字符集
//		String charset = ppc.getMessage("ucfpay.bank.charset");
//		Date statementFrom = null;
//		String startTime = null;
//        try{
//	        sb.append(charset);
//	        //签名类型
//	        String signType = ppc.getMessage("ucfpay.bank.signType");
//	        sb.append(signType);
//	        //商户号
//	        String customerNo = ppc.getMessage("ucfpay.bank.merchantNo");
//	        sb.append(customerNo);
//	        //付款请求号
//	        String requestNo = UUID.randomUUID().toString().replaceAll("-", "");
//	        sb.append(requestNo);
//	        //对账单类型
//	        String statementType = ppc.getMessage("ucfpay.bank.statementType");
//	        sb.append(statementType);
//	        //起始日期
//	        statementFrom = (Date)(param.get(Constant.START_TIME));
//	        startTime = CommonUtil.sdf.format(statementFrom);
//	        GregorianCalendar cal = new GregorianCalendar();
//	        cal.setTime(statementFrom);
//	        XMLGregorianCalendar startDay = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
//	        sb.append(startTime);
//	        //结束时间
//	        Date statementTo = (Date)(param.get(Constant.END_TIME));
//	        String endTime = CommonUtil.sdf.format(statementTo);
//	        cal.setTime(statementTo);
//	        XMLGregorianCalendar endDay = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
//	        sb.append(endTime);
//			
//			//1、组织上送报文
//			logger.info("待签名字符串：" + sb.toString());
//			String sign = null;
//			if("MD5".equals(signType)){
//				String privateKey = ppc.getMessage("ucfpay.bank.privateKey");
//				String signMsg = MD5.sign(sb.toString(), privateKey, charset);
//				//转换成大写
//				sign = signMsg.toUpperCase();
//			}
//			request.setSign(sign);
//			request.setCharset(charset);
//			request.setCustomerNo(customerNo);
//			request.setRequestNo(requestNo);
//			request.setSignType(signType);
//			request.setStatementType(statementType);
//			request.setStatementFrom(startDay);
//			request.setStatementTo(endDay);
//			logger.info("查询对账单上送至银行报文: " + request);
//		}catch(Exception e){
//			logger.info("获取请求参数异常,原因:" + e);
//			throw new ConnectorException("310050505", "获取请求参数异常,原因:"+ e.getMessage(), e);
//		}
//		
//		//2、与银行通信
//		StatementResponse response = null;
//		try{
//			response = QueryStatementUtil.doQueryStatement(request);
//			logger.info("API系统返回：" + response);
//		}catch(Exception e){
//			logger.info("获取请求参数异常,原因:" + e);
//			throw new ConnectorException("310050505", "与银行连接异常,原因:"+ e.getMessage(), e);
//		}
//		//3、解析从银行返回的报文信息
//		try{
//			String resultCode = response.getResultCode();
//			String resultMessage = response.getResultMessage();
//			if(!"00".equals(resultCode)){
//				logger.info("对账单查询操作失败");
//				throw new ConnectorException("310050505", "对账单获取失败",resultMessage);
//			}
//			//签名信息
//			String respSign = response.getSign();
//			String respSignType = response.getSignType();
//			if(!"MD5".equals(respSignType) || respSign == null){
//				logger.info("返回信息签名方式有误或签名为空");
//				throw new ConnectorException("310050505", "返回信息签名方式有误或签名为空","返回信息签名方式有误或签名为空");
//			}
//			//验签字段
//			StringBuffer resp = new StringBuffer();
//			String respResquestNo = response.getRequestNo();
//			resp.append(respResquestNo);
//			resp.append(resultCode);
//			resp.append(resultMessage);
//			//验证数据签名
//			boolean verifySign = MD5.verify(resp.toString(), respSign, md5key, charset);
//			logger.info("B2C交易查询操作返回信息校验结果validate="+verifySign);
//			
//			if (!verifySign) {
//				logger.info("查询对账单操作，返回信息验证签名失败");
//				throw new ConnectorException("310050505", "返回信息验证签名失败","返回信息验证签名失败");
//			}
//			
//			List<StatementItem> statemtItems = response.getStatemtItems();
//			for (StatementItem statementItem : statemtItems) {
//				//账单时间
//				String period = statementItem.getPeriod();
//				//账单下载地址
//				String downloadAddress = statementItem.getDownloadAddress();
//				URL url = new URL(downloadAddress);
//				InputStream is = url.openConnection().getInputStream();
//				byte[] array = new byte[is.available()];
//				is.read(array);
//				is.close();
//				String localPath = ppc.getMessage("ucfpay.sumapay.checkbillsPath");
//				File file = new File(localPath + period + ".rar");
//				FileOutputStream fosr = new FileOutputStream(file);
//		        fosr.write(array);
//		        fosr.close();
//		        
//		        //解析压缩包
//		        File newFile = new File(localPath + period);
//		        CommonUtil.unrar(file, newFile);
//		        
//		        //对账单文件名
//		        String checkbillsName = ppc.getMessage("ucfpay.bank.checkbillsName");
//		        File dzdFile = new File(newFile, "01网关产品-单信息支付明细.csv");
//		        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dzdFile),"GBK"));
//            	String msg = br.readLine();
//            	while ((msg = br.readLine()) != null) {
//            		BankRecord bankRecord = new BankRecord();
//            		String[] r = new String[5];
//            		String[] linestr = msg.split(",");
//            		
//            		//交易类型
//    				String rzSum = linestr[4];
//    				if(!rzSum.startsWith("-")){
//    					//交易类型
//    					r[0] = "0";
//    					//订单号
//    					r[3] = linestr[8];
//    				}else{
//    					r[0] = "1";
//    					//原交易流水号
//    					r[3] = linestr[8];
//    				}
//            		//交易成功
//	            	r[1] = startTime;      //交易日期
//	            	r[2] = linestr[2];     //金额
//	            	r[4] = linestr[10];     //银行票号
//	            	bankRecord.setRecord(r);
//	    			bankRecord.setRawContent(msg);
//	    			bankRecord.setQueryDate(statementFrom);
//	    			recordList.add(bankRecord);
//            	}
//            	br.close();
//			}
//		}catch(Exception e){
//			logger.info("获取请求参数异常,原因:" + e);
//			throw new ConnectorException("310050505", "解析报文异常,原因:"+ e.getMessage(), e);
//		}
//        returnMap.put(Constant.ACTION_RESULT, 0);
//		returnMap.put(Constant.STATMENT, recordList);
//		logger.info("获取对账单返回交易总条数：" + recordList.size());
//		return returnMap;
//	}
//	
//	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
//		return ppc;
//	}
//
//	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
//		this.ppc = ppc;
//	}
//}