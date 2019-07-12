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
		AccView._toForm(form);
		AccViewList._obj.ajaxQuery(null,form);
	};    
	 
	
	function doSelect(){
		var data = AccViewList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close(); 
		} else {
			alert('请先选择一条记录！');
		}
	}; 
	function returnCus(data){
		AccView.cus_id._setValue(data.cus_id._getValue());
		AccView.cus_id_displayname._setValue(data.cus_name._getValue());
    };     

    function doClose(){
        window.close();  
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="AccViewGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccView.cont_no" label="合同编号" />
			<emp:text id="AccView.bill_no" label="借据编号" />
			<emp:pop id="AccView.cus_id_displayname" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus" hidden="true"/>
			<emp:text id="AccView.cus_id" label="客户码" hidden="true"/>   
	</emp:gridLayout> 
	  
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选取返回" />
	</div>
	<emp:table icollName="AccViewList" pageMode="true" url="pageAccPop.do?cus_id=${context.cus_id}">
		<emp:text id="cont_no" label="合同编号" />  
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" /> 
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/> 
		<emp:text id="prd_id" label="产品编号" hidden="true" /> 
		<emp:text id="prd_id_displayname" label="产品名称" hidden="false" />  
		<emp:text id="bill_amt" label="借据金额" dataType="Currency"/>
		<emp:text id="bill_bal" label="借据余额" dataType="Currency"/>
		<emp:text id="inner_owe_int" label="表内欠息" dataType="Currency"/>
		<emp:text id="out_owe_int" label="表外欠息" dataType="Currency"/>
	</emp:table>
	<div ><br>
	    <emp:button id="select" label="选取返回" />  
	    <emp:button id="close" label="关闭" />
	<br>
	</div>
</body>
</html>
</emp:page>
    