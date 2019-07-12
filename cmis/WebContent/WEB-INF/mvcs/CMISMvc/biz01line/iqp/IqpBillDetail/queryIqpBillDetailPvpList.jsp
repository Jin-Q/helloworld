<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function onload(){
		IqpBillDetail.status._setValue('02');
		doQuery();
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBillDetail._toForm(form);
		IqpBillDetailList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpBillDetail() {
		var paramStr = IqpBillDetailList._obj.getParamStr(['porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBillDetailViewAccPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBillDetailGroup.reset();
	};
	function returnCus(data){
		AccLoan.cus_id._setValue(data.cus_id._getValue());
	};
	/** 导出excel **/
	function doExcelSDuty(){
		var form = document.getElementById("queryForm");
		IqpBillDetailList._toForm(form);
		form.submit();
	}
	
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onload()">   
	<emp:form  method="POST" action="iqpBillDetailListToExcel.do" id="queryForm">
	
	<emp:gridLayout id="IqpBillDetailGroup" title="输入查询条件" maxColumn="2">   
			<emp:text id="IqpBillDetail.porder_no" label="汇票号码" />
			<emp:select id="IqpBillDetail.status" label="票据状态" dictname="STD_ZB_DRFT_STATUS"/>  
	</emp:gridLayout> 
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	
	<div align="left">
	    <emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="viewIqpBillDetail" label="查看" op="view"/>	 
	</div>

	<emp:table icollName="IqpBillDetailList" pageMode="true" url="pageIqpBillDetailPvpQuery.do">
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_isse_date" label="票据签发日" />
		<emp:text id="porder_end_date" label="汇票到期日" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency" />
		<emp:text id="isse_name" label="出票/付款人名称" />
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="aaorg_no" label="承兑人开户行行号" />
		<emp:text id="aaorg_name" label="承兑人开户行名称" />
		<emp:text id="aorg_no" label="承兑行行号" />
		<emp:text id="aorg_name" label="承兑行名称" />
		<emp:text id="aorg_type" label="承兑行类型" dictname="STD_AORG_ACCTSVCR_TYPE" hidden="true"/>
		<emp:text id="status" label="票据状态" dictname="STD_ZB_DRFT_STATUS"/>
		<emp:text id="dscnt_type" label="贴现状态" dictname="STD_ZB_RPDDSCNT_MODE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    