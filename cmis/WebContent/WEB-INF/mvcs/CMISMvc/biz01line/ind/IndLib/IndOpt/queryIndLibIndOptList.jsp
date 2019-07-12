<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIndOptPage() {
		var paramStr = IndOptList._obj.getParamStr(['index_no','index_value']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIndLibIndOptUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddIndOptPage(){
		var index_no = window.parent.window.IndLib.index_no._getValue();
		var url = '<emp:url action="getIndLibIndOptAddPage.do"/>?IndOpt.index_no='+index_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteIndOpt() {
		var paramStr = IndOptList._obj.getParamStr(['index_no','index_value']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndLibIndOptRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndOpt() {
		var paramStr = IndOptList._obj.getParamStr(['index_no','index_value']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndLibIndOptDetail.do"/>?'+paramStr;
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
		<emp:button id="getAddIndOptPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndOptPage" label="修改" op="update"/>
		<emp:button id="deleteIndOpt" label="删除" op="remove"/>
		<emp:button id="viewIndOpt" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="IndOptList" pageMode="true" url="pageIndLibIndOptQuery.do" reqParams="IndLib.index_no=$IndLib.index_no;">

		<emp:text id="index_no" label="指标编号" />
		<emp:text id="ind_desc" label="指标描述" />
		<emp:text id="index_value" label="指标选项值" />
		<emp:text id="value_score" label="选项值得分" />
	</emp:table>
				
</body>
</html>
</emp:page>