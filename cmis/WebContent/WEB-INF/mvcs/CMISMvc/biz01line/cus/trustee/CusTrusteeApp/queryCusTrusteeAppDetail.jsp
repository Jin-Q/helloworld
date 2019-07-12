<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flow = "";
	String flag = "";
	String menuId = "";
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
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
		var url = '<emp:url action="queryCusTrusteeAppList.do"/>?process=${context.process}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusTrusteeAppGroup" title="委托托管表" maxColumn="2">
			<emp:text id="CusTrusteeApp.serno" label="业务编号" maxlength="40" required="true" colSpan="2" readonly="true"/>
			
			<emp:select id="CusTrusteeApp.consignor_type" label="委托类别" required="true" dictname="STD_CUS_CONSIG_TYPE" defvalue="1" onchange="initType()"/>
			<emp:select id="CusTrusteeApp.is_provid_accredit" label="是否提供书面授权书" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="CusTrusteeApp.consignor_br_id_displayname" label="委托机构"   required="true" />
			<emp:pop id="CusTrusteeApp.consignor_id_displayname" label="委托人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setConsignorId" required="true" />
			<emp:text id="CusTrusteeApp.trustee_br_id_displayname" label="托管机构"  required="true" />
			<emp:pop id="CusTrusteeApp.trustee_id_displayname" label="托管人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setTrusteeId"  required="true" />
			
			<emp:textarea id="CusTrusteeApp.trustee_detail" label="托管说明" maxlength="250" required="true" colSpan="2" />
			<emp:date id="CusTrusteeApp.trustee_date" label="托管日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:text id="CusTrusteeApp.consignor_id" label="委托人"   required="true" hidden="true"/>
			<emp:text id="CusTrusteeApp.consignor_br_id" label="委托机构" maxlength="20" required="true" hidden="true"/>
			<emp:pop id="CusTrusteeApp.trustee_id" label="托管人" url="null" required="true" hidden="true"/>
			<emp:text id="CusTrusteeApp.trustee_br_id" label="托管机构" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusTrusteeAppGroup2" title="登记信息" maxColumn="2">
			<emp:select id="CusTrusteeApp.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" colSpan="2"/>
			<emp:text id="CusTrusteeApp.input_id_displayname" label="登记人"   readonly="true"/>
			<emp:text id="CusTrusteeApp.input_br_id_displayname" label="登记机构"   readonly="true"/>
			<emp:date id="CusTrusteeApp.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:text id="CusTrusteeApp.input_id" label="登记人" maxlength="20" hidden="true" defvalue="${context.loginuserid}"/>
			<emp:text id="CusTrusteeApp.input_br_id" label="登记机构" maxlength="20" hidden="true" defvalue="${context.loginorgid}"/>
			<emp:date id="CusTrusteeApp.supervise_date" label="审批日期" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("".equals(flow)|| flow == null) {%>
		   <emp:button id="return" label="返回到列表页面"/>
		<% }%> 
		
	</div>
</body>
</html>
</emp:page>
