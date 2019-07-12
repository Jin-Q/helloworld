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
		var options = IqpExportCollectCont.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="05" || options[i].value=="06" || options[i].value=="07" || options[i].value=="01" || options[i].value=="02" ){
				options.remove(i);
			}
		}
	};
	
	function doSave(){
		if(!IqpExportCollectCont._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpExportCollectCont._toForm(form);
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

		var url = '<emp:url action="updateIqpExportCollectContRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}	
	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpExportCollectCont.is_replace._getValue();
		if(isPay == 1){
			IqpExportCollectCont.rpled_serno._obj._renderHidden(false);
			IqpExportCollectCont.rpled_serno._obj._renderRequired(true);
		}else {
			IqpExportCollectCont.rpled_serno._setValue("");
			IqpExportCollectCont.rpled_serno._obj._renderHidden(true);
			IqpExportCollectCont.rpled_serno._obj._renderRequired(false);
		}
	};
	function getSerno(data){
		IqpExportCollectCont.rpled_serno._setValue(data.bill_no._getValue());
	};			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updateIqpExportCollectContRecord.do" method="POST"> 
		<emp:gridLayout id="IqpExportCollectContGroup" maxColumn="2" title="托收信息">
			<emp:text id="IqpExportCollectCont.serno" label="业务编号" defvalue="${context.serno}" colSpan="2" hidden="true" maxlength="40" required="false" readonly="true" />
			<emp:select id="IqpExportCollectCont.is_replace" label="是否置换" onclick="isReplace();" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:pop id="IqpExportCollectCont.rpled_serno" label="被置换业务编号" url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="IqpExportCollectCont.biz_settl_mode" label="原业务结算方式" required="true" dictname="STD_BIZ_SETTL_MODE"/> 
			<emp:select id="IqpExportCollectCont.receipt_cur_type" label="单据币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpExportCollectCont.receipt_cur_amt" label="单据金额" maxlength="18" required="true" dataType="Currency" />			 
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
