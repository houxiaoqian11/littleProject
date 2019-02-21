package cn.guddqs.peakshop.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.guddqs.peakshop.service.OrderService;

@Controller
public class TradeOrderController {
	
	@Autowired
	private OrderService orderService;

	//获取订单列表
	@RequestMapping(value = "/order/list.do")
	public String list(Integer pageNo,String orderId,Integer status,Model model) throws Exception{
		orderService.list(pageNo, status, orderId, model);
		return "order/list";
	}
	
	//发货
	@RequestMapping(value = "/order/ship.do")
	public String ship(Integer id,Model model,String wuliuNo) throws Exception{
		orderService.ship(id, wuliuNo, model);
		return "redirect:/order/list.do";
	}
	
}
