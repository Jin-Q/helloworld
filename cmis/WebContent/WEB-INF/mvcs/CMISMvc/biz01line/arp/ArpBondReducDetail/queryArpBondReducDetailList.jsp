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
		ArpBondReducDetail._toForm(form);
		ArpBondReducDetailList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpBondReducDetailGroup.reset();
	};

	function doGetUpdateArpBondReducDetailPage() {
		var paramStr = ArpBondReducDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBondReducDetailUpdatePage.do"/>?'+paramStr+postStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpBondReducDetail() {
		var paramStr = ArpBondReducDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBondReducDetailViewPage.do"/>?'+paramStr+postStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpBondReducDetailPage() {
		var url = '<emp:url action="getArpBondReducDetailAddPage.do"/>'+postStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpBondReducDetail() {
		var paramStr = ArpBondReducDetailList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpBondReducDetailRecord.do"/>?'+paramStr;
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
	<emp:gridLayout id="ArpBondReducDetailGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="ArpBondReducDetail.bill_no" label="借据编号" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpBondReducDetailPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpBondReducDetailPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpBondReducDetail" label="删除" op="remove"/>
		<emp:actButton id="viewArpBondReducDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpBondReducDetailList" pageMode="true" url="pageArpBondReducDetailQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="inner_owe_int" label="表内欠息" dataType="Currency" />
		<emp:text id="out_owe_int" label="表外欠息" dataType="Currency" />
		<emp:text id="reduc_cap" label="减免本金" dataType="Currency"/>
		<emp:text id="reduc_inner_owe_int" label="减免表内利息" dataType="Currency"/>
		<emp:text id="reduc_out_owe_int" label="减免表外利息" dataType="Currency"/>
	</emp:table>
	
</body>
</html>
</emp:page>