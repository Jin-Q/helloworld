<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtFpayout._checkAll();
		if(result){
			LmtFpayout._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("修改成功！");
						window.close();
						window.opener.location.reload();
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};		

	function doReturn(){
		window.close();
	}

	function checkPayout(){
		var mpayout = LmtFpayout.mpayout._getValue();
		var ypayout = LmtFpayout.ypayout._getValue();
		if(parseFloat(mpayout)>parseFloat(ypayout)){
			alert("月收入必须小于年收入！");
			LmtFpayout.mpayout._setValue("");
			return;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtFpayoutRecord.do" method="POST">
		<emp:gridLayout id="LmtFpayoutGroup" maxColumn="2" title="家庭支出">
			<emp:pop id="LmtFpayout.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='${context.cus_id}')or cus_id = '${context.cus_id}'&returnMethod=returnCus" colSpan="2" readonly="true" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="LmtFpayout.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFpayout.cert_type" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="LmtFpayout.cert_code" label="证件号码" required="false"  readonly="true"/>
			<emp:select id="LmtFpayout.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" colSpan="2" readonly="true"/>
			<emp:select id="LmtFpayout.fpayout_type" label="家庭支出类型" required="true" dictname="STD_ZB_FPAYOUT_TYPE"  colSpan="2"/>
			<emp:text id="LmtFpayout.mpayout" label="月支出" maxlength="18" required="true" dataType="Currency" onchange="checkPayout()"/>
			<emp:text id="LmtFpayout.ypayout" label="年支出" maxlength="18" required="true" dataType="Currency" onchange="checkPayout()"/>
			<emp:textarea id="LmtFpayout.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFpayout.serno" label="流水号" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updatee" label="修改"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
