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
		MortGuarantyBaseInfo._toForm(form);
		MortGuarantyBaseInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};

	function returnCus(data){
		MortGuarantyBaseInfo.cus_id._setValue(data.cus_id._getValue());
	};

	function doViewMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?flagCs=view&task_set_type=${context.task_set_type}&cus_id=${context.cus_id}&op=view&menuId=mort_maintain&oversee_agr_no=${context.oversee_agr_no}&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;  
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:button id="viewMortGuarantyBaseInfo" label="查看"/>

	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseInfoTabForChkStoreQuery.do?task_set_type=${context.task_set_type}&cus_id=${context.cus_id}&oversee_agr_no=${context.oversee_agr_no}">
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_type_displayname" label="押品类型"/>
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    