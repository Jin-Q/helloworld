<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
		if(weatherSpouse._obj.element.value == 2){
			cus_id_rel._obj._renderRequired(false);
			cus_id_rel._obj._renderHidden(true);
			
			indiv_rel_cert_typ._obj._renderRequired(false);
			indiv_rel_cert_typ._obj._renderHidden(true);
			
			indiv_rl_cert_code._obj._renderRequired(false);
			indiv_rl_cert_code._obj._renderHidden(true);
			
			indiv_rel_cus_name._obj._renderHidden(true);
			indiv_sex._obj._renderHidden(true);
			indiv_com_name._obj._renderHidden(true);
			indiv_rl_y_incm._obj._renderHidden(true);
			
		}else if(weatherSpouse._obj.element.value == 1){
			cus_id_rel._obj._renderRequired(true);
			cus_id_rel._obj._renderHidden(false);
			
			indiv_rel_cert_typ._obj._renderRequired(false);
			indiv_rel_cert_typ._obj._renderHidden(false);
			
			indiv_rl_cert_code._obj._renderRequired(false);
			indiv_rl_cert_code._obj._renderHidden(false);
			
			indiv_rel_cus_name._obj._renderHidden(false);
			indiv_sex._obj._renderHidden(false);
			indiv_com_name._obj._renderHidden(false);
			indiv_rl_y_incm._obj._renderHidden(false);
		}
	}

	function doChange(){
		weatherSpouse._obj._renderReadonly(true);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="querySpouseInfo.do" method="POST">
		<emp:gridLayout id="CusIndivSocRelGroup" maxColumn="2" title=" ">
			<emp:select id="weatherSpouse" label="是否有配偶" required="true" readonly="true" dictname="STD_ZX_YES_NO" onchange="doChange()" colSpan="2"/>
			<emp:pop id="cus_id_rel" label="配偶客户码" readonly="true" url="queryCusBasePop.do" returnMethod="setCurPageDatas" required="true" colSpan="2"/>
			<emp:select id="indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0" readonly="true" />
			<emp:text id="indiv_rl_cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:text id="indiv_rel_cus_name" label="姓名" maxlength="80" readonly="true" />
			<emp:select id="indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX" readonly="true" />
			<emp:text id="indiv_com_name" label="工作单位" maxlength="60" required="true" readonly="true" />
			<emp:text id="indiv_rl_y_incm" label="年收入（元）" dataType="Currency" required="true" maxlength="18" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
	</emp:form>
</body>
</html>
</emp:page>

