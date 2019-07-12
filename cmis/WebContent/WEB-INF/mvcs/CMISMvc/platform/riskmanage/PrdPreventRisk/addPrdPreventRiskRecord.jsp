<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doSave(){
	var form = document.getElementById("submitForm");
	if(PrdPreventRisk._checkAll()){
		
		PrdPreventRisk._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var prevent_id = jsonstr.prevent_id;
				if(flag == "success"){
					alert("保存成功!");
					var url = '<emp:url action="getPrdPreventRiskUpdatePage.do"/>?prevent_id='+prevent_id;
					
					url = EMPTools.encodeURI(url);
					window.location=url;
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
		var url = '<emp:url action="addPrdPreventRiskRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	}
	
};	

	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdPreventRiskRecord.do" method="POST">
		<emp:gridLayout id="PrdPreventRiskGroup" title="风险拦截方案" maxColumn="2">
			<emp:text id="PrdPreventRisk.prevent_id" label="方案编号" maxlength="32" readonly="true" hidden="true" />
			<emp:text id="PrdPreventRisk.prevent_desc" label="方案名称" maxlength="100" required="true" />
			<emp:select id="PrdPreventRisk.used_ind" label="是否使用" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PrdPreventRisk.used_flow" label="是否适用流程" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="PrdPreventRisk.memo" label="备注" maxlength="200" required="true" colSpan="2" />
			<emp:text id="PrdPreventRisk.input_id" label="登记人" maxlength="20" defvalue="$currentUserId" hidden="true" />
			<emp:text id="PrdPreventRisk.input_br_id" label="登记机构" maxlength="20" defvalue="$organNo" hidden="true" />
			<emp:date id="PrdPreventRisk.input_date" label="登记日期" hidden="true" defvalue="$OPENDAY"/>
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
