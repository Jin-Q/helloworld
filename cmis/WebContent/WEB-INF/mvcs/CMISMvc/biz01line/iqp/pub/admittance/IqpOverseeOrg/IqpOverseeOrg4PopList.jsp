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

	function doReset(){
		page.dataGroups.IqpOverseeOrgGroup.reset();
	};
	
	/*--user code begin--*/
	
	function doReturnMethod(){
		var data = IqpOverseeOrgList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="IqpOverseeOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpOverseeOrg.oversee_org_id" label="监管机构编号" />
			<emp:select id="IqpOverseeOrg.oversee_org_status" label="状态" dictname="STD_ZB_OVERSEE_STATUS"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	 <emp:returnButton label="选择返回"/>
	 <emp:table icollName="IqpOverseeOrgList" pageMode="true" url="queryIqpOverseeOrg4Pop.do">
		<emp:text id="serno" label="业务流水号" hidden="false"/>
		<emp:text id="oversee_org_id" label="监管机构编号" />
		<emp:text id="oversee_org_id_displayname" label="监管机构名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:select id="oversee_org_status" label="状态" dictname="STD_ZB_OVERSEE_STATUS"/>
	</emp:table>
	<div align="left">
		<br>
		<emp:returnButton label="选择返回"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    