<%@page language="java" contentType="text/html; charset=UTF-8"%>
 
 <%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>


<html>
<head>

<title>操作返回页面</title>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/yahoo/yahoo-min.js'/>"/> </script>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/event/event-min.js'/>"/> </script>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/connection/connection.js'/>"/> </script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CreditMvc/signOn.mvc" -->
<!-- <link href="styles/default.css" rel="stylesheet" type="text/css" />
<link href="styles/dtree.css" rel="stylesheet" type="text/css" />-->

<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/creditMenu.js'/>" type="text/javascript" language="javascript"></script>


<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  >
  
</body>
</html>

<%

Enumeration paramNames = request.getParameterNames();
String params="";
if(paramNames!=null){
	while(paramNames.hasMoreElements()){
		String paramName = (String) paramNames.nextElement();
		String paramValue=request.getParameter(paramName);
		if(paramValue!=null){
			//把参数拼成name=value;name2=value2;.....的形式
			params = params + paramName + "=" + paramValue + "&";
//			param.append(paramName).append("=").append(paramValue).append(";");
		}
	}
}
//System.out.println("params==="+params);
String userId = (String)request.getSession().getAttribute("userid");

ResourceBundle res = ResourceBundle.getBundle("cmis");
String url = res.getString("report.url");
String port = res.getString("report.port");
String ctxroot = res.getString("report.context.root");
String requestUrl = "http://"+url+":"+port+"/"+ctxroot+"/reportRecords.jsp?"+params+"userId="+userId;
 //System.out.println("requestUrl==="+requestUrl);

response.sendRedirect(response.encodeRedirectURL(requestUrl));
//System.out.println("===========================dddddddddddddddddddd=======================");

%>
<script type="text/javascript">
  

	function doOnLoad(){
		var doUrl = "http://localhost:8080/report/PrintReportTable.jsp?Sys_ReportName=report_ts&serno=LS9000010090005020";
		 
		window.location=doUrl;
	};

</script>
