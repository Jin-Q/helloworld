<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>用于维护</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshFncAccPayable() {
		FncDetailBase_tabs.tabs.FncAccPayable_tab.refresh();
	};
	function refreshFncAccReceivable() {
		FncDetailBase_tabs.tabs.FncAccReceivable_tab.refresh();
	};
	function refreshFncAssure() {
		FncDetailBase_tabs.tabs.FncAssure_tab.refresh();
	};
	function refreshFncFixedAsset() {
		FncDetailBase_tabs.tabs.FncFixedAsset_tab.refresh();
	};
	function refreshFncInventory() {
		FncDetailBase_tabs.tabs.FncInventory_tab.refresh();
	};
	function refreshFncInvestment() {
		FncDetailBase_tabs.tabs.FncInvestment_tab.refresh();
	};
	function refreshFncLoan() {
		FncDetailBase_tabs.tabs.FncLoan_tab.refresh();
	};
	function refreshFncOtherPayable() {
		FncDetailBase_tabs.tabs.FncOtherPayable_tab.refresh();
	};
	function refreshFncOtherRecv() {
		FncDetailBase_tabs.tabs.FncOtherRecv_tab.refresh();
	};
	function refreshFncProject() {
		FncDetailBase_tabs.tabs.FncProject_tab.refresh();
	};

	function doUpdateFncDetailBase(){

		var form = document.getElementById("submitForm");
		var result = FncDetailBase._checkAll();
		if(result){
			FncDetailBase._toForm(form)
			form.submit();
		}else alert("请输入必填项！");
}


	function doReturn(){
        var paramStr="FncDetailBase.cus_id="+FncDetailBase.cus_id._obj.element.value;
		var url = '<emp:url action="queryFncDetailBaseList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;		
	}
	

	

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateFncDetailBaseRecord.do" method="POST">
		<emp:gridLayout id="FncDetailBaseGroup" title="报表明细基表" maxColumn="2">
			<emp:text id="FncDetailBase.pk" label="PK" maxlength="40" hidden="true" required="true" readonly="true" />
			<emp:text id="FncDetailBase.cus_id" label="客户码" maxlength="30" readonly="true" required="true" />
			<emp:text id="FncDetailBase.fnc_ym" label="年月" maxlength="6"  required="true" readonly="true" onblur="checkYM(this)"/>
			<emp:text id="FncDetailBase.input_id" label="登记人" hidden="true"  maxlength="20" readonly="true" required="false" />
			<emp:text id="FncDetailBase.input_br_id" label="登记机构" hidden="true"  maxlength="20" readonly="true" required="false" />
			<emp:date id="FncDetailBase.input_date" label="登记日期" hidden="true"  required="false" readonly="true" />
			<emp:text id="FncDetailBase.last_upd_id" label="更新人" maxlength="20" hidden="true"  required="false" readonly="true" />
			<emp:date id="FncDetailBase.last_upd_date" label="更新日期" required="false" hidden="true"  readonly="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="return" label="返回"/>
		</div>
		
	</emp:form>
	<emp:tabGroup id="FncDetailBase_tabs" mainTab="FncAccPayable_tab">
		<emp:tab id="FncAccPayable_tab" label="应付账款" url="queryFncDetailBaseFncAccPayableList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;" initial="true"/>
				
		<emp:tab id="FncAccReceivable_tab" label="应收账款" url="queryFncDetailBaseFncAccReceivableList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
		
		<emp:tab id="FncInventory_tab" label="主要存货" url="queryFncDetailBaseFncInventoryList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
				
		<emp:tab id="FncInvestment_tab" label="主要投资" url="queryFncDetailBaseFncInvestmentList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
				
		<emp:tab id="FncOtherPayable_tab" label="其它应付款" url="queryFncDetailBaseFncOtherPayableList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
				
		<emp:tab id="FncOtherRecv_tab" label="其它应收款" url="queryFncDetailBaseFncOtherRecvList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
				
		<emp:tab id="FncProject_tab" label="在建工程" url="queryFncDetailBaseFncProjectList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
		
		<emp:tab id="FncOrDebt_tab" label="或有负债" url="queryFncDetailBaseFncOrDebtList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/>
			
			
	</emp:tabGroup>
</body>
</html>
</emp:page>

			
		<!-- emp:tab id="FncAssure_tab" label="对外担保及表外业务" url="queryFncDetailBaseFncAssureList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/-->	
		<!-- emp:tab id="FncFixedAsset_tab" label="固定资产" url="queryFncDetailBaseFncFixedAssetList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/-->
		<!-- emp:tab id="FncLoan_tab" label="借款明细" url="queryFncDetailBaseFncLoanList.do" reqParams="FncDetailBase.pk=$FncDetailBase.pk;"/ -->
		


	
	