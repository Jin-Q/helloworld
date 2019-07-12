<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String butFlag = "";
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	if(context.containsKey("butFlag")){
		butFlag = (String)context.getDataValue("butFlag");
	}
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusBlkCheckinappList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusBlkCheckinappGroup" title="不宜贷款户进入申请" maxColumn="2">
			<emp:text id="CusBlkCheckinapp.serno" label="业务流水号" maxlength="40" required="true" colSpan="2" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.cus_id" label="客户码" maxlength="20" required="true" readonly="true" colSpan="2"/>
			<emp:text id="CusBlkCheckinapp.cus_id_displayname" label="客户名称"   required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusBlkCheckinapp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusBlkCheckinapp.cert_code" label="证件号码" maxlength="20" required="true" />
			<emp:select id="CusBlkCheckinapp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="CusBlkCheckinapp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkCheckinapp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkCheckinapp.legal_phone" label="联系电话" maxlength="35" required="false" />
			<emp:text id="CusBlkCheckinapp.legal_addr_displayname" label="通讯地址"   required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusBlkCheckinapp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:textarea id="CusBlkCheckinapp.black_reason" label="客户描述" maxlength="250" required="true" colSpan="2" />
			<emp:select id="CusBlkCheckinapp.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" hidden="true"/>
	</emp:gridLayout>
	
	<emp:gridLayout id="CusBlkCheckinappGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkCheckinapp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  required="true"/>
			<emp:pop id="CusBlkCheckinapp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.manager_id" label="责任人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_id_displayname" label="登记人" required="true" defvalue="$currentUserId" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id_displayname" label="登记机构" required="true" defvalue="$organNo"  readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo"  readonly="true" hidden="true"/>
			<emp:date id="CusBlkCheckinapp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
	<%if(!"hidd".equals(butFlag)){ %>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
	<%} %>
</body>
</html>
</emp:page>
