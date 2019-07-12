<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String po_no = context.getDataValue("po_no").toString();
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBuscontInfo._toForm(form);
		IqpBuscontInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.IqpBuscontInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = IqpBuscontInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect()
	{
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">

	<form  method="POST" action="#" id="queryForm">
	</form>

 	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="IqpBuscontInfoList" pageMode="false" url="pageIqpBuscontInfoQuery.do">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="tcont_no" label="贸易合同编号" hidden="false"/>
		<emp:text id="sup_mat_cprt" label="供货单位" />
		<emp:text id="tcont_amt" label="贸易合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="贸易合同起始日" />
		<emp:text id="end_date" label="贸易合同到期日" />
		
		<emp:text id="trade_detail" label="贸易交易内容" hidden="true"/>
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
	
</body>
</html>
</emp:page>
    