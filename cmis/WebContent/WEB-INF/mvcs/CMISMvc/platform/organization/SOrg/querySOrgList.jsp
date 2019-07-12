<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSOrgPage() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSOrgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSOrg() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSOrgViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSOrgPage() {
		var url = '<emp:url action="getSOrgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteSOrg() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
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
				var url = '<emp:url action="deleteSOrgRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doMkProfiles(){
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr == null){
			alert("请选择一条记录！");
			return;
		} 
		
		paramStr = SOrgList._obj.getParamStr(['organno']).replace("organno","orgno");
		
		if (paramStr != null) {
			var flag=window.confirm('本操作将会生成选定机构下的所有用户的权限文件，是否继续？');	  
			if(flag){
			var url = '<emp:url action="doMkProfiles.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
		}
	
	}	
	function doCasMkProfiles(){
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr == null){
			alert("请选择一条记录！");
			return;
		} 
		
		paramStr = SOrgList._obj.getParamStr(['organno']).replace("organno","orgno");
		if (paramStr != null) {
			var flag=window.confirm('本操作将会生成选定机构及其子机构下的所有用户的权限文件，是否继续？');	  
			if(flag){
			var url = '<emp:url action="doCasMkProfiles.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
		}
	}	
	function doCasResetPwd(){
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr == null){
			alert("请选择一条记录！");
			return;
		} 
		
		paramStr = SOrgList._obj.getParamStr(['organno']).replace("organno","orgno");
		if (paramStr != null) {
			var flag=window.confirm('本操作将会重置选定机构及其子机构下的所有用户的密码为原始密码(与用户名相同)，是否继续？');	 
			if(flag){
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
							alert("操作成功!");
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
				var url = '<emp:url action="doCasResetPwd.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}	
	function doReset(){
		page.dataGroups.SOrgGroup.reset();
	};
	
	/*--user code begin--*/
	function doICollSelAll(Tabobj){
		Tabobj._obj.selectAll();
	}

	function doICollCleanSel(Tabobj){
		Tabobj._obj.clearAll();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SOrg.organno" label="机构码" />
			<!--modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段   begin-->
			<emp:text id="SOrg.organname" label="机构名称"/>
			<emp:select id="SOrg.org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL"/>
			<!--modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段   end-->
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		
		<emp:button id="getAddSOrgPage" label="新增" op="add"/>
		<emp:button id="getUpdateSOrgPage" label="修改" op="update"/>
		<emp:button id="deleteSOrg" label="删除" op="remove"/>
		<emp:button id="viewSOrg" label="查看" op="view"/>
		<emp:button id="casResetPwd" label="级联重置密码" op="casResetPwd" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
	</div>

	<emp:table icollName="SOrgList" pageMode="true" url="pageSOrgQuery.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
		<emp:text id="organname" label="机构名称" />
		<!--modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段   begin-->
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" />
		<!--modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段   end-->
		<emp:text id="fincode" label="金融代码" />
	</emp:table>
	
</body>
</html>
</emp:page>
    