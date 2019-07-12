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
		IqpCreditChangeApp._toForm(form);
		IqpCreditChangeAppList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpCreditChangeApp() {
		var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditChangeAppViewPage.do"/>?'+paramStr+'&op=view';   
			url = EMPTools.encodeURI(url);
			window.location = url;
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
	/*--user code begin--*/
	function doPrint(){
		var paramStr = IqpCreditChangeAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = IqpCreditChangeAppList._obj.getParamValue(['approve_status']);
			if(status=='997'){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_GYXD_XYZBG.raq?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert("只有申请状态为'通过'的记录才能进行【打印】操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	}		

	function doImageView(){
		var data = IqpCreditChangeAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
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
		<emp:button id="viewIqpCreditChangeApp" label="查看" op="view"/>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpCreditChangeAppList" pageMode="true" url="pageIqpCreditChangeHisQuery.do">
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
	</emp:table>
	
</body>
</html>
</emp:page>
    