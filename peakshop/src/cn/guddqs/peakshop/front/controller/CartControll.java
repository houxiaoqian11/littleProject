package cn.guddqs.peakshop.front.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.CartDAO;
import cn.guddqs.peakshop.dao.ColorDAO;
import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.SizeDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.entity.Cart;
import cn.guddqs.peakshop.entity.CartExample;
import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;

@Controller
public class CartControll {

	@Autowired
	private CartDAO cartDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ColorDAO colorDAO;
	
	@Autowired
	private SizeDAO sizeDAO;
	
	@Autowired
	private StoreDAO storeDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	//获取购物车列表
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/cart/getlist")
	public Map<String, Object> getList(Integer userId) throws Exception {
		//返回结果
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> cartlist = new ArrayList<Map<String,Object>>();
		try{
			//获取所有的cart
			CartExample cartExample = new CartExample();
			cartExample.setOrderByClause("edit_time desc");
			cartExample.createCriteria().andUserIdEqualTo(userId);
			List<Cart> carts = cartDAO.selectByExample(cartExample);
			//遍历取出商品名称、颜色名称和尺码名称
			for(Cart cart : carts){
				Map<String, Object> cartMap = new HashMap<String, Object>();
				cartMap.put("cart", cart);
				//商品Id
				Integer productId = cart.getProductId();
				if(productId != null){
					Product product = productDAO.selectByPrimaryKey(productId);
					cartMap.put("product", product);
				}
				//颜色Id
				Integer colorId = cart.getColorId();
				if(colorId != null){
					Color color = colorDAO.selectByPrimaryKey(colorId);
					cartMap.put("color", color);
				}
				//尺码Id
				Integer sizeId = cart.getSizeId();
				if(sizeId != null){
					Size size = sizeDAO.selectByPrimaryKey(sizeId);
					cartMap.put("size", size);
				}
				//库存
				StoreExample storeExample = new StoreExample();
				storeExample.createCriteria().andColorIdEqualTo(colorId).andProductIdEqualTo(productId).andSizeIdEqualTo(sizeId);
				List<Store> stores = storeDAO.selectByExample(storeExample);
				if(stores.size()<1){
					logger.error("没有productId=" + productId+"的仓储信息");
				}
				cartMap.put("store", stores.get(0));
				cartlist.add(cartMap);
			}
		}catch(Exception e){
			logger.error("获取购物车列表异常",e);
		}
		returnMap.put("cartlist", cartlist);
		return returnMap;
	}
	
	//删除购物车
	@ResponseBody
	@RequestMapping("/cart/del")
	public Map<String, Object> del(Integer id, Integer userId) throws Exception {
		//返回结果
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", false);
		try{
			//删除
			cartDAO.deleteByPrimaryKey(id);
			//获取新的购物车列表
			returnMap = getList(userId);
			returnMap.put("flag", true);
		}catch(Exception e){
			logger.error("删除购物车异常",e);
		}
		return returnMap;
	}
	
	//更改购物车选中状态
	@ResponseBody
	@RequestMapping("/cart/check")
	public Map<String, Object> check(Integer id) throws Exception {
		//返回结果
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", false);
		try{
			//获取购物车信息
			Cart cart = cartDAO.selectByPrimaryKey(id);
			//购物车状态
			Boolean check = cart.getChecked();
			//赋值为相反状态
			cart.setChecked(!check);
			//更新
			cartDAO.updateByPrimaryKeySelective(cart);
			returnMap.put("flag", true);
		}catch(Exception e){
			logger.error("更改购物车选中状态异常",e);
		}
		return returnMap;
	}
	
	//更改购物车中商品数量
	@ResponseBody
	@RequestMapping("/cart/changeNum")
	public Map<String, Object> changeNum(Integer id, Integer num) throws Exception {
		//返回结果
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", false);
		try{
			//获取购物车信息
			Cart cart = new Cart();
			cart.setId(id);
			cart.setNumber(num);
			cart.setEditTime(new Date());
			cartDAO.updateByPrimaryKeySelective(cart);
			returnMap.put("flag", true);
		}catch(Exception e){
			logger.error("更改购物车中商品数量异常",e);
		}
		return returnMap;
	}
	
}
