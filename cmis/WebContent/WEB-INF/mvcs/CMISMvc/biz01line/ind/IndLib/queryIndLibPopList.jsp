<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doSelect(){	
	var data = IndLibList._obj.getSelectedData();

	if (data != null) { 
		window.opener["${context.returnMethod}"](data[0]);
		window.close(); 
	} else {
		alert('请先选择一条记录！');
	}
};	
function doReturnMethod(methodName){
	if (methodName) {
		var data = IndLibList._obj.getSelectedData();
		var parentWin = EMPTools.getWindowOpener();
		eval("parentWin."+methodName+"(data[0])");
		window.close();
	}else{
		alert("未定义返回的函数，请检查弹出按钮的设置!");
	}
};	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IndLib._toForm(form);
		IndLibList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IndLibGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IndLibGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IndLib.index_no" label="指标编号" />
			<emp:text id="IndLib.index_name" label="指标名称" />
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
	 
	<emp:table icollName="IndLibList" pageMode="true" url="pageIndLibQuery.do">
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="index_name" label="指标名称" /> 
		<emp:select id="index_property" label="指标性质" dictname="STD_ZB_PARA_PROP"/>
		<emp:select id="index_type" label="指标类别" dictname="STD_ZB_IND_TYPE"/>
		<emp:select id="input_type" label="指标取值方式" dictname="STD_ZB_PARA_VAL_TYP"/>   
	</emp:table>
	<emp:returnButton label="选取并返回"></emp:returnButton>
</body>
</html>
</emp:page>