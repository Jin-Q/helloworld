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
	//获取收货地点信息
	function onReturnRegStateCode(date){
		IqpBconCoopAgr.consign_addr._obj.element.value=date.id;
		IqpBconCoopAgr.consign_addr_displayname._obj.element.value=date.label;
	}
	function doReturn(){
		var url = '<emp:url action="queryIqpBconCoopAgrList.do"/>'+"&cus_id=${context.cus_id}"
															      +"&mem_cus_id=${context.mem_cus_id}"
															      +"&net_agr_no=${context.net_agr_no}"
															      +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	//异步保存数据
	  function doSub(){
			if(IqpBconCoopAgr._checkAll()){
				var form = document.getElementById("submitForm");
				IqpBconCoopAgr._toForm(form);
				var handleSuccess = function(o){
				if(o.responseText !== undefined) {									
						var jsonstr = eval("("+o.responseText+")");
						var flag = jsonstr.flag;
						if(flag == 'success'){
							alert("保存成功！");
							var url = '<emp:url action="queryIqpBconCoopAgrList.do"/>'+"&cus_id=${context.cus_id}"
																			          +"&mem_cus_id=${context.mem_cus_id}"
																			          +"&net_agr_no=${context.net_agr_no}"
																			          +"&mem_manuf_type=${context.mem_manuf_type}";
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
		};
		    //获取收货人编号
		   function getCusInfo4consign(data){
			   IqpBconCoopAgr.consign_cus_id._setValue(data.cus_id._getValue());
			   IqpBconCoopAgr.consign_cus_id_displayname._setValue(data.cus_name._getValue());
	       }
			function doLoad(){
				IqpBconCoopAgr.refndmt_acct._obj.addOneButton("cus_id","选择",queryCusForm);
			}
			function queryCusForm(){
				var cus_id = IqpBconCoopAgr.refndmt_acct._getValue();
				var url = '<emp:url action=".do"/>?cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow');
			}
			//设置购销合同
			function setPsale(data){
				IqpBconCoopAgr.psale_cont._setValue(data.psale_cont._getValue());
				IqpPsaleCont.cont_amt._setValue(data.cont_amt._getValue());
				IqpPsaleCont.start_date._setValue(data.start_date._getValue());
				IqpPsaleCont.end_date._setValue(data.end_date._getValue());
			}
			//设置订货计划信息
			function setDesgood(data){
				IqpBconCoopAgr.desgoods_plan_no._setValue(data.desgoods_plan_no._getValue());
			}
			//检查警戒线和平仓线的大小。警戒线大于平仓线
			function  checkLine(){
				var vigiLine=parseFloat(IqpBconCoopAgr.vigi_line._obj.element.value);//警戒线
				var storLine= parseFloat(IqpBconCoopAgr.stor_line._obj.element.value);//平仓线
				if(null!=vigiLine && ""!=vigiLine && null!=storLine && ""!=storLine){
					if(vigiLine<storLine){
						alert("警戒线要大于平仓线！");
						IqpBconCoopAgr.stor_line._setValue("");
						return false;
					}
				}
			}
		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">	
	<emp:form id="submitForm" action="addIqpBconCoopAgrRecord.do" method="POST">
		<emp:gridLayout id="IqpBconCoopAgrGroup" title="银企合作协议" maxColumn="2">
			<emp:text id="IqpBconCoopAgr.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpBconCoopAgr.coop_agr_no" label="银企商协议号" maxlength="32" hidden="true" />
			<emp:text id="IqpBconCoopAgr.borrow_cus_id" label="借款人客户码" maxlength="32" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.borrow_cus_id_displayname" label="借款人客户名称"  required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="IqpBconCoopAgr.manuf_cus_id" label="厂商客户码" maxlength="32" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.manuf_cus_id_displayname" label="厂商客户名称"   required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:pop id="IqpBconCoopAgr.psale_cont" label="年度购销合同" url="queryIqpPsaleContPop.do?net_agr_no=${context.net_agr_no}" returnMethod="setPsale" required="true"/>
			<emp:text id="IqpPsaleCont.cont_amt" label="年度合同金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:date id="IqpPsaleCont.start_date" label="年度合同开始日期" required="true" readonly="true"/>
			<emp:date id="IqpPsaleCont.end_date" label="年度合同结束日期"  required="true" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.lmt_agr_no" label="借款人授信协议号" maxlength="32" required="false" colSpan="2" readonly="true"/>
			<emp:pop id="IqpBconCoopAgr.desgoods_plan_no" label="订货计划" url="queryIqpDesbuyPlanPop.do?net_agr_no=${context.net_agr_no}" returnMethod="setDesgood" required="true" />
			<emp:text id="IqpBconCoopAgr.low_bail_perc" label="最低保证金比例" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpBconCoopAgr.vigi_line" label="警戒线" maxlength="10" required="true" dataType="Percent" onblur="checkLine()"/>
			<emp:text id="IqpBconCoopAgr.stor_line" label="平仓线" maxlength="10" required="true" dataType="Percent" onblur="checkLine()"/>
			<emp:text id="IqpBconCoopAgr.froze_line" label="冻结线" maxlength="10" required="true" dataType="Percent" />
			<emp:pop id="IqpBconCoopAgr.consign_cus_id" label="收货人" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo4consign" required="false" colSpan="2"/>
			<emp:text id="IqpBconCoopAgr.consign_cus_id_displayname" label="收货人"  required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:pop id="IqpBconCoopAgr.consign_addr_displayname" label="收货地点" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" 
			          returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>		
			<emp:text id="IqpBconCoopAgr.refndmt_acct" label="退款账户" maxlength="32" required="false" />
			<emp:text id="IqpBconCoopAgr.refndmt_acct_name" label="退款账户名称" maxlength="32" required="false" />		
			<emp:textarea id="IqpBconCoopAgr.memo" label="备注" maxlength="100" required="false" colSpan="2" />
			<emp:select id="IqpBconCoopAgr.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="1" readonly="true"/>
			<emp:date id="IqpBconCoopAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY"/>		
			<emp:text id="IqpBconCoopAgr.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>	
			<emp:text id="IqpBconCoopAgr.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpBconCoopAgr.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>		
			<emp:pop id="IqpBconCoopAgr.consign_addr" label="收货地点" url="" required="false" hidden="true"/>
			<emp:date id="IqpBconCoopAgr.start_date" label="协议起始日期" required="false" hidden="true"/>
			<emp:date id="IqpBconCoopAgr.end_date" label="协议到期日期" required="false" hidden="true"/>
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

