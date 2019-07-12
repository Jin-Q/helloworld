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
		var url = '<emp:url action="queryCusTrusteeLogList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
    function doOnLoad(){
        doQuery();
    }
    function doQuery(){
        var form = document.getElementById('queryForm');
        CusTrusteeLst._toForm(form);
        
        CusTrusteeLstList._obj.ajaxQuery(null,form);
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:gridLayout id="CusTrusteeLogGroup" title="客户托管日志" maxColumn="2">
			<emp:text id="CusTrusteeLog.serno" label="申请流水号" maxlength="40" required="true" />
			<emp:select id="CusTrusteeLog.org_type" label="托管机构与委托机构关系" required="true" dictname="STD_ZB_ORG_TYPE" />
			<emp:select id="CusTrusteeLog.trustee_scope" label="托管范围" required="true" dictname="STD_ZB_HAND_SCOPE" />
			<emp:pop id="CusTrusteeLog.area_code" label="区域编码" url="null" required="false" hidden="true"/>
			
			<emp:pop id="CusTrusteeLog.consignor_id_displayname" label="委托人" url="null" required="true" />
			<emp:pop id="CusTrusteeLog.consignor_br_id_displayname" label="委托机构" url="null" required="true" />
			<emp:pop id="CusTrusteeLog.supervise_id_displayname" label="监交人" url="null" required="false" />
			<emp:pop id="CusTrusteeLog.supervise_br_id_displayname" label="监交机构" url="null" required="false" />
			<emp:pop id="CusTrusteeLog.trustee_id_displayname" label="托管人" url="null" required="true" />
			<emp:pop id="CusTrusteeLog.trustee_br_id_displayname" label="托管机构" url="null" required="true" />
			
			<emp:textarea id="CusTrusteeLog.trustee_detail" label="托管说明" maxlength="250" required="false" colSpan="2" />
			<emp:date id="CusTrusteeLog.trustee_date" label="托管日期" required="true" />
			<emp:date id="CusTrusteeLog.retract_date" label="收回日期" required="true" />
			
            <emp:text id="CusTrusteeLog.area_name" label="区域名称" maxlength="100" required="false" hidden="true"/>
			
			<emp:pop id="CusTrusteeApp.consignor_br_id" label="委托机构" url="getValuequerySOrgList.do" returnMethod="conOrg" required="true" hidden="true"/>
            <emp:pop id="CusTrusteeApp.consignor_id" label="委托人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="conId" required="true" hidden="true"/>
            <emp:pop id="CusTrusteeApp.trustee_br_id" label="托管机构" url="getValuequerySOrgList.do" returnMethod="truOrg" hidden="true" />
            <emp:pop id="CusTrusteeApp.trustee_id" label="托管人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="truId" hidden="true" />
            <emp:pop id="CusTrusteeApp.supervise_br_id" label="监交机构" url="getValuequerySOrgList.do" returnMethod="supOrg" hidden="true" readonly="true"/>
            <emp:pop id="CusTrusteeApp.supervise_id" label="监交人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="supId" hidden="true" />
	</emp:gridLayout>
	
	<form  method="POST" action="#" id="queryForm" >
    </form>
    
    <emp:text id="CusTrusteeLst.serno" label="申请流水号" defvalue="${context.CusTrusteeLog.serno}" hidden="true"/>
    
    <emp:table icollName="CusTrusteeLstList" pageMode="true" url="pageCusTrusteeLstQuery.do" reqParams="CusTrusteeLst.serno=${context.CusTrusteeLog.serno}">
        <emp:text id="cus_id" label="客户代码" />
        <emp:text id="cus_name" label="客户名称" />
        <emp:text id="serno" label="申请流水号" hidden="true"/>
    </emp:table>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
