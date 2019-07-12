<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusDeptInItem._toForm(form);
		CusDeptInItemList._obj.ajaxQuery(null,form);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:table icollName="CusDeptInItemDetailList" pageMode="true" url="pageCusDeptInItemDetailsQuery.do" reqParams="acc_no=${context.acc_no}">
		
		<emp:text id="acc_no" label="账号" />
		<emp:text id="cus_deposit_typ" label="存款类型" />
		<emp:text id="cus_acc_blc" label="存款余额" />
		<emp:text id="cus_happen_dt" label="交易日期" />
		<emp:text id="cus_biz_typ" label="交易类型" />
		<emp:text id="cus_hpp_amt" label="交易金额" />
		<emp:text id="crt_usr_id" label="登记人" />
		<emp:text id="crt_usr_id_displayname" label="登记人" hidden="true" />
		<emp:text id="crt_dt" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    