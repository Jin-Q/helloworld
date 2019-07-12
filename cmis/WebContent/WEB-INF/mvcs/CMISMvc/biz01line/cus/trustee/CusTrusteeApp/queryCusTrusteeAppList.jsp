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
		CusTrusteeApp._toForm(form);
		CusTrusteeAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusTrusteeAppPage() {
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusTrusteeApp() {
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeAppViewPage.do"/>?process=${context.process}&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusTrusteeAppPage() {
		var url = '<emp:url action="getCusTrusteeAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doReset(){
		page.dataGroups.CusTrusteeAppGroup.reset();
	};
	/*--user code begin--*/
	function doDeleteCusTrusteeApp() {
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
		var _status = CusTrusteeAppList._obj.getParamValue(['approve_status']);
		if(_status != '000'){
			alert('申请状态不是【待发起】,不能删除!');
			return;
		}
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							alert("删除成功!");
							window.location.reload();
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="deleteCusTrusteeAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2014年11月11日  增加提交流程方法，通过提供书面授权书标志判断业务是否提交流程  begin**/
	function doSubmitWF(){
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
		if (paramStr != null){
			var is_provid_accredit = CusTrusteeAppList._obj.getSelectedData()[0].is_provid_accredit._getValue();
		if(is_provid_accredit == '1'){
			doSubm();
		}else{
			var serno = CusTrusteeAppList._obj.getSelectedData()[0].serno._getValue();
			var cus_id = CusTrusteeAppList._obj.getSelectedData()[0].consignor_id._getValue();
			var cus_name = CusTrusteeAppList._obj.getSelectedData()[0].consignor_id_displayname._getValue();
			var approve_status = CusTrusteeAppList._obj.getSelectedData()[0].approve_status._getValue();
			var consignor_type = CusTrusteeAppList._obj.getSelectedData()[0].consignor_type._getValue();
			WfiJoin.table_name._setValue("CusTrusteeApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(serno);
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_pk._setValue("");
			WfiJoin.prd_name._setValue("托管申请");
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("900");
			initWFSubmit(false);
			}
		}else{
			alert('请先选择一条记录！');
			}
	};
	/**add by lisj 2014年11月11日  增加提交流程方法，通过提供书面授权书标志判断业务是否提交流程  end**/
	function doSubm(){
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
        var _status = CusTrusteeAppList._obj.getParamValue(['approve_status']);
		if(_status != '000' && _status != '993' && _status != '992'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
		}
		if (paramStr != null) {
			if(confirm("是否确认要提交？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							alert("提交成功!");
							doQuery();
						}else{
							alert(flag);
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="submCusTrusteeAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doBack(){
		var paramStr = CusTrusteeAppList._obj.getParamStr(['serno']);
		var sele = CusTrusteeAppList._obj.getSelectedData()[0].approve_status._getValue();
		if(sele != '997'){
			alert('不是通过的不能回收!');
			return ;
		}
		if (paramStr != null) {
			if(confirm("是否确认要回收？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							alert("收回成功!");
						}else{
							alert("已回收");
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="backCusTrusteeAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function setConsignorId(data){
		CusTrusteeApp.consignor_id._setValue(data.actorno._getValue());
		CusTrusteeApp.consignor_id_displayname._setValue(data.actorname._getValue());
	}
	function setTrusteeId(data){
		CusTrusteeApp.trustee_id._setValue(data.actorno._getValue());
		CusTrusteeApp.trustee_id_displayname._setValue(data.actorname._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusTrusteeAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusTrusteeApp.serno" label="业务流水号" />
			<emp:text id="CusTrusteeApp.consignor_id" label="委托人编号" hidden="true"/>
			<emp:pop id="CusTrusteeApp.consignor_id_displayname" label="委托人名称" url="getAllSUserPopListOp.do" returnMethod="setConsignorId"/>
			<emp:pop id="CusTrusteeApp.trustee_id_displayname" label="托管人名称" url="getAllSUserPopListOp.do" returnMethod="setTrusteeId"/>
			<emp:text id="CusTrusteeApp.consignor_id" label="委托人编号" hidden="true"/>
			<emp:text id="CusTrusteeApp.trustee_id" label="托管人编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusTrusteeAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusTrusteeAppPage" label="修改" op="update"/>
		<emp:button id="deleteCusTrusteeApp" label="删除" op="remove"/>
		<emp:button id="viewCusTrusteeApp" label="查看" op="view"/>
		<emp:button id="submitWF" label="提交" op="subm"/>
		
	</div>

	<emp:table icollName="CusTrusteeAppList" pageMode="true" url="pageCusTrusteeAppQuery.do?process=${context.process}">
		<emp:text id="serno" label="业务编号" />
		
		<emp:text id="consignor_id" label="委托人" hidden="true"/>
		<emp:text id="consignor_br_id" label="委托机构" hidden="true"/>
		<emp:text id="trustee_id" label="托管人" hidden="true"/>
		<emp:text id="trustee_br_id" label="托管机构" hidden="true"/>
		
		<emp:text id="consignor_id_displayname" label="委托人"/>
		<emp:text id="consignor_br_id_displayname" label="委托机构" />
		<emp:text id="trustee_id_displayname" label="托管人" />
		<emp:text id="trustee_br_id_displayname" label="托管机构" />
		
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
		<emp:text id="is_provid_accredit" label="是否提供书面授权书"  dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="consignor_type" label="委托类别" dictname="STD_CUS_CONSIG_TYPE" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    