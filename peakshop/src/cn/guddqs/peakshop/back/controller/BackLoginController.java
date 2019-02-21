package cn.guddqs.peakshop.back.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.guddqs.peakshop.dao.MerchantInfoDAO;
import cn.guddqs.peakshop.entity.MerchantInfo;
import cn.guddqs.peakshop.entity.MerchantInfoExample;

/**
 * 登陆
 * 
 * @author hxq
 *
 */
@Controller
public class BackLoginController {
	
	@Autowired
	private MerchantInfoDAO merchantInfoDAO;
	
	// 去登陆页面
	@RequestMapping(value = "/login/login.do",method=RequestMethod.GET)
	public String login() {

		return "buyer/login";
	}
	
	// 获取登录信息
	@RequestMapping(value = "/login/get.do",method=RequestMethod.POST)
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Object login = session.getAttribute("login");
		if(login != null){
			response.getWriter().write(login.toString());
		}else{
			response.getWriter().write("false");
		}
	}
	//提交登陆表单
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login/login.do",method=RequestMethod.POST)
	public String login(String username,String password,String code,String returnUrl
			,HttpServletRequest request,HttpServletResponse response,Model model) {
		
		//首次登录无需用户名密码
		MerchantInfoExample merchantInfoExample = new MerchantInfoExample();
		List<MerchantInfo> merchantInfos = merchantInfoDAO.selectByExample(merchantInfoExample);
		
		//1：验证码不能为空
		HttpSession session = request.getSession();
		if(null != code && !"".equals(code)){
			//2:验证码必须正确
			String c = (String) session.getAttribute("code");
			if(c.equalsIgnoreCase(code)){
				if(merchantInfos.size() ==0){
					session.setAttribute("login", true);
					return "success";
				}
				//3:用户名不能为空
				if(null != username && !"".equals(username)){
					//4:密码不能为空
					if(null != password && !"".equals(password)){
						//5:用户必须正确
						MerchantInfo merchantInfo = merchantInfos.get(0);
						if(username.equals(merchantInfo.getName())){
							//6:密码必须正确
							if(password.equals(merchantInfo.getPassword())){
								//7:设置登录标识
								session.setAttribute("login", true);
								//8:跳转回之前访问页面
								return "success";
								
							}else{
								model.addAttribute("error", "密码错误");
								return "right";
							}
							
						}else{
							model.addAttribute("error", "用户名错误");
							return "right";
						}
					}else{
						model.addAttribute("error", "密码不能为空");
						return "right";
					}
					
				}else{
					model.addAttribute("error", "用户名不能为空");
					return "right";
				}
				
			}else{
				model.addAttribute("error", "验证码必须正确");
				return "right";
			}
			
		}else{
			model.addAttribute("error", "验证码不能为空");
			return "right";
		}
	}
	
	//加密 
	public String encodePassword(String passowrd){
		String algorithm = "MD5";
		
	//	passowrd = "sdfsafwefe" + passowrd + "fdhjghgkrth";
		
	//	passowrd = "sfasdfsafg123456weterytyrtyjrjfgjrfhhr";
		
		char[] encodeHex = null;
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			byte[] digest = instance.digest(passowrd.getBytes());
			//十六进制加密
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encodeHex);
	}
	
	public static void main(String[] args) {
		BackLoginController l = new BackLoginController();
		String e = l.encodePassword("123456");
		System.out.println(e);
	}

	// 验证码生成
	@RequestMapping(value = "/login/getCodeImage.do")
	public void getCodeImage(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("#######################生成数字和字母的验证码#######################");
		HttpSession session = request.getSession();
		BufferedImage img = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();

		Random r = new Random();

		Color c = new Color(200, 150, 255);

		g.setColor(c);

		// 填充整个图片的颜色

		g.fillRect(0, 0, 68, 22);

		// 向图片中输出数字和字母

		StringBuffer sb = new StringBuffer();

		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		int index, len = ch.length;

		for (int i = 0; i < 4; i++) {

			index = r.nextInt(len);

			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt

			(255)));

			g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
			// 输出的 字体和大小

			g.drawString("" + ch[index], (i * 15) + 3, 18);
			// 写什么数字，在图片 的什么位置画

			sb.append(ch[index]);

		}
		
		// 把上面生成的验证码放到Session域中
//		sessionProvider.setAttributeForCode(RequestUtils.getCSESSIONID(response, request), sb.toString());
		session.setAttribute("code", sb.toString());
		try {
			ImageIO.write(img, "JPG", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
