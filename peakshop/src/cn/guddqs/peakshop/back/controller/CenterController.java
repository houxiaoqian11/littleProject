package cn.guddqs.peakshop.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CenterController {

	//入口
	@RequestMapping(value = "/index.do")
	public String index(Model model){
		return "index";
	}
	
	@RequestMapping(value = "/top.do")
	public String top(Model model){
		return "top";
	}
	
	//身体
	@RequestMapping(value = "/main.do")
	public String main(Model model){
		return "main";
	}
	//左
	@RequestMapping(value = "/left.do")
	public String left(Model model){
		return "left";
	}
	//右
	@RequestMapping(value = "/right.do")
	public String right(Model model){
		return "right";
	}
}
