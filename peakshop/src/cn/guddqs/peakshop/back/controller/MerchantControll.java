package cn.guddqs.peakshop.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.entity.MerchantInfo;
import cn.guddqs.peakshop.service.MerchantService;

@Controller
public class MerchantControll {

	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/merchant/show.do")
	public String list(Model model) throws Exception{
		//判断是否有商家信息，没有跳转至添加页面，有则跳转至修改页面
		if(merchantService.checkHasInfo(model)){
			return "merchant/edit";
		}else{
			return "merchant/add";
		}
	}
	
	//添加商家信息
	@RequestMapping(value = "/merchant/add.do")
	public String add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		merchantService.add(request, resp);
		return "redirect:/merchant/show.do"; 
	}
	
	//修改商家信息
	@RequestMapping(value = "/merchant/edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		merchantService.edit(request, resp);
		return "redirect:/merchant/show.do"; 
	}
	
	//获取商家信息
	@ResponseBody
	@RequestMapping("/merchant/get")
	public MerchantInfo get() throws Exception {
		return merchantService.get();
	}
}
