<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		url = '<emp:url action="queryArpLawDefendantInfoList.do"/>?serno='+ArpLawMemberInfo.serno._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		cus_type = "${context.cus_type}";
		addCusForm(ArpLawMemberInfo);
		
		if(cus_type == 'indiv'){
			ArpLawMemberInfo.acu_addr_displayname._obj._renderHidden(true);
			ArpLawMemberInfo.street._obj._renderHidden(true);
		}else{
			ArpLawMemberInfo.indiv_sex._obj._renderHidden(true);
			ArpLawMemberInfo.indiv_ntn._obj._renderHidden(true);
			ArpLawMemberInfo.indiv_rsd_addr_displayname._obj._renderHidden(true);
			ArpLawMemberInfo.street3._obj._renderHidden(true);
			ArpLawMemberInfo.indiv_dt_of_birth._obj._renderHidden(true);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpLawMemberInfoGroup" title="被告人员信息" maxColumn="2">
			<emp:text id="ArpLawMemberInfo.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpLawMemberInfo.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:select id="ArpLawMemberInfo.member_type" label="人员类别" hidden="true" dictname="STD_ZB_MEMBER_TYPE" />
			<emp:text id="ArpLawMemberInfo.cus_id" label="客户码" maxlength="30"  />
			<emp:text id="ArpLawMemberInfo.cus_name" label="客户名称" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:select id="ArpLawMemberInfo.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="ArpLawMemberInfo.cert_code" label="证件号码" />
			<emp:select id="ArpLawMemberInfo.indiv_sex" label="性别" dictname="STD_ZX_SEX" />
			<emp:select id="ArpLawMemberInfo.indiv_ntn" label="民族" dictname="STD_ZB_NATION" />
			<emp:text id="ArpLawMemberInfo.indiv_rsd_addr_displayname" label="住址" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawMemberInfo.street3" label="居住街道" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawMemberInfo.indiv_dt_of_birth" label="出生日期" />
			<emp:text id="ArpLawMemberInfo.acu_addr_displayname" label="公司地址" cssElementClass="emp_field_text_cusname" colSpan="2"/>	
			<emp:text id="ArpLawMemberInfo.street" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
