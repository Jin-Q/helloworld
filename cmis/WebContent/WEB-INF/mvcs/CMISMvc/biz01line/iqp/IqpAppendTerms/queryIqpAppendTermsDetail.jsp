<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = "";
	String apply_cur_type = "";
	String apply_amount = "";
	String canFeeCode = "";
	String prd_id = "";
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg ="";
	String modify_rel_serno ="";
	String op="";
	String wf_flag="";
	String modiclose="";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	if(context.containsKey("apply_cur_type")){
		apply_cur_type = (String)context.getDataValue("apply_cur_type");
	}
	if(context.containsKey("apply_amount")){
		apply_amount = (String)context.getDataValue("apply_amount");
	}
	if(context.containsKey("canFeeCode")){
		canFeeCode = (String)context.getDataValue("canFeeCode");
	}
	if(context.containsKey("prd_id")){
		prd_id = (String)context.getDataValue("prd_id");
	}
	if(context.containsKey("modiflg")){
		modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	if(context.containsKey("op")){
		op = (String) context.getDataValue("op");
	}
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	if(context.containsKey("modiclose")){
		modiclose = (String) context.getDataValue("modiclose");
	}
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_field_select_select1 {
	width: 665px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
	/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	function doReturn() {
		var modiflg  ='<%=modiflg%>';
		var op='<%=op%>';
		var url ="";
		if(modiflg =="yes"){
		 	if(op=="update"){
			 	url = '<emp:url action="queryIqpAppendTermsList.do"/>?menuIdTab=queryIqpLoanApp&subMenuId=queryIqpAppendTermsList&apply_cur_type=${context.apply_cur_type}&apply_amount=${context.apply_amount}&prd_id=${context.prd_id}&op=update&serno='+'<%=serno%>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'; 
		 	}else{
		 		 url = '<emp:url action="queryIqpAppendTermsList.do"/>?menuIdTab=queryIqpLoanApp&subMenuId=queryIqpAppendTermsList&apply_cur_type=${context.apply_cur_type}&apply_amount=${context.apply_amount}&prd_id=${context.prd_id}&op=view&serno='+'<%=serno%>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+'&wf_flag='+'<%=wf_flag%>'; 
			}
		}else{
		 url = '<emp:url action="queryIqpAppendTermsList.do"/>?serno='+'<%=serno%>'+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>';
		}		
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

 	function doClose(){
		window.close();
 	};
	/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	function onload(){
		checkIsCycleChrg();
		checkCollectType();
	};

	//-----------------控制是否周期---------------------
	function checkIsCycleChrg(){
		var is_cycle_chrg = IqpAppendTerms.is_cycle_chrg._getValue();
		if(is_cycle_chrg == '1'){
        	IqpAppendTerms.chrg_date._obj._renderHidden(false);
        	IqpAppendTerms.chrg_date._obj._renderRequired(true);
        	
        	IqpAppendTerms.chrg_freq._obj._renderHidden(false);
        	IqpAppendTerms.chrg_freq._obj._renderRequired(true);
        }else{
        	IqpAppendTerms.chrg_date._obj._renderHidden(true);
        	IqpAppendTerms.chrg_date._obj._renderRequired(false);
        	IqpAppendTerms.chrg_date._setValue("");
        	
        	IqpAppendTerms.chrg_freq._obj._renderHidden(true);
        	IqpAppendTerms.chrg_freq._obj._renderRequired(false);
        	IqpAppendTerms.chrg_freq._setValue("");
        }
	};
	 //-------------是否我行账户效验，是则需要校验，不是则无需校验------------
    function isThisOrg(){
		var isThis = IqpAppendTerms.is_this_org_acct._getValue();
		if(isThis == "1"){
			IqpAppendTerms.acct_no._setValue("");
			IqpAppendTerms.acct_name._setValue("");
			IqpAppendTerms.acct_name._obj._renderReadonly(true);
			IqpAppendTerms.opac_org_no._setValue("");
			IqpAppendTerms.opan_org_name._setValue("");
			IqpAppendTerms.opan_org_name._obj._renderReadonly(true);
			IqpAppendTerms.opac_org_no._obj._renderReadonly(true);
		}else {
			IqpAppendTerms.acct_no._setValue("");
			IqpAppendTerms.acct_name._setValue("");
			IqpAppendTerms.acct_name._obj._renderReadonly(false);
			IqpAppendTerms.opac_org_no._setValue("");
			IqpAppendTerms.cur_type._setValue("");
			IqpAppendTerms.opan_org_name._setValue("");
			IqpAppendTerms.opan_org_name._obj._renderReadonly(false);
			IqpAppendTerms.opac_org_no._obj._renderReadonly(false);
		}
    };
  //-------------通过账户属性和是否非是否本行账户判断是否币种是否可选------------
	function changeCurTypeReadOnly(){
        var is_this_org_acct = IqpAppendTerms.is_this_org_acct._getValue();
        if(is_this_org_acct == "1" ){  //本行的币种不可选
        	IqpAppendTerms.cur_type._obj._renderReadonly(true);
        	//设为人民币默认
        	IqpAppendTerms.cur_type._setValue("CNY");
        		
        }else if(is_this_org_acct == "2" ){ //非本行的时候 币种可以选
        	IqpAppendTerms.cur_type._obj._renderReadonly(false);
        }
	};
	function checkCollectType(){
        var collect_type = IqpAppendTerms.collect_type._getValue();
        if(collect_type == "01"){//按固定金额
        	IqpAppendTerms.fee_rate._obj._renderReadonly(true);
        	IqpAppendTerms.fee_rate._obj._renderHidden(true);
        	IqpAppendTerms.fee_rate._obj._renderRequired(false);
        }else if(collect_type == "02"){//按比率
        	IqpAppendTerms.fee_rate._obj._renderReadonly(false);
        	IqpAppendTerms.fee_rate._obj._renderHidden(false);
        	IqpAppendTerms.fee_rate._obj._renderRequired(true);
        }
    };

    function getAccNo(data){
        IqpAppendTerms.fee_acct_no._setValue(data.acct_no._getValue());
     };
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="IqpAppendTermsGroup" title="费用信息" maxColumn="2">
			<emp:select id="IqpAppendTerms.fee_code" label="费用描述" required="true" dictname="STD_ZB_FEE_CODE"/>
			<emp:select id="IqpAppendTerms.fee_type" label="费用类型" required="true" dictname="STD_ZB_FEE_MODE" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="IqpAppendTerms.fee_cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE"  readonly="true"/>
			
			<emp:select id="IqpAppendTerms.collect_type" label="收费方式" dictname="STD_ZB_COLLECT_TYPE" onblur="checkCollectType()" required="false" />
			
			<emp:text id="IqpAppendTerms.fee_rate" label="费用比率" maxlength="16" required="true"  dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAppendTerms.fee_amt" label="费用总金额" maxlength="16" required="true"  dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAppendTerms.is_cycle_chrg" label="是否周期性收费" required="true"  dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="IqpAppendTerms.chrg_freq" label="收费频率" required="false" dictname="STD_BACK_CYCLE" hidden="true"/>
			<emp:text id="IqpAppendTerms.chrg_date" label="收费日" required="false" dataType="Int" maxlength="10" hidden="true"/>
		    <emp:pop id="IqpAppendTerms.fee_acct_no" label="费用账号" required="true" url="queryIqpCusAcctPop.do?serno=${context.serno}&returnMethod=getAccNo"  />
		    <emp:text id="IqpAppendTerms.serno" label="业务编号"  hidden="true" maxlength="40" required="false" />
		    <emp:text id="IqpAppendTerms.append_terms_pk" label="主键"  hidden="true" maxlength="40" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("yes".equals(modiclose)){ %>
		<emp:button id="close" label="关闭"/>	
		<%}else{ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
