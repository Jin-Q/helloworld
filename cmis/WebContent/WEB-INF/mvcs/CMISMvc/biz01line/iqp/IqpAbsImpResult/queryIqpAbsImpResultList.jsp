<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>批量明细信息校验结果</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAbsImpResult._toForm(form);
		IqpAbsImpResultList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAbsImpResultPage() {
		var paramStr = IqpAbsImpResultList._obj.getParamStr(['pre_package_serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAbsImpResultUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAbsImpResult() {
		var paramStr = IqpAbsImpResultList._obj.getParamStr(['pre_package_serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAbsImpResultViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAbsImpResultPage() {
		var url = '<emp:url action="getIqpAbsImpResultAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doClose() {
		window.close();
	};
	
	function doReset(){
		page.dataGroups.IqpAbsImpResultGroup.reset();
	};
	function doExcel(){                                                                                             
		var form = document.getElementById("queryForm");
		form.submit();
		} 
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form  method="POST" action="iqpAbsImpResultListToExcel.do?pre_package_serno=${context.pre_package_serno}" id="queryForm" >
	</emp:form>
	<emp:table icollName="IqpAbsImpResultList" pageMode="true" url="pageIqpAbsImpResultQuery.do" reqParams="pre_package_serno=${context.pre_package_serno}">
		<emp:text id="batch_no" label="批次号" hidden="true"/>
		<emp:text id="pre_package_serno" label="预封包流水号" hidden="true"/>
		<emp:text id="bill_no" label="贷款台账账号" />
		<emp:text id="cont_type" label="合同类型" />
		<emp:text id="guar_type" label="担保类型" />
		<emp:text id="cus_id" label="客户编号" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="bill_balance" label="借据余额" />
		<emp:text id="bill_amt" label="借据金额" />
		<emp:text id="repay_type" label="还款方式" hidden="true"/>
		<emp:text id="loan_start_date" label="贷款起始日" />
		<emp:text id="loan_end_date" label="贷款到期日" />
		<emp:text id="five_class" label="五级分类" />
		<emp:text id="manager_br_id" label="管理机构" />
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="acc_status" label="台账状态" />
		<emp:text id="check_result" label="校验结果" dictname="STD_ABS_CHECK_RESULT"/>
		<emp:text id="error_msg" label="错误信息" />
	</emp:table>
	<div align="center" >
		<emp:button id="excel" label="导出" />
		<emp:button id="close" label="返回" />
	</div>
</body>
</html>
</emp:page>
    