<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>调整对比页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	/*--user code begin--*/
	function doReturn() {
		var url = "";
		var type = '${context.type}';
		if("indivApp"==type){   //个人额度申请
			url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&lrisk_type=${context.lrisk_type}';
		}else{
			url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&lrisk_type=${context.lrisk_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="AdjustDistinctionRecord.do" method="POST">
		<emp:gridLayout id="LmtAgrDetailsGroup" maxColumn="2" title="调整前分项信息">
			<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号" maxlength="32" required="false"/>
			<emp:text id="LmtAgrDetails.limit_name_displayname" label="额度品种名称" readonly="true" />
			<emp:select id="LmtAgrDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE"/>
			<emp:select id="LmtAgrDetails.limit_type" label="额度类型" required="false" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:select id="LmtAgrDetails.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="LmtAgrDetails.crd_amt" label="授信额度"  required="false" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.prd_id" label="适用产品" required="false" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称" required="false"  readonly="true" colSpan="2" />
			<emp:select id="LmtAgrDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAgrDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="LmtAgrDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAgrDetails.term" label="期限" maxlength="5" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppDetailsGroup" maxColumn="2" title="调整后分项信息">
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="false"/>
			<emp:text id="LmtAppDetails.limit_name_displayname" label="额度品种名称"   readonly="true" />
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE" readonly="true"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="false" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:select id="LmtAppDetails.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="false" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppDetails.prd_id" label="适用产品" required="false" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="false"  readonly="true" colSpan="2" />
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="LmtAppDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppDetails.term" label="期限" maxlength="5" />	
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>