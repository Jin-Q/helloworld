<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	

	
	function doViewLmtSigLmt(){
		var paramStr = LmtSigLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryLmtSigLmtDetail.do"/>?'+paramStr+"&type=his";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtSigLmt._toForm(form);
		LmtSigLmtList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtSigLmtGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCusId(data){
		LmtSigLmt.cus_id._setValue(data.cus_id._getValue());
		LmtSigLmt.cus_id_displayname._setValue(data.same_org_cnname._getValue());
	}	
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtSigLmtGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtSigLmt.serno" label="业务编号" />
			<emp:text id="LmtSigLmt.cus_id" label="客户码" />
			<emp:pop id="LmtSigLmt.cus_id_displayname" label="客户名称" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:datespace id="LmtSigLmt.app_date" label="申请日期" />
			<emp:select id="LmtSigLmt.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="viewLmtSigLmt" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtSigLmtList" pageMode="true" url="pageLmtSigLmtLsQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称"/>
		<emp:text id="app_cls" label="申请类别" dictname="STD_ZB_APP_CLS"/>
		<emp:text id="lmt_amt" label="授信金额(元)" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>		
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
</body>
</html>
</emp:page>