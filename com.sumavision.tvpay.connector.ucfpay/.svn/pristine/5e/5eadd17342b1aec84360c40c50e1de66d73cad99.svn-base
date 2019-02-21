package com.sumavision.tvpay.connector.handler.ucfpay.plateform;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.sumavision.tvpay.connector.handler.ucfpay.plateform.GenerateWebBankPayFormHandler;
import com.sumavision.tvpay.connector.util.RedisLock;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class GenerateWebBankPayFormHandlerTest {

	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	
	private GenerateWebBankPayFormHandler handler;
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	StringRedisTemplate redisTemplate;
	RedisLock redisLock;
	
	@Before
	public void setUp(){
		handler = new GenerateWebBankPayFormHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
		redisTemplate = (StringRedisTemplate) ac.getBean("redisTemplate");
		redisLock = (RedisLock) ac.getBean("redisLock");
		handler.setPpc(ppc);
		handler.setRedisLock(redisLock);
		handler.setRedisTemplate(redisTemplate);
	}
	@Test
	public void test() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("connectorName", "PLA-CON-001");
		params.put("BANK_CODE", "CSYH");
		params.put("BANK_CARD_TYPE", "1");
		params.put("B2B_FLAG", "0");
		params.put("PAY_DESC", "test");
		params.put("SUM", new BigDecimal(1));
		params.put("TRADE_ID", "201803231402014");
		params.put("ANTI_PHISH_FLAG", "0");
		params.put("SMER_ID", "1234567890");
		params.put("SMER_NAME", "xiao");
		params.put("SMER_TYPE_ID", "1234567890");
		params.put("SMER_TYPE", "1234567890");
		
		@SuppressWarnings("unused")
		Map<String, Object> result = handler.process(params);
	}
}