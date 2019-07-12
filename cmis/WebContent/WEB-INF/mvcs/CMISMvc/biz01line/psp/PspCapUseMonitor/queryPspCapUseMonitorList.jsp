<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
	String check_type = "";//检查类型
	if(context.containsKey("check_type")){
		check_type = (String)context.getDataValue("check_type");
	}
	//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspCapUseMonitor._toForm(form);
		PspCapUseMonitorList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspCapUseMonitorPage() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
		    //added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
			var check_type = '<%=check_type%>';
			var cus_id = '${context.cus_id}';
			var url = '<emp:url action="getPspCapUseMonitorUpdatePage.do"/>?'+paramStr+'&cus_id='+cus_id+'&dataType=first&check_type='+check_type;
			//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspCapUseMonitor() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
		    //added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
			var check_type = '<%=check_type%>';
			var url = '<emp:url action="getPspCapUseMonitorViewPage.do"/>?'+paramStr+'&check_type='+check_type;
			//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspCapUseMonitorPage() {
		var cus_id = '${context.cus_id}';
		var task_id = '${context.task_id}';
		//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
		var check_type = '<%=check_type%>';
		var url = '<emp:url action="getPspCapUseMonitorAddPage.do"/>?'+'cus_id='+cus_id+'&task_id='+task_id+'&dataType=first&check_type='+check_type;
		//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doReset(){
		page.dataGroups.PspCapUseMonitorGroup.reset();
	};

	function doDeletePspCapUseMonitor() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		var task_id = PspCapUseMonitorList._obj.getParamValue(['task_id']);
		var cus_id = '${context.cus_id}';
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
							var url = '<emp:url action="queryPspCapUseMonitorList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspCapUseMonitorRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};   
	/*--user code begin--*/
	function doload(){
		rq_cus_id = '${context.cus_id}';
		rq_task_id = '${context.task_id}';
	};
	function doPrintPspCapUseMonitor() {
		var bill_no = PspCapUseMonitorList._obj.getParamValue(['bill_no']);
		if('<%=op%>' == 'view'){
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/pspcurcheckView.raq&cus_id='+rq_cus_id+'&task_id='+rq_task_id+'&bill_no='+bill_no;
		}else{
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/pspcurcheck.raq&cus_id='+rq_cus_id+'&task_id='+rq_task_id+'&bill_no='+bill_no;
		}
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div class="emp_gridLayout_title">资金用途</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspCapUseMonitorPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspCapUseMonitorPage" label="修改" op="update"/>
		<emp:button id="deletePspCapUseMonitor" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspCapUseMonitor" label="查看" op="view"/>
		<emp:button id="printPspCapUseMonitor" label="打印" />
	</div>

	<emp:table icollName="PspCapUseMonitorList" pageMode="true" url="pagePspCapUseMonitorQuery.do?cus_id=${context.cus_id}">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="disb_date" label="用款日期" />
		<emp:text id="disb_amt" label="用款金额" dataType="Currency"/>
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="paorg_acct_no" label="收款人账号" />
		<emp:text id="paorg_no" label="收款人开户行行号" hidden="true"/>
		<emp:text id="paorg_name" label="收款人开户行名称" />
		<emp:text id="use_type" label="用途" dictname="STD_CAP_USE_TYPE"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    