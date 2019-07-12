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
	String biz_area_type = (String)context.getDataValue("biz_area_type");
	String type = "";
	if(context.containsKey("type")){
		type = (String)context.getDataValue("type");
	}
%>
<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryLmtAgrBizAreaList.do"/>'; 
		url = EMPTools.encodeURI(url);
	//	window.palocation=url;
		window.location=url;
	};
	/*--user code begin--*/
	function doOnload(){
		changeSharedScope();
	}

	function changeSharedScope(){
		var share_range = LmtAgrBizArea.share_range._getValue();
		if( share_range == '' || share_range == '1' ){
			LmtAgrBizArea.belg_org._obj._renderHidden(true);
			LmtAgrBizArea.belg_org_displayname._obj._renderHidden(true);
			LmtAgrBizArea.belg_org._obj._renderRequired(false);
			LmtAgrBizArea.belg_org_displayname._obj._renderRequired(false);
		}else{
			LmtAgrBizArea.belg_org_displayname._obj._renderHidden(false);
			LmtAgrBizArea.belg_org_displayname._obj._renderRequired(true);

			LmtAgrBizArea.belg_org._obj._renderHidden(false);
			LmtAgrBizArea.belg_org._obj._renderRequired(true);
		}
	}
	//返回主管机构
	function getOrganName(data){
		LmtAgrBizArea.belg_org._setValue(data[0]);
		LmtAgrBizArea.belg_org_displayname._setValue(data[1]);
	}
	function setManagerId(data){
		LmtAgrBizArea.manager_id._setValue(data.actorno._getValue());
		LmtAgrBizArea.manager_id_displayname._setValue(data.actorname._getValue());
	}
	//返回主管机构
	function getOrganName2(data){
		LmtAgrBizArea.manager_br_id._setValue(data.organno._getValue());
		LmtAgrBizArea.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function doClose(){
        window.close();
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="圈商额度信息" id="main_tabs" needFlush="true" initial="true">
		<emp:form id="submitForm" action="getLmtAgrBizAreaSupmkUpdatePage.do" method="POST">
			<emp:gridLayout id="LmtAgrBizAreaGroup" title="圈商额度信息" maxColumn="2">
				<emp:text id="LmtAgrBizArea.agr_no" label="圈商编号" maxlength="40" required="true" hidden="true" colSpan="2"/>
				<emp:text id="LmtAgrBizArea.serno" label="业务流水号" maxlength="40" required="true" hidden="true" colSpan="2"/>
				<emp:select id="LmtAgrBizArea.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="changeSharedScope()"/>
				<emp:pop id="LmtAgrBizArea.belg_org"  label="所属机构" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getOrganName" required="true" />
				<emp:textarea id="LmtAgrBizArea.belg_org_displayname"  label=" " colSpan="2" readonly="true" />
				<emp:text id="LmtAgrBizArea.biz_area_name" label="圈商名称" maxlength="40" required="true" />
				<emp:select id="LmtAgrBizArea.biz_area_type" label="圈商类型" required="false" dictname="STD_LMT_BIZ_AREA_TYPE" readonly="true"/>
				<emp:select id="LmtAgrBizArea.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY"/>
				<emp:text id="LmtAgrBizArea.lmt_totl_amt" label="授信总额度(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrBizArea.already_used" label="剩余额度" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrBizArea.single_max_amt" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrBizArea.totl_cus" label="总户数" maxlength="16" cssElementClass="emp_currency_text_readonly"/>
				<emp:checkbox id="LmtAgrBizArea.guar_type" disabled="true" label="授信合作担保方式" dictname="STD_LMT_CRD_COOP_LN_TYPE" required="true" colSpan="2" layout="false"/>
				<emp:date id="LmtAgrBizArea.start_date" label="授信起始日期" required="false"  />
				<emp:date id="LmtAgrBizArea.end_date" label="授信到期日期" required="false"  />
			</emp:gridLayout>
			<%if("0".equals(biz_area_type)){ %>
			<emp:gridLayout id="LmtAgrBizAreaComnGroup" maxColumn="2" title="目标客户群（一般圈商）">
				<emp:text id="LmtAgrBizAreaComn.serno" label="业务编号" maxlength="40" required="true"  hidden="true"/>
				<emp:select id="LmtAgrBizAreaComn.shop_type" label="商户类型" required="true" dictname="STD_LMT_BIZ_TYPE" />
				<emp:textarea id="LmtAgrBizAreaComn.main_prd" label="主要产品" maxlength="200" required="false" colSpan="2" />
				<emp:select id="LmtAgrBizAreaComn.oper_model" label="经营规模" required="false" dictname="STD_LMT_BIZ_SIZE" />
				<emp:textarea id="LmtAgrBizAreaComn.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
			<%}else if("1".equals(biz_area_type)){ %>
			<emp:gridLayout id="LmtAgrBizAreaCoreGroup" maxColumn="2" title="目标客户群（核心企业）">
				<emp:text id="LmtAgrBizAreaCore.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
				<emp:select id="LmtAgrBizAreaCore.core_con_type" label="核心企业类型" required="false" dictname="STD_LMT_BIZ_TYPE" />
				<emp:text id="LmtAgrBizAreaCore.year_sale_amt" label="年供货销售额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrBizAreaCore.fore_debt_bal" label="平均应收(付)账款余额" maxlength="18" required="false" cssElementClass="emp_currency_text_readonly" dataType="Currency" />
				<emp:text id="LmtAgrBizAreaCore.coop_year" label="合作年限" maxlength="6" required="false" />
				<emp:textarea id="LmtAgrBizAreaCore.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
			<%}else{ %>
			<div  class='emp_gridlayout_title'>目标客户群（超市百货类）</div>
			<emp:table icollName="LmtAgrBizAreaSupmkList" editable="true" pageMode="false" url="">
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
			<emp:gridLayout id="LmtAgrBizAreaGroup" title="机构信息" maxColumn="2">
				<emp:pop id="LmtAgrBizArea.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
				<emp:text id="LmtAgrBizArea.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				<emp:text id="LmtAgrBizArea.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
				<emp:text id="LmtAgrBizArea.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
				<emp:pop id="LmtAgrBizArea.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
				<emp:pop id="LmtAgrBizArea.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName2" readonly="true"/>
				<emp:text id="LmtAgrBizArea.input_id_displayname" label="登记人" required="false" />
				<emp:text id="LmtAgrBizArea.input_br_id_displayname" label="登记机构" required="false" />
				<emp:date id="LmtAgrBizArea.input_date" label="登记日期" required="false"  />
			</emp:gridLayout>
		</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("surp".equals(type)){%>
			<emp:button id="close" label="关闭"/>
		<%}else{%>
		<emp:button id="return" label="返回"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
