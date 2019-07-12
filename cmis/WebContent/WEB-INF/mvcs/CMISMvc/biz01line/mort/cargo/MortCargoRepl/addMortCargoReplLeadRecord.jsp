<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
	String oversee_agr_no = "";
	if(context.containsKey("oversee_agr_no")){
		oversee_agr_no = (String)context.getDataValue("oversee_agr_no");
	}
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">

.emp_text2{
border:1px solid #b7b7b7;
width:100px;
background-color:#eee;
}
.emp_text{
border:1px solid #b7b7b7;
width:150px;
background-color:#eee;
}
.emp_text3{
border:1px solid #b7b7b7;
width:100px;
}  
</style>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	function doReturn(){
		<%if(!"".equals(oversee_agr_no)){%>
		var url = '<emp:url action="queryMortInfo4AccOverseeRemindList.do"/>?oversee_agr_no=${context.oversee_agr_no}';
		<%}else if("hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
		<%}else{%>
		var url = '<emp:url action="queryMortCargoReplList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		//alert(url);
		window.location=url;
	}
	function doLoad(){
		var agr_type = MortCargoRepl.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(3)").text(labelName);
		 });
	};
	function returnGuaranty(data){
		MortCargoRepl.guaranty_no._setValue(data.guaranty_no._getValue());
		MortCargoRepl.cus_id._setValue(data.cus_id._getValue());
		MortCargoRepl.oversee_agr_no._setValue(data.agr_no._getValue());
		MortCargoRepl.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
	}

	function doNext(){
		if(!MortCargoRepl._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){
						var serno = jsonstr.serno;
						alert("保存成功");
					/*	var url = '<emp:url action="queryMortCargoReplList.do"/>?menuId=hwzh';
						url = EMPTools.encodeURI(url);
						location.href(url);*/
						var url = '<emp:url action="getMortCargoReplAddPage.do"/>?op=cargo_repl&menuIdTab=hwzh&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location=url;
					}else{
						alert("保存失败");
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			MortCargoRepl._toForm(form);
			var url = '<emp:url action="addMortCargoReplRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addMortCargoReplRecord.do" method="POST">
		
		<emp:gridLayout id="MortCargoReplGroup" title="货物置换" maxColumn="2">
			<emp:text id="MortCargoRepl.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoRepl.guaranty_no" label="押品编号" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoRepl.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id_displayname" label="出质人客户名称" required="false" cssElementClass="emp_field_text_readonly" readonly="true" colSpan="2"/>
			<emp:text id="MortCargoRepl.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.this_repl_total" label="此次置换总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" defvalue="0"/>
			<emp:text id="MortCargoRepl.not_out_total" label="出库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.not_to_total" label="入库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.after_repl_total" label="置换后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortCargoRepl.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoRepl.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" defvalue="00" readonly="true"/>
			<emp:textarea id="MortCargoRepl.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoReplGroup" title="登记信息" maxColumn="2">	
			<emp:text id="MortCargoRepl.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoRepl.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="MortCargoRepl.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortCargoRepl.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:date id="MortCargoRepl.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoRepl.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存" />
			<emp:button id="return" label="返回"/>
			<br>
		</div>
		</emp:form>
</body>
</html>
</emp:page>

