<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
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
	<div class="rpos">当前位置: 仓储管理 - 列表</div>
</div>
<div class="body-box">
<form action="/peakshop/store/list.do" method="post" style="padding-top:5px;">
名称: <input type="text" name="name" value="${name }"/>
	<select name="colorId">
		<option value="">请选择颜色</option>
		<c:forEach items="${colors }" var="color">
			<option value="${color.id }" <c:if test="${colorId == color.id}">selected="selected"</c:if>>${color.name }</option>
		</c:forEach>
	</select>
	<select name="sizeId">
		<option value="">请选择尺码</option>
		<c:forEach items="${sizes }" var="size">
			<option value="${size.id }" <c:if test="${sizeId == size.id}">selected="selected"</c:if>>${size.name }</option>
		</c:forEach>
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
			<th width="5%">颜色</th>
			<th width="5%">尺码</th>
			<th width="5%">库存</th>
			<th width="11%">添加库存时间</th>
			<th width="11%">修改库存时间</th>
			<th width="12%">操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${pagination.list }" var="store">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${product.id }"/></td>
				<td>${store.id }</td>
				<td align="center">${store.product.name }</td>
				<td align="center"><img width="50" height="50" src="${store.picture }"/></td>
				<td align="center">${store.color.name }</td>
				<td align="center">${store.size.name }</td>
				<td align="center">${store.store}</td>
				<td align="center"><fmt:formatDate value="${store.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center"><fmt:formatDate value="${store.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center">
				<a href="javascript:window.location.href='toEdit.do?id=${store.id }&picture=${store.picture }'" class="pn-opt">修改库存数</a> | 
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