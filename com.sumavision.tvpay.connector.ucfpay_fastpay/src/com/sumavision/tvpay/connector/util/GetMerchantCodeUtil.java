package com.sumavision.tvpay.connector.util;

import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class GetMerchantCodeUtil {

	public static final String merchantInfoKey = "UCF_FASTPAY_MERCHANT_LIST";
	
	public static String getMerchantCode(RedisLock redisLock, RedisTemplate redisTemplate) throws Exception{
		//redis全局锁
		if(redisLock.lock()){
			//获取redis中所有的商户信息
			ListOperations<String, String> vo = redisTemplate.opsForList();
			List<String> listMerchantInfo = vo.range("UCF_FASTPAY_MERCHANT_LIST", 0, -1);
			//获取
			
		}else{
			throw new Exception("获取redis锁超时");
		}
		
		return null;
		
	}
	
}
