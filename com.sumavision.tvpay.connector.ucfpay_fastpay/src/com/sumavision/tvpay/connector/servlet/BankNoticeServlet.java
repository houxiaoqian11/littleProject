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

import com.sumavision.tvpay.connector.dispatch.BankConnector;
import com.sumavision.tvpay.connector.util.Constant;

/**
 * <p>解析银行异步通知结果</p>
 * @author Administrator
 */
public class BankNoticeServlet extends HttpServlet{
	
	private static final long serialVersionUID = -1884866967588846292L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BankConnector connectorService;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		connectorService = (BankConnector) ctx.getBean("bankConnector");
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doPost(arg0, arg1);
	}

	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("厦门民生B2C异步通知");
		Map<String,String> respMap = new HashMap<String,String>();
		//银行返回的参数
		for(Object k : request.getParameterMap().keySet()){
			String param = ((String[]) request.getParameterMap().get(k))[0];
			String paramFinal = new String(param.getBytes("UTF-8"), "UTF-8");
			logger.info("接收到厦门民生B2C发送的 异步通知, " + k + " ---  ["+ paramFinal + "]");
			respMap.put((String) k, paramFinal);
		}
		
		Map<String,Object> input = new HashMap<String,Object>();
		input.put("METHOD","BANK-CON-001");
		input.put("BANK_CODE","ucfpay.fastpay");
		input.put("BANK_RESPONSE_PARAMS", respMap);
		
		Map<String,Object> output = null;
		try{
			output = connectorService.process(input);
		}catch(Exception e){
			logger.error("处理 银行发送的异步结果通知 异常, 原因 :"+e.getMessage());
		}
		logger.info(" 银行发送的异步结果通知 处理结果 : ["+output+"]");
		if(output!= null && (Integer)output.get(Constant.ACTION_RESULT)==0 && output.containsKey("RETURN_MSG")){
			//需要返回银行处理通知消息
			String responseEncoding = (String)output.get("RESPONSE_ENCODING");
			String responseMsg = (String)output.get("RETURN_MSG");
			
			if(null==responseEncoding || "".equals(responseEncoding)){
				logger.info("返回银行确认消息 编码格式 为空，使用默认的GBK格式");
				responseEncoding = "GBK";
			}
			logger.info("返回银行确认消息 编码格式->"+responseEncoding);
			logger.info("返回银行确认消息->"+responseMsg);
			logger.info("准备返回银行确认消息");
			response.setCharacterEncoding(responseEncoding);
			response.addHeader("HTTP/1.1 200", "OK");
			response.addHeader("Server", "Apache/1.39");
			response.addHeader("Content-Length", String.valueOf(responseMsg.length()));
			response.addHeader("Content-type", "text/html");
			response.getWriter().write(responseMsg);
			response.flushBuffer();
		}else{
			logger.info("不需要返回银行确认消息");
		}
	}
}
