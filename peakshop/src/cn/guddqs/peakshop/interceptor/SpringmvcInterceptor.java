package cn.guddqs.peakshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

/**
 * 自定义拦截器      前端控制器  处理器映射器  处理器适配器   处理器 Controller（handler)
 * @author lx
 *
 */
public class SpringmvcInterceptor implements  HandlerInterceptor{

	@Autowired
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	//Handler之前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String allowPath = ppc.getMessage("allowPath");
		String username = (String) request.getSession().getAttribute("username");
		if(null != username){
			//已经登陆
			request.setAttribute("isLogin", true);
		}else{
			//未登陆
			//未登陆
			//跳转到单点登陆系统    直接下一个页面
			response.sendRedirect(allowPath);
			//不放行
			return false;
		}
		return true;
	}
	//Handler之后
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//页面渲染后
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
