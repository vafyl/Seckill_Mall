<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>秒杀商品列表</title>
    <%@include file="head.jsp" %>
</head>
    <style type="text/css">
        body{
            background:url("../../static/img/bg1.JPG");
            background-size: 100%,100%;
        }
    </style>
<body>
<div class="container">
    <div class="panel panel-default">
        <input id="killId" value="${detail.id}" type="hidden"/>
        <div class="panel-heading">
            <h1 id="itemName">商品名称：${detail.itemName}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">剩余数量：${detail.total}</h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">开始时间：<fmt:formatDate value="${detail.start_time}" pattern='yyyy-MM-dd HH:mm:ss'/></h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">结束时间：<fmt:formatDate value="${detail.end_time}" pattern='yyyy-MM-dd HH:mm:ss'/></h2>
        </div>
    </div>

    <td>
        <c:choose>
            <c:when test="${detail.canKill==1}">
                <a class="btn btn-info" style="font-size: 20px" onclick="executeKill()">抢购</a>
            </c:when>
            <c:otherwise>
                <a class="btn btn-info" style="font-size: 20px">哈哈~商品已抢购完毕或者不在抢购时间段哦!</a>
            </c:otherwise>
        </c:choose>
    </td>
</div>

</body>
<script src="${ctx}/static/plugins/jquery.js"></script>
<script src="${ctx}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${ctx}/static/plugins/jquery.cookie.min.js"></script>
<script src="${ctx}/static/plugins/jquery.countdown.min.js"></script>
<script src="${ctx}/static/plugins/layer-v3.1.1/layer/layer.js"></script>
<%--<script src="${ctx}/static/script/kill.js"></script>--%>
<link rel="stylesheet" href="${ctx}/static/css/detail.css" type="text/css">
<script type="text/javascript">
    function executeKill() {
        $.ajax({
            type: "POST",
            url: "${ctx}/kill/execute",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(getJsonData()),
            dataType: "json",

            success: function(res){
                if (res.code==0) {
                    layer.msg("恭喜您，商品秒杀"+res.msg, {icon: 1,time: 2000, shift: 6},function () {
                        window.location.href="${ctx}/kill/execute/success"
                    });
                }else{
                    // alert(res.msg+$("#killId").val());
                    layer.msg(res.msg, {icon: 2,time: 4000,shift: 6},function () {
                        window.location.href="${ctx}/kill/execute/fail"
                    });
                }
            },
            error: function (message) {
                layer.msg("数据提交失败！！",{icon: 5,time: 3000,shift: 6})
                return;
            }
        });
    }

    function getJsonData() {
        var killId=$("#killId").val();
        /*var data = {
            "killId":killId,
            "userId":1
        };*/
        var data = {
            "killId":killId
        };
        return data;
    }
</script>

</html>
















