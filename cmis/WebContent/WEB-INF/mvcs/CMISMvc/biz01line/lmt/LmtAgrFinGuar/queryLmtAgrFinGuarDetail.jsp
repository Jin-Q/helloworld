<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">

.emp_field_text_input_user_name { /****** 长度固定 ******/
	width: 451px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		//var url = '<emp:url action="queryLmtAgrFinGuarList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="授信基本信息" id="main_tabs">
			<emp:gridLayout id="LmtAgrFinGuarGroup" title="担保公司信息" maxColumn="2">
				<emp:text id="LmtAgrFinGuar.serno" label="业务编号" maxlength="40" required="true" cssElementClass="emp_field_text_readonly"/>
				<emp:text id="LmtAgrFinGuar.agr_no" label="协议编号" maxlength="40" required="true" />
				<emp:text id="LmtAgrFinGuar.cus_id" label="客户码" maxlength="30" required="true" />
				<emp:text id="LmtAgrFinGuar.cus_id_displayname" label="客户名称"  colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
				<emp:text id="LmtAgrFinGuar.fin_cls" label="融资类别" maxlength="20" required="false" />
				<emp:select id="LmtAgrFinGuar.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAgrFinGuarGroup" title="融资额度信息" maxColumn="2">
				<emp:select id="LmtAgrFinGuar.guar_cls" label="担保类别" required="false" dictname="STD_ZB_GUAR_TYPE" />
				<emp:select id="LmtAgrFinGuar.eval_rst" label="评级结果" required="false" dictname="STD_ZB_FINA_GRADE" />
				<emp:text id="LmtAgrFinGuar.guar_bail_multiple" label="担保放大倍数" maxlength="10" required="false" dataType="Int" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrFinGuar.fin_totl_limit" label="融资总额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrFinGuar.single_quota" label="单户限额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrFinGuar.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtAgrFinGuar.lmt_term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtAgrFinGuar.term" label="期限" maxlength="10" required="false" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
				<emp:date id="LmtAgrFinGuar.lmt_start_date" label="授信起始日期" required="false" />
				<emp:date id="LmtAgrFinGuar.lmt_end_date" label="授信到期日期" required="false" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAgrFinGuarGroup" title="登记信息" maxColumn="2">
				<emp:pop id="LmtAgrFinGuar.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
				<emp:pop id="LmtAgrFinGuar.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true"/>
				<emp:text id="LmtAgrFinGuar.input_id_displayname" label="登记人" required="false" defvalue="$currentUserId" readonly="true"/>
				<emp:text id="LmtAgrFinGuar.input_br_id_displayname" label="登记机构" required="false" defvalue="$organNo" readonly="true"/>
				<emp:text id="LmtAgrFinGuar.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
				<emp:text id="LmtAgrFinGuar.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
				<emp:text id="LmtAgrFinGuar.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAgrFinGuar.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAgrFinGuar.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			</emp:gridLayout>
			<div align="center">
				<br>
				<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
			</div>
		</emp:tab>
			<emp:ExtActTab></emp:ExtActTab>
			<emp:tab label="代偿信息" id="subTabs" url="queryLmtAppFinSubpayListForTab.do?LmtAppFinSubpay.cus_id=${context.LmtAgrFinGuar.cus_id}&dcpa=yes&rt=yes&type=app&op=${context.op}" initial="false" needFlush="true" ></emp:tab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
