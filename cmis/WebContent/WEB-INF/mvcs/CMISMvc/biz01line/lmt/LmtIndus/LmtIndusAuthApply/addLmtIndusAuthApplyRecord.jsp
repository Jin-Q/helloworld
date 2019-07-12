<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String serno= (String)request.getParameter("serno");
	String single_amt= (String)request.getParameter("single_amt");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function getOrgID(data){
		LmtIndusAuthApply.input_br_id._setValue(data.organno._getValue());
		LmtIndusAuthApply.input_br_id_displayname._setValue(data.organname._getValue());
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusAuthApplyList.do"/>?serno=<%=serno%>&single_amt=<%=single_amt%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*-- 单户限额校验 --*/
	function checkAmt(){
		var lmt_amt = "<%=single_amt%>";
		var check_amt = LmtIndusAuthApply.single_auth_amt._getValue(); //单户授信金额
		if( (lmt_amt - check_amt < 0)  && lmt_amt != "" && check_amt != ""){
			alert("单户授权金额不得大于单户限额");
			LmtIndusAuthApply.single_auth_amt._setValue("");
		}
	};
	function doSubmits(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("新增失败!\n在此行业分类与担保方式下,已存在该机构的授权\n或"+e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
		            alert('新增成功!');
		            doReturn();
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
		var result = LmtIndusAuthApply._checkAll();
		if(result){
			LmtIndusAuthApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtIndusAuthApplyRecord.do" method="POST">
		
		<emp:gridLayout id="LmtIndusAuthApplyGroup" title="行业授权申请表" maxColumn="2">
			<emp:text id="LmtIndusAuthApply.serno" label="业务编号" maxlength="40" required="true"
			 defvalue="<%=serno%>" readonly="true" colSpan="2" />
			<emp:pop id="LmtIndusAuthApply.input_br_id_displayname" label="申请机构"  required="true"  
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />	
			<emp:text id="LmtIndusAuthApply.input_br_id" label="申请机构" maxlength="20" required="true" hidden="true"/>
			<emp:select id="LmtIndusAuthApply.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" colSpan="2"/>
			<emp:text id="LmtIndusAuthApply.single_auth_amt" label="单户授权金额(元)" maxlength="18" 
			required="true" dataType="Currency" onblur="checkAmt()" colSpan="2"/>
			<emp:select id="LmtIndusAuthApply.status" label="状态" required="false" defvalue="00" 
			hidden="true" dictname="STD_DRFPO_STATUS" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>