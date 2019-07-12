<%@page import="com.yucheng.cmis.pub.CMISConstant"%>
<%@page import="java.util.Enumeration"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>ModualJSPServiceAdapter</title>
<jsp:include page="/include.jsp" flush="true"/>
<!-- 
	该JSP主要用于页面服务适配器
	跳转到jspServiceId所指定的页面，并将request范围内的参数往下传递。
-->

<% 
	String url = "";
	//取得jspServiceId所对面的.do
	String jspServiceId = (String)request.getAttribute(CMISConstant.JSP_SERVICE_ID);
	//String jspServiceId = request.getParameter(CMISContents.JSP_SERVICE_ID);
	url = jspServiceId.endsWith(".do")?jspServiceId:jspServiceId+".do";
	
	int i=0; //计算器用于控制url后接"?"还是"&"
	//拼接url参数
	Enumeration em = request.getParameterNames();
	while(em.hasMoreElements()){
		String paraName = (String)em.nextElement();
		if(i==0)
			url = url+"?"+paraName+"="+request.getParameter(paraName);
		else
			url = url+"&"+paraName+"="+request.getParameter(paraName);
		
		i++;
	}
	
%>

<script type="text/javascript">
	function doLoad(){
		//直接跳转到目标页面
		var url = "<%=url%>";
		window.location = url;
	}

</script>

</head>
<body onload="doLoad()">
</body>
</html>
</emp:page>

