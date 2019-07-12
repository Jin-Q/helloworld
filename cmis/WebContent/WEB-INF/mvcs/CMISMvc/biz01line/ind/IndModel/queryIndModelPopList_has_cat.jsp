<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doSelect(){	
	var data = IndModelList._obj.getSelectedData();

	if (data != null) { 
		window.opener["${context.returnMethod}"](data[0]);
		window.close(); 
	} else {
		alert('请先选择一条记录！');
	}
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

	
	<emp:table icollName="IndModelList" pageMode="false" url="pageIndModel_has_catQuery.do">
		<emp:text id="model_no" label="模型编号" />
		<emp:text id="model_name" label="模型名称" />
		<emp:select id="com_biz_kind" label="适用业务品种" dictname="STD_ZB_BIZ_KIND" hidden="true"/>
		<emp:select id="com_opt_scale" label="适用企业规模" dictname="STD_ZB_ENTERPRISE" hidden="true"/>
		<emp:select id="com_biz_kind" label="适用业务类型" dictname="STD_ZB_BIZ_KIND" hidden="true"/>  
		<emp:select id="use_flag" label="启用标志" dictname="STD_ZB_USE_FLAG" hidden="true"/> 
	</emp:table>
	<emp:returnButton label="选取并返回"></emp:returnButton>
</body>
</html>
</emp:page>