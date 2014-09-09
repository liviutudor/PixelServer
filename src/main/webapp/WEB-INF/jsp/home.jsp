<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="liv.*"%>
<jsp:useBean id="param_name" scope="request" class="java.lang.String" />
<html>
<head>
<title>pixelserver</title>
</head>
<body>
<p>Hello, <c:out value="${param_name}"/>!</p>
</body>
</html>
