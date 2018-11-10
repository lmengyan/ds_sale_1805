<%--
  Created by IntelliJ IDEA.
  User: pc
  Date: 2018/11/5
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/js/jquery-1.7.2.min.js"></script>
    <script>
        function toIndexPage() {
            location.href="toMainPage.do ";
        }
        //查看迷你购物车
        function fingMiniCart() {
            $.post("fingMiniCart.do",function (data) {
                $("#miniCartInnerDiv").html(data)
            })
            $("#miniCartInnerDiv").show();
        }
        function outMiniCart() {
            $("#miniCartInnerDiv").hide();
        }
        
    </script>
</head>
<body>
    <div class="search">
        <div class="logo"><img src="./images/logo.jpg" alt="" onclick="toIndexPage()"></div>
        <div class="search_on">
            <div class="se">
                <input type="text" name="search" class="lf">
                <input type="submit" class="clik" value="搜索">
            </div>
            <div class="se">
                <a href="">取暖神奇</a>
                <a href="">1元秒杀</a>
                <a href="">吹风机</a>
                <a href="">玉兰油</a>
            </div>
        </div>
        <div class="card" onmouseover="fingMiniCart()" onmouseout="outMiniCart()">
            <a href="toCartListPage.do">购物车<div class="num">0</div></a>

            <!--购物车商品-->
            <div class="cart_pro">
                <%--内嵌的迷你购物车--%>
                <div id="miniCartInnerDiv" style=""></div>




            </div>

        </div>


    </div>

</body>
</html>
