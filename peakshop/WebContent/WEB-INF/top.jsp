<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>BABASPORT Administrator's Control Panel - powered by
	babasport</title>
<link href="/peakshop/res/itcast/css/admin.css" rel="stylesheet"
	type="text/css" />
<link href="/peakshop/res/common/css/theme.css" rel="stylesheet"
	type="text/css" />
<link href="/peakshop/res/common/css/jquery.validate.css"
	rel="stylesheet" type="text/css" />
<link href="/peakshop/res/common/css/jquery.treeview.css"
	rel="stylesheet" type="text/css" />
<link href="/peakshop/res/common/css/jquery.ui.css" rel="stylesheet"
	type="text/css" />

<script type="text/javascript"
	src="/peakshop/res/fckeditor/fckeditor.js"></script>
<script src="/peakshop/res/common/js/jquery.js" type="text/javascript"></script>
<script src="/peakshop/res/common/js/jquery.ext.js"
	type="text/javascript"></script>
<script src="/peakshop/res/common/js/jquery.form.js"
	type="text/javascript"></script>
<script src="/peakshop/res/common/js/itcast.js" type="text/javascript"></script>
<script src="/peakshop/res/itcast/js/admin.js" type="text/javascript"></script>
<style type="text/css">
* {
	margin: 0;
	padding: 0
}

a:focus {
	outline: none;
}

html {
	height: 100%;
	overflow: hidden;
}

body {
	height: 100%;
}

#top {
	background-color: #1d63c6;
	height: 69px;
	width: 100%;
}

.logo {
	width: 215px;
	height: 69px;
}

.topbg {
	background: url(/peakshop/res/itcast/img/admin/top-tbg.png) no-repeat;
	height: 38px;
}

.login-welcome {
	padding-left: 20px;
	color: #fff;
	font-size: 12px;
	background: url(/peakshop/res/itcast/img/admin/topbg.gif) no-repeat;
}

.login-welcome a:link, .login-welcome a:visited {
	color: #fff;
	text-decoration: none;
}

#welcome {
	color: #FFFFFF;
	padding: 0 30px 0 5px;
}

#logout {
	color: #FFFFFF;
	padding-left: 5px;
}

.nav {
	height: 31px;
	overflow: hidden;
}

.nav-menu {
	background: url(/peakshop/res/itcast/img/admin/bg.png) repeat-x;
	height: 31px;
	list-style: none;
	padding-left: 20px;
	font-size: 14px;
}

.nav .current {
	background: url(/peakshop/res/itcast/img/admin/navcurrbg.gif) no-repeat
		0px 2px;
	color: #fff;
	width: 72px;
	text-align: center;
}

.nav .current a {
	color: #fff;
}

.nav-menu li {
	height: 31px;
	text-align: center;
	line-height: 31px;
	float: left;
}

.nav-menu li a {
	color: #2b2b2b;
	font-weight: bold;
}

.nav-menu li.sep {
	background: url(/peakshop/res/itcast/img/admin/step.png) no-repeat;
	width: 2px;
	height: 31px;
	margin: 0px 5px;
}

.nav .normal {
	width: 72px;
	text-align: center;
}

.top-bottom {
	width: 100%;
	background: url(/peakshop/res/itcast/img/admin/bg.png) repeat-x 0px
		-34px;
	height: 3px;
}

.undis {
	display: none;
}

.dis {
	display: block;
}
</style>

<script type="text/javascript">
	function HoverLi(id) {
		var login = false;
		$.ajax({
			type : "POST",
			url : "/peakshop/login/get.do",
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(data) {
				login = data;
				if (login) {
					$("li[id^='tb_']").each(function() {
						$(this).removeClass("current");
						$(this).addClass("normal");
					});
					$("#tb_" + id).removeClass("normal");
					$("#tb_" + id).addClass("current");
					if (id == 12) {
					/* 	document.getElementById('a12').target = "mainFrame";
						document.getElementById('a12').href = "frame/product_main.do";  */
						window.open("frame/product_main.do","mainFrame")
					}
					if (id == 11) {
						/* document.getElementById('a11').target = "mainFrame";
						document.getElementById('a11').href = "main.do";  */
						window.open("main.do","mainFrame")
					}
					
				} else {
					alert('请先登录');
					$("li[id^='tb_']").each(function() {
						$(this).removeClass("current");
						$(this).addClass("normal");
					});
					$("#tb_" + 11).removeClass("normal");
					$("#tb_" + 11).addClass("current");
				/* 	document.getElementById('a11').target = "mainFrame";
					document.getElementById('a11').href = "main.do"; */
					window.open("main.do","mainFrame")
				}
			},
			error : function(error) {
			}
		});
	}
</script>
</head>
<body>
	<div id="top">
		<div class="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="215"><div class="logo">
							<img src="/peakshop/res/itcast/img/admin/logo.jpg" width="215"
								height="69" />
						</div></td>
					<td valign="top">
						<div class="topbg">
							<div class="login-welcome">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="420" height="38"><img
											src="/peakshop/res/itcast/img/admin/welconlogin-icon.png" /><span
											id="welcome">欢迎登陆PEAK专卖管理平台</span></td>
									</tr>
								</table>
							</div>
							<div class="nav">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td
											style="background-image: url('/peakshop/res/itcast/img/admin/nav-left.png')"
											width="14" height="31"></td>
										<ul class="nav-menu">
											<li class="current" id="tb_11"><a href="#" onclick="HoverLi(11)" id='a11'>首页</a></li>
											<li class="sep"></li>
											<li class="normal" id="tb_12"><a href="#" onclick="HoverLi(12)" id='a12' >商品</a></li>
										</ul>
										</td>

									</tr>
								</table>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="top-bottom"></div>
</body>
</html>