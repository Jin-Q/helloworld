<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	String showButton = request.getParameter("showButton");
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryMortStorExwaInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLoad(){
		var stor_exwa_mode=MortStorExwaInfo.stor_exwa_mode._getValue();
		if(stor_exwa_mode=="02"){//借出的时候需要显示归还时间
			MortStorExwaInfo.back_date._obj._renderHidden(false);
			MortStorExwaInfo.back_date._obj._renderRequired(true);

			MortStorExwaInfo.act_back_date._obj._renderHidden(false);
		}else{//除借出外
			MortStorExwaInfo.back_date._obj._renderHidden(true);
			MortStorExwaInfo.back_date._obj._renderRequired(false);
			MortStorExwaInfo.act_back_date._obj._renderHidden(true);
		}
	}
	
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup id="MortStorExwaInfoTab" mainTab="appinf">
   <emp:tab label="申请信息" id="appinf"  needFlush="true" initial="true" >
		<emp:gridLayout id="MortStorExwaInfoGroup" maxColumn="2" title="押品出入库申请">
		<emp:text id="MortStorExwaInfo.serno" label="业务编号" maxlength="60" required="false" readonly="true"/>
			<emp:select id="MortStorExwaInfo.stor_exwa_mode" label="出入库方式" required="true" dictname="STD_STOR_EXWA_MODEL" />
			<emp:date id="MortStorExwaInfo.back_date" label="预计归还时间" required="true" />
			<emp:date id="MortStorExwaInfo.act_back_date" label="实际归还时间" readonly="true" />
			<emp:textarea id="MortStorExwaInfo.exwa_memo" label="出入库说明" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortStorExwaInfoGroup" maxColumn="2" title="登记信息">	
		    <emp:pop id="MortStorExwaInfo.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="MortStorExwaInfo.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true"/>
			<emp:text id="MortStorExwaInfo.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false" defvalue="$currentUserName"/>
			<emp:text id="MortStorExwaInfo.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" defvalue="$organName"/>
			<emp:text id="MortStorExwaInfo.input_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="MortStorExwaInfo.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="false"/>
			<emp:text id="MortStorExwaInfo.manager_id" label="主管客户经理" required="false" hidden="true" />
			<emp:text id="MortStorExwaInfo.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="MortStorExwaInfo.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="MortStorExwaInfo.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
	</emp:tab>
    <emp:tab id="warrant_tab" label="权证详情" url="queryMortGuarantyCertiInfoAddList.do" reqParams="op=view&serno=${context.serno}" initial="false" needFlush="true"/>
</emp:tabGroup>
		<%if(!"N".equals(showButton)){ %>
		<div align="center">
			<br>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
		<%} %>
</body>
</html>
</emp:page>
