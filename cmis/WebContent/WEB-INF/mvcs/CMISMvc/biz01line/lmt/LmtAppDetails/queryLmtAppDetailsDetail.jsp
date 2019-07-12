<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	request.setAttribute("canwrite","");
	String app_type = context.getDataValue("app_type").toString();
	String sub_type = context.getDataValue("sub_type").toString();
	String belg_line = (String)context.getDataValue("BelgLine");
	String update_flag = "01";
	if(context.containsKey("LmtAppDetails.update_flag")){
		update_flag = (String)context.getDataValue("LmtAppDetails.update_flag");
	}
%>

<script type="text/javascript">
	
	//返回方法
	function doReturn(){
		var url = "";
		var belgLine = '<%=belg_line%>';
		if(belgLine!=null&&belgLine=='BL300'){
			//如果条线为个人返回个人详情页面
			url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
		}else{
			//返回授信分项列表页面
			url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doload(){
		var green_indus = LmtAppDetails.green_indus._getValue();
		if(green_indus != '2'&&green_indus!=''){
			LmtAppDetails.green_pro_type._obj._renderHidden(false);
			LmtAppDetails.green_pro_type._obj._renderRequired(true);
		}else{
			LmtAppDetails.green_pro_type._setValue('');
			LmtAppDetails.green_pro_type._obj._renderHidden(true);
			LmtAppDetails.green_pro_type._obj._renderRequired(false);
		}
		/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 begin**/
		var guarType = LmtAppDetails.guar_type._getValue();
		var belgLine = '<%=belg_line%>';
		//	if(guarType =="100"){//XD150715055 2015-07-23 Edited by FCL 新增个人N+1显示
		//	LmtAppDetails.is_opn_mode._obj._renderHidden(false);
		//	LmtAppDetails.is_opn_mode._obj._renderRequired(true);
		//}else{
		//	LmtAppDetails.is_opn_mode._obj._renderHidden(true);
		//	LmtAppDetails.is_opn_mode._obj._renderRequired(false);
		//}
		/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 end**/
	}
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
<body class="page_content" onload="doload()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信分项基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:gridLayout id="LmtAppDetailsGroup" title="授信分项基本信息" maxColumn="2">
			<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<% if("05".equals(sub_type)){ //变更  %>
			<emp:text id="LmtAppDetails.core_corp_cus_id" label="核心企业客户码 " required="false" />
			<emp:select id="LmtAppDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY" />
			<emp:text id="LmtAppDetails.core_corp_cus_id_displayname" label="核心企业客户名称" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly" />
			<%} %>
			
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:text id="LmtAppDetails.org_limit_code" label="授信额度编号" maxlength="32" required="false" />
			<emp:text id="LmtAppDetails.limit_name_displayname" label="额度品种名称"  required="true" />
			<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="40" required="true" hidden="true"/>
			<emp:text id="LmtAppDetails.prd_id" label="适用产品编号" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppDetails.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="true" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
			<!-- XD150715055 2015-07-23 Edited by FCL 新增个人N+1显示 begin -->
			<% if("BL300".equals(belg_line)){ %>
			<emp:select id="LmtAppDetails.is_opn_mode" label="是否为 N+1或1+N" dictname="STD_ZX_YES_NO" hidden="true"/>
			<%}else{ %>
			<emp:select id="LmtAppDetails.is_opn_mode" label="是否为1+N模式" dictname="STD_ZX_YES_NO" hidden="true"/>
			<%} %>
			<!-- XD150715055 2015-07-23 Edited by FCL 新增个人N+1显示 end -->
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" />
			<% if("02".equals(update_flag)){ //变更  %>
			<emp:date id="LmtAppDetails.start_date" label="原授信起始日" required="false" readonly="true" />
			<emp:date id="LmtAppDetails.end_date" label="原授信到期日" required="false" readonly="true" />
			<emp:select id="LmtAppDetails.is_adj_term" label="是否调整期限" required="false" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2"/>
			<%} %>
			<emp:select id="LmtAppDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppDetails.term" label="授信期限" maxlength="5" required="false" dataType="Int" />
			
			<!-- 绿色产业类型  2013-12-04 唐顺岩-->
			<emp:pop id="LmtAppDetails.green_indus_displayname" label="绿色产业类型" required="true" url="showDicTree.do?dicTreeTypeId=STD_ZB_GREEN_INDUS" returnMethod="greenIndusReturn" cssElementClass="emp_field_text_long_readonly" colSpan="2" />
			<emp:select id="LmtAppDetails.green_pro_type" label="绿色授信项目类型" required="false" dictname="STD_GREEN_PRO_TYPE"  readonly="true" cssFakeInputClass="emp_field_select_select1"/>
			<emp:text id="LmtAppDetails.green_indus" label="绿色产业类型" required="true" hidden="true"/>
			
			<emp:select id="LmtAppDetails.update_flag" label="修改类型" required="false" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true" hidden="true"/>
			<emp:text id="LmtAppDetails.ori_crd_amt" label="原有授信金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="LmtAppDetails.lrisk_type" label="低风险业务类型" maxlength="16" hidden="true" readonly="true"/>
	</emp:gridLayout>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
	<div align="center">
		<br>
		<input type="button" value="返回到列表页面" class="button100" onclick="doReturn(this)">
	</div>
</body>
</html>
</emp:page>
