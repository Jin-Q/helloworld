<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	String smallFlag = (String)request.getAttribute("smallFlag");
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isFlag = (String)context.getDataValue("is_authorize");
	request.setAttribute("canwrite","");
	String flow = "";//流程查看标识
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
	}
	String flag = "";//控制菜单标志
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input_user_name { /****** 长度固定 ******/
	width: 451px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.lab{
	font-size:10pt;
	font-style:宋体; 
	color:#2B527D;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
function setconId(data){
	CcrAppInfo.manager_id_displayname._setValue(data.actorname._getValue());
	CcrAppInfo.manager_id._setValue(data.actorno._getValue());
}

function getOrgID(data){
	CcrAppInfo.manager_br_id._setValue(data.organno._getValue());
	CcrAppInfo.manager_br_id_displayname._setValue(data.organname._getValue());
}	
function doload(){
 	addOneButton();  
	CcrAppDetail.limit_reason._obj._renderHidden(true);
	CcrAppDetail.limit_grade._obj._renderHidden(true);
	var flag = CcrAppInfo.approve_status._getValue();
	if(flag=="997"){
		//CusCom.cus_crd_grade._obj._renderHidden(true);  
		//CusCom.com_crd_dt._obj._renderHidden(true);
	}
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
	
	
    function doReturn() {
		var url = '<emp:url action="queryCcrAppFinGuarList.do"/>?flag=<%=flag%>';
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
	if(changeAdjuest_grade()){
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
function doSubmitWF(){
	var serno = CcrAppInfo.serno._getValue();
		var approve_status = CcrAppInfo.approve_status._getValue() ;
	
//    var _status = CcrAppInfo.approve_status._getValue() ;
//    if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'){
//		alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
//		return;
//	}
	WfiJoin.table_name._setValue("CcrAppInfo");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
    WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue("650");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：CcrAppFinGuar 
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
		if(cusType!=null&&cusType!=''&&cusType.length>0){
			if(cusType.substring(0,1)=='1'){
				url= "<emp:url action='getCusViewPage.do'/>&info=tree&cusId="+CcrAppInfo.cus_id._getValue(); 
			}else{
				url = "<emp:url action='getCusViewPage.do'/>&info=tree&cusId="+CcrAppInfo.cus_id._getValue();
			} 
		}else{
			url = "<emp:url action='getCusViewPage.do'/>&info=tree&cusId="+CcrAppInfo.cus_id._getValue();
		}  
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//查看模型详细信息
	function viewModelInfo(){
      var url = "<emp:url action='queryIndModelDetail.do'/>&model_no="+CcrAppDetail.model_no._getValue();
      url=encodeURI(url); 
      window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	function saveChange(){
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

	function checkSpac(){
		var spac = LmtAppFinGuar.fin_totl_spac._getValue();//融资总敞口
		var totl = LmtAppFinGuar.fin_totl_limit._getValue();//融资总额
		if(parseFloat(spac)>parseFloat(totl)){
			alert("融资总敞口应小于等于融资总额！");
			LmtAppFinGuar.fin_totl_spac._setValue("");
		}
	}

	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = CcrAppInfo.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = CcrAppInfo.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	
	/*--user code end--*/
	
</script>
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
			<emp:select id="CcrAppInfo.cus_type" label="客户类型 " required="true"  dictname="STD_ZB_CUS_TYPE" defvalue="${context.cus_type}" readonly="true"/>
			<emp:text id="CcrAppInfo.cus_id_displayname" label="客户名称"  cssElementClass="emp_field_text_input_user_name" colSpan = "2" required="true" readonly="true" defvalue="${context.cus_name}"/>
			<emp:select id="CcrAppDetail.is_authorize" label="是否授信" required="false"  readonly="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="CcrAppInfo.app_begin_date" label="申请日期" maxlength="10" required="true" readonly="true"/>
			<emp:text id="CcrAppDetail.bail_multi" label="担保放大倍数" maxlength="30" required="true" dataType="Currency" onchange="doWrite()"/>
			<emp:select id="CcrAppDetail.guar_type" label="担保类别" required="true" dictname="STD_ZB_GUAR_TYPE" onchange="doWriteTo()"/>
			<emp:date id="CcrAppInfo.expiring_date" label="到期日期" required="false" readonly="true" colSpan="2" hidden="true"/>
		    <emp:text id="CcrAppInfo.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="4"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusComGroup" maxColumn="2" title="信用评级信息">
		    <emp:select id="CcrAppInfo.auto_grade1" label="机评信用等级"  required="false" readonly="true" dictname="STD_ZB_FINA_GRADE" defvalue="$CcrAppDetail.auto_grade"/>
			<emp:select id="CcrAppInfo.adj_auto_grade1" label="调整后信用等级" required="false"  readonly="true" dictname="STD_ZB_FINA_GRADE" defvalue="$CcrAppDetail.adjusted_grade" />
			<emp:select id="CcrAppDetail.last_adjusted_grade" label="上次信用等级" required="false"  readonly="true"  dictname="STD_ZB_FINA_GRADE"/>
			<emp:text id="CcrAppDetail.lat_app_end_date" label="上次评级日期" maxlength="10" required="false"  readonly="true"/>
			<emp:text id="CcrAppDetail.auto_score1" label="机评得分" maxlength="16" required="false" readonly="true" defvalue="$CcrAppDetail.auto_score" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CcrAppInfo.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CcrAppInfo.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true"/>
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
    	<div align="left">
	         <label class="lab"><b>${context.cus_name}(${context.model_name})信用等级评定一览表：（所用财报时间：${context.CcrAppDetail.fnc_year}-${context.CcrAppDetail.fnc_month};财报类型：${context.fnc_type_name}）
	         </b></label>
	    </div>
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
			<emp:select id="CcrAppDetail.auto_grade" label="机评等级" required="false"  disabled="true" dictname="STD_ZB_FINA_GRADE" colSpan="2" />
			<emp:select id="CcrAppDetail.limit_grade" label="限定等级" required="false" disabled="true" dictname="STD_ZB_FINA_GRADE" colSpan="2" />
			<emp:textarea id="CcrAppDetail.limit_reason" label="限定原因" maxlength="500" required="false" colSpan="2" />
			<emp:select id="CcrAppDetail.adjusted_grade" label="客户经理调整等级" required="true" colSpan="2"  dictname="STD_ZB_FINA_GRADE" onblur="saveChange()"/>
			<emp:textarea id="CcrAppDetail.reason" label="评级理由" maxlength="500" required="false"  />		
		</emp:gridLayout>
		</div>
		</form>
		<emp:form id="updateGradeForm" action="updateSugGrade.do" method="POST" >
		</emp:form>	
  </emp:tab>
  <emp:tab id="fin_tab" label="财务报表" url="queryFncStatLmtList.do" reqParams="cus_id=$CcrAppInfo.cus_id;" initial="false" needFlush="true"/>
	<!-- modify by jiangcuihua 2019-03-16 isFlag 可能为null -->
	<% if("1".equals(isFlag)){//是授信项下值为，“是”时显示%>
   <emp:tab label="融资性担保公司额度申请" id="lmt_app"  needFlush="true" initial="true">
		<emp:gridLayout id="LmtAppFinGuarGroup" title="担保公司信息" maxColumn="2">
			<emp:text id="LmtAppFinGuar.serno" label="业务编号" maxlength="40" required="false" hidden="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:pop id="LmtAppFinGuar.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_type='A2' and cus_status='20'&returnMethod=returnCus" required="true" colSpan="2" readonly="true"/>
			<emp:text id="LmtAppFinGuar.cus_id_displayname" label="客户名称"  colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true" defvalue="${context.cus_name}"/>
			<emp:text id="LmtAppFinGuar.fin_cls" label="融资类别" maxlength="20" required="false" defvalue="融资性担保公司" readonly="true"/>
			<emp:select id="LmtAppFinGuar.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" defvalue="1" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppFinGuarGroup" title="融资额度信息" maxColumn="2">
			<emp:select id="LmtAppFinGuar.guar_cls" label="担保类别" required="true" dictname="STD_ZB_GUAR_TYPE" readonly="true"/>
			<emp:select id="LmtAppFinGuar.eval_rst" label="评级结果" required="true" dictname="STD_ZB_FINA_GRADE" readonly="true"/>
			<emp:text id="LmtAppFinGuar.guar_bail_multiple" label="担保放大倍数" maxlength="10" required="true" readonly="true"/>
			<emp:select id="LmtAppFinGuar.fin_cur_type" label="融资币种" dictname="STD_ZX_CUR_TYPE" required="true" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppFinGuar.fin_totl_limit" label="融资总额" maxlength="18" required="true" dataType="Currency" onchange="checkMoney()" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFinGuar.single_quota" label="单户限额" maxlength="18" required="true" dataType="Currency" onchange="checkMoney()" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFinGuar.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" onblur="checkSpac()" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAppFinGuar.lmt_term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" required="true"/>
			<emp:text id="LmtAppFinGuar.term" label="期限" maxlength="2" required="true"/>
			<emp:date id="LmtAppFinGuar.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:date id="LmtAppFinGuar.end_date" label="办结日期" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppFinGuarGroup" title="登记信息" maxColumn="2">
			<emp:pop id="LmtAppFinGuar.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" readonly="true"/>
			<emp:pop id="LmtAppFinGuar.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="true"/>
			<emp:text id="LmtAppFinGuar.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserId" readonly="true"/>
			<emp:text id="LmtAppFinGuar.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organNo" readonly="true"/>
			<emp:text id="LmtAppFinGuar.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="LmtAppFinGuar.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
			<emp:text id="LmtAppFinGuar.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtAppFinGuar.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtAppFinGuar.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="LmtAppFinGuar.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="true"/>
		</emp:gridLayout>
  </emp:tab>
  <%} %>
	</emp:tabGroup>	
		<div align="center">
			<br>
			<%if(flow.equals("wf")){ %>
			<%}else{ %>
			<emp:button id="return" label="返回到列表页面"/>
			<%} %>
			<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		</div>
<div id = "test"></div>
</body>
</html>
</emp:page>

