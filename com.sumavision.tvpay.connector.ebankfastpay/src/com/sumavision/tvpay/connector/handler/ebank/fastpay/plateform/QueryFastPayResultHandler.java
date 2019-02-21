//package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;
//
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
//import com.sumavision.tvpay.connector.vo.Body;
//import com.sumavision.tvpay.connector.vo.Head;
//import com.sumavision.tvpay.connector.vo.Ipay;
//import com.sumavision.tvpay.connector.vo.Merchant;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.CompactWriter;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//
////@UnSupportedConnectorHanler()
//@HandlerExtends(
//		method="PLA-CON-018",
//		code=17L,
//		validate="off",
//		description="查询快捷支付/充值结果")
//public class QueryFastPayResultHandler  implements Handler{
//
//	//log
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//	
//	private String method;
//	
//	private long code;
//	
//	@HandlerAutowired(springName = "propertyConfigurer")
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//	
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
//	 * <p>设置Handler代码，表示当前连接器处理服务编码，用于判断连接器是否支持某些功能（如：支持卡通支付，支持网银充值）</p>
//	 *
//	 * @param code
//	 */
//	public void setCode(long code){
//		this.code = code;
//	}
//	
//	/**
//	 * <p>获取Handler代码</p>
//	 *
//	 * @return
//	 */
//	public long getCode(){
//		return this.code;
//	}
//	
//	@Override
//	public Map<String, Object> process(Map<String, Object> param) {
//		logger.info("快钱快捷查询平台传入参数为：" + param);
//		Map<String,Object> returnMap  = businessProcess(param);
//		logger.info("快钱快捷查询返回平台参数为：" + returnMap);
//		return returnMap;
//	}
//	/**
//	 * 业务处理
//	 * */
//	private Map<String, Object> businessProcess(Map<String, Object> param) {
//		String tradeId = String.valueOf(param.get(Constant.TRADE_ID)); // 订单号
//		
//		XStream xstream = new XStream(new  DomDriver( ));
//		xstream.processAnnotations(new Class[] { Merchant.class ,Ipay.class});
//		
//		// 返回值初始化
//		Map<String,Object> returnMap = new HashMap<String,Object>();
//		HashMap<String,String > requestMap = new HashMap<String, String>();
//		
//		returnMap.put("ACTION_RESULT", 2); // 默认为"待确认"
//		returnMap.put(Constant.TRADE_ID, tradeId);
//		// 调接口查询
//		try {
//			String fastPayInterfaceVersion = ppc.getMessage("fastPayInterfaceVersion");
//			String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
//			String msgType = "01";
//			String tranCode = ppc.getMessage("fastPayQueryCode");
//			String reqMsgId = UUID.randomUUID().toString().replaceAll("-", "");
//			String reqDate = CommonUtil.sdf4.format(new Date());
//			
//			Head head = new Head();
//			head.setVersion(fastPayInterfaceVersion);
//			head.setMerchantId(fastPayMerchantId);
//			head.setMsgType(msgType);
//			head.setTranCode(tranCode);
//			head.setReqMsgId(reqMsgId);
//			head.setReqDate(reqDate);
//			Body body = new Body();
//			body.setOriReqMsgId(tradeId);
//			Merchant merchant = new Merchant();
//			merchant.setHead(head);
//			merchant.setBody(body);
//			Writer writer = new StringWriter();
//	        xstream.marshal(merchant, new CompactWriter(writer));
//	        String reqXml = Constant.XML_HEADER +  writer.toString();
//			System.out.println(reqXml);
//			logger.info("民生鉴权上送报文体为："+ reqXml );
//			requestMap = CommonUtil.processRequsetMap(reqXml, tranCode, ppc);
//			
//		} catch (Exception e) {
//			logger.info("本次快捷支付查询组织报文出现异常,", e);
//			return returnMap;
//		}
//		
//		try{
//			String shareFilePath = ppc.getMessage("shareFilePath");
//			shareFilePath = shareFilePath.endsWith("/")?shareFilePath : shareFilePath + "/";
//			String timeWaitForResult = ppc.getMessage("timeWaitForResult");
//			String timeWaitForReReadtFile = ppc.getMessage("timeWaitForReReadtFile");
//			String url = ppc.getMessage("fastPayQueryUrl");
//			url = url.endsWith("/")? url : (url + "/");
//			int connTimeOut = Integer.parseInt(ppc.getMessage("connTimeOut"));
//			int readTimeout = Integer.parseInt(ppc.getMessage("readTimeout"));
//			
//			//发送请求
//			int status = HttpUtil.sendHttpWithoutResponse(url, requestMap, readTimeout, connTimeOut, Constant.FAST_PAY_ENCODING);
//			logger.info("交易查询请求银行完毕", + status);
//			Thread.sleep(Integer.parseInt(timeWaitForResult));//等待结果
//			//结果
//			String key = "query_" + tradeId;
//			String result = CacheService.INSTANCE.get(key);
//			if(result == null ){
//				try {
//					result = CommonUtil.readFileContent(shareFilePath,key);
//					logger.info("查询结果result= " + result);
//				} catch (FileNotFoundException e) {
//					Thread.currentThread();
//					Thread.sleep(Integer.parseInt(timeWaitForReReadtFile));
//					try {
//						result = CommonUtil.readFileContent(shareFilePath, key);
//					} catch (Exception ex) {
//						logger.info("获取支付结果异常，", e);
//						return returnMap;
//					}
//				}
//			}
//			if (result.indexOf("###") > -1) {
//				returnMap.put(Constant.ACTION_RESULT, 0);
//				returnMap.put(Constant.BANK_TRADE_ID,  result.split(",")[1]);
//				returnMap.put(Constant.SUM, CommonUtil.fen2YuanAsBigdecimal(result.split(",")[2]));
//			}
//		}catch (Exception e) {
//			logger.info("获取支付结果异常，", e);
//		}
//		return returnMap;
//	}
//	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
//		return ppc;
//	}
//
//	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
//		this.ppc = ppc;
//	}
//}
