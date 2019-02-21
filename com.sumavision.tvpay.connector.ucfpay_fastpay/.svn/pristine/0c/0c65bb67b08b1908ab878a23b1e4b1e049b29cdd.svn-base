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

public class QueryTradeServlet extends HttpServlet {

	private static final long serialVersionUID = -1884866967588846292L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BankConnector connectorService;


	@Override
	public void init() throws ServletException {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		connectorService = (BankConnector) ctx.getBean("bankConnector");
		super.init();
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
		String tradeId = request.getParameter("tradeId");

		String result = queryTradeStatus(tradeId );
		if ("0".equals(result)) {
			response.setCharacterEncoding("GBK");
			response.addHeader("HTTP/1.1 200", "OK");
			response.addHeader("Server", "Apache/1.39");
			response.addHeader("Content-type", "text/html");
			response.getWriter().write("OK");
			response.flushBuffer();
		} else {
			response.setCharacterEncoding("GBK");
			response.addHeader("HTTP/1.1 500", "Error");
			response.addHeader("Server", "Apache/1.39");
			response.addHeader("Content-type", "text/html");
			response.getWriter().write("ERROR:" + result);
			response.flushBuffer();
		}

	}

	public String queryTradeStatus(String orderId ) {
		Map<String,Object> input = new HashMap<String,Object>();
		input.put("METHOD","PLA-CON-018");
		input.put("BANK_CODE","ucfpayfastpay");
		input.put(Constant.TRADE_ID, orderId);
		
		Map<String,Object> output = connectorService.process(input);
		logger.info("查询交易返回结果 : ["+output+"]");
		
		if(output.get(Constant.ACTION_RESULT) != null && (Integer)output.get(Constant.ACTION_RESULT)==0)
			return "0";
		
		return null;
		
	}

}
