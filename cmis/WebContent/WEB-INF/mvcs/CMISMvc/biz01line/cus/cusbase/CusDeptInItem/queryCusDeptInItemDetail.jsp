<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
	/*	var cus_id  =CusDeptInItem.cus_id._obj.element.value;
		var paramStr="CusDeptInItem.cus_id="+cus_id;
		var url = '<emp:url action="queryCusDeptInItemList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;*/
		window.close();
	};
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:tabGroup id="CusCom_tabs" mainTab="base_tab">
	<emp:tab id="base_tab" label="基本信息" initial="true">
		<emp:form id="submitWFForm" action="#" method="POST" >
			<emp:gridLayout id="CusDeptInItemGroup" title="本行交易－本行存款" maxColumn="2">
				<emp:text id="CusDeptInItem.seq" label="序号" maxlength="38" hidden="true" readonly="true"/>
				<emp:text id="CusDeptInItem.cus_id" label="客户码" maxlength="20" required="true" hidden="true"/>
				<emp:text id="CusDeptInItem.acc_no" label="账号" maxlength="40" required="false" />
				<emp:select id="CusDeptInItem.cus_typ" label="客户类型" required="false" dictname="STD_ZB_CUS_TYPE" />
				<emp:text id="CusDeptInItem.cus_bch_id" label="开户机构代码" maxlength="20" required="true" />
				<emp:date id="CusDeptInItem.cus_open_dt" label="开户时间" required="false" />
				<emp:date id="CusDeptInItem.cus_close_dt" label="销户时间" required="false" />
				<emp:select id="CusDeptInItem.cus_acc_cur_typ" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
				<emp:select id="CusDeptInItem.cus_acc_typ" label="账户类型" required="false" dictname="STD_ZB_CUS_E_ACCTYP" />
				<emp:text id="CusDeptInItem.cus_acc_cls" label="科目" maxlength="20" required="false" />
				<emp:select id="CusDeptInItem.cus_acc_st" label="账户状态" required="false" dictname="STD_ZB_CUS_ACC_ST" />
				<emp:select id="CusDeptInItem.cus_deposit_typ" label="存款类型" required="false" dictname="STD_ZB_DEP_TYP" />
				<emp:text id="CusDeptInItem.cus_acc_blc" label="存款余额" maxlength="18" required="false" dataType="Currency" />
				<emp:date id="CusDeptInItem.cus_end_dt" label="到期日期" required="false" />
				<emp:text id="CusDeptInItem.cus_card_no" label="卡号" maxlength="20" required="false" />
				<emp:select id="CusDeptInItem.cert_typ" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" />
				<emp:text id="CusDeptInItem.cert_code" label="证件号码" maxlength="20" required="false" />
				<emp:text id="CusDeptInItem.cus_last_m_avg_day" label="账户上月日均存款余额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_high_amt" label="账户最高时点金额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_low_amt" label="账户最低时点金额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_last_y_avg_day" label="上年日均余额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_plot" label="本年积数" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_avg_day" label="本年日均余额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_last_plot" label="上年积数" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="CusDeptInItem.cus_avg_day_cr" label="年日均余额变化(%)" maxlength="6" required="false" dataType="Rate" />
				<emp:date id="CusDeptInItem.cus_happen_dt" label="发生日期" required="false" />
			</emp:gridLayout>
		</emp:form>
	</emp:tab>
	<emp:tab id="ArpPress_tab" label="交易明细" needFlush="true" url="getCusDeptInItemDetailList.do" reqParams="acc_no=${context.CusDeptInItem.acc_no}">
	</emp:tab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
