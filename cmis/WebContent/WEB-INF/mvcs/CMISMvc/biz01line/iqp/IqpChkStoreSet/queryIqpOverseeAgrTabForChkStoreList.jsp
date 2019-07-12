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
		IqpOverseeAgr._toForm(form);
		IqpOverseeAgrList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpOverseeAgr() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeAgrViewPage.do"/>?'+paramStr+'&menuId=JGXY&flag=havaButton';
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpOverseeAgrGroup.reset();
	};

	function returnCus(data){
		IqpOverseeAgr.mortgagor_id._setValue(data.cus_id._getValue());
		IqpOverseeAgr.mortgagor_id_displayname._setValue(data.cus_name._getValue());
	};
	function returnCus1(data){
		IqpOverseeAgr.oversee_con_id._setValue(data.oversee_org_id._getValue());
		IqpOverseeAgr.oversee_con_id_displayname._setValue(data.oversee_org_id_displayname._getValue());
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpOverseeAgrGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="IqpOverseeAgr.mortgagor_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus&cusTypCondition=belg_line in ('BL100','BL200') and cus_status='20'" />
			<emp:pop id="IqpOverseeAgr.oversee_con_id_displayname" label="监管企业客户名称" buttonLabel="选择" url="IqpOverseeOrg4PopList.do?returnMethod=returnCus1" />
			<emp:text id="IqpOverseeAgr.mortgagor_id" label="客户码" hidden="true"/>
			<emp:text id="IqpOverseeAgr.oversee_con_id" label="监管企业编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:actButton id="viewIqpOverseeAgr" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeAgrList" pageMode="true" url="pageIqpOverseeAgrTabForChkStoreQuery.do?task_set_type=${context.task_set_type}&cus_id=${context.cus_id}">
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="mortgagor_id" label="客户码" />
		<emp:text id="mortgagor_id_displayname" label="客户名称" />
		<emp:text id="oversee_con_id" label="监管企业编号" />
		<emp:text id="oversee_con_id_displayname" label="监管企业名称" />
		<emp:text id="status" label="协议状态" dictname="STD_ZB_STATUS" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    