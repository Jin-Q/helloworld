<%@ page contentType="text/html; charset=GBK" language="java" errorPage="" %>
<%@ page import="java.awt.*" %>
<html>
<%
String ShuffleServlet=request.getScheme()+"://"+request.getLocalAddr()+":"+request.getServerPort()+request.getContextPath()+"/ShuffleServlet";
String ruleSetId=request.getParameter("ruleSetId");
String ruleId=request.getParameter("ruleId");
//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
%>
  <head>
    <title>eChainMonitor图形化流程跟踪</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">      
  </head>
  
  <body text=#ffffff bgColor=#ffffff leftMargin=0 topMargin=0 onload="" marginheight="0" marginwidth="0" scroll=yes status=no>
    <applet archive = "<%=request.getContextPath()%>/shuffle/studio/shufflestudiolib/shufflestudio.jar"
			codebase="." 
            code="com.ecc.shufflestudio.ui.RuleTableApplet.class" 
            name="ShuffleStudio" 
            width="1095" 
            height="545">
<param name = "ShuffleServlet" value = "<%=ShuffleServlet%>">
<param name = "ruleSetId" value = "<%=ruleSetId%>">
<param name = "ruleId" value = "<%=ruleId%>">
    </applet>
  </body>
</html>
