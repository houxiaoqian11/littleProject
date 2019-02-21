package cn.guddqs.peakshop.back.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.ColorDAO;
import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.SizeDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.dao.ex.StoreDAOEx;
import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.ColorExample;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.SizeExample;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;
import cn.guddqs.peakshop.service.StoreService;
import cn.guddqs.peakshop.util.Pagination;
import cn.guddqs.peakshop.view.entity.ViewStore;

@Controller
public class StoreControll {

	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private StoreDAOEx storeDAOEx;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ColorDAO colorDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private SizeDAO sizeDAO;

	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/store/getlist")
	public List<Store> getList() {
		List<Store> viewStores = new ArrayList<Store>();
		try{
			StoreExample example = new StoreExample();
			List<Store> stores = storeDAO.selectByExample(example);
			for(Store store : stores){
				try{
					//先判断商品是否下架
					Integer productId = store.getProductId();
					Product product = productDAO.selectByPrimaryKey(productId);
					if(product.getIsShow() != 0){
						continue;
					}
					ViewStore viewStore = storeService.getViewStore(store);
					viewStores.add(viewStore);
				}catch(Exception e){
					logger.error("根据仓储ID="+store.getId()+"获取视图仓储异常",e);
				}
			}
		}catch(Exception e){
			logger.error("获取仓储失败", e);
		}
		return viewStores;
	}
	
	//分页查询
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/store/list.do")
	public String list(Integer pageNo,Model model, Integer productId, Integer colorId, Integer sizeId, String name){
		//查询尺码结果集
		SizeExample sizeExample = new SizeExample();
		List<Size> sizes = sizeDAO.selectByExample(sizeExample);
		model.addAttribute("sizes", sizes);
		//查询颜色结果集
		ColorExample colorExample = new ColorExample();
		List<Color> colors = colorDAO.selectByExample(colorExample);
		model.addAttribute("colors", colors);
		//获取所有商品类型总条数
		Map<String,Object> map = new HashMap<String,Object>();
		if(productId != null){
			map.put("productId", productId);
			model.addAttribute("productId", productId);
		}
		if(colorId != null){
			map.put("colorId", colorId);
			model.addAttribute("colorId", colorId);
		}
		if(sizeId != null){
			map.put("sizeId", sizeId);
			model.addAttribute("sizeId", sizeId);
		}
		if(!StringUtils.isEmpty(name)){
			map.put("name", name);
			model.addAttribute("name", name);
		}
		int totalCount = storeDAOEx.countByMap(map);
		
		Pagination pagination = new Pagination(Pagination.cpn(pageNo),10,totalCount);
		//分页查询仓储信息
		
		List<Store> stores = storeDAOEx.getStoreListEx(map, pagination);
		List<ViewStore> viewStores = new ArrayList<ViewStore>();
		for(Store store : stores){
			try{
				ViewStore viewStore = storeService.getViewStore(store);
				viewStores.add(viewStore);
			}catch(Exception e){
				logger.error("根据仓储ID="+store.getId()+"获取视图仓储异常",e);
			}
		}
		pagination.setList(viewStores);
		StringBuilder params = new StringBuilder();
		//页面分页展示
		String url = "/peakshop/store/list.do";
		pagination.pageView(url, params.toString());
		model.addAttribute("pagination", pagination);
		return "store/list";
	}
	
	//编辑
	@RequestMapping(value = "/store/toEdit.do")
	public String toEdit(Integer id,Model model,String picture) throws Exception{
		//查询仓储记录
		Store store = storeDAO.selectByPrimaryKey(id);
		ViewStore viewStore = storeService.getViewStore(store);
		model.addAttribute("viewStore", viewStore);
		model.addAttribute("picture", picture);
		return "store/edit";
	}
	
	@RequestMapping(value = "/store/edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		//仓储ID
		Integer id = Integer.parseInt(request.getParameter("id"));
		//库存数量
		Integer storeNum = Integer.parseInt(request.getParameter("store"));
		Store store = new Store();
		store.setId(id);
		store.setStore(storeNum);
		store.setEditTime(new Date());
		storeDAO.updateByPrimaryKeySelective(store);
		return "redirect:/store/list.do"; 
	}
	
	//根据颜色、尺码、和商品Id获取库存
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/store/getStore.do")
	public Store getStore(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		logger.info("查询库存数");
		Store store = new Store();
		try{
			//商品Id
			Integer productId = Integer.parseInt(request.getParameter("productId"));
			//颜色Id
			Integer colorId = Integer.parseInt(request.getParameter("colorId"));
			//尺寸Id
			Integer sizeId = Integer.parseInt(request.getParameter("sizeId"));
			StoreExample example = new StoreExample();
			example.createCriteria().andColorIdEqualTo(colorId).andProductIdEqualTo(productId).andSizeIdEqualTo(sizeId);
			List<Store> stores = storeDAO.selectByExample(example);
			if(stores.size() == 1){
				store = stores.get(0);
			}
		}catch(Exception e){
			logger.error("查询库存出现异常",e);
		}
		return store;
		
	}
	
	
}
