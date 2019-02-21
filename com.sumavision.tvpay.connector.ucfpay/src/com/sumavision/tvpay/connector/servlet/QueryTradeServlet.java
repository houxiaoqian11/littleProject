//package com.sumavision.tvpay.connector.servlet;
//
//import java.io.IOException;
//import java.util.Date;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.sdo.mas.common.api.common.entity.Extension;
//import com.sdo.mas.common.api.common.entity.Header;
//import com.sdo.mas.common.api.common.entity.Sender;
//import com.sdo.mas.common.api.common.entity.Service;
//import com.sdo.mas.common.api.common.entity.Signature;
//import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryRequest;
//import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryResponse;
//import com.sumavision.tvpay.commons.config.ConfigManager;
//import com.sumavision.tvpay.connector.HandlerAutowired;
//import com.sumavision.tvpay.connector.util.CommonUtil;
//import com.sumavision.tvpay.connector.util.MD5;
//import com.sumavision.tvpay.connector.util.QueryWSHelper;
//import com.sumavision.tvpay.connector.util.QueryWSProxy;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//
//public class QueryTradeServlet extends HttpServlet {
//
//	private static final long serialVersionUID = -1884866967588846292L;
//
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	@HandlerAutowired(springName = "propertyConfigurer")
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//
//	@Override
//	public void init() throws ServletException {
//		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ConfigManager
//				.getApplicationContext().getBean("propertyConfigurer");
//
//		super.init();
//	}
//
//	@Override
//	public void destroy() {
//		super.destroy();
//	}
//
//	@Override
//	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
//			throws ServletException, IOException {
//		doPost(arg0, arg1);
//	}
//
//	@Override
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String tradeId = request.getParameter("tradeId");
//		String result = queryTradeStatus(tradeId);
//		if ("0".equals(result)) {
//			response.setCharacterEncoding("GBK");
//			response.addHeader("HTTP/1.1 200", "OK");
//			response.addHeader("Server", "Apache/1.39");
//			response.addHeader("Content-type", "text/html");
//			response.getWriter().write("OK");
//			response.flushBuffer();
//		} else {
//			response.setCharacterEncoding("GBK");
//			response.addHeader("HTTP/1.1 500", "Error");
//			response.addHeader("Server", "Apache/1.39");
//			response.addHeader("Content-type", "text/html");
//			response.getWriter().write("ERROR:" + result);
//			response.flushBuffer();
//		}
//
//	}
//
//	public String queryTradeStatus(String tradeId) {
//		
//		//版本名称
//		String serviceCode = ppc.getMessage("ucfpay.bank.query.serviceCode");
//		//版本号
//		String version = ppc.getMessage("ucfpay.bank.query.version");
//		//字符集
//		String charset = ppc.getMessage("ucfpay.bank.charset");
//		//发送方标识
//		String merchantNo = ppc.getMessage("ucfpay.bank.merchantNo");
//		String senderId = merchantNo;
//		//发送支付请求时间
//		String sendTime = CommonUtil.sdf4.format(new Date());
//		//签名类型
//		String signType = ppc.getMessage("ucfpay.bank.signType");
//	
//		//密钥
//		String md5key = ppc.getMessage("ucfpay.bank.privateKey");
//		OrderQueryRequest request = new OrderQueryRequest();
//		
//		Header head = new Header();
//		Extension ext = new Extension();
//		Signature sign = new Signature();
//		Sender sender = new Sender();
//		Service service = new Service();
//		//1、组织上送报文
//		try{
//			//组织head参数
//			head.setCharset(charset);
//			head.setSendTime(sendTime);
////			head.setTraceNo(traceNo);//文档中没有traceNo的值
//			head.setSender(sender);
//			head.setService(service);
//			//组织sender中的参数
//			head.getSender().setSenderId(senderId);
//			//组织service中的参数
//			head.getService().setServiceCode(serviceCode);
//			head.getService().setVersion(version);
//			
//			//组织extension参数
//			request.setExtension(ext);
//			request.setHeader(head);
//			request.setSignature(sign);
//			request.getSignature().setSignType(signType);
//			//组织request参数
//			request.setMerchantNo(merchantNo);
//			request.setOrderNo(tradeId);
////			request.setTransNo(transNo);//文档中不是必填参数
//			//数据签名
//			QueryWSHelper.signOrderQueryRequest(request, md5key,signType);
//			logger.info("查询上送至银行报文: " + request);
//			QueryWSProxy proxy = new QueryWSProxy();		
//			OrderQueryResponse response = proxy.doQueryOrder(request);
//	        logger.info("API系统返回：" + response);
//	        
//	        Header respHeader = response.getHeader();
//			Extension respExtension = response.getExtension();
//			Signature respSignature = response.getSignature();
//			
//			//签名信息
//	    	String respSign = respSignature.getSignMsg();
//	    	String respSignType = respSignature.getSignType();
//			if(!"MD5".equals(respSignType) || respSign == null){
//				logger.info("B2C同步通知，返回信息签名方式有误或签名为空");
//				return request.toString();
//			}
//			
//			String respOrderNo = response.getOrderNo();
//			if(respOrderNo == null){
//				logger.info("查询订单号有误，请确认");
//				return request.toString();
//			}
//			
//			if(!respOrderNo.equals(tradeId)){
//				logger.info("查询订单号与返回订单号不符");
//				return request.toString();
//			}
//			//验签字段
//			StringBuffer resp = new StringBuffer();
//			String respServiceCode = respHeader.getService().getServiceCode();
//			if(!"".equals(respServiceCode) && respServiceCode != null){
//				resp.append(respServiceCode);
//			}
//			String respVersion = respHeader.getService().getVersion();
//			if(!"".equals(respVersion) && respVersion != null){
//				resp.append("|" + respVersion);
//			}
//			String respCharset = respHeader.getCharset();
//			if(!"".equals(respCharset) && respCharset != null){
//				resp.append("|" + respCharset);
//			}
//			String respTraceNo = respHeader.getTraceNo();
//			if(!"".equals(respTraceNo) && respTraceNo != null){
//				resp.append("|" + respTraceNo);
//			}
//			String respSenderId = respHeader.getSender().getSenderId();
//			if(!"".equals(respSenderId) && respSenderId != null){
//				resp.append("|" + respSenderId);
//			}
//			String respSendTime = respHeader.getSendTime();
//			if(!"".equals(respSendTime) && respSendTime != null){
//				resp.append("|" + respSendTime);
//			}
//			if(!"".equals(respOrderNo) && respOrderNo != null){
//				resp.append("|" + respOrderNo);
//			}
//			String respOrderAmount = response.getOrderAmount();
//			if(!"".equals(respOrderAmount) && respOrderAmount != null){
//				resp.append("|" + respOrderAmount);
//			}
//			String respTransNo = response.getTransNo();
//			if(!"".equals(respTransNo) && respTransNo != null){
//				resp.append("|" + respTransNo);
//			}
//			String respTransAmount = response.getTransAmoumt();
//			if(!"".equals(respTransAmount) && respTransAmount != null){
//				resp.append("|" + respTransAmount);
//			}
//			String respTransStatus = response.getTransStatus();
//			if(!"".equals(respTransStatus) && respTransStatus != null){
//				resp.append("|" + respTransStatus);
//			}
//			String respTransTime = response.getTransTime();
//			if(!"".equals(respTransTime) && respTransTime != null){
//				resp.append("|" + respTransTime);
//			}
//			String respExt1 = respExtension.getExt1();
//			if(!"".equals(respExt1) && respExt1 != null){
//				resp.append("|" + respExt1);
//			}
//			if(!"".equals(respSignType) && respSignType != null){
//				resp.append("|" + respSignType + "|");
//			}
//			
//			//验证数据签名
//			boolean verifySign = MD5.verify(resp.toString(), respSign, md5key, charset);
//			logger.info("B2C交易查询操作返回信息校验结果validate="+verifySign);
//				
//			if (!verifySign) {
//				logger.info("交易查询操作，返回信息验证签名失败");
//				return request.toString();
//			}
//			
//			logger.info("查询交易--->成功");
//			logger.info("(00：等待付款中；01：付款成功；02：付款失败;03:过期;04:撤销成功;05:退款中;06:退款成功;07:退款失败;08:部分退款成功)   ,原交易银行返回=" + respTransStatus);
//			if(!"01".equals(respTransStatus)){
//				logger.info("被查询交易状态为失败");
//				return request.toString();
//			}	
//		} catch(Exception e){
//			logger.error("B2C业务查询,原tradeId=" + tradeId + "，组织查询信息错误" + e );
//			return request.toString();
//		}
//		
//		return "OK";
//	}
//}
