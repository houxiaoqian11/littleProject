<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>peakshop-addDetail</title>
<style type="">
.div-image{
	float:left;
	margin-left:9px
}
.div-color{
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


//上传图片1
function uploadPic1(){
	var options = {
			url : "/peakshop/upload/batchUploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl1").attr("src",data.url);
				$("#path1").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片2
function uploadPic2(){
	var options = {
			url : "/peakshop/upload/batchUploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl2").attr("src",data.url);
				$("#path2").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片3
function uploadPic3(){
	var options = {
			url : "/peakshop/upload/batchUploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl3").attr("src",data.url);
				$("#path3").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片4
function uploadPic4(){
	var options = {
			url : "/peakshop/upload/batchUploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl4").attr("src",data.url);
				$("#path4").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片5
function uploadPic5(){
	var options = {
			url : "/peakshop/upload/batchUploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl5").attr("src",data.url);
				$("#path5").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片6
function uploadPic6(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl6").attr("src",data.url);
				$("#path6").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片7
function uploadPic7(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl7").attr("src",data.url);
				$("#path7").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片8
function uploadPic8(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl8").attr("src",data.url);
				$("#path8").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片9
function uploadPic9(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl9").attr("src",data.url);
				$("#path9").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}

//上传图片10
function uploadPic10(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl10").attr("src",data.url);
				$("#path10").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 添加商品详情</div>
	<form class="ropt">
		<input type="submit" onclick="this.form.action='list.do';" value="返回列表" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<h2 class="h2_ch">
<span id="tabs">
<a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
</span></h2>
<div class="body-box" style="float:right">
	<form id="jvForm" action="editDetail.do" method="post" enctype="multipart/form-data" name="jvForm" onsubmit="return sbjvForm();">
		<input type="text" name="productId" value="${product.id}" style="display:none"/>
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody id="tab_1">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品名称:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="name" maxlength="100" size="100" value="${product.name} " disabled="disabled"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">上传商品详情图片</td>
					<td width="80%" class="pn-fcontent">
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl1" src="${details[0].picture }"/>
							<input type="hidden" id="path1" name="picture1" value="${details[0].picture }"/>
							<input type="text" name="deatilId0" value="${details[0].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic1()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl2" src="${details[1].picture }"/>
							<input type="hidden" id="path2" name="picture2" value="${details[1].picture }"/>
							<input type="text" name="deatilId1" value="${details[1].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic2()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl3" src="${details[2].picture }"/>
							<input type="hidden" id="path3" name="picture3" value="${details[2].picture }"/>
							<input type="text" name="deatilId2" value="${details[2].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic3()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl4" src="${details[3].picture }"/>
							<input type="hidden" id="path4" name="picture4" value="${details[3].picture }"/>
							<input type="text" name="deatilId3" value="${details[3].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic4()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl5" src="${details[4].picture }"/>
							<input type="hidden" id="path5" name="picture5" value="${details[4].picture }"/>
							<input type="text" name="deatilId4" value="${details[4].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic5()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl6" src="${details[5].picture }"/>
							<input type="hidden" id="path6" name="picture6" value="${details[5].picture }"/>
							<input type="text" name="deatilId5" value="${details[5].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic6()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl7" src="${details[6].picture }"/>
							<input type="hidden" id="path6" name="picture7" value="${details[6].picture }"/>
							<input type="text" name="deatilId6" value="${details[6].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic7()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl8" src="${details[7].picture }"/>
							<input type="hidden" id="path6" name="picture8" value="${details[7].picture }"/>
							<input type="text" name="deatilId7" value="${details[7].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic8()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl9" src="${details[8].picture }"/>
							<input type="hidden" id="path6" name="picture9" value="${details[8].picture }"/>
							<input type="text" name="deatilId8" value="${details[8].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic9()" name="file"/>
					    </div>
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl10" src="${details[9].picture }"/>
							<input type="hidden" id="path6" name="picture10" value="${details[9].picture }"/>
							<input type="text" name="deatilId9" value="${details[9].id }" style="display:none"/> 
							<input type="file" onchange="uploadPic10()" name="file"/>
					    </div>
					</td>
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
					<td class="pn-fbutton" colspan="4">
						<input type="submit" class="submit" value="提交"/> &nbsp;
						<input type="reset" class="reset" value="重置"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
</body>
</html>