<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String poNo= request.getParameter("poNo");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:628px;
}

.emp_select{
width:628px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){ 
	//	IqpActrecbondDetail.cont_no._obj.addOneButton("tcont_no","选择",getCont);
		if('<%=poNo%>'!='null'){
			IqpActrecbondDetail.po_no._setValue('<%=poNo%>');
		}else{
			IqpActrecbondDetail.invc_no._obj._renderReadonly(true);
		}
	}
	function returnCont(data){
		IqpActrecbondDetail.cont_no._setValue(data.tcont_no._getValue());
	};
	function doReturn(){
		window.close();
		}

	function doAdd(){
		var form = document.getElementById("submitForm");
		IqpActrecbondDetail._checkAll();
		if(IqpActrecbondDetail._checkAll()){
			var invcDate = IqpActrecbondDetail.invc_date._getValue();
			var bondPayDate = IqpActrecbondDetail.bond_pay_date._getValue();
			if(invcDate>bondPayDate){
				alert("付款日期早于开票日期！");
				return;
				}
			IqpActrecbondDetail._toForm(form);
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
						alert("保存成功！");
						window.opener.location.reload();
						window.close();
					}else if(flag == "exist"){
						alert("发票已存在！")
						return;
					}else {
						alert("保存失败！");
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	}
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
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
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addIqpActrecbondDetailRecord.do" method="POST">
		
		<emp:gridLayout id="IqpActrecbondDetailGroup" title="应收账款明细" maxColumn="2">
			<emp:text id="IqpActrecbondDetail.po_no" label="池编号" maxlength="30" required="false" colSpan="2" readonly="true" />
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin -->
			<emp:pop id="IqpActrecbondDetail.buy_cus_name" label="买方客户名称"  colSpan="2"  required="true" cssElementClass="emp_field_text_long" url="queryAllCusPop.do?returnMethod=returnBuyCus"/>
			<emp:pop id="IqpActrecbondDetail.sel_cus_name" label="卖方客户名称"  colSpan="2"  required="true" cssElementClass="emp_field_text_long" url="queryAllCusPop.do?returnMethod=returnSelCus"/>
			<emp:select id="IqpActrecbondDetail.bond_mode" label="债权类型"  dictname="STD_ACTRECPO_BOND_TYPE" required="true" colSpan="2" cssElementClass="emp_select"/>
			<emp:text id="IqpActrecbondDetail.invc_no" label="权证号" maxlength="40" required="true" />
			<emp:text id="IqpActrecbondDetail.invc_amt" label="权证金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:pop id="IqpActrecbondDetail.cont_no" label="贸易合同编号" required="true" url="getIqpBuscontInfoList.do?po_no=${context.poNo}&returnMethod=returnCont"/>
			<emp:text id="IqpActrecbondDetail.bond_amt" label="债权金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="IqpActrecbondDetail.invc_date" label="权证日期"  required="true" />
			<emp:date id="IqpActrecbondDetail.bond_pay_date" label="付款日期"   required="true" />
			<emp:text id="IqpActrecbondDetail.status" label="状态" maxlength="5" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_id" label="登记人" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_br_id" label="登记机构" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpActrecbondDetail.invc_ccy" label="发票币种" maxlength="3" required="false"  hidden="true"/>
			<emp:text id="IqpActrecbondDetail.buy_cus_id" label="买方客户编号" maxlength="40" required="false"  hidden="true"/>
			<emp:text id="IqpActrecbondDetail.sel_cus_id" label="卖方客户编号" maxlength="40" required="false"  hidden="true"/>
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end -->
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="add" label="保存"  />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

