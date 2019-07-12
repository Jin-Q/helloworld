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
		ArpLawLawsuitDtmana._toForm(form);
		ArpLawLawsuitDtmanaList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpLawLawsuitDtmanaGroup.reset();
	};

	function doGetUpdateArpLawLawsuitDtmanaPage() {
		var paramStr = ArpLawLawsuitDtmanaList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitDtmanaUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawLawsuitDtmana() {
		var paramStr = ArpLawLawsuitDtmanaList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitDtmanaViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawLawsuitDtmanaPage() {
		var url = '<emp:url action="getArpLawLawsuitDtmanaAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawLawsuitDtmana() {
		var paramStr = ArpLawLawsuitDtmanaList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawLawsuitDtmanaRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doLoad(){
		case_no = "${context.case_no}";
	};

	function returnCus(data){
		ArpLawLawsuitDtmana.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="ArpLawLawsuitDtmanaGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="ArpLawLawsuitDtmana.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="ArpLawLawsuitDtmana.bill_no" label="借据编号" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawLawsuitDtmanaPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawLawsuitDtmanaPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawLawsuitDtmana" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawLawsuitDtmana" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawLawsuitDtmanaList" pageMode="true" url="pageArpLawLawsuitDtmanaQuery.do?case_no=${context.case_no}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="lawsuit_cap" label="诉讼本金" dataType="Currency" />
		<emp:text id="lawsuit_int" label="诉讼利息" dataType="Currency" />
		<emp:text id="lawsuit_sub" label="诉讼标的" dataType="Currency" />
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    