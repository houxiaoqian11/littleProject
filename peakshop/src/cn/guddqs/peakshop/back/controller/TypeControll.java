package cn.guddqs.peakshop.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.guddqs.peakshop.service.TypeService;

@Controller
public class TypeControll {

	@Autowired
	private TypeService typeService;

	//查询商品类型列表
	@RequestMapping(value = "/type/list.do")
	public String list(Integer pageNo,Model model, Boolean isShow) throws Exception{
		typeService.list(pageNo, model, isShow);
		return "type/list";
	}
	
	//跳转至添加商品类型页面
	@RequestMapping(value = "/type/toAdd.do")
	public String toAdd(Model model){
		return "type/add";
	}
	
	//添加商品类型
	@RequestMapping(value = "/type/add.do")
	public String add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		typeService.add(request, resp);
		return "redirect:/type/list.do"; 
	}
	
	//跳转至修改商品类型页面
	@RequestMapping(value = "/type/toEdit.do")
	public String toEdit(Model model, Integer id) throws Exception{
		typeService.toEdit(model, id);
		return "type/edit";
	}
	
	//跳转至修改商品类型页面
	@RequestMapping(value = "/type/edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		typeService.edit(request, resp);
		return "redirect:/type/list.do"; 
	}
	
	//商品类型上架
	@RequestMapping(value = "/type/onShow.do")
	public String onShow(Integer id) throws Exception{
		typeService.onShow(id);
		return "redirect:/type/list.do"; 
	}
	
	//商品类型下架
	@RequestMapping(value = "/type/notShow.do")
	public String notShow(Integer id) throws Exception{
		typeService.notShow(id);
		return "redirect:/type/list.do"; 
	}
	
}
