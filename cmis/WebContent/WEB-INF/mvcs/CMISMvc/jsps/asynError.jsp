<%@page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.yucheng.cmis.base.CMISException"%>
<%@ page import="com.yucheng.cmis.message.CMISMessageManager"%>
 <%	
			CMISException exception1 =(CMISException)request.getAttribute("exception");
			String display = CMISMessageManager.getMessage(exception1);
%><%= display%>
	 
