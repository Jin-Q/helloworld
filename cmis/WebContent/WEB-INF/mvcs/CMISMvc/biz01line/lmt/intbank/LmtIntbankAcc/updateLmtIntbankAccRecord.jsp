<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshLmtIntbankDetail() {
		LmtIntbankAcc_tabs.tabs.LmtIntbankDetail_tab.refresh();
	};
	function doReturn() 
	{		
		var url = '<emp:url action="queryLmtIntbankAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function checkAmt()
	{
		var froze_amt = LmtIntbankAcc.froze_amt._getValue();
		var odd_amt = LmtIntbankAcc.odd_amt._getValue();//可用额度
		if(eval(froze_amt)>eval(odd_amt))
		{
			alert("冻结额度不能大于可用额度！");
			LmtIntbankAcc.froze_amt._setValue("");
		}
	}
	function doForze(){
		if(LmtIntbankAcc._checkAll()){
			var form = document.getElementById("submitForm");
			LmtIntbankAcc._toForm(form);
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
						var url = '<emp:url action="queryLmtIntbankAccList.do"/>?';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("发生异常！");
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
		}
	};
	
	

	/*--user code begin--*/
	function doLoad()
	{
		LmtIntbankAcc.froze_amt._setValue("");
	}
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateLmtIntbankAccRecord.do" method="POST">
		<emp:gridLayout id="LmtIntbankAccGroup" title="同业客户授信台帐" maxColumn="2">
			<emp:text id="LmtIntbankAcc.serno" label="业务编号" maxlength="32" required="true" readonly="true" colSpan="2" hidden="true"/>
			<emp:text id="LmtIntbankAcc.agr_no" label="协议编号" maxlength="32" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIntbankAcc.batch_cus_no" label="批量客户编号" maxlength="32" required="false" readonly="true" />
			<emp:text id="LmtIntbankAcc.same_org_cnname" label="客户名称" required="false" readonly="true" />
			<emp:select id="LmtIntbankAcc.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="LmtIntbankAcc.lmt_amt" label="授信总额(元)" maxlength="18" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIntbankAcc.odd_amt" label="可用额度(元)" maxlength="18" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIntbankAcc.froze_amt" label="冻结额度(元)" maxlength="18" required="true" dataType="Currency" onblur="checkAmt()"/>
			<emp:date id="LmtIntbankAcc.start_date" label="授信起始日期" required="false" readonly="true"/>
			<emp:date id="LmtIntbankAcc.end_date" label="授信到期日期" required="false" readonly="true"/>
			<emp:pop id="LmtIntbankAcc.manager_id_displayname" label="责任人" url="" readonly="true" required="false" />
			<emp:pop id="LmtIntbankAcc.manager_br_id_displayname" label="管理机构" url="null" readonly="true" />
			<emp:select id="LmtIntbankAcc.lmt_status" label="额度状态" required="false" dictname="STD_LMT_STATUS" defvalue="20" readonly="true"/>
			<emp:select id="LmtIntbankAcc.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" readonly="true"/>
			<emp:date id="LmtIntbankAcc.break_date" label="终止日期" required="false" hidden="true"/>
			<emp:pop id="LmtIntbankAcc.manager_id" label="责任人"  required="false" url="" hidden="true"/>
			<emp:pop id="LmtIntbankAcc.manager_br_id" label="管理机构" url="null" required="false" hidden="true"/>
			<emp:text id="LmtIntbankAcc.cus_id" label="客户码" maxlength="32" required="false" hidden="true" />
		</emp:gridLayout>
	

		<div align="center">
			<emp:button id="forze" label="冻结"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
