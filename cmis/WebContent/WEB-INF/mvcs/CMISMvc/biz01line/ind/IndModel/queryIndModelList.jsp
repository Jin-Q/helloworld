<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddIndModelPage(){
		var url = '<emp:url action="getIndModelAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIndModel(){		
		var paramStr = IndModelList._obj.getParamStr(['model_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndModelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateIndModelPage(){
		var paramStr = IndModelList._obj.getParamStr(['model_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIndModelUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndModel(){
		var paramStr = IndModelList._obj.getParamStr(['model_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryIndModelDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IndModel._toForm(form);
		IndModelList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IndModelGroup.reset();
	};
	
	/*--user code begin--*/
		function doGenJspFile(){
			var data = new Date();
			var paramStr = IndModelList._obj.getParamStr(['model_no']);
			   if (paramStr != null) {
				var url = '<emp:url action="genJspFileOp.do"/>&'+paramStr+'&date='+data;
				url = EMPTools.encodeURI(url);    
				var handleSuccess = function(o){ EMPTools.unmask();
					try{ 
				     	var result = eval("("+o.responseText+")");
					}catch(e){
						alert("页面生成失败,错误信息:"+e.message);
					}
				     alert(result.genResult);
				};
				var handleFailure = function(o){ EMPTools.unmask();	
					alert(o);
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
				} else {
					alert('请先选择一条记录！');
				}
	};

	function doModelCopy(){
		var paramStr = IndModelList._obj.getParamStr(['model_no']);
		if (paramStr != null) {
			var url = '<emp:url action="addModelCopy.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IndModelGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IndModel.model_no" label="模型编号" />
			<emp:text id="IndModel.model_name" label="模型名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddIndModelPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndModelPage" label="修改" op="update"/>
		<emp:button id="deleteIndModel" label="删除" op="remove"/>
		<emp:button id="viewIndModel" label="查看" op="view"/>
		<emp:button id="genJspFile" label="生成jsp页面" op="genjsp"/>
		<!--  emp:button id="modelCopy" label="复制模型" op="modelcp" / -->
	</div>
	<emp:table icollName="IndModelList" pageMode="true" url="pageIndModelQuery.do">
		<emp:text id="model_no" label="模型编号" />
		<emp:text id="model_name" label="模型名称" />
		<emp:select id="biz_belg" label="业务所属" dictname="STD_ZB_BIZ_BELG"/>
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		
	</emp:table>
</body>
</html>
</emp:page>