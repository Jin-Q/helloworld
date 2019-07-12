<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CtrLimitApp._toForm(form);
		CtrLimitAppList._obj.ajaxQuery(null,form);
	};

	
	function doGetUpdateCtrLimitAppPage() {
		var paramStr = CtrLimitAppList._obj.getParamStr(['serno']);
		var appType = CtrLimitAppList._obj.getParamValue(['app_type']);
		var appStatus = CtrLimitAppList._obj.getParamValue(['approve_status']);
		
		if (paramStr != null) {
			if(appStatus == '997'){
				alert("改申请状态禁止修改操作！");
				return;
			}
			var url;
			if(appType == 01){
				url = '<emp:url action="getCtrLimitAppUpdatePage.do"/>?op=update&'+paramStr;
			}else {
				url = '<emp:url action="updateCtrLimitContChangePage.do"/>?&op=update&'+paramStr;
			}
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrLimitApp() {
		var paramStr = CtrLimitAppList._obj.getParamStr(['serno']);
		var appType = CtrLimitAppList._obj.getParamValue(['app_type']);
		var appStatus = CtrLimitAppList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			var url;
			if(appType == 01){
				url = '<emp:url action="getCtrLimitAppViewPage.do"/>?op=view&'+paramStr+'&approve_status='+appStatus;
			}else {
				url = '<emp:url action="getCtrLimitContChangeViewPage.do"/>?&op=view&'+paramStr+'&approve_status='+appStatus;
				
			} 
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrLimitAppPage() {
		var url = '<emp:url action="getCtrLimitAppAddPage.do"/>?&op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCtrLimitApp() {
		var paramStr = CtrLimitAppList._obj.getParamStr(['serno','app_type']);
		var appStatus = CtrLimitAppList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(appStatus == '997'){
				alert("改申请状态禁止删除操作！");
				return;
			}
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
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除出错！");
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
				var url = '<emp:url action="deleteCtrLimitAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrLimitAppGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		CtrLimitApp.cus_id._setValue(data.cus_id._getValue());
		CtrLimitApp.cus_name._setValue(data.cus_name._getValue());
	};
	function doImageView(){
		var data = CtrLimitAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLimitAppList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = CtrLimitAppList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrLimitAppList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=05&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CtrLimitAppGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CtrLimitApp.serno" label="业务编号" />
			<emp:pop id="CtrLimitApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=main_br_id='${context.organNo}'&returnMethod=returnCus" />
			<emp:select id="CtrLimitApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
			<emp:text id="CtrLimitApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCtrLimitAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateCtrLimitAppPage" label="修改" op="update"/>
		<emp:button id="deleteCtrLimitApp" label="删除" op="remove"/>
		<emp:button id="viewCtrLimitApp" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>


	<emp:table icollName="CtrLimitAppList" pageMode="true" url="pageCtrLimitAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="app_amt" label="申请金额" dataType="Currency"/>		
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    