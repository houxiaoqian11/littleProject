package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.MerchantInfo;
import com.sumavision.tvpay.connector.util.RedisLock;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class GetFastTradeValidateCodeHandleTest {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	ReloadablePlaceHolderResourceBundleMessageSource ppc ;
	GetFastTradeValidateCodeHandler hand;
	StringRedisTemplate redisTemplate;
	RedisLock redisLock;
	@Before
	public void setUp(){
		hand = new GetFastTradeValidateCodeHandler();
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ac.getBean("propertyConfigurer");
		redisTemplate = (StringRedisTemplate) ac.getBean("redisTemplate");
		redisLock = (RedisLock) ac.getBean("redisLock");
		hand.setPpc(ppc);
		hand.setRedisLock(redisLock);
		hand.setRedisTemplate(redisTemplate);
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void runIt(){
		Map params = new HashMap();
		params.put(Constant.TRADE_ID, "201803291500003");
		params.put(Constant.BANK_ACCOUNT, "6227000735070071208");
		params.put(Constant.BANK_ACCOUNT_NAME, "侯晓谦");
		params.put(Constant.ID_NUMBER, "142724198912083135");
		params.put(Constant.MOBILE_PHONE, "13581693144");
		params.put(Constant.BANK_ACCOUNT_TYPE, "0");
		params.put(Constant.PRODUCT_NAME, "测试商品");
		params.put(Constant.FUND, new BigDecimal(0.01));
		
		hand.process(params);
	}
}
