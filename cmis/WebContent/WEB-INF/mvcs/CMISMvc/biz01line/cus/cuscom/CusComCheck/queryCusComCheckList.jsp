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
		CusComCheck._toForm(form);
		CusComCheckList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComCheckPage() {
		var paramStr = CusComCheckList._obj.getParamStr(['cus_id','stat_prd']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusComCheckUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComCheck() {
		var paramStr = CusComCheckList._obj.getParamStr(['cus_id','stat_prd']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusComCheckViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComCheckPage() {
		
		var url = '<emp:url action="getCusComCheckAddPage.do"/>?cus_id=${context.CusComCheck.cus_id}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComCheck() {
		var paramStr = CusComCheckList._obj.getParamStr(['cus_id','stat_prd']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComCheckRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusComCheckGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddCusComCheckPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusComCheckPage" label="修改" op="update"/>
		<emp:button id="deleteCusComCheck" label="删除" op="remove"/>
		<emp:button id="viewCusComCheck" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusComCheckList" pageMode="true" url="pageCusComCheckQuery.do?cus_id=${context.cus_id}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="stat_prd" label="月份" />
		<emp:text id="ele_cons" label="用电量(度)" />
		<emp:text id="water_cons" label="用水量(吨)" />
		<emp:text id="gas_cons" label="用气量(m³)" />
		<emp:text id="per_tax" label="增值税(元)" />
		<emp:text id="income_tax" label="所得税(元)" />
		<emp:text id="check_person" label="检查人" />
		<emp:text id="check_date" label="日期" />
		<emp:text id="remarks" label="备注" />
	</emp:table>
	
</body>
</html>
</emp:page>
    