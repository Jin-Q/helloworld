<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdateCusIndivFamLby(){
		var form = document.getElementById("submitForm");
		var result = CusIndivFamLby._checkAll();
		if(result){
			CusIndivFamLby._toForm(form)
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
				if(flag=="修改成功"){
					alert(flag);
					goback();
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
	
	function goback(){
		var paramStr="CusIndivFamLby.cus_id="+CusIndivFamLby.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivFamLbyList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function doReturn(){
		goback();
	}
	
	function CheckRegDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		var openDay = '${context.OPENDAY}';
		//登记日期的效验 
		if(start!=null && start!="" ){
			if(start>openDay){
				alert("债务开始时间应小于等于当前日期！");
				date1._obj.element.value = "";
				return;	
			}
			//到期日期的效验
			if(end!=null && end!="" ){
				if(end<=start){
					alert("债务到期时间应大于债务开始时间！");
					date2._obj.element.value = "";
					return;
				}
			}
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusIndivFamLbyRecord.do" method="POST">
		<emp:gridLayout id="CusIndivFamLbyGroup" maxColumn="2" title="负债情况">
			<emp:text id="CusIndivFamLby.indiv_debt_id" label="负债编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="CusIndivFamLby.indiv_debt_typ" label="负债类型" required="true" dictname="STD_ZB_INV_DE_TYP"/>
			<emp:text id="CusIndivFamLby.indiv_creditor" label="债权人" maxlength="80" required="true" />
			<emp:select id="CusIndivFamLby.indiv_debt_cur" label="负债币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusIndivFamLby.indiv_debt_amt" label="负债金额(元)" maxlength="18" dataType="Currency" required="true" colSpan="2" />
			<emp:date id="CusIndivFamLby.indiv_debt_str_dt" label="债务开始时间" required="true" onblur="CheckRegDate(CusIndivFamLby.indiv_debt_str_dt,CusIndivFamLby.indiv_debt_end_dt)"/>
			<emp:date id="CusIndivFamLby.indiv_debt_end_dt" label="债务到期时间" required="true" onblur="CheckRegDate(CusIndivFamLby.indiv_debt_str_dt,CusIndivFamLby.indiv_debt_end_dt)"/>
			<emp:text id="CusIndivFamLby.indiv_debt_desc" label="负债描述" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:textarea id="CusIndivFamLby.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivFamLby.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusIndivFamLby.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusIndivFamLby.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusIndivFamLby.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusIndivFamLby.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusIndivFamLby.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateCusIndivFamLby" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>