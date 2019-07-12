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
		CusGoverFinTer._toForm(form);
		CusGoverFinTerList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.CusGoverFinTerGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		CusGoverFinTer.cus_id._setValue(data.cus_id._getValue());
    };
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = CusGoverFinTerList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGoverFinTerGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGoverFinTer.serno" label="申请流水号" />
			<emp:pop id="CusGoverFinTer.cus_id" label="客户码"  
			buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />				
			<emp:select id="CusGoverFinTer.gover_fin_loan_type" label="政府融资贷款类型" dictname="STD_ZB_GOVER_FIN_TYPE" />
	</emp:gridLayout>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="CusGoverFinTerList" pageMode="true" url="pageCusGoverFinTerQuery.do">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="gover_fin_loan_type" label="政府融资贷款类型" dictname="STD_ZB_GOVER_FIN_TYPE" />
		<emp:text id="cash_cover_rate" label="现金流覆盖率" dataType="Rate" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>