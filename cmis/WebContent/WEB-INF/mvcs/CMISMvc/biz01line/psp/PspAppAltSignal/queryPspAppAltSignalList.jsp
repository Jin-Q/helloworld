<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspAppAltSignal._toForm(form);
		PspAppAltSignalList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspAppAltSignalPage() {
		var paramStr = PspAppAltSignalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAppAltSignalUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspAppAltSignal() {
		var paramStr = PspAppAltSignalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAppAltSignalViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspAppAltSignalPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspAppAltSignalAddPage.do"/>?'+'task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspAppAltSignal() {
		var paramStr = PspAppAltSignalList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspAppAltSignalRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert("删除成功！");
							window.location.reload();
						}
					}	
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.PspAppAltSignalGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
	<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspAppAltSignalPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspAppAltSignalPage" label="修改" op="update"/>
		<emp:button id="deletePspAppAltSignal" label="删除" op="update"/>
	<%} %>
		<emp:button id="viewPspAppAltSignal" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspAppAltSignalList" pageMode="false" url="pagePspAppAltSignalQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户编码" hidden="true"/>
		<emp:text id="signal_info" label="风险预警信息内容及影响" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE"/>
		<emp:text id="last_date" label="预计持续时间（天）" />
		<emp:text id="disp_mode" label="处置措施及进展情况" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    