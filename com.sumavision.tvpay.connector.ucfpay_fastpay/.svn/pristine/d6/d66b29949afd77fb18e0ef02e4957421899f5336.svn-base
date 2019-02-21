
/**
 * @author Administrator
 *
 */
package com.sumavision.tvpay.connector.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil{
	
	public static String send(Map<String, String> reqParams, Map<String, Object> resultMap, String url) throws Exception {

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : reqParams.keySet()) {
                String value = reqParams.get(key);
                NameValuePair nameValuePair = new BasicNameValuePair(key, value);
                nvps.add(nameValuePair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (200 != status) {
                resultMap.put("isSucceed", false);
            }
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            return responseString;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
}
