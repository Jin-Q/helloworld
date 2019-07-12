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
<style type="text/css">
.emp_input{
	width: 600px;
	height: 50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryPrdSubTabActivityList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
		<emp:tabGroup mainTab="mainTab" id="mainTab">
		<emp:tab label="tab页签挂接信息" id="mainTab">
		<emp:gridLayout id="PrdSubTabActivityGroup" title="Table标签页动态挂接" maxColumn="2">
			<emp:text id="PrdSubTabActivity.mainid" label="主资源ID" maxlength="80" required="true" />
			<emp:text id="PrdSubTabActivity.mainmodel" label="主资源表模型" maxlength="80" required="false" />
			<emp:textarea id="PrdSubTabActivity.mainterm" label="主资源过滤条件" maxlength="500" required="false" cssElementClass="emp_input" colSpan="2"/>
			<emp:text id="PrdSubTabActivity.subid" label="从资源ID" maxlength="80" required="true" />
			<emp:text id="PrdSubTabActivity.submodel" label="从资源表模型" maxlength="80" required="false" />
			<emp:text id="PrdSubTabActivity.subnum" label="从资源标识" maxlength="30" required="false" hidden="true"/>
			<emp:textarea id="PrdSubTabActivity.subterm" label="从资源过滤条件" maxlength="300" required="false" cssElementClass="emp_input" colSpan="2" />
			<emp:text id="PrdSubTabActivity.num" label="序号" maxlength="10" required="true" />
			<emp:text id="PrdSubTabActivity.subname" label="从资源名称" maxlength="80" readonly="true" required="false" />	
			<emp:text id="PrdSubTabActivity.inputid" label="登记人员" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="PrdSubTabActivity.inputdate" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="PrdSubTabActivity.inputorg" label="登记机构" maxlength="10" required="false" readonly="true" hidden="true"/>
			<emp:text id="PrdSubTabActivity.pkid" label="主键" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
		
		
		<div align="center">
			<br>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
		</emp:tab>
		<emp:tab label="tab页签操作权限配置" id="subTab" url="queryPrdSubTabActionList.do" initial="false" needFlush="true" reqParams="mainid=$PrdSubTabActivity.mainid;&subid=$PrdSubTabActivity.subid;&act=view"/>
	</emp:tabGroup>
</body>
</html>
</emp:page>
