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
		IqpForftin._toForm(form);
		IqpForftinList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpForftinPage() {
		var paramStr = IqpForftinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpForftinUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpForftin() {
		var paramStr = IqpForftinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpForftinViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpForftinPage() {
		var url = '<emp:url action="getIqpForftinAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpForftin() {
		var paramStr = IqpForftinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpForftinRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpForftinGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpForftinGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpForftin.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpForftin.limit_cont_no" label="额度合同编号" />
			<emp:select id="IqpForftin.is_replace" label="是否置换" />
			<emp:text id="IqpForftin.rpled_serno" label="被置换业务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpForftinPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpForftinPage" label="修改" op="update"/>
		<emp:button id="deleteIqpForftin" label="删除" op="remove"/>
		<emp:button id="viewIqpForftin" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpForftinList" pageMode="true" url="pageIqpForftinQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="rpled_serno" label="被置换业务编号" />
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_cur_type" label="票据币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="drft_amt" label="票面金额" />
		<emp:text id="bill_end_date" label="票据到期日" />
		<emp:text id="pay_amt" label="实付金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    