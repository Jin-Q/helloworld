<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doReset(){
		page.dataGroups.IqpMortCatalogManaGroup.reset();
	};
	
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpMortCatalogMana._toForm(form);
		IqpMortCatalogManaList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpMortCatalogManaPage() {
		var paramStr = IqpMortCatalogManaList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			var status = IqpMortCatalogManaList._obj.getParamValue(['status']);
			if("1"==status){  //如果已经是生效状态
				alert("该押品目录已是【生效】状态，不能进行修改！");
				return false;
			}
			var url = '<emp:url action="getIqpMortCatalogManaUpdatePage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpMortCatalogMana() {
		var paramStr = IqpMortCatalogManaList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpMortCatalogManaViewPage.do"/>?'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpMortCatalogManaPage() {
		var url = '<emp:url action="getIqpMortCatalogManaAddPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//删除
	function doDeleteIqpMortCatalogMana() {
		var status = IqpMortCatalogManaList._obj.getParamValue(['status']);
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
					
					doQuery();  //异步刷新列表
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
		
		var paramStr = IqpMortCatalogManaList._obj.getParamStr(['catalog_no','catalog_lvl']);
		if (paramStr != null) {
			if("1"==status){  //如果已经是生效状态
				alert("该押品目录已是【生效】状态，不能进行删除操作！");
				return false;
			}
			if(confirm("删除后将同时删除此目录下的供应商，是否确认要删除？")){
				var url = '<emp:url action="deleteIqpMortCatalogManaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	//生效
	function doInureIqpMortCatalogMana(){
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
				if("Y" == flag){
					alert("修改成功！");
					
					doQuery();  //异步刷新列表
				}else{
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("修改失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = IqpMortCatalogManaList._obj.getParamStr(['catalog_no','catalog_name','catalog_lvl']);
		if (paramStr != null) {
			var status = IqpMortCatalogManaList._obj.getParamValue(['status']);
			if("1"==status){  //如果已经是生效状态
				alert("该押品目录已是【生效】状态！");
				return false;
			}
			var url = '<emp:url action="updateCatalogManaStatus.do"/>?'+ encodeURI(paramStr)+"&status=1";
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	//失效
	function doLogOutIqpMortCatalogMana(){
		var status = IqpMortCatalogManaList._obj.getParamValue(['status']);
		if("2"==status){  //已经生效的押品价格
			alert("该押品目录还未生效，不能做失效操作！");
			return false;
		}
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
				if("Y" == flag){
					alert("修改成功！");
					
					doQuery();  //异步刷新列表
				}else{
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("修改失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var paramStr = IqpMortCatalogManaList._obj.getParamStr(['catalog_no','catalog_name']);
		if (paramStr != null) {
			var status = IqpMortCatalogManaList._obj.getParamValue(['status']);
			if("0"==status){  //如果已经是无效状态
				alert("该押品目录已是【无效】状态！");
				return false;
			}
			var url = '<emp:url action="updateCatalogManaStatus.do"/>?'+ encodeURI(paramStr)+"&status=0";
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}

	/*
	function onLoad(){
		//清除状态中的未完成状态 
		var options = IqpMortCatalogMana.status._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "2"){  //清除未完成状态
				options.remove(i);
			}
		}
	}
	*/
	
	//预览产品树
	function doSubmitIqpMortCatalogMana(){
		var url = '<emp:url action="showCatalogManaTree.do"/>?isMin=N&close=close';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="IqpMortCatalogManaGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpMortCatalogMana.catalog_name" label="目录名称" />
		<emp:select id="IqpMortCatalogMana.attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
		<emp:select id="IqpMortCatalogMana.status" label="状态" dictname="STD_ZB_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpMortCatalogManaPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpMortCatalogManaPage" label="修改" op="update"/>
		<emp:button id="deleteIqpMortCatalogMana" label="删除" op="remove"/>
		<emp:button id="viewIqpMortCatalogMana" label="查看" op="view"/>
		<emp:button id="inureIqpMortCatalogMana" label="生效" op="effect"/>
		<emp:button id="logOutIqpMortCatalogMana" label="失效" op="abate"/>
		<emp:button id="submitIqpMortCatalogMana" label="预览押品类型树" op="show" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		
	</div>

	<emp:table icollName="IqpMortCatalogManaList" pageMode="true" url="pageIqpMortCatalogManaQuery.do">
		<emp:text id="catalog_no" label="目录编号" />
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
		<emp:text id="catalog_lvl" label="押品目录层级" dataType="Int"/>
		<emp:text id="imn_rate" label="基准质押率" dataType="Percent" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    