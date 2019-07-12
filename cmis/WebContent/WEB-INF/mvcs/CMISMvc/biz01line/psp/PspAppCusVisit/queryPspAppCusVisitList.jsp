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
		PspAppCusVisit._toForm(form);
		PspAppCusVisitList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspAppCusVisitPage() {
		var paramStr = PspAppCusVisitList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = PspAppCusVisitList._obj.getParamValue(['approve_status']);
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getPspAppCusVisitUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspAppCusVisit() {
		var paramStr = PspAppCusVisitList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAppCusVisitViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspAppCusVisitPage() {
		var url = '<emp:url action="getPspAppCusVisitAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspAppCusVisit() {
		var paramStr = PspAppCusVisitList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = PspAppCusVisitList._obj.getParamValue(['approve_status']);
			if(approve_status == "000" ){
				if(confirm("是否确认要删除？")){
					deletePspAppCusVisit(paramStr);
				}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspAppCusVisitGroup.reset();
	};
	
	/*--user code begin--*/
	//删除记录
	function deletePspAppCusVisit(paramStr){
		var url = '<emp:url action="deletePspAppCusVisitRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("删除失败！");
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="success"){
					alert('删除成功！');
					window.location.reload();							
				}else{
					alert("删除失败！");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("删除失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//提交流程
	function doSubPspAppCusVisit(){
		var paramStr = PspAppCusVisitList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = PspAppCusVisitList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = PspAppCusVisitList._obj.getParamValue(['cus_id_displayname']);//客户名称
			var approve_status = PspAppCusVisitList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("PspAppCusVisit");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue("000");
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("700");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("客户走访");
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

	<emp:gridLayout id="PspAppCusVisitGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspAppCusVisit.cus_id" label="受访客户码" />
			<emp:text id="PspAppCusVisit.serno" label="业务编号" />
			<emp:select id="PspAppCusVisit.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspAppCusVisitPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspAppCusVisitPage" label="修改" op="update"/>
		<emp:button id="deletePspAppCusVisit" label="删除" op="remove"/>
		<emp:button id="viewPspAppCusVisit" label="查看" op="view"/>
		<emp:button id="subPspAppCusVisit" label="提交" op="sub"/>
	</div>

	<emp:table icollName="PspAppCusVisitList" pageMode="true" url="pagePspAppCusVisitQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="受访客户码" />
		<emp:text id="cus_id_displayname" label="受访客户名称" />
		<emp:text id="visit_time" label="访客时间" />
		<emp:text id="is_cret_need" label="是否存在信贷需求" dictname="STD_ZX_YES_NO" />
		<emp:text id="is_advice_sale" label="是否建议再次营销" dictname="STD_ZX_YES_NO" />
		<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    