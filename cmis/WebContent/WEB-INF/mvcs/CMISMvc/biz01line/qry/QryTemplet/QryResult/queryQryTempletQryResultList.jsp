<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateQryResultPage() {
		var paramStr = QryResultList._obj.getParamStr(['temp_no','result_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getQryTempletQryResultUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddQryResultPage(){
		var temp_no = window.parent.window.QryTemplet.temp_no._getValue();
		var url = '<emp:url action="getQryTempletQryResultAddPage.do"/>?QryResult.temp_no='+temp_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteQryResult() {
		var paramStr = QryResultList._obj.getParamStr(['temp_no','result_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteQryTempletQryResultRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewQryResult() {
		var paramStr = QryResultList._obj.getParamStr(['temp_no','result_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryQryTempletQryResultDetail.do"/>?'+paramStr;
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
		<emp:button id="getAddQryResultPage" label="新增" op="add"/>
		<emp:button id="getUpdateQryResultPage" label="修改" op="update"/>
		<emp:button id="deleteQryResult" label="删除" op="remove"/>
		<emp:button id="viewQryResult" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="QryResultList" pageMode="false" url="pageQryTempletQryResultQuery.do" reqParams="QryTemplet.temp_no=$QryTemplet.temp_no;">

		<emp:text id="temp_no" label="查询模板编号" hidden="true"/>
		<emp:text id="result_no" label="返回值编号" />
		<emp:text id="cnname" label="返回值标题名称" />
		<emp:text id="enname" label="列名称" />
		<emp:text id="enname2" label="别名" />
		<emp:text id="result_type" label="返回值类型" dictname="STD_ZB_QRYREST_TYPE" />
		<emp:text id="result_title_displayname" label="返回值数据字典号" />
		<emp:text id="link_temp_no_displayname" label="内部链接" />
		<emp:text id="orderid" label="排序字段" />
	</emp:table>
				
</body>
</html>
</emp:page>