<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	function doSelect() {
		var data = WfiWorkflow2bizList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<div align="left">
		<emp:button id="select" label="选取返回" />
	</div>

	<emp:table icollName="WfiWorkflow2bizList" pageMode="false" url="">
		<emp:text id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfname" label="流程名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    