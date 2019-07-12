<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%
	String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortLandBelongsRecord.do" method="POST">
		
		<emp:gridLayout id="MortLandBelongsGroup" title="土地承包经营权共有人情况" maxColumn="2">
			<emp:text id="MortLandBelongs.guaranty_no" label="押品编号" maxlength="30" required="true" colSpan="2" defvalue="<%=guaranty_no%>" readonly="true"/>
			<emp:text id="MortLandBelongs.cus_name" label="姓名" maxlength="100" colSpan="2"  required="true" />
			<emp:select id="MortLandBelongs.sex" label="性别"  required="true" dictname="STD_ZX_SEX"/>
			<emp:text id="MortLandBelongs.age" label="年龄" maxlength="3" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

