<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doAddCusCertInfo(){
		var form = document.getElementById("submitForm");
		var result = CusCertInfo._checkAll();
		if(result){
			CusCertInfo._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
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
						var paramStr="CusCertInfo.cus_id="+CusCertInfo.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusCertInfoAddPage.do"/>&'+paramStr;
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
		var paramStr="CusCertInfo.cus_id="+CusCertInfo.cus_id._obj.element.value;
		var stockURL = '<emp:url action="queryCusCertInfoList.do"/>&'+paramStr+"&EditFlag=<%=editFlag%>";
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	//输入日期不能大于当前日期
	function CheckDate(obj,errMsg){
		var str_date=obj._getValue();
		var openDay = '${context.OPENDAY}';
		if( str_date==""|| str_date==null){
	        return;
		}
		if(str_date>openDay){
			alert(errMsg);
		    obj._obj.element.value="";
		}
	}
	CusCertInfo.input_date='${context.OPENDAY}';
	
	function doReturn(){
		goback();
	}

	function checkSignDate(){
		//	var openDay = '${context.OPENDAY}';
			var initDate = CusCertInfo.crt_date._getValue();
			var endDate = CusCertInfo.crt_end_date._getValue();
		
			if(initDate!=null&&initDate!=''&&endDate!=null&&endDate!=''){
				if(initDate>endDate){
					alert('签发开始日期不能大于签发到期日期！');
					CusCertInfo.crt_date._setValue('');
					CusCertInfo.crt_end_date._setValue('');
				}
			}
		}

	function setCusInfo(){
		var certTyp = CusCertInfo.cert_typ._obj.element.value;
		var certCode = CusCertInfo.cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            alert("证件类型或证件号码不能为空！");
            return ;
        }
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusCertInfo.cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusCertInfo.cert_code._obj.element.value="";
					}
				}
			}else{
				CusCertInfo.cert_code._obj.element.value="";
				CusCertInfo.cert_code._obj.element.focus();
				return;
			}
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
		for (var i = 16; i >= 0; i--){
	        N += parseInt(cardID17.substring(i, i + 1)) * v[j];
	        j++;
		}
		R = N % 11;
		T = vs.charAt(R);
		cardID18 = cardID17 + T;
		return cardID18;
	}
	
	/*--user code begin--*/
	
	
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="addCusCertInfoRecord.do" method="POST" >
		<emp:gridLayout id="CusCertInfoGroup" title="其他证件信息" maxColumn="2">
		    <emp:text id="CusCertInfo.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
		    <emp:select id="CusCertInfo.cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" colSpan="2"/>
			<emp:text id="CusCertInfo.cert_code" label="证件号码" required="true" colSpan="2" onblur="setCusInfo()"/>
		    <emp:date id="CusCertInfo.crt_date" label="签发日期" required="true" />
		    <emp:date id="CusCertInfo.crt_end_date" label="签发到期日期" required="true" onblur="checkSignDate()"/>
		    <emp:text id="CusCertInfo.input_cli" label="登记人" required="true" dictname="STD_ZB_CERT_TYP" defvalue="$currentUserId" readonly="true"/>
		    <emp:text id="CusCertInfo.input_org" label="登记机构" required="true" defvalue="$organNo" readonly="true"/>
		    <emp:date id="CusCertInfo.input_date" label="登记日期 " required="true"  defvalue="$OPENDAY" readonly="true"/>
		   
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusCertInfo" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>