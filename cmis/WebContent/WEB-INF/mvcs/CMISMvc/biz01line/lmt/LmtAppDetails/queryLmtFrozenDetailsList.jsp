<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String updflag = context.getDataValue("updflag").toString();
	String app_type = context.getDataValue("app_type").toString();
%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppDetails._toForm(form);
		LmtAppDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppDetailsPage() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFrozenDetailsUpdatePage.do"/>?'+paramStr+"&app_type=<%=app_type%>";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFrozenDetails() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFrozenDetailsViewPage.do"/>?'+paramStr+"&updflag=<%=updflag%>&app_type=<%=app_type%>";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdateLmtAgrDetailsPage() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
	<%if("update".equals(updflag)){ %>
		<emp:button id="getUpdateLmtAppDetailsPage" label="修改" />
	<%} %>
		<emp:button id="viewLmtFrozenDetails" label="查看" />
	</div>

	<emp:table icollName="LmtAppDetailsList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" hidden="true" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_code" label="授信额度编号" hidden="true"/>
		<emp:text id="org_limit_code" label="授信额度编号" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<%if(app_type.equals("03")){ %>
		<emp:text id="froze_amt" label="冻结金额" dataType="Currency" hidden="false"/>
		<emp:text id="unfroze_amt" label="解冻金额" dataType="Currency" hidden="true"/>
		<%}else{ %>
		<emp:text id="froze_amt" label="冻结金额" dataType="Currency" hidden="true"/>
		<emp:text id="unfroze_amt" label="解冻金额" dataType="Currency" hidden="false"/>
		<%} %>
		<emp:text id="term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="term" label="授信期限" dictname="STD_ZB_TERM_TYPE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    