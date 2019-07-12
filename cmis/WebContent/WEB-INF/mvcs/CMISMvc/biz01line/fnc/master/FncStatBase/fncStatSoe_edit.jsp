<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
	context.setDataValue("editFlag","edit");
%>
<emp:page>
<html>
<head>
<title>企业财务报表</title>
<jsp:include page="/include.jsp" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>


<script type="text/javascript"><!--
	function doOnLoad() {

	};

	function getParam(){
		var cus_id = document.getElementById('cus_id').value;
		var stat_prd_style = document.getElementById('stat_prd_style').value;
		var stat_prd = document.getElementById('stat_prd').value;
		var fnc_conf_data_col = document.getElementById('fnc_conf_data_col').value;
		var fnc_name = document.getElementById('fnc_name').value;
		var style_id = document.getElementById('style_id').value;
		var state_flg = document.getElementById('state_flg').value;
		var stat_style = document.getElementById('stat_style').value;

		var param = "cus_id="+cus_id+"&stat_prd_style="+stat_prd_style+"&stat_prd="+stat_prd+
		"&fnc_conf_data_col="+fnc_conf_data_col+"&fnc_name="+fnc_name+"&style_id="+style_id
		+"&state_flg="+state_flg+"&fnc_conf_typ=05&stat_style="+stat_style;
		return param;
	}
	
	function doTempAddFncStatSoe(){
		var param = getParam();
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="tempAddFncStatSoe.do"/>&'+param;
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form);
			form.action = action;
			form.submit();
		}
	};
	
	function doAddFncStatSoe(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addFncStatSoe.do"/>';
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doUpdateFncStatSoe(){
		var param = getParam();
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addOrupdateFncStatSoe.do"/>&'+param;
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form);
			form.action = action;
			form.submit();
		}
	};

	 function doSetData(o){
		 alert(o);
		 if(o==null||o==""){
			 o.value = "0.0";
		}
	}

	 function whenClickHappen(obj)
	 {
	     if(obj.value==0) obj.value="";
	     
	 }
	 function doWhenBlurHappen(obj,showWord)
	 {
		 alert(obj);
		 var oValue;
		 //oValue=obj.value.trim();
		 oValue=obj.value;
	     if(oValue==undefined){
	    	 obj.value="0.0";
		 }else{}
	      //alert(showWord+"不能为空");
	     if(isNaN(obj.value)) 
	         {
	          alert(showWord+" 中输入的不是数字或数字中有空格,现值为:"+obj.value);
	           obj.focus();
	          }
	 }

	 function doWhenBlurHappen2(obj,showWord)
	 {
		 var oValue;
		 oValue=document.getElementsByName(obj).value;
	     if(oValue==undefined){
		     document.getElementsByName(obj).value=0.0;
		 }else{}
	     if(isNaN(document.getElementsByName(obj).value)) 
	         {
	          alert(showWord+" 中输入的不是数字或数字中有空格,现值为:"+document.getElementsByName(obj).value);
	          document.getElementsByName(obj).focus();
	          }
	 }
	 
	 String.prototype.trim = function()
	{
	 		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
		
  </script>
</head>
<body  class="page_content">
  <emp:form id="submitForm" action="" method="POST">  </emp:form>

	<emp:rpt id="rptstyle" cusComRptId="fncstatbase" editFlag="editFlag"/>
	<div align="center">
		<emp:button id="tempAddFncStatSoe" label="暂存"/>&nbsp;&nbsp;
		<emp:button id="updateFncStatSoe" label="完成"/>
	</div>
	<emp:text id="cus_id" label="客户码" hidden="true"/>
	<emp:text id="stat_prd_style" label="报表周期类型" hidden="true"/>
	<emp:text id="stat_prd" label="报表期间" hidden="true"/>
	<emp:text id="fnc_conf_data_col" label="数据列数" hidden="true"/>
	<emp:text id="fnc_name" label="报表名称"  hidden="true"/>
	<emp:text id="style_id" label="报表样式编号"  hidden="true"/>
	<emp:text id="state_flg" label="状态"  hidden="true"/>
	
	<emp:text id="fnc_conf_typ" label="报表类型"  hidden="true"/>
	<emp:text id="stat_style" label="报表口径" hidden="true"/>

</body>
</html>
</emp:page>

