<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.io.*,java.util.*,data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%  
	IDataHandler handler = DataHandler.getInstance();
	handler.createUser(request.getParameter("Username"), request.getParameter("Email"), request.getParameter("Password"));
	
	response.sendRedirect("index.jsp");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>

</body>
</html>