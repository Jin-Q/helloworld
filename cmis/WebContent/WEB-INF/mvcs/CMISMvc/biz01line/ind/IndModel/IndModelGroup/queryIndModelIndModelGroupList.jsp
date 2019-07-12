<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIndModelGroupPage() {
		var paramStr = IndModelGroupList._obj.getParamStr(['model_no','group_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIndModelIndModelGroupUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddIndModelGroupPage(){
		var model_no = window.parent.window.IndModel.model_no._getValue();
		var url = '<emp:url action="getIndModelIndModelGroupAddPage.do"/>?IndModelGroup.model_no='+model_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteIndModelGroup() {
		var paramStr = IndModelGroupList._obj.getParamStr(['model_no','group_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndModelIndModelGroupRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndModelGroup() {
		var paramStr = IndModelGroupList._obj.getParamStr(['model_no','group_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndModelIndModelGroupDetail.do"/>?'+paramStr;
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
		<emp:button id="getAddIndModelGroupPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndModelGroupPage" label="修改" op="update"/>
		<emp:button id="deleteIndModelGroup" label="删除" op="remove"/>
		<emp:button id="viewIndModelGroup" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="IndModelGroupList" pageMode="true" url="pageIndModelIndModelGroupQuery.do" reqParams="IndModel.model_no=$IndModel.model_no;">

		<emp:text id="model_no" label="模型编号" />
		<emp:text id="group_no" label="组别编号" />
		<emp:text id="group_name" label="组别名称"/>
		<emp:text id="weight" label="权重" />
		<emp:text id="seqno" label="顺序号" />
	</emp:table>
				
</body>
</html>
</emp:page>