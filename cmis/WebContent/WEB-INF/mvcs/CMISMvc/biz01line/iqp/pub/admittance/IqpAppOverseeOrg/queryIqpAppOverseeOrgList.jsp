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
		IqpAppOverseeOrg._toForm(form);
		IqpAppOverseeOrgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppOverseeOrgPage() {
		var paramStr = IqpAppOverseeOrgList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppOverseeOrgUpdatePage.do"/>?'+paramStr+'&op=update';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppOverseeOrg() {
		var paramStr = IqpAppOverseeOrgList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppOverseeOrgViewPage.do"/>?'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppOverseeOrgPage() {
		var url = '<emp:url action="next2addIqpAppOverseeOrgPage.do"/>&op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppOverseeOrg(){
		var paramStr = IqpAppOverseeOrgList._obj.getParamStr(['serno']);
		if (paramStr != null){
			if(confirm("是否确认要删除？")){
				var approve_status = IqpAppOverseeOrgList._obj.getSelectedData()[0].approve_status._getValue();
				if(approve_status =="000"){
					var handleSuccess = function(o){
						if(o.responseText !== undefined){
							try{
								var jsonstr = eval("("+o.responseText+")");
							}catch(e){
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("删除成功！");
								window.location.reload();
							}else {
								alert("删除失败！");
							}
						}
					};
					var handleFailure = function(o){
						alert("异步请求出错！");	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					};
					var url = '<emp:url action="deleteIqpAppOverseeOrgRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				}else{
					alert("只有状态为'待发起'状态的才能被删除");
					return false;
				}
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppOverseeOrgGroup.reset();
	};
	
	/*--user code begin--*/
	function doSubIqpAppOverseeOrg(){
		var paramStr = IqpAppOverseeOrgList._obj.getParamValue(['serno']);
		var approve_status = IqpAppOverseeOrgList._obj.getParamValue(['approve_status']);
		var cus_id = IqpAppOverseeOrgList._obj.getParamValue(['oversee_org_id']);
		var cus_name = IqpAppOverseeOrgList._obj.getParamValue(['oversee_org_id_displayname']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("IqpAppOverseeOrg");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("512");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("监管机构准入申请");//产品名称
			initWFSubmit(false);
		}else{
			alert('请先选择一条记录！');
		}
	}

	function returnCus(data){
		IqpAppOverseeOrg.oversee_org_id._setValue(data.cus_id._getValue());
		IqpAppOverseeOrg.oversee_org_id_displayname._setValue(data.cus_name._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAppOverseeOrgGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpAppOverseeOrg.serno" label="业务流水号" />
		<emp:pop id="IqpAppOverseeOrg.oversee_org_id_displayname" label="监管机构名称" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus"/>
		<emp:select id="IqpAppOverseeOrg.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="IqpAppOverseeOrg.oversee_org_id" label="监管机构编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAppOverseeOrgPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAppOverseeOrgPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAppOverseeOrg" label="删除" op="remove"/>
		<emp:button id="viewIqpAppOverseeOrg" label="查看" op="view"/>
		<emp:button id="subIqpAppOverseeOrg" label="提交" op="update"/>
	</div>

	<emp:table icollName="IqpAppOverseeOrgList" pageMode="true" url="pageIqpAppOverseeOrgQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="oversee_org_id" label="监管机构编号" />
		<emp:text id="oversee_org_id_displayname" label="监管机构名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    