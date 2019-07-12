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
		PspIostoreDoc._toForm(form);
		PspIostoreDocList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspIostoreDocPage() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspIostoreDocUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspIostoreDoc() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspIostoreDocViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspIostoreDocPage() {
		var url = '<emp:url action="getPspIostoreDocAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspIostoreDoc() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspIostoreDocRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspIostoreDocGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspIostoreDocPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspIostoreDocPage" label="修改" op="update"/>
		<emp:button id="deletePspIostoreDoc" label="删除" op="remove"/>
		<emp:button id="viewPspIostoreDoc" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspIostoreDocList" pageMode="false" url="pagePspIostoreDocQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="goods_name" label="货物名称" />
		<emp:text id="qnt_unit" label="数量单位" />
		<emp:text id="qnt" label="数量" />
		<emp:text id="total_price" label="总价值" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="cus_id" label="客户码" />
	</emp:table>
	
</body>
</html>
</emp:page>
    