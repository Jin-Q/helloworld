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
		PspGuarantyValueReeval._toForm(form);
		PspGuarantyValueReevalList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspGuarantyValueReevalPage() {
		var paramStr = PspGuarantyValueReevalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspGuarantyValueReevalUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspGuarantyValueReeval() {
		var paramStr = PspGuarantyValueReevalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspGuarantyValueReevalViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspGuarantyValueReevalPage() {
		var url = '<emp:url action="getPspGuarantyValueReevalAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspGuarantyValueReeval() {
		var paramStr = PspGuarantyValueReevalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspGuarantyValueReevalRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspGuarantyValueReevalGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspGuarantyValueReevalPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspGuarantyValueReevalPage" label="修改" op="update"/>
		<emp:button id="deletePspGuarantyValueReeval" label="删除" op="remove"/>
		<emp:button id="viewPspGuarantyValueReeval" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspGuarantyValueReevalList" pageMode="false" url="pagePspGuarantyValueReevalQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="guaranty_no" label="担保品编号" />
		<emp:text id="batch_reeval_value" label="本期批量重估押品价值" />
		<emp:text id="reeval_value" label="本期建议押品价值" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    