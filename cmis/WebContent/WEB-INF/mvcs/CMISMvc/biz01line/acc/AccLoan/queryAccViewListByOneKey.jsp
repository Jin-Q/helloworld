<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cus_id = "";
	if(context.containsKey("cus_id")){
		cus_id = (String)context.getDataValue("cus_id");
}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccView._toForm(form);
		AccViewList._obj.ajaxQuery(null,form);
	};
	
	
	function doGetAccView(){
		var paramStr = AccViewList._obj.getParamStr(['bill_no','cont_no']);
		if (paramStr != null) {
			var table_model = AccViewList._obj.getSelectedData()[0].table_model._getValue();
			var url = '<emp:url action="getAccViewPage.do"/>?'+paramStr+'&isHaveButton=not&table_model='+table_model+"&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function returnPrdId(data){
		AccView.prd_id._setValue(data.id);
		AccView.prd_id_displayname._setValue(data.label); 
	};
	function doReturn(){
		var cusId = '<%=cus_id%>';
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cusId;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doClose(){
		window.close();
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccView.bill_no" label="借据编号" />
			<emp:text id="AccView.cont_no" label="合同编号" />
			<emp:pop id="AccView.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
			 <emp:text id="AccView.prd_id" label="产品编号"  hidden="true" />
	</emp:gridLayout> 	
	<jsp:include page="/queryInclude.jsp" flush="true" />	
	<div align="left">
		<emp:button id="getAccView" label="查看" op="view"/>
	</div>
	<emp:table icollName="AccViewList" pageMode="true" url="pageQueryAccViewListByOneKey.do" reqParams="cus_id=${context.cus_id}&table_model=${context.table_model}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="bill_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="bill_bal" label="贷款余额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="table_model" label="台账表名" hidden="true"/>
	</emp:table>
	<br>
	<div align="center">
		<br>
		<emp:button id="return" label="返回" />
		<emp:button id="close" label="关闭页面" />
	</div>
</body>
</html>
</emp:page>
    