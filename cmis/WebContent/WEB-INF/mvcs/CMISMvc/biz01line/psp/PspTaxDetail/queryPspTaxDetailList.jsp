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
		PspTaxDetail._toForm(form);
		PspTaxDetailList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspTaxDetailPage() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspTaxDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspTaxDetail() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspTaxDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspTaxDetailPage() {
		var url = '<emp:url action="getPspTaxDetailAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspTaxDetail() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspTaxDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspTaxDetailGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspTaxDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspTaxDetailPage" label="修改" op="update"/>
		<emp:button id="deletePspTaxDetail" label="删除" op="remove"/>
		<emp:button id="viewPspTaxDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspTaxDetailList" pageMode="false" url="pagePspTaxDetailQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="tax_regi_no" label="税务登记证号" />
		<emp:text id="tax_type" label="税费类型" />
		<emp:text id="tax_amt" label="缴费金额" />
		<emp:text id="paid_start_date" label="税务缴纳起始日期" />
		<emp:text id="paid_end_date" label="税务缴纳结束日期" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    