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
		ExtensionAgrPop._toForm(form);
		resultSet._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.ExtensionAgrPopGroup.reset();
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
		ExtensionAgrPop.cus_id._setValue(data.cus_id._getValue());
		ExtensionAgrPop.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="ExtensionAgrPopGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="ExtensionAgrPop.agr_no" label="协议编号" />
		<emp:pop id="ExtensionAgrPop.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
		<emp:text id="ExtensionAgrPop.fount_bill_no" label="原借据编号"  />
		<emp:text id="ExtensionAgrPop.cus_id" label="客户码"  hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="resultSet" pageMode="true" url="pageExtensionAgrNo.do?orgId=${context.orgId}">
		<emp:text id="agr_no" label="展期协议编号" />
		<emp:text id="fount_bill_no" label="原借据编号" />
		<emp:text id="fount_cont_no" label="原合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="fount_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="extension_amt" label="展期金额" dataType="Currency"/>
		<emp:text id="extension_date" label="展期到期日期" />
		<emp:text id="extension_rate" label="展期利率(年)" dataType="Rate"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="status" label="协议状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		
		<emp:text id="fount_loan_amt" label="贷款金额" hidden="true"/>
		<emp:text id="fount_loan_balance" label="贷款余额" hidden="true"/>
		<emp:text id="fount_rate" label="利率" hidden="true"/>
		<emp:text id="base_rate" label="基准利率" hidden="true"/>
		<emp:text id="fount_start_date" label="起贷日期" hidden="true"/>
		<emp:text id="fount_end_date" label="止贷日期" hidden="true"/>
		<emp:text id="memo" label="说明书" hidden="true"/>
		<emp:text id="manager_id_displayname" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="责任机构" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>		
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>