package com.sumavision.tvpay.connector.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tvpay.connector.dispatch.BankConnector;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;

/**
 * <p>
 * 接收鉴权签约结果
 * </p>
 * 
 * @author Administrator
 */
public class FastPayNoticeServlet extends HttpServlet {

	private static final long serialVersionUID = -1884866967588846292L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BankConnector connectorService;
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

	Map<String, String> tradeType = new HashMap<String, String>();

	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		connectorService = (BankConnector) ctx.getBean("bankConnector");
		ppc = (ReloadablePlaceHolderResourceBundleMessageSource) ctx.getBean("propertyConfigurer");
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doPost(arg0, arg1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("快捷支付异步开始");
		String result = "";
		try {
			
			String parameters = request.getParameter("data");
			logger.info("快捷支付接收到密文: " + parameters);
			//商户密钥
			String merchantKey = ppc.getMessage("ucfpay.fastpay.key");
			//解密
			String decryptData = AESCoder.decrypt(parameters, merchantKey);
			logger.info("快捷支付接收到异步通知: " + decryptData);
			
			JSONObject json = (JSONObject) JSON.parse(decryptData);
			Map map = JSON.toJavaObject(json, Map.class);
			
			//签名信息
			String signInfo = (String) map.remove("sign");
			//签名方式
			String type = ppc.getMessage("ucfpay.fastpay.sign.method");
			Boolean verifyResult = UcfForOnline.verify(merchantKey, "sign", signInfo, map, type);
			if(!verifyResult){
				result = "FAIL";
			}else{
				//调用平台
				Map<String,Object> input = new HashMap<String,Object>();
				input.put("METHOD","BANK-CON-001");
				input.put("BANK_CODE","ucfpay_fastpay");
				input.put("BANK_RESPONSE_PARAMS", map);
				Map<String,Object> output = connectorService.process(input);
				logger.info(" 银行发送的异步结果通知 处理结果 : ["+output+"]");
				if(output!= null && (Integer)output.get(Constant.ACTION_RESULT)==0){
					result = "SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.info("处理快捷异步出现异常! ", e);
			result = "FAIL";
		}
		response.getWriter().write(result);

	}
}
