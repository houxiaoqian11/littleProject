package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class FastPayHandlerTest {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	ReloadablePlaceHolderResourceBundleMessageSource ppc ;
	FastPayHandler hand;
	@Before
	public void setUp(){
		hand = new FastPayHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
		hand.setPpc(ppc);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void runIt(){
		Map params = new HashMap();
		params.put(Constant.TRADE_ID, "201803211500008");
		params.put(Constant.BANK_MERCHANT_ID, "M200000550");
		params.put(Constant.DYN_VERIFY_CODE, "999999");
		params.put(Constant.SUM, new BigDecimal(0.01));
		params.put(Constant.BANK_CODE, "ICBC");
		Map bankParams = new HashMap();
		bankParams.put("ID_NUMBER", "142724198912083135");
		bankParams.put("BANK_ACCOUNT_TYPE", "0");
		bankParams.put("ID_TYPE", "0");
		bankParams.put("BANK_ACCOUNT_NAME", "侯晓谦");
		bankParams.put("PHONE_NO", "13581693144");
		bankParams.put("BANK_ACCOUNT", "622700000071208");
		params.put("PARAMS", bankParams);
		params.put("SMS_SEND_FLG", 1);
		hand.process(params);
	}
}
