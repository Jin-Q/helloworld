<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="iqpBillDetailComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpBillDetailPvpList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnLoad(){
		var menuId = "${context.menuId}";
		if(menuId != 'billDetailPvpList'){
			document.getElementById('button_return').style.display = 'none';
		}
		chageBillType();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:tabGroup mainTab="mainTab" id="票据明细">
		<emp:tab label="票据明细" id="mainTab">
			<emp:form id="submitForm" action="updateIqpBillDetailRecord.do" method="POST">
				<emp:gridLayout id="IqpBillDetailGroup" title="票据基本信息" maxColumn="2">
					<emp:text id="IqpBillDetail.porder_no" label="汇票号码" maxlength="40" readonly="true" required="true" />
					<emp:select id="IqpBillDetail.bill_type" onblur="chageBillType();" label="票据种类" required="true" dictname="STD_DRFT_TYPE"/>
					<emp:select id="IqpBillDetail.porder_curr" label="汇票币种" required="true" dictname="STD_ZX_CUR_TYPE" />
					<emp:text id="IqpBillDetail.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" />
					<emp:select id="IqpBillDetail.porder_addr" label="汇票签发地" dictname="STD_ZB_DRFT_ADDR" required="true" />
					<emp:select id="IqpBillDetail.is_ebill" label="是否电票" required="false" dictname="STD_ZX_YES_NO" />
					<emp:date id="IqpBillDetail.bill_isse_date" label="票据签发日" required="true" />
					<emp:date id="IqpBillDetail.porder_end_date" label="汇票到期日" required="true" />
					<emp:select id="IqpBillDetail.utakeover_sign" label="不得转让标记" required="true" dictname="STD_ZX_YES_NO"/> 
					<emp:select id="IqpBillDetail.status" label="票据状态" required="true" dictname="STD_ZB_DRFT_STATUS"/>
				    <emp:textarea id="IqpBillDetail.tran_increment_invc" label="交易增值税发票号/货运发票号" maxlength="40" required="true" />
				    <emp:textarea id="IqpBillDetail.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="出票人信息" maxColumn="2">
					<emp:text id="IqpBillDetail.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="false" colSpan="2"/>
					<emp:text id="IqpBillDetail.isse_name" label="出票/付款人名称" maxlength="80" required="false" />  
					<emp:text id="IqpBillDetail.daorg_no" label="出票人开户行行号"  required="false" />
					<emp:text id="IqpBillDetail.daorg_name" label="出票人开户行行名" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpBillDetail.daorg_acct" label="出票人开户行账号"  required="false" />
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="贸易合同信息" maxColumn="2">
					<emp:text id="IqpBillDetail.tcont_no" label="贸易合同编号" maxlength="40" required="true" />
					<emp:text id="IqpBillDetail.tcont_amt" label="贸易合同金额" maxlength="18" required="true" dataType="Currency" />
					<emp:textarea id="IqpBillDetail.tcont_content" label="贸易合同内容" maxlength="500" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="收款人信息" maxColumn="2">
					<emp:text id="IqpBillDetail.pyee_name" label="收款人名称" maxlength="80" required="true" />
					<emp:text id="IqpBillDetail.paorg_no" label="收款人开户行行号"  required="true" />
					<emp:text id="IqpBillDetail.paorg_name" label="收款人开户行行名" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpBillDetail.paorg_acct_no" label="收款人开户行账号"  required="true" /> 
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="承兑行信息" maxColumn="2">
					<emp:select id="IqpBillDetail.aorg_type" label="承兑行类型" required="true" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
					<emp:text id="IqpBillDetail.aorg_no" label="承兑行行号" maxlength="20" required="true" />
					<emp:text id="IqpBillDetail.aorg_name" label="承兑行名称" maxlength="100" required="true" />
					<emp:select id="IqpBillDetail.aaorg_type" label="承兑人开户行类型" required="true" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
					<emp:text id="IqpBillDetail.aaorg_no" label="承兑人开户行行号"  required="true" />
					<emp:text id="IqpBillDetail.aaorg_name" label="承兑人开户行名称" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpBillDetail.accptr_cmon_code" label="承兑人组织机构代码" maxlength="20" required="true" />
					<emp:text id="IqpBillDetail.aaorg_acct_no" label="承兑人开户行账号" maxlength="40" required="true" />
					
				</emp:gridLayout>
				<div align="center">
					<br>
					
				</div>
			</emp:form>
		</emp:tab>
		<emp:tab label="票据流水" id="subTab" url="queryAccDrftList.do?porder_no=${context.porder_no}" initial="false" needFlush="true"/>
	</emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	
</body>
</html>
</emp:page>
