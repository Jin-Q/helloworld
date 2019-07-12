<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String schemeId = "";
if(context.containsKey("schemeid")){
	schemeId = (String)context.getDataValue("schemeid");
}
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
//-----政策资料关联方案场景列表增加行数-----
function doAddPrdPolcySchemeListApply() {
	PrdPolcySchemeList._obj._addRow();
	PrdPolcySchemeList._obj.recordCount +=1;
	addData1();
};
function addData1(){
	var recordCount = PrdPolcySchemeList._obj.recordCount;					//取总记录数
	PrdPolcySchemeList._obj.data[recordCount-1].prd_pk._setValue(PrdBasicinfo.prd_pk._getValue());
	PrdPolcySchemeList._obj.data[recordCount-1].fee_cde._obj._renderReadonly(false);
	PrdPolcySchemeList._obj.data[recordCount-1].fee_cde._obj.config.url='<emp:url action="queryPrdFeeTypListPop.do"/>&rowIndex='+(recordCount-1)+'&returnMethod=selFeeTyp' ;
	PrdPolcySchemeList._obj.data[recordCount-1].optType._setValue("add")		//向某个字段增加值
}
//----------------------------------------

//新增政策关联场景
function doAddPrdPolcySchemeFirst(){
	var url = '<emp:url action="getPrdPolcySchemeFirstAddPage.do"/>?schemeId=<%=schemeId %>';
	url = EMPTools.encodeURI(url);
	window.location = url;
}
//--修改节点记录--
function doUpdatePrdPolcySchemeFirst(){
	var paramStr = PrdSchemeSpaceRel._obj.getParamStr(['schemeid','flowid','effective']);
	if (paramStr != null) {
		var url = '<emp:url action="updatePrdPolcyRelRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择一条记录！');
	}
}
//--删除记录--
function doDelPrdPolcySchemeFirst(){
	var paramStr = PrdSchemeSpaceRel._obj.getParamStr(['schemeid','flowid','effective']);
	if (paramStr != null) {
		if(confirm("是否确认要删除？")){
			var url = '<emp:url action="delPrdPolcySchemeFirst.do"/>?doId=delete&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
			window.location.reload();
			//window.parent.location.reload();
		}
	} else {
		alert('请先选择一条记录！');
	}
}
//打开政策关联场景
function doUpdatePrdPolcySchemeFirstStart(){
	var paramStr = PrdSchemeSpaceRel._obj.getParamStr(['schemeid','flowid','effective']);
	if (paramStr != null) {
		var url = '<emp:url action="delPrdPolcySchemeFirst.do"/>?doId=start&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择一条记录！');
	}
}
//关闭政策关联场景
function doUpdatePrdPolcySchemeFirstEnd(){
	var paramStr = PrdSchemeSpaceRel._obj.getParamStr(['schemeid','flowid','effective']);
	if (paramStr != null) {
		if(confirm("是否确认要停用？")){
			var url = '<emp:url action="delPrdPolcySchemeFirst.do"/>?doId=close&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	} else {
		alert('请先选择一条记录！');
	}
}
</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="" method="POST"></emp:form>
	
	<!-- 政策资料方案关联场景列表 -->
		<div  class='emp_gridlayout_title'>政策资料方案关联场景设置</div>
		
		<div id="tempButton"  align="left" style="display:${param.optype}">
		  	<emp:button id="addPrdPolcySchemeFirst" label="新增"/>
		  	<emp:button id="updatePrdPolcySchemeFirst" label="修改" />
			<emp:button id="delPrdPolcySchemeFirst" label="删除" locked="false"/>
			<emp:button id="updatePrdPolcySchemeFirstStart" label="启用" />
			<emp:button id="updatePrdPolcySchemeFirstEnd" label="停用" />
		</div>
		<emp:table icollName="PrdSchemeSpaceRel" pageMode="false" url="">
			<emp:text id="schemeid" label="方案编号" maxlength="30" hidden="true" />
			<emp:select id="flowid" label="流程名称" dictname="FLOW_TYPE" />
			<emp:select id="effective" label="是否启用" dictname="STD_ZX_YES_NO"  />
		</emp:table>
</body>
</html>
</emp:page>