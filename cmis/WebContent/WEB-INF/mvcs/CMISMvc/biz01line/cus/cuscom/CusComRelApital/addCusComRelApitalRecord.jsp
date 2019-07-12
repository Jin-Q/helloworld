<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<meta http-equiv="Content-Type" content="text/HTML; charset=utf-8" /> 
<%
//String cert_code=request.getParameter("cert_code");
String editFlag = request.getParameter("EditFlag");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<style type="text/css">
.emp_field_text_input1 {width:450px;}
</style>
<script type="text/javascript">
	function doAddCusComRelApital(){
		var form = document.getElementById("submitForm");
	    var cus_id = CusComRelApital.cus_id._getValue();
	    var invt_amt =  CusComRelApital.invt_amt._getValue();
	    var cur_type = CusComRelApital.cur_type._getValue();
	    var cusIdRel = CusComRelApital.cus_id_rel._getValue();
		var flag = '';
		var message = "";
	
		var result = CusComRelApital._checkAll();
		if(result){
			 CusComRelApital._toForm(form)
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
			var subUrl = '<emp:url action="checkCusComRelApitalPerc.do"/>'+'&cus_id='+cus_id+'&cur_type='+cur_type+'&invt_amt='+invt_amt+'&cus_id_rel='+cusIdRel+'&op=add';//&opFlag=add
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
						var paramStr="CusComRelApital.cus_id="+CusComRelApital.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusComRelApitalAddPage.do"/>&'+paramStr;
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}

	function goback(){
		var paramStr="CusComRelApital.cus_id="+CusComRelApital.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusComRelApitalList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};
	
	function CheckDate(){
		var inv_date = CusComRelApital.inv_date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(inv_date!=null && inv_date!="" ){
			var flag = CheckDate1BeforeDate2(inv_date,openDay);
			if(!flag){
				alert("出资时间要小于等于当前日期！");
				CusComRelApital.inv_date._obj.element.value="";
			}
	    }
	};
	
	function doReturn(){
		goback();
	}

	//根据证件类型过滤出资人性质
	function checkInvtTyp(){
		var certYtp = CusComRelApital.cert_typ._getValue();
		if(certYtp == "20"||certYtp == "a"||certYtp == "b"||certYtp == "c"||certYtp == "X"){//证件类型为：组织机构代码、登记注册号
			var options = CusComRelApital.invt_typ._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
			    if(options[i].value=='7'){
					options.remove(i);
			    }
			}
			CusComRelApital.invt_typ._obj._renderReadonly(false);    	
		}else{
			var options = CusComRelApital.invt_typ._obj.element.options;
			var oOption = document.createElement("OPTION");
			oOption.text = '自然人';
		    oOption.value = '7';
			options.add(oOption);
			CusComRelApital.invt_typ._setValue('7');
			CusComRelApital.invt_typ._obj._renderReadonly(true);
		}
	}

	function addBtn(){
		CusComRelApital.cert_code._obj.addOneButton('uniquCheck','获 取', setCusInfo);
		CusComRelApital.cus_id_rel._obj.addOneButton('view12','查看',viewCusInfo);
		changeRel();
		//移除部分证件类型
		var options = CusComRelApital.cert_typ._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='c'){
	  			options.remove(i);
	   	    }
	    }
	}
	//移除部分关联企业类型
	function changeRel(){
		var options = CusComRelApital.rela_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value!='0' && options[i].value!='5'){
		    	options.remove(i);
			}
	    }
	}
	
	//查看客户信息
	function viewCusInfo(){
		var cus_id_rel = CusComRelApital.cus_id_rel._getValue();
		if(cus_id_rel==null||cus_id_rel==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id_rel;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}

	function setCusInfo(){
		var certTyp = CusComRelApital.cert_typ._obj.element.value;
		var certCode = CusComRelApital.cert_code._obj.element.value;
		if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
		    alert("证件类型证件号码不能为空！");
		    return ;
		}

		//校验组织机构代码
		if(certTyp=="a"){
			if(!CheckOrganFormat(certCode)){
				CusComRelApital.cert_code._obj.element.value="";
	         	return;
			}
		}
		if(certTyp=="20"){
			if(!CheckCertCodeFormat(certCode)){
				CusComRelApital.cert_code._obj.element.value="";
	         	return;
			}
		}
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusComRelApital.cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusComRelApital.cert_code._obj.element.value="";
					}
				}
			}else{
				CusComRelApital.cert_code._obj.element.value="";
				CusComRelApital.cert_code._obj.element.focus();
				return;
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
	
	//15位身份证转为18位
	function oldCardToNewCard(certCode){
		var vs = "10X98765432"; 
		var v = new Array(); 
		v.push(2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7); 
		var cardID17 = certCode.substring(0,6)+"19"+certCode.substring(6); 
	    var N = 0; 
	    var R = -1; 
	    var T = '0';//储存最后一个数字 
	    var j = 0; 
	    var cardID18=""; 
	    //计数出第18位数字 
	    for (var i = 16; i >= 0; i--) { 
            N += parseInt(cardID17.substring(i, i + 1)) * v[j]; 
            j++; 
	    } 
	    R = N % 11; 
	    T = vs.charAt(R); 
	    cardID18 = cardID17 + T; 
	    return cardID18;
	}
	
	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			CusComRelApital.cus_id_rel._setValue('');
			return;
		}
		
		var cus_name=cusObj.cus_name;
		if (cus_name != null) {
			CusComRelApital.invt_name._setValue(cus_name);
		}

		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusComRelApital.cus_id_rel._setValue(cus_id_rel);
		}
		var cus_id = CusComRelApital.cus_id._getValue();
		if(cus_id_rel == cus_id){
			alert("出资人不能为自己,获取失败！")
			CusComRelApital.cert_typ._setValue("");
			CusComRelApital.cert_code._setValue("");
			CusComRelApital.cus_id_rel._setValue("");
		}
		var com_inv_loan_card = cusObj.loan_card_id;
		
		if (com_inv_loan_card != null) {	
			CusComRelApital.loan_card._setValue(com_inv_loan_card);	
		}
	}

	function checkAmt(){
		var invt_amt = CusComRelApital.invt_amt._getValue();
		if(parseFloat(invt_amt)<=0){
			alert("【出资金额（万元）】必须大于零！");
			CusComRelApital.invt_amt._setValue("");
		}
	}

	function checkTypeRel(){
		CusComRelApital.invt_perc._setValue("");
	}
	
	function checkType(){
		var invt_perc = CusComRelApital.invt_perc._obj.element.value;
		invt_perc = percentFilter(invt_perc);
		var rela_type = CusComRelApital.rela_type._getValue();
		if(rela_type==5&&parseFloat(invt_perc)>5){
		alert("持股5%以下的股东对应的出资比例必须小于等于5%");
		CusComRelApital.invt_perc._setValue("");
		return;
		}else if(rela_type==0&&parseFloat(invt_perc)<=5){
			alert("持股5%以上以及银行认为重要的股东对应的出资比例要大于5%！");
			CusComRelApital.invt_perc._setValue("");
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
		return amtStrTmp;
	}
</script>
</head>
<body class="page_content" onload="addBtn()">

	<emp:form id="submitForm" action="addCusComRelApitalRecord.do" method="POST">

		<emp:gridLayout id="CusComRelApitalGroup" title="资本构成信息" maxColumn="2">
			<emp:select id="CusComRelApital.cert_typ" label="出资人证件类型" required="true" dictname="STD_ZB_CERT_TYP" onchange="checkInvtTyp()"  colSpan="2"/>
			<emp:text id="CusComRelApital.cert_code" label="出资人证件号码" maxlength="20" required="true"  colSpan="2"/>
			<emp:text id="CusComRelApital.cus_id_rel" label="出资人客户码" required="true" maxlength="30" readonly="true"/>
			<emp:text id="CusComRelApital.cus_id" label="客户码" maxlength="35" required="false" hidden="true" />
			<emp:text id="CusComRelApital.invt_name" label="出资人名称" maxlength="80" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusComRelApital.invt_typ" label="出资人性质" required="false" dictname="STD_ZB_INVESTOR2" hidden="true" colSpan="2"/>
			<emp:select id="CusComRelApital.rela_type" label="关联类型" required="true" dictname="STD_ZB_RELA_TYP" colSpan="2" cssElementClass="emp_field_text_input1" onblur="checkTypeRel()"/>
			<emp:text id="CusComRelApital.loan_card" label="出资人贷款卡编号" maxlength="16" required="false"   />
			<emp:select id="CusComRelApital.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="CusComRelApital.invt_amt" label="出资金额(万元)" maxlength="18" dataType="Currency" required="true" onblur="checkAmt()" />
			<emp:select id="CusComRelApital.invt_type" label="出资类型" required="true" dictname="STD_ZB_INVT_TYPE" />
			<emp:text id="CusComRelApital.invt_perc" label="出资比例" maxlength="10"  dataType="Percent" required="true" onblur="checkType()"/>
			<emp:textarea id="CusComRelApital.com_invt_desc" label="出资说明" maxlength="250" required="false" colSpan="2" />
			<emp:date id="CusComRelApital.inv_date" label="出资时间" required="true" onblur="CheckDate()"/>
			<emp:textarea id="CusComRelApital.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComRelApital.cus_name_rel" label="关联客户姓名" maxlength="80" required="false" readonly="false" hidden="true"/>
			<emp:text id="CusComRelApital.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComRelApital.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComRelApital.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComRelApital.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComRelApital.last_upd_date" label="更新日期" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusComRelApital" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>

</body>
</html>
</emp:page>

