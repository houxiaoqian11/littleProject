package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.HttpUtil;
import com.sumavision.tvpay.connector.util.ParamValidator;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

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
			logger.info("快捷支付校验出现异常，", e.getMessage());
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
		// 请求参数
		HashMap<String, String> reqParams = new HashMap<String, String>();
		// 商户密钥
		String merchantKey = ppc.getMessage("ucfpay.fastpay.key");
		// 商户号
		String merchantId = (String) param.get(Constant.BANK_MERCHANT_ID);
		returnMap.put("BANK_MERCHANT_ID", merchantId);
		try {
			// 参与加密的参数
			HashMap<String, String> bizParams = new HashMap<String, String>();
			// 接口名称
			String service = ppc.getMessage("ucfpay.fastpay.confirm.service.name");
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
			// 短信验证码
			String checkCode = (String) param.get(Constant.DYN_VERIFY_CODE);
			bizParams.put("checkCode", checkCode);
			String bizData = JSON.toJSONString(bizParams);
			String encryptData = "";
			encryptData = AESCoder.encrypt(bizData, merchantKey);
			reqParams.put("data", encryptData);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", reqParams, "RSA");
			reqParams.put("sign", signValue);
		} catch (Exception e) {
			logger.info("快捷支付组织报文出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付接口组织上送报文异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		String respStr = null;
		try {
			String url = ppc.getMessage("ucfpay.fastpay.url");
			logger.info("发送往银行参数为：" + reqParams);
			respStr = HttpUtil.send(reqParams, returnMap, url);

		} catch (Exception e) {
			logger.info("快捷支付与银行异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付接口与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
		}

		try {
			// 解密银行返回
			String decryptData = AESCoder.decrypt(respStr, merchantKey);
			logger.info("快捷查询银行返回参数为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			String resCode = json.getString("resCode");
			String resMessage = json.getString("resMessage");
			// 获取交易状态
			String status = json.getString("status");
			if ("S".equals(status)) {
				// 成功
				returnMap.put(Constant.ACTION_RESULT, Constant.SUCCEED);
			}else if("F".equals(status)){
				// 交易失败
				String errCode = "";
				try {
					errCode = ppc.getMessage(resCode);
				} catch (Exception e) {
					errCode = "310040004";
				}
				returnMap.put(Constant.ACTION_RESULT, 1);
				returnMap.put(Constant.ERROR_CODE, "310040004");
				returnMap.put(Constant.ERROR_MESSAGE, resMessage);
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
			}else{
				// 交易待确认
				returnMap.put(Constant.ACTION_RESULT, 2);
			}
		} catch (Exception e) {
			logger.info("解析银行返回结果出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
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
