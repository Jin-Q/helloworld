<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String arp = "";
	if(context.containsKey("arp")){
		arp = (String)context.getDataValue("arp");
	}
%>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoanPop._toForm(form);
		resultSet._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanPopGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = resultSet._obj.getSelectedData();
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
		AccLoanPop.cus_id._setValue(data.cus_id._getValue());
		AccLoanPop.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanPopGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="AccLoanPop.bill_no" label="借据编号"  />
		<emp:text id="AccLoanPop.cont_no" label="合同编号" />
		<emp:pop id="AccLoanPop.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="AccLoanPop.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="resultSet" pageMode="true" url="pageBillNoPop.do?moduleId=${context.moduleId}&condition=${context.condition}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />		
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<%if(!"arp".equals(arp)){%>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<%} %>		
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="inner_owe_int" label="表内欠息" />
		<emp:text id="out_owe_int" label="表外欠息" />
		<emp:text id="rec_int_accum" label="应收利息累计" hidden="true"/>
		<emp:text id="recv_int_accum" label="实收利息累计" hidden="true"/>
		<!--modified by wangj 需求编号【XD141222087】法人透支改造 begin-->
		<emp:text id="ruling_ir" label="基准利率（年）" dataType="Rate" hidden="true"/>
		<!--modified by wangj 需求编号【XD141222087】法人透支改造 end-->
		<emp:text id="rate" label="执行利率(年)" dataType="Rate"/>
		<emp:text id="start_date" label="起贷日期" />
		<emp:text id="end_date" label="止贷日期" />
		<emp:text id="five_class" label="五级分类 " dictname="STD_ZB_FIVE_SORT" />
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE" />
		<!-- add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin -->
		<emp:text id="cust_mgr_displayname" label="主管客户经理" hidden="true"/>
		<emp:text id="main_br_id_displayname" label="主管机构" hidden="true" />
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true" />
		<emp:text id="main_br_id" label="主管机构" hidden="true" />
		<!-- add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end -->
		
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>