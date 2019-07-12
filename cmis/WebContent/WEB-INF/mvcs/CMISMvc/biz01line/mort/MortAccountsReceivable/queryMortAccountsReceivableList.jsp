<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortAccountsReceivable._toForm(form);
		MortAccountsReceivableList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortAccountsReceivablePage() {
		var paramStr = MortAccountsReceivableList._obj.getParamStr(['debt_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortAccountsReceivableUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortAccountsReceivable() {
		var paramStr = MortAccountsReceivableList._obj.getParamStr(['debt_id','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortAccountsReceivableViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortAccountsReceivablePage() {
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getMortAccountsReceivableAddPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortAccountsReceivable() {
		var paramStr = MortAccountsReceivableList._obj.getParamStr(['debt_id']);
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
							alert("已删除!");
							window.location.reload();
						}else{
							alert("删除失败!");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteMortAccountsReceivableRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortAccountsReceivableGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortAccountsReceivableGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortAccountsReceivable.guaranty_no" label="押品编号" />
			<emp:text id="MortAccountsReceivable.buy_cus_name" label="买方客户名称" />
			<emp:text id="MortAccountsReceivable.sel_cus_name" label="卖方客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<%if(op.equals("update")||op.equals("add")) {%>
		<emp:button id="getAddMortAccountsReceivablePage" label="新增" op="add"/>
		<emp:button id="getUpdateMortAccountsReceivablePage" label="修改" op="update"/>
		<emp:button id="deleteMortAccountsReceivable" label="删除" op="remove"/>
		<%} %>
		<emp:button id="viewMortAccountsReceivable" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortAccountsReceivableList" pageMode="true" url="pageMortAccountsReceivableQuery.do" reqParams="guaranty_no=${context.guaranty_no}">
		<emp:text id="debt_id" label="应收账款编号" hidden="false"/>
		<emp:text id="guaranty_no" label="押品编号" hidden="false"/>
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="商业合同编号" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		<emp:text id="bond_pay_date" label="付款日期" />
		<emp:text id="status" label="状态" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    