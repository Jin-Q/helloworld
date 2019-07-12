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
		LmtAppGrp._toForm(form);
		LmtAppGrpList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtGrpApplyPage() {
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppGrpList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"||approve_status == "991"||approve_status == "992"||approve_status == "993"){
				var url = '<emp:url action="getLmtGrpApplyUpdatePage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtGrpApply() {
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtGrpApplyViewPage.do"/>?op=view&'+paramStr+"&type=${context.type}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtGrpApplyPage() {
		var url = '<emp:url action="getLmtGrpLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	//异步删除
	function doDeleteLmtGrpApply(){
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno','app_type']);
		if (paramStr != null) {
			var approve_status = LmtAppGrpList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status != "000"){
				alert("只有状态为【待发起】的申请才可以进行删除！");
				return ;
			}
			if(confirm("该操作会关联删除集团下成员的授信申请，是否确认要删除？")){
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
				var url = '<emp:url action="deleteLmtGrpApplyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppGrpGroup.reset();
	};
	
	/*--user code begin--*/
	//提交流程
	//var url = '<emp:url action="submitLmtAppGrp.do"/>?'+paramStr;
	function doSubmitLmtAppGrp(){
		var paramStr = LmtAppGrpList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppGrpList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status != "000"&&approve_status != "991"&&approve_status != "992"&&approve_status != "993"){
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行提交！");
				return ;
			}
			var grp_no = LmtAppGrpList._obj.getSelectedData()[0].grp_no._getValue();
			var grp_no_displayname = LmtAppGrpList._obj.getSelectedData()[0].grp_no_displayname._getValue();
			var crd_totl_amt = LmtAppGrpList._obj.getSelectedData()[0].crd_totl_amt._getValue();
			var approve_status = LmtAppGrpList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppGrp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3221");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(grp_no);//客户码
			WfiJoin.cus_name._setValue(grp_no_displayname);//客户名称
			WfiJoin.amt._setValue(crd_totl_amt);//金额
			WfiJoin.prd_name._setValue("集团客户授信申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function doUpload(){
		var paramStr = LmtAppGrpList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=04&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppGrpGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppGrp.grp_no" label="集团编号" />
			<emp:text id="LmtAppGrp.serno" label="业务编号" />
			<!-- add by lisj 2014年11月27日--集团查询环节， 支持输入客户名称可模糊查询-->
			<emp:text id="LmtAppGrp.grp_name" label="集团名称" />
			<emp:text id="LmtAppGrp.grp_member_name" label="集团成员名称" />
			<emp:select id="LmtAppGrp.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
			<emp:select id="LmtAppGrp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtGrpApplyPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtGrpApplyPage" label="修改" op="update"/>
		<emp:button id="deleteLmtGrpApply" label="删除" op="remove"/>
		<emp:button id="viewLmtGrpApply" label="查看" op="view"/>
		<emp:button id="submitLmtAppGrp" label="提交" op="update"/>
		<emp:button id="upload" label="附件"/>
	</div>
	<emp:table icollName="LmtAppGrpList"  url="pageLmtGrpApplyQuery.do?type=${context.type}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />		
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>