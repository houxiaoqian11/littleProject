<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>peakshop-list</title>
<script type="text/javascript">
function getTableForm() {
	return document.getElementById('tableForm');
}
//上架
function isShow() {
	if(Pn.checkedCount('ids')<=0) {
		alert("请至少选择一个!");
		return;
	}
	if(!confirm("确定上架吗?")) {
		return;
	}
	var f = getTableForm();
	f.action="/product/isShow.do";
	f.submit();
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加" onclick="javascript:window.location.href='toAdd.do'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="/peakshop/product/list.do" method="post" style="padding-top:5px;">
名称: <input type="text" name="name" value="${name }"/>
	<select name="typeId">
		<option value="">请选择品牌</option>
		<c:forEach items="${types }" var="type">
			<option value="${type.id }" <c:if test="${typeId == type.id}">selected="selected"</c:if>>${type.name }</option>
		</c:forEach>
	</select>
	<select name="isShow">
		<option value="0" <c:if test="${isShow eq 0}">selected="selected"</c:if>>上架</option>
		<option value="1" <c:if test="${isShow eq 1}">selected="selected"</c:if>>下架</option>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" width="100%" border="0" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th width="6%">商品编号</th>
			<th>商品名称</th>
			<th width="5%">图片</th>
			<th width="5%">价格</th>
			<th width="3%">上架</th>
			<th width="3%">热卖</th>
			<th width="3%">新品</th>
			<th width="3%">特卖</th>
			<th width="5%">折扣价</th>
			<th width="11%">添加商品时间</th>
			<th width="11%">修改商品时间</th>
			<th width="20%">操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${pagination.list }" var="product">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${product.id }"/></td>
				<td>${product.id }</td>
				<td align="center">${product.name }</td>
				<td align="center"><img width="50" height="50" src="${product.picture }"/></td>
				<td align="center">${product.prevprice }</td>
				<td align="center">
					<c:if test="${product.isShow eq 0}">是</c:if>
					<c:if test="${product.isShow eq 1}">否</c:if>
				</td>
				<td align="center">
					<c:if test="${product.isHot eq 0}">是</c:if>
					<c:if test="${product.isHot eq 1}">否</c:if>
				</td>
				<td align="center">
					<c:if test="${product.isNew eq 0}">是</c:if>
					<c:if test="${product.isNew eq 1}">否</c:if>
				</td>
				<td align="center">
					<c:if test="${product.isDiscount eq 0}">是</c:if>
					<c:if test="${product.isDiscount eq 1}">否</c:if>
				</td>
				<td align="center">${product.price }</td>
				<td align="center"><fmt:formatDate value="${product.showTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center"><fmt:formatDate value="${product.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center">
				<a href="javascript:window.location.href='toEdit.do?id=${product.id }'" class="pn-opt">修改商品</a> | 
				<a href="javascript:window.location.href='toAddDetail.do?id=${product.id }'" class="pn-opt">添加详细</a> | 
				<a href="javascript:window.location.href='toEditDetail.do?id=${product.id }'" class="pn-opt">修改详细</a> |
				<a href="javascript:window.location.href='notShow.do?id=${product.id }'" onclick="if(!confirm('您确定要下架吗？')) {return false;}" class="pn-opt">下架</a>
				<a href="javascript:window.location.href='onShow.do?id=${product.id }'" class="pn-opt">上架</a>
				</td>
			</tr>
		</c:forEach>
		
	</tbody>
</table>
<div class="page pb15">
	<span class="r inb_a page_b">
		<c:forEach items="${pagination.pageView }" var="page">
			${page }
		</c:forEach>
	</span>
</div>
</form>
</div>
</body>
</html>