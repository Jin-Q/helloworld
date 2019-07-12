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
		LmtRediApply._toForm(form);
		LmtRediApplyList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewLmtApply() {
		var paramStr = LmtRediApplyList._obj.getParamStr(['serno']);
		var type = '${context.type}';
		if (paramStr != null) {
			var lx = LmtRediApplyList._obj.getSelectedData()[0].lx._getValue();
			var url = "";
			if("COM" == lx || "com" == lx){
				url = '<emp:url action="getLmtRediApplyViewPage.do"/>?'+paramStr+"&menuId=lmtRediApp&op=view&isShow=Y&showButton=N&lx="+lx+"&type="+type;
			}else{
				url = '<emp:url action="getLmtRediApplyViewPage.do"/>?'+paramStr+"&menuId=IndivRediApply&op=view&isShow=Y&showButton=N&lx="+lx+"&type="+type;
			}
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	//异步删除
	function doDeleteLmtApply(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}else{
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = LmtRediApplyList._obj.getParamStr(['serno','lx']);
		if (paramStr != null) {
			var approve_status = LmtRediApplyList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("该操作不可恢复，是否确认要删除？")){
					var url = '<emp:url action="deleteLmtRediApplyRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
		
	};
	
	function doReset(){
		page.dataGroups.LmtRediApplyGroup.reset();
	};
	
	
	//提交流程
	//var url = '<emp:url action="submitLmtApply.do"/>?'+paramStr;
	function doSubmitLmtApply(){
		var paramStr = LmtRediApplyList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtRediApplyList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtRediApplyList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var crd_totl_amt = LmtRediApplyList._obj.getSelectedData()[0].crd_totl_amt._getValue();
			var lx = LmtRediApplyList._obj.getSelectedData()[0].lx._getValue();
			var approve_status = LmtRediApplyList._obj.getParamValue(['approve_status']);
			if("com"==lx || "COM"==lx){
				WfiJoin.table_name._setValue("LmtRediApply");
				WfiJoin.prd_name._setValue("对公授信复议申请");//产品名称
				WfiJoin.appl_type._setValue("0061");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			}else{
				WfiJoin.table_name._setValue("LmtAppIndivRedi");
				WfiJoin.prd_name._setValue("个人授信复议申请");//产品名称
				WfiJoin.appl_type._setValue("0062");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			}
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(crd_totl_amt);//金额
			
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function returnCus(data){
		LmtRediApply.cus_id._setValue(data.cus_id._getValue());
		LmtRediApply.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtRediApplyGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="LmtRediApply.serno" label="业务编号" />
		<emp:pop id="LmtRediApply.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:select id="LmtRediApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="LmtRediApply.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="deleteLmtApply" label="删除" op="remove"/>
		<emp:button id="viewLmtApply" label="查看" op="view"/>
		<emp:button id="submitLmtApply" label="提交" op="update"/>
	</div>

	<emp:table icollName="LmtRediApplyList" pageMode="true" url="pageLmtRediApplyQuery.do?type=${context.type}" >
		<emp:text id="lx" label="类型" hidden="true"/> 
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" /> 
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    