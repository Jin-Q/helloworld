<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = "";
	if(context.containsKey("type")){
		type = context.getDataValue("type").toString();
	}
	String serno = request.getParameter("serno");
%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtSubpayList._toForm(form);
		LmtSubpayListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtSubpayListPage() {
		var paramStr = LmtSubpayListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSubpayListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtSubpayList() {
		var paramStr = LmtSubpayListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSubpayListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtSubpayListPage() {
		var serno ='<%=serno%>';
		alert(serno);
		var url = '<emp:url action="getLmtSubpayListAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtSubpayList() {
		var paramStr = LmtSubpayListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtSubpayListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtSubpayListGroup.reset();
	};
	
	/*--user code begin--*/

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="代偿业务明细" id="main_tabs">
			<form  method="POST" action="#" id="queryForm">
			</form>
			<emp:gridLayout id="LmtSubpayListGroup" title="输入查询条件" maxColumn="2">
				<emp:text id="LmtSubpayList.subpay_bill_no" label="代偿业务借据号" />
			</emp:gridLayout>
			<jsp:include page="/queryInclude.jsp" flush="true" />
			<div align="left">
			<%if("query".equals(type)){%>		
				<emp:button id="viewLmtSubpayList" label="查看" op="view"/>
			<%}else{%>
				<emp:button id="getAddLmtSubpayListPage" label="新增" op="add"/>
				<emp:button id="getUpdateLmtSubpayListPage" label="修改" op="update"/>
				<emp:button id="deleteLmtSubpayList" label="删除" op="remove"/>
				<emp:button id="viewLmtSubpayList" label="查看" op="view"/>
			<% }%>
			</div>

			<emp:table icollName="LmtSubpayListList" pageMode="true" url="pageLmtSubpayListQuery.do">
				<emp:text id="serno" label="流水号"/>
				<emp:text id="biz_variet" label="业务品种" defvalue="贴现"/>
				<emp:text id="cont_no" label="合同编号" />
				<emp:text id="bill_no" label="借据编号"/>
				<emp:text id="guar_mode" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
				<emp:text id="bill_amt" label="借据金额" dataType="Currency" />
				<emp:text id="bill_bal" label="借据余额" dataType="Currency"/>
				<emp:text id="int_cumu" label="欠息累计" dataType="Currency"/>
				<emp:text id="subpay_cap" label="代偿本金" dataType="Currency"/>
				<emp:text id="subpay_int" label="代偿利息" dataType="Currency"/>
			</emp:table>
			
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
    