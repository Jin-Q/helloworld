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
		var url = '<emp:url action="queryFncAccPayableList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="FncAccPayableGroup" title="应付账款及帐龄分析表" maxColumn="2">
			<emp:pop id="FncAccPayable.cus_id" label="客户码" url="selectCusInfoPop.do?type=indiv" returnMethod="returnCus" required="true" />
			<emp:text id="FncAccPayable.cus_name" label="客户姓名" maxlength="80" required="true" />
			<emp:text id="FncAccPayable.fnc_con_cus_id" label="对方客户码" maxlength="30" required="true" />					
			<emp:text id="FncAccPayable.fnc_con_cus_name" label="对方客户名称" maxlength="60" required="true" />
			<emp:text id="FncAccPayable.fnc_ym" label="年月" maxlength="6" required="true" onchange="checkZero(this.value)"/>			
			<emp:select id="FncAccPayable.fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" required="true" />
			<emp:text id="FncAccPayable.fnc_sum_amt" label="应付款余额合计" maxlength="18" required="true" dataType="Currency" onchange="checkT()"/>
			<emp:text id="FncAccPayable.fnc_imm_trm_amt" label="即期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccPayable.fnc_imm_trm_amt)"/>
			<emp:text id="FncAccPayable.fnc_short_trm_amt" label="短期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccPayable.fnc_short_trm_amt)"/>
			<emp:text id="FncAccPayable.fnc_inter_trm_amt" label="中期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccPayable.fnc_inter_trm_amt)"/>
			<emp:text id="FncAccPayable.fnc_long_trm_amt" label="长期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccPayable.fnc_long_trm_amt)"/>
			<emp:select id="FncAccPayable.rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" required="false" />
			<emp:textarea id="FncAccPayable.remark" label="备注" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="FncAccPayable.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncAccPayable.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncAccPayable.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncAccPayable.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncAccPayable.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncAccPayable.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncAccPayable.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
			<emp:text id="FncAccPayable.pk_id" label="主键" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
