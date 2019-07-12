<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
	request.setAttribute("canwrite","");
//	String app_type = context.getDataValue("app_type").toString();
//	String sub_type = context.getDataValue("sub_type").toString();
//	String belg_line = (String)context.getDataValue("BelgLine");
%>

<script type="text/javascript">
	
	//返回方法
	function doCloseAppDet(){
		window.close();
	}
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtAgrDetailsGroup" title="联保小组成员额度信息" maxColumn="2">
			<emp:text id="LmtAgrDetails.serno" label="业务编号" maxlength="40" required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:select id="LmtAgrDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE"/>
			<emp:select id="LmtAgrDetails.limit_type" label="额度类型" required="false" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号" maxlength="32" required="false" />
			<emp:text id="LmtAgrDetails.limit_name_displayname" label="额度品种名称"  required="true" />
			<emp:text id="LmtAgrDetails.limit_name" label="额度品种名称" maxlength="40" required="true" hidden="true"/>
			<emp:text id="LmtAgrDetails.prd_id" label="适用产品" required="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAgrDetails.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAgrDetails.crd_amt" label="授信额度"  required="true" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAgrDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="LmtAgrDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAgrDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAgrDetails.term" label="期限" maxlength="2" required="false" />
			
			<emp:date id="LmtAgrDetails.start_date" label="授信起始日" required="false" hidden="true"/>
			<emp:date id="LmtAgrDetails.end_date" label="授信到期日" required="false" hidden="true"/>
			<emp:select id="LmtAgrDetails.update_flag" label="修改类型" required="false" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true" hidden="true"/>
			<emp:text id="LmtAgrDetails.ori_crd_amt" label="原有授信金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAgrDetails.froze_amt" label="冻结金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAgrDetails.unfroze_amt" label="解冻金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="closeAppDet" label="关闭" />
	</div>
</body>
</html>
</emp:page>
