package com.sumavision.tvpay.connector.servlet;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.tvpay.connector.dispatch.BankConnector;
import com.sumavision.tvpay.connector.util.CacheService;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.FastPayCryptionUtil;
import com.sumavision.tvpay.connector.vo.Ipay;
import com.sumavision.tvpay.connector.vo.Merchant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * <p>接收鉴权签约结果</p>
 * @author Administrator
 */
public class FastPayNoticeServlet extends HttpServlet{
	
	private static final long serialVersionUID = -1884866967588846292L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BankConnector connectorService;
	ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	Map<String, String> tradeType = new HashMap<String, String>();
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		connectorService = (BankConnector) ctx.getBean("bankConnector");
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ctx.getBean("propertyConfigurer");
		tradeType.put("IFP001", "鉴权");
		tradeType.put("IFP004", "支付");
		tradeType.put("IFP005", "退款");
		tradeType.put("IFP006", "查询");
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doPost(arg0, arg1);
	}

	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("厦门民生快捷异步开始");
		try {
			String tranCode = request.getParameter(Constant.TRANCODE);
			logger.info("接收到参数：tranCode=[" +  tranCode + "]，类型为" + tradeType.get(tranCode));
			String encryptData = request.getParameter(Constant.ENCRYPTDATA);
			String encryptKey = request.getParameter(Constant.ENCRYPTKEY);
			String signData = request.getParameter(Constant.SIGNDATA);
			
			XStream xstream = new XStream(new  DomDriver( ));
			xstream.processAnnotations(new Class[] { Merchant.class ,Ipay.class});
			PrivateKey privateKey = FastPayCryptionUtil.getPrivateKey(ppc.getMessage("fastPayPrivateKey"), null, "RSA");
			PublicKey publicKey = FastPayCryptionUtil.getPublicKey(ppc.getMessage("fastPayPublicKey"),"RSA");
			
			byte [] aesKey = FastPayCryptionUtil.RSADecrypt(Base64.decodeBase64(encryptKey.getBytes(Constant.FAST_PAY_ENCODING)),privateKey);
			logger.info("对端AES秘钥为:" + new String(aesKey));
			byte[] xmlBytes = FastPayCryptionUtil.AESDecrypt(Base64.decodeBase64(encryptData.getBytes(Constant.FAST_PAY_ENCODING)), aesKey);
			byte[] signBytes = Base64.decodeBase64(signData.getBytes(Constant.FAST_PAY_ENCODING));
			boolean signFlag = FastPayCryptionUtil.verifyDigitalSign(xmlBytes, signBytes, publicKey);
			String xml = new String(xmlBytes,Constant.FAST_PAY_ENCODING);
			logger.info("解密后xml报文：" + xml);
			String fastPaySignCode = ppc.getMessage("fastPaySignCode");
			String fastPayCode = ppc.getMessage("fastPayCode");
			String fastPayRefundCode = ppc.getMessage("fastPayRefundCode");
			String fastPayQueryCode = ppc.getMessage("fastPayQueryCode");
			Ipay ipay = (Ipay)xstream.fromXML(xml);
			
			if(fastPaySignCode.equalsIgnoreCase(tranCode)){
				processSign(signFlag,ipay);			
			}else if(fastPayCode.equalsIgnoreCase(tranCode)){
				boolean result = processPay(signFlag,ipay);
				if(result)
					sendNotice2Plateform(ipay ,response);
			}else if(fastPayRefundCode.equalsIgnoreCase(tranCode)){
				processRefunfd(signFlag,ipay);
			}else if(fastPayQueryCode.equalsIgnoreCase(tranCode)){
				processQuery(signFlag,ipay);
			}else{
				logger.info("无此交易代码!");
			}
		
		} catch (Exception e) {
			logger.info("处理民生快捷异步出现异常! ",e);
		}

	}
	private void  processSign(boolean signFlag, Ipay ipay){
		try {
			String successType = ppc.getMessage("successType");
			String successCode = ppc.getMessage("successCode");
			String respType = ipay.getHead().getRespType();
			String respCode = ipay.getHead().getRespCode();
			String respMsg = ipay.getHead().getRespMsg();
			String userId = ipay.getBody().getUserId();
			String bankCardNo = ipay.getBody().getBankCardNo();
			String fileName = userId + bankCardNo;//文件名使用UserId + 卡号   userId 使用请求流水号截断30位，保证唯一
			String result = "";
			if(successType.equals(respType) && successCode.equals(respCode)){
				String bindId = ipay.getBody().getBindId();
				result = "###" + "," + bindId;
				logger.info("签约成功!");
			}else{
				logger.error("签约返回状态不是成功!");
				result = "&&&" + "," + respCode + "," + respMsg;
			}
			CacheService.INSTANCE.put(fileName, result);
			
			String needsTowriteFile = ppc.getMessage("needsTowriteFile");//是否写结果文件
			if("1".equals(needsTowriteFile))
				writeResult2File(fileName, result);
		} catch (Exception e) {
			logger.error("处理签约请求出现异常：" , e);
		}
		
	}

	private boolean processPay(boolean signFlag, Ipay ipay) {
		boolean status = false;
		try{
			String successType = ppc.getMessage("successType");
			String successCode = ppc.getMessage("successCode");
			String respType = ipay.getHead().getRespType();
			String respCode = ipay.getHead().getRespCode();
			String respMsg = ipay.getHead().getRespMsg();
			String reqMsgId = ipay.getHead().getReqMsgId();
			String payMsgId = ipay.getHead().getPayMsgId();
			String fileName = "pay_" + reqMsgId;//文件名使用 pay_+ 请求流水号
			String result = "";
			if(successType.equals(respType) && successCode.equals(respCode)){
				String amount = ipay.getBody().getAmount();
				result = "###" + "," + payMsgId + "," + amount;
				status = true;
				logger.info("支付成功!");
			}else{
				logger.error("支付返回状态不是成功!");
				result = "&&&" + "," + respCode + "," + respMsg;
			}
			CacheService.INSTANCE.put(fileName, result);
			
			String needsTowriteFile = ppc.getMessage("needsTowriteFile");//是否写结果文件
			if("1".equals(needsTowriteFile))
				writeResult2File(fileName, result);
		} catch (Exception e) {
			logger.error("处理支付请求出现异常：" , e);
		}
		return status;
	}
	private void processQuery(boolean signFlag, Ipay ipay) {
		try{
			String successType = ppc.getMessage("successType");
			String successCode = ppc.getMessage("successCode");
			String respType = ipay.getHead().getRespType();
			String respCode = ipay.getHead().getRespCode();
			String respMsg = ipay.getHead().getRespMsg();
			String oriReqMsgId = ipay.getBody().getOriReqMsgId();
			String payMsgId = ipay.getBody().getOriPayMsgId();
			String fileName = "query_" + oriReqMsgId;//文件名使用 pay_+ 请求流水号
			String result = "";
			if(successType.equals(respType) && successCode.equals(respCode)){
				String oriRespCode = ipay.getBody().getOriRespCode();
				String oriAmount = ipay.getBody().getOriAmount();
				String oriRespMsg = ipay.getBody().getOriRespMsg();
				if(successCode.equals(oriRespCode)){
					result = "###" + "," + payMsgId + "," + oriAmount;
					logger.info("查询成功!");
				}else{
					logger.error("交易状态不是成功!");
					result = "&&&" + "," + oriRespCode + "," + oriRespMsg;
				}
			}else{
				logger.error("请求状态不是成功!");
				result = "&&&" + "," + respCode + "," + respMsg;
			}
			CacheService.INSTANCE.put(fileName, result);
			
			String needsTowriteFile = ppc.getMessage("needsTowriteFile");//是否写结果文件
			if("1".equals(needsTowriteFile))
				writeResult2File(fileName, result);
		} catch (Exception e) {
			logger.error("处理查询请求出现异常：" , e);
		}
		
	}
	private void processRefunfd(boolean signFlag, Ipay ipay) {
		try{
			String successType = ppc.getMessage("successType");
			String successCode = ppc.getMessage("successCode");
			String respType = ipay.getHead().getRespType();
			String respCode = ipay.getHead().getRespCode();
			String respMsg = ipay.getHead().getRespMsg();
			String reqMsgId = ipay.getHead().getReqMsgId();
			String fileName = "refund_" + reqMsgId;//文件名使用 refund_+ 请求流水号
			String result = "";
			if(successType.equals(respType) && successCode.equals(respCode)){
				result = "###" + "," + respCode ;
				logger.info("退款成功!");
			}else{
				logger.error("退款返回状态不是成功!");
				result = "&&&" + "," + respCode + "," + respMsg;
			}
			CacheService.INSTANCE.put(fileName, result);
			
			String needsTowriteFile = ppc.getMessage("needsTowriteFile");//是否写结果文件
			if("1".equals(needsTowriteFile))
				writeResult2File(fileName, result);
			
		} catch (Exception e) {
			logger.error("处理退款请求出现异常：" , e);
		}
		
	}
	
	/**
	 * 将同步请求结果写入文件
	 * @param fileName
	 * @param result
	 */
	public void writeResult2File(String fileName, String result) {
		String synchronizedModel = ppc.getMessage("synchronizedModel");
		if("1".equals(synchronizedModel))
			CommonUtil.processSmbFile(fileName, result ,ppc);
		else
			CommonUtil.processLocalFile(fileName, result ,ppc);
	}
	

private void sendNotice2Plateform(Ipay ipay, HttpServletResponse response ){
	String reqMsgId = ipay.getHead().getReqMsgId();
	String payMsgId = ipay.getHead().getPayMsgId();
	String amount = ipay.getBody().getAmount();
	Map<String,Object> respMap = new HashMap<String,Object>();
	respMap.put(Constant.TRADE_ID, reqMsgId);
	respMap.put(Constant.BANK_TRADE_ID, payMsgId);
	respMap.put(Constant.SUM, amount);
	
	Map<String,Object> input = new HashMap<String,Object>();
	input.put("METHOD","BANK-CON-001");
	input.put("BANK_CODE","ebankfastpay");
	input.put("BANK_RESPONSE_PARAMS", respMap);
	try{
		Map<String,Object> output = connectorService.process(input);
		logger.info(" 银行发送的异步结果通知 处理结果 : ["+output+"]");
		if(output!= null && (Integer)output.get(Constant.ACTION_RESULT)==0 && output.containsKey("RETURN_MSG")){
			//需要返回银行处理通知消息
			String responseEncoding = (String)output.get("RESPONSE_ENCODING");
			String responseMsg = (String)output.get("RETURN_MSG");
			
			if(StringUtils.isBlank(responseEncoding)){
				logger.info("返回银行确认消息 编码格式 为空，使用默认的GBK格式");
				responseEncoding = "GBK";
			}
			logger.info("返回银行确认消息 编码格式->"+responseEncoding);
			logger.info("返回银行确认消息->"+responseMsg);
			logger.info("准备返回银行确认消息");
			response.setCharacterEncoding(responseEncoding);
			response.addHeader("HTTP/1.1 200", "OK");
			response.addHeader("Server", "Apache/1.39");
			response.addHeader("Content-Length", String.valueOf(responseMsg.length()));
			response.addHeader("Content-type", "text/html");
			response.getWriter().write(responseMsg);
			response.flushBuffer();
		}else{
			logger.info("不需要返回银行确认消息");
		}
	}catch(Exception e){
		logger.error("处理 银行发送的异步结果通知 异常, 原因 :" ,e);
	}
	
}



}
