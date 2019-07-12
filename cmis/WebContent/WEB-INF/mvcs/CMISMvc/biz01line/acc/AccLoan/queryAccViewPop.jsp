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
		AccView._toForm(form);
		AccViewPopList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = AccViewPopList._obj.getSelectedData();
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
	function returnCus(data){
		AccView.cus_id._setValue(data.cus_id._getValue());
    	AccView.cus_name._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="AccView.bill_no" label="借据编号"  />
		<emp:text id="AccView.cont_no" label="合同编号" />
		<emp:pop id="AccView.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
		<emp:text id="AccView.cus_id" label="客户码" hidden="true"/>
		
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="AccViewPopList" pageMode="true" url="pageAccViewPop.do?cusTypCondition=${context.cusTypCondition}&from=${context.from}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="bill_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="bill_bal" label="贷款余额" dataType="Currency"/>
		<emp:text id="start_date" label="发放日期"  hidden="false"/>
		<emp:text id="end_date" label="到期日期"  hidden="false"/>
		<emp:text id="status" label="台账状态" dictname="STD_ZB_ACC_TYPE" />
		
		<emp:text id="cur_type" label="币种" hidden="true"/>
		<emp:text id="inner_owe_int" label="表内欠息" hidden="true"/>
		<emp:text id="out_owe_int" label="表外欠息"  hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构"  hidden="true"/>
		<emp:text id="fina_br_id" label="账务机构"  hidden="true"/>
		<emp:text id="fina_br_id_displayname" label="账务机构"  hidden="true"/>
		<emp:text id="reality_ir_y" label="执行利率" hidden="true"/>
		<emp:text id="five_class" label="五级分类" hidden="true"/>
		<emp:text id="twelve_cls_flg" label="十二级分类" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>