<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-add</title>
<style type="">
.div-image{
	float:left
}
.h2_ch a:hover, .h2_ch a.here {
    color: #FFF;
    font-weight: bold;
    background-position: 0px -32px;
}
.h2_ch a {
    float: left;
    height: 32px;
    margin-right: -1px;
    padding: 0px 16px;
    line-height: 32px;
    font-size: 14px;
    font-weight: normal;
    border: 1px solid #C5C5C5;
    background: url('/peakshop/res/itcast/img/admin/bg_ch.gif') repeat-x scroll 0% 0% transparent;
}
a {
    color: #06C;
    text-decoration: none;
}
</style>
<script type="text/javascript">
$(function(){
	var tObj;//当前被选中a标签是谁
	$("#tabs a").each(function(){
		if($(this).attr("class").indexOf("here") == 0){tObj = $(this)}
		$(this).click(function(){
			var c = $(this).attr("class");
			if(c.indexOf("here") == 0){return;}
			var ref = $(this).attr("ref");
			var ref_t = tObj.attr("ref");
			tObj.attr("class","nor");
			$(this).attr("class","here");
			$(ref_t).hide();
			$(ref).show();
			tObj = $(this);
			if(ref == '#tab_2'){
				var fck = new FCKeditor("productdesc");
				fck.BasePath = "/res/fckeditor/";
				fck.Height = 400 ;
				fck.Config["ImageUploadURL"] = "/upload/uploadFck.do";
				fck.ReplaceTextarea();
			}
		});
	});
});

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

function sbjvForm(){
	var name = document.getElementById("loginName");
	var password = document.getElementById("loginPassword");
	
	if(trim(name.value)==null || trim(name.value)==""){
	    alert("请输入后台登录用户名");
	    name.focus();
	    return false;
	}
	
	if(trim(password.value)==null || trim(password.value)==""){
	    alert("请输入后台登录密码");
	    password.focus();
	    return false;
	}
}

</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商家信息 - 添加</div>
	<div class="clear"></div>
</div>
<h2 class="h2_ch">
<span id="tabs">
<a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
</span></h2>
<div class="body-box" style="float:right">
	<form id="jvForm" action="add.do" method="post" enctype="multipart/form-data" onsubmit="return sbjvForm();">
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody id="tab_1">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商家地址:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="address" maxlength="100" size="100"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商家电话:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="phone" maxlength="100" size="100"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商家微信:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="weixin" maxlength="100" size="100"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商家邮箱:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="email" maxlength="100" size="100"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商家QQ:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="qq" maxlength="100" size="100"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						后台登录用户名:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="name" maxlength="100" size="100" id="loginName"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						后台登录密码:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="password" maxlength="100" size="100" id="loginPassword"/></td>
				</tr>
				
			</tbody>
			<tbody id="tab_2" style="display: none">
				<tr>
					<td >
						<textarea rows="10" cols="10" id="productdesc" name="description"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody id="tab_3" style="display: none">
				<tr>
					<td >
						<textarea rows="15" cols="136" id="productList" name="packageList"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<td class="pn-fbutton" colspan="2">
						<input type="submit" class="submit" value="提交"/> &nbsp; <input type="reset" class="reset" value="重置"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
</body>
</html>