<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>

<html>
<head>
<title>预封包明细信息筛选</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};

	function doSelect(){
		doReturnMethod();
	}
	
	function doReturnMethod(){
		var data = AccLoanList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择引入项");
		}else {

			var subTaskIdStr="";//编号字符串，以","间隔
			var num = AccLoanList._obj.getSelectedData().length;
			if (num != 0) {
				for(var i=0;i<num;i++){
					subTaskIdStr = subTaskIdStr+AccLoanList._obj.getSelectedData()[i].bill_no._getValue()+",";
				}
			}
			var handleSuccess = function(o){
				if(o.responseText !== undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("引入成功!");
						window.location.reload();
						window.opener.location.reload();
					}else {
						alert("引入失败!");
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = '<emp:url action="importIqpAbsProList.do"/>?pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}&subTaskIdStr='+subTaskIdStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="AccLoan.bill_no" label="贷款账号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:returnButton id="s1" label="添加"/>
	</div>

	<emp:table icollName="AccLoanList" pageMode="true" url="pageAccLoanForAbsListPop.do" reqParams="pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}" selectType="2">
			<emp:text id="cus_id" label="客户编号" />
			<emp:text id="bill_no" label="贷款台账编号" />
			<emp:text id="loan_balance" label="贷款余额" />
			<emp:text id="loan_amt" label="贷款金额" />
			<emp:text id="distr_date" label="开始日期" />
			<emp:text id="end_date" label="结束日期" />
			<emp:text id="five_class" label="五级分类" />
			<emp:text id="fina_br_id" label="账务机构" />
			<emp:text id="acc_status" label="台账状态" />
	</emp:table>
</body>
</html>
</emp:page>
    