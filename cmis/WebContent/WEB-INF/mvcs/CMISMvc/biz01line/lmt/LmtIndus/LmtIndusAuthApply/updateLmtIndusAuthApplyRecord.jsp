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
		var url = '<emp:url action="queryLmtIndusAuthApplyList.do"/>?serno=${context.serno}&single_amt=${context.single_amt}';
		url = EMPTools.encodeURI(url);
		window.location=url;
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
				if(flag=='success'){
		            alert('保存成功!');
		            //doReturn();
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
		var result = LmtIndusAuthApply._checkAll();
		if(result){
			LmtIndusAuthApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function checkAmt(){
		var lmt_amt = "${context.single_amt}";
		var check_amt = LmtIndusAuthApply.single_auth_amt._getValue(); //单户授信金额
		if( (lmt_amt - check_amt < 0)  && lmt_amt != "" && check_amt != ""){
			alert("单户授权金额不得大于单户限额");
			LmtIndusAuthApply.single_auth_amt._setValue("");
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtIndusAuthApplyRecord.do" method="POST">
		<emp:gridLayout id="LmtIndusAuthApplyGroup" maxColumn="2" title="行业授权申请表">
			<emp:text id="LmtIndusAuthApply.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIndusAuthApply.input_br_id_displayname" label="申请机构"  required="true" readonly="true" colSpan="2"/>
			<emp:select id="LmtIndusAuthApply.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" colSpan="2" readonly="true"/>
			<emp:text id="LmtIndusAuthApply.single_auth_amt" label="单户授权金额(元)" maxlength="18" required="true" colSpan="2"
			 dataType="Currency" onblur="checkAmt()" />
			<emp:select id="LmtIndusAuthApply.status" label="状态" required="false" hidden="true" dictname="STD_DRFPO_STATUS"  />
			<emp:text id="LmtIndusAuthApply.input_br_id" label="申请机构" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
