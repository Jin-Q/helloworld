<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpDbtWriteoffDetail._toForm(form);
		ArpDbtWriteoffDetailList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpDbtWriteoffDetailGroup.reset();
	};

	function doViewArpDbtWriteoffDetail() {
		var paramStr = ArpDbtWriteoffDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtWriteoffDetailViewPage.do"/>?'+paramStr+postStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpDbtWriteoffDetailPage() {
		var url = '<emp:url action="getArpDbtWriteoffDetailAddPage.do"/>'+postStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpDbtWriteoffDetail() {
		var paramStr = ArpDbtWriteoffDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpDbtWriteoffDetailRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm"></form>	
	<emp:gridLayout id="ArpDbtWriteoffDetailGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="ArpDbtWriteoffDetail.bill_no" label="借据编号" />
		<emp:text id="ArpDbtWriteoffDetail.cont_no" label="合同编号" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpDbtWriteoffDetailPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpDbtWriteoffDetail" label="删除" op="remove"/>
		<emp:actButton id="viewArpDbtWriteoffDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpDbtWriteoffDetailList" pageMode="true" url="pageArpDbtWriteoffDetailQuery.do?serno=${context.serno}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="loan_amt" label="借据金额" dataType="Currency" />
		<emp:text id="loan_balance" label="借据余额" dataType="Currency" />
		<emp:text id="distr_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="writeoff_amt" label="核销总金额" dataType="Currency" />
		<emp:text id="writeoff_cap" label="核销本金" dataType="Currency" />
		<emp:text id="writeoff_int" label="核销利息" dataType="Currency" />
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>