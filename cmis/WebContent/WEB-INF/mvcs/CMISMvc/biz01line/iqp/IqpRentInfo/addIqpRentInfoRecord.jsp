<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
    //request = (HttpServletRequest) pageContext.getRequest();
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = request.getParameter("serno");
	context.put("serno",serno);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
.emp_field_long_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:665px;
};
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
    //票据到期日期onchange事件响应方法。
	function endDateChange(){
		var str_date=IqpRentInfo.start_date._getValue();
		var endDate=IqpRentInfo.end_date._getValue();
		if(endDate&&str_date){
			var flag = CheckDate1BeforeDate2(str_date,endDate);
			if(!flag){
				alert("【起始日期】要小于等于【到期日期】！");
				IqpRentInfo.end_date._setValue("");
				return false;
			}
	     }
	};
	function rent_amt_type(){
		var rent_amt_coll_type = IqpRentInfo.rent_amt_coll_type._getValue();
		if(rent_amt_coll_type == "01"){
			IqpRentInfo.rent_acctsvcr_no._setValue("");
			IqpRentInfo.rent_acctsvcrnm._setValue("");
			IqpRentInfo.rent_no._setValue("");
			IqpRentInfo.rent_name._setValue("");

			IqpRentInfo.rent_acctsvcr_no._obj._renderRequired(false);
			IqpRentInfo.rent_acctsvcrnm._obj._renderRequired(false);
			IqpRentInfo.rent_no._obj._renderRequired(false);
			IqpRentInfo.rent_name._obj._renderRequired(false);

			IqpRentInfo.rent_acctsvcr_no._obj._renderHidden(true);
			IqpRentInfo.rent_acctsvcrnm._obj._renderHidden(true);
			IqpRentInfo.rent_no._obj._renderHidden(true);
			IqpRentInfo.rent_name._obj._renderHidden(true);
		}else if(rent_amt_coll_type == "02"){
			IqpRentInfo.rent_acctsvcr_no._obj._renderRequired(true);
			IqpRentInfo.rent_acctsvcrnm._obj._renderRequired(true);
			IqpRentInfo.rent_no._obj._renderRequired(true);
			IqpRentInfo.rent_name._obj._renderRequired(true);

			IqpRentInfo.rent_acctsvcr_no._obj._renderHidden(false);
			IqpRentInfo.rent_acctsvcrnm._obj._renderHidden(false);
			IqpRentInfo.rent_no._obj._renderHidden(false);
			IqpRentInfo.rent_name._obj._renderHidden(false);
		}
	};		
	//计算出租总价
	function clcTotal(){
	    var price=IqpRentInfo.every_rent_amt._getValue();
	    var qnt=IqpRentInfo.rent_coll_term._getValue();
	    if(price!=null && price!="" &&qnt!=null &&qnt!=""){
		   	var qntp=parseInt(qnt);
		   	var amt=parseFloat(price);
		   	var total=parseFloat(qntp*amt).toFixed(2);
		   	IqpRentInfo.total_rent_amt._setValue(total);
	    }
	};	
	//校验证件号码
	function checkCertCode(){
		var certType =IqpRentInfo.lessee_cert_type._getValue();
		var certCode =IqpRentInfo.lessee_cert_no._getValue();
		if(certCode!=""&&certType=="a"){
			if(!CheckOrganFormat(certCode)){
				IqpRentInfo.lessee_cert_no._setValue("");
			}
		}
		if(certCode!=""&&certType=="20"){
			if(!CheckCertCodeFormat(certCode)){
				IqpRentInfo.lessee_cert_no._setValue("");
			}
		}
		if(certCode!=""&&(certType=="0"||certType=="7")){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if("100"==certType){ //身份证 
					var length = certCode.length;
					if (length != 15 && length != 18){
					    alert("身份证号码，长度必须为15位或18位！");
					    IqpRentInfo.lessee_cert_no._setValue("");
					    return false;
					}
				  	if (length == 15){//15位的身份证号
				    	var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
				    	var checkres = reg.test(value);
						if (!checkres) {
							alert("身份证号码格式不正确！");
							IqpRentInfo.lessee_cert_no._setValue("");
							return false;
						}
				    }else if (length == 18){
				        var reg = /^[1-9]\d{6}[1-9]\d{2}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9|x|X])$/;
				    	var checkres = reg.test(value);
						if (!checkres) {
							alert("身份证号码格式不正确！");
							IqpRentInfo.lessee_cert_no._setValue("");
							return false;
						}
				    }
				}
			}else{
				IqpRentInfo.lessee_cert_no._setValue("");
			}
		}
	};
	
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
	};
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpRentInfo._checkAll()){
			IqpRentInfo._toForm(form); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var message = jsonstr.message;
					if(flag == "success"){
						alert("新增成功!");
						var url = '<emp:url action="queryIqpRentInfoList.do"/>?op=update&serno='+'<%=serno%>'; 
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert(message);
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
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};		
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpRentInfoRecord.do" method="POST">
	 <emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:gridLayout id="IqpRentInfoGroup" title="出租信息" maxColumn="2">
			<emp:date id="IqpRentInfo.start_date" label="起始日期" required="true" onblur="endDateChange()"/>
			<emp:date id="IqpRentInfo.end_date" label="到期日期" required="true" onblur="endDateChange()"/>
			<emp:select id="IqpRentInfo.rent_coll_term_unit" label="租金收取期限单位" required="true" dictname="STD_ZX_RENT_UNIT" />
			<emp:text id="IqpRentInfo.rent_coll_term" label="租金收取期限" maxlength="38" required="true" dataType="Int" onblur="clcTotal()"/>
			<emp:text id="IqpRentInfo.every_rent_amt" label="每期租金" maxlength="16" required="true" dataType="Currency" onblur="clcTotal()"/>
			<emp:text id="IqpRentInfo.pld_amt" label="押金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpRentInfo.total_rent_amt" label="总租金" maxlength="16" required="true" readonly="true" dataType="Currency" onblur="clcTotal()"/>
			<emp:select id="IqpRentInfo.rent_amt_coll_type" label="租金收取方式" required="true" onchange="rent_amt_type()" dictname="STD_ZB_RENT_COLL_TYPE" colSpan="2"/>
			<emp:text id="IqpRentInfo.rent_acctsvcr_no" label="租金收取开户行行号" maxlength="40" required="false" hidden="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.rent_acctsvcrnm" label="租金收取开户行行名" maxlength="150" required="false" hidden="true"/>
			<emp:text id="IqpRentInfo.rent_no" label="租金收取账号" maxlength="40" required="false" hidden="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.rent_name" label="租金收取账户名" maxlength="150" required="false" hidden="true"/>
		</emp:gridLayout>
		<br>
		<emp:gridLayout id="IqpRentInfoGroup" title="承租人信息" maxColumn="2">
			<emp:text id="IqpRentInfo.lessee_name" label="承租人名称" maxlength="80" required="true" colSpan="2"/>
			<emp:text id="IqpRentInfo.lessee_mobile" label="承租人手机" maxlength="20" required="true" dataType="Mobile" cssElementClass="emp_field_input"/>
			<emp:text id="IqpRentInfo.lessee_phone" label="承租人电话" maxlength="20" required="true" dataType="Phone" cssElementClass="emp_field_input"/>
			<emp:select id="IqpRentInfo.lessee_cert_type" label="承租人证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="IqpRentInfo.lessee_cert_no" label="承租人证件号码" maxlength="20" required="true" onchange="checkCertCode()"/>
			<emp:text id="IqpRentInfo.lessee_addr" label="承租人地址" maxlength="150" required="true" colSpan="2" cssElementClass="emp_field_long_input"/>
			<emp:textarea id="IqpRentInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpRentInfo.rent_serno" label="出租编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpRentInfo.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>
		</emp:gridLayout>
	 </emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置 "/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

