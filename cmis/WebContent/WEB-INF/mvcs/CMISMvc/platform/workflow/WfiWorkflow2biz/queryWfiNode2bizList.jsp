<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String op = request.getParameter("op");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiNode2biz._toForm(form);
		WfiNode2bizList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiNode2bizPage() {
		var paramStr = WfiNode2bizList._obj.getParamStr(['pk1','nodeid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiNode2bizUpdatePage.do"/>?'+paramStr+'&wfsign=<%=request.getParameter("wfsign")%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiNode2biz() {
		var paramStr = WfiNode2bizList._obj.getParamStr(['pk1','nodeid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiNode2bizViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiNode2bizPage() {
		var url = '<emp:url action="getWfiNode2bizAddPage.do"/>?pk1=<%=request.getParameter("pk1")%>&wfsign=<%=request.getParameter("wfsign")%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiNode2biz() {
		var paramStr = WfiNode2bizList._obj.getParamStr(['pk1','nodeid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiNode2bizRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				doSubmitAsyn(url);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiNode2bizGroup.reset();
	};
	
	/*--user code begin--*/
		
	function doSubmitAsyn(url) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 1) {
					alert('操作成功！');
					var url = '<emp:url action="queryWfiNode2bizList.do"/>?pk1=${context.pk1}&wfsign=${context.wfsign}';
					url = EMPTools.encodeURI(url);
					window.location = url;
				} else {
					alert('操作失败！'+flag);
				}
			}catch(e) {
				alert(o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('get', url, callback, null);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<% if(!"view".equals(op)){%>
	<div align="left">
		<emp:button id="getAddWfiNode2bizPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiNode2bizPage" label="修改" op="update"/>
		<emp:button id="deleteWfiNode2biz" label="删除" op="remove"/>
		<%-- <emp:button id="viewWfiNode2biz" label="查看" op="view"/> --%>
	</div>
	<%} %>
	<emp:table icollName="WfiNode2bizList" pageMode="true" url="pageWfiNode2bizQuery.do" reqParams="pk1=${context.pk1 }">
		<emp:text id="nodeid" label="节点ID" />
		<emp:text id="nodename" label="节点名称" />
		<emp:text id="app_url" label="节点自定义申请信息页面" />
		<emp:text id="biz_url" label="节点自定义业务要素修改页面" />
		<emp:text id="pk1" label="关联配置主键" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    