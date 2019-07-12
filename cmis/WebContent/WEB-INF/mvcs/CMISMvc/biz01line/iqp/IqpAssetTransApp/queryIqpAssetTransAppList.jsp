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
		IqpAssetTransApp._toForm(form);
		IqpAssetTransAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetTransAppPage() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetTransAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status=='000'){
				var url = '<emp:url action="getIqpAssetTransAppUpdatePage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert('非待发起状态不能修改！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetTransApp() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTransAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetTransAppPage() {
		var url = '<emp:url action="getIqpAssetTransAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetTransApp() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetTransAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status=='000'){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("删除成功!");
								var url = '<emp:url action="queryIqpAssetTransAppList.do"/>';
								url = EMPTools.encodeURI(url);
								window.location = url;
							}else {
								alert("删除异常!");
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
					var url = '<emp:url action="deleteIqpAssetTransAppRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
				}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetTransAppGroup.reset();
	};
	
	function returnCusId(data){
		IqpAssetTransApp.toorg_no._setValue(data.same_org_no._getValue());
		IqpAssetTransApp.toorg_name._setValue(data.same_org_cnname._getValue());
	}	

	//-----------通过产品编号查询产品配置中使用流程类型----------
    function doSubWF(){
    	var prdId = IqpAssetTransAppList._obj.getSelectedData()[0].prd_id._getValue();
    	var url = '<emp:url action="getIqpApplyTypeByPrdId.do"/>?prdid='+prdId;
    	url = EMPTools.encodeURI(url);
    	var handleSuccess = function(o){
    		if(o.responseText !== undefined) {
    			try {
    				var jsonstr = eval("("+o.responseText+")");
    			} catch(e) {
    				alert("Parse jsonstr1 define error!" + e.message);
    				return;
    			}
    			var flag = jsonstr.flag;
    			var msg = jsonstr.msg;
    			var apply_type = jsonstr.apply_type;
    			if(flag == "success"){
    				doSubmitWF(apply_type);
    			}else {
    				alert(msg);
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
    	//var postData = YAHOO.util.Connect.setForm(form);	
    	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    };
	
	//-----------提交流程----------
    function doSubmitWF(apply_type){
    	var serno = IqpAssetTransAppList._obj.getSelectedData()[0].serno._getValue();
    	var cus_id = IqpAssetTransAppList._obj.getSelectedData()[0].toorg_no._getValue();
    	var cus_name = IqpAssetTransAppList._obj.getSelectedData()[0].toorg_no_displayname._getValue();
    	var approve_status = IqpAssetTransAppList._obj.getSelectedData()[0].approve_status._getValue();
    	var prd_id = IqpAssetTransAppList._obj.getSelectedData()[0].prd_id._getValue();
    	var prd_name = IqpAssetTransAppList._obj.getSelectedData()[0].prd_name._getValue();
    	var trans_amt = IqpAssetTransAppList._obj.getSelectedData()[0].trans_amt._getValue();
    	WfiJoin.table_name._setValue("IqpAssetTransApp");
    	WfiJoin.pk_col._setValue("serno");
    	WfiJoin.pk_value._setValue(serno);
    	WfiJoin.cus_id._setValue(cus_id);
    	WfiJoin.cus_name._setValue(cus_name);
    	WfiJoin.prd_pk._setValue(prd_id);
    	WfiJoin.prd_name._setValue(prd_name);
    	WfiJoin.amt._setValue(trans_amt);
    	WfiJoin.wfi_status._setValue(approve_status);
    	WfiJoin.status_name._setValue("approve_status");
    	WfiJoin.appl_type._setValue(apply_type);
    	initWFSubmit(false);
    };

    function doCheckAssetInfo(){
    	var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno = IqpAssetTransAppList._obj.getParamValue(['serno']);
	       	var handleSuccess = function(o){
	       		if(o.responseText !== undefined) {
	       			try {
	       				var jsonstr = eval("("+o.responseText+")");
	       			} catch(e) {
	       				alert("Parse jsonstr1 define error!" + e.message);
	       				return;
	       			}
	       			var flag = jsonstr.flag;
	       			var msg = jsonstr.msg;
	       			if(flag == "success"){
	       				doSubWF();
	       			}else {
	       				alert(msg);
	       				return;
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

	       	var url="<emp:url action='checkAssetInfoForAssetTrans.do'/>?serno="+serno;
	       	url = EMPTools.encodeURI(url);
	       	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}else {
			alert('请先选择一条记录！');
		}
       }
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetTransAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetTransApp.serno" label="业务编号" />
			<emp:pop id="IqpAssetTransApp.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:select id="IqpAssetTransApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpAssetTransApp.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetTransAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetTransAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetTransApp" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetTransApp" label="查看" op="view"/>
		<emp:button id="checkAssetInfo" label="提交" op="submit"/>
	</div>

	<emp:table icollName="IqpAssetTransAppList" pageMode="true" url="pageIqpAssetTransAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_no_displayname" label="交易对手行名" />
		<emp:text id="trans_type" label="业务类型" dictname="STD_ZB_TRANS_TYPE"/>
		<emp:text id="trans_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="trans_date" label="转让日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="prd_id" label="产品编码" hidden="true"/>
		<emp:text id="prd_name" label="产品名称" defvalue="资产流转" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    