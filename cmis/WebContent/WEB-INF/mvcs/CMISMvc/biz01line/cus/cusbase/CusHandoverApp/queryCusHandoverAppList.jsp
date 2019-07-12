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
		CusHandoverApp._toForm(form);
		CusHandoverAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusHandoverAppPage() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusHandoverAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			    return ;
			}
			paramStr = paramStr + "&update=update";
			var url = '<emp:url action="getCusHandoverAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverApp() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&update=newView";
			var url = '<emp:url action="getCusHandoverAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusHandoverAppPage() {
		var url = '<emp:url action="getCusHandoverAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//删除
	function doDeleteCusHandoverApp() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if(paramStr != null){
			var status = CusHandoverAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("只有状态为【待发起】的申请才可以进行删除！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusHandoverAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
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
		}else {
			alert('请先选择一条记录！');
		}
	};
    //风险拦截 added by yangzy 2014/10/28 客户移交增加风险拦截校验贷后检查任务
    function interRisk(){
        var serno = CusHandoverAppList._obj.getParamValue(['serno']);
	    var _applType="";
	    var _modelId="CusHandoverApp";
	    var _pkVal=serno;
	    var _preventIdLst="FFFA27693EC63EC6415A2E485689B4AC";
	    var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
        var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
        if(!_retObj || _retObj == '2' || _retObj == '5'){
            if( _retObj == '5'){
                alert("执行风险拦截有错误，请检查！");
			} 
		    return false;
	    }else{
		   return true;
	    }
    }
	function doSubmitCusHandoverApp() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
		//	paramStr = paramStr+"&approve_status=000"
			if(confirm("是否确认要提交？")){
				var org_type = CusHandoverAppList._obj.getParamValue(['org_type']);
				//支行内移交不走流程，跨支行需走流程
				if(org_type=='10'){//支行内
				    /* added by yangzy 2014/10/28 客户移交增加风险拦截校验贷后检查任务 start */
					if(!interRisk()){
						return false;
					}
					/* added by yangzy 2014/10/28 客户移交增加风险拦截校验贷后检查任务 end */
					var url = '<emp:url action="updateCusHandoverAppStatus.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=="0"){
								alert("提交成功!");
									//var paramStr2="CusHandoverApp.approve_status=000";
									//var url = '<emp:url action="queryCusHandoverAppList.do"/>&'+paramStr2;
									var url = '<emp:url action="queryCusHandoverAppList.do"/>';
									url = EMPTools.encodeURI(url);
									window.location = url;
						   }else if(flag == "2"){
								alert("移交明细为空，请添加移交明细后再操作！");
								return;
						   }else if(flag == "3"){
							   alert(jsonstr.msg);
	                           return;
						   }else {
	                           alert("请选择一个及以上托管用户后再提交!");
	                           return;
	                       }
						}
					};
					var handleFailure = function(o){};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					};
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}else{
					var serno = CusHandoverAppList._obj.getParamValue(['serno']);
			//		var cus_id = CusBlkLogoutappList._obj.getParamValue(['cus_id']);//客户码
			//		var cus_name = CusBlkLogoutappList._obj.getParamValue(['cus_name']);//客户名称
					var approve_status = CusHandoverAppList._obj.getParamValue(['approve_status']);//流程审批流程
					WfiJoin.table_name._setValue("CusHandoverApp");
					WfiJoin.pk_col._setValue("serno");
					WfiJoin.pk_value._setValue(serno);
					WfiJoin.wfi_status._setValue(approve_status);
					WfiJoin.status_name._setValue("approve_status");
					WfiJoin.appl_type._setValue("012");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			//		WfiJoin.cus_id._setValue(cus_id);
			//		WfiJoin.cus_name._setValue(cus_name);
					WfiJoin.prd_name._setValue("客户移交申请");
					initWFSubmit(false);
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusHandoverAppGroup.reset();
	};


	/*--user code begin--*/
    function idHandover(data){
    	//CusHandoverApp.handover_id._setValue(data.actorno._getValue());
		CusHandoverApp.handover_id_displayname._setValue(data.actorname._getValue());
		CusHandoverApp.handover_id._setValue(data.actorno._getValue());
	}

    function orgHandover(data){
    	//CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
		CusHandoverApp.handover_br_id_displayname._setValue(data.organname._getValue());
		CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
	}

	function idReceiver(data){
		var managerId= data.actorno._getValue();
		var managerIdName= data.actorname._getValue();
	    var handoverId = CusHandoverApp.handover_id_displayname._obj.element.value;
		var handoverIdName = CusHandoverApp.handover_id_displayname._obj.element.value;
		if(managerIdName==handoverIdName){
			alert("移出人和接收人不能是同一人!");
			return;
		}else{
			CusHandoverApp.receiver_id_displayname._setValue(managerIdName);
			CusHandoverApp.receiver_id._setValue(managerId);
		}
	}

	function orgReceiver(data){
		var retBrId = data.organno._getValue();
        var retBrIdName = data.organname._getValue();	
		var hanBrIdName = CusHandoverApp.handover_br_id_displayname._obj.element.value;
		if(retBrIdName==hanBrIdName){
			alert("不是机构内移交\n[移出机构]和[接收机构]不能 是同一个！");
			return;
		}
	    CusHandoverApp.receiver_br_id_displayname._setValue(retBrIdName);
	    CusHandoverApp.receiver_br_id._setValue(retBrId);  
    }
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusHandoverAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusHandoverApp.serno" label="申请流水号" colSpan="2"/>
			<emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover"  />
			<emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" />
			<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" />
			<emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver"  />
			<emp:text id="CusHandoverApp.handover_id" label="移出人" hidden="true"/>
			<emp:text id="CusHandoverApp.handover_br_id" label="移出机构" hidden="true"/>
			<emp:text id="CusHandoverApp.receiver_id" label="接收人" hidden="true" />
			<emp:text id="CusHandoverApp.receiver_br_id" label="接收机构" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusHandoverAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusHandoverAppPage" label="修改" op="update"/>
		<emp:button id="deleteCusHandoverApp" label="删除" op="remove"/>
		<emp:button id="viewCusHandoverApp" label="查看" op="view"/>
		<emp:button id="submitCusHandoverApp" label="提交" op="submit"/>
	</div>

	<emp:table icollName="CusHandoverAppList" pageMode="true" url="pageCusHandoverAppQuery.do" >
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="handover_id" label="移出人" hidden="true" />
		<emp:text id="handover_br_id" label="移出机构" hidden="true" />
		<emp:text id="receiver_id" label="接收人" hidden="true" />
		<emp:text id="receiver_br_id" label="接收机构" hidden="true" />
		<emp:text id="supervise_id" label="监交人" hidden="true" />
		<emp:text id="supervise_br_id" label="监交机构" hidden="true" />
		<emp:text id="handover_id_displayname" label="移出人" />
        <emp:text id="handover_br_id_displayname" label="移出机构" />
        <emp:text id="receiver_id_displayname" label="接收人" />
        <emp:text id="receiver_br_id_displayname" label="接收机构" />
        <emp:text id="supervise_id_displayname" label="监交人" hidden="true"/>
        <emp:text id="supervise_br_id_displayname" label="监交机构" hidden="true"/>
		<emp:text id="approve_status" label="状态" dictname="WF_APP_STATUS" />
		<emp:text id="org_type" label="移交方式" dictname="STD_ZB_ORG_TYPE" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    