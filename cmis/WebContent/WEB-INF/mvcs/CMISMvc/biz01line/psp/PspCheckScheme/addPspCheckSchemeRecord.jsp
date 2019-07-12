<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doSave(){
	if(!PspCheckScheme._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	PspCheckScheme._toForm(form);
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var scheme_id = jsonstr.schemeId;
			if(flag == "success"){
				alert("新增成功！");
				var url = '<emp:url action="getPspCheckSchemeUpdatePage.do"/>?scheme_id='+scheme_id;
				url = EMPTools.encodeURI(url);
				window.location = url;  
			}else {
				alert("新增失败！");
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

	var url = '<emp:url action="addPspCheckSchemeRecord.do"/>';
	url = EMPTools.encodeURI(url);
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
};
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPspCheckSchemeRecord.do" method="POST">
		<emp:gridLayout id="PspCheckSchemeGroup" title="贷后检查项方案" maxColumn="2">
			<emp:text id="PspCheckScheme.scheme_id" label="方案编号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspCheckScheme.scheme_name" label="方案名称" colSpan="2" maxlength="100" required="false" />
			<emp:textarea id="PspCheckScheme.memo" label="备注" maxlength="250" required="false" />
			<emp:text id="PspCheckScheme.input_id" label="登记人" maxlength="20" required="false"  hidden="true" readonly="true" defvalue="${context.currentUserId}"/>
			<emp:date id="PspCheckScheme.input_date" label="登记日期" required="false" readonly="true" hidden="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="PspCheckScheme.input_br_id" label="登记机构" maxlength="20" readonly="true" hidden="true" required="false" defvalue="${context.organNo}" />
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

