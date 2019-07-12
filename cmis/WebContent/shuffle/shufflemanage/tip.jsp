<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<HTML>
<HEAD>
<TITLE>ECC IDE Jsp file</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<style type='text/css'>
A{font-size:9pt}
BODY{font-size:9pt}
</style>
</HEAD>
<BODY>
<%
	String tip = (String)request.getAttribute("tip");
%>
<%=tip%>
</BODY></HTML>