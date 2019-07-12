<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String forwf = "";//控制流程办结事项，查看详细信息不显示按钮
	if(context.containsKey(forwf)){
		forwf = (String)context.getDataValue("forwf");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCtrLimitAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="maintabs" id="maintabs">
		<emp:tab label="额度合同业务申请基本信息" id="maintabs">
			<emp:form id="submitForm" action="getCtrLimitAppUpdatePage.do" method="POST">
				<emp:gridLayout id="CtrLimitAppGroup" title="额度合同申请表" maxColumn="2">
					<emp:text id="CtrLimitApp.serno" label="业务编号" maxlength="40" hidden="true" required="false" />
					<emp:select id="CtrLimitApp.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" />
					<emp:pop id="CtrLimitApp.cus_id" label="客户码" url="queryAllCusPop.do" required="true" />
					<emp:select id="CtrLimitApp.cur_type" label="币种"  required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
					<emp:text id="CtrLimitApp.app_amt" label="申请金额" maxlength="16" required="true" dataType="Currency"/>
					<emp:date id="CtrLimitApp.start_date" label="起始日期"  required="true" defvalue="${context.OPENDAY}"/>
					<emp:date id="CtrLimitApp.end_date" label="到期日期"  required="true" />
					<emp:select id="CtrLimitApp.approve_status" label="申请状态" required="false" hidden="true" dictname="WF_APP_STATUS" defvalue="000" />
					<emp:textarea id="CtrLimitApp.memo" label="备注" maxlength="200" required="false" />
				</emp:gridLayout>
				<emp:gridLayout id="CtrLimitAppGroup" title="机构信息" maxColumn="2">
					<emp:pop id="CtrLimitApp.manager_br_id_displayname" label="管理机构" defvalue="${context.organNo}" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
					<emp:text id="CtrLimitApp.input_id_displayname" label="登记人"  defvalue="${context.currentUserId}" required="false" readonly="true"/>
					<emp:text id="CtrLimitApp.input_br_id_displayname" label="登记机构" defvalue="${context.organNo}"  required="false" readonly="true"/>
					<emp:text id="CtrLimitApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="CtrLimitApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" />
					<emp:text id="CtrLimitApp.input_id" label="登记人" maxlength="32" required="false" hidden="true" />
					<emp:text id="CtrLimitApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
				</emp:gridLayout>
				<%if(!forwf.equals("")){ %>
				<div align="center">
					<br>
					<emp:button id="return" label="返回到列表页面"/>
				</div>
				<%} %>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
