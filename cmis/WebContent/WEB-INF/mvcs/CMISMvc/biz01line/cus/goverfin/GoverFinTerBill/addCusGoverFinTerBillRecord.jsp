<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%	
	String cus_id=request.getParameter("cus_id"); 
	String manager_id=request.getParameter("manager_id");
	String manager_br_id=request.getParameter("manager_br_id");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_cus_addr_input {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 617;
};
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){
		CusGoverFinTerBill.bill_no._obj.addOneButton('viewCus','选择',showCusDetails);
	};	
	function showCusDetails(){
		var url = '<emp:url action="queryGoverBillNoPop.do"/>?cus_id='+"<%=cus_id%>"+'&returnMethod=selCusId';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=400,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	/*** 选择借据并校验begin ***/
	function selCusId(data){
		cus_id  = CusGoverFinTerBill.cus_id._getValue() ;
		bill_no  = data.bill_no._getValue() ;
		loan_amount  = data.loan_amt._getValue() ;
		loan_balance  = data.loan_balance._getValue() ;

		var url = '<emp:url action="checkCusidApplyed.do"/>?cus_id='+cus_id+'&bill_no='+bill_no;
		checkCusid(url);
	};
	function checkCusid(url){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == 'true'){
				CusGoverFinTerBill.bill_no._setValue( bill_no );
				CusGoverFinTerBill.loan_amount._setValue( loan_amount );
				CusGoverFinTerBill.loan_balance._setValue( loan_balance );
			}else{
				alert("已存在此借据的记录!");
			}
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};
	/*** 选择借据并校验end ***/
	function doReturn() {
		var url = '<emp:url action="queryCusGoverFinTerBillList.do"/>?cus_id='+"<%=cus_id%>"+
		'&manager_id='+"<%=manager_id%>"+'&manager_br_id='+"<%=manager_br_id%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doSaveRecord(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
		            alert('保存成功!');
		            doReturn();
				}else if(operMsg=='2'){
					alert('保存失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = CusGoverFinTerBill._checkAll();
		if(result){
			CusGoverFinTerBill._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	
	function onReturnDirection(date){
		CusGoverFinTerBill.loan_direction._obj.element.value=date.id;
		CusGoverFinTerBill.loan_direction_name._obj.element.value=date.label;
	};
	function getOrgID(data){
		CusGoverFinTerBill.manager_br_id._setValue(data.organno._getValue());
		CusGoverFinTerBill.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function setconId(data){
		CusGoverFinTerBill.manager_id_displayname._setValue(data.actorname._getValue());
		CusGoverFinTerBill.manager_id._setValue(data.actorno._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addCusGoverFinTerBillRecord.do" method="POST">
		<emp:gridLayout id="CusGoverFinTerBillGroup" title="政府融资平台借据信息" maxColumn="2">
			<emp:text id="CusGoverFinTerBill.serno" label="申请流水号" maxlength="40" hidden="true" readonly="true" />
			<emp:text id="CusGoverFinTerBill.cus_id" label="客户码" maxlength="30" required="true" readonly="true" defvalue="${context.cus_id}" />
			<emp:text id="CusGoverFinTerBill.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" defvalue="${context.cus_name}"/>
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
			<emp:text id="CusGoverFinTerBill.input_id_displayname" label="登记人" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_br_id_displayname" label="登记机构" readonly="true" required="false"  hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_id" label="登记人" maxlength="20" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="false"  hidden="true"/>
			<emp:text id="CusGoverFinTerBill.manager_id" label="责任人" maxlength="20" required="false" readonly="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.manager_br_id" label="管理机构"  required="false" hidden="true"/>
			<emp:text id="CusGoverFinTerBill.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusGoverFinTerBill.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY" colSpan="2" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="saveRecord" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>