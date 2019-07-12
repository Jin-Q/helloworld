<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAddCusIndivTax(){
		var form = document.getElementById("submitForm");
		var result = CusIndivTax._checkAll();
		if(result){
			CusIndivTax._toForm(form)
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
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusIndivTax.cus_id="+CusIndivTax.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusIndivTaxAddPage.do"/>&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
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
		var paramStr="CusIndivTax.cus_id="+CusIndivTax.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusIndivTaxList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}	
	//输入日期不能大于当前日期
	function CheckDate(obj,errMsg){
		var str_date=obj._getValue();
		var openDay = '${context.OPENDAY}';
		if( str_date==""|| str_date==null){
	        return;
		}
		if(str_date>openDay){
			alert(errMsg);
		    obj._obj.element.value="";
		}
	}
	
	function doReturn(){
		goback();
	}
	
	function checkTaxAmt(obj,value,errMsg){
		if(obj){
			if(obj._getValue()-value>0){
		  		alert(errMsg);
		  		obj._setValue("");
	 		}
	 	}
		return false;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusIndivTaxRecord.do" method="POST">
		<emp:gridLayout id="CusIndivTaxGroup" title="税费缴纳情况" maxColumn="2">
			<emp:select id="CusIndivTax.indiv_taxes" label="税费种类" required="true" dictname="STD_ZB_INV_TAX_TYP" colSpan="2"/>
			<emp:text id="CusIndivTax.indiv_tax_amt" label="应缴纳/支付金额(元)" maxlength="18" required="true" dataType="Currency" onblur="checkTaxAmt(CusIndivTax.indiv_tax_pay_amt,CusIndivTax.indiv_tax_amt._getValue(),'已缴纳/支付金额(元) 不能大于  应缴纳/支付金额(元)')"/>
			<emp:text id="CusIndivTax.indiv_tax_pay_amt" label="已缴纳/支付金额(元)" maxlength="18" required="true" dataType="Currency" onblur="checkTaxAmt(CusIndivTax.indiv_tax_pay_amt,CusIndivTax.indiv_tax_amt._getValue(),'已缴纳/支付金额(元) 不能大于  应缴纳/支付金额(元)')"/>
			<emp:date id="CusIndivTax.indiv_tax_dt" label="缴纳/支付日期" required="true" onblur="CheckDate(CusIndivTax.indiv_tax_dt,'缴纳/支付日期不能大于当前日期')"/>
			<emp:select id="CusIndivTax.indiv_tax_flg" label="是否正常缴纳/支付" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="CusIndivTax.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivTax.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusIndivTax.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusIndivTax.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusIndivTax.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusIndivTax.last_upd_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="CusIndivTax.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusIndivTax.serno" label="编号" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusIndivTax" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>