<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>peakshop-right</title>
<link rel="stylesheet" href="/peakshop/res/css/style.css" />
<script src="/peakshop/res/js/jquery.js"></script>
<script src="/peakshop/res/js/com.js"></script>
</head>
<style>
.logo {
	position: absolute;
	left: 431px;
	top: 618px;
	width: 270px;
	height: 23px;
	border: 0px;
	color: #2ec0f6;
	font-size: 20px;
}

.weixin {
	position: absolute;
	left: 480px;
	top: 640px;
	width: 270px;
	height: 23px;
	border: 0px;
	color: #2ec0f6;
	font-size: 15px;
}
</style>
<body>
	<div class="bar">
		<div class="bar_w">
			<p class="l">
			</p>
			<ul class="r uls">
				<li class="dev">您好,欢迎来到PEAK运动管理平台！</li>
				<li class="dev"><a href="#" title="在线客服">在线客服</a></li>
				<li class="dev after"><a href="#" title="English">English</a></li>
			</ul>
		</div>
	</div>
	<div class="w loc">
		<div class="h-title" id="logo">
			<div class="h-logo l">
				<img src="/peakshop/res/img/pic/logo.png" />
			</div>
			<div class="l"
				style="margin: 13px 10px; font-size: 20px; font-family: '微软雅黑'; letter-spacing: 2px">请登录</div>
		</div>
	</div>
	<div class="sign">
		<div class="l ad420x205">
			<a href="#" title="title"><img
				src="/peakshop/res/img/pic/ppp0.jpg" width="400" height="350" /></a>
		</div>
		<div class="r">
			<h2 title="登录PEAK运动管理平台">登录PEAK运动管理平台</h2>
			<form id="jvForm" action="/peakshop/login/login.do" method="post">
				<input type="hidden" name="returnUrl" value="${param.returnUrl}" />
				<ul class="uls form">
					<li id="errorName" class="errorTip"
						<c:if test="${empty error }">style="display:none"</c:if>>${error}</li>
					<li><label for="username">用户名：</label> <span class="bg_text">
							<input type="text" id="username" name="username" maxLength="100" />
					</span></li>
					<li><label for="password">密 码：</label> <span class="bg_text">
							<input type="password" id="password" name="password"
							maxLength="32" />
					</span></li>
					<li><label for="captcha">验证码：</label> <span
						class="bg_text small"> <input type="text" id="captcha"
							name="code" maxLength="7" />
					</span> <img src="/peakshop/login/getCodeImage.do"
						onclick="this.src='/peakshop/login/getCodeImage.do?d='+new Date()"
						class="code" alt="换一张" /><a href="javascript:void(0);"
						onclick="$('.code').attr('src','/peakshop/login/getCodeImage.do?d='+new Date())"
						title="换一张">换一张</a></li>
					<li><label for="">&nbsp;</label><input type="submit"
						value="登 录" class="hand btn66x23" /><a href="#" title="忘记密码？">忘记密码？</a></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="weixin">微信：133 0123 5050</div>
	<div class="logo">北京智联网络提供技术支持</div>
</body>
</html>