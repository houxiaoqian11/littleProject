package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.bank;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.exception.business.ConnectorException;
import com.sumavision.tvpay.front.domain.entity.TradeRecord;
import com.sumavision.tvpay.front.transaction.TransactionControllerRemote;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

@HandlerExtends(
		method="BANK-CON-001",
		code=48L,
		validate="off",
		description="处理银行返回的网银支付结果通知信息")
public class WebBankTradeResultNoticeHandler implements Handler{

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
		logger.info("快捷的异步通知平台:"+params);
		
		Map bankParam = (Map) params.get(Constant.BANK_RESPONSE_PARAMS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constant.ACTION_RESULT, 2);// 默认为待确认
		//订单状态
		String status = (String) bankParam.get("status");
		if(!"S".equals(status)){
			return result;
		}
		//商户订单号
		String merchantNo = (String) bankParam.get("merchantNo");
		//银行流水号
		String tradeNo = (String) bankParam.get("tradeNo");
		//交易金额
		BigDecimal sum = CommonUtil.fen2YuanAsBigdecimal((String)bankParam.get("amount"));
		String tradeCode = queryTradeInfo(merchantNo);
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put(Constant.TRADE_ID, merchantNo);
		reqMap.put(Constant.TRADE_USER, ppc.getMessage(Constant.CONNECTOR_TRADE_USER));
		reqMap.put(Constant.TRADE_LOC, ppc.getMessage(Constant.CONNECTOR_TRADE_LOC));
		reqMap.put(Constant.CLIENT_ID, String.valueOf(new Date().getTime()));
		reqMap.put(Constant.TRADE_PROCESS, ppc.getMessage(Constant.CONNECTOR_TRADE_PROCESS));
		reqMap.put(Constant.TRADE_START_TIME, new Date());
		reqMap.put(Constant.BANK_CODE, params.get("BANK_CODE"));
		reqMap.put(Constant.BANK_TRADE_ID, tradeNo);
		reqMap.put(Constant.ACTION_RESULT, 0);
		reqMap.put("SUM", sum);
			
		try {
			String plateformTradeInfo = ppc.getMessage(tradeCode);
			String plateformCode = plateformTradeInfo.split(",")[0];
			String plateformMsg = plateformTradeInfo.split(",")[1];
			logger.info("该交易类型代码为 :[" + tradeCode + "],交易类型为:[" + plateformMsg + "],调用平台处理器为:[" + plateformCode +"]!");
			reqMap.put(Constant.TRADE_CODE, plateformCode);
		} catch (Exception e) {
			logger.error("该交易交易类型[" + tradeCode + "]无法确认" , e);
			throw new ConnectorException("调用平台接口获取交易信息, 无法确认该交易交易类型TRADE_ID:" + tradeNo);
		}
			
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
	
	/**
	 * 调用平台查询交易类型
	 * @return trade_code
	 */
	public String queryTradeInfo(String tradeId) {
		String tradeCode = "";
		try {
			Map<String, Object> queryTradeInfo = new HashMap<String, Object>();
			String queryTradeInfoCode = ppc.getMessage(Constant.QUERY_TRADEINFO_TRADECODE);
			queryTradeInfo.put(Constant.TRADE_CODE, queryTradeInfoCode);
			queryTradeInfo.put(Constant.QUERY_TRADE_ID, tradeId);
			queryTradeInfo.put(Constant.CLIENT_ID,String.valueOf(new Date().getTime()) + tradeId);
			queryTradeInfo.put(Constant.TRADE_PROCESS,ppc.getMessage(Constant.CONNECTOR_TRADE_PROCESS));
			queryTradeInfo.put(Constant.TRADE_USER,ppc.getMessage(Constant.CONNECTOR_TRADE_USER));
			queryTradeInfo.put(Constant.TRADE_LOC,ppc.getMessage(Constant.CONNECTOR_TRADE_LOC));
			queryTradeInfo.put(Constant.TRADE_START_TIME, new Date());
			
			Map<String, Object> queryOut = this.transactionSessionBean.process(queryTradeInfo);
			TradeRecord tradeInfo = (TradeRecord) queryOut.get("TRADE_RECORD");
			tradeCode = tradeInfo.getTradeCode();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectorException("310040404", "快捷支付调用平台接口获取交易信息异常!", e);
		}
		return tradeCode;
	}

}
