<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
	// 在文档加载完成后执行
    $ (function ()
    {
	    //$(function(){});这个方法 通常 是为了 绑定Dom节点的事件，
	    // 以及在页面所有的Dom文档加载完成后执行的事件 
	    // $(function(){}); 是一个 大大 的方法
	    $ (".add").click (function ()
	    {
		    //当前对象的父节点 寻找input标签下class为text_box的元素的内容
		    var t = $ (this).parent ().find ('input[class=text_box]');
		    //要 显示 的 转为int 不然 是字符串的连接
		    t.val (parseInt (t.val ()) + 1);
		    var product_id = $ (this).parent ().find ('input[id=product_id]').val ();
		    var price = $ ('#price_' + product_id).html ();
		    $ ('#productItem_total_' + product_id).html (price * t.val ());
		    refreshSession (product_id, t.val ());
		    setTotal ();
	    });
	    
	    $ (".min").click (function ()
	    {
		    //当前对象的父节点 寻找input标签下class为text_box的元素的内容
		    var t = $ (this).parent ().find ('input[class=text_box]');
		    t.val (parseInt (t.val ()) - 1);
		    if (parseInt (t.val ()) < 0)
		    {
			    t.val (0);
		    }
		    var product_id = $ (this).parent ().find ('input[id=product_id]').val ();
		    var price = $ ('#price_' + product_id).html ();
		    $ ('#productItem_total_' + product_id).html (price * t.val ());
		    refreshSession (product_id, t.val ())
		    setTotal ();
	    });
	    
	    //失去焦点触发的事件
	    $ (".text_box").blur (function ()
	    {
		    //当前对象的父节点 寻找input标签下class为text_box的元素的内容
		    var t = $ (this).parent ().find ('input[class=text_box]');
		    if (parseInt (t.val ()) < 0)
		    {
			    t.val (0);
		    }
		    var product_id = $ (this).parent ().find ('input[id=product_id]').val ();
		    var price = $ ('#price_' + product_id).html ();
		    $ ('#productItem_total_' + product_id).html (price * t.val ());
		    refreshSession (product_id, t.val ())
		    setTotal ();
	    });
	    
	    //ajax请求后台
	    function refreshSession (productId, count)
	    {
		    $.post ("shopping_updateShoppingItem.action",
		    {
		        productId : productId,
		        count : count
		    }, function (result)
		    {
			    var result = eval ("(" + result + ")");
			    if (result.success)
			    {
				    
			    }
			    else
			    {
				    alert ("更新商品失败");
			    }
		    });
	    }
	    
	    //定义方法
	    function setTotal ()
	    {
		    var s = 0;
		    //获取 类为productTr的每一行
		    $ (".productTr").each (function ()
		    {
			    //$(this)当前这一行 find()为JQuery选择器
			    // input(label)为要选取的行的标签，class为样式
			    // html为获取元素的内容
			    var num = $ (this).find ('input[class=text_box]').val ();
			    var price = $ (this).find ('label[class=price_]').html ();
			    // 	alert("n="+num);
			    // 	alert("price="+price);
			    s += num * price;
		    });
		    $ ("#product_total").html (s);
	    }
	    //调用方法
	    setTotal ();
    });
    
    //此方法 不能写在上面的$(function(){。。。。。。})中，上面的是文档加载完成后执行的
    function deleteShoppingCartItem (productId)
    {
	    if (confirm ("确认删除？"))
	    {
		    javascript: window.location.href = 'shopping_deleteShoppingCartItem.action?productId=' + productId;
	    }
    }
</script>
</head>
<body>
	<div id="shopping">
		<form action="order_save.action" method="post">
			<table id="myTableProduct">
				<tr>
					<th>商品名称</th>
					<th>商品单价</th>
					<th>金额</th>
					<th>购买数量</th>
					<th>操作</th>
				</tr>
				<c:forEach var="cartItem" items="${shoppingCart.shoppingCartItems }">
					<tr class="productTr">
						<td class="thumb"><img class="imgs" src="${cartItem.product.proPic }" />
							<a href="">${cartItem.product.name }</a></td>
						<td class="price"><span>
								￥
								<label class="price_" id="price_${cartItem.product.id }">${cartItem.product.price }</label>
							</span></td>
						<td class="price"><span>
								￥
								<label id="productItem_total_${cartItem.product.id }">${cartItem.product.price * cartItem.count}</label>
							</span></td>
						<td class="number"><input type="hidden" id="product_id" value="${cartItem.product.id }" /> <input class="min" name=""
								type="button" value=" - " /> <input class="text_box" style="width: 30px; text-align: center" name="" type="text"
								value="${cartItem.count }" /> <input class="add" name="" type="button" value=" + " /></td>
						<td class="delete"><a href="javascript:deleteShoppingCartItem(${cartItem.product.id })">删除</a></td>
					</tr>
				</c:forEach>
			</table>

			<div class="button">
				<input type="submit" value="" />
			</div>
		</form>
	</div>

	<div class="shopping_list_end">

		<ul>
			<li class="shopping_list_end_2">
				￥
				<label id="product_total"></label>
			</li>
			<li class="shopping_list_end_3">商品金额总计：</li>
		</ul>
	</div>

	<div style="background-color: #cddbb8; margin-top: 10px; font-size: 20px; height: 40px; text-align: center">
		<div style="padding: 5px;">
			<b>个人信息</b>&nbsp;&nbsp;&nbsp;&nbsp;<b>收件人：</b>${currentUser.trueName }&nbsp;&nbsp;<b>收获地址：</b>${currentUser.address }&nbsp;&nbsp;<b>联系电话：</b>${currentUser.mobile }
		</div>
	</div>
</body>
</html>