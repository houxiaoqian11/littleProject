package cn.guddqs.peakshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 
 * HTTP请求对象
 *
 * 
 * 
 * @author hxq
 * 
 */

public class HttpUtil {

	private String defaultContentEncoding;

	public HttpUtil() {

		this.defaultContentEncoding = Charset.defaultCharset().name();

	}

	/**
	 * 
	 * 发送GET请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendGet(String urlString) throws IOException {

		return this.send(urlString, "GET", null, null);
	}

	/**
	 * 
	 * 发送GET请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @param params
	 * 
	 *            参数集合
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendGet(String urlString, Map<String, String> params)

			throws IOException {

		return this.send(urlString, "GET", params, null);

	}

	/**
	 * 
	 * 发送GET请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @param params
	 * 
	 *            参数集合
	 * 
	 * @param propertys
	 * 
	 *            请求属性
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendGet(String urlString, Map<String, String> params,

			Map<String, String> propertys) throws IOException {

		return this.send(urlString, "GET", params, propertys);

	}

	/**
	 * 
	 * 发送POST请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendPost(String urlString) throws IOException {

		return this.send(urlString, "POST", null, null);

	}

	/**
	 * 
	 * 发送POST请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @param params
	 * 
	 *            参数集合
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendPost(String urlString, Map<String, String> params)

			throws IOException {

		return this.send(urlString, "POST", params, null);

	}

	/**
	 * 
	 * 发送POST请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 *            URL地址
	 * 
	 * @param params
	 * 
	 *            参数集合
	 * 
	 * @param propertys
	 * 
	 *            请求属性
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	public HttpRespons sendPost(String urlString, Map<String, String> params,

			Map<String, String> propertys) throws IOException {

		return this.send(urlString, "POST", params, propertys);

	}

	/**
	 * 
	 * 发送HTTP请求
	 *
	 * 
	 * 
	 * @param urlString
	 * 
	 * @return 响映对象
	 * 
	 * @throws IOException
	 * 
	 */

	private HttpRespons send(String urlString, String method,

			Map<String, String> parameters, Map<String, String> propertys)

			throws IOException {

		HttpURLConnection urlConnection = null;

		if (method.equalsIgnoreCase("GET") && parameters != null) {

			StringBuffer param = new StringBuffer();

			int i = 0;

			for (String key : parameters.keySet()) {

				if (i == 0)

					param.append("?");

				else

					param.append("&");

				param.append(key).append("=").append(parameters.get(key));

				i++;

			}

			urlString += param;

		}

		URL url = new URL(urlString);

		urlConnection = (HttpURLConnection) url.openConnection();

		urlConnection.setRequestMethod(method);

		urlConnection.setDoOutput(true);

		urlConnection.setDoInput(true);

		urlConnection.setUseCaches(false);

		if (propertys != null)

			for (String key : propertys.keySet()) {

				urlConnection.addRequestProperty(key, propertys.get(key));

			}

		if (method.equalsIgnoreCase("POST") && parameters != null) {

			StringBuffer param = new StringBuffer();

			for (String key : parameters.keySet()) {

				param.append("&");

				param.append(key).append("=").append(parameters.get(key));

			}

			urlConnection.getOutputStream().write(param.toString().getBytes());

			urlConnection.getOutputStream().flush();

			urlConnection.getOutputStream().close();

		}

		return this.makeContent(urlString, urlConnection);

	}

	/**
	 * 
	 * 得到响应对象
	 *
	 * 
	 * 
	 * @param urlConnection
	 * 
	 * @return 响应对象
	 * 
	 * @throws IOException
	 * 
	 */

	private HttpRespons makeContent(String urlString,

			HttpURLConnection urlConnection) throws IOException {

		HttpRespons httpResponser = new HttpRespons();

		try {

			InputStream in = urlConnection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(

					new InputStreamReader(in));

			httpResponser.contentCollection = new Vector<String>();

			StringBuffer temp = new StringBuffer();

			String line = bufferedReader.readLine();

			while (line != null) {

				httpResponser.contentCollection.add(line);

				temp.append(line).append("\r\n");

				line = bufferedReader.readLine();

			}

			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();

			if (ecod == null)

				ecod = this.defaultContentEncoding;

			httpResponser.urlString = urlString;

			httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();

			httpResponser.file = urlConnection.getURL().getFile();

			httpResponser.host = urlConnection.getURL().getHost();

			httpResponser.path = urlConnection.getURL().getPath();

			httpResponser.port = urlConnection.getURL().getPort();

			httpResponser.protocol = urlConnection.getURL().getProtocol();

			httpResponser.query = urlConnection.getURL().getQuery();

			httpResponser.ref = urlConnection.getURL().getRef();

			httpResponser.userInfo = urlConnection.getURL().getUserInfo();

			httpResponser.content = new String(temp.toString().getBytes(), ecod);

			httpResponser.contentEncoding = ecod;

			httpResponser.code = urlConnection.getResponseCode();

			httpResponser.message = urlConnection.getResponseMessage();

			httpResponser.contentType = urlConnection.getContentType();

			httpResponser.method = urlConnection.getRequestMethod();

			httpResponser.connectTimeout = urlConnection.getConnectTimeout();

			httpResponser.readTimeout = urlConnection.getReadTimeout();

			return httpResponser;

		} catch (IOException e) {

			throw e;

		} finally {

			if (urlConnection != null)

				urlConnection.disconnect();

		}

	}

	/**
	 * 
	 * 默认的响应字符集
	 * 
	 */

	public String getDefaultContentEncoding() {

		return this.defaultContentEncoding;

	}

	/**
	 * 
	 * 设置默认的响应字符集
	 * 
	 */

	public void setDefaultContentEncoding(String defaultContentEncoding) {

		this.defaultContentEncoding = defaultContentEncoding;

	}
	
	public static void main(String[] args) throws Exception {
		HttpUtil httpUtil = new HttpUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "wx82be7eb346f83237");
		params.put("secret", "0c7980440c048bf653d3f6274f92f053");
		params.put("js_code", "001kgrPp1tVgon0SsoPp1odFPp1kgrP5");
		params.put("grant_type", "authorization_code");
		HttpRespons sendGet = httpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params );
		System.out.println(sendGet);
	}
	
	public static Map<String, String> send(String url, String reqXml,
			int readTimeout, int connTimeOut, String encoding) throws Exception {
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		RequestConfig config =  RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connTimeOut).build();
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entityParams = new StringEntity(reqXml, "utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			httpPost.setConfig(config);
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if (response != null && response.getEntity() != null) {
				resultMap = XmlUtils.toMap(
						EntityUtils.toByteArray(response.getEntity()), "utf-8");
				System.out.println("请求结果：" + resultMap);

			} else {
				System.out.println("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
			if (client != null) {
				client.close();
			}
		}
		return resultMap;
	}
	
	public static String send1(String url, String reqXml,
			int readTimeout, int connTimeOut, String encoding) throws Exception {
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		RequestConfig config =  RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connTimeOut).build();
		String resp = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entityParams = new StringEntity(reqXml, "utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			httpPost.setConfig(config);
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if (response != null && response.getEntity() != null) {
				resp = new String(EntityUtils.toByteArray(response.getEntity()), "utf-8");

			} else {
				System.out.println("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
			if (client != null) {
				client.close();
			}
		}
		return resp;
	}
	

}
