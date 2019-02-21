package com.sumavision.tvpay.connector.util;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tvpay.exception.tech.ParameterException;

public class ParamValidator {
	final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);

	/**
	 * 网银支付Handler参数校验
	 * 校验项："connectorName" "BANK_CARD_TYPE" "B2B_FLAG"
	 *       "SUM" "TRADE_ID" "ANTI_PHISH_FLAG" "SMER_ID" "SMER_NAME" "SMER_TYPE_ID" "SMER_TYPE"
	 * */
	public static void webBankPay(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] {
				Constant.CONNECTOR_NAME,
				Constant.BANK_CARD_TYPE,
				Constant.B2B_FLAG,
				Constant.SUM,
				Constant.TRADE_ID,
				Constant.SMER_ID,
				Constant.SMER_NAME,
				Constant.SMER_TYPE_ID,
				Constant.SMER_TYPE
		};
		checkNull(_fields,params);
	}
	
	/**
	 * 网银充值/支付结果查询参数检验
	 * 检验项："BANK_CARD_TYPE" "B2B_FLAG" "TRADE_ID" "TRADE_DATE"
	 * */
	public static void queryBankResult(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] {
				Constant.BANK_CARD_TYPE,
				Constant.B2B_FLG,
				Constant.TRADE_ID,
				Constant.TRADE_DATE
		};
		checkNull(_fields,params);
	}

	/**
	 *  退款校验
	 * 
	 * */
	public static void refoundCheck(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] { 
				Constant.PAY_TYPE,
				Constant.SUM,
				Constant.PAY_TRADE_ID,
				Constant.ORIGIN_TRADE_ID,
				Constant.BANK_MERCHANT_ID
		};
		checkNull(_fields,params);
	}
	
	/**
	 * 解析支付/充值同步返回结果检验
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static void synchronizeNotice(Map<String, Object> params) {
/*		//1、非空校验
		String[] _fields = new String[] {
				Constant.METHOD,
				Constant.BANK_CODE,
				Constant.BANK_CARD_TYPE,
				Constant.B2B_FLAG,
				Constant.BANK_RESPONSE_PARAMS
		};
		checkNull(_fields,params);*/
		try{
			Map<String, Object> bankParam = (Map<String, Object>) params.get(Constant.BANK_RESPONSE_PARAMS);
			if(bankParam==null){
				throw new RuntimeException("输入参数错误，银行返回参数为空!");
			}
		}catch (Exception e) {
			ParameterException paramException = new ParameterException("310000000", "输入参数不合法!", e);
			paramException.printStackTrace();
			throw paramException;
		}
	}
	/**
	 * 退款参数检验
	 * 检验项："SUM" "ORIGIN_SUM" "BANK_CARD_TYPE" "PAY_TRADE_ID"
	 *       "ORIGIN_TRADE_ID" "PAY_TYPE" "REFUND_REAS"
	 * */
	public static void refund(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] {
				Constant.SUM,
				Constant.ORIGIN_SUM,
				Constant.BANK_CARD_TYPE,
				Constant.PAY_TRADE_ID,
				Constant.ORIGIN_TRADE_ID,
				Constant.PAY_TYPE
		};
		checkNull(_fields,params);
	}
	
	/*
	 * 参数非空校验方法
	 * */
	public static void checkNull(String[] _fields,Map<String, Object> input) {
		if (null != input && input.size() > 0) {
			for (String tmp : _fields) {
				if (input.get(tmp) == null || "".equals(input.get(tmp))) {
					logger.error("缺少必输参数" + tmp );
					throw new ParameterException("310000000","缺少参数"+tmp);
				}
			}
		} else {
			logger.error("平台输入参数为空");
			throw new ParameterException("310000000","平台输入参数为空");
		}
	}
	
}
