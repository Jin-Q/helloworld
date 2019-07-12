<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<style type="text/css">
	.emp_field_textarea_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 450px;
		height: 50px;
	};
</style>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String updflag = context.getDataValue("updflag").toString();
	request.setAttribute("canwrite","");
	String app_type = context.getDataValue("app_type").toString();
%>
<script type="text/javascript">
	
	function doReturn() {
		var serno = LmtAppDetails.serno._getValue();
		var app_type = '<%=app_type%>';
		var url = '<emp:url action="queryLmtFrozenDetailsList.do"/>&serno='+serno+"&updflag=<%=updflag%>&app_type="+app_type;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="LmtAppDetailsGroup" title="额度冻结解冻信息" maxColumn="2">
		<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="false" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="false" hidden="true"/>
		<emp:text id="LmtAppDetails.org_limit_code" label="授信额度编号" maxlength="32" required="false" />
		<emp:text id="LmtAppDetails.limit_name_displayname" label="额度品种名称" required="true" cssElementClass="emp_field_text_readonly"/>
		<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="40" hidden="true" required="true" cssElementClass="emp_field_text_readonly"/>
		<emp:text id="LmtAppDetails.prd_id" label="适用产品" required="true" cssElementClass="emp_field_text_readonly"/>
		<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
		<emp:select id="LmtAppDetails.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
		<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="true" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtAgrDetails.froze_amt" label="已冻结金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" defvalue="0"/>
		<%if("03".equals(app_type)){ %>
		<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<%} %>
		<%if("04".equals(app_type)){ %>
		<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<%} %>
		<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="true" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="LmtAppDetails.start_date" label="起始日期" required="true" readonly="true"/>  
		<emp:text id="LmtAppDetails.end_date" label="到期日期" required="true" readonly="true"/>

		<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="false" colSpan="2" readonly="true" hidden="true" cssElementClass="emp_field_text_readonly"/>
		<emp:text id="LmtAppDetails.core_corp_cus_id" label="核心企业客户码 " required="false" hidden="true"/>
		<emp:text id="LmtAppDetails.core_corp_cus_name" label="核心企业客户名称" required="false" maxlength="40" hidden="true" cssElementClass="emp_field_text_readonly"/>
		<emp:text id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true" hidden="true" cssElementClass="emp_field_text_readonly"/>
		<emp:select id="LmtAppDetails.core_corp_duty" label="核心企业责任" required="false" hidden="true" dictname="STD_ZB_CORP_DUTY"/>
		<emp:select id="LmtAppDetails.is_adj_term" label="是否调整期限" required="false" hidden="true" dictname="STD_ZX_YES_NO"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<input type="button" value="返回到列表页面" onclick="doReturn()" class="button100">
	</div>
</body>
</html>
</emp:page>