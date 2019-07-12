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
		AccAssetstrsf._toForm(form);
		AccAssetstrsfList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccAssetstrsfPage() {
		var paramStr = AccAssetstrsfList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAssetstrsfUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccAssetstrsf() {
		var paramStr = AccAssetstrsfList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAssetstrsfViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccAssetstrsfPage() {
		var url = '<emp:url action="getAccAssetstrsfAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccAssetstrsf() {
		var paramStr = AccAssetstrsfList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteAccAssetstrsfRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccAssetstrsfGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		AccAssetstrsf.cus_id._setValue(data.cus_id._getValue());
	};

    function getOrgNo(data){
    	AccAssetstrsf.toorg_no._setValue(data.same_org_no._getValue());
    	AccAssetstrsf.toorg_name._setValue(data.same_org_cnname._getValue());
    };	

    function doImageView(){
		var data = AccAssetstrsfList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccAssetstrsfList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = AccAssetstrsfList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = AccAssetstrsfList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/** 导出excel **/                                      
	function doExcelSDuty(){                             
		var form = document.getElementById("queryForm");   
		AccAssetstrsf._toForm(form);                          
		form.submit();                                     
	}                                                    
	
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = AccAssetstrsfList._obj.getParamValue(['fount_serno']);
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
<body class="page_content">
	<emp:form  method="POST" action="accAssetstrsfExpBatchToExcel.do" id="queryForm">
	<emp:gridLayout id="AccAssetstrsfGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="AccAssetstrsf.bill_no" label="借据编号" />
	        <emp:text id="AccAssetstrsf.cont_no" label="合同编号" />
			<emp:pop id="AccAssetstrsf.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="AccAssetstrsf.acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>	
			<emp:text id="AccAssetstrsf.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout> 
	<jsp:include page="/queryInclude.jsp" flush="true" />
	</emp:form>
	
	<div align="left">
		<emp:button id="viewAccAssetstrsf" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="upload" label="附件"/> 
	</div>

	<emp:table icollName="AccAssetstrsfList" pageMode="true" url="pageAccAssetstrsfQuery.do">
		<emp:text id="fount_serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="cus_name" label="借款人名称" />	
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" hidden="true"/>
		<emp:text id="takeover_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE"/>
		<emp:text id="takeover_date" label="转让日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    