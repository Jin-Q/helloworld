<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript">

	function getCusInfo(returnMethod,certTyp,certCode,operMethod){
		if(window[returnMethod])
		  eval(window[returnMethod]());
	  
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
				var cus_id = cusObj.cus_id;//客户码
				if (cus_id == null) {//modify by zhoujf 2009.07.15
					if (confirm('该证件号未在系统开户，点击【确定】继续开户，点击【取消】取消')) {
						var url = '';
						if (certTyp=='20'||certTyp=='a'||certTyp=='b'||certTyp=='X'){
							url = '<emp:url action="newCusComAddPage.do"/>?openTmp=true&CusBase.cert_code='+decodeURIComponent(certCode)+"&CusBase.cert_type="+certTyp;
						}else{
							url = '<emp:url action="newCusIndivAddPage.do"/>?openTmp=true&CusBase.cert_code='+decodeURIComponent(certCode)+'&CusBase.cert_type='+certTyp;
						}
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
					}
				} else {
					if(window[returnMethod])
					  eval(window[returnMethod](cusObj));
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var paramStr = "cert_type=" + certTyp + "&cert_code=" + certCode;
		var url = '<emp:url action="getCusJsonByCert.do"/>&' + paramStr;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
 	}

	//不宜贷款户用，传入客户名称
	function getCusBlkInfo(returnMethod,certTyp,certCode,cusName,operMethod){
		if(window[returnMethod])
		  eval(window[returnMethod]());
	  
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
				var cus_id = cusObj.cus_id;//客户码
				if (cus_id == null) {//modify by zhoujf 2009.07.15
					if (confirm('该证件号未在系统开户，点击【确定】继续开户，点击【取消】取消')) {
						var url = '';
						if (certTyp=='20'||certTyp=='a'||certTyp=='b'||certTyp=='X'){
							url = '<emp:url action="newCusComAddPage.do"/>&openTmp=true&CusBase.cert_code='+certCode+"&CusBase.cert_type="+certTyp+"&cusName="+cusName;
						}else{
							url = '<emp:url action="newCusIndivAddPage.do"/>&openTmp=true&CusBase.cert_code='+certCode+'&CusBase.cert_type='+certTyp+"&cusName="+cusName;
						}
						url = encodeURI(encodeURI(url));
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
					}
				} else {
					if(window[returnMethod])
					  eval(window[returnMethod](cusObj));
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var paramStr = "cert_type=" + certTyp + "&cert_code=" + certCode;
		var url = '<emp:url action="getCusJsonByCert.do"/>&' + paramStr;
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
 	}

	function getCusAccInfo(cusIdObj,operMethod,accObj){
	 	var handleSuccess = function(o) {
		 	//alert(o.responseText);
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusComAccList=jsonstr.CusComAccList;
					//alert(cusComAccList.length);
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}	
				//eval("cusComAccList=" + cusComAccList.CusComAccList);
				//alert("cusComAccList2="+cusComAccList);
				if (cusComAccList != null && cusComAccList.length > 0) {
					if(window[operMethod])
						  eval(window[operMethod](cusComAccList,accObj));
					//for(var i=0;i<cusList.length;i++){
	               //       cusObj=cusList[i]; 
					//	}
				} else {
					alert(cusId+" 客户信息中结算帐户登记簿为空！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var cusId;
		if(cusIdObj)
		   cusId=cusIdObj._getValue();
		var paramStr = "cusId=" + cusId;
		var url= '<emp:url action="queryCusAccListById.do"/>&' + paramStr;
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback)
	}
</script>