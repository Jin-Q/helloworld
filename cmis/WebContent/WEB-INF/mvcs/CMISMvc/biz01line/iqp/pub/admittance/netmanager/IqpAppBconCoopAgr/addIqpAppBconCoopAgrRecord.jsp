<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	//设置购销合同
	function setPsale(data){
		IqpAppBconCoopAgr.psale_cont._setValue(data.psale_cont._getValue());
		IqpAppPsaleCont.cont_amt._setValue(data.cont_amt._getValue());
		IqpAppPsaleCont.start_date._setValue(data.start_date._getValue());
		IqpAppPsaleCont.end_date._setValue(data.end_date._getValue());
	};

	//设置订货计划信息
	function setDesgood(data){
		IqpAppBconCoopAgr.desgoods_plan_no._setValue(data.desgoods_plan_no._getValue());
	};
	//检查警戒线和平仓线的大小。警戒线大于平仓线
	function  checkLine(){
		var vigiLine=parseFloat(IqpAppBconCoopAgr.vigi_line._obj.element.value);//警戒线
		var storLine= parseFloat(IqpAppBconCoopAgr.stor_line._obj.element.value);//平仓线
		if(null!=vigiLine && ""!=vigiLine && null!=storLine && ""!=storLine){
			if(vigiLine<storLine){
				alert("警戒线要大于平仓线！");
				IqpAppBconCoopAgr.stor_line._setValue("");
				return false;
			}else{
                return true;
			}
		}
	};	

	 //获取收货人编号
	   function getCusInfo4consign(data){
		   IqpAppBconCoopAgr.consign_cus_id._setValue(data.cus_id._getValue());
		   IqpAppBconCoopAgr.consign_cus_id_displayname._setValue(data.cus_name._getValue());
    }
		function load(){
			//IqpAppBconCoopAgr.refndmt_acct._obj.addOneButton("cus_id","选择",queryCusForm);
		}
		function queryCusForm(){
			var cus_id = IqpAppBconCoopAgr.refndmt_acct._getValue();
			var url = '<emp:url action=".do"/>?cus_id='+cus_id;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		};

		//获取收货地点信息
		function onReturnRegStateCode(date){
			IqpAppBconCoopAgr.consign_addr._obj.element.value=date.id;
			IqpAppBconCoopAgr.consign_addr_displayname._obj.element.value=date.label;
		};
		
		function doSub(){
			var form = document.getElementById("submitForm");
			if(IqpAppBconCoopAgr._checkAll()){
				if(!checkLine()){
	                   return;
				}
				IqpAppBconCoopAgr._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("保存成功!");
							var url = '<emp:url action="queryIqpAppBconCoopAgrList.do"/>'+"&cus_id=${context.cus_id}"
					                   +"&mem_cus_id=${context.mem_cus_id}"
					                   +"&serno=${context.serno}"
					                   +"&mem_manuf_type=${context.mem_manuf_type}";
                              url = EMPTools.encodeURI(url);
                              window.location = url;
						}else {
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
	
</script>
</head>
<body class="page_content" onload="load()">
	
	<emp:form id="submitForm" action="addIqpAppBconCoopAgrRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppBconCoopAgrGroup" title="银企商合作协议信息" maxColumn="2">
			<emp:text id="IqpAppBconCoopAgr.borrow_cus_id" label="借款人客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.borrow_cus_id_displayname" label="借款人客户名称"   required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="IqpAppBconCoopAgr.manuf_cus_id" label="核心企业客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.manuf_cus_id_displayname" label="核心企业客户名称"  required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:pop id="IqpAppBconCoopAgr.psale_cont" label="购销合同" url="queryIqpAppPsaleContPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setPsale" required="true"/>
			<emp:text id="IqpAppPsaleCont.cont_amt" label="年度合同金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:date id="IqpAppPsaleCont.start_date" label="年度合同开始日期" required="true" readonly="true"/>
			<emp:date id="IqpAppPsaleCont.end_date" label="年度合同结束日期"  required="true" readonly="true"/>
			
			<emp:pop id="IqpAppBconCoopAgr.desgoods_plan_no" label="订货计划" url="queryIqpAppDesbuyPlanPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setDesgood" required="false" />
			<emp:text id="IqpAppBconCoopAgr.low_bail_perc" label="最低保证金比例" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpAppBconCoopAgr.vigi_line" label="警戒线" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="IqpAppBconCoopAgr.stor_line" label="平仓线" maxlength="10" required="true" dataType="Percent"/>
			<emp:text id="IqpAppBconCoopAgr.froze_line" label="冻结线" maxlength="10" required="true" dataType="Percent"  />
			
			<emp:pop id="IqpAppBconCoopAgr.consign_cus_id" label="收货人客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=getCusInfo4consign" required="false" colSpan="2"/>
			<emp:text id="IqpAppBconCoopAgr.consign_cus_id_displayname" label="收货人" required="false" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:pop id="IqpAppBconCoopAgr.consign_addr_displayname" label="收货地点" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" 
			          returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>		
			
			
			<emp:text id="IqpAppBconCoopAgr.refndmt_acct" label="退款账户账号" maxlength="40" required="false" />
			<emp:text id="IqpAppBconCoopAgr.refndmt_acct_name" label="退款账户名称" maxlength="80" required="false" />
			
			<emp:textarea id="IqpAppBconCoopAgr.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			<emp:text id="IqpAppBconCoopAgr.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppBconCoopAgr.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>	
			<emp:text id="IqpAppBconCoopAgr.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			
			 
			<emp:text id="IqpAppBconCoopAgr.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.consign_addr" label="收货地点" maxlength="100" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.coop_agr_no" label="银企商协议号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.start_date" label="协议起始日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.end_date" label="协议到期日期" maxlength="10" required="false" hidden="true"/>
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

