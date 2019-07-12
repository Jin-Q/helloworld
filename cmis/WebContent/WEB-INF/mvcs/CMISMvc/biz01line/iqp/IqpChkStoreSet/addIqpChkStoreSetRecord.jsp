<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	function doNext(){
		var form = document.getElementById("submitForm");
		if(IqpChkStoreSet._checkAll()){
			IqpChkStoreSet._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var task_set_id = jsonstr.task_set_id;
					if(flag == "success"){
						var url = '<emp:url  action="getIqpChkStoreSetUpdatePage.do"/>?task_set_id='+task_set_id;
						url = EMPTools.encodeURI(url);
						window.location = url;
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
			var url = '<emp:url action="addIqpChkStoreSetRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return;
		}
	};

	function setCus(){
		var task_set_type = IqpChkStoreSet.task_set_type._getValue();
		if(task_set_type=='01'){
			IqpChkStoreSet.cus_name._setValue('');
			IqpChkStoreSet.cus_id._setValue('');
			$(".emp_field_label:eq(1)").text("出质人");
			IqpChkStoreSet.cus_name._obj._renderHidden(false);
			//IqpChkStoreSet.task_set_type._obj.element.text="11111";
			IqpChkStoreSet.cus_name._obj.config.url="<emp:url action='queryAllCusPop.do'/>&returnMethod=returnCus1&cusTypCondition=belg_line in ('BL100','BL200') and cus_status='20'";
		}else if(task_set_type=='02'){
			IqpChkStoreSet.cus_name._setValue('');
			IqpChkStoreSet.cus_id._setValue('');
			$(".emp_field_label:eq(1)").text("监管企业");
			IqpChkStoreSet.cus_name._obj._renderHidden(false);
			IqpChkStoreSet.cus_name._obj.config.url="<emp:url action='IqpOverseeOrg4PopList.do'/>&returnMethod=returnCus2";
		}else{
			IqpChkStoreSet.cus_name._setValue('');
			IqpChkStoreSet.cus_id._setValue('');
			IqpChkStoreSet.cus_name._obj._renderHidden(true);
		}
	}

	function returnCus1(data){
		IqpChkStoreSet.cus_id._setValue(data.cus_id._getValue());
		IqpChkStoreSet.cus_name._setValue(data.cus_name._getValue());
	};
	function returnCus2(data){
		IqpChkStoreSet.cus_id._setValue(data.oversee_org_id._getValue());
		IqpChkStoreSet.cus_name._setValue(data.oversee_org_id_displayname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpChkStoreSetRecord.do" method="POST">
		
		<emp:gridLayout id="IqpChkStoreSetGroup" title="任务向导" maxColumn="2">
			<emp:select id="IqpChkStoreSet.task_set_type" label="任务维度" required="true" dictname="STD_ZB_INSURE_MODE" onchange="setCus()"/>
			<emp:pop id="IqpChkStoreSet.cus_name" label="出质人" required="true" buttonLabel="选择" url='' hidden="true"/>
			<emp:text id="IqpChkStoreSet.cus_id" label="出质人" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

