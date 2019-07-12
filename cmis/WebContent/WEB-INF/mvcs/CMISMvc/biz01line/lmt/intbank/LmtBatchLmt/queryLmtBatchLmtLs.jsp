<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_pop {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 230px;
}
</style>

<script type="text/javascript">
	
	function doQuery()
	{
		var form = document.getElementById('queryForm');
		LmtBatchLmt._toForm(form);
		LmtBatchLmtList._obj.ajaxQuery(null,form);
	};
	function doReset()
	{
		page.dataGroups.LmtBatchLmtGroup.reset();
	};
	function doViewLmtBatchLmt() 
	{
		var paramStr = LmtBatchLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtBatchLmtViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function getOrgID(data)
	{
		LmtBatchLmt.manager_br_id._setValue(data.organno._getValue());
    	LmtBatchLmt.manager_br_id_displayname._setValue(data.organname._getValue());      
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="LmtBatchLmtGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtBatchLmt.serno" label="业务编号  " />
			<emp:text id="LmtBatchLmt.batch_cus_no" label="批量客户编号" />
			<emp:pop id="LmtBatchLmt.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_field_text_pop"/>
			<emp:select id="LmtBatchLmt.approve_status" label="审批状态" dictname="WF_APP_STATUS" />
			<emp:datespace id="LmtBatchLmt.app_date" label="申请日期" />
			<emp:pop id="LmtBatchLmt.manager_br_id" label="vv" url="" hidden="true"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtBatchLmt" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtBatchLmtList" pageMode="true" url="pageLmtBatchLmtLsQuery.do">
		<emp:text id="serno" label="业务编号  " />
		<emp:text id="batch_cus_no" label="批量客户编号"/>
		<emp:text id="app_cls" label="申请类别" dictname="STD_ZB_APP_CLS"/>
		<emp:text id="lmt_totl_amt" label="授信总额(元)" dataType="Currency"/>
		<emp:text id="term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE"/>
		<emp:text id="term" label="期限" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    