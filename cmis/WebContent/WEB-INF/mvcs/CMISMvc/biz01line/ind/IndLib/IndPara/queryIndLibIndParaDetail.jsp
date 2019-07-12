<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IndParaGroup" title="参数设置" maxColumn="2">
			<emp:text id="IndPara.index_no" label="指标编号" maxlength="12" required="true" />
			<emp:text id="IndPara.enname" label="参数英文名" maxlength="10" required="true" />
			<emp:text id="IndPara.para_cnname" label="参数中文名" maxlength="60" required="false" />
			<emp:select id="IndPara.para_val_type" label="参数值类型" required="false" dictname="STD_ZB_PARA_TYPE"/>
			<emp:select id="IndPara.para_val_way" label="参数值来源"  required="false" dictname="STD_ZB_SOURCE"  hidden="true"/>
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
