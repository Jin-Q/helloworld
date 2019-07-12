<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String opType = request.getParameter("opType");
%>
<emp:page>

<html>
<head>
<title>列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiApproverUser._toForm(form);
		WfiApproverUserList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiApproverUserPage() {
		var paramStr = WfiApproverUserList._obj.getParamStr(['confid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiApproverUserUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiApproverUser() {
		var paramStr = WfiApproverUserList._obj.getParamStr(['confid','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiApproverUserViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiApproverUserPage() {
		var url = '<emp:url action="getWfiApproverUserAddPage.do"/>?confid=${context.confid}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiApproverUser() {
		var paramStr = WfiApproverUserList._obj.getParamStr(['confid','actorno']);
		if (paramStr != null) {
			if(confirm("确定要删除此记录？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryWfiApproverUserList.do"/>?'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!错误原因："+flag);
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteWfiApproverUserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiApproverUserGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfiApproverUserGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiApproverUser.actorno" label="用户ID"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<%if(!"view".equals(opType)) { %>
	<div align="left">
		<emp:button id="getAddWfiApproverUserPage" label="新增" op=""/>
		<emp:button id="deleteWfiApproverUser" label="删除" op=""/>
	</div>
	<%} %>
	<emp:table icollName="WfiApproverUserList" pageMode="true" url="pageWfiApproverUserQuery.do?confid=${context.confid }">
		<emp:text id="confid" label="配置ID" hidden="true"/>
		<emp:text id="actorno" label="用户ID"/>
		<emp:text id="actorno_displayname" label="用户名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    