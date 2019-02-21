package com.sumavision.tvpay.connector.handler.ucfpay.plateform;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sumavision.tvpay.connector.handler.ucfpay.plateform.QueryWebBankPayResultHandler;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class QueryWebBankPayResultHandlerTest {

	private QueryWebBankPayResultHandler handler;
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	@Before
	public void setUp(){
		handler = new QueryWebBankPayResultHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
		handler.setPpc(ppc);
	}
	@Test
	public void test() {	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.BANK_CARD_TYPE, 0);
		params.put(Constant.BANK_CODE, "cmb");
		params.put(Constant.B2B_FLG, 0);
		params.put(Constant.TRADE_ID, "201803231402013");
		params.put("TRADE_DATE", new Date());
		params.put("BANK_MERCHANT_ID", "M200006113");
		@SuppressWarnings("unused")
		Map<String, Object> result = handler.process(params);
	}
}