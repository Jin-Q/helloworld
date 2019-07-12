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
	function doAddCusIndivIncome(){
		var form = document.getElementById("submitForm");
		var result = CusIndivIncome._checkAll();
		if(result){
			CusIndivIncome._toForm(form)
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
						var paramStr="CusIndivIncome.cus_id="+CusIndivIncome.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusIndivIncomeAddPage.do"/>&'+paramStr;
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
		var paramStr="CusIndivIncome.cus_id="+CusIndivIncome.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusIndivIncomeList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function checkYear(){
		var year0 = CusIndivIncome.indiv_sur_year._obj.element.value;
		if(year0!=null && year0!=""){
			if(CheckYearBeforeToday(year0)){
			}else {
				CusIndivIncome.indiv_sur_year._obj.element.value="";
			}
		}
	}
	
	function doReturn(){
		goback();
	}
	
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusIndivIncomeRecord.do" method="POST">
		<emp:gridLayout id="CusIndivIncomeGroup" title="个人收入情况" maxColumn="2">
			<emp:text id="CusIndivIncome.indiv_sur_year" label="调查年份" maxlength="4" required="true" onblur="checkYear()" />
			<emp:select id="CusIndivIncome.indiv_deposits" label="收入来源" required="true" dictname="STD_ZB_INDIV_DEPOS"/>
			<emp:text id="CusIndivIncome.indiv_ann_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="CusIndivIncome.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivIncome.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusIndivIncome.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusIndivIncome.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusIndivIncome.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusIndivIncome.last_upd_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="CusIndivIncome.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusIndivIncome" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>