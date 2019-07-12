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
		WorkFlow._toForm(form);
		WorkFlowList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.WorkFlowGroup.reset();
	};
	
	/*--user code begin--*/
		
	function doReloadWfCache() {
		var url = '<emp:url action="reloadWfCache.do" />';
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "0") {
					alert('重新加载流程缓存成功！');
				} else {
					alert('重新加载流程缓存异常！');
				}
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = null;	 
		var obj1 = YAHOO.util.Connect.asyncRequest('post',url, callback, postData);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WorkFlowGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WorkFlow.wfsign" label="流程标识" />
			<emp:text id="WorkFlow.wfname" label="流程名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:button label="重载流程缓存" id="reloadWfCache"></emp:button>
	<emp:table icollName="WorkFlowList" pageMode="true" url="pageWFListQuery.do">
		<emp:text id="wfid" label="流程ID" />
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfname" label="流程名称" />
		<emp:text id="wfver" label="版本" />
	</emp:table>
	
</body>
</html>
</emp:page>