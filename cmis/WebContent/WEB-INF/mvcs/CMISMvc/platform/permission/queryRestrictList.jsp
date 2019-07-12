<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String resourceid = "";
	if(context.containsKey("resourceid")){
		resourceid = (String)context.getDataValue("resourceid");
	}
%>
<emp:page>
<html>
<head>
<title>记录级权限列表页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">
	function addBackFun(){
		window.parent.resTree.getRootNode().reload();
		//doClose();
	}
	
	//--------------新增记录集权限调用函数-------------
	function doAddRestrict(){
		var count = IqpBillDetailList._obj.recordCount;
		if(count == 0){
			var url = '<emp:url action="getRestrictAddPage.do"/>?resourceid=<%=resourceid %>';
			url = EMPTools.encodeURI(url);
			window.location = url; 
		}else {
			alert("一个资源只能配置一条记录集权限！");
		}
		
	};
	//--------------修改记录集权限调用函数-------------
	function doUpdateRestrict(){
		var paramStr = IqpBillDetailList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getRestrictupdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url; 
		} else {
			alert('请先选择一条记录！');
		}
	};
	//--------------删除记录集权限调用函数-------------
	function doDeleteRestrict(){
		var paramStr = IqpBillDetailList._obj.getParamStr(['pk1']);
		var resourceid = IqpBillDetailList._obj.getParamStr(['resourceid']);
		if (paramStr != null) {
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
						alert("删除成功!");
						var url = '<emp:url action="queryRestrictList.do"/>?'+resourceid;
						url = EMPTools.encodeURI(url);
						window.location = url;
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
			var url = '<emp:url action="deleteRestrict.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		} else {
			alert('请先选择一条记录！');
		}
	};	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>	
	<div align="left">
		<emp:button id="addRestrict" label="新增"/>
		<emp:button id="updateRestrict" label="修改" />
		<emp:button id="deleteRestrict" label="删除"/>
	</div>
	<emp:table icollName="IqpBillDetailList" pageMode="false" url="pageIqpBillDetailQuery.do">
		<emp:text id="pk1" label="主键" hidden="true"/>
		<emp:text id="resourceid" label="资源ID" />
		<emp:text id="cnname" label="资源名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
