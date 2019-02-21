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

public class TestRefoundFastPay {
	ApplicationContext ac = new ClassPathXmlApplicationContext(
			"applicationSpring.xml");
	ReloadablePlaceHolderResourceBundleMessageSource ppc;
	RefundHandler hand;

	@Before
	public void setUp() {
		hand = new RefundHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac
				.getBean("propertyConfigurer");
		hand.setPpc(ppc);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void runIt() {
		Map params = new HashMap();
		params.put(Constant.BANK_MERCHANT_ID, "M200000550");
		params.put(Constant.PAY_TRADE_ID, "201803221125002");
		params.put(Constant.ORIGIN_TRADE_ID, "201803211500006");
		params.put(Constant.SUM, new BigDecimal(0.01));
		params.put(Constant.REFUND_REAS, "假货");
		params.put(Constant.PAY_TYPE, 0);
		hand.process(params);
	}
}
