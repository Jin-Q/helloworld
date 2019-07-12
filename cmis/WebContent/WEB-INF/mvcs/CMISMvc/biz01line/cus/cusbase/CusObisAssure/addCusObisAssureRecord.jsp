<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doOnLoad() {
		removeGtyTyp();
	}
	function doAddCusObisAssure(){
		var form = document.getElementById("submitForm");
		var result = CusObisAssure._checkAll();
		if(result){
			CusObisAssure._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
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
						var paramStr="CusObisAssure.cus_id="+CusObisAssure.cus_id._obj.element.value;
						var EditFlag  ='${context.EditFlag}';
						var url = '<emp:url action="getCusObisAssureAddPage.do"/>&'+paramStr+"&EditFlag="+EditFlag;
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	  }
	function goback(){
		var paramStr="CusObisAssure.cus_id="+CusObisAssure.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusObisAssureList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function checkStrData(){
		var strDt = CusObisAssure.gty_str_dt._getValue();
		var endDt = CusObisAssure.gty_end_dt._getValue();
		var openDay = '${context.OPENDAY}';
		if(strDt!=null && strDt!="" ){
			if(strDt>openDay){
				alert("起始日期不能大于当前日期！");
				CusObisAssure.gty_str_dt._setValue("");
				return;
			}
			if(endDt!=null && endDt!="" ){
				if(strDt>=endDt){
					alert("起始日期要小于到期日期！");
					CusObisAssure.gty_end_dt._setValue("");
				}
			}
		}
	}	

	function doReturn(){
		goback();
	}

	/*--user code begin--*/
			
	/*--user code end--*/
	function cheakAmt(amt){
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<0){
		  alert("金额值不能为负数！");
		  amt._obj.element.value="";
	   }
   }

	function removeGtyTyp(){
		var options = CusObisAssure.gty_typ._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '00'){
				options.remove(i);
			}
		}
	}

	function cheakGtyAmt(amt){
	    //担保金额
		var gtyAmt=CusObisAssure.gty_amt._getValue();
		//担保余额
		var gtyBlc=CusObisAssure.gty_blc._getValue();
		if(gtyAmt==""||gtyAmt==null){
			return;
		}
		if(gtyBlc==""||gtyBlc==null){
			gtyBlc=0;
		}
		gtyAmt = parseFloat(gtyAmt);
		gtyBlc = parseFloat(gtyBlc);
		if(isNaN(gtyAmt)){
			alert("担保金额输入有误！");
			CusObisAssure.gty_amt._obj.element.value="";
			return ;
		}
		if(gtyAmt<=0){
			alert("[担保金额]应大于零！");
			CusObisAssure.gty_amt._obj.element.value="";
			return ;
		}
		if(isNaN(gtyBlc)){
			alert("担保余额输入有误！");
			CusObisAssure.gty_blc._obj.element.value="";
			return ;
		}
		if(gtyBlc<0){
			alert("金额不能为负数！");
			CusObisAssure.gty_blc._obj.element.value="";
			return ;
		}
		if(gtyAmt<gtyBlc){
               alert("[担保金额]不能小于[担保余额] ");
               amt._obj.element.value="";
		}
	}
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addCusObisAssureRecord.do" method="POST">
		
		<emp:gridLayout id="CusObisAssureGroup" title="他行交易－他行担保" maxColumn="2">
			<emp:text id="CusObisAssure.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusObisAssure.seq" label="序号" maxlength="38" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusObisAssure.warrantee_name" label="被担保人名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisAssure.gty_typ" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" colSpan="2"/>
			<emp:text id="CusObisAssure.gty_amt" label="担保金额(元)" maxlength="18" required="true" dataType="Currency"  onblur="cheakGtyAmt(CusObisAssure.gty_amt)"/>
			<emp:text id="CusObisAssure.gty_blc" label="担保余额(元)" maxlength="18" required="true" dataType="Currency"  onblur="cheakGtyAmt(CusObisAssure.gty_blc)"/>
			<emp:date id="CusObisAssure.gty_str_dt" label="起始日期" required="false" onblur="checkStrData()"/>
			<emp:date id="CusObisAssure.gty_end_dt" label="到期日期" required="false" onblur="checkStrData()"/>
			<emp:text id="CusObisAssure.gty_bus_bch_dec" label="发生行详细名称" maxlength="60" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisAssure.valid_flg" label="有效标志" required="true" dictname="STD_ZB_STATUS" defvalue="1"/>
			<emp:textarea id="CusObisAssure.remark" label="备注" maxlength="250" required="false"  colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusObisAssure.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusObisAssure.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusObisAssure.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusObisAssure.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusObisAssure.last_upd_date" label="更新日期" maxlength="10" required="false" hidden="true" />
			<emp:textarea id="CusObisAssure.guar_detail" label="担保业务描述" maxlength="250" required="false" colSpan="2" hidden="false"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusObisAssure" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

