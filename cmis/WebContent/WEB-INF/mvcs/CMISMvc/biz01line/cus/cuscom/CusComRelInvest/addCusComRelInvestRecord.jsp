<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String cert_code=request.getParameter("cert_code");
	String EditFlag=request.getParameter("EditFlag");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_onerow {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:395px;
}
.emp_field_text_onepop {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:365px;
}
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	
	function doAddCusComRelInvest(){
		var form = document.getElementById("submitForm");
	    var cus_id = CusComRelInvest.cus_id._getValue();//投资人
	    var invt_amt = CusComRelInvest.com_inv_amt._getValue();//投资金额
	    var cusIdRel = CusComRelInvest.cus_id_rel._getValue();//被投资人
	    var cur_type =CusComRelInvest.com_inv_cur_typ._getValue();//币种
		var flag = '';
		
		var result = CusComRelInvest._checkAll();
		if(result){
			CusComRelInvest._toForm(form);
			 var handleSuccess = function(o){
		    	 try {
					var jsonstr = eval("("+o.responseText+")");
					if(o.responseText !== undefined) {
						flag = jsonstr.flag;
						if(flag != "可以新增"){
							alert(flag);
							return;
						}else{
							toSubmitForm(form);	
						}
					}
				 } catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				 }
			};
			var handleFailure = function(o){
				return;
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var subUrl = '<emp:url action="checkCusComRelInvestPerc.do"/>'+'&cus_id='+cus_id+'&invt_amt='+invt_amt+'&cur_type='+cur_type+'&cus_id_rel='+cusIdRel+'&op=add';
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',subUrl, callback);		
		}
	}

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
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusComRelInvest.cus_id="+CusComRelInvest.cus_id._obj.element.value;
						var EditFlag  ='<%=EditFlag%>';
						var url = '<emp:url action="getCusComRelInvestAddPage.do"/>&'+paramStr+"&EditFlag="+EditFlag;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
				}else {
				 alert(flag);
				 return;
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
		var subUrl = '<emp:url action="addCusComRelInvestRecord.do"/>';
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',subUrl, callback,postData);
	}

	function goback(){
		var paramStr="CusComRelInvest.cus_id="+CusComRelInvest.cus_id._obj.element.value;
		var EditFlag  ='<%=EditFlag%>';
		var stockURL = '<emp:url action="queryCusComRelInvestList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function CheckDate(){
		var com_inv_dt = CusComRelInvest.com_inv_dt._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(com_inv_dt!=null && com_inv_dt!="" ){
			var flag = CheckDate1BeforeDate2(com_inv_dt,openDay);
			if(!flag){
				alert("发生时间要小于等于当前日期！");
				CusComRelInvest.com_inv_dt._obj.element.value="";
			}
		}
	};
	
	function doReturn(){
		goback();
	}
	
	function CheakCode(code){
		var cardCord = code._obj.element.value;
		if(cardCord!=""){
			if(!CheckOrganFormat(cardCord)){
				code._obj.element.value = "";
			}else{
				var cusCode="<%=cert_code%>";
				if(cardCord!=cusCode){
					toGetCusCom(cardCord);
					return;
				}else{
				    alert(" 投资的组织机构代码 不能和原客户的重复");
				    code._obj.element.value="";
				}
			}
		}
	}
	
	function addBtn(){
		CusComRelInvest.cert_code._obj.addOneButton('uniquCheck','获 取', setCusInfo);
		CusComRelInvest.cus_id_rel._obj.addOneButton('view12','查看',viewCusInfo);
		//移除部分证件类型
		var options = CusComRelInvest.cert_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value!='a' && options[i].value!='b' && options[i].value!='X'){
		    	options.remove(i);
			}
	    }
	}

	//查看客户信息
	function viewCusInfo(){
		var cus_id_rel = CusComRelInvest.cus_id_rel._getValue();
		if(cus_id_rel==null||cus_id_rel==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id_rel;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	
	function setCusInfo(){
		var certTyp = CusComRelInvest.cert_type._getValue();
		var certCode = CusComRelInvest.cert_code._obj.element.value;
		if(certCode==null||certCode=='') {
		    alert("证件号码不能为空！");
		    return ;
		}else if(certTyp=='a'){
			if(!CheckOrganFormat(certCode)){
				CusComRelInvest.cert_code._obj.element.value="";
	         	return false;
			}
		}else if(certTyp=='20'){
			if(!CheckCertCodeFormat(certCode)){
				CusComRelInvest.cert_code._obj.element.value="";
	         	return false;
			}
		}
		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
		getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
	}

	//校验统一社会信用代码输入是否正确
	function CheckCertCodeFormat(Code) { 
	　　var patrn = /^[0-9A-Z]+$/;
	 　	//18位校验及大写校验
	　　if ((Code.length != 18) || (patrn.test(Code) == false)) {
	　　　　 alert("不是有效的统一社会信用代码！"); 
			return false;
	　　} else { 
	　　　	var Ancode;//统一社会信用代码的每一个值
	 　　　	var Ancodevalue;//统一社会信用代码每一个值的权重 
	　　　　	var total = 0; 
	　　　　	var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子 
	　　　　	var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
	　　　　	//不用I、O、S、V、Z 
	　　　　	for (var i = 0; i < Code.length - 1; i++) {
	 　　　		Ancode = Code.substring(i, i + 1); 
	　　　　		Ancodevalue = str.indexOf(Ancode); 
	　　　　		total = total + Ancodevalue * weightedfactors[i];
	　　　　		//权重与加权因子相乘之和 
	　　　　	}
	 　　　 	var logiccheckcode = 31 - total % 31;
	　　　　	if (logiccheckcode == 31){
	　　　　　　logiccheckcode = 0;
	　　　　	}
	　　　　	var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
	　　　　	var Array_Str = Str.split(',');
	　　　　	logiccheckcode = Array_Str[logiccheckcode];
	　　　　 var checkcode = Code.substring(17, 18);
	　　　　 if (logiccheckcode != checkcode) {
	　　　　　　	alert("不是有效的统一社会信用代码！"); 
				return false;
	 　　　　}
	 		return true;
	 　　} 
	}
	
	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			CusComRelInvest.cus_id_rel._setValue('');
			return;
		}
		var cus_name=cusObj.cus_name;
		if (cus_name != null) {
			CusComRelInvest.com_inv_name._setValue(cus_name);
		}
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusComRelInvest.cus_id_rel._setValue(cus_id_rel);
		}
		var cus_id = CusComRelInvest.cus_id._getValue();
		if(cus_id_rel == cus_id){
			alert("不可以给自己投资,获取失败！");
			CusComRelInvest.com_inv_typ._setValue("");
			CusComRelInvest.cert_code._setValue("");
			CusComRelInvest.cus_id_rel._setValue("");
			CusComRelInvest.com_inv_name._setValue("");
			return;
		}
		//贷款卡号
		var com_inv_loan_card = cusObj.loan_card_id;
		if (com_inv_loan_card != null) {
			CusComRelInvest.com_inv_loan_card._setValue(com_inv_loan_card);	
		}
	//	getCusDetails();
	}

/*	function getCusDetails() {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
						returnCusCom(cusObj);
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
			}
		}
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var cus_id_rel = CusComRelInvest.cus_id_rel._getValue();
		var url = '<emp:url action="getCusCom4manager.do"/>?cus_id_rel='+cus_id_rel;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}

	function returnCusCom(cusObj){
		//注册登记号
		var reg_code = cusObj.reg_code;
		if (reg_code != null) {
			CusComRelInvest.reg_code._setValue(reg_code);
		}
	}*/
	
	function doClean() {
		CusComRelInvest.com_inv_name._setValue("");
		CusComRelInvest.reg_code._setValue("");
		CusComRelInvest.cus_id_rel._setValue("");
		CusComRelInvest.com_inv_loan_card._setValue("");
	}
	
	/*oujj code*/
	function doCheck() {
		var com_inv_perc = CusComRelInvest.com_inv_perc._obj.element.value;
	    var cus_id = CusComRelInvest.cus_id_rel._getValue();
	    com_inv_perc = percentFilter(com_inv_perc);
	    if(com_inv_perc>=0&com_inv_perc<=100){
			checkPerc(com_inv_perc,cus_id);
		}else{
			alert("比例输入不合法");
			CusComRelInvest.com_inv_perc._setValue("");
			return;
		}
	}
	
	function percentFilter(amt) {
		if (amt == null) {
			return null;
		}
		var amtStr = amt;
		var amtStrTmp = amt;
		while (amtStrTmp.indexOf("%") != -1) {
			amtStr = amtStr.replace("%", "");
			amtStrTmp=amtStr;
		}
		return amtStr;
	}
	//校验投资时间
	function checkInvDt(){
		var toDay = '${context.OPENDAY}';
		var invDt = CusComRelInvest.com_inv_dt._getValue();
		if(invDt>toDay){
			alert('投资时间不能大于当前日期！');
			CusComRelInvest.com_inv_dt._setValue('');
		}
	}
	//校验金额是否为负数
	function checkAmt(){
		var Amt = CusComRelInvest.com_inv_amt._getValue();
		if(parseFloat(Amt)<=0){
			alert("【投资金额(万元)】需大于零！");
			CusComRelInvest.com_inv_amt._setValue("");
		}
	}

</script>
</head>
<body class="page_content" onload="addBtn()">
	<emp:form id="submitForm" action="addCusComRelInvestRecord.do" method="POST">
		<emp:gridLayout id="CusComRelInvestGroup" title="对外投资信息" maxColumn="2">
			<emp:select id="CusComRelInvest.com_inv_typ" label="投资性质"  required="true"  dictname="STD_ZB_INVT_NATURE" colSpan="2"/>
			<emp:select id="CusComRelInvest.cert_type" label="证件类型" required="true" colSpan="2" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusComRelInvest.cert_code" label="证件号码" required="true" colSpan="2"  onchange="doClean();"/>
			<emp:text id="CusComRelInvest.cus_id_rel" label="被投资人客户码" maxlength="30" required="true" readonly="true"  />
			<emp:text id="CusComRelInvest.reg_code" label="登记注册号" maxlength="30" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusComRelInvest.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusComRelInvest.com_inv_name" label="被投资企业名称(全称)" maxlength="80" required="true" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusComRelInvest.com_inv_loan_card" label="贷款卡号" maxlength="16" required="false" readonly="true" colSpan="2"/>
			<emp:select id="CusComRelInvest.com_inv_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="CusComRelInvest.com_inv_amt" label="投资金额(万元)" maxlength="18" required="true"  dataType="Currency" onblur="checkAmt()"/>
			<emp:select id="CusComRelInvest.com_inv_app" label="出资方式" required="true" dictname="STD_ZB_INVT_TYPE" />
			<emp:date id="CusComRelInvest.com_inv_dt" label="投资时间" required="true" onblur="checkInvDt()"/>
			<emp:text id="CusComRelInvest.com_inv_perc" label="所占比例" maxlength="10"  dataType="Percent" required="true"/>
			<emp:textarea id="CusComRelInvest.com_inv_desc" label="投资说明" maxlength="200" required="true" colSpan="2" />
			<emp:textarea id="CusComRelInvest.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComRelInvest.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComRelInvest.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComRelInvest.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComRelInvest.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusComRelInvest.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusComRelInvest" label="保存" />
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>