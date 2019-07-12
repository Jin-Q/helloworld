<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
   Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
   String isFromLmt = "";
   if(context.containsKey("isFromLmt")){
	   isFromLmt = (String)context.getDataValue("isFromLmt");
   }
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/     
	function returnCont(data){
		GrtLoanRGur.guar_cont_no._setValue(data.guar_cont_no._getValue());
    };
    function load(){
    	if("is" != '<%=isFromLmt%>'){
    		GrtLoanRGur.is_per_gur._obj._renderReadonly(false);
    		GrtLoanRGur.is_add_guar._obj._renderReadonly(false);
		} 
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "error"){
					alert("已引用此担保合同,请重新引用!");
					var url;
					if("is" == '<%=isFromLmt%>'){
						url = '<emp:url action="grtGuarZgeList.do"/>?cus_id=${context.cus_id}&guar_cont_type=${context.guar_cont_type}&limit_acc_no=${context.limit_acc_no}&limit_credit_no=${limit_credit_no}&serno=${context.serno}';   
					}else{
						url = '<emp:url action="introGrtGuarContList.do"/>?action=zg&isCreditChange=${context.isCreditChange}&cus_id=${context.cus_id}&serno=${context.serno}&cont_no=${context.cont_no}';
					}
					url=EMPTools.encodeURI(url);  
			      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
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
		var url = '<emp:url action="checkGrtLoanRGur.do"/>&guar_cont_no=${context.guar_cont_no}&serno=${context.serno}&cont_no=${context.cont_no}&isCreditChange=${context.isCreditChange}';
		url = EMPTools.encodeURI(url);            
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    }; 
     
    function checkCur(date){
        var guar_amt = GrtLoanRGur.guar_amt._getValue();
        var guar_cont_amt;
    	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag; 
				var canAmt = jsonstr.canAmt; 
				var Amt = jsonstr.Amt; 
				if(flag == "success"){
					if((parseFloat(guar_amt)+parseFloat(canAmt))>parseFloat(Amt)){
						alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+(parseFloat(Amt)-parseFloat(canAmt)));     
						GrtLoanRGur.guar_amt._setValue(""); 
					}   
				}else if(flag == "error"){
					alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:【"+canAmt+"】"); 
					GrtLoanRGur.guar_amt._setValue("");
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
		var url = '<emp:url action="checkGrtLoanCur.do"/>&guar_cont_no=${context.guar_cont_no}&serno=${context.serno}&guar_amt='+guar_amt;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    }; 
    function doSub(){
        var guar_cont_amt = GrtLoanRGur.guar_cont_no._getValue();
    	var guar_amt = GrtLoanRGur.guar_amt._getValue();
    	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var canAmt = jsonstr.canAmt; 
				var Amt = jsonstr.Amt; 
				if(flag == "success"){
					if((parseFloat(guar_amt)+parseFloat(canAmt))>parseFloat(Amt)){
						alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+(parseFloat(Amt)-parseFloat(canAmt)));     
						GrtLoanRGur.guar_amt._setValue(""); 
					}else{
						doSub1();
					}   
				}else if(flag == "error" && canAmt == 0){
					GrtLoanRGur.guar_amt._setValue("");  
					return false;
				}else{
					doSub1();
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
		var url = '<emp:url action="checkGrtLoanCur.do"/>&guar_cont_no=${context.guar_cont_no}&serno=${context.serno}&guar_amt='+guar_amt;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };
	function doSub1(){
			if(GrtLoanRGur._checkAll()){
				var form = document.getElementById("submitForm");
				GrtLoanRGur._toForm(form);  
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
							alert("引入成功!");
							window.opener.location.reload();
						    window.close();    
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
				var postData = YAHOO.util.Connect.setForm(form);	
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
			}else{
                alert("请输入完整数据!");
			}
	};

	function doBack(){ 
		if("is" == '<%=isFromLmt%>'){ 
			var url = '<emp:url action="grtGuarYbList.do"/>?cus_id=${context.cus_id}&guar_cont_type=${context.guar_cont_type}&limit_acc_no=${context.limit_acc_no}&limit_credit_no=${limit_credit_no}&serno=${context.serno}';
		}else{
		    var url = '<emp:url action="introYbGrtGuarContList.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}';
		}
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};  
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load();">
	
	<emp:form id="submitForm" action="addGrtLoanRGurRecord.do?isCreditChange=${context.isCreditChange}" method="POST">
		
		<emp:gridLayout id="GrtLoanRGurGroup" title="业务和担保合同关联信息" maxColumn="2">
			<emp:text id="GrtLoanRGur.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.cont_no" label="合同编号" maxlength="40" defvalue="${context.cont_no}" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.guar_cont_no" label="担保合同编号" defvalue="${context.guar_cont_no}" readonly="true"/>      
			<emp:text id="GrtLoanRGur.guar_amt" label="本次担保金额" maxlength="18" required="true" dataType="Currency" onchange="checkCur('check');"/>
			<emp:select id="GrtLoanRGur.is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO" required="true" defvalue="${context.is_per_gur}" readonly="true"/>
			<emp:select id="GrtLoanRGur.is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO" required="true" defvalue="${context.is_add_guar}" readonly="true"/>
			<emp:select id="GrtLoanRGur.corre_rel" label="关联关系" dictname="STD_BIZ_CORRE_REL" defvalue="1" required="false" hidden="true"/>
		    <emp:text id="GrtLoanRGur.grt_type" label="担保类型"  defvalue="YB" required="false" hidden="true"/> 
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/> 
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

