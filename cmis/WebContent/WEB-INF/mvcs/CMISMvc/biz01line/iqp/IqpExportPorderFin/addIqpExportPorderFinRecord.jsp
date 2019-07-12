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
	
	<emp:form id="submitForm" action="addIqpExportPorderFinRecord.do" method="POST">
		
		<emp:gridLayout id="IqpExportPorderFinGroup" title="出口商票融资从表" maxColumn="2">
			<emp:text id="IqpExportPorderFin.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpExportPorderFin.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportPorderFin.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpExportPorderFin.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpExportPorderFin.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:select id="IqpExportPorderFin.invc_cur_type" label="发票币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpExportPorderFin.invc_amt" label="发票金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpExportPorderFin.biz_settl_mode" label="原业务结算方式" required="false" />
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

