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
		IqpEquipInfo._toForm(form);
		IqpEquipInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpEquipInfoPage() {
		var paramStr = IqpEquipInfoList._obj.getParamStr(['equip_pk']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpEquipInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpEquipInfo() {
		var paramStr = IqpEquipInfoList._obj.getParamStr(['equip_pk']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpEquipInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpEquipInfoPage() {
		var serno = '<%=serno%>';
		var url = '<emp:url action="getIqpEquipInfoAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpEquipInfo() {
		var paramStr = IqpEquipInfoList._obj.getParamStr(['equip_pk']);
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
					var url = '<emp:url action="deleteIqpEquipInfoRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		} else {
			alert('请先选择一条记录！');
		}
	  }
	};
	
	function doReset(){
		page.dataGroups.IqpEquipInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpEquipInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpEquipInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpEquipInfo" label="删除" op="remove"/>
		<emp:actButton id="viewIqpEquipInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpEquipInfoList" pageMode="true" url="pageIqpEquipInfoQuery.do">
		<emp:text id="equip_name" label="设备名称" />
		<emp:text id="equip_model" label="设备型号" />
		<emp:text id="equip_unit_price" label="设备单价" dataType="Currency"/>
		<emp:text id="qnt" label="数量" />
		<emp:text id="pur_total_amt" label="购置总价" dataType="Currency"/>
		<emp:text id="produce_date" label="出厂日期" />
		<emp:text id="pur_date" label="购入日期" />
		<emp:text id="equip_pk" label="设备编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    