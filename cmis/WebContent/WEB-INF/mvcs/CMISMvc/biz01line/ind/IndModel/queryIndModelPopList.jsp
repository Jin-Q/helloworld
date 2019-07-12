<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doSelect(){
	var methodName="${context.popReturnMethod}";
	doReturnMethod(methodName);
};	
function doReturnMethod(methodName){
	if (methodName) {
		var data = IndModelList._obj.getSelectedData();
		var parentWin = EMPTools.getWindowOpener();
		eval("parentWin."+methodName+"(data[0])");
		window.close();
	}else{
		alert("未定义返回的函数，请检查弹出按钮的设置!");
	}
};
	
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IndModel._toForm(form);
		IndModelList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IndModelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IndModelGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IndModel.model_no" label="模型编号" />
			<emp:text id="IndModel.model_name" label="模型名称" />
	</emp:gridLayout>
    <table width="100%"  align=center  class="searchTb">
		<tr>
			<td colspan="4">
			<div align=center>
				<emp:button id="query" label="查询"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<emp:button id="reset" label="重置"/>
			</div>
		</tr>
	</table> 
	<emp:returnButton label="选取并返回"></emp:returnButton>
	<emp:table icollName="IndModelList" pageMode="true" url="pageIndModelPopQuery.do">
		<emp:text id="model_no" label="模型编号" />
		<emp:text id="model_name" label="模型名称" />
		<emp:select id="biz_belg" label="业务所属" dictname="STD_ZB_BIZ_BELG"/>
	</emp:table>
	<emp:returnButton label="选取并返回"></emp:returnButton>
</body>
</html>
</emp:page>