<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	/** 登记信息begin **/
	function setconId(data){
		ArpLawLawsuitApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpLawLawsuitApp.manager_id._setValue(data.actorno._getValue());
		ArpLawLawsuitApp.manager_br_id._setValue(data.orgid._getValue());
		ArpLawLawsuitApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//ArpLawLawsuitApp.manager_br_id_displayname._obj._renderReadonly(true);
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
					ArpLawLawsuitApp.manager_br_id._setValue(jsonstr.org);
					ArpLawLawsuitApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					ArpLawLawsuitApp.manager_br_id._setValue("");
					ArpLawLawsuitApp.manager_br_id_displayname._setValue("");
					ArpLawLawsuitApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpLawLawsuitApp.manager_id._getValue();
					ArpLawLawsuitApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpLawLawsuitApp.manager_br_id._setValue("");
					ArpLawLawsuitApp.manager_br_id_displayname._setValue("");
					ArpLawLawsuitApp.manager_br_id_displayname._obj._renderReadonly(false);
					ArpLawLawsuitApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpLawLawsuitApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		ArpLawLawsuitApp.manager_br_id._setValue(data.organno._getValue());
		ArpLawLawsuitApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	function doReturn() {
		var url = '<emp:url action="queryArpLawLawsuitAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doSubmits(){
		url = '<emp:url action="getArpLawLawsuitAppUpdatePage.do"/>?restrictUsed=false&op=update&serno=';
		doPubAdd(url,ArpLawLawsuitApp);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addArpLawLawsuitAppRecord.do" method="POST">		
		<emp:gridLayout id="ArpLawLawsuitAppGroup" title="诉讼申请信息" maxColumn="2">
			<emp:text id="ArpLawLawsuitApp.serno" label="业务编号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:text id="ArpLawLawsuitApp.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" readonly="true" defvalue="0"/>
			<emp:text id="ArpLawLawsuitApp.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" readonly="true" defvalue="0"/>
			<emp:text id="ArpLawLawsuitApp.lawsuit_sub" label="诉讼标的" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0"/>
			<emp:text id="ArpLawLawsuitApp.other_fee" label="其他费用" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpLawLawsuitApp.executable_property_status" label="可执行财产状况" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitApp.law_disp_mode" label="法律处置方式" required="false" dictname="STD_ZB_LAW_DISP" />
			<emp:textarea id="ArpLawLawsuitApp.lawsuit_reason" label="起诉原因" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitApp.whether_invite_agt_lawyer" label="是否聘请代理律师" required="false" dictname="STD_ZX_YES_NO" />
			<emp:date id="ArpLawLawsuitApp.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawLawsuitAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpLawLawsuitApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpLawLawsuitApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpLawLawsuitApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpLawLawsuitApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpLawLawsuitApp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpLawLawsuitApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpLawLawsuitApp.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpLawLawsuitApp.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpLawLawsuitApp.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
			<emp:select id="ArpLawLawsuitApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"  defvalue="000" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>