package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.bank;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.exception.business.ConnectorException;
import com.sumavision.tvpay.front.transaction.TransactionControllerRemote;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

@HandlerExtends(
		method="BANK-CON-002",
		code=48L,
		validate="off",
		description="处理银行返回的网银支付结果通知信息")
public class RefundResultNoticeHandler implements Handler{

	//log
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String method;
	
	@HandlerAutowired(springName = "TransactionSessionBean")
	private TransactionControllerRemote transactionSessionBean;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	private long code ;
	
	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}
	
	/**
	 * <p>设置Handler代码，表示当前连接器处理服务编码，用于判断连接器是否支持某些功能（如：支持卡通支付，支持网银充值）</p>
	 *
	 * @param code
	 */
	public void setCode(long code){
		this.code = code;
	}
	
	/**
	 * <p>获取Handler代码</p>
	 *
	 * @return
	 */
	public long getCode(){
		return this.code;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("快捷退款的异步通知平台:"+params);
		Map bankParam = (Map) params.get(Constant.BANK_RESPONSE_PARAMS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constant.ACTION_RESULT, 2);// 默认为待确认
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		//订单状态
		String status = (String) bankParam.get("status");
		if("S".equals(status)){
			reqMap.put(Constant.ACTION_RESULT, 0);
		}else{
			reqMap.put(Constant.ACTION_RESULT, 1);
			//异常信息
			String resMessage = (String) bankParam.get("resMessage");
			reqMap.put("FAIL_REASON", resMessage);
		}
		//商户订单号
		String merchantNo = (String) bankParam.get("merchantNo");
		//银行流水号
		String tradeNo = (String) bankParam.get("tradeNo");
		
		reqMap.put(Constant.TRADE_ID, merchantNo);
		reqMap.put(Constant.TRADE_USER, ppc.getMessage(Constant.CONNECTOR_TRADE_USER));
		reqMap.put(Constant.TRADE_LOC, ppc.getMessage(Constant.CONNECTOR_TRADE_LOC));
		reqMap.put(Constant.CLIENT_ID, String.valueOf(new Date().getTime()));
		reqMap.put(Constant.TRADE_PROCESS, ppc.getMessage(Constant.CONNECTOR_TRADE_PROCESS));
		reqMap.put(Constant.TRADE_START_TIME, new Date());
		reqMap.put(Constant.PAY_BANK_SN, tradeNo);
		
		reqMap.put(Constant.TRADE_CODE, "TZ0006");	
			
		try {
			logger.info("平台处理异步通知，请求参数为         reqMap="+reqMap);
			Map<String, Object> respMap = this.transactionSessionBean.process(reqMap);
			logger.info("平台处理完毕，返回的Map为           respMap="+respMap);
			if ((Integer) respMap.get(Constant.ACTION_RESULT) != 0) {
				throw new ConnectorException((String) respMap.get(Constant.ERROR_MESSAGE));
			} else {
				logger.info("厦门民生快捷异步交易通知成功，平台处理正常");
				result.put("ACTION_RESULT", 0);// 成功
			}
		} catch (Exception e) {
			throw new ConnectorException("310040404","调用支付平台 支付异步确认接口异常!",e);
		}
		return result;
	}

}
