//package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;
//
//import java.lang.reflect.Field;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.sumavision.tvpay.connector.HandlerAutowired;
//import com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform.GetStatementHandler;
//
//public class GetStatementHandlerTest {
//
//	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
//	
//	private GetStatementHandler handler;
//	
//	@Test
//	public void test() {
//		handler = new GetStatementHandler();
//		handler.setCode(19L);
//		handler.setMethod("PLA-CON-020");
//		
//		Field[] fields = handler.getClass().getDeclaredFields();
//		for(Field field : fields){
//			if(field.isAnnotationPresent(HandlerAutowired.class)){
//				HandlerAutowired ha = field.getAnnotation(HandlerAutowired.class);
//				String spname = ha.springName();
//				if("$$".equals(spname)){
//					spname = field.getName();
//				}
//				Object spbean = ac.getBean(spname);
//				// 修改field的作用域为可访问，设置spbean后恢复为不可访问
//				field.setAccessible(true);
//				try {
//					field.set(handler, spbean);
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				field.setAccessible(false);
//			}
//		}
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//		Date OriginTradeTime;
//		try {
//			OriginTradeTime = sdf1.parse("20151209");
//			params.put("START_TIME", OriginTradeTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		@SuppressWarnings("unused")
//		Map<String, Object> result = handler.process(params);
//	}
//
//}
