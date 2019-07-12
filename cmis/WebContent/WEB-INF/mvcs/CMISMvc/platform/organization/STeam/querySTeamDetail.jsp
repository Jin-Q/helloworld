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
		var url = '<emp:url action="querySTeamList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="STeamGroup" title="团队信息表" maxColumn="2">
			<emp:text id="STeam.team_no" label="团队编号" maxlength="20" required="true" />
			<emp:text id="STeam.team_name" label="团队名称" maxlength="60" required="true" />
			<emp:text id="STeam.team_type" label="团队类型" maxlength="20" required="false" />
			<emp:select id="STeam.belg_line" label="归属条线" required="false" dictname="STD_ZB_BUSILINE" />
			<emp:textarea id="STeam.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="STeamGroup" title="登记信息" maxColumn="2">
			<emp:text id="STeam.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true"/>
			<emp:text id="STeam.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$S_organno" readonly="true"/>
			<emp:date id="STeam.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
	<emp:tabGroup id="Team_tabs" mainTab="STeamUser_tab">
		<emp:tab id="STeamUser_tab" label="团队成员" url="querySTeamUserList.do" reqParams="team_no=${context.STeam.team_no}&act=view" initial="true" needFlush="true"/>
		<emp:tab id="STeamOrg_tab" label="归属机构" url="querySTeamOrgList.do" reqParams="team_no=${context.STeam.team_no}&act=view" initial="true" needFlush="true"/>
	</emp:tabGroup>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
