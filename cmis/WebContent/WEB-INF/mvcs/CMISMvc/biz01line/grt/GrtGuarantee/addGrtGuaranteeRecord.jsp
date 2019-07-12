<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
String guarContNo = (String)request.getParameter("guar_cont_no");
%>
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
		var guarContNo = '<%=guarContNo%>';
		GrtGuarantee.guar_cont_no._setValue(guarContNo);
	}
	//客户pop返回事件
	function returnCus(data){
		GrtGuarantee.cus_id._setValue(data.cus_id._getValue());
		GrtGuarantee.cus_id_displayname._setValue(data.cus_name._getValue());
		checkExit();
	}
	//异步校验所选择客户是否已经选择过，或者是否为借款人客户。
	function checkExit(){
		var guar_co_no ='<%=guarContNo%>';
		var url = "<emp:url action='checkCusExist.do'/>&cus_id="+GrtGuarantee.cus_id._getValue()+"&guar_cont_no="+guar_co_no;	
		var callback = {
			success : "doReturnMethod",
			isJSON : true
		};
		
		EMPTools.ajaxRequest('GET', url, callback);
	}
	function doReturnMethod(json, callback){
		var msg = json.msg;
		if('true'==json.ccr_result){
			//未发现重名客户
		}else if('false'==json.ccr_result){
			alert(msg);
			GrtGuarantee.cus_id._setValue("");
			GrtGuarantee.cus_id_displayname._setValue("");
		}else{
			alert('查询保证人信息表出错!');
		}	
	}
	function doNext(){
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
					alert("保存成功！");
					doReturn();
				}else {
					alert("保存失败！");
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
		var url = '<emp:url action="addGrtGuaranteeRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);

	}
    function doReturn() {
		var guar_cont_no ='<%=guarContNo%>';
		var url = '<emp:url action="queryGrtGuaranteeList.do"/>&menuIdTab=ybCount&guar_cont_no='+guar_cont_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addGrtGuaranteeRecord.do" method="POST">
		
		<emp:gridLayout id="GrtGuaranteeGroup" title="保证人信息" maxColumn="2">
			<emp:text id="GrtGuarantee.guar_id" label="保证编码 " maxlength="40" required="false" hidden="true"/>
			<emp:pop id="GrtGuarantee.cus_id" label="保证人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="true"/>
			<emp:text id="GrtGuarantee.cus_id_displayname" label="保证人名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="GrtGuarantee.guar_type" label="保证形式" required="true" dictname="STD_GUAR_FORM" />
			<emp:select id="GrtGuarantee.is_spadd" label="是否为追加担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="GrtGuarantee.guar_amt" label="担保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="GrtGuarantee.guar_cont_no" label="合同编号" maxlength="30" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="确定" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

