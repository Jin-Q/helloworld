<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}   
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	} 
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		isReplace();
		var options = IqpCreditExportApp.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="01" || options[i].value=="02" || options[i].value=="03" || options[i].value=="04" || options[i].value=="07" ){
				options.remove(i);
			}
		}
	}
	
	function doSave(){
		if(!IqpCreditExportApp._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpCreditExportApp._toForm(form);
		//var serno = IqpBksyndic._getValue();
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
					alert("保存成功！");
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpCreditExportAppRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}			
	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpCreditExportApp.is_replace._getValue();
		if(isPay == 1){
			IqpCreditExportApp.rpled_serno._obj._renderHidden(false);
			IqpCreditExportApp.rpled_serno._obj._renderRequired(true);
		}else if(isPay == 2){
			IqpCreditExportApp.rpled_serno._setValue("");
			IqpCreditExportApp.rpled_serno._obj._renderHidden(true);
			IqpCreditExportApp.rpled_serno._obj._renderRequired(false);
		}
	};

	function getSerno(data){
		IqpCreditExportApp.rpled_serno._setValue(data.bill_no._getValue());
	};					
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="updateIqpCreditExportAppRecord.do" method="POST">
		<emp:gridLayout id="IqpCreditExportAppGroup" maxColumn="2" title="信用证项下出口押汇信息">
			<emp:text id="IqpCreditExportApp.serno" label="业务编号" defvalue="${context.serno}" hidden="true" colSpan="2" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpCreditExportApp.is_replace" label="是否置换" required="true" onclick="isReplace();"  dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpCreditExportApp.rpled_serno" label="被置换业务编号" url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="false" hidden="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="IqpCreditExportApp.is_internal_cert" label="是否国内证项下" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:select id="IqpCreditExportApp.receipt_cur_type" label="单据币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpCreditExportApp.receipt_cur_amt" label="单据金额" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="IqpCreditExportApp.biz_settl_mode" label="原业务结算方式" required="true" dictname="STD_BIZ_SETTL_MODE"/> 
		</emp:gridLayout>
		  
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
