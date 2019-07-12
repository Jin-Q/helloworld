<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppJointCoop._toForm(form);
		LmtAppJointCoop_jointList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAppJointCoopGroup.reset();
	};

	function doView() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?'+paramStr+"&op=view&type=${context.type}&cus_id=${context.cus_id}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppJointCoopGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppJointCoop.serno" label="业务编号" />
			<emp:text id="LmtAppJointCoop.cus_id" label="组长客户码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="view" label="查看" op="view"/>
	</div>
	<!-- modify by jiangcuihua 2019-03-16 修正查询 -->
	<emp:table icollName="LmtAppJointCoop_jointList" pageMode="true" url="pageLmtAppJointCoop_jointQuery.do" reqParams="process=${context.process}&type=${context.type}&cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="类别" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    