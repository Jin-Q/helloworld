<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAssetRegiApp._toForm(form);
		IqpAssetRegiAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetRegiAppPage() {
		var paramStr = IqpAssetRegiAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetRegiAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status=='000'||approve_status=='992'){
				var url = '<emp:url action="getIqpAssetRegiAppUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert('只有状态为【待发起】或者【打回】的申请才可以进行修改！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetRegiApp() {
		var paramStr = IqpAssetRegiAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetRegiAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetRegiAppPage() {
		var url = '<emp:url action="getIqpAssetRegiAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetRegiApp() {
		var paramStr = IqpAssetRegiAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpAssetRegiAppList._obj.getSelectedData()[0].approve_status._getValue();
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
								var url = '<emp:url action="queryIqpAssetRegiAppList.do"/>?serno='+'<%=serno%>';
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
					var url = '<emp:url action="deleteIqpAssetRegiAppRecord.do"/>?'+paramStr;
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
		page.dataGroups.IqpAssetRegiAppGroup.reset();
	};
	
	//流程提交
    function doSubmitWF(apply_type){
    	var paramStr = IqpAssetRegiAppList._obj.getParamStr(['serno']);
 		if (paramStr != null) {
 			var approve_status = IqpAssetRegiAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status=='000'||approve_status=='992'){
				var serno = IqpAssetRegiAppList._obj.getSelectedData()[0].serno._getValue();
	 			var cus_id = IqpAssetRegiAppList._obj.getSelectedData()[0].cus_id._getValue();
	 			var cus_name = IqpAssetRegiAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
	 			var approve_status = IqpAssetRegiAppList._obj.getSelectedData()[0].approve_status._getValue();
	 			WfiJoin.table_name._setValue("IqpAssetRegiApp");
	 			WfiJoin.pk_col._setValue("serno");
	 			WfiJoin.pk_value._setValue(serno);
	 			WfiJoin.cus_id._setValue(cus_id);
	 			WfiJoin.cus_name._setValue(cus_name);
	 			WfiJoin.prd_name._setValue("信贷资产登记审批");
	 			WfiJoin.wfi_status._setValue(approve_status);
	 			WfiJoin.status_name._setValue("approve_status");
	 			WfiJoin.appl_type._setValue("092");
	 			initWFSubmit(false);
			}else{
				alert("只有状态为【待发起】或者【打回】的申请才可以进行提交！");
			}
 		}else{
 			alert('请先选择一条记录！');
 		}
    };

    function returnCus(data){
    	IqpAssetRegiApp.cus_id._setValue(data.cus_id._getValue());
	   IqpAssetRegiApp.cus_name._setValue(data.cus_name._getValue());
    };
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="IqpAssetRegiAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetRegiApp.bill_no" label="借据编号" />
			<emp:pop id="IqpAssetRegiApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:text id="IqpAssetRegiApp.cont_no" label="合同编号" />
			<emp:select id="IqpAssetRegiApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpAssetRegiApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetRegiAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetRegiAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetRegiApp" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetRegiApp" label="查看" op="view"/>
		<emp:button id="submitWF" label="提交" op="sub"/>
	</div>

	<emp:table icollName="IqpAssetRegiAppList" pageMode="true" url="pageIqpAssetRegiAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="regi_type" label="业务类别" dictname="STD_ZB_REGI_TYPE"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户编号" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    