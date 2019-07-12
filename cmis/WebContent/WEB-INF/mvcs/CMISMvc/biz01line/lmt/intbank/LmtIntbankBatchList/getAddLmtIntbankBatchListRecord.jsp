<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
		function refreshLmtBatchCorre() {
			LmtIntbankBatchList_tabs.tabs.LmtBatchCorre_tab.refresh();
		};
		function doImport(){	
			var batch_cus_no =LmtIntbankBatchList.batch_cus_no._getValue();
			var crd_grade = LmtIntbankBatchList.cdt_lvl._getValue();		
			var url = '<emp:url action="queryCusSameOrgPop4Batch.do"/>?crd_grade='+ crd_grade + '&batch_cus_no='+batch_cus_no;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		};
		function doDelete() {
			var paramStr = LmtBatchCorreList._obj.getParamStr(['batch_cus_no','cus_id']);
			if (paramStr != null) {
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtIntbankBatchListLmtBatchCorreRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			} else {
				alert('请先选择一条记录！');
			}
		};
	
		function doView() {
			var paramStr = LmtBatchCorreList._obj.getParamStr(['cus_id']);
			if (paramStr!=null) {
				var url = '<emp:url action="getIntbankViewPage.do"/>?cus_id='+paramStr;
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow');
			}else {
				alert('请先选择一条记录！');
			}
		};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtIntbankBatchListRecord.do" method="POST">
	<emp:tab label="" id="">
		<emp:gridLayout id="LmtIntbankBatchListGroup" title="同业客户批量名单维护" maxColumn="2">		
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" maxlength="32" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.batch_cus_type" label="批量客户类型" maxlength="32" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.cdt_lvl" label="信用等级" required="true" dictname="STD_ZB_FINA_GRADE" readonly="true"/>		
			<emp:text id="LmtIntbankBatchList.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="LmtIntbankBatchList.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.input_id" label="登记人" maxlength="20"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id" label="登记机构" maxlength="32" hidden="true"/>
			<emp:date id="LmtIntbankBatchList.start_date" label="生效日期"  hidden="true"/>
			<emp:date id="LmtIntbankBatchList.end_date" label="到期日期"  hidden="true"/>
			<emp:select id="LmtIntbankBatchList.state" label="状态"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.serno" label="业务编号" maxlength="32" hidden="true"/>						
			<emp:text id="LmtIntbankBatchList.manager_id" label="责任人" required="false" hidden="true"/>
			<emp:text id="LmtIntbankBatchList.manager_br_id" label="管理机构"  required="false" hidden="true"/>
			<emp:select id="LmtIntbankBatchList.approve_status" label="审批状态"  hidden="true"/>		
		</emp:gridLayout>                   
		<div align="left">	
				<emp:button id="import" label="引入" />		
				<emp:button id="delete" label="剔除" />
				<emp:button id="intro" label="导入" op=""/>
				<emp:button id="view" label="查看" />
	    </div>	
		<emp:table icollName="LmtBatchCorreList" pageMode="true" url="pageLmtIntbankBatchListLmtBatchCorreQuery.do" reqParams="LmtIntbankBatchList.batch_cus_no=$LmtIntbankBatchList.batch_cus_no;">
			<emp:text id="batch_cus_no" label="批量客户编号" />
			<emp:text id="cus_id" label="客户码" />
			<emp:text id="same_org_cnname" label="同业机构(行)名称" />
			<emp:select id="same_org_type" label="同业机构类型" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:select id="cust_level" label="监管评级" dictname="STD_ZB_CUSTD_RATE"/>
			<emp:text id="assets" label="总资产" dataType="Currency"/>
			<emp:text id="paid_cap_amt" label="实收资本" dataType="Currency"/>
			<emp:text id="input_id_displayname" label="登记人" />
			<emp:text id="input_br_id_displayname" label="登记机构" />
			<emp:text id="input_date" label="登记日期" defvalue="$OPENDAY"/>
			<emp:text id="input_id" label="登记人" hidden="true"/>
			<emp:text id="input_br_id" label="登记机构" hidden="true"/>		
	   </emp:table>
	</emp:tab>
</emp:form>	
</body>
</html>
</emp:page>
