<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
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
		IqpRentInfo._toForm(form);
		IqpRentInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpRentInfoPage() {
		var paramStr = IqpRentInfoList._obj.getParamStr(['rent_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpRentInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpRentInfo() {
		var paramStr = IqpRentInfoList._obj.getParamStr(['rent_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpRentInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpRentInfoPage() {
		var url = '<emp:url action="getIqpRentInfoAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpRentInfo() {
		var paramStr = IqpRentInfoList._obj.getParamStr(['rent_serno']);
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
						var message = jsonstr.message;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert(message);
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
				var url = '<emp:url action="deleteIqpRentInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpRentInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:actButton id="getAddIqpRentInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpRentInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpRentInfo" label="删除" op="remove"/>
		<emp:actButton id="viewIqpRentInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpRentInfoList" pageMode="true" url="pageIqpRentInfoQuery.do">
		<emp:text id="lessee_name" label="承租人名称" />
		<emp:text id="lessee_cert_no" label="承租人证件号码" />
		<emp:text id="every_rent_amt" label="每期租金" dataType="Currency"/>
		<emp:text id="pld_amt" label="押金" dataType="Currency"/>
		<emp:text id="total_rent_amt" label="总租金" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="rent_serno" label="出租编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    