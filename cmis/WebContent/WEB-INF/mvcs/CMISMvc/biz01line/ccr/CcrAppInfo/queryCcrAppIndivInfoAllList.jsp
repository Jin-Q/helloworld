<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CcrAppInfo._toForm(form);
		CcrAppInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCcrAppInfoPage() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			//判断审批状态，如果审批中的，不允许修改或删除
			var data = CcrAppInfoList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status =="111"){
				alert("审批中的评级申请无法修改");
				return;
			}
			var url = '<emp:url action="getCcrAppInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCcrAppInfo() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrAppInfoViewPage.do"/>?'+paramStr+'&restrict_tab=Y';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetAddCcrAppInfoPage() {
		var lmt_serno = '';
		var url = '<emp:url action="getCcrAppIndivInfoAllAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCcrAppInfo() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id','approve_status']);
		if (paramStr != null) {
			//判断审批状态，如果审批中的，不允许修改或删除
			var data = CcrAppInfoList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			
			if(approve_status == '000' ){			
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCcrAppInfoRecord.do"/>?'+paramStr;
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
			}else{
				alert('该申请不是待发起状态，不能删除!');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CcrAppInfoGroup.reset();
	};
	//提交按钮操作
	function doSubmitCcrAppInfo(){
		var manager_id_displayname =CcrAppInfoList._obj.getParamValue(['manager_id_displayname']);
		if(manager_id_displayname==''){
			alert("评级申请信息没有完成，不能进行提交操作！");
			return;
		}
		var paramStr = CcrAppInfoList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = CcrAppInfoList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = CcrAppInfoList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var approve_status = CcrAppInfoList._obj.getParamValue(['approve_status']);
			
			WfiJoin.table_name._setValue("CcrAppInfo");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("680");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：ccr_app_info
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_id_displayname);
			WfiJoin.prd_name._setValue("信用评级-个人信用评级");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
	function returnCus(data){
		CcrAppInfo.cus_id._setValue(data.cus_id._getValue());
		CcrAppInfo.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    function doImageView(){
		var data = CcrAppInfoList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CcrAppInfoList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = CcrAppInfoList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CcrAppInfoList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=03&serno='+paramStr;
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
	<emp:gridLayout id="CcrAppInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CcrAppInfo.serno" label="业务编号" />
			<emp:pop id="CcrAppInfo.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:date id="CcrAppInfo.app_begin_date" label="申请日期" />
			<emp:select id="CcrAppInfo.approve_status" label="审批状态" dictname="WF_APP_STATUS"  />
			<emp:text id="CcrAppInfo.manager_id" label="客户经理编号" hidden="true"/>
			<emp:text id="CcrAppInfo.cus_id" label="客户码" hidden="true"/>
			<emp:pop id="CcrAppInfo.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCcrAppInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateCcrAppInfoPage" label="修改" op="update"/>
		<emp:button id="deleteCcrAppInfo" label="删除" op="remove"/>
		<emp:button id="viewCcrAppInfo" label="查看" op="view"/>
		<emp:button id="submitCcrAppInfo" label="提交" op="submit"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="CcrAppInfoList" pageMode="true" url="pageCcrAppInfoIndivQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_type" label="客户类型"  dictname="STD_ZB_CUS_TYPE" hidden="true"/>
		<emp:text id="app_begin_date" label="申请日期" /> 
		<emp:text id="expiring_date" label="到期日期" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="manager_id_displayname" label="客户经理" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>		
		<emp:text id="flag" label="申请类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    