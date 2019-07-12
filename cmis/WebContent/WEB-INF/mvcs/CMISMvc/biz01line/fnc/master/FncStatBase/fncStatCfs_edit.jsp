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
		//alert("onload");
		try{
			page.renderEmpObjects();
		}catch(e){
			alert(e);
		}
	};
	function doTempAddFncStatCfs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="tempAddFncStatCfs.do"/>';
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doAddFncStatCfs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addFncStatCfs.do"/>';
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doUpdateFncStatCfs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addOrupdateFncStatCfs.do"/>';
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};

    function doCheckButton(){
    	var styleFlg = document.getElementById("div1").style.display;
    	if(styleFlg == "none"){
    		document.getElementById("div2").style.display = "none";
    	}else{
    		document.getElementById("div1").style.display = "none";
    	    document.getElementById("div2").style.display = "block";
    	    document.getElementById("noeditDiv").style.display = "none";
    	    document.getElementById("editDiv").style.display = "block";
    	}
    }
</script>
</head>
<body  class="page_content" >

	<%
			Context context0 = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
			context0.setDataValue("editFlag","edit");
	 %>
	<emp:rpt id="rptstyle" cusComRptId="fncstatbase" editFlag="editFlag"/>


	<div align="center" >
		<br>
		<emp:button id="tempAddFncStatCfs" label="暂存"/>&nbsp;&nbsp;
		<emp:button id="updateFncStatCfs" label="完成"/>
	</div>

	<form id="submitForm" action="#" method="POST">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="stat_prd_style" label="报表周期类型" hidden="true"/>
		<emp:text id="stat_prd" label="报表期间" hidden="true"/>
		<emp:text id="fnc_conf_data_col" label="数据列数" hidden="true"/>
		<emp:text id="fnc_name" label="报表名称"  hidden="true"/>
		<emp:text id="style_id" label="报表样式编号"  hidden="true"/>
		<emp:text id="state_flg" label="状态"  hidden="true"/>
		
		<emp:text id="fnc_conf_typ" label="报表类型"  hidden="true"/>
		<emp:text id="stat_style" label="报表口径" hidden="true"/>
		<emp:text id="fnc_type" label="财报类型" hidden="true"/>
	</form>

</body>
		

</html>
</emp:page>

