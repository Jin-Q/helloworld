<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<html>
<head>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String rep_flag = "";	
	if(context.containsKey("rep_flag")){
		rep_flag = (String)context.getDataValue("rep_flag");
	}
	String serno = "";	
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	String guaranty_no = "";	
	if(context.containsKey("guaranty_no")){
		guaranty_no = (String)context.getDataValue("guaranty_no");
	}
%>
<title>操作返回页面</title>
<script type="text/javascript">

function doOnLoad(){
	var rep_flag = '<%=rep_flag%>';	
	var serno = '<%=serno%>';	
	var guaranty_no = '<%=guaranty_no%>';
	if(rep_flag == 'ckqd'){
		var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=MortStor/hwzhckqd.raq&serno='+serno+'&guaranty_no='+guaranty_no;
	}else if(rep_flag == 'rkqd'){
		var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=MortStor/hwzhrkqd.raq&serno='+serno+'&guaranty_no='+guaranty_no;
	}else if(rep_flag == 'zhtzd'){
		var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=MortStor/hwzhtzd.raq&serno='+serno+'&guaranty_no='+guaranty_no;
	}else if(rep_flag == 'thtzd'){
		var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=MortStor/hwzhthtzd.raq&serno='+serno+'&guaranty_no='+guaranty_no;
	}
	url = EMPTools.encodeURI(url);
    window.location = url;
}

</script>
<jsp:include page="/include.jsp" />
</head>
<body class="page_content" onload="doOnLoad()">

</body>
</html>