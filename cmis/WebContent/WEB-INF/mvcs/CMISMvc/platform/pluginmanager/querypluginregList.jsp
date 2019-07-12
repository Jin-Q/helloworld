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
		pluginreg._toForm(form);
		pluginregList._obj.ajaxQuery(null,form);
	};
	
	//安装插件
	function doInstallPlugin(){
		var path = prompt("请选择插件安装包","/home/yuhq/mydoc/产品研发/插件/测试模块一");
		if(path==null || path=="null") return ;
		if(confirm("你选择的插件安装包是:\n"+path+"\n请确认!")){
			document.getElementById("plugin_path").value=path;
			doAsynInstallPlugin(path);
		}else{
			//alert("用户重置插件发装");
		}
		
		
	}
	
	//异步安装插件
	function doAsynInstallPlugin(path){
 		var form =  document.getElementById("dataForm");
 		form.action="<emp:url action='installPlugin.do'/>"
 		form.method = "POST"; 

 		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
				var message = jsonstr.message;
				if(flag=="success"){
					alert(message);
					
					var url ='<emp:url action="querypluginregList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					 alert(message);
					 return;
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
	}

	//更新插件
	function doUpdatePlugin(){
		var path = prompt("请选择插件安装包","/home/yuhq/mydoc/产品研发/插件/测试模块一");
		if(path==null || path=="null") return ;
		if(confirm("你选择的插件更新包是:\n"+path+"\n请确认!")){
			document.getElementById("plugin_path").value=path;
			doAsynUpdatePlugin(path);
		}else{
			//alert("用户重置插件发装");
		}
	}

	//异步更新插件
	function doAsynUpdatePlugin(path){
 		var form =  document.getElementById("dataForm");
 		form.action="<emp:url action='updatePlugin.do'/>";
 		form.method = "POST"; 

 		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
				var message = jsonstr.message;
				if(flag=="success"){
					alert(message);
					
					var url ='<emp:url action="querypluginregList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					 alert(message);
					 return;
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
	}
	
	
	
	//卸载插件
	function doUninstallPlugin(){
		var paramStr = pluginregList._obj.getParamStr(['plugin_modual_id']);
		if (paramStr != null) {
			if(confirm("您确定要卸载该插件?")){
				doAsynUninstallPlugin(paramStr);
			}else{
			}
		} else {
			alert('请先选择一条记录！');
		}
		
	}
	
	//异步安装插件
	function doAsynUninstallPlugin(pathUrl){
 		var form =  document.getElementById("dataForm");
 		form.action="<emp:url action='uninstallPlugin.do'/>&"+pathUrl;
 		form.method = "POST"; 

 		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
				var message = jsonstr.message;
				if(flag=="success"){
					alert(message);
					
					var url ='<emp:url action="querypluginregList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					 alert(message);
					 return;
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
	}
	
	
	
	//启用插件
	function doStartupPlugin(){
		alert("待实现");
	}
	
	//停用插件
	function doStopPlugin(){
		alert("待实现");
	}

	//导出插件
	function doExportPlugin(){
		var paramStr = pluginregList._obj.getParamStr(['plugin_modual_id']);
		var pluginId = "";
		if (paramStr == null) {
			pluginId = prompt("请输入需要导出的插件ID","");
			if(pluginId==null || pluginId=="") {
				alert("插件ID不能为空");
				return ;
			}

			paramStr = "plugin_modual_id="+pluginId;
		} 

		var path = prompt("请选择导出插件目录","E:/开发记录/信贷研发/2013/信贷业务开发平台/插件/导出的插件");
		if(path==null || path=="null") return ;
		if(confirm("你选择的导出插件目录是:\n"+path+"\n请确认!")){
			paramStr = paramStr+"&export_path="+path;
			
			doAsynExportPlugin(paramStr);
		}
	}

	//异步导出插件
	function doAsynExportPlugin(paramStr){
 		var form =  document.getElementById("dataForm");
 		form.action="<emp:url action='exportPlugin.do'/>&"+paramStr;
 		form.method = "POST"; 

 		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
				var message = jsonstr.message;
				if(flag=="success"){
					alert(message);
					
					var url ='<emp:url action="querypluginregList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					 alert(message);
					 return;
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
	}
	
	
	function doReset(){
		page.dataGroups.pluginregGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<form  method="POST" action="#" id="dataForm">
		<emp:text id="plugin_path" label="路径" hidden="true"></emp:text>
	</form>

	<emp:gridLayout id="pluginregGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="pluginreg.plugin_modual_name" label="插件名称" />
			<emp:text id="pluginreg.install_date" label="安装时间" />
			<emp:text id="pluginreg.plugin_status" label="插件状态" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="installPlugin" label="安装插件" op=""/>
		<emp:button id="updatePlugin" label="更新插件" op=""/>
		<emp:button id="uninstallPlugin" label="卸载插件" op=""/>
		<emp:button id="startupPlugin" label="启用插件" op=""/>
		<emp:button id="stopPlugin" label="停用插件" op=""/>
		<emp:button id="exportPlugin" label="导出插件" op=""/>
	</div>

	<emp:table icollName="pluginregList" pageMode="false" url="pagepluginregQuery.do">
		<emp:text id="plugin_modual_id" label="插件ID" />
		<emp:text id="plugin_modual_name" label="插件名称" />
		<emp:text id="install_date" label="安装时间" />
		<emp:text id="plugin_version" label="插件版本" />
		<emp:text id="plugin_status" label="插件状态" dictname="PLUGIN_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    