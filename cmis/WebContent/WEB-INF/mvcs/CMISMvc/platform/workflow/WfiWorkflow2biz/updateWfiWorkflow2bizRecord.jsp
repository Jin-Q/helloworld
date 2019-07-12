<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
		
	function doSubmitAsyn() {
		if(!WfiWorkflow2biz._checkAll()) {
			return;
		}
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 1) {
					alert('操作成功！');
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
		var url = '<emp:url action="updateWfiWorkflow2bizRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateWfiWorkflow2bizRecord.do" method="POST">
		<emp:gridLayout id="WfiWorkflow2bizGroup" maxColumn="2" title="流程关联业务配置">
			<emp:text id="WfiWorkflow2biz.pk1" label="配置主键" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:select id="WfiWorkflow2biz.appl_type" label="申请类型" required="true" dictname="ZB_BIZ_CATE" readonly="true" colSpan="2"/>
			<emp:text id="WfiWorkflow2biz.wfsign" label="流程标识" maxlength="32" required="false" readonly="true"/>
			<emp:text id="WfiWorkflow2biz.wfname" label="流程名称" maxlength="50" required="false" readonly="true"/>
			<emp:text id="WfiWorkflow2biz.app_url" label="业务信息页面" maxlength="200" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiWorkflow2biz.biz_url" label="业务要素修改页面" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiWorkflow2biz.prevent_list" label="风险拦截" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="WfiWorkflow2biz.scene_scope" label="配置适用范围" required="true" dictname="WF_2BIZ_SCOPE" />
			<emp:textarea id="WfiWorkflow2biz.remark" label="备注" maxlength="100" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitAsyn" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
	<emp:tabGroup mainTab="wfiNode2BizTab" id="wfiNode2BizTabGroup">
		<emp:tab label="节点配置" id="wfiNode2BizTab" url="queryWfiNode2bizList.do" needFlush="true" reqParams="pk1=${context.pk1 }&wfsign=${context.WfiWorkflow2biz.wfsign }"/>
	</emp:tabGroup>
	
</body>
</html>
</emp:page>
