package cn.guddqs.peakshop.front.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.TypeDAO;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductExample;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.entity.TypeExample;

@Controller
public class IndexControll {

	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/index/getlist")
	public Map<String, Object> getList() {
		Map<String, Object> map = new HashMap<String, Object>();
		//商品类型
		try{
			TypeExample example = new TypeExample();
			example.createCriteria().andIsShowEqualTo(true);
			List<Type> types = typeDAO.selectByExample(example);
			map.put("typeList", types);
		}catch(Exception e){
			logger.error("获取商品类型失败", e);
		}
		//新品商品
		try{
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			productExample.createCriteria().andIsNewEqualTo(0).andIsShowEqualTo(0);
			List<Product> newProduct = productDAO.selectByExample(productExample);
			if(newProduct !=null && newProduct.size()>2){
				//随机取两个
				map.put("newProduct", getRandomList(newProduct, 2));
			}else{
				map.put("newProduct", newProduct);
			}
		}catch(Exception e){
			logger.error("获取新品商品失败", e);
		}
		//热卖商品
		try{
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			productExample.createCriteria().andIsHotEqualTo(0).andIsShowEqualTo(0);
			List<Product> hotProduct = productDAO.selectByExample(productExample);
			if(hotProduct !=null && hotProduct.size()>2){
				//随机取两个
				map.put("hotProduct", getRandomList(hotProduct, 2));
			}else{
				map.put("hotProduct", hotProduct);
			}
		}catch(Exception e){
			logger.error("获取热卖商品失败", e);
		}
		//促销商品
		try{
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			productExample.createCriteria().andIsDiscountEqualTo(0).andIsShowEqualTo(0);
			List<Product> discountProduct = productDAO.selectByExample(productExample);
			if(discountProduct !=null && discountProduct.size()>2){
				//随机取两个
				map.put("discountProduct", getRandomList(discountProduct, 2));
			}else{
				map.put("discountProduct", discountProduct);
			}
		}catch(Exception e){
			logger.error("获取促销商品失败", e);
		}
		//团购商品
		try{
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			productExample.createCriteria().andIsGroupbuyEqualTo(0).andIsShowEqualTo(0);
			List<Product> groupbuyProduct = productDAO.selectByExample(productExample);
			if(groupbuyProduct !=null && groupbuyProduct.size()>2){
				//随机取两个
				map.put("groupbuyProduct", getRandomList(groupbuyProduct, 2));
			}else{
				map.put("groupbuyProduct", groupbuyProduct);
			}
		}catch(Exception e){
			logger.error("获取促销商品失败", e);
		}
		return map;
	}
	
	//随机取出几个元素
	private List<Product> getRandomList(List<Product> products, int count){
		//返回结果
		List<Product> subProduct = new ArrayList<Product>();
		//已取出元素的下标
		List<Integer> indexs = new ArrayList<Integer>();
		while(count>0){
			int index = new Random().nextInt(products.size());
			if(!indexs.contains(index)){
				subProduct.add(products.get(index));
				indexs.add(index);
				--count;
			}
		}
		return subProduct;
	}
	
}
