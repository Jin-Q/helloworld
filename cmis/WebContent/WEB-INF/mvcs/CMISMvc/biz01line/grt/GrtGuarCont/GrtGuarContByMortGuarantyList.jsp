<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String flag = "";
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarCont._toForm(form);
		GrtGuarContList._obj.ajaxQuery(null,form);
	};
	
	function doViewGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?flag=arp&menuIdTab=mort_maintain&op=view&'+paramStr+'&oper='+flag+'&guaranty_no=${context.guaranty_no}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.GrtGuarContGroup.reset();
	};

	
	function returnCus(data){
		GrtGuarCont.cus_id._setValue(data.cus_id._getValue());
		GrtGuarCont.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    function returnCus2(data){
    	GrtGuarCont.guar_id._setValue(data.cus_id._getValue());
    	GrtGuarCont.guar_id_displayname._setValue(data.cus_name._getValue());
    };

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="GrtGuarContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号 " />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" />
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="viewGrtGuarCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageQueryGrtGuarContByMortGuarantyList.do?guaranty_no=${context.guaranty_no}">
		<emp:text id="guar_cont_no" label="担保合同编号 " />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_model" label="担保模式" dictname="STD_GUAR_MODEL"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="cus_id_displayname" label="借款人名称" hidden="true"/>
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
		<emp:text id="reg_date" label="登记日期" />
		<emp:text id="rel" label="申请类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    