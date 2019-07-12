<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpTogetherRqstr._toForm(form);
		IqpTogetherRqstrList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewTogetherRqstr() {
		var paramStr = IqpTogetherRqstrList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getTogetherRqstrViewPage.do"/>?'+paramStr; 
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddTogetherRqstrPage() {
		var url = '<emp:url action="getTogetherRqstrAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteTogetherRqstr() {
		var paramStr = IqpTogetherRqstrList._obj.getParamStr(['serno','cus_id']);
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
								alert("删除成功!");
								var url = '<emp:url action="queryTogetherRqstrList.do"/>?serno='+'<%=serno%>';
								url = EMPTools.encodeURI(url);
								window.location = url; 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteTogetherRqstrRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpTogetherRqstrGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left"> 
		<emp:actButton id="getAddTogetherRqstrPage" label="新增" op="add"/>
		<emp:actButton id="deleteTogetherRqstr" label="删除" op="remove"/>
		<emp:actButton id="viewTogetherRqstr" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpTogetherRqstrList" pageMode="false" url="pageTogetherRqstrQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    