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
		IqpAssetstrsf._toForm(form);
		IqpAssetstrsfList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetstrsfPage() {
		var paramStr = IqpAssetstrsfList._obj.getParamStr(['serno','asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetstrsfUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetstrsf() {
		var paramStr = IqpAssetstrsfList._obj.getParamStr(['serno','asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetstrsfViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetstrsfPage() {
		var url = '<emp:url action="getIqpAssetstrsfAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetstrsf() {
		var paramStr = IqpAssetstrsfList._obj.getParamStr(['serno','asset_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpAssetstrsfRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetstrsfGroup.reset();
	};
	
	/*--user code begin--*/
    function getOrgNo(data){
    	IqpAssetstrsf.toorg_no._setValue(data.bank_no._getValue());
    	IqpAssetstrsf.toorg_name._setValue(data.bank_name._getValue());
    };

	function doImageView(){
		var data = IqpAssetstrsfList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpAssetstrsfList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = IqpAssetstrsfList._obj.getParamValue(['toorg_no']);	//客户码
		data['prd_id'] = IqpAssetstrsfList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpAssetstrsfList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="IqpAssetstrsfGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpAssetstrsf.serno" label="业务编号" />
			<emp:pop id="IqpAssetstrsf.toorg_name" label="交易对手行名" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="IqpAssetstrsf.approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpAssetstrsf.toorg_no" label="对手行行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpAssetstrsf" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpAssetstrsfList" pageMode="true" url="pageIqpAssetstrsfQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_no" label="资产包编号" hidden="true"/>		
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id" label="产品名称" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE" />
		<emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		<emp:text id="takeover_total_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="takeover_date" label="转让日期" />
		<emp:text id="takeover_int" label="转让利息" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="prd_id" label="产品编号"  hidden="true" />
	</emp:table>
	
</body>
</html>
</emp:page>
    