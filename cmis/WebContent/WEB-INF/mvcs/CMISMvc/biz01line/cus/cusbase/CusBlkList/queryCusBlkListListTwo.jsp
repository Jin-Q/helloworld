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
		CusBlkList._toForm(form);
		CusBlkListList._obj.ajaxQuery(null,form);
	};
	
	function doViewCusBlkList() {
		var paramStr = CusBlkListList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBlkListViewPage.do"/>?'+paramStr+'&type=other&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusBlkListGroup.reset();
	};

	/*--user code begin--*/
	//责任机构
	function getOrgID(data){
		CusBlkList.manager_br_id._setValue(data.organno._getValue());
		CusBlkList.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function getOrgID1(data){
		CusBlkList.input_br_id._setValue(data.organno._getValue());
		CusBlkList.input_br_id_displayname._setValue(data.organname._getValue());
	};


	/*导出不宜贷款户信息*/
	 function doPrintTable(){
		var form = document.getElementById("queryForm");
		CusBlkList._toForm(form);
		form.submit();
	} 
	/*影像查看*/
	function doImageView(){
		var data = CusBlkListList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CusBlkListList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = CusBlkListList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form  method="POST" action="cusBlkListExpBatchToExcel.do" id="queryForm">
		<emp:gridLayout id="CusBlkListGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CusBlkList.cus_name" label="客户名称" />
			<emp:select id="CusBlkList.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="CusBlkList.cert_code" label="证件号码" />
			<emp:pop id="CusBlkList.manager_br_id_displayname" label="管理机构" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="CusBlkList.manager_br_id" label="管理机构" hidden="true"/>
			<emp:pop id="CusBlkList.input_br_id_displayname" label="登记机构" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID1" cssElementClass="emp_pop_common_org" />
			<emp:text id="CusBlkList.input_br_id" label="登记机构" hidden="true"/>
			<emp:select id="CusBlkList.status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
            <emp:datespace id="CusBlkList.black_date" label="登记日期" />
			<emp:text id="CusBlkList.legal_name" label="法定代表人" />
		</emp:gridLayout>
	</emp:form>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCusBlkList" label="查看" op="view"/>
		<emp:button id="printTable" label="导出" op="putout"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
	</div>

	<emp:table icollName="CusBlkListList" pageMode="true" url="pageCusBlkListQuery.do">
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" hidden="false"/>
		<emp:text id="legal_name" label="法定代表人" hidden="false"/>
		<emp:text id="black_level" label="不宜贷款户级别" dictname="STD_ZB_BLACKLIST_TYP" hidden="true"/>
		<emp:text id="data_source" label="数据来源" dictname="STD_ZB_DATA_SOURCE"/>
		<emp:text id="input_id_displayname" label="登记人"/>
		<emp:text id="input_br_id_displayname" label="登记机构"/>
		<emp:text id="manager_id_displayname" label="责任人" hidden="false"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" hidden="false"/>
		<emp:date id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    