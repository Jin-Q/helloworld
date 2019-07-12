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
		IqpGuarChangeApp._toForm(form);
		IqpGuarChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpGuarChangeApp() {
		var paramStr = IqpGuarChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGuarChangeAppViewPage.do"/>?showFlow=is&'+paramStr+'&op=view';  
			url = EMPTools.encodeURI(url);  
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
 	
	function doReset(){
		page.dataGroups.IqpGuarChangeAppGroup.reset();
	};

	function returnCus(data){
		IqpGuarChangeApp.cus_id._setValue(data.cus_id._getValue());
		IqpGuarChangeApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };     
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>   

	<emp:gridLayout id="IqpGuarChangeAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpGuarChangeApp.serno" label="业务编号" />
			<emp:text id="IqpGuarChangeApp.cont_no" label="合同编号" />
			<emp:pop id="IqpGuarChangeApp.cus_id_displayname" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus"/>
			<emp:text id="IqpGuarChangeApp.cus_id" label="客户码" hidden="true"/> 
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpGuarChangeApp" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpGuarChangeAppList" pageMode="true" url="pageIqpGuarChangeAppHisQuery.do">
		<emp:text id="serno" label="业务编号" /> 
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/> 
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/> 
		<emp:text id="cont_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/> 
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    