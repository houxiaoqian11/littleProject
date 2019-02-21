package cn.guddqs.peakshop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.TypeDAO;
import cn.guddqs.peakshop.dao.ex.TypeDAOEx;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductExample;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.entity.TypeExample;
import cn.guddqs.peakshop.util.Pagination;
import cn.guddqs.peakshop.view.entity.ViewType;


@Service
public class TypeService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private TypeDAOEx typeDAOEx;
	
	@SuppressWarnings("unchecked")
	public ViewType getViewType(Type type) throws Exception{
		logger.info("开始获取视图商品类型");
		if(type == null){
			return null;
		}
		ViewType viewType = new ViewType(type);
		try{
			//商品类型ID
			Integer typeId = type.getId();
			ProductExample productExample = new ProductExample();
			productExample.createCriteria().andTypeIdEqualTo(typeId);
			List<Product> products = productDAO.selectByExample(productExample);
			viewType.setProducts(products);
		}catch(Exception e){
			logger.error("获取视图商品类型信息异常",e);
			throw new Exception("获取视图商品类型信息异常",e);
		}
		return viewType;
	}
	
	//展示商品类型列表
	public void list(Integer pageNo,Model model, Boolean isShow) throws Exception{
		try{
			//获取所有商品类型总条数
			TypeExample example = new TypeExample();
			if(isShow == null){
				isShow = true;
			}
			example.createCriteria().andIsShowEqualTo(isShow);
			int totalCount = typeDAO.countByExample(example);
			Pagination pagination = new Pagination(Pagination.cpn(pageNo),10,totalCount);
			//分页查询商品类型
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("isShow", isShow);
			List<Type> types = typeDAOEx.getTypeListEx(map, pagination);
			pagination.setList(types);
			StringBuilder params = new StringBuilder();
			//页面分页展示
			String url = "/peakshop/type/list.do";
			pagination.pageView(url, params.toString());
			model.addAttribute("pagination", pagination);
			model.addAttribute("isShow", isShow);
		}catch(Exception e){
			logger.error("获取商品类型列表isShow="+isShow+"异常",e);
			throw new Exception("获取商品类型列表isShow="+isShow+"异常",e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		logger.info("开始添加商品类型");
		
		request.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=UTF-8");
		
		Type type = new Type();
		try{
			//商品种类名称
			String name = request.getParameter("name");
			type.setName(name);
			//商品种类图片
			String picture = request.getParameter("picture");
			type.setPicture(picture);
			//是否上架
			String isShowStr = request.getParameter("isShow");
			Boolean isShow = null;
			if("true".equals(isShowStr)){
				isShow = true;
			}else{
				isShow = false;
			}
			type.setIsShow(isShow);
			//添加时间
			type.setStartTime(new Date());
			type.setEditTime(new Date());
			typeDAO.insert(type);
		}catch(Exception e){
			logger.error("添加商品类型异常",e);
			throw new Exception("添加商品类型异常",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void onShow(Integer id) throws Exception{
		logger.info("上架商品类型ID="+id);
		try{
			Type type = new Type();
			type.setId(id);
			type.setIsShow(true);
			typeDAO.updateByPrimaryKeySelective(type);
			//相关的商品都上架
			ProductExample productExample = new ProductExample();
			productExample.createCriteria().andTypeIdEqualTo(id);
			List<Product> products = productDAO.selectByExample(productExample);
			for(Product product : products){
				product.setIsShow(0);
				productDAO.updateByPrimaryKeySelective(product);
			}
		}catch(Exception e){
			logger.error("上架商品类型ID="+id+"异常",e);
			throw new Exception("上架商品类型ID="+id+"异常",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void notShow(Integer id) throws Exception{
		logger.info("下架商品类型ID="+id);
		try{
			Type type = new Type();
			type.setId(id);
			type.setIsShow(false);
			typeDAO.updateByPrimaryKeySelective(type);
			//相关的商品都下架
			ProductExample productExample = new ProductExample();
			productExample.createCriteria().andTypeIdEqualTo(id);
			List<Product> products = productDAO.selectByExample(productExample);
			for(Product product : products){
				product.setIsShow(1);
				productDAO.updateByPrimaryKeySelective(product);
			}
		}catch(Exception e){
			logger.error("下架商品类型ID="+id+"异常",e);
			throw new Exception("下架商品类型ID="+id+"异常",e);
		}
	}
	
	public void toEdit(Model model, Integer id) throws Exception{
		logger.info("跳转至修改商品类型页面id="+id);
		try{
			Type type = typeDAO.selectByPrimaryKey(id);
			model.addAttribute("type", type);
		}catch(Exception e){
			logger.error("跳转至修改商品类型页面id="+id+"异常",e);
			throw new Exception("跳转至修改商品类型页面id="+id+"异常",e);
		}
	}
	
	public void edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		Integer id = Integer.parseInt(request.getParameter("id"));
		logger.info("开始编辑修改商品类型id=" +id);
		
		request.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=UTF-8");
		
		Type type = new Type();
		try{
			//商品种类ID
			type.setId(id);
			//商品种类名称
			String name = request.getParameter("name");
			type.setName(name);
			//商品种类图片
			String picture = request.getParameter("picture");
			type.setPicture(picture);
			//修改时间
			type.setEditTime(new Date());
			typeDAO.updateByPrimaryKeySelective(type);
		}catch(Exception e){
			logger.error("编辑修改商品类型异常id="+id,e);
			throw new Exception("编辑修改商品类型异常id="+id,e);
		}
	}
	
}
