<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIndLibList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewIndOpt() {
		var paramStr = IndLib.IndOpt._obj.getParamStr(['index_no','index_value']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndLibIndOptDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewIndPara() {
		var paramStr = IndLib.IndPara._obj.getParamStr(['index_no','enname']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndLibIndParaDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:tabGroup id="IndLib_tabs" mainTab="tab1">
<emp:tab label="指标库信息" id="tab1"  needFlush="true" initial="true">
	<emp:gridLayout id="IndLibGroup" title="指标库信息" maxColumn="2">
			<emp:text id="IndLib.index_no" label="指标编号" maxlength="12" required="true" readonly="true" />
			<emp:text id="IndLib.index_name" label="指标名称" maxlength="60" required="true" />
			<emp:text id="IndLib.fnc_index_rpt" label="财务指标编号" maxlength="16" required="true" />
			<emp:text id="IndLib.par_index_no" label="上级指标编号" maxlength="8" required="false" />
			<emp:select id="IndLib.index_property" label="指标性质"  required="true" dictname="STD_ZB_PARA_PROP"/>
			<emp:select id="IndLib.index_type" label="指标类别"  required="true" dictname="STD_ZB_IND_TYPE"/>
			<emp:select id="IndLib.input_type" label="指标取值方式"  required="true" dictname="STD_ZB_PARA_VAL_TYP"/>
			<emp:text id="IndLib.input_classpath" label="指标取值实现类" maxlength="100" required="true" />
			<emp:select id="IndLib.exe_cycle" label="执行周期"  required="false" dictname="STD_ZB_RUN_FREQ"/>
			<emp:select id="IndLib.index_level" label="指标级别"   required="false" dictname="STD_ZB_PARA_LEVEL"/>
	</emp:gridLayout>
</emp:tab>
	<br>
<%-- 
	<emp:tabGroup id="IndLib_tabs" mainTab="IndOpt_tab">
--%>
		<emp:tab id="IndOpt_tab" label="指标选项值配置">
			<div align="left">
				<emp:button id="viewIndOpt" label="查看" op="view_IndOpt"/>
			</div>
			<emp:table icollName="IndLib.IndOpt" pageMode="false" url="">
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="ind_desc" label="指标描述" />
		<emp:text id="index_value" label="指标选项值" />
		<emp:text id="value_score" label="选项值得分" />
			</emp:table>
		</emp:tab>
		<emp:tab id="IndPara_tab" label="参数设置">
			<div align="left">
				<emp:button id="viewIndPara" label="查看" op="view_IndPara"/>
			</div>
			<emp:table icollName="IndLib.IndPara" pageMode="false" url="">
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="enname" label="参数英文名" />
		<emp:text id="para_cnname" label="参数中文名" />
		<emp:text id="para_val_type" label="参数值类型" />
		<emp:text id="para_val_way" label="参数值来源" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align="center">
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
