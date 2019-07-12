<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_pop {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 230px;
}
</style>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtBatchLmt._toForm(form);
		LmtBatchLmtList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtBatchLmtPage() {
		var paramStr = LmtBatchLmtList._obj.getParamStr(['serno']);
		var approve_status = LmtBatchLmtList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status=="000" || approve_status=="992" || approve_status=="993" ){
				var url = '<emp:url action="getLmtBatchLmtUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("只有状态为'待发起','追回','打回'的申请才能被修改！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtBatchLmt() {
		var paramStr = LmtBatchLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtBatchLmtViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtBatchLmtPage() {
		var url = '<emp:url action="getLmtBatchLmtAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtBatchLmt() {
		var paramStr = LmtBatchLmtList._obj.getParamStr(['serno','batch_cus_no']);
		var approve_status = LmtBatchLmtList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status == "000"){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtBatchLmtRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
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
	
	function doReset(){
		page.dataGroups.LmtBatchLmtGroup.reset();
	};
	//提交流程
	function doSubLmtBatchLmt(){
	//   var url ='<emp:url action="handleFlow4Batch.do"/>'
		var approve_status = LmtBatchLmtList._obj.getParamValue(['approve_status']);//流程审批状态
		var paramStr = LmtBatchLmtList._obj.getParamValue(['serno']);
		var lmt_totl_amt = LmtBatchLmtList._obj.getParamValue(['lmt_totl_amt']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("LmtBatchLmt");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("374");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.amt._setValue(lmt_totl_amt);
			WfiJoin.prd_name._setValue("批量额度授信申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}		
	
	function getOrgID(data)
	{
		LmtBatchLmt.manager_br_id._setValue(data.organno._getValue());
    	LmtBatchLmt.manager_br_id_displayname._setValue(data.organname._getValue());      
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtBatchLmtGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtBatchLmt.serno" label="业务编号  " />
			<emp:text id="LmtBatchLmt.batch_cus_no" label="批量客户编号" />
			<emp:pop id="LmtBatchLmt.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_field_text_pop"/>
			<emp:select id="LmtBatchLmt.approve_status" label="审批状态" dictname="WF_APP_STATUS" />
			<emp:datespace id="LmtBatchLmt.app_date" label="申请日期" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtBatchLmtPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtBatchLmtPage" label="修改" op="update"/>
		<emp:button id="deleteLmtBatchLmt" label="删除" op="remove"/>
		<emp:button id="viewLmtBatchLmt" label="查看" op="view"/>
		<emp:button id="subLmtBatchLmt" label="提交" op="submit"/>
	</div>

	<emp:table icollName="LmtBatchLmtList" pageMode="true" url="pageLmtBatchLmtQuery.do">
		<emp:text id="serno" label="业务编号  " />
		<emp:text id="batch_cus_no" label="批量客户编号"/>
		<emp:text id="app_cls" label="申请类别" dictname="STD_ZB_APP_CLS"/>
		<emp:text id="lmt_totl_amt" label="授信总额(元)" dataType="Currency"/>
		<emp:text id="term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE"/>
		<emp:text id="term" label="期限" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="flow_type" label="流程类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    