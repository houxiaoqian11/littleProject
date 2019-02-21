//package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;
//
//import java.io.FileNotFoundException;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.sumavision.tvpay.connector.Handler;
//import com.sumavision.tvpay.connector.HandlerAutowired;
//import com.sumavision.tvpay.connector.HandlerExtends;
//import com.sumavision.tvpay.connector.util.CacheService;
//import com.sumavision.tvpay.connector.util.CommonUtil;
//import com.sumavision.tvpay.connector.util.Constant;
//import com.sumavision.tvpay.connector.util.HttpUtil;
//import com.sumavision.tvpay.connector.util.ParamValidator;
//import com.sumavision.tvpay.connector.vo.Body;
//import com.sumavision.tvpay.connector.vo.Head;
//import com.sumavision.tvpay.connector.vo.Ipay;
//import com.sumavision.tvpay.connector.vo.Merchant;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.CompactWriter;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//
//@HandlerExtends(method = "PLA-CON-060", code = 59L, validate = "off", description = "代扣鉴权" )
//public class AuthenticationValidateHandler implements Handler {
//
//	private String method;
//
//	private long code;
//
//	@HandlerAutowired(springName = "propertyConfigurer")
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//
//
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	@Override
//	public void setMethod(String method) {
//		this.method = method;
//	}
//
//	@Override
//	public String getMethod() {
//		return method;
//	}
//
//	/**
//	 * <p>
//	 * 设置Handler代码，表示当前连接器处理服务编码，用于判断连接器是否支持某些功能（如：支持卡通支付，支持网银充值）
//	 * </p>
//	 * 
//	 * @param code
//	 */
//	public void setCode(long code) {
//		this.code = code;
//	}
//
//	/**
//	 * <p>
//	 * 获取Handler代码
//	 * </p>
//	 * 
//	 * @return
//	 */
//	public long getCode() {
//		return this.code;
//	}
//
//	/**
//	 * 商户支付交易
//	 */
//	public Map<String, Object> process(Map<String, Object> params) {
//		logger.info("民生快捷鉴权接口平台传入参数为：" + params);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		try {
//			ParamValidator.authenticationValidateCheck(params);
//		} catch (Exception e) {
//			logger.error("民生快捷参数校验出现异常，", e);
//			resultMap.put(Constant.ACTION_RESULT, 1);
//			resultMap.put(Constant.ERROR_CODE, "310012122");
//			resultMap.put(Constant.ERROR_MESSAGE, "参数校验出现异常");
//			resultMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
//			return resultMap;
//		}
//		resultMap = businessProcess(params);
//		logger.info("民生快捷鉴权接口返回平台参数为：" + resultMap);
//		return resultMap;
//	}
//
//	/**
//	 * 业务处理
//	 * */
//	private Map<String, Object> businessProcess(Map<String, Object> param) {
//		
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		String bankAccountName = (String) param.get(Constant.BANK_ACCOUNT_NAME); // 买方姓名
////		String certType = (String) param.get(Constant.ID_TYPE); // 证件类型 我方固定为0  渠道方固定为ZR01
//		String certNo = (String) param.get(Constant.ID_NUMBER); // 证件号码
//		String bankCardNo = (String) param.get(Constant.BANK_ACCOUNT); // 银行卡号
//		String cardType = (String) param.get(Constant.BANK_ACCOUNT_TYPE); // 银行卡类型
//		String mobilePhone = (String) param.get(Constant.MOBILE_PHONE);
//		String tradeId = (String) param.get(Constant.TRADE_ID);
//		String cvn2 = (String) param.get(Constant.CVN_CODE); // cvn2
//		String valD = (String) param.get(Constant.VALID_DATE); // 有效期
//		String valDate = "";
//		String fileName =bankAccountName + "_" + bankCardNo + "_" + certNo + "_" + mobilePhone;
//		if ("1".equals(cardType)){
//			String d = valD.substring(0, 2);
//			String d1 = valD.substring(2);
//			valDate = d1 + d;
//			fileName = fileName + "_" +  valDate + "_" + cvn2;
//		}
//		String md5FileName = CommonUtil.getMd5(fileName);
//		HashMap<String,String > requestMap = new HashMap<String, String>();
//		try {
//			String useFtpOrNot = ppc.getMessage("useFtpOrNot");
//			if("yes".equals(useFtpOrNot)){
//				returnMap =CommonUtil.getAuthResultFromFtpFile(tradeId,md5FileName, fileName, ppc);
//				if(returnMap.get(Constant.ACTION_RESULT) != null)
//					return returnMap;
//			}
//			
//			XStream xstream = new XStream(new  DomDriver( ));
//			xstream.processAnnotations(new Class[] { Merchant.class ,Ipay.class});
//			String reqMsgId = UUID.randomUUID().toString().replaceAll("-", "");//商户请求交易流水号，唯一标识一笔交易，必须保证永远唯一
//			
//			String fastPayInterfaceVersion = ppc.getMessage("fastPayInterfaceVersion");
//			String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
//			String msgType = "01";
//			String tranCode = ppc.getMessage("fastPaySignCode");
//			String reqDate = CommonUtil.sdf4.format(new Date());
//			Head head = new Head();
//			head.setVersion(fastPayInterfaceVersion);
//			head.setMerchantId(fastPayMerchantId);
//			head.setMsgType(msgType);
//			head.setTranCode(tranCode);
//			head.setReqMsgId(reqMsgId);
//			head.setReqDate(reqDate);
//			Body body = new Body();
//			body.setBankCardNo(bankCardNo);
//			body.setAccountName(bankAccountName);
//			body.setBankCardType(ppc.getMessage("debitCardType"));
//			body.setCertificateType(ppc.getMessage("certType"));
//			body.setCertificateNo(certNo);
//			body.setMobilePhone(mobilePhone);
//			if ("1".equals(cardType)) {
//				body.setBankCardType(ppc.getMessage("creditCardType"));
//				body.setValid(valDate);
//				body.setCvn2(cvn2);
//			}
//			body.setUserId(tradeId);
//			Merchant merchant = new Merchant();
//			merchant.setHead(head);
//			merchant.setBody(body);
//			Writer writer = new StringWriter();
//	        xstream.marshal(merchant, new CompactWriter(writer));
//	        String reqXml = Constant.XML_HEADER +  writer.toString();
//			System.out.println(reqXml);
//			logger.info("民生鉴权上送报文体为："+ reqXml );
//			requestMap = CommonUtil.processRequsetMap(reqXml, tranCode, ppc);
//			System.out.println(requestMap);
//			
//		} catch (Exception e) {
//			logger.info("民生鉴权组织报文出现异常，", e);
//			returnMap.put(Constant.ACTION_RESULT, 1);
//			returnMap.put(Constant.ERROR_CODE, "310012122");
//			returnMap.put(Constant.ERROR_MESSAGE, "身份校验组织报文异常");
//			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
//			return returnMap;
//		}
//		
//		try {
//			String shareFilePath = ppc.getMessage("shareFilePath");
//			shareFilePath = shareFilePath.endsWith("/")?shareFilePath : shareFilePath + "/";
//			String timeWaitForResult = ppc.getMessage("timeWaitForResult");
//			String timeWaitForReReadtFile = ppc.getMessage("timeWaitForReReadtFile");
//			String url = ppc.getMessage("fastPayTradeUrl");
//			url = url.endsWith("/")? url : (url + "/");
//			int connTimeOut = Integer.parseInt(ppc.getMessage("connTimeOut"));
//			int readTimeout = Integer.parseInt(ppc.getMessage("readTimeout"));
//			
//			//发送请求
//			
//			int status = HttpUtil.sendHttpWithoutResponse(url, requestMap, readTimeout, connTimeOut, Constant.FAST_PAY_ENCODING);
//			logger.info("鉴权请求银行完毕 ", + status);
//			Thread.sleep(Integer.parseInt(timeWaitForResult));//等待结果
//			//结果
//			String key = tradeId + bankCardNo;
//			String result = CacheService.INSTANCE.get(key);
//			
//			if(result == null ){
//				logger.info("读文件");
//				try {
//					result = CommonUtil.readFileContent(shareFilePath,key);
//				} catch (FileNotFoundException e) {
//					Thread.sleep(Integer.parseInt(timeWaitForReReadtFile));
//					try {
//						result = CommonUtil.readFileContent(shareFilePath, key);
//					} catch (Exception ex) {
//						logger.info("获取鉴权结果异常，", e);
//						returnMap.put(Constant.ACTION_RESULT, 1);
//						returnMap.put(Constant.ERROR_CODE, "310012122");
//						returnMap.put(Constant.ERROR_MESSAGE, "获取鉴权结果异常" + e.getMessage());
//						returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310012122"));
//						return returnMap;
//					}
//				}
//			}
//			
//			returnMap.put(Constant.ACTION_RESULT, 0);
//			if(result.indexOf("###") >-1){
//				returnMap.put(Constant.VALIDATE_RESULT, 0);
//				String bindId = result.split(",")[1];
//				CacheService.INSTANCE.put("bind_" + tradeId, bindId);
//				CommonUtil.processSmbFile("bind_" + tradeId, bindId ,ppc);
//				String useFtpOrNot = ppc.getMessage("useFtpOrNot");
//				if("yes".equals(useFtpOrNot)){
//					CommonUtil.uploadAuth2Ftp(fileName, md5FileName, bindId, ppc);
//				}
//			}else{
//				returnMap.put(Constant.VALIDATE_RESULT, 1);
//				returnMap.put(Constant.ERROR_CODE, "310040001");
//				returnMap.put(Constant.ERROR_MESSAGE, result.split(",")[2] );
//				returnMap.put(Constant.ERROR_SHOW_MESSAGE,
//						ppc.getMessage("310040001"));
//			}
//		} catch (Exception e) {
//			logger.info("本次代发代扣鉴权获取验证码请求异常，", e);
//			returnMap.put(Constant.ACTION_RESULT, 1);
//			returnMap.put(Constant.ERROR_CODE, "310012122");
//			returnMap.put(Constant.ERROR_MESSAGE, "鉴权解析返回报文现异常" + e.getMessage());
//			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
//			
//		}
//
//		return returnMap;
//	}
//
//
//	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
//		return ppc;
//	}
//
//	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
//		this.ppc = ppc;
//	}
//}
