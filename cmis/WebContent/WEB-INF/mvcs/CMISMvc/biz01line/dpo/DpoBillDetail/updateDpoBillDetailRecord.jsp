<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	String drfpo_no = request.getParameter("drfpo_no");
%>
<jsp:include page="DpoBillDetailComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input2{
border:1px solid #b7b7b7;
width:445px;
}    
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){
		//IqpBillDetailInfo.drwr_org_code._obj.addOneButton("orgCode","获取",getOrgCode);
		chageBillType();
		unReadOnly();
	}
	function getOrgCode(){
		var acctNo = IqpBillDetailInfo.bail_acc_no._getValue();
        if(acctNo == null || acctNo == ""){
			alert("请先输入保证金账号信息！");
			return;
        }
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var retMsg = jsonstr.retMsg;
				var GUARANTEE_ACCT_NO = jsonstr.BODY.GUARANTEE_ACCT_NO;
				var GUARANTEE_ACCT_NAME = jsonstr.BODY.GUARANTEE_ACCT_NAME;
				var CCY = jsonstr.BODY.CCY;
				var AMT = jsonstr.BODY.AMT;
				var GUARANTEE_TYPE = jsonstr.BODY.GUARANTEE_TYPE;
				var INT_RATE = jsonstr.BODY.INT_RATE;
				var INTER_FLT_RATE = jsonstr.BODY.INTER_FLT_RATE;
				var TERM = jsonstr.BODY.TERM;
				var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
				if(flag == "success"){
					IqpBillDetailInfo.bail_acc_name._setValue(GUARANTEE_ACCT_NAME);
				}else {
					alert(retMsg); 
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
		var url = '<emp:url action="clientTrade4Esb.do"/>?bail_acct_no='+acctNo+'&service_code=11003000007&sence_code=16';	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}
	//-------异步保存主表单页面信息-------
	function doNext(){
		if(!IqpBillDetailInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpBillDetailInfo._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var porderno = jsonstr.porderno;
				if(flag == "success"){
					alert("修改成功！");
					if("tab"=='${context.tab}'){
					window.opener.location.reload();
					window.close();
					}else{
					window.location.reload();
					}
				}else {
					alert("修改失败！");
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
		var url = '<emp:url action="updateDpoBillDetailRecord.do"/>?drfpo_no=<%=drfpo_no%>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	//------------通过汇票号码验证/获得汇票相关信息,并赋予页面------------
	function getPorderMsg(){
		var porderno = IqpBillDetailInfo.porder_no._getValue();
		if(porderno == null || porderno == ""){
			alert("请输入汇票号码！");
			return;
		}
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
				var bill_type = jsonstr.bill_type;
				var porder_no = jsonstr.porder_no;
				var porder_curr = jsonstr.porder_curr;
				var drft_amt = jsonstr.drft_amt;
				var porder_addr = jsonstr.porder_addr;
				var is_ebill = jsonstr.is_ebill;
				var bill_isse_date = jsonstr.bill_isse_date;
				var porder_end_date = jsonstr.porder_end_date;
				var utakeover_sign = jsonstr.utakeover_sign;
				var tcont_no = jsonstr.tcont_no;
				var tcont_amt = jsonstr.tcont_amt;
				var tcont_content = jsonstr.tcont_content;
				var drwr_org_code = jsonstr.drwr_org_code;
				var isse_name = jsonstr.isse_name;
				var daorg_no = jsonstr.daorg_no;
				var daorg_name = jsonstr.daorg_name;
				var daorg_acct = jsonstr.daorg_acct;
				var pyee_name = jsonstr.pyee_name;
				var paorg_no = jsonstr.paorg_no;
				var paorg_name = jsonstr.paorg_name;
				var paorg_acct_no = jsonstr.paorg_acct_no;
				var aaorg_type = jsonstr.aaorg_type;
				var aaorg_no = jsonstr.aaorg_no;
				var aaorg_name = jsonstr.aaorg_name;
				var accptr_cmon_code = jsonstr.accptr_cmon_code;
				var aaorg_acct_no = jsonstr.aaorg_acct_no;
				var aorg_type = jsonstr.aorg_type;
				var aorg_no = jsonstr.aorg_no;
				var aorg_name = jsonstr.aorg_name;
				var status = jsonstr.status;
				if(flag == "0000"){
					unReadOnly();
					//alert(msg);
					IqpBillDetailInfo.bill_type._setValue(bill_type);
					IqpBillDetailInfo.porder_no._setValue(porder_no);
					IqpBillDetailInfo.porder_curr._setValue(porder_curr);
					IqpBillDetailInfo.drft_amt._setValue(drft_amt);
					IqpBillDetailInfo.porder_addr._setValue(porder_addr);
					IqpBillDetailInfo.is_ebill._setValue(is_ebill);
					IqpBillDetailInfo.bill_isse_date._setValue(bill_isse_date);
					IqpBillDetailInfo.porder_end_date._setValue(porder_end_date);
					IqpBillDetailInfo.utakeover_sign._setValue(utakeover_sign);
					IqpBillDetailInfo.tcont_no._setValue(tcont_no);
					IqpBillDetailInfo.tcont_amt._setValue(tcont_amt);
					IqpBillDetailInfo.tcont_content._setValue(tcont_content);
					IqpBillDetailInfo.drwr_org_code._setValue(drwr_org_code);
					IqpBillDetailInfo.isse_name._setValue(isse_name);
					IqpBillDetailInfo.daorg_no._setValue(daorg_no);
					IqpBillDetailInfo.daorg_name._setValue(daorg_name);
					IqpBillDetailInfo.daorg_acct._setValue(daorg_acct);
					IqpBillDetailInfo.pyee_name._setValue(pyee_name);
					IqpBillDetailInfo.paorg_no._setValue(paorg_no);
					IqpBillDetailInfo.paorg_name._setValue(paorg_name);
					IqpBillDetailInfo.paorg_acct_no._setValue(paorg_acct_no);
					IqpBillDetailInfo.aaorg_type._setValue(aaorg_type);
					IqpBillDetailInfo.aaorg_no._setValue(aaorg_no);
					IqpBillDetailInfo.aaorg_name._setValue(aaorg_name);
					IqpBillDetailInfo.accptr_cmon_code._setValue(accptr_cmon_code);
					IqpBillDetailInfo.aaorg_acct_no._setValue(aaorg_acct_no);
					IqpBillDetailInfo.aorg_type._setValue(aorg_type);
					IqpBillDetailInfo.aorg_no._setValue(aorg_no);
					IqpBillDetailInfo.aorg_name._setValue(aorg_name);
					IqpBillDetailInfo.status._setValue(status);
					chageBillType();
					//标志此条汇票记录不能重复插入数据库
					IqpBillDetailInfo.flag._setValue("2");
				}else if(flag == "9999"){
					alert(msg);
					unReadOnly();
				}else if(flag =="0001"){//已存在的票据信息处于其他池中，不能在此票据池中进行新增
					alert(msg);
					IqpBillDetailInfo.porder_no._setValue("");
				}else{
					if(status == '03'){
						alert("此票据已托收在途，不能在此票据池中进行新增！");
						IqpBillDetailInfo.porder_no._setValue("");
						readOnly();
					}else if(status == '05'){
						alert("此票据已质押，不能在此票据池中进行新增！");
						IqpBillDetailInfo.porder_no._setValue("");
						readOnly();
					}else if(status == '04'){
						alert("此票据已核销，不能在此票据池中进行新增！");
						IqpBillDetailInfo.porder_no._setValue("");
						readOnly();
					}
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
		var url = '<emp:url action="getPorderMsgByPorderNoDrfpo.do"/>?porder_no='+porderno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		
	};
	function unReadOnly(){
		IqpBillDetailInfo.bill_type._obj._renderReadonly(false);
		IqpBillDetailInfo.porder_curr._obj._renderReadonly(false);
		IqpBillDetailInfo.drft_amt._obj._renderReadonly(false);
		IqpBillDetailInfo.porder_addr._obj._renderReadonly(false);
		IqpBillDetailInfo.is_ebill._obj._renderReadonly(false);
		IqpBillDetailInfo.bill_isse_date._obj._renderReadonly(false);
		IqpBillDetailInfo.porder_end_date._obj._renderReadonly(false);
		IqpBillDetailInfo.utakeover_sign._obj._renderReadonly(false);
		IqpBillDetailInfo.tcont_no._obj._renderReadonly(false);
		IqpBillDetailInfo.tcont_amt._obj._renderReadonly(false);
		IqpBillDetailInfo.tcont_content._obj._renderReadonly(false);
		IqpBillDetailInfo.drwr_org_code._obj._renderReadonly(false);
		IqpBillDetailInfo.isse_name._obj._renderReadonly(false);
		IqpBillDetailInfo.daorg_no._obj._renderReadonly(false);
		IqpBillDetailInfo.daorg_name._obj._renderReadonly(false);
		IqpBillDetailInfo.daorg_acct._obj._renderReadonly(false);
		IqpBillDetailInfo.pyee_name._obj._renderReadonly(false);
		IqpBillDetailInfo.paorg_no._obj._renderReadonly(false);
		IqpBillDetailInfo.paorg_name._obj._renderReadonly(false);
		IqpBillDetailInfo.paorg_acct_no._obj._renderReadonly(false);
		IqpBillDetailInfo.aaorg_type._obj._renderReadonly(false);
		IqpBillDetailInfo.aaorg_no._obj._renderReadonly(false);
		IqpBillDetailInfo.aaorg_name._obj._renderReadonly(false);
		IqpBillDetailInfo.accptr_cmon_code._obj._renderReadonly(false);
		IqpBillDetailInfo.aaorg_acct_no._obj._renderReadonly(false);
		IqpBillDetailInfo.aorg_type._obj._renderReadonly(false);
		IqpBillDetailInfo.aorg_no._obj._renderReadonly(false);
		IqpBillDetailInfo.aorg_name._obj._renderReadonly(false);
		//IqpBillDetailInfo.status._obj._renderReadonly(false);
	};
	function readOnly(){
		IqpBillDetailInfo.bill_type._obj._renderReadonly(true);
		IqpBillDetailInfo.porder_curr._obj._renderReadonly(true);
		IqpBillDetailInfo.drft_amt._obj._renderReadonly(true);
		IqpBillDetailInfo.porder_addr._obj._renderReadonly(true);
		IqpBillDetailInfo.is_ebill._obj._renderReadonly(true);
		IqpBillDetailInfo.bill_isse_date._obj._renderReadonly(true);
		IqpBillDetailInfo.porder_end_date._obj._renderReadonly(true);
		IqpBillDetailInfo.utakeover_sign._obj._renderReadonly(true);
		IqpBillDetailInfo.tcont_no._obj._renderReadonly(true);
		IqpBillDetailInfo.tcont_amt._obj._renderReadonly(true);
		IqpBillDetailInfo.tcont_content._obj._renderReadonly(true);
		IqpBillDetailInfo.drwr_org_code._obj._renderReadonly(true);
		IqpBillDetailInfo.isse_name._obj._renderReadonly(true);
		IqpBillDetailInfo.daorg_no._obj._renderReadonly(true);
		IqpBillDetailInfo.daorg_name._obj._renderReadonly(true);
		IqpBillDetailInfo.daorg_acct._obj._renderReadonly(true);
		IqpBillDetailInfo.pyee_name._obj._renderReadonly(true);
		IqpBillDetailInfo.paorg_no._obj._renderReadonly(true);
		IqpBillDetailInfo.paorg_name._obj._renderReadonly(true);
		IqpBillDetailInfo.paorg_acct_no._obj._renderReadonly(true);
		IqpBillDetailInfo.aaorg_type._obj._renderReadonly(true);
		IqpBillDetailInfo.aaorg_no._obj._renderReadonly(true);
		IqpBillDetailInfo.aaorg_name._obj._renderReadonly(true);
		IqpBillDetailInfo.accptr_cmon_code._obj._renderReadonly(true);
		IqpBillDetailInfo.aaorg_acct_no._obj._renderReadonly(true);
		IqpBillDetailInfo.aorg_type._obj._renderReadonly(true);
		IqpBillDetailInfo.aorg_no._obj._renderReadonly(true);
		IqpBillDetailInfo.aorg_name._obj._renderReadonly(true);
		IqpBillDetailInfo.status._obj._renderReadonly(true);
	};	
	function doReturn(){
		if("tab"=='${context.tab}'){
			window.opener.location.reload();
			window.close();
			}else{
			var url = '<emp:url action="queryDpoBillDetailList.do"/>?drfpo_no=${context.drfpo_no}';
			url = EMPTools.encodeURI(url);
			window.location=url;
		}
	}	
	//校验组织机构代码
	function checkCertCode(){
		var certCode =IqpBillDetailInfo.drwr_org_code._obj.element.value;
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				IqpBillDetailInfo.drwr_org_code._obj.element.value="";
				 return false;
			}
		}
	};	
	//校验票据签发日	
	function CheckDate(date){
		var str_date=date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(str_date!=null && str_date!="" ){
			var flag = CheckDate1BeforeDate2(str_date,openDay);
			if(!flag){
				alert("输入日期要小于等于当前日期！！");
				date._obj.element.value="";
			}
	     }
	}
	//校验汇票到期日
	function CheckEndDate(date1,date2){
		var flag = IqpBillDetailInfo.is_ebill._obj.element.value;
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		if(flag=="1"){//是电子汇票时，汇票到期日不能大于签发日后延一年
			if(start!=null && start!=""){
				if(end!=null && end!="" ){
					var flag = CheckDate1BeforeDate2(end,DateAddMonth(start,12));
					
					if(flag){
						date2._obj.element.value=end;
					}else{
						alert("汇票到期日不能大于签发日后延一年");
						date2._obj.element.value="";
					}
			     }
			}else{
					alert("请先输入票据签发日期！");
					date2._obj.element.value="";
				}
		}else if(flag=="2"){//不是电子汇票时，汇票到期日不能大于签发日后延六个月
			if(start!=null && start!=""){
				if(end!=null && end!="" ){
					var flag = CheckDate1BeforeDate2(end,DateAddMonth(start,6));
					if(flag){
						date2._obj.element.value=end;
					}else{
						alert("汇票到期日不能大于签发日后延六个月");
						date2._obj.element.value="";
					}
			     }
			}else{
					alert("请先输入票据签发日期！");
					date2._obj.element.value="";
				}
			}
	}
	function getPaorgNo(data){
		IqpBillDetailInfo.paorg_no._setValue(data.bank_no._getValue());
		IqpBillDetailInfo.paorg_name._setValue(data.bank_name._getValue());
	};	
	function getAaorgNo(data){
		IqpBillDetailInfo.aaorg_no._setValue(data.bank_no._getValue());
		IqpBillDetailInfo.aaorg_name._setValue(data.bank_name._getValue());
	};	
	function getAorgNo(data){
		IqpBillDetailInfo.aorg_no._setValue(data.bank_no._getValue());
		IqpBillDetailInfo.aorg_name._setValue(data.bank_name._getValue());
	};
	function getDaorgNo(data){
		IqpBillDetailInfo.daorg_no._setValue(data.bank_no._getValue());
		IqpBillDetailInfo.daorg_name._setValue(data.bank_name._getValue());
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad();">
		<emp:form id="submitForm" action="updateDpoBillDetailRecord.do" method="POST">
			<emp:gridLayout id="DpoBillDetailGroup" title="票据基本信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.porder_no" label="汇票号码" hidden="false" colSpan="2" maxlength="40" readonly="true" required="true" />
				<emp:select id="IqpBillDetailInfo.bill_type" label="票据种类" onblur="chageBillType();" required="true" dictname="STD_DRFT_TYPE"/>
				<emp:select id="IqpBillDetailInfo.porder_curr" label="汇票币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
				<emp:text id="IqpBillDetailInfo.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" />
				<emp:select id="IqpBillDetailInfo.porder_addr" label="汇票签发地" dictname="STD_ZB_DRFT_ADDR" required="true" />    
				<emp:select id="IqpBillDetailInfo.is_ebill" label="是否电票" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
				<emp:date id="IqpBillDetailInfo.bill_isse_date" label="票据签发日" required="true" onblur="CheckDate(IqpBillDetailInfo.bill_isse_date);"/>
				<emp:date id="IqpBillDetailInfo.porder_end_date" label="汇票到期日" required="true" onblur="CheckEndDate(IqpBillDetailInfo.bill_isse_date,IqpBillDetailInfo.porder_end_date)"/>
				<emp:select id="IqpBillDetailInfo.utakeover_sign" label="不得转让标记" required="true" dictname="STD_ZX_YES_NO"/> 
				<emp:select id="IqpBillDetailInfo.status" label="票据状态" readonly="true" required="true" dictname="STD_ZB_DRFT_STATUS"/>
			</emp:gridLayout>
			<emp:gridLayout id="DpoBillDetailGroup"  title="出票人信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="true" colSpan="2" onchange="checkCertCode();" />
				<emp:text id="IqpBillDetailInfo.isse_name" label="出票/付款人名称" maxlength="80" required="true" readonly="true"/>  
				<emp:text id="IqpBillDetailInfo.daorg_acct" label="出票人开户行账号"  required="true" />
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="IqpBillDetailInfo.daorg_no" label="出票人开户行行号"  required="true" url="getPrdBankInfoPopList.do?status=1" returnMethod="getDaorgNo" />
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="IqpBillDetailInfo.daorg_name" label="出票人开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long"/>
				
			</emp:gridLayout>
			<emp:gridLayout id="DpoBillDetailGroup"  title="贸易合同信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.tcont_no" label="贸易合同编号" maxlength="40" required="true" />
				<emp:text id="IqpBillDetailInfo.tcont_amt" label="贸易合同金额" maxlength="18" required="true" dataType="Currency" />
				<emp:textarea id="IqpBillDetailInfo.tcont_content" label="贸易合同内容" maxlength="500" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="DpoBillDetailGroup"  title="收款人信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.pyee_name" label="收款人名称" maxlength="80" required="true" />
				<emp:text id="IqpBillDetailInfo.paorg_acct_no" label="收款人开户行账号"  required="true" /> 
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="IqpBillDetailInfo.paorg_no" label="收款人开户行行号"  required="true" url="getPrdBankInfoPopList.do?status=1" returnMethod="getPaorgNo"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="IqpBillDetailInfo.paorg_name" label="收款人开户行行名" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long"/>
				
			</emp:gridLayout>
			<emp:gridLayout id="DpoBillDetailGroup"  title="承兑方信息" maxColumn="2">
				<emp:select id="IqpBillDetailInfo.aorg_type" label="承兑行类型" required="false" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="IqpBillDetailInfo.aorg_no" label="承兑行行号"  required="false" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAorgNo"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="IqpBillDetailInfo.aorg_name" label="承兑行名称" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long"/>
				<emp:select id="IqpBillDetailInfo.aaorg_type" label="承兑人开户行类型" required="false" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="IqpBillDetailInfo.aaorg_no" label="承兑人开户行行号"  required="false" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAaorgNo" />
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="IqpBillDetailInfo.aaorg_name" label="承兑人开户行名称" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long"/>
				<emp:text id="IqpBillDetailInfo.accptr_cmon_code" label="承兑人组织机构代码" maxlength="20" required="false" />
				<emp:text id="IqpBillDetailInfo.aaorg_acct_no" label="承兑人开户行账号" maxlength="40" required="false" />
				<emp:text id="IqpBillDetailInfo.flag" label="标志位（用来标记此汇票是否是可以新增的1--可以，2--不可以）" maxlength="40" required="false" hidden="true" defvalue="1"/>
			</emp:gridLayout>
			<div align="center">
				<br>
				<emp:button id="next" label="确定" op="add"/>
				<emp:button id="return" label="返回"/>
			</div>
		</emp:form>
</body>
</html>
</emp:page>

