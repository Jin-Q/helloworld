<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	String is_elec_bill="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}  
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
		IqpAppPsaleContGood._toForm(form);
		IqpAppPsaleContGoodList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppPsaleContGoodPage() {
		var paramStr = IqpAppPsaleContGoodList._obj.getParamStr(['psale_cont','commo_name','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppPsaleContGoodUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppPsaleContGood() {
		var paramStr = IqpAppPsaleContGoodList._obj.getParamStr(['psale_cont','commo_name','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppPsaleContGoodViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppPsaleContGoodPage() {
		var psale_cont = window.parent.window.IqpAppPsaleCont.psale_cont._getValue();
		var serno = window.parent.window.IqpAppPsaleCont.serno._getValue();
		var url = '<emp:url action="getIqpAppPsaleContGoodAddPage.do"/>?psale_cont='+psale_cont+'&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
	};
	
	function doDeleteIqpAppPsaleContGood() {
		var paramStr = IqpAppPsaleContGoodList._obj.getParamStr(['psale_cont','commo_name','serno']);
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
						var msg = jsonstr.msg;
						if(flag == "success"){
							window.parent.location.reload();   
						}else {
							alert("删除失败！"+msg);   
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
				var url = '<emp:url action="deleteIqpAppPsaleContGoodRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppPsaleContGoodGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
	<%if(!op.equals("view")){%> 
		<emp:button id="getAddIqpAppPsaleContGoodPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAppPsaleContGoodPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAppPsaleContGood" label="删除" op="remove"/>
	<%} %>
		<emp:button id="viewIqpAppPsaleContGood" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAppPsaleContGoodList" pageMode="true" url="pageIqpAppPsaleContGoodQuery.do" reqParams="op=${context.op}&psale_cont=${context.psale_cont}&serno=${context.serno}">
		<emp:text id="serno" label="业务流水号" hidden="true"/>
		<emp:text id="commo_name" label="商品名称" hidden="true"/>
		<emp:text id="psale_cont" label="协议号" hidden="true"/>
		<emp:text id="commo_name_displayname" label="商品名称" />
		<emp:text id="qnt" label="数量" />
		<emp:text id="unit_price" label="单价（元）" dataType="Currency"/>
		<emp:text id="total" label="总价值（元）" dataType="Currency"/>
		<emp:text id="psale_cont" label="购销合同编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    