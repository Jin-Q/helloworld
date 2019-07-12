<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String act = "";
	if(context.containsKey("act")){
		act = (String)context.getDataValue("act");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdSubTabAction._toForm(form);
		PrdSubTabActionList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdSubTabActionPage() {
		var paramStr = PrdSubTabActionList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdSubTabActionUpdatePage.do"/>?mainid=${context.mainid}&subid=${context.subid}&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdSubTabAction() {
		var paramStr = PrdSubTabActionList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdSubTabActionViewPage.do"/>?'+paramStr+'&act=<%=act%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdSubTabActionPage() {
		var url = '<emp:url action="getPrdSubTabActionAddPage.do"/>?mainid=${context.mainid}&subid=${context.subid}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeletePrdSubTabAction() {
		var paramStr = PrdSubTabActionList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("删除成功!");
								var url = '<emp:url action="queryPrdSubTabActionList.do"/>?mainid=${context.mainid}&subid=${context.subid}';
								url = EMPTools.encodeURI(url);
								window.location = url;  
							}else {
								alert("发生异常!");
							}
						}
					};
					var handleFailure = function(o){
						alert("异步请求出错！");	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					};
					var url = '<emp:url action="deletePrdSubTabActionRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
  


	
	function doReset(){
		page.dataGroups.PrdSubTabActionGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdSubTabActionGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdSubTabAction.pkid" label="主键" />
			<emp:text id="PrdSubTabAction.main_act_id" label="主资源操作ID" />
			<emp:text id="PrdSubTabAction.sub_act_id" label="从资源操作ID" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	<% 
		if(!"view".equals(act)){
	%>
		<emp:button id="getAddPrdSubTabActionPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdSubTabActionPage" label="修改" op="update"/>
		<emp:button id="deletePrdSubTabAction" label="删除" op="remove"/>
	<% 
		}
	%>
		<emp:button id="viewPrdSubTabAction" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdSubTabActionList" pageMode="true" url="pagePrdSubTabActionQuery.do?mainid=${context.mainid}&subid=${context.subid}">
		<emp:text id="pkid" label="主键" />
		<emp:text id="main_act_id" label="主资源操作ID" />
		<emp:text id="sub_act_id" label="从资源操作ID" />
		<emp:text id="memo" label="备注" />
		<emp:text id="input_id" label="登记人员" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_br_id" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    