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
		IqpExportPorderFin._toForm(form);
		IqpExportPorderFinList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExportPorderFinPage() {
		var paramStr = IqpExportPorderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportPorderFinUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExportPorderFin() {
		var paramStr = IqpExportPorderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportPorderFinViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExportPorderFinPage() {
		var url = '<emp:url action="getIqpExportPorderFinAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpExportPorderFin() {
		var paramStr = IqpExportPorderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpExportPorderFinRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpExportPorderFinGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpExportPorderFinGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpExportPorderFin.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportPorderFin.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpExportPorderFin.rpled_serno" label="被置换业务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpExportPorderFinPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExportPorderFinPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExportPorderFin" label="删除" op="remove"/>
		<emp:button id="viewIqpExportPorderFin" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpExportPorderFinList" pageMode="true" url="pageIqpExportPorderFinQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="rpled_serno" label="被置换业务编号" />
		<emp:text id="invc_cur_type" label="发票币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="invc_amt" label="发票金额" />
		<emp:text id="biz_settl_mode" label="原业务结算方式" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    