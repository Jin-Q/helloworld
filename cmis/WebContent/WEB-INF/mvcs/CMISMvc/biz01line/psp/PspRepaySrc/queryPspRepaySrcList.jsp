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
		PspRepaySrc._toForm(form);
		PspRepaySrcList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspRepaySrcPage() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRepaySrcUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspRepaySrc() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRepaySrcViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspRepaySrcPage() {
		var url = '<emp:url action="getPspRepaySrcAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspRepaySrc() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspRepaySrcRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspRepaySrcGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspRepaySrcPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspRepaySrcPage" label="修改" op="update"/>
		<emp:button id="deletePspRepaySrc" label="删除" op="remove"/>
		<emp:button id="viewPspRepaySrc" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspRepaySrcList" pageMode="false" url="pagePspRepaySrcQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="asgn_date" label="转入日期" />
		<emp:text id="asgn_amt" label="转入金额" />
		<emp:text id="sour" label="来源" />
		<emp:text id="asgn_acct_no" label="转入方账号" />
		<emp:text id="asgn_acct_name" label="转入方账户名" />
		<emp:text id="asgn_acctsvcr_no" label="转入方开户行行名" />
		<emp:text id="asgn_acctsvcr_name" label="转入方开户行行号" />
		<emp:text id="proof_type" label="凭证种类和编号" />
		<emp:text id="asgn_acct_bal" label="转入后账户余额" />
		<emp:text id="cus_rela" label="转入方和借款人关系" />
		<emp:text id="memo" label="补充说明" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    