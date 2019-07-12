<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String oper="";
if(context.containsKey("oper")){
	//用来控制显示按钮是修改还是签订
	oper=(String)context.getDataValue("oper");
}
String flag = "";
if(context.containsKey("flag")){
	//用来控制tab页的按钮操作
	flag=(String)context.getDataValue("flag");
}

String rel = "";
if(context.containsKey("rel")){
	rel=(String)context.getDataValue("rel");
}
String isCreditChange = "";
if(context.containsKey("isCreditChange")){
	isCreditChange=(String)context.getDataValue("isCreditChange");
}
String canUpdate = "";
if(context.containsKey("canUpdate")){
	canUpdate=(String)context.getDataValue("canUpdate");
}
//担保变更签订时，所传的参数
String menu = "";
if(context.containsKey("menu")){
	menu=(String)context.getDataValue("menu");
}
if(oper.equals("sign")){
	request.setAttribute("canwrite","");
}
//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
String modify_rel_serno = "";
if(context.containsKey("modify_rel_serno")){
	modify_rel_serno =(String)context.getDataValue("modify_rel_serno");
} 
//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
%>   
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 450px;
	};
</style>
<script type="text/javascript">
//客户pop返回事件
function returnCus(data){
	GrtGuarCont.cus_id._setValue(data.cus_id._getValue());
	GrtGuarCont.cus_id_displayname._setValue(data.cus_name._getValue());
}
//客户经理pop返回方法
function setconId(data){
	GrtGuarCont.manager_id_displayname._setValue(data.actorname._getValue());
	GrtGuarCont.manager_id._setValue(data.actorno._getValue());
	GrtGuarCont.manager_br_id._setValue(data.orgid._getValue());
	GrtGuarCont.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
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
				GrtGuarCont.manager_br_id._setValue(jsonstr.org);
				GrtGuarCont.manager_br_id_displayname._setValue(jsonstr.orgName);
			}else if("more" == flag){//客户经理属于多个机构
				GrtGuarCont.manager_br_id._setValue("");
				GrtGuarCont.manager_br_id_displayname._setValue("");
				GrtGuarCont.manager_br_id_displayname._obj._renderReadonly(false);
				var manager_id = GrtGuarCont.manager_id._getValue();
				GrtGuarCont.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
			}else if("yteam"==flag){
				GrtGuarCont.manager_br_id._setValue("");
				GrtGuarCont.manager_br_id_displayname._setValue("");
				GrtGuarCont.manager_br_id_displayname._obj._renderReadonly(false);
				GrtGuarCont.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
			}
		}
	};
	var handleFailure = function(o) {
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var manager_id = GrtGuarCont.manager_id._getValue();
	var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
}

//机构pop返回方法
function getOrgID(data){
	GrtGuarCont.manager_br_id._setValue(data.organno._getValue());
	GrtGuarCont.manager_br_id_displayname._setValue(data.organname._getValue());
}

//窗口关闭事件
function window.onbeforeunload(){        
  //用户点击浏览器右上角关闭按钮
  if((event.clientX > (document.body.clientWidth-15) && event.clientY<0) || event.altKey){   
	  doReturn();
  }
  //用户点击任务栏，右键关闭
  else if(event.clientY > document.body.clientHeight || event.altKey){
	  doReturn();
  }
} 

//返回按钮事件
function doReturn() {
	var type = GrtGuarCont.guar_cont_type._getValue();
	var cus_id = GrtGuarCont.cus_id._getValue();
	var url;
	if(type=="01"){
		action="zg";
	}else{
		action="yb";
	}
	<%if(flag.equals("ybWh")){%>
	window.close();
	window.opener.location.reload();  
	<%}else if(menu.equals("dbbg")){%>
	url = '<emp:url action="QueryGuarChangeSignList.do"/>?&menuId=QueryGuarChangeSignList';
	<%}else{%>
	url = '<emp:url action="queryGrtGuarContList.do"/>?action='+action;
	<%}%> 
	url = EMPTools.encodeURI(url);
	window.location=url;
};
//业务修改时保存前校验。
function doCheckCurSave(){
	var guar_cont_amt = GrtGuarCont.guar_amt._getValue(); 
    var guar_amt = GrtLoan.guar_amt._getValue(); 
    if(guar_amt == null || guar_amt == ""){
    	alert("请输入本次担保金额！");
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
			var canAmt = jsonstr.canAmt; 
			if(flag == "success"){
				var value = GrtGuarCont.guar_amt._getValue();
				if((parseFloat(guar_amt)+parseFloat(canAmt))>parseFloat(value)){
					alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+(parseFloat(value)-parseFloat(canAmt)));     
	                GrtLoan.guar_amt._setValue(""); 
				}else{
					doNext();
				}   
			}else if(flag == "error"){
				var value = GrtGuarCont.guar_amt._getValue();
				if(value!=null && value !=""){ 
					if((parseFloat(guar_amt)+parseFloat(canAmt))>parseFloat(value)){
		                  alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+canAmt);     
		                  GrtLoan.guar_amt._setValue(""); 
					}
				}else{
                    GrtLoan.guar_amt._setValue(""); 
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
	var url = '<emp:url action="checkGrtLoanCur.do"/>&serno=${context.serno}&guar_cont_no=${context.GrtGuarCont.guar_cont_no}&guar_amt='+guar_amt+'&guar_cont_amt='+guar_cont_amt;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null); 
}; 
//修改事件
function doNext(){	
	if(GrtGuarCont.lmt_grt_flag._getValue()=="1"){
		if(!(GrtGuarCont._checkAll())){
			return false; 
		}
	}else{
		if(!(GrtGuarCont._checkAll()&GrtLoan._checkAll())){
			return false; 
	   }
   }
	
	if('<%=rel%>' == "ywRel"){ 	
		if(!GrtLoan._checkAll()){
			return false;
	   }
	}
    var handleSuccess = function(o){   
    	if(o.responseText !== undefined) {
            try {
				var jsonstr = eval("("+o.responseText+")");
            } catch(e) {
				alert("修改失败！"); 
              return;  
            }
			var flag = jsonstr.flag;
			if(flag=="success"){
				alert("修改成功！");
				var type = GrtGuarCont.guar_cont_type._getValue();
				var cus_id = GrtGuarCont.cus_id._getValue();
				if(type=="01"){
					action="zg";
				}else{
					action="yb";
			    }
				<%if(flag.equals("ybWh")){%>
				//window.close();
				//reLoadx();
				window.location.reload();
			//	window.opener.location.reload();  
				<%}else if(menu.equals("dbbg")){%>
				var url = '<emp:url action="QueryGuarChangeSignList.do"/>';
				url = EMPTools.encodeURI(url);
				window.location = url;
				<%}else{%>
				window.location.reload();
				<%}%>   
				
            }else {    
				alert("修改失败！");
		    }
         }
	}
	var handleFailure = function(o){
	}
	var callback = {
        success:handleSuccess,
        failure:handleFailure
    }
    var form = document.getElementById("submitForm");
	form.action = '<emp:url action="updateGrtGuarContRecord.do"/>'; 
	GrtGuarCont._toForm(form);   
	GrtLoan._toForm(form);    
//	RLmtGuar._toForm(form);   
    var postData = YAHOO.util.Connect.setForm(form);
    var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
}    
//风险拦截
function interRisk(){
	var serno = GrtGuarCont.guar_cont_no._getValue();
	var _applType="";
	var _modelId="GrtGuarCont";
	var _pkVal=serno;
	var _preventIdLst="FFFA27620062BBA731ADAE238FEF8130";
	var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
    if(!_retObj || _retObj == '2' || _retObj == '5'){
		if( _retObj == '5'){
			alert("执行风险拦截有错误，请检查！");
		}
		return false;
	}else{
		return true;
	}
}
//签订事件
function doSign(){	
	if(GrtGuarCont.lmt_grt_flag._getValue()=="1"){
		if(!(GrtGuarCont._checkAll())){
			return false; 
		}
	}else{
		if(!(GrtGuarCont._checkAll()&GrtLoan._checkAll())){
			return false; 
	   }
   }
	
	if('<%=rel%>' == "ywRel"){ 	
		if(!GrtLoan._checkAll()){
			return false;
	   }
	}
    var handleSuccess = function(o){   
    	if(o.responseText !== undefined) {
            try {
				var jsonstr = eval("("+o.responseText+")");
            } catch(e) {
				alert("保存失败！"); 
              return;  
            }
			var flag = jsonstr.flag;
			if(flag=="success"){
				doSign2();
				
            }else {    
				alert("保存失败！");
		    }
         }
	}
	var handleFailure = function(o){
	}
	var callback = {
        success:handleSuccess,
        failure:handleFailure
    }
    var form = document.getElementById("submitForm");
	form.action = '<emp:url action="updateGrtGuarContRecord.do"/>'; 
	GrtGuarCont._toForm(form);   
	GrtLoan._toForm(form);    
//	RLmtGuar._toForm(form);   
    var postData = YAHOO.util.Connect.setForm(form);
    var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
}    
	
function doSign2() {
	if(GrtGuarCont.lmt_grt_flag._getValue()=="1"){
		if(!(GrtGuarCont._checkAll())){
			return false; 
		}
	}else{
		if(!(GrtGuarCont._checkAll()&GrtLoan._checkAll())){
			return false; 
	   }
   }
	var openday = '${context.OPENDAY}';
	var guar_end_date = GrtGuarCont.guar_end_date._getValue();
	if(openday>guar_end_date){
		alert("担保终止日需大于或者等于当前日期！");
		return false;
	}
	if(!interRisk()){
		return false;
	}
	if(confirm("请核对中文合同编号、担保金额、担保起始日及担保终止日等信息是否与书面合同一致！")){
		if(!confirm("确定要签订该笔合同？")){
			return;
		}
	 var handleSuccess = function(o){
	    	if(o.responseText !== undefined) {
	            try {
					var jsonstr = eval("("+o.responseText+")");
	            } catch(e) {
					alert("签订失败！");
	              return;
	            }
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("签订成功！");
					var type = GrtGuarCont.guar_cont_type._getValue();
					var cus_id = GrtGuarCont.cus_id._getValue();
					if(type=="01"){
						action="zg";
					}else{
						action="yb";
					}
					<%if(flag.equals("ybWh")){%>
					var url = '<emp:url action="queryYbGrtGuarContList.do"/>?cus_id='+cus_id;
					<%}else if(menu.equals("dbbg")){%>
					var url = '<emp:url action="QueryGuarChangeSignList.do"/>';
					<%}else{%>
					var url = '<emp:url action="queryGrtGuarContList.do"/>?action='+action;
					<%}%>
					url = EMPTools.encodeURI(url);
					window.location = url;
	            }else {
					alert("签订失败！");
			    }
	         }
		}
		var handleFailure = function(o){
		}
		var callback = {
	        success:handleSuccess,
	        failure:handleFailure
	    }
	    
	    var form = document.getElementById("submitForm");
		form.action = '<emp:url action="signGrtGuarContRecord.do"/>';
		GrtGuarCont._toForm(form);
//		RLmtGuar._toForm(form);     
	    var postData = YAHOO.util.Connect.setForm(form);
	    var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
}
    
function doLoad(){
	document.getElementById("main_tab").href="javascript:reLoadx()";
	if (GrtGuarCont.lmt_grt_flag._obj.element.value == "1") {
		//GrtGuarCont.limit_code._obj._renderRequired(true);
		//GrtGuarCont.limit_code._obj._renderReadonly(true);
		//GrtGuarCont.limit_code._obj._renderHidden(false);
	/*	document.getElementById('RLmtGuarCont').style.display="";   
		RLmtGuar.guar_amt._obj._renderRequired(false);
		RLmtGuar.is_per_gur._obj._renderRequired(true); 
		RLmtGuar.is_add_guar._obj._renderRequired(true); */
	}else{  
		//GrtGuarCont.limit_code._obj._renderRequired(false);
		//GrtGuarCont.limit_code._obj._renderReadonly(false);
		//GrtGuarCont.limit_code._obj._renderHidden(true);
	/*	document.getElementById('RLmtGuarCont').style.display="none";   
		RLmtGuar.guar_amt._obj._renderRequired(false);
		RLmtGuar.is_per_gur._obj._renderRequired(false); 
		RLmtGuar.is_add_guar._obj._renderRequired(false); */
	}   
	if(GrtGuarCont.guar_model._getValue()=="05"){//担保模式为联保时
		//GrtGuarCont.limit_code1._setValue(GrtGuarCont.limit_code._getValue());
	  //  GrtGuarCont.limit_code1._obj._renderHidden(false);
	   // GrtGuarCont.limit_code._obj._renderHidden(true);
	
	}
   /**ws添加--------start----*/   
	if('<%=rel%>' != "ywRel"){
		/**隐藏业务担保合同相关信息 */ 
		document.getElementById('GrtLoanRGur').style.display="none";   
		GrtLoan.guar_amt._obj._renderRequired(false);
		GrtLoan.is_per_gur._obj._renderRequired(false); 
		GrtLoan.is_add_guar._obj._renderRequired(false);     
	}else {       
		GrtLoan.rel._setValue('<%=rel%>');    
		GrtLoan.isCreditChange._setValue('<%=isCreditChange%>');    
	} 
	/**如果是业务则隐藏授信担保合同相关信息 */
	if('<%=rel%>' == "ywRel"){ 	
	/*	document.getElementById('RLmtGuarCont').style.display="none";   
		RLmtGuar.guar_amt._obj._renderRequired(false);
		RLmtGuar.is_per_gur._obj._renderRequired(false); 
		RLmtGuar.is_add_guar._obj._renderRequired(false);  */
	}
	
	if('<%=rel%>' == "sxRel"){         
	//	RLmtGuar.rel._setValue('<%=rel%>');
	} 
	if('<%=canUpdate%>' == "canNot"){
		/**如果是引用进的担保合同，则不允许修改担保合同信息 */   
 		GrtGuarCont.guar_cont_cn_no._obj._renderReadonly(true);
		GrtGuarCont.agr_no._obj._renderReadonly(true);
		GrtGuarCont.cur_type._obj._renderReadonly(true);
		GrtGuarCont.guar_amt._obj._renderReadonly(true);
		GrtGuarCont.guar_start_date._obj._renderReadonly(true);
		GrtGuarCont.guar_end_date._obj._renderReadonly(true);
		GrtGuarCont.memo._obj._renderReadonly(true);
		GrtGuarCont.manager_id_displayname._obj._renderReadonly(true);
		GrtGuarCont.manager_br_id_displayname._obj._renderReadonly(true);

		GrtLoan.is_per_gur._obj._renderReadonly(true);
		GrtLoan.is_add_guar._obj._renderReadonly(true);
    }
	if('<%=oper%>' == "sign"){ 
		GrtGuarCont.guar_cont_cn_no._obj._renderRequired(true);
		GrtGuarCont.guar_start_date._obj._renderRequired(true);
		GrtGuarCont.guar_end_date._obj._renderRequired(true);
	}     
	<%if("sign".equals(oper)){%>
/*	document.getElementById('RLmtGuarCont').style.display="none";   
	RLmtGuar.guar_amt._obj._renderRequired(false);
	RLmtGuar.is_per_gur._obj._renderRequired(false); 
	RLmtGuar.is_add_guar._obj._renderRequired(false); */

	document.getElementById('GrtLoanRGur').style.display="none";   
	GrtLoan.guar_amt._obj._renderRequired(false);
	GrtLoan.is_per_gur._obj._renderRequired(false); 
	GrtLoan.is_add_guar._obj._renderRequired(false);  
//document.getElementById("main_tab").href="javascript:reLoadx();";
<%}%>         
	/*--------end-------*/
	var guar_cont_state = GrtGuarCont.guar_cont_state._getValue();
	if(guar_cont_state == "01"){
		var guar_amt = GrtGuarCont.guar_amt._getValue();
		var used_amt = GrtGuarCont.used_amt._getValue();
		if(parseFloat(guar_amt)>=parseFloat(used_amt)){
			var can_amt = parseFloat(guar_amt)-parseFloat(used_amt);
			can_amt = Math.round(can_amt*100)/100;
			GrtGuarCont.can_amt._setValue(''+can_amt+'');
		}else{
			GrtGuarCont.can_amt._setValue(''+0.00+'');
		}
	}else{
		GrtGuarCont.used_amt._obj._renderHidden(true);
		GrtGuarCont.can_amt._obj._renderHidden(true);
	}
	/**add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 begin **/
    var guarWay = GrtGuarCont.guar_way._getValue();
    if(guarWay!="00"){
    	GrtGuarCont.actual_mort_rate._obj._renderHidden(true);
    	GrtGuarCont.actual_mort_rate._obj._renderRequired(false);
    }
    /**add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 end **/
	//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
	var modify_rel_serno = '<%=modify_rel_serno%>';
    GrtLoan.modify_rel_serno._setValue(modify_rel_serno);
    //add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
}
//担保金额不能大于担保品的可担保金额
function checkAmt(){
	//在池金额
	var flag = GrtGuarCont.bill_amt._getValue();
	//担保金额
	var value = GrtGuarCont.guar_amt._getValue();
	//担保方式（用来判断校验保证人担保金额）
	var guarWay = GrtGuarCont.guar_way._getValue();
	//担保模式（用来判断校验票据池）
	var guarModel = GrtGuarCont.guar_model._getValue();
	if(guarModel=="01"){
		if(parseFloat(flag)<parseFloat(value)){
			alert("在池总金额不足！");
			GrtGuarCont.guar_amt._setValue("");
		}
	}
	if(guarModel=="02"){
		if(parseFloat(flag)<parseFloat(value)){
			alert("应收账款池在池总金额不足！");
			GrtGuarCont.guar_amt._setValue("");
		}
	}
	if(guarModel=="03"){
		if(parseFloat(flag)<parseFloat(value)){
			alert("保理池在池总金额不足！");
			GrtGuarCont.guar_amt._setValue("");
		}
	}
	if((guarWay=="02"||guarWay=="03")&&(guarModel=="00")){
			if(parseFloat(flag)<parseFloat(value)){
				alert("保证人保证金额不足！");
				GrtGuarCont.guar_amt._setValue("");
			}
	}
	if((guarWay=="00"||guarWay=="01")&&(guarModel=="00")){
			if(parseFloat(flag)<parseFloat(value)){
				alert("担保品担保金额不足！");
				GrtGuarCont.guar_amt._setValue("");
			}
	}
	if((guarWay=="04")&&(guarModel=="05")){
		if(parseFloat(flag)<parseFloat(value)){
			alert("联保授信总金额不足！");
			GrtGuarCont.guar_amt._setValue("");
		}
	}
}

//点击tab页
function reLoadx(){  
	var url = "";
	var agr_no=GrtGuarCont.agr_no._getValue();
	<%if(flag.equals("")||flag==null){%>
		if(agr_no==""){
			url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=<%=oper%>&guar_cont_no=${context.GrtGuarCont.guar_cont_no}&oper=${context.oper}&rel=${context.rel}&serno=${context.serno}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}&limit_code=${context.limit_code}&menu=${context.menu}';
		}else{//担保模式为联保时，此时需要传menuId=zge动态所挂的tab需要强制替换其 menuId
			url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=<%=oper%>&menuId=zge&guar_cont_no=${context.GrtGuarCont.guar_cont_no}&oper=${context.oper}&rel=${context.rel}&serno=${context.serno}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}&limit_code=${context.limit_code}';
		}
    <%}else{%> 
	url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?flag=ybWh&op=<%=oper%>&menuId=ybCount&guar_cont_no=${context.GrtGuarCont.guar_cont_no}&oper=${context.oper}&rel=${context.rel}&serno=${context.serno}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}&limit_code=${context.limit_code}&pk_id=${context.pk_id}';
    <%}%>
	url = EMPTools.encodeURI(url);
	window.location = url;      
};
//检查本次担保合同可输入金额是否超过总金额 
function checkCur(){
    var guar_amt = GrtLoan.guar_amt._getValue(); 
    var guar_cont_amt = GrtGuarCont.guar_amt._getValue(); 
    if(guar_amt == null || guar_amt == ""){
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
			var canAmt = jsonstr.canAmt; 
			if(flag == "success"){
				var value = GrtGuarCont.guar_amt._getValue();
				if((parseFloat(guar_amt)+parseFloat(canAmt))>parseFloat(value)){
					alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+(parseFloat(value)-parseFloat(canAmt)));     
	                GrtLoan.guar_amt._setValue(""); 
				}     
			}else if(flag == "error"){
				var value = GrtGuarCont.guar_amt._getValue();
				if(value!=null && value !=""){ 
	                  alert("输入担保金额已超出担保合同剩余金额,可输入最高金额为:"+canAmt);     
	                  GrtLoan.guar_amt._setValue(""); 
				}else{
                    alert("请输入担保合同金额!");
                    GrtLoan.guar_amt._setValue("");  
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
	var url = '<emp:url action="checkGrtLoanCur.do"/>&serno=${context.serno}&guar_cont_no=${context.GrtGuarCont.guar_cont_no}&guar_amt='+guar_amt+'&guar_cont_amt='+guar_cont_amt;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null); 
}; 

   function setAmt(){
/*	   if(GrtGuarCont.lmt_grt_flag._obj.element.value == "1"){
		   var amt = GrtGuarCont.guar_amt._getValue();
			  RLmtGuar.guar_amt._setValue(amt);  
		  }*/
	   checkCur();
   }

 //两个日期作比较（2013-10-28 byXiaod）
	function checkStartDate(){
		if(GrtGuarCont.guar_start_date._obj.element.value!=''){
			var e = GrtGuarCont.guar_end_date._obj.element.value;
			var s = GrtGuarCont.guar_start_date._obj.element.value;
			if(e!=''){
				if(s>e){
	           		alert('担保起始日必须小于或等于担保终止日！');
	           		GrtGuarCont.guar_start_date._obj.element.value="";
	           		return;
           		}
			}
		}
	}
	function checkEndDate(){
		if(GrtGuarCont.guar_end_date._obj.element.value!=''){
			var e = GrtGuarCont.guar_end_date._obj.element.value;
			var s = GrtGuarCont.guar_start_date._obj.element.value;
			if(e){
				var todayDate='${context.OPENDAY}';
				var flag = CheckDate1BeforeDate2(e,todayDate);
	            if(e==todayDate){
	                return true;
	            }
	            if(flag){
	           	 alert("担保终止日不能小于当前日期！");
	           	 GrtGuarCont.guar_end_date._setValue("");
					 return false;
	            }
			}
			if(s!=''){
				 if(s>e){
           		   alert('担保终止日必须大于或等于担保起始日！');
           		   GrtGuarCont.guar_end_date._obj.element.value="";
           		   return;
           	     }
			}
		}
	}
	

</script>   
</head>
<body class="page_content" onload="doLoad()" >
<emp:tabGroup mainTab="main_tab" id="main_tabs">
<emp:tab label="基本信息" id="main_tab" needFlush="true" initial="true">	
	<emp:form id="submitForm" method="POST">
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="基本信息">
			<emp:pop id="GrtGuarCont.cus_id" label="借款人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="false" readonly="true" />
			<emp:text id="GrtGuarCont.cus_id_displayname" label="借款人客户名称" required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2" />
		    <emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" maxlength="60" readonly="false"/>
			<emp:select id="GrtGuarCont.guar_cont_type" label="担保合同类型" required="false" dictname="STD_GUAR_CONT_TYPE" readonly="true" colSpan="2"/>
			<emp:select id="GrtGuarCont.guar_model" label="担保模式" required="false" dictname="STD_GUAR_MODEL" hidden="false" readonly="true"/>
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" required="false" dictname="STD_GUAR_TYPE" readonly="true"/>
			<!-- **add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 begin -->
			<emp:text id="GrtGuarCont.actual_mort_rate" label="实际抵押率" dataType="Rate" maxlength="16" required="true" colSpan="2"/>
			<!-- **add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 end -->
			<emp:select id="GrtGuarCont.lmt_grt_flag" label="是否授信项下" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:text id="GrtGuarCont.limit_code" label="授信额度编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.limit_code1" label="联保协议编号" maxlength="40" required="false" readonly="true" hidden="true" colSpan="2"/>
			<emp:select id="GrtGuarCont.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="GrtGuarCont.guar_amt" label="担保金额" maxlength="18" dataType="Currency" onchange="setAmt();" readonly="false" required="true"/>
			<emp:text id="GrtGuarCont.used_amt" label="已用担保金额" maxlength="18" dataType="Currency" readonly="true"/>
			<emp:text id="GrtGuarCont.can_amt" label="可用担保金额" maxlength="18" dataType="Currency" readonly="true"/>
			<emp:date id="GrtGuarCont.guar_start_date" label="担保起始日" onblur="checkStartDate()" readonly="false"/>
			<emp:date id="GrtGuarCont.guar_end_date" label="担保终止日" onblur="checkEndDate()" readonly="false"/>
			<emp:date id="GrtGuarCont.sign_date" label="签订日期" required="false" readonly="true"/>
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" required="false" dictname="STD_CONT_STATUS" readonly="true" defvalue="00" />
			<emp:textarea id="GrtGuarCont.memo" label="备注信息" maxlength="200" required="false" />
		
			<emp:text id="GrtGuarCont.rel" label="申请类型（1--担保合同管理进入，2--授信进入，3--业务进入）" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.guar_term" label="担保期限" maxlength="0" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.guar_term_type" label="担保期限类型" maxlength="2" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.drfpo_no" label="票据池编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.po_no" label="应收账款池编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.agr_no" label="联保协议编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.poType" label="标志位（1--应收账款，2--保理池）" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.bill_amt" label="担保合同项下担保品的价值总额（辅助字段）" maxlength="40" required="false" hidden="true" dataType="Currency" defvalue="0"/>
		</emp:gridLayout>
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="登记信息">
			<emp:pop id="GrtGuarCont.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="GrtGuarCont.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="false"/>
			<emp:text id="GrtGuarCont.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false" /> 
			<emp:text id="GrtGuarCont.input_br_id_displayname" label="登记机构" required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarCont.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:text id="GrtGuarCont.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="GrtGuarCont.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="GrtGuarCont.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>                            
		<div id="GrtLoanRGur">
		 <emp:gridLayout id="GrtLoanRGurGroup" title="业务担保合同关联信息" maxColumn="2"> 
			<emp:text id="GrtLoan.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>   
			<emp:text id="GrtLoan.cont_no" label="合同编号" maxlength="40" defvalue="${context.cont_no}" required="false" hidden="true"/>    
			<emp:text id="GrtLoan.guar_cont_no" label="担保合同编号" defvalue="${context.GrtGuarCont.guar_cont_no}" readonly="true" hidden="true"/>       
			<emp:text id="GrtLoan.guar_amt" label="本次担保金额" maxlength="18" required="true" onblur="checkCur()" dataType="Currency" /> 
			<emp:select id="GrtLoan.is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO" required="true"/> 
			<emp:select id="GrtLoan.is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:text id="GrtLoan.rel" label="业务标识" required="false" hidden="true"/>    
			<emp:text id="GrtLoan.isCreditChange" label="是否担保变更" required="false" hidden="true"/>    
			<emp:text id="GrtLoan.modify_rel_serno" label="出账退回变更流水" required="false" hidden="true"/> <!-- add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求-->
		 </emp:gridLayout>
		</div> 
		<div align="center">
			<br>
			<% if (oper.equals("update")&&("ywRel".equals(rel))){ //业务中的保存%>
			<emp:button id="checkCurSave" label="保存" op=""/>  
			<%}else if (oper.equals("update")&&(!"ywRel".equals(rel))){//非业务保存%>
			<emp:button id="next" label="保存" op=""/>  
			<%} %>
			<%if (oper.equals("sign")) {%>
			<emp:button id="sign" label="签订" op=""/>
			<%} %>
			<%if(flag.equals("ybWh")){%>
			<emp:button id="return" label="关闭"/>
			<%}else{%>
				<emp:button id="return" label="返回"/>  
			<% }%>
			
		</div>
	</emp:form>
	</emp:tab>
   <emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
</body>
</html>
</emp:page>
