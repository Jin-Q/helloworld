<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAdd(){
		if(!PrdPolcyScheme._checkAll()){
			return false;
		}
		var form = document.getElementById("submitForm");
		PrdPolcyScheme._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var schemeid = jsonstr.schemeid;
				if(flag == "success" & schemeid != null){
					var url = '<emp:url action="getPrdPolcySchemeUpdatePage.do"/>?schemeid='+schemeid;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("新增选择产品发生异常！");
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
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdPolcySchemeRecord.do" method="POST">
		
		<emp:gridLayout id="PrdPolcySchemeGroup" title="政策资料设置方案" maxColumn="2">
			
			<emp:text id="PrdPolcyScheme.schemename" label="方案名称" maxlength="40" required="true" />
			<emp:select id="PrdPolcyScheme.effectived" label="是否启用" dictname="STD_ZX_YES_NO" required="true" />
			<emp:textarea id="PrdPolcyScheme.comments" label="备注" maxlength="200" required="false" />
			<emp:text id="PrdPolcyScheme.schemeid" label="方案编号" hidden="true" maxlength="30" required="false" />
			<emp:text id="PrdPolcyScheme.inputid" label="登记人员" maxlength="20" hidden="true" required="false" defvalue="${context.currentUserId}" />
			<emp:text id="PrdPolcyScheme.inputdate" label="登记日期" maxlength="10" hidden="true" required="false" defvalue="${context.OPENDAY}" />
			<emp:text id="PrdPolcyScheme.orgid" label="登记机构" maxlength="20" hidden="true" required="false" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="add" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

