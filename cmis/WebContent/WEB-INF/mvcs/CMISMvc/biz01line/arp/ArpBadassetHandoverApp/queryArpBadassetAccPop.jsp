<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String outCondition = "";
	if(context.containsKey("outCondition")){
		outCondition = (String)context.getDataValue("outCondition");
	}
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doLoad(){
	    var url="<emp:url action='pageArpBadassetAccPop.do'/>&outCondition=<%=outCondition%>";debugger;
	    AccLoanList._obj.url = EMPTools.encodeURI(url); 
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	function returnCus(data){
		AccLoan.cus_id._setValue(data.cus_id._getValue());
	};
	/*--user code begin--*/
	function doReturnMethod(){
		var data = AccLoanList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccLoan.bill_no" label="借据编号" />
			<emp:pop id="AccLoan.cus_id" label="客户码" url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" />
			<emp:date id="AccLoan.distr_date" label="发放日期" />
			<emp:date id="AccLoan.end_date" label="到期日期" /> 
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="AccLoanList" pageMode="true" url="pageArpBadassetAccPop.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>   
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" hidden="true" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bill_bal" label="贷款余额" dataType="Currency"/>
		<emp:text id="bill_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="inner_owe_int" label="表内欠息" dataType="Currency" hidden="true"/>
		<emp:text id="out_owe_int" label="表外欠息" dataType="Currency" hidden="true"/>
		<emp:text id="rec_int_accum" label="应收利息累计" dataType="Currency" hidden="true"/>
		<emp:text id="recv_int_accum" label="实收利息累计" dataType="Currency" hidden="true"/>
		<emp:text id="start_date" label="发放日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="fina_br_id_displayname" label="账务机构" />
		<emp:text id="status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="twelve_cls_flg" label="十二级分类" dictname="STD_ZB_TWELVE_CLASS"  hidden="true"/>
		<emp:text id="normal_balance" label="正常余额" dataType="Currency" hidden="true"/>
		<emp:text id="overdue_balance" label="逾期余额" dataType="Currency" hidden="true"/>
		<emp:text id="slack_balance" label="呆滞余额" dataType="Currency" hidden="true"/>
		<emp:text id="bad_dbt_balance" label="呆账余额" dataType="Currency" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>