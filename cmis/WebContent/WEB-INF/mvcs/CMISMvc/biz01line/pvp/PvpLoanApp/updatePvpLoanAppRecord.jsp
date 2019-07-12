<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String biz_type = "";
    if(context.containsKey("biz_type")){
    	biz_type = (String)context.getDataValue("biz_type");
    }
    String prd_id = "";
	if(context.containsKey("prd_id")){
		prd_id = (String)context.getDataValue("prd_id");
	}
%>
<jsp:include page="jsPvpComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>	
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">

function doOnLoad(){
    /*
	doChangLimitInt();//额度使用标识 
	changeRmbAmt();//获取折合人民币金额、保证金比例、风险敞口比例
	showPromissory();//是否承诺函下
	showTrust();//是否信托贷款
	showPayType();//支付方式
	changeFloatType();//利率浮动方式
	getRulMounth();//--加载利率--
	show_net();//显示所属网络
	controlBizType();//业务模式控制
	*/
	document.getElementById("base_tab").href="javascript:reLoad();";
	getFirstPayDate();
	changePvpAmtRead();//100039时可修改出账金额
	
}
function reLoad(){
	var url = '<emp:url action="getPvpLoanAppUpdatePage.do"/>?menuId=${context.menuId}&serno=${context.PvpLoanApp.serno}&op=update&biz_type=${context.biz_type}';
	url = EMPTools.encodeURI(url);
	window.location = url;
	//window.location.reload();
}; 
//-----------保存出账申请信息操作----------
function doSave(data){
	var form = document.getElementById("submitForm");
	var result = PvpLoanApp._checkAll();
	if(!result){
		return;
	}
	PvpLoanApp._toForm(form);
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
				if(data == "save"){
					   alert("保存成功!");
				}
			}else {
				alert("保存失败!");
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
	var url = '<emp:url action="updatePvpLoanAppRecord.do"/>';
	url = EMPTools.encodeURI(url);
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
};
//-----------提交流程----------
function doSubmitWF(apply_type){
	var serno = PvpLoanApp.serno._getValue();
	var cus_id = PvpLoanApp.cus_id._getValue();
	var cus_name = PvpLoanApp.cus_id_displayname._getValue();
	var approve_status = PvpLoanApp.approve_status._getValue();
	WfiJoin.cus_id._setValue(cus_id);
	WfiJoin.cus_name._setValue(cus_name);
	WfiJoin.prd_pk._setValue(PvpLoanApp.prd_id._getValue());
	WfiJoin.prd_name._setValue(PvpLoanApp.prd_id_displayname._getValue());
	WfiJoin.amt._setValue(PvpLoanApp.pvp_amt._getValue());
	WfiJoin.table_name._setValue("PvpLoanApp");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
	WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue(apply_type);
	initWFSubmit(false);
};

//-----------通过产品编号查询产品配置中使用流程类型----------
function getApplyTypeByPrdId(){
	var prdId = PvpLoanApp.prd_id._getValue();
	var url = '<emp:url action="getPvpApplyTypeByPrdId.do"/>?prdid='+prdId;
	url = EMPTools.encodeURI(url);
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
			var apply_type = jsonstr.apply_type;
			if(flag == "success"){
				doSubmitWF(apply_type);
			}else {
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
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};

//获取首次还款日默认值
function getFirstPayDate(){
	var cont_no = PvpLoanApp.cont_no._getValue(); //贷款编号
	var day = PvpLoanApp.first_pay_date._getValue();
	//alert(day);
	if(day =="" || day ==null) {
		var url = '<emp:url action="getFirstPayDate.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
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
				var first_pay_date = jsonstr.first_pay_date;
				if(flag == "success"){
					PvpLoanApp.first_pay_date._setValue(first_pay_date);//设置首次还款日
				}else {
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
//-----------放入流程操作，放入之前先保存修改数据----------
function doFlow(){
	doSave("subWF");  
	var form = document.getElementById("submitForm");
	var result = PvpLoanApp._checkAll();
	if(!result){
		return;
	}
	getApplyTypeByPrdId();
};		

function doSub(){ 
	doSave("save");
};


function doReturn1() { 
    var url = '<emp:url action="queryPvpLoanAppList.do"/>?menuId=${context.menuId}&biz_type='+'<%=biz_type%>';  
	url = EMPTools.encodeURI(url);
	window.location=url;  
};

function doReturn() {
	var url = '<emp:url action="queryPvpLoanAppList.do"/>?menuId=${context.menuId}&biz_type='+'<%=biz_type%>';  
	url = EMPTools.encodeURI(url);
	window.location=url;
};

function changePvpAmtRead(){
	debugger;
	var prd_id = PvpLoanApp.prd_id._getValue();
	if(prd_id == "100039"){
		PvpLoanApp.pvp_amt._obj._renderReadonly(false);
    }else{
    	 document.getElementById("PvpLoanApp.pvp_amt").setAttribute('cssElementClass','emp_currency_text_readonly');
    }
}
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updatePvpLoanAppRecord.do" method="POST">
	  <emp:tabGroup mainTab="base_tab" id="mainTab" >
		 <emp:tab label="出账信息" id="base_tab" needFlush="true" initial="true" >
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="出账基本信息">
			<emp:text id="PvpLoanApp.serno" label="出账流水号" maxlength="60" required="true" readonly="true"  colSpan="2"/>
			<emp:text id="PvpLoanApp.prd_id" label="产品编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.prd_id_displayname" label="产品名称"   required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cus_id_displayname" label="客户名称"   required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>	
			<emp:text id="PvpLoanApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cn_cont_no" label="中文合同编号" maxlength="100" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.bill_no" label="借据编号" maxlength="40" required="false" hidden="true" readonly="true"/>
			<emp:date id="PvpLoanApp.first_pay_date" label="首次还款日" required="true" onclick="getFirstPayDate();" hidden="false"/>
		  </emp:gridLayout>
		  
         <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:text id="PvpLoanApp.cont_amt" label="合同金额" cssElementClass="emp_currency_text_readonly" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    <emp:select id="PvpLoanApp.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="PvpLoanApp.cont_balance" label="合同余额" cssElementClass="emp_currency_text_readonly" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>		    
		    <emp:text id="PvpLoanApp.pvp_amt" label="出账金额"  readonly="true" maxlength="18" required="true"  dataType="Currency"/>	
   		 </emp:gridLayout>
		  <emp:gridLayout id="" maxColumn="3" title="登记信息">   
		  	<emp:text id="PvpLoanApp.manager_br_id_displayname" label="管理机构"   required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id_displayname" label="入账机构" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" required="true"/>
		    <emp:select id="PvpLoanApp.flow_type" label="流程类型" dictname="STD_ZB_FLOW_TYPE" defvalue="01" required="false" readonly="true"/>    
		    <emp:text id="PvpLoanApp.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
			<emp:date id="PvpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="PvpLoanApp.approve_status" label="审批状态" required="false" hidden="true" dictname="WF_APP_STATUS"/>
		    <emp:text id="PvpLoanApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id" label="入账机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" required="true"/>
		     <emp:text id="PvpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false" readonly="true"/>
		 </emp:gridLayout>
		 <div align="center">
			<br>
			<emp:button id="flow" label="放入流程" />
			<emp:button id="sub" label="保存" />       
			<emp:button id="return" label="关闭"/>
		</div>
		</emp:tab>
		<%if("600020".equals(prd_id)){ %>
	    <emp:tab label="资产转受让合同信息" id="assetContSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrAssetstrsfContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
		<emp:tab label="转贴现合同信息" id="rpContsubTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrRpddscntContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else if("8".equals(biz_type) && (prd_id.equals("300021")||prd_id.equals("300020"))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/> 
	   <%}else if("8".equals(biz_type) && !prd_id.equals("300021") && !prd_id.equals("300020")){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else if(prd_id.equals("300021")||prd_id.equals("300020")){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/> 
	     <%}else{ %>
		 <emp:tab label="合同信息" id="subTab" url="getCtrLoanContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%} %>
		</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>
