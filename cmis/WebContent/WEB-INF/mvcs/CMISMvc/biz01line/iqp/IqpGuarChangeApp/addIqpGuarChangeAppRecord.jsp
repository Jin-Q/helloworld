<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
	   var form = document.getElementById('queryForm');
	   CtrLoanCont._toForm(form);
	   CtrLoanContList._obj.ajaxQuery(null,form);
    };
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};
	
	function doSelect(){
	var data = CtrLoanContList._obj.getSelectedData();  
	if (data != null && data.length !=0) {
		var cont_no = data[0].cont_no._getValue(); 
		var form = document.getElementById("queryForm"); 
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
					doAddIqpGuarChangeAppPage();  
				}else {        
					alert("存在在途的担保变更申请！"); 
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

		var url = '<emp:url action="queryIsConfIqpGuarChange.do"/>?op=update&cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}else {
		alert('请先选择一条记录！');    
	}
	
};	
	
	function doAddIqpGuarChangeAppPage(){
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']); 
		if (paramStr != null) {
			var url = '<emp:url action="addIqpGuarChangeAppPage.do"/>?'+paramStr;  
			url = EMPTools.encodeURI(url);     
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};		
	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};

	function doview(){
		alert("风险提醒：所谓担保变更，即为已经历过有权审批人书面同意授信担保变更的行为");
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doview()">
	<form  method="POST" action="#" id="queryForm">
	</form>
   <emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLoanCont.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
	        <emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />   
 
	 <emp:table icollName="CtrLoanContList" pageMode="true" url="pageAddIqpGuarChange.do"> 
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" /> 
		<emp:text id="prd_id" label="产品编号" hidden="true"/> 
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/> 
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>       
		<emp:text id="cont_balance" label="合同余额" dataType="Currency"/> 
		<emp:text id="cont_start_date" label="起始日期" />    
		<emp:text id="cont_end_date" label="到期日期" />    
		<emp:text id="manager_br_id_displayname" label="管理机构" />
	</emp:table> 
		<div align="center">
			<br>
			<emp:button id="select" label="下一步" />
		</div>
</body>
</html>
</emp:page>

