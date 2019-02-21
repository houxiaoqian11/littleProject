<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 类型管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加商品类型" onclick="javascript:window.location.href='toAdd.do'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="/peakshop/type/list.do" method="post" style="padding-top:5px;">
	<select name="isShow">
		<option value="true" <c:if test="${isShow}">selected="selected"</c:if>>上架</option>
		<option value="false" <c:if test="${!isShow}">selected="selected"</c:if>>下架</option>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" width="100%" border="0" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th width="8%">类型编号</th>
			<th>类型名称</th>
			<th>类型图标</th>
			<th width="16%">添加类型时间</th>
			<th width="16%">修改类型时间</th>
			<th width="8%">操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${pagination.list }" var="type">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${product.id }"/></td>
				<td>${type.id }</td>
				<td align="center">${type.name }</td>
				<td align="center"><img width="50" height="50" src="${type.picture }"/></td>
				<td align="center"><fmt:formatDate value="${type.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center"><fmt:formatDate value="${type.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center">
				<a href="javascript:window.location.href='toEdit.do?id=${type.id }'" class="pn-opt">修改</a> | 
				<a href="javascript:window.location.href='notShow.do?id=${type.id }'" onclick="if(!confirm('您确定要下架吗？')) {return false;}" class="pn-opt">下架</a>
				<a href="javascript:window.location.href='onShow.do?id=${type.id }'" class="pn-opt">上架</a>
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