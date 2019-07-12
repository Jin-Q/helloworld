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
		
	function doViewGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="view";
		var menuId = "";
		if (paramStr != null) {
			var guar_cont_type = GrtGuarContList._obj.getParamValue(['guar_cont_type']);
			if(guar_cont_type =='00'){
				menuId = 'ybCount';
			}else{
				menuId = 'zge';
			}
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&flag=queryGrtGuarCont4Tab&drfpo_no=${context.drfpo_no}&po_no=${context.po_no}&'+paramStr+'&menuId='+menuId;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewGrtGuarCont" label="查看"/>
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageGrtGuarCont4TabQuery.do?drfpo_no=${context.drfpo_no}">
		<emp:text id="guar_cont_no" label="担保合同编号 " />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_model" label="担保模式" dictname="STD_GUAR_MODEL"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="cus_id_displayname" label="借款人名称" hidden="true"/>
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
		<emp:text id="rel" label="申请类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    