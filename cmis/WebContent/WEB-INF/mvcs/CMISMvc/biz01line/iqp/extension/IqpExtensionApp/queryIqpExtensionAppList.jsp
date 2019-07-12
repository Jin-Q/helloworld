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
		IqpExtensionApp._toForm(form);
		IqpExtensionAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExtensionAppPage() {
		var paramStr = IqpExtensionAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status=IqpExtensionAppList._obj.getParamValue('approve_status');
	       	if(status != '000'&&status != '992'&&status != '991'&&status != '993') {
	           alert("该申请所处状态不是【待发起】、【追回】、【打回】不能修改！");
	           return false;
	       }
			var url = '<emp:url action="getIqpExtensionAppUpdatePage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExtensionApp() {
		var paramStr = IqpExtensionAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExtensionAppViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExtensionAppPage() {
		var url = '<emp:url action="getIqpExtensionAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
		
	function doReset(){
		page.dataGroups.IqpExtensionAppGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		IqpExtensionApp.cus_id._setValue(data.cus_id._getValue());
		IqpExtensionApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    function doDeleteIqpExtensionApp() {
		var paramStr = IqpExtensionAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = IqpExtensionAppList._obj.getParamValue('approve_status');
			if(status != '000' ){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpExtensionAppRecord.do"/>?'+paramStr;
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
	function doSubmitIqpExtensionApp(){
		var paramStr = IqpExtensionAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = IqpExtensionAppList._obj.getParamValue(['approve_status']);
			cus_id = IqpExtensionAppList._obj.getParamValue(['cus_id']);//客户码
			cus_name = IqpExtensionAppList._obj.getParamValue(['cus_id_displayname']);//客户名称
			amt = IqpExtensionAppList._obj.getParamValue(['extension_amt']);//展期金额
			WfiJoin.table_name._setValue("IqpExtensionApp");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("016");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("展期业务申请流程");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	function doHisIqpExtensionApp() {
		var paramStr = IqpExtensionAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExtensionAppViewHis.do"/>?'+paramStr+"&op=view&restrictUsed=false";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doImageScan(){
		var data = IqpExtensionAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = IqpExtensionAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = IqpExtensionAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpExtensionAppList._obj.getParamValue(['serno']);	//业务编号  展期取新流水号 
		data['cus_id'] = IqpExtensionAppList._obj.getParamValue(['cus_id']);	//客户码
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' begin**/
		data['prd_id'] = 'zqyw';	//业务品种
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' end**/
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpExtensionAppList._obj.getParamValue(['serno']);
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
	<form  method="POST" action="#" id="queryForm" style="width: 1500">

	<emp:gridLayout id="IqpExtensionAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpExtensionApp.serno" label="业务编号" />
			<emp:text id="IqpExtensionApp.bill_no" label="原借据编号" />
			<emp:pop id="IqpExtensionApp.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
			<emp:select id="IqpExtensionApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" /> 
			<emp:text id="IqpExtensionApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpExtensionAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExtensionAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExtensionApp" label="删除" op="remove"/>
		<emp:button id="viewIqpExtensionApp" label="查看" op="view"/>
		<emp:button id="hisIqpExtensionApp" label="查看" op="his"/>
		<emp:button id="submitIqpExtensionApp" label="提交" op="startFlow"/>.
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpExtensionAppList" pageMode="true" url="pageIqpExtensionAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="fount_serno" label="原业务编号" hidden="true"/>
		<emp:text id="bill_no" label="原借据编号" />
		<emp:text id="cont_no" label="原合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_name" label="产品名称"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="extension_amt" label="展期金额" dataType="Currency"/>		
		<emp:text id="extension_rate" label="展期利率(年)" dataType="Rate"/>
		<emp:text id="extension_date" label="展期到期日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	</form>
</body>
</html>
</emp:page>