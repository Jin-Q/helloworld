<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtAgrFinGuarRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAgrFinGuarGroup" title="融资性担保公司授信查询" maxColumn="2">
			<emp:text id="LmtAgrFinGuar.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="LmtAgrFinGuar.agr_no" label="协议编号" maxlength="40" required="true" />
			<emp:text id="LmtAgrFinGuar.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="LmtAgrFinGuar.fin_cls" label="融资类别" maxlength="20" required="false" />
			<emp:text id="LmtAgrFinGuar.fin_totl_limit" label="融资总额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtAgrFinGuar.single_quota" label="单户限额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="LmtAgrFinGuar.lmt_start_date" label="授信起始日期" required="false" />
			<emp:date id="LmtAgrFinGuar.lmt_end_date" label="授信到期日期" required="false" />
			<emp:select id="LmtAgrFinGuar.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" />
			<emp:text id="LmtAgrFinGuar.term" label="期限" maxlength="10" required="false" dataType="Int" />
			<emp:text id="LmtAgrFinGuar.guar_bail_multiple" label="担保放大倍数" maxlength="10" required="false" dataType="Int" />
			<emp:select id="LmtAgrFinGuar.guar_cls" label="担保类别" required="false" dictname="STD_ZB_GUAR_TYPE" />
			<emp:select id="LmtAgrFinGuar.eval_rst" label="评级结果" required="false" />
			<emp:text id="LmtAgrFinGuar.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="LmtAgrFinGuar.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="LmtAgrFinGuar.manager_id" label="责任人" maxlength="20" required="false" />
			<emp:text id="LmtAgrFinGuar.manager_br_id" label="责任机构" maxlength="20" required="false" />
			<emp:text id="LmtAgrFinGuar.input_date" label="登记日期" maxlength="10" required="false" />
			<emp:select id="LmtAgrFinGuar.lmt_term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

