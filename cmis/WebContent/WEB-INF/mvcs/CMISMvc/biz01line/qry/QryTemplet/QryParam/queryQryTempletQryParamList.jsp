<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateQryParamPage() {
		var paramStr = QryParamList._obj.getParamStr(['temp_no','param_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getQryTempletQryParamUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddQryParamPage(){
		var temp_no = window.parent.window.QryTemplet.temp_no._getValue();
		var url = '<emp:url action="getQryTempletQryParamAddPage.do"/>?QryParam.temp_no='+temp_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteQryParam() {
		var paramStr = QryParamList._obj.getParamStr(['temp_no','param_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteQryTempletQryParamRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewQryParam() {
		var paramStr = QryParamList._obj.getParamStr(['temp_no','param_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryQryTempletQryParamDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！'); 
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<div align="left">
		<emp:button id="getAddQryParamPage" label="新增" op="add"/>
		<emp:button id="getUpdateQryParamPage" label="修改" op="update"/>
		<emp:button id="deleteQryParam" label="删除" op="remove"/>
		<emp:button id="viewQryParam" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="QryParamList" pageMode="false" url="pageQryTempletQryParamQuery.do" reqParams="QryTemplet.temp_no=$QryTemplet.temp_no;">

		<emp:text id="temp_no" label="查询模板编号" hidden="true"/>
		<emp:text id="param_no" label="条件参数编号" />
		<emp:text id="enname" label="参数英文名称" />
		<emp:text id="cnname" label="参数中文名称" />
		<emp:text id="param_type" label="条件参数类型" dictname="STD_ZB_PARAM_TYPE" />
		<emp:text id="param_dic_no_displayname" label="参数选项字典编号" />
		<emp:text id="orderid" label="排序字段" />
	</emp:table>
				
</body>
</html>
</emp:page>