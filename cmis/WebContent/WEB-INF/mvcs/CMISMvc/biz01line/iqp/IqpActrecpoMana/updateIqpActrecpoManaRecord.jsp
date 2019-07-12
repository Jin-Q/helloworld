<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpActrecpoManaRecord.do" method="POST">
		<emp:gridLayout id="IqpActrecpoManaGroup" maxColumn="2" title="应收账款池管理">
			<emp:text id="IqpActrecpoMana.po_no" label="池编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpActrecpoMana.po_type" label="业务类型" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="IqpActrecpoMana.po_mode" label="池模式" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.factor_mode" label="保理方式" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.is_rgt_res" label="是否有追索权" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.bail_acc_no" label="回款保证金账号" maxlength="40" required="false" />
			<emp:text id="IqpActrecpoMana.bail_acc_name" label="回款保证金账户名" maxlength="60" required="false" />
			<emp:text id="IqpActrecpoMana.invc_quant" label="发票数量" maxlength="16" required="false" />
			<emp:text id="IqpActrecpoMana.invc_amt" label="发票总金额" maxlength="16" required="false" />
			<emp:text id="IqpActrecpoMana.crd_rgtchg_amt" label="债权转让总金额" maxlength="16" required="false" />
			<emp:text id="IqpActrecpoMana.pledge_rate" label="质押率" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.period_grace" label="宽限期" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.memo" label="备注" maxlength="250" required="false" />
			<emp:text id="IqpActrecpoMana.status" label="状态" maxlength="5" required="false" />
			<emp:text id="IqpActrecpoMana.input_id" label="登记人" maxlength="30" required="false" />
			<emp:text id="IqpActrecpoMana.input_br_id" label="登记机构" maxlength="30" required="false" />
			<emp:text id="IqpActrecpoMana.input_date" label="登记日期" maxlength="10" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
