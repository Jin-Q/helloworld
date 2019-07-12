<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppGrp._toForm(form);
		LmtAppGrpList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtGrpApply() {
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtGrpApplyViewPage.do"/>?op=view&'+paramStr+"&type=${context.type}&cus_id=${context.cus_id}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppGrpGroup.reset();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppGrpGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppGrp.grp_no" label="集团编号" />
			<emp:text id="LmtAppGrp.serno" label="业务编号" />
			<emp:text id="LmtAppGrp.grp_name" label="集团名称" />
			<emp:select id="LmtAppGrp.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
			<emp:select id="LmtAppGrp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtGrpApply" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAppGrpList" url="pageLmtGrpApplyQuery.do?type=${context.type}&cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />		
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    