<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	//获取买方客户信息
   function getCusInfo(data){
	   IqpPsaleCont.buyer_cus_id._setValue(data.cus_id._getValue());
	   IqpPsaleCont.buyer_cus_id_displayname._setValue(data.cus_name._getValue());
	   var barg =IqpPsaleCont.barg_cus_id._getValue();
	   if(data.cus_id._getValue() == barg){
		   alert("买方和卖方客户不能是同一人！");
		   IqpPsaleCont.buyer_cus_id._setValue("");
		   IqpPsaleCont.buyer_cus_id_displayname._setValue("");
	   }
	}
    //获取卖方客户信息
	function getCusInfo4barg(data){
	   IqpPsaleCont.barg_cus_id._setValue(data.cus_id._getValue());
	   IqpPsaleCont.barg_cus_id_displayname._setValue(data.cus_name._getValue());
	   var buyer = IqpPsaleCont.buyer_cus_id._getValue();
	   if(data.cus_id._getValue() == buyer){
		   alert("卖方和买方客户不能是同一人！");
		   IqpPsaleCont.barg_cus_id._setValue("");
		   IqpPsaleCont.barg_cus_id_displayname._setValue("");
	   }
	}
	
	function doReturn(){
		 var url = '<emp:url action="queryIqpPsaleContList.do"/>?net_agr_no=${context.net_agr_no}';
		 url = EMPTools.encodeURI(url);
		 window.location = url;
	}
    //检查合同起始日期不能早于当前日期; 到期日期要大于起始日期
	function chechDt(){
		 var openDay = '${context.OPENDAY}';
		 var start = IqpPsaleCont.start_date._getValue();
		 var end = IqpPsaleCont.end_date._getValue();
		 if(start!=null && start !=""){
		     var flag=CheckDate1BeforeDate2(openDay,start);
		     if(!flag){
			     alert("开始日期不能早于当前日期！");
			     IqpPsaleCont.start_date._setValue("");
			     return false;
			 }
		 }
		if(start!=null && start !="" && end!=null && end !=""){
			var flag=CheckDate1BeforeDate2(start,end);
			if(!flag){
				alert("合同到期日期要大于起始日期！");
				IqpPsaleCont.end_date._setValue("");
				return false;
			}
		} 
		return true;
	}
	 //计算商品总价
	function clcTotal(){
		var qnt=IqpPsaleCont.qnt._getValue();
		var price =IqpPsaleCont.unit_price._getValue();
		if( qnt !=null && qnt !="" && price !=null && price !=""){
			var qntp=parseInt(IqpPsaleCont.qnt._getValue());
			var pricep=parseFloat(IqpPsaleCont.unit_price._getValue());
			var total = parseFloat(qnt*price).toFixed(2);
			IqpPsaleCont.total._setValue(total);
		}
		return false;
	}
	
	function doSub(){
		if(IqpPsaleCont._checkAll()){
			var form = document.getElementById("submitForm");
			IqpPsaleCont._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {							
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="queryIqpPsaleContList.do"/>?net_agr_no=${context.net_agr_no}&mem_cus_id=${context.mem_cus_id}&mem_manuf_type=${context.mem_manuf_type}&cus_id=${context.cus_id}';                                                 
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("发生异常！");
					}
				}
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}
	}
	
	function doReturn(){
		var url = '<emp:url action="queryIqpPsaleContList.do"/>?net_agr_no=${context.net_agr_no}&mem_cus_id=${context.mem_cus_id}&mem_manuf_type=${context.mem_manuf_type}&cus_id=${context.cus_id}';                                                 
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function setCatalogPath(data){
		IqpPsaleCont.commo_name._setValue(data.id);
		IqpPsaleCont.commo_name_displayname._setValue(data.label);
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addIqpPsaleContRecord.do" method="POST">
		<emp:gridLayout id="IqpPsaleContGroup" title="年度购销合同" maxColumn="2">
			<emp:text id="IqpPsaleCont.buyer_cus_id" label="买方客户码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="IqpPsaleCont.buyer_cus_id_displayname" label="买方客户名称"   required="true" readonly="true"/>
			<emp:text id="IqpPsaleCont.barg_cus_id" label="卖方客户码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="IqpPsaleCont.barg_cus_id_displayname" label="买方客户名称"   required="true" readonly="true"/>		
			<emp:text id="IqpPsaleCont.cont_amt" label="购销合同金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/>
			<emp:date id="IqpPsaleCont.start_date" label="购销合同起始到期" required="true" onblur="chechDt()"/>
			<emp:date id="IqpPsaleCont.end_date" label="购销合同到期日期" required="true" onblur="chechDt()"/>
			<emp:text id="IqpPsaleCont.commo_name" label="购买商品名称" required="true" hidden="true"/>
			<emp:pop id="IqpPsaleCont.commo_name_displayname" label="购买商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpPsaleCont.qnt" label="购买商品数量" maxlength="16" required="true" onblur="clcTotal()"/>
			<emp:text id="IqpPsaleCont.unit_price" label="购买商品单价" maxlength="18" required="true" dataType="Currency" onblur="clcTotal()"/>
			<emp:text id="IqpPsaleCont.total" label="购买商品总价" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="IqpPsaleCont.memo" label="备注" maxlength="100" required="true" colSpan="2"/>
			<emp:select id="IqpPsaleCont.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true" defvalue="1"/>
			<emp:text id="IqpPsaleCont.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpPsaleCont.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:date id="IqpPsaleCont.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="IqpPsaleCont.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpPsaleCont.psale_cont" label="购销合同码" maxlength="32" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>