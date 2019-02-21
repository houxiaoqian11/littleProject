package cn.guddqs.peakshop.front.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

import cn.guddqs.peakshop.dao.UserInfoDAO;
import cn.guddqs.peakshop.dao.ex.UserInfoDAOEx;
import cn.guddqs.peakshop.entity.UserInfo;
import cn.guddqs.peakshop.entity.UserInfoExample;
import cn.guddqs.peakshop.util.HttpRespons;
import cn.guddqs.peakshop.util.HttpUtil;

@Controller
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private UserInfoDAOEx userInfoDAOEx;
	
	@Autowired
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/weixin/login")
	public Map<String, Object> login(String code, String nickName) throws Exception{
		logger.info("code="+code+"::nickName="+nickName);
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//根据code换取session_key
			//appid
			String appid = ppc.getMessage("appid");
			//secret
			String secret = ppc.getMessage("secret");
			//grant_type
			String grant_type = "authorization_code";
			Map<String, String> params = new HashMap<String, String>();
			params.put("appid", appid);
			params.put("secret", secret);
			params.put("js_code", code);
			params.put("grant_type", grant_type);
			
			HttpUtil httpUtil = new HttpUtil();
			HttpRespons httpResponse = httpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
			String content = httpResponse.getContent();
			JSONObject json = new JSONObject(content);
			String openId = json.getString("openid");
			logger.info("openid="+openId);
			map.put("openid", openId);
			String sessionKey = json.getString("session_key");
			map.put("session_key", sessionKey);
			//先判断userInfo表中openId是否存在
			UserInfoExample userInfoExample = new UserInfoExample();
			userInfoExample.createCriteria().andOpenidEqualTo(openId);
			List<UserInfo> userInfos = userInfoDAO.selectByExample(userInfoExample);
			if(userInfos.size()>0){
				Integer userId = userInfos.get(0).getId();
				map.put("userId", userId);
			}else{
				//保存用户信息
				UserInfo info = new UserInfo();
				info.setOpenid(openId);
				info.setJifen(0.f);
				info.setStartTime(new Date());
				info.setEndTime(new Date());
				info.setNickName(nickName);
				//TODO
				//userInfo 表加上nickName
				Integer userId = userInfoDAOEx.insertReturnKey(info);
				map.put("userId", userId);
			}
		}catch(Exception e){
			logger.error("获取用户信息失败",e);
		}
		return map;
		
	}
	
	public static void main(String[] args) throws Exception {
//		String appid = "wx25ed57189694bb66";
		String appid = "wxa3d00a2bd95be1a0";
//		String appid = "wx842855ca9b50753a";
		//secret
//		String secret = "ee184a89e8940ae429496645873d2b28";
		String secret = "76190f9e113b5e6e0165047352a07979";
//		String secret = "4a6199a7e075f4ceec595ef1093ffe51";
		//grant_type
		String grant_type = "client_credential";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("secret", secret);
//		params.put("js_code", "061MtM3e1JaZrt0n2z0e1zFv3e1MtM3S");
		params.put("grant_type", grant_type);
		HttpUtil httpUtil = new HttpUtil();
		HttpRespons httpResponse = httpUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
		String content = httpResponse.getContent();
		System.out.println(content);
		JSONObject json = new JSONObject(content);
		String openId = json.getString("openid");
		System.out.println(openId);
	}

	public static void main(HttpServletRequest req, HttpServletResponse resp) throws Exception{
	
	}

}
