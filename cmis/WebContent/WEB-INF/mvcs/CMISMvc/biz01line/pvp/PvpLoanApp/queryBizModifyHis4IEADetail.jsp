<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);   
	String wf_flag="";
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	String modiflag="";
	if(context.containsKey("modiflag")){
		modiflag = (String) context.getDataValue("modiflag");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">	
	function doClose(){
		window.close();
	};
	function doOnLoad(){
		var pm_cn_cont_no  = BizModifyHis4IEA.pm_cn_cont_no._getValue();
		var cn_cont_no  = BizModifyHis4IEA.cn_cont_no._getValue();
		if(pm_cn_cont_no == cn_cont_no){
			BizModifyHis4IEA.pm_cn_cont_no._obj._renderHidden(true);
			BizModifyHis4IEA.cn_cont_no._obj._renderHidden(true);
		}
		var pm_extension_date  = BizModifyHis4IEA.pm_extension_date._getValue();
		var extension_date  = BizModifyHis4IEA.extension_date._getValue();
		if(pm_extension_date == extension_date){
			BizModifyHis4IEA.pm_extension_date._obj._renderHidden(true);
			BizModifyHis4IEA.extension_date._obj._renderHidden(true);
		}

		var pm_extension_rate  = BizModifyHis4IEA.pm_extension_rate._getValue();
		var extension_rate  = BizModifyHis4IEA.extension_rate._getValue();
		if(pm_extension_rate == extension_rate){
			BizModifyHis4IEA.pm_extension_rate._obj._renderHidden(true);
			BizModifyHis4IEA.extension_rate._obj._renderHidden(true);
		}
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>	
	<emp:gridLayout id="BizModifyHis4IEAGroup" title="展期修改历史对比信息" maxColumn="2">
			<emp:text id="BizModifyHis4IEA.agr_no" label="展期协议号" readonly="true" colSpan="2"/>
			<emp:text id="BizModifyHis4IEA.pm_cn_cont_no" label="中文合同编号(修改前)" readonly="true"/>
			<emp:text id="BizModifyHis4IEA.cn_cont_no" label="中文合同编号(修改后)" readonly="true"/>
			<emp:date id="BizModifyHis4IEA.pm_extension_date" label="展期到期日期(修改前)" readonly="true"/>
			<emp:date id="BizModifyHis4IEA.extension_date" label="展期到期日期(修改后)" readonly="true"/>
			<emp:text id="BizModifyHis4IEA.pm_extension_rate" label="展期利率（年）(修改前)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis4IEA.extension_rate" label="展期利率（年）(修改后)" dataType="Rate" readonly="true"/>
			<emp:text id="BizModifyHis4IEA.update_time" label="修改时间" readonly="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<%if(!"1".equals(wf_flag) && !"yes".equals(modiflag)){ %>
			<emp:button id="close" label="关闭" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
