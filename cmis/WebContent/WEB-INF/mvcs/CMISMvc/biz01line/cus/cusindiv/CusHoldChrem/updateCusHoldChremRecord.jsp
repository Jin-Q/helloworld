<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>持有理财信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function CheckRegDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(start,openDay);
			if(!flag){
				alert("您输入的日期应小于当前日期！");
				date1._obj.element.value="";
			}else{
				if(end!=null && end!=""){
					var ff = CheckDate1BeforeDate2(end,start);
					if(ff){
						alert("到期日期不小于登记日期！");
						date2._obj.element.value="";
					}
				}
		    }
		}
	}

	//返回列表页面
  	function doReturn(){
  		var editFlag = '${context.EditFlag}';
  		var paramStr="CusHoldChrem.cus_id="+CusHoldChrem.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusHoldChremList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}

  	//保存修改
  	function doUpdateHoldChrem(){
		var form = document.getElementById("submitForm");
		var result = CusHoldChrem._checkAll();
		if(result){
			CusHoldChrem._toForm(form)
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
				if(flag=="success"){
					alert("修改成功");
					doReturn();
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

  	function CheckSubDate(date){
		var start = date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(start,openDay);
			if(!flag){
				alert("您输入的日期应小于当前日期！");
				date._obj.element.value="";
			}
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusHoldChremRecord.do" method="POST">
		<emp:gridLayout id="CusHoldChremGroup" title="理财基本信息" maxColumn="2">
			<emp:text id="CusHoldChrem.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusHoldChrem.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
			<emp:text id="CusHoldChrem.prod_name" label="产品名称" maxlength="80" required="true" />
			<emp:select id="CusHoldChrem.chrem_type" label="理财类型" required="false" dictname="STD_CHREM_TYPE" defvalue="000"/>
			<emp:date id="CusHoldChrem.subscr_date" label="认购时间" required="true" onblur="CheckSubDate(CusHoldChrem.subscr_date);"/>
			<emp:text id="CusHoldChrem.subscr_amt" label="认购金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="CusHoldChrem.start_date" label="开始时间"  required="true" onblur="CheckRegDate(CusHoldChrem.start_date,CusHoldChrem.end_date);"/>
			<emp:date id="CusHoldChrem.end_date" label="到期时间" required="true" onblur="CheckRegDate(CusHoldChrem.start_date,CusHoldChrem.end_date);"/>
			<emp:text id="CusHoldChrem.expect_income_rate" label="预期收益率" maxlength="8" required="true" dataType="Percent"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusHoldChremGroup" title="登记信息" maxColumn="2">
		<emp:text id="CusHoldChrem.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserId"/>
			<emp:text id="CusHoldChrem.input_br_id_displayname" label="登记机构"  required="false" readonly="true" defvalue="$organNo"/>
			<emp:text id="CusHoldChrem.input_date" label="登记日期" maxlength="10" required="false" readonly="true" defvalue="$OPENDAY"/>
			<emp:text id="CusHoldChrem.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusHoldChrem.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusHoldChrem.serno" label="流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateHoldChrem" label="保 存"/>
			<emp:button id="reset" label="重 置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>