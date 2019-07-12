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
		IqpStermGuarFin._toForm(form);
		IqpStermGuarFinList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpStermGuarFinPage() {
		var paramStr = IqpStermGuarFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpStermGuarFinUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpStermGuarFin() {
		var paramStr = IqpStermGuarFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpStermGuarFinViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpStermGuarFinPage() {
		var url = '<emp:url action="getIqpStermGuarFinAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpStermGuarFin() {
		var paramStr = IqpStermGuarFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpStermGuarFinRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpStermGuarFinGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpStermGuarFinGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpStermGuarFin.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpStermGuarFin.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpStermGuarFin.insettl_serno" label="国结业务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpStermGuarFinPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpStermGuarFinPage" label="修改" op="update"/>
		<emp:button id="deleteIqpStermGuarFin" label="删除" op="remove"/>
		<emp:button id="viewIqpStermGuarFin" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpStermGuarFinList" pageMode="true" url="pageIqpStermGuarFinQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="insettl_serno" label="国结业务编号" />
		<emp:text id="invc_cur_type" label="发票币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="invc_amt" label="发票金额" />
		<emp:text id="insur_no" label="保单号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    