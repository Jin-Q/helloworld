<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<%
	request.setAttribute("canwrite", "");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cus_id = (String)request.getParameter("cus_id");
%>
<html>
<head>
<title>一键查询详情页面</title>
<jsp:include page="/include.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<jsp:include page="jsCusCom.jsp" flush="true" />
<script type="text/javascript">
	function doOnLoad() {
	};
	function doViewCusComRelApitalInfo() {
		var cusIdRel = CusComRelApitalList._obj.getParamValue(['cus_id_rel']);
		var cusId = CusComRelApitalList._obj.getParamValue(['cus_id']);
		if (cusIdRel != null && cusId !=null) {
			var url = '<emp:url action="getCusComRelApitalViewPage.do"/>?cus_id_rel='+cusIdRel+"&EditFlag=query&cus_id="+cusId+"&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};

	function doViewCusGrpInfo(){
		var grpNo = CusGrpInfoList._obj.getParamValue(['grp_no']);
		if (grpNo != null) {
			var url = '<emp:url action="queryCusGrpInfoDetail.do"/>?grp_no='+grpNo+"&CusGrpInfo.cus_id=<%=cus_id%>&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};

	function doViewLmtApply() {
		var serno = LmtApplyList._obj.getParamValue(['serno']);
		if (serno != null) {
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?serno='+serno+"&op=view&type=Y&isShow=N&menuId=corp_crd_query&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
	//联保授信审批详情
	function doViewLmtAppJointCoop() {
		var serno = LmtAppJointCoopList._obj.getParamValue(['serno']);
		var cus_id=CusBase.cus_id._getValue();
		if (serno != null) {
			var url = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?serno='+serno+"&cus_id="+cus_id+"&op=view&menuId=unit_team_crd_apply&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};
	//集团（关联）授信审批历史详情
	function doViewLmtGrpApply() {
		var serno = LmtGrpApplyList._obj.getParamValue(['serno']);
		var cus_id=CusBase.cus_id._getValue();
		if (serno != null) {
			var url = '<emp:url action="getLmtGrpApplyViewPage.do"/>?serno='+serno+"&cus_id="+cus_id+"&op=view&type=app&menuId=grp_crd_query&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */

	function doViewLmtAgrInfo() {
		var agrNo = LmtAgrInfoList._obj.getParamValue(['agr_no']);
		var cusId = LmtAgrInfoList._obj.getParamValue(['cus_id']);
		if (agrNo != null) {
			var	url = '<emp:url action="getLmtAgrInfoViewPageNoTab.do"/>?agr_no='+agrNo+"&cus_id="+cusId+"&menuId=crd_agr&isShow=view&op=view&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};

	function doViewGrtGuarantee() {
		var guarContNo = GrtGuaranteeList._obj.getParamValue(['guar_cont_no']);
		var guarCusId = GrtGuaranteeList._obj.getParamValue(['guar_cus_id']);
		if (guarContNo != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&guar_cont_no='+guarContNo+"&guar_cus_id="+guarCusId+"&oper=view&OneKey=1";
			url = EMPTools.encodeURI(url);
			window.location = url;
			}
	};

	function doGetAccViewList() {
		var cusId = AccViewList._obj.getParamValue(['cus_id']);
		var tableModel = AccViewList._obj.getParamValue(['table_model']);
		if (cusId != null) {
			var url = '<emp:url action="queryAccViewListByOneKey.do"/>?cus_id='+cusId+"&table_model="+tableModel;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};

	function doExcel(){
		var cusId ='<%=cus_id%>';
		if (cusId != null) {
			var url = '<emp:url action="queryCusComByOneKeyToExcel.do"/>?cus_id='+cusId;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
		};
	
	function doClose(){
		window.close();
		};
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="" method="POST">
	<emp:gridLayout id="CusComGroup" title="对公客户基本信息" maxColumn="2">
		<emp:text id="CusBase.cus_name" label="客户名称" maxlength="80" readonly="true"  colSpan="2"/>
		<emp:text id="CusCom.reg_addr_displayname" label="注册地址"  readonly="true" colSpan="2"/>
		<emp:text id="CusCom.ceo_name" label="法人代表" readonly="true"/>
		<emp:text id="CusCom.reg_cap_amt" label="注册资本(万元)" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusCom.reg_code" label="登记注册号" maxlength="30" required="false" />
		<emp:text id="CusBase.cert_code1" label="组织机构代码" maxlength="20" required="false" readonly="true" defvalue="$CusBase.cert_code"/>
		<emp:text id="CusCom.nat_tax_reg_code" label="国税税务登记号" maxlength="20" />
		<emp:text id="CusCom.loc_tax_reg_code" label="地税税务登记号" maxlength="20" />
		<emp:text id="CusBase.loan_card_id" label="贷款卡号" maxlength="16" required="false"/>
		<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="false" readonly="true" />		
	</emp:gridLayout> 
	<div class='emp_gridlayout_title'>关联关系智能搜索</div>
	<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/queryCusRelTree.jsp" flush="true"/>
	<br>
	<div class='emp_gridlayout_title'>资本构成</div>
	<emp:table icollName="CusComRelApitalList" pageMode="false" url="">
		<emp:link id="cus_id_rel" label="出资人客户码" operation="viewCusComRelApitalInfo"/>
		<emp:text id="invt_name" label="出资人名称" />
		<emp:text id="invt_typ" label="出资人类型" dictname="STD_ZB_INVESTOR2" hidden="true"/>
		<emp:text id="cert_typ" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="invt_amt" label="出资金额(万元)" dataType="Currency"/>
		<emp:text id="invt_perc" label="所占比例" dataType="Percent"/>
		<emp:text id ="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
	<div class='emp_gridlayout_title'>所在关联集团</div>
	<emp:table icollName="CusGrpInfoList" pageMode="false" url="">
		<emp:link id="grp_no" label="关联(集团)编号" operation="viewCusGrpInfo"/>
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主关联(集团)公司客户码" />
		<emp:text id="parent_cus_id_displayname" label="主关联(集团)公司名称" />
		<emp:text id="manager_id_displayname" label="主办客户经理" />
		<emp:text id="manager_br_id_displayname" label="主办行" />	
	</emp:table>
	
	<div class='emp_gridlayout_title'>授信审批历史</div>	
	<emp:table icollName="LmtApplyList" pageMode="false" url="" >
		<emp:link id="serno" label="业务编号" operation="viewLmtApply"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */-->
	<div class='emp_gridlayout_title'>联保授信审批历史</div>	
	<emp:table icollName="LmtAppJointCoopList" pageMode="false" url="" >
		<emp:link id="serno" label="业务编号" operation="viewLmtAppJointCoop" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="类别" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	<div class='emp_gridlayout_title'>集团（关联）授信审批历史</div>	
	<emp:table icollName="LmtGrpApplyList" pageMode="false" url="" >
		<emp:link id="serno" label="业务编号" operation="viewLmtGrpApply"/>
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */-->
	<div class='emp_gridlayout_title'>授信额度信息</div>	
	<emp:table icollName="LmtAgrInfoList" pageMode="false" url="">
		<emp:link id="serno" label="业务流水编号" operation="viewLmtAgrInfo"/>
		<emp:text id="agr_no" label="协议编号"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="crd_amt" label="授信额度（元）" dataType="Currency"/>
		<!-- modified by lisj 2015-3-3 修复授信额度信息已用金额取数问题，于2015-3-5上线 -->
		<emp:text id="used_amt" label="已用额度（元）" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
	</emp:table>
	<div class='emp_gridlayout_title'>台账信息</div>
	<emp:table icollName="AccViewList" pageMode="false" url="">
		<emp:link id="cus_id" label="客户码" hidden="false" operation="getAccViewList"/>
		<emp:text id="table_name" label="台账业务类型" />
		<emp:text id="bill_bal" label="贷款余额总计" dataType="Currency"/>
		<emp:text id="table_model" label="table_model" hidden="true"/>
	</emp:table>
	<div class='emp_gridlayout_title'>在我行担保情况</div>
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="">
		<emp:link id="guar_cont_no" label="担保合同编号" operation="viewGrtGuarantee"/>
		<emp:text id="cus_id_displayname" label="借款人名称"/>
	    <emp:text id="guar_cus_id_displayname" label="担保人名称"/>
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="occupy_amt" label="占用金额" dataType="Currency"/>
		<emp:text id="cus_id" label="借款人客户码" hidden="true"/>
		<emp:text id="guar_cus_id" label="担保人客户码" hidden="true"/>
		<emp:text id="guar_way" label="业务类型" dictname="STD_GUAR_TYPE"/>
		<emp:text id="guar_start_date" label="担保起始日期" />
		<emp:text id="guar_end_date" label="担保到期日期" />
		<emp:text id="guar_cont_state" label="合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	<div align="center">
		<br>
		<emp:button id="excel" label="导出" />
		<emp:button id="close" label="关闭" />
	</div>
	</emp:form>
	</body>
	</html>
</emp:page>