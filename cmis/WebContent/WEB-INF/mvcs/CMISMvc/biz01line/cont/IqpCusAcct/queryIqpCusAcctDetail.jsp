<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String is_agent_disc="";
	if(context.containsKey("is_agent_disc")){
		is_agent_disc =(String)context.getDataValue("is_agent_disc");
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg = "";
	String modify_rel_serno = "";
	String wf_flag="";
	if(context.containsKey("modiflg")){
		modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_input2{
border:1px solid #b7b7b7;
width:600px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
    function load(){
    	acct_attr_change();
    	isThisOrg();
    }

	function doReturn() { 
		var serno = IqpCusAcct.serno._getValue();   
		var url = '<emp:url action="queryIqpCusAcctList.do"/>?menuIdTab=${context.menuIdTab}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&subButtonId=queryIqpCusAcctList&serno='+serno; 
		url = EMPTools.encodeURI(url);  
		window.location=url;
	};
	//-------------检查支付金额是否显示------------
   function acct_attr_change(){
    	var acct_attr = IqpCusAcct.acct_attr._getValue();
    	if(acct_attr == "04"){
    		IqpCusAcct.pay_amt._obj._renderHidden(false);
    		IqpCusAcct.pay_amt._obj._renderRequired(true);
        }else {
        	IqpCusAcct.pay_amt._setValue("");
        	IqpCusAcct.pay_amt._obj._renderHidden(true);
        	IqpCusAcct.pay_amt._obj._renderRequired(false);
        }
    };

  //-------------是否我行账户效验，是则需要校验，不是则无需校验------------
    function isThisOrg(){
		var isThis = IqpCusAcct.is_this_org_acct._getValue();
		if(isThis == 1){
			$(".emp_field_label:eq(7)").text("机构号");
			$(".emp_field_label:eq(8)").text("机构名称");
		}else {
			$(".emp_field_label:eq(7)").text("开户行行号");
			$(".emp_field_label:eq(8)").text("开户行行名");
		}
    };
    /**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
    function doClose(){
		window.close();
    };
    /**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	
</script>
</head>
<body class="page_content" onload="load();">
	
	<emp:gridLayout id="IqpCusAcctGroup" title="账户信息" maxColumn="2">
	        <emp:text id="IqpCusAcct.pk_id" label="PKID" maxlength="40" colSpan="2" required="false" hidden="true"/>
			<emp:text id="IqpCusAcct.serno" label="业务编号" maxlength="40" colSpan="2" required="false" hidden="true"/>
			<emp:text id="IqpCusAcct.cont_no" label="合同编号" maxlength="40" colSpan="2" required="false" hidden="true"/>
			<emp:select id="IqpCusAcct.acct_attr" label="账户属性" required="true" dictname="STD_ZB_BR_ID_ATTR" />
			<emp:select id="IqpCusAcct.is_this_org_acct" label="是否本行账户" required="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpCusAcct.acct_no" label="账号" url="null" required="true" buttonLabel="获取"/>
			<emp:text id="IqpCusAcct.acct_name" label="账户名称" maxlength="80" required="true" />
			<emp:pop id="IqpCusAcct.opac_org_no" label="开户行行号" url="null" required="true" buttonLabel="选择"/>
			<emp:text id="IqpCusAcct.opan_org_name" label="开户行行名" maxlength="100" required="true" colSpan="2" cssElementClass="emp_input2"/>
			<emp:text id="IqpCusAcct.pay_amt" label="支付金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:select id="IqpCusAcct.cur_type" label="币种" required="true"  defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpCusAcct.interbank_id" label="银联行号" maxlength="40" hidden="true"/>
	</emp:gridLayout>   
	 
	<div align="center">
		<br>
		<!-- modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		<%if("yes".equals(modiflg)){ %>
		<emp:button id="close" label="关闭"/>
		<%}else{ %>
		<emp:button id="return" label="返回"/>
		<%} %>
		<!-- modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
	</div>
</body>
</html>
</emp:page>
