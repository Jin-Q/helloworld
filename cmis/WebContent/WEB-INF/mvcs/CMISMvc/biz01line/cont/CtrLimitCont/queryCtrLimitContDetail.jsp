<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String his = "";
    if(context.containsKey("his")){
    	his = (String)context.getDataValue("his");
    }
%>
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
		var his = '<%=his %>';
		if(his == "is"){
			var url = '<emp:url action="queryCtrLimitContHisList.do"/>';
		}else{
			var url = '<emp:url action="queryCtrLimitContList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="maintabs" id="maintabs">
		<emp:tab label="额度合同基本信息" id="maintabs">
			<emp:gridLayout id="CtrLimitContGroup" title="额度合同申请表" maxColumn="2">
					<emp:text id="CtrLimitCont.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="CtrLimitCont.serno" label="业务编号" maxlength="40" required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.cont_cn" label="中文合同编号" maxlength="200" required="true" />
					<emp:text id="CtrLimitCont.cus_id" label="客户码" maxlength="32" required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
					<emp:select id="CtrLimitCont.cur_type" label="币种"   dictname="STD_ZX_CUR_TYPE" required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.cont_amt" label="合同金额" maxlength="16" required="false" readonly="true"/>
					<emp:date id="CtrLimitCont.start_date" label="起始日期" required="false" readonly="true"/>
					<emp:date id="CtrLimitCont.end_date" label="到期日期"  required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" hidden="true" required="false" />
					<emp:textarea id="CtrLimitCont.memo" label="备注" maxlength="200" required="false" readonly="true"/>
				</emp:gridLayout>
				
				
				<emp:gridLayout id="CtrLimitContGroup" title="机构信息" maxColumn="2">
					<emp:pop id="CtrLimitCont.manager_br_id_displayname" label="管理机构" defvalue="${context.organNo}" required="true" readonly="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
					<emp:text id="CtrLimitCont.input_id_displayname" label="登记人"  defvalue="${context.currentUserId}" required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.input_br_id_displayname" label="登记机构" defvalue="${context.organNo}"  required="false" readonly="true"/>
					<emp:text id="CtrLimitCont.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="CtrLimitCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" />
					<emp:text id="CtrLimitCont.input_id" label="登记人" maxlength="32" required="false" hidden="true" />
					<emp:text id="CtrLimitCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
				</emp:gridLayout>
			
			<div align="center">
				<br>
				<emp:button id="return" label="返回到列表页面"/>
			</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	
	
</body>
</html>
</emp:page>
