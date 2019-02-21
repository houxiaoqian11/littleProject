package cn.guddqs.peakshop.back.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.guddqs.peakshop.service.VideoService;

@Controller
public class ShopVideoControll {

	@Autowired
	private VideoService videoService;
	
	//跳转至添加门店视频
	@RequestMapping(value = "/shopVideo/toAdd.do")
	public String toAdd(Model model) throws Exception{
//		String path = videoService.getVideoPath();
//		if(path != null){
//			model.addAttribute("path", path);
//		}
		return "shopVideo/add";
	}
	
	@RequestMapping(value = "/shopVideo/add.do")
	public String add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		videoService.add(request, resp);
		return "redirect:/shopVideo/toAdd.do"; 
	}
	
}
