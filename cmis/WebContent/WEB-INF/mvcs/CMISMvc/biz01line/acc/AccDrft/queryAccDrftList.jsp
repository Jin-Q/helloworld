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
		AccDrft._toForm(form);
		AccDrftList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewAccDrft() {       
		var paramStr = AccDrftList._obj.getParamStr(['bill_no','prd_id']);  
		if (paramStr != null) {
			var url = '<emp:url action="getAccDrftViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url; 
		} else {
			alert('请先选择一条记录！');
		}
	};
 	
	function doReset(){
		page.dataGroups.AccDrftGroup.reset();
	};
	
	/*--user code begin--*/
	function doImageView(){
		var data = AccDrftList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccDrftList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = AccDrftList._obj.getParamValue(['discount_per']);	//客户码
		data['prd_id'] = AccDrftList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/** 导出excel **/
	function doExcelSDuty(){
		var form = document.getElementById("queryForm");
		AccDrftList._toForm(form);
		form.submit();
	}
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = AccDrftList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=08&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" >
	<emp:form  method="POST" action="accDrftGroupExpBatchToExcel.do?porder_no=${context.porder_no}" id="queryForm">
	<emp:gridLayout id="AccDrftGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccDrft.cont_no" label="合同编号" />
			<emp:text id="AccDrft.bill_no" label="借据编号" />
			<emp:select id="AccDrft.dscnt_type" label="贴现方式" dictname="STD_ZB_RPDDSCNT_MODE"/>
			<emp:date id="AccDrft.dscnt_date" label="贴现日期" />
	</emp:gridLayout>
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="viewAccDrft" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="AccDrftList" pageMode="true" url="pageAccDrftQuery.do?porder_no=${context.porder_no}">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="discount_per" label="贴现人/交易对手" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品名称" hidden="true"/>
		<emp:text id="dscnt_type" label="贴现方式" dictname="STD_ZB_RPDDSCNT_MODE"/>
		<emp:text id="cur_type" label="交易币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="rpay_amt" label="实付金额" dataType="Currency"/>
		<emp:text id="porder_no" label="汇票号码" /> 
		<emp:text id="dscnt_date" label="贴现日期" />
		<emp:text id="accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    