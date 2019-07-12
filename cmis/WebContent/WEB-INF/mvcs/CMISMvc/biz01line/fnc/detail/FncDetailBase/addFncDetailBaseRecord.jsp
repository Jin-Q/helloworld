<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
function doAddFncDetailBase(){
		//var now=new Date();
		//var year=now.getFullYear();
		//var month=now.getMonth()+1;
		//var date=now.getDate();


		//FncDetailBase.input_date._obj.element.value = year+'-'+month+'-'+date+'';

		
		var form = document.getElementById("submitForm");
		var result = FncDetailBase._checkAll();
		if(result){
			FncDetailBase._toForm(form)
			
			toSubmitForm(form);
		}else alert("请输入必填项！");
}




function toSubmitForm(form){
	  var handleSuccess = function(o){ EMPTools.unmask();
			if(o.responseText !== undefined) {
						try {
							//alert(o.responseText);
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						var pk = jsonstr.pk_value;
						var cus_id = jsonstr.cus_id;

						if(flag=="update"){
							if(confirm("该时点的报表信息已存在！点【确定】将维护该时点报表信息！")){
								var paramStr="pk="+pk;
								var url = '<emp:url action="getFncDetailBaseUpdatePage.do"/>?'+paramStr;
								url = EMPTools.encodeURI(url);
								
								window.location = url;
						     }else goback();
					    }else{
					    	var paramCusid="FncDetailBase.cus_id="+cus_id;
					    	//var paramStr="pk="+pk;
					    	var url = '<emp:url action="queryFncDetailBaseList.do"/>?'+paramCusid;
					    	url = EMPTools.encodeURI(url);
							
							window.location = url;
						}
			}
		};
		var handleFailure = function(o){ EMPTools.unmask();	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
};



function doReturn(){
    var paramStr="FncDetailBase.cus_id="+FncDetailBase.cus_id._obj.element.value;
	var url = '<emp:url action="queryFncDetailBaseList.do"/>&'+paramStr;
	url = EMPTools.encodeURI(url);
	window.location = url;		
}
String.prototype.trim = function()
{
		return this.replace(/(^\s*)|(\s*$)/g, "");
}
function checkYM(obj){
	  var re = new RegExp(/\d{4}((0[1-9])|(1[0-2]))/); 
	  var openDay="${context.OPENDAY}";
	
	  yyyymm=openDay.substring(0,4)+openDay.substring(5,7);
;
     if (re.test(obj.value)==false&&obj.value.trim().length>0){
		alert("您输入的年月有误,输入格式应该为YYYYMM！");
		this.value="";
		return obj.focus();
     }
     if (obj.value>yyyymm){
			alert("您输入的年月不能大于当前年月！");
			this.value="";
			return obj.focus();
	     }
     
};
	/*--user code end--*/

</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncDetailBaseRecord.do" method="POST">
		<emp:gridLayout id="FncDetailBaseGroup" title="报表明细基表" maxColumn="2">
			<emp:text id="FncDetailBase.pk" label="PK" hidden="true" maxlength="40" readonly="true" required="false" />
			<emp:text id="FncDetailBase.cus_id" label="客户码" readonly="true" maxlength="30" required="true" />
			<emp:text id="FncDetailBase.fnc_ym" label="年月" maxlength="6" required="true" onblur="checkYM(this)"/>
			<emp:text id="FncDetailBase.input_id" label="登记人" readonly="true" maxlength="20" defvalue="$currentUserId" required="false" />
			<emp:text id="FncDetailBase.input_br_id" label="登记机构" readonly="true" maxlength="20" defvalue="$organNo" required="false" />
			<emp:date id="FncDetailBase.input_date" label="登记日期" readonly="true" required="false" defvalue="$OPENDAY"/>
			<emp:text id="FncDetailBase.last_upd_id" label="更新人" hidden="true" readonly="true" maxlength="20" required="false" />
			<emp:date id="FncDetailBase.last_upd_date" label="更新日期" hidden="true" readonly="true" required="false" />
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="addFncDetailBase" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
