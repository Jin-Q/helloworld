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
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComAptitude.cus_id._obj.element.value;
		var paramStr="CusComAptitude.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComAptitudeList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusComAptitudeGroup" title="对公客户资质信息表" maxColumn="2">
			<emp:text id="CusComAptitude.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:select id="CusComAptitude.com_apt_typ" label="资质类型" required="false" dictname="STD_ZB_COM_APT_TYP" hidden="true"/>
			<emp:text id="CusComAptitude.com_apt_code" label="资质证书编号" maxlength="40" required="true" hidden="false"/>
			<emp:text id="CusComAptitude.com_apt_name" label="资质名称" maxlength="60" required="true" />
			<emp:text id="CusComAptitude.com_apt_cls" label="资质等级" maxlength="20" required="false" />
			<emp:text id="CusComAptitude.reg_bch_id" label="发证/登记机构" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:date id="CusComAptitude.crt_date" label="发证/登记日期" required="true" />
			<emp:date id="CusComAptitude.com_apt_expired" label="资质到期日期"  required="true" />
			<emp:textarea id="CusComAptitude.com_apt_dec" label="资质说明" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="CusComAptitude.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComAptitude.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComAptitude.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:date id="CusComAptitude.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:text id="CusComAptitude.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComAptitude.last_upd_date" label="更新日期" required="false" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>