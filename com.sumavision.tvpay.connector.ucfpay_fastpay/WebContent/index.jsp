<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    	               "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GlassFish JSP Page</title>
</head>
<body>
<h1>ucfpayfastpay Bank ~ It works!</h1>
<form action="http://sandbox.firstpay.com/security/gateway.do" method="post">
	<input type="text" value="K7Sl2rxIkQhjvThz7ZqJa7SoSflDeOgXkvNTOefwU+vsZdubytkAgEDa7G1GltN3QIZ5Nx48rJz5Jzv9mretcKn5YlhBvG+bjRRYwYMZCYB7XSMKoHxmEg3DxMskPZ4mcj+J8JWyx/7Hdd4QDRVxTTwwYEaS2BhDYAiEIEY4zb/pAmF/PerlNZDUCWb3dQKxQG3t0uxA4EnX/no0qm38PKJfudS199T5nUyZ5OxF2eyKZV+uheau/r8JPw1eaThXntxSaPvE4j4V3+a8zZp6tk0VIxb4XmYad0cCdTumGo5fPdQpwQaMcllvf1HGhWcyI4WQPDFiM83nj8yQJHx4qg==" name="data">
	<input type="text" value="M200000550" name="merchantId">
	<input type="text" value="TO_UCF_CASHIER" name="service">
	<input type="text" value="WrabPK2ThTgXNZc0glJNSH/IE3Xj4bx6ldj1Qf1SKTB29GVMUxeXpqBR8zkma668keoGrFcNl+A0dNNcJImCENzDqATJrO+Wasb4SZhsqTu12NdTO/Zs3f+/zxai8JSN9jZ7oAw0qc/YIim/rdu79u+C0I7SQMt4UjoKoUfdJD8=" name="sign">
	<input type="text" value="4.0.0" name="version">
	<input type="text" value="RSA" name="secId">
	<input type="text" value="A432CA61EE5DA45FEDAF21F091B8E9D8" name="reqSn">
	<input type="button" name="提交">
</form>
</body>
</html>
