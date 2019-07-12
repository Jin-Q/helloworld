<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//检查合同起始日期不能早于当前日期; 到期日期要大于起始日期
	function chechDt(){
		 var openDay = '${context.OPENDAY}';
		 var start = IqpAppPsaleCont.start_date._getValue();
		 var end = IqpAppPsaleCont.end_date._getValue();
		 //if(start!=null && start !=""){
		 //    var flag=CheckDate1BeforeDate2(openDay,start);
		 //    if(!flag){
		 //	     alert("开始日期不能早于当前日期！");
		 //	     IqpAppPsaleCont.start_date._setValue("");
		 //	     return false;
		 //	 }
		 // }
		if(start!=null && start !="" && end!=null && end !=""){
			var flag=CheckDate1BeforeDate2(start,end);
			if(!flag){
				alert("合同到期日期要大于起始日期！");
				IqpAppPsaleCont.end_date._setValue("");
				return false;
			}
		} 
		return true;
	}
	 //计算商品总价
	function clcTotal(){
		var qnt=IqpAppPsaleCont.qnt._getValue();
		var price =IqpAppPsaleCont.unit_price._getValue();
		if( qnt !=null && qnt !="" && price !=null && price !=""){
			var qntp=parseInt(IqpAppPsaleCont.qnt._getValue());
			var pricep=parseFloat(IqpAppPsaleCont.unit_price._getValue());
			var total = parseFloat(qnt*price).toFixed(2);
			IqpAppPsaleCont.total._setValue(total);
		}
		return false;
	 };

	 function setCatalogPath(data){
		IqpAppPsaleCont.commo_name._setValue(data.locate);
		IqpAppPsaleCont.commo_name_displayname._setValue(data.locate_cn);
	 };


	 function doSub(){
			var form = document.getElementById("submitForm");
			if(IqpAppPsaleCont._checkAll()){
				IqpAppPsaleCont._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var psale_cont = jsonstr.psale_cont;
						if(flag == "success"){
							alert("保存成功!");
							var url = '<emp:url action="getIqpAppPsaleContUpdatePage.do"/>?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&mem_manuf_type=${context.mem_manuf_type}&cus_id=${context.cus_id}&psale_cont='+psale_cont;                                                 
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else if(flag == "have"){
							alert("已存在此笔购销合同!");
							IqpAppPsaleCont.psale_cont._setValue("");
						}else{
							alert("保存失败!");
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
				return;
			}
		};
	/*--user code end--*/
	
--></script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAppPsaleContRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppPsaleContGroup" title="购销合同信息" maxColumn="2">
			<emp:text id="IqpAppPsaleCont.buyer_cus_id" label="买方客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.buyer_cus_id_displayname" label="买方客户名称"    required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleCont.barg_cus_id" label="卖方客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.barg_cus_id_displayname" label="卖方客户名称"    required="true" readonly="true"/>
			
			
			<emp:text id="IqpAppPsaleCont.cont_amt" label="合同金额" maxlength="16" required="true" dataType="Currency"  colSpan="2"/>
			<emp:date id="IqpAppPsaleCont.start_date" label="合同起始日" required="true" />
			<emp:date id="IqpAppPsaleCont.end_date" label="合同到期日" required="true" onblur="chechDt()" />
			
			<emp:pop id="IqpAppPsaleCont.commo_name_displayname" label="商品名称" required="false" hidden="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppPsaleCont.commo_name" label="商品名称" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.qnt" label="购买商品数量" maxlength="16" required="false" hidden="true" onblur="clcTotal()" />
			<emp:select id="IqpAppPsaleCont.qnt_unit" label="数量单位" required="false" hidden="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpAppPsaleCont.unit_price" label="购买商品单价（元）" maxlength="16" required="false" hidden="true" dataType="Currency" onblur="clcTotal()" />
			<emp:text id="IqpAppPsaleCont.total" label="购买商品总价（元）" maxlength="16" required="false" hidden="true" dataType="Currency" readonly="true" />
			
			<emp:textarea id="IqpAppPsaleCont.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			
			<emp:text id="IqpAppPsaleCont.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleCont.input_br_id_displayname" label="登记机构"   required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true" />
			
			<emp:text id="IqpAppPsaleCont.psale_cont" label="购销合同编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="IqpAppPsaleCont.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.mem_cus_id" label="成员编号" maxlength="30" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
	
	
</body>
</html>
</emp:page>

