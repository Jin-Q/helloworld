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
		IqpFastCreditDscnt._toForm(form);
		IqpFastCreditDscntList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpFastCreditDscntPage() {
		var paramStr = IqpFastCreditDscntList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpFastCreditDscntUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpFastCreditDscnt() {
		var paramStr = IqpFastCreditDscntList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpFastCreditDscntViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpFastCreditDscntPage() {
		var url = '<emp:url action="getIqpFastCreditDscntAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpFastCreditDscnt() {
		var paramStr = IqpFastCreditDscntList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpFastCreditDscntRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpFastCreditDscntGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpFastCreditDscntGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpFastCreditDscnt.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpFastCreditDscnt.limit_cont_no" label="额度合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpFastCreditDscntPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpFastCreditDscntPage" label="修改" op="update"/>
		<emp:button id="deleteIqpFastCreditDscnt" label="删除" op="remove"/>
		<emp:button id="viewIqpFastCreditDscnt" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpFastCreditDscntList" pageMode="true" url="pageIqpFastCreditDscntQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="rpled_serno" label="被置换业务编号" />
		<emp:text id="bank_bp_no" label="我行bp号" />
		<emp:text id="is_internal_cert" label="是否国内证项下" />
		<emp:text id="drft_amt" label="票面金额" />
		<emp:text id="bill_cur_type" label="票据币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="issue_date" label="出票日期" />
		<emp:text id="bill_end_date" label="票据到期日" />
		<emp:text id="pay_amt" label="实付金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    