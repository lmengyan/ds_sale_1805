<%--
  Created by IntelliJ IDEA.
  User: pc
  Date: 2018/11/5
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery-1.7.2.min.js"></script>
    <script>
        $(function () {

            var yhMch=getCookieByKey("yhMch");
            $("#yhMch_span").html(yhMch);
        })
        //分割  通过key获取到value
        function getCookieByKey(key) { //通过key获取对象的下标
            var val= "";
            var ck =document.cookie;
            ck=ck.replace(" ","");
            var ckArr=ck.split(";");
            for(var i =0;i<ckArr.length;i++){
                    console.log(ckArr[i]);
                    var arr=ckArr[i].split("=");
                    if (arr[0] == key){
                        val = arr[1];
                    }
            }
            return val;
        }
    </script>

</head>
<body>
    <div class="top">
        <div class="top_text">
            <c:if test="${empty user}">
                <a href="toLoginPage.do">用户登录<span id="yhMch_span"></span></a>
                <a href="">用户注册</a>
            </c:if>
            <c:if test="${!empty user}"><%-- 用户有值情况下登陆成功--%>
                <a href="toLoginPage.do">用户名:${user.yhMch}</a>
                <a href="logout.do">注销</a>
            </c:if>
        </div>
    </div>

    <div class="top_img">
        <img src="./images/top_img.jpg" alt="">
    </div>
</body>
</html>
