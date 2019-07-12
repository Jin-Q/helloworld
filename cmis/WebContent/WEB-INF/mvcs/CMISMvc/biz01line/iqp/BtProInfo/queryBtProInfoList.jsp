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
		BtProInfo._toForm(form);
		BtProInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateBtProInfoPage() {
		var paramStr = BtProInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getBtProInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewBtProInfo() {
		var paramStr = BtProInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getBtProInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddBtProInfoPage() {
		var url = '<emp:url action="getBtProInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteBtProInfo() {
		var paramStr = BtProInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteBtProInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.BtProInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddBtProInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateBtProInfoPage" label="修改" op="update"/>
		<emp:button id="deleteBtProInfo" label="删除" op="remove"/>
		<emp:button id="viewBtProInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="BtProInfoList" pageMode="false" url="pageBtProInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="pro_cls" label="项目类别" />
		<emp:text id="pro_name" label="项目名称" />
		<emp:text id="pro_addr" label="项目地点" />
		<emp:text id="pro_occup_squ" label="项目占地面积" />
		<emp:text id="pro_arch_squ" label="项目建筑面积" />
		<emp:text id="approve_gover_dept" label="批项政府部门" />
		<emp:text id="pro_approve_no" label="项目批准文号" />
		<emp:text id="govlanduser_no" label="国有土地使用证编号" />
		<emp:text id="landuser_lic" label="建设用地规划许可证号" />
		<emp:text id="pro_tolinves" label="项目总投资" />
		<emp:text id="get_capital" label="到位资本金" />
		<emp:text id="get_capital_rate" label="到位资本金比例" />
		<emp:text id="construct_lic" label="施工许可证" />
		<emp:text id="pro_main_term" label="项目经营期限" />
		<emp:text id="construct_name" label="施工企业名称" />
		<emp:text id="pro_memo" label="项目情况说明" />
	</emp:table>
	
</body>
</html>
</emp:page>
    