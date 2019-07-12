<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIndParaPage() {
		var paramStr = IndParaList._obj.getParamStr(['index_no','enname']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIndLibIndParaUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddIndParaPage(){
		var index_no = window.parent.window.IndLib.index_no._getValue();
		var url = '<emp:url action="getIndLibIndParaAddPage.do"/>?IndPara.index_no='+index_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteIndPara() {
		var paramStr = IndParaList._obj.getParamStr(['index_no','enname']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndLibIndParaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndPara() {
		var paramStr = IndParaList._obj.getParamStr(['index_no','enname']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndLibIndParaDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<div align="left">
		<emp:button id="getAddIndParaPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndParaPage" label="修改" op="update"/>
		<emp:button id="deleteIndPara" label="删除" op="remove"/>
		<emp:button id="viewIndPara" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="IndParaList" pageMode="true" url="pageIndLibIndParaQuery.do" reqParams="IndLib.index_no=$IndLib.index_no;">

		<emp:text id="index_no" label="指标编号" />
		<emp:text id="enname" label="参数英文名" />
		<emp:text id="para_cnname" label="参数中文名" />
		<emp:select id="para_val_type" label="参数值类型" dictname="STD_ZB_PARA_TYPE"/>
		<emp:select id="para_val_way" label="参数值来源" dictname="STD_ZB_SOURCE"  hidden="true"/>
	</emp:table>
				
</body>
</html>
</emp:page>