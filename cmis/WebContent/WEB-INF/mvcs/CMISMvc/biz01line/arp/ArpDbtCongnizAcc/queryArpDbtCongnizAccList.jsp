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
		ArpDbtCongnizAcc._toForm(form);
		ArpDbtCongnizAccList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpDbtCongnizAccGroup.reset();
	};
	
	function doGetUpdateArpDbtCongnizAccPage() {
		var paramStr = ArpDbtCongnizAccList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtCongnizAccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpDbtCongnizAcc() {
		var paramStr = ArpDbtCongnizAccList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtCongnizAccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpDbtCongnizAccPage() {
		var url = '<emp:url action="getArpDbtCongnizAccAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpDbtCongnizAcc() {
		var paramStr = ArpDbtCongnizAccList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpDbtCongnizAccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function returnCus(data){
		ArpDbtCongnizAcc.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpDbtCongnizAccGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="ArpDbtCongnizAcc.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
			<emp:text id="ArpDbtCongnizAcc.bill_no" label="借据编号" />
			<emp:text id="ArpDbtCongnizAcc.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpDbtCongnizAccPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpDbtCongnizAccPage" label="修改" op="update"/>
		<emp:button id="deleteArpDbtCongnizAcc" label="删除" op="remove"/>
		<emp:button id="viewArpDbtCongnizAcc" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpDbtCongnizAccList" pageMode="true" url="pageArpDbtCongnizAccQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_type" label="产品名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_amt" label="借据金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="借据余额" dataType="Currency"/>
		<emp:text id="bad_dbt_balance" label="呆账金额" dataType="Currency"/>
		<emp:text id="congniz_date" label="认定日期" />
		<emp:text id="input_id_displayname" label="经办人" />		
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>