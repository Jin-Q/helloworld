<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpMortValueMana._toForm(form);
		IqpMortValueManaList._obj.ajaxQuery(null,form);
	};
	//修改
	function doGetUpdateIqpMortValueManaPage() {
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			var status = IqpMortValueManaList._obj.getParamValue(['status']);
			if("1"==status){  //已经生效的押品价格
				alert("该押品价格信息已经生效，不能修改！");
				return false;
			}
			var url = '<emp:url action="getIqpMortValueManaUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	//查看
	function doViewIqpMortValueMana() {
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpMortValueManaViewPage.do"/>?'+paramStr+"&isAdj=Y&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//新增
	function doGetAddIqpMortValueManaPage() {
		var url = '<emp:url action="getIqpMortValueManaAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//删除
	function doDeleteIqpMortValueMana() {
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			var status = IqpMortValueManaList._obj.getParamValue(['status']);
			if("1"==status){  //已经生效的押品价格
				alert("该押品价格信息已经生效，不能删除！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("已删除！");
							doQuery();  //异步刷新列表
						}else{
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
				var url = '<emp:url action="deleteIqpMortValueManaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpMortValueManaGroup.reset();
	};

	//生效
	function doInureIqpMortValueMana(){
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
		
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no','catalog_no']);
		if (paramStr != null) {
			var status = IqpMortValueManaList._obj.getParamValue(['status']);
			if("1"==status){  //如果已经是生效状态
				alert("该押品价格信息已是【生效】状态！");
				return false;
			}
			var url = '<emp:url action="updateValueManaStatus.do"/>?'+paramStr+"&status=1";
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	//失效
	function doLogOutIqpMortValueMana(){
		var status = IqpMortValueManaList._obj.getParamValue(['status']);
		if("2"==status){  //已经生效的押品价格
			alert("该押品价格信息还未生效，不能做失效操作！");
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
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no','catalog_no']);
		if (paramStr != null) {
			var status = IqpMortValueManaList._obj.getParamValue(['status']);
			if("0"==status){  //如果已经是无效状态
				alert("该押品价格信息已是【无效】状态！");
				return false;
			}
			var url = '<emp:url action="updateValueManaStatus.do"/>?'+paramStr+"&status=0";
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	}

	//价格调整
	function doAdjVlaueMana(){
		var paramStr = IqpMortValueManaList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			var status = IqpMortValueManaList._obj.getParamValue(['status']);
			if("0"==status||"2"==status){  //如果已经是未生效或者无效状态
				alert("该押品价格信息为非生效，不能做价格调整！");
				return false;
			}
			var url = '<emp:url action="getIqpMortValueManaUpdatePage.do"/>?'+paramStr+"&isAdj=Y";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*
	function onLoad(){
		//清除状态中的未完成状态 
		var options = IqpMortValueMana.status._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "2"){  //清除未完成状态
				options.remove(i);
			}
		}
	}
	*/
	function getReturnValueForGuarantyType(data){
		IqpMortValueMana.catalog_no_displayname._setValue(data.locate_cn);
		IqpMortValueMana.catalog_no._setValue(data.locate);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="IqpMortValueManaGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpMortValueMana.value_no" label="价格编号" />
		<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="押品类型名称" url="showCatalogManaTree.do?&isMin=N" returnMethod="getReturnValueForGuarantyType"/>
		<emp:select id="IqpMortValueMana.status" label="状态" dictname="STD_ZB_STATUS" />
		<emp:text id="IqpMortValueMana.catalog_no" label="押品类型编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpMortValueManaPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpMortValueManaPage" label="修改" op="update"/>
		<emp:button id="deleteIqpMortValueMana" label="删除" op="remove"/>
		<emp:button id="viewIqpMortValueMana" label="查看" op="view"/>
		<emp:button id="inureIqpMortValueMana" label="生效" op="beable"/>
		<emp:button id="logOutIqpMortValueMana" label="失效" op="disabl"/>
		<emp:button id="adjVlaueMana" label="价格调整" op="value"/>
	</div>

	<emp:table icollName="IqpMortValueManaList" pageMode="true" url="pageIqpMortValueManaQuery.do">
		<emp:text id="value_no" label="价格编号" />
		<emp:text id="catalog_no_displayname" label="押品类型名称" />
		<emp:text id="freq_unit" label="盯市频率单位" dictname="STD_ZX_FREQ_UNIT" />
		<emp:text id="freq" label="盯市频率" dataType="Int"/>
		<emp:text id="market_value" label="市场价" dataType="Currency" />
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT"/>
		<emp:text id="auth_date" label="价格核准时间" />
		<emp:text id="info_sour" label="价格信息来源" dictname="STD_ZB_INFO_SOUR" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
		<emp:text id="catalog_no" label="押品目录" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    