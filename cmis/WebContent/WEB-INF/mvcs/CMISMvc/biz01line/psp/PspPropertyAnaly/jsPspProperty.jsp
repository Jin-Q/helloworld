<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
.emp_field_text_input1 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
//-----------------------公共JS----------------------------
/*	function doLoad(){
		PspPropertyAnaly.owner_displayname._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//查看客户信息
	function viewCusInfo(){
		var cus_id = PspPropertyAnaly.owner._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}*/

	//校验证件号码
	function checkCertCode(){
		var certType =PspPropertyAnaly.owner_cert_type._getValue();
		var certCode =PspPropertyAnaly.owner_cert_code._getValue();
		if(certCode!=""&&certType=="a"){
			if(!CheckOrganFormat(certCode)){
				PspPropertyAnaly.owner_cert_code._setValue('');
			}
		}
		if(certCode!=""&&certType=="20"){
			if(!CheckCertCodeFormat(certCode)){
				PspPropertyAnaly.owner_cert_code._setValue('');
			}
		}
		if(certCode!=""&&(certType=='0'||certType=='7')){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(!flg){
				PspPropertyAnaly.owner_cert_code._setValue('');
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
	
</script>