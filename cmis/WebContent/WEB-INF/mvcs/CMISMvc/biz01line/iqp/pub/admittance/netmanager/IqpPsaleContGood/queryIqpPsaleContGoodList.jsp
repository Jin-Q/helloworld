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
		IqpPsaleContGood._toForm(form);
		IqpPsaleContGoodList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpPsaleContGood() {
		var paramStr = IqpPsaleContGoodList._obj.getParamStr(['psale_cont','commo_name']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPsaleContGoodViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpPsaleContGoodGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="viewIqpPsaleContGood" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpPsaleContGoodList" pageMode="true" url="pageIqpPsaleContGoodQuery.do" reqParams="op=view&psale_cont=${context.psale_cont}">
		<emp:text id="psale_cont" label="协议号" hidden="true"/>
		<emp:text id="commo_name" label="商品名称" hidden="true"/>
		<emp:text id="commo_name_displayname" label="商品名称" />
		<emp:text id="qnt" label="数量" />
		<emp:text id="unit_price" label="单价（元）" dataType="Currency"/>
		<emp:text id="total" label="总价值（元）"  dataType="Currency"/>
		<emp:text id="psale_cont" label="购销合同编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    