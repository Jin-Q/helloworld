<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/empext.tld" prefix="empext" %>
<empext:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doReturn() {
		var url = '<empext:url url="queryFncConfDefFormatList.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="updateFncConfDefFormatRecord.do" method="POST">
	</form>
	
	<div id="FncConfDefFormatGroup" class="emp_group_div">
		<empext:gridLayout maxColumn="2" title="报表配置定义表">
			<empext:text id="FncConfDefFormat.style_id" label="报表样式编号" maxlen="6" required="true" />
			<empext:text id="FncConfDefFormat.item_id" label="项目编号" maxlen="9" required="true" />
			<empext:text id="FncConfDefFormat.fnc_conf_order" label="顺序编号" maxlen="10" required="false" />
			<empext:text id="FncConfDefFormat.fnc_conf_cotes" label="栏位" maxlen="38" required="false" />
			<empext:select id="FncConfDefFormat.fnc_conf_row_flg" label="行次标识" required="false" dictname="STD_ZB_FNCCONFROW" />
			<empext:text id="FncConfDefFormat.fnc_conf_indent" label="层次" maxlen="38" required="false" />
			<empext:textarea id="FncConfDefFormat.fnc_conf_prefix" label="前缀" maxlen="200" required="false" colSpan="2" />
			<empext:select id="FncConfDefFormat.fnc_item_edit_typ" label="项目编辑方式" required="false" dictname="STD_ZB_FNCITEMEDT" />
			<empext:text id="FncConfDefFormat.fnc_conf_display_amt" label="显示数值" maxlen="16" required="false" />
			<empext:text id="FncConfDefFormat.fnc_conf_append_row" label="追加行数" maxlen="38" required="false" />
			<empext:text id="FncConfDefFormat.fnc_conf_display_tpy" label="默认现实类型" maxlen="2" required="false" />
			<empext:textarea id="FncConfDefFormat.fnc_conf_check_formula" label="检查公式1" maxlen="300" required="false" colSpan="2" />
			<empext:textarea id="FncConfDefFormat.fnc_conf_cal_formula" label="计算公式1" maxlen="300" required="false" colSpan="2" />
		</empext:gridLayout>
	</div>
		
	<div align="center">
		<br>
		<empext:button id="return" label="返回"/>
	</div>
</body>
</html>
</empext:page>
