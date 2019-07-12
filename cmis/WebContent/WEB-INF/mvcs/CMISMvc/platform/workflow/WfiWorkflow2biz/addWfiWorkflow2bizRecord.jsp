<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:510px;
}

</style>

<script type="text/javascript">

	/*--user code begin--*/
	
	function setWfSign(data) {
		WfiWorkflow2biz.wfsign._setValue(data.wfsign._getValue());
		WfiWorkflow2biz.wfname._setValue(data.wfname._getValue());
	}
	
	function doSubmitAsyn() {
		if(!WfiWorkflow2biz._checkAll()) {
			return;
		}
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var result = jsonstr.result;
				var flag = result.flag;
				if(flag == 1) {
					alert('操作成功！');
					var pk1 = result.pk1;
					var url = '<emp:url action="getWfiWorkflow2bizUpdatePage.do"/>?pk1='+pk1;
					url = EMPTools.encodeURI(url);
					window.location = url;
				} else {
					alert('操作失败！'+flag);
				}
			}catch(e) {
				alert(o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var form = document.getElementById("submitForm");
		WfiWorkflow2biz._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = '<emp:url action="addWfiWorkflow2bizRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfiWorkflow2bizRecord.do" method="POST">
		
		<emp:gridLayout id="WfiWorkflow2bizGroup" title="流程关联业务配置" maxColumn="2">
			<emp:text id="WfiWorkflow2biz.pk1" label="配置主键" maxlength="40" hidden="true"/>
			<emp:select id="WfiWorkflow2biz.appl_type" label="申请类型" required="true" dictname="ZB_BIZ_CATE" colSpan="2"/>
			<emp:pop id="WfiWorkflow2biz.wfsign" label="流程标识" url="queryWFList.do?returnMethod=setWfSign&viewType=pop" returnMethod="setWfSign"  required="true" />
			<emp:text id="WfiWorkflow2biz.wfname" label="流程名称" maxlength="50" required="false" readonly="true"/>
			<emp:text id="WfiWorkflow2biz.app_url" label="业务信息页面" maxlength="200" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiWorkflow2biz.biz_url" label="业务要素修改页面" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiWorkflow2biz.prevent_list" label="风险拦截" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="WfiWorkflow2biz.scene_scope" label="配置适用范围" required="true" dictname="WF_2BIZ_SCOPE" defvalue="999"/>
			<emp:textarea id="WfiWorkflow2biz.remark" label="备注" maxlength="100" required="false" colSpan="2" />
		</emp:gridLayout>
		<div align="center">
			<emp:button id="submitAsyn" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
		
	</emp:form>
	
</body>
</html>
</emp:page>

