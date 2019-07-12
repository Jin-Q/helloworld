<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String act = "";
	if(context.containsKey("act")){
		act = (String)context.getDataValue("act");
	}
	
%>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdatePrdPvRiskScenePage() {
		var paramStr = PrdPvRiskSceneList._obj.getParamStr(['prevent_id','wfid']);
		if (paramStr!=null) {
			var url = '<emp:url action="getPrdPreventRiskPrdPvRiskSceneUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdPvRiskScenePage(){
		var prevent_id = window.parent.window.PrdPreventRisk.prevent_id._getValue();
		var url = '<emp:url action="getPrdPreventRiskPrdPvRiskSceneAddPage.do"/>?prevent_id='+prevent_id;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeletePrdPvRiskScene() {
		var paramStr = PrdPvRiskSceneList._obj.getParamStr(['prevent_id','wfid']);
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
							alert("异步发生异常！");
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
				var url = '<emp:url action="deletePrdPreventRiskPrdPvRiskSceneRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdPvRiskScene() {
		var paramStr = PrdPvRiskSceneList._obj.getParamStr(['prevent_id','wfid']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryPrdPreventRiskPrdPvRiskSceneDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
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
	<% 
		if(!act.equals("view")){
	%>
		<emp:button id="getAddPrdPvRiskScenePage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdPvRiskScenePage" label="修改" op="update"/>
		<emp:button id="deletePrdPvRiskScene" label="删除" op="remove"/>
	<% 
		}
	%>
		<emp:button id="viewPrdPvRiskScene" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="PrdPvRiskSceneList" pageMode="true" url="pagePrdPreventRiskPrdPvRiskSceneQuery.do" reqParams="PrdPreventRisk.prevent_id=$PrdPreventRisk.prevent_id;">
		<emp:text id="prevent_id" label="方案编号" />
		<emp:text id="wfid" label="流程编号" />
		<emp:text id="wfiname" label="流程名称" />
	</emp:table>
				
</body>
</html>
</emp:page>