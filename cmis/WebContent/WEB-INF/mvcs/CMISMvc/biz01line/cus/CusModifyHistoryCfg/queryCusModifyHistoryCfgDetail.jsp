<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
		var url = '<emp:url action="queryCusModifyHistoryCfgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//加载的时候删除默认请选择提示
	window.onload = function(){
		 fromList._obj.element.options.remove(0);
		 toList._obj.element.options.remove(0);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:gridLayout id="CusModifyHistoryCfgGroup" title="客户修改历史配置" maxColumn="2">
			<emp:text id="CusModifyHistoryCfg.model_id" label="表名" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusModifyHistoryCfg.input_id_displayname" label="登录人"  required="true" readonly="true"/>
			<emp:text id="CusModifyHistoryCfg.input_br_id_displayname" label="登录机构"  required="true" readonly="true"/>
			<emp:date id="CusModifyHistoryCfg.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			<emp:select id="fromList" label="模型字段" dictname="STD_ZB_TABLEMODEL_COLUMN" size="30" ondblclick="putToList()" readonly="false"></emp:select>
			<emp:select id="toList" label="配置字段" dictname="STD_ZB_CFG_COLUMN" size="30" ondblclick="putFromList()" readonly="false"></emp:select>
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button label="返回到列表页面" id="return"></emp:button>
	</div>
</body>
</html>
</emp:page>
