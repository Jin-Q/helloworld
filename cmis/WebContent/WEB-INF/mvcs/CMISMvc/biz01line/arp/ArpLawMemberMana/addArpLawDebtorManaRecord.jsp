<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String case_no= (String)request.getParameter("case_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function returnCus(data){
		cus_id = data.cus_id._getValue();
		cus_name = data.cus_name._getValue();
		cert_type = data.cert_type._getValue();
		cert_code = data.cert_code._getValue();
		var url="<emp:url action='checkAssetPreserve.do'/>&type="+subMenuId+"&value="+cus_id+"&serno=<%=case_no%>";
		doPubCheck(url,result);		
	};
	
	function result(flag){
		if(flag == 'success'){
			ArpLawMemberMana.cus_id._setValue(cus_id);
			ArpLawMemberMana.cus_id_displayname._setValue(cus_name);
			ArpLawMemberMana.cert_type._setValue(cert_type);
			ArpLawMemberMana.cert_code._setValue(cert_code);
		}else{
			alert("此客户已存在于此次诉讼中!");
		}
	};

	function doReturn() {
		var url = '<emp:url action="queryArpLawDebtorManaList.do"/>?case_no='+ArpLawMemberMana.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpLawMemberMana);
	};
	function doLoad(){
		subMenuId = "${context.subMenuId}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpLawMemberManaRecord.do" method="POST">
		
		<emp:gridLayout id="ArpLawMemberManaGroup" title="涉及人员管理" maxColumn="2">
			<emp:text id="ArpLawMemberMana.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpLawMemberMana.case_no" label="案件编号" maxlength="40" hidden="true" defvalue="<%=case_no%>"/>
			<emp:select id="ArpLawMemberMana.member_type" label="人员类别" required="true" 
			defvalue="002" dictname="STD_ZB_MEMBER_TYPE" hidden="true" />
			<emp:pop id="ArpLawMemberMana.cus_id" label="客户码" required="true"  colSpan="2"
			url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200','BL300')and cus_status='20'&returnMethod=returnCus" />
			<emp:text id="ArpLawMemberMana.cus_id_displayname" label="客户名" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="ArpLawMemberMana.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="ArpLawMemberMana.cert_code" label="证件号码" required="true" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

