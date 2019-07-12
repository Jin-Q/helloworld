<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doNext(){
	if(!IqpAppMortAcess._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	IqpAppMortAcess._toForm(form);
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var serno = jsonstr.serno;
			if(flag == "success"){
				alert("新增成功!"); 
				var url = '<emp:url action="getIqpAppMortAcessUpdatePage.do"/>?serno='+serno;
				url = EMPTools.encodeURI(url);
				window.location=url;
			}else {
				alert("新增失败!"); 
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
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
}
function doReturn() {
	var url = '<emp:url action="queryIqpAppMortAcessList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};
//选择机构信息返回方法
function getOrgID(data){
	IqpAppMortAcess.manager_br_id._setValue(data.organno._getValue());
	IqpAppMortAcess.manager_br_id_displayname._setValue(data.organname._getValue());
};	

//选择责任人返回方法
function setconId(data){
	IqpAppMortAcess.manager_id_displayname._setValue(data.actorname._getValue());
	IqpAppMortAcess.manager_id._setValue(data.actorno._getValue());
	IqpAppMortAcess.manager_br_id._setValue(data.orgid._getValue());
	IqpAppMortAcess.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
	//IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(true);
	doOrgCheck();
};	
function doOrgCheck(){
	var handleSuccess = function(o) {
		if (o.responseText !== undefined) {
			try {
				var jsonstr = eval("(" + o.responseText + ")");
			} catch (e) {
				alert("Parse jsonstr define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			if("one" == flag){//客户经理只属于一个机构
				IqpAppMortAcess.manager_br_id._setValue(jsonstr.org);
				IqpAppMortAcess.manager_br_id_displayname._setValue(jsonstr.orgName);
			}else if("more" == flag){//客户经理属于多个机构
				IqpAppMortAcess.manager_br_id._setValue("");
				IqpAppMortAcess.manager_br_id_displayname._setValue("");
				IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(false);
				var manager_id = IqpAppMortAcess.manager_id._getValue();
				IqpAppMortAcess.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
			}else if("yteam"==flag){
				IqpAppMortAcess.manager_br_id._setValue("");
				IqpAppMortAcess.manager_br_id_displayname._setValue("");
				IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(false);
				IqpAppMortAcess.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
			}
		}
	};
	var handleFailure = function(o) {
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var manager_id = IqpAppMortAcess.manager_id._getValue();
	var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAppMortAcessRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppMortAcessGroup" title="押品目录准入申请" maxColumn="2">
			<emp:select id="IqpAppMortAcess.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_ADMIT_TYPE" defvalue="1" readonly="true"/>
			<emp:date id="IqpAppMortAcess.acsee_date" label="准入日期" required="true" defvalue="$OPENDAY"/>
			<emp:textarea id="IqpAppMortAcess.memo" label="准入描述" maxlength="500" required="false" colSpan="2" />
			
			<emp:pop id="IqpAppMortAcess.manager_id_displayname" label="责任人" required="true" readonly="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpAppMortAcess.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpAppMortAcess.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpAppMortAcess.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:text id="IqpAppMortAcess.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="IqpAppMortAcess.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:date id="IqpAppMortAcess.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpAppMortAcess.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppMortAcess.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:select id="IqpAppMortAcess.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
			<emp:text id="IqpAppMortAcess.serno" label="业务流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

