<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doBack(){
		var paramStr="cus_id="+CusIndivBusiness.cus_id._obj.element.value;
		var stockURL = '<emp:url action="queryCusIndivBusinessList.do"/>&'+paramStr+"&EditFlag=edit";
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	//校验金额
	function checkFunds(){
		var reg_fund = CusIndivBusiness.reg_fund._getValue();
		var paid_in_capt = CusIndivBusiness.paid_in_capt._getValue();
		if(reg_fund!=null&&reg_fund!=''&&paid_in_capt!=null&&paid_in_capt!=''){
			if(parseFloat(paid_in_capt)>parseFloat(reg_fund)){
				alert('实收资本金应小于等于注册资金!');
				CusIndivBusiness.paid_in_capt._setValue('');
			}
		}
	}
	//校验组织机构代码
	function checkCertCode(){
		var certCode =CusIndivBusiness.org_code._obj.element.value;
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				CusIndivBusiness.org_code._obj.element.value="";
			}
		}
	};

	function CheckEndDate(){
		var liceExp = CusIndivBusiness.biz_lice_exp._getValue();
		var openDay='${context.OPENDAY}';
		if(liceExp!=null && liceExp!="" ){
			liceExp = dateLenghtChang(liceExp);
			var flag = CheckDate1BeforeDate2(openDay,liceExp);
			if(!flag){
				alert("当前日期要小于等于输入的日期！");
				CusIndivBusiness.biz_lice_exp._setValue('');
			}
		}
	}

	function dateLenghtChang(date){
		if(date.length==8){
			date = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		}
		return date;
	}

	function doUpdIndivBuis(){
		var form = document.getElementById("submitForm");
		var result = CusIndivBusiness._checkAll();
		if(result){
			CusIndivBusiness._toForm(form)
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
					alert('修改成功!');
					var cusid = CusIndivBusiness.cus_id._getValue();
					var paramStr="cus_id="+cusid;
					var url = '<emp:url action="queryCusIndivBusinessList.do"/>&'+paramStr+"&EditFlag=edit";
					url = EMPTools.encodeURI(url);
					window.location = url;
				 }else {
					 alert("修改失败!" );
					 return;
				 }
			}
		};
		var handleFailure = function(o){
			alert("修改失败!" );
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}

	//校验股权占比
	function checkPercn(){
		var percn = CusIndivBusiness.debit_percn._getValue();
		if(percn!=null&&percn!=''){
			if(parseFloat(percn)<0){
				alert('借款人股权占比不能为负数！');
				CusIndivBusiness.debit_percn._setValue('');
			}
		}
	}

	function checkOpac(){
		var ourBank = CusIndivBusiness.is_ourbank_opac._getValue();
		if(ourBank == "2"){
			CusIndivBusiness.acct_type._obj._renderRequired(false);
		}else{
			CusIndivBusiness.acct_type._obj._renderRequired(true);
		}
	}

	function doLoad(){
		checkOpac();
	}

	//经营场地面积(平方米)
	function cheakAera(){
	   var aera = parseFloat(CusIndivBusiness.opt_aera._obj.element.value);
	   if(isNaN(aera)){
	       alert("面积值应该为数值型！");
	       CusIndivBusiness.opt_aera._obj.element.value="";
	       return ;
	 	}
	   if(aera<= 0){
		   alert("经营场地面积要大于零！");
		   CusIndivBusiness.opt_aera._obj.element.value="";
		   return ;
	    }
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="updateCusIndivBusinessRecord.do" method="POST">
		<emp:gridLayout id="CusIndivBusinessGroup" title="经营信息" maxColumn="2">
			<emp:text id="CusIndivBusiness.serno" label="SERNO" maxlength="40" hidden="true"/>
			<emp:text id="CusIndivBusiness.cus_id" label="客户码" maxlength="40" hidden="true"/>
			<emp:text id="CusIndivBusiness.biz_lice_id" label="营业执照号码" maxlength="40" required="true" />
			<emp:text id="CusIndivBusiness.com_name" label="企业名称" maxlength="80" required="true" />
			<emp:text id="CusIndivBusiness.major_biz" label="主营业务" maxlength="40" required="true" />
			<emp:select id="CusIndivBusiness.indus_type" label="行业类别" dictname="STD_GB_COM_TYPE" required="true" />
			<emp:select id="CusIndivBusiness.org_qlty" label="机构性质" dictname="STD_ORG_QLTY" required="true" />
			<emp:date id="CusIndivBusiness.biz_lice_exp" label="营业执照有效期" required="true" onblur="CheckEndDate()"/>
			<emp:text id="CusIndivBusiness.emp_num" label="雇佣人数" maxlength="10" required="true" dataType="Int"/>
			<emp:text id="CusIndivBusiness.reg_addr" label="注册地址" maxlength="80" required="true" />
			<emp:text id="CusIndivBusiness.opera_field" label="经营场所" maxlength="50" required="true" />
			<emp:select id="CusIndivBusiness.prop_qlty" label="产权性质" dictname="STD_PROP_QLTY" required="true" />
			<emp:text id="CusIndivBusiness.opt_aera" label="经营场地面积(平方米)"  required="true" onblur="cheakAera()"/>
			<emp:text id="CusIndivBusiness.reg_fund" label="注册资金" maxlength="18" dataType="Currency" onblur="checkFunds()"/>
			<emp:text id="CusIndivBusiness.paid_in_capt" label="实收资本金" maxlength="18" dataType="Currency" onblur="checkFunds()"/>
			<emp:text id="CusIndivBusiness.real_controller" label="实际控制人" maxlength="40" required="true" />
			<emp:text id="CusIndivBusiness.debit_percn" label="借款人股权占比" maxlength="10" required="true" dataType="Rate" onblur="checkPercn()"/>
			<emp:select id="CusIndivBusiness.ent_model" label="企业规模" dictname="STD_ZB_ENTERPRISE" required="true" />
			<emp:select id="CusIndivBusiness.is_ourbank_opac" label="是否在我行开户" dictname="STD_ZX_YES_NO" required="true" onchange="checkOpac()"/>
			<emp:select id="CusIndivBusiness.acct_type" label="账户类型" dictname="STD_IND_ACCT_TYPE" required="true" />
			<emp:select id="CusIndivBusiness.is_ourbank_authorize" label="是否在我行授信客户" dictname="STD_ZX_YES_NO" required="true" />
			<emp:text id="CusIndivBusiness.org_code" label="组织机构代码" maxlength="40" onblur="checkCertCode()"/>
			<emp:text id="CusIndivBusiness.tax_reg_no_c" label="税务登记证号（国税）" maxlength="80" required="false" />
			<emp:text id="CusIndivBusiness.tax_reg_no_a" label="税务登记证号（地税）" maxlength="80" required="false" />
			<emp:text id="CusIndivBusiness.ln_card_no" label="贷款卡号" maxlength="40" required="false" />
			<emp:text id="CusIndivBusiness.phone" label="电话" maxlength="20" required="false" />
			<emp:text id="CusIndivBusiness.linkman" label="联系人" maxlength="40" required="false" />
			<emp:text id="CusIndivBusiness.other" label="其他" maxlength="80" required="false" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updIndivBuis" label="修改" />
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>