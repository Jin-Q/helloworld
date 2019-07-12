<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../jsPspProperty.jsp" flush="true" />
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
	/*	var url = '<emp:url action="queryPspPropertyRealproList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;*/
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:gridLayout id="PspPropertyRealproGroup" title="固定资产分析" maxColumn="2">
			<emp:text id="PspPropertyAnaly.property_id" label="资产编号" maxlength="32" required="true" hidden="true"/>
			<emp:text id="PspPropertyAnaly.task_id" label="任务编号" maxlength="32" required="true" hidden="true"/>
			<emp:text id="PspPropertyAnaly.cus_id" label="客户码" maxlength="32" required="true" hidden="true"/>
			<emp:text id="PspPropertyRealpro.property_id" label="资产编号" maxlength="32" required="true" hidden="true"/>
			
			<emp:text id="PspPropertyAnaly.owner" label="所有权人" required="true" maxlength="40" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:select id="PspPropertyAnaly.property_type" label="资产类型" required="true" dictname="STD_ZB_PROPERTY_TYPE" readonly="true"/>
			<emp:select id="PspPropertyAnaly.rela_type" label="客户关系" required="true" dictname="STD_ZB_PSP_RELA_TYPE" />
			<emp:select id="PspPropertyAnaly.owner_cert_type" label="所有权人证件类型" required="true" dictname="STD_ZB_CERT_TYP" onchange="checkCertCode()"/>
			<emp:text id="PspPropertyAnaly.owner_cert_code" label="所有权人证件号码" required="true" maxlength="40" />
			<emp:select id="PspPropertyAnaly.warrant_type" label="权证类型" required="true" dictname="STD_WRR_PROVE_TYPE" />
			<emp:text id="PspPropertyAnaly.warrant_no" label="权证号码" required="true" maxlength="40" />
			
			<emp:text id="PspPropertyRealpro.location" label="坐落" maxlength="100" required="false" />
			<emp:text id="PspPropertyRealpro.arch_squ" label="建筑面积" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="PspPropertyRealpro.land_squ" label="土地面积" maxlength="16" required="false" dataType="Double"/>
			<emp:select id="PspPropertyRealpro.land_cha" label="土地性质" dictname="STD_ZB_LAND_CHA" required="false" />
			<emp:text id="PspPropertyRealpro.use_agelimit" label="使用年限" maxlength="8" required="false" dataType="Int" />
			<emp:text id="PspPropertyRealpro.pur_price" label="购置价或造价" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="PspPropertyRealpro.is_pld" label="是否抵押" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspPropertyRealpro.is_close" label="是否查封" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspPropertyRealpro.is_rent" label="是否出租" required="false" dictname="STD_ZX_YES_NO" />
		</emp:gridLayout>
		<emp:gridLayout id="PspPropertyAnalyGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspPropertyAnaly.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspPropertyAnaly.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspPropertyAnaly.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspPropertyAnaly.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspPropertyAnaly.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
