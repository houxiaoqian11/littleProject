package com.sumavision.tvpay.connector.handler.ucfpay.plateform;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.ParamValidator;
import com.sumavision.tvpay.exception.business.ConnectorException;
import com.sumavision.tvpay.front.domain.entity.TradeRecord;
import com.sumavision.tvpay.front.transaction.TransactionControllerRemote;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.util.AESCoder;

@HandlerExtends(method = "PLA-CON-002", code = 1L, validate = "off", description = "解析网银支付/充值同步返回结果")
public class WebBankTradeSyncResultParseHandler implements Handler {

	// log
	static final Logger logger = LoggerFactory.getLogger(WebBankTradeSyncResultParseHandler.class);

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

	@HandlerAutowired(springName = "TransactionSessionBean")
	private TransactionControllerRemote transactionSessionBean;

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getCode() {
		return this.code;
	}
	
	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
		return ppc;
	}

	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		this.ppc = ppc;
	}

	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("解析同步通知，平台传入参数为：" + params);
		Map<String, Object> returnMap = null;
		// 参数校验
		ParamValidator.synchronizeNotice(params);
		// 业务处理
		returnMap = businessProcess(params);
		logger.info("解析同步通知，返回平台的结果为：" + returnMap);
		return returnMap;
	}

	/**
	 * 业务处理
	 * */
	@SuppressWarnings("unused")
	private Map<String, Object> businessProcess(Map<String, Object> params) {

		Map<String, Object> returnMap = null;

		String bankCardType = String.valueOf(params.get(Constant.BANK_CARD_TYPE)); // 借贷分离标志
		String b2bFlag = String.valueOf(params.get(Constant.B2B_FLAG)); // B2B标志

		if ("1".equals(b2bFlag)) {// b2b
			logger.info("B2B网银支付!");
			returnMap = B2BProcess(params );
		} else {// b2c
			returnMap = allB2CProcess(params);
		}
		return returnMap;
	}
	/**
	 * 业务处理子方法1-b2c借记卡+贷记卡
	 * */
	private Map<String, Object> allB2CProcess(Map<String, Object> params ) {
		return pubB2CProcess(params );
	}
	/**
	 * 业务处理子方法4-b2b
	 * */
	private Map<String, Object> B2BProcess(Map<String, Object> params ) {
		throw new  ConnectorException("暂不支持此功能");
	}
		
	@SuppressWarnings("unchecked")
	private Map<String, Object> pubB2CProcess(Map<String, Object> params ) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			Map<String, String> bankParam =  (Map<String, String>) params.get(Constant.BANK_RESPONSE_PARAMS);
			logger.info("bankParam="  +   bankParam);
			String encryptData = bankParam.get("data");
			//商户密钥
			String merchantKey = ppc.getMessage("ucfpay.fastpay.key");
			//解密
			String decryptData = AESCoder.decrypt(encryptData, merchantKey);
			logger.info("解密后报文为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			//订单状态
			String status = json.getString("status");
			
			if("S".equals(status)){
				logger.info("B2C网银同步通知返回成功");
				returnMap.put(Constant.ACTION_RESULT, 0);// 成功
				//商户订单号
				String merchantNo = json.getString("merchantNo");
				//银行订单号
				String tradeNo = json.getString("tradeNo");
				//金额
				BigDecimal sum = CommonUtil.fen2YuanAsBigdecimal(json.getString("amount"));
				returnMap.put(Constant.TRADE_ID, merchantNo);
				returnMap.put(Constant.BANK_TRADE_ID, tradeNo);
				returnMap.put(Constant.SUM, sum);
			}else{
				logger.info("B2C网银同步通知，银行返回待确认");
				returnMap.put(Constant.ACTION_RESULT, 2);
				returnMap.put(Constant.ERROR_CODE, "310040404");
				returnMap.put(Constant.ERROR_MESSAGE, "网银支付失败");
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, "网银支付失败");
			}
		}catch (Exception e) {
			ConnectorException connException = new ConnectorException("310040404", "B2C网银同步解析异常!", e);
			connException.printStackTrace();
			throw connException;
		}
		return returnMap;
	}

}
