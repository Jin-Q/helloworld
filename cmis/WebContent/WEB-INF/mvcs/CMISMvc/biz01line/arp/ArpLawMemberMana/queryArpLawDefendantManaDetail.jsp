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
		var url = '<emp:url action="queryArpLawDefendantManaList.do"/>?case_no='+ArpLawMemberMana.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		cus_type = "${context.cus_type}";
		addCusForm(ArpLawMemberMana);
		
		if(cus_type == 'indiv'){
			ArpLawMemberMana.acu_addr_displayname._obj._renderHidden(true);
			ArpLawMemberMana.street._obj._renderHidden(true);
		}else{
			ArpLawMemberMana.indiv_sex._obj._renderHidden(true);
			ArpLawMemberMana.indiv_ntn._obj._renderHidden(true);
			ArpLawMemberMana.indiv_rsd_addr_displayname._obj._renderHidden(true);
			ArpLawMemberMana.street3._obj._renderHidden(true);
			ArpLawMemberMana.indiv_dt_of_birth._obj._renderHidden(true);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpLawMemberManaGroup" title="涉及人员管理" maxColumn="2">
			<emp:text id="ArpLawMemberMana.pk_serno" label="流水号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpLawMemberMana.case_no" label="案件编号" maxlength="40" required="true" hidden="true"/>
			<emp:select id="ArpLawMemberMana.member_type" label="人员类别" hidden="true" dictname="STD_ZB_MEMBER_TYPE" />
			<emp:text id="ArpLawMemberMana.cus_id" label="客户码" maxlength="30"  />
			<emp:text id="ArpLawMemberMana.cus_name" label="客户名称" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:select id="ArpLawMemberMana.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="ArpLawMemberMana.cert_code" label="证件号码" />
			<emp:select id="ArpLawMemberMana.indiv_sex" label="性别" dictname="STD_ZX_SEX" />
			<emp:select id="ArpLawMemberMana.indiv_ntn" label="民族" dictname="STD_ZB_NATION" />
			<emp:text id="ArpLawMemberMana.indiv_rsd_addr_displayname" label="住址" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawMemberMana.street3" label="居住街道" cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawMemberMana.indiv_dt_of_birth" label="出生日期" />
			<emp:text id="ArpLawMemberMana.acu_addr_displayname" label="公司地址" cssElementClass="emp_field_text_cusname" colSpan="2"/>	
			<emp:text id="ArpLawMemberMana.street" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
