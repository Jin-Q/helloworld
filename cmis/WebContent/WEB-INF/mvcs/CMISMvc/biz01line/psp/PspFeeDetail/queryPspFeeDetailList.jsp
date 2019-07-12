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
		PspFeeDetail._toForm(form);
		PspFeeDetailList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspFeeDetailPage() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspFeeDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspFeeDetail() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspFeeDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspFeeDetailPage() {
		var url = '<emp:url action="getPspFeeDetailAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspFeeDetail() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspFeeDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspFeeDetailGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspFeeDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspFeeDetailPage" label="修改" op="update"/>
		<emp:button id="deletePspFeeDetail" label="删除" op="remove"/>
		<emp:button id="viewPspFeeDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspFeeDetailList" pageMode="false" url="pagePspFeeDetailQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="last_regi_date" label="缴纳起始日期" />
		<emp:text id="regi_date" label="缴纳结束日期" />
		<emp:text id="paid_acct_no" label="缴费账号" />
		<emp:text id="paid_acct_name" label="缴费账户名" />
		<emp:text id="paid_type" label="缴费类别" />
		<emp:text id="qnt" label="数量" />
		<emp:text id="paid_amt" label="缴费金额" />
		<emp:text id="breach_amt" label="违约金" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    