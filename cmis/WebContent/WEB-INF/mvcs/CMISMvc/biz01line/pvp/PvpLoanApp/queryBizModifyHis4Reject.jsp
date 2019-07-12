<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);   
	String wf_flag="";
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	String modiflag="";
	if(context.containsKey("modiflag")){
		modiflag = (String) context.getDataValue("modiflag");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">	
	function doClose(){
		window.close();
	};
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div class='emp_gridlayout_title'>不存在修改历史对比值</div>
	<div align="center">
		<br>
		<%if(!"1".equals(wf_flag) && !"yes".equals(modiflag)){ %>
			<emp:button id="close" label="关闭" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
