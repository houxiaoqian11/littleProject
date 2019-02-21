package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.sumavision.tvpay.connector.util.MerchantInfo;
import com.sumavision.tvpay.connector.util.RedisLock;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

public class TestRedis {
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationSpring.xml");
	ReloadablePlaceHolderResourceBundleMessageSource ppc ;
	StringRedisTemplate redisTemplate;
	RedisLock redisLock;
	@Before
	public void setUp(){
		redisTemplate = (StringRedisTemplate) ac.getBean("redisTemplate");
		redisLock = (RedisLock) ac.getBean("redisLock");
	}
	@Test
	public void runIt(){
		redisTemplate.delete("LIST_MERCHANT_INFO");
		ListOperations<String, String> vo = redisTemplate.opsForList();
		List<String> merchantInfoJson = vo.range("LIST_MERCHANT_INFO", 0, -1);
		System.out.println(merchantInfoJson);  
		
		MerchantInfo info1 = new MerchantInfo("111111","100000","10000", "100");
        MerchantInfo info2 = new MerchantInfo("222222","200000","20000", "200");
        MerchantInfo info3 = new MerchantInfo("333333","300000","30000", "300");
        MerchantInfo info4 = new MerchantInfo("444444","400000","40000", "400");
        MerchantInfo info5 = new MerchantInfo("555555","500000","50000", "500");
        MerchantInfo info6 = new MerchantInfo("666666","600000","60000", "600");
        MerchantInfo info7 = new MerchantInfo("777777","700000","70000", "700");
        MerchantInfo info8 = new MerchantInfo("888888","800000","80000", "800");
        MerchantInfo info9 = new MerchantInfo("999999","900000","90000", "900");
		
        vo.rightPushAll("LIST_MERCHANT_INFO", info1.toString(),info2.toString(),info3.toString(),info4.toString(),info5.toString(),info6.toString());
        merchantInfoJson = vo.range("LIST_MERCHANT_INFO", 0, -1);
		System.out.println(merchantInfoJson);  
	}
	
	
	public void test1(){
		
	}
}
