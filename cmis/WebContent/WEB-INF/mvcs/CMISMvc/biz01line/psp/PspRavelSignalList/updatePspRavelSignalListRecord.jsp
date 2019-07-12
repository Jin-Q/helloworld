<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
    	var task_id = PspAppAltSignal.task_id._getValue();
    	var cus_id = PspAppAltSignal.cus_id._getValue();
		var form = document.getElementById("submitForm");
		if(PspAppAltSignal._checkAll()){
			PspAppAltSignal._toForm(form); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("修改成功!");
						var url = '<emp:url action="queryPspAppAltSignalList.do"/>?task_id='+task_id+'&cus_id='+cus_id;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改异常!"); 
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePspRavelSignalListRecord.do" method="POST">
		<emp:gridLayout id="PspRavelSignalListGroup" maxColumn="2" title="解除预警信号申请关联表">
			<emp:text id="PspRavelSignalList.serno" label="申请编号" maxlength="32" required="true" readonly="true" />
			<emp:text id="PspRavelSignalList.pk_id" label="预警信号ID" maxlength="40" required="true" readonly="true" />
			<emp:text id="PspRavelSignalList.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:textarea id="PspRavelSignalList.signal_info" label="风险预警信息内容及影响" maxlength="500" required="true" colSpan="2" />
			<emp:select id="PspRavelSignalList.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
			<emp:text id="PspRavelSignalList.last_date" label="预计持续时间（天）" maxlength="10" required="true" dataType="Int" />
			<emp:textarea id="PspRavelSignalList.disp_mode" label="处置措施及进展情况" maxlength="50" required="true" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspRavelSignalList.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspRavelSignalList.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspRavelSignalList.input_date" label="登记日期" required="false" />
			
			<emp:text id="PspRavelSignalList.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="PspRavelSignalList.input_br_id" label="登记机构" maxlength="20" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
