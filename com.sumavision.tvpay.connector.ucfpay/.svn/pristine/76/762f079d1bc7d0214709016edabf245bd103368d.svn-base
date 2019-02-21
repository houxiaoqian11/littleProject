package com.sumavision.tvpay.connector.util;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil {
	
    @SuppressWarnings("rawtypes")
	public static String post(String url, Map<String, String> params, int readTimeout, int connTimeOut) throws Exception {	
    	HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        // 链接超时
		client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeOut);
		// 读取超时
		client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        String content = "";
        try{
            post.setRequestHeader("Connection", "close");
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String paramName = entry.getKey().toString();
                String paramValue = entry.getValue().toString();
                //paramValue = URLEncoder.encode(paramValue, "UTF-8");
                //System.out.println(paramName + "=" + paramValue);
                post.addParameter(paramName, paramValue);
            }
            client.executeMethod(post);
            content = post.getResponseBodyAsString();
        }finally{
        	post.abort();
            ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            //释放连接资源
            post.releaseConnection();
        }
        return content;
    }
}