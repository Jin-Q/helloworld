<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
String serno = request.getParameter("serno");
String cus_id = (String)request.getParameter("cus_id");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doSave(){
	if(CtrLimitLmtRel._checkAll()){
		var form = document.getElementById("submitForm");
		CtrLimitLmtRel._toForm(form);
		var cus_id = '<%=cus_id %>';
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				var msg = jsonstr.msg;
				if(flag == "success" && serno != null){
					var	url = '<emp:url action="queryCtrLimitLmtRelList.do"/>?serno='+serno+'&cus_id='+cus_id;
					url = EMPTools.encodeURI(url);  
					window.location = url;
				}else {
					alert(msg);
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
	}
};

function getLmtMsg(data){
	CtrLimitLmtRel.lmt_code_no._setValue(data.limit_code._getValue());
	CtrLimitLmtRel.lmt_code_name._setValue(data.limit_name_displayname._getValue());
	CtrLimitLmtRel.lmt_code_amt._setValue(data.crd_amt._getValue());
	CtrLimitLmtRel.lmt_type._setValue(data.limit_type._getValue());
};

function doOnLoad(){
	var cus_id = '<%=cus_id %>';
	var url = "<emp:url action='queryLmtAgrDetailsPop.do'/>?returnMethod=getLmtMsg&flag=1&cus_id="+cus_id;
	url = EMPTools.encodeURI(url);
	CtrLimitLmtRel.lmt_code_no._obj.config.url = url;
};
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="addCtrLimitLmtRelRecord.do" method="POST">
		<emp:gridLayout id="CtrLimitLmtRelGroup" title="额度合同占用授信关联表" maxColumn="2">
			<emp:text id="CtrLimitLmtRel.pk_id" label="主键" maxlength="40" required="false" hidden="true"/>
			<emp:text id="CtrLimitLmtRel.limit_serno" label="额度合同业务编号" maxlength="40" defvalue="<%=serno %>" required="false" hidden="false"/>
			<emp:text id="CtrLimitLmtRel.limit_cont_no" label="额度合同合同编号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:pop id="CtrLimitLmtRel.lmt_code_no" label="授信额度编号" url="" returnMethod="getLmtMsg"  required="true" />
			<emp:text id="CtrLimitLmtRel.lmt_code_name" label="授信额度品种名称" readonly="true"/>
			<emp:text id="CtrLimitLmtRel.lmt_code_amt" label="授信额度金额" dataType="Currency" readonly="true"/>
			<emp:select id="CtrLimitLmtRel.lmt_type" label="授信额度类型" dictname="STD_ZB_LIMIT_TYPE" readonly="true"/>
			<emp:select id="CtrLimitLmtRel.status" label="状态"  required="false" dictname="STD_ZB_STATUS" defvalue="1" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

