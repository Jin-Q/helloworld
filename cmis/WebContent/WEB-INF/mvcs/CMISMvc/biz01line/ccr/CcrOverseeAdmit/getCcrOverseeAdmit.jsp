<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = (String)context.getDataValue("op");
	if("view".equals(op)){
		request.setAttribute("canwrite","");	
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<!--  jsp:include page="/flowExt/include4WF.jsp" flush="true"/ -->

<script type="text/javascript">
function doload(){
//	var limit_grade ='${context.CcrAppDetail.limit_grade}';
//	if(limit_grade==''){
//		CcrAppDetail.limit_reason._obj._renderHidden(true);
//		CcrAppDetail.limit_grade._obj._renderHidden(true);
//	}
	var itms1 = document.getElementById('CcrAppDetail.adjusted_grade').options;//更改客户经理调整等级的字典项
	itms1.remove("01");
}	
//判断是否已经发起了流程
function checkApplyStatus(){
	var status=CcrAppInfo.approve_status._getValue();
	if('000'==status||'992'==status){
		return true;
	}else{
		return false;
	}
}	
function doReturnMethod(json, callback){
	var modelNo='${context.CcrAppDetail.model_no}';
	if (json.substring(0,7)=='errMsg:'){
		var button = document.getElementById('button_creditRating');
		button.disabled="";			
		return
	}
	
	json = "({"+json+"})";
	var obj = eval(json);
	var str = '';
	for (var i in obj){
		var kcoll = eval("obj."+i);
		for(var j=0;j<kcoll.fields.length;j++){
			var input = document.getElementById(i+"."+kcoll.fields[j]);
			if(input){
				input.value = eval('(obj.'+i+"."+kcoll.fields[j]+")");
				//str=str+'obj.'+i+"."+kcoll.fields[j]+":"+input.value+"\n";
				continue;
			}
		}
	}
	//EMPTools.removeWait();
	var button = document.getElementById('button_creditRating');
	button.disabled="";	
	window.location.reload();
}
function doCreditRating(){
	if(!checkApplyStatus()){
		alert("已经发起的流程不允许修改");
		return "";
	}
	//EMPTools.setWait();
	var button = document.getElementById('button_creditRating');
	button.disabled="disabled";	
	var url = "creditRating.do";
	var callback = {
		success : "doReturnMethod",
		isJSON : true,
		form : "creditRatingForm"
	};
	EMPTools.ajaxRequest('POST', url, callback);
}	


/*function changeAdjuest_grade(){
	if(CcrAppDetail.auto_score._getValue()==''){
		alert("必须先[获得机评得分]才可以进行此操作");
		return false;
	}
	//检验调整得分是否在配置的调整范围内.

	var auto_score = new Number(CcrAppDetail.auto_score._getValue());
	var adjust_score = new Number(CcrAppDetail.adjust_score._getValue());
	var grpCusType = '${context.CcrAppDetail.com_grp_mode}';
	var grpGrade ='${context.CcrAppDetail.grp_crd_grade}';
	var all_score = '${context.CcrAppDetail.all_score}';

	
	if(!checkAdjuestScore(adjust_score)){
		CcrAppDetail.adjust_score._setValue("");
		return false;
	}
	
	//获得调整后的得分.修改调整后等级.
	adjusted_score = auto_score+adjust_score

	if(parseFloat(adjusted_score) >parseFloat(all_score)){		
		alert('调整后得分必须小于等于'+all_score);
		CcrAppDetail.adjusted_score._setValue(myStr);
		CcrAppDetail.adjusted_score2._setValue(myStr);
		return false;
	}
	adjusted_grade = modelScore2Grade(adjusted_score);
	adjusted_grade = modelScore2Grade(adjusted_score);
	
	//如果调整后的分没有小数点就给它加上~
	var myStr=new String(adjusted_score); 
	if(!(myStr.indexOf(".")>-1)){
		myStr = myStr+".0";
	}
	

	CcrAppDetail.adjusted_grade._setValue(adjusted_grade);
	CcrAppDetail.final_grade._setValue(adjusted_grade);
	CcrAppDetail.adjusted_grade2._setValue(adjusted_grade);
	
	return true;
	
}
*/


function doSaveAdjuest_grade(){
    doSaveGrade();
	if(!checkApplyStatus()){
		alert("不能重复发起流程");
		return "";
	}
	if(!changeAdjuest_grade()){
		return false;
	}
	//虽然OP叫UpdateSugGradeOp,但是其实保存的是调整得分以及调整后等级,以及调整后的最后得分.
	//最后得分将在流程结束后根据流程中的调整得分 再次修改.
	//建议等级已经失效.
/*	var form = document.getElementById("updateGradeForm");
	CcrAppInfo.flag._toForm(form);
	CcrAppDetail.serno._toForm(form);
	CcrAppDetail.cus_id._toForm(form);
	CcrAppDetail.adjusted_score._toForm(form);
	CcrAppDetail.adjusted_grade._toForm(form);
	CcrAppDetail.final_grade._toForm(form);
	CcrAppDetail.adjust_score._toForm(form);
	CcrAppDetail.reason._toForm(form);
	var callback = {
			success : "checkNSubmit",
			isJSON : true,
			form : "updateGradeForm"
		};	
	EMPTools.ajaxRequest('POST', form.action, callback);
	*/
	doSubmitWF();

}
function checkNSubmit(data){
	if(data.ccr_result=="success"){
		alert("保存成功");
		//EMPTools.mask();
		//doSubmitWF();
	}else{
		alert("更新建议等级失败");
	}
}
//只保存修正得分最终等级之类字段,不进行提交
function saveAdjuest_gradeOnly(){
	
	/*if(!changeAdjuest_grade()){
		return false;
	}*/
	//虽然OP叫UpdateSugGradeOp,但是其实保存的是调整得分以及调整后等级,以及调整后的最后得分.
	//最后得分将在流程结束后根据流程中的调整得分 再次修改.
	//建议等级已经失效.
	var form = document.getElementById("updateGradeForm");
	var result = CcrAppInfo._checkAll();
 	var result1 = CcrAppDetail._checkAll();
    if(result&result1){
    	CcrAppInfo.manager_id._toForm(form);
    	CcrAppInfo.manager_br_id._toForm(form);
    	CcrAppInfo.reg_date._toForm(form);
    	CcrAppInfo.serno._toForm(form);
    	CcrAppInfo.cus_id._toForm(form);
    	CcrAppInfo.flag._toForm(form);
    	CcrAppInfo.approve_status._toForm(form)
    //	CcrAppDetail.auto_score._toForm(form);
    //	CcrAppDetail.auto_grade._toForm(form);
    	CcrAppDetail.adjusted_grade._toForm(form);
    	CcrAppDetail.reason._toForm(form);
		var callback = {
				success : "retSaveAdjuest",
				isJSON : true,
				form : "updateGradeForm"
			};	
		EMPTools.ajaxRequest('POST', form.action, callback);
    }else {
	    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	}
}

function retSaveAdjuest(data){

	if(data.ccr_result=="success"){
		alert("保存成功!");
		window.location.reload();
	}else{
		alert("保存失败!");
	}

}
function doSaveGrade(){
	if(CcrAppDetail.auto_score._getValue()==''){
		alert("必须先[获得机评得分]才可以进行此操作");
		return false;
	} 
	saveAdjuest_gradeOnly();
	
}
function doSubmitWF(){
	var custype=CcrAppInfo.cus_type._getValue()
	//个人信用评分
	if(custype.substring(0,1)=="1"){
		var _applType=650;
	}
	//对公信用评级
	else { 
		var _applType=670;
	}
    var _status = CcrAppInfo.approve_status._getValue() ;
    if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
	}
	var _wfSign  = "";
	var _modelId = "CcrAppInfo";
	var _pkCol   = "serno";
	var _pkVal   = CcrAppInfo.serno._getValue();
	
	WfiJoin.cus_id._setValue(CcrAppInfo.cus_id._getValue());
//	WfiJoin.cus_name._setValue(CcrAppInfo.cus_name._getValue());
	
 //	var _noteMessageContent = "客户【"+CcrAppInfo.cus_name._getValue() + "】流水号为【"+ _pkVal +"】的信用评级/信用评分";
 //	WfiJoin.note_message_content._setValue(_noteMessageContent);
	
	var _rurl=EMPTools.encodeURI('<emp:url action="queryCcrAppInfoList.do"/>');
	var _variable = "" ;
	initWFSubmit(_applType, _wfSign, _modelId, _pkCol, _pkVal,_variable,"",_status,_rurl);

}

	function saveChange(){
	//	CcrAppInfo.adj_auto_grade1._setValue(CcrAppDetail.adjusted_grade._getValue());
	//	ccrAppTab.tabs.appinf.refresh();
	}
	function saveScore(){
		var all_score = CcrAppDetail.all_score._getValue();
		var adjusted_score = CcrAppDetail.adjusted_score._getValue();
		var auto_score = CcrAppDetail.auto_score._getValue();
		
		if(parseFloat(adjusted_score )>parseFloat(all_score)){
			alert('调整后得分必须小于等于'+all_score);
			CcrAppDetail.adjusted_score._setValue(auto_score);
			
			return false;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
		<form id = "creditRatingForm" action="creditRating.do" method='post' onsubmit="return checkRequired();">
			<input type="hidden" name="EMP_SID" value="${context.EMP_SID}"/>
			<input type="hidden" name="serno" value="${context.serno}"/>
			<input type="hidden" name="cus_id" value="${context.cus_id}"/>
			<input type="hidden" name="model_no" value="${context.model_no}"/>
			<input type="hidden" name="fnc_year" value="${context.fnc_year}"/>
			<input type="hidden" name="fnc_month" value="${context.fnc_month}"/>
			<input type="hidden" name="stat_prd_style" value="${context.stat_prd_style}"/>
			<jsp:include page="../../ind/jspfiles/${context.model_no}.jsp" flush="true"/>
		</form>
		<emp:form id="updateGradeForm" action="updateSugGrade.do" method="POST" >
		</emp:form>
		<div align="center">
		<emp:gridLayout id="scorePanel" maxColumn="2" title="信用评级得分">
	    	<emp:text id="CcrAppInfo.serno" label="业务编号" maxlength="40" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="${context.serno}"/>
	    	<emp:text id="CcrAppInfo.cus_id" label="客户码" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="${context.cus_id}"/>
	    	<emp:text id="CcrAppInfo.manager_id" label="主管客户经理" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="$currentUserId"/>
	    	<emp:text id="CcrAppInfo.manager_br_id" label="管理机构" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="$organNo"/>
	    	<emp:text id="CcrAppInfo.reg_date" label="登记日期" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="$OPENDAY"/>
	    	<emp:text id="CcrAppInfo.approve_status" label="申请状态" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="000"/>
	    	<emp:text id="CcrAppInfo.flag" label="申请类型" maxlength="16" required="false"  readonly="true" colSpan="2" hidden="true" defvalue="7"/>
	    	
			<emp:text id="CcrAppDetail.all_score" label="评级总分" maxlength="16" required="false"  readonly="true" colSpan="2" />
			<emp:text id="CcrAppDetail.auto_score" label="机评得分" maxlength="18" required="false" readonly="true" colSpan="2" dataType="Currency"/>
			<emp:select id="CcrAppDetail.auto_grade" label="机评等级"  required="false" disabled="true" dictname="STD_ZB_FINA_GRADE" colSpan="2" />
			<emp:select id="CcrAppDetail.limit_grade" label="限定等级" required="false" disabled="true" dictname="STD_ZB_FINA_GRADE" colSpan="2" hidden="true"/>
			<emp:textarea id="CcrAppDetail.limit_reason" label="限定原因" maxlength="500" required="false" colSpan="2" readonly="true" hidden="true"/>
			<emp:select id="CcrAppDetail.adjusted_grade" label="客户经理调整等级" required="true" colSpan="2"  dictname="STD_ZB_FINA_GRADE" onblur="saveChange()"/>
			<emp:textarea id="CcrAppDetail.reason" label="评级理由" maxlength="500" required="false"  />		
			
		</emp:gridLayout>
		</div>
		<div align="center">
			<br>
			<emp:actButton id="creditRating" label="获得机评得分" op="update" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
			<emp:actButton id="saveGrade" label="保 存" op="update"/>
		</div>
</body>
</html>
</emp:page>
