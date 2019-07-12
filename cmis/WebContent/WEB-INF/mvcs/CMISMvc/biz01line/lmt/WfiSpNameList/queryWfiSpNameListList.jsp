<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
function doQuery(){
	var form = document.getElementById('queryForm');
	WfiSpNameList._toForm(form);
	WfiSpNameListList._obj.ajaxQuery(null,form);
};
function doReset(){
	page.dataGroups.WfiSpNameListGroup.reset();
};
//跳转到新增引导页
function doGetWfiSpNameListAddPage() {
	var url = '<emp:url action="getWfiSpNameListAddPage.do"/>?op=add';
	url = EMPTools.encodeURI(url);
	window.location = url;
};
//异步删除
function doDeleteWfiSpNameListRecord(){
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

	var paramStr = WfiSpNameListList._obj.getParamStr(['pk_id','cus_id']);
	if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiSpNameListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
	} else {
		alert('请先选择一条记录！');
	}
};
</script>
</head>
<body class="page_content">    
<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfiSpNameListGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="WfiSpNameList.cus_name" label="客户名称" />
		<emp:text id="WfiSpNameList.cus_id" label="客户码" hidden="true"/>
		<emp:pop id="WfiSpNameList.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getWfiSpNameListAddPage" label="新增" op="add"/>
		<emp:button id="deleteWfiSpNameListRecord" label="删除" op="remove"/>
	</div>
	<emp:table icollName="WfiSpNameListList" pageMode="true" url="pageWfiSpNameListQuery.do?type=app" statisticType="" >
		<emp:text id="pk_id" label="流水号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="sp_right_type" label="特别权限类型" dictname="STD_SP_RIGHT_TYPE" />
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
	</emp:table>
</body>
</html>
</emp:page>
    