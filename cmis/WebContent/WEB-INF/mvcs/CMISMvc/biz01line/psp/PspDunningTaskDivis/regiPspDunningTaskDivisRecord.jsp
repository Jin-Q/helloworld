<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<%
	request.setAttribute("canwrite","");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//选择客户POP框返回方法
	function onLoad(){
		PspDunningTaskDivis.cus_id._obj.addOneButton('view12','查看',viewCusInfo);//客户信息查看
		PspDunningTaskDivis.bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
	}
	
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+PspDunningTaskDivis.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function viewAccInfo(){
		var accNo = PspDunningTaskDivis.bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&bill_no="+accNo+"&isHaveButton=not";
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="催收信息" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updatePspDunningTaskDivisRecord.do" method="POST">
		<emp:gridLayout id="PspDunningTaskDivisGroup" title="借据信息" maxColumn="2">
			<emp:text id="PspDunningTaskDivis.serno" label="业务编号" maxlength="40" hidden="true" colSpan="2" />
			<emp:text id="PspDunningTaskDivis.cus_id" label="客户码" maxlength="80" readonly="true" required="true"/>
			<emp:text id="PspDunningTaskDivis.cus_id_displayname" label="客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" />
			<emp:text id="PspDunningTaskDivis.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:text id="PspDunningTaskDivis.bill_no" label="借据编号" maxlength="40" required="true" />
			
			<emp:text id="PspDunningTaskDivis.prd_id" label="产品编号" />
			<emp:text id="PspDunningTaskDivis.prd_id_displayname" label="产品名称" />
			<emp:text id="PspDunningTaskDivis.bill_amt" label="贷/垫款金额" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="PspDunningTaskDivis.bill_bal" label="贷/垫款余额" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
	<%/*		<emp:text id="PspDunningTaskDivis.acc_no" label="借据编号" maxlength="40" required="true" />
			<emp:date id="PspDunningTaskDivis.task_create_date" label="任务生成日期" required="true" />
			<emp:date id="PspDunningTaskDivis.need_end_date" label="要求完成日期" required="true" />
			<emp:text id="PspDunningTaskDivis.exe_id_displayname" label="任务执行人" required="true" />
			<emp:text id="PspDunningTaskDivis.exe_br_id_displayname" label="任务执行机构" required="true" />
			<emp:text id="PspDunningTaskDivis.exe_id" label="任务执行人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.exe_br_id" label="任务执行机构" maxlength="20" required="true" hidden="true"/>
			
			<emp:text id="PspDunningTaskDivis.divis_id_displayname" label="任务分配人" required="true" />
			<emp:text id="PspDunningTaskDivis.divis_br_id_displayname" label="任务分配机构" required="true" />
			<emp:text id="PspDunningTaskDivis.divis_id" label="任务分配人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.divis_br_id" label="任务分配机构" maxlength="20" required="true" hidden="true"/>	*/%>
			
			<emp:date id="PspDunningTaskDivis.overdue_date" label="逾期发生日期" />
		<%/*	<emp:date id="PspDunningTaskDivis.dunning_date" label="催收进入时间" required="true" />
			<emp:text id="PspDunningTaskDivis.totl_dunning_qnt" label="总逾期期数" maxlength="5" required="true" /> */%>
			<emp:text id="PspDunningTaskDivis.overdue" label="总逾期期数" maxlength="5" />
			<emp:select id="PspDunningTaskDivis.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:text id="PspDunningTaskDivis.high_dunning_qnt" label="最高连续逾期期数" maxlength="5" required="false" hidden="true"/>
			<emp:select id="PspDunningTaskDivis.twelve_class" label="十二级分类" hidden="true" dictname="STD_ZB_TWELVE_CLASS" />
			<emp:select id="PspDunningTaskDivis.dunning_status" label="催收状态" required="true" hidden="true"/>
			
			<emp:textarea id="PspDunningTaskDivis.memo" label="备注" maxlength="500" required="false" colSpan="2" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="PspDunningTaskDivis.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%//<emp:button id="reset" label="重置"/>%>
	</div>
</body>
</html>
</emp:page>
