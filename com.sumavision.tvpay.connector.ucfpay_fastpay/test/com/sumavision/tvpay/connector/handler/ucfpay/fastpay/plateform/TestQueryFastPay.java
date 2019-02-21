package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class TestQueryFastPay {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	ReloadablePlaceHolderResourceBundleMessageSource ppc ;
	QueryFastPayResultHandler hand;
	@Before
	public void setUp(){
		hand = new QueryFastPayResultHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
		hand.setPpc(ppc);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void runIt(){
		Map params = new HashMap();
		params.put(Constant.TRADE_ID, "201803211500006");
		params.put(Constant.BANK_MERCHANT_ID, "M200000550");
		params.put(Constant.BANK_CODE, "ICBC");
		hand.process(params);
	}
}
