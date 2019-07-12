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
		CusScaleApply._toForm(form);
		CusScaleApplyList._obj.ajaxQuery(null,form);
	};
	
	function doViewCusScaleApply() {
		var paramStr = CusScaleApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusScaleApplyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
		
	function doReset(){
		page.dataGroups.CusScaleApplyGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		CusScaleApply.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusScaleApplyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusScaleApply.serno" label="申请流水号" />
			<emp:pop id="CusScaleApply.cus_id" label="客户码"  
			buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:select id="CusScaleApply.scale_type" label="认定类别" dictname="STD_ZB_COGNIZ_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCusScaleApply" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusScaleApplyList" pageMode="true" url="pageCusScaleApplyQuery.do?type=his">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="scale_type" label="认定类别" dictname="STD_ZB_COGNIZ_TYPE"  />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="approve_status" label="审批状态"  dictname="WF_APP_STATUS"  />
		<emp:text id="input_date" label="登记日期" />		
	</emp:table>
	
</body>
</html>
</emp:page>
    