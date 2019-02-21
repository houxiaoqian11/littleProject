package cn.guddqs.peakshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import cn.guddqs.peakshop.dao.TradeOrderDAO;
import cn.guddqs.peakshop.dao.ex.TradeOrderDAOEx;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.entity.TradeOrderExample;
import cn.guddqs.peakshop.entity.TradeOrderExample.Criteria;
import cn.guddqs.peakshop.util.Pagination;
import cn.guddqs.peakshop.view.entity.OrderStatus;


@Service
public class OrderService {
	
	@Autowired
	private TradeOrderDAO tradeOrderDAO;
	
	@Autowired
	private TradeOrderDAOEx tradeOrderDAOEx;

	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	public void list(Integer pageNo,Integer status,String orderId, Model model) throws Exception{
		logger.info("获取订单列表: orderId="+orderId+";status="+status);
		try{
			//获取所有的订单状态
			List<OrderStatus> allOrderStatus = getAllOrderStatus();
			model.addAttribute("allOrderStatus", allOrderStatus);
			
			//获取所有订单总条数
			TradeOrderExample tradeOrderExample = new TradeOrderExample();
			Map<String,Object> map = new HashMap<String,Object>();
			Criteria criteria = tradeOrderExample.createCriteria();
			if(status == null){
				//默认选择已付款待发货的订单
				status = 1;
			}
			map.put("status", status);
			criteria.andStatusEqualTo(status);
			if(orderId != null){
				map.put("orderId", orderId);
				criteria.andOrderIdEqualTo(orderId);
			}
			
			int totalCount = tradeOrderDAO.countByExample(tradeOrderExample);
			Pagination pagination = new Pagination(Pagination.cpn(pageNo),10,totalCount);
			//分页查询订单
			List<TradeOrder> tradeOrders = tradeOrderDAOEx.getOrderListEx(map, pagination);
			pagination.setList(tradeOrders);
			StringBuilder params = new StringBuilder();
			//页面分页展示
			String url = "/peakshop/order/list.do";
			pagination.pageView(url, params.toString());
			model.addAttribute("pagination", pagination);
			model.addAttribute("status", status);
			model.addAttribute("orderId", orderId);
		}catch(Exception e){
			logger.error("获取订单列表失败",e);
			throw new Exception("获取订单列表失败",e);
		}
	}
	
	private List<OrderStatus> getAllOrderStatus(){
		List<OrderStatus> orderStatus = new ArrayList<OrderStatus>();
		orderStatus.add(new OrderStatus(0, "待付款"));
		orderStatus.add(new OrderStatus(1, "已付款待发货"));
		orderStatus.add(new OrderStatus(2, "待收货"));
		orderStatus.add(new OrderStatus(3, "已完成"));
		orderStatus.add(new OrderStatus(9, "已取消"));
		return orderStatus;
	}

	public void ship(Integer id, String wuliuNo, Model model) throws Exception {
		logger.info("发货: id="+id+";wuliuNo="+wuliuNo);
		try{
			//获取订单
			TradeOrder tradeOrder = tradeOrderDAO.selectByPrimaryKey(id);
			//校验订单状态
			Integer status = tradeOrder.getStatus();
			if(status != 1){
				throw new Exception("订单状态不为已支付待发货");
			}
			//更新状态为已发货
			tradeOrder.setStatus(2);
			//填入物流编号
			tradeOrder.setWuliuNo(wuliuNo);
			//更新
			tradeOrderDAO.updateByPrimaryKeySelective(tradeOrder);
		}catch(Exception e){
			logger.error("发货失败",e);
			throw new Exception("发货失败",e);
		}
	}
	
}
