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
	
	function doReset(){
		page.dataGroups.LmtAppGrpGroup.reset();
	};
	
	/*--user code begin--*/
	//提交流程
	function doSubmitLmtApply(){
		var paramStr = LmtAppGrpList._obj.getParamValue(['serno']);
		var approve_status = LmtAppGrpList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if("998"==approve_status){
				WfiJoin.table_name._setValue("LmtAppGrp");
				WfiJoin.pk_col._setValue("serno");
				WfiJoin.pk_value._setValue(paramStr);
				WfiJoin.wfi_status._setValue(approve_status);
				WfiJoin.status_name._setValue("approve_status");
				WfiJoin.appl_type._setValue("3221");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
				initWFSubmit(false);
			}else{
				alert('只有申请状态为【否决】的业务才能发起复议！');
			}
		}else {
			alert('请先选择一条记录！');
		}
	}
	//复议
	function doSubmitRedi(){
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
					var url = '<emp:url action="getLmtGrpAppRediPage.do"/>?'+paramStr+"&menuId=grp_crd_query&op=view&isShow=N&showButton=Y";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno']);
		var approve_status = LmtAppGrpList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if("998"==approve_status){
				var url = '<emp:url action="searchRediApply.do"/>?'+paramStr+"&type=grp&menuId=LmtGrpAppRedi";
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}else{
				alert('只有审批状态为【否决】的业务才能发起复议！');
			}
		}else {
			alert('请先选择一条记录！');
		}
	}
	 /**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin**/
	function doPrintAP(){
		var paramStr = LmtAppGrpList._obj.getParamStr(['serno']);
		if(paramStr != null) {
			var approve_status  = LmtAppGrpList._obj.getParamValue(['approve_status']);
			if(approve_status == "997"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=lmt/rcapprovalopinion.raq&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert('仅有审批状态为【通过】的授信审批才允许打印！');
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end**/
	
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
			<!-- add by lisj 2015-3-24 增加通过集团成员查询集团客户的功能-->
			<emp:text id="LmtAppGrp.grp_member_name" label="集团成员名称" />
			<emp:select id="LmtAppGrp.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
			<emp:select id="LmtAppGrp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtGrpApply" label="查看" op="view"/>
		<emp:button id="submitRedi" label="复议" op="redi"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin -->
		<emp:button id="printAP" label="审批意见打印" op="printAP"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end -->
	</div>

	<emp:table icollName="LmtAppGrpList" url="pageLmtGrpApplyQuery.do?type=${context.type}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />		
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true" />
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
    