<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-add</title>
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

//上传图片
function uploadPic(){
	var options = {
			url : "/peakshop/upload/uploadPic.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//把url 设置给 img
				$("#allUrl").attr("src",data.url);
				$("#path").val(data.path);
			}
	}
	
	//异步上传
	$("#jvForm").ajaxSubmit(options);
}
function sbjvForm(){
	var name = document.getElementById("productName");
	var prevprice = document.getElementById("prevprice");
	var type = document.getElementById("productType");
	var colors = document.getElementsByName("color");
	var colorFlag = false ;
	var isShows = document.getElementsByName("isShow");
	var showFlag = false ;
	var isHots = document.getElementsByName("isHot");
	var hotFlag = false ;
	var isNews = document.getElementsByName("isNew");
	var newFlag = false ;
	var isDiscounts = document.getElementsByName("isDiscount");
	var discountFlag = false ;
	var discount = document.getElementById("discount");
	var productPrice = document.getElementById("productPrice");
	var groupBuy = document.getElementById("groupBuy");
	var groupBuyFlag = false;
	var sizes = document.getElementsByName("size");
	var sizeFlag = false ;
	var path = document.getElementById("path");
	if(trim(name.value)==null || trim(name.value)==""){
	    alert("请输入商品名");
	    name.focus();
	    return false;
	}
	if(trim(prevprice.value)==null || trim(prevprice.value)==""){
	    alert("请输入商品价格");
	    prevprice.focus();
	    return false;
	}
	if(trim(type.value)==null || trim(type.value)==""){
	    alert("请选择商品种类");
	    type.focus();
	    return false;
	}
	for(var i=0;i<colors.length;i++){  
		if(colors[i].checked){  
			colorFlag = true ;  
            break ;  
         }  
     }  
     if(!colorFlag){  
        alert("请最少选择一种颜色！");  
        return false ;  
     }
     for(var i=0;i<isShows.length;i++){  
    	 if(isShows[i].checked){  
        	 showFlag = true ;  
               break ;  
          }  
      }  
      if(!showFlag){  
         alert("请选择商品是否上架");  
         return false ;  
      }
      for(var i=0;i<isNews.length;i++){  
          if(isNews[i].checked){  
         	 newFlag = true ;  
                break ;  
           }  
       }  
       if(!newFlag){  
          alert("请选择商品是否为新品");  
          return false ;  
       }
      for(var i=0;i<isHots.length;i++){  
     	 if(isHots[i].checked){  
         	 hotFlag = true ;  
             break ;  
           }  
       }  
       if(!hotFlag){  
          alert("请选择商品是否热卖");  
          return false ;  
       }
       for(var i=0;i<isDiscounts.length;i++){  
           if(isDiscounts[i].checked){  
          	 discountFlag = true ;  
             break ;  
            }  
        }  
        if(!discountFlag){  
           alert("请选择商品是否打折");  
           return false ;  
        }
        if(discount.checked){
        	if(trim(productPrice.value)==null || trim(productPrice.value)==""){
        	    alert("该商品为打折商品，请输入折扣价");
        	    productPrice.focus();
        	    return false;
        	}
        }
        
        for(var i=0;i<sizes.length;i++){  
    		if(sizes[i].checked){  
    			sizeFlag = true ;  
                break ;  
             }  
         }  
         if(!sizeFlag){  
            alert("请最少选择一种尺码！");  
            return false ;  
         }
        if(trim(path.value)==null || trim(path.value)==""){
    	    alert("请上传图片");
    	    path.focus();
    	    return false;
    	}
	return true;
}
function trim(str){ //删除左右两端的空格
　　     return str.replace(/(^\s*)|(\s*$)/g, "");
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 添加</div>
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
	<form id="jvForm" action="add.do" method="post" enctype="multipart/form-data" onsubmit="return sbjvForm();">
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody id="tab_1">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品名称:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="name" maxlength="100" size="100" id="productName"/></td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品价格:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="prevprice" id="prevprice" maxlength="100" size="100" onkeyup="value=value.replace(/[^\d\.]/g,'')"/>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品种类:</td><td width="80%" class="pn-fcontent">
						<select name="type" id="productType">
							<option value="">请选择品牌</option>
							<c:forEach items="${types }" var="type">
								<option value="${type.id }">${type.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品颜色:</td>
					<td width="80%" class="pn-fcontent">
						 <c:forEach items="${colors}" var="color">
						 	<div class='div-color'>
						 		<input type="checkbox" name="color" value="${color.id }"/>${color.name }<span>&nbsp;&nbsp;</span>
						 	</div>
						 </c:forEach>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						是否上架:</td><td width="80%" class="pn-fcontent">
							<input type="radio" value="0" name="isShow"/>是
							<input type="radio" value="1" name="isShow"/>否
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						是否新品:</td><td width="80%" class="pn-fcontent">
							<input type="radio" value="0" name="isNew"/>是
							<input type="radio" value="1" name="isNew"/>否
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						是否热卖:</td><td width="80%" class="pn-fcontent">
							<input type="radio" value="0" name="isHot"/>是
							<input type="radio" value="1" name="isHot"/>否
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						是否打折:</td><td width="80%" class="pn-fcontent">
							<input type="radio" value="0" name="isDiscount" id="discount"/>是
							<input type="radio" value="1" name="isDiscount"/>否
							折扣价：<input type="text" name="price" onkeyup="value=value.replace(/[^\d\.]/g,'')" id="productPrice"/>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						是否团购:</td><td width="80%" class="pn-fcontent">
							<input type="radio" value="0" name="isGroupBuy" id="groupBuy"/>是
							<input type="radio" value="1" name="isGroupBuy"/>否
							团购价：<input type="text" name="groupPrice" onkeyup="value=value.replace(/[^\d\.]/g,'')" id="groupPrice"/>
							参团数量：<input type="text" name="groupNum" onkeyup="value=value.replace(/[^\d\.]/g,'')" id="groupNum"/>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品尺码:</td><td width="80%" class="pn-fcontent">
							<c:forEach items="${sizes}" var="size">
						 	<div class='div-color'>
						 		<input type="checkbox" name="size" value="${size.id }"/>${size.name }<span>&nbsp;&nbsp;</span>
						 	</div>
						 </c:forEach>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
					<span class="pn-frequired">*</span>
					上传商品图片</td>
					<td width="80%" class="pn-fcontent">
					    <div class="div-image">
					    	<img width="100" height="100" id="allUrl"/>
							<input type="hidden" id="path" name="picture"/>
							<input type="file" onchange="uploadPic()" name="file"/>
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