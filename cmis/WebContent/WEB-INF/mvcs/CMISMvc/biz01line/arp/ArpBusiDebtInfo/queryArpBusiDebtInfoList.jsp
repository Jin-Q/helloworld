<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	String cus_id = "";
	if(context.containsKey("cus_id")){
		cus_id = (String)context.getDataValue("cus_id");
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
		ArpBusiDebtInfo._toForm(form);
		ArpBusiDebtInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpBusiDebtInfoPage() {
		var paramStr = ArpBusiDebtInfoList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBusiDebtInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpBusiDebtInfo() {
		var paramStr = ArpBusiDebtInfoList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBusiDebtInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpBusiDebtInfoPage() {
		var serno='<%=serno%>';
		var cus_id='<%=cus_id%>';
		var url = '<emp:url action="getArpBusiDebtInfoAddPage.do"/>?serno='+serno+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpBusiDebtInfo() {
		var paramStr = ArpBusiDebtInfoList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("记录已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteArpBusiDebtInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpBusiDebtInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="getAddArpBusiDebtInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpBusiDebtInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpBusiDebtInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpBusiDebtInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpBusiDebtInfoList" pageMode="true" url="pageArpBusiDebtInfoQuery.do">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="debt_cap" label="抵偿本金" dataType="Currency"/>
		<emp:text id="debt_inner_int" label="抵偿表内利息" dataType="Currency"/>
		<emp:text id="debt_out_int" label="抵偿表外利息" dataType="Currency"/>
		<emp:text id="debt_other_expense" label="抵偿其他发生费用" dataType="Currency"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    