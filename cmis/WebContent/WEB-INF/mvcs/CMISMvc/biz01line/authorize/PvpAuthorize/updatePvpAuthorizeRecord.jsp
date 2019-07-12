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
	
	<emp:form id="submitForm" action="updatePvpAuthorizeRecord.do" method="POST">
		<emp:gridLayout id="PvpAuthorizeGroup" maxColumn="2" title="授权信息主表">
			<emp:text id="PvpAuthorize.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="PvpAuthorize.tran_serno" label="交易流水号" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.prd_id" label="产品编号" maxlength="10" required="false" />
			<emp:text id="PvpAuthorize.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.cus_name" label="客户名称" maxlength="80" required="false" />
			<emp:text id="PvpAuthorize.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.bill_no" label="借据编码" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.tran_id" label="交易码" maxlength="10" required="false" />
			<emp:text id="PvpAuthorize.tran_amt" label="交易金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="PvpAuthorize.tran_date" label="交易日期" required="false" />
			<emp:text id="PvpAuthorize.send_times" label="发送次数" maxlength="38" required="false" />
			<emp:text id="PvpAuthorize.return_code" label="返回编码" maxlength="10" required="false" />
			<emp:textarea id="PvpAuthorize.return_desc" label="返回说明" maxlength="250" required="false" colSpan="2" />
			<emp:text id="PvpAuthorize.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="PvpAuthorize.in_acct_br_id" label="入账机构" maxlength="20" required="false" />
			<emp:select id="PvpAuthorize.status" label="状态" required="false" />
			<emp:text id="PvpAuthorize.fldvalue01" label="FLDVALUE01" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue02" label="FLDVALUE02" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue03" label="FLDVALUE03" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue04" label="FLDVALUE04" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue05" label="FLDVALUE05" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue06" label="FLDVALUE06" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue07" label="FLDVALUE07" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue08" label="FLDVALUE08" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue09" label="FLDVALUE09" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue10" label="FLDVALUE10" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue11" label="FLDVALUE11" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue12" label="FLDVALUE12" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue13" label="FLDVALUE13" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue14" label="FLDVALUE14" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue15" label="FLDVALUE15" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue16" label="FLDVALUE16" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue17" label="FLDVALUE17" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue18" label="FLDVALUE18" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue19" label="FLDVALUE19" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue20" label="FLDVALUE20" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue21" label="FLDVALUE21" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue22" label="FLDVALUE22" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue23" label="FLDVALUE23" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue24" label="FLDVALUE24" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue25" label="FLDVALUE25" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue26" label="FLDVALUE26" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue27" label="FLDVALUE27" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue28" label="FLDVALUE28" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue29" label="FLDVALUE29" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue30" label="FLDVALUE30" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue31" label="FLDVALUE31" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue32" label="FLDVALUE32" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue33" label="FLDVALUE33" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue34" label="FLDVALUE34" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue35" label="FLDVALUE35" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue36" label="FLDVALUE36" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue37" label="FLDVALUE37" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue38" label="FLDVALUE38" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue39" label="FLDVALUE39" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.fldvalue40" label="FLDVALUE40" maxlength="40" required="false" />
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
