<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String catalogId = (String)context.getDataValue("catalog_id");
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspCatItemRel._toForm(form);
		PspCatItemRelList._obj.ajaxQuery(null,form);
	};

	function doViewPspCatItemRel() {
		var paramStr = PspCatItemRelList._obj.getParamStr(['catalog_id','item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCatItemRelViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetAddPspCatItemRelPage() {
		var url = '<emp:url action="getPspCatItemRelAddPage.do"/>?catalog_id=<%=catalogId %>';
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeletePspCatItemRel() {
		var paramStr = PspCatItemRelList._obj.getParamStr(['catalog_id','item_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除失败！");
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

				var url = '<emp:url action="deletePspCatItemRelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCatItemRelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:button id="getAddPspCatItemRelPage" label="新增" op="add"/>
		<emp:button id="deletePspCatItemRel" label="删除" op="remove"/>
		<emp:button id="viewPspCatItemRel" label="查看" op="view"/>
	</div>


	<emp:table icollName="PspCatItemRelList" pageMode="true" url="pagePspCatItemRelQuery.do?catalog_id=${context.catalog_id}">
		<emp:text id="catalog_id" label="目录编号" hidden="true"/>
		<emp:text id="catalog_id_displayname" label="目录名称" hidden="true"/>
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_id_displayname" label="项目名称" />
		<emp:text id="seq" label="排序" />
	</emp:table>
	
</body>
</html>
</emp:page>
    