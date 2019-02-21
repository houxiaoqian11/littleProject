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

function ship(id){
	var wuliuNo = prompt("请输入物流编号");

	if(wuliuNo){
 		window.location.href='ship.do?id='+id+'&wuliuNo='+wuliuNo; 
	}else{
		alert("物流编号不能为空");
	}
}

</script>
</head>
<body>
<div class="body-box">
<form action="/peakshop/order/list.do" method="post" style="padding-top:5px;">
订单号: <input type="text" name="orderId" value="${orderId }"/>
订单状态：	<select name="status">
		<c:forEach items="${allOrderStatus }" var="orderStatus">
			<option value="${orderStatus.id }" <c:if test="${status == orderStatus.id}">selected="selected"</c:if>>${orderStatus.desc }</option>
		</c:forEach>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" width="100%" border="0" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th>商品名称</th>
			<th width="5%">图片</th>
			<th width="5%">金额</th>
			<th width="10%">订单号</th>
			<th width="8%">订单状态</th>
			<th width="5%">姓名</th>
			<th width="8%">电话</th>
			<th width="18%">送货地址</th>
			<th width="10%">物流号</th>
			<c:if test="${status eq 1}"><th width="8%">操作选项</th></c:if>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${pagination.list }" var="order">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td align="center">${order.productName }</td>
				<td align="center"><img width="50" height="50" src="${order.picture }"/></td>
				<td align="center">${order.totalPrice }</td>
				<td align="center">${order.orderId }</td>
				<td align="center">
					<c:if test="${order.status eq 0}">待付款</c:if>
					<c:if test="${order.status eq 1}">已付款待发货</c:if>
					<c:if test="${order.status eq 2}">待收货</c:if>
					<c:if test="${order.status eq 3}">已完成</c:if>
					<c:if test="${order.status eq 9}">已取消</c:if>
				</td>
				<td align="center">${order.name }</td>
				<td align="center">${order.phone }</td>
				<td align="center">${order.addressInfo }</td>
				<td align="center">${order.wuliuNo }</td>
				<c:if test="${status eq 1}">
					<td align="center">
						<a href="javascript:ship(${order.id });" class="pn-opt">确认发货</a> 
					</td>
				</c:if>
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