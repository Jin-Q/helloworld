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
		SDept._toForm(form);
		SDeptList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSDeptPage() {
		var paramStr = SDeptList._obj.getParamStr(['depno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDeptUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSDept() {
		var paramStr = SDeptList._obj.getParamStr(['depno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDeptViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSDeptPage() {
		var url = '<emp:url action="getSDeptAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		//window.location = url;
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteSDept() {
		var paramStr = SDeptList._obj.getParamStr(['depno']);
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
				var url = '<emp:url action="deleteSDeptRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SDeptGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SDeptGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SDept.depno" label="部门码" />
			<emp:text id="SDept.organno" label="机构码" />
			<emp:text id="SDept.depname" label="部门名称" />
			<emp:text id="SDept.distno" label="地区编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSDeptPage" label="新增" op="add"/>
		<emp:button id="getUpdateSDeptPage" label="修改" op="update"/>
		<emp:button id="deleteSDept" label="删除" op="remove"/>
		<emp:button id="viewSDept" label="查看" op="view"/>
	</div>

	<emp:table icollName="SDeptList" pageMode="true" url="pageSDeptQuery.do">
		<emp:text id="depno" label="部门码" />
		<emp:text id="organno" label="机构码" />
		<emp:text id="depname" label="部门名称" />
		<emp:text id="distno" label="地区编号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    