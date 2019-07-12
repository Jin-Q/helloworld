<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_select{
width:628px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		window.close();
	}

	//异步修改应收账款的状态
	function doRepayConfirm(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("该应收账款回款已完成。"); 
					//window.parent.location.reload();
					window.opener.location.reload();
					window.close();
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var po_no = IqpActrecbondDetail.po_no._getValue();
		var cont_no = IqpActrecbondDetail.cont_no._getValue();
		var invc_no = IqpActrecbondDetail.invc_no._getValue();
		var url = '<emp:url action="updateActrecStatus.do"/>?po_no='+po_no+"&cont_no="+cont_no+"&invc_no="+invc_no+"&repay=Y";

		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}

	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begins**/
	function returnBuyCus(data){
		IqpActrecbondDetail.buy_cus_id._setValue(data.cus_id._getValue());
		IqpActrecbondDetail.buy_cus_name._setValue(data.cus_name._getValue());
    };

    function returnSelCus(data){
		IqpActrecbondDetail.sel_cus_id._setValue(data.cus_id._getValue());
		IqpActrecbondDetail.sel_cus_name._setValue(data.cus_name._getValue());
    };
    /**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateIqpActrecbondDetailRecord.do" method="POST">
		<emp:gridLayout id="IqpActrecbondDetailGroup" title="应收账款明细" maxColumn="2">
			<emp:text id="IqpActrecbondDetail.po_no" label="池编号" maxlength="30" required="true" colSpan="2" readonly="true" />
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin -->
			<emp:pop id="IqpActrecbondDetail.buy_cus_name" label="买方客户名称"   colSpan="2"  required="true" cssElementClass="emp_field_text_long" url="queryAllCusPop.do?returnMethod=returnBuyCus"/>
			<emp:pop id="IqpActrecbondDetail.sel_cus_name" label="卖方客户名称"  colSpan="2"  required="true" cssElementClass="emp_field_text_long" url="queryAllCusPop.do?returnMethod=returnSelCus"/>
			<emp:select id="IqpActrecbondDetail.bond_mode" label="债权类型" cssFakeInputClass="emp_field_text_long_readonly" dictname="STD_ACTRECPO_BOND_TYPE" required="true" colSpan="2"/>
			<emp:text id="IqpActrecbondDetail.invc_no" label="权证号" maxlength="40" required="true" />
			
			<emp:text id="IqpActrecbondDetail.invc_amt" label="权证金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpActrecbondDetail.cont_no" label="贸易合同编号" maxlength="40" required="true" />
			<emp:text id="IqpActrecbondDetail.bond_amt" label="债权金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpActrecbondDetail.invc_date" label="权证日期"  required="true" />
			<emp:date id="IqpActrecbondDetail.bond_pay_date" label="付款日期"   required="true" />
			<emp:text id="IqpActrecbondDetail.status" label="状态" maxlength="5" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_id" label="登记人" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_br_id" label="登记机构" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.invc_ccy" label="发票币种" maxlength="3" required="false"  hidden="true"/>
			<emp:text id="IqpActrecbondDetail.buy_cus_id" label="买方客户编号" maxlength="40" required="false"  hidden="true"/>
			<emp:text id="IqpActrecbondDetail.sel_cus_id" label="卖方客户编号" maxlength="40" required="false"  hidden="true"/>
		</emp:gridLayout>
	</emp:form>
	<div align="center"><emp:button id="repayConfirm" label="回款确认"/>
	<emp:button id="return" label="关闭"/>
	</div>
	<emp:tabGroup mainTab="repaytab" id="main_tabs">
		<emp:tab label="回款信息" id="repaytab" needFlush="true" url="queryRIqpActrecRepayList.do?invc_no=${context.IqpActrecbondDetail.invc_no}&cont_no=${context.IqpActrecbondDetail.cont_no}&po_no=${context.IqpActrecbondDetail.po_no}&buy_cus_id=${context.IqpActrecbondDetail.buy_cus_id}&type=update"></emp:tab>
	</emp:tabGroup>
	<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end -->
</body>
</html>
</emp:page>
