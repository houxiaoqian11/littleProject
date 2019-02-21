package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerExtends;

@HandlerExtends(
		method="PLA-CON-040",
		code=15L,
		validate="off",
		description="快捷支付认证")
public class FastPayAuthHandler implements Handler{
	
	//log
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String method;
	
	private long code ;
	
	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}
	
	/**
	 * <p>设置Handler代码，表示当前连接器处理服务编码，用于判断连接器是否支持某些功能（如：支持卡通支付，支持网银充值）</p>
	 *
	 * @param code
	 */
	public void setCode(long code){
		this.code = code;
	}
	
	/**
	 * <p>获取Handler代码</p>
	 *
	 * @return
	 */
	public long getCode(){
		return this.code;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> process(Map<String, Object> params) {
		//为保证现有的
		Map returnMap = new HashMap();
		returnMap.put("ACTION_RESULT", 0); 
		return returnMap;
	}
}
