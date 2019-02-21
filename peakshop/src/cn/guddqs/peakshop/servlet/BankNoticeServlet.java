package cn.guddqs.peakshop.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.guddqs.peakshop.dao.CartDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.dao.TradeOrderDAO;
import cn.guddqs.peakshop.entity.Cart;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.entity.TradeOrderExample;
import cn.guddqs.peakshop.util.SignUtils;
import cn.guddqs.peakshop.util.XmlUtils;

/**
 * <p>
 * 解析银行异步通知结果
 * </p>
 * 
 * @author Administrator
 */
public class BankNoticeServlet extends HttpServlet {

	private static final long serialVersionUID = -1884866967588846292L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private TradeOrderDAO tradeOrderDAO;
	
	private StoreDAO storeDAO;
	
	private CartDAO cartDAO;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		tradeOrderDAO = (TradeOrderDAO) ctx.getBean("tradeOrderDAO");
		storeDAO = (StoreDAO) ctx.getBean("storeDAO");
		cartDAO = (CartDAO) ctx.getBean("cartDAO");
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

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("微信支付异步通知");
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	    response.setHeader("Content-type", "text/html;charset=UTF-8");
	    String resString = XmlUtils.parseRequst(request);
	    logger.info("银行返回结果:" + resString);
	    Map<String,String> respMap = new HashMap<String, String>();
		try {
			respMap = XmlUtils.toMap(resString.getBytes(), "utf-8");
		} catch (Exception e) {
			logger.error("处理 银行发送的异步结果通知 异常, 原因 :" + e.getMessage());
		}
		
		if("SUCCESS".equals(respMap.get("return_code"))){
			//验签
			if (SignUtils.checkParam(respMap, "d7899f3caa651c577975a6ac0ea8fe55")) {
				logger.info("验签通过");
				if("SUCCESS".equals(respMap.get("result_code"))){
					//订单号
					String orderId = respMap.get("out_trade_no");
					TradeOrderExample tradeOrderExample = new TradeOrderExample();
					tradeOrderExample.createCriteria().andOrderIdEqualTo(orderId);
					List<TradeOrder> orders = tradeOrderDAO.selectByExample(tradeOrderExample);
					for(TradeOrder order : orders){
						//订单更新为已付款待发货
						order.setStatus(1);
						tradeOrderDAO.updateByPrimaryKeySelective(order);
						//TODO
						//减少库存并删除购物车
						try{
							Integer cartId = order.getCartId();
							Cart cart = cartDAO.selectByPrimaryKey(cartId);
							Integer productId = cart.getProductId();
							Integer colorId = cart.getColorId();
							Integer sizeId = cart.getSizeId();
							StoreExample storeExample = new StoreExample();
							storeExample.createCriteria().andColorIdEqualTo(colorId).andSizeIdEqualTo(sizeId).andProductIdEqualTo(productId);
							List<Store> stores = storeDAO.selectByExample(storeExample);
							Store store = stores.get(0);
							Integer num = store.getStore()-order.getNumber();
							store.setStore(num);
							storeDAO.updateByPrimaryKeySelective(store);
							cartDAO.deleteByPrimaryKey(cartId);
						}catch(Exception e){
							logger.error("减少库存异常并删除购物车出现",e);
						}
					}
					String responseMsg = "success";
					response.setCharacterEncoding("UTF-8");
					response.addHeader("HTTP/1.1 200", "success");
					response.addHeader("Server", "Apache/1.39");
					response.addHeader("Content-Length",String.valueOf(responseMsg.length()));
					response.addHeader("Content-type", "text/html");
					response.getWriter().write(responseMsg);
					response.flushBuffer();
				}else{
					logger.warn("交易失败");
				}
				
			}else{
				logger.warn("验签不通过");
			}
		}
	
	}
}
