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
		ArpCollDispApp._toForm(form);
		ArpCollDispAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDispAppPage() {
		var paramStr = ArpCollDispAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = ArpCollDispAppList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status!='000' && approve_status!='992'&& approve_status!='993' ){
				alert("非待发起、退回、追回状态的申请无法修改");
				return;
			}
			var url = '<emp:url action="getArpCollDispAppUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDispApp() {
		var paramStr = ArpCollDispAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDispAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpCollDispAppPage() {
		var url = '<emp:url action="getArpCollDispAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDispApp() {
		var paramStr = ArpCollDispAppList._obj.getParamStr(['serno','asset_disp_mode']);
		if (paramStr != null) {
			var data = ArpCollDispAppList._obj.getSelectedData();
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
				var url = '<emp:url action="deleteArpCollDispAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDispAppGroup.reset();
	};
	function doSubmitArpCollDispApp(){
		var paramStr = ArpCollDispAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var approve_status = ArpCollDispAppList._obj.getParamValue(['approve_status']);
			disp_amt = ArpCollDispAppList._obj.getParamValue(['disp_amt']);
			WfiJoin.table_name._setValue("ArpCollDispApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("023");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：ccr_app_info
			WfiJoin.amt._setValue(disp_amt);
			WfiJoin.prd_name._setValue("以物抵债-抵债资产处置申请");
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

	<emp:gridLayout id="ArpCollDispAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpCollDispApp.serno" label="业务编号" />
			<emp:text id="ArpCollDispApp.guaranty_no" label="抵债资产编号" />
			<emp:select id="ArpCollDispApp.asset_disp_mode" label="资产处置方式" dictname="STD_ASSET_DISP_MODEL" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpCollDispAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpCollDispAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpCollDispApp" label="删除" op="remove"/>
		<emp:button id="viewArpCollDispApp" label="查看" op="view"/>
		<emp:button id="submitArpCollDispApp" label="提交" op="submit"/>
	</div>

	<emp:table icollName="ArpCollDispAppList" pageMode="true" url="pageArpCollDispAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="asset_disp_mode" label="资产处置方式" dictname="STD_ASSET_DISP_MODEL" />
		<emp:text id="disp_amt" label="处置金额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    