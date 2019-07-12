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
		IqpAverageAssetApp._toForm(form);
		IqpAverageAssetAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAverageAssetAppPage() {
		var paramStr = IqpAverageAssetAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAverageAssetAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAverageAssetApp() {
		var paramStr = IqpAverageAssetAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAverageAssetAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAverageAssetAppPage() {
		var url = '<emp:url action="getIqpAverageAssetAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAverageAssetApp() {
		var paramStr = IqpAverageAssetAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAverageAssetAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){ 
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
				   var url = '<emp:url action="deleteIqpAverageAssetAppRecord.do"/>?'+paramStr;
				   url = EMPTools.encodeURI(url);
				   var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			   }
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行删除操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAverageAssetAppGroup.reset();
	};
	
	/*--user code begin--*/
    function returnCus(data){
    	IqpAverageAssetApp.cus_id._setValue(data.cus_id._getValue());
	   IqpAverageAssetApp.cus_name._setValue(data.cus_name._getValue());
    };	
    
    //流程提交
    function doSubmitWF(apply_type){
    	var paramStr = IqpAverageAssetAppList._obj.getParamStr(['serno']);
 		if (paramStr != null) {
 			var approve_status = IqpAverageAssetAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){ 
				var serno = IqpAverageAssetAppList._obj.getSelectedData()[0].serno._getValue();
	 			var cus_id = IqpAverageAssetAppList._obj.getSelectedData()[0].cus_id._getValue();
	 			var cus_name = IqpAverageAssetAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
	 			var approve_status = IqpAverageAssetAppList._obj.getSelectedData()[0].approve_status._getValue();
	 			WfiJoin.table_name._setValue("IqpAverageAssetApp");
	 			WfiJoin.pk_col._setValue("serno");
	 			WfiJoin.pk_value._setValue(serno);
	 			WfiJoin.cus_id._setValue(cus_id);
	 			WfiJoin.cus_name._setValue(cus_name);
	 			WfiJoin.prd_name._setValue("卖出资产登记申请");
	 			WfiJoin.wfi_status._setValue(approve_status);
	 			WfiJoin.status_name._setValue("approve_status");
	 			WfiJoin.appl_type._setValue("0024");
	 			initWFSubmit(false);
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行提交操作！");
			}
 		}else{
 			alert('请先选择一条记录！');
 		}
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAverageAssetAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAverageAssetApp.bill_no" label="借据编号" />
			<emp:pop id="IqpAverageAssetApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:text id="IqpAverageAssetApp.cont_no" label="合同编号" />
			<emp:select id="IqpAverageAssetApp.approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpAverageAssetApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAverageAssetAppPage" label="新增" op="add"/>
		<!--<emp:button id="getUpdateIqpAverageAssetAppPage" label="修改" op="update"/>-->
		<emp:button id="deleteIqpAverageAssetApp" label="删除" op="remove"/>
		<emp:button id="viewIqpAverageAssetApp" label="查看" op="view"/>
		<emp:button id="submitWF" label="提交" op="sub"/>
	</div>

	<emp:table icollName="IqpAverageAssetAppList" pageMode="true" url="pageIqpAverageAssetAppQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    