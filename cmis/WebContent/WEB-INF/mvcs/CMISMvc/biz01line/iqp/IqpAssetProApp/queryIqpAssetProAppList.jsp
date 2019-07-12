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
		IqpAssetProApp._toForm(form);
		IqpAssetProAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetProAppPage() {
		var paramStr = IqpAssetProAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetProAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status=='000'){
				var url = '<emp:url action="getIqpAssetProAppUpdatePage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetProApp() {
		var paramStr = IqpAssetProAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetProAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetProAppPage() {
		var url = '<emp:url action="getIqpAssetProAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetProApp() {
		var paramStr = IqpAssetProAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetProAppList._obj.getSelectedData()[0].approve_status._getValue();
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
								var url = '<emp:url action="queryIqpAssetProAppList.do"/>';
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
					var url = '<emp:url action="deleteIqpAssetProAppRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetProAppGroup.reset();
	};

	//-----------通过产品编号查询产品配置中使用流程类型----------
    function doSubWF(){
    	var prdId = IqpAssetProAppList._obj.getSelectedData()[0].prd_id._getValue();
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
    	var serno = IqpAssetProAppList._obj.getSelectedData()[0].serno._getValue();
    	var approve_status = IqpAssetProAppList._obj.getSelectedData()[0].approve_status._getValue();
    	var prd_id = IqpAssetProAppList._obj.getSelectedData()[0].prd_id._getValue();
    	var prd_name = IqpAssetProAppList._obj.getSelectedData()[0].prd_name._getValue();
    	var pro_amt = IqpAssetProAppList._obj.getSelectedData()[0].pro_amt._getValue();
    	WfiJoin.table_name._setValue("IqpAssetProApp");
    	WfiJoin.pk_col._setValue("serno");
    	WfiJoin.pk_value._setValue(serno);
    	WfiJoin.cus_id._setValue('');
    	WfiJoin.cus_name._setValue('');
    	WfiJoin.prd_pk._setValue(prd_id);
    	WfiJoin.prd_name._setValue(prd_name);
    	WfiJoin.amt._setValue(pro_amt);
    	WfiJoin.wfi_status._setValue(approve_status);
    	WfiJoin.status_name._setValue("approve_status");
    	WfiJoin.appl_type._setValue(apply_type);
    	initWFSubmit(false);
    };

    function doCheckAssetInfo(){
    	var paramStr = IqpAssetProAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno = IqpAssetProAppList._obj.getParamValue(['serno']);
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

	       	var url="<emp:url action='checkAssetInfoForAssetPro.do'/>?serno="+serno;
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

	<emp:gridLayout id="IqpAssetProAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetProApp.serno" label="业务编号" />
			<emp:select id="IqpAssetProApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetProAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetProAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetProApp" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetProApp" label="查看" op="view"/>
		<emp:button id="checkAssetInfo" label="提交" op="submit"/>
	</div>

	<emp:table icollName="IqpAssetProAppList" pageMode="true" url="pageIqpAssetProAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="pro_name" label="项目名称" />
		<emp:text id="pro_type" label="项目类型" dictname="STD_ZB_ASSET_PRO_TYPE"/>
		<emp:text id="pro_amt" label="项目金额" dataType="Currency"/>
		<emp:text id="pro_qnt" label="笔数" dataType="Int"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="prd_id" label="产品编码" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="prd_name" label="产品名称" defvalue="资产证券化" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    