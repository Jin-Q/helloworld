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
		LmtAppGrpRedi._toForm(form);
		LmtAppGrpRediList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAppGrpRedi() {
		var paramStr = LmtAppGrpRediList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtGrpAppRediPage.do"/>?'+paramStr+"&menuId=LmtAppGrpRedi&op=view&isShow=Y&showButton=N";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppGrpRediGroup.reset();
	};
	//异步删除
	function doDeleteLmtAppGrpRedi(){
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

		var paramStr = LmtAppGrpRediList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppGrpRediList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("该操作不可恢复，是否确认要删除？")){
					var url = '<emp:url action="deleteLmtRediApplyRecord.do"/>?'+paramStr+'&type=grp';
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
	/*--user code begin--*/
	//提交流程
	function doSubmitLmtAppGrpRedi(){
		var paramStr = LmtAppGrpRediList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtAppGrpRediList._obj.getSelectedData()[0].grp_no._getValue();
			var cus_id_displayname = LmtAppGrpRediList._obj.getSelectedData()[0].grp_no_displayname._getValue();
			var crd_totl_amt = LmtAppGrpRediList._obj.getSelectedData()[0].crd_totl_amt._getValue();
			var approve_status = LmtAppGrpRediList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppGrpRedi");
			WfiJoin.prd_name._setValue("集团客户授信复议申请");//产品名称
			WfiJoin.appl_type._setValue("3221");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppGrpRediGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppGrpRedi.grp_no" label="集团编号" />
			<emp:text id="LmtAppGrpRedi.serno" label="业务编号" />
			<emp:select id="LmtAppGrpRedi.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
			<emp:select id="LmtAppGrpRedi.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="deleteLmtAppGrpRedi" label="删除" op="remove"/>
		<emp:button id="viewLmtAppGrpRedi" label="查看" op="view"/>
		<emp:button id="submitLmtAppGrpRedi" label="提交" op="update"/>
	</div>

	<emp:table icollName="LmtAppGrpRediList" url="pageLmtAppGrpRediQuery.do">
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
    