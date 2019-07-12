<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAbsBatchMng._toForm(form);
		IqpAbsBatchMngList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.IqpAbsBatchMngGroup.reset();
	};
	
	function doReturnMethod(){
		var data = IqpAbsBatchMngList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpAbsBatchMngGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAbsBatchMng.batch_no" label="批次号" />
			<emp:text id="IqpAbsBatchMng.batch_name" label="证券化批次名称"/>
		    <emp:text id="IqpAbsBatchMng.trust_org_no" label="受托机构名称"/>
	        <emp:select id="IqpAbsBatchMng.is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO"/>
	        <emp:text id="IqpAbsBatchMng.input_id" label="操作人员" />
	        <emp:date id="IqpAbsBatchMng.trust_date" label="信托成立日" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	 <emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="IqpAbsBatchMngList" pageMode="true" url="queryAllAbsBatchInfoPopQuery.do" >
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="batch_name" label="证券化批次名称" />
		<emp:text id="trust_org_no" label="受托机构名称" />
		<emp:text id="trust_date" label="信托成立日" />
		<emp:text id="input_id" label="操作人员" />
		<emp:text id="acct_status" label="账务状态" dictname="STD_ABS_ACCOUNT_STATUS"/>		

		<emp:text id="cash_date" label="最后兑付日期" hidden="true"/>
		<emp:text id="input_br_id" label="操作机构" hidden="true"/>
		<emp:text id="update_date" label="修改日期" hidden="true"/>
		<emp:text id="manager_br_id" label="所属行社" hidden="true"/>
		<emp:text id="fund_acct_no" label="信托财产资金账号" hidden="true"/>
		<emp:text id="fund_acct_name" label="信托财产资金户名" hidden="true"/>
		<emp:text id="keep_org_no" label="资金保管机构行号" hidden="true"/>
		<emp:text id="keep_org_name" label="资金保管机构行名" hidden="true"/>
		<emp:text id="is_this_org_service" label="是否本机构服务" hidden="true"/>
		<emp:text id="service_org_no" label="贷款服务机构行号" hidden="true"/>
		<emp:text id="service_org_name" label="贷款服务机构行名" hidden="true"/>
		<emp:text id="service_fee_rate" label="服务费率" hidden="true"/>
		<emp:text id="service_fee" label="总服务费" hidden="true"/>
		<emp:text id="trust_org_cert_type" label="受托机构证件类型" hidden="true"/>
		<emp:text id="trust_org_cert_no" label="受托机构证件号码" hidden="true"/>
		<emp:text id="mark" label="备注" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    