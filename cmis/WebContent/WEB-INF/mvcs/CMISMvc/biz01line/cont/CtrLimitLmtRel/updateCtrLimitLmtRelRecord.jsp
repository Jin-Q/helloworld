<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doSave(){
	if(CtrLimitLmtRel._checkAll()){
		var serno = CtrLimitLmtRel.limit_serno._getValue();
		var form = document.getElementById("submitForm");
		CtrLimitLmtRel._toForm(form);
		
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success" && serno != null){
					var	url = '<emp:url action="queryCtrLimitLmtRelList.do"/>?serno='+serno;
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
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCtrLimitLmtRelRecord.do" method="POST">
		<emp:gridLayout id="CtrLimitLmtRelGroup" maxColumn="2" title="额度合同占用授信关联表">
			<emp:text id="CtrLimitLmtRel.pk_id" label="主键" maxlength="40" required="false" hidden="true"/>
			<emp:text id="CtrLimitLmtRel.limit_serno" label="额度合同业务编号" maxlength="40"  required="false" readonly="true" hidden="false"/>
			<emp:text id="CtrLimitLmtRel.limit_cont_no" label="额度合同合同编号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:pop id="CtrLimitLmtRel.lmt_code_no" label="授信额度品种编号" url="queryLmtAgrDetailsPop.do?returnMethod=getLmtMsg&&flag=1&cus_id=${context.cus_id}" returnMethod="getLmtMsg"  required="true" />
			<emp:text id="CtrLimitLmtRel.lmt_code_name" label="授信额度品种名称" readonly="true"/>
			<emp:text id="CtrLimitLmtRel.lmt_code_amt" label="授信额度金额" dataType="Currency" readonly="true"/>
			<emp:select id="CtrLimitLmtRel.lmt_type" label="授信额度类型" dictname="STD_ZB_LIMIT_TYPE" readonly="true"/>
			<emp:select id="CtrLimitLmtRel.status" label="状态"  required="false" dictname="STD_ZB_STATUS" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
