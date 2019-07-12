<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshFncAccPayable() {
		FncDetailBase_tabs.tabs.FncAccPayable_tab.refresh();
	};
	function refreshFncAccReceivable() {
		FncDetailBase_tabs.tabs.FncAccReceivable_tab.refresh();
	};
	function refreshFncAssure() {
		FncDetailBase_tabs.tabs.FncAssure_tab.refresh();
	};
	function refreshFncFixedAsset() {
		FncDetailBase_tabs.tabs.FncFixedAsset_tab.refresh();
	};
	function refreshFncInventory() {
		FncDetailBase_tabs.tabs.FncInventory_tab.refresh();
	};
	function refreshFncInvestment() {
		FncDetailBase_tabs.tabs.FncInvestment_tab.refresh();
	};
	function refreshFncLoan() {
		FncDetailBase_tabs.tabs.FncLoan_tab.refresh();
	};
	function refreshFncOtherPayable() {
		FncDetailBase_tabs.tabs.FncOtherPayable_tab.refresh();
	};
	function refreshFncOtherRecv() {
		FncDetailBase_tabs.tabs.FncOtherRecv_tab.refresh();
	};
	function refreshFncProject() {
		FncDetailBase_tabs.tabs.FncProject_tab.refresh();
	};

	function refreshFncOrDebt() {
		FncDetailBase_tabs.tabs.FncOrDebt_tab.refresh();
	};

	function doUpdateFncDetailBase(){

		var form = document.getElementById("submitForm");
		var result = FncDetailBase._checkAll();
		if(result){
			FncDetailBase._toForm(form)
			
			toSubmitForm(form);
		}else alert("请输入必填项！");
	};


	function toSubmitForm(form){
		  var handleSuccess = function(o){ EMPTools.unmask();
				if(o.responseText !== undefined) {
							try {
								//alert(o.responseText);
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							
							if(flag=="update"){
								if(confirm("该时点的报表信息已存在！请选其他的时点")){
							     }else goback();
						    }else{
								confirm("修改成功");
							}
				}
			};
			var handleFailure = function(o){ EMPTools.unmask();	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
	};

	function doReturn(){
        var paramStr="FncDetailBase.cus_id="+FncDetailBase.cus_id._obj.element.value;
		var url = '<emp:url action="queryFncDetailBaseList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;		
	}

	
	String.prototype.trim = function()
	{
			return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function checkYM(obj){
		  var re = new RegExp(/\d{4}((0[1-9])|(1[0-2]))/); 
		  var openDay="${context.OPENDAY}";
		
		  yyyymm=openDay.substring(0,4)+openDay.substring(5,7);
	;
	     if (re.test(obj.value)==false&&obj.value.trim().length>0){
			alert("您输入的年月有误,输入格式应该为YYYYMM！");
			this.value="";
			return obj.focus();
	     }
	     if (obj.value>yyyymm){
				alert("您输入的年月不能大于当前年月！");
				this.value="";
				return obj.focus();
		     }
	     
	};
	

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateFncDetailBaseRecord.do" method="POST">
		<emp:gridLayout id="FncDetailBaseGroup" title="报表明细基表" maxColumn="2">
			<emp:text id="FncDetailBase.pk" label="PK" maxlength="40" hidden="true" required="true" readonly="true" />
			<emp:text id="FncDetailBase.cus_id" label="客户码" maxlength="30" readonly="true" required="true" />
			<emp:text id="FncDetailBase.fnc_ym" label="年月" maxlength="6"  required="true" onblur="checkYM(this)"/>
			<emp:text id="FncDetailBase.input_id" label="登记人" hidden="true"  maxlength="20" readonly="true" required="false" />
			<emp:text id="FncDetailBase.input_br_id" label="登记机构" hidden="true"  maxlength="20" readonly="true" required="false" />
			<emp:date id="FncDetailBase.input_date" label="登记日期" hidden="true"  required="false" readonly="true" />
			<emp:text id="FncDetailBase.last_upd_id" label="更新人" maxlength="20" hidden="true"  required="false" readonly="true" defvalue="$currentUserId"/>
			<emp:date id="FncDetailBase.last_upd_date" label="更新日期" required="false" hidden="true"  readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>

		<div align=center>
			<emp:button id="updateFncDetailBase" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
		
	</emp:form>
	<emp:tabGroup id="FncDetailBase_tabs" mainTab="FncAccPayable_tab">
		<emp:tab id="FncAccPayable_tab" label="应付账款" url="queryFncDetailBaseFncAccPayableList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;" initial="true"/>
				
		<emp:tab id="FncAccReceivable_tab" label="应收账款" url="queryFncDetailBaseFncAccReceivableList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
				
		
		<emp:tab id="FncInventory_tab" label="主要存货" url="queryFncDetailBaseFncInventoryList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
				
		<emp:tab id="FncInvestment_tab" label="主要投资" url="queryFncDetailBaseFncInvestmentList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
			
		<emp:tab id="FncOtherPayable_tab" label="其它应付款" url="queryFncDetailBaseFncOtherPayableList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
				
		<emp:tab id="FncOtherRecv_tab" label="其它应收款" url="queryFncDetailBaseFncOtherRecvList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
				
		<emp:tab id="FncProject_tab" label="在建工程" url="queryFncDetailBaseFncProjectList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=${context.FncDetailBase.fnc_ym}"/>
		
		<emp:tab id="FncOrDebt_tab" label="或有负债" url="queryFncDetailBaseFncOrDebtList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/>
				
	</emp:tabGroup>
</body>
</html>
</emp:page>


			
		<!-- emp:tab id="FncLoan_tab" label="借款明细" url="queryFncDetailBaseFncLoanList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/-->
			
	<!-- emp:tab id="FncAssure_tab" label="对外担保及表外业务" url="queryFncDetailBaseFncAssureList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/ -->
				
<!-- emp:tab id="FncFixedAsset_tab" label="固定资产" url="queryFncDetailBaseFncFixedAssetList.do" reqParams="FncDetailBase.pk=${context.FncDetailBase.pk}&FncDetailBase.cus_id=${context.FncDetailBase.cus_id}&FncDetailBase.fnc_ym=$FncDetailBase.fnc_ym;"/  -->
