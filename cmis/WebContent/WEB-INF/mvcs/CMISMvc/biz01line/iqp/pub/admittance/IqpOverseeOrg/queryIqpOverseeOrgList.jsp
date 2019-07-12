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
		IqpOverseeOrg._toForm(form);
		IqpOverseeOrgList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpOverseeOrg() {
		var paramStr = IqpOverseeOrgList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeOrgViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.IqpOverseeOrgGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		IqpOverseeOrg.oversee_org_id._setValue(data.cus_id._getValue());
		IqpOverseeOrg.oversee_org_id_displayname._setValue(data.cus_name._getValue());
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpOverseeOrgGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpOverseeOrg.serno" label="业务流水号" />
		<emp:pop id="IqpOverseeOrg.oversee_org_id_displayname" label="监管机构名称" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus"/>
		<emp:select id="IqpOverseeOrg.oversee_org_status" label="状态" dictname="STD_ZB_OVERSEE_STATUS"/>
		<emp:text id="IqpOverseeOrg.oversee_org_id" label="监管机构编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpOverseeOrg" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeOrgList" pageMode="true" url="pageIqpOverseeOrgQuery.do">
		<emp:text id="serno" label="业务流水号" hidden="false"/>
		<emp:text id="oversee_org_id" label="监管机构编号" />
		<emp:text id="oversee_org_id_displayname" label="监管机构名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:select id="oversee_org_status" label="状态" dictname="STD_ZB_OVERSEE_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    