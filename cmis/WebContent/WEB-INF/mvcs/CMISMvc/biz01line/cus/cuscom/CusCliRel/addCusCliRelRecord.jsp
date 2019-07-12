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
.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 330px;
}
</style>
<script type="text/javascript">
	
	function doAddCusCliRel(){
		var form = document.getElementById("submitForm");
		var result = CusCliRel._checkAll();

		if(result){
			CusCliRel._toForm(form);
			toSubmitForm(form);	
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
						var paramStr="CusCliRel.cus_id="+CusCliRel.cus_id._obj.element.value;
						var EditFlag  ='<%=EditFlag%>';
						var url = '<emp:url action="getCusCliRelAddPage.do"/>&'+paramStr+"&EditFlag="+EditFlag;
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
	
	function doReturn(){
		goback();
	}
    
	function goback(){
		var paramStr="CusCliRel.cus_id="+CusCliRel.cus_id._getValue()+"&EditFlag=<%=EditFlag%>";
		var stockURL = '<emp:url action="queryCusCliRelList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function addBtn(){
		CusCliRel.cert_code._obj.addOneButton('uniquCheck','获 取', setCusInfo);
		//移除部分证件类型
		changeRel();
		var options = CusCliRel.cert_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value!='a' && options[i].value!='b' && options[i].value!='X'){
		    	options.remove(i);
			}
	    }
	}
	
	function setCusInfo(){
		var certTyp = CusCliRel.cert_type._getValue();
		var certCode = CusCliRel.cert_code._obj.element.value;
		if(certCode==null||certCode=='') {
		    alert("证件号码不能为空！");
		    return ;
		}else if(certTyp=='a'){
			if(!CheckOrganFormat(certCode)){
				CusCliRel.cert_code._obj.element.value="";
	         	return false;
			}
		}else if(certTyp=='20'){
			if(!CheckCertCodeFormat(certCode)){
				CusCliRel.cert_code._obj.element.value="";
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
	
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			CusCliRel.cus_id_rel._setValue('');
			return;
		}
		var cus_name=cusObj.cus_name;
		if (cus_name != null) {
			CusCliRel.cus_name_rel._setValue(cus_name);
		}
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusCliRel.cus_id_rel._setValue(cus_id_rel);
		}
		var cus_id = CusCliRel.cus_name_rel._getValue();
		if(cus_id_rel == cus_id){
			alert("不可以给自己投资,获取失败！");
			CusCliRel.cus_id_rel._setValue("");
			CusCliRel.cus_name_rel._setValue("");
			return;
		}
		}
	
	function doClean() {
		CusCliRel.cus_id_rel._setValue("");
		CusCliRel.cus_name_rel._setValue("");
	}


	function changeRel(){
		var options = CusCliRel.cus_type_rel._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value=='0' || options[i].value=='5'){
		    	options.remove(i);
			}
	    }
	}
	/*oujj code*/

</script>
</head>
<body class="page_content" onload="addBtn()">
	<emp:form id="submitForm" action="addCusCliRelRecord.do" method="POST">
		<emp:gridLayout id="CusCliRelGroup" title="关联企业客户信息" maxColumn="2">
			
			<emp:select id="CusCliRel.cert_type" label="证件类型" required="true" colSpan="2" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusCliRel.cert_code" label="证件号码" required="true" colSpan="2"  onchange="doClean()"/>
			<emp:text id="CusCliRel.cus_id_rel" label="关联客户客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusCliRel.cus_name_rel" label="关联客户名称" maxlength="80" required="true" readonly="true" />
			<emp:select id="CusCliRel.cus_type_rel" label="关联客户类型" required="true" dictname="STD_ZB_RELA_TYP" cssElementClass="emp_field_text_readonly" />
			
			<emp:text id="CusCliRel.cus_id" label="客户码" maxlength="30" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusCliRel" label="保存" />
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>