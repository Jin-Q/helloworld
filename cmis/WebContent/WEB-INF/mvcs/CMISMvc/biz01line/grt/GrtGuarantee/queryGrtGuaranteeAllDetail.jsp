<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String tab = "";
	if(context.containsKey("tab")){
		tab = (String)context.getDataValue("tab");
	}
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%>
<%
request.setAttribute("canwrite","");
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>


<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryGrtGuaranteeAllList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLoad(){
		}
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var guarty_cus_id  =GrtGuarantee.guarty_cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+guarty_cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="GrtGuaranteeGroup" title="保证人信息" maxColumn="2">
			<emp:text id="GrtGuarantee.guar_cont_no" label="担保合同编号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="GrtGuarantee.guarty_cus_id" label="保证人客户码" required="false" />
			<emp:text id="GrtGuarantee.guarty_cus_id_displayname" label="保证人客户名称" required="false" />
			<emp:select id="GrtGuarantee.guar_type" label="保证形式" required="false" dictname="STD_GUAR_FORM" />
			<emp:text id="GrtGuarantee.guar_amt" label="保证金额" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="GrtGuarantee.cus_id" label="借款人客户码" required="false"/>
			<emp:text id="GrtGuarantee.cus_id_displayname" label="借款人客户名称" required="false"/>
			<emp:text id="GrtGuarantee.guar_start_date" label="担保起始日期" maxlength="30" required="false" readonly="true"/>
			<emp:text id="GrtGuarantee.guar_end_date" label="担保终止日期" maxlength="30" required="false" readonly="true"/>
	</emp:gridLayout>
	<emp:gridLayout id="GrtGuaranteeGroup" maxColumn="2" title="登记信息">
			<emp:pop id="GrtGuarantee.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  readonly="true"/>
			<emp:pop id="GrtGuarantee.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="true"/>
			<emp:text id="GrtGuarantee.input_id_displayname" label="登记人"  required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarantee.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarantee.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:text id="GrtGuarantee.manager_id" label="主管客户经理" required="false" readonly="true" hidden="true" />
			<emp:text id="GrtGuarantee.manager_br_id" label="主管机构" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarantee.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarantee.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<%if(!"tab".equals(tab)){ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
		<%if(!"".equals(one_key) && one_key != null) {%>
			  <emp:button id="returnByOneKey" label="返回" />
		 <%} %>
	</div>
</body>
</html>
</emp:page>
