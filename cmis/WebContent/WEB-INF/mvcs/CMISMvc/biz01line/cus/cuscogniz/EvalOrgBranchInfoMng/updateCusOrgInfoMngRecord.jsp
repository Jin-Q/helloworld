<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var cus_id ='cus_id='+ CusOrgInfoMng.cus_id._getValue();
		var url = '<emp:url action="queryCusOrgInfoMngList.do"/>?'+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onReturnRegStateCode(date){
		CusOrgInfoMng.branch_addr._obj.element.value=date.id;
		CusOrgInfoMng.branch_addr_displayname._obj.element.value=date.label;
	};

	//新增
	function doUpdCusOrgInfoMng(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.flag;
				if(operMsg=='success'){
		            alert('保存成功!');
		            doReturn();
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
		var result = CusOrgInfoMng._checkAll();
		if(result){
			CusOrgInfoMng._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusOrgInfoMngRecord.do" method="POST">
		<emp:gridLayout id="CusOrgInfoMngGroup" title="评估机构分部信息" maxColumn="2">
			<emp:text id="CusOrgInfoMng.serno" label="申请流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusOrgInfoMng.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusOrgInfoMng.branch_type" label="类型" required="true" dictname="STD_ZB_BRANCH_TYPE" />
			<emp:text id="CusOrgInfoMng.branch_addr" label="地址" required="false" hidden="true"/>
			<emp:pop id="CusOrgInfoMng.branch_addr_displayname" label="地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgInfoMng.street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusOrgInfoMng.duty_man" label="负责人" maxlength="30" required="true" />
			<emp:text id="CusOrgInfoMng.phone" label="联系电话" maxlength="30" required="true" dataType="Phone"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updCusOrgInfoMng" label="修改" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
