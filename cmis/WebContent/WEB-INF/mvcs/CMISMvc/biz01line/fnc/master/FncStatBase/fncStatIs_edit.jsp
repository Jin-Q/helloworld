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
<script src="<emp:file fileName='scripts/qry/fnc/${context.style_id}/${context.style_id}.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	
	function doOnLoad() {
		//alert("onload");
		try{
			page.renderEmpObjects();
		}catch(e){
			alert(e);
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

	//先校验是否存在上年同期报表是否存在
	function doCheckLsFncStatBaseExists(){
		var cus_id = document.getElementById('cus_id').value;
		var stat_prd = document.getElementById('stat_prd').value;
		var stat_style = document.getElementById('stat_style').value;
		var stat_prd_style = document.getElementById('stat_prd_style').value;
		stat_prd = (parseFloat(stat_prd.substring(0,4))-1) + stat_prd.substring(4,stat_prd.length);
		var url = '<emp:url action="checkLsFncStatBaseExist.do"/>&cus_id='+cus_id+"&stat_prd_style="
		+stat_prd_style+"&stat_style="+stat_style+"&stat_prd="+stat_prd+"&type=lsSame";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("查询失败!");
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="notexists"){
					if(confirm('上年同期报表不存在或不是完成状态，是否确定完成？')){
						doUpdateFncStatIs();
					}
				}else{
					doUpdateFncStatIs();
				}
			}
		};
		var handleFailure = function(o){ 
			alert("查询失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

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

 /*   function doGetInitData(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="getInitDataIS.do"/>';
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form);
			form.action = action;
			form.submit();
		}
	};*/

	//添加键盘事件，跳转到下一个输入框
	function _doKeypressDown(obj) {
		try{
			if(event.keyCode == 13){
				var ne = obj.getAttribute("nextElement");
				if(ne != null && ne != '' && document.all(ne) ){
					try{
						document.all(ne).focus();///到下一个
						document.all(ne).select();
						//	 _doKeypress_1(document.all(ne));///再下一个
							 //document.getElementById(obj1).style.backgroundColor="red";
					}catch(ex){
						 ///如果下一个输入框无法使用focus(), 则递归调用 下一个输入框的下一个输入框///
						_doKeypressDown(document.all(ne)); 
					}
				}
			}
		}catch(e){
			//alert(e);
		}
	}
</script>
</head>
<body  class="page_content" >

	<%
		Context context0 = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
		context0.setDataValue("editFlag","edit");
		String style_id = (String)context0.getDataValue("style_id");
	 %>
	<emp:rpt id="rptstyle" cusComRptId="fncstatbase" editFlag="editFlag"/>

	<div align="center" >
		<br>
		<!--<emp:button id="getInitData" label="引入本期数"/>&nbsp;&nbsp;
		--><emp:button id="tempAddFncStatIs" label="暂存"/>&nbsp;&nbsp;
		<emp:button id="checkLsFncStatBaseExists" label="完成"/>
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
		<emp:text id="fnc_type" label="报表类型" hidden="true"/>
	</form>

</body>
		

</html>
</emp:page>

