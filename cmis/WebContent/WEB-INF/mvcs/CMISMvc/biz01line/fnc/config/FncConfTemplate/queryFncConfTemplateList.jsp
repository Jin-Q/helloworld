<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" />


<script>

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncConfTemplate._toForm(form);
		FncConfTemplateList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncConfTemplatePage() {
		var paramStr = FncConfTemplateList._obj.getParamStr(['fnc_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfTemplateUpdatePage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncConfTemplate() {
		var paramStr = FncConfTemplateList._obj.getParamStr(['fnc_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfTemplateViewPage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncConfTemplatePage() {
		var url = '<emp:url action="getFncConfTemplateAddPage.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncConfTemplate() {
		var paramStr = FncConfTemplateList._obj.getParamStr(['fnc_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncConfTemplateRecord.do"/>?'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncConfTemplateGroup.reset();
	};

	//生成财报页面校验js
	function doCreatJs(){
		var paramStr = FncConfTemplateList._obj.getParamStr(['fnc_id']);
		if (paramStr != null) {
			var url = '<emp:url action="genFncPage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			EMPTools.mask();
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("生成失败！");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert("生成成功！");							
					}else{
						alert("生成失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("生成失败，请联系管理员！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div id="FncConfTemplateGroup" class="emp_group_div">
	<emp:gridLayout id="FncConfTemplateGroup" maxColumn="2" title="输入查询条件">
		<emp:text id="FncConfTemplate.fnc_id" label="报表编号" />
		<emp:text id="FncConfTemplate.fnc_name" label="财务报表类型" />
	</emp:gridLayout>
	</div>
	<jsp:include page="/queryInclude.jsp" />
	
	<div align="left">
		<emp:button id="getAddFncConfTemplatePage" label="新增" op="add"/>
		<emp:button id="getUpdateFncConfTemplatePage" label="修改" op="update"/>
		<emp:button id="deleteFncConfTemplate" label="删除" op="remove"/>
		<emp:button id="viewFncConfTemplate" label="查看" op="view"/>
		
		<emp:button id="creatJs" label="生成js"/>
	</div>

	<emp:table icollName="FncConfTemplateList" pageMode="true" url="pageFncConfTemplateQuery.do">
		<emp:text id="fnc_id" label="报表编号" />
		<emp:text id="fnc_name" label="财务报表类型" />
		<emp:text id="fnc_bs_style_id" label="资产负债表样式编号" />
		<emp:text id="fnc_pl_style_id" label="损益表(收入支出总表)" />
		<emp:text id="fnc_cf_style_id" label="现金流量(事业支出明细表)" />
		<emp:text id="fnc_fi_style_id" label="财务指标" />
	</emp:table>
	
</body>
</html>
</emp:page>
    