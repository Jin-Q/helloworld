<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%
	String hidden_button= (String)request.getParameter("hidden_button");
%>
<emp:page>
	<html>
	<head>
	<title>详情查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<style type="text/css">
.emp_field_textarea_textarea {
	width: 600;
	height: 60;
}

;
.emp_field_text_readonly {
	border: 1px solid #b7b7b7;
	background-color: #e3e3e3;
	text-align: left;
	width: 240px;
}
;
</style>
	<%
		request.setAttribute("canwrite", "");
	%>

	<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doload(){
		var obj = LmtIndusApply.shared_scope._obj;
		if(obj.value == 2){
			LmtIndusApply.belg_org_displayname._obj._renderHidden(false);
			LmtIndusApply.belg_org._obj._renderHidden(false);
		}else{
			LmtIndusApply.belg_org_displayname._obj._renderHidden(true);
			LmtIndusApply.belg_org._obj._renderHidden(true);
		}
		
		hidden_button = "<%=hidden_button%>";
		if(hidden_button == 'true'){
			
			document.getElementById('button_return').style.display = 'none';
		}
	};
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="行业授信基本信息" id="main_tabs">
			<emp:gridLayout id="LmtIndusApplyGroup" title="行业授信申请信息" maxColumn="2">
				<emp:text id="LmtIndusApply.serno" label="业务编号" maxlength="40"	required="true" 
				readonly="true"	cssElementClass="emp_field_common_serno"  />
				<emp:select id="LmtIndusApply.indus_type" label="行业分类"	required="true" dictname="STD_ZB_INDUS_TYPE" readonly="true" />
				<emp:select id="LmtIndusApply.shared_scope" label="共享范围"	required="true" dictname="STD_SHARED_SCOPE" readonly="true" />
				<emp:pop id="LmtIndusApply.belg_org" label="所属机构" url="queryMultiSOrgPop.do"
					returnMethod="getOrganName" required="false" hidden="true" />
				<emp:textarea id="LmtIndusApply.belg_org_displayname" label="所属机构"	required="false" readonly="true" hidden="true" />
				<emp:pop id="LmtIndusApply.suit_prd" label="适用产品"	url='showPrdCheckTreeDetails.do?bizline=BL200'	
				returnMethod="setProds" required="true" colSpan="2"	/>
				<emp:textarea id="LmtIndusApply.suit_prd_displayname" label="适用产品名称"	readonly="true"  colSpan="2" />
				<emp:select id="LmtIndusApply.cur_type" label="授信币种" required="true"	dictname="STD_ZX_CUR_TYPE" colSpan="2" />
				<emp:text id="LmtIndusApply.indus_amt" label="行业总额(元)" maxlength="18"	required="true" 
				dataType="Currency" onchange="checkAmt(this)"	cssElementClass="emp_currency_text_readonly" />
				<emp:text id="LmtIndusApply.single_amt" label="单户限额(元)" maxlength="18"	required="true" 
				dataType="Currency" onchange="checkAmt(this)"	cssElementClass="emp_currency_text_readonly" />
				<emp:date id="LmtIndusApply.start_date" label="申请日期" required="true" />
				<emp:date id="LmtIndusApply.end_date" label="办结日期" required="false"	hidden="true" />
				<emp:select id="LmtIndusApply.crd_term_type" label="授信期限类型"	required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtIndusApply.crd_term" label="授信期限" maxlength="5"	required="true" dataType="Int" />
				<emp:select id="LmtIndusApply.is_list_mana" label="是否名单制管理" required="true" dictname="STD_ZX_YES_NO" />
				<emp:select id="LmtIndusApply.flow_type" label="流程类型" required="false" dictname="STD_ZB_FLOW_TYPE" hidden="true"/>
				<emp:textarea id="LmtIndusApply.memo" label="备注" maxlength="250"	required="false" colSpan="2"	onblur="this.value = this.value.substring(0, 250)" />
				<emp:select id="LmtIndusApply.apply_type" label="申请类型"	required="true" dictname="STD_ZB_LMT_APPLY_TYPE" hidden="true" />
			</emp:gridLayout>

			<emp:gridLayout id="LmtIndusApplyGroup" maxColumn="2" title="登记信息">
				<emp:pop id="LmtIndusApply.manager_id_displayname" label="责任人"	required="true" url="" />
				<emp:pop id="LmtIndusApply.manager_br_id_displayname" label="责任机构"	required="true" url=""  />
				<emp:text id="LmtIndusApply.input_id_displayname" label="登记人"	  readonly="true" required="true" />
				<emp:text id="LmtIndusApply.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
				<emp:text id="LmtIndusApply.input_date" label="登记日期" required="true"	readonly="true" />
				<emp:select id="LmtIndusApply.approve_status" label="审批状态"	required="true" dictname="WF_APP_STATUS" readonly="true" />
				<emp:text id="LmtIndusApply.manager_br_id" label="责任机构"	required="true" hidden="true" />
				<emp:text id="LmtIndusApply.manager_id" label="责任人" required="true"	hidden="true" />
				<emp:text id="LmtIndusApply.input_id" label="登记人" maxlength="20"	readonly="true" required="true" hidden="true" />
				<emp:text id="LmtIndusApply.input_br_id" label="登记机构" maxlength="20"	readonly="true" required="true" hidden="true" />
			</emp:gridLayout>
			<div align="center"><br>
			<emp:button id="return" label="返回列表"/>
			</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	</body>
	</html>
</emp:page>
