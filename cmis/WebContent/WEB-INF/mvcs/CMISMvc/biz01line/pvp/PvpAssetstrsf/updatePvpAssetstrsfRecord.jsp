<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="jsPvpComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:180px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>	
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
//-----------提交流程(目前只做保存和生成生成记录，暂时未加入流程)----------
function doFlow(){
	doSave("flow");  
	var form = document.getElementById("submitForm");
	var result = PvpLoanApp._checkAll();
	if(!result){
		return;
	}
	getApplyTypeByPrdId();
}
function doSave(data){
	var form = document.getElementById("submitForm");
	var result = PvpLoanApp._checkAll();
	if(!result){
		return;
	}
	PvpLoanApp._toForm(form);
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
				if(data != "flow"){
					alert("保存成功！");
				}
			}else {
				alert("保存失败!");
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
	var url = '<emp:url action="updatePvpRpddscantRecord.do"/>?menuId=${context.menuId}';
	url = EMPTools.encodeURI(url);
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
};
//-----------通过产品编号查询产品配置中使用流程类型----------
function getApplyTypeByPrdId(){
	var prdId = PvpLoanApp.prd_id._getValue();
	var url = '<emp:url action="getPvpApplyTypeByPrdId.do"/>?prdid='+prdId;
	url = EMPTools.encodeURI(url);
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
			var apply_type = jsonstr.apply_type;
			if(flag == "success"){
				doSubmitWF(apply_type);
			}else {
				alert(msg);
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
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};
//-----------提交流程----------
function doSubmitWF(apply_type){
	var serno = PvpLoanApp.serno._getValue();
	var approve_status = PvpLoanApp.approve_status._getValue();
	WfiJoin.table_name._setValue("PvpLoanApp");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
	var cus_id = PvpLoanApp.cus_id._getValue();
	var cus_name = PvpLoanApp.toorg_name._getValue();
	WfiJoin.cus_id._setValue(cus_id);
	WfiJoin.cus_name._setValue(cus_name);
	WfiJoin.prd_pk._setValue(PvpLoanApp.prd_id._getValue());
	WfiJoin.prd_name._setValue(PvpLoanApp.prd_id_displayname._getValue());
	WfiJoin.amt._setValue(PvpLoanApp.pvp_amt._getValue());
	WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue(apply_type);
	initWFSubmit(false);
};

function doReturn() {
	window.close();
};
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updatePvpRpddscantRecord.do" method="POST">
	  <emp:tabGroup mainTab="base_tab" id="mainTab" > 
		 <emp:tab label="出账信息" id="base_tab" needFlush="true" initial="true" >
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="出账基本信息">
			<emp:text id="PvpLoanApp.serno" label="出账流水号" maxlength="60" required="true" readonly="true"  colSpan="2" cssElementClass="emp_input"/> 
			<emp:text id="PvpLoanApp.prd_id" label="产品编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.prd_id_displayname" label="产品名称"  required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cus_id" label="交易对手行号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.toorg_name" label="交易对手行名" maxlength="80" required="false" colSpan="2" readonly="true" cssElementClass="emp_input2"/>	
			<emp:text id="PvpLoanApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_input" colSpan="2"/>
			<emp:text id="PvpLoanApp.bill_no" label="借据编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_input" hidden="true"/>      
		  </emp:gridLayout>
		  
         <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:text id="PvpLoanApp.cont_amt" label="合同金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    <emp:select id="PvpLoanApp.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="PvpLoanApp.pvp_amt" label="出账金额" maxlength="18" required="true" onblur="" dataType="Currency" readonly="true"/>	
   		    <emp:text id="PvpLoanApp.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
   		 </emp:gridLayout>
		
		 <emp:gridLayout id="" maxColumn="3" title="登记信息">   
		  	<emp:text id="PvpLoanApp.manager_br_id_displayname" label="管理机构"   required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id_displayname" label="入账机构" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" required="true"/>
		    <emp:select id="PvpLoanApp.flow_type" label="流程类型" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true" required="false"/>    
		    <emp:text id="PvpLoanApp.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
			<emp:date id="PvpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="PvpLoanApp.approve_status" label="审批状态" required="false" hidden="true" dictname="WF_APP_STATUS"/>
		    <emp:text id="PvpLoanApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id" label="入账机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" required="true"/>
		     <emp:text id="PvpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false" readonly="true"/>
		 </emp:gridLayout>
		 <div align="center">  
			<br>
			<emp:button id="flow" label="放入流程" />  
			<emp:button id="save" label="保存" />
			<emp:button id="return" label="关闭"/>
		</div>
		</emp:tab>
		<emp:tab label="资产转受让合同信息" id="subTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	</emp:tabGroup>
		
	</emp:form>
</body>
</html>
</emp:page>
