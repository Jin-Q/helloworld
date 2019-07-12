<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarantee._toForm(form);
		GrtGuaranteeList._obj.ajaxQuery(null,form);
	};
	function doViewGrtGuarantee() {
		var paramStr = GrtGuaranteeList._obj.getParamStr(['guar_cont_no']);
		var paramStr1 = GrtGuaranteeList._obj.getParamStr(['guarty_cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuaranteeAllViewPage.do"/>?'+paramStr+'&'+paramStr1;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.GrtGuaranteeGroup.reset();
	};
	function doLoad(){
		
	}
	function returnCus(data){
		GrtGuarantee.guarty_cus_id._setValue(data.cus_id._getValue());
		GrtGuarantee.guarty_cus_id_displayname._setValue(data.cus_name._getValue());
    };

    /*** 影像部分操作按钮begin ***/
	function doImageView(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = GrtGuaranteeList._obj.getParamValue(['guarty_cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = GrtGuaranteeList._obj.getParamValue(['guarty_cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="GrtGuaranteeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="GrtGuarantee.guar_cont_no" label="担保合同编号 " />
			<emp:pop id="GrtGuarantee.guarty_cus_id_displayname" label="保证人客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus"/>
			<emp:select id="GrtGuarantee.guar_type" label="保证形式" dictname="STD_GUAR_FORM" />
			<emp:text id="GrtGuarantee.guarty_cus_id" label="保证人客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="viewGrtGuarantee" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
	</div>
	<emp:table icollName="GrtGuaranteeList" pageMode="true" url="pageGrtGuaranteeAllQuery.do">
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guarty_cus_id" label="保证人客户码"/>
	    <emp:text id="guarty_cus_id_displayname" label="保证人名称"/>
		<emp:text id="guar_type" label="保证形式" dictname="STD_GUAR_FORM"/>
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="cus_id" label="借款人客户码" hidden="true"/>
        <emp:text id="cus_id_displayname" label="借款人名称" hidden="true"/>
		<emp:text id="guar_start_date" label="担保起始日期" />
		<emp:text id="guar_end_date" label="担保到期日期" />
		<emp:text id="input_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
	</emp:table>
</body>
</html>
</emp:page>
    