<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddQryTempletPage(){
		var url = '<emp:url action="getQryTempletAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteQryTemplet(){		
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
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteQryTempletRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateQryTempletPage(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getQryTempletUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewQryTemplet(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryQryTempletDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/**生成查询页面**/
	function doGenQryPage(){
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
					alert("生成查询页面成功！");
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="genQryPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};

	//综合查询
	function doGetQueryPage(){
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
					var res = jsonstr.res;
					if("Y"==res){
						var paramStr = QryTempletList._obj.getParamStr(['temp_no','jsp_file_name','temp_name','templet_type']);
						var url = '<emp:url action="getQueryPage.do"/>?'+paramStr;
					}else{
						var _tempno = QryTempletList._obj.getParamValue(['temp_no']);
						var url = '<emp:url action="doQuery.do"/>?IN_TEMPNO='+_tempno+"&ShowColumns=&OrderByColumns=&INT_COUNT=50000";
					}
					url = EMPTools.encodeURI(url);
					window.open(url,'getQueryPageWindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="checkParamCount.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doQuery(){
		var form = document.getElementById('queryForm');
		QryTemplet._toForm(form);
		QryTempletList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.QryTempletGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>
	<emp:gridLayout id="QryTempletGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="QryTemplet.temp_name" label="查询名称"/>
			<emp:select id="QryTemplet.templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" readonly="false" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />


	<div align="left">
		<emp:button id="getAddQryTempletPage" label="新增" op="add"/>
		<emp:button id="getUpdateQryTempletPage" label="修改" op="update"/>
		<emp:button id="deleteQryTemplet" label="删除" op="remove"/>
		<emp:button id="genQryPage" label="生成查询页面" op="update" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
		<emp:button id="getQueryPage" label="综合查询"/>
	</div>
	<emp:table icollName="QryTempletList" pageMode="true" url="pageQryTempletQuery.do">
		<emp:text id="temp_no" label="查询模板编号" hidden="false"/>
		<emp:text id="temp_name" label="查询名称"/>
		<emp:text id="organlevel_displayname" label="适用机构" />
		<emp:text id="templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" />
		<emp:text id="temp_pattern" label="查询模式" dictname="STD_ZB_TEMP_PATTERN" hidden="true"/>
		<emp:text id="classpath" label="扩展类路径" hidden="true"/>
		<emp:text id="temp_enable" label="是否启用" dictname="STD_ZX_YES_NO"   />
		<emp:text id="query_sql" label="查询SQL语句" hidden="true" />
		<emp:text id="jsp_file_name" label="查询页面文件夹名" hidden="true"/>
		<emp:text id="order_id" label="排序字段" />
	</emp:table>
</body>
</html>
</emp:page>