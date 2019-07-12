<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//异步提交申请数据
	function doSubmitModifyHistoryCfg(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("保存成功！");
					var url = '<emp:url action="queryCusModifyHistoryCfgList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
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

		var form = document.getElementById("submitForm");
		ModifyHistoryCfgList._toForm(form);
		page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateModifyHistoryCfgRecords.do" method="POST">
		<emp:table icollName="ModifyHistoryCfgList" editable="true" pageMode="false" url="">
			<emp:text id="model_id" label="表名" hidden="true"/>
			<emp:text id="column_name" label="字段ID" readonly="true"/>
			<emp:text id="column_name_displayname" label="字段名" readonly="true"/>
			<emp:select id="popordic" label="字典类型" dictname="STD_ZB_POPORDIC"/>
			<emp:text id="opttype" label="字典名" maxlength="30"/>
		</emp:table>
		
		<div align="center">
			<br>
			<emp:button id="submitModifyHistoryCfg" label="保存" op="update"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
    