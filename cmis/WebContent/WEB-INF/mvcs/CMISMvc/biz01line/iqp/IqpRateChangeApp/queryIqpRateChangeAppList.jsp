<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpRateChangeApp._toForm(form);
		IqpRateChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpRateChangeAppPage() {
		var paramStr = IqpRateChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpRateChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getIqpRateChangeAppUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpRateChangeApp() {
		var paramStr = IqpRateChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpRateChangeAppViewPage.do"/>?'+paramStr+'&his=no';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpRateChangeAppPage() {
		var url = '<emp:url action="getIqpRateChangeAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpRateChangeApp() {
		var paramStr = IqpRateChangeAppList._obj.getParamStr(['serno']);
		var approve_status = IqpRateChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
		if (paramStr != null) {
			if(approve_status == "000"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							var msg = jsonstr.msg;
							if(flag == "success"){
								alert("删除成功!");
								window.location.reload();
							}else {
								alert(msg);
							}
						}
					};
					var handleFailure = function(o){
						alert("异步请求出错！");	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					};
					var url = '<emp:url action="deleteIqpRateChangeAppRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
				
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpRateChangeAppGroup.reset();
	};
	function returnCus(data){
		IqpRateChangeApp.cus_id._setValue(data.cus_id._getValue());
		IqpRateChangeApp.cus_name._setValue(data.cus_name._getValue());
	};
	/*--user code begin--*/
	//提交流程
   function doSubWF(){ 
	   var paramStr = IqpRateChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) { 
			doSubmitWF();
		}else{
			alert('请先选择一条记录！');
		}
   };
		
   function doSubmitWF(){
		var serno = IqpRateChangeAppList._obj.getSelectedData()[0].serno._getValue();
		var cus_id = IqpRateChangeAppList._obj.getSelectedData()[0].cus_id._getValue();
		var cus_name = IqpRateChangeAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
		var approve_status = IqpRateChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
		WfiJoin.table_name._setValue("IqpRateChangeApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.prd_name._setValue("利率调整申请");
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
	    WfiJoin.appl_type._setValue("0023");//0023利率调整申请
		initWFSubmit(false);
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpRateChangeAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpRateChangeApp.bill_no" label="借据编号" />
			<emp:pop id="IqpRateChangeApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:text id="IqpRateChangeApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpRateChangeAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpRateChangeAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpRateChangeApp" label="删除" op="remove"/>
		<emp:button id="viewIqpRateChangeApp" label="查看" op="view"/>
		<emp:button id="subWF" label="提交" op="sub"/>
	</div>

	<emp:table icollName="IqpRateChangeAppList" pageMode="true" url="pageIqpRateChangeAppQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户码" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    