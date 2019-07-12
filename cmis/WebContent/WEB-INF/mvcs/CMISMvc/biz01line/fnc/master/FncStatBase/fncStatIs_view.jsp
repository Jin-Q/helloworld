<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<emp:page>
<html>
<head>
<title>企业财务报表</title>

<jsp:include page="/include.jsp" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>

<script type="text/javascript">
	
	function doOnLoad() {
		var str = document.getElementById('errList').value;
	    if(str!=""){
	    	alert(str);
	    }
	};
	function doTempAddFncStatIs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="tempAddFncStatIs.do"/>';
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doAddFncStatIs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addFncStatIs.do"/>';
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doUpdateFncStatIs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addOrupdateFncStatIs.do"/>';
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};

    function doCheckButton(){
    	var cus_id = document.getElementById('cus_id').value;
		var stat_prd_style = document.getElementById('stat_prd_style').value;
		var stat_prd = document.getElementById('stat_prd').value;
		var stat_style = document.getElementById('stat_style').value;
		var param = "cus_id="+cus_id+"&stat_prd_style="+stat_prd_style+"&stat_prd="+stat_prd
		+"&fnc_conf_typ=02&stat_style="+stat_style;
        
    	var stockURL = '<emp:url action="queryFncStatIsEdit.do"/>&'+param;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
    }
</script>
</head>
<body  class="page_content" >

	<%
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
			context.setDataValue("editFlag","noedit");
	 %>
	<emp:rpt id="rptstyle" cusComRptId="fncstatbase" editFlag="editFlag"/>



	<form id="submitForm" action="#" method="POST">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="stat_prd_style" label="报表周期类型" hidden="true"/>
		<emp:text id="stat_prd" label="报表期间" hidden="true"/>
		<emp:text id="fnc_conf_data_col" label="数据列数" hidden="true"/>
		<emp:text id="fnc_name" label="报表名称"  hidden="true"/>
		<emp:text id="style_id" label="报表样式编号"  hidden="true"/>
		<emp:text id="state_flg" label="状态"  hidden="true"/>
		
		<emp:text id="errList" label="公式校验信息"  hidden="true"/>
		<emp:text id="fnc_conf_typ" label="报表类型"  hidden="true"/>
		
		<emp:text id="stat_style" label="报表口径" hidden="true"/>
	</form>

</body>
		

</html>
</emp:page>

