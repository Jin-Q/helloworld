<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doSave(){
	if(CtrLimitCont._checkAll()){
		var form = document.getElementById("submitForm");
		CtrLimitCont._toForm(form);
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
					alert("签订成功！");
					window.location.reload();
				}else {
					alert("签订失败！");
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
		
	}
};

function getOrgID(data){
	CtrLimitCont.manager_br_id._setValue(data.organno._getValue());
	CtrLimitCont.manager_br_id_displayname._setValue(data.organname._getValue());
};
/**-------------流程提交检查---------------*/
function doSubWF(){
	if(CtrLimitApp._checkAll()){
		var form = document.getElementById("submitForm");
		CtrLimitApp._toForm(form);
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
					alert("保存成功！");
					doSubmitWF();
				}else {
					alert("保存出错！");
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
		
	}
};

/**-------------流程提交操作---------------*/
function doSubmitWF(){
	var serno = CtrLimitApp.serno._getValue();
	var cus_id = CtrLimitApp.cus_id._getValue();
	var cus_name = CtrLimitApp.cus_id_displayname._getValue();
	var approve_status = CtrLimitApp.approve_status._getValue();
	WfiJoin.table_name._setValue("CtrLimitApp");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
	WfiJoin.cus_id._setValue(cus_id);
	WfiJoin.cus_name._setValue(cus_name);
	WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue("018");
	initWFSubmit(false);
};	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="maintabs" id="maintabs">
		<emp:tab label="额度合同基本信息" id="maintabs">
			<emp:form id="submitForm" action="updateCtrLimitContRecord.do" method="POST">
				<emp:gridLayout id="CtrLimitContGroup" title="额度合同申请表" maxColumn="2">
					<emp:text id="CtrLimitCont.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="CtrLimitCont.serno" label="业务编号" maxlength="40" required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.cont_cn" label="中文合同编号" maxlength="200" required="true" />
					<emp:select id="CtrLimitCont.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" readonly="true"/>
					<emp:text id="CtrLimitCont.cus_id" label="客户码" maxlength="32" required="false" readonly="true"/>
					<emp:select id="CtrLimitCont.cur_type" label="币种"   dictname="STD_ZX_CUR_TYPE" required="true" readonly="false"/>
					<emp:text id="CtrLimitCont.cont_amt" label="合同金额" maxlength="16" required="true" readonly="false"/>
					<emp:date id="CtrLimitCont.start_date" label="起始日期" required="true" readonly="false"/>
					<emp:date id="CtrLimitCont.end_date" label="到期日期"  required="true" readonly="false"/>
					<emp:text id="CtrLimitCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" hidden="true" defvalue="200" required="false" />
					<emp:textarea id="CtrLimitCont.memo" label="备注" maxlength="200" required="false" readonly="true"/>
				</emp:gridLayout>
				
				<emp:gridLayout id="CtrLimitContGroup" title="机构信息" maxColumn="2">
					<emp:pop id="CtrLimitCont.manager_br_id_displayname" label="管理机构" defvalue="${context.organNo}" required="true" readonly="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
					<emp:text id="CtrLimitCont.input_id_displayname" label="登记人"  defvalue="${context.currentUserId}"  required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.input_br_id_displayname" label="登记机构" defvalue="${context.organNo}"  required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="CtrLimitCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" />
					<emp:text id="CtrLimitCont.input_id" label="登记人" maxlength="32" required="false" hidden="true" />
					<emp:text id="CtrLimitCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="save" label="保存" op="add"/>
					<emp:button id="subWF" label="放入流程" op="update"/>
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
