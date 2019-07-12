<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" />


<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">



	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doReturn() {
		var url = '<emp:url action="queryFncConfStylesList.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="updateFncConfStylesRecord.do" method="POST">
	</form>
	
	<div id="FncConfStylesGroup" class="emp_group_div">
		<emp:gridLayout id="FncConfStylesGroup" maxColumn="2" title="报表样式列表">
			<emp:text id="FncConfStyles.style_id" label="报表样式编号" maxlength="6" required="true" readonly="true"/>
			<emp:select id="FncConfStyles.fnc_conf_typ" label="所属报表种类" required="false" dictname="STD_ZB_FNC_TYP" readonly="true"/>
			<emp:text id="FncConfStyles.fnc_name" label="报表名称" maxlength="200" required="false"  readonly="true"/>
			<emp:text id="FncConfStyles.fnc_conf_dis_name" label="显示名称" maxlength="200" required="false" readonly="true"/>
			
			<emp:select id="FncConfStyles.no_ind" label="新旧报表标志"  dictname="STD_ZB_FNC_ON_TYP" readonly="true" hidden="true"/>
			<emp:select id="FncConfStyles.com_ind" label="企事业报表标志"  dictname="STD_ZB_FNC_COMIND" readonly="true" hidden="true"/>
			<emp:select id="FncConfStyles.fnc_conf_data_col" label="数据列数" required="true" dictname="STD_ZB_FNC_COL" />	
			<emp:select id="FncConfStyles.fnc_conf_cotes" label="栏位" required="true" dictname="STD_ZB_FNC_COTES" />
			
			<emp:select id="FncConfStyles.data_dec1" label="第一列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec2" label="第二列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec3" label="第三列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec4" label="第四列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec5" label="第五列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec6" label="第六列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec7" label="第七列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec8" label="第八列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:text id="FncConfStyles.head_left" label="表头左侧描述" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncConfStyles.head_center" label="表头中部描述" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncConfStyles.head_right" label="表头右侧描述" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncConfStyles.food_left" label="表尾左侧描述" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncConfStyles.food_center" label="表尾中部描述" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncConfStyles.food_right" label="表尾右侧描述" maxlength="200" required="false" hidden="true"/>
			
				
				
		</emp:gridLayout>
	</div>
		
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
