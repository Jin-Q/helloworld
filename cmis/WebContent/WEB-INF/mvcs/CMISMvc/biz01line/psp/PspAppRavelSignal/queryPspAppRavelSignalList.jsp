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
		PspAppRavelSignal._toForm(form);
		PspAppRavelSignalList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspAppRavelSignalPage() {
		var paramStr = PspAppRavelSignalList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = PspAppRavelSignalList._obj.getParamValue(['approve_status']);
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getPspAppRavelSignalUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspAppRavelSignal() {
		var paramStr = PspAppRavelSignalList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAppRavelSignalViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspAppRavelSignalPage() {
		var url = '<emp:url action="getPspAppRavelSignalAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspAppRavelSignal() {
		var paramStr = PspAppRavelSignalList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = PspAppRavelSignalList._obj.getParamValue(['approve_status']);
			if(approve_status != "000"){
				alert("只有状态为【待发起】的申请才可以进行删除！");
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspAppRavelSignalRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;
						if(flag=="success"){
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspAppRavelSignalGroup.reset();
	};
	
	/*--user code begin--*/
	//提交流程
	function doSubmitApp(){
		var paramStr = PspAppRavelSignalList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var signal_type = PspAppRavelSignalList._obj.getParamValue(['signal_type']);
			var cus_id = PspAppRavelSignalList._obj.getParamValue(['cus_id']);
			var cus_id_displayname = PspAppRavelSignalList._obj.getParamValue(['cus_id_displayname']);
			var approve_status = PspAppRavelSignalList._obj.getParamValue(['approve_status']);
			var app_type;
			if(signal_type=='01'){//重大预警
				app_type = '061';
			}else{
				app_type = '060';
			}
			WfiJoin.table_name._setValue("PspAppRavelSignal");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);	
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue(app_type);  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue('');//金额
			WfiJoin.prd_name._setValue("预警信号解除申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspAppRavelSignalGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspAppRavelSignal.serno" label="业务编号" />
			<emp:text id="PspAppRavelSignal.cus_id" label="客户编码" />
			<emp:select id="PspAppRavelSignal.signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspAppRavelSignalPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspAppRavelSignalPage" label="修改" op="update"/>
		<emp:button id="deletePspAppRavelSignal" label="删除" op="remove"/>
		<emp:button id="viewPspAppRavelSignal" label="查看" op="view"/>
		<emp:button id="submitApp" label="提交" op="submit"/>
	</div>

	<emp:table icollName="PspAppRavelSignalList" pageMode="true" url="pagePspAppRavelSignalQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    