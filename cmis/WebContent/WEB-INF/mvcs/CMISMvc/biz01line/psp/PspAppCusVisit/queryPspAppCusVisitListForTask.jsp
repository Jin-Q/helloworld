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
			var url = '<emp:url action="getPspAppCusVisitUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
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
			if(confirm("是否确认要删除？")){
			/*	var url = '<emp:url action="deletePspAppCusVisitRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;*/
				deletePspAppCusVisit(paramStr);
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
		var _status = PspAppCusVisitList._obj.getParamValue(['approve_status']);
        if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
		}
		var paramStr = PspAppCusVisitList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = PspAppCusVisitList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = PspAppCusVisitList._obj.getParamValue(['cus_id_displayname']);//客户名称
			WfiJoin.table_name._setValue("PspAppCusVisit");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue("000");
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
			<emp:text id="PspAppCusVisit.cus_id" label="受访客户码" readonly="true" />
			<emp:text id="PspAppCusVisit.serno" label="业务编号" />
			<emp:select id="PspAppCusVisit.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewPspAppCusVisit" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspAppCusVisitList" pageMode="true" url="pagePspAppCusVisitQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="受访客户码" />
		<emp:text id="cus_id_displayname" label="受访客户名称" />
		<emp:text id="visit_time" label="访客时间" />
		<emp:text id="is_cret_need" label="是否存在信贷需求" dictname="STD_ZX_YES_NO" />
		<emp:text id="is_advice_sale" label="是否建议再次营销" dictname="STD_ZX_YES_NO" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    