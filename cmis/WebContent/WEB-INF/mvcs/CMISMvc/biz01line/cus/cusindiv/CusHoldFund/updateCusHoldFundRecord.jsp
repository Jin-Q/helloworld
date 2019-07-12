<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>持有资金信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function checkSubscrDate(){
		var subscr_date = CusHoldFund.subscr_date._getValue();
		var openDay = '${context.OPENDAY}';
		if(subscr_date > openDay){
			alert("认购时间应小于等于当前日期");
		CusHoldFund.subscr_date._setValue("");
		}
	}

	function CheckDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		var openDay = '${context.OPENDAY}';
		//登记日期的效验 
		if(start!=null && start!="" ){
			if(start>openDay){
				alert("开始日期应小于等于当前日期！");
				date1._obj.element.value = "";
				return;	
			}
			//到期日期的效验
			if(end!=null && end!="" ){
				if(end<=start){
					alert("到期日期应大于开始日期！");
					date2._obj.element.value = "";
					return;
				}
			}
		}
	/*	if(end!=null&&end!=""){
			if(openDay>end){
				alert("到期日期应大于等于当前日期！");
				date2._obj.element.value = "";
				return;
			}
		}*/
	}

	//保存修改
  	function doUpdateHoldFund(){
		var form = document.getElementById("submitForm");
		var result = CusHoldFund._checkAll();
		if(result){
			CusHoldFund._toForm(form)
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
					alert("修改成功");
					doReturn();
			    }else {
				 alert(flag);
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}

  	//返回列表页面
  	function doReturn(){
  		var editFlag = '${context.EditFlag}';
  		var paramStr="CusHoldFund.cus_id="+CusHoldFund.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusHoldFundList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusHoldFundRecord.do" method="POST">
		<emp:gridLayout id="CusHoldFundGroup" title="持有基金信息" maxColumn="2">
			<emp:text id="CusHoldFund.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusHoldFund.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
			<emp:text id="CusHoldFund.prod_name" label="产品名称" maxlength="80" required="true" />
			<emp:select id="CusHoldFund.fund_type" label="理财类型" required="false" dictname="STD_FUND_TYPE" defvalue="1"/>
			<emp:date id="CusHoldFund.subscr_date" label="认购时间" required="true" onblur="checkSubscrDate()"/>
			<emp:text id="CusHoldFund.hold_shr" label="持有份额" maxlength="16" required="true" dataType="Int"/>
			<emp:date id="CusHoldFund.start_date" label="开始时间"  required="true" onblur="CheckDate(CusHoldFund.start_date,CusHoldFund.end_date);"/>
			<emp:date id="CusHoldFund.end_date" label="到期时间" required="true" onblur="CheckDate(CusHoldFund.start_date,CusHoldFund.end_date);"/>
			<emp:text id="CusHoldFund.expect_income_rate" label="预期收益率" maxlength="8" required="true" dataType="Percent"/>
			<emp:text id="CusHoldFund.acct_no" label="账号" maxlength="32" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="CusHoldFundGroup" title="登记信息" maxColumn="2">
			<emp:text id="CusHoldFund.input_id_displayname" label="登记人"   required="false" readonly="true" defvalue="$currentUserId"/>
			<emp:text id="CusHoldFund.input_br_id_displayname" label="登记机构"   required="false" readonly="true" defvalue="$organNo"/>
			<emp:text id="CusHoldFund.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusHoldFund.serno" label="流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusHoldFund.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusHoldFund.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateHoldFund" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>