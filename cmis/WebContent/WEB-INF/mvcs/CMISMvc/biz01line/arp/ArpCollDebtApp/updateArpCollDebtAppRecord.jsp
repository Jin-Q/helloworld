<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
function returnCus(data){
	ArpCollDebtApp.cus_id._setValue(data.cus_id._getValue());
	ArpCollDebtApp.cus_id_displayname._setValue(data.cus_name._getValue());
};
function doReturn() {
	var url = '<emp:url action="queryArpCollDebtAppList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};
function doNext(){
	if(!ArpCollDebtApp._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	ArpCollDebtApp._toForm(form);
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
				alert("修改成功!");
				window.location.reload();
			}else {
				alert("修改失败!"); 
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
//返回主管客户经理	
function setconId(data){
	ArpCollDebtApp.manager_id._setValue(data.actorno._getValue());
	ArpCollDebtApp.manager_id_displayname._setValue(data.actorname._getValue());
	ArpCollDebtApp.manager_br_id._setValue(data.orgid._getValue());
	ArpCollDebtApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
	//ArpCollDebtApp.manager_br_id_displayname._obj._renderReadonly(true);
	doOrgCheck();
}

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
				ArpCollDebtApp.manager_br_id._setValue(jsonstr.org);
				ArpCollDebtApp.manager_br_id_displayname._setValue(jsonstr.orgName);
			}else if("more" == flag){//客户经理属于多个机构
				ArpCollDebtApp.manager_br_id._setValue("");
				ArpCollDebtApp.manager_br_id_displayname._setValue("");
				ArpCollDebtApp.manager_br_id_displayname._obj._renderReadonly(false);
				var manager_id = ArpCollDebtApp.manager_id._getValue();
				ArpCollDebtApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
			}else if("yteam"==flag){
				ArpCollDebtApp.manager_br_id._setValue("");
				ArpCollDebtApp.manager_br_id_displayname._setValue("");
				ArpCollDebtApp.manager_br_id_displayname._obj._renderReadonly(false);
				var manager_id = ArpCollDebtApp.manager_id._getValue();
				ArpCollDebtApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
			}
		}
	};
	var handleFailure = function(o) {
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var manager_id = ArpCollDebtApp.manager_id._getValue();
	var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
}
//返回主管机构
function getOrganName(data){
	ArpCollDebtApp.manager_br_id._setValue(data.organno._getValue());
	ArpCollDebtApp.manager_br_id_displayname._setValue(data.organname._getValue());
}
function doLoad(){
	addCusForm(ArpCollDebtApp);
	document.getElementById("main_tabs").href="javascript:reLoadx();";
}
function reLoadx(){  
	var url = "";
	var serno=ArpCollDebtApp.serno._getValue();
	url = '<emp:url action="getArpCollDebtAppUpdatePage.do"/>?serno='+serno;
	url = EMPTools.encodeURI(url);
	window.location = url;      
};
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
		<emp:form id="submitForm" action="updateArpCollDebtAppRecord.do" method="POST">
		<emp:gridLayout id="ArpCollDebtAppGroup" title="以物抵债申请" maxColumn="2">
			<emp:text id="ArpCollDebtApp.serno" label="业务编号" maxlength="40" required="false" readonly="true" colSpan="2"/>
			<emp:text id="ArpCollDebtApp.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="ArpCollDebtApp.cus_id_displayname" label="客户名称" required="false" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:date id="ArpCollDebtApp.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:date id="ArpCollDebtApp.end_date" label="办结日期" required="false" hidden="true" />
			<emp:select id="ArpCollDebtApp.debt_mode" label="抵债方式" required="true" dictname="STD_ZB_DEBT_MODEL" />
			<emp:text id="ArpCollDebtApp.debt_in_amt" label="抵入金额（元）" maxlength="12" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.debt_cap" label="抵债本金（元）" maxlength="12" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.inner_int" label="抵债表内利息（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.off_int" label="抵债表外利息（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.debt_expense" label="抵债费用（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="ArpCollDebtApp.coll_debt_resn" label="以物抵债理由" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDebtApp.collect_addr" label="收取地点" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDebtApp.asset_status" label="资产现状" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDebtApp.is_handover" label="是否移交管理" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpCollDebtApp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDebtApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpCollDebtAppGroup" title="登记信息" maxColumn="2">
			<emp:text id="ArpCollDebtApp.manager_id" label="责任人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.manager_br_id" label="责任机构" maxlength="40" required="false" hidden="true"/>
			<emp:pop id="ArpCollDebtApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpCollDebtApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="ArpCollDebtApp.input_id" label="登记人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.input_br_id" label="登记机构" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtApp.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="ArpCollDebtApp.input_date" label="登记日期" required="false" readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
  </emp:tab>
  <emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
</body>
</html>
</emp:page>
