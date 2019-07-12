<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSelect(){
		var data = AccPopList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}  
	};    

	function doClose(){
       window.close();
    };
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<br>   
	<emp:table icollName="AccPopList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="prd_id" label="产品编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bill_amt" label="贷款金额" dataType="Currency"/> 
		<emp:text id="bill_bal" label="贷款余额" dataType="Currency"/>
		
		<emp:text id="limit_ind" label="授信额度使用标志" dictname="STD_LIMIT_IND" hidden="true"/>
		<emp:text id="limit_acc_no" label="授信台账编号" hidden="true"/>
		<emp:text id="limit_credit_no" label="第三方授信编号" hidden="true"/>
	</emp:table>
	<div ><br>
	     <emp:button id="select" label="选取返回"/>  
	     <emp:button id="close" label="关闭" />
	<br> 
	</div>
</body>
</html>
</emp:page>
    