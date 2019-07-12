<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
	context.setDataValue("editFlag","edit");
	String style_id = (String)context.getDataValue("style_id");
%>
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

	};

	function getParam(){
		var cus_id = document.getElementById('cus_id').value;
		var stat_prd_style = document.getElementById('stat_prd_style').value;
		var stat_prd = document.getElementById('stat_prd').value;
		var fnc_conf_data_col = document.getElementById('fnc_conf_data_col').value;
		var fnc_name = document.getElementById('fnc_name').value;
		var style_id = document.getElementById('style_id').value;
		var state_flg = document.getElementById('state_flg').value;
		var fnc_type = document.getElementById('fnc_type').value;
		var stat_style = document.getElementById('stat_style').value;

		var param = "cus_id="+cus_id+"&stat_prd_style="+stat_prd_style+"&stat_prd="+stat_prd+
		"&fnc_conf_data_col="+fnc_conf_data_col+"&fnc_name="+fnc_name+"&style_id="+style_id
		+"&state_flg="+state_flg+"&fnc_conf_typ=01&stat_style="+stat_style+"&fnc_type="+fnc_type;
		return param;
	}
	
	function doTempAddFncStatBs(){
		var param = getParam();
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="tempAddFncStatBs.do"/>&'+param;
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form);
			form.action = action;
			form.submit();
		}
	};
	
	function doAddFncStatBs(){
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addFncStatBs.do"/>';
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form)
			form.action = action;
			form.submit();
		}
	};
	
	function doUpdateFncStatBs(){
		var param = getParam();
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="addOrupdateFncStatBs.do"/>&'+param;
		
		var result = rptstyle._checkAll();
		if(result){
			rptstyle._toForm(form);
			form.action = action;
			form.submit();
		}
	};
	//先校验是否存在上年年报
	function doCheckLsFncStatBaseExists(){
		var cus_id = document.getElementById('cus_id').value;
		var stat_prd = document.getElementById('stat_prd').value;
		var stat_style = document.getElementById('stat_style').value;
		stat_prd = (parseFloat(stat_prd.substring(0,4))-1) + "12";
		var url = '<emp:url action="checkLsFncStatBaseExist.do"/>&cus_id='+cus_id+"&stat_prd_style=4&stat_style="+stat_style+"&stat_prd="+stat_prd+"&type=lsYear";
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
					if(confirm('上年年报不存在或未严格按照年份顺序录入报表(或不是完成状态下),是否确定完成?')){
						doUpdateFncStatBs();
					}
				}else{
					doUpdateFncStatBs();
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
	 
	 String.prototype.trim = function(){
	 		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

/*	 function doGetInitData(){
		var param = getParam();
		var form = document.getElementById('submitForm');
		var action = '<emp:url action="getInitData.do"/>&'+param;
		
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
							//_doKeypress_1(document.all(ne));///再下一个
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
		//<emp:button id="getInitData" label="引入期初数"/>&nbsp;&nbsp;
</script>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:form id="submitForm" action="" method="POST">  </emp:form>

	<emp:rpt id="rptstyle" cusComRptId="fncstatbase" editFlag="editFlag"/>
	<div align="center">
		<emp:button id="tempAddFncStatBs" label="暂存"/>&nbsp;&nbsp;
		<emp:button id="checkLsFncStatBaseExists" label="完成"/>
	</div>
	<emp:text id="cus_id" label="客户码" hidden="true"/>
	<emp:text id="stat_prd_style" label="报表周期类型" hidden="true"/>
	<emp:text id="stat_prd" label="报表期间" hidden="true"/>
	<emp:text id="fnc_conf_data_col" label="数据列数" hidden="true"/>
	<emp:text id="fnc_name" label="报表名称"  hidden="true"/>
	<emp:text id="style_id" label="报表样式编号"  hidden="true"/>
	<emp:text id="state_flg" label="状态"  hidden="true"/>
	<emp:text id="fnc_type" label="报表类型"  hidden="true"/>
	<emp:text id="fnc_conf_typ" label="报表类型"  hidden="true"/>
	
	<emp:text id="stat_style" label="报表口径" hidden="true"/>
	
</body>
</html>
</emp:page>

