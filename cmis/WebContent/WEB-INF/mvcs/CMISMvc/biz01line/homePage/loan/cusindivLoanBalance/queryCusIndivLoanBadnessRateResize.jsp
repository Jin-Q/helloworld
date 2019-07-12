<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>个人贷款余额FLASH图形展示</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/charts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script> 
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>

<script type="text/javascript">
	
	/*--user code end--*/
	
	function changeItemBadRate(){

		var form =  document.getElementById("submitForm"); 
		
		form.action="<emp:url action='ttttttttt.do'/>";
		form.method = "post"; 
		
		 var handleSuccess = function(o){
				if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var xml = jsonstr.CusIndivLoanAmtYearIncrease;
						freshFUFChar(xml);
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	}

	function freshFUFChar(xml){
		var myChart = new FusionCharts("scripts/charts/swfs/FCF_Column3D.swf","myChartid","700","600");   
		myChart.setDataXML(xml); 
		myChart.render("chartdiv");
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="submitForm">
	</form>

	<!-- 划分维度<emp:select id="ItemBadRate" label="划分维度" required="false" dictname="HOMEPAGE_COLOR" onchange="changeItemBadRate()"/>  -->
	
	<emp:text id="CusIndivLoanBadRate" label="CusIndivLoanBadRate"  defvalue="${context.CusIndivLoanBadRate}" hidden="true"></emp:text> 
	
	<div id="chartdiv">
		this is pic !
	</div>
	<script type="text/javascript">
		var xml = CusIndivLoanBadRate.value;    
		var myChart = new FusionCharts("scripts/charts/swfs/FCF_Column3D.swf","myChartid","700","600");   
		myChart.setDataXML(xml); 
		myChart.render("chartdiv");
	</script>
	
	
	
</body>
</html>
</emp:page>