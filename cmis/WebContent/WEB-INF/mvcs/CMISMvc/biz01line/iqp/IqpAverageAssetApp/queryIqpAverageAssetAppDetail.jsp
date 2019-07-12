<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
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
		var url = '<emp:url action="queryIqpAverageAssetAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function onload(){
		IqpAverageAssetApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpAverageAssetApp.cont_no._obj.addOneButton("cont_no","查看",getContForm);
		IqpAverageAssetApp.bill_no._obj.addOneButton("bill_no","查看",getBillNoForm);
    };
    function getCusForm(){
		var cus_id = IqpAverageAssetApp.cus_id._getValue();
		if(cus_id != "" && cus_id != null && cus_id != "null"){
		   var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };

    function getBillNoForm(){
    	var bill_no = IqpAverageAssetApp.bill_no._getValue();
		if(bill_no != "" && bill_no != null && bill_no != "null"){
		   var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+bill_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
    function getContForm(){
    	var cont_no = IqpAverageAssetApp.cont_no._getValue();
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
		<emp:gridLayout id="IqpAverageAssetAppGroup" title="基本信息" maxColumn="2">
			<emp:pop id="IqpAverageAssetApp.bill_no" label="借据编号" url="AccViewPop.do?cusTypCondition=table_model in('AccLoan','AccPad') and status='1'&returnMethod=returnAcc" required="true" />
			<emp:text id="IqpAverageAssetApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAverageAssetApp.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:text id="IqpAverageAssetApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="详细信息" maxColumn="2">
             <emp:text id="IqpAverageAssetApp.loan_amt" label="贷款金额"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.loan_balance" label="贷款余额"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.distr_date" label="发放日期"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.end_date" label="到期日期"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.reality_ir_y" label="执行利率" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
             <emp:select id="IqpAverageAssetApp.five_class" label="五级分类"  required="false" dictname="STD_ZB_FIVE_SORT" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
             <emp:select id="IqpAverageAssetApp.twelve_cls_flg" label="十二级分类"  required="false" dictname="STD_ZB_TWELVE_CLASS" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
             <emp:text id="IqpAverageAssetApp.inner_owe_int" label="表内欠息"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.out_owe_int" label="表外欠息"  required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.manager_br_id_displayname" label="管理机构"  required="false" cssElementClass="emp_currency_text_readonly"/>
             <emp:text id="IqpAverageAssetApp.fina_br_id_displayname" label="账务机构"  required="false" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="3">
			<emp:text id="IqpAverageAssetApp.input_id_displayname" label="登记人"  required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpAverageAssetApp.input_br_id_displayname" label="登记机构"  required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpAverageAssetApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}"  readonly="true"/>
			<emp:text id="IqpAverageAssetApp.input_id" label="登记人" maxlength="40" required="false" defvalue="${context.currentUserId}" hidden="true" readonly="true"/>
			<emp:text id="IqpAverageAssetApp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true" readonly="true"/>
			<emp:select id="IqpAverageAssetApp.approve_status" label="申请状态" required="false" defvalue="000" readonly="true" hidden="true" dictname="WF_APP_STATUS" />
		    <emp:text id="IqpAverageAssetApp.serno" label="业务流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"noButton".equals(flag)){ %>
		   <emp:button id="return" label="返回到列表页面"/>
		<%}%>
	</div>
</body>
</html>
</emp:page>
