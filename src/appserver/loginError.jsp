<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html;charset=utf-8" %>
<%@include file="/taglib.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>管理中心</title>
</head>
<body>
<script language="javascript">
    alert("<c:out value="${message}"/>");
    location.href = "login.jsp";
</script>
</body>
</html>

