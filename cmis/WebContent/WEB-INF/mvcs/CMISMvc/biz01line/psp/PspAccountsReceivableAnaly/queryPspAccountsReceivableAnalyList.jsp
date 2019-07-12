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
		PspAccountsReceivableAnaly._toForm(form);
		PspAccountsReceivableAnalyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspAccountsReceivableAnalyPage() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAccountsReceivableAnalyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspAccountsReceivableAnaly() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAccountsReceivableAnalyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspAccountsReceivableAnalyPage() {
		var url = '<emp:url action="getPspAccountsReceivableAnalyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspAccountsReceivableAnaly() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspAccountsReceivableAnalyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspAccountsReceivableAnalyGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspAccountsReceivableAnalyPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspAccountsReceivableAnalyPage" label="修改" op="update"/>
		<emp:button id="deletePspAccountsReceivableAnaly" label="删除" op="remove"/>
		<emp:button id="viewPspAccountsReceivableAnaly" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspAccountsReceivableAnalyList" pageMode="false" url="pagePspAccountsReceivableAnalyQuery.do">
		<emp:text id="pk_id" label="主键" />
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="pyee" label="收款对象" />
		<emp:text id="rec_amt" label="应收款金额" />
		<emp:text id="hpp_date" label="发生日期" />
		<emp:text id="agreed_end_date" label="约定收款到期日期" />
		<emp:text id="paid_amt" label="实收款金额" />
		<emp:text id="avg_acc_day" label="平均账期（日）" />
		<emp:text id="tran_freq" label="交易频率（月）" />
		<emp:text id="analy_tran_amt" label="分析期内的交易金额" />
		<emp:text id="pay_type" label="支付方式" />
		<emp:text id="info_sour" label="信息来源" />
		<emp:text id="memo" label="其他信息" />
		<emp:text id="remarks" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记日期" />
		<emp:text id="input_date" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    