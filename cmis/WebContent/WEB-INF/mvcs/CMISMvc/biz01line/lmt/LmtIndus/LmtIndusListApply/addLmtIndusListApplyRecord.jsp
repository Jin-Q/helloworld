<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String serno= (String)request.getParameter("serno");
%>
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
};
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function returnCus(data){
		LmtIndusListApply.cus_id._setValue(data.cus_id._getValue());
		LmtIndusListApply.cus_name._setValue(data.cus_name._getValue());
		checkIndusType(data.cus_id._getValue());
	};
	//行业名单唯一校验
	function checkIndusType(cus_id){
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

				}else {
					alert("此客户已存在于行业名单中!");
					LmtIndusListApply.cus_id._setValue('');
					LmtIndusListApply.cus_name._setValue('');
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
		var url="<emp:url action='checkUniqueType.do'/>&type=indusList&value="+cus_id;
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	function doSubmits(){
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
				var serno = jsonstr.serno;
				if(flag=='success'){
		            alert('新增成功!');
					//var url = '<emp:url action="getLmtIndusListApplyUpdatePage.do"/>?serno='+serno+'&cus_id='+LmtIndusListApply.cus_id._getValue();
					var url = '<emp:url action="queryLmtIndusListApplyList.do"/>?serno='+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = LmtIndusListApply._checkAll();
		if(result){
			LmtIndusListApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusListApplyList.do"/>?serno=<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtIndusListApplyRecord.do" method="POST">
		
		<emp:gridLayout id="LmtIndusListApplyGroup" title="行业名单申请" maxColumn="2">
			<emp:text id="LmtIndusListApply.serno" label="业务编号" maxlength="40" required="true"
			 defvalue="<%=serno%>" colSpan="2" readonly="true" />
			<emp:pop id="LmtIndusListApply.cus_id" label="客户码" required="true"
			url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200')and cus_status='20'&returnMethod=returnCus"  />
			<emp:text id="LmtIndusListApply.cus_name" label="客户名称" colSpan="2"
			 readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="LmtIndusListApply.is_do_limit" label="是否进行额度设置" 
			defvalue="2" required="true" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="LmtIndusListApply.status" label="状态" required="true" 
			readonly="true" dictname="STD_ZB_LIST_STATUS" defvalue="001" />
			<emp:textarea id="LmtIndusListApply.memo" label="备注" maxlength="250" required="false" colSpan="2" />			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>