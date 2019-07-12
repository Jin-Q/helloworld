<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
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
	
	function refreshIqpBksyndicInfo() {
		window.location.reload();
		//IqpBksyndic_tabs.tabs.IqpBksyndicInfo_tab.refresh();
	};

	/*--user code begin--*/
	//-------异步保存主表单页面信息-------
	function doSave(){
		var amt = IqpBksyndic.bank_syndic_amt._getValue();
        if(amt<=0){
           alert("请填写银团详细信息！");
            return;
        }
		if(!IqpBksyndic._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpBksyndic._toForm(form);
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

		var url = '<emp:url action="updateIqpBksyndicRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}					
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpBksyndicRecord.do" method="POST">
		<emp:gridLayout id="IqpBksyndicGroup" title="银团信息" maxColumn="2">
			<emp:text id="IqpBksyndic.serno" label="业务编号" defvalue="${context.serno}" colSpan="2" hidden="true" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpBksyndic.bank_syndic_type" label="银团类型" required="true" dictname="STD_BKSYNDIC_TYPE"/> 
			<emp:select id="IqpBksyndic.agent_org_flag" label="代理行标志" required="true" dictname="STD_ZX_YES_NO"/>   
			<emp:text id="IqpBksyndic.bank_syndic_amt" label="银团贷款总金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
			<emp:text id="IqpBksyndic.agent_rate" label="代理费率" maxlength="10" required="true" dataType="Rate" />
			<emp:text id="IqpBksyndic.amt_arra_rate" label="资金安排费率" maxlength="10" required="true" dataType="Rate" />
			<emp:textarea id="IqpBksyndic.bank_syndic_desc" label="银团项目描述" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
	

		<div align="center"> 
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>  
		</div>
	</emp:form>
	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">   
		<emp:tab id="IqpBksyndicInfo_tab" label="银团详细信息" url="queryIqpBksyndicIqpBksyndicInfoList.do" reqParams="serno=${context.serno}&op=${context.op}&cont=${context.cont}&subButtonId=queryIqpBksyndic" initial="true"/>
	</emp:tabGroup>
</body>
</html>
</emp:page>
