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
	
	<emp:form id="submitForm" action="addIqpDelivAssureRecord.do" method="POST">
		
		<emp:gridLayout id="IqpDelivAssureGroup" title="提货担保从表" maxColumn="2">
			<emp:text id="IqpDelivAssure.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpDelivAssure.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDelivAssure.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpDelivAssure.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpDelivAssure.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:text id="IqpDelivAssure.chrg_rate" label="手续费率" required="true" dataType="Rate"/>
			<emp:text id="IqpDelivAssure.cdt_cert_no" label="信用证编号" maxlength="40" required="false" />
			<emp:textarea id="IqpDelivAssure.cdt_approve_advice" label="国业部审查意见" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpDelivAssure.reorder_no" label="提单号码" maxlength="40" required="false" />
			<emp:text id="IqpDelivAssure.commo_name" label="商品名称" maxlength="100" required="false" />
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

