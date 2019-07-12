<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	
	/*** 选择客户并校验begin ***/
	function returnCus(data){
		cus_id = data.cus_id._getValue();
		cus_name = data.cus_name._getValue();
		cert_type = data.cert_type._getValue();
		cert_code = data.cert_code._getValue();
		var url="<emp:url action='checkAssetPreserve.do'/>&type="+subMenuId+"&value="+cus_id+"&serno="+serno;
		doPubCheck(url,result);
	};	
	function result(flag){
		if(flag == 'success'){
			ArpLawMemberInfo.cus_id._setValue(cus_id);
			ArpLawMemberInfo.cus_id_displayname._setValue(cus_name);
			ArpLawMemberInfo.cert_type._setValue(cert_type);
			ArpLawMemberInfo.cert_code._setValue(cert_code);
		}else{
			alert("此客户已存在于此次诉讼中!");
		}
	};
	/*** 选择客户并校验end ***/

	function doReturn() {
		var url = '<emp:url action="queryArpLawDefendantInfoList.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpLawMemberInfo);
	};
	function doLoad(){
		subMenuId = "${context.subMenuId}";
		cus_type = "${context.cus_type}";
		serno = "${context.serno}";
		if(cus_type == 'Com'){
			ArpLawMemberInfo.cus_id._obj.config.url 
			= "<emp:url action='queryAllCusPop.do'/>&cusTypCondition=BELG_LINE in ('BL100','BL200') and cus_status='20'&returnMethod=returnCus";
		}		
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()" >
	
	<emp:form id="submitForm" action="addArpLawMemberInfoRecord.do" method="POST">
		
		<emp:gridLayout id="ArpLawMemberInfoGroup" title="被告人员信息" maxColumn="2">
			<emp:text id="ArpLawMemberInfo.pk_serno" label="流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="ArpLawMemberInfo.serno" label="业务编号" maxlength="40" required="true" defvalue="${context.serno}" hidden="true"/>
			<emp:select id="ArpLawMemberInfo.member_type" label="人员类别" required="true" 
			defvalue="001" dictname="STD_ZB_MEMBER_TYPE" hidden="true" />
			<emp:pop id="ArpLawMemberInfo.cus_id" label="客户码" required="true"  colSpan="2"
			url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL300') and cus_status='20'&returnMethod=returnCus" />
			<emp:text id="ArpLawMemberInfo.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="ArpLawMemberInfo.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="ArpLawMemberInfo.cert_code" label="证件号码" required="true" readonly="true" />
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