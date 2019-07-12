<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%	
	String cus_id=request.getParameter("cus_id"); 
	String manager_id=request.getParameter("manager_id");
	String manager_br_id=request.getParameter("manager_br_id");
	String ops=request.getParameter("ops");
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_cus_addr_input {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 617;
};
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
		
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusGoverFinTerBillList.do"/>?cus_id='+"<%=cus_id%>"+'&manager_id='
		+"<%=manager_id%>"+'&manager_br_id='+"<%=manager_br_id%>"+'&ops='+"<%=ops%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onLoad(){
		CusGoverFinTerBill.cus_id._obj.addOneButton('view12','查看',viewCusInfo);//客户信息查看
		CusGoverFinTerBill.bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
	};

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusGoverFinTerBill.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function viewAccInfo(){
		var accNo = CusGoverFinTerBill.bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+accNo;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:gridLayout id="CusGoverFinTerBillGroup" title="政府融资平台借据信息" maxColumn="2">
			<emp:text id="CusGoverFinTerBill.serno" label="申请流水号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CusGoverFinTerBill.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGoverFinTerBill.cus_id_displayname" label="客户名称" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="CusGoverFinTerBill.bill_no" label="借据编号" maxlength="30" required="true" colSpan="2" readonly="true"/>
			<emp:text id="CusGoverFinTerBill.loan_amount" label="借款金额(元)" maxlength="18" required="true" readonly="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTerBill.loan_balance" label="借款余额(元)" maxlength="18" required="true" readonly="true" dataType="Currency"/>
			<emp:select id="CusGoverFinTerBill.repayment_mode" label="还款方式" required="true" dictname="STD_ZB_REPAYMENT_MODE" />
			<emp:select id="CusGoverFinTerBill.rfn_ori" label="还款来源"  required="true" dictname="STD_ZB_RFN_ORI" />
			<emp:pop id="CusGoverFinTerBill.loan_direction_name" label="政府融资平台贷款投向" required="true" cssElementClass="emp_field_cus_addr_input" 
			url="showDicTree.do?dicTreeTypeId=STD_ZB_GOVER_DIRECTION" returnMethod="onReturnDirection" colSpan="2"/>
			<emp:select id="CusGoverFinTerBill.sort_disp_status" label="当期分类处置情况" required="false" dictname="STD_ZB_SORT_DISP_STATUS" />
			<emp:text id="CusGoverFinTerBill.loan_direction" label="贷款投向" required="true" hidden="true"/>
			
			<emp:pop id="CusGoverFinTerBill.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="false" hidden="true"/>
			<emp:pop id="CusGoverFinTerBill.manager_br_id_displayname" label="管理机构"  required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_id_displayname" label="登记人"  readonly="true" required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_br_id_displayname" label="登记机构"   readonly="true" required="false"  hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_id" label="登记人" maxlength="20" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="false"  hidden="true"/>
			<emp:text id="CusGoverFinTerBill.manager_id" label="责任人" maxlength="20" required="false" readonly="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.manager_br_id" label="管理机构"  required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" />
			<emp:text id="CusGoverFinTerBill.input_date" label="登记日期" required="false" readonly="true"  colSpan="2" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
