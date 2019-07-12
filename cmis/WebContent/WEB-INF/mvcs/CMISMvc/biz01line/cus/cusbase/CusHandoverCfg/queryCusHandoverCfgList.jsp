<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddCusHandoverCfgPage(){
		var url = '<emp:url action="getCusHandoverCfgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusHandoverCfg(){		
		var paramStr = CusHandoverCfgList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusHandoverCfgRecord.do"/>&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateCusHandoverCfgPage(){
		var paramStr = CusHandoverCfgList._obj.getParamStr(['serno']);
		
		if (paramStr != null) {
			var url = '<emp:url action="getCusHandoverCfgUpdatePage.do"/>&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverCfg(){
		var paramStr = CusHandoverCfgList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusHandoverCfgDetail.do"/>&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusHandoverCfg._toForm(form);
		CusHandoverCfgList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusHandoverCfgGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusHandoverCfgGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="CusHandoverCfg.table_mode" label="移交方式" dictname="STD_ZB_HAND_TYPE" />
			<emp:select id="CusHandoverCfg.table_scope" label="移交范围" dictname="STD_ZB_HAND_SCOPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddCusHandoverCfgPage" label="新增" />
		<emp:button id="getUpdateCusHandoverCfgPage" label="修改" />
		<emp:button id="deleteCusHandoverCfg" label="删除" />
		<emp:button id="viewCusHandoverCfg" label="查看" />
	</div>
	<emp:table icollName="CusHandoverCfgList" pageMode="true" url="pageCusHandoverCfgQuery.do">
		<emp:text id="table_mode" label="移交方式" dictname="STD_ZB_HAND_TYPE" />
		<emp:text id="table_scope" label="移交范围" dictname="STD_ZB_HAND_SCOPE" />
		<emp:text id="ext_class" label="扩展处理" />
		<emp:text id="memo" label="备注" />
		<emp:text id="serno" label="序列号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>