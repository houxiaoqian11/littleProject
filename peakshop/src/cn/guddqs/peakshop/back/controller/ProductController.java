package cn.guddqs.peakshop.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.guddqs.peakshop.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	//获取商品列表
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo,String name,Integer typeId,Integer isShow,Model model) throws Exception{
		productService.list(pageNo, name, typeId, isShow, model);
		return "product/list";
	}
	
	//跳转至编辑商品页面
	@RequestMapping(value = "/product/toEdit.do")
	public String toEdit(Integer id,Model model) throws Exception{
		productService.toEdit(id, model);
		return "product/edit";
	}
	
	//编辑修改商品
	@RequestMapping(value = "/product/edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		productService.edit(request, resp);
		return "redirect:/product/list.do"; 
	}

	//商品下架
	@RequestMapping(value = "/product/notShow.do")
	public String notShow(Integer id) throws Exception{
		productService.notShow(id);
		return "redirect:/product/list.do"; 
	}
	
	//商品上架
	@RequestMapping(value = "/product/onShow.do")
	public String onShow(Integer id) throws Exception{
		productService.onShow(id);
		return "redirect:/product/list.do"; 
	}

	//跳转至添加商品页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model) throws Exception{
		productService.toAdd(model);
		return "product/add";
	}
	
	//跳转至添加商品详情页面
	@RequestMapping(value = "/product/toAddDetail.do")
	public String toAddDetail(Model model, Integer id) throws Exception{
		productService.toAddDetail(model, id);
		return "product/addDetail";
	}
	//完成添加商品详情
	@RequestMapping(value = "/product/finishAddDetail.do")
	public String finishAddDetail(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		productService.addProductDetail(request);
		return "redirect:/product/list.do"; 
	}

/*	//继续添加商品详情
	@RequestMapping(value = "/product/continueAddDetail.do")
	public String continueAddDetail(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		//商品ID
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		productService.addProductDetail(request);
		return "redirect:/product/toAddDetail.do?id="+productId+""; 
	}*/
	
	//跳转至修改商品详情页面
	@RequestMapping(value = "/product/toEditDetail.do")
	public String toEditDetail(Model model, Integer id) throws Exception{
		productService.toEditDetail(model, id);
		return "product/editDetail";
	}
	
	//修改商品详情
	@RequestMapping(value = "/product/editDetail.do")
	public String editDetail(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		productService.editDetail(request, resp);
		return "redirect:/product/toEditDetail.do?id="+productId; 
	}
	
	//添加商品
	@RequestMapping(value = "/product/add.do")
	public String add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		productService.addProduct(request, resp);
		return "redirect:/product/list.do"; 
	}
}
