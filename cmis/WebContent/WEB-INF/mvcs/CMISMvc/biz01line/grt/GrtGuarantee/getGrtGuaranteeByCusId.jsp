<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

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
			var url = '<emp:url action="getGrtGuaranteeAllViewPage.do"/>?tab=tab&'+paramStr+'&'+paramStr1;
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
	</div>
	<emp:table icollName="GrtGuaranteeList" pageMode="true" url="pageGetGrtGuaranteeByCusId.do?cus_id=${context.cus_id}">
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
    