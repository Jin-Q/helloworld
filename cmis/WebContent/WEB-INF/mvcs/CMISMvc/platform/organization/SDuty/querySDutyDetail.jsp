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
		//var url = '<emp:url action="querySDutyList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SDutyGroup" maxColumn="2" title="岗位表">
			<emp:text id="SDuty.dutyno" label="岗位码" maxlength="20" required="true" readonly="true" />
			<emp:text id="SDuty.dutyname" label="岗位名称" maxlength="40" required="true" />
			<emp:text id="SDuty.organno" label="机构码" maxlength="16" required="false" />
			<emp:text id="SDuty.depno" label="部门码" maxlength="16" required="false" />
			<emp:text id="SDuty.orderno" label="排序字段" hidden="true" maxlength="38" required="false" />
			<emp:text id="SDuty.orgid" label="组织号" hidden="true" maxlength="16" required="false" />
			<emp:text id="SDuty.type" label="类型" maxlength="38" required="false" hidden="true"/>
			<emp:textarea id="SDuty.memo" label="备注" maxlength="60" required="false" colSpan="2" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
