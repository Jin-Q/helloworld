<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdPlocy._toForm(form);
		PrdPlocyList._obj.ajaxQuery(null,form);
	};
	
	function doConn(){
		//var schemeId = PrdPolcyScheme.schemeid._getValue();
		var data = PrdPlocyList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择关联资料");
		}else {
			var schemecodeArr = "";
			//组装多记录选择返回参数
			for(var i=0;i<data.length;i++){
				schemecodeArr += data[i].schemecode._getValue()+",";
			}
			var url="<emp:url action='doConnPrdPlocyAndPrdScheme.do'/>?schemeId=${context.schemeId}&schemecodeArr="+schemecodeArr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, "",null)
			
			alert("关联成功！");
			window.location.reload();

			window.opener.location.reload();
		}
		
	}
	
	function doReset(){
		page.dataGroups.PrdPlocyGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPlocyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPlocy.schemecode" label="政策资料代码" />
			<emp:select id="PrdPlocy.ifwarrant" label="是否权证类" dictname="STD_ZX_YES_NO" />
			<emp:select id="PrdPlocy.schemetype" label="政策资料类型" dictname="STD_ZB_INFO_TYPE" />
			<emp:text id="PrdPlocy.inputid" label="登记人员" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="conn" label="引入" />
	</div>

	<emp:table icollName="PrdPlocyList" pageMode="true" selectType="2" url="queryPrdPlocyListPopPage.do?schemeId=${context.schemeId}">
		<emp:text id="schemecode" label="政策资料代码" />
		<emp:text id="schemedesc" label="政策资料描述" />
		<emp:text id="ifwarrant" label="是否权证类" />
		<emp:text id="schemetype" label="政策资料类型" />
		<emp:text id="inputid" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="orgid" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    