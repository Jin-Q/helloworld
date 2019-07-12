<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% 
	String serno = request.getParameter("serno");
	//String cus_id = request.getParameter("cus_id");
%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PubBailInfo._toForm(form);
		PubBailInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PubBailInfoGroup.reset();
	};
	
	/*--user code begin--*/
	
	function doView() {
		var paramStr = PubBailInfoList._obj.getParamStr(['serno','cus_id','bail_acct_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getPubBailInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
	 function returnCus(data){
		 PubBailInfo.cus_id._setValue(data.cus_id._getValue());
		 PubBailInfo.cus_name._setValue(data.cus_name._getValue());
	  };
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="PubBailInfoGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="PubBailInfo.cont_no" label="合同编号" />
			<emp:pop  id="PubBailInfo.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
	        <emp:text id="PubBailInfo.bail_acct_no" label="保证金账号"   />
	        <emp:text id="PubBailInfo.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />   
	
	<div align="left">
		<emp:button id="view" label="查看" op="view"/>
	</div>

	<emp:table icollName="PubBailInfoList" pageMode="true" url="pagePubBailInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bail_acct_no" label="保证金账号" />
		<emp:text id="security_rate" label="保证金比例" dataType="Percent"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="rate" label="利率" dataType="Rate"/>
		<emp:text id="dep_term" label="存期" dictname="STD_BAIL_DEP_TERM"/>
		<emp:text id="open_org_displayname" label="开户机构" />
		<emp:text id="open_org" label="开户机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    