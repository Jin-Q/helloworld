<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doNext(){
		if(!ArpCollDispApp._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		ArpCollDispApp._toForm(form);
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
					window.location.reload();
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
	};		
	function doReturn() {
		var url = '<emp:url action="queryArpCollDispAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//返回主管机构
	function getOrganName(data){
		ArpCollDispApp.manager_br_id._setValue(data.organno._getValue());
		ArpCollDispApp.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	//返回主管客户经理	
	function setconId(data){
		ArpCollDispApp.manager_id._setValue(data.actorno._getValue());
		ArpCollDispApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpCollDispApp.manager_br_id._setValue(data.orgid._getValue());
		ArpCollDispApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//ArpCollDispApp.manager_br_id_displayname._obj._renderReadonly(true);
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
					ArpCollDispApp.manager_br_id._setValue(jsonstr.org);
					ArpCollDispApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					ArpCollDispApp.manager_br_id._setValue("");
					ArpCollDispApp.manager_br_id_displayname._setValue("");
					ArpCollDispApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpCollDispApp.manager_id._getValue();
					ArpCollDispApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpCollDispApp.manager_br_id._setValue("");
					ArpCollDispApp.manager_br_id_displayname._setValue("");
					ArpCollDispApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpCollDispApp.manager_id._getValue();
					ArpCollDispApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpCollDispApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
</script>
</head>
<body class="page_content">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">		
	<emp:form id="submitForm" action="updateArpCollDispAppRecord.do" method="POST">
		<emp:gridLayout id="ArpCollDispAppGroup" title="抵债物处置申请" maxColumn="2">
			<emp:text id="ArpCollDispApp.serno" label="业务编号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:date id="ArpCollDispApp.app_date" label="申请日期" required="false" hidden="true"/>
			<emp:date id="ArpCollDispApp.end_date" label="办结日期" required="false" hidden="true"/>
			<emp:pop id="ArpCollDispApp.guaranty_no" label="抵债资产编号" required="true" url="queryArpCollDebtAccRePopList.do" returnMethod="setGuaranty" readonly="true"/>
			<emp:text id="ArpCollDispApp.guaranty_no_displayname" label="抵债资产名称"  required="false" readonly="true" colSpan="2"/>
			<emp:text id="ArpCollDispApp.debt_in_amt" label="抵入金额" maxlength="16" required="true" dataType="Currency" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDispApp.disp_amt" label="处置金额" maxlength="16" required="true" dataType="Currency" colSpan="2"/>
			
			<emp:select id="ArpCollDispApp.asset_disp_mode" label="资产处置方式" required="true" dictname="STD_ASSET_DISP_MODEL" readonly="true"/>
			<emp:text id="ArpCollDispApp.fore_disp_expense" label="预计处置费用" maxlength="16" required="true" dataType="Currency" />
			
			<emp:textarea id="ArpCollDispApp.disp_memo" label="处置说明" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDispApp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDispApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpCollDispAppGroup" title="登记信息" maxColumn="2">
			<emp:pop id="ArpCollDispApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpCollDispApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="ArpCollDispApp.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="ArpCollDispApp.input_br_id_displayname" label="登记机构"  required="false" readonly="true" defvalue="$organName"/>
			<emp:text id="ArpCollDispApp.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDispApp.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDispApp.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="ArpCollDispApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="ArpCollDispApp.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			
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
