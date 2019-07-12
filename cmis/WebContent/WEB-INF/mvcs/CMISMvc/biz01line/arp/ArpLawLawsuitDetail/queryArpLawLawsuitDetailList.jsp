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
		ArpLawLawsuitDetail._toForm(form);
		ArpLawLawsuitDetailList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpLawLawsuitDetailGroup.reset();
	};
	
	function doGetUpdateArpLawLawsuitDetailPage() {
		var paramStr = ArpLawLawsuitDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawLawsuitDetail() {
		var paramStr = ArpLawLawsuitDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawLawsuitDetailPage() {
		var url = '<emp:url action="getArpLawLawsuitDetailAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawLawsuitDetail() {
		var paramStr = ArpLawLawsuitDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawLawsuitDetailRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
	};

	function returnCus(data){
		ArpLawLawsuitDetail.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm"></form>	
	<emp:gridLayout id="ArpLawLawsuitDetailGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="ArpLawLawsuitDetail.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="ArpLawLawsuitDetail.bill_no" label="借据编号" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawLawsuitDetailPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawLawsuitDetailPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawLawsuitDetail" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawLawsuitDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawLawsuitDetailList" pageMode="true" url="pageArpLawLawsuitDetailQuery.do?serno=${context.serno}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>		
		<emp:text id="lawsuit_cap" label="诉讼本金" dataType="Currency" />
		<emp:text id="lawsuit_int" label="诉讼利息" dataType="Currency" />
		<emp:text id="lawsuit_sub" label="诉讼标的" dataType="Currency" />
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>