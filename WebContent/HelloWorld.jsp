<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<title>Hello.jsp</title>
</head>
<body>
	<h1>Hello World, JSP aqui...</h1>
	<%-- Usando scriplet --%>
	<%out.println("Diego Sena no comando"); %>
	<%-- Usando elementos de expressão--%>
	<p>Data: <%= new Date() %></p>
	
	<%!int contador = 0; %>
	<p>Visitas = <%=contador++ %></p>
</body>
</html>