<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cus_type = "";
	if(context.containsKey("cus_type")){
		cus_type = (String)context.getDataValue("cus_type");
	}
	String task_type = "";//&task_type=${context.PspCheckTask.task_type}
	if(context.containsKey("task_type")){
		task_type =(String)context.getDataValue("task_type");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
//贷款/保函/贸易融资
function doViewAccLoan() {
	var paramStr = AccLoanList._obj.getParamStr(['bill_no']);
	if (paramStr != null) {
		var url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr+'&openNew=Y';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};
//银承台账
function doViewAccAccp() {
	var paramStr = AccAccpList._obj.getParamStr(['bill_no']);
	if (paramStr != null) {
		var url = '<emp:url action="getAccAccpViewPage.do"/>?'+paramStr+'&openNew=Y';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};
//票据台账
function doViewAccDrft() {
	var paramStr = AccDrftList._obj.getParamStr(['bill_no']);
	if (paramStr != null) {
		var url = '<emp:url action="getAccDrftViewPage.do"/>?'+paramStr+'&openNew=Y';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};
//垫款台账
function doViewAccPad() {
	var paramStr = AccPadList._obj.getParamStr(['bill_no']);
	if (paramStr != null) {
		var url = '<emp:url action="getAccPadViewPage.do"/>?'+paramStr+'&openNew=Y';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};

function doViewIqpCusAcct() {
	var paramStr = AccLoanList._obj.getParamStr(['cont_no']);
	if (paramStr != null) {
		var url = '<emp:url action="queryIqpCusAcctList.do"/>?'+paramStr+'&op=view';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.5+',width='+window.screen.availWidth*0.8+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};
</script>
</head>
<body class="page_content">
<%if(!"05".equals(task_type)&&!"06".equals(task_type)){ %>
	<emp:gridLayout id="AccDetailsGroup" title="合计（人民币）" maxColumn="2">
		<emp:text id="AccDetails.loan_balance" label="贷款余额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.owe_int" label="贷款欠息累计" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.drft_amt" label="银承票面金额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.guarant_amt" label="保函金额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.rpay_amt" label="贴现金额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.tf_loan_bal" label="贸易融资余额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.pad_bal" label="垫款余额" readonly="true" maxlength="18" dataType="Currency" defvalue="0"/>
		<emp:text id="AccDetails.pad_bal_qx" label="垫款欠息累计" maxlength="18" dataType="Currency" hidden="true" defvalue="0"/>
	</emp:gridLayout>
	<br>
<%} %>
	<div class='emp_gridlayout_title'>贷款/保函/贸易融资台账</div>
	<emp:button id="viewAccLoan" label="查看" op="view"/>
	<emp:button id="viewIqpCusAcct" label="支付清单" op="view"/>
	<emp:table icollName="AccLoanList" pageMode="false" url="">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="distr_date" label="发放日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:select id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:select id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	<br>
	<%if(!cus_type.equals("indiv")){ %>
	<div class='emp_gridlayout_title'>银承台账</div>
	<emp:button id="viewAccAccp" label="查看" op="view"/>
	<emp:table icollName="AccAccpList" pageMode="false" url="">
		<emp:text id="daorg_cusid" label="出票人客户码" />
		<emp:text id="daorg_cusid_displayname" label="出票人名称 " />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="bill_isse_date" label="签发日期" />
		<emp:text id="isse_date" label="出票日期" />
		<emp:text id="porder_end_date" label="到期日期" />
		<emp:select id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:select id="accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	<br>
	<div class='emp_gridlayout_title'>票据流水台账</div>
	<emp:button id="viewAccDrft" label="查看" op="view"/>
	<emp:table icollName="AccDrftList" pageMode="false" url="">
		<emp:text id="discount_per" label="贴现人/交易对手编号" />
		<emp:text id="discount_per_displayname" label="贴现人/交易对手名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="rpay_amt" label="实付金额" dataType="Currency"/>
		<emp:text id="dscnt_int" label="贴现利息" dataType="Currency"/>
		<emp:text id="dscnt_date" label="贴现日期" />
		<emp:select id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:select id="accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	<br>
	<div class='emp_gridlayout_title'>垫款台账</div>
	<emp:button id="viewAccPad" label="查看" op="view"/>
	<emp:table icollName="AccPadList" pageMode="false" url="">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="pad_bill_no" label="垫款业务借据编号" />
		<emp:text id="pad_amt" label="垫款金额" dataType="Currency"/>
		<emp:text id="pad_bal" label="垫款余额" dataType="Currency"/>
		<emp:text id="pad_date" label="垫款日期" />
		<emp:select id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:select id="accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	<br>
	<%} %>
</body>
</html>
</emp:page>
    