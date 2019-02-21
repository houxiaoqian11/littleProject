package com.sumavision.tvpay.connector.handler.ucfpay.plateform;

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
import com.sumavision.tvpay.exception.business.ConnectorException;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

@HandlerExtends(method = "PLA-CON-004", code = 3L, validate = "off", description = "查询网银支付/充值结果")
public class QueryWebBankPayResultHandler implements Handler {

	// log
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String method;

	private long code;

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	

	public void setCode(long code) {
		this.code = code;
	}

	public long getCode() {
		return this.code;
	}

	@Override
	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("B2C查询支付结果，平台传入参数为：" + params);
		Map<String, Object> returnMap = new HashMap<String, Object>();

		try {
			// 参数校验
			ParamValidator.queryBankResult(params);
			// 业务处理
			returnMap = businessProcess(params);
		} catch (Exception e) {
			logger.error("查询交易订单出现异常", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
		}
		logger.info("B2C查询支付结果，返回平台的结果为：" + returnMap);
		return returnMap;
	}

	/**
	  业务处理
	*/
	private Map<String, Object> businessProcess(Map<String, Object> params) {

		Map<String, Object> returnMap = null;
		// B2B标志
		int b2bFlag = (Integer) params.get(Constant.B2B_FLG); 
		
		if (1 == b2bFlag) {// b2b
			returnMap = B2BProcess(params);
		} else {// b2c
			returnMap = allB2CProcess(params);
		}
		return returnMap;
	}

	/**
	 * 业务处理子方法1-b2c借记卡+贷记卡
	 */
	private Map<String, Object> allB2CProcess(Map<String, Object> params ) {
		return pubProcess(params);
	}

	/**
	 * 业务处理子方法4-b2b
	 *  
	 */
	private Map<String, Object> B2BProcess(Map<String, Object> params)  {
		throw new  ConnectorException("暂不支持此功能");
	}

	/**
	 * 
	 */
	private Map<String, Object> pubProcess(Map<String, Object> param ) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 请求参数
		HashMap<String, String> reqParams = new HashMap<String, String>();
		// 商户密钥
		String merchantKey = ppc.getMessage("ucfpay.key");
		// 商户号
		String merchantId = (String) param.get(Constant.BANK_MERCHANT_ID);
		returnMap.put("BANK_MERCHANT_ID", merchantId);
		
		try {
			// 接口名称
			String service = ppc.getMessage("ucfpay.query.service.name");
			reqParams.put("service", service);
			// 签名算法
			String secId = ppc.getMessage("ucfpay.sign.method");
			reqParams.put("secId", secId);
			// 接口版本
			String version = ppc.getMessage("ucfpay.version");
			reqParams.put("version", version);
			reqParams.put("merchantId", merchantId);
			// 订单号
			String merchantNo = (String) param.get(Constant.TRADE_ID);
			reqParams.put("merchantNo", merchantNo);
			// 序列号
			String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId, service, merchantNo);
			reqParams.put("reqSn", reqSn);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", reqParams, "RSA");
			reqParams.put("sign", signValue);

		} catch (Exception e) {
			logger.info("本次快捷支付查询组织报文出现异常,", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付查询接口组织报文异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		String respStr = null;
		try {
			String url = ppc.getMessage("ucfpay.bank.cashier.url");
			logger.info("快捷查询发送往银行参数为：" + reqParams);
			respStr = HttpUtil.send(reqParams, returnMap, url);
		} catch (Exception e) {
			logger.info("快捷支付查询与银行异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付查询接口与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
		}

		try {
			// 解密银行返回
			String decryptData = AESCoder.decrypt(respStr, merchantKey);
			logger.info("快捷查询银行返回参数为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			String errorCode = json.getString("errorCode");
			String errorMessage = json.getString("errorMessage");
			// 获取交易状态
			String status = json.getString("status");
			// 渠道商户号
			if ("S".equals(status)) {
				// 成功
				returnMap.put(Constant.ACTION_RESULT, 0);
			} else if ("F".equals(status)) {
				// 交易失败
				String errCode = "";
				try {
					errCode = ppc.getMessage(errorCode);
				} catch (Exception e) {
					errCode = "310040004";
				}
				returnMap.put(Constant.ACTION_RESULT, 1);
				returnMap.put(Constant.ERROR_CODE, errCode);
				returnMap.put(Constant.ERROR_MESSAGE, errorMessage);
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
			} else {
				
				// 交易待确认
				if("10009".equals(errorCode)){
					returnMap.put(Constant.ACTION_RESULT, 1);
				}else{
					returnMap.put(Constant.ACTION_RESULT, 2);
				}
				// 交易失败
				String errCode = "";
				try {
					errCode = ppc.getMessage(errorCode);
				} catch (Exception e) {
					errCode = "310040004";
				}
				returnMap.put(Constant.ERROR_CODE, errCode);
				returnMap.put(Constant.ERROR_MESSAGE, errorMessage);
				returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage(errCode));
			}
		} catch (Exception e) {
			logger.info("快捷查询解析银行返回结果出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷查询解析银行返回结果出现异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}
		return returnMap;		
		
	}

	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		this.ppc = ppc;
	}

	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
		return ppc;
	}
}
