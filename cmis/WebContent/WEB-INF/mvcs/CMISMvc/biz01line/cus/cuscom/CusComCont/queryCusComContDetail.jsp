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
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}

</style>
<script type="text/javascript">
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComCont.cus_id._obj.element.value;
		var paramStr="CusComCont.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComContList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusComContGroup" title="联系信息" maxColumn="2">
			<emp:text id="CusComCont.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusComCont.seq" label="序号" maxlength="38" required="true" hidden="true"/>
			<emp:select id="CusComCont.com_addr_typ" label="地址类型" required="true" dictname="STD_ZB_COM_ADDR_TYP"/>
			<emp:text id="CusComCont.com_addr" label="通讯地址" colSpan="2" hidden="true" />
			<emp:text id="CusComCont.com_addr_displayname" label="通讯地址" colSpan="2" cssElementClass="emp_field_text_input2" required="true" />
			<emp:text id="CusComCont.street_addr" label="街道" required="false" cssElementClass="emp_field_text_input2" maxlength="100" colSpan="2" />
			<emp:text id="CusComCont.com_zip_code" label="邮政编码" maxlength="6" required="false" dataType="Postcode" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComCont.com_phn_code" label="联系电话" maxlength="35" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComCont.com_fax_code" label="传真电话" maxlength="35" required="false" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="CusComCont.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComCont.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComCont.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComCont.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:date id="CusComCont.last_upd_date" label="更新日期" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
