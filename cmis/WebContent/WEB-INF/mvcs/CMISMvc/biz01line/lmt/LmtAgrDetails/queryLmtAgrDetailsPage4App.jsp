<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 	
    /**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 ,1+N担保模式 begin**/
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String belg_line="";
	if(context.containsKey("belg_line")){
		belg_line = (String)context.getDataValue("belg_line");
	}
	/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 ,1+N担保模式 end**/
%>
<emp:page>
<html>
<head>
<title>授信台账查看页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	function doReturn() {
		var serno = LmtAgrDetails.serno._getValue();
		var type = '${context.type}';
		var url = "";
		if("app"==type){
			url = "<emp:url action='queryLmtAppDetailsList.do'/>&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}";
		}else if("indiv"==type){
			url = "<emp:url action='queryLmtAgrIndivDetailsList.do'/>&agr_no=${context.LmtAgrDetails.agr_no}&lrisk_type=${context.lrisk_type}";
		}else if("indivApp"==type){   //个人额度变更是查看台账
			url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
		}else{
			url = "<emp:url action='queryLmtAgrDetailsList.do'/>&subConndition=${context.subConndition}&restrict_tab=false&lrisk_type=${context.lrisk_type}";
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		//根据授信分项类别区分是否需要展示 核心企业责任
		var sub_type = LmtAgrDetails.sub_type._getValue();
		if("05"==sub_type){  //供应链授信
			LmtAgrDetails.core_corp_cus_id._obj._renderHidden(false);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderHidden(false);

			LmtAgrDetails.core_corp_cus_id._obj._renderRequired(true);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderRequired(true);
			LmtAgrDetails.core_corp_duty._obj._renderRequired(true);
			
			LmtAgrDetails.core_corp_duty._obj._renderHidden(false);
		}else if("03"==sub_type){  //联保授信
			LmtAgrDetails.green_indus_displayname._obj._renderHidden(true);
			//LmtAgrDetails.lmt_type._obj._renderHidden(true);
		}
		//给客户、核心企业加查看按钮
		LmtAgrDetails.cus_id._obj.addOneButton('viewCusInfo','查看',viewCusInfo);
		LmtAgrDetails.core_corp_cus_id._obj.addOneButton('viewCoreCusInfo','查看',viewCoreCusInfo);
		var green_indus = LmtAgrDetails.green_indus._getValue();
		if(green_indus != '2'&&green_indus!=''){
			LmtAgrDetails.green_pro_type._obj._renderHidden(false);
			LmtAgrDetails.green_pro_type._obj._renderRequired(true);
		}else{
			LmtAgrDetails.green_pro_type._setValue('');
			LmtAgrDetails.green_pro_type._obj._renderHidden(true);
			LmtAgrDetails.green_pro_type._obj._renderRequired(false);
		}
		/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置,1+N担保模式 begin**/
		var guarType = LmtAgrDetails.guar_type._getValue();
		var belgLine = '<%=belg_line%>';
		if(guarType =="100"){
			LmtAgrDetails.is_opn_mode._obj._renderHidden(false);
			LmtAgrDetails.is_opn_mode._obj._renderRequired(true);
		}else{
			LmtAgrDetails.is_opn_mode._obj._renderHidden(true);
			LmtAgrDetails.is_opn_mode._obj._renderRequired(false);
		}
		/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置,1+N担保模式 end**/
	}
	
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrDetails.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCusInfo','height=600,width=1024,top=70,left=60,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//核心企业客户信息查
	function viewCoreCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrDetails.core_corp_cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCoreCusInfo','height=600,width=1024,top=70,left=60,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
<style type="text/css">
.emp_field_select_select1 {
	width: 629px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
</head>
<body class="page_content" onload="doLoad()">
		<emp:gridLayout id="LmtAgrDetailsGroup" title="授信额度台账" maxColumn="2">			
			<emp:text id="LmtAgrDetails.cus_id" label="客户码" maxlength="32" required="true" />
			<emp:text id="LmtAgrDetails.agr_no" label="授信协议编号" maxlength="40" required="true" />
			<emp:text id="LmtAgrDetails.cus_id_displayname" label="客户名称" colSpan="2" cssElementClass="emp_field_text_long_readonly" readonly="true"/>
			<emp:select id="LmtAgrDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" hidden="true"/>
			<emp:select id="LmtAgrDetails.sub_type" label="分项类别" readonly="true" required="true" dictname="STD_LMT_PROJ_TYPE"/>
			
			<emp:text id="LmtAgrDetails.core_corp_cus_id" label="核心企业客户码 " required="false"  hidden="true"/>
			<emp:select id="LmtAgrDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY"  hidden="true" />
			<emp:text id="LmtAgrDetails.core_corp_cus_id_displayname" label="核心企业客户名称" required="false"  colSpan="2" cssElementClass="emp_field_text_long_readonly" hidden="true"/>
			
			<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号"  required="true" readonly="true"/>
			<emp:text id="LmtAgrDetails.limit_name_displayname" label="额度品种名称" required="true" />
			<emp:text id="LmtAgrDetails.limit_name" label="额度品种名称" required="true" maxlength="40" hidden="true"/>
			<emp:text id="LmtAgrDetails.prd_id" label="适用产品编号" maxlength="200" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly" />
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称" required="true"  colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAgrDetails.lrisk_type" label="低风险业务类型"  dictname="STD_ZB_LRISK_TYPE" required="true"/>
			<emp:select id="LmtAgrDetails.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" />
			<emp:text id="LmtAgrDetails.crd_amt" label="授信金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.enable_amt" label="启用金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtAgrDetails.froze_amt" label="冻结金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true"/>
			<emp:text id="LmtAgrDetails.bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true"/>
			<emp:select id="LmtAgrDetails.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" />
			<!-- XD150715055 2015-07-23 Edited by FCL 新增个人N+1显示 begin -->
			<% if("BL300".equals(belg_line)){ %>
			<emp:select id="LmtAgrDetails.is_opn_mode" label="是否为 N+1或1+N" dictname="STD_ZX_YES_NO" hidden="true"/>
			<%}else{ %>
			<emp:select id="LmtAgrDetails.is_opn_mode" label="是否为1+N模式" dictname="STD_ZX_YES_NO" hidden="true"/>
			<%} %>
			<!-- XD150715055 2015-07-23 Edited by FCL 新增个人N+1显示 end -->
			<emp:select id="LmtAgrDetails.is_pre_crd" label="是否预授信" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:select id="LmtAgrDetails.term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" required="true" />
			<emp:text id="LmtAgrDetails.term" label="授信期限" maxlength="2" required="true" />
			<emp:date id="LmtAgrDetails.start_date" label="授信起始日" required="true" />
			<emp:date id="LmtAgrDetails.end_date" label="授信到期日" required="true" /> 
			<!-- 绿色产业类型  2013-12-04 唐顺岩-->
			<emp:pop id="LmtAgrDetails.green_indus_displayname" label="绿色产业类型" required="true" url="" returnMethod="" cssElementClass="emp_field_text_long_readonly" colSpan="2" />
			<emp:select id="LmtAgrDetails.green_pro_type" label="绿色授信项目类型" required="false" dictname="STD_GREEN_PRO_TYPE"  readonly="true" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="LmtAgrDetails.is_adj_term" label="是否调整期限" dictname="STD_ZX_YES_NO" required="true" hidden="true"/>
			<emp:select id="LmtAgrDetails.lmt_status" label="额度状态" required="true" dictname="STD_LMT_STATUS" />
			<emp:text id="LmtAgrDetails.green_indus" label="绿色产业类型" hidden="true"/>
			<emp:text id="LmtAgrDetails.cus_type" label="客户类别" maxlength="3" required="false" hidden="true"/>
			<emp:text id="LmtAgrDetails.serno" label="申请流水号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="LmtAgrDetails.bail_rate" label="保证金缴存比例" maxlength="16" required="false" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
    	<input type="button" class="button80" id="btReturn" onclick="doReturn()" value="关闭"> 
    	<!-- <input type="button" class="button80" id="btClose" onclick="window.close()"  value="关闭">  -->
	</div>
</body>
</html>
</emp:page>
