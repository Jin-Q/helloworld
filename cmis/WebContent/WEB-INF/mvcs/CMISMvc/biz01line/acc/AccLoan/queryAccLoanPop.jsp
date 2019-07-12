<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
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
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="AccLoan.bill_no" label="借据编号"  />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="AccLoanList" pageMode="true" url="pageAccLoanPop.do?condition=${context.condition}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="distr_date" label="起贷日期" />
		<emp:text id="end_date" label="止贷日期" />
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE" />
		
		<emp:text id="ir_accord_type" label="利率依据方式" hidden="true"/>
		<emp:text id="ir_type" label="利率种类" hidden="true"/>
		<emp:text id="ruling_ir" label="基准利率（年）" hidden="true"/>
		<emp:text id="pad_rate_y" label="垫款利率（年）" hidden="true"/>
		<emp:text id="ir_adjust_type" label="利率调整方式" hidden="true"/>
		<emp:text id="ir_float_type" label="利率浮动方式" hidden="true"/>
		<emp:text id="ir_float_rate" label="利率浮动比" hidden="true"/>
		<emp:text id="ir_float_point" label="贷款利率浮动点数" hidden="true"/>
		<emp:text id="reality_ir_y" label="执行利率（年）" hidden="true"/>
		<emp:text id="overdue_float_type" label="逾期利率浮动方式" hidden="true"/>
		<emp:text id="overdue_rate" label="逾期利率浮动比" hidden="true"/>
		<emp:text id="overdue_point" label="逾期利率浮动点" hidden="true"/>
		<emp:text id="overdue_rate_y" label="逾期利率（年）" hidden="true"/>
		<emp:text id="default_float_type" label="违约利率浮动方式" hidden="true"/>
		<emp:text id="default_rate" label="违约利率浮动比" hidden="true"/>
		<emp:text id="default_point" label="违约利率浮动点" hidden="true"/>
		<emp:text id="default_rate_y" label="违约利率（年）" hidden="true"/>
		<emp:text id="ruling_ir_code" label="基准利率代码" hidden="true"/>
		
		<emp:text id="term_type" label="期限类型" hidden="true"/>
		<emp:text id="cont_term" label="合同期限" hidden="true"/>
		<emp:text id="manager_br_id" label="主管机构" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="主管机构" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>