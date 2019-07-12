<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if("view".equals(op)){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doNext(){
	if(!ArpAssetPegInfo._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	ArpAssetPegInfo._toForm(form);
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
				window.location.reload();
			}else {
				alert("保存失败!"); 
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
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
};
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateArpAssetPegInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpAssetPegInfoGroup" maxColumn="2" title="资产转固信息">
			<emp:text id="ArpAssetPegInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="ArpAssetPegInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpAssetPegInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetPegInfo.guaranty_name" label="抵债资产名称" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetPegInfo.guaranty_type_displayname" label="抵债资产类型" required="true" readonly="true" cssElementClass="emp_field_text_cusname" colSpan="2" />
			<emp:text id="ArpAssetPegInfo.debt_in_amt" label="抵入金额" maxlength="16" required="true" dataType="Currency" defvalue="${context.debt_in_amt}" readonly="true" colSpan="2"/>
			<emp:text id="ArpAssetPegInfo.to_prop_value" label="转固价值" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpAssetPegInfo.eval_amt" label="评估金额" maxlength="16" required="true" dataType="Currency" />
			<emp:textarea id="ArpAssetPegInfo.disp_resn" label="处置理由" maxlength="200" required="false" colSpan="2" />
			<emp:date id="ArpAssetPegInfo.asgn_date" label="入账日期" required="false" />
			<emp:textarea id="ArpAssetPegInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetPegInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" readonly="true" defvalue="00"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if(!"view".equals(op)){ %>
			<emp:button id="next" label="保存" op="update"/>
			<%} %>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
