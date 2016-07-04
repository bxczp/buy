<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">

	function goBuy(productId){
		if ('${currentUser.userName}' == '') {
			alert("请登录...");
			javascript:window.location.href='login.jsp';
		} else {
			window.location.href='shopping_buy.action?productId='+productId;
		}
	}
	function addCart(productId) {
		//首先判断用户是否登录 要注意 单引号的使用
		if ('${currentUser.userName}' == '') {
			alert("请登录...");
			javascript:window.location.href='login.jsp';
		} else {
			if (confirm("确认添加？")) {
				$.post("shopping_addShoppingCartItem.action", {
					productId : productId
				}, function(result) {
					var result = eval("(" + result + ")")
					if (result.success) {
						alert("添加成功");
						//立即刷新当前页面
						location.reload();
					}
				});
			}
		}
	}
</script>
</head>
<body>
	<div id="product" class="main">
		<h1>${product.name}</h1>
		<div class="infos">
			<div class="thumb">
				<img class="img" src="${product.proPic}" />
			</div>
			<div class="buy">
				<br />
				<p>
					商城价：
					<span class="price">￥${product.price}</span>
				</p>
				<p>库 存：${product.stock}</p>
				<br />
				<div class="button">
					<input type="button" name="button" value="" onclick="goBuy(${product.id})" />
					<br />
					<a href="javascript:addCart(${product.id })">放入购物车</a>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="introduce">
			<h2>
				<strong>商品详情</strong>
			</h2>
			<div class="text">${product.description}</div>
		</div>
	</div>
</body>
</html>