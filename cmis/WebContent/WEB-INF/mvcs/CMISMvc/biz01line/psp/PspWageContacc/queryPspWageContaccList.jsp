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
		PspWageContacc._toForm(form);
		PspWageContaccList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspWageContaccPage() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspWageContaccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspWageContacc() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspWageContaccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspWageContaccPage() {
		var url = '<emp:url action="getPspWageContaccAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspWageContacc() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspWageContaccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspWageContaccGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspWageContaccPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspWageContaccPage" label="修改" op="update"/>
		<emp:button id="deletePspWageContacc" label="删除" op="remove"/>
		<emp:button id="viewPspWageContacc" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspWageContaccList" pageMode="false" url="pagePspWageContaccQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="con_acct_no" label="企业账户" />
		<emp:text id="con_acct_name" label="企业账户名" />
		<emp:text id="acctsvcr_no" label="开户行行号" />
		<emp:text id="acctsvcr_name" label="开户行行名" />
		<emp:text id="person_qnt" label="人数" />
		<emp:text id="the_amt" label="应发金额" />
		<emp:text id="act_amt" label="实发金额" />
		<emp:text id="settl_start_date" label="工资结算起始日期" />
		<emp:text id="settl_end_date" label="工资阶段结束日期" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    