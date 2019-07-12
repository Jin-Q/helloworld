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
		IqpRpddscnt._toForm(form);
		IqpRpddscntList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpRpddscntPage() {
		var paramStr = IqpRpddscntList._obj.getParamStr(['serno','batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpRpddscntUpdatePage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpRpddscnt() {
		var paramStr = IqpRpddscntList._obj.getParamStr(['serno','batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpRpddscntViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpRpddscntPage() {
		var url = '<emp:url action="getIqpRpddscntAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpRpddscnt() {
		var paramStr = IqpRpddscntList._obj.getParamStr(['serno','batch_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpRpddscntRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpRpddscntGroup.reset();
	};
	
	//-------------对手行行号pop框选择返回函数-----------
    function getOrgNo(data){
    	IqpRpddscnt.toorg_no._setValue(data.same_org_no._getValue());
    	IqpRpddscnt.toorg_name._setValue(data.same_org_cnname._getValue());
    };	

	function doImageView(){
		var data = IqpRpddscntList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpRpddscntList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = IqpRpddscntList._obj.getParamValue(['toorg_no']);	//客户码
		data['prd_id'] = IqpRpddscntList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpRpddscntList._obj.getParamValue(['serno']);
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
	<emp:gridLayout id="IqpRpddscntGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpRpddscnt.serno" label="业务编号" />
			<emp:pop id="IqpRpddscnt.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="IqpRpddscnt.bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
			<emp:select id="IqpRpddscnt.rpddscnt_type" label="转贴现方式" dictname="STD_ZB_RPDDSCNT_MODE"/>
			<emp:select id="IqpRpddscnt.approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpRpddscnt.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpRpddscnt" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpRpddscntList" pageMode="true" url="pageIqpRpddscntHistoryQuery.do">
		<emp:text id="serno" label="业务编号" hidden="false"/>
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id" label="产品编码" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
		<emp:text id="rpddscnt_type" label="转贴现方式" dictname="STD_ZB_BUSI_TYPE"/>
		<emp:text id="rpddscnt_rate" label="转贴现利率" dataType="Rate"/>
		<emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		<emp:text id="rpddscnt_date" label="转贴现日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="batch_no" label="批次号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    