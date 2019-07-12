<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
.emp_field_long_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:665px;
};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var serno = IqpRentInfo.serno._getValue();
		var url = '<emp:url action="queryIqpRentInfoList.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onload(){
		rent_amt_type();
	};
	function rent_amt_type(){
		var rent_amt_coll_type = IqpRentInfo.rent_amt_coll_type._getValue();
		if(rent_amt_coll_type == "01"){
			IqpRentInfo.rent_acctsvcr_no._setValue("");
			IqpRentInfo.rent_acctsvcrnm._setValue("");
			IqpRentInfo.rent_no._setValue("");
			IqpRentInfo.rent_name._setValue("");

			IqpRentInfo.rent_acctsvcr_no._obj._renderRequired(false);
			IqpRentInfo.rent_acctsvcrnm._obj._renderRequired(false);
			IqpRentInfo.rent_no._obj._renderRequired(false);
			IqpRentInfo.rent_name._obj._renderRequired(false);

			IqpRentInfo.rent_acctsvcr_no._obj._renderHidden(true);
			IqpRentInfo.rent_acctsvcrnm._obj._renderHidden(true);
			IqpRentInfo.rent_no._obj._renderHidden(true);
			IqpRentInfo.rent_name._obj._renderHidden(true);
		}else if(rent_amt_coll_type == "02"){
			IqpRentInfo.rent_acctsvcr_no._obj._renderRequired(true);
			IqpRentInfo.rent_acctsvcrnm._obj._renderRequired(true);
			IqpRentInfo.rent_no._obj._renderRequired(true);
			IqpRentInfo.rent_name._obj._renderRequired(true);

			IqpRentInfo.rent_acctsvcr_no._obj._renderHidden(false);
			IqpRentInfo.rent_acctsvcrnm._obj._renderHidden(false);
			IqpRentInfo.rent_no._obj._renderHidden(false);
			IqpRentInfo.rent_name._obj._renderHidden(false);
		}
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:gridLayout id="IqpRentInfoGroup" title="出租信息" maxColumn="2">
			<emp:date id="IqpRentInfo.start_date" label="起始日期" required="true" onblur="endDateChange()"/>
			<emp:date id="IqpRentInfo.end_date" label="到期日期" required="true" onblur="endDateChange()"/>
			<emp:select id="IqpRentInfo.rent_coll_term_unit" label="租金收取期限单位" required="true" dictname="STD_ZX_RENT_UNIT" />
			<emp:text id="IqpRentInfo.rent_coll_term" label="租金收取期限" maxlength="38" required="true" dataType="Int" />
			<emp:text id="IqpRentInfo.every_rent_amt" label="每期租金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpRentInfo.pld_amt" label="押金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpRentInfo.total_rent_amt" label="总租金" maxlength="16" required="true" readonly="true" dataType="Currency" />
			<emp:select id="IqpRentInfo.rent_amt_coll_type" label="租金收取方式" required="true"  dictname="STD_ZB_RENT_COLL_TYPE" colSpan="2"/>
			<emp:text id="IqpRentInfo.rent_acctsvcr_no" label="租金收取开户行行号" maxlength="40" required="false" hidden="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.rent_acctsvcrnm" label="租金收取开户行行名" maxlength="150" required="false" hidden="true"/>
			<emp:text id="IqpRentInfo.rent_no" label="租金收取账号" maxlength="40" required="false" hidden="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.rent_name" label="租金收取账户名" maxlength="150" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="IqpRentInfoGroup" title="承租人信息" maxColumn="2">
			<emp:text id="IqpRentInfo.lessee_name" label="承租人名称" maxlength="80" required="true" colSpan="2"/>
			<emp:text id="IqpRentInfo.lessee_mobile" label="承租人手机" maxlength="20" required="true" dataType="Mobile" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.lessee_phone" label="承租人电话" maxlength="20" required="true" dataType="Phone" cssElementClass="emp_field_input"/>
			<emp:select id="IqpRentInfo.lessee_cert_type" label="承租人证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="IqpRentInfo.lessee_cert_no" label="承租人证件号码" maxlength="20" required="true" onchange="checkCertCode()"/>
			<emp:text id="IqpRentInfo.lessee_addr" label="承租人地址" maxlength="150" required="true" colSpan="2" cssElementClass="emp_field_long_input"/>
			<emp:textarea id="IqpRentInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpRentInfo.rent_serno" label="出租编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpRentInfo.serno" label="业务编号" maxlength="40"  required="false" hidden="true"/>
		</emp:gridLayout>
	 </emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
