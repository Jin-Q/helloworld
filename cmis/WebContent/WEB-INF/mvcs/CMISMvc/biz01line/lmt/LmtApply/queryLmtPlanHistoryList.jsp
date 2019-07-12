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
		LmtApply._toForm(form);
		LmtApplyList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	/*--user code begin--*/
	//一票否决
	function doSubmit(){
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		var approve_status = LmtApplyList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status=!"998"){
				LmtApplyList._obj.getSelectedData()[0].approve_status._setValue("998");
			}
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtApplyGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtApply.serno" label="业务编号" />
			<emp:text id="LmtApply.cus_id" label="客户码" />
			<emp:select id="LmtApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtApply" label="查看" op="view"/>
		<emp:button id="submit" label="一票否决" op="view"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="true" url="pageLmtPlanQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    