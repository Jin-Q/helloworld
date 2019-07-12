<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryPspBatchTaskRelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//返回主管客户经理	
	function setconId(data){
		PspBatchTaskRel.manager_id._setValue(data.actorno._getValue());
		PspBatchTaskRel.manager_id_displayname._setValue(data.actorname._getValue());
		PspBatchTaskRel.manager_br_id._setValue(data.orgid._getValue());
		PspBatchTaskRel.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					PspBatchTaskRel.manager_br_id._setValue(jsonstr.org);
					PspBatchTaskRel.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					PspBatchTaskRel.manager_br_id._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspBatchTaskRel.manager_id._getValue();
					PspBatchTaskRel.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspBatchTaskRel.manager_br_id._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._obj._renderReadonly(false);
					PspBatchTaskRel.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspBatchTaskRel.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	
	//返回主管机构
	function getOrganName(data){
		PspBatchTaskRel.manager_br_id._setValue(data.organno._getValue());
		PspBatchTaskRel.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="getPspBatchTaskRelAddPage.do" method="POST">
		
		<emp:gridLayout id="PspBatchTaskRelGroup" title="贷后批量任务" maxColumn="2">
			<emp:text id="PspBatchTaskRel.major_task_id" label="主任务编号" maxlength="40" required="true" defvalue="${context.major_task_id}" readonly="true" colSpan="2"/>
			<emp:select id="PspBatchTaskRel.batch_task_type" label="批量任务类型" dictname="STD_BATCH_TASK_TYPE" required="true"/>
			<emp:select id="PspBatchTaskRel.task_type" label="任务类型" dictname="STD_ZB_TASK_TYPE" required="true" defvalue="09" readonly="true"/>
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
			<emp:pop id="PspBatchTaskRel.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" readonly="false"/>
			<emp:pop id="PspBatchTaskRel.manager_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="false"/>
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
			<emp:text id="PspBatchTaskRel.input_name" label="登记人" maxlength="40" required="false" defvalue="${context.input_name}" readonly="true"/>
			<emp:text id="PspBatchTaskRel.input_br_name" label="登记机构" required="false" defvalue="${context.input_br_name}" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_id" label="主管客户经理" maxlength="40" required="false" defvalue="${context.manager_id}" hidden="true"/>
			<emp:text id="PspBatchTaskRel.manager_br_id" label="主管机构" maxlength="20" required="false" defvalue="${context.manager_br_id}" hidden="true"/>
			<emp:text id="PspBatchTaskRel.input_id" label="登记人" maxlength="40" required="false" defvalue="${context.input_id}" hidden="true"/>
			<emp:text id="PspBatchTaskRel.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.input_br_id}" hidden="true"/>
		</emp:gridLayout>
				
		<div align="center">
			<br>
			<emp:button id="submit" label="下一步"/>
			<emp:button id="return" label="返回列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

