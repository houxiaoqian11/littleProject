package com.sumavision.tvpay.connector.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.exception.tech.ParameterException;

public class ParamValidator {
	final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);
	
	public static void authenticationValidateCheck(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] { 
				Constant.MOBILE_PHONE,
				Constant.BANK_ACCOUNT,
				Constant.ID_TYPE,
				Constant.ID_NUMBER,
				Constant.BANK_ACCOUNT_NAME
		};
		checkNull(_fields,params);
	}
	
	/**
	 * 支付
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static void fastPayCheck(Map<String, Object> params) {
		//1、非空校验
		String[] _fields = new String[] { 
				Constant.SUM, 
				Constant.TRADE_ID,
				Constant.BANK_CODE,
				Constant.SMS_SEND_FLG

		};
		checkNull(_fields,params);
		
		String[] flds = new String[] { 
				Constant.BANK_ACCOUNT_TYPE, 
				Constant.BANK_ACCOUNT,
				Constant.PHONE_NO,

		};
		Map<String,Object> mp = (Map<String, Object>) params.get(Constant.PARAMS);
		checkNull(flds,mp);
		
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
				Constant.ORIGIN_BANK_TRADE_ID
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
