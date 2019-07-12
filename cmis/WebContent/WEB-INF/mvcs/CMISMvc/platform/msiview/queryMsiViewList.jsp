<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var viewModel = "${context.viewModel}"  
		
		//因为emp:grouptable标签对分页支持有问题，所以不通过异步访问
		var modualName = document.getElementById("ModualService.modual_name").value;
		var methodName = document.getElementById("ModualService.method_name").value;
		var url = '<emp:url action="queryMsiViewList.do"/>?ModualService.modual_name='+modualName+'&ModualService.method_name='+methodName;
		
		url = EMPTools.encodeURI(url);
		window.location = url;
		
	};

	function doReset(){
		page.dataGroups.ModualServiceGroup.reset();
	};

	//缺省视图
	function doDefaultView(){
		var url = '<emp:url action="queryMsiViewList.do"/>?viewModel=default';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//模块视图：仅查看模块
	function doModualView(){
		var url = '<emp:url action="queryMsiViewList.do"/>?viewModel=modual';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//服务视图：查看当前系统所有服务
	function doServiceView(){
		var url = '<emp:url action="queryMsiViewList.do"/>?viewModel=method';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function doViewRelyOn(){
		var paramStr = ModualServiceList2._obj.getParamStr(['modual_id']);
		if(paramStr!=null){
			var url = '<emp:url action="viewModualRelyOn.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.showModalDialog(url);
			window.open(url);
		}else{
			alert("请选择一条记录!");
		}
	}
	
	function doLoad(){
		var viewModel = "${context.viewModel}"  
			
		if(viewModel == "default")
			document.getElementById("defualtView").style.display='';
		
		if(viewModel == "modual")
			 document.getElementById("modualView").style.display='';

		if(viewModel == "method")
			 document.getElementById("methodView").style.display='';
		 
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<form  method="POST" action="#" id="dataForm">
	</form>

	<emp:gridLayout id="ModualServiceGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ModualService.modual_name" label="模块名称" />
			<emp:text id="ModualService.method_name" label="服务名称" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="defaultView" label="缺省视图" op=""/>
		<emp:button id="modualView" label="模块视图" op=""/>
		<emp:button id="serviceView" label="服务视图" op=""/>
	</div>	
	
	<div id="defualtView" style="display:none">
		<emp:grouptable groupColumn="1" icollName="ModualServiceList" pageMode="false" url="pageMsiViewList.do">
			<emp:text id="modual_name" label="Modual"  hidden="true"/>
			<emp:text id="method_name" label="服务名称" />
			<emp:text id="method_desc" label="服务描述" />
			<emp:text id="in_param" label="输入参数" />
			<emp:text id="out_param" label="输出参数" />
			<emp:text id="example" label="调用示例" />
		</emp:grouptable>
	</div>
	
	

	<div id="modualView" style="display:none">
		<emp:table icollName="ModualServiceList2" pageMode="false" url="pageMsiViewList.do">
			<emp:text id="modual_id" label="模块ID"  hidden="false"/>
			<emp:text id="modual_name" label="模块名称"  hidden="false"/>
			<emp:link id="veiw_rely_on" label="依赖关系" onclick="doViewRelyOn(this)"  defvalue="查看"/>
		</emp:table>
	</div>
		
		
	<div id="methodView" style="display:none">
		<emp:table icollName="ModualServiceList3" pageMode="false" url="pageMsiViewList.do">
			<emp:text id="modual_name" label="模块名称"  hidden="false"/>
			<emp:text id="method_name" label="服务名称" />
			<emp:text id="method_desc" label="服务描述" />
			<emp:text id="in_param" label="输入参数" />
			<emp:text id="out_param" label="输出参数" />
			<emp:text id="example" label="调用示例" />
		</emp:table>
	</div>
	

	
</body>
</html>
</emp:page>
    