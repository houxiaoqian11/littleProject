package cn.guddqs.peakshop.front.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.TradeOrderDAO;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.entity.TradeOrderExample;

@Controller
public class OrderControll {

	@Autowired
	private TradeOrderDAO tradeOrderDAO;
	
//	@Autowired
//	private OrderService orderService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	//查询各个状态的订单数
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/order/getNum")
	public Map<String, Object> getNum(Integer userId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//查询所有的订单
		try{
			Map<String, Object> orderInfo = new HashMap<String, Object>();
			TradeOrderExample example = new TradeOrderExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<TradeOrder> orders = tradeOrderDAO.selectByExample(example);
			//待付款
			int noPayNum = 0;
			//待发货
			int noSendNum = 0;
			//待收货
			int noRecNum = 0;
			//已完成
			int finishNum = 0;
			for(TradeOrder order : orders){
				if(order.getStatus() == 0){
					noPayNum++;
				}else if(order.getStatus() == 1){
					noSendNum++;
				}else if(order.getStatus() == 2){
					noRecNum++;
				}else if(order.getStatus() == 3){
					finishNum++;
				}
			}
			orderInfo.put("noPayNum", noPayNum);
			orderInfo.put("noSendNum", noSendNum);
			orderInfo.put("noRecNum", noRecNum);
			orderInfo.put("finishNum", finishNum);
			returnMap.put("orderInfo", orderInfo);
			returnMap.put("status", 1);
		}catch(Exception e){
			logger.error("查询各个状态的订单数异常",e);
		}
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/order/getList")
	public Map<String, Object> getList(Integer userId, Integer status) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try{
			//查询订单
			TradeOrderExample example = new TradeOrderExample();
			example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(status);
			List<TradeOrder> orders = tradeOrderDAO.selectByExample(example);
			returnMap.put("orderlist", orders);
		}catch(Exception e){
			logger.error("获取订单异常",e);
		}
		return returnMap;
	}

	@ResponseBody
	@RequestMapping("/order/getOrder")
	public Map<String, Object> getOrder(Integer orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			TradeOrder tradeOrder = tradeOrderDAO.selectByPrimaryKey(orderId);
			map.put("status", 1);
			map.put("orderInfo", tradeOrder);
		}catch(Exception e){
			logger.error("获取ID="+orderId+"订单失败",e);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/order/changeStatus")
	public Map<String, Object> changeStatus(Integer orderId, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			TradeOrder tradeOrder = new TradeOrder();
			tradeOrder.setId(orderId);
			tradeOrder.setStatus(status);
			tradeOrderDAO.updateByPrimaryKeySelective(tradeOrder);
			map.put("status", 1);
		}catch(Exception e){
			logger.error("修改订单状态异常",e);
		}
		return map;
	}
	
//	@ResponseBody
//	@RequestMapping("/order_cancel")
//	public Map<String, Object> cancelOrder(Integer id) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("flag", false);
//		if (id != null) {
//			Order ord = orderService.getOrder(id);
//			ord.setStatus("交易关闭");
//			orderService.updateOrder(ord);
//			map.put("flag", true);
//		}
//		return map;
//	}
	
//	@ResponseBody
//	@RequestMapping("/order_confirm")
//	public Map<String, Object> confirmOrder(Integer id) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("flag", false);
//		if (id != null) {
//			Order ord = orderService.getOrder(id);
//			ord.setStatus("交易成功");
//			orderService.updateOrder(ord);
//			map.put("flag", true);
//		}
//		return map;
//	}


}
