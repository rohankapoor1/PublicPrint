<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% LinkedList<String> list = (LinkedList<String>)request.getAttribute("filepath");
Iterator<String> itr = list.iterator();
while(itr.hasNext())
{
	String fname = itr.next();
%>
<a href="${pageContext.request.contextPath}/fetch?filepath="<%=fname%>>Download File</a>
<%} %>
</body>
</html>