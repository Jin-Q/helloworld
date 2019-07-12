<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doOnLoad() {

    showCertTyp(CusCom.cert_type, 'com');
};
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusCom._toForm(form);
		CusComList._obj.ajaxQuery(null,form);
	};
	
	function doSelect(){	
		var data = CusComList._obj.getSelectedData();

		if (data != null) {
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
			
		} else {
			alert('请先选择一条记录！');
		}
	};		
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusComGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusCom.cus_id" label="客户码" />
			<emp:select id="CusCom.cert_type" label="开户证件类型" dictname='STD_ZB_CERT_TYP'/>
			<emp:text id="CusCom.cert_code" label="开户证件号码" />
	</emp:gridLayout>
	
	<table width="100%"  align="center"  class="searchTb">
		<tr>
			<td colspan="4"/>
			<div align="center">
				<emp:button id="query" label="查询"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<emp:button id="reset" label="重置"/>
			</div>
		</tr>
	</table>
	<button onclick="doSelect()">选取返回</button>
	<emp:table icollName="CusComList" pageMode="true" url="pageCusComQuery.do" reqParams="CusCom.cus_status=$CusCom.cus_status;">
		<emp:text id="cus_id" label="客户代码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="开户证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="开户证件号码" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
		<emp:text id="cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK"/>
		<emp:text id="cust_mgr" label="主管客户经理" />
		<emp:text id="main_br_id" label="主管机构" />
		<emp:text id="loan_card_id" label="贷款卡编号" hidden="true" />
		<emp:text id="cus_status" label="状态" hidden="true" />
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    