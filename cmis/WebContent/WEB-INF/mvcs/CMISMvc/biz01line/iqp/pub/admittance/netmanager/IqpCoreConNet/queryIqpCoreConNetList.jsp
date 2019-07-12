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
		IqpCoreConNet._toForm(form);
		IqpCoreConNetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCoreConNetPage(){
		var paramStr = IqpCoreConNetList._obj.getParamStr(['serno']);
		if(paramStr != null){
			var status =IqpCoreConNetList._obj.getSelectedData()[0].approve_status._getValue();
			var app_type =IqpCoreConNetList._obj.getSelectedData()[0].app_type._getValue();
			if(status=='000' || status == "992" || status == "993"){
				var url;
                if(app_type == "01"){//建网
                   url = '<emp:url action="getIqpCoreConNetUpdatePage.do"/>?'+paramStr;
                }else if(app_type == "02"){//网络变更
                   url = '<emp:url action="getIqpCoreConNetViewPage4Change.do"/>?op=view&'+paramStr;
                }
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCoreConNet(){
		var paramStr = IqpCoreConNetList._obj.getParamStr(['serno']);
		if(paramStr != null){
			var app_type =IqpCoreConNetList._obj.getSelectedData()[0].app_type._getValue();
			var url;
            if(app_type == "01"){
            	url = '<emp:url action="getIqpCoreConNetViewPage.do"/>?'+paramStr+'&op=view&showTab=no&showMem=no&showBut=IqpCoreView';
            }else if(app_type == "02"){
            	url = '<emp:url action="getIqpCoreConNetViewPage.do"/>?'+paramStr+'&showMem=no&showBut=IqpCoreView';
            }
			
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCoreConNetPage(){
		var url = '<emp:url action="getIqpCoreConNetNextPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpCoreConNet(){
		var paramStr = IqpCoreConNetList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpCoreConNetList._obj.getSelectedData()[0].approve_status._getValue();
			if (approve_status == "000"){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteIqpCoreConNetRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						EMPTools.unmask();
						if(o.responseText !== undefined){
							try{
								var jsonstr = eval("("+o.responseText+")");
							}catch(e) {
								alert("删除失败!");
								return;
							}
							var flag=jsonstr.flag;	
							var flagInfo=jsonstr.flagInfo;						
							if(flag=="success"){
								alert('删除成功！');
								window.location.reload();								
							}
						}
					};
					var handleFailure = function(o){ 
						alert("删除失败，请联系管理员");
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}; 
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert("只有状态为'待发起'的数据才能删除！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCoreConNetGroup.reset();
	};
	
	function doSub(){
		var paramStr = IqpCoreConNetList._obj.getParamStr(['serno']);	
		if(paramStr != null){	
			var status=IqpCoreConNetList._obj.getSelectedData()[0].approve_status._getValue();
			if(status=='000' ||status=='992'||status=='998'){
				var handleSuccess = function(o){
	                var jsonstr = eval("(" + o.responseText + ")");
							var flag = jsonstr.flag;
							if(flag == "success" ){
								alert("提交成功！");
								window.location.reload();
							 }else{
								 alert("提交失败！");
							      }
							}
							var handleFailure = function(o){
							         alert("异步回调失败！");	
							};
							var url = '<emp:url action="subData2NetManager.do"/>?'+paramStr;
							var callback = {
							         success:handleSuccess,
							         failure:handleFailure
							};
							url = EMPTools.encodeURI(url);
							var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
				}else{
					alert("该记录已提交！不能进行再次提交！");
				}			
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doSubCoreConNet(){
		var paramStr = IqpCoreConNetList._obj.getParamValue(['serno']);
		var approve_status = IqpCoreConNetList._obj.getParamValue(['approve_status']);
		var cus_id = IqpCoreConNetList._obj.getParamValue(['cus_id']);
		var cus_name = IqpCoreConNetList._obj.getParamValue(['cus_id_displayname']);
		var app_type =IqpCoreConNetList._obj.getParamValue(['app_type']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("IqpCoreConNet");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			if(app_type == "01"){
				WfiJoin.appl_type._setValue("511");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
				WfiJoin.prd_name._setValue("网络建立申请");//产品名称
			}else if(app_type == "02"){
				WfiJoin.appl_type._setValue("513");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
				WfiJoin.prd_name._setValue("网络变更申请");//产品名称
			}
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_name);//客户名称
			
			initWFSubmit(false);
		}else{
			alert('请先选择一条记录！');
		}
	}

	function getCusInfo(data){
		IqpCoreConNet.cus_id._setValue(data.cus_id._getValue());
  	   	IqpCoreConNet.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="IqpCoreConNetGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpCoreConNet.serno" label="业务编号"/>	
			<emp:pop id="IqpCoreConNet.cus_id_displayname" label="核心企业客户名称" url="queryAllCusPop.do?cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'&returnMethod=getCusInfo" />		
			<emp:select id="IqpCoreConNet.approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpCoreConNet.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpCoreConNetPage" label="新增" op="add" />
		<emp:button id="getUpdateIqpCoreConNetPage" label="修改" op="update"/>
		<emp:button id="deleteIqpCoreConNet" label="删除" op="remove"/>
		<emp:button id="viewIqpCoreConNet" label="查看" op="view"/>
		<emp:button id="subCoreConNet" label="提交" op="sub"/>
	</div>

	<emp:table icollName="IqpCoreConNetList" pageMode="true" url="pageIqpCoreConNetQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="核心企业客户码" />
		<emp:text id="cus_id_displayname" label="核心企业客户名称" />
		<emp:select id="app_type" label="申请类型" dictname="STD_ZB_NET_APP_TYPE"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>		
		<emp:select id="flow_type" label="流程类型" hidden="true"/>
	    <emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    