<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
  function doSelect(){
	var data = AccLoanList._obj.getSelectedData();  
	if (data != null && data.length !=0) {
		var cont_no = data[0].cont_no._getValue(); 
		var form = document.getElementById("submitForm"); 
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					doAddIqpCreditChangeAppPage();  
				}else {        
					alert("存在在途的信用证修改申请！"); 
					return;
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
		var url = '<emp:url action="queryIsConfIqpCreditChange.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url); 
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}else {
		alert('请先选择一条记录！');
	}
	
};
	
	function doAddIqpCreditChangeAppPage() {
		var paramStr = AccLoanList._obj.getParamStr(['cont_no','bill_no']); 
		if (paramStr != null) {
			var url = '<emp:url action="addIqpCreditChangeAppPage.do"/>?menuId=iqp_credit_change_app&'+paramStr;  
			url = EMPTools.encodeURI(url); 
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:form id="submitForm" action="" method="POST">
<div  class='emp_gridlayout_title'>新增信用证修改申请</div> 
   </emp:form>
	<emp:table icollName="AccLoanList" pageMode="true" url="pageAddIqpCreditQuery.do">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>   
		<emp:text id="cus_id" label="客户码" hidden="false"/>
		<emp:text id="cus_id_displayname" label="客户名称" /> 	
		<emp:text id="prd_id_displayname" label="产品名称" /> 		
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/> 
		<emp:text id="loan_amt" label="开证金额" dataType="Currency"/>       
		<emp:text id="distr_date" label="起始日期" />    
		<emp:text id="end_date" label="到期日期" hidden="true"/>    
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	<div align="center">       
	<br> 
		<emp:button id="select" label="下一步" />
	</div>  
	
</body>
</html>
</emp:page>
    