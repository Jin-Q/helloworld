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
		CusOrgApp._toForm(form);
		CusOrgAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusOrgAppPage() {
		var paramStr = CusOrgAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusOrgAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getCusOrgAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusOrgApp() {
		var paramStr = CusOrgAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgAppViewPage.do"/>?ops=view&hidebt=0&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusOrgAppPage() {
		var url = '<emp:url action="getCusOrgAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusOrgApp() {
		var paramStr = CusOrgAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusOrgAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusOrgAppRecord.do"/>?'+paramStr;
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
		page.dataGroups.CusOrgAppGroup.reset();
	};
	
	/*--user code begin--*/
	function doSumbitCusCognizApply(){
		var paramStr = CusOrgAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = CusOrgAppList._obj.getParamValue(['approve_status']);
			var cus_id = CusOrgAppList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = CusOrgAppList._obj.getParamValue(['cus_name']);//客户名称
			WfiJoin.table_name._setValue("CusOrgApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("007");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("评估机构认定申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	function returnCus(data){
		CusOrgApp.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusOrgAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusOrgApp.serno" label="申请流水号" />
			<emp:pop id="CusOrgApp.cus_id" label="评估机构客户码"  
			buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:select id="CusOrgApp.approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusOrgAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusOrgAppPage" label="修改" op="update"/>
		<emp:button id="deleteCusOrgApp" label="删除" op="remove"/>
		<emp:button id="viewCusOrgApp" label="查看" op="view"/>
		<emp:button id="sumbitCusCognizApply" label="提交" op="startFlow"/>
	</div>
			
	<emp:table icollName="CusOrgAppList"  pageMode="true" url="pageCusOrgAppQuery.do?resourceType=${context.resourceType}">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="cus_id" label="评估机构客户码" />
		<emp:text id="cus_name" label="评估机构名称" />
		<emp:text id="extr_eval_quali" label="资质等级" dictname="STD_ZB_EXTR_EVAL_QUALI"/>
		<emp:text id="extr_eval_rng" label="评估范围" />
		<emp:text id="extr_eval_exp_type" label="有效期类型" dictname="STD_ZB_TERM_TYPE"/>
		<emp:text id="extr_eval_exp_term" label="有效期期限"  />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    