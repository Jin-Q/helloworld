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
		IqpCreditChangeApp._toForm(form);
		IqpCreditChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCreditChangeAppPage() {
		var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpCreditChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
			   var url = '<emp:url action="getIqpCreditChangeAppUpdatePage.do"/>?'+paramStr+'&op=update';
			   url = EMPTools.encodeURI(url);      
			   window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//flag 申请的查看页面、历史查看页面返回按钮标识
	function doViewIqpCreditChangeApp() {
		var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditChangeAppViewPage.do"/>?'+paramStr+'&op=view&flag=iqpCreditChangeApp';   
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCreditChangeAppPage() {
		var url = '<emp:url action="getIqpCreditChangeAppAddPage.do"/>?menuId=addCreditChange';
		url = EMPTools.encodeURI(url);
		window.location = url; 
	};
	
	function doDeleteIqpCreditChangeApp() {
		var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = IqpCreditChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
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
				var url = '<emp:url action="deleteIqpCreditChangeAppRecord.do"/>?'+paramStr;
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
		page.dataGroups.IqpCreditChangeAppGroup.reset();
	};
	function returnCus(data){
		IqpCreditChangeApp.cus_id._setValue(data.cus_id._getValue());
		IqpCreditChangeApp.cus_id_displayname._setValue(data.cus_name._getValue());
    };
//提交流程
    function doSubWfiFlow(){
    	var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
    	if (paramStr != null) {
    		var end_date = IqpCreditChangeAppList._obj.getSelectedData()[0].end_date._getValue();
            var openDay = '${context.OPENDAY}';
    		if(end_date<openDay){
               alert("该笔业务已过期,不能提交流程!");
               return false;
        	}
    		var serno = IqpCreditChangeAppList._obj.getSelectedData()[0].serno._getValue();
    		var cus_id = IqpCreditChangeAppList._obj.getSelectedData()[0].cus_id._getValue();
    		var cus_id_displayname = IqpCreditChangeAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
    		var approve_status = IqpCreditChangeAppList._obj.getSelectedData()[0].approve_status._getValue();
    		var new_apply_amt = IqpCreditChangeAppList._obj.getSelectedData()[0].new_apply_amt._getValue();
    		WfiJoin.table_name._setValue("IqpCreditChangeApp");
    		WfiJoin.pk_col._setValue("serno");
    		WfiJoin.pk_value._setValue(serno);
    		WfiJoin.wfi_status._setValue(approve_status);
    		WfiJoin.status_name._setValue("approve_status");
    		WfiJoin.appl_type._setValue("011");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：
    		WfiJoin.cus_id._setValue(cus_id);//客户码
    		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
    		WfiJoin.prd_name._setValue("信用证修改申请");//产品名称
    		WfiJoin.amt._setValue(new_apply_amt);
    		initWFSubmit(false);	 
    	} else {
			alert('请先选择一条记录！'); 
		}
    }    
	
	/*--user code begin--*/
	function doImageScan(){
		var data = IqpCreditChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doImageView(){
		var data = IqpCreditChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = IqpCreditChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpCreditChangeAppList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = IqpCreditChangeAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = IqpCreditChangeAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpCreditChangeAppList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="IqpCreditChangeAppGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpCreditChangeApp.serno" label="业务编号" />
			<emp:text id="IqpCreditChangeApp.bill_no" label="借据编号" />
			<emp:pop id="IqpCreditChangeApp.cus_id_displayname" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus"/>
			<emp:text id="IqpCreditChangeApp.cus_id" label="客户码" hidden="true"/>     
	</emp:gridLayout>   
	
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	
	<div align="left">
		<emp:button id="getAddIqpCreditChangeAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpCreditChangeAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpCreditChangeApp" label="删除" op="remove"/>
		<emp:button id="viewIqpCreditChangeApp" label="查看" op="view"/>
		<emp:button id="subWfiFlow" label="提交" op="submit"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpCreditChangeAppList" pageMode="true" url="pageIqpCreditChangeAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="fount_serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />		
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>     
		<emp:text id="cont_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="cont_amt" label="开证金额" dataType="Currency"/> 
		<emp:text id="new_apply_amt" label="修改后信用证金额" dataType="Currency"/>
		<emp:text id="apply_date" label="申请日期" /> 
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />    
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>  
		<emp:text id="end_date" label="效期" hidden="true"/>  
	</emp:table>
	
</body>
</html>
</emp:page>
    