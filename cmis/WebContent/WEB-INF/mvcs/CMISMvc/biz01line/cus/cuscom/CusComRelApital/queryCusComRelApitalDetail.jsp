<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%>

<script type="text/javascript">

	function doOnLoad(){
		CusComRelApital.cus_id_rel._obj.addOneButton('view12','查看',viewCusInfo);
		changeCardFlg();
	};

	//查看客户信息
	function viewCusInfo(){
		var cus_id_rel = CusComRelApital.cus_id_rel._getValue();
		if(cus_id_rel==null||cus_id_rel==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id_rel;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}

	function doReturn() {
		var cus_id  =CusComRelApital.cus_id._obj.element.value;
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComRelApital.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComRelApitalList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function changeCardFlg(){
		if(CusComRelApital.invt_typ._obj.element.value=="2"){
			CusComRelApital.loan_card._obj._renderHidden(false);
		 }else if(CusComRelApital.invt_typ._obj.element.value=="1"){
			 CusComRelApital.loan_card._obj._renderHidden(true);
		}
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =CusComRelApital.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	/*--user code begin--*/

	/*--user code end--*/

</script>
</head>
<body class="page_content" onload="doOnLoad()" >

	<emp:gridLayout id="CusComRelApitalGroup" title="资本构成信息" maxColumn="2">
			<emp:select id="CusComRelApital.cert_typ" label="出资人证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP" colSpan="2"/>
			<emp:text id="CusComRelApital.cert_code" label="出资人证件号码" maxlength="20" required="true" readonly="true" colSpan="2"/>
			<emp:text id="CusComRelApital.cus_id_rel" label="出资人客户码" required="true" maxlength="35" readonly="true"/>
			<emp:text id="CusComRelApital.cus_id" label="客户码" maxlength="35" required="false" hidden="true" />
			<emp:text id="CusComRelApital.invt_name" label="出资人名称" maxlength="80" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusComRelApital.invt_typ" label="出资人性质" required="false" dictname="STD_ZB_INVESTOR2" readonly="true" hidden="true" colSpan="2"/>
			<emp:select id="CusComRelApital.rela_type" label="关联类型" required="true" dictname="STD_ZB_RELA_TYP" colSpan="2" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
			<emp:text id="CusComRelApital.loan_card" label="出资人贷款卡编号" maxlength="16" required="false" readonly="true" onblur="cheakCardId() "/>
			<emp:select id="CusComRelApital.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusComRelApital.invt_amt" label="出资金额（万元）" maxlength="18" dataType="Currency" required="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusComRelApital.invt_type" label="出资类型" required="true" dictname="STD_ZB_INVT_TYPE" />
			<emp:text id="CusComRelApital.invt_perc" label="出资比例" maxlength="10" dataType="Percent" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="CusComRelApital.com_invt_desc" label="出资说明" maxlength="250" required="false" colSpan="2" />
			<emp:date id="CusComRelApital.inv_date" label="出资时间" required="true" onblur="CheckDate()"/>
			<emp:textarea id="CusComRelApital.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComRelApital.cus_name_rel" label="关联客户姓名" maxlength="80" required="false" readonly="false" hidden="true"/>
			<emp:text id="CusComRelApital.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComRelApital.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComRelApital.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComRelApital.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComRelApital.last_upd_date" label="更新日期" required="false" hidden="true"/>
		</emp:gridLayout>

	<div align="center">
		<br>
		<%if(!"".equals(one_key) && one_key != null) {%>
			  <emp:button id="returnByOneKey" label="返回" />
		<% }else{%>
			 <emp:button id="return" label="返回"/> 	
		<% }%> 
	</div>
</body>
</html>
</emp:page>
