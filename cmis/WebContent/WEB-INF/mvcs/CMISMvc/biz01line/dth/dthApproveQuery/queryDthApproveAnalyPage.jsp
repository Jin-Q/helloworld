<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>审批报备</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var result = ApproveAnaly._checkAll();
		if(result){
			var analy_type = ApproveAnaly.analy_type._getValue();
			var begin_date = ApproveAnaly.begin_date._getValue();
			var end_date = ApproveAnaly.end_date._getValue();
			var right_type = ApproveAnaly.right_type._getValue();
			var url = '<emp:url action="getDthApproveAnalyPage.do"/>?analy_type='+analy_type+'&begin_date='+begin_date+'&end_date='+end_date+'&right_type='+right_type;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};
		
	function doReset(){
		page.dataGroups.ApproveAnalyGroup.reset();
	};

	function checkDate(obj){
		var begin_date = ApproveAnaly.begin_date._getValue();
		var end_date = ApproveAnaly.end_date._getValue();

		if(begin_date != '' && end_date != '' ){
			if(begin_date > end_date){
				alert('查询起始日期应该小于查询终止日期');
				obj.value = "";
			}
		}
	};
	//XD150629048  分支机构报备 2015-08-27 Edited by FCL
	function initRightTyp(){
		for (var i = document.all('ApproveAnaly.right_type').options.length - 1; i > 0; i--) {
			if (document.all('ApproveAnaly.right_type').item(i).value == '03') {
				document.all('ApproveAnaly.right_type').options.remove(i);
			}
		}
	}
</script>
</head>

<body class="page_content" onload="initRightTyp();">

	<emp:form method="POST" action="getDthApproveAnalyPage.do" id="queryForm">
		<emp:gridLayout id="ApproveAnalyGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="ApproveAnaly.analy_type" label="报备类型" dictname="STD_APPROVE_ANALY" required="true"/>
			<emp:select id="ApproveAnaly.right_type" label="权限类型" dictname="STD_ZB_RIGHT_TYPE" required="true"/>
			<emp:date id="ApproveAnaly.begin_date" label="查询起始日期" onblur="checkDate(this)" />
			<emp:date id="ApproveAnaly.end_date" label="查询终止日期" onblur="checkDate(this)" />
		</emp:gridLayout>
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" />

</body>
</html>
</emp:page>