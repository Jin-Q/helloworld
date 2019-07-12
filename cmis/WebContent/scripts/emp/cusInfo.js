
var contextPath = "/cmis-main";

 function getCusInfo(returnMethod,certTyp,certCode,operMethod,confirmMsg){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						for(var i=0;i<cusList.length;i++){
	                      cusObj=cusList[i];
						}
						cusObj=cusList[0];
					} else {
						alert("dddsfds到处都是");
					}

				} catch (e) {
					 
					 var errMsg=o.responseText;
                  var err=errMsg.split('<LABEL id="errorDetialMsg" style="display: none">');
                  alert((err[1].split('</LABEL>'))[0]);	
					 alert("Parse jsonstr define error!" + e);
					return;
				}
				 
				//
				var cus_id = cusObj.cus_id;
				if (cus_id == null) {//modify by zhoujf 2009.07.15
					if (confirm(confirmMsg)) {
						var url = '<emp:url action="getTmpCusIndivRecordaddPage.do"/>?certType='+certTyp+'&certCode='+certCode+'&managerAdd=manager&operMethod='+operMethod;
						url = EMPTools.encodeURI(url);
						window.open(url,'addwindow','height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
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
		var url='';
		 
		  url = '<emp:url action="getCusJsonByCert.do"/>&' + paramStr;
		 
		  
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback)
 }
 