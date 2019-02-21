package com.sumavision.tvpay.connector.util;

import java.util.concurrent.ConcurrentHashMap;

public class CacheService{

	private ConcurrentHashMap<String, String> cacheMap = new ConcurrentHashMap<String, String>();
	public final static CacheService INSTANCE = new CacheService();
	
	
	private CacheService() {
	}

	public void put( String key, String value){
		cacheMap.put(key, value);
	}
	
	public String  get( String key){
		return cacheMap.remove(key);
	}
	
}
