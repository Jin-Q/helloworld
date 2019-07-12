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
<style type="text/css">

.emp_field_text_input2 {
	border:1px solid #BCD7E2;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	function doAddCusIndivInsu(){
		if(cheakTotAmt()){
			var form = document.getElementById("submitForm");
			var result = CusIndivInsu._checkAll();
			if(result){
				CusIndivInsu._toForm(form)
				toSubmitForm(form);
			}//else alert("请输入必填项！");
		}
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
						var paramStr="CusIndivInsu.cus_id="+CusIndivInsu.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusIndivInsurancesAddPage.do"/>&'+paramStr;
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
		var paramStr="CusIndivInsu.cus_id="+CusIndivInsu.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusIndivInsurancesList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}

	function CheckRegDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		var openDay = '${context.OPENDAY}';
		//登记日期的效验 
		if(start!=null && start!="" ){
			if(start>openDay){
				alert("投保日期应小于等于当前日期！");
				date1._obj.element.value = "";
				return;	
			}
			//到期日期的效验
			if(end!=null && end!="" ){
				if(end<=start){
					alert("到期日期应大投保日期！");
					date2._obj.element.value = "";
					return;
				}
			}
		}
		//到期日校验
	/*	if(end!=null && end!="" ){
			if(end<openDay){
				alert('到期日期应大于当前日期！');
				date2._obj.element.value = "";
				return;
			}
		}*/
	}
	
	function doReturn(){
		goback();
	}

	function cheakAmt(amt){
		var getAmt = parseFloat(amt._getValue());
		var ins_val = CusIndivInsu.indiv_ins_val._getValue();//保单现有价值
		var ins_tot_amt = CusIndivInsu.indiv_ins_tot_amt._getValue();//应缴保费总额
		var ins_amt = CusIndivInsu.indiv_ins_amt._getValue();//保险金额
		if(ins_amt!=null&&ins_amt!=''&&ins_tot_amt!=null&&ins_tot_amt!=''){
			if(parseFloat(ins_amt)<parseFloat(ins_tot_amt)){
				alert('保险金额应大于等于应缴保费总额！');
				CusIndivInsu.indiv_ins_amt._setValue('');
			}
		}
		if(ins_amt!=null&&ins_amt!=''&&ins_val!=null&&ins_val!=''){
			if(parseFloat(ins_amt)<parseFloat(ins_val)){
				alert('保单现有价值应小于保险金额！');
				CusIndivInsu.indiv_ins_val._setValue('');
			}
		}
	}
	
	function cheakTotAmt(){
	    //应缴保费总额parseFloat
		var totAmt=CusIndivInsu.indiv_ins_tot_amt._getValue();
		//已缴保费
		var insAmt=CusIndivInsu.indiv_ins_fee._getValue();
		if(totAmt==""){
			totAmt=0;
		}
		if(insAmt==""){
			insAmt=0;
		}
		totAmt = parseFloat(totAmt);
		insAmt = parseFloat(insAmt);
		if(isNaN(totAmt)){
			alert("应缴保费总额输入有误！");
			CusIndivInsu.indiv_ins_tot_amt._obj.element.value="";
			return false;
		}else{
			if(isNaN(insAmt)){
				return false;
			}else{
				if(totAmt<insAmt){
	                alert("[应缴保费总额]不能小于[已缴保费] ");
	                CusIndivInsu.indiv_ins_tot_amt._setValue('');
	                return false;
	             }else return true;
			}
		}
	}
	
	function cheakInsAmt(){
		//已缴保费
		var insAmt=CusIndivInsu.indiv_ins_fee._getValue();
		 //应缴保费总额
		var totAmt=CusIndivInsu.indiv_ins_tot_amt._getValue();
		if(totAmt==""){
			totAmt=0;
		}
		if(insAmt==""){
			insAmt=0;
		}
		totAmt = parseFloat(totAmt);
		insAmt = parseFloat(insAmt);
		if(isNaN(insAmt)){
			alert("保险费 输入有误！");
			CusIndivInsu.indiv_ins_fee._obj.element.value="";
			return false;
		}else{
			if(isNaN(totAmt)){
				return false;
			}else{
				if(totAmt<insAmt){
	                alert("[应缴保费总额]不能小于[已缴保费] ");
	                CusIndivInsu.indiv_ins_fee._setValue('');
	                return false;
	             }else return true;
			}
		}
	}
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusIndivInsurancesRecord.do" method="POST">
		<emp:gridLayout id="CusIndivInsuGroup" title="持有保险信息" maxColumn="2">
			<emp:text id="CusIndivInsu.indiv_ins_id" label="保险编号" maxlength="40" required="false" readonly="true" hidden="true" />
			<emp:text id="CusIndivInsu.indiv_ins_cvg" label="保险名称" required="true" maxlength="80" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusIndivInsu.indiv_ins_com" label="保险公司" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusIndivInsu.indiv_ins_typ" label="保险种类" required="true" dictname="STD_ZB_INV_INS_TYP" />
			<emp:text id="CusIndivInsu.policyholders" label="投保人" maxlength="60" required="true" />
			<emp:text id="CusIndivInsu.beneficiaries" label="受益人" maxlength="60" required="true" />
			<emp:text id="CusIndivInsu.indiv_ins_sub" label="保险标的" maxlength="16" required="true" />
			<emp:text id="CusIndivInsu.indiv_ins_val" label="保单现有价值(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusIndivInsu.indiv_ins_val)"/>
			<emp:text id="CusIndivInsu.indiv_ins_tot_amt" label="应缴保费总额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakTotAmt()"/>
			<emp:text id="CusIndivInsu.indiv_ins_fee" label="已缴保费(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakInsAmt()"/>
			<emp:text id="CusIndivInsu.indiv_ins_amt" label="保险金额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusIndivInsu.indiv_ins_amt)"/>
			<emp:date id="CusIndivInsu.indiv_ins_str_dt" label="投保日期" required="true" onblur="CheckRegDate(CusIndivInsu.indiv_ins_str_dt,CusIndivInsu.indiv_ins_end_dt);"/>
			<emp:date id="CusIndivInsu.indiv_ins_end_dt" label="到期日期" required="true" onblur="CheckRegDate(CusIndivInsu.indiv_ins_str_dt,CusIndivInsu.indiv_ins_end_dt);"/>
			<emp:text id="CusIndivInsu.indiv_ins_und" label="承保公司" maxlength="80" required="false" hidden="true"/>
			<emp:select id="CusIndivInsu.indiv_ins_status" label="抵押状况" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="CusIndivInsu.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivInsu.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusIndivInsu.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
			<emp:date id="CusIndivInsu.input_date" label="登记日期" required="false" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="CusIndivInsu.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusIndivInsu.last_upd_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="CusIndivInsu.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusIndivInsu" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>