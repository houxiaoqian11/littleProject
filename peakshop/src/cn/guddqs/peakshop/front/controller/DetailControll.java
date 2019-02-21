package cn.guddqs.peakshop.front.controller;

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

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.dao.ex.CartDAOEx;
import cn.guddqs.peakshop.entity.Cart;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;
import cn.guddqs.peakshop.service.ProductService;
import cn.guddqs.peakshop.view.entity.ViewProduct;

@Controller
public class DetailControll {

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartDAOEx cartDAOEx;
	
	@Autowired
	private StoreDAO storeDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	//获取指定商品
	@ResponseBody
	@RequestMapping("/detail/get")
	public ViewProduct get(Integer id, Integer colorId) {
		if (id == null) {
			return null;
		}
		ViewProduct viewProduct = null;
		try {
			Product product = productDAO.selectByPrimaryKey(id);
			// 获取该商品对应的商品详情详情
			viewProduct = productService.getViewProduct(product, colorId);
		} catch (Exception e) {
			logger.error("根据ID=" + id + "获取商品失败", e);
		}
		return viewProduct;
	}
	
	//添加到购物车
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/detail/addToCart")
	public Map<String, Object> addToCart(Integer productId,Integer colorId, Integer sizeId,Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		try{
			//先判断库存是否为0
			StoreExample storeExample = new StoreExample();
			storeExample.createCriteria().andColorIdEqualTo(colorId).andProductIdEqualTo(productId).andSizeIdEqualTo(sizeId);
			List<Store> stores = storeDAO.selectByExample(storeExample);
			if(stores.size()<1){
				throw new Exception("没有该物品的仓储信息");
			}
			Integer storeNum = stores.get(0).getStore();
			if(storeNum <1){
				map.put("flag", false);
				map.put("errMsg", "库存不足");
				return map;
			}
			
			Cart cart = new Cart();
			cart.setProductId(productId);
			cart.setColorId(colorId);
			cart.setSizeId(sizeId);
			cart.setUserId(userId);
			cart.setMode(0);
			cart.setChecked(false);
			cart.setNumber(1);
			cart.setAddTime(new Date());
			cart.setEditTime(new Date());
			Integer cartId = cartDAOEx.insertReturnKey(cart);
			map.put("flag", true);
			map.put("cartId", cartId);
		}catch(Exception e){
			logger.error("添加到购物车异常",e);
		}
		return map;
	}
}
