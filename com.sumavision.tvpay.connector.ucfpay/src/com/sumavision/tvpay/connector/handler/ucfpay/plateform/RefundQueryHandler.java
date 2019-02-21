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
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

@HandlerExtends(method = "PLA-CON-121", code = 120L, validate = "off", description = "退款交易查询")
public class RefundQueryHandler implements Handler {

	// log
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

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

	@Override
	public Map<String, Object> process(Map<String, Object> param) {
		logger.info("网银退款查询平台传入参数为：" + param);
		Map<String, Object> returnMap = businessProcess(param);
		logger.info("网银退款查询返回平台参数为：" + returnMap);
		return returnMap;
	}

	/**
	 * 业务处理
	 * */
	private Map<String, Object> businessProcess(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 请求参数
		HashMap<String, String> reqParams = new HashMap<String, String>();
		// 商户密钥
		String merchantKey = ppc.getMessage("ucfpay.key");
		// 商户号
		String merchantId = (String) param.get(Constant.BANK_MERCHANT_ID);
		returnMap.put("BANK_MERCHANT_ID", merchantId);
		try {
			// 参与加密的参数
		    HashMap<String, String> bizParams = new HashMap<String, String>();
			// 接口名称
			String service = ppc.getMessage("ucfpay.refund.query.service.name");
			reqParams.put("service", service);
			// 签名算法
			String secId = ppc.getMessage("ucfpay.sign.method");
			reqParams.put("secId", secId);
			// 接口版本
			String version = ppc.getMessage("ucfpay.version");
			reqParams.put("version", version);
			reqParams.put("merchantId", merchantId);
			// 订单号
			String merchantNo = (String) param.get(Constant.PAY_TRADE_ID);
			bizParams.put("merchantNo", merchantNo);
			// 序列号
			String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId, service, merchantNo);
			reqParams.put("reqSn", reqSn);
			String bizData = JSON.toJSONString(bizParams);
			String encryptData = "";
			encryptData = AESCoder.encrypt(bizData, merchantKey);
			reqParams.put("data", encryptData);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", reqParams, "RSA");
			reqParams.put("sign", signValue);

		} catch (Exception e) {
			logger.info("本次网银退款查询组织报文出现异常,", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "网银退款查询接口与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}

		String respStr = null;
		try {
			String url = ppc.getMessage("ucfpay.bank.cashier.url");
			logger.info("网银退款查询发送往银行参数为：" + reqParams);
			respStr = HttpUtil.send(reqParams, returnMap, url);
		} catch (Exception e) {
			logger.info("网银退款查询与银行异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "网银退款查询接口与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
		}

		try {
			// 解密银行返回
			String decryptData = AESCoder.decrypt(respStr, merchantKey);
			logger.info("网银退款查询银行返回参数为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			String resCode = json.getString("resCode");
			//处理状态
			String result = json.getString("result");
			String resMessage = json.getString("resMessage");
			if("S".equals(result)){
				//退款成功
				returnMap.put(Constant.ACTION_RESULT, 0);
			}else if("F".equals(result)){
				//退款失败
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
			}else{
				//待确认
				returnMap.put(Constant.ACTION_RESULT, 2);
			}
		} catch (Exception e) {
			logger.info("网银查询解析银行返回结果出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "网银查询解析银行返回结果出现异常");
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
