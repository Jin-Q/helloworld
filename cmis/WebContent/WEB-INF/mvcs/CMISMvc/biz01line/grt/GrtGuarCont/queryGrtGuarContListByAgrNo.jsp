<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&'+paramStr+'&oper=view&flag=ybWh&menuId=zge';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.GrtGuarContGroup.reset();
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewGrtGuarCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="false" url="pageGrtGuarContQuery.do?action=${context.action}">
		<emp:text id="guar_cont_no" label="担保合同编号 " />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_model" label="担保模式" dictname="STD_GUAR_MODEL"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="cus_id_displayname" label="借款人名称" hidden="true"/>
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    