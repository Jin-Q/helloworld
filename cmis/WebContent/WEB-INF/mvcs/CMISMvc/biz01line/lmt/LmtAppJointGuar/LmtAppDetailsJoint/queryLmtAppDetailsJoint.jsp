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
	request.setAttribute("canwrite","");
//	String app_type = context.getDataValue("app_type").toString();
//	String sub_type = context.getDataValue("sub_type").toString();
//	String belg_line = (String)context.getDataValue("BelgLine");
%>

<script type="text/javascript">
	
	//关闭
	function doCloseAppDet(){
		window.close();
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtAppDetailsGroup" title="额度分项信息" maxColumn="2">
			<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="true" hidden="true"/>
			<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="40" required="true" hidden="true"/>
			<emp:text id="LmtAppDetails.limit_name_displayname" label="额度品种名称"   required="true" />
			<emp:text id="LmtAppDetails.prd_id" label="适用产品编号" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true"  colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppDetails.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="true" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAppDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppDetails.term" label="授信期限" maxlength="5" required="false" />
			
			<emp:date id="LmtAppDetails.start_date" label="授信起始日" required="false" hidden="true"/>
			<emp:date id="LmtAppDetails.end_date" label="授信到期日" required="false" hidden="true"/>
			<emp:select id="LmtAppDetails.update_flag" label="修改类型" required="false" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true" hidden="true"/>
			<emp:text id="LmtAppDetails.ori_crd_amt" label="原有授信金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="closeAppDet" label="关闭" />
	</div>
</body>
</html>
</emp:page>
