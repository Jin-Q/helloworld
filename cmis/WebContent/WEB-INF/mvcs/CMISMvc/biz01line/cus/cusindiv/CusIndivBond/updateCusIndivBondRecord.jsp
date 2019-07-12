<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border:1px solid #BCD7E2;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdateCusIndivBond(){
		var form = document.getElementById("submitForm");
		var result = CusIndivBond._checkAll();
		if(result){
			CusIndivBond._toForm(form)
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
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivBond.cus_id="+CusIndivBond.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusIndivBondList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function checkStrData(strDate,endDate){
		var strDt = strDate._obj.element.value;
		var endDt = endDate._obj.element.value;
		var openDay = '${context.OPENDAY}';
		if(strDt!=null && strDt!="" ){
			if(strDt>openDay){
				alert("起始日期不能大于当前日期！");
				strDate._obj.element.value="";
				return;
			}
			if(endDt!=null && endDt!="" ){
				if(strDt>=endDt){
					alert("起始日期要小于 到期日期！");
					endDate._obj.element.value="";
				}
			}
		}
	}
	
	function doReturn(){
		goback();
	}
	
	function cheakAmt(amt){
		var getAmt = parseFloat(amt._getValue());
		if(getAmt<0){
			alert("金额值不能为负数！");
		  	amt._obj.element.value="";
	   	}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusIndivBondRecord.do" method="POST">
		<emp:gridLayout id="CusIndivBondGroup" maxColumn="2" title="持有资本证券信息">
			<emp:text id="CusIndivBond.indiv_bond_id" label="证券编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusIndivBond.indiv_bond_typ" label="证券类别" required="true" dictname="STD_ZB_INV_BON_TYP"/>
			<emp:text id="CusIndivBond.indiv_bond_name" label="证券名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusIndivBond.indiv_bond_eva_amt" label="证券估价总额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusIndivBond.indiv_bond_eva_amt)"/>
			<emp:text id="CusIndivBond.indiv_bond_pub" label="发行商" maxlength="80" required="true" />
			<emp:date id="CusIndivBond.indiv_bond_str_dt" label="持有起始日期" required="true" onfocus="checkStrData(CusIndivBond.indiv_bond_str_dt,CusIndivBond.indiv_bond_end_dt);"/>
			<emp:date id="CusIndivBond.indiv_bond_end_dt" label="持有到期日期" required="false" onfocus="checkStrData(CusIndivBond.indiv_bond_str_dt,CusIndivBond.indiv_bond_end_dt);"/>
			<emp:select id="CusIndivBond.indiv_bond_status" label="抵押状况" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:textarea id="CusIndivBond.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivBond.input_id_displayname" label="登记人"  required="false" hidden="true"/>
			<emp:text id="CusIndivBond.input_br_id_displayname" label="登记机构"  required="false" hidden="true"/>
			<emp:date id="CusIndivBond.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:text id="CusIndivBond.last_upd_id_displayname" label="更新人"  required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusIndivBond.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusIndivBond.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusIndivBond.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
            <emp:text id="CusIndivBond.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
            <emp:text id="CusIndivBond.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateCusIndivBond" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>