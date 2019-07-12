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
		PspBankContacc._toForm(form);
		PspBankContaccList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspBankContaccPage() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspBankContaccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspBankContacc() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspBankContaccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspBankContaccPage() {
		var url = '<emp:url action="getPspBankContaccAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspBankContacc() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspBankContaccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspBankContaccGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspBankContaccPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspBankContaccPage" label="修改" op="update"/>
		<emp:button id="deletePspBankContacc" label="删除" op="remove"/>
		<emp:button id="viewPspBankContacc" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspBankContaccList" pageMode="false" url="pagePspBankContaccQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="acct_no" label="账号" />
		<emp:text id="acct_name" label="账户名" />
		<emp:text id="acctsvcr_no" label="开户行行号" />
		<emp:text id="acctsvcr_name" label="开户行行名" />
		<emp:text id="orie_type" label="业务方向" />
		<emp:text id="qnt" label="笔数" />
		<emp:text id="amt" label="金额" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    