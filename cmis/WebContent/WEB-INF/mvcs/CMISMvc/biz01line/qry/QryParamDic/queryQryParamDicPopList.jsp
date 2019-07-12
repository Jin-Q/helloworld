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
		QryParamDic._toForm(form);
		QryParamDicList._obj.ajaxQuery(null,form);
	};


	function doSelect(methodName){
		//由于这个页面只有查询分析一个模块用到所以不做公共的接口
		if(window.opener){
			var data = QryParamDicList._obj.getSelectedData();
			var res = new Array();
			res[0] = data[0].param_dic_no._getValue();   //字典编号
			res[1] = data[0].name._getValue();			 //字典名称
			window.opener.returnDicNo(res);
			window.close();
		}else{
			alert("找不到父窗口");
		}
	}
	
	function doReset(){
		page.dataGroups.QryParamDicGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="QryParamDicGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="QryParamDic.name" label="字典名称"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="QryParamDicList" pageMode="true" url="pageQryParamDicQuery.do">
		<emp:text id="param_dic_no" label="参数选项字典编号" />
		<emp:text id="name" label="字典名称" />
		<emp:text id="par_dic_type" label="参数类型" dictname="STD_ZB_PAR_DIC_TYPE" />
		<emp:text id="opttype" label="参数选项字典编号" />
		<emp:text id="query_sql" label="查询SQL语句" hidden="true"/>
		<emp:text id="popname" label="POPNAME" />
	</emp:table>
	
	<div align="left">
		<emp:button id="select" label="选取并返回"/>
	</div>
	
</body>
</html>
</emp:page>
    