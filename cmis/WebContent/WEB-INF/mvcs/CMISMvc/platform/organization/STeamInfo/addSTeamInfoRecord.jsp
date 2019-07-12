<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doReturn() {
	var url = '<emp:url action="querySTeamInfoList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};

function doSave(){
	var form = document.getElementById("submitForm");
	if(STeamInfo._checkAll()){
		STeamInfo._toForm(form);
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
					var team_no = STeamInfo.team_no._getValue();
					var url = '<emp:url action="getSTeamInfoUpdatePage.do"/>?team_no='+encodeURI(team_no);
					url = EMPTools.encodeURI(url);
					window.location=url;
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
function doCheck(){
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			if(flag == "success"){
			}else {
				alert("此团队编号已存在！");
				STeamInfo.team_no._setValue("");
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
	var team_no = STeamInfo.team_no._getValue();
	var url = '<emp:url action="checkSTeamInfoRecord.do"/>?team_no='+team_no;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSTeamInfoRecord.do" method="POST">
		
		<emp:gridLayout id="STeamInfoGroup" title="团队信息表" maxColumn="2">
			<emp:text id="STeamInfo.team_no" label="团队编号" maxlength="20" required="true" onblur="doCheck()"/>
			<emp:text id="STeamInfo.team_name" label="团队名称" maxlength="60" required="true" />
			<emp:text id="STeamInfo.team_type" label="团队类型" maxlength="20" required="false" />
			<emp:select id="STeamInfo.belg_line" label="归属条线" required="true" dictname="STD_ZB_BUSILINE" />
			<emp:textarea id="STeamInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="STeamInfoGroup" title="登记信息" maxColumn="2">
			<emp:text id="STeamInfo.input_id_displayname" label="登记人" required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="STeamInfo.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="STeamInfo.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="STeamInfo.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
			<emp:date id="STeamInfo.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

