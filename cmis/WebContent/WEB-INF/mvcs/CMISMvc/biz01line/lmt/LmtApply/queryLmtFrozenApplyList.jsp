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
		LmtApply._toForm(form);
		LmtApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtApplyPage() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr == null) {
			alert('请先选择一条记录！');
			return;
		}
		var appStatus = LmtApplyList._obj.getSelectedData()[0].approve_status._getValue();
		var appType = LmtApplyList._obj.getSelectedData()[0].app_type._getValue();
		if(appStatus=='000'||appStatus=='992'||appStatus=='993'){
			var url = '<emp:url action="getLmtFrozenUpdateRecord.do"/>?'+paramStr+"&menuId=LmtAgrInfoFrozen&updflag=update&app_type="+appType;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert('非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！');
		}
	};
	/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  begin */	
	//风险拦截
	function interRisk(){
		var serno = LmtApplyList._obj.getSelectedData()[0].serno._getValue();
		var _applType="";
		var _modelId="LmtApply";
		var _pkVal=serno;
		var _preventIdLst="FFFA2769264E264E984576B2CAFFE9F6";//单一法人额度冻结风险拦截方案（非流程） 
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
	/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  end */
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var appType = LmtApplyList._obj.getSelectedData()[0].app_type._getValue();
			var url = '<emp:url action="getLmtFrozenUpdateRecord.do"/>?'+paramStr+"&menuId=LmtAgrInfoFrozen&updflag=query&app_type="+appType;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDeleteLmtApply(){
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var appStatus = LmtApplyList._obj.getSelectedData()[0].approve_status._getValue();
			if(appStatus=='000'){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtApplyRecord.do"/>?'+paramStr;
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
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert('只有状态为【待发起】的申请才可以进行删除！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	/*--user code begin--*/
	
	//提交流程
	function searchAmt4Wfi(Amt){
		var paramStr = LmtApplyList._obj.getParamValue(['serno']);
		var cus_id = LmtApplyList._obj.getSelectedData()[0].cus_id._getValue();
		var cus_id_displayname = LmtApplyList._obj.getSelectedData()[0].cus_id_displayname._getValue();
		var approve_status = LmtApplyList._obj.getParamValue(['approve_status']);
		WfiJoin.table_name._setValue("LmtApply");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(paramStr);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("049");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("授信额度冻结/解冻申请");//产品名称
		initWFSubmit(false);
	}

	function doSubmitLmtApply(){
		/**modified by lisj 2015-7-20 需求编号：【XD150123005】小微自助循环贷款改造 begin**/
		var approve_status = LmtApplyList._obj.getSelectedData()[0].approve_status._getValue();
		if(approve_status=='000'||approve_status=='992'||approve_status=='993'){
			/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  begin */
			//var app_type=LmtApplyList._obj.getSelectedData()[0].app_type._getValue();
			//if("03"==app_type&&!interRisk()){
			//	return false;
			//}
			/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  end */
			var paramStr = LmtApplyList._obj.getParamStr(['serno']);
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
				var app_type = LmtApplyList._obj.getParamValue(['app_type']);
				var url = '<emp:url action="searchAmt4wfi.do"/>?'+paramStr+"&app_type="+app_type;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}else {
				alert('请先选择一条记录！');
			}
		}else{
			alert('非【待发起】、【打回】、【追回】状态的记录不能进行提交操作！');
		}
		/**modified by lisj 2015-7-20 需求编号：【XD150123005】小微自助循环贷款改造 end**/
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtApplyGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtApply.serno" label="业务编号" />
			<emp:text id="LmtApply.cus_id" label="客户码" />
			<emp:select id="LmtApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getUpdateLmtApplyPage" label="修改" op="update"/>
		<emp:button id="deleteLmtApply" label="删除" op="remove"/>
		<emp:button id="viewLmtApply" label="查看" op="view"/>
		<emp:button id="submitLmtApply" label="提交" op="subm"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="true" url="pageLmtFrozenApplyQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="协议编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" /> 
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_id_displayname" label="责任人" cssTDClass="tdRight" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter" />
	</emp:table>
	
</body>
</html>
</emp:page>
    