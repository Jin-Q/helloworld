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
		PspOrderAnaly._toForm(form);
		PspOrderAnalyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspOrderAnalyPage() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspOrderAnalyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspOrderAnaly() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspOrderAnalyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspOrderAnalyPage() {
		var url = '<emp:url action="getPspOrderAnalyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspOrderAnaly() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspOrderAnalyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspOrderAnalyGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspOrderAnalyPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspOrderAnalyPage" label="修改" op="update"/>
		<emp:button id="deletePspOrderAnaly" label="删除" op="remove"/>
		<emp:button id="viewPspOrderAnaly" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspOrderAnalyList" pageMode="false" url="pagePspOrderAnalyQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户编码" />
		<emp:text id="rcver_name" label="需方名称" />
		<emp:text id="order_date" label="签订时间" />
		<emp:text id="prd_name" label="产品名称" />
		<emp:text id="model_no" label="型号" />
		<emp:text id="unit_price" label="单价" />
		<emp:text id="amt" label="金额" />
		<emp:text id="provid_date" label="供货时间" />
		<emp:text id="qnt" label="供货数量" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    