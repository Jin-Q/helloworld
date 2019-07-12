<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%	
	String cus_id=request.getParameter("cus_id"); 
	String app_serno=request.getParameter("app_serno");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusOrgInfoList.do"/>?cus_id='+"<%=cus_id%>"+'&serno='+"<%=app_serno%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doUpdateRecord(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='success'){
		            alert('修改成功!');
		            //doReturn();
				}else {
					alert('修改失败!');
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
		var result = CusOrgInfo._checkAll();
		if(result){
			CusOrgInfo._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};

    function onReturnRegStateCode(date){
    	CusOrgInfo.branch_addr._obj.element.value=date.id;
    	CusOrgInfo.branch_addr_displayname._obj.element.value=date.label;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusOrgInfoRecord.do" method="POST">
		<emp:gridLayout id="CusOrgInfoGroup" title="评估机构分部信息" maxColumn="2">
			<emp:text id="CusOrgInfo.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusOrgInfo.branch_type" label="类型" required="true" dictname="STD_ZB_BRANCH_TYPE" />
			<emp:text id="CusOrgInfo.branch_addr" label="地址" required="false" hidden="true"/>
			<emp:pop id="CusOrgInfo.branch_addr_displayname" label="地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgInfo.street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusOrgInfo.duty_man" label="负责人" maxlength="30" required="true" />
			<emp:text id="CusOrgInfo.phone" label="联系电话" maxlength="30" required="true" dataType="Phone"/>
			<emp:text id="CusOrgInfo.serno" label="申请流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusOrgInfo.app_serno" label="业务流水号" maxlength="30" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateRecord" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
