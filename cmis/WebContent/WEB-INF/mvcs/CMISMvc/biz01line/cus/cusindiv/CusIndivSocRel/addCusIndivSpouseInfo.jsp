<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%
String cus_id = (String)request.getParameter("cus_id");
%>
<html>
<head>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"><!--
	
	/*--user code begin--*/
	var cusid_rel ;
	var indivrel_cert_typ;
	var indivrl_cert_code;
	var indivrel_cus_name;
	var indivsex ;
	var indivrl_y_incm ;
	function doLoad(){
		cusid_rel =  cus_id_rel._obj.element.value;
		indivrel_cert_typ = indiv_rel_cert_typ._obj.element.value;
		indivrl_cert_code = indiv_rl_cert_code._obj.element.value;
		indivrel_cus_name = indiv_rel_cus_name._obj.element.value;
		indivsex = indiv_sex._obj.element.value;
		indivrl_y_incm = indiv_rl_y_incm._obj.element.value;
	}
	function doChange(){
		if(weatherSpouse._obj.element.value == 2){
			cus_id_rel._obj._renderReadonly(true);
			cus_id_rel._obj._renderRequired(false);
			
			indiv_sex._obj._renderReadonly(true);
			indiv_rel_cert_typ._obj._renderRequired(false);

			indiv_rl_cert_code._obj._renderRequired(false);
			
			indiv_rel_cus_name._obj._renderRequired(false);
			
			cus_id_rel._obj.element.value = '';
			indiv_rel_cert_typ._setValue( '' );
			indiv_rl_cert_code._obj.element.value = '';
			indiv_rel_cus_name._obj.element.value = '';
			indiv_sex._setValue( '' );
			indiv_rl_y_incm._obj.element.value = '';
		}else if(weatherSpouse._obj.element.value == 1){
			cus_id_rel._obj._renderReadonly(false);
			cus_id_rel._obj._renderRequired(true);
			
			indiv_sex._obj._renderReadonly(false);
			indiv_rel_cert_typ._obj._renderRequired(true);

			indiv_rl_cert_code._obj._renderRequired(true);
			
			indiv_rel_cus_name._obj._renderRequired(true);

			cus_id_rel._obj.element.value = cusid_rel;
			indiv_rel_cert_typ._setValue( indiv_relcert_typ );
			indiv_rl_cert_code._obj.element.value = indivrl_cert_code;
			indiv_rel_cus_name._obj.element.value = indivrel_cus_name;
			indiv_sex._setValue( indivsex );
			indiv_rl_y_incm._obj.element.value = indivrl_y_incm;
			
		}
	}
	function doReset(){
		page.dataGroups.CusIndivSocRelGroup.reset();
	}
	function setCurPageDatas(data){
		//客户码,证件类型,证件号码,姓名
		cus_id_rel._obj.element.value = data.cus_id._getValue();
		indiv_rel_cert_typ._setValue( data.cert_type._getValue());
		indiv_rl_cert_code._obj.element.value = data.cert_code._getValue();
		indiv_rel_cus_name._obj.element.value = data.cus_name._getValue();
	}
	function doSaveRe(){
		if( weatherSpouse._obj.element.value == 1)
		{	
			 var paramStr="cus_id_rel="+cus_id_rel._obj.element.value 
				+ "&weatherSpouse=" + weatherSpouse._obj.element.value
				+ "&indiv_rel_cert_typ=" + indiv_rel_cert_typ._obj.element.value
				+ "&indiv_rl_cert_code=" + indiv_rl_cert_code._obj.element.value
				+ "&indiv_rel_cus_name=" + indiv_rel_cus_name._obj.element.value
				+ "&indiv_sex=" + indiv_sex._obj.element.value
				+ "&indiv_rl_y_incm=" + indiv_rl_y_incm._obj.element.value
				+ "&cus_id=<%=cus_id%>";
		}else{
			var paramStr="cus_id_rel="+cusid_rel 
			+ "&weatherSpouse=2"
			+ "&indiv_rel_cert_typ=" + indivrel_cert_typ
			+ "&indiv_rl_cert_code=" + indivrl_cert_code
			+ "&indiv_rel_cus_name=" + indivrel_cus_name
			+ "&indiv_sex=" + indivsex
			+ "&indiv_rl_y_incm=" + indivrl_y_incm
			+ "&cus_id=<%=cus_id%>";
		}
			var url = '<emp:url action="saveCusIndivSpouseInfo.do"/>&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
			
	}
	/*--user code end--*/
	
--></script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="saveCusIndivSpouseInfo.do" method="POST">
		<emp:gridLayout id="CusIndivSocRelGroup" maxColumn="2" title=" ">
			<emp:select id="weatherSpouse" label="是否有配偶" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()" colSpan="2"/>
			<emp:pop id="cus_id_rel" label="配偶客户码" url="queryCusBasePop.do" returnMethod="setCurPageDatas" required="true" colSpan="2"/>
			<emp:select id="indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0" readonly="true" />
			<emp:text id="indiv_rl_cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:text id="indiv_rel_cus_name" label="姓名" maxlength="80" required="true" readonly="true" />
			<emp:select id="indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX" />
			<emp:text id="indiv_rl_y_incm" label="年收入（元）" dataType="Currency" maxlength="18"/>
			<emp:select id="indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS" defvalue="1" hidden="true"/>
		</emp:gridLayout>
		
		<%
		    String flag=(String)request.getSession().getAttribute("buttonFlag");
		    if(!(flag!=null&&flag.equals("query"))){
		%>
		<div align="center">
			<emp:button id="saveRe" label="保 存" ></emp:button>
		</div>
		<%} %>
	</emp:form>
</body>
</html>
</emp:page>

