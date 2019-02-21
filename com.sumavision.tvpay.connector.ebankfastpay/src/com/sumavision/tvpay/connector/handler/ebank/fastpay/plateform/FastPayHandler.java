package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.CacheService;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.HttpUtil;
import com.sumavision.tvpay.connector.util.ParamValidator;
import com.sumavision.tvpay.connector.vo.Body;
import com.sumavision.tvpay.connector.vo.Head;
import com.sumavision.tvpay.connector.vo.Ipay;
import com.sumavision.tvpay.connector.vo.Merchant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

@HandlerExtends(method = "PLA-CON-017", code = 16L, validate = "off", description = "快捷支付")
public class FastPayHandler implements Handler {

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}

	/**
	 * <p>
	 * 设置Handler代码，表示当前连接器处理服务编码，用于判断连接器是否支持某些功能（如：支持卡通支付，支持网银充值）
	 * </p>
	 * 
	 * @param code
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * <p>
	 * 获取Handler代码
	 * </p>
	 * 
	 * @return
	 */
	public long getCode() {
		return this.code;
	}

	/**
	 * 商户支付交易
	 */
	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("快捷平台传入参数为：" + params);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 参数校验
		try {
			ParamValidator.fastPayCheck(params);
			// 业务处理
			resultMap = businessProcess(params);

		} catch (Exception e) {
			logger.info("厦门民生快捷支付校验出现异常，", e.getMessage());
			resultMap.put(Constant.ACTION_RESULT, 1);
			resultMap.put(Constant.ERROR_CODE, "310030001");
			resultMap.put(Constant.ERROR_MESSAGE, e.getMessage());
			resultMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310030001"));
			return resultMap;
		}

		logger.info("返回平台参数为：" + resultMap);
		return resultMap;
	}

	/**
	 * 业务处理
	 * */
	private Map<String, Object> businessProcess(Map<String, Object> param) {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String,String > requestMap = new HashMap<String, String>();
		XStream xstream = new XStream(new  DomDriver( ));
		xstream.processAnnotations(new Class[] { Merchant.class ,Ipay.class});
		String txAmount = CommonUtil.yuan2fenAsString((BigDecimal) param.get(Constant.SUM));
		String tradeId = (String) param.get(Constant.TRADE_ID);
		String bindId = null;
		returnMap.put(Constant.TRADE_ID, tradeId);
		try {
			bindId = CacheService.INSTANCE.get("bind_" + tradeId);

			if ( StringUtils.isEmpty(bindId)) {
				try {
					String localFilePath = ppc.getMessage("localFilePath");
					localFilePath = localFilePath.endsWith("/")?localFilePath : localFilePath + File.separator ;
					bindId = CommonUtil.readFileContent(localFilePath, "bind_" + tradeId);
				} catch (Exception e) {
					logger.info("厦门民生快捷支付请求异常，", e);
					returnMap.put(Constant.ACTION_RESULT, 1);
					returnMap.put(Constant.ERROR_CODE, "310012122");
					returnMap.put(Constant.ERROR_MESSAGE, "获取BindId异常");
					returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
					return returnMap;
				}
			}
			String fastPayInterfaceVersion = ppc.getMessage("fastPayInterfaceVersion");
			String fastPayMerchantId = ppc.getMessage("fastPayMerchantId");
			String productCategory = ppc.getMessage("productCategory");
			String productName = ppc.getMessage("productName");
			String msgType = "01";
			String tranCode = ppc.getMessage("fastPayCode");
			String reqDate = CommonUtil.sdf4.format(new Date());
			Head head = new Head();
			head.setVersion(fastPayInterfaceVersion);
			head.setMerchantId(fastPayMerchantId);
			head.setMsgType(msgType);
			head.setTranCode(tranCode);
			head.setReqMsgId(tradeId);
			head.setReqDate(reqDate);
			Body body = new Body();
			body.setUserId(tradeId);
			body.setBindId(bindId);
			body.setCurrency("156");
			body.setAmount(txAmount);
			body.setProductCategory(productCategory);
			body.setProductName(productName);
			body.setProductDesc("");
			
			Merchant merchant = new Merchant();
			merchant.setHead(head);
			merchant.setBody(body);
			Writer writer = new StringWriter();
	        xstream.marshal(merchant, new CompactWriter(writer));
	        String reqXml = Constant.XML_HEADER +  writer.toString();
			System.out.println(reqXml);
			logger.info("民生快捷支付请求上送报文体为："+ reqXml );
			requestMap = CommonUtil.processRequsetMap(reqXml, tranCode, ppc);
		} catch (Exception e) {
			logger.info("厦门民生快捷支付组织报文出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付接口组织上送报文异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		try {
			String shareFilePath = ppc.getMessage("shareFilePath");
			shareFilePath = shareFilePath.endsWith("/")?shareFilePath : shareFilePath + "/";
			String timeWaitForResult = ppc.getMessage("timeWaitForResult");
			String timeWaitForReReadtFile = ppc.getMessage("timeWaitForReReadtFile");
			String url = ppc.getMessage("fastPayTradeUrl");
			url = url.endsWith("/")? url : (url + "/");
			int connTimeOut = Integer.parseInt(ppc.getMessage("connTimeOut"));
			int readTimeout = Integer.parseInt(ppc.getMessage("readTimeout"));
			
			//发送请求
			int status = HttpUtil.sendHttpWithoutResponse(url, requestMap, readTimeout, connTimeOut, Constant.FAST_PAY_ENCODING);
			logger.info("支付请求银行完毕 ", + status);
			Thread.sleep(Integer.parseInt(timeWaitForResult));//等待结果
			//处理结果
			String key = "pay_" + tradeId;
			String result = CacheService.INSTANCE.get(key);
			
			if(result == null ){
				try {
					result = CommonUtil.readFileContent(shareFilePath,key);
				} catch (FileNotFoundException e) {
					Thread.currentThread();
					Thread.sleep(Integer.parseInt(timeWaitForReReadtFile));
					try {
						result = CommonUtil.readFileContent(shareFilePath, key);
						logger.info("支付结果result= " + result);
					} catch (Exception ex) {
						logger.info("获取支付结果异常，", e);
						returnMap.put(Constant.ACTION_RESULT, 1);
						returnMap.put(Constant.ERROR_CODE, "310012122");
						returnMap.put(Constant.ERROR_MESSAGE, "获取支付结果异常" + ex.getMessage());
						returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
						return returnMap;
					}
				}
			}
			if (result.indexOf("###") > -1) {
				returnMap.put(Constant.ACTION_RESULT, 0);
				returnMap.put(Constant.BANK_TRADE_ID,  result.split(",")[1]);
				returnMap.put(Constant.SUM, CommonUtil.fen2YuanAsBigdecimal(result.split(",")[1]));
			} else {
				String respCode = result.split(",")[1];
				String respMsg = result.split(",")[2];
				String errCode = "";
				try {
					errCode = ppc.getMessage(respCode);
				} catch (Exception e) {
					errCode = "310040004";
				}
				returnMap.put(Constant.ACTION_RESULT, 1);
				returnMap.put(Constant.ERROR_CODE, errCode);
				returnMap.put(Constant.ERROR_MESSAGE,respMsg);
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
			}

		} catch (Exception e) {
			logger.info("本次快捷支付请求异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付接口与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
		}

		return returnMap;
	}


	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
		return ppc;
	}

	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		this.ppc = ppc;
	}

}
