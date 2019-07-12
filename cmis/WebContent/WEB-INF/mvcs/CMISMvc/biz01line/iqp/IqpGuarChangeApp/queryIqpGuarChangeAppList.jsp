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
		IqpGuarChangeApp._toForm(form);
		IqpGuarChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpGuarChangeAppPage() {
		var paramStr = IqpGuarChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpGuarChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
			   var url = '<emp:url action="getIqpGuarChangeAppUpdatePage.do"/>?'+paramStr+'&op=update';
			   url = EMPTools.encodeURI(url);
			   window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}   
	};     
	
	function doViewIqpGuarChangeApp() {
		var paramStr = IqpGuarChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGuarChangeAppViewPage.do"/>?'+paramStr+'&op=view';  
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpGuarChangeAppPage() {
		var url = '<emp:url action="getIqpGuarChangeAppAddPage.do"/>?menuId=addIqpGuarChangeAppList';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpGuarChangeApp() {
		var paramStr = IqpGuarChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpGuarChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
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
						if(flag == "success"){ 
							alert("删除成功!");
							window.location.reload(); 
						}else {
							alert("异步请求出错！"); 
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
				var url = '<emp:url action="deleteIqpGuarChangeAppRecord.do"/>?'+paramStr;
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
		page.dataGroups.IqpGuarChangeAppGroup.reset();
	};

	function returnCus(data){
		IqpGuarChangeApp.cus_id._setValue(data.cus_id._getValue());
		IqpGuarChangeApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };

    function doSubWfiFlow(){
    	var paramStr = IqpGuarChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno = IqpGuarChangeAppList._obj.getSelectedData()[0].serno._getValue();
			var cus_id = IqpGuarChangeAppList._obj.getSelectedData()[0].cus_id._getValue();
			var prd_id_displayname = IqpGuarChangeAppList._obj.getSelectedData()[0].prd_id_displayname._getValue();
			var cus_id_displayname = IqpGuarChangeAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var approve_status = IqpGuarChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			var cont_amt = IqpGuarChangeAppList._obj.getSelectedData()[0].cont_amt._getValue();
			WfiJoin.table_name._setValue("IqpGuarChangeApp"); 
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(serno);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("019");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.prd_name._setValue(prd_id_displayname+"(担保变更申请)");//产品名称
			WfiJoin.amt._setValue(cont_amt); 
			initWFSubmit(false);
		} else {      
			alert('请先选择一条记录！');
		}
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
		<emp:button id="getAddIqpGuarChangeAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpGuarChangeAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpGuarChangeApp" label="删除" op="remove"/>
		<emp:button id="viewIqpGuarChangeApp" label="查看" op="view"/>
		<emp:button id="subWfiFlow" label="提交" op="submit"/>
	</div>

	<emp:table icollName="IqpGuarChangeAppList" pageMode="true" url="pageIqpGuarChangeAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
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
    