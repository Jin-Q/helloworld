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
		var url = '<emp:url action="queryCusHandoverLogList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
    function doOnLoad(){
	    doQuery();
	}
	function doQuery(){
	    var form = document.getElementById('queryForm');
	    
	    CusHandoverLst._toForm(form);
	    
	    CusHandoverLstList._obj.ajaxQuery(null,form);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup id="CusHandoverLog_tabs" mainTab="tab1">
  	 <emp:tab label="客户移交" id="tab1"  needFlush="true" initial="true">
	<emp:gridLayout id="CusHandoverLogGroup" title="客户移交日志" maxColumn="2">
			<emp:text id="CusHandoverLog.serno" label="申请流水号" maxlength="40" required="true" />
			<emp:select id="CusHandoverLog.org_type" label="接收机构与移出机构关系" required="true" dictname="STD_ZB_ORG_TYPE" />
			<emp:select id="CusHandoverLog.handover_scope" label="移交范围" required="true" dictname="STD_ZB_HAND_SCOPE" />
			<emp:select id="CusHandoverLog.handover_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" />
			<emp:text id="CusHandoverLog.area_code" label="区域编码" maxlength="12" required="false" hidden="true"/>
			<emp:text id="CusHandoverLog.area_name" label="区域名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="CusHandoverLog.handover_id" label="移出人" maxlength="20" hidden="true" />
			<emp:text id="CusHandoverLog.handover_br_id" label="移出机构" maxlength="20" hidden="true" />
			<emp:text id="CusHandoverLog.receiver_id" label="接收人" maxlength="20" hidden="true" />
			<emp:text id="CusHandoverLog.receiver_br_id" label="接收机构" maxlength="20" hidden="true" />
			
			<emp:text id="CusHandoverLog.handover_id_displayname" label="移出人"  required="true" />
            <emp:text id="CusHandoverLog.handover_br_id_displayname" label="移出机构"  required="true" />
            <emp:text id="CusHandoverLog.receiver_id_displayname" label="接收人"   required="true" />
            <emp:text id="CusHandoverLog.receiver_br_id_displayname" label="接收机构"  required="true" />
			
			<emp:textarea id="CusHandoverLog.handover_detail" label="移交说明" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusHandoverLog.handover_date" label="移交日期" maxlength="10" required="true" />
	</emp:gridLayout>
	</emp:tab>
	 <emp:tab label="客户移交明细" id="tab2"  needFlush="true" initial="false">	
    <form  method="POST" action="#" id="queryForm" >
    </form>
    <emp:text id="CusHandoverLst.serno" label="申请流水号" defvalue="${context.CusHandoverLog.serno}" hidden="true"/>
    
    <emp:table icollName="CusHandoverLstList" pageMode="true" url="pageCusHandoverLstQuery.do" reqParams="CusHandoverLst.serno=${context.CusHandoverLog.serno}">
        <emp:text id="handover_type" label="业务类型" dictname="STD_ZB_OP_TYPE" />
        <emp:text id="cus_id" label="客户码" />
        <emp:text id="business_detail" label="移交业务说明" />
        <emp:text id="serno" label="申请流水号" hidden="true"/>
    </emp:table>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
