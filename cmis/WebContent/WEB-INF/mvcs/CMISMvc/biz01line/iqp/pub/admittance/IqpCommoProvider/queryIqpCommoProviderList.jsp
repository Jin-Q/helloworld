<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
<%
	String catalog_no = request.getParameter("catalog_no");
%>
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpCommoProvider._toForm(form);
		IqpCommoProviderList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCommoProviderPage() {
		var paramStr = IqpCommoProviderList._obj.getParamStr(['mort_catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCommoProviderUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCommoProvider() {
		var paramStr = IqpCommoProviderList._obj.getParamStr(['mort_catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCommoProviderViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCommoProviderPage() {
		var catalog_no = '<%=catalog_no%>';
		var url = '<emp:url action="getIqpCommoProviderAddPage.do"/>?catalog_no='+catalog_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//删除
	function doDeleteIqpCommoProvider() {
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("删除成功！");
					window.location.reload();  
				}else{
					alert("删除失败，失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("删除失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = IqpCommoProviderList._obj.getParamStr(['mort_catalog_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var mort_catalog_no = IqpCommoProviderList._obj.getSelectedData()[0].mort_catalog_no._getValue();
				var url = '<emp:url action="deleteIqpCommoProviderRecord.do"/>?mort_catalog_no='+mort_catalog_no;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCommoProviderGroup.reset();
	};

	function doInsert(){//准入
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}else{
					alert("准入失败，失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("准入失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = IqpCommoProviderList._obj.getParamStr(['mort_catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryInsertCommoProvider.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doOut(){//退出
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}else{
					alert("退出失败，失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("退出失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = IqpCommoProviderList._obj.getParamStr(['mort_catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryOutCommoProvider.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpCommoProviderGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpCommoProvider.mort_catalog_no" label="押品目录编号" />
			<emp:text id="IqpCommoProvider.provider_no" label="供应商编号" />
			<emp:select id="IqpCommoProvider.status" label="状态" dictname="STD_ZB_COMMO_PROVIDER"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddIqpCommoProviderPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpCommoProviderPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpCommoProvider" label="删除" op="remove"/>
		<emp:actButton id="viewIqpCommoProvider" label="查看" op="view"/>
		<emp:actButton id="insert" label="准入" op="update"/>
		<emp:actButton id="out" label="退出" op="update"/>
	</div>

	<emp:table icollName="IqpCommoProviderList" pageMode="true" url="pageIqpCommoProviderQuery.do">
		<emp:text id="mort_catalog_no" label="押品目录编号" />
		<emp:text id="provider_no" label="供应商编号" />
		<emp:text id="provider_no_displayname" label="供应商名称" />
		<emp:text id="linkman" label="联系人" />
		<emp:text id="link_addr" label="联系地址" />
		<emp:text id="link_phone" label="联系电话" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="status" label="状态" dictname="STD_ZB_COMMO_PROVIDER"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    