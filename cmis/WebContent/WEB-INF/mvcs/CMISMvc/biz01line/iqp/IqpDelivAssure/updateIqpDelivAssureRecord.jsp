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
	function onload(){
		isReplace();
    };
    
	function doSave(){
		if(!IqpDelivAssure._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpDelivAssure._toForm(form);
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

		var url = '<emp:url action="updateIqpDelivAssureRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}

	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpDelivAssure.is_replace._getValue();
		if(isPay == 1){
			IqpDelivAssure.rpled_serno._obj._renderHidden(false);
			IqpDelivAssure.rpled_serno._obj._renderRequired(true);
		}else {
			IqpDelivAssure.rpled_serno._setValue("");
			IqpDelivAssure.rpled_serno._obj._renderHidden(true);
			IqpDelivAssure.rpled_serno._obj._renderRequired(false);
		}
	};
	function getSerno(data){
		IqpDelivAssure.rpled_serno._setValue(data.bill_no._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpDelivAssureRecord.do" method="POST">
		<emp:gridLayout id="IqpDelivAssureGroup" maxColumn="2" title="提货担保信息"> 
			<emp:select id="IqpDelivAssure.is_replace" label="是否置换" required="true" hidden="false"  dictname="STD_ZX_YES_NO" onclick="isReplace();"/>  
			<emp:pop id="IqpDelivAssure.rpled_serno" label="被置换业务编号" url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="true"  hidden="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>  
			<emp:text id="IqpDelivAssure.cdt_cert_no" label="信用证编号" maxlength="40" required="true"/>
			<emp:text id="IqpDelivAssure.reorder_no" label="提单号码" maxlength="40" required="true" />
			<emp:select id="IqpDelivAssure.reorder_curtype" label="提单币种" dictname="STD_ZX_CUR_TYPE" required="true" />
			<emp:text id="IqpDelivAssure.reorder_amt" label="提单金额" maxlength="16" dataType="Currency" required="true" />
			<emp:text id="IqpDelivAssure.commo_name" label="商品名称" maxlength="100" required="true" />
			
			<emp:textarea id="IqpDelivAssure.cdt_approve_advice" label="国业部审查意见" maxlength="250" required="false" colSpan="2" hidden="true"/>
			<emp:text id="IqpDelivAssure.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" hidden="true" required="false" readonly="true" />
		   
			<emp:text id="IqpDelivAssure.chrg_rate" label="手续费率" required="false" hidden="true" dataType="Rate"/> 
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
