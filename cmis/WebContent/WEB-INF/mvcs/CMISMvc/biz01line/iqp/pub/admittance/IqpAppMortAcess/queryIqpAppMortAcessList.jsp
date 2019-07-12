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
		IqpAppMortAcess._toForm(form);
		IqpAppMortAcessList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppMortAcessPage() {
		var paramStr = IqpAppMortAcessList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = IqpAppMortAcessList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status!='000' && approve_status!='992'&& approve_status!='993' ){
				alert("非待发起、退回、追回状态的申请无法修改");
				return;
			}
			var url = '<emp:url action="getIqpAppMortAcessUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppMortAcess() {
		var paramStr = IqpAppMortAcessList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMortAcessViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppMortAcessPage() {
		var url = '<emp:url action="getIqpAppMortAcessAddPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppMortAcess() {
		var paramStr = IqpAppMortAcessList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var appSta = IqpAppMortAcessList._obj.getParamValue(['approve_status']);
			if(appSta != '000'){
				alert('非待发起状态记录，不能删除!');
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					EMPTools.unmask();
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("删除成功！");
							window.location.reload();
						}else{
							alert("删除失败，失败原因："+jsonstr.msg);
						}
					}
				};
				var handleFailure = function(o) {
					alert("删除失败！");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteIqpAppMortAcessRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppMortAcessGroup.reset();
	};
	
	function doSubmitIqpAppMortAcess(){
		var paramStr = IqpAppMortAcessList._obj.getParamValue(['serno']);
		var approve_status = IqpAppMortAcessList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("IqpAppMortAcess");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("514");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.prd_name._setValue("押品目录准入申请");//产品名称
			initWFSubmit(false);
		}else{
			alert('请先选择一条记录！');
		}

	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAppMortAcessGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAppMortAcess.serno" label="业务流水号" />
			<emp:select id="IqpAppMortAcess.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAppMortAcessPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAppMortAcessPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAppMortAcess" label="删除" op="remove"/>
		<emp:button id="viewIqpAppMortAcess" label="查看" op="view"/>
		<emp:button id="submitIqpAppMortAcess" label="提交" op="submit"/>
	</div>

	<emp:table icollName="IqpAppMortAcessList" pageMode="true" url="pageIqpAppMortAcessQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="app_type" label="申请类型 " dictname="STD_ZB_APP_ADMIT_TYPE" />
		<emp:text id="acsee_date" label="准入日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    