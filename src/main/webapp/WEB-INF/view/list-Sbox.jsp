<%--
  Created by IntelliJ IDEA.
  User: pc
  Date: 2018/11/6
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div class="Sbox">
        <c:forEach items="${skuList}" var="sku">
            <div class="list">
                <div class="img"><img src="images/img_4.jpg" alt=""></div>
                <div class="price">￥${sku.jg}</div>
                <div class="title">
                    <a href="toItmePage.do?skuId=${sku.id}&spu=${sku.shpId}">${sku.skuMch}</a>
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>
