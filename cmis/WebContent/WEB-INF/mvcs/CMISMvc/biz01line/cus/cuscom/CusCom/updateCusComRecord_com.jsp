<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<%
	String flag = request.getParameter("flag");
	String oper = request.getParameter("oper");
	String btnflag = request.getParameter("btnflag");//控制按钮 (rep:补录完成  due:正式客户 temp:临时、草稿)
	if ((flag != null && flag.equals("query"))) {
		request.setAttribute("canwrite", "");
	}
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	context.addDataField("infotree",oper);
%>
<html>
<head>
<title>客户信息页面</title>
<jsp:include page="/include.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<jsp:include page="jsCusCom.jsp" flush="true" />

<link href="<emp:file fileName='styles/start/jquery-ui-1.7.1.custom.css'/>" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-ui-1.7.1.custom.min.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery.cmisDialogs.js'/>"></script>
<script type="text/javascript">
	/* modify by yangzy 2015-05-21  中征码修改 begin*/
	var tmpLoanCardId;
	/* modify by yangzy 2015-05-21  中征码修改 end*/
	//正式客户的保存
	function doUpdateCusCom(){
		checkCusRel();
	}

	//保存客户信息方法
	function saveCusInfo(){
		var cusId = CusBase.cus_id._getValue();
		
		var chkRes = checkBeforeSub();
	    if(chkRes){
		    var form = document.getElementById("submitForm");
		    var result = CusCom._checkAll();
		    var result2 = CusBase._checkAll();
		    if(result&&result2){
		        CusCom._toForm(form);
		        CusBase._toForm(form);
		        toSubmitForm(form);
		    }else {
			    alert("保存失败！\n请检查各标签页面中的必填信息填写是否正确！");
			}
		}
	}
	//检查是否有关联客户
	function checkCusRel(){
		var relFlag = CusCom.rel_flag._getValue();
        var cusId = CusBase.cus_id._getValue();
        var handleSuccess = function(o){
            if(relFlag==1){
        	if(o.responseText !== undefined) {
        		try {
        			var jsonstr = eval("("+o.responseText+")");
        		} catch(e) {
        			alert("Parse jsonstr1 define error!" + e.message);
        			return;
        		}
        		var flag = jsonstr.flag;
        		var msg = jsonstr.msg;
        		if(flag == "failed"){
        			alert(msg);
        			return false;
        		}else{
        			saveCusInfo();
        		}
        	}
            }else{
        		saveCusInfo();
            }
        };
        var handleFailure = function(o){
        	alert("异步回调失败！");	
        };
        var callback = {
        	success:handleSuccess,
        	failure:handleFailure
        };
        var url = '<emp:url action="getCusCliRelByCusId.do"/>&cusId='+cusId;
        var url = EMPTools.encodeURI(url);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
       }      
          
	//补录
	function doSuppCusCom(){
	    //企业规模
	    var com_scale = CusCom.com_scale._getValue();
	    if(com_scale==null||com_scale==''){
			alert('请进行企业规模测算！');
			return;
		}
	    var chkRes = checkBeforeSub();
	    if(chkRes){
		    var result = CusCom._checkAll();
		    var result1 = CusBase._checkAll();
		    if(result&&result1){
			    CusBase.cus_status._setValue("20");
			    var form = document.getElementById("submitForm");
			    form.setAttribute("action","UpdateCusComRepRecord.do");
		        CusCom._toForm(form);
		        CusBase._toForm(form);
		        toSubmitForm2Supp(form);
		    }else {
			    alert("保存失败！\n请检查各标签页面中的必填信息填写是否正确！");
			}
		}
	}

	//保存前进行校验
	function checkBeforeSub(){
		var loanCardId = CusBase.loan_card_id._obj.element.value;
		var LocTaxRegCode = CusCom.loc_tax_reg_code._getValue();
		var NatTaxRegcode = CusCom.nat_tax_reg_code._getValue();
		//20150113 Edited by FCL 财务部负责人和财务部联系人二选一
        var fna_mgr = CusCom.fna_mgr._getValue();
        var com_opr = CusCom.com_operator._getValue();
        if((fna_mgr == null || fna_mgr == '')&& (com_opr == null || com_opr == '')){
			alert("财务部负责人和财务部联系人不能同时为空");
			return false;
        }
        //---------------20150113 END--------------------------- 
		if((LocTaxRegCode == null || LocTaxRegCode == "")&&(NatTaxRegcode == null || NatTaxRegcode =="")){
			alert("地税、国税信息必须至少录入一项！");
			return false;
		}

		var cus_bank_rel = CusCom.cus_bank_rel._getValue();
	    if(cus_bank_rel == 'B2' || cus_bank_rel == 'B3' || cus_bank_rel == 'B5' || cus_bank_rel == 'B8'){
	        if(CusCom.bank_equi_amt._obj.element.value == ''){
	            alert("该客户为本行股东,请输入拥有我行股份金额!");
	            return false;
	        }
	    }
		/**地址信息中的联系电话与联系手机至少录入一项    2014-09-25 唐顺岩**/
		var legal_phone = CusCom.legal_phone._getValue(); //联系电话
		var phone = CusCom.phone._getValue();   //联系手机
		if((legal_phone == null || legal_phone == "")&&(phone == null || phone =="")){
			alert("地址信息中的[联系电话]与[联系手机]至少录入一项 ！")
			return false;
		}
		/**END**/
		
	    return true;
	}

	function toSubmitForm2Supp(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
               try {
                 var jsonstr = eval("("+o.responseText+")");
               } catch(e) {
                 alert("保存失败！");
                 return;
               }
               var flag = jsonstr.flag;
               var message = jsonstr.message;
               if(flag=="success"){
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
        };
        var handleFailure = function(o){
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var postData = YAHOO.util.Connect.setForm(form);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	//临时客户的保存
	function doTempAddCusCom(){
		var cus_status = CusBase.cus_status._getValue();
		if(cus_status=="20"){
			alert("该客户为正式客户，请点击保存按钮进行保存！");
			return;
		}
		var form = document.getElementById("submitForm");
//		var tempflag = ;
//		form.setAttribute("action","tempUpdateCusComRecord.do?tempflag="+tempflag);
		var btnflag = '<%=btnflag%>';
		form.setAttribute("action","tempUpdateCusComRecord.do?btnflag="+btnflag);
		CusCom._toForm(form);
		CusBase._toForm(form);
		form.submit();
	}

	function doCheckCusRelTemp(){
		var relFlag = CusCom.rel_flag._getValue();
        var cusId = CusBase.cus_id._getValue();
        var handleSuccess = function(o){
            if(relFlag==1){
        	if(o.responseText !== undefined) {
        		try {
        			var jsonstr = eval("("+o.responseText+")");
        		} catch(e) {
        			alert("Parse jsonstr1 define error!" + e.message);
        			return;
        		}
        		var flag = jsonstr.flag;
        		var msg = jsonstr.msg;
        		if(flag == "failed"){
        			alert(msg);
        			return false;
        		}else{
        			doTempAddCusCom();
        		}
        	}
            }else{
            	doTempAddCusCom();
            }
        };
        var handleFailure = function(o){
        	alert("异步回调失败！");	
        };
        var callback = {
        	success:handleSuccess,
        	failure:handleFailure
        };
        var url = '<emp:url action="getCusCliRelByCusId.do"/>&cusId='+cusId;
        var url = EMPTools.encodeURI(url);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
       }      

	
	function toSubmitForm(form){
      var handleSuccess = function(o){
            if(o.responseText !== undefined) {
               try {
                 var jsonstr = eval("("+o.responseText+")");
               } catch(e) {
                 alert("保存失败！");
                 return;
               }
               var flag = jsonstr.flag;
               var infotree = '${context.infotree}';
               if(flag=="success"){
	           	  	alert("保存成功！");
		           	var cus_status = CusBase.cus_status._getValue();
				 	if(cus_status!="20"){
				 		window.location.reload();
					}
               }else doReturn();
            }
        };
        var handleFailure = function(o){
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var postData = YAHOO.util.Connect.setForm(form);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
     };

	function doOnLoad() {
		/* modify by yangzy 2015-05-21  中征码修改 begin*/
		tmpLoanCardId = CusBase.loan_card_id._getValue();
		/* modify by yangzy 2015-05-21  中征码修改 end*/
		CusCom.com_scale._obj.addOneButton('uniquCheck', '测算',doMeasure);
		CusCom.grp_no._obj.addOneButton('view12','查看',viewGrpCusInfo);  //集团编号加查看按钮
		//CusCom.reg_addr._obj.addPrompt('大额上报:省市县街道必填');
		checkRegCode();//税务关系
		changeRelaCust();//是否为我行关联客户
	
	    //上市公司标识事件
		EMPTools.addEvent(CusCom.com_mrk_flg._obj.element, "change", cheakMrk);
	
	    //特种经营信息事件
	    EMPTools.addEvent(CusCom.com_sp_business._obj.element, "change", cheakSpBusiness);

		//信用等级（外部）
	    changeGrade();
		
	    licenceRequired('1');
	    cheakSpBusiness();//检查特种经营标识
	    cheakMrk();
	    //是否有贷款卡
	    changeCardFlg();
		//高环境风险高污染企业
		doChange();
	    //得到财务报表类型列表
	    //getRepType();
		//联动显示集团客户相关信息
		linkChangeComGrpMode();
		//判断客户类型是否为融资性担保公司
		checkCoopInfo();

		//若客户类型为正式客户则主管客户经理、主管机构字段不能修改
		var cus_status = CusBase.cus_status._getValue();
		if(cus_status=='20'){
			CusBase.main_br_id_displayname._obj._renderReadonly(true);
			CusBase.cust_mgr_displayname._obj._renderReadonly(true);
		}

		//进出口权标志
		checkIeFlag();
		var val = CusBase.cert_type._getValue();
		if("a"!=val){
			//CusBase.cert_code1._setValue("");
			//CusBase.cert_code1._obj._renderReadonly(false);
		}
	};
	function doMeasure(){
		var com_cll_type = CusCom.com_cll_type._obj.element.value;  //所属行业编号
		var com_employee = CusCom.com_employee._getValue();  //从业人员
		if(com_cll_type == null || com_cll_type == ""){
			alert("行业编号不能为空,请选择行业编号！");
			return;
		}
		//if(com_employee == null || com_employee == ""){
		//	alert("从业人数不能为空,请填写从业人数！");
		//	return;
		//}		
		var cus_id = CusBase.cus_id._obj.element.value;
		//调用代码
		//var url = '<emp:url action="getEnterpriseScaleOp.do"/>&cus_id='+cus_id+'&com_cll_type='+com_cll_type+'&com_employee='+com_employee;
		//调用规则
		var url = '<emp:url action="getEnterpriseSizeOp.do"/>&cus_id='+cus_id+'&com_cll_type='+com_cll_type+'&com_employee='+com_employee;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var out_flag = jsonstr.out_flag;
				var out_msg = jsonstr.out_msg;
				var com_scale = jsonstr.com_scale;
				if("N"==out_flag || "n"==out_flag){
					alert(out_msg);
					CusCom.com_scale._setValue('20');
				}else{
					alert("测算成功！");
					CusCom.com_scale._setValue(com_scale);
				}
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	
	function cheakCusType(){
		var cus_type= CusBase.cus_type._obj.element.value;
		if(cus_type!=null && cus_type!=""){
			if(cus_type=='D1'||cus_type=='D2'||cus_type=='E1'||cus_type=='E2'||cus_type=='B1'||cus_type=='B2'){
				 CusCom.appr_org._obj._renderRequired(true);
				 CusCom.appr_doc_no._obj._renderRequired(true);
			}else{
				 CusCom.appr_org._obj.element.value="";
				 CusCom.appr_doc_no._obj.element.value="";
	
				 CusCom.appr_org._obj._renderRequired(false);
				 CusCom.appr_doc_no._obj._renderRequired(false);
			}
		}
	}

	function CheckRegDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		var openDay='${context.OPENDAY}';
		
		if(start!=null && start!="" ){
			start = dateLenghtChang(start);
			var flag = CheckDate1BeforeDate2(start,openDay);
			if(!flag){
				alert("输入的日期要小于等于当前日期！");
				date1._obj.element.value="";
			}else{
				if(end!=null && end!=""){
					end = dateLenghtChang(end);
					var ff = CheckDate1BeforeDate2(end,start);
					if(ff){
						alert("到期日期不小于登记日期！");
						date2._obj.element.value="";
					}
				}
		    }
		}
	}

	//校验有效日期
	function CheckLocExpDate(date1,date2,date3){
		var start =  date1._getValue();//登记日期
		var exp = date2._getValue();//登记有效期
		var end = date3._getValue();//年检到期日
		var openDay='${context.OPENDAY}';
		if(start != null && start != ""){
			start = dateLenghtChang(start);
		}
	    if(exp != null && exp != ""){
	    	exp = dateLenghtChang(exp);
		}
	    if(end != null && end != ""){
	    	end = dateLenghtChang(end);
		}
		if(exp!=null && exp!="" ){
			var flag = CheckDate1BeforeDate2(openDay,exp);
			if(!flag){
				alert("登记有效期应大于等于当前日期！");
				date2._obj.element.value="";
				return false;
			}
			if(end!=null && end!="" ){
				var flag = CheckDate1BeforeDate2(end,exp);
				if (!flag) {
					alert("登记有效期应大于等于年检到期日！");
					date2._obj.element.value = "";
					return false;
				}
			}
		 }
	}
	
	function CheckLocEndDate(date1,date2,date3){
		var start =  date1._getValue();//税务登记日期
		var exp = date2._getValue();//税务登记有效期
		var end = date3._getValue();//年检到期日
		var openDay='${context.OPENDAY}';
		if(start != null && start != ""){
			start = dateLenghtChang(start);
		}
	    if(exp != null && exp != ""){
	    	exp = dateLenghtChang(exp);
		}
	    if(end != null && end != ""){
	    	end = dateLenghtChang(end);
		}
		if(end!=null && end!="" ){
			var flag = CheckDate1BeforeDate2(openDay,end);
			if(!flag){
				alert("年检到期日应大于等于当前日期！");
				date3._obj.element.value="";
				return;
			}
		}
		if(exp!=null && exp!="" ){
			if (!CheckDate1BeforeDate2(end,exp)) {
				alert("年检到期日要小于等于登记有效期！");
				date3._obj.element.value = "";
				return;
			}
		}
	}
	
	function CheckExpDate(date1,date2){
		var start = date1._obj.element.value;//登记日期
		var end = date2._obj.element.value;//登记有效期
		var openDay='${context.OPENDAY}';
		if(end!=null && end!="" ){
			var flag = CheckDate1BeforeDate2(openDay,end);
			if(!flag){
				alert("登记有效期应大于等于当前日期！");
				date2._obj.element.value="";
				if(start!=null && start!=""){
					var ff = CheckDate1BeforeDate2(end,start);
					if(ff){
						alert("年检到期日期大于等于登记日期！");
						date2._obj.element.value="";
					}
				}
			}
		}
	}

	function CheckDate4End(date1,date2){
		var start = date1._obj.element.value;//登记有效期
		var end = date2._obj.element.value;//年检到期日
		var flag = CheckDate1BeforeDate2(end,start);
		if(!flag){
			alert("登记有效期应大于等于年检到期日！");
			date1._obj.element.value="";
		}
	}

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

	function toRequired(){
		var country = CusBase.cus_country._obj.element.value;
		if(country=='CTN'||country=='CHN'||country=='HKG'||country=='MAC'){
			CusCom.cus_en_name._obj._renderRequired(false);
		}else{
			CusCom.cus_en_name._obj._renderRequired(true);
		}
	};
	
	//经营场地面积(平方米)
	function cheakAera(){
	   var aera = parseFloat(CusCom.com_opt_aera._obj.element.value);
	   if(isNaN(aera)){
	       alert("面积值应该为数值型！");
	       CusCom.com_opt_aera._obj.element.value="";
	       return ;
	 	}
	   if(aera<= 0){
		   alert("经营场地面积要大于零！");
		   CusCom.com_opt_aera._obj.element.value="";
		   return ;
	    }
	}
	
	//行业编号(国标)
	function onReturnComCllName(date){
		CusCom.com_cll_type._obj.element.value=date.One+date.id;
		CusCom.com_cll_type_displayname._obj.element.value=date.label;
	}

	function onReturnRegStateCode2(date){
		CusCom.acu_addr._obj.element.value=date.id;
		CusCom.acu_addr_displayname._obj.element.value=date.label;
	}

	function onReturnStreetPost(date){
		CusCom.post_addr._obj.element.value=date.id;
		CusCom.post_addr_displayname._obj.element.value=date.label;
		//searchPcodeOther(date.id);
		var addSort = "streePost";
		searchPcode(date.id,addSort);
	}

	function onReturnRegStateCode1(date){
		CusCom.reg_addr._obj.element.value=date.id;
		CusCom.reg_addr_displayname._obj.element.value=date.label;
		var addSort = "regState";
		searchPcode(date.id,addSort);
	}

	//获取相对应的邮政编码
	function searchPcode(date,addSort){
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
					if(addSort=="streePost"){
						CusCom.post_code._setValue(post_code);
					}else if(addSort=="regState"){
						CusCom.reg_area_code._setValue(post_code);
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
        var url = '<emp:url action="getRegStateCode2Pcode.do"/>?post_addr='+post_addr+"&addSort="+addSort;
  	  	url = EMPTools.encodeURI(url);
  	  	var postData = YAHOO.util.Connect.setForm();
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}

	//注册地行政区划
	function onReturnRegStateCode(date){
		CusCom.reg_state_code._obj.element.value=date.id;
		CusCom.reg_state_code_displayname._obj.element.value=date.label;
	}
	
	function cheakAmt(amt){
		var cap_amt = CusCom.paid_cap_amt._getValue();
		if(cap_amt==null||cap_amt==''){
			return null;
		}
		var reg_amt = CusCom.reg_cap_amt._getValue();
		if(reg_amt==null||reg_amt==''){
			return null;
		}
		if(parseFloat(cap_amt)>parseFloat(reg_amt)){
		   alert("实收注册资金不能大于注册资金！");
		   amt._obj.element.value="";
		}
	}

	//基本存款账户信息
	function licenceRequired(a){
		var bas_acc_flg = CusCom.bas_acc_flg._obj.element.value;
		if(bas_acc_flg=='1'){
			CusCom.bas_acc_no._obj._renderReadonly(true);
			CusCom.bas_acc_date._obj._renderReadonly(true);
			CusCom.bas_acc_bank._obj._renderReadonly(true);
		//	CusCom.bas_acc_licence._obj._renderReadonly(true);
			CusCom.bas_acc_bank_displayname._obj._renderReadonly(true);
			if('2'==a){
				queryBasAccByCusId();
				setlicenceNull();
			}
		}else if(bas_acc_flg=='2'){
			CusCom.bas_acc_no._obj._renderReadonly(false);
			CusCom.bas_acc_date._obj._renderReadonly(false);
			CusCom.bas_acc_bank._obj._renderReadonly(false);
		//	CusCom.bas_acc_licence._obj._renderReadonly(false);
			CusCom.bas_acc_bank_displayname._obj._renderReadonly(false);
			if('2'==a){
				setlicenceNull();
			}
		}
	}
	
	//变化后将值置为空
	function setlicenceNull(){
		CusCom.bas_acc_no._setValue('');
		CusCom.bas_acc_date._setValue('');
		CusCom.bas_acc_bank._setValue('');
		CusCom.bas_acc_licence._setValue('');
		CusCom.bas_acc_bank_displayname._setValue('');
	}
	//根据客户码获取基本存款账户信息
	function queryBasAccByCusId(){ 
		var cert_type = CusBase.cert_type._getValue();
		var cert_code = CusBase.cert_code._getValue();
		var handleSuccess = function(o){
            if(o.responseText !== undefined) {
				try {
				  var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
				  alert("获取失败！");
				  return;
				}
			 	var msg=jsonstr.msg
			 	if(msg!=null&&msg!=""){
			 		alert(msg);
					return;
				}
               	var accList=jsonstr.accList;
				var accObj;
				if (accList != null && accList.length > 0) {
					accObj=accList[0];
					returnCusAcc(accObj);
				} else {
					alert("记录为空！");
				}
            }
        };
        var handleFailure = function(o){
            alert("获取失败");
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var url = '<emp:url action="queryBasAccByCusId.do"/>?cert_type='+cert_type+'&cert_code='+cert_code;
  	  	url = EMPTools.encodeURI(url);
  	  	var postData = YAHOO.util.Connect.setForm();
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}
	//返回客户基表账户信息
	function returnCusAcc(cusObj){
		if(cusObj.flag=='success'){
			//基本存款账户开户许可证
			var bas_acc_licence = cusObj.bas_acc_licence;
			if(bas_acc_licence!=null){
				CusCom.bas_acc_licence._setValue(bas_acc_licence);
			}
			//基本存款账户帐号
			var bas_acc_no = cusObj.bas_acc_no;
			if(bas_acc_no!=null){
				CusCom.bas_acc_no._setValue(bas_acc_no);
			}
			//基本账户开户日期
			var bas_acc_date = cusObj.bas_acc_date;
			if(bas_acc_date!=null){
				CusCom.bas_acc_date._setValue(bas_acc_date);
			}
			//基本存款账户开户行
			var bas_acc_bank = cusObj.bas_acc_bank;
			if(bas_acc_bank!=null){
				CusCom.bas_acc_bank._setValue(bas_acc_bank);
			}
			//基本存款账户开户行名称
			var bas_acc_bank_displayname = cusObj.bas_acc_bank_displayname;
			if(bas_acc_bank_displayname!=null){
				CusCom.bas_acc_bank_displayname._setValue(bas_acc_bank_displayname);
			}
		}else if(cusObj.flag=='notexists'){
			alert('客户基本存款账户不存在！');
			return;
		}else {
			alert('客户存在账户，但不是基本户！');
			return;
		}
	}
	//基本账户开户行
	function getAaorgNo(data){
		CusCom.bas_acc_bank._setValue(data.bank_no._getValue());
		CusCom.bas_acc_bank_displayname._setValue(data.bank_name._getValue());
	};
	function dateLenghtChang(date){
		if(date.length==8){
			date = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		}
		return date;
	}
	
	function dateBetweenAnd(date1,date2,date3){
		var dateStr = date1._obj.element.value;//登记日期
		var dateMo = date2._obj.element.value;//年检到期日
		var dateEnd = date3._obj.element.value;//登记有效期
		var openDay='${context.OPENDAY}';
		if(dateMo!=null && dateMo!="" ){
			if(!CheckDate1BeforeDate2(openDay,dateMo)){
				alert("年检到期日应大于等于当前日期！");
				date2._obj.element.value="";
			}
			if(dateEnd!=null && dateEnd!="" ){
				if (!CheckDate1BeforeDate2(dateMo,dateEnd)) {
					alert("年检到期日要小于等于登记有效期!");
					date2._obj.element.value = "";
				}
			}
		 }
	}

	//校验母公司组织机构代码
	function checkParentCertCode(){
		var certCode = CusCom.parent_cert_code._getValue();
		if(!CheckOrganFormat(certCode)){
			CusCom.parent_cert_code._setValue('');
		}
	}
	/* modify by wangj 2015-05-20  中征码修改 begin*/
	//校验中征码
	/* modify by wangj 2015-05-20  中征码修改 end*/
	function cheakCardId(){
		var loanCardId = CusBase.loan_card_id._obj.element.value;
		if(loanCardId!=null && loanCardId!=""){
		var CheckDKK_flag=CheckDKK(loanCardId);
			if(CheckDKK_flag!=true){
				/* modify by wangj 2015-05-21  中征码修改 begin*/
				 alert("中征码不正确");
				/* modify by wangj 2015-05-21  中征码修改 end*/
			     CusBase.loan_card_id._setValue('');
			     CusBase.loan_card_id._obj._renderStatus();
			}else{
				CheckLoanCardId();
			}
		}
	};
	
	//add by zhoujf 2009-08-08　贷款帐号唯一性检查
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
						alert("中征码在系统中已经存在！");
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
	//检查集团客户贷款卡号
	function CheckParentLoanCardId(){
	  var loan_card_id = CusCom.parent_loan_card._getValue();
	  if(loan_card_id!=null && loan_card_id!=""){
			var CheckDKK_flag=CheckDKK(loan_card_id);
			if(CheckDKK_flag!=true){
				 alert("贷款卡号不正确");
			}
		}
	}
	//add by zhoujf 2009-08-08 帐号唯一性检查
	function CheckBasAccNo(){
		var baseAccNo = CusCom.bas_acc_no._obj.element.value;
		var cusNo = CusBase.cus_id._obj.element.value;
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.accNoFlag;
				if(flag == 'y'){
					alert("该基本存款帐号在系统中已经存在！");
					CusCom.bas_acc_no._obj.element.value = '';
					return;
				}
			}
		  }
		  var handleFailure = function(o){
		  };
		  var callback = {
			success:handleSuccess,
			failure:handleFailure
		  };
		  var paramStr="accType=bas&accNo="+baseAccNo+"&cusNo="+cusNo;
		  var url = '<emp:url action="getAccNoOnly.do"/>&' + paramStr;
		  var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}

	function giveValue2PaidCapCurType(){
		var regCurType = CusCom.reg_cur_type._getValue();
		CusCom.paid_cap_cur_type._setValue(regCurType);
    }
    
    //基本存款账户开户许可证
	function checkBasAccLic(){
		var bas = CusCom.bas_acc_licence._getValue();
		if(bas != '' && bas != null){
			var basFirst = bas.substring(0,1);
			if(!(/^[A-Z]+$/.test(basFirst))){
				//return true;
				alert("首字母必须大写！");
				CusCom.bas_acc_licence._setValue('');
				return false;
			}
		}
	}

	function regStartDate(){
		var com_str_date = CusCom.com_str_date._getValue();
		var openDay='${context.OPENDAY}';
		if(com_str_date!=null && com_str_date!="" ){
			var flag = CheckDate1BeforeDate2(com_str_date,openDay);
			if(!flag){
				alert("输入日期要小于等于当前日期！");
				CusCom.com_str_date._setValue("");
			}
	     }
		CusCom.reg_start_date._setValue(com_str_date);
	}

	//企业性质
	function onReturnCorpQlty(date){
	//	CusCom.corp_qlty._obj.element.value=date.One+date.id;
		CusCom.corp_qlty._obj.element.value=date.id;
		CusCom.corp_qlty_displayname._obj.element.value=date.label;
	}

	//国民经济部门
	function onReturnEconDept(date){
	//	CusCom.econ_dep._obj.element.value=date.One+date.id;
		CusCom.econ_dep._obj.element.value=date.id;
		CusCom.econ_dep_displayname._obj.element.value=date.label;
	}

	function checkRegCodeRequire(){
		var LocTaxRegCode = CusCom.loc_tax_reg_code._getValue();
		var NatTaxRegcode = CusCom.nat_tax_reg_code._getValue();
		if((LocTaxRegCode == null || LocTaxRegCode == "")&&(NatTaxRegcode == null||NatTaxRegcode == "")){
			alert("地税、国税信息必须至少录入一项！")
			return;
		}
	}
	//返回主管客户经理	
	function setconId(data){
		CusBase.cust_mgr._setValue(data.actorno._getValue());
		CusBase.cust_mgr_displayname._setValue(data.actorname._getValue());
		CusBase.main_br_id._setValue(data.orgid._getValue());
		CusBase.main_br_id_displayname._setValue(data.orgid_displayname._getValue());
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
		CusBase.main_br_id._setValue(data.organno._getValue());
		CusBase.main_br_id_displayname._setValue(data.organname._getValue());
	}

	//校验建立信贷关系时间小于等于当前日期
	function checkInitLoanDate(){
		var initLoanDate = CusCom.com_init_loan_date._getValue();
		var openDay='${context.OPENDAY}';
		if(initLoanDate>openDay){
			alert("建立信贷关系时间应小于等于当前日期！");
			CusCom.com_init_loan_date._setValue("");
			return;
		}
	}

	function checkLoanCardAuditDt(){
		var date = CusBase.loan_card_audit_dt._getValue();
		var openDay = '${context.OPENDAY}';
		if(date !=null && date != ""){
			if(date<openDay){
				alert("年检到期日应大于等于当前日期！");
				CusBase.loan_card_audit_dt._setValue("");
			}
		}
	}

	function checkIsLong(){//是否为长期证件
		var is_long_com = CusCom.is_long_com._getValue();
		if(is_long_com == '2'){//否
			CusCom.reg_end_date._obj._renderHidden(false);
			CusCom.reg_end_date._obj._renderRequired(true);
		}else{
			CusCom.reg_end_date._obj._renderHidden(true);
			CusCom.reg_end_date._obj._renderRequired(false);
		}
	}

	function doSaveTempCusInfo(){
		var form = document.getElementById("submitForm");
		CusCom._toForm(form);
        CusBase._toForm(form);
        toSubmitForm(form);
	}
	
</script>

	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateCusComRecord.do" method="POST">
		<emp:tabGroup id="CusCom_tabs" mainTab="base_tab">
			<emp:tab id="base_tab" label="基本信息" initial="true" >
				<div>
					<emp:gridLayout id="CusComGroup" title="对公客户基本信息" maxColumn="2">
					<emp:select id="CusBase.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP" /> 
					<emp:text id="CusBase.cert_code" label="证件号码" maxlength="20" required="true" readonly="true" />
					<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
					<emp:text id="CusBase.cus_name" label="客户名称" maxlength="80" onchange="CusBase.cus_short_name._setValue(this.value);" required="true" readonly="true" />
					<emp:text id="CusBase.cus_short_name" label="客户简称" maxlength="46" required="false" />
					<emp:text id="CusCom.cus_en_name" label="外文名称" maxlength="80" required="false" />
					<emp:select id="CusBase.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
					<emp:select id="CusBase.cus_country" label="国别" required="true" dictname="STD_GB_2659-2000" defvalue="CHN" onblur="toRequired()" readonly="true" />
					<emp:select id="CusCom.com_city_flg" label="城乡类型" required="true" dictname="STD_ZB_URBAN_RURAL" />
					<emp:select id="CusCom.invest_type" label="投资主体" required="true" dictname="STD_ZB_INVESTOR" />
					<emp:select id="CusCom.com_sub_typ" label="隶属关系" required="true" dictname="STD_ZB_SUBJECTION" />
					<emp:select id="CusCom.com_hold_type" label="控股类型" required="true" dictname="STD_ZB_HOLD_TYPE" />
					<emp:pop id="CusCom.corp_qlty" label="企业性质编号" url="showDicTree.do?dicTreeTypeId=STD_GB_CORP_QLTY" required="true" returnMethod="onReturnCorpQlty"/>
					<emp:text id="CusCom.corp_qlty_displayname" label="企业性质" colSpan="2" cssElementClass="emp_field_text_input2"   readonly="true"/>
					<emp:pop id="CusCom.econ_dep" label="国民经济部门编号" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_ECON_DEPT" returnMethod="onReturnEconDept"/>
					<emp:text id="CusCom.econ_dep_displayname" label="国民经济部门"  colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:pop id="CusCom.com_cll_type" label="行业编号(国标)" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturnComCllName"/>
					<emp:text id="CusCom.com_cll_type_displayname" label="行业名称" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:date id="CusCom.com_str_date" label="成立日期" required="true" onblur="regStartDate();" />
					<emp:text id="CusCom.com_employee" label="从业人数" maxlength="38" required="true" dataType="Int" />
					<emp:select id="CusCom.com_scale" label="企业规模" dictname="STD_ZB_ENTERPRISE" readonly="true" required="true" colSpan="2"/>
					<emp:pop id="CusCom.reg_state_code" label="注册地行政区划" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" colSpan="2" />
					<emp:text id="CusCom.reg_state_code_displayname" label="行政区划名称" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"  />
				</emp:gridLayout> 
				<%-- <emp:gridLayout id="" title="组织机构代码信息" maxColumn="2">
					<emp:text id="CusBase.cert_code1" label="组织机构代码" maxlength="20" required="true" colSpan="2" readonly="true" defvalue="$CusBase.cert_code"/>
					<emp:date id="CusCom.com_ins_reg_date" label="组织机构登记日期" required="true" onblur="CheckRegDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);" />
					<emp:date id="CusCom.com_ins_exp_date" label="组织机构登记有效期" required="true" readonly="false"
						onblur="CheckExpDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);CheckDate4End(CusCom.com_ins_exp_date,CusCom.com_ins_ann_date)" />
					<emp:text id="CusCom.com_ins_org" label="组织机构代码证颁发机关" maxlength="60" required="true"/>
					<emp:date id="CusCom.com_ins_ann_date" label="组织机构代码证年检到期日" required="false" onblur="dateBetweenAnd(CusCom.com_ins_reg_date,CusCom.com_ins_ann_date,CusCom.com_ins_exp_date);" />
				</emp:gridLayout>  --%>
				<emp:gridLayout id="" title="证件信息" maxColumn="2">
					<emp:text id="CusBase.cert_code1" label="证件代码" maxlength="20" required="true" colSpan="2" readonly="true" defvalue="$CusBase.cert_code"/>
					<emp:date id="CusCom.com_ins_reg_date" label="证件登记日期" required="true" onblur="CheckRegDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);" />
					<emp:date id="CusCom.com_ins_exp_date" label="证件登记有效期" required="true" readonly="false"
						onblur="CheckExpDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);CheckDate4End(CusCom.com_ins_exp_date,CusCom.com_ins_ann_date)" />
					<emp:text id="CusCom.com_ins_org" label="证件颁发机关" maxlength="60" required="false"/>
					<emp:date id="CusCom.com_ins_ann_date" label="证件年检到期日" required="false" />
				</emp:gridLayout>
				<emp:gridLayout id="" title="登记注册信息" maxColumn="2">
					<emp:text id="CusCom.reg_code" label="登记注册号" maxlength="30" required="true" colSpan="2" readonly="false"/>
					<emp:select id="CusCom.reg_type" label="登记注册类型" required="true" dictname="STD_ZB_REG_TYPE" cssElementClass="emp_field_text_input2"/>
					<emp:text id="CusCom.admin_org" label="主管单位" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.appr_org" label="登记机关" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.appr_doc_no" label="批准文号" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.reg_addr" label="注册登记地址" colSpan="2" hidden="true"/>
					<emp:pop id="CusCom.reg_addr_displayname" label="注册登记地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
					<emp:text id="CusCom.reg_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
					<emp:text id="CusCom.en_reg_addr" label="外文注册登记地址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.acu_addr" label="实际经营地址" required="false" colSpan="2" hidden="true"/>
					<emp:pop id="CusCom.acu_addr_displayname" label="实际经营地址" colSpan="2" cssElementClass="emp_field_text_input2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnRegStateCode2" required="true"/>	
					<emp:text id="CusCom.street" label="街道(住所)"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
					<emp:textarea id="CusCom.com_part_opt_scp" label="一般经营项目" maxlength="250" required="true" colSpan="2" />
					<emp:textarea id="CusCom.com_main_opt_scp" label="许可经营项目" maxlength="500" required="false" colSpan="2" />
					<emp:select id="CusCom.reg_cur_type" label="注册资本币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" onblur="giveValue2PaidCapCurType()" readonly="false"/>
					<emp:text id="CusCom.reg_cap_amt" label="注册资金(万元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusCom.reg_cap_amt)" />
					<emp:select id="CusCom.paid_cap_cur_type" label="实收资本币种" dictname="STD_ZX_CUR_TYPE" required="true" hidden="false" readonly="false" defvalue="CNY"/>
					<emp:text id="CusCom.paid_cap_amt" label="实收注册资金(万元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusCom.paid_cap_amt)" />	
					<emp:date id="CusCom.reg_start_date" label="注册登记日期" required="true" onblur="CheckRegDate(CusCom.reg_start_date,CusCom.reg_end_date)" />
					<emp:date id="CusCom.reg_audit_end_date" label="注册登记年检到期日" onblur="dateBetweenAnd(CusCom.reg_start_date,CusCom.reg_audit_end_date,CusCom.reg_end_date);" />
					<emp:select id="CusCom.is_long_com" label="是否为长期" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsLong()" defvalue="1"/>	
					<emp:date id="CusCom.reg_end_date" label="注册登记有效期" required="false" hidden="true" onblur="CheckLocExpDate(CusCom.reg_start_date,CusCom.reg_end_date,CusCom.reg_audit_end_date)" />	
					<emp:text id="CusCom.reg_area_code" label="注册地区域编码" required="true" colSpan="2" readonly="false" dataType="Postcode"/>
					<emp:date id="CusCom.reg_audit_date" label="注册登记年审日期" required="false" colSpan="2"/>	
					<emp:textarea id="CusCom.reg_audit" label="注册登记年审结论" maxlength="200" required="false" colSpan="2" />	
				</emp:gridLayout> 
				<emp:gridLayout id="" title="税务信息" maxColumn="2">
					<emp:text id="CusCom.loc_tax_reg_code" label="地税税务登记代码" maxlength="20" colSpan="2" onblur="checkRegCode()"/>
					<emp:date id="CusCom.loc_tax_reg_dt" label="地税税务登记日期" onblur="CheckDate(CusCom.loc_tax_reg_dt);checkRegCode()" />
					<emp:date id="CusCom.loc_tax_reg_end_dt" label="地税税务登记有效期" onblur="CheckLocExpDate(CusCom.loc_tax_reg_dt,CusCom.loc_tax_reg_end_dt,CusCom.loc_tax_ann_date);checkRegCode()" />
					<emp:date id="CusCom.loc_tax_ann_date" label="地税年检到期日" onblur="CheckLocEndDate(CusCom.loc_tax_reg_dt,CusCom.loc_tax_reg_end_dt,CusCom.loc_tax_ann_date);checkRegCode()" colSpan="2" />
					<emp:text id="CusCom.loc_tax_reg_org" label="地税登记机关" maxlength="80" onblur="checkRegCode()"/>
					<emp:text id="CusCom.nat_tax_reg_code" label="国税税务登记代码" maxlength="20" colSpan="2" onblur="checkRegCode()"/>
					<emp:date id="CusCom.nat_tax_reg_dt" label="国税税务登记日期" onblur="CheckDate(CusCom.nat_tax_reg_dt);checkRegCode()" />
					<emp:date id="CusCom.nat_tax_reg_end_dt" label="国税税务登记有效期" onblur="CheckLocExpDate(CusCom.nat_tax_reg_dt,CusCom.nat_tax_reg_end_dt,CusCom.nat_tax_ann_date);checkRegCode()" />
					<emp:date id="CusCom.nat_tax_ann_date" label="国税年检到期日" onblur="CheckLocEndDate(CusCom.nat_tax_reg_dt,CusCom.nat_tax_reg_end_dt,CusCom.nat_tax_ann_date);checkRegCode()" colSpan="2" />
					<emp:text id="CusCom.nat_tax_reg_org" label="国税登记机关" maxlength="80" onblur="checkRegCode()"/>
				</emp:gridLayout> 
				<emp:gridLayout id="" title="特种经营信息" maxColumn="2">
					<emp:select id="CusCom.com_sp_business" label="特种经营标识" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />
					<emp:text id="CusCom.com_sp_lic_no" label="特种经营许可证编号" maxlength="80" required="false" colSpan="2" />
					<emp:textarea id="CusCom.com_sp_detail" label="特种经营情况" maxlength="80" required="false" colSpan="2" />
					<emp:text id="CusCom.com_sp_lic_org" label="特种许可证颁发机关" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:date id="CusCom.com_sp_str_date" label="特种经营登记日期" required="true" onblur="CheckRegDate(CusCom.com_sp_str_date,CusCom.com_sp_end_date)" />
					<emp:date id="CusCom.com_sp_end_date" label="特种经营到期日期" required="true" onblur="CheckExpDate(CusCom.com_sp_str_date,CusCom.com_sp_end_date)" />
				</emp:gridLayout>
				<emp:gridLayout id="" title="与我行关系" maxColumn="2">
					<emp:select id="CusCom.is_ours_rela_cust" label="是否为我行关联客户" dictname="STD_ZB_OURS_RECUST" required="true" onchange="changeRelaCust()" colSpan="2" defvalue="03"/>
					<emp:select id="CusCom.cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusCom.cus_bank_rel_fake" label="与我行关联关系" defvalue="普通客户关系" readonly="true" cssElementClass="emp_field_select_select1" colSpan="2"/>
					<emp:select id="CusCom.bank_duty" label="在我行职务" required="false" dictname="STD_ZB_BANK_DUTY" hidden="true"/>
					<emp:text id="CusCom.equi_no" label="股权证号码" maxlength="30" required="false" dataType="Int" hidden="true"/>
					<emp:text id="CusCom.bank_equi_amt" label="拥有我行股份(元)" maxlength="18" required="false" dataType="Currency" hidden="true"/>
				</emp:gridLayout>
				<!-- /* modify by wangj 2015-05-20  中征码修改 begin*/ -->
				<emp:gridLayout id="" title="中征码信息" maxColumn="2">
					<emp:select id="CusBase.loan_card_flg" label="是否有中征码" required="true" dictname="STD_ZX_YES_NO" colSpan="2" onchange="changeCardFlg()" defvalue="2"/>
					<emp:text id="CusBase.loan_card_id" label="中征码" maxlength="16" required="false" onchange="cheakCardId();" hidden="true"/>
				<!-- /* modify by wangj 2015-05-20  中征码修改 end*/ -->
					<emp:text id="CusBase.loan_card_pwd" label="贷款卡密码" hidden="true" maxlength="6" required="false" />
					<emp:select id="CusBase.loan_card_eff_flg" label="贷款卡状态" hidden="true" required="false" dictname="STD_ZB_LOAN_CARD_FLG" />
					<emp:date id="CusBase.loan_card_audit_dt" label="贷款卡年检到期日" hidden="true" required="false" onblur="checkLoanCardAuditDt()"/>
				</emp:gridLayout>
				
				<emp:gridLayout id="" title="银行账户信息" maxColumn="2">
					<emp:select id="CusCom.bas_acc_flg" label="基本存款账户是否在本行" required="false" hidden="false" 
						colSpan="2" dictname="STD_ZX_YES_NO" onchange="licenceRequired('2');"/>
					<emp:text id="CusCom.bas_acc_licence" label="基本账户开户许可证核准号" hidden="true" required="false" onblur="checkBasAccLic();"/>
					<emp:text id="CusCom.bas_acc_no" label="基本存款账户帐号" maxlength="32" required="false" hidden="false" onblur="CheckBasAccNo();"/>
					<emp:date id="CusCom.bas_acc_date" label="基本账户开户日期 " required="false" hidden="false" onblur="CheckDate(CusCom.bas_acc_date)" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="CusCom.bas_acc_bank_displayname" label="基本存款账户开户行" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAaorgNo" required="false" hidden="false" buttonLabel="选择"/>
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="CusCom.bas_acc_bank" label="基本存款账户开户行" maxlength="80" required="false" hidden="true"/>
				</emp:gridLayout>
				</div>
			</emp:tab>
			<emp:tab id="cert_tab" label="联系信息" initial="true">
				<div>
				<emp:gridLayout id="" title="财务部信息" maxColumn="2">
					<emp:text id="CusCom.fna_mgr" label="财务部负责人" maxlength="35" required="false" />
					<emp:text id="CusCom.com_operator" label="财务部联系人" maxlength="35" required="false" />
					<emp:text id="CusCom.fina_per_tel" label="财务部联系人电话" maxlength="35" required="false" dataType="Phone" />
					<emp:text id="CusCom.fina_per_phone" label="财务部联系人手机" maxlength="35" required="true" dataType="Mobile" />
				</emp:gridLayout>		
				<emp:gridLayout id="" title="地址信息" maxColumn="2">	
					<emp:text id="CusCom.post_addr" label="省/直辖市/自治区" required="true" colSpan="2" hidden="true" />
					<emp:pop id="CusCom.post_addr_displayname" label="省/直辖市/自治区" colSpan="2" cssElementClass="emp_field_text_input2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnStreetPost" required="true"/>	
					<emp:text id="CusCom.street_post" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusCom.post_code" label="邮政编码" maxlength="6" required="true" colSpan="2" dataType="Postcode" />
					<emp:text id="CusCom.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone" />
					<emp:text id="CusCom.phone" label="联系手机" maxlength="20" required="false" dataType="Mobile" />
					<emp:text id="CusCom.fax_code" label="传真" maxlength="35" required="false" dataType="Phone" />
					<emp:text id="CusCom.email" label="Email" maxlength="80" required="false" dataType="Email" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.web_url" label="网址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
				</emp:gridLayout></div>
			</emp:tab>
			<emp:tab id="Cont_tab" label="概况信息" initial="true">
				<div>
				<emp:gridLayout id="CusComGroup" title="对公客户概况信息" maxColumn="2">
					<emp:select id="CusCom.com_mrk_flg" label="上市公司标志" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />						
					<emp:select id="CusCom.com_mrk_area" label="上市地" required="true" dictname="STD_ZX_LISTED" />
					<emp:text id="CusCom.com_stock_id" label="股票代码" maxlength="32" required="true" />
					<emp:select id="CusCom.gover_fin_ter" readonly="true" label="政府融资平台" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />
					<emp:select id="CusBase.belg_line" readonly="true" label="所属条线" required="true" dictname="STD_ZB_BUSILINE" colSpan="2" defvalue="01"/>	
					<emp:select id="CusCom.com_grp_mode" label="集团客户类型" required="true" defvalue="9" dictname="STD_ZB_GROUP_TYPE" colSpan="2" readonly="false" onchange="linkChangeComGrpMode();" />
					<emp:text id="CusCom.grp_no" label="所属集团号" maxlength="40"/>
					<emp:text id="CusCom.parent_cus_name" label="集团客户母公司名称" maxlength="80" hidden="true"/>
					<emp:text id="CusCom.parent_cert_code" label="母公司组织机构代码" maxlength="20" hidden="true" onchange="checkParentCertCode();" />
					<emp:text id="CusCom.parent_loan_card" label="母公司贷款卡编码" maxlength="16" hidden="true" onblur="CheckParentLoanCardId();"/>
					<emp:select id="CusCom.str_cus" readonly="true" label="战略性客户" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" onchange="changeLine()"/>	
					<emp:date id="CusCom.str_cus_end_dt" readonly="true" label="战略性客户到期日" required="false" onblur="CheckDate(CusCom.bas_acc_date)" hidden="true"/>	
					<emp:select id="CusCom.hou_exp" readonly="true" label="房地产开发商" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />	
					<emp:select id="CusCom.ie_flag" label="进出口权标志" required="true" dictname="STD_ZX_YES_NO" onchange="checkIeFlag()" colSpan="2"/>
					<emp:select id="CusCom.rel_flag" label="是否存在主要关联企业" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2"/>
					<emp:text id="CusCom.ie_con_code" label="进出口企业代码" required="flase" maxlength="20" hidden="true"/>
					<emp:textarea id="CusCom.com_main_product" label="主要产品情况" maxlength="250" required="false" colSpan="2" />
					<emp:textarea id="CusCom.com_prod_equip" label="主要生产设备" maxlength="250" required="false" colSpan="2" hidden="false" />
					<emp:textarea id="CusCom.com_fact_prod" label="实际生产能力" maxlength="250" required="false" colSpan="2" hidden="false" />
					<emp:text id="CusCom.com_opt_aera" label="经营场地面积(平方米)" maxlength="16" required="true" onblur="cheakAera()" />
					<emp:select id="CusCom.com_opt_owner" label="经营场地所有权" required="true" dictname="STD_ZX_FIELD_OWNER" />
					<emp:select id="CusCom.com_opt_st" label="经营状况" required="true" dictname="STD_ZB_BUSI_STATUS"/>
					<emp:select id="CusCom.com_imptt_flg" label="地区重点企业" required="true" dictname="STD_ZX_YES_NO" />
					<emp:select id="CusCom.com_prep_flg" label="优势企业" required="true" dictname="STD_ZX_YES_NO" />
					<emp:select id="CusCom.com_dhgh_flg" label="高环境风险高污染企业" required="true" dictname="STD_ZX_YES_NO" /><!-- 20150113 Edited by FCL 删除校验排污许可证(编号)和排污许可证到期日 -->
					<emp:select id="CusCom.com_cl_entp" label="国家宏观调控限控行业" required="true" dictname="STD_ZX_YES_NO" defvalue="2" colSpan="2"/>	
					<emp:text id="CusCom.row_lice" label="排污许可证(编号)" maxlength="25" required="false"/>	
					<emp:date id="CusCom.row_lice_end_dt" label="排污许可证到期日" required="false" hidden="false" onblur="CheckDate(CusCom.bas_acc_date)" />			
					<emp:select id="CusCom.com_hd_enterprise" label="龙头企业" required="true" dictname="STD_ZB_COM_HD_ENTER" colSpan="2" />	
					<emp:text id="CusCom.com_mng_org" label="上级主管单位" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:select id="CusCom.com_fin_rep_type" label="财务报表类型" required="true" colSpan="2" dictname="STD_ZB_FIN_REP_TYPE"  hidden="true" defvalue="PB0001" readonly="true"/>	
				</emp:gridLayout></div>
			</emp:tab>
			<emp:tab id="pro_tab" label="合作信息" initial="true">
				<div>
				<emp:gridLayout id="CusComGroup" title="合作信息" maxColumn="2">
					<emp:date id="CusCom.com_init_loan_date" label="建立信贷关系时间" required="true" readonly="false" defvalue="${context.OPENDAY}" colSpan="2" onblur="checkInitLoanDate()"/>
					<emp:select id="CusBase.cus_crd_grade" label="信用等级(内部)" required="false" dictname="STD_ZB_CREDIT_GRADE" defvalue="00" readonly="true" colSpan="2"/>
					<emp:select id="CusBase.guar_crd_grade" label="担保信用等级(内部)" required="false" dictname="STD_ZB_FINA_GRADE" defvalue="Z" hidden="true" readonly="true"/>		
					<emp:date id="CusBase.cus_crd_dt" label="信用评定到期日期" required="false" readonly="true" />
					<emp:select id="CusCom.com_out_crd_grade" label="信用等级(外部)" required="true" dictname="STD_ZB_CREDIT_GRADE" defvalue="00" colSpan="2" onchange="changeGrade()"/>
					<emp:select id="CusCom.guar_crd_grade2" label="担保信用等级(外部)" required="false" dictname="STD_ZB_FINA_GRADE" defvalue="Z" hidden="true" onchange="changeGrade()"/>
					<emp:date id="CusCom.com_out_crd_dt" label="评定日期(外部)" onblur="CheckDate(CusCom.com_out_crd_dt)" />
					<emp:date id="CusCom.com_coop_exp" label="合作有效期" required="false" hidden="true"/>	
					<emp:select id="CusCom.guar_cls" label="担保类别" required="false" dictname="STD_ZB_GUAR_TYPE" hidden="true" readonly="true" />	
					<emp:text id="CusCom.guar_bail_multiple" label="担保放大倍数" required="false" hidden="true" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>		
					<emp:text id="CusCom.com_out_crd_org" label="评定机构" maxlength="60" colSpan="2" />
					
				</emp:gridLayout>
				<emp:gridLayout id="cusManagerInfo" title="管户人">
					<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
					<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="false"/>
					<emp:text id="CusBase.input_id_displayname" label="登记人" required="false" readonly="true" />
					<emp:text id="CusBase.input_br_id_displayname" label="登记机构" required="false" readonly="true" />
					<emp:text id="CusBase.input_date" label="登记日期" maxlength="10" required="false" readonly="true" defvalue="$OPENDAY"/>
					<emp:text id="CusCom.cust_mgr_phone" label="主管客户经理手机" maxlength="20" required="true" dataType="Mobile"/>
					<emp:select id="CusBase.cus_status" label="客户状态" required="false" dictname="STD_ZB_CUS_STATUS" readonly="true"/>
					<emp:text id="CusBase.main_br_id" label="主管机构" required="false" hidden="true"/>
					<emp:text id="CusBase.cust_mgr" label="主管客户经理" required="false" hidden="true"/>
					<emp:text id="CusBase.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/> 
					<emp:text id="CusBase.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
				</emp:gridLayout>
				</div>
			</emp:tab>
        <emp:tab label="修改历史记录" id="modifyHistory" url="queryModifyHistoryList.do" initial="true" needFlush="true" reqParams="cus_id=${context.CusBase.cus_id}"></emp:tab>
		</emp:tabGroup>
		<div align="center"><br>
		<%if( "rep".equals(btnflag) ) { %>
			<emp:button id="suppCusCom" label="补录完成"/>
		<% }else if("temp".equals(btnflag)){%>
			<emp:button id="saveTempCusInfo" label="暂存" />
			<emp:button id="checkCusRelTemp" label="保存"/> 
		<% }else if("due".equals(btnflag)){%>
			<emp:button id="updateCusCom" label="保存" />
		<% }else if(oper != null && !oper.equals("infotree")){%>
			<emp:button id="return" label="返回"/>
		<% }%> 
		</div>
	</emp:form>
	</body>
	</html>
</emp:page>