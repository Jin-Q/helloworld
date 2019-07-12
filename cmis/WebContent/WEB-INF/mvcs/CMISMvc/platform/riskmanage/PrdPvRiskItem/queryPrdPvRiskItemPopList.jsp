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
		PrdPvRiskItem._toForm(form);
		PrdPvRiskItemList._obj.ajaxQuery(null,form);
	};
	
	function doReturnMethod(){
		var methodName='${context.returnMethod}';
		if (methodName) {
			var data = PrdPvRiskItemList._obj.getSelectedData();
			if(data!=null&&data!=''){
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin."+methodName+"(data[0])");
				window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	
	function doReset(){
		page.dataGroups.PrdPvRiskItemGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPvRiskItemGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPvRiskItem.item_id" label="项目编号" />
			<emp:text id="PrdPvRiskItem.item_name" label="项目名称" />
			<emp:select id="PrdPvRiskItem.used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:returnButton label="选择返回" />
	</div>

	<emp:table icollName="PrdPvRiskItemList" pageMode="true" url="pagePrdPvRiskItemQuery.do">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:table>
	
</body>
</html>
</emp:page>
    