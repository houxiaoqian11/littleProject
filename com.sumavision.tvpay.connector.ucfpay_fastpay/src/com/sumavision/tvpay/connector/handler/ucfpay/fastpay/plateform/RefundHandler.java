package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.math.BigDecimal;
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
import com.sumavision.tvpay.connector.util.HttpUtil;
import com.sumavision.tvpay.connector.util.ParamValidator;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

@HandlerExtends(method = "PLA-CON-021", code = 20L, validate = "off", description = "退款")
public class RefundHandler implements Handler {

	// log
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

	//
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
		logger.info("快捷退款，平台传入参数为：" + param);
		Map<String, Object> returnMap = new HashMap<String, Object>();

		try {
			// 参数校验
			ParamValidator.refoundCheck(param);
			// 业务处理
			returnMap = fastPayBusinessProcess(param);
			
		} catch (Exception e) {
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310030001");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷退款异常:" + e.getMessage());
			returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310030001"));
		}

		logger.info("快捷退款，返回平台的结果为：" + returnMap);
		return returnMap;
	}


	/**
	 * 快捷退款
	 * @param param
	 * @return
	 */
	private Map<String, Object> fastPayBusinessProcess(Map<String, Object> param) {
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
			String service = ppc.getMessage("ucfpay.fastpay.refund.service.name");
			reqParams.put("service", service);
			// 签名算法
			String secId = ppc.getMessage("ucfpay.fastpay.sign.method");
			reqParams.put("secId", secId);
			// 接口版本
			String version = ppc.getMessage("ucfpay.fastpay.version");
			reqParams.put("version", version);
			reqParams.put("merchantId", merchantId);
			// 订单号
			String merchantNo = (String) param.get(Constant.PAY_TRADE_ID);
			bizParams.put("merchantNo", merchantNo);
			// 序列号
			String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId, service, merchantNo);
			reqParams.put("reqSn", reqSn);
			
			//原支付单商户订单号
			String originMerchantNo = (String) param.get(Constant.ORIGIN_TRADE_ID);
			bizParams.put("originMerchantNo", originMerchantNo);
			//来源
			String source = "1";
			bizParams.put("source", source);
			//金额
			String amount = CommonUtil.yuan2fenAsString((BigDecimal) param.get(Constant.SUM));
			bizParams.put("amount", amount);
			// 币种
			String transCur = ppc.getMessage("ucfpay.fastpay.transCur");
			bizParams.put("transCur", transCur);
			//后台通知地址
			String noticeUrl = ppc.getMessage("ucfpay.fastpay.refund.noticeUrl");
			bizParams.put("noticeUrl", noticeUrl);
			//退款原因
			String refundReason = (String) param.get(Constant.REFUND_REAS);
			bizParams.put("refundReason", refundReason);
			String bizData = JSON.toJSONString(bizParams);
			String encryptData = "";
			encryptData = AESCoder.encrypt(bizData, merchantKey);
			reqParams.put("data", encryptData);
			// 签名-START
			String signValue = UcfForOnline.createSign(merchantKey, "sign", reqParams, "RSA");
			reqParams.put("sign", signValue);
		} catch (Exception e) {
			logger.error("快捷退款,组织上送报文异常:", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310030001");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷退款,组织上送报文异常" + e);
			returnMap.put(Constant.ERROR_SHOW_MESSAGE,ppc.getMessage("310030001"));
			return returnMap;
		}
		// 2、与银行通信
		String respStr = null;
		try {
			String url = ppc.getMessage("ucfpay.fastpay.url");
			logger.info("发送往银行参数为：" + reqParams);
			respStr = HttpUtil.send(reqParams, returnMap, url);

		} catch (Exception e) {
			logger.info("快捷支付退款与银行异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310012122");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付退款与银行通信异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
		}
		// 3、解析银行返回报文
		try {
			// 解密银行返回
			String decryptData = AESCoder.decrypt(respStr, merchantKey);
			logger.info("快捷退款银行返回参数为：" + decryptData);
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			String result = json.getString("result");
			String resMessage = json.getString("resMessage");
			String resCode = json.getString("resCode");
			
			if("S".equals(result)){
				//退款成功
				returnMap.put(Constant.ACTION_RESULT, Constant.SUCCEED);
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
			logger.error("快捷退款,解析银行返回报文异常", e);
			returnMap.put(Constant.ACTION_RESULT, 2);
			returnMap.put(Constant.ERROR_CODE, "310020001");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷退款交易解析银行返回报文异常:" + e.getMessage());
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310020001"));
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
