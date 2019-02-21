//package com.sumavision.tvpay.connector.handler.ucfpay.plateform;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.sumavision.tvpay.connector.handler.ucfpay.plateform.WebBankTradeSyncResultParseHandler;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//
//public class WebBankTradeSyncResultParseHandlerTest {
//
//	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
//	
//	private WebBankTradeSyncResultParseHandler handler;
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//	
//	@Before
//	public void setUp(){
//		handler = new WebBankTradeSyncResultParseHandler();
//		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
//		handler.setPpc(ppc);
//	}
//	@Test
//	public void test() {
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("BANK_CARD_TYPE", "2");
//		params.put("BANK_CARD_TYPE", "0");
//		Map<String, Object> bankParams = new HashMap<String, Object>();
//		bankParams.put("res", "eyJtZXJfaWQiOiIxMDAwMTM2ODciLCJvcmRlX3RpbWUiOiIyMDE2MTEyNTA4NDEzNCIsIm9yZGVy\r\nX25vIjoiMTYxMTE2MjAwMDE1MTQxNDI0IiwicmVzcF9jb2RlIjoiMDAwMDAwIiwicmVzcF9tc2ci\r\nOiIiLCJzZXJpYWxfbm8iOiJUNzU4MjI0MyIsInNpZ24iOiJSTWpRczg2Q09QdzR4bkcrUVQzUVdS\r\nalQvZnVvcENiaW50aW1ZZEpTbEd3UVFXU1JBQk1jUHA4clB1c0JqZUl3Vmpid0NIbGx0cTY3dDFR\r\nbWJNOHhGV2tTVmpmM2QxanNzeVR0TnhSTVA2dmpEWDFCMjZwc0J2dXFGbC9qUVJSWmpPTzBPajI5\r\nS1p3dTV4M3BUVzhxek9CWlhRMlZwTk1IYmNCTlpzbGJkR3M9Iiwic2lnbl90eXBlIjoiQ0VSVCIs\r\nInN0YXR1cyI6IjEiLCJzdWNjX2FtdCI6IjEiLCJzdWNjX3RpbWUiOiIyMDE2MTEyNTA4NTAzNCJ9");
//		params.put("BANK_RESPONSE_PARAMS", bankParams);
//		
//		@SuppressWarnings("unused")
//		Map<String, Object> result = handler.process(params);
//	}
//}