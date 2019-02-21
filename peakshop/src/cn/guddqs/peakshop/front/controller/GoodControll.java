package cn.guddqs.peakshop.front.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.TypeDAO;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductExample;
import cn.guddqs.peakshop.entity.ProductExample.Criteria;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.entity.TypeExample;

@Controller
public class GoodControll {

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/good/getProductList")
	public List<Product> getList(HttpServletRequest request, Integer typeId, String keyword) throws Exception {
		logger.info("typeId=" + typeId + "; keyword=" + keyword);
		List<Product> products = new ArrayList<Product>();
		try {
			// 根据typeId和keyword查询所有的商品
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			Criteria criteria = productExample.createCriteria().andIsShowEqualTo(0);
			if (typeId != null) {
				criteria.andTypeIdEqualTo(typeId);
			}
			if (!StringUtils.isEmpty(keyword)) {
				String name = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
				logger.info( "keyword=" + name);
				String decodeName = URLDecoder.decode(name, "UTF-8");
				criteria.andNameLike("%" + decodeName + "%");
			}
			products = productDAO.selectByExample(productExample);
		} catch (Exception e) {
			logger.error("获取商品失败", e);
		}
		return products;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/good/getTypeList")
	public List<Type> getList() {
		List<Type> types = new ArrayList<Type>();
		try{
			TypeExample example = new TypeExample();
			example.createCriteria().andIsShowEqualTo(true);
			types = typeDAO.selectByExample(example);
			
		}catch(Exception e){
			logger.error("获取商品类型失败", e);
		}
		return types;
	}
}
