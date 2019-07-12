<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 240px;
	};
</style>
<script type="text/javascript">
	
   function doLoad(){
	}
	function doFix(){
		var guar_cont_no = GrtGuarantee.guar_cont_no._getValue();
		if(!GrtGuarantee._checkAll()){
			return;
		}
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
					alert("修改成功！");
					doReturn();
				/*	var url = '<emp:url action="queryGrtGuaranteeList.do"/>?guar_cont_no='+guar_cont_no;
					url = EMPTools.encodeURI(url);
					window.location=url;*/
				}else {
					alert("修改失败！");
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
		var form = document.getElementById("submitForm");
		GrtGuarantee._toForm(form);
		var url = '<emp:url action="updateGrtGuaranteeRecord.do"/>?guar_cont_no='+guar_cont_no;
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
				
	}
	function doReturn() {
		var guar_cont_no = GrtGuarantee.guar_cont_no._getValue();
		var url = '<emp:url action="queryGrtGuaranteeList.do"/>&menuIdTab=ybCount&guar_cont_no='+guar_cont_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateGrtGuaranteeRecord.do" method="POST">
		<emp:gridLayout id="GrtGuaranteeGroup" title="保证人信息" maxColumn="2">
			<emp:text id="GrtGuarantee.guar_id" label="保证编码 " maxlength="40" required="false" hidden="true"/>
			<emp:pop id="GrtGuarantee.cus_id" label="保证人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="true" readonly="true"/>
			<emp:text id="GrtGuarantee.cus_id_displayname" label="保证人名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="GrtGuarantee.guar_type" label="保证形式" required="true" dictname="STD_GUAR_FORM" />
			<emp:select id="GrtGuarantee.is_spadd" label="是否为追加担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="GrtGuarantee.guar_amt" label="担保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="GrtGuarantee.guar_cont_no" label="合同编号" maxlength="30" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="fix" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
