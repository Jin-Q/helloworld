<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
       var billType = '${context.bill_type}';
       if(billType == "100"){//银票
    	   IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(true);
       }else{//商票
    	   IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(false);
       }
    }
    
	function doSave(){
		if(!IqpDiscApp._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpDiscApp._toForm(form);
		//var serno = IqpBksyndic._getValue();
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
					alert("保存成功！");
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpDiscAppRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateIqpDiscAppRecord.do" method="POST">
		<emp:gridLayout id="IqpDiscAppGroup" maxColumn="2" title="贴现申请从表">
			<emp:text id="IqpDiscApp.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="true" readonly="true" />
			<emp:select id="IqpDiscApp.bill_type" label="票据种类" required="true" defvalue="${context.bill_type}"   readonly ="true" dictname="STD_DRFT_TYPE" />
			<emp:select id="IqpDiscApp.busdrft_dscnt_mode" label="商票贴现类型" required="true" dictname="STD_BUSDRFT_DISCOUNT_TYPE"/> 
			<emp:select id="IqpDiscApp.is_elec_bill" label="是否电子票据" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpDiscApp.disc_type" label="贴现类型" required="true" dictname="STD_ZB_DISCOUNT_TYPE"/>
			<emp:date id="IqpDiscApp.disc_date" label="贴现日期" required="false" readonly="true"/> 
			
			<emp:text id="IqpDiscApp.bill_qty" label="票据数量" defvalue="0" maxlength="38" required="true" readonly="true"/>
			<emp:text id="IqpDiscApp.disc_rate" label="贴现利息" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpDiscApp.net_pay_amt" label="实付总金额" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			
			<emp:select id="IqpDiscApp.is_agent_disc" label="是否代理贴现" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDiscApp.agent_acct_no" label="代理人账户" maxlength="40" required="true" />
			<emp:text id="IqpDiscApp.agent_acct_name" label="代理人名称" maxlength="80" required="true" />
			<emp:pop id="IqpDiscApp.disc_sett_acct_no" label="贴现人结算账户" url="null" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="IqpDiscApp.disc_sett_acct_name" label="贴现人结算账户户名" maxlength="100" required="true" />
			<emp:pop id="IqpDiscApp.pint_no" label="付息账号" url="null" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="IqpDiscApp.pint_acct_name" label="付息账户名" maxlength="80" required="true" /> 
			<emp:textarea id="IqpDiscApp.pvp_pact_cond_memo" label="出账落实条件说明" maxlength="250" required="false" colSpan="2" />
			<emp:textarea id="IqpDiscApp.dscnt_int_pay_mode" label="贴现利息支付方式" maxlength="5" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>      
		</div>
	</emp:form>
</body>
</html>
</emp:page>
