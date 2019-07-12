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
	
	<emp:form id="submitForm" action="addIqpAccAccpRecord.do" method="POST">
		<emp:gridLayout id="IqpAccAccpGroup" title="银行承兑汇票" maxColumn="2">
			<emp:text id="IqpAccAccp.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpAccAccp.opac_type" label="签发类型" required="false" />
			<emp:text id="IqpAccAccp.chrg_rate" label="手续费率" required="true" dataType="Rate"/>
			<emp:text id="IqpAccAccp.opac_org" label="签发行" maxlength="20" required="false" />
			<emp:select id="IqpAccAccp.is_elec_bill" label="是否电子票据" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpAccAccp.bill_qty" label="汇票数量" maxlength="38" required="false" />
			<emp:text id="IqpAccAccp.adv_rate" label="垫款利率" maxlength="10" required="false" dataType="Rate" />
			<emp:select id="IqpAccAccp.acpt_org_type" label="承兑行类型" required="false" />
			<emp:text id="IqpAccAccp.actp_org_no" label="承兑行行号" maxlength="20" required="false" />
			<emp:text id="IqpAccAccp.actp_org_name" label="承兑行名称" maxlength="100" required="false" />
			<emp:textarea id="IqpAccAccp.use" label="用途" maxlength="250" required="false" colSpan="2" />
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
