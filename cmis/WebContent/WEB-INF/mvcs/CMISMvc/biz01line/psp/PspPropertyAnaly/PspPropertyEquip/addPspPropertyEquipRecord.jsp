<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../jsPspProperty.jsp" flush="true" />
<script type="text/javascript">

	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspPropertyAnaly.task_id._getValue();
		var cus_id = PspPropertyAnaly.cus_id._getValue();
		if(PspPropertyAnaly._checkAll()&PspPropertyEquip._checkAll()){
			PspPropertyAnaly._toForm(form);
			PspPropertyEquip._toForm(form);
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
						var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
						url = EMPTools.encodeURI(url);
						window.location = url; 
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

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="addPspPropertyAllRecord.do" method="POST">
		
		<emp:gridLayout id="PspPropertyEquipGroup" title="固定资产分析" maxColumn="2">
			<emp:text id="PspPropertyAnaly.property_id" label="资产编号" maxlength="32" required="false" hidden="true" />
			<emp:text id="PspPropertyAnaly.task_id" label="任务编号" maxlength="32" required="true" hidden="true"/>
			<emp:text id="PspPropertyAnaly.cus_id" label="客户码" maxlength="32" required="true" hidden="true" />
			<emp:text id="PspPropertyEquip.property_id" label="资产编号" maxlength="32" hidden="true" />
			
			<emp:text id="PspPropertyAnaly.owner" label="所有权人" required="true" maxlength="40" cssElementClass="emp_field_text_input1" colSpan="2"/>
			<emp:select id="PspPropertyAnaly.property_type" label="资产类型" required="true" dictname="STD_ZB_PROPERTY_TYPE" readonly="true"/>
			<emp:select id="PspPropertyAnaly.rela_type" label="客户关系" required="true" dictname="STD_ZB_PSP_RELA_TYPE" />
			<emp:select id="PspPropertyAnaly.owner_cert_type" label="所有权人证件类型" required="true" dictname="STD_ZB_CERT_TYP" onchange="checkCertCode()"/>
			<emp:text id="PspPropertyAnaly.owner_cert_code" label="所有权人证件号码" required="true" maxlength="40" />
			<emp:select id="PspPropertyAnaly.warrant_type" label="权证类型" required="true" dictname="STD_WRR_PROVE_TYPE" />
			<emp:text id="PspPropertyAnaly.warrant_no" label="权证号码" required="true" maxlength="40" />
		
			<emp:text id="PspPropertyEquip.property_qnt" label="数量" maxlength="8" required="false" dataType="Int" />
			<emp:text id="PspPropertyEquip.model_no" label="型号或品牌" maxlength="50" required="false" />
			<emp:date id="PspPropertyEquip.pur_date" label="购置时间" required="false" />
			<emp:text id="PspPropertyEquip.pur_price" label="购置价格" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="PspPropertyEquip.is_invc" label="是否有发票" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="PspPropertyEquip.invc_no" label="购置设备发票号" maxlength="50" required="false" />
			<emp:select id="PspPropertyEquip.is_pld" label="是否抵押" required="false" dictname="STD_ZX_YES_NO" />
		</emp:gridLayout>
		<emp:gridLayout id="PspPropertyAnalyGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspPropertyAnaly.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspPropertyAnaly.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspPropertyAnaly.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspPropertyAnaly.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspPropertyAnaly.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" />
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

