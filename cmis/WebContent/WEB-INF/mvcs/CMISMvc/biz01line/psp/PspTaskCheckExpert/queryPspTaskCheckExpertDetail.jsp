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
String task_create_mode = "";
if(context.containsKey("task_create_mode")){
	task_create_mode = (String)context.getDataValue("task_create_mode");
}
%>
<style type="text/css">

.emp_field_text_input3 {
	width: 650px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_disabled .emp_field_text_input3 {
	border-color: #b7b7b7;
	color: #CEC7BD;
}
.emp_field_readonly .emp_field_text_input3 {
	border-color: #b7b7b7;
}
</style>
<script type="text/javascript">
	function doLoad(){
		var task_create_mode = PspTaskCheckExpert.task_create_mode._getValue();
		if(task_create_mode == '02'){
			checkBiz();	
			checkBizArea();
		}else if(task_create_mode == '00'){
			checkBizArea();
		}else if(task_create_mode == '01'){
			checkBizArea();
			checkIsIndus();
		}
	};
	function checkBiz(){
		var is_biz_circle = PspTaskCheckExpert.is_biz_circle._getValue();
		if(is_biz_circle!=null&&is_biz_circle=='2'){
			PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		}else {
			PspTaskCheckExpert.biz_circle_no._obj._renderHidden(false);
		}
	}
	function checkBizArea(){
		var is_biz_area = PspTaskCheckExpert.is_biz_area._getValue();
		if(is_biz_area!=null&&is_biz_area=='2'){
			PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		}else {
			PspTaskCheckExpert.biz_area_no._obj._renderHidden(false);
		}
	}
	function checkIsIndus(){
		var is_indus = PspTaskCheckExpert.is_indus._getValue();
		if(is_indus!=null&&is_indus=='2'){
			PspTaskCheckExpert.indus_no._obj._renderHidden(true);
		}else {
			PspTaskCheckExpert.indus_no._obj._renderHidden(false);
		}
	}
	function doReturn() {
		var url = '<emp:url action="queryPspTaskCheckExpertList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//单一客户选择pop返回方法
	function returnCus(data){
		PspTaskCheckExpert.cus_id._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.cus_id_displayname._setValue(data.cus_name._getValue());
	}
	//客户经理pop返回方法
	function setconId(data){
		PspTaskCheckExpert.task_exe_id_displayname._setValue(data.actorname._getValue());
		PspTaskCheckExpert.task_exe_id._setValue(data.actorno._getValue());
	}
	//机构pop返回方法
	function getOrgID(data){
		PspTaskCheckExpert.task_exe_br_id._setValue(data.organno._getValue());
		PspTaskCheckExpert.task_exe_br_id_displayname._setValue(data.organname._getValue());
	}
	//集团客户选择pop返回方法
	function returnCusGrp(data){
		PspTaskCheckExpert.grp_no._setValue(data.grp_no._getValue());
		PspTaskCheckExpert.grp_no_displayname._setValue(data.grp_name._getValue());
	}
	//产品树返回方法
	function returnPrdId(data){
		PspTaskCheckExpert.biz_type._setValue(data.id);
		PspTaskCheckExpert.biz_type_displayname._setValue(data.label);
	};
	//所属支行返回方法
	function getOrgId1(data){
		PspTaskCheckExpert.belg_branch._setValue(data.organno._getValue());
		PspTaskCheckExpert.belg_branch_displayname._setValue(data.organname._getValue());
	}
	//所属客户经理返回方法
	function setconId1(data){
		PspTaskCheckExpert.belg_manager_id._setValue(data.actorno._getValue());
		PspTaskCheckExpert.belg_manager_id_displayname._setValue(data.actorname._getValue());
	}
	//行业投向返回方法
	function onReturnComCllName(date){
		PspTaskCheckExpert.indus_type._obj.element.value=date.One+date.id;
		PspTaskCheckExpert.indus_type_displayname._obj.element.value=date.label;
	}
	//圈商返回方法
	function setBizAreaNo(data){
		PspTaskCheckExpert.biz_circle_no._setValue(data.agr_no._getValue());
		//PspTaskCheckExpert.biz_area_name._setValue(data.biz_area_name._getValue());
		//PspTaskCheckExpert.biz_area_type._setValue(data.biz_area_type._getValue());
	}
	//联保协议编号返回方法
	function returnAgrNo(data){
		PspTaskCheckExpert.biz_area_no._setValue(data.agr_no._getValue());
	}
	//政府融资性平台返回方法
	function getGoverFinTer(data){
		PspTaskCheckExpert.gover_fin_ter._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.gover_fin_ter_displayname._setValue(data.cus_id_displayname._getValue());
	}
	//合作方返回方法
	function returnCoopAgrNo(data){
		PspTaskCheckExpert.agr_no._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.agr_no_displayname._setValue(data.cus_id_displayname._getValue());
		PspTaskCheckExpert.coop_type._setValue(data.coop_type._getValue());
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	
		<emp:gridLayout id="PspTaskCheckExpertGroup" title="专项检查任务设置" maxColumn="2">
			<emp:text id="PspTaskCheckExpert.serno" label="任务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.task_name" label="任务名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input3" readonly="true"/>
			<emp:select id="PspTaskCheckExpert.task_create_mode" label="任务生成方式" required="true" readonly="true" dictname="STD_ZB_TASK_CREATE" colSpan="2"/>
			<emp:date id="PspTaskCheckExpert.task_create_date" label="任务生成日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:date id="PspTaskCheckExpert.need_finish_date" label="要求完成时间" required="true" onblur="checkDt()"/>
			<emp:select id="PspTaskCheckExpert.task_status" label="任务状态" dictname="STD_ZB_TASK_STATUS" required="false" defvalue="00" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspTaskCheckExpertGroup1" title="专项检查任务生成条件" maxColumn="2">	
		<%if(task_create_mode.equals("00")){ //公司类客户%>
			<emp:select id="PspTaskCheckExpert.is_grpmem" label="是否属集团(关联)客户" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspTaskCheckExpert.is_gover_fin" label="是否属政府融资平台" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspTaskCheckExpert.is_biz_area" label="是否属联保小组成员" required="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="PspTaskCheckExpert.biz_area_no" label="联保协议编号" required="false" url="queryLmtAgrJointPop.do?returnMethod=returnAgrNo&flag=1"/>
			<emp:pop id="PspTaskCheckExpert.belg_branch_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId1"/>
			<emp:pop id="PspTaskCheckExpert.belg_manager_id_displayname" label="主管客户经理" required="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId1"/>
			<emp:pop id="PspTaskCheckExpert.cus_id" label="客户码" required="false" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&returnMethod=returnCus" />
			<emp:text id="PspTaskCheckExpert.cus_id_displayname" label="客户名称"   required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.indus_type" label="行业投向" maxlength="5" required="false" hidden="true" colSpan="2" />
			<emp:pop id="PspTaskCheckExpert.indus_type_displayname" label="行业投向" required="false" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturnComCllName" cssElementClass="emp_field_text_input3"/>
			<emp:checkbox id="PspTaskCheckExpert.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" disabled="true"/>
			<emp:pop id="PspTaskCheckExpert.biz_type_displayname" label="业务品种" required="false" url="showPrdTreeDetails.do?bizline=BL100" returnMethod="returnPrdId"/>
			<emp:text id="PspTaskCheckExpert.biz_type" label="业务品种编号" maxlength="40" required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_begin" label="贷款余额区间：从" maxlength="18" required="false" dataType="Currency" onblur="checkBegin()"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_end" label="至： " maxlength="18" required="false" dataType="Currency" onblur="checkEnd()"/>
			<emp:textarea id="PspTaskCheckExpert.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspTaskCheckExpert.belg_manager_id" label="主管客户经理" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.belg_branch" label="管理机构" maxlength="60" required="false" readonly="true" hidden="true"/>
		<%}else if(task_create_mode.equals("01")){ //小微类客户%>
			<emp:select id="PspTaskCheckExpert.is_grpmem" label="是否属集团(关联)客户" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspTaskCheckExpert.is_gover_fin" label="是否属政府融资平台" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspTaskCheckExpert.is_biz_area" label="是否属联保小组成员" required="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="PspTaskCheckExpert.biz_area_no" label="联保协议编号" required="false" url="queryLmtAgrJointPop.do?returnMethod=returnAgrNo&flag=1"/>
			<emp:select id="PspTaskCheckExpert.is_indus" label="是否属行业成员" required="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="PspTaskCheckExpert.indus_no" label="行业协议编号" required="false" url="queryLmtAgrJointPop.do?returnMethod=returnAgrNo&flag=1"/>
			<emp:pop id="PspTaskCheckExpert.belg_branch_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId1"/>
			<emp:pop id="PspTaskCheckExpert.belg_manager_id_displayname" label="主管客户经理" required="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId1"/>
			<emp:pop id="PspTaskCheckExpert.cus_id" label="客户码" required="false" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&returnMethod=returnCus" />
			<emp:text id="PspTaskCheckExpert.cus_id_displayname" label="客户名称"   required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.indus_type" label="行业投向" maxlength="5" required="false" hidden="true"/>
			<emp:pop id="PspTaskCheckExpert.indus_type_displayname" label="行业投向" required="false" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturnComCllName" cssElementClass="emp_field_text_input3" colSpan="2"/>
			<emp:checkbox id="PspTaskCheckExpert.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" disabled="true"/>
			<emp:pop id="PspTaskCheckExpert.biz_type_displayname" label="业务品种" required="false" url="showPrdTreeDetails.do?bizline=BL100,BL200" returnMethod="returnPrdId"/>
			<emp:text id="PspTaskCheckExpert.biz_type" label="业务品种编号" maxlength="40" required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_begin" label="贷款余额区间：从" maxlength="18" required="false" dataType="Currency" onblur="checkBegin()"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_end" label="至： " maxlength="18" required="false" dataType="Currency" onblur="checkEnd()"/>
			<emp:textarea id="PspTaskCheckExpert.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspTaskCheckExpert.belg_branch" label="管理机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.belg_manager_id" label="主管客户经理" maxlength="60" required="false" readonly="true" hidden="true"/>
		<%}else if(task_create_mode.equals("02")){ //个人类客户%>
		    <emp:select id="PspTaskCheckExpert.is_grpmem" label="是否属集团(关联)客户" required="true" dictname="STD_ZX_YES_NO" colSpan="2" />
			<emp:select id="PspTaskCheckExpert.is_biz_area" label="是否属联保小组成员" required="true" dictname="STD_ZX_YES_NO" onchange="checkBizArea()"/>
			<emp:pop id="PspTaskCheckExpert.biz_area_no" label="联保协议编号" required="false" url=""/>
			<emp:select id="PspTaskCheckExpert.is_biz_circle" label="是否属圈商成员" required="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="PspTaskCheckExpert.biz_circle_no" label="圈商编号" required="false" url="queryLmtAgrBizAreaList.do?type=select"   returnMethod="setBizAreaNo"/>
			<emp:pop id="PspTaskCheckExpert.belg_branch_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId1"/>
			<emp:pop id="PspTaskCheckExpert.belg_manager_id_displayname" label="主管客户经理" required="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId1"/>
			<emp:pop id="PspTaskCheckExpert.cus_id" label="客户码" required="false" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&returnMethod=returnCus" />
			<emp:text id="PspTaskCheckExpert.cus_id_displayname" label="客户名称"   required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.indus_type" label="行业投向" maxlength="5" required="false" hidden="true"/>
			<emp:pop id="PspTaskCheckExpert.indus_type_displayname" label="行业投向" required="false" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturnComCllName" cssElementClass="emp_field_text_input3" colSpan="2" readonly="true"/>
			<emp:checkbox id="PspTaskCheckExpert.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" disabled="true"/>
			<emp:pop id="PspTaskCheckExpert.biz_type_displayname" label="业务品种" required="false" url="showPrdTreeDetails.do?bizline=BL100,BL200" returnMethod="returnPrdId"/>
			<emp:text id="PspTaskCheckExpert.biz_type" label="业务品种编号" maxlength="40" required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_begin" label="贷款余额区间：从" maxlength="18" required="false" dataType="Currency" onblur="checkBegin()"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_end" label="至： " maxlength="18" required="false" dataType="Currency" onblur="checkEnd()"/>
			<emp:textarea id="PspTaskCheckExpert.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspTaskCheckExpert.biz_area_name" label="圈商名称" maxlength="60" required="false" hidden="true"/>	
			<emp:text id="PspTaskCheckExpert.belg_branch" label="管理机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.belg_manager_id" label="主管客户经理" maxlength="60" required="false" readonly="true" hidden="true"/>
		<%}else if(task_create_mode.equals("03")){ //集团（关联）客户%>
		
			<emp:pop id="PspTaskCheckExpert.grp_no" label="集团编号" required="false" url="queryCusGrpInfoPopList.do?returnMethod=returnCusGrp"/>
			<emp:text id="PspTaskCheckExpert.grp_no_displayname" label="集团名称"   required="false" cssElementClass="emp_field_text_readonly"/>
			<emp:pop id="PspTaskCheckExpert.belg_branch_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId1"/>
			<emp:pop id="PspTaskCheckExpert.belg_manager_id_displayname" label="主管客户经理" required="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId1"/>
			<emp:textarea id="PspTaskCheckExpert.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspTaskCheckExpert.belg_branch" label="主办机构" maxlength="60" required="false" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.belg_manager_id" label="主办客户经理" maxlength="60" required="false" hidden="true"/>
			
		<%}else if(task_create_mode.equals("04")){ //合作方客户%>
			<emp:select id="PspTaskCheckExpert.coop_type" label="合作方类型" required="false" readonly="true" dictname="STD_PSP_COOP_TYPE" colSpan="2" />
			<emp:pop id="PspTaskCheckExpert.belg_branch_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId1"/>
			<emp:pop id="PspTaskCheckExpert.belg_manager_id_displayname" label="主管客户经理" required="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId1"/>
			<emp:pop id="PspTaskCheckExpert.agr_no" label="合作方客户码" required="false" url="queryLmtAgrJointPop.do?returnMethod=returnCoopAgrNo&flag=3"/>
			<emp:text id="PspTaskCheckExpert.agr_no_displayname" label="合作方客户名称"   required="false" readonly="true"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_begin" label="授信总额：  从" maxlength="18" required="false" dataType="Currency" onblur="checkBegin()"/>
			<emp:text id="PspTaskCheckExpert.loan_amt_end" label="至： " maxlength="18" required="false" dataType="Currency" onblur="checkEnd()"/>
			<emp:textarea id="PspTaskCheckExpert.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspTaskCheckExpert.belg_branch" label="责任机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.belg_manager_id" label="责任人" maxlength="60" required="false" readonly="true" hidden="true"/>
		<%}%>
		</emp:gridLayout>
		<emp:gridLayout id="PspTaskCheckExpertGroup" title="登记信息" maxColumn="2">	
			<emp:text id="PspTaskCheckExpert.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="PspTaskCheckExpert.input_br_id_displayname" label="登记机构"   required="false" readonly="true" defvalue="$organName"/>
			<emp:date id="PspTaskCheckExpert.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
			<emp:text id="PspTaskCheckExpert.input_id" label="登记人" maxlength="20" required="false" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="PspTaskCheckExpert.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" defvalue="$organNo" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
