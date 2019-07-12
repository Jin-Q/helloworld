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
		CusCognizApply._toForm(form);
		CusCognizApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusCognizApplyPage() {
		var paramStr = CusCognizApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusCognizApplyList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getCusCognizApplyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusCognizApply() {
		var paramStr = CusCognizApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusCognizApplyViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusCognizApplyPage() {
		var url = '<emp:url action="getCusCognizApplyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusCognizApply() {
		var paramStr = CusCognizApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusCognizApplyList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusCognizApplyRecord.do"/>?'+paramStr;
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusCognizApplyGroup.reset();
	};
	
	/*--user code begin--*/
	function doSumbitCusCognizApply(){
		var paramStr = CusCognizApplyList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = CusCognizApplyList._obj.getParamValue(['approve_status']);
			var cus_id = CusCognizApplyList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = CusCognizApplyList._obj.getParamValue(['cus_id_displayname']);//客户名称
			WfiJoin.table_name._setValue("CusCognizApply");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("005");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("客户认定申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	function returnCus(data){
		CusCognizApply.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusCognizApplyGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusCognizApply.serno" label="申请流水号" />
		<emp:pop id="CusCognizApply.cus_id" label="客户码" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
		<emp:select id="CusCognizApply.scale_type" label="认定类别" dictname="STD_ZB_CUS_COGNIZ_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusCognizApplyPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusCognizApplyPage" label="修改" op="update"/>
		<emp:button id="deleteCusCognizApply" label="删除" op="remove"/>
		<emp:button id="viewCusCognizApply" label="查看" op="view"/>
		<emp:button id="sumbitCusCognizApply" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="CusCognizApplyList" pageMode="true" url="pageCusCognizApplyQuery.do">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="scale_type" label="认定类别" dictname="STD_ZB_CUS_COGNIZ_TYPE" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    