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
	String com_id="";
	if(context.containsKey("com_id")){
		com_id = context.getDataValue("com_id").toString();
	}
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusIndivSocRelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doReturnCusIndivSocRel(){
		goback();
	}
		
	function goback(){
		var com_id = "<%=com_id%>";
		var paramStr = "";
		var stockURL = "";
		var editFlag = '${context.EditFlag}';
		if(com_id!=null &&com_id!=""){
			paramStr="cus_id="+com_id;
			stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr+"&EditFlag="+editFlag;
		}else{
			paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		}
	//	var stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivSocRelGroup" title="个人社会关系" maxColumn="2">
		<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" colSpan="2"/>
		<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码" maxlength="20" required="true" colSpan="2"/>
		<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员" required="true" dictname="STD_ZX_YES_NO" />
		<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS" />
		<emp:text id="CusIndivSocRel.cus_id_rel" label="关联客户码" required="true" maxlength="30" />
		<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名" maxlength="80" required="true" />
		<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX" />
		<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业" required="false" dictname="STD_ZX_EMPLOYMET" />
		<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称" maxlength="60" required="false" hidden="true"/>
		<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务" required="false" dictname="STD_ZX_DUTY" />
		<emp:select id="CusIndivSocRel.indiv_rel_ind" label="所在行业" required="false" dictname="STD_GB_4754-2011" hidden="true"/>
		<emp:text id="CusIndivSocRel.indiv_rel_phn" label="联系电话" maxlength="35" required="false" dataType="Phone" hidden="true"/>
		<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)" />
		<emp:date id="CusIndivSocRel.input_date" label="登记日期" hidden="true"/>		
		<emp:date id="CusIndivSocRel.last_upd_date" label="更新日期" hidden="true"/>
		<emp:text id="CusIndivSocRel.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
        <emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
        <emp:text id="CusIndivSocRel.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivSocRel" label="返回"/>
	</div>
</body>
</html>
</emp:page>