//package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;
//
//import java.io.FileNotFoundException;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
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
//@HandlerExtends(method = "PLA-CON-021", code = 20L, validate = "off", description = "退款")
//public class RefundHandler implements Handler {
//
//	// log
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	private String method;
//
//	private long code;
//
//	@HandlerAutowired(springName = "propertyConfigurer")
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//
//	//
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
//	@Override
//	public Map<String, Object> process(Map<String, Object> param) {
//		logger.info("厦门民生退款，平台传入参数为：" + param);
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//
//		try {
//			// 参数校验
//			ParamValidator.refoundCheck(param);
//			// 业务处理
//			returnMap = fastPayBusinessProcess(param);
//			
//		} catch (Exception e) {
//			returnMap.put(Constant.ACTION_RESULT, 1);
//			returnMap.put(Constant.ERROR_CODE, "310030001");
//			returnMap.put(Constant.ERROR_MESSAGE, "厦门民生退款异常:" + e.getMessage());
//			returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310030001"));
//		}
//
//		logger.info("厦门民生退款，返回平台的结果为：" + returnMap);
//		return returnMap;
//	}
//
//
//	/**
//	 * 快捷退款
//	 * @param param
//	 * @return
//	 */
//	private Map<String, Object> fastPayBusinessProcess(Map<String, Object> param) {
//		BigDecimal txAmount = (BigDecimal) param.get(Constant.SUM);
//		String tradeId = (String) param.get(Constant.PAY_TRADE_ID);
//		String orgBankTradeID = (String) param.get(Constant.ORIGIN_BANK_TRADE_ID);
//		XStream xstream = new XStream(new  DomDriver( ));
//		xstream.processAnnotations(new Class[] { Merchant.class ,Ipay.class});
//		
//
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		HashMap<String,String > requestMap = new HashMap<String, String>();
//		returnMap.put(Constant.ACTION_RESULT, 1);
//		// 1、组织上送报文
//		try {
//			String fastPayInterfaceVersion = ppc.getMessage("fastPayInterfaceVersion");
//			String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
//		
//			String msgType = "01";
//			String tranCode = ppc.getMessage("fastPayRefundCode");
//			String refundReason = ppc.getMessage("refundReason");
//			String reqDate = CommonUtil.sdf4.format(new Date());
//			
//			Head head = new Head();
//			head.setVersion(fastPayInterfaceVersion);
//			head.setMerchantId(fastPayMerchantId);
//			head.setMsgType(msgType);
//			head.setTranCode(tranCode);
//			head.setReqMsgId(tradeId);
//			head.setReqDate(reqDate);
//			Body body = new Body();
//			body.setCurrency("156");
//			body.setAmount(CommonUtil.yuan2fenAsString(txAmount));
//			body.setRefundReason(refundReason);
//			body.setOriPayMsgId(orgBankTradeID);
//			Merchant merchant = new Merchant();
//			merchant.setHead(head);
//			merchant.setBody(body);
//			Writer writer = new StringWriter();
//	        xstream.marshal(merchant, new CompactWriter(writer));
//	        String reqXml = Constant.XML_HEADER +  writer.toString();
//			System.out.println(reqXml);
//			logger.info("厦门民生快捷退款上送报文体为："+ reqXml );
//			requestMap = CommonUtil.processRequsetMap(reqXml, tranCode, ppc);
//		} catch (Exception e) {
//			logger.error("订单号:" + tradeId + ",厦门民生快捷退款,组织上送报文异常:", e);
//			returnMap.put(Constant.ERROR_CODE, "310030001");
//			returnMap.put(Constant.ERROR_MESSAGE, "厦门民生快捷退款,组织上送报文异常" + e);
//			returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310030001"));
//			return returnMap;
//		}
//		// 2、与银行通信
//		// 3、解析银行返回报文
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
//			int status = HttpUtil.sendHttpWithoutResponse(url, requestMap, readTimeout, connTimeOut, Constant.FAST_PAY_ENCODING);
//			logger.info("退款请求银行完毕 " + status);
//			Thread.sleep(Integer.parseInt(timeWaitForResult));//等待结果
//			
//			//处理结果
//			String key = "refund_" + tradeId ;
//			String result = CacheService.INSTANCE.get(key);
//			if(result == null ){
//				try {
//					result = CommonUtil.readFileContent(shareFilePath,key);
//				} catch (FileNotFoundException e) {
//					Thread.currentThread();
//					Thread.sleep(Integer.parseInt(timeWaitForReReadtFile));
//					try {
//						result = CommonUtil.readFileContent(shareFilePath, key);
//						logger.info("退款结果result= " + result);
//					} catch (Exception ex) {
//						logger.info("获取退款结果异常，", e);
//						returnMap.put(Constant.ERROR_CODE, "310020001");
//						returnMap.put(Constant.ERROR_MESSAGE, "获取退款结果异常" + e.getMessage());
//						returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310020001"));
//						return returnMap;
//					}
//				}
//			}
//			if (result.indexOf("###") > -1) {
//				returnMap.put(Constant.ACTION_RESULT, 0);
//			} else {
//				String respCode = result.split(",")[1];
//				String respMsg = result.split(",")[2];
//				String errCode = "";
//				try {
//					errCode = ppc.getMessage(respCode);
//				} catch (Exception e) {
//					errCode = "310020001";
//				}
//				returnMap.put(Constant.ACTION_RESULT, 1);
//				returnMap.put(Constant.ERROR_CODE, errCode);
//				returnMap.put(Constant.ERROR_MESSAGE,respMsg);
//				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
//			}
//
//		} catch (Exception e) {
//			logger.error("订单号:" + tradeId + ",厦门民生快捷退款,解析银行返回报文异常", e);
//			returnMap.put(Constant.ERROR_CODE, "310020001");
//			returnMap.put(Constant.ERROR_MESSAGE, "厦门民生快捷退款交易解析银行返回报文异常:" + e.getMessage());
//			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310020001"));
//		}
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
