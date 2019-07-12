<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<style type="text/css">
	.tdCenter{
		text-align:center;
	}
</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppIndiv._toForm(form);
		LmtAppIndivList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppIndivPage() {
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		if (paramStr == null) {
			alert('请先选择一条记录！');
			return;
		}
		var appType = LmtAppIndivList._obj.getSelectedData()[0].app_type._getValue();
		var appStatus = LmtAppIndivList._obj.getSelectedData()[0].approve_status._getValue();
		if(appStatus == "000" || appStatus == "992" || appStatus == "993"){
			var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?'+paramStr+"&updflag=update&app_type="+appType+"&lst=list";
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
		}
	};
	
	function doViewLmtAppIndiv() {
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var appType = LmtAppIndivList._obj.getSelectedData()[0].app_type._getValue();
			var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?'+paramStr+"&updflag=query&app_type="+appType+"&lst=list";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//删除申请信息
	function doDeleteLmtAppIndiv(){		
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var appStatus = LmtAppIndivList._obj.getSelectedData()[0].approve_status._getValue();
			if(appStatus=='000'){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtAppIndivRecord.do"/>?'+paramStr;
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
							if(flag=="success"){
								alert("删除成功!");
								window.location.reload();
						   }else {
							 alert(flag);
							 return;
						   }
						}
					};
					var handleFailure = function(o){	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}; 
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
		page.dataGroups.LmtAppIndivGroup.reset();
	};
	
	/*--user code begin--*/
	
	//提交流程
	function doSubmitLmtAppIndiv(){
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					var Amt = jsonstr.Amt;
					if(flag == "success"){
						searchAmt4Wfi(Amt);
				   	}else{
				   		alert("异步获取失败！");
					}
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var app_type = LmtAppIndivList._obj.getParamValue(['app_type']);
			var url = '<emp:url action="searchAmt4wfi.do"/>?'+paramStr+"&app_type="+app_type;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}else {
			alert('请先选择一条记录！');
		}
	}
	
	
	function searchAmt4Wfi(Amt){
		var paramStr = LmtAppIndivList._obj.getParamValue(['serno']);
		var cus_id = LmtAppIndivList._obj.getSelectedData()[0].cus_id._getValue();
		var cus_id_displayname = LmtAppIndivList._obj.getSelectedData()[0].cus_id_displayname._getValue();
		var approve_status = LmtAppIndivList._obj.getParamValue(['approve_status']);
		WfiJoin.table_name._setValue("LmtAppIndiv");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(paramStr);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("050");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("授信额度冻结/解冻申请");//产品名称
		initWFSubmit(false);
	}

	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppIndivGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtAppIndiv.serno" label="业务编号" />
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" />
			<emp:select id="LmtAppIndiv.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getUpdateLmtAppIndivPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppIndiv" label="删除" op="remove"/>
		<emp:button id="viewLmtAppIndiv" label="查看" op="view"/>
		<emp:button id="submitLmtAppIndiv" label="提交" op="subm"/>
	</div>

	<emp:table icollName="LmtAppIndivList" pageMode="true" url="pageLmtFrozenAppIndivQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="self_amt" label="自助金额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter" />
	</emp:table>
	
</body>
</html>
</emp:page>
    