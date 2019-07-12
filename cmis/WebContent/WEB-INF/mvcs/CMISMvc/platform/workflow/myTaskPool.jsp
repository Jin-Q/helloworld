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
		WfTaskpool._toForm(form);
		WfTaskpoolList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.WfTaskpoolGroup.reset();
	};
	
	/*--user code begin--*/
		
	function doViewAppList() {
		var paramStr = WfTaskpoolList._obj.getParamStr(['tpid']);
		if(paramStr==null || paramStr=='') {
			alert('请选择一条记录！');
			return;
		}
		var url = '<emp:url action="getToDoTaskPoolList.do" />?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location.href=url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfTaskpoolGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfTaskpool.tpid" label="项目池编号" />
		<emp:text id="WfTaskpool.tpname" label="项目池名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewAppList" label="查看项目池" op="view" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
	</div>

	<emp:table icollName="WfTaskpoolList" pageMode="false" url="pageMyTaskPoolQuery.do">
		<emp:text id="tpid" label="项目池编号" />
		<emp:text id="tpname" label="项目池名称" />
		<emp:text id="tpsize" label="任务数量" />
		<emp:text id="tpdsc" label="描述" />
		<emp:text id="sysid" label="系统ID" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
 