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
		var url = '<emp:url action="queryFncInventoryList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="FncInventoryGroup" title="主要存货明细表" maxColumn="2">
			<emp:pop id="FncInventory.cus_id" label="客户码" url="selectCusInfoPop.do?type=indiv" returnMethod="returnCus" required="true" />
			<emp:text id="FncInventory.cus_name" label="客户名称" maxlength="80" required="true" />
			<emp:text id="FncInventory.fnc_ym" label="年月" maxlength="6" required="true" />
			<emp:select id="FncInventory.fnc_per_typ" label="报表周期类型" dictname="STD_ZB_FNC_STAT" required="true" />			
			<emp:text id="FncInventory.fnc_invy_name" label="存货名称" maxlength="200" required="true" />
			<emp:select id="FncInventory.guar_st" label="抵质押情况" dictname="STD_ZB_GUAR_ST" required="true" />		
			<emp:select id="FncInventory.fnc_prc_typ" label="计价方式" dictname="STD_ZB_FNC_PRT" required="true" />
			<emp:select id="FncInventory.fnc_invy_typ" label="存货种类" dictname="STD_ZX_CUR_TYPE" required="true" />
			<emp:text id="FncInventory.fnc_invy_amt" label="数量" maxlength="38" required="true" dataType="Int"/>
			<emp:text id="FncInventory.fnc_invy_val" label="价值" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="FncInventory.fnc_invy_dt" label="库存日期" required="true" />
			<emp:select id="FncInventory.fnc_unit" label="单位" defMsg="未说明" required="false" colSpan="2"/>
			<emp:textarea id="FncInventory.remark" label="备注" maxlength="100" required="true" colSpan="2"/>			
			<emp:text id="FncInventory.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncInventory.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncInventory.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncInventory.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="FncInventory.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncInventory.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncInventory.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncInventory.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
