<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusBlkListTempList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doReturnOther(){
		var url = '<emp:url action="queryCusBlkListListTwo.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function doOnload(){
		changeFromType();
	}
	/*--user code begin--*/
	//根据数据来源显示不同信息
	function changeFromType(){
		var dataSource = CusBlkListTemp.data_source._getValue();//数据来源
		if(dataSource=='20'){//系统外
			CusBlkListTemp.cus_id._obj._renderHidden(true);
			CusBlkListTemp.cus_id._obj._renderRequired(false);
		}else{
			CusBlkListTemp.cus_id._obj._renderHidden(false);
			CusBlkListTemp.cus_id._obj._renderRequired(true);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="updateCusBlkListRecord.do" method="POST">
		<emp:gridLayout id="CusBlkListGroup" maxColumn="2" title="共享客户信息">
			<emp:text id="CusBlkListTemp.serno" label="登记流水号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="true"/>	
			<emp:select id="CusBlkListTemp.data_source" label="数据来源" required="true" dictname="STD_ZB_DATA_SOURCE" colSpan="2" />
			<emp:pop id="CusBlkListTemp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=returnCus" required="true"  readonly="true"/>
			<emp:text id="CusBlkListTemp.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:select id="CusBlkListTemp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="10" readonly="true"/>
			<emp:text id="CusBlkListTemp.cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:select id="CusBlkListTemp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" colSpan="2" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="CusBlkListTemp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkListTemp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkListTemp.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone"/>
			<emp:text id="CusBlkListTemp.legal_addr" label="通讯地址" required="true" hidden="true"/>
			<emp:pop id="CusBlkListTemp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>	
			<emp:text id="CusBlkListTemp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:date id="CusBlkListTemp.black_date" label="列入日期" required="false" colSpan="2" 
				onblur="CheckDate(CusBlkListTemp.black_date,'列入日期不能大于当前日期')" hidden="true"/>
			<emp:textarea id="CusBlkListTemp.black_reason" label="客户描述" maxlength="250" 
				required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusBlkListGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkListTemp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusBlkListTemp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true"/>	
			<emp:text id="CusBlkListTemp.input_id_displayname" label="登记人" required="true" />
			<emp:text id="CusBlkListTemp.input_br_id_displayname" label="登记机构" required="true"/>
			<emp:date id="CusBlkListTemp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusBlkListTemp.manager_id" label="责任人" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.manager_br_id" label="管理机构" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
		</emp:gridLayout>
	
		<div align="center">
			<br>
			<%if("query".equals(type)){ %>
				<emp:button id="return" label="返回"/>
			<%}else{ %>
				<emp:button id="returnOther" label="返回"/>
			<%} %>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
