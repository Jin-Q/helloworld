<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function getOrgID(data){
		WfiWorkflow2org.org_id._setValue(data.organno._getValue());
		WfiWorkflow2org.org_name._setValue(data.organname._getValue());
	};
	
	function getWfsign(data) {
		WfiWorkflow2org.wfsign._setValue(data.wfsign._getValue());
		WfiWorkflow2org.wfname._setValue(data.wfname._getValue());
	}
	
	
	function changeApplType(){
		var applType = WfiWorkflow2org.appl_type._getValue();
		var url = '<emp:url action="queryWorkflow2bizByApplType.do"/>?returnMethod=getWfsign';
		url = EMPTools.encodeURI(url);
		if(applType==null || applType=='') {
			WfiWorkflow2org.wfsign._obj._renderReadonly(true);
		} else {
			WfiWorkflow2org.wfsign._obj._renderReadonly(false);
			WfiWorkflow2org.wfsign._obj.config.url =url+"&appl_type="+applType;
		}
		WfiWorkflow2org.wfsign._setValue('');
		WfiWorkflow2org.wfname._setValue('');
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfiWorkflow2orgRecord.do" method="POST">
		
		<emp:gridLayout id="WfiWorkflow2orgGroup" title="流程关联机构配置" maxColumn="2">
			<emp:text id="WfiWorkflow2org.wf2org_id" label="关联ID" maxlength="40" required="false" hidden="true"/>
			<emp:pop id="WfiWorkflow2org.org_id" label="机构ID" required="true" returnMethod="getOrgID" url="querySOrgPop.do?restrictUsed=false" />
			<emp:text id="WfiWorkflow2org.org_name" label="机构名称" maxlength="40" required="true" readonly="true"/>
			<emp:select id="WfiWorkflow2org.appl_type" label="申请类型" required="true" dictname="ZB_BIZ_CATE" colSpan="2" onchange="changeApplType()"/>
			<emp:pop id="WfiWorkflow2org.wfsign" label="流程标识" url="queryWorkflow2bizByApplType.do?returnMethod=getWfsign" returnMethod="getWfsign" required="true" readonly="true"/>
			<emp:text id="WfiWorkflow2org.wfname" label="流程名称" maxlength="50" required="true" readonly="true"/>
			<emp:textarea id="WfiWorkflow2org.remark" label="备注" maxlength="100" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

