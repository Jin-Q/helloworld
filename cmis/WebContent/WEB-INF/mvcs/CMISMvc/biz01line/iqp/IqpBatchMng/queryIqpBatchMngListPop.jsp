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
		IqpBatchMng._toForm(form);
		IqpBatchMngList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.IqpBatchMngGroup.reset();
	};
	/*--user code begin--*/
	function doSelect(){
		var data = IqpBatchMngList._obj.getSelectedData();
		if(data!=null&&data!=''){
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
		}else{
			alert('请先选择一条记录！');
		}
	};	
	//双击事件
	$(function (){ 
		$(".emp_field_td").dblclick(function (){ 
			doSelect();
		}); 
	  });	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBatchMngGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpBatchMng.batch_no" label="批次号" />
			<emp:text id="IqpBatchMng.opp_org_name" label="对手行行名" />
			<emp:select id="IqpBatchMng.bill_type" label="票据种类" />
			<emp:text id="IqpBatchMng.input_id" label="登记人" />
			<emp:text id="IqpBatchMng.input_br_id" label="登记机构" />
			<emp:datespace id="IqpBatchMng.input_date" label="登记日期" colSpan="2"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选取返回" op="add"/>
	</div>

	<emp:table icollName="IqpBatchMngList" pageMode="true" url="pageIqpBatchMngQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
		<emp:text id="bill_qnt" label="票据数量" />
		<emp:text id="bill_total_amt" label="票据总金额" />
		<emp:text id="opp_org_name" label="对手行行名" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_DRFT_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    