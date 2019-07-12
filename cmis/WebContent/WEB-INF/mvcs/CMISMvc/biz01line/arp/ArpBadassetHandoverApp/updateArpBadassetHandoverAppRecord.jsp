<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function idReceiver(data){
		ArpBadassetHandoverApp.rcv_person._setValue(data.actorno._getValue());
		ArpBadassetHandoverApp.rcv_person_displayname._setValue(data.actorname._getValue());
	};
	function orgReceiver(data){
		ArpBadassetHandoverApp.rcv_org._setValue(data.organno._getValue());
		ArpBadassetHandoverApp.rcv_org_displayname._setValue(data.organname._getValue());
		ArpBadassetHandoverApp.rcv_person._setValue();
		ArpBadassetHandoverApp.rcv_person_displayname._setValue('');
		var url="<emp:url action='getAllSUserPopListOp.do'/>&restrictUsed=false&flag=receive&receiveOrg="+data.organno._getValue('');
		ArpBadassetHandoverApp.rcv_person_displayname._obj.config.url = EMPTools.encodeURI(url);alert(url);
	};
	function doSubmits(){
		doPubUpdate(ArpBadassetHandoverApp);
	};
	function doReturn() {
		if(menuId=='ACC_badasset'){
			var url = '<emp:url action="queryArpBadassetAccList.do"/>';
		}else{
			var url = '<emp:url action="queryArpBadassetHandoverAppList.do"/>';
		}		
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doFlow(){
		var _status = document.getElementById("ArpBadassetHandoverApp.approve_status").value;
        if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'&&_status!= '993'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
		}
		var paramStr = document.getElementById("ArpBadassetHandoverApp.serno").value;
		var cus_id = document.getElementById("ArpBadassetHandoverApp.cus_id").value;
		var cus_name = document.getElementById("ArpBadassetHandoverApp.cus_id_displayname").value;
		if (paramStr != null) {
			WfiJoin.table_name._setValue("ArpBadassetHandoverApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("021");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("不良资产移交申请流程");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	function doLoad(){
		menuId = "${context.menuId}";
		addContForm(ArpBadassetHandoverApp);
		addCusForm(ArpBadassetHandoverApp);
		addBillForm(ArpBadassetHandoverApp);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="不良资产移交信息" id="main_tabs">
	<emp:form id="submitForm" action="updateArpBadassetHandoverAppRecord.do" method="POST">
		<emp:gridLayout id="ArpBadassetHandoverAppGroup" maxColumn="2" title="借据信息">	
			<emp:text id="ArpBadassetHandoverApp.bill_no" label="借据编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverApp.cont_no" label="合同编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverApp.cont_no_displayname" label="中文合同编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverApp.prd_id_displayname" label="产品类别" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverApp.cus_id" label="客户码" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverApp.cus_id_displayname" label="客户名称" colSpan="2" readonly="true"
			required="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="ArpBadassetHandoverApp.cur_type" label="币种" required="true" readonly="true"  dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="ArpBadassetHandoverApp.loan_amt" label="借据金额" required="true" readonly="true" dataType="Currency"/>
			<emp:text id="ArpBadassetHandoverApp.loan_balance" label="借据余额" required="true" readonly="true" dataType="Currency"/>
			<emp:select id="ArpBadassetHandoverApp.five_class" label="五级分类标志" required="true" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="ArpBadassetHandoverApp.four_class" label="四级分类标志" required="true" readonly="true" dictname="STD_ZB_FOUR_SORT"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpBadassetHandoverAppGroup" maxColumn="2" title="移交信息">	
			<emp:text id="ArpBadassetHandoverApp.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:select id="ArpBadassetHandoverApp.handover_resn" label="移交原因" required="true" dictname="STD_ZB_HANDOVER_RESN" />			
			<emp:text id="ArpBadassetHandoverApp.fount_manager_br_id_displayname" label="原管理机构" required="true" readonly="true"/>
			<emp:text id="ArpBadassetHandoverApp.fount_manager_id_displayname" label="原主管客户经理" required="true" readonly="true"/>
			<emp:pop id="ArpBadassetHandoverApp.rcv_org_displayname" label="接收机构" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver" required="true" readonly="true"/>
			<emp:pop id="ArpBadassetHandoverApp.rcv_person_displayname" label="接收人员" 
			url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" required="true"  />
			<emp:textarea id="ArpBadassetHandoverApp.bad_resn" label="不良成因及分析" maxlength="250" required="true" colSpan="2" />
			<emp:textarea id="ArpBadassetHandoverApp.collect_measures" label="清收措施" maxlength="250" required="true" colSpan="2" />		
			<emp:textarea id="ArpBadassetHandoverApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpBadassetHandoverApp.app_date" label="申请日期" required="true" hidden="true"/>
			<emp:date id="ArpBadassetHandoverApp.over_date" label="办结日期" required="false" hidden="true"/>
			<emp:text id="ArpBadassetHandoverApp.fount_manager_id" label="原主管客户经理" maxlength="20" required="true" hidden="true" />
			<emp:text id="ArpBadassetHandoverApp.fount_manager_br_id" label="原管理机构" maxlength="20" required="true" hidden="true" />
			<emp:text id="ArpBadassetHandoverApp.rcv_person" label="接收人员" maxlength="20" required="true" hidden="true" />
			<emp:text id="ArpBadassetHandoverApp.rcv_org" label="接收机构" maxlength="20" required="true" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpBadassetHandoverAppGroup" maxColumn="2" title="登记信息">			
			<emp:text id="ArpBadassetHandoverApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpBadassetHandoverApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="ArpBadassetHandoverApp.input_id" label="登记人" required="true"  hidden="true"/>
			<emp:text id="ArpBadassetHandoverApp.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpBadassetHandoverApp.input_date" label="登记日期" required="true"   readonly="true" />
			<emp:select id="ArpBadassetHandoverApp.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存"/>
			<emp:button id="flow" label="提交"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
