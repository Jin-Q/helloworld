<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspDunningTaskDivis._toForm(form);
		PspDunningTaskDivisList._obj.ajaxQuery(null,form);
	};

	//登记
/*	function doRegiPspDunningTaskPage() {
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="regiPspDunningTaskRecord.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};*/

	function doRegiPspDunningTaskPage() {
		var accNo = PspDunningTaskDivisList._obj.getParamValue(['bill_no']);
		if (accNo != null) {
			var url = '<emp:url action="regiPspDunningTaskRecord.do"/>?bill_no='+accNo+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看
/*	function doViewRegiPspDunningTask() {
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="regiPspDunningTaskRecord.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};*/
	function doViewRegiPspDunningTask(){
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var accNo = PspDunningTaskDivisList._obj.getParamValue(['bill_no']);
			var du_type = PspDunningTaskDivisList._obj.getParamValue(['du_type']);
			if(accNo==null||accNo==''){
				alert('借据编号为空！');
			}else{
				var url = "";
				if(du_type=="垫款"){
					url = "<emp:url action='getAccViewPage.do'/>&bill_no="+accNo+"&isHaveButton=not&isAccPad=is";
				}else{
					url = "<emp:url action='getAccViewPage.do'/>&bill_no="+accNo+"&isHaveButton=not";
				}
			    url=encodeURI(url); 
		      	window.open(url,'viewwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}
		} else {
			alert('请先选择一条记录！');
		}
		
	}
	
	function doReset(){
		page.dataGroups.PspDunningTaskDivisGroup.reset();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspDunningTaskDivisGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspDunningTaskDivis.cus_id" label="客户码" />
			<emp:text id="PspDunningTaskDivis.acc_no" label="借据编号" />
			<emp:text id="PspDunningTaskDivis.cont_no" label="合同编号" />
			<emp:text id="PspDunningTaskDivis.serno" label="业务编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="regiPspDunningTaskPage" label="登记" op="update"/>
		<emp:button id="viewRegiPspDunningTask" label="查看" op="view"/>
	</div>
<!-- 
	<emp:table icollName="PspDunningTaskDivisList" pageMode="true" url="pageRegiPspDunningTaskQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="acc_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="need_end_date" label="要求完成日期" />
		<emp:text id="exe_id" label="任务执行人" hidden="true"/>
		<emp:text id="divis_id" label="任务分配人" hidden="true" />
		<emp:text id="exe_id_displayname" label="任务执行人" />
		<emp:text id="divis_id_displayname" label="任务分配人" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table> -->
	
	<emp:table icollName="PspDunningTaskDivisList" pageMode="true" url="pageRegiPspDunningTaskQuery.do">
		<emp:text id="du_type" label="逾期类型" />
		<emp:text id="manager_br_id" label="机构编号" hidden="true"/>
		<emp:text id="manager_br_name" label="机构名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="loan_amt" label="贷/垫款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷/垫款余额" dataType="Currency" />
		<emp:text id="distr_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="inner_owe_int" label="表内欠息" />
		<emp:text id="out_owe_int" label="表外欠息" />
		<emp:text id="five_class" label="五级分类" />
	</emp:table>
	
</body>
</html>
</emp:page>
    