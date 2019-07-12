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
	
	function doReturn() {
		var url = '<emp:url action="queryCusSubmitInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
	function doCancel(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusSubmitInfoGroup" title="客户影像扫描信息录入操作" maxColumn="2">
			<emp:text id="CusSubmitInfo.cus_id" label="客户码" maxlength="21" required="true" />
			<emp:text id="CusSubmitInfo.cus_name" label="客户名称" maxlength="60" required="true" />
			
			<emp:text id="CusSubmitInfo.submit_id_displayname" label="提交人"  required="true" />
			<emp:text id="CusSubmitInfo.rcv_id_displayname" label="接收人"  required="true" />
			<emp:textarea id="CusSubmitInfo.memo" label="提示信息" maxlength="400" required="true" colSpan="2" />
			<emp:date id="CusSubmitInfo.input_date" label="录入日期" required="true" />
			<emp:text id="CusSubmitInfo.serno" label="流水号" maxlength="30" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusSubmitInfo.submit_id" label="提交人" maxlength="20" required="true" hidden="true" />
			<emp:text id="CusSubmitInfo.rcv_id" label="接收人" maxlength="20" required="true" hidden="true" />
			<emp:text id="CusSubmitInfo.end_flag" label="完成标志(0.完成 1.未完成)" maxlength="1" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="cancel" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
