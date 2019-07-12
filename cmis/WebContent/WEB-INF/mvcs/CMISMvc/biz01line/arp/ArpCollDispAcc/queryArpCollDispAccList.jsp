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
		ArpCollDispAcc._toForm(form);
		ArpCollDispAccList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDispAccPage() {
		var paramStr = ArpCollDispAccList._obj.getParamStr(['asset_disp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDispAccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDispAcc() {
		var paramStr = ArpCollDispAccList._obj.getParamStr(['asset_disp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDispAccViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpCollDispAccPage() {
		var url = '<emp:url action="getArpCollDispAccAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDispAcc() {
		var paramStr = ArpCollDispAccList._obj.getParamStr(['asset_disp_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpCollDispAccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDispAccGroup.reset();
	};
	function doReclaimArpCollDispAcc(){
		var paramStr = ArpCollDispAccList._obj.getParamStr(['asset_disp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDispAccViewPage.do"/>?op=reclaim&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpCollDispAccGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpCollDispAcc.serno" label="业务编号" />
			<emp:text id="ArpCollDispAcc.guaranty_no" label="抵债资产编号" />
			<emp:text id="ArpCollDispAcc.asset_disp_no" label="资产处置编号" />
			<emp:select id="ArpCollDispAcc.asset_disp_mode" label="资产处置方式" dictname="STD_ASSET_DISP_MODEL" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpCollDispAccPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpCollDispAccPage" label="修改" op="update"/>
		<emp:button id="deleteArpCollDispAcc" label="删除" op="remove"/>
		<emp:button id="viewArpCollDispAcc" label="查看" op="view"/>
		<emp:button id="reclaimArpCollDispAcc" label="处置回收信息" op="reclaim" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>

	<emp:table icollName="ArpCollDispAccList" pageMode="true" url="pageArpCollDispAccQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_disp_no" label="资产处置编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="asset_disp_mode" label="资产处置方式" dictname="STD_ASSET_DISP_MODEL" />
		<emp:text id="disp_amt" label="处置金额" dataType="Currency"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    