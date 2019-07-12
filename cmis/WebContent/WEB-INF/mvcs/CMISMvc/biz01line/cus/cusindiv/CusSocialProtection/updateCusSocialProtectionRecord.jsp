<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>社会保障信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdateCusSocialProtection(){
		var form = document.getElementById("submitForm");
		var result = CusSocialProtection._checkAll();
		if(result){
			CusSocialProtection._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
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
					goback();
				}else if(flag=="exists"){
					alert("存在【公积金账号】和【单位公积金账号】的数据,修改失败！");
					CusSocialProtection.provid_fund_id._setValue("");
					CusSocialProtection.ent_provid_fund_id._setValue("");
					return;
				}else{
					alert("修改失败！");
					CusSocialProtection.provid_fund_id._setValue("");
					CusSocialProtection.ent_provid_fund_id._setValue("");
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
	}

	function goback(){
		var editFlag = '${context.EditFlag}';
		var paramStr="CusSocialProtection.cus_id="+CusSocialProtection.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusSocialProtectionList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}

	//校验参保时间
  	function checkTime(){
  	  	var openDay = '${context.OPENDAY}';
		var timeInter = CusSocialProtection.time_interzone._getValue();
		if(timeInter!=null&&timeInter!=''){
			if(timeInter>openDay){
				alert('参保时间不能大于当前日期！');
				CusSocialProtection.time_interzone._setValue('');
			}
		}
  	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusSocialProtectionRecord.do" method="POST">
		<emp:gridLayout id="CusSocialProtectionGroup" title="社会保障信息表" maxColumn="2">
			<emp:date id="CusSocialProtection.time_interzone" label="参保时间" required="true" onblur="checkTime()"/>
			<emp:select id="CusSocialProtection.with_cust_rela" label="与客户关系" dictname="STD_ZB_INDIV_CUS" required="true" defvalue="1"/>
			<emp:text id="CusSocialProtection.social_prot_id" label="社会保障号码" maxlength="40" required="true" />
			<emp:text id="CusSocialProtection.provid_fund_id" label="公积金账号" maxlength="40" required="true" />
			<emp:text id="CusSocialProtection.ent_provid_fund_id" label="单位公积金账号" maxlength="40" required="true" />
			<emp:text id="CusSocialProtection.provid_fund_pay_monthly" label="公积金月缴额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusSocialProtection.provid_fund_bal" label="公积金余额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusSocialProtection.supp_pay_monthly" label="补充月缴额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusSocialProtection.supp_provid_fund_bal" label="补充公积金余额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusSocialProtection.family_pay_monthly_total" label="家庭月缴合计" maxlength="18" required="true" dataType="Currency" />
			<emp:checkbox id="CusSocialProtection.join_insur" label="参保情况" dictname="STD_JOIN_INSUR" colSpan="2" required="true"/>
			<emp:text id="CusSocialProtection.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusSocialProtection.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusSocialProtection.input_date" label="登记日期" maxlength="10" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusSocialProtection.serno" label="流水号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="CusSocialProtection.cus_id" label="客户码" maxlength="30" required="true" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateCusSocialProtection" label="保 存"/>
			<emp:button id="reset" label="重 置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
