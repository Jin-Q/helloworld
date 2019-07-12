<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String serno = request.getParameter("serno"); 
	String cus_id = request.getParameter("cus_id");
	String signal_type = request.getParameter("signal_type");
%>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var serno = '<%=serno%>';
		PspRavelSignalList.serno._setValue(serno);
		var urls = '&signalCond=cus_id=<%=cus_id%> and signal_type=<%=signal_type%> and signal_status=1';
		PspRavelSignalList.pk_id._obj.config.url=PspRavelSignalList.pk_id._obj.config.url + urls;
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspRavelSignalList._checkAll()){
			PspRavelSignalList._toForm(form); 
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
						alert("新增成功!");
						window.opener.location.reload();
						window.close();
					}else if(flag=="exists"){
						alert('该预警信号已存在！');
					}else {
						alert("新增异常!"); 
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
	
	function doClose(){
		window.close();
	}

	function returnSignal(data){
		PspRavelSignalList.pk_id._setValue(data.pk_id._getValue());
		PspRavelSignalList.cus_id._setValue(data.cus_id._getValue());
		PspRavelSignalList.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		PspRavelSignalList.signal_info._setValue(data.signal_info._getValue());
		PspRavelSignalList.signal_type._setValue(data.signal_type._getValue());
		PspRavelSignalList.last_date._setValue(data.last_date._getValue());
		PspRavelSignalList.disp_mode._setValue(data.disp_mode._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspRavelSignalListRecord.do" method="POST">
		
		<emp:gridLayout id="PspRavelSignalListGroup" title="解除预警信号申请关联表" maxColumn="2">
			<emp:text id="PspRavelSignalList.serno" label="申请编号" maxlength="32" required="true" readonly="true"/>
			<emp:pop id="PspRavelSignalList.pk_id" label="预警信号ID" url="queryPspAltSignalPop.do?returnMethod=returnSignal" required="true" colSpan="2" />
			<emp:text id="PspRavelSignalList.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspRavelSignalList.cus_id_displayname" label="客户名称"  readonly="true"/>
			<emp:textarea id="PspRavelSignalList.signal_info" label="风险预警信息内容及影响" maxlength="500" required="true" colSpan="2" readonly="true"/>
			<emp:select id="PspRavelSignalList.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE" readonly="true"/>
			<emp:text id="PspRavelSignalList.last_date" label="预计持续时间（天）" maxlength="10" required="true" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="PspRavelSignalList.disp_mode" label="处置措施及进展情况" maxlength="50" required="true" colSpan="2" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspRavelSignalList.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspRavelSignalList.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspRavelSignalList.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspRavelSignalList.input_id" label="登记人" maxlength="20" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="PspRavelSignalList.input_br_id" label="登记机构" maxlength="20" defvalue="${context.organNo}" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

