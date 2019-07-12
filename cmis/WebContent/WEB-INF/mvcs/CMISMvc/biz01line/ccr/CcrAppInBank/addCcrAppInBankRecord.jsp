<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.yucheng.cmis.pub.PUBConstant"%><emp:page>
<% 
   String flag = (String)request.getAttribute("flag");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>


<script type="text/javascript">

/*--user code begin--*/
	
		function CheckDt(){
		var openDay='${context.OPENDAY}';
		start = CcrAddInf.app_begin_date._getValue();
		expiring = CcrAddInf.expiring_date._getValue();
		
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(openDay,start);
			if(!flag){
				alert("开始日期要大于当前日期！");
				CcrAddInf.start_date._setValue("");
				return false;
			}
		}
		if (start!=null && start!="" &&expiring!=null && expiring!=""){
			var flag = CheckDate1BeforeDate2(start,expiring);
			if(!flag){
				alert("到期日期要大于申请日期！");
				CcrAddInf.expiring_date._setValue("");
				return false;
			}			
		} 
		return true;
		
	}

	
function returnCus(data){
	CcrAddInf.cus_id._setValue(data.cus_id._getValue());
	CcrAddInf.cus_id_displayname._setValue(data.same_org_cnname._getValue());
	//CcrAddInf.cus_type._setValue(data.actorno._getValue()); 
	//CcrAddInf.cert_type._setValue(data.cert_type._getValue()); 
	//CcrAddInf.cert_code._setValue(data.cert_code._getValue()); 
	checkCusExist();

}
	
//模型编号为"M20090300050-60"时，此模型为 	工业企业信用评级
//模型编号为"M20090300035","M20090300039","M20090300040"		此模型为个人客户模型
//当客户类型首位为"2"是，此客户为		企业客户。
//当客户类型首位为"1"时, 次客户为		个人客户
//这两者需要对应，不对应就报错及清空

function checkCusType(checkSpot){
	var cusType = CcrAddInf.cus_type._getValue();
	var modelNo = CcrAddInf.model_no._getValue();
	if(cusType==''||modelNo==''){
		return;
	}
	if ('2'==cusType.substring(0,1)&&modelNo=='M20090300050'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300051'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300052'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300053'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300054'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300055'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300056'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300057'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300058'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300059'){
		return true;
	}else if ('2'==cusType.substring(0,1)&&modelNo=='M20090300060'){
		return true;
	}else if ('1'==cusType.substring(0,1)&&modelNo=='M20090300035'){
		return true;
	}else if ('1'==cusType.substring(0,1)&&modelNo=='M20090300039'){
		return true;
	}else if ('1'==cusType.substring(0,1)&&modelNo=='M20090300040'){
		return true;
	}else{
		alert("客户类型和模型类型不符");
	
	if(checkSpot==cus_id_displayname){
		CcrAddInf.cus_id._setValue("");
		CcrAddInf.cus_id_displayname._setValue("");
		CcrAddInf.cus_type._setValue("");			
	}else if(checkSpot==returnMod){
		CcrAddInf.model_no._setValue("");
		CcrAddInf.model_name._setValue("");				
	}else{
		CcrAddInf.cus_id._setValue("");
		CcrAddInf.cus_id_displayname._setValue("");
		CcrAddInf.cus_type._setValue("");
		CcrAddInf.model_no._setValue("");
		CcrAddInf.model_name._setValue("");		
	}
	
		return false;	
	}

}
	
function returnMod(data){

	CcrAddInf.model_no._setValue(data.model_no._getValue());
	CcrAddInf.model_no_displayname._setValue(data.model_name._getValue());  

}			
function doReturnMethod(json, callback){

	if('false'==json.ccr_result){
		//未发现重名客户
	}else if('true'==json.ccr_result){
		alert("该客户已经开始信用评级,在评级结束之前不允许重复评级");
		CcrAddInf.cus_id._setValue("");
		CcrAddInf.cus_id_displayname._setValue("");
		CcrAddInf.cus_type._setValue("");
	}else{
		alert('查询信用评级明细表出错了');
	}

}
function checkCusExist(){
	
	var url = "<emp:url  action='checkAppExist.do'/>&cus_id="+ CcrAddInf.cus_id._getValue();
	var callback = {
		success : "doReturnMethod",
		isJSON : true
	};
	
	EMPTools.ajaxRequest('GET', url, callback);
		
}

function doReturn() {

	var url = '<emp:url action="queryCcrAppInBankList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;

};	
	/*--user code end--*/
function doload(){ 
	<%if("6".equals(flag)){%>
	CcrAddInf.flag._setValue("6");
	<%}else{%>
	<%}%>
//	CcrAddInf.cus_id._obj.config.url=CcrAddInf.cus_id._obj.config.url+"&returnMethod=returnCus"
//	CcrAddInf.cus_id_displayname._obj.addOneButton('uniquCheck', '修改',doGetCusInfo4Per);

	
 } 	

function riskInspect(){

	var _applType = "991";
	var _wfSign  = "";
	var _modelId = "CusBase";
	var _pkCol   = "cus_id";
	var _pkVal   = CcrAddInf.cus_id._getValue();
	var _preventIdLst = "FFFBC88600508B0E7FB983B78B144893";
    var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&wfsign="+_wfSign + "&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
	return _retObj;	

}

function doNext(){
	
	var form = document.getElementById('submitForm');
	var result = CcrAddInf._checkAll();
    if(result){
    	var cus_id = CcrAddInf.cus_id._getValue();
		var model_no = CcrAddInf.model_no._getValue();
		var flag = CcrAddInf.flag._getValue();
		//符合条件后，提交至下一步
		var url = '<emp:url action="getCcrAppInBankAddLeadPage.do"/>?cus_id='+cus_id+'&model_no='+model_no+'&flag='+flag;
		url = EMPTools.encodeURI(url);
		window.location=url;
    }else {
	    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	}
	
}
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCcrAppInBankRecord.do" method="POST">
		
		<emp:gridLayout id="CcrAddInfGroup" title="信用评级申请主表" maxColumn="2">
			
			<emp:pop id="CcrAddInf.cus_id" label="客户码" url="getValueQueryUpOrgEstList.do" returnMethod="returnCus" required="true" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="CcrAddInf.cus_id_displayname" label="客户名"  required="true" readonly="true" /> 
			<emp:pop id="CcrAddInf.model_no" label="评级模型ID" url="queryIndModelPopList.do" returnMethod="returnMod" required="false" hidden="true" defvalue="SQ2012226229"/>
			<emp:text id="CcrAddInf.model_no_displayname" label="评级模型名"  required="false" readonly="true" hidden="false" defvalue="同业客户评级模型" colSpan="2"/>
			<emp:text id="CcrAddInf.app_begin_date" label="申请日期" required="false" readonly="true" defvalue="$OPENDAY"/>
			<emp:date id="CcrAddInf.start_date" label="开始日期" required="true"  hidden="true" defvalue="$OPENDAY"/>
			<emp:date id="CcrAddInf.expiring_date" label="到期日期" required="false" onblur="CheckDt()" hidden="true"/>
			<emp:select id="CcrAddInf.approve_status" label="申请状态" required="false"  dictname="WF_APP_STATUS"  defvalue="000" hidden="true"/>
			<emp:select id="CcrAddInf.cus_type" label="客户类型" required="false" hidden="true" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:text id="CcrAddInf.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="3"/>
			
			<emp:text id="CcrAddInf.cert_type" label="证件类型" required="false" maxlength="60" readonly="true" colSpan="2" hidden="true"/>
			<emp:text id="CcrAddInf.cert_code" label="证件号码" required="false" maxlength="60" readonly="true" colSpan="2" hidden="true"/>
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

