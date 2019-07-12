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
		ArpCollDebtApp._toForm(form);
		ArpCollDebtAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDebtAppPage() {
		var paramStr = ArpCollDebtAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = ArpCollDebtAppList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status!='000' && approve_status!='992'&& approve_status!='993' ){
				alert("非待发起、退回、追回状态的申请无法修改");
				return;
			}
			var url = '<emp:url action="getArpCollDebtAppUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDebtApp() {
		var paramStr = ArpCollDebtAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDebtAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpCollDebtAppPage() {
		var url = '<emp:url action="getArpCollDebtAppAddPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDebtApp() {
		var paramStr = ArpCollDebtAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = ArpCollDebtAppList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status != '000'){		
				alert("非登记状态的记录不能进行删除操作！");
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已成功删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteArpCollDebtAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDebtAppGroup.reset();
	};
	function returnCus(data){
		ArpCollDebtApp.cus_id._setValue(data.cus_id._getValue());
		ArpCollDebtApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	function doSubmitArpCollDebtApp(){
		var paramStr = ArpCollDebtAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			guar_amt = ArpCollDebtAppList._obj.getParamValue(['guar_amt']); //押品金额合计
			if(guar_amt == null  || guar_amt == ''|| guar_amt == '0' ){
				alert("请先录入抵债物！");
				return false;
			}
			debt_in_amt = ArpCollDebtAppList._obj.getParamValue(['debt_in_amt']); //押品金额合计
			if(debt_in_amt == null  || debt_in_amt == ''|| debt_in_amt == '0' ){
				alert("请先录入业务信息！");
				return false;
			}
			
			var cus_id = ArpCollDebtAppList._obj.getParamValue(['cus_id']);
			var cus_id_displayname = ArpCollDebtAppList._obj.getParamValue(['cus_id_displayname']);
			var approve_status = ArpCollDebtAppList._obj.getParamValue(['approve_status']);
			
			WfiJoin.table_name._setValue("ArpCollDebtApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("022");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_id_displayname);
			WfiJoin.amt._setValue(debt_in_amt);
			WfiJoin.prd_name._setValue("以物抵债申请流程");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpCollDebtAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpCollDebtApp.serno" label="业务编号" />
			<emp:pop id="ArpCollDebtApp.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:select id="ArpCollDebtApp.debt_mode" label="抵债方式" dictname="STD_ZB_DEBT_MODEL" />
			<emp:select id="ArpCollDebtApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
			<emp:text id="ArpCollDebtApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpCollDebtAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpCollDebtAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpCollDebtApp" label="删除" op="remove"/>
		<emp:button id="viewArpCollDebtApp" label="查看" op="view"/>
		<emp:button id="submitArpCollDebtApp" label="提交" op="submit"/>
	</div>

	<emp:table icollName="ArpCollDebtAppList" pageMode="true" url="pageArpCollDebtAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="debt_mode" label="抵债方式" dictname="STD_ZB_DEBT_MODEL" />
		<emp:text id="debt_in_amt" label="抵入金额" dataType="Currency"/>
		<emp:text id="guar_amt" label="押品金额" dataType="Currency" hidden="true"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>