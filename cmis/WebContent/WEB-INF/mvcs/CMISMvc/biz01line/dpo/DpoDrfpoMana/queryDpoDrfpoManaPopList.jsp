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
	IqpDrfpoMana._toForm(form);
	IqpDrfpoManaList._obj.ajaxQuery(null,form);
};
/**added by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 begin **/
function doReset(){
	page.dataGroups.IqpDrfpoManaGroup.reset();
};
/**added by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 end **/
function doReturnMethod(){
	var data = IqpDrfpoManaList._obj.getSelectedData();
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

/**added by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 begin **/
function returnCus(data){
	IqpDrfpoMana.cus_id._setValue(data.cus_id._getValue());
	IqpDrfpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
};
/**added by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 end **/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpDrfpoManaGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpDrfpoMana.drfpo_no" label="池编号" />		
		<emp:pop id="IqpDrfpoMana.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:text id="IqpDrfpoMana.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="IqpDrfpoManaList" pageMode="true" url="pageDpoDrfpoManaPopQuery.do">
		<emp:text id="drfpo_no" label="池编号" />
		<emp:text id="drfpo_type" label="池类型" dictname="STD_DRFPO_TYPE" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />		
		<emp:text id="bill_amt" label="在池票据总金额" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />		
		<emp:text id="status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    