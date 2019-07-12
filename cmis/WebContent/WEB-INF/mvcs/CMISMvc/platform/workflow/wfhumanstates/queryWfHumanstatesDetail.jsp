<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>查看</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfHumanstatesList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	window.onload = function() {
		changeType();
	}
	
	function changeType() {
		var type = WfHumanstates.vicarioustype._getValue();
		if(type == '0') {
			WfHumanstates.appid._setValue('');
			WfHumanstates.appid._obj._renderRequired(false);
			WfHumanstates.appid._obj._renderHidden(true);
			//WfHumanstates.appname._setValue('');
			//WfHumanstates.appname._obj._renderRequired(false);
			//WfHumanstates.appname._obj._renderHidden(true);
		} else {
			WfHumanstates.appid._obj._renderRequired(true);
			WfHumanstates.appid._obj._renderHidden(false);
			//WfHumanstates.appname._obj._renderRequired(true);
			//WfHumanstates.appname._obj._renderHidden(false);
		}
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfHumanstatesGroup" title="工作委托设置" maxColumn="2">
	
			<emp:text id="WfHumanstates.pkey" label="流水号" maxlength="32" required="true" hidden="true" colSpan="2"/>
			<emp:text id="WfHumanstates.userid" label="委托人用户名" required="true" defvalue="${context.currentUserId}" readonly="true"/>
			<emp:text id="WfHumanstates.username" label="委托人姓名" required="true" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="WfHumanstates.vicar" label="被委托人用户名" required="true" readonly="true"/>
			<emp:text id="WfHumanstates.vicarname" label="被委托人姓名" maxlength="32" required="true" readonly="true"/>
			<emp:text id="WfHumanstates.begintime" label="开始时间" required="true" onclick="setday(this)" readonly="true"/>
			<emp:text id="WfHumanstates.endtime" label="结束时间" required="true" onclick="setday(this)" readonly="true"/>
			<emp:text id="WfHumanstates.onoff" label="是否启用" maxlength="1" required="false" hidden="true" defvalue="0"/>
			<emp:radio id="WfHumanstates.vicarioustype" label="委托类型" required="true" defvalue="0" dictname="WF_VICARIOUSTYPE" disabled="true" colSpan="2" />
			<emp:select id="WfHumanstates.appid" label="申请类型" required="false" hidden="true" dictname="ZB_BIZ_CATE"/>
			<emp:text id="WfHumanstates.appname" label="APPNAME" maxlength="50" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.wfid" label="WFID" maxlength="32" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.wfname" label="WFNAME" maxlength="50" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.orgid" label="ORGID" maxlength="32" required="false" hidden="true"/>
			<emp:text id="sysid" label="SYSID" hidden="true" defvalue="${context.sysId }"/>
			<emp:textarea id="WfHumanstates.vicarmemo" label="说明" maxlength="500" required="false" colSpan="2"/>
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
