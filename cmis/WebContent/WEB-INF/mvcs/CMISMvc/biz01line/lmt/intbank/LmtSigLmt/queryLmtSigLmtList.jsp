<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doGetAddLmtSigLmtPage(){
		var url = '<emp:url action="getLmtSigLmtAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtSigLmt(){
		var paramStr = LmtSigLmtList._obj.getParamStr(['serno']);
		var approve_status = LmtSigLmtList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if (approve_status == "000"){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtSigLmtRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					//window.location = url;
					var handleSuccess = function(o){
						EMPTools.unmask();
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("删除失败!");
								return;
							}
							var flag=jsonstr.flag;	
							var flagInfo=jsonstr.flagInfo;						
							if(flag=="success"){
								alert('删除成功！');
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
			}else{
				alert("只有状态为'待发起'的数据才能删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateLmtSigLmtPage(){
		var paramStr = LmtSigLmtList._obj.getParamStr(['serno','approve_status']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSigLmtUpdatePage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtSigLmt(){
		var paramStr = LmtSigLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryLmtSigLmtDetail.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtSigLmt._toForm(form);
		LmtSigLmtList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtSigLmtGroup.reset();
	};
	
	/*--user code begin--*/
	//提交流程
	//   var url = '<emp:url action="handleFlow.do"/>'
	function doSubm(){
		var paramStr = LmtSigLmtList._obj.getParamValue(['serno']);
		var approve_status = LmtSigLmtList._obj.getParamValue(['approve_status']);
		var cus_id = LmtSigLmtList._obj.getParamValue(['cus_id']);
		var cus_name = LmtSigLmtList._obj.getParamValue(['cus_id_displayname']);
		var lmt_amt = LmtSigLmtList._obj.getParamValue(['lmt_amt']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("LmtSigLmt");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("371");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("单笔额度授信申请");
			WfiJoin.amt._setValue(lmt_amt);
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function returnCusId(data){
		LmtSigLmt.cus_id._setValue(data.cus_id._getValue());
		LmtSigLmt.cus_id_displayname._setValue(data.same_org_cnname._getValue());
	}
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtSigLmtGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtSigLmt.serno" label="业务编号" />
			<emp:text id="LmtSigLmt.cus_id" label="客户码" />
			<emp:pop id="LmtSigLmt.cus_id_displayname" label="客户名称" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:datespace id="LmtSigLmt.app_date" label="申请日期" />
			<emp:select id="LmtSigLmt.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddLmtSigLmtPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtSigLmtPage" label="修改" op="update"/>
		<emp:button id="deleteLmtSigLmt" label="删除" op="remove"/>
		<emp:button id="viewLmtSigLmt" label="查看" op="view"/>
		<emp:button id="subm" label="提交" op="submit"/>
	</div>
	<emp:table icollName="LmtSigLmtList" pageMode="true" url="pageLmtSigLmtQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="同业机构(行)名称"/>
		<emp:text id="app_cls" label="申请类别" dictname="STD_ZB_APP_CLS"/>
		<emp:text id="lmt_amt" label="授信金额(元)" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>		
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="flow_type" label="流程类型" hidden="true"/> 
	</emp:table>
</body>
</html>
</emp:page>