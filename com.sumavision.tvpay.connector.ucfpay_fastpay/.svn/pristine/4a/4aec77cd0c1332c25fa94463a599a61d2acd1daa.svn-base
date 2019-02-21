package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.HttpUtil;
import com.sumavision.tvpay.connector.util.RedisLock;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

@HandlerExtends(method = "PLA-CON-025", code = 24L, validate = "off", description = "快捷获取验证码")
public class GetFastTradeValidateCodeHandler implements Handler {

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	@HandlerAutowired(springName = "redisLock")
	private RedisLock redisLock;
	
	@HandlerAutowired(springName = "redisTemplate")
	private RedisTemplate redisTemplate;

	final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		logger.info("鉴权接口平台传入参数为：" + params);
		Map<String, Object> resultMap = businessProcess(params);
		logger.info("鉴权接口返回平台参数为：" + resultMap);
		return resultMap;
	}

	/**
	 * 业务处理
	 * */
	private Map<String, Object> businessProcess(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 请求参数
		HashMap<String, String> reqParams = new HashMap<String, String>();
		// 商户密钥
		String merchantKey = ppc.getMessage("ucfpay.fastpay.key");
		// 商户号
		try{
			CommonUtil.getMerchantId(redisLock, redisTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String merchantId = ppc.getMessage("merchantId");
		returnMap.put("BANK_MERCHANT_ID", merchantId);
		try {
			// 参与加密的参数
			HashMap<String, String> bizParams = new HashMap<String, String>();
			// 接口名称
			String service = ppc.getMessage("ucfpay.fastpay.apply.service.name");
			reqParams.put("service", service);
			// 签名算法
			String secId = ppc.getMessage("ucfpay.fastpay.sign.method");
			reqParams.put("secId", secId);
			// 接口版本
			String version = ppc.getMessage("ucfpay.fastpay.version");
			reqParams.put("version", version);
			
			reqParams.put("merchantId", merchantId);
			// 订单号
			String merchantNo = (String) param.get(Constant.TRADE_ID);
			bizParams.put("merchantNo", merchantNo);
			// 序列号
			String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId, service, merchantNo);
			reqParams.put("reqSn", reqSn);
			// 金额
			String amount = CommonUtil.yuan2fenAsString((BigDecimal) param.get(Constant.FUND));
			bizParams.put("amount", amount);
			// 币种
			String transCur = ppc.getMessage("ucfpay.fastpay.transCur");
			bizParams.put("transCur", transCur);
			// 证件类型
			String certificateType = ppc.getMessage("ucfpay.fastpay.certificateType");
			bizParams.put("certificateType", certificateType);
			// 证件号码
			String certificateNo = (String) param.get(Constant.ID_NUMBER);
			bizParams.put("certificateNo", certificateNo);
			// 银行卡号
			String accountNo = (String) param.get(Constant.BANK_ACCOUNT);
			bizParams.put("accountNo", accountNo);
			// 持卡人姓名
			String accountName = (String) param.get(Constant.BANK_ACCOUNT_NAME);
			bizParams.put("accountName", accountName);
			// 银行卡类型
			String accountType = (String) param.get(Constant.BANK_ACCOUNT_TYPE);
			if ("1".equals(accountType)) {
				// 信用卡背后三位安全码
				String cvn2 = (String) param.get(Constant.CVN_CODE);
				bizParams.put("cvn2", cvn2);
				// 信用卡有效期
				String validDate = (String) param.get(Constant.VALID_DATE);
				bizParams.put("validDate", validDate);
			}
			// 手机号
			String mobileNo = (String) param.get(Constant.MOBILE_PHONE);
			bizParams.put("mobileNo", mobileNo);
			// 商品名称
			String productName = (String) param.get(Constant.PRODUCT_NAME);
			bizParams.put("productName", productName);
			//后台通知地址
			String noticeUrl = ppc.getMessage("ucfpay.fastpay.noticeUrl");
			bizParams.put("noticeUrl", noticeUrl);
			String bizData = JSON.toJSONString(bizParams);
			String encryptData = "";
			encryptData = AESCoder.encrypt(bizData, merchantKey);
			reqParams.put("data", encryptData);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", reqParams, "RSA");
			reqParams.put("sign", signValue);
		} catch (Exception e) {
			logger.info("快捷支付获取验证码组织报文出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付获取验证码组织报文出现异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		String respStr = null;
		try {
			String url = ppc.getMessage("ucfpay.fastpay.url");
			logger.info("发送往银行参数为：" + reqParams);
			respStr = HttpUtil.send(reqParams, returnMap, url);
		} catch (Exception e) {
			logger.info("快捷支付获取验证码与银行通信出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付获取验证码与银行通信出现异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		try {
			// 解密银行返回
			String decryptData = AESCoder.decrypt(respStr, merchantKey);
			logger.info("快捷查询银行返回参数为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			String resCode = json.getString("resCode");
			if ("00000".equals(resCode)) {
				// 成功
				returnMap.put(Constant.ACTION_RESULT, Constant.SUCCEED);
			} else {
				String resMessage = json.getString("resMessage");
				String errCode = "";
				try {
					errCode = ppc.getMessage(resCode);
				} catch (Exception e) {
					errCode = "310040004";
				}
				returnMap.put(Constant.ACTION_RESULT, 1);
				returnMap.put(Constant.ERROR_CODE, errCode);
				returnMap.put(Constant.ERROR_MESSAGE, resMessage);
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
			}
		} catch (Exception e) {
			logger.info("解析银行返回结果出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "解析银行返回结果出现异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
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
