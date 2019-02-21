package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.HttpUtil;
import com.sumavision.tvpay.connector.util.UtilSign;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

@HandlerExtends(method = "PLA-CON-025", code = 24L, validate = "off", description = "快捷获取验证码")
public class GetFastTradeValidateCodeHandler implements Handler {

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
		logger.info("鉴权接口平台传入参数为：" + params);
		Map<String, Object> resultMap =  businessProcess(params);
		logger.info("鉴权接口返回平台参数为：" + resultMap);
		return resultMap;
	}

	/**
	 * 业务处理
	 * */
	private Map<String, Object> businessProcess(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String,String > reqParams = new HashMap<String, String>();
		try{
			//商户号
			String merchantId = "100000000002350";
			reqParams.put("merchantId", merchantId);
			//商户订单号
			String tradeId = (String) param.get(Constant.TRADE_ID);
			reqParams.put("tradeId", tradeId);
			//银行卡账号
			String cardNo = (String) param.get(Constant.BANK_ACCOUNT);
			reqParams.put("cardNo", cardNo);
			//持卡人姓名
			String cardName = (String) param.get(Constant.BANK_ACCOUNT_NAME);
			reqParams.put("cardName", cardName);
			//持卡人证件号码
			String idNo = (String) param.get(Constant.ID_NUMBER);
			reqParams.put("idNo", idNo);
			//持卡人手机号码
			String mobile = (String) param.get(Constant.MOBILE_PHONE);
			reqParams.put("mobile", mobile);
			//银行卡类型
			String accountType = (String) param.get(Constant.BANK_ACCOUNT_TYPE);
			reqParams.put("accountType", accountType);
			if("1".equals(accountType)){
				//信用卡背后三位安全码
				String cvv2 = (String) param.get(Constant.CVN_CODE);
				reqParams.put("cvv2", cvv2);
				//信用卡有效期
				String expired = (String) param.get(Constant.VALID_DATE);
				reqParams.put("expired", expired);
			}
			//签名方式
			String signType = "SHA";
			reqParams.put("signType", signType);
			//签名
			String sign = UtilSign.GetSHAstr(reqParams, "4b4aad5aaa15bfad568c68gg3gc850f6ed46740635715beb12d38ae4494d2e4e");
			reqParams.put("sign", sign);
		}catch(Exception e){
			logger.info("快捷支付获取验证码组织报文出现异常，", e);
			returnMap.put(Constant.ACTION_RESULT, 1);
			returnMap.put(Constant.ERROR_CODE, "310012121");
			returnMap.put(Constant.ERROR_MESSAGE, "快捷支付获取验证码组织报文出现异常");
			returnMap.put(Constant.ERROR_SHOW_MESSAGE, ppc.getMessage("310012122"));
			return returnMap;
		}
		
		try{
			String url = "https://ebank.ztpo.cn";
			logger.info("发送往银行参数为：" + reqParams);
			String requestForm = HttpUtil.RequestForm(url, reqParams);
			System.out.println(requestForm);
		}catch(Exception e){
			
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
