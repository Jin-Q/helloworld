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
	text-align: left;
	width: 450px;
}
</style>
<script type="text/javascript"><!--
<%
	String catalog_no = request.getParameter("catalog_no");
%>		
	function onReturnLinkAddr(date){
		IqpCommoProvider.link_addr._obj.element.value=date.id;
		IqpCommoProvider.link_addr_displayname._obj.element.value=date.label;
	}
	
	function returnCus(data){
		IqpCommoProvider.provider_no._setValue(data.cus_id._getValue());
		IqpCommoProvider.provider_no_displayname._setValue(data.cus_name._getValue());
		IqpCommoProvider.cert_type._setValue(data.cert_type._getValue());
		IqpCommoProvider.cert_code._setValue(data.cert_code._getValue());
		checkProviderNo();
	}
	
		//异步提交申请数据
	function doUpdateIqpCommo(){
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
				if("Y" == flag){
					alert("保存成功！");
					document.getElementById("button_UpdateIqpCommo").disabled = "";
					var catalog_no = IqpCommoProvider.mort_catalog_no._getValue();
					var url = '<emp:url action="queryIqpCommoProviderList.do"/>?catalog_no='+catalog_no;
					url = EMPTools.encodeURI(url);
					window.location = url;
					var form = document.getElementById('submitForm');
					form.action = url;
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success:handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = IqpCommoProvider._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			document.getElementById("button_UpdateIqpCommo").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};

	function doReturn(){
		var catalog_no = IqpCommoProvider.mort_catalog_no._getValue();
		var url = '<emp:url action="queryIqpCommoProviderList.do"/>&catalog_no='+catalog_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function checkProviderNo(){
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
					
				}else if(flag == "failed"){
					alert("该押品目录编号下已存在此客户！");
					IqpCommoProvider.provider_no._setValue("");
					IqpCommoProvider.provider_no_displayname._setValue("");
					IqpCommoProvider.cert_type._setValue("");
					IqpCommoProvider.cert_code._setValue("");
					
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
			var provider_no = IqpCommoProvider.provider_no._getValue();
			var url = '<emp:url action="queryProviderNoListPage.do"/>&provider_no='+provider_no;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		
	}
	/*--user code end--*/
	
--></script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="${context.operate}" method="POST">
		<emp:gridLayout id="IqpCommoProviderGroup" maxColumn="2" title="商品供应商管理">
			<emp:text id="IqpCommoProvider.mort_catalog_no" label="押品目录编号" maxlength="30" required="true" readonly="true" defvalue="${context.catalog_no}" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:pop id="IqpCommoProvider.provider_no" label="供应商编号" url="queryAllCusPop.do?cusTypCondition=IqpCommo&returnMethod=returnCus" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="IqpCommoProvider.provider_no_displayname" label="供应商名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" /> 
			<emp:select id="IqpCommoProvider.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="IqpCommoProvider.cert_code" label="证件号码" maxlength="20" readonly="true" required="true"/>
			<emp:text id="IqpCommoProvider.linkman" label="联系人" maxlength="20" required="true" />
			<emp:text id="IqpCommoProvider.link_phone" label="联系电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpCommoProvider.link_addr" label="联系地址" colSpan="2" hidden="true"/>
			<emp:pop id="IqpCommoProvider.link_addr_displayname" label="联系地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnLinkAddr" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="IqpCommoProvider.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="IqpCommoProvider.input_id_displayname" label="登记人" required="false" readonly="true" />
			<emp:text id="IqpCommoProvider.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:text id="IqpCommoProvider.input_id" label="登记人" maxlength="60" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpCommoProvider.input_br_id" label="登记机构" maxlength="60" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="IqpCommoProvider.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="IqpCommoProvider.status" label="状态" required="false" readonly="true" dictname="STD_ZB_COMMO_PROVIDER" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="UpdateIqpCommo" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
