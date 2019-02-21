package cn.guddqs.peakshop.front.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductExample;
import cn.guddqs.peakshop.entity.ProductExample.Criteria;

@Controller
public class DiscountControll {

	@Autowired
	private ProductDAO productDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/discount/getlist")
	public List<Product> getList(String keyword) throws Exception {
		List<Product> productlist = new ArrayList<Product>();
		
		try{
			// keyword查询所有上架的促销商品
			ProductExample productExample = new ProductExample();
			productExample.setOrderByClause("edit_time desc");
			Criteria criteria = productExample.createCriteria();
			criteria.andIsDiscountEqualTo(0).andIsShowEqualTo(0);
			if (!StringUtils.isEmpty(keyword)) {
				String name = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
				logger.info("keyword=" + name);
				String decodeName = URLDecoder.decode(name, "UTF-8");
				criteria.andNameLike("%" + decodeName + "%");
			}
			productlist = productDAO.selectByExample(productExample);
		}catch(Exception e){
			logger.error("获取促销商品失败", e);
		}
		return productlist;
	}
}
