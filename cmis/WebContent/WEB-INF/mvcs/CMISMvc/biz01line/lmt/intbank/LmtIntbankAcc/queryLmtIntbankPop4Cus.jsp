<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doViewLmtIntbankAcc(){
		var paramStr = LmtIntbankAccList._obj.getParamStr(['serno','cus_id','agr_no']);
		if (paramStr != null) {
			var obj = LmtIntbankAccList._obj.getSelectedData();
			var odd_amt = obj[0].odd_amt._getValue();
			var url = '<emp:url action="queryLmtIntbankAccDetail.do"/>?'+paramStr+"&odd_amt="+odd_amt+"flag=close&menuIdTab=LmtIntbankAcc";
			url = EMPTools.encodeURI(url);
			window.open(url,'LmtWindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIntbankAcc._toForm(form);
		LmtIntbankAccList._obj.ajaxQuery(null,form);
	};
	
	
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<div align="left">
	    <emp:button id="viewLmtIntbankAcc" label="查看" op="view"/>
		
	</div>
	<emp:table icollName="LmtIntbankAccList" pageMode="true" url="pageLmtIntbankPop4Cus.do" reqParams="cus_id=${context.cus_id}">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称"/>
		<emp:text id="crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
		<emp:text id="lmt_amt" label="授信总额(元)" dataType="Currency" />
		<emp:text id="froze_amt" label="冻结金额(元)" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日期"/>
		<emp:text id="end_date" label="授信到期日期"/>
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
		<emp:text id="user_amt" label="已用额度(元)" dataType="Currency" hidden="true"/>
		<emp:text id="odd_amt" label="剩余额度(元)" dataType="Currency" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>