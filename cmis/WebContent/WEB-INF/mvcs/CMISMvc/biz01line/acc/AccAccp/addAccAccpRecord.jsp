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
	
	<emp:form id="submitForm" action="addAccAccpRecord.do" method="POST">
		
		<emp:gridLayout id="AccAccpGroup" title="银承台帐" maxColumn="2">
			<emp:text id="AccAccp.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="AccAccp.acc_day" label="日期" maxlength="10" required="false" />
			<emp:text id="AccAccp.acc_year" label="年份" maxlength="5" required="false" />
			<emp:text id="AccAccp.acc_mon" label="月份" maxlength="5" required="false" />
			<emp:text id="AccAccp.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccAccp.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccAccp.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:select id="AccAccp.bill_type" label="票据类型" required="false" dictname="STD_DRFT_TYPE" />
			<emp:text id="AccAccp.porder_no" label="汇票号码" maxlength="40" required="false" />
			<emp:text id="AccAccp.utakeover_sign" label="不得转让标记" maxlength="5" required="false" />
			<emp:select id="AccAccp.is_ebill" label="是否电子票据" required="false" dictname="STD_ZX_YES_NO" />
			<emp:pop id="AccAccp.daorg_cusid" label="出票人客户码" url="null" required="false" />
			<emp:text id="AccAccp.daorg_cus_name" label="出票人名称" maxlength="80" required="false" />
			<emp:text id="AccAccp.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="false" />
			<emp:text id="AccAccp.daorg_no" label="出票人开户行行号" maxlength="40" required="false" />
			<emp:text id="AccAccp.daorg_name" label="出票人开户行行名" maxlength="100" required="false" />
			<emp:text id="AccAccp.daorg_acct" label="出票人开户行账号" maxlength="40" required="false" />
			<emp:select id="AccAccp.aorg_type" label="承兑行类型" required="false" dictname="STD_AORG_ACCTSVCR_TYPE" />
			<emp:text id="AccAccp.aorg_no" label="承兑行行号" maxlength="20" required="false" />
			<emp:text id="AccAccp.aorg_name" label="承兑行名称" maxlength="100" required="false" />
			<emp:text id="AccAccp.pyee_name" label="收款人名称" maxlength="100" required="false" />
			<emp:text id="AccAccp.paorg_no" label="收款人开户行行号" maxlength="20" required="false" />
			<emp:text id="AccAccp.paorg_name" label="收款人开户行行名" maxlength="100" required="false" />
			<emp:text id="AccAccp.paorg_acct_no" label="收款人账号" maxlength="40" required="false" />
			<emp:text id="AccAccp.exchange_rate" label="汇率" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="AccAccp.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccAccp.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccAccp.bill_isse_date" label="签发日期" required="false" />
			<emp:date id="AccAccp.isse_date" label="出票日期" required="false" />
			<emp:date id="AccAccp.porder_end_date" label="到期日期" required="false" />
			<emp:text id="AccAccp.pad_rate" label="垫款利率" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="AccAccp.pad_amt" label="垫款金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccAccp.paydate" label="转垫款日期" required="false" />
			<emp:date id="AccAccp.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccAccp.writeoff_date" label="核销日期" required="false" />
			<emp:select id="AccAccp.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccAccp.twelve_cls_flg" label="十二级分类标志" required="false" />
			<emp:text id="AccAccp.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="AccAccp.fina_br_id" label="账务机构" maxlength="20" required="false" />
			<emp:text id="AccAccp.accp_status" label="台帐状态" maxlength="5" required="false" />
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

