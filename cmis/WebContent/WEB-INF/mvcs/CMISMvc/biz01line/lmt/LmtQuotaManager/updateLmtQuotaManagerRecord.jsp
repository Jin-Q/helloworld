<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	//限定到期日期
	function CheckDt(){
		var openDay='${context.OPENDAY}';
		start = LmtQuotaManager.start_date._getValue();
		expiring = LmtQuotaManager.end_date._getValue();
		
		if (start!=null && start!="" &&expiring!=null && expiring!=""){
			var flag = CheckDate1BeforeDate2(start,expiring);
			if(!flag){
				alert("到期日期要大于开始日期！！");
				LmtQuotaManager.end_date._setValue("");
				return false;
			}			
		}
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(openDay,expiring);
			if(!flag){
				alert("到期日期要大于当前日期！！");
				LmtQuotaManager.end_date._setValue("");
				return false;
			}
		}
	    return true;		
	 };
	/****** 金额校验 *******/
	function checkAmt(obj){
		var sig_amt_quota = LmtQuotaManager.sig_amt_quota._getValue(); //单户授信限额
		var single_amt_quota = LmtQuotaManager.single_amt_quota._getValue(); //授信限额
		var sig_loan_quota = LmtQuotaManager.sig_loan_quota._getValue(); //单笔贷款限额
		var sig_use_quota = LmtQuotaManager.sig_use_quota._getValue(); //单笔支用限额
		//单户授信限额<=授信限额
		if( (single_amt_quota - sig_amt_quota < 0)  && sig_amt_quota != "" && single_amt_quota != ""){
			obj.value = "";
			alert("'授信限额'应该大于'单户授信限额'");
			return ;
		}
		//单笔贷款限额<=单户授信限额
		if( (sig_amt_quota - sig_loan_quota < 0)  && sig_amt_quota != "" && single_amt_quota != ""){
			obj.value = "";
			alert("'单户授信限额'应该大于'单笔贷款限额'");
			return;
		}
		//单笔支用限额<=单笔贷款限额
		if( (sig_loan_quota - sig_use_quota < 0)  && sig_amt_quota != "" && single_amt_quota != ""){
			obj.value = "";
			alert("'单笔贷款限额'应该大于'单笔支用限额'");
			return;
		}
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
		            window.location.reload();
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
		var result = LmtQuotaManager._checkAll();
		if(result){
			LmtQuotaManager._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtQuotaManagerList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//设置产品的值
	function setProds(data){
		LmtQuotaManager.prd_id._setValue(data[0]);
		LmtQuotaManager.prd_id_displayname._setValue(data[1]);
		checkIndusType();
	};
	//业务品种校验
	function checkIndusType(){
		var prd_id = LmtQuotaManager.prd_id._getValue();
		var code_id = LmtQuotaManager.code_id._getValue();
		var serno = LmtQuotaManager.serno._getValue();
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
					LmtQuotaManager.prd_id._setValue("");
					LmtQuotaManager.prd_id_displayname._setValue("");
					alert("存在已限额产品!");
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
		var url="<emp:url action='checkUniqueType.do'/>&type=LmtQuotaManager&value="+prd_id+"&code_id="+code_id+"&serno="+serno;
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	function doReturn(){
		if(1==1){
			var url = '<emp:url action="queryLmtQuotaManagerList.do"/>'
			url = EMPTools.encodeURI(url);
			window.location=url;
		}else{
			var url = '<emp:url action="queryLmtQuotaManager4MagList.do"/>'
				url = EMPTools.encodeURI(url);
				window.location=url;
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtQuotaManagerRecord.do" method="POST">
		<emp:gridLayout id="LmtQuotaManagerGroup" title="限额管理" maxColumn="2">
			<emp:text id="LmtQuotaManager.serno" label="业务编号" maxlength="40" required="false" hidden="true"  colSpan="2" />
		<%if("OrgLmtQuota".equals(menuId)){%>
			<emp:text id="LmtQuotaManager.code_id" label="机构代码" readonly="true"  required="true" />
			<emp:text id="LmtQuotaManager.code_id_displayname" label="机构名称" colSpan="2" required="true" 
			cssElementClass="emp_field_text_cusname" readonly="true"/>
		<%} else{%>
			<emp:text id="LmtQuotaManager.code_id" label="客户经理编号" readonly="true" required="true" />
			<emp:text id="LmtQuotaManager.code_id_displayname" label="客户经理名称" colSpan="2" required="true" 
			readonly="true" cssElementClass="emp_field_text_cusname"/>
		<%} %>
			<emp:pop id="LmtQuotaManager.prd_id" label="产品代码" url='showPrdCheckTreeDetails.do?bizline=BL300' returnMethod="setProds"
			 colSpan="2" cssElementClass="emp_field_text_readonly" required="true" />
			<emp:textarea id="LmtQuotaManager.prd_id_displayname" label="产品名称" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtQuotaManager.single_amt_quota" label="授信限额" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(this)"/>
			<emp:text id="LmtQuotaManager.sig_amt_quota" label="单户授信限额" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(this)"/>			
			<emp:text id="LmtQuotaManager.sig_loan_quota" label="单笔贷款限额" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(this)"/>
			<emp:text id="LmtQuotaManager.sig_use_quota" label="单笔支用限额" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(this)"/>
			<emp:date id="LmtQuotaManager.start_date" label="起始日期" required="true" readonly="true"/>
			<emp:date id="LmtQuotaManager.end_date" label="到期日期" required="true" onblur="CheckDt()"/>
			<emp:text id="LmtQuotaManager.manager_id_displayname" label="经办人" required="true" readonly="true" />
			<emp:text id="LmtQuotaManager.manager_br_id_displayname" label="经办机构"  required="true" readonly="true" />
			<emp:text id="LmtQuotaManager.manager_br_id" label="经办机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="LmtQuotaManager.manager_id" label="经办人" maxlength="32" required="false"  hidden="true" />
			<emp:select id="LmtQuotaManager.quota_type" label="限额类型" required="false" dictname="STD_ZB_QUOTA_MAG" hidden="true"/>
			<emp:select id="LmtQuotaManager.approve_status" label="审批状态" dictname="WF_APP_STATUS" hidden="true" />
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
