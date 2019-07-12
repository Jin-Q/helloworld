<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusEvent.cus_id._obj.element.value;
		var paramStr="CusEvent.cus_id="+cus_id;
		var url = '<emp:url action="queryCusEventList.do"/>&'+paramStr+"&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnLoad() {
		if(CusEvent.event_bank_flg._obj.element.value == 1){
			CusEvent.event_bch_name._obj._renderHidden(true);
		}else if(CusEvent.event_bank_flg._obj.element.value == 2){
			CusEvent.event_bch_name._obj._renderHidden(false);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:gridLayout id="CusEventGroup" title="客户重大事件" maxColumn="2">
		<emp:text id="CusEvent.serno" label="登记流水号" maxlength="40" required="true" />
		<emp:text id="CusEvent.cus_id" label="客户码" maxlength="20" required="true" hidden="true"/>
		<emp:date id="CusEvent.event_dt" label="发生日期" required="true" />
		<emp:text id="CusEvent.event_name" label="事件名称" maxlength="60" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:select id="CusEvent.event_typ" label="事件类型" required="true" dictname="STD_ZB_EVENT_TYP" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
		<emp:select id="CusEvent.event_classify" label="事件分类" required="true" dictname="STD_ZB_EVENT_KIND" colSpan="2" />
		<emp:select id="CusEvent.event_imp_deg" label="事件影响程度" required="true" dictname="STD_ZB_EVENT_IMP_DEG" />
		<emp:textarea id="CusEvent.event_desc" label="事件描述" maxlength="200" required="true" colSpan="2" />
		<emp:text id="CusEvent.event_amt" label="涉及金额(元)" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusEvent.event_exps_com" label="曝光单位" maxlength="60" required="false" />
		<emp:select id="CusEvent.event_bank_flg" label="是否本行发生" required="true" dictname="STD_ZX_YES_NO" />
		<emp:text id="CusEvent.event_bch_name" label="发生机构" maxlength="60"/>
		<emp:select id="CusEvent.status" label="状态" required="false" hidden="true" dictname="STD_ZB_COMM_STATUS" />
		<emp:text id="CusEvent.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
		<emp:text id="CusEvent.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		<emp:date id="CusEvent.input_date" label="登记日期" required="false" readonly="true" hidden="true" defvalue="$OPENDAY"/>
		<emp:text id="CusEvent.last_upd_id" label="更新人" required="false" hidden="true" />
		<emp:date id="CusEvent.last_upd_date" label="更新日期" required="false" hidden="true" />
		<emp:text id="CusEvent.logout_id" label="注销人" maxlength="20" required="false" hidden="true"/>
		<emp:text id="CusEvent.logout_br_id" label="注销机构" maxlength="20" required="false" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>