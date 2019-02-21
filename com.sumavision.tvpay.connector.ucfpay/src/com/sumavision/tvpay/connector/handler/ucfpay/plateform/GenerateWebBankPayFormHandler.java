package com.sumavision.tvpay.connector.handler.ucfpay.plateform;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.ParamValidator;
import com.sumavision.tvpay.connector.util.RedisLock;
import com.sumavision.tvpay.connector.utils.Constants;
import com.sumavision.tvpay.exception.business.ConnectorException;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

@HandlerExtends(method = "PLA-CON-001", code = 0L, validate = "off", description = "为支付平台生成网银支付提交表单")
public class GenerateWebBankPayFormHandler implements Handler {

	private String method;

	private long code;

	// log
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	@HandlerAutowired(springName = "redisLock")
	private RedisLock redisLock;
	
	@HandlerAutowired(springName = "redisTemplate")
	private RedisTemplate redisTemplate;

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

	public RedisLock getRedisLock() {
		return redisLock;
	}

	public void setRedisLock(RedisLock redisLock) {
		this.redisLock = redisLock;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
		return ppc;
	}

	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		this.ppc = ppc;
	}

	/*
	 * METHOD String Y BANK_CODE String Y ID_NUMBER String Y
	 */
	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("生成支付表单，平台输入参数：" + params);
		Map<String, Object> returnMap = null;
		// 参数校验
		ParamValidator.webBankPay(params);
		// 业务处理
		returnMap = businessProcess(params);
		logger.info("生成支付表单，返回平台的结果：" + returnMap);
		return returnMap;
	}

	/**
	 * 业务处理
	 */
	public Map<String, Object> businessProcess(Map<String, Object> params) {

		Map<String, Object> returnMap = null;

		String bankCardType = String.valueOf(params.get(Constant.BANK_CARD_TYPE)); // 借贷分离标志
		String b2bFlag = String.valueOf(params.get(Constant.B2B_FLAG)); // B2B标志

		if ("1".equals(b2bFlag)) {// b2b
			logger.info("B2B网银支付!");
			returnMap = B2BProcess(params);
		} else {// b2c
			if ("1".equals(bankCardType)) {
				logger.info("B2C网银支付仅借记卡网银支付!");
				returnMap = allB2CProcess(params, "1");
			} else {// 借记卡+信用卡
				// logger.info("B2C网银支付，目前只支持借记卡,暂不支持混合渠道!");
				logger.info("B2C网银支付混合渠道网银支付!");
				returnMap = allB2CProcess(params, "1");
				// throw new ConnectorException("B2C网银支付，目前只支持借记卡,暂不支持混合渠道");
			}
		}
		return returnMap;
	}

	/**
	 * 业务处理子方法1-b2c借记卡+贷记卡
	 */
	private Map<String, Object> allB2CProcess(Map<String, Object> params, String type) {
		return pubProcess(params, type);
	}

	/**
	 * 业务处理子方法4-b2b
	 */
	private Map<String, Object> B2BProcess(Map<String, Object> params) {
		throw new ConnectorException("暂不支持此功能");
	}

	/**
	 * 
	 * */
	private Map<String, Object> pubProcess(Map<String, Object> params, String type) {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 提交至银行参数
		Map<String, String> formParams = new HashMap<String, String>();
		// 商户密钥
		String merchantKey = ppc.getMessage("ucfpay.key");
		// 商户号
		String merchantId = "M200006113";
		returnMap.put("BANK_MERCHANT_ID", merchantId);
		
		// 商户号
		try{
			CommonUtil.getMerchantId(redisLock, redisTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			// 参与加密的参数
			HashMap<String, String> bizParams = new HashMap<String, String>();
			// 接口名称
			String service = ppc.getMessage("ucfpay.cashier.service.name");
			formParams.put("service", service);
			// 签名算法
			String secId = ppc.getMessage("ucfpay.sign.method");
			formParams.put("secId", secId);
			// 接口版本
			String version = ppc.getMessage("ucfpay.version");
			formParams.put("version", version);

			formParams.put("merchantId", merchantId);
			// 订单号
			String merchantNo = (String) params.get(Constant.TRADE_ID);
			bizParams.put("merchantNo", merchantNo);
			// 序列号
			String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId, service, merchantNo);
			formParams.put("reqSn", reqSn);
			System.out.println(reqSn);

			// 来源(固定值1 表示PC)
			String source = "1";
			bizParams.put("source", source);
			// 产品类型(W:网银)
			String productType = "W";
			bizParams.put("productType", productType);
			// 金额
			String amount = CommonUtil.yuan2fenAsString((BigDecimal) params.get(Constant.SUM));
			bizParams.put("amount", amount);
			// 币种
			String transCur = ppc.getMessage("ucfpay.transCur");
			bizParams.put("transCur", transCur);
			// 借贷分离标志
			String bankCardType = String.valueOf(params.get(Constant.BANK_CARD_TYPE));
			if ("1".equals(bankCardType)) {
				// 借记卡
				String accountType = "1";
				bizParams.put("accountType", accountType);
			} else if ("2".equals(bankCardType)) {
				// 贷记卡
				String accountType = "2";
				bizParams.put("accountType", accountType);
			}
			// 商品名称
			String productName = (String) params.get(Constant.PAY_DESC);
			bizParams.put("productName", productName);
			// 前台通知地址
			String returnUrl = ppc.getMessage("ucfpay.sumapay.pageUrl");
			bizParams.put("returnUrl", returnUrl);
			// 后台通知地址
			String noticeUrl = ppc.getMessage("ucfpay.sumapay.notifyUrl");
			bizParams.put("noticeUrl", noticeUrl);

			String bizData = JSON.toJSONString(bizParams);
			String encryptData = "";
			encryptData = AESCoder.encrypt(bizData, merchantKey);
			formParams.put("data", encryptData);
			System.out.println(encryptData);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", formParams, "RSA");
			formParams.put("sign", signValue);
			System.out.println(signValue);

			returnMap.put(Constants.BANK_PAY_FORM_STR, "");
			returnMap.put("SUBMIT_URL", ppc.getMessage("ucfpay.bank.cashier.url"));
			returnMap.put("PARAMS", formParams);
			returnMap.put(Constant.ACTION_RESULT, 0);
			returnMap.put("BANK_ENCODE", "UTF-8");
		} catch (Exception e) {
			logger.info("支付表单生成异常", e);
			ConnectorException connException = new ConnectorException("310010101", "B2C支付接口组织上送报文异常!", e);
			connException.printStackTrace();
			throw connException;
		}
		return returnMap;
	}
}
