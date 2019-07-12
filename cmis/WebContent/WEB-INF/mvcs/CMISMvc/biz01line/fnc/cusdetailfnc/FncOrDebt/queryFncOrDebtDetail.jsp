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
	
	function doReturn() {
		var url = '<emp:url action="queryFncOrDebtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="FncOrDebtGroup" title="或有负债表" maxColumn="2">
			<emp:pop id="FncOrDebt.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus" readonly="true" required="true" />
			<emp:text id="FncOrDebt.cus_name" label="客户名称" maxlength="80" required="true" readonly="true" />
			<emp:text id="FncOrDebt.fnc_ym" label="年月" maxlength="6" required="true" onchange="checkZero(this.value)"/>
			<emp:select id="FncOrDebt.fnc_or_debt_typ" label="或有负债类型" dictname="STD_ZB_FNC_ORDT_TYP" required="true" />
			<emp:text id="FncOrDebt.fnc_amt" label="金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="FncOrDebt.fnc_input_id" label="干系人" maxlength="60" required="true" />
			<emp:date id="FncOrDebt.fnc_input_date" label="日期" required="true" />
			<emp:textarea id="FncOrDebt.fnc_or_debt_des" label="或有负债明细" maxlength="100" required="true" colSpan="2"/>
			<emp:textarea id="FncOrDebt.remark" label="备注" maxlength="100" required="true" colSpan="2"/>
			<emp:text id="FncOrDebt.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserId" readonly="true" />
			
			<emp:text id="FncOrDebt.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organNo" readonly="true"/>
			<emp:text id="FncOrDebt.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
				<!-- 下面是真实值字段 -->	
			<emp:text id="FncOrDebt.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="FncOrDebt.last_upd_date" label="更新日期" maxlength="10" required="false" hidden="true"/>
			
				<emp:text id="FncOrDebt.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="FncOrDebt.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="FncOrDebt.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
				<emp:text id="FncOrDebt.pk_id" label="主键" maxlength="32" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
