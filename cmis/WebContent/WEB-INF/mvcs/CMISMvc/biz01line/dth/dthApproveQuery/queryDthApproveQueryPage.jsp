<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<html>
<head>
<title>审批报备</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/** 上周授信begin **/
	function doViewLmtLast(){
		var url = '<emp:url action="getDthApproveQueryPage.do"/>?submitType=getDthApproveLmtLast';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLmtLast(){ //link，多传一个userid
		var data = ApproveQueryList._obj.getSelectedData()[0];
		var userid=data.userid._getValue();
		var url = '<emp:url action="singleDthApproveQueryPage.do"/>?submitType=getDthApproveLmtLast&userid='+userid;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** 上周授信end **/

	/** 本周授信begin **/
	function doViewLmtThis(){
		var url = '<emp:url action="getDthApproveQueryPage.do"/>?submitType=getDthApproveLmtThis';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLmtThis(){ //link，多传一个userid
		var data = ApproveQueryList._obj.getSelectedData()[0];
		var userid=data.userid._getValue();
		var url = '<emp:url action="singleDthApproveQueryPage.do"/>?submitType=getDthApproveLmtThis&userid='+userid;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** 本周授信end **/

	/** 上周评级begin **/
	function doViewCcrLast(){
		var url = '<emp:url action="getDthApproveQueryPage.do"/>?submitType=getDthApproveCcrLast';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doCcrLast(){ //link，多传一个userid
		var data = ApproveQueryList._obj.getSelectedData()[0];
		var userid=data.userid._getValue();
		var url = '<emp:url action="singleDthApproveQueryPage.do"/>?submitType=getDthApproveCcrLast&userid='+userid;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** 上周评级end **/

	/** 本周评级begin **/
	function doViewCcrThis(){
		var url = '<emp:url action="getDthApproveQueryPage.do"/>?submitType=getDthApproveCcrThis';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doCcrThis(){ //link，多传一个userid
		var data = ApproveQueryList._obj.getSelectedData()[0];
		var userid=data.userid._getValue();
		var url = '<emp:url action="singleDthApproveQueryPage.do"/>?submitType=getDthApproveCcrThis&userid='+userid;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** 本周评级end **/
		
</script>
</head>

<body class="page_content">
	<div align="left">
		<emp:button id="viewLmtThis" label="本周授信" op="lmtThis"/>
		<emp:button id="viewLmtLast" label="上周授信" op="lmtLast"/>
		<emp:button id="viewCcrThis" label="本周评级" op="ccrThis"/>
		<emp:button id="viewCcrLast" label="上周评级" op="ccrLast"/>		
	</div>
	
	<emp:table icollName="ApproveQueryList" pageMode="false" url="queryDthApproveQueryPage.do" >
		<emp:text id="userid" label="审批官编号" />
		<emp:text id="username" label="审批官名称" />
		<emp:link id="this_lmt" label="本周授信"  operation="LmtThis"/>
		<emp:link id="last_lmt" label="上周授信"  operation="LmtLast"/>
		<emp:link id="this_ccr" label="本周评级"  operation="CcrThis"/>
		<emp:link id="last_ccr" label="上周评级"  operation="CcrLast"/>
	</emp:table>
	
</body>
</html>
</emp:page>