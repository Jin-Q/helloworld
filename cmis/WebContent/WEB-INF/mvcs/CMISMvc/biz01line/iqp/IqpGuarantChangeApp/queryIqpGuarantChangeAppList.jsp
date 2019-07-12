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
		IqpGuarantChangeApp._toForm(form);
		IqpGuarantChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpGuarantChangeAppPage() {
		var paramStr = IqpGuarantChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpGuarantChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
			   var url = '<emp:url action="getIqpGuarantChangeAppUpdatePage.do"/>?'+paramStr+'&op=update';
			   url = EMPTools.encodeURI(url);    
			   window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//flag 申请查看页面 、历史查看页面返回标识参数
	function doViewIqpGuarantChangeApp() {
		var paramStr = IqpGuarantChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGuarantChangeAppViewPage.do"/>?'+paramStr+'&op=view&flag=iqpGuarantChangeApp'; 
			url = EMPTools.encodeURI(url); 
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpGuarantChangeAppPage() {
		var url = '<emp:url action="getIqpGuarantChangeAppAddPage.do"/>?menuId=addGuarantChange';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpGuarantChangeApp() {
		var paramStr = IqpGuarantChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpGuarantChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
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
							window.location.reload(); 
						}else {
							alert("异步请求出错！");
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
				var url = '<emp:url action="deleteIqpGuarantChangeAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
			} 
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行删除操作！");
			}
		} else {
			alert('请先选择一条记录！'); 
		}
	};
	
	function doReset(){
		page.dataGroups.IqpGuarantChangeAppGroup.reset();
	};
	function returnCus(data){
		IqpGuarantChangeApp.cus_id._setValue(data.cus_id._getValue());
		IqpGuarantChangeApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };

    function doSubWfiFlow(){
    	var paramStr = IqpGuarantChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var end_date = IqpGuarantChangeAppList._obj.getSelectedData()[0].end_date._getValue();
            var openDay = '${context.OPENDAY}';
    		if(end_date<openDay){
               alert("该笔业务已过期,不能提交流程!");
               return false;
        	}
			var serno = IqpGuarantChangeAppList._obj.getSelectedData()[0].serno._getValue();
			var cus_id = IqpGuarantChangeAppList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = IqpGuarantChangeAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var approve_status = IqpGuarantChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			var new_cont_amt = IqpGuarantChangeAppList._obj.getSelectedData()[0].new_cont_amt._getValue();
			WfiJoin.table_name._setValue("IqpGuarantChangeApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(serno);
			WfiJoin.wfi_status._setValue(approve_status);   
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("013");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：wfi_credit_change_app
			WfiJoin.cus_id._setValue(cus_id);//客户码  
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.prd_name._setValue("保函修改申请");//产品名称
			WfiJoin.amt._setValue(new_cont_amt);
			initWFSubmit(false); 
		} else {
			alert('请先选择一条记录！'); 
		} 
};
	
	/*--user code begin--*/
	function doImageScan(){
		var data = IqpGuarantChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doImageView(){
		var data = IqpGuarantChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = IqpGuarantChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		/**modified by lisj 2014年11月14日 更改获取serno字段 begin**/
		data['serno'] = IqpGuarantChangeAppList._obj.getParamValue(['serno']);	//业务编号
		/**modified by lisj 2014年11月14日 更改获取serno字段 begin**/
		data['cus_id'] = IqpGuarantChangeAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = IqpGuarantChangeAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpGuarantChangeAppList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="IqpGuarantChangeAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpGuarantChangeApp.serno" label="业务编号" />
			<emp:pop id="IqpGuarantChangeApp.cus_id_displayname" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus"/>
			<emp:text id="IqpGuarantChangeApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpGuarantChangeAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpGuarantChangeAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpGuarantChangeApp" label="删除" op="remove"/>
		<emp:button id="viewIqpGuarantChangeApp" label="查看" op="view"/>
		<emp:button id="subWfiFlow" label="提交" op="submit"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpGuarantChangeAppList" pageMode="true" url="pageIqpGuarantChangeAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="fount_serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="保函金额" dataType="Currency" />
		<emp:text id="new_cont_amt" label="修改后保函金额" dataType="Currency" />
		<emp:text id="apply_date" label="申请日期" /> 
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="end_date" label="保函到期日" />
	</emp:table>
	
</body>
</html>
</emp:page>
    