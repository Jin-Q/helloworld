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
		ArpDbtBizRel._toForm(form);
		ArpDbtBizRelList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpDbtBizRelGroup.reset();
	};
	
	function doGetAddArpDbtBizRelPage() {
		var url = '<emp:url action="getArpDbtBizRelAddPage.do"/>'+postStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doViewArpDbtBizRel() {
		var paramStr = ArpDbtBizRelList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtBizRelViewPage.do"/>?'+paramStr+postStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doDeleteArpDbtBizRel() {
		var paramStr = ArpDbtBizRelList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpDbtBizRelRecord.do"/>?'+paramStr;
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
	<emp:gridLayout id="ArpDbtBizRelGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="ArpDbtBizRel.bill_no" label="借据编号" />
		<emp:text id="ArpDbtBizRel.cont_no" label="合同编号" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpDbtBizRelPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpDbtBizRel" label="删除" op="remove"/>
		<emp:actButton id="viewArpDbtBizRel" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpDbtBizRelList" pageMode="true" url="pageArpDbtBizRelQuery.do?serno=${context.serno}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="loan_amt" label="借据金额" dataType="Currency" />
		<emp:text id="loan_balance" label="借据余额"  dataType="Currency" />
		<emp:text id="owe_int" label="欠息累计"  dataType="Currency" />
		<emp:text id="distr_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>    