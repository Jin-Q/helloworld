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
		IqpChkMarketSet._toForm(form);
		IqpChkMarketSetList._obj.ajaxQuery(null,form);
	};
	
	function addTask() {
		var paramStr = IqpChkMarketSetList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			var url = '<emp:url action="addIqpChkMarketTaskRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpChkMarketSetGroup.reset();
	};

	function doCretask() {
		var paramStr = IqpChkMarketSetList._obj.getParamStr(['value_no']);
		if (paramStr != null) {
			checkIqpChkMarketTask();
		} else {
			alert('请先选择一条记录！');
		}
	}

	//生成任务前校验是否已存在未处理任务
	function checkIqpChkMarketTask(){
		var value_no = IqpChkMarketSetList._obj.getParamValue(['value_no']);
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
					addTask();
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

		var url="<emp:url action='checkIqpChkMarketTask.do'/>?value_no="+value_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpChkMarketSetGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpChkMarketSet.catalog_name" label="目录名称" />
		<emp:select id="IqpChkMarketSet.attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="cretask" label="生成任务" op="update"/>
	</div>

	<emp:table icollName="IqpChkMarketSetList" pageMode="true" url="pageIqpChkMarketSetQuery.do">
		<emp:text id="value_no" label="价格编号" hidden="false"/>
		<emp:text id="catalog_no" label="目录编号" hidden="true"/>
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="catalog_lvl" label="押品目录层级" />
		<emp:text id="attr_type" label="类型属性" dictname="STD_ZB_ATTR_TYPE"/>
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT"/>
		<emp:text id="market_value" label="市场价" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    