<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewAccLoan() {
		var paramStr = AccLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doSelect(){
		var data = AccLoanList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};    
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccLoan.cont_no" label="合同编号" />
			<emp:text id="AccLoan.prd_id" label="产品编号" />
			<emp:text id="AccLoan.cus_id" label="客户码" />
			<emp:date id="AccLoan.distr_date" label="发放日期" />
			<emp:date id="AccLoan.end_date" label="到期日期" />
	</emp:gridLayout> 
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<button onclick="doSelect()">选取返回</button>
	</div>

	<emp:table icollName="AccLoanList" pageMode="true" url="pageAccLoanForAssetQuery.do" reqParams="restrictUsed=${context.restrictUsed}">
		<emp:text id="serno" label="业务编号" hidden="true"/>  
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="prd_id_displayname" label="产品编号" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="distr_date" label="发放日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="fina_br_id_displayname" label="账务机构" />
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="repay_type" label="还款方式" hidden="true"/>
		<emp:text id="repay_type_displayname" label="还款方式" hidden="true"/>
		<emp:text id="interest_term" label="结息方式" hidden="true"/>
		<emp:text id="five_class" label="五级分类" hidden="true"/>
		<emp:text id="assure_main" label="担保方式" hidden="true"/>
		<emp:text id="reality_ir_y" label="执行年利率（年）" hidden="true"/>
		<emp:text id="distr_date" label="发放日期" hidden="true"/>
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="repay_term" label="还款间隔周期" hidden="true"/>
		<emp:text id="repay_space" label="还款间隔" hidden="true"/>
		<emp:text id="ir_accord_type" label="利率依据方式" hidden="true"/>
		<emp:text id="ir_type" label="利率种类" hidden="true"/>
		<emp:text id="ruling_ir" label="基准利率（年）" hidden="true"/>
		<emp:text id="pad_rate_y" label="垫款利率（年）" hidden="true"/>
		<emp:text id="ir_adjust_type" label="利率调整方式" hidden="true"/>
		<emp:text id="ir_next_adjust_term" label="下一次利率调整间隔" hidden="true"/>
		<emp:text id="ir_next_adjust_unit" label="下一次利率调整单位" hidden="true"/>
		<emp:text id="fir_adjust_day" label="第一次调整日" hidden="true"/>
		<emp:text id="ir_float_type" label="利率浮动方式" hidden="true"/>
		<emp:text id="ir_float_rate" label="利率浮动比" hidden="true"/>
		<emp:text id="ir_float_point" label="贷款利率浮动点数" hidden="true"/>
		<emp:text id="reality_ir_y" label="执行利率（年）" hidden="true"/>
		<emp:text id="overdue_float_type" label="逾期利率浮动方式" hidden="true"/>
		<emp:text id="overdue_rate" label="逾期利率浮动比" hidden="true"/>
		<emp:text id="overdue_point" label="逾期利率浮动点数" hidden="true"/>
		<emp:text id="overdue_rate_y" label="逾期利率（年）" hidden="true"/>
		<emp:text id="default_float_type" label="违约利率浮动方式" hidden="true"/>
		<emp:text id="default_rate" label="违约利率浮动比" hidden="true"/>
		<emp:text id="default_point" label="违约利率浮动点数" hidden="true"/>
		<emp:text id="default_rate_y" label="违约利率（年）" hidden="true"/>
		
	</emp:table>
	<div ><br>
	<button onclick="doSelect()">选取返回</button>
	<br>
	</div>
</body>
</html>
</emp:page>
    