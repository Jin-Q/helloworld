<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpOverseeOrgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		changeOslimit();
		Change();
	}

	function changeOslimit(){
		var is_build_oslimit = IqpOverseeOrg.is_build_oslimit._getValue();
		if(is_build_oslimit=="1"){
			IqpOverseeOrg.defray_limit._obj._renderHidden(false);
			IqpOverseeOrg.limit_start_date._obj._renderHidden(false);
			IqpOverseeOrg.limit_over_date._obj._renderHidden(false);
			IqpOverseeOrg.defray_limit._obj._renderRequired(true);
			IqpOverseeOrg.limit_start_date._obj._renderRequired(true);
			IqpOverseeOrg.limit_over_date._obj._renderRequired(true);
		}else{
			IqpOverseeOrg.defray_limit._obj._renderHidden(true);
			IqpOverseeOrg.limit_start_date._obj._renderHidden(true);
			IqpOverseeOrg.limit_over_date._obj._renderHidden(true);
			IqpOverseeOrg.defray_limit._obj._renderRequired(false);
			IqpOverseeOrg.limit_start_date._obj._renderRequired(false);
			IqpOverseeOrg.limit_over_date._obj._renderRequired(false);
		}
	}

	function Change(){
		var flag = IqpOverseeOrg.oversee_way._getValue();//监管方式
		if("01"==flag){//动态监管时，显示最低核准价值
			IqpOverseeOrg.low_auth_value._obj._renderHidden(false);
		    IqpOverseeOrg.low_auth_value._obj._renderRequired(true);
		}else if("02"==flag){
			IqpOverseeOrg.low_auth_value._obj._renderHidden(true);
		    IqpOverseeOrg.low_auth_value._obj._renderRequired(false);
		    IqpOverseeOrg.low_auth_value._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:gridLayout id="IqpOverseeOrgGroup" title="监管机构管理" maxColumn="2">
					<emp:text id="IqpOverseeOrg.serno" label="业务流水号" maxlength="40" required="false" colSpan="2" hidden="true"/>
					<emp:pop id="IqpOverseeOrg.oversee_org_id" label="监管机构编号" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus"
								popParam="width=700px,height=650px" required="true" />
					<emp:text id="IqpOverseeOrg.oversee_org_id_displayname" label="监管机构名称"   required="true" readonly="true" cssElementClass="emp_field_text_input2" />
					<emp:text id="IqpOverseeOrg.oversee_org_addr" label="监管机构地址" required="false" hidden="true" readonly="true" cssElementClass="emp_field_text_input2"/>
					<emp:text id="IqpOverseeOrg.oversee_org_addr_displayname" label="监管机构地址" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:text id="IqpOverseeOrg.oversee_org_street" label="街道" required="false" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true" />			
					<emp:text id="IqpOverseeOrg.legal" label="法人代表" maxlength="30" required="false" readonly="true" hidden="true" colSpan="2" />
					<emp:text id="IqpOverseeOrg.legal_displayname" label="法人代表"   required="true" readonly="true" />
					<emp:select id="IqpOverseeOrg.orgmodal" label="组织形式" required="true" dictname="STD_ZB_HOLD_TYPE" readonly="true" />
					<emp:text id="IqpOverseeOrg.belg_grp" label="所属集团" maxlength="80" readonly="true" />
					<emp:text id="IqpOverseeOrg.regi_cap" label="注册资金(万元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
					<emp:date id="IqpOverseeOrg.build_date" label="成立日期" required="true" onblur="checkOpenday()" readonly="true" />
					<emp:select id="IqpOverseeOrg.com_scale" label="企业规模" required="true" dictname="STD_ZB_ENTERPRISE" readonly="true" />
					<emp:select id="IqpOverseeOrg.is_busnes_con" label="是否为贸易企业" required="true" dictname="STD_ZX_YES_NO" />
					<emp:text id="IqpOverseeOrg.other_oper_pro" label="其他经营项目" maxlength="10" required="true" />
					<emp:select id="IqpOverseeOrg.is_parmachin" label="是否附带加工" required="true" dictname="STD_ZX_YES_NO" />
					<emp:text id="IqpOverseeOrg.storage_volume" label="仓库容量(吨)" maxlength="10" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="IqpOverseeOrg.transfer_abi_y" label="年吞吐量(吨)" maxlength="10" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>			
					<emp:text id="IqpOverseeOrg.openairsqu" label="露天面积(m²)" maxlength="20" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="IqpOverseeOrg.storage_chrg_normal" label="仓储收费标准" maxlength="18" required="true" />
					<emp:select id="IqpOverseeOrg.place_util_case" label="场地使用权情况" required="true" dictname="STD_ZB_STORE_CHA" />
					<emp:select id="IqpOverseeOrg.con_trade_stats" label="企业行业地位" required="true" dictname="STD_ZB_COM_HD_ENTER" readonly="true" />
					<emp:select id="IqpOverseeOrg.trade_stand_status" label="行业信誉状况" required="true" dictname="STD_MAINTAIN_STATUS" />
					<emp:text id="IqpOverseeOrg.oversee_biz_chief" label="监管业务负责人" maxlength="10" required="true" />
					<emp:text id="IqpOverseeOrg.link_mode" label="联系方式" maxlength="20" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
					<emp:textarea id="IqpOverseeOrg.oper_range" label="经营范围" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.now_main_biz_oper" label="现主要业务及经营状况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.store_occlude_modal" label="仓库封闭形式" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.person_iostore_proce" label="人员出入库手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.goods_iostore_proce" label="货物出入库手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.goods_test_proce" label="货物检、化验手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.imn_is_espec_flow" label="质押监管融资是否有特别流程" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.bkloan_outguar_case" label="银行借款及对外担保情况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.his_record" label="历史资信记录" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.alcoopbk_cpyear" label="已合作货押监管的银行及合作年限记录" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.his_coop_case" label="我行历史合作情况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.cpwish_advice" label="仓储单位监管能力、与我行的监管合作意愿及对我行动产融资业务的建议" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpOverseeOrg.other_indgt_case" label="其他调查情况" maxlength="60" required="false" colSpan="2" />
					<emp:text id="IqpOverseeOrg.cdt_eval" label="信用评级" maxlength="3" required="false" disabled="true" hidden="true"/>
					<emp:date id="IqpOverseeOrg.eval_time" label="评估日期" required="false" disabled="true" hidden="true"/>			
					<emp:checkbox id="IqpOverseeOrg.oversee_mode" label="监管模式" required="true" dictname="STD_ZB_OVERSEE_MODE" disabled="true"/>
					<emp:checkbox id="IqpOverseeOrg.opr_mode" label="操作模式" required="true" dictname="STD_ZB_OPR_MODE" disabled="true"/>
					<emp:select id="IqpOverseeOrg.is_build_oslimit" label="是否设立监管额度" required="true" dictname="STD_ZX_YES_NO" onchange="changeOslimit()" />
					<emp:text id="IqpOverseeOrg.defray_limit" label="最高监管额度" maxlength="18" dataType="Currency" hidden="true" cssElementClass="emp_currency_text_readonly"/>
					<emp:date id="IqpOverseeOrg.limit_start_date" label="额度开始日期" defvalue="$OPENDAY" readonly="true" hidden="true" />
					<emp:date id="IqpOverseeOrg.limit_over_date" label="额度结束日期" onblur="checkOverDate()" hidden="true" />
					<emp:select id="IqpOverseeOrg.oversee_way" label="监管方式" required="true" dictname="STD_ZB_OVERSEE_TYPE" />
					<emp:text id="IqpOverseeOrg.low_auth_value" label="最低核准价值" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>			
					<emp:date id="IqpOverseeOrg.begin_date" label="合作起始日" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:date id="IqpOverseeOrg.end_date" label="合作到期日" required="true" />
					<emp:select id="IqpOverseeOrg.oversee_org_status" label="状态" required="false" dictname="STD_ZB_OVERSEE_STATUS" readonly="true" />			
				</emp:gridLayout>
				<emp:gridLayout id="IqpOverseeOrgGroup" title="登记信息" maxColumn="2">
					<emp:pop id="IqpOverseeOrg.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
					<emp:pop id="IqpOverseeOrg.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="true"/>
					<emp:text id="IqpOverseeOrg.manager_id" label="责任人" required="true" hidden="true" />
					<emp:text id="IqpOverseeOrg.manager_br_id" label="责任机构" required="true" hidden="true" />
					<emp:text id="IqpOverseeOrg.input_id_displayname" label="登记人"   required="true" readonly="true" defvalue="$currentUserName" />
					<emp:text id="IqpOverseeOrg.input_br_id_displayname" label="登记机构"   required="true" readonly="true" defvalue="$organName" />
					<emp:text id="IqpOverseeOrg.input_id" label="登记人" maxlength="10" required="false" hidden="true" defvalue="$currentUserId" />
					<emp:text id="IqpOverseeOrg.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
					<emp:date id="IqpOverseeOrg.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true" />			
				</emp:gridLayout>
			<div align="center">
				<br>
				<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
			</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
