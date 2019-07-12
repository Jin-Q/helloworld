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
		SfTrans._toForm(form);
		SfTransList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.SfTransGroup.reset();
	};

	function doReturnMethod(){
		var methodName='${context.returnMethod}';
		if (methodName) {
			var data = SfTransList._obj.getSelectedData();
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
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SfTransGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SfTrans.trans_name" label="交易名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:returnButton label="选择返回" />
	</div>

	<emp:table icollName="SfTransList" pageMode="true" url="pageSfTransQuery.do">
		<emp:text id="trans_id" label="交易ID" />
		<emp:text id="trans_name" label="交易名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    