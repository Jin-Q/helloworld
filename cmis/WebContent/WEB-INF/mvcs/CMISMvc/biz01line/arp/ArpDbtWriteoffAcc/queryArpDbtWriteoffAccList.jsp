<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpDbtWriteoffAcc._toForm(form);
		ArpDbtWriteoffAccList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.ArpDbtWriteoffAccGroup.reset();
	};

	function doViewArpDbtWriteoffAcc() {
		var paramStr = ArpDbtWriteoffAccList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtWriteoffAccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
		
	/*--user code begin--*/
	function returnCus(data){
		ArpDbtWriteoffAcc.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpDbtWriteoffAccGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="ArpDbtWriteoffAcc.cus_id" label="客户码"  
			url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
			<emp:text id="ArpDbtWriteoffAcc.bill_no" label="借据编号" />
			<emp:text id="ArpDbtWriteoffAcc.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewArpDbtWriteoffAcc" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpDbtWriteoffAccList" pageMode="true" url="pageArpDbtWriteoffAccQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_type" label="产品名称" />
		<emp:text id="loan_amt" label="借据金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="借据余额" dataType="Currency"/>
		<emp:text id="writeoff_cap" label="核销本金" dataType="Currency"/>
		<emp:text id="writeoff_int" label="核销利息" dataType="Currency"/>
		<emp:text id="bill_writeoff_date" label="核销日期" />
		<emp:text id="writeoff_org_displayname" label="核销机构" />
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>