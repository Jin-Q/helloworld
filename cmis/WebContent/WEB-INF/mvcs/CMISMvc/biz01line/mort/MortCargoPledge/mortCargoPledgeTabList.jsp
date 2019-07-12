<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortCargoPledge._toForm(form);
		MortCargoPledgeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoPledgePage() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoPledgeUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortCargoPledge() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?'+paramStr+'&flag=tab&op=view';
			url = EMPTools.encodeURI(url);
			var param = 'height=600, width=800, top=200, left=200, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			modifyWindow = window.open(url,'newWindow1',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoPledgePage() {
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getMortCargoPledgeAddPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortCargoPledge() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteMortCargoPledgeRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoPledgeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortCargoPledgeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortCargoPledge.cargo_id" label="货物质押编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortCargoPledgeList" pageMode="true" url="pageMortCargoPledgeTabList.do?oversee_agr_no=${context.oversee_agr_no}">
		<emp:text id="cargo_id" label="货物编号" />
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
		<emp:text id="cargo_name" label="货物名称" />
		<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
		<emp:text id="storage_date" label="入库日期" />
		<emp:text id="exware_date" label="出库日期" />
		<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
		<emp:text id="reg_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    