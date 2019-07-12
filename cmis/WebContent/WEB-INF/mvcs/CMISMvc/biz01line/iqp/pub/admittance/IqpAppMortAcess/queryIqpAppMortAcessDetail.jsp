<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flow = "";//流程查看标识
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
	}
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAppMortAcessList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doViewIqpAppMortDetail() {
		var paramStr = IqpAppMortDetailList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMortDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doShowIqpAppMortDetail(){
		var serno = IqpAppMortAcess.serno._getValue();
		var url = '<emp:url action="showCatalogManaTree.do"/>?close=close&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	
</script>
</head>
<body class="page_content">
		<emp:gridLayout id="IqpAppMortAcessGroup" title="准入申请基本信息" maxColumn="2">
			<emp:text id="IqpAppMortAcess.serno" label="业务流水号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:select id="IqpAppMortAcess.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_ADMIT_TYPE" defvalue="1" readonly="true"/>
			<emp:date id="IqpAppMortAcess.acsee_date" label="准入日期" required="true" defvalue="$OPENDAY"/>
			<emp:textarea id="IqpAppMortAcess.memo" label="准入描述" maxlength="500" required="false" colSpan="2" />
			
			<emp:text id="IqpAppMortAcess.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpAppMortAcess.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:pop id="IqpAppMortAcess.manager_id_displayname" label="责任人" required="true" readonly="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpAppMortAcess.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="true"/>
			<emp:text id="IqpAppMortAcess.input_id_displayname" label="登记人"   required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="IqpAppMortAcess.input_br_id_displayname" label="登记机构"   required="false" defvalue="$organName" readonly="true"/>
			<emp:date id="IqpAppMortAcess.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="IqpAppMortAcess.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
			<emp:text id="IqpAppMortAcess.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="IqpAppMortAcess.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
		</emp:gridLayout>
	<div align="center">
		<br>
		<%if(flow.equals("wf")){ %>
		<%}else{ %>
		  <emp:button id="return" label="返回列表"/>
	    <%} %>
	</div>
	<div class='emp_gridlayout_title'>准入押品细类列表</div>
	<div align="left">
		<emp:button id="viewIqpAppMortDetail" label="查看" op="view"/>
		<emp:button id="showIqpAppMortDetail" label="预览押品类型树" op="view"  mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>

	<emp:table icollName="IqpAppMortDetailList" pageMode="false" url="pageIqpAppMortDetailQuery.do">
		<emp:text id="serno" label="业务流水号" hidden="true" />
		<emp:text id="catalog_no" label="目录编号" />
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="sup_catalog_no_displayname" label="上级目录" />
		<emp:text id="attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
		<emp:text id="commo_trait" label="商品特性" hidden="true"/>
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT" />
		<emp:text id="imn_rate" label="基准质押率" dataType="Percent"/>
	</emp:table>
</body>
</html>
</emp:page>
