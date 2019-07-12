<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String smallFlag = (String)request.getAttribute("smallFlag");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<style type="text/css">

.emp_field_text_input_user_name { /****** 长度固定 ******/
	width: 451px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

</style>
<script type="text/javascript"><!--
	
	/*--user code begin--*/
	
/*--修改等级选项框值--*/
function updateGrade(){
		var optionJosn = "00,11,12,13,14,15,16,17,18,19";
		var options =CcrAppInfo.adj_auto_grade1._obj.element.options;
		var opt1 = CcrAppInfo.auto_grade1._getValue();
		
		for ( var i = options.length - 1; i >= 0; i--) {
			if(opt1 != null && opt1 != '' ){
				var opt2 = opt1-1;
				var opt3 = opt2+2;
				if(opt1 != options[i].value && opt3 != options[i].value && opt2 != options[i].value){			
					options.remove(i);
				}
			}
	}
}		

function setconId(data){
	CcrAppInfo.manager_id_displayname._setValue(data.actorname._getValue());
	CcrAppInfo.manager_id._setValue(data.actorno._getValue());
	CcrAppInfo.manager_br_id._setValue(data.orgid._getValue());
	CcrAppInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
	//CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(true);
	doOrgCheck();
}

function doOrgCheck(){
	var handleSuccess = function(o) {
		if (o.responseText !== undefined) {
			try {
				var jsonstr = eval("(" + o.responseText + ")");
			} catch (e) {
				alert("Parse jsonstr define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			if("one" == flag){//客户经理只属于一个机构
				CcrAppInfo.manager_br_id._setValue(jsonstr.org);
				CcrAppInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
			}else if("more" == flag){//客户经理属于多个机构
				CcrAppInfo.manager_br_id._setValue("");
				CcrAppInfo.manager_br_id_displayname._setValue("");
				CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(false);
				var manager_id = CcrAppInfo.manager_id._getValue();
				CcrAppInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
			}else if("yteam"==flag){
				CcrAppInfo.manager_br_id._setValue("");
				CcrAppInfo.manager_br_id_displayname._setValue("");
				CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(false);
				CcrAppInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
			}
		}
	};
	var handleFailure = function(o) {
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var manager_id = CcrAppInfo.manager_id._getValue();
	var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
}
function getOrgID(data){
	CcrAppInfo.manager_br_id._setValue(data.organno._getValue());
	CcrAppInfo.manager_br_id_displayname._setValue(data.organname._getValue());
}
function doload(){
 	addOneButton();  
	var	cusType = '${context.CcrAppInfo.cus_type}';		
	var modelNo='${context.model_name}';
	CcrAppDetail.limit_reason._obj._renderHidden(true);
	CcrAppDetail.limit_grade._obj._renderHidden(true);
	var itms1 = document.getElementById('CcrAppDetail.adjusted_grade').options;//更改客户经理调整等级的字典项
    itms1.remove("01");
    var cus_type = "${context.cus_type}";
	if(cus_type == null || cus_type == ""){
		CcrAppInfo.cus_type._obj._renderHidden(true);
		CcrAppInfo.cus_type._obj._renderRequired(false);
	}
}	
//判断是否已经发起了流程
function checkApplyStatus(){
	var status=CcrAppInfo.approve_status._getValue();
	if('000'==status||'992'==status||'993'==status){
		return true;
	}else{
		return false;
	}
}	
	
	
    function doReturn() {
		var url = '<emp:url action="queryCcrAppInBankList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};	
	
	function doReturnMethod(json, callback){
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
		
		//调试用js
		var textnode = document.createTextNode(str);
		var testdiv = document.getElementById("test");
		testdiv.appendChild(textnode);
		//EMPTools.removeWait();
		var button = document.getElementById('button_creditRating');
		button.disabled="";	
		//如果调整得分不为空,则更新调整等级
		if(CcrAppDetail.adjusted_grade._getValue()!=''){
			saveAdjuest_gradeOnlyNoFlush();
		}else{
			window.ccrAppTab.tabs['ratingtab']._clickLink();
			alert("获取机评得分成功，请录入客户经理调整等级后再进行“保存”或者“放入流程”操作！");
		}
	}
	function doCreditRating(){
		var result = CcrAppInfo._checkAll();
	    if(result){
		    //首先对申请信息的部分修改做保存（获得机评得分时，客户经理调整等级不可能出现）
		    window.ccrAppTab.tabs['ratingtab']._clickLink();
		    var form = document.getElementById("updateGradeForm");
	    	CcrAppInfo.manager_id._toForm(form);
	    	CcrAppInfo.manager_br_id._toForm(form);
	    	CcrAppInfo.reg_date._toForm(form);
	    	CcrAppInfo.serno._toForm(form);
	    	CcrAppInfo.cus_id._toForm(form);
	    	CcrAppInfo.flag._toForm(form);
	    	CcrAppDetail.adjusted_grade._toForm(form);
	    	CcrAppDetail.reason._toForm(form);
	    	CcrAppDetail.o_rating_unit._toForm(form);
	    	CcrAppDetail.o_rating_result._toForm(form);
	    	CcrAppDetail.o_rating_date._toForm(form);
	    	var callback = {
	    			success : "checkNSubmitScore",
	    			isJSON : true,
	    			form : "updateGradeForm"
	    		};	
	    	EMPTools.ajaxRequest('POST',form.action,callback);
			if(!checkApplyStatus()){
				alert("已经发起的流程不允许修改");
				return "";
			}
			if(!checkRequired()){
				return;
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
	    }else{
	        alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	        window.ccrAppTab.tabs['appinf']._clickLink();
		}
	}	
	//只保存不刷新
	function checkNSubmitScore(data){
		if(data.ccr_result=="success"){
		}else{
			alert("保存失败");
		}
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

//发起流程按钮事件
function doSaveAdjuest_grade(){
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
	var form = document.getElementById("updateGradeForm");
	var result = CcrAppInfo._checkAll();
 	var result1 = CcrAppDetail._checkAll();
    if(result&&result1){
    	CcrAppInfo.manager_id._toForm(form);
    	CcrAppInfo.manager_br_id._toForm(form);
    	CcrAppInfo.reg_date._toForm(form);
    	CcrAppInfo.serno._toForm(form);
    	CcrAppInfo.cus_id._toForm(form);
    	CcrAppInfo.flag._toForm(form);
    	CcrAppDetail.adjusted_grade._toForm(form);
    	CcrAppDetail.reason._toForm(form);
    	CcrAppDetail.o_rating_unit._toForm(form);
    	CcrAppDetail.o_rating_result._toForm(form);
    	CcrAppDetail.o_rating_date._toForm(form);
    	var callback = {
    			success : "checkNSubmit",
    			isJSON : true,
    			form : "updateGradeForm"
    		};	
    	EMPTools.ajaxRequest('POST', form.action, callback);
    }else {
	    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	}
}
//提交流程之前的保存成功事件
function checkNSubmit(data){
	if(data.ccr_result=="success"){
		doSubmitWF();
	}else{
		alert("更新建议等级失败");
	}
}

function changeAdjuest_grade(){
	if(CcrAppDetail.auto_score._getValue()==''){
		alert("必须先[获得机评得分]才可以进行此操作");
		return false;
	}else{
		return true;
	}
}
//只保存修正得分最终等级之类字段,不进行提交
function saveAdjuest_gradeOnly(){
	
	//虽然OP叫UpdateSugGradeOp,但是其实保存的是调整得分以及调整后等级,以及调整后的最后得分.
	//最后得分将在流程结束后根据流程中的调整得分 再次修改.
	//建议等级已经失效.
	var form = document.getElementById("updateGradeForm");
	var result = CcrAppInfo._checkAll();
 	var result1 = CcrAppDetail._checkAll();
    if(result&&result1){
    	CcrAppInfo.manager_id._toForm(form);
    	CcrAppInfo.manager_br_id._toForm(form);
    	CcrAppInfo.reg_date._toForm(form);
    	CcrAppInfo.serno._toForm(form);
    	CcrAppInfo.cus_id._toForm(form);
    	CcrAppInfo.flag._toForm(form);
    //	CcrAppDetail.auto_score._toForm(form);
    //	CcrAppDetail.auto_grade._toForm(form);
    	CcrAppDetail.adjusted_grade._toForm(form);
    	CcrAppDetail.reason._toForm(form);
    	CcrAppDetail.o_rating_unit._toForm(form);
    	CcrAppDetail.o_rating_result._toForm(form);
    	CcrAppDetail.o_rating_date._toForm(form);
    	var callback = {
    			success : "retSaveAdjuest",
    			isJSON : true,
    			form : "updateGradeForm"
    		};	
    	EMPTools.ajaxRequest('POST', form.action, callback);
    }else {
    	window.ccrAppTab.tabs['ratingtab']._clickLink();
	    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	}
}
//只保存修正得分最终等级之类字段,不进行提交和当前页面的刷新
function saveAdjuest_gradeOnlyNoFlush(){
	
	//虽然OP叫UpdateSugGradeOp,但是其实保存的是调整得分以及调整后等级,以及调整后的最后得分.
	//最后得分将在流程结束后根据流程中的调整得分 再次修改.
	//建议等级已经失效.
	var form = document.getElementById("updateGradeForm");
	var result = CcrAppInfo._checkAll();
 	var result1 = CcrAppDetail._checkAll();
    if(result&&result1){
    	CcrAppInfo.manager_id._toForm(form);
    	CcrAppInfo.manager_br_id._toForm(form);
    	CcrAppInfo.reg_date._toForm(form);
    	CcrAppInfo.serno._toForm(form);
    	CcrAppInfo.cus_id._toForm(form);
    	CcrAppInfo.flag._toForm(form);
    //	CcrAppDetail.auto_score._toForm(form);
    //	CcrAppDetail.auto_grade._toForm(form);
    	CcrAppDetail.adjusted_grade._toForm(form);
    	CcrAppDetail.reason._toForm(form);
    	CcrAppDetail.o_rating_unit._toForm(form);
    	CcrAppDetail.o_rating_result._toForm(form);
    	CcrAppDetail.o_rating_date._toForm(form);
    	
    	var callback = {
    			success : "retSaveAdjuestNoFlush",
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
function retSaveAdjuestNoFlush(data){
	if(data.ccr_result=="success"){
		window.ccrAppTab.tabs['ratingtab']._clickLink();
		alert("获取机评得分成功！");
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
function modelScore2Grade(score){
	var newComFlag = '${context.CcrAppDetail.new_com_flag}';

		if('1' == newComFlag){			//一般企业的新增开户
			if(score-30.0<0){
				return '16';
			}else if(score-35.0<0){
				return '15';
			}else if(score-45.0<0){
				return '14';
			}else if(score-55.0<0){
				return '13';
			}else if(score-65.0<0){
				return '12';
			}else{
				return '11';
			}		
		}else if('2' == newComFlag){	//一般企业的存量客户
			if(score-45.0<0){
				return '16';
			}else if(score-55.0<0){
				return '15';
			}else if(score-65.0<0){
				return '14';
			}else if(score-75.0<0){
				return '13';
			}else if(score-85.0<0){
				return '12';
			}else{
				return '11';
			}
		}	

}

function doSubmitWF(){
	var serno = CcrAppInfo.serno._getValue();
    var approve_status = CcrAppInfo.approve_status._getValue() ;
    var cus_name = CcrAppInfo.cus_id_displayname._getValue();
//    if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'){
//		alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
//		return;
//	}
	var cus_id = CcrAppInfo.cus_id._getValue();//客户码
	var cus_name = CcrAppInfo.cus_id_displayname._getValue();//客户名称
	WfiJoin.table_name._setValue("CcrAppInfo");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
	WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue("660");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：ccr_app_info
	WfiJoin.cus_id._setValue(cus_id);
	WfiJoin.cus_name._setValue(cus_name);
	WfiJoin.prd_name._setValue("信用评级-同业信用评级");
	initWFSubmit(false);
}

	function addOneButton(){  
	//	CcrAppInfo.model_no._obj.addOneButton('view11','查看',viewModelInfo);  //在评级模型后面新增 查看 按钮
		CcrAppInfo.cus_id._obj.addOneButton('view12','查看',viewCusInfo);   //在客户码后面新增 查看 按钮
	}

	//查看客户详细信息
	function viewCusInfo(){
		var cusType=CcrAppInfo.cus_type._getValue();
		var url = "";
		url= "<emp:url action='getCusSameOrgViewPage.do'/>&type=cusSame&cus_id="+CcrAppInfo.cus_id._getValue()+"&op=view&restrict_tab=Y"; 
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	//查看模型详细信息
	function viewModelInfo(){
      var url = "<emp:url action='queryIndModelDetail.do'/>&model_no="+CcrAppDetail.model_no._getValue();
      url=encodeURI(url); 
      window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	/*--根据模型编号判断个人和企业的相应的显示--*/
	function doDisplayWithJudge(){
		var modelNo='${context.CcrAppDetail.model_no}';
		
		if("M20091200009" == modelNo){	//个人

			//CcrAppDetail.last_grade._obj._renderHidden(true);
			//CcrAppDetail.last_score._obj._renderHidden(false);
			//CcrAppDetail.com_scale_ccr._obj._renderHidden(true);
			//CcrAppDetail.auto_grade._obj._renderHidden(true);
			//CcrAppDetail.adjusted_grade._obj._renderHidden(true);
			//CcrAppDetail.reason._obj._renderHidden(true);
			//CcrAppDetail.auto_grade2._obj._renderHidden(true);
			//CcrAppDetail.adjusted_grade2._obj._renderHidden(true);
			//CcrAppDetail.limit_grade._obj._renderHidden(true);
			//CcrAppDetail.limit_reason._obj._renderHidden(true);
		}else{							//企业
			CcrAppDetail.adjust_score._obj._renderHidden(true);
			CcrAppDetail.adjusted_score._obj._renderHidden(true);
			CcrAppDetail.auto_score._obj._renderHidden(true);
			CcrAppDetail.adjusted_score2._obj._renderHidden(true);
			//修改等级
			if("M20091200019" == modelNo || "M20091200021" == modelNo){
				CcrAppDetail.limit_grade._obj._renderHidden(true);
				CcrAppDetail.limit_reason._obj._renderHidden(true);
			}
			updateGrade();
		}		
	}
	function saveChange(){
		CcrAppDetail.adjusted_grade._setValue(CcrAppDetail.adjusted_grade._getValue());
		CcrAppDetail.auto_score1._setValue(CcrAppDetail.auto_score._getValue());
		CcrAppInfo.auto_grade1._setValue(CcrAppDetail.auto_grade._getValue());
		CcrAppInfo.adj_auto_grade1._setValue(CcrAppDetail.adjusted_grade._getValue());
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
	
--></script>
</head>
<body class="page_content" onload="doload()">
<emp:tabGroup id="ccrAppTab" mainTab="appinf">
  <emp:tab label="申请信息" id="appinf"  needFlush="true" initial="true" >
		<emp:form id="submitWFForm" action="#" method="POST" >
			<emp:text id="caseNo" label="caseNo" required="false" maxlength="500" hidden="true" defvalue="$CcrAppInfo.serno"/>  
			<emp:text id="caseName" label="caseName" required="false" maxlength="500" hidden="true" defvalue="单笔信用评级"/>
			<emp:text id="bizCateCd" label="bizCateCd" required="false" maxlength="500" hidden="true" defvalue="BIZ221"/>
			<emp:text id="relAmt" label="relAmt" required="false" maxlength="500" hidden="true"/>
			<emp:text id="cusType" label="cusType" required="false" maxlength="500" hidden="true"/>
			<emp:text id="comScaleCcr" label="comScaleCcr" required="false" maxlength="500" hidden="true"/>
			<emp:text id="inCusId" label="inCusId" required="false" maxlength="500" hidden="true" defvalue="$CcrAppInfo.cus_id"/>
			<emp:text id="cusName" label="cusName" required="false" maxlength="500" hidden="true" defvalue="$CcrAppInfo.cus_name"/>
			<emp:text id="finalGrade" label="finalGrade" required="false" maxlength="500" hidden="true"/>
			<emp:text id="adjustScore" label="adjustScore" required="false" maxlength="500" hidden="true"/>
		</emp:form>	
		<emp:gridLayout id="CcrAppInfoGroup" maxColumn="2" title="信用评级基本信息">
			<emp:text id="CcrAppInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="CcrAppDetail.model_no_displayname" label="评级模型"  required="false"  readonly="true" defvalue="${context.model_name}"/>
			<emp:text id="CcrAppInfo.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CcrAppInfo.cus_id_displayname" label="客户名称"  cssElementClass="emp_field_text_input_user_name" colSpan = "2" required="true" readonly="true" defvalue="${context.cus_name}"/>
			<emp:select id="CcrAppInfo.cus_type" label="客户类型 " required="true"  dictname="STD_ZB_INTER_BANK_ORG" defvalue="${context.cus_type}" readonly="true"/>
			<emp:text id="CcrAppInfo.app_begin_date" label="申请日期" maxlength="10" required="true" readonly="true"/>
			<emp:date id="CcrAppInfo.expiring_date" label="到期日期" required="false" readonly="true" colSpan="2" hidden="true"/>
			
		
		</emp:gridLayout>
		<emp:gridLayout id="CusComGroup" maxColumn="2" title="信用评级评级信息">
			<emp:text id="CcrAppDetail.auto_score1" label="机评得分" maxlength="16" required="false" readonly="true" defvalue="$CcrAppDetail.auto_score"/>
			<emp:select id="CcrAppInfo.auto_grade1" label="机评信用等级"  required="false" readonly="true" dictname="STD_ZB_FINA_GRADE" defvalue="$CcrAppDetail.auto_grade"/>
			<emp:select id="CcrAppInfo.adj_auto_grade1" label="调整后信用等级" required="false"  readonly="true" dictname="STD_ZB_FINA_GRADE" defvalue="$CcrAppDetail.adjusted_grade" />
			<emp:select id="CcrAppDetail.last_adjusted_grade" label="上次信用等级" required="false"  readonly="true"  dictname="STD_ZB_FINA_GRADE" />
			<emp:text id="CcrAppDetail.o_rating_unit" label="外部评级单位" maxlength="100" required="false"  readonly="false" hidden="false"/>
		    <emp:select id="CcrAppDetail.o_rating_result" label="外部评级结果" required="false"  readonly="false"  dictname="STD_ZB_FINA_GRADE"/>
			<emp:date id="CcrAppDetail.o_rating_date" label="外部评级日期" required="false"  readonly="false" hidden="false"/>
			<emp:text id="CcrAppDetail.lat_app_end_date" label="上次评级日期" maxlength="10" required="false"  readonly="true" hidden="true"/>
		    <emp:text id="CcrAppInfo.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="3"/>
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CcrAppInfo.manager_id_displayname" label="主管客户经理" required="true" readonly="false" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CcrAppInfo.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="CcrAppInfo.input_id_displayname" label="登记人"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="CcrAppInfo.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="false"/>
			<emp:text id="CcrAppInfo.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="CcrAppInfo.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="CcrAppInfo.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CcrAppInfo.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
	</emp:tab>
  <emp:tab label="信用等级评估表" id="ratingtab"  needFlush="true" initial="true">
		<form id = "creditRatingForm" action="creditRating.do" method='post' onsubmit="return checkRequired();">
		<input type="hidden" name="EMP_SID" value="${context.EMP_SID}"/>
		<input type="hidden" name="serno" value="${context.CcrAppInfo.serno}"/>
		<input type="hidden" name="cus_id" value="${context.CcrAppInfo.cus_id}"/>
		<input type="hidden" name="model_no" value="${context.CcrAppDetail.model_no}"/>
		<input type="hidden" name="fnc_year" value="${context.CcrAppDetail.fnc_year}"/>
		<input type="hidden" name="fnc_month" value="${context.CcrAppDetail.fnc_month}"/>
		<input type="hidden" name="stat_prd_style" value="${context.CcrAppDetail.stat_prd_style}"/>
		<jsp:include page="../../ind/jspfiles/${context.CcrAppDetail.model_no}.jsp" flush="true"/>
		<div align="center">
		<emp:gridLayout id="scorePanel" maxColumn="2" title="信用评级得分">
			<emp:text id="CcrAppDetail.all_score" label="评级总分" maxlength="16" required="false"  readonly="true" colSpan="2" />
			<emp:text id="CcrAppDetail.auto_score" label="机评得分" maxlength="16" required="false" readonly="true" colSpan="2" />
			<emp:select id="CcrAppDetail.auto_grade" label="机评等级"  required="false" disabled="true" dictname="STD_ZB_FINA_GRADE" colSpan="2" />
			<emp:select id="CcrAppDetail.limit_grade" label="限定等级" required="false" dictname="STD_ZB_FINA_GRADE" disabled="true" colSpan="2" />
			<emp:textarea id="CcrAppDetail.limit_reason" label="限定原因" maxlength="500" required="false" colSpan="2" />
			<emp:select id="CcrAppDetail.adjusted_grade" label="客户经理调整等级" readonly="false" required="true" colSpan="2"  dictname="STD_ZB_FINA_GRADE" onblur="saveChange()"/>
			<emp:textarea id="CcrAppDetail.reason" label="评级理由" maxlength="500" required="false"  />		
		</emp:gridLayout>
		</div>
		</form>
		<emp:form id="updateGradeForm" action="updateSugGrade.do" method="POST" >
		</emp:form>	
  </emp:tab>
  <emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
		<div align="center">
			<br>
			<emp:button id="creditRating" label="获得机评得分" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
			<emp:button id="saveGrade" label="保 存"/>
			<!--<emp:button id="saveAdjuest_grade" label="放入流程"/>-->
			<emp:button id="return" label="返 回"/>
		</div>
<div id = "test"></div>
</body>
</html>
</emp:page>
