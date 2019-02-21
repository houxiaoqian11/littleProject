package cn.guddqs.peakshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import cn.guddqs.peakshop.dao.MerchantInfoDAO;
import cn.guddqs.peakshop.entity.MerchantInfo;
import cn.guddqs.peakshop.entity.MerchantInfoExample;


@Service
public class MerchantService {
	
	@Autowired
	private MerchantInfoDAO merchantInfoDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public boolean checkHasInfo(Model model) throws Exception {
		logger.info("开始判断是否已有商户信息");
		try{
			MerchantInfoExample merchantInfoExample = new MerchantInfoExample();
			List<MerchantInfo> merchantInfos = merchantInfoDAO.selectByExample(merchantInfoExample);
			if(merchantInfos.size()>0){
				MerchantInfo merchantInfo = merchantInfos.get(0);
				model.addAttribute("merchantInfo", merchantInfo);
				return true;
			}
		}catch(Exception e){
			logger.error("判断是否已有商户信息异常",e);
			throw new Exception("判断是否已有商户信息异常",e);
		}
		return false;
	}

	public void add(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		logger.info("开始添加商家信息");
		try{
			//商家地址
			String address = request.getParameter("address");
			//商家电话
			String phone = request.getParameter("phone");
			//商家邮箱
			String email = request.getParameter("email");
			//商家微信
			String weixin = request.getParameter("weixin");
			//商家QQ
			String qq = request.getParameter("qq");
			//后台登录用户名
			String name = request.getParameter("name");
			//后台登录密码
			String password = request.getParameter("password");
			
			MerchantInfo merchantInfo = new MerchantInfo();
			merchantInfo.setAddress(address);
			merchantInfo.setPhone(phone);
			merchantInfo.setEmail(email);
			merchantInfo.setQq(qq);
			merchantInfo.setWeixin(weixin);
			merchantInfo.setName(name);
			merchantInfo.setPassword(password);
			merchantInfoDAO.insert(merchantInfo);
		}catch(Exception e){
			logger.error("添加商家信息异常",e);
			throw new Exception("添加商家信息异常",e);
		}
	}

	public void edit(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		logger.info("开始修改商家信息");
		try{
			//商家地址
			String address = request.getParameter("address");
			//商家电话
			String phone = request.getParameter("phone");
			//商家邮箱
			String email = request.getParameter("email");
			//商家微信
			String weixin = request.getParameter("weixin");
			//商家QQ
			String qq = request.getParameter("qq");
			//后台登录用户名
			String name = request.getParameter("name");
			//后台登录密码
			String password = request.getParameter("password");
			
			//删除旧数据
			MerchantInfoExample merchantInfoExample = new MerchantInfoExample();
			merchantInfoDAO.deleteByExample(merchantInfoExample);
			
			MerchantInfo merchantInfo = new MerchantInfo();
			merchantInfo.setAddress(address);
			merchantInfo.setPhone(phone);
			merchantInfo.setEmail(email);
			merchantInfo.setQq(qq);
			merchantInfo.setWeixin(weixin);
			merchantInfo.setName(name);
			merchantInfo.setPassword(password);
			merchantInfoDAO.insert(merchantInfo);
		}catch(Exception e){
			logger.error("修改商家信息异常",e);
			throw new Exception("修改商家信息异常",e);
		}
	}

	@SuppressWarnings("unchecked")
	public MerchantInfo get() {
		MerchantInfoExample merchantInfoExample = new MerchantInfoExample();
		List<MerchantInfo> merchantInfos = merchantInfoDAO.selectByExample(merchantInfoExample);
		if(merchantInfos.size()>0){
			return merchantInfos.get(0);
		}else{
			return null;
		}
	};
	
	
}
