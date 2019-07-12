<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<%
	String flag=request.getParameter("flag");
	String oper=request.getParameter("oper");
	String infoTree=request.getParameter("info");
	String btnflag = request.getParameter("btnflag");//控制按钮 (rep:补录完成  due:正式客户 temp:临时、草稿)
//	String temCus = request.getParameter("temCus");
//	boolean isRead = false;
	if ((flag != null && flag.equals("query"))) {
//		isRead = true;
		request.setAttribute("canwrite", "");
	}
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	context.addDataField("infotree",oper);
//	boolean repBtnFlag  = "yes".equals((String)request.getParameter("repBtn"));
%>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 350px;
}

.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}

.emp_field_label {
	vertical-align: top;
	padding-top: 4px;
	text-align: right;
	width:14s0px;
}
</style>
<link href="<emp:file fileName='styles/start/jquery-ui-1.7.1.custom.css'/>" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-ui-1.7.1.custom.min.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery.cmisDialogs.js'/>"></script>
<script type="text/javascript">
	/*--user code begin--*/
	/* modify by yangzy 2015-05-21  中征码修改 begin*/
	var tmpLoanCardId;
	/* modify by yangzy 2015-05-21  中征码修改 end*/
	//返回列表页面
	function doReturn(){
		var gobackURL = '<emp:url action="queryCusIndivList.do"/>';
		gobackURL = EMPTools.encodeURI(gobackURL);
		window.location = gobackURL;
	};
	
	//页面初始化
	function doOnLoad(){
		/* modify by yangzy 2015-05-21  中征码修改 begin*/
		tmpLoanCardId = CusBase.loan_card_id._getValue();
		/* modify by yangzy 2015-05-21  中征码修改 end*/
		showCertTyp(CusBase.cert_type, 'indiv');
		//showCertTyp(CusBase.cus_type, 'indiv');
		//证件类型是否为身份证，如是则根据身份证直接获取性别并锁定
		var certType = CusBase.cert_type._getValue();
        var certCode = CusBase.cert_code._getValue();
        //判断18位身份证
        if((certType=='100') && certCode.length==18){
         //   var sexNum=certCode.charAt(16);
            //判断性别
         //   if(sexNum%2==1){
         //   	CusIndiv.indiv_sex._setValue("01");
         //   }else{
         //   	CusIndiv.indiv_sex._setValue("02");
         //   }
         //   CusIndiv.indiv_sex._obj._renderReadonly(true);
            //判断生日
            var birthdayYear = certCode.substring(6,10);
            var birthdayMonth = certCode.substring(10,12);
            var birthdayDay = certCode.substring(12,14);
            var birthdayDate=birthdayYear+"-"+birthdayMonth+"-"+birthdayDay;
            CusIndiv.indiv_dt_of_birth._setValue(birthdayDate);
            CusIndiv.indiv_dt_of_birth._obj._renderReadonly(true);
        }
		// 校验是否为我行关联客户  
		checkIsRelaCust();
      	//校验贷款卡事件
		EMPTools.addEvent(CusBase.loan_card_id._obj.element, "change", cheakCardId);
		EMPTools.addEvent(CusBase.loan_card_flg._obj.element, "change", changeCardFlg);//added by tzb  2009-12-16
		changeCardFlg();

		//若客户类型为正式客户则主管客户经理、主管机构字段不能修改
		var cus_status = CusBase.cus_status._getValue();
		if(cus_status=='20'){
			CusBase.main_br_id_displayname._obj._renderReadonly(true);
			CusBase.cust_mgr_displayname._obj._renderReadonly(true);
		}
		checkIsLong();//是否为长期证件

		//----20150123 Edited by FCL 港澳台邮政编码为非必输 
		var paddr  =CusIndiv.post_addr._obj.element.value;
		var ira = CusIndiv.indiv_rsd_addr._obj.element.value;
		
		if(paddr=='710000'||paddr=='810000'||paddr=='820000'){
			CusIndiv.post_code._obj._renderRequired(false);
		}
		if(ira=='710000'||ira=='810000'||ira=='820000'){
			CusIndiv.indiv_zip_code._obj._renderRequired(false);
		}
		//--------------2015-01-23 END----------------------
		doHidden();
	}
	//add by lilx 2013-7-3 12:04	
	function checkIsRelaCust(){
		if (CusIndiv.is_rela_cust._obj.element.value == "1") {//是
			//与银行的关联关系
			CusIndiv.cus_bank_rel._obj._renderRequired(true);
			//在我行职务
			CusIndiv.bank_duty._obj._renderRequired(true);
			CusIndiv.bank_duty._obj._renderReadonly(false);
			CusIndiv.bank_duty._obj._renderHidden(false);
			//股权证号码
			CusIndiv.stockhold_code._obj._renderRequired(false);
			CusIndiv.stockhold_code._obj._renderReadonly(false);
			CusIndiv.stockhold_code._obj._renderHidden(false);
			//拥有我行股份金额(元)
			CusIndiv.com_hold_stk_amt._obj._renderRequired(false);
			CusIndiv.com_hold_stk_amt._obj._renderReadonly(false);
			CusIndiv.com_hold_stk_amt._obj._renderHidden(false);
			var options = CusIndiv.cus_bank_rel._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
				if(options[i].value == 'B1'){
					options.remove(i);
				}
			}
			CusIndiv.cus_bank_rel._obj._renderHidden(false);
			CusIndiv.cus_bank_rel_fake._obj._renderHidden(true);
		} else if (CusIndiv.is_rela_cust._obj.element.value == "2") {//否
			//银行的关联关系
			CusIndiv.cus_bank_rel._obj.element.value = "";
			CusIndiv.cus_bank_rel._obj._renderRequired(false);
			//在我行职务
			CusIndiv.bank_duty._obj.element.value = "";
			CusIndiv.bank_duty._obj._renderRequired(false);
			CusIndiv.bank_duty._obj._renderHidden(true);
			//股权证号码
			CusIndiv.stockhold_code._setValue("");
			CusIndiv.stockhold_code._obj._renderRequired(false);
			CusIndiv.stockhold_code._obj._renderHidden(true);
			//拥有我行股份金额(元)
			CusIndiv.com_hold_stk_amt._setValue("");
			CusIndiv.com_hold_stk_amt._obj._renderRequired(false);
			CusIndiv.com_hold_stk_amt._obj._renderHidden(true);
			var options = CusIndiv.cus_bank_rel._obj.element.options;
	    	var option1 = new Option('普通客户关系','B1');
	    	options.add(option1);
	    	CusIndiv.cus_bank_rel._setValue("B1");

	    	CusIndiv.cus_bank_rel._obj._renderHidden(true);
	    	CusIndiv.cus_bank_rel_fake._obj._renderHidden(false);
		}
	}

	//选择是否贷款卡时触发的事件
	function changeCardFlg() {
		if (CusBase.loan_card_flg._obj.element.value == "1") {
			CusBase.loan_card_id._obj._renderRequired(true);
			CusBase.loan_card_id._obj._renderReadonly(false);
			CusBase.loan_card_id._obj._renderHidden(false);
			/* modify by wangj 2015-05-20  中征码修改 begin*//*
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderReadonly(false);
			CusBase.loan_card_pwd._obj._renderHidden(false);
			
			CusBase.loan_card_eff_flg._obj._renderRequired(false);
			CusBase.loan_card_eff_flg._obj._renderReadonly(false);
			CusBase.loan_card_eff_flg._obj._renderHidden(false);
			
			//CusBase.loan_card_audit_dt._obj._renderRequired(true);
			CusBase.loan_card_audit_dt._obj._renderReadonly(false);
			CusBase.loan_card_audit_dt._obj._renderHidden(false);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		} else if (CusBase.loan_card_flg._obj.element.value == "2") {
			CusBase.loan_card_id._obj.element.value = "";
			CusBase.loan_card_id._obj._renderRequired(false);
			CusBase.loan_card_id._obj._renderHidden(true);
			/* modify by wangj 2015-05-20  中征码修改 begin*//*
			CusBase.loan_card_pwd._obj.element.value = "";
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderHidden(true);
			
			CusBase.loan_card_eff_flg._setValue("");
			CusBase.loan_card_eff_flg._obj._renderRequired(false);
			CusBase.loan_card_eff_flg._obj._renderHidden(true);
			
			CusBase.loan_card_audit_dt._setValue("");
			CusBase.loan_card_audit_dt._obj._renderRequired(false);
			CusBase.loan_card_audit_dt._obj._renderHidden(true);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		}
		/* modify by yangzy 2015-05-21  中征码修改 begin*/
		var loanCardId= CusBase.loan_card_id._getValue();
		if(loanCardId!=tmpLoanCardId){
			alert("请提供客户征信异议处理申请书");
		}
		/* modify by yangzy 2015-05-21  中征码修改 end*/
	};
	//检查贷款卡

	function cheakCardId(){
		var loanCardId = CusBase.loan_card_id._obj.element.value;
		if(loanCardId!=null && loanCardId!=""){
			var CheckDKK_flag=CheckDKK(loanCardId);
			if(CheckDKK_flag!=true){
				/* modify by wangj 2015-05-20  中征码修改 begin*/
			   alert("中征码不正确");
			   /* modify by wangj 2015-05-21  中征码修改 end*/ 
			   CusBase.loan_card_id._obj.element.value="";
			   CusBase.loan_card_id._obj._renderStatus();
			   /* modify by wangj 2015-05-21  中征码修改 begin*/ 
			}else{
				CheckLoanCardId();
			}
			/* modify by wangj 2015-05-21  中征码修改 end*/
		}
		
	}
	
	//贷款帐号唯一性检查
	function CheckLoanCardId(){
		var loan_card_id = CusBase.loan_card_id._obj.element.value;
		if(loan_card_id != ''){
			var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.loanCardIdFlag;
				if(flag == 'y'){
					/* modify by wangj 2015-05-21  中征码修改 begin*/
	 				alert("该中征码在系统中已经存在！");
	 				/* modify by wangj 2015-05-21  中征码修改 end*/
	 				CusBase.loan_card_id._obj.element.value = '';
	 				return;
	 				/* modify by wangj 2015-05-21  中征码修改 begin*/
				}else{
					if(loan_card_id!=tmpLoanCardId){
						alert("请提供客户征信异议处理申请书");
				  	}
				}
				/* modify by wangj 2015-05-21  中征码修改 end*/
			}
			}
			var handleFailure = function(o){
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var cusNo = CusBase.cus_id._obj.element.value;
			var paramStr="loanCardNo="+loan_card_id + "&cusNo=" + cusNo;
			var url = '<emp:url action="getCusLoanCardOnly.do"/>&' + paramStr;
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}
	}
	//校验学历和学位之间的关系
	function getValueToDgr(){
		if(CusIndiv.indiv_edt._obj.element.value != '10' && CusIndiv.indiv_edt._obj.element.value != '20'){
			CusIndiv.indiv_dgr._obj.element.value = '0';
		}
	}
	//保存
	function doUpdateCusIndiv(){
	    var form = document.getElementById("submitForm");
	    var result = CusIndiv._checkAll();
	    var result1 = CusBase._checkAll();
	    if(result&&result1){
	        CusIndiv._toForm(form);
	        CusBase._toForm(form);
	        toSubmitForm(form);
	    }else{
		    alert("保存失败！\n请检查各标签页面中的必填信息填写是否正确！");
	    }
	}
	//补录
	function doUpdateCusIndiv3(){
	    var result = CusIndiv._checkAll();
	    var result1 = CusBase._checkAll();
	    if(result&&result1){
	    	var form = document.getElementById("submitForm");
	    	form.setAttribute("action","updateCusIndivRepRecord.do");
	        CusIndiv._toForm(form);
	        CusBase._toForm(form);
	        toSubmitForm(form);
	    }else{
		    alert("保存失败！\n请检查各标签页面中的必填信息填写是否正确！");
	    }
	}
	//保存(没有校验  add by lilx 2013-7-3 10:54
	function doUpdateCusIndiv2(){
		var result = false;
		var baseFlag = "";
		var cus_status = CusBase.cus_status._getValue();
		if(cus_status=="20")
		{
			alert("该客户为正式客户，请点击保存按钮进行保存！");
			return;
		}
		var form = document.getElementById("submitForm");
		var btnflag = '<%=btnflag%>';
		form.setAttribute("action","tempUpdateCusIndivRecord.do?btnflag="+btnflag);
		CusIndiv._toForm(form);
		CusBase._toForm(form);
	    form.submit();
	}
	//暂存
/*	function doTempUpdateCusIndiv(){
		var cus_status = CusBase.cus_status._getValue();
		if(cus_status=="20"){
			alert("该客户为正式客户，请点击保存按钮进行保存！");
			return;
		}
		var form = document.getElementById("submitForm");
		form.setAttribute("action","tempUpdateCusIndivRecord.do");
		CusIndiv._toForm(form);
		CusBase._toForm(form);
	    form.submit();
	}*/
	
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
				var message = jsonstr.message;
				if(flag=="success"){
				 	alert("保存成功！");
					var cus_status = CusBase.cus_status._getValue();
				 	if(cus_status!="20"){
					 	window.location.reload();
					}
				}else if(flag=="succ"){
					alert(message);
			        window.opener.location.reload();
			        window.close();
				}else if(flag=="fail"){
					//补录成功，但不存在提交信息
					alert(message);
					//window.opener.location.reload();
			        //window.close();
				}
			}
		}
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}

	//单位所属行业
	function onReturn(date){
		CusIndiv.indiv_com_fld._obj.element.value=date.One+date.id;
		CusIndiv.indiv_com_fld_displayname._obj.element.value=date.label;
	}
			
	function CheckDate(date){
		var str_date= date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(str_date!=null && str_date!="" ){
			var flag = CheckDate1BeforeDate2(str_date,openDay);
			if(!flag){
				alert("建立信贷关系时间应小于等于当前日期！");
				date._obj.element.value="";
			}
	    }
	}

	//检验贷款卡年检到期日
	function checkLoanCardAuditDt(){
		var loan_card_audit_dt = CusBase.loan_card_audit_dt._getValue();//贷款卡年检到期日
		var openDay='${context.OPENDAY}';
		if(loan_card_audit_dt!=null && loan_card_audit_dt!="" ){
			var flag = CheckDate1BeforeDate2(loan_card_audit_dt,openDay);
			if(flag){
				alert("贷款卡年检到期日要大于当前日期！");
				CusBase.loan_card_audit_dt._setValue("");
			}
		}
	}

	//检验证件到期日
	function checkIndivIdExpDt(){
		var indiv_id_exp_dt = CusIndiv.indiv_id_exp_dt._getValue();//证件到期日
		var indiv_id_start_dt = CusIndiv.indiv_id_start_dt._getValue();
		var openDay='${context.OPENDAY}';
		if(indiv_id_exp_dt!=null && indiv_id_exp_dt!="" ){
			var flag = CheckDate1BeforeDate2(indiv_id_exp_dt,openDay);
			if(flag){
				alert("证件到期日要大于当前日期！");
				CusIndiv.indiv_id_exp_dt._setValue("");
			}
		}
		if(indiv_id_exp_dt!=null && indiv_id_exp_dt!="" ){
			if(indiv_id_exp_dt<indiv_id_start_dt){
				alert("证件到期日要大于证件签发日");
				CusIndiv.indiv_id_exp_dt._setValue("");
			}
		}
	}

	//返回主管客户经理
	function setconId(data){
		CusBase.cust_mgr_displayname._setValue(data.actorname._getValue());
		CusBase.cust_mgr._setValue(data.actorno._getValue());
		CusBase.main_br_id_displayname._setValue(data.orgid_displayname._getValue());
		CusBase.main_br_id._setValue(data.orgid._getValue());
		//CusBase.main_br_id_displayname._obj._renderReadonly(true);
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
					CusBase.main_br_id._setValue(jsonstr.org);
					CusBase.main_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusBase.main_br_id._setValue("");
					CusBase.main_br_id_displayname._setValue("");
					CusBase.main_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBase.cust_mgr._getValue();
					CusBase.main_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusBase.main_br_id._setValue("");
					CusBase.main_br_id_displayname._setValue("");
					CusBase.main_br_id_displayname._obj._renderReadonly(false);
					CusBase.main_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusBase.cust_mgr._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		CusBase.main_br_id_displayname._setValue(data.organname._getValue());
		CusBase.main_br_id._setValue(data.organno._getValue());
	}

	//户籍地址
	function onReturnHouhReg(date){
		CusIndiv.indiv_houh_reg_add._obj.element.value=date.id;
		CusIndiv.indiv_houh_reg_add_displayname._obj.element.value=date.label;
	}

	//区域编号
	function onReturnRegStateCode(date){
		CusIndiv.area_code._obj.element.value=date.id;
		CusIndiv.area_code_displayname._obj.element.value=date.label;
	}

	//通讯地址
	function onReturnPostAddr(date){
		CusIndiv.post_addr._obj.element.value=date.id;
		CusIndiv.post_addr_displayname._obj.element.value=date.label;
		var addSort = "PostAddr";
		//-----20150123 Edited by FCL ----------
		var paddr=date.id;
		if(paddr=='710000'||paddr=='810000'||paddr=='820000'){
			CusIndiv.post_code._obj._renderRequired(false);
		}else{
			CusIndiv.post_code._obj._renderRequired(true);
		}
		//--------20150123 END--------------------
		searchPcodeByPostAddr(date.id,addSort);
	}

	//居住地址
	function onReturnRsdAddr(date){
		CusIndiv.indiv_rsd_addr._obj.element.value=date.id;
		CusIndiv.indiv_rsd_addr_displayname._obj.element.value=date.label;
		
		//区域编码、区域名称默认为居住地址，只读   2014-09-25 唐顺岩
		CusIndiv.area_code._obj.element.value=date.id;
		CusIndiv.area_code_displayname._obj.element.value=date.label;

		//----20150123 Edited by FCL ---------------
		var ira = date.id;
		if(ira=='710000'||ira=='810000'||ira=='820000'){
			CusIndiv.indiv_zip_code._obj._renderRequired(false);
		}else{
			CusIndiv.indiv_zip_code._obj._renderRequired(true);
		}
		//---------20150123 END----------------------
		var addSort = "RsdAddr";
		searchPcodeByPostAddr(date.id,addSort);
	}

	//返回单位地址
	function onReturnIndivAddr(date){
		CusIndiv.indiv_com_addr._obj.element.value=date.id;
		CusIndiv.indiv_com_addr_displayname._obj.element.value=date.label;
		var addSort = "IndivAddr";
		searchPcodeByPostAddr(date.id,addSort);
	}

	//籍贯
	function onReturnBrtPlace(date){
		CusIndiv.indiv_brt_place._obj.element.value=date.id;
		CusIndiv.indiv_brt_place_displayname._obj.element.value=date.label;
	//	var addSort = "PostAddr";
	//	searchPcodeByPostAddr(date.id,addSort);
	}

	//根据通讯地址获取相应的邮政编码
	function searchPcodeByPostAddr(date,addSort){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
				var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("获取失败！");
					return;
				}
				var flag = jsonstr.flag;
				var post_code = jsonstr.post_code;
				var addSort = jsonstr.addSort;
				if(flag=="success"){
					if(addSort=="PostAddr"){//通讯地址
						CusIndiv.post_code._setValue(post_code);
					}else if(addSort=="RsdAddr"){//居住地址
						CusIndiv.indiv_zip_code._setValue(post_code);
					}else{//返回单位地址
						CusIndiv.indiv_com_zip_code._setValue(post_code);
					}
				}
            }
        };
        var handleFailure = function(o){
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var post_addr = date;
        var url = '<emp:url action="getPostAddr2PcodeOne.do"/>?post_addr='+post_addr+"&addSort="+addSort;
  	  	url = EMPTools.encodeURI(url);
  	  	var postData = YAHOO.util.Connect.setForm();
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}
	//校验工作起始年
	function checkWorkJobY(){
		var year0 = CusIndiv.indiv_work_job_y._obj.element.value;
		if(year0!=null && year0!=""){
			if(CheckYearBeforeToday(year0)){
			}else {
				CusIndiv.indiv_work_job_y._obj.element.value="";
			}
		}
	}

	function checkIsLong(){
		var is_long_indiv = CusIndiv.is_long_indiv._getValue();
		if(is_long_indiv == '2'){
//			var indiv_id_exp_dt = CusIndiv.indiv_id_exp_dt._getValue();
			CusIndiv.indiv_id_exp_dt._obj.element.value='${context.CusIndiv.indiv_id_exp_dt}';
			CusIndiv.indiv_id_exp_dt._obj._renderReadonly(false);
//			CusIndiv.indiv_id_exp_dt._obj.element.value="2999-12-31";
//			CusIndiv.indiv_id_exp_dt._obj._renderHidden(false);
//			CusIndiv.indiv_id_exp_dt._obj._renderRequired(true);
		}else{
			var ori_date = '${context.CusIndiv.indiv_id_exp_dt}';
			CusIndiv.indiv_id_exp_dt._obj.element.value="2999-12-31";
			CusIndiv.indiv_id_exp_dt._obj._renderReadonly(true);
//			CusIndiv.indiv_id_exp_dt._obj.element.value="";
//			CusIndiv.indiv_id_exp_dt._obj._renderHidden(true);
//			CusIndiv.indiv_id_exp_dt._obj._renderRequired(false);
		}
	}

	/**当职业为：农、林、牧、渔、水利业，不便分类的其他从业人员，未知，除了年收入其他都是非必输项         2014-08-07     邓亚辉*/
	function doHidden(){
		var indiv_occ=CusIndiv.indiv_occ._getValue();
		//alert(indiv_occ);
		//alert(indiv_occ + "5" );
		if(indiv_occ == 'Y' || indiv_occ == 'Z' || indiv_occ == '5' ){
			CusIndiv.indiv_com_name._obj._renderRequired(false);
			CusIndiv.indiv_com_typ._obj._renderRequired(false);
			CusIndiv.indiv_com_fld._obj._renderRequired(false);
			CusIndiv.indiv_com_addr._obj._renderRequired(false);
			CusIndiv.indiv_com_fld_displayname._obj._renderRequired(false);
			CusIndiv.indiv_com_addr_displayname._obj._renderRequired(false);
			CusIndiv.street_unit._obj._renderRequired(false);
			CusIndiv.indiv_work_job_y._obj._renderRequired(false);
			CusIndiv.indiv_com_job_ttl._obj._renderRequired(false);
			CusIndiv.indiv_crtfctn._obj._renderRequired(false);
				
		}else{
			CusIndiv.indiv_com_name._obj._renderRequired(true);
			CusIndiv.indiv_com_typ._obj._renderRequired(true);
			CusIndiv.indiv_com_fld._obj._renderRequired(true);
			CusIndiv.indiv_com_addr._obj._renderRequired(true);
			CusIndiv.indiv_com_fld_displayname._obj._renderRequired(true);
			CusIndiv.indiv_com_addr_displayname._obj._renderRequired(true);
			CusIndiv.street_unit._obj._renderRequired(true);
			CusIndiv.indiv_work_job_y._obj._renderRequired(true);
			CusIndiv.indiv_com_job_ttl._obj._renderRequired(true);
			CusIndiv.indiv_crtfctn._obj._renderRequired(true);
		}
	}
	function checkIndivIdExpDt1(){
		var indiv_id_exp_dt = CusIndiv.indiv_id_exp_dt._getValue();//证件到期日
		var indiv_id_start_dt = CusIndiv.indiv_id_start_dt._getValue();
		if(indiv_id_exp_dt!=null && indiv_id_exp_dt!="" ){
			if(indiv_id_exp_dt<indiv_id_start_dt){
				alert("证件到期日要大于证件签发日");
				CusIndiv.indiv_id_exp_dt._setValue("");
			}
		}
	}
	function checkIndivIdExpDt2(){
		var indiv_id_exp_dt = CusIndiv.indiv_id_exp_dt._getValue();//证件到期日
		var indiv_id_start_dt = CusIndiv.indiv_id_start_dt._getValue();
		if(indiv_id_exp_dt!=null && indiv_id_exp_dt!="" ){
			if(indiv_id_exp_dt<indiv_id_start_dt){
				alert("证件签发日要小于证件到期日");
				CusIndiv.indiv_id_start_dt._setValue("");
			}
		}
	}
	/*--user code end--*/
	   	 
</script>
</head>
<form id="postForm" action="" method="post" target="_blank"  >
 <input type='hidden' name="EMP_SID" value='<%=request.getParameter("EMP_SID") %>' />
</form>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateCusIndivRecord.do" method="POST"  >
		<emp:tabGroup id="CusIndiv_tabs" mainTab="base_tab">
			<emp:tab id="base_tab" label="基本信息" initial="true" needFlush="true">
				<div>
					<emp:gridLayout id="CusIndivGroup" title="基本信息" maxColumn="2">
						<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
						<emp:select id="CusBase.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
						<emp:text id="CusBase.cus_name" label="姓名" maxlength="80" required="true" readonly="true"/>
						<emp:select id="CusIndiv.indiv_sex" label="性别" required="true" dictname="STD_ZX_SEX" readonly="false"/>
						<emp:select id="CusBase.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="10"  readonly="true"/>
						<emp:text id="CusBase.cert_code" label="证件号码" maxlength="20" required="true"  readonly="true"/>
						<emp:select id="CusIndiv.is_long_indiv" label="是否为长期证件" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsLong()" defvalue="1" colSpan="2"/>
						<emp:date id="CusIndiv.indiv_id_start_dt" label="证件签发日" required="true" onblur="checkIndivIdExpDt2()"/>	
						<emp:date id="CusIndiv.indiv_id_exp_dt" label="证件到期日" required="true" onblur="checkIndivIdExpDt()" readonly="false"/>								
						<emp:select id="CusIndiv.agri_flg" label="是否为农户" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
						<emp:select id="CusBase.cus_country" label="国籍" required="true" dictname="STD_GB_2659-2000" defvalue="CHN" readonly="true"/>
						<emp:select id="CusIndiv.indiv_ntn" label="民族" required="true" dictname="STD_ZB_NATION" defvalue="01"/>
						<emp:text id="CusIndiv.indiv_brt_place" label="籍贯" required="true" hidden="true"/>
						<emp:pop id="CusIndiv.indiv_brt_place_displayname" label="籍贯" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnBrtPlace" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
		                <emp:text id="CusIndiv.indiv_houh_reg_add" label="户籍地址" required="true" hidden="true"/>
						<emp:pop id="CusIndiv.indiv_houh_reg_add_displayname" label="户籍地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
							returnMethod="onReturnHouhReg" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
						<emp:text id="CusIndiv.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
						<emp:date id="CusIndiv.indiv_dt_of_birth" label="出生日期" required="true" readonly="false"/>
						<emp:select id="CusIndiv.indiv_pol_st" label="政治面貌"  required="false" dictname="STD_ZB_POLITICAL"/>
						<emp:select id="CusIndiv.indiv_edt" label="最高学历" required="true" dictname="STD_ZX_EDU" onblur="getValueToDgr();"/>
						<emp:select id="CusIndiv.indiv_dgr" label="最高学位" required="true" dictname="STD_ZX_DEGREE"/>
						<emp:textarea id="CusIndiv.indiv_hobby" label="爱好" maxlength="200" required="false" colSpan="2"  rows="3"/>
					</emp:gridLayout>
					
					<emp:gridLayout id="" title="与我行合作关系" maxColumn="2">
						<emp:date id="CusIndiv.com_init_loan_date" label="建立信贷关系时间" required="true" onblur="CheckDate(CusIndiv.com_init_loan_date)"/>	
						<%-- <emp:checkbox id="CusIndiv.indiv_hld_acnt" label="在我行开立账户情况" colSpan="2" dictname="STD_ZB_INV_HL_ACN" /> --%>
						<emp:select id="CusIndiv.hold_card" label="持卡情况" required="true" dictname="STD_ZB_HOLD_CARD_YZ" />
						<emp:select id="CusIndiv.passport_flg" label="是否拥有外国护照或居住权" required="true" dictname="STD_ZX_YES_NO"/>			
						<emp:select id="CusBase.cus_crd_grade" label="信用等级" required="true"  dictname="STD_ZB_CREDIT_GRADE" defvalue="00" readonly="true" />
						<emp:date id="CusBase.cus_crd_dt" label="信用评级到期日期" required="false" readonly="true"/>
						<emp:select id="CusBase.cus_status" label="客户状态" required="false" colSpan="2" dictname="STD_ZB_CUS_STATUS"  hidden="false" readonly="true"/>
						<emp:textarea id="CusIndiv.remark" label="备注" maxlength="250" required="false" colSpan="2" hidden="true" />
					</emp:gridLayout>
					<emp:gridLayout id="" title="与我行关系" maxColumn="2">
						<emp:select id="CusIndiv.is_rela_cust" label="是否为我行关联客户" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsRelaCust()"/>
						<emp:select id="CusIndiv.cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK" cssElementClass="emp_field_text_input2" colSpan="2"/>
						<emp:text id="CusIndiv.cus_bank_rel_fake" label="与我行关联关系" defvalue="普通客户关系" hidden="true" readonly="true" cssElementClass="emp_field_select_select1" colSpan="2"/>
						<emp:select id="CusIndiv.bank_duty" label="在我行职务" dictname="STD_ZB_BANK_DUTY" />
						<emp:text id="CusIndiv.stockhold_code" label="股权证号码" />
						<emp:text id="CusIndiv.com_hold_stk_amt" label="拥有我行股份金额(元)" maxlength="18" required="false" dataType="Currency"/>
						
					</emp:gridLayout>
					<!-- /* modify by wangj 2015-05-20  中征码修改 begin*/ -->
					<emp:gridLayout id="" title="中征码信息" maxColumn="2" >
						<emp:select id="CusBase.loan_card_flg" label="是否有中征码" required="true" colSpan="2" hidden="false" dictname="STD_ZX_YES_NO" />
						<emp:text id="CusBase.loan_card_id" label="中征码" maxlength="16" required="true"  hidden="true" />
						<emp:text id="CusBase.loan_card_pwd" label="贷款卡密码" maxlength="6" required="false"  hidden="true"/>
						<emp:select id="CusBase.loan_card_eff_flg" label="贷款卡状态" required="false"  hidden="true" dictname="STD_ZB_LOAN_CARD_FLG" />
						<emp:date id="CusBase.loan_card_audit_dt" label="贷款卡年检到期日" required="false"  hidden="true" onblur="checkLoanCardAuditDt()"/>
					</emp:gridLayout>
					<!-- /* modify by wangj 2015-05-20  中征码修改 end*/ -->
					<emp:gridLayout id="" title="登记信息" maxColumn="2" >
						<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" />
						<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="false"/>
						<emp:text id="CusBase.input_id_displayname" label="登记人"  required="true" readonly="true" defvalue="$currentUserName" />
						<emp:text id="CusBase.input_br_id_displayname" label="登记机构"  required="true" readonly="true" defvalue="$organName" />
						<emp:date id="CusBase.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
						<emp:select id="CusBase.belg_line" label="所属条线" dictname="STD_ZB_BUSILINE" readonly="true" defvalue="BL300"/>
						
						<emp:text id="CusBase.cust_mgr" label="主管客户经理" required="true" hidden="true"/>
						<emp:text id="CusBase.main_br_id" label="主管机构" required="true" hidden="true"/>
						<emp:text id="CusBase.input_id" label="登记人" maxlength="20" required="true" readonly="true" defvalue="$currentUserId" hidden="true"/>
						<emp:text id="CusBase.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" defvalue="$organNo" hidden="true"/>
					</emp:gridLayout>
				</div>
			</emp:tab>
			<emp:tab id="cont_tab" label="联系信息" initial="true" needFlush="true"><div>
				<emp:gridLayout id="CusIndivGroup" title="个人客户联系信息" maxColumn="2">
					<emp:text id="CusIndiv.post_addr" label="身份证地址" required="true" hidden="true"/>
					<emp:pop id="CusIndiv.post_addr_displayname" label="身份证地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnPostAddr" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
					<emp:text id="CusIndiv.street2" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusIndiv.post_code" label="邮政编码" maxlength="6" required="true" dataType="Postcode"/>
					<emp:text id="CusIndiv.indiv_rsd_addr" label="常住居住地址" required="true" hidden="true"/>
					<emp:pop id="CusIndiv.indiv_rsd_addr_displayname" label="常住居住地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnRsdAddr" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
					<emp:text id="CusIndiv.street3" label="常住居住地址街道/路"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusIndiv.indiv_zip_code" label="常住居住地邮政编码" maxlength="6" required="true" dataType="Postcode" />
					<emp:select id="CusIndiv.indiv_rsd_st" label="常住居住状况" required="true" dictname="STD_ZB_RESIDE_STATUS"/>
					<emp:pop id="CusIndiv.area_code" label="区域编号" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" colSpan="2" readonly="true"/>
					<emp:text id="CusIndiv.area_code_displayname" label="区域名称" required="true" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true"/>
					<emp:text id="CusIndiv.fphone" label="住宅电话" maxlength="35" required="false"  dataType="Phone" defvalue="0573"/>
					<emp:text id="CusIndiv.mobile" label="手机号码" maxlength="35" required="true" dataType="Mobile"/>
					<emp:text id="CusIndiv.phone" label="第二联系方式(手机)" maxlength="35" required="false" dataType="Mobile" />
					<emp:text id="CusIndiv.fax_code" label="传真" maxlength="35" required="false" dataType="Phone" />
					<emp:text id="CusIndiv.email" label="Email地址" maxlength="80" required="false" dataType="Email" colSpan="2" />
				</emp:gridLayout>
			</div>
			</emp:tab>
			<emp:tab id="work_tab" label="单位信息" initial="true" needFlush="true"><div>
				<emp:gridLayout id="CusIndivGroup" title="个人客户单位信息" maxColumn="2">
					<emp:select id="CusIndiv.indiv_occ" label="职业" dictname="STD_ZX_EMPLOYMET"  required="true" cssElementClass="emp_field_text_input2" onchange="doHidden()" colSpan="2"/>
				    <emp:text id="CusIndiv.indiv_com_name" label="工作单位" maxlength="60" required="true" colSpan="2" />
					<emp:select id="CusIndiv.indiv_com_typ" label="单位性质" dictname="STD_ZB_UNIT_TYPE" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:pop id="CusIndiv.indiv_com_fld" label="单位所属行业" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturn" colSpan="2" />
					<emp:text id="CusIndiv.indiv_com_fld_displayname" label="单位所属行业名称" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:text id="CusIndiv.indiv_com_addr" label="单位地址" required="true" hidden="true"/>
					<emp:pop id="CusIndiv.indiv_com_addr_displayname" label="单位地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL"
						returnMethod="onReturnIndivAddr" colSpan="2" cssElementClass="emp_field_text_input2" required="true" />
					<emp:text id="CusIndiv.street_unit" label="单位地址街道/路"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusIndiv.indiv_com_zip_code" label="单位邮编" maxlength="6" required="false" />
					<emp:text id="CusIndiv.indiv_com_phn" label="单位电话" maxlength="25" required="false" dataType="Phone" />
					<emp:text id="CusIndiv.indiv_com_fax" label="单位传真" maxlength="25" required="false" dataType="Phone" />
					<emp:text id="CusIndiv.indiv_com_cnt_name" label="单位联系人" maxlength="30" required="false" />
					<emp:text id="CusIndiv.indiv_work_job_y" label="单位工作起始年" required="true" onblur="checkWorkJobY()"/>
					<emp:select id="CusIndiv.indiv_com_job_ttl" label="职务" dictname="STD_ZX_DUTY" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:select id="CusIndiv.indiv_crtfctn" label="职称" required="true" dictname="STD_ZX_TITLE"/>
					<emp:text id="CusIndiv.indiv_ann_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency"/>
					<emp:text id="CusIndiv.indiv_sal_acc_bank" label="工资账户开户行" maxlength="80" required="false" colSpan="2" />
					<emp:text id="CusIndiv.indiv_sal_acc_no" label="工资账号" maxlength="32" required="false" />
					<emp:textarea id="CusIndiv.work_resume" label="个人简历" maxlength="125" colSpan="2" required="false"></emp:textarea>
				</emp:gridLayout></div>
			</emp:tab>
		 	<emp:tab label="配偶信息" id="addSpouseInfo" url="addCusIndivSpouseInfo.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}&indiv_sex=$CusIndiv.indiv_sex;" needFlush="true"></emp:tab>
			<emp:tab label="经营信息" id="managerBusInfo" url="queryCusIndivBusinessList.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}&EditFlag=${context.flag}" needFlush="true"></emp:tab>
			<emp:tab label="修改痕迹" id="modifyHistory" url="queryModifyHistoryList.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}" needFlush="true"></emp:tab>
		</emp:tabGroup>

		<div align="center">
		<br>
		<%if( "rep".equals(btnflag) ) {%>
			<emp:button id="updateCusIndiv3" label="补录完成" />
		<%}else if( "temp".equals(btnflag)) {%>
			<emp:button id="updateCusIndiv2" label="保存"/>
	    <% }else if("due".equals(btnflag)){%>
			<emp:button id="updateCusIndiv" label="保存" />
		<%} %>
		<%if(oper != null && !oper.equals("infotree")){ %>
			<emp:button id="return" label="返回"/>
		<%}%>
		</div>
	</emp:form>
</body>
</html>
</emp:page>