<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SUser._toForm(form);
		SUserList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSUserPage() {
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSUserUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSUser() {
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSUserViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSUserPage() {
		var url = '<emp:url action="getSUserAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		//window.location = url;
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteSUser() {
		var paramStr = SUserList._obj.getParamStr(['actorno']);
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
				var url = '<emp:url action="deleteSUserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};


	function doResetPassword() {
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr != null) {
			if(confirm("是否确认重置该用户的密码？")){
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
							alert("重置密码成功!");
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
				var url = '<emp:url action="resetPasswordSUserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};


	function doResetAllPassword() {
		
		
			if(confirm("警告！您正在进行重置所有的用户的密码的操作，如果确定会将所有的用户密码重置为原始密码！！")){
				EMPTools.setWait(); 
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
							EMPTools.removeWait();
							alert("批量重置密码成功!");
							window.location.reload();
						}else {
							EMPTools.removeWait();
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
				var url = '<emp:url action="resetAllPasswordSUserRecord.do"/>';
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		
	};
	
	
	function doReset(){
		page.dataGroups.SUserGroup.reset();
	};
	
	/*--user code begin--*/
    function doAddUserRole(){
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr!=null) {
			var url = "<emp:url action='listUserRole.do'/>&"+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		}else {
			alert('请先选择一条记录！');
		}
	};			
	function doAddDutyUser(){
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr!=null) {
			var url = "<emp:url action='listDutyUser.do'/>&"+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		}else {
			alert('请先选择一条记录！');
		}
	};		
	function doAddDeptUser(){
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr!=null) {       
			var url = "<emp:url action='listDeptUser.do'/>&mode=suborg&"+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);	
		}else {
			alert('请先选择一条记录！');
		}
	};	
	function doUserPerFile(){
		var paramStr = SUserList._obj.getParamStr(['actorno']);
		if (paramStr!=null) {       
			var flag=window.confirm('本操作将会生成选定用户的权限文件，是否继续？');	  
			if(flag){
			var url = "<emp:url action='genUserPermissionFile.do'/>&"+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;			
			}
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	//选取所属机构
	function getOrgID(data){
		SUser.orgid._setValue(data.organno._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SUserGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="SUser.actorno" label="用户码" />
			<emp:text id="SUser.actorname" label="姓名" />
			<emp:text id="SUser.organname" label="机构名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSUserPage" label="新增" op="add"/>
		<emp:button id="getUpdateSUserPage" label="修改" op="update"/>
		<emp:button id="deleteSUser" label="删除" op="remove"/>
		<emp:button id="viewSUser" label="查看" op="view"/>
		<emp:button id="addUserRole" label="设置角色" op="addUserRole"/>
		<emp:button id="addDutyUser" label="设置岗位" op="addDutyUser"/>
		<emp:button id="resetPassword" label="重置密码" op="resetPassword"/> 
		<emp:button id="resetAllPassword" label="批量重置密码" op="resetAllPassword" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		<emp:button id="addDeptUser" label="设置机构" op="addDeptUser"/> 
		 
	</div>
	<emp:table icollName="SUserList" pageMode="true" url="pageSUserQuery.do">
		<emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
		<emp:text id="orgid" label="所属机构" />
		<emp:text id="state" label="用户状态" dictname="STD_ZB_USER_STATE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    