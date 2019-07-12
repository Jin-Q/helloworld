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
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_area_type = (String)context.getDataValue("LmtAppBizArea.biz_area_type");
	String hiddReturn =request.getParameter("hiddReturn");//流程中隐藏返回按钮
%>
<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryLmtAppBizAreaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
	function doOnload(){
		changeSharedScope();
		var appId = LmtAppBizArea.serno._getValue();
		var biz_area_type ="<%=biz_area_type%>" ; 

		var url_add = '<emp:url action="addLmtAppBizAreaRecord.do"/>?biz_area_type=' + biz_area_type;
		url_add = EMPTools.encodeURI(url_add);
		document.getElementById("submitForm").action = url_add;
	}
	
	function changeSharedScope(){
		var share_range = LmtAppBizArea.share_range._getValue();
		if( share_range == '' || share_range == '1' ){
			LmtAppBizArea.belg_org._obj._renderHidden(true);
			LmtAppBizArea.belg_org_displayname._obj._renderHidden(true);
			LmtAppBizArea.belg_org._obj._renderRequired(false);
			LmtAppBizArea.belg_org_displayname._obj._renderRequired(false);
		}else{
			LmtAppBizArea.belg_org_displayname._obj._renderHidden(false);
			LmtAppBizArea.belg_org_displayname._obj._renderRequired(true);

			LmtAppBizArea.belg_org._obj._renderHidden(false);
			LmtAppBizArea.belg_org._obj._renderRequired(true);
		}
	}
	//返回主管机构
	function getOrganName(data){
		LmtAppBizArea.belg_org._setValue(data[0]);
		LmtAppBizArea.belg_org_displayname._setValue(data[1]);
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="圈商准入额度申请" id="main_tabs" needFlush="true" initial="true">
		<emp:form id="submitForm" action="updateLmtAppBizAreaRecord.do" method="POST">
			<emp:gridLayout id="LmtAppBizAreaGroup" title="圈商准入额度申请" maxColumn="2">
				<emp:text id="LmtAppBizArea.serno" label="业务流水号" maxlength="40" required="true" hidden="true" colSpan="2"/>
				<emp:select id="LmtAppBizArea.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="changeSharedScope()"/>
				<emp:pop id="LmtAppBizArea.belg_org"  label="所属机构" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getOrganName" required="true" />
				<emp:textarea id="LmtAppBizArea.belg_org_displayname"  label=" " colSpan="2" readonly="true" />
				
				<emp:text id="LmtAppBizArea.biz_area_name" label="圈商名称" maxlength="40" required="true" />
				<emp:select id="LmtAppBizArea.biz_area_type" label="圈商类型" required="false" dictname="STD_LMT_BIZ_AREA_TYPE" readonly="true"/>
				<emp:select id="LmtAppBizArea.cur_type" label="授信币种" readonly="true" required="false" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY"/>
				<emp:text id="LmtAppBizArea.lmt_totl_amt" label="授信总额度(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppBizArea.single_max_amt" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:checkbox disabled="true" id="LmtAppBizArea.guar_type" label="授信合作担保方式" dictname="STD_LMT_CRD_COOP_LN_TYPE" required="true" colSpan="2" layout="false"/>
				<emp:select id="LmtAppBizArea.term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtAppBizArea.term" label="授信期限" maxlength="8" required="true" />
				<emp:text id="LmtAppBizArea.app_date" label="申请日期" maxlength="10" required="false" />
				<emp:text id="LmtAppBizArea.over_date" label="办结日期" maxlength="10" required="false" hidden="true"/>
			</emp:gridLayout>
		<%if("0".equals(biz_area_type)){ %>
			<emp:gridLayout id="LmtAppBizAreaComnGroup" maxColumn="2" title="目标客户群（一般圈商）">
				<emp:text id="LmtAppBizAreaComn.serno" label="业务编号" maxlength="40" required="true"  hidden="true"/>
				<emp:select id="LmtAppBizAreaComn.shop_type" label="商户类型" required="true" dictname="STD_LMT_BIZ_TYPE" />
				<emp:textarea id="LmtAppBizAreaComn.main_prd" label="主要产品" maxlength="200" required="false" colSpan="2" />
				<emp:select id="LmtAppBizAreaComn.oper_model" label="经营规模" required="false" dictname="STD_LMT_BIZ_SIZE" />
				<emp:textarea id="LmtAppBizAreaComn.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
		<%}else if("1".equals(biz_area_type)){ %>
			<emp:gridLayout id="LmtAppBizAreaCoreGroup" maxColumn="2" title="目标客户群（核心企业）">
				<emp:text id="LmtAppBizAreaCore.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
				<emp:select id="LmtAppBizAreaCore.core_con_type" label="核心企业类型" required="false" dictname="STD_LMT_BIZ_TYPE" />
				<emp:text id="LmtAppBizAreaCore.year_sale_amt" label="年供货销售额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="LmtAppBizAreaCore.fore_debt_bal" label="平均应收(付)账款余额" maxlength="18" required="false" dataType="Currency" />
				<emp:text id="LmtAppBizAreaCore.coop_year" label="合作年限" maxlength="6" required="false" />
				<emp:textarea id="LmtAppBizAreaCore.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
		<%}else{ %>
			<div  class='emp_gridlayout_title'>目标客户群（超市百货类）</div>
			<emp:table icollName="LmtAppBizAreaSupmkList" editable="true" pageMode="false" url="">
				<emp:text id="supmk_serno" label="流水号" readonly="true" hidden="true"/>
				<emp:text id="serno" label="业务编号" hidden="true"/>
				<emp:select id="oper_trade" label="经营行业" dictname="STD_LMT_BIZ_INDUS" />
				<emp:select id="oper_model" label="经营规模" dictname="STD_LMT_BIZ_SIZE" />
				<emp:text id="trade_rank" label="行业排名" dataType="Int"/>
				<emp:text id="provid_year" label="供货年限" dataType="Int"/>
				<emp:text id="net_asset" label=" 净资产" dataType="Currency" />
				<emp:text id="other_cond" label="其他准入条件" />
			</emp:table>
		<%} %>
			<emp:gridLayout id="LmtAppBizAreaGroup" title="机构信息" maxColumn="2">
				<emp:text id="LmtAppBizArea.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAppBizArea.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAppBizArea.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAppBizArea.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
				
				<emp:text id="LmtAppBizArea.manager_id_displayname" label="责任人" required="false" />
				<emp:text id="LmtAppBizArea.manager_br_id_displayname" label="责任机构" required="false" />
				<emp:text id="LmtAppBizArea.input_id_displayname" label="登记人" required="false" />
				<emp:text id="LmtAppBizArea.input_br_id_displayname" label="登记机构" required="false" />
				
				<emp:text id="LmtAppBizArea.input_date" label="登记日期" maxlength="10" required="false" />
				<emp:select id="LmtAppBizArea.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true"/>
			</emp:gridLayout>
		</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if(hiddReturn==null||"".equals(hiddReturn)){ %>
			<emp:button id="return" label="返回列表" />
		<%} %>
	</div>	
</body>
</html>
</emp:page>
