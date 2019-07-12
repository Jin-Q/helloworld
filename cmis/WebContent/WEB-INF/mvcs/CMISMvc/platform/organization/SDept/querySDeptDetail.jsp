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
	
	function doClose() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SDeptGroup" title="部门表" maxColumn="2">
			<emp:text id="SDept.depno" label="部门码" maxlength="16" required="true" />
			<emp:text id="SDept.organno" label="机构码" maxlength="16" required="false" />
			<emp:text id="SDept.supdepno" label="上级部门码" maxlength="16" required="true" />
			<emp:text id="SDept.depname" label="部门名称" maxlength="40" required="true" />
			<emp:text id="SDept.depshortform" label="部门简称" maxlength="40" required="false" />
			<emp:text id="SDept.enname" label="英文名" maxlength="40" required="false" />
			<emp:text id="SDept.orderno" label="序号" maxlength="38" required="false" />
			
			<emp:date id="SDept.launchdate" label="开办日期" required="false" />
			
			
			<emp:text id="SDept.depchief" label="部门负责人" maxlength="32" required="false" />
			<emp:text id="SDept.telnum" label="联系电话" maxlength="20" required="false" />
			
			<emp:text id="SDept.postcode" label="邮编" maxlength="10" required="false" />
			<emp:text id="SDept.orgid" label="组织号" maxlength="16" required="false" />
			<emp:textarea id="SDept.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:select id="SDept.state" label="状态" required="true" dictname="STD_ZB_ORG_STATUS"/>
			<emp:text id="SDept.address" label="地址" maxlength="200" required="false" hidden="true"/>
			<emp:text id="SDept.deplevel" label="部门级别" maxlength="38" required="false" hidden="true"/>
			<emp:text id="SDept.distno" label="地区编号" maxlength="10" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
