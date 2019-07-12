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
<style type="text/css">
.emp_field_select_select1 {
	width: 200px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAverageAssetList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function onload(){
		IqpAverageAsset.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpAverageAsset.cont_no._obj.addOneButton("cont_no","查看",getContForm);
		IqpAverageAsset.bill_no._obj.addOneButton("bill_no","查看",getBillNoForm);
    };
    function getCusForm(){
		var cus_id = IqpAverageAsset.cus_id._getValue();
		if(cus_id != "" && cus_id != null && cus_id != "null"){
		   var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };

    function getBillNoForm(){
    	var bill_no = IqpAverageAsset.bill_no._getValue();
		if(bill_no != "" && bill_no != null && bill_no != "null"){
		   var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+bill_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
    function getContForm(){
    	var cont_no = IqpAverageAsset.cont_no._getValue();
		if(cont_no != "" && cont_no != null && cont_no != "null"){
		   var url = "<emp:url action='getAllCtrDetailView.do'/>&pvp=pvp&cont_no="+cont_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
			
	   <emp:gridLayout id="IqpAverageAssetGroup" title="基本信息" maxColumn="2">
			<emp:pop id="IqpAverageAsset.bill_no" label="借据编号" url="AccViewPop.do?cusTypCondition=table_model in('AccLoan','AccPad') and status='1'&returnMethod=returnAcc" required="true" />
			<emp:text id="IqpAverageAsset.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAverageAsset.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAverageAsset.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="详细信息" maxColumn="2">
             <emp:text id="IqpAverageAsset.loan_amt" label="贷款金额"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.loan_balance" label="贷款余额"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.distr_date" label="发放日期"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.end_date" label="到期日期"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.reality_ir_y" label="执行利率" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
             <emp:select id="IqpAverageAsset.five_class" label="五级分类"  required="false" dictname="STD_ZB_FIVE_SORT" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
             <emp:select id="IqpAverageAsset.twelve_cls_flg" label="十二级分类"  required="false" dictname="STD_ZB_TWELVE_CLASS" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
             <emp:text id="IqpAverageAsset.inner_owe_int" label="表内欠息"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.out_owe_int" label="表外欠息"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.manager_br_id_displayname" label="管理机构"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAsset.fina_br_id_displayname" label="账务机构"  required="false" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="3">
			<emp:text id="IqpAverageAsset.input_id_displayname" label="登记人"  required="false"  readonly="true"/>
			<emp:text id="IqpAverageAsset.input_br_id_displayname" label="登记机构"  required="false"  readonly="true"/>
			<emp:text id="IqpAverageAsset.input_id" label="登记人" maxlength="40" required="false"  hidden="true" readonly="true"/>
			<emp:text id="IqpAverageAsset.input_br_id" label="登记机构" maxlength="20" required="false"  hidden="true" readonly="true"/>
		    <emp:select id="IqpAverageAsset.average_status" label="资产状态 " required="false" dictname="STD_ZB_AVERGER_STATUS" />
		    <emp:text id="IqpAverageAsset.serno" label="业务流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
