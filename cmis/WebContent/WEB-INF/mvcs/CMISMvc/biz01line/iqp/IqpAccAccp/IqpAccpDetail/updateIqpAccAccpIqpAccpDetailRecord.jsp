<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	String is_elec_bill = request.getParameter("is_elec_bill");
	/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cont ="";
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
	}
	String modify_rel_serno ="";
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
	}
	/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doSub(){
	if(IqpAccpDetail._checkAll()){
		var is_elec_bill = <%=is_elec_bill %>;
		var type = IqpAccpDetail.term_type._getValue();
		var term = IqpAccpDetail.term._getValue();
		if(is_elec_bill == 1){//电票
			if(type == '001'){
				if(term > 1){
					alert("电票期限应在1年以内！");
					return;
				}
			}else if(type == '002'){
				if(term > 12){
					alert("电票期限应在1年以内！");
					return;
				}
			}else if(type == '003'){
				if(term > 365){
					alert("电票期限应在1年以内！");
					return;
				}
			}
		}else if(is_elec_bill == 2){
			if(type == '001'){
				if(term > 0.5){
					alert("非电子票据，票据期限应在6个月以内！");
					return;
				}
			}else if(type == '002'){
				if(term > 6){
					alert("非电子票据，票据期限应在6个月以内！");
					return;
				}
			}
		}else {
			alert("请先录入银行承兑汇票信息！");
			return;
		}
		var form = document.getElementById("submitForm");
		IqpAccpDetail._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var message = jsonstr.message;
				if(flag == "success"){
					alert("修改成功!");
					/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
					window.opener.location.reload();
					/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
					window.opener.parent.location.reload();         
					window.close();   
				}else {
					alert(message);
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
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var postData = YAHOO.util.Connect.setForm(form);
		var cont ='<%=cont%>';
		var modify_rel_serno = IqpAccpDetail.modify_rel_serno._getValue();
		var url = '<emp:url action="updateIqpAccAccpIqpAccpDetailRecord.do"/>?is_elec_bill='+is_elec_bill+"&cont="+cont+"&modify_rel_serno="+modify_rel_serno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	}
};
function doReturn(){
	window.close();
};
function getOrgNo(data){
	IqpAccpDetail.paorg_no._setValue(data.bank_no._getValue());
	IqpAccpDetail.paorg_name._setValue(data.bank_name._getValue());
};

function doOnLoad(){
	var cont='<%=cont%>';
	if(cont=="modify"){
		IqpAccpDetail.clt_acct_no._obj._renderReadonly(true);
		IqpAccpDetail.paorg_no._obj._renderReadonly(true);
		IqpAccpDetail.paorg_no._obj._renderReadonly(true);
		IqpAccpDetail.drft_amt._obj._renderReadonly(true);
	}
};
</script>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
.emp_input2{
border: 1px solid #b7b7b7;
width:600px;
}
</style>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updateIqpAccAccpIqpAccpDetailRecord.do?is_elec_bill=${context.is_elec_bill}" method="POST">
		<emp:gridLayout id="IqpAccpDetailGroup" title="承兑汇票申请明细" maxColumn="2">
			<emp:text id="IqpAccpDetail.clt_person" label="收款人" maxlength="80" required="true" colSpan="2" cssElementClass="emp_input2"/>
			<emp:text id="IqpAccpDetail.clt_acct_no" label="收款人账号" maxlength="40" required="true" dataType="Acct" cssElementClass="emp_field_input" colSpan="2"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpAccpDetail.paorg_no" label="收款人开户行行号" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOrgNo" required="true" buttonLabel="选择" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpAccpDetail.paorg_name" label="收款人开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="IqpAccpDetail.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/>
			<emp:select id="IqpAccpDetail.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="IqpAccpDetail.term" label="期限" maxlength="38" required="true" dataType="Int"/>
			<emp:text id="IqpAccpDetail.pk1" label="承兑汇票申请明细流水号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpAccpDetail.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<!-- modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
			<emp:text id="IqpAccpDetail.modify_rel_serno" label="打回业务修改流水编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<!-- modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
