package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.util.Constant;

public class RefundQueryHandlerTest {

	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	
	private RefundQueryHandler handler;
	
	@Test
	public void test() {
		handler = new RefundQueryHandler();
		handler.setCode(20L);
		handler.setMethod("PLA-CON-121");
		
		Field[] fields = handler.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(HandlerAutowired.class)){
				HandlerAutowired ha = field.getAnnotation(HandlerAutowired.class);
				String spname = ha.springName();
				if("$$".equals(spname)){
					spname = field.getName();
				}
				Object spbean = ac.getBean(spname);
				// 修改field的作用域为可访问，设置spbean后恢复为不可访问
				field.setAccessible(true);
				try {
					field.set(handler, spbean);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				field.setAccessible(false);
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		Date OriginTradeTime = null;
		try {
			OriginTradeTime = sdf1.parse("20140807");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		params.put("SUM", new BigDecimal("0.01"));
		params.put("BANK_CARD_TYPE", "0");
		params.put("PAY_TRADE_ID", "201803221125001");
		params.put("ORIGIN_TRADE_ID", "201803211500006");
		params.put("ORIGIN_BANK_TRADE_ID", "201502252152025421848");
		
		params.put("ORIGIN_TRADE_TIME", OriginTradeTime);
		params.put("B2B_FLG", 0);
		params.put(Constant.BANK_MERCHANT_ID, "M200000550");
		
		
		Map<String, Object> result = handler.process(params);
		System.out.println(result);
	}

}
