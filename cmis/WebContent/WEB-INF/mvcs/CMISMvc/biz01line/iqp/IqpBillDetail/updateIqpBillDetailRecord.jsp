<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="iqpBillDetailComm.jsp" flush="true" /> 
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String status = "";
	
	if(context.containsKey("status")){
		status = (String)context.getDataValue("status");
		if(!"01".equals(status)){
			//不允许修改页面要素
			request.setAttribute("canwrite","");
		}
	}
	String isexists = "";
	if(context.containsKey("isexists")){
		isexists = (String)context.getDataValue("isexists");
		if("1".equals(isexists)){
			//不允许修改页面要素
			request.setAttribute("canwrite","");
		}
	}
	
%>
<emp:page>
<html>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};

</style>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	
	/*--user code begin--*/
	function CheckComInsCode(){
		var certCode = IqpBillDetail.accptr_cmon_code._getValue();
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				IqpBillDetail.accptr_cmon_code._obj.element.value="";
				return false;
			}
		}
	};
	function checkCertCode(){
		var certCode = IqpBillDetail.drwr_org_code._getValue();
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				IqpBillDetail.drwr_org_code._obj.element.value="";
				return false;
			}
		}
	};
	function doOnLoad(){
		//IqpBillDetail.drwr_org_code._obj.addOneButton("orgCode","获取",getOrgCode);
		chageBillType();
		checkIsDis();
		checkIsEbill();
		selectBillType();
	};
    // 如果为商票，则承兑人信息展示为承兑人开户行信息
	function selectBillType(){
		var bill_type = IqpBillDetail.bill_type._getValue();
        if(bill_type == "100"){//银票
        	$(".emp_gridlayout_title:eq(4)").text("承兑行信息");
        }else{
        	$(".emp_gridlayout_title:eq(4)").text("承兑人开户行信息");
        }
	};
	function checkIsEbill(){
		var is_ebill = IqpBillDetail.is_ebill._getValue();
		if(is_ebill == '1'){
			IqpBillDetail.drft_amt._obj._renderReadonly(true);
			IqpBillDetail.bill_isse_date._obj._renderReadonly(true);
			IqpBillDetail.porder_end_date._obj._renderReadonly(true);
			IqpBillDetail.isse_name._obj._renderReadonly(true);
			IqpBillDetail.daorg_no._obj._renderReadonly(true);
			IqpBillDetail.daorg_acct._obj._renderReadonly(true);
			IqpBillDetail.pyee_name._obj._renderReadonly(true);
			IqpBillDetail.paorg_no._obj._renderReadonly(true);
			IqpBillDetail.paorg_acct_no._obj._renderReadonly(true);
			IqpBillDetail.aaorg_no._obj._renderReadonly(true);
			IqpBillDetail.aaorg_acct_no._obj._renderReadonly(true);
			IqpBillDetail.aorg_no._obj._renderReadonly(true);
			IqpBillDetail.porder_addr._setValue("1");
			IqpBillDetail.porder_addr._obj._renderReadonly(true);
			
		}
	}
	function checkIsDis(){
	       var biz_type = '${context.biz_type}';
	       if(biz_type == "07"){
	    	   IqpBillDetail.daorg_acct._obj._renderRequired(true);
	    	   IqpBillDetail.tcont_no._obj._renderRequired(true);
	    	   IqpBillDetail.tcont_amt._obj._renderRequired(true);
	    	   IqpBillDetail.tcont_content._obj._renderRequired(true);
	    	   IqpBillDetail.pyee_name._obj._renderRequired(true);
	    	   IqpBillDetail.paorg_acct_no._obj._renderRequired(true);
	    	   IqpBillDetail.paorg_no._obj._renderRequired(true);
	    	   IqpBillDetail.paorg_name._obj._renderRequired(true);
	       }else{
	    	   IqpBillDetail.daorg_acct._obj._renderRequired(false);
	    	   IqpBillDetail.tcont_no._obj._renderRequired(false);
	    	   IqpBillDetail.tcont_amt._obj._renderRequired(false);
	    	   IqpBillDetail.tcont_content._obj._renderRequired(false);
	    	   IqpBillDetail.pyee_name._obj._renderRequired(false);
	    	   IqpBillDetail.paorg_acct_no._obj._renderRequired(false);
	    	   IqpBillDetail.paorg_no._obj._renderRequired(false);
	    	   IqpBillDetail.paorg_name._obj._renderRequired(false);
	       }
		};
	
	function doReturn(){
		var menuID = '${context.menuIdFather}';
		if(menuID == ''){
			menuID='${context.menuId}';
		}
		if(menuID == 'queryIqpBillDetail'){
			var url = '<emp:url action="queryIqpBillList4Rpd.do"/>&menuId=queryIqpRpddscnt&subMenuId=queryIqpBillList4Rpd&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
		}else if(menuID == 'queryIqpBatchMng'){
			var url = '<emp:url action="queryIqpBillDetailList.do"/>&menuId=${context.menuIdFather}&subMenuId=${context.subMenuIdFather}&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
		}
		else{
			var url = '<emp:url action="queryIqpBillDetailList.do"/>&menuId=${context.menuIdFather}&subMenuId=${context.subMenuIdFather}&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
		}
		url = EMPTools.encodeURI(url); 
	    window.location=url; 
	}
	
	function doSave(){
		if(!IqpBillDetail._checkAll()){
			return;
		}
		var contAmt = IqpBillDetail.tcont_amt._getValue();//贸易合同金额
		var drftAmt = IqpBillDetail.drft_amt._getValue();//票面金额
		if(contAmt){
           if(parseFloat(contAmt) < parseFloat(drftAmt)){
               alert("【贸易合同金额】不能小于【票面金额】,请重新输入！");
               IqpBillDetail.tcont_amt._setValue("");
               return;
           }
	    }
		var form = document.getElementById("submitForm");
		IqpBillDetail._toForm(form);
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
					alert("保存成功！");
				}else {
					alert(jsonstr.msg);
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

		var url = '<emp:url action="updateIqpBillDetailRecord.do"/>?batch_no=${context.batch_no}';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) ;
	};

	//-------判断汇票到期日期是否超过 转/贴现日期-------
	function doCheckPorderEndDate(){
		if(!IqpBillDetail._checkAll()){
			return;
		}
		var porder_no = IqpBillDetail.porder_no._getValue();
		var porder_end_date = IqpBillDetail.porder_end_date._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var porderno = jsonstr.porderno;
				if(flag == "success"){
					doSave();
				}else{
					alert(msg);
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

		var url = '<emp:url action="checkPorderEndDate.do"/>?batch_no=${context.batch_no}&porder_no='+porder_no+'&porder_end_date='+porder_end_date;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};	

	function getPaorgNo(data){
		IqpBillDetail.paorg_no._setValue(data.bank_no._getValue());
		IqpBillDetail.paorg_name._setValue(data.bank_name._getValue());
	};	
	function getAaorgNo(data){
		IqpBillDetail.aaorg_no._setValue(data.bank_no._getValue());
		IqpBillDetail.aaorg_name._setValue(data.bank_name._getValue());
	};	
	function getAorgNo(data){
		IqpBillDetail.aorg_no._setValue(data.bank_no._getValue());
		IqpBillDetail.aorg_name._setValue(data.bank_name._getValue());
	};
	function getDaorgNo(data){
		IqpBillDetail.daorg_no._setValue(data.bank_no._getValue());
		IqpBillDetail.daorg_name._setValue(data.bank_name._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:tabGroup mainTab="mainTab" id="票据明细">
		<emp:tab label="票据明细" id="mainTab">
			<emp:form id="submitForm" action="updateIqpBillDetailRecord.do?batch_no=${context.batch_no}" method="POST">
				<emp:gridLayout id="IqpBillDetailGroup" title="票据基本信息" maxColumn="2">			
					<emp:text id="IqpBillDetail.porder_no" label="汇票号码" defvalue="${context.porderno}" maxlength="40"  cssElementClass="emp_field_input" readonly="true" required="true" />
					<emp:select id="IqpBillDetail.bill_type" label="票据种类" onblur="chageBillType();" readonly="true" required="true" dictname="STD_DRFT_TYPE"/>
			        <emp:select id="IqpBillDetail.porder_curr" label="汇票币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
					<emp:text id="IqpBillDetail.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" onchange="tcontAmtChange()"/>
					<emp:select id="IqpBillDetail.porder_addr" label="汇票签发地" dictname="STD_ZB_DRFT_ADDR" required="true" />   
					<emp:select id="IqpBillDetail.is_ebill" label="是否电票" required="true" onchange="isEbillChange()" readonly="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
					<emp:date id="IqpBillDetail.bill_isse_date" label="票据签发日" required="true" onblur="startDateChange();"/>
					<emp:date id="IqpBillDetail.porder_end_date" label="汇票到期日" required="true" onblur="endDateChange()"/>
					<emp:select id="IqpBillDetail.utakeover_sign" label="不得转让标记" required="true" dictname="STD_ZX_YES_NO"/> 
					<emp:select id="IqpBillDetail.status" label="票据状态" readonly="true" required="true" dictname="STD_ZB_DRFT_STATUS"/>
				    <emp:textarea id="IqpBillDetail.tran_increment_invc" label="交易增值税发票号/货运发票号" maxlength="250" required="false" colSpan="2"/>
				    <emp:textarea id="IqpBillDetail.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="出票人信息" maxColumn="2">
					<emp:text id="IqpBillDetail.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="false" onchange="checkCertCode();" colSpan="2"/>
					<emp:text id="IqpBillDetail.isse_name" label="出票人名称" maxlength="80" required="true" />  
					<emp:text id="IqpBillDetail.daorg_acct" label="出票人开户行账号"  required="false" dataType="Acct" cssElementClass="emp_field_input"/>
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="IqpBillDetail.daorg_no" label="出票人开户行行号"  url="getPrdBankInfoPopList.do?status=1" returnMethod="getDaorgNo" required="true" buttonLabel="选择" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="IqpBillDetail.daorg_name" label="出票人开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="贸易合同信息" maxColumn="2">
					<emp:text id="IqpBillDetail.tcont_no" label="贸易合同编号" maxlength="40" required="false" />
					<emp:text id="IqpBillDetail.tcont_amt" label="贸易合同金额" maxlength="18"  onchange="contAmtChange()"  required="false" dataType="Currency" />
					<emp:textarea id="IqpBillDetail.tcont_content" label="贸易合同内容" maxlength="500" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="收款人信息" maxColumn="2">
					<emp:text id="IqpBillDetail.pyee_name" label="收款人名称" maxlength="80" required="false" />
					<emp:text id="IqpBillDetail.paorg_acct_no" label="收款人开户行账号"  required="false" dataType="Acct" cssElementClass="emp_field_input"/> 
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="IqpBillDetail.paorg_no" label="收款人开户行行号"  required="false" url="getPrdBankInfoPopList.do?status=1" returnMethod="getPaorgNo" buttonLabel="选择" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="IqpBillDetail.paorg_name" label="收款人开户行行名" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				</emp:gridLayout>
				<emp:gridLayout id="IqpBillDetailGroup"  title="承兑人信息" maxColumn="2">
					<emp:select id="IqpBillDetail.aorg_type" label="承兑行类型" required="true" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="IqpBillDetail.aorg_no" label="承兑行行号" required="true" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAorgNo" buttonLabel="选择" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="IqpBillDetail.aorg_name" label="承兑行名称" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:select id="IqpBillDetail.aaorg_type" label="承兑人开户行类型" required="true" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
					<emp:text id="IqpBillDetail.accptr_cmon_code" label="承兑人组织机构代码" maxlength="20" required="false" onchange="CheckComInsCode()"/>
					<emp:text id="IqpBillDetail.aaorg_acct_no" label="承兑人开户行账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input"/>
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="IqpBillDetail.aaorg_no" label="承兑人开户行行号"  required="true" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAaorgNo"  buttonLabel="选择" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="IqpBillDetail.aaorg_name" label="承兑人开户行名称" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				</emp:gridLayout>
				<div align="center">
					<br>
					<emp:button id="checkPorderEndDate" label="确定"/>
					<emp:button id="return" label="返回"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		<!-- emp:tab label="利息计算" id="subTab" url="getIqpBillIncomeUpdatePage.do?batch_no=${context.batchno}&porder_no=${context.porder_no}" initial="false" needFlush="true" -->
	</emp:tabGroup>
</body>
</html>
</emp:page>
