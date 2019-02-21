//package com.sumavision.tvpay.connector.handler.ucfpay.plateform;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.sumavision.tvpay.connector.handler.ucfpay.plateform.GetStatementHandler;
//import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
//
//public class GetStatementHandlerTest {
//
//	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
//	
//	private GetStatementHandler handler;
//	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
//	
//	@Before
//	public void setUp(){
//		handler = new GetStatementHandler();
//		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
//		handler.setPpc(ppc);
//	}
//	@Test
//	public void test() {
//		Map<String, Object> params = new HashMap<String, Object>();
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//		Date OriginTradeTime;
//		Date endTime;
//		try {
//			OriginTradeTime = sdf1.parse("201702023");
//			endTime = sdf1.parse("201702023");
//			params.put("START_TIME", OriginTradeTime);
//			params.put("END_TIME", endTime);
//			params.put("BANK_CODE", "cmb");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		@SuppressWarnings("unused")
//		Map<String, Object> result = handler.process(params);
//	}
//}