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
		SRole._toForm(form);
		SRoleList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSRolePage() {
		var paramStr = SRoleList._obj.getParamStr(['roleno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSRoleUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSRole() {
		var paramStr = SRoleList._obj.getParamStr(['roleno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSRoleViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSRolePage() {
		var url = '<emp:url action="getSRoleAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		//window.location = url;
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteSRole() {
		var paramStr = SRoleList._obj.getParamValue(['roleno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert(msg);
							return;
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
				var url = '<emp:url action="deleteSRoleRecord.do"/>?roleNos='+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SRoleGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SRoleGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SRole.roleno" label="角色码" />
			<emp:text id="SRole.rolename" label="角色名称" />
			<emp:select id="SRole.type" label="类型" dictname="STD_ZB_SROLE_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSRolePage" label="新增" op="add"/>
		<emp:button id="getUpdateSRolePage" label="修改" op="update"/>
		<emp:button id="deleteSRole" label="删除" op="remove"/>
		<emp:button id="viewSRole" label="查看" op="view"/>
	</div>

	<emp:table icollName="SRoleList" pageMode="true" url="pageSRoleQuery.do">
		<emp:text id="roleno" label="角色码" />
		<emp:text id="rolename" label="角色名称" />
		<emp:text id="type" label="类型" dictname="STD_ZB_SROLE_TYPE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    