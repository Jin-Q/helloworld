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
	text-align: left;
	width: 450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function setCusInfo(){
		var certTyp = CusComRelApital.cert_typ._obj.element.value;
		var certCode = CusComRelApital.cert_code._obj.element.value;
		if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
		    alert("证件类型证件号码不能为空！");
		    return ;
		}
	}

	function onReturnLinkAddr(date){
		IqpCommoProvider.link_addr._obj.element.value=date.id;
		IqpCommoProvider.link_addr_displayname._obj.element.value=date.label;
	}

	function returnCus(data){
		IqpCommoProvider.provider_no._setValue(data.cus_id._getValue());
		IqpCommoProvider.provider_no_displayname._setValue(data.cus_name._getValue());
		cert_type._setValue(data.cert_type._getValue());
		cert_code._setValue(data.cert_code._getValue());
	}

	function doAddIqpCommo(){
		var form = document.getElementById("submitForm");
		IqpCommoProvider._checkAll();
		if(IqpCommoProvider._checkAll()){
			IqpCommoProvider._toForm(form);
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
						alert("新增成功！");
						var provider_no = IqpCommoProvider.provider_no._getValue();
						var url = '<emp:url action="queryIqpCommoProviderList.do"/>&provider_no='+provider_no;
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="addIqpCommoProviderRecord.do" method="POST">
		
		<emp:gridLayout id="IqpCommoProviderGroup" title="商品供应商管理" maxColumn="2">
			<emp:text id="IqpCommoProvider.mort_catalog_no" label="押品目录编号" maxlength="30" required="true" readonly="true" defvalue="${context.catalog_no}" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:pop id="IqpCommoProvider.provider_no" label="供应商编号" url="queryAllCusPop.do?cusTypCondition=Com&returnMethod=returnCus" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="IqpCommoProvider.provider_no_displayname" label="供应商名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" /> 
			<emp:select id="cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="cert_code" label="证件号码" maxlength="20" readonly="true" required="true"/>
			<emp:text id="IqpCommoProvider.linkman" label="联系人" maxlength="20" required="false" />
			<emp:text id="IqpCommoProvider.link_phone" label="联系电话" maxlength="20" required="false" dataType="Phone"/>
			<emp:text id="IqpCommoProvider.link_addr" label="联系地址" colSpan="2" hidden="true"/>
			<emp:pop id="IqpCommoProvider.link_addr_displayname" label="联系地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnLinkAddr" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="IqpCommoProvider.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="IqpCommoProvider.input_id_displayname" label="登记人"  required="false" readonly="true" />
			<emp:text id="IqpCommoProvider.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:text id="IqpCommoProvider.input_id" label="登记人" maxlength="60" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpCommoProvider.input_br_id" label="登记机构" maxlength="60" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="IqpCommoProvider.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpCommoProvider.status" label="状态" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addIqpCommo" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

