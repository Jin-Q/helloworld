<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="querySTeamList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doSave(){
		var form = document.getElementById("submitForm");
		if(STeam._checkAll()){
			STeam._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert("保存成功!");
						window.location.reload();
					}else {
						alert(msg);
						return;
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};		

	function getOrganName(data){
		STeam.team_org_id._setValue(data.organno._getValue());
		STeam.team_org_id_displayname._setValue(data.organname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSTeamRecord.do" method="POST">
		<emp:gridLayout id="STeamGroup" title="团队信息表" maxColumn="2">
			<emp:text id="STeam.team_no" label="团队编号" maxlength="20" required="true" readonly="true"/>
			<emp:text id="STeam.team_name" label="团队名称" maxlength="60" required="true" />
			<emp:text id="STeam.team_type" label="团队类型" maxlength="20" required="false" />
			<emp:select id="STeam.belg_line" label="归属条线" required="true" dictname="STD_ZB_BUSILINE" />
			<emp:pop id="STeam.team_org_id_displayname" label="归属机构"   url="querySOrgPop.do?opt=team&restrictUsed=false" returnMethod="getOrganName" required="true" />
			<emp:select id="STeam.status" label="是否有效" dictname="STD_ZX_YES_NO" required="true" />
			<emp:textarea id="STeam.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="STeam.team_org_id" label="归属机构" hidden="true" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="STeamGroup" title="登记信息" maxColumn="2">
			<emp:text id="STeam.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="STeam.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$S_organno" readonly="true" hidden="true"/>
			<emp:text id="STeam.input_id_displayname" label="登记人"  required="false" readonly="true"/>
			<emp:text id="STeam.input_br_id_displayname" label="登记机构"  required="false" readonly="true"/>
			<emp:date id="STeam.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<emp:tabGroup id="Team_tabs" mainTab="STeamUser_tab">
		<emp:tab id="STeamUser_tab" label="团队成员" url="querySTeamUserList.do" reqParams="team_no=${context.STeam.team_no}" initial="true" needFlush="true"/>
		<emp:tab id="STeamOrg_tab" label="归属机构" url="querySTeamOrgList.do" reqParams="team_no=${context.STeam.team_no}" initial="true" needFlush="true"/>
	</emp:tabGroup>
	
</body>
</html>
</emp:page>
