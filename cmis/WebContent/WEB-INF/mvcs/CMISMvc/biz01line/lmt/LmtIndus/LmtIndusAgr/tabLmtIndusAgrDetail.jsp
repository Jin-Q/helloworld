<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 600;
	height: 60;
};
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doload(){
		var obj = LmtIndusAgr.shared_scope._obj;
		if(obj.value == 2){
			LmtIndusAgr.belg_org_displayname._obj._renderHidden(false);
			LmtIndusAgr.belg_org._obj._renderHidden(false);
		}else{
			LmtIndusAgr.belg_org_displayname._obj._renderHidden(true);
			LmtIndusAgr.belg_org._obj._renderHidden(true);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:gridLayout id="LmtIndusAgrGroup" title="行业授信协议表" maxColumn="2">
			<emp:text id="LmtIndusAgr.serno" label="业务编号" maxlength="40" required="true" colSpan="2" cssElementClass="emp_field_common_serno"/>
			<emp:text id="LmtIndusAgr.agr_no" label="协议编号" maxlength="40" required="true" />
			<emp:select id="LmtIndusAgr.indus_type" label="行业分类" required="false" dictname="STD_ZB_INDUS_TYPE" />
			<emp:select id="LmtIndusAgr.shared_scope" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" />
			<emp:pop id="LmtIndusAgr.belg_org" label="所属机构"  cssElementClass="emp_pop_common_org"
			url="queryMultiSOrgPop.do" returnMethod="getOrganName"  required="false"  hidden="true"/>
			<emp:textarea id="LmtIndusAgr.belg_org_displayname" label="所属机构"  required="false"  readonly="true" hidden="true"/>
			<emp:pop id="LmtIndusAgr.suit_prd" label="适用产品" url='showPrdCheckTreeDetails.do?bizline=BL200' 
			returnMethod="setProds" required="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
			<emp:textarea id="LmtIndusAgr.suit_prd_displayname" label="适用产品名称" readonly="true"  colSpan="2" />
			<emp:select id="LmtIndusAgr.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="LmtIndusAgr.indus_amt" label="行业总额(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtIndusAgr.single_amt" label="单户限额(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="LmtIndusAgr.start_date" label="授信起始日期" required="false" />
			<emp:date id="LmtIndusAgr.end_date" label="授信到期日期" required="false" />
			<emp:textarea id="LmtIndusAgr.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:select id="LmtIndusAgr.agr_status" label="协议状态" required="true" dictname="STD_ZB_AGR_STATUS" hidden="true"/>
	</emp:gridLayout>
	
	<emp:gridLayout id="LmtIndusAgrGroup" maxColumn="2" title="登记信息">
			<emp:text id="LmtIndusAgr.input_date" label="登记日期" required="true" readonly="true" colSpan="2" />
			<emp:text id="LmtIndusAgr.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="LmtIndusAgr.input_br_id_displayname" label="登记机构"   readonly="true" required="true" />
			<emp:pop id="LmtIndusAgr.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="LmtIndusAgr.manager_br_id_displayname" label="管理机构"  required="true"  
			 url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="LmtIndusAgr.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="LmtIndusAgr.manager_id" label="责任人" required="true"  hidden="true"  />
			<emp:text id="LmtIndusAgr.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"   />
			<emp:text id="LmtIndusAgr.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"  />
		</emp:gridLayout>
	
	<div align="center">
		<br>
	</div>
</body>
</html>
</emp:page>
