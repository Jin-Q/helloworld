<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>�����ѯҳ��</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="querySfTransList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SfTransGroup" title="交易定义" maxColumn="2">
			<emp:text id="SfTrans.trans_id" label="交易ID" maxlength="32" required="true" />
			<emp:text id="SfTrans.trans_name" label="交易名称" maxlength="50" required="true" />
			<emp:text id="SfTrans.trans_permission" label="交易授权" maxlength="50" required="false" />
			<emp:text id="SfTrans.trans_ext" label="交易预处理" maxlength="100" required="false" />
			<emp:textarea id="SfTrans.trans_desc" label="交易描述" maxlength="500" required="false" colSpan="2" />
			<emp:text id="SfTrans.sysid" label="sysid" maxlength="32" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="���ص��б�ҳ��"/>
	</div>
</body>
</html>
</emp:page>
