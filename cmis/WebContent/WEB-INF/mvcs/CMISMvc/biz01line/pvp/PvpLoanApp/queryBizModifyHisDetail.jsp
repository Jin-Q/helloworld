<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String prd_id="";
	if(context.containsKey("prd_id")){
		prd_id = (String) context.getDataValue("prd_id");
	}
	String wf_flag="";
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	String apply_cur_type="";
	if(context.containsKey("apply_cur_type")){
		apply_cur_type =(String)context.getDataValue("apply_cur_type");
	}  
	String apply_amount="";
	if(context.containsKey("apply_amount")){
		apply_amount =(String)context.getDataValue("apply_amount");
	}
	String modify_rel_serno="";
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	String modiflg="yes";
	String modiclose="yes";
	String flag_FreedomPayInfo="";
	String flag_PubBailInfo ="";
	String flag_AppendTerms ="";
	String flag_IqpAccpDetailInfo ="";
	String flag_IqpCusAcct ="no";
	if(context.containsKey("flag_FreedomPayInfo")){
		flag_FreedomPayInfo = (String) context.getDataValue("flag_FreedomPayInfo");
	}
	if(context.containsKey("flag_PubBailInfo")){
		flag_PubBailInfo = (String) context.getDataValue("flag_PubBailInfo");
	}
	if(context.containsKey("flag_AppendTerms")){
		flag_AppendTerms = (String) context.getDataValue("flag_AppendTerms");
	}
	if(context.containsKey("flag_IqpCusAcct")){
		flag_IqpCusAcct = (String) context.getDataValue("flag_IqpCusAcct");
	}
	if(context.containsKey("flag_IqpAccpDetailInfo")){
		flag_IqpAccpDetailInfo = (String) context.getDataValue("flag_IqpAccpDetailInfo");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">	
	function doViewIqpAppendTermsTmp() {
		var paramStr = IqpAppendTermsTmpList._obj.getParamStr(['append_terms_pk','serno']);
		if (paramStr != null) {
			var wf_flag= '<%=wf_flag%>';
			var url = '<emp:url action="getIqpAppendTermsViewPage.do"/>?'+paramStr+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+"&op=view&modiclose="+'<%=modiclose%>'+"&qFlag=BMT";
			url = EMPTools.encodeURI(url);
			var param = 'height=400, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow4IATT',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpAppendTermsHis() {
		var paramStr = IqpAppendTermsHisList._obj.getParamStr(['append_terms_pk','serno']);
		if (paramStr != null) {
			var wf_flag= '<%=wf_flag%>';
			var url = '<emp:url action="getIqpAppendTermsViewPage.do"/>?'+paramStr+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+"&op=view&modiclose="+'<%=modiclose%>'+"&qFlag=BMH";
			url = EMPTools.encodeURI(url);
			var param = 'height=400, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow4IATT',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewPubBailInfoTmp() {
		var paramStr = PubBailInfoTmpList._obj.getParamStr(['serno','cus_id','bail_acct_no']);
		if (paramStr != null) {
			var modiflg = '<%=modiflg%>';
			var modify_rel_serno = '<%=modify_rel_serno%>';
			var url = '<emp:url action="getPubBailInfo_jointViewPage.do"/>?'+paramStr+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno+"&qFlag=BMT";
			url = EMPTools.encodeURI(url);
			var param = 'height=400, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewPubBailInfoHis() {
		var paramStr = PubBailInfoHisList._obj.getParamStr(['serno','cus_id','bail_acct_no']);
		if (paramStr != null) {
			var modiflg = '<%=modiflg%>';
			var modify_rel_serno = '<%=modify_rel_serno%>';
			var url = '<emp:url action="getPubBailInfo_jointViewPage.do"/>?'+paramStr+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno+"&qFlag=BMH";
			url = EMPTools.encodeURI(url);
			var param = 'height=400, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCusAcctTmp() {
		var paramStr = IqpCusAcctTmpList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var modiflag='<%=modiflg%>';
			var cont_no = BizModifyHis.cont_no._getValue();
			if(modiflag =="yes"){
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+'&cont_no='+cont_no;
				url = EMPTools.encodeURI(url);
				window.open(url,'iqpCusAcct','height=500,width=800,top=80,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpCusAcctHis() {
		var paramStr = IqpCusAcctHisList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var modiflag='<%=modiflg%>';
			var cont_no = BizModifyHis.cont_no._getValue();
			if(modiflag =="yes"){
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+'&cont_no='+cont_no;
				url = EMPTools.encodeURI(url);
				window.open(url,'iqpCusAcct','height=500,width=800,top=80,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doClose(){
		window.close();
	};

	function doOnLoad(){
		var flag_PubBailInfo = '<%=flag_PubBailInfo%>';
		if(flag_PubBailInfo == 'yes'){
			document.getElementById('pubBailInfo').style.display="none"; 
		}
		var flag_AppendTerms ='<%=flag_AppendTerms%>';
		if(flag_AppendTerms == 'yes'){
			document.getElementById('appendTerms').style.display="none"; 
		}
		var flag_IqpCusAcct = '<%=flag_IqpCusAcct%>';
		if(flag_IqpCusAcct == 'no'){
			document.getElementById('iqpCusAcctInfo').style.display="none"; 
		}
		var flag_IqpAccpDetailInfo = '<%=flag_IqpAccpDetailInfo%>';
		if(flag_IqpAccpDetailInfo == 'yes'){
			document.getElementById('iqpAccpDetailInfo').style.display="none"; 
		}
		
		var prd_id = '<%=prd_id%>';
		if(prd_id =="200024"){
			var pm_cn_cont_no  = BizModifyHis.pm_cn_cont_no._getValue();
			var cn_cont_no  = BizModifyHis.cn_cont_no._getValue();
			var pm_security_rate  = BizModifyHis.pm_security_rate._getValue();
			var security_rate  = BizModifyHis.security_rate._getValue();
			if(pm_cn_cont_no == cn_cont_no){
				BizModifyHis.pm_cn_cont_no._obj._renderHidden(true);
				BizModifyHis.cn_cont_no._obj._renderHidden(true);
			}
			if(pm_security_rate == security_rate){
				BizModifyHis.pm_security_rate._obj._renderHidden(true);
				BizModifyHis.security_rate._obj._renderHidden(true);
			}
		}else if(prd_id =="400020" || prd_id =="400021"){
			var pm_cn_cont_no  = BizModifyHis.pm_cn_cont_no._getValue();
			var cn_cont_no  = BizModifyHis.cn_cont_no._getValue();
			if(pm_cn_cont_no == cn_cont_no){
				BizModifyHis.pm_cn_cont_no._obj._renderHidden(true);
				BizModifyHis.cn_cont_no._obj._renderHidden(true);
			}
			var pm_term = BizModifyHis.pm_term._getValue();
			var term = BizModifyHis.term._getValue(); 
			if(pm_term == term){
				BizModifyHis.pm_term._obj._renderHidden(true);
				BizModifyHis.term._obj._renderHidden(true);
			}
			var pm_security_rate  = BizModifyHis.pm_security_rate._getValue();
			var security_rate  = BizModifyHis.security_rate._getValue();
			if(pm_security_rate == security_rate){
				BizModifyHis.pm_security_rate._obj._renderHidden(true);
				BizModifyHis.security_rate._obj._renderHidden(true);
			}
		}else{
			var pm_cn_cont_no  = BizModifyHis.pm_cn_cont_no._getValue();
			var cn_cont_no  = BizModifyHis.cn_cont_no._getValue();
			var pm_term = BizModifyHis.pm_term._getValue();
			var term = BizModifyHis.term._getValue(); 
			var pm_pay_type = BizModifyHis.pm_pay_type._getValue();
			var pay_type = BizModifyHis.pay_type._getValue();
			var pm_repay_type = BizModifyHis.pm_repay_type._getValue();
			var repay_type = BizModifyHis.repay_type._getValue();
			var pm_interest_term = BizModifyHis.pm_interest_term._getValue();
			var interest_term = BizModifyHis.interest_term._getValue();
			var pm_limit_useed_type = BizModifyHis.pm_limit_useed_type._getValue();
			var limit_useed_type = BizModifyHis.limit_useed_type._getValue();
			var pm_repay_term = BizModifyHis.pm_repay_term._getValue();
			var repay_term = BizModifyHis.repay_term._getValue();
			var pm_repay_space = BizModifyHis.pm_repay_space._getValue();
			var repay_space = BizModifyHis.repay_space._getValue();
			
			if(pm_cn_cont_no == cn_cont_no){
				BizModifyHis.pm_cn_cont_no._obj._renderHidden(true);
				BizModifyHis.cn_cont_no._obj._renderHidden(true);
			}
			if(pm_term == term){
				BizModifyHis.pm_term._obj._renderHidden(true);
				BizModifyHis.term._obj._renderHidden(true);
			}
			if(pm_pay_type == pay_type){
				BizModifyHis.pm_pay_type._obj._renderHidden(true);
				BizModifyHis.pay_type._obj._renderHidden(true);
			}
			if(pm_repay_type == repay_type){
				BizModifyHis.pm_repay_type._obj._renderHidden(true);
				BizModifyHis.repay_type._obj._renderHidden(true);
			}
			if(pm_interest_term == interest_term){
				BizModifyHis.pm_interest_term._obj._renderHidden(true);
				BizModifyHis.interest_term._obj._renderHidden(true);
			}
			if(pm_repay_term == repay_term && pm_repay_space == repay_space){
				BizModifyHis.pm_repay_term._obj._renderHidden(true);
				BizModifyHis.repay_term._obj._renderHidden(true);
				BizModifyHis.pm_repay_space._obj._renderHidden(true);
				BizModifyHis.repay_space._obj._renderHidden(true);
			}
			
			if(pm_repay_type == repay_type && pm_interest_term == interest_term 
					&& pm_repay_term == repay_term && pm_repay_space == repay_space){
				document.getElementById('repayInfo').style.display="none"; 
			}
			if(pm_limit_useed_type == limit_useed_type){
				document.getElementById('otherInfo').style.display="none"; 
				BizModifyHis.pm_limit_useed_type._obj._renderHidden(true);
				BizModifyHis.limit_useed_type._obj._renderHidden(true);
			}

			//利率信息
			var pm_ir_accord_type = BizModifyHis.pm_ir_accord_type._getValue();
			var ir_accord_type = BizModifyHis.ir_accord_type._getValue();
			if(pm_ir_accord_type == ir_accord_type){
				BizModifyHis.pm_ir_accord_type._obj._renderHidden(true);
				BizModifyHis.ir_accord_type._obj._renderHidden(true);
			}
			
			var pm_ir_adjust_type = BizModifyHis.pm_ir_adjust_type._getValue();
			var ir_adjust_type = BizModifyHis.ir_adjust_type._getValue();
			if(pm_ir_adjust_type == ir_adjust_type){
				BizModifyHis.pm_ir_adjust_type._obj._renderHidden(true);
				BizModifyHis.ir_adjust_type._obj._renderHidden(true);
			}
			
			var pm_ir_float_type = BizModifyHis.pm_ir_float_type._getValue();
			var ir_float_type = BizModifyHis.ir_float_type._getValue();
			if(pm_ir_float_type == ir_float_type){
				BizModifyHis.pm_ir_float_type._obj._renderHidden(true);
				BizModifyHis.ir_float_type._obj._renderHidden(true);
			}
			
			var pm_ir_float_rate = BizModifyHis.pm_ir_float_rate._getValue();
			var ir_float_rate = BizModifyHis.ir_float_rate._getValue();
			if(pm_ir_float_rate == ir_float_rate){
				BizModifyHis.pm_ir_float_rate._obj._renderHidden(true);
				BizModifyHis.ir_float_rate._obj._renderHidden(true);
			}
			
			var pm_overdue_float_type = BizModifyHis.pm_overdue_float_type._getValue();
			var overdue_float_type = BizModifyHis.overdue_float_type._getValue();
			if(pm_overdue_float_type == overdue_float_type){
				BizModifyHis.pm_overdue_float_type._obj._renderHidden(true);
				BizModifyHis.overdue_float_type._obj._renderHidden(true);
			}
			
			var pm_overdue_rate = BizModifyHis.pm_overdue_rate._getValue();
			var overdue_rate = BizModifyHis.overdue_rate._getValue();
			if(pm_overdue_rate == overdue_rate){
				BizModifyHis.pm_overdue_rate._obj._renderHidden(true);
				BizModifyHis.overdue_rate._obj._renderHidden(true);
			}
			
			var pm_default_float_type = BizModifyHis.pm_default_float_type._getValue();
			var default_float_type = BizModifyHis.default_float_type._getValue();
			if(pm_default_float_type == default_float_type){
				BizModifyHis.pm_default_float_type._obj._renderHidden(true);
				BizModifyHis.default_float_type._obj._renderHidden(true);
			}
			
			var pm_default_rate = BizModifyHis.pm_default_rate._getValue();
			var default_rate = BizModifyHis.default_rate._getValue();	
			if(pm_default_rate == default_rate){
				BizModifyHis.pm_default_rate._obj._renderHidden(true);
				BizModifyHis.default_rate._obj._renderHidden(true);
			}
			if(pm_ir_accord_type == ir_accord_type && pm_ir_adjust_type == ir_adjust_type && pm_ir_float_type == ir_float_type
					&& pm_overdue_float_type == overdue_float_type && pm_overdue_rate == overdue_rate  && pm_default_float_type == default_float_type
					&& pm_default_rate == default_rate){
				document.getElementById('rateInfo').style.display="none"; 
			}
		}

		var flag_FreedomPayInfo = '<%=flag_FreedomPayInfo%>';
		if(flag_FreedomPayInfo == 'yes'){
			alert('业务还款计划发生变更！');
		}
	};
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div class='emp_gridlayout_title'></div>
	<%if("400020".equals(prd_id) || "400021".equals(prd_id)){ %>		
		<emp:gridLayout id="BizModifyHisGroup" title="保函修改历史对比值列表" maxColumn="2">
			<emp:text id="BizModifyHis.cont_no" label="合同编号" readonly="true" colSpan="2"/>
			<emp:text id="BizModifyHis.pm_cn_cont_no" label="中文合同编号(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.cn_cont_no" label="中文合同编号(修改后)" readonly="true"/>
			<emp:text id="BizModifyHis.pm_term" label="期限(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.term" label="期限(修改后)" readonly="true"/>
			<emp:text id="BizModifyHis.pm_security_rate" label="保证金比例(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.security_rate" label="保证金比例(修改后)" dataType="Rate" readonly="true"/>
		</emp:gridLayout>
		<div id="pubBailInfo"> 
		<div class='emp_gridlayout_title'>保证金信息(修改前)</div>
		<emp:button id="viewPubBailInfoHis" label="查看" />
		<emp:table icollName="PubBailInfoHisList" pageMode="false" url="#">
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="cont_no" label="合同编号" hidden="true"/>
			<emp:text id="cus_id" label="客户码" hidden="true"/>   
			<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
			<emp:text id="bail_acct_no" label="保证金账户账号" />
			<emp:text id="bail_acct_name" label="保证金账户名称" />
			<emp:text id="bail_type" label="保证金类型" dictname="STD_PUB_BAIL_TYPE"/>  
			<emp:text id="open_org_displayname" label="开户机构" />  
		</emp:table> 
		<div class='emp_gridlayout_title'>保证金信息(修改后)</div>
		<emp:button id="viewPubBailInfoTmp" label="查看" />
		<emp:table icollName="PubBailInfoTmpList" pageMode="false" url="#">
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="cont_no" label="合同编号" hidden="true"/>
			<emp:text id="cus_id" label="客户码" hidden="true"/>   
			<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
			<emp:text id="bail_acct_no" label="保证金账户账号" />
			<emp:text id="bail_acct_name" label="保证金账户名称" />
			<emp:text id="bail_type" label="保证金类型" dictname="STD_PUB_BAIL_TYPE"/>  
			<emp:text id="open_org_displayname" label="开户机构" />  
		</emp:table>
		</div>		
	<%}else if("200024".equals(prd_id)){ %>
		<emp:gridLayout id="BizModifyHisGroup" title="基本信息" maxColumn="2">
			<emp:text id="BizModifyHis.cont_no" label="合同编号" readonly="true" colSpan="2"/>
			<emp:text id="BizModifyHis.pm_cn_cont_no" label="中文合同编号(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.cn_cont_no" label="中文合同编号(修改后)" readonly="true"/>
			<emp:text id="BizModifyHis.pm_security_rate" label="保证金比例(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.security_rate" label="保证金比例(修改后)" dataType="Rate" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" title="汇票基本信息" maxColumn="3">
			<emp:text id="IqpAccpInfo.actp_org_no" label="签发行行号" readonly="true" />
			<emp:text id="IqpAccpInfo.actp_org_name" label="签发行行名" readonly="true" />
		</emp:gridLayout>
		<div id="iqpAccpDetailInfo">
		<div class='emp_gridlayout_title'>银行承兑汇票明细</div>
			<emp:table icollName="IqpAccpDetailInfoList" pageMode="false" url="#">
			<emp:text id="pm_term" label="期限(修改前)" />
			<emp:text id="term" label="期限(修改后)" />
			<emp:text id="pm_clt_person" label="收款人(修改前)"  />
			<emp:text id="clt_person" label="收款人(修改后)" />
		</emp:table>
		</div>
		<div id="pubBailInfo"> 
		<div class='emp_gridlayout_title'>保证金信息(修改前)</div>
		<emp:button id="viewPubBailInfoHis" label="查看" />
		<emp:table icollName="PubBailInfoHisList" pageMode="false" url="#">
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="cont_no" label="合同编号" hidden="true"/>
			<emp:text id="cus_id" label="客户码" hidden="true"/>   
			<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
			<emp:text id="bail_acct_no" label="保证金账户账号" />
			<emp:text id="bail_acct_name" label="保证金账户名称" />
			<emp:text id="bail_type" label="保证金类型" dictname="STD_PUB_BAIL_TYPE"/>  
			<emp:text id="open_org_displayname" label="开户机构" />  
		</emp:table> 
		<div class='emp_gridlayout_title'>保证金信息(修改后)</div>
		<emp:button id="viewPubBailInfoTmp" label="查看" />
		<emp:table icollName="PubBailInfoTmpList" pageMode="false" url="#">
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="cont_no" label="合同编号" hidden="true"/>
			<emp:text id="cus_id" label="客户码" hidden="true"/>   
			<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
			<emp:text id="bail_acct_no" label="保证金账户账号" />
			<emp:text id="bail_acct_name" label="保证金账户名称" />
			<emp:text id="bail_type" label="保证金类型" dictname="STD_PUB_BAIL_TYPE"/>  
			<emp:text id="open_org_displayname" label="开户机构" />  
		</emp:table>
		</div>
		<div id="appendTerms">
		<div class='emp_gridlayout_title'>附加条款(修改前)</div>
		<emp:button id="viewIqpAppendTermsHis" label="查看" />
		<emp:table icollName="IqpAppendTermsHisList" pageMode="false" url="#">
			<emp:text id="append_terms_pk" label="主键" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="fee_code" label="费用描述" dictname="STD_ZB_FEE_CODE"/>
			<emp:text id="fee_type" label="费用类型" dictname="STD_ZB_FEE_MODE"/>
			<emp:text id="fee_amt" label="费用总金额" dataType="Currency"/>
			<emp:text id="fee_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="fee_rate" label="费用比率" dataType="Rate"/>
			<emp:text id="is_cycle_chrg" label="是否周期性收费" dictname="STD_ZX_YES_NO" />
			<emp:text id="modify_rel_serno" label="打回业务修改流水号" hidden="true"/>
		</emp:table>   		
		<div class='emp_gridlayout_title'>附加条款(修改后)</div>
		<emp:button id="viewIqpAppendTermsTmp" label="查看" />
		<emp:table icollName="IqpAppendTermsTmpList" pageMode="false" url="#">
			<emp:text id="append_terms_pk" label="主键" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="fee_code" label="费用描述" dictname="STD_ZB_FEE_CODE"/>
			<emp:text id="fee_type" label="费用类型" dictname="STD_ZB_FEE_MODE"/>
			<emp:text id="fee_amt" label="费用总金额" dataType="Currency"/>
			<emp:text id="fee_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="fee_rate" label="费用比率" dataType="Rate"/>
			<emp:text id="is_cycle_chrg" label="是否周期性收费" dictname="STD_ZX_YES_NO" />
			<emp:text id="modify_rel_serno" label="打回业务修改流水号" hidden="true"/>
		</emp:table>
		</div>
	<%}else{ %>
		<emp:gridLayout id="BizModifyHisGroup" title="基本信息" maxColumn="2">
			<emp:text id="BizModifyHis.cont_no" label="合同编号" readonly="true" colSpan="2"/>
			<emp:text id="BizModifyHis.pm_cn_cont_no" label="中文合同编号(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.cn_cont_no" label="中文合同编号(修改后)" readonly="true"/>
			<emp:text id="BizModifyHis.pm_term" label="期限(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.term" label="期限(修改后)" readonly="true"/>
			<emp:select id="BizModifyHis.pm_pay_type" label="支付方式(修改前)" dictname="STD_IQP_PAY_TYPE" readonly="true"/>
			<emp:select id="BizModifyHis.pay_type" label="支付方式(修改后)" dictname="STD_IQP_PAY_TYPE" readonly="true"/>
		</emp:gridLayout>
		<div id="rateInfo">
		<emp:gridLayout id="" title="利率信息" maxColumn="2">
			<emp:select id="BizModifyHis.pm_ir_accord_type" label="利率依据方式(修改前)" readonly="true" dictname="STD_ZB_IR_ACCORD_TYPE"/>
			<emp:select id="BizModifyHis.ir_accord_type" label="利率依据方式(修改后)" readonly="true" dictname="STD_ZB_IR_ACCORD_TYPE"/>
			<emp:select id="BizModifyHis.pm_ir_adjust_type" label="利率调整方式(修改前)" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
			<emp:select id="BizModifyHis.ir_adjust_type" label="利率调整方式(修改后)" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
			<emp:select id="BizModifyHis.pm_ir_float_type" label="利率浮动方式(修改前)" dictname="STD_RATE_FLOAT_TYPE" readonly="true"/>
			<emp:select id="BizModifyHis.ir_float_type" label="利率浮动方式(修改后)" dictname="STD_RATE_FLOAT_TYPE" readonly="true"/>
			<emp:text id="BizModifyHis.pm_ir_float_rate" label="利率浮动比(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.ir_float_rate" label="利率浮动比(修改后)" dataType="Rate" readonly="true"/>
			
			<emp:text id="BizModifyHis.pm_overdue_float_type" label="逾期利率浮动方式(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.overdue_float_type" label="逾期利率浮动方式(修改后)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.pm_overdue_rate" label="逾期利率浮动比(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.overdue_rate" label="逾期利率浮动比(修改后)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.pm_default_float_type" label="违约利率浮动方式(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.default_float_type" label="违约利率浮动方式(修改后)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.pm_default_rate" label="违约利率浮动比(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis.default_rate" label="违约利率浮动比(修改后)" dataType="Rate" readonly="true"/>
		</emp:gridLayout>
		</div>
		<div id="repayInfo">
		<emp:gridLayout id="" title="还款方式信息修改历史对比" maxColumn="2">
			<emp:text id="BizModifyHis.pm_repay_type" label="还款方式(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis.repay_type" label="还款方式(修改后)"  readonly="true"/>
			<emp:select id="BizModifyHis.pm_interest_term" label="计息周期(修改前)" dictname="STD_IQP_RATE_CYCLE" readonly="true"/>
			<emp:select id="BizModifyHis.interest_term" label="计息周期(修改后)" dictname="STD_IQP_RATE_CYCLE" readonly="true"/>
			<emp:select id="BizModifyHis.pm_repay_term" label="还款间隔周期(修改前)" required="true" dictname="STD_BACK_CYCLE" readonly="true"/>
			<emp:text id="BizModifyHis.pm_repay_space" cssElementClass="emp_currency_text_readonly" label="还款间隔(修改前)" dataType="Int" readonly="true"/>
			<emp:select id="BizModifyHis.repay_term" label="还款间隔周期(修改后)" required="true" dictname="STD_BACK_CYCLE" readonly="true"/>
			<emp:text id="BizModifyHis.repay_space" cssElementClass="emp_currency_text_readonly" label="还款间隔(修改后)" dataType="Int" readonly="true"/>
		</emp:gridLayout>
		</div>
	    <div id="otherInfo">
		<emp:gridLayout id="" maxColumn="2" title="其他信息"> 
			<emp:select id="BizModifyHis.pm_limit_useed_type" label="额度占用来源(修改前)" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
			<emp:select id="BizModifyHis.limit_useed_type" label="额度占用来源(修改后)" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
		</emp:gridLayout>
		</div>
	<%} %>
	<div id="iqpCusAcctInfo">
		<div class='emp_gridlayout_title'>受托支付账户信息(修改前)</div>
		<emp:button id="viewIqpCusAcctHis" label="查看" />
		<emp:table icollName="IqpCusAcctHisList" pageMode="false"  url="#">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="pk_id" label="pkid" hidden="true"/> 
		<emp:text id="acct_no" label="账户账号" />
		<emp:text id="acct_name" label="账户名称" />
		<emp:text id="acct_attr" label="账户属性" dictname="STD_ZB_BR_ID_ATTR" />
		<emp:text id="is_this_org_acct" label="是否本行账户" dictname="STD_ZX_YES_NO" />
		<emp:text id="opac_org_no" label="开户行行号" hidden="true"/>
		<emp:text id="opan_org_name" label="开户行行名" />
		<emp:text id="pay_amt" label="支付金额" hidden="true"/>	
		</emp:table>	
		<div class='emp_gridlayout_title'>受托支付账户信息(修改后)</div>
		<emp:button id="viewIqpCusAcctTmp" label="查看" />
		<emp:table icollName="IqpCusAcctTmpList" pageMode="false"  url="#">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="pk_id" label="pkid" hidden="true"/> 
		<emp:text id="acct_no" label="账户账号" />
		<emp:text id="acct_name" label="账户名称" />
		<emp:text id="acct_attr" label="账户属性" dictname="STD_ZB_BR_ID_ATTR" />
		<emp:text id="is_this_org_acct" label="是否本行账户" dictname="STD_ZX_YES_NO" />
		<emp:text id="opac_org_no" label="开户行行号" hidden="true"/>
		<emp:text id="opan_org_name" label="开户行行名" />
		<emp:text id="pay_amt" label="支付金额" hidden="true"/>	
		</emp:table>
	</div>
	<div align="center">
		<br>
		<%if(!"1".equals(wf_flag)){ %>
			<emp:button id="close" label="关闭" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
