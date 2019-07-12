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
		IqpDelayCreditPur._toForm(form);
		IqpDelayCreditPurList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDelayCreditPurPage() {
		var paramStr = IqpDelayCreditPurList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDelayCreditPurUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDelayCreditPur() {
		var paramStr = IqpDelayCreditPurList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDelayCreditPurViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDelayCreditPurPage() {
		var url = '<emp:url action="getIqpDelayCreditPurAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpDelayCreditPur() {
		var paramStr = IqpDelayCreditPurList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpDelayCreditPurRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDelayCreditPurGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpDelayCreditPurGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpDelayCreditPur.serno" label="业务编号" />
			<emp:select id="IqpDelayCreditPur.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDelayCreditPur.limit_cont_no" label="额度合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpDelayCreditPurPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpDelayCreditPurPage" label="修改" op="update"/>
		<emp:button id="deleteIqpDelayCreditPur" label="删除" op="remove"/>
		<emp:button id="viewIqpDelayCreditPur" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpDelayCreditPurList" pageMode="true" url="pageIqpDelayCreditPurQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="fin_day" label="融资天数" />
		<emp:text id="pay_amt" label="实付金额" />
		<emp:text id="rece_amt" label="应收款金额" />
		<emp:text id="rece_end_date" label="应收款到期日" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    