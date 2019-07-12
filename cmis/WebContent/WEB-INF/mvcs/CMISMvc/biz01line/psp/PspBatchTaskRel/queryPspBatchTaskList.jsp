<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String major_task_id = request.getParameter("major_task_id");
	String manager_id = request.getParameter("manager_id");
	String batch_task_type = request.getParameter("batch_task_type").trim();
	String op= request.getParameter("op").toString().trim();
	String roles = "";
	if(context.containsKey("roles")){
		roles = (String)context.getDataValue("roles");
	} 
	String flagSM = "";//扫描岗标志
	// modified by yangzy 2015/04/10 需求：XD150325024，集中作业扫描岗权限改造 start
	if(roles.contains("3002") ){
		flagSM = "1";
	}
	// modified by yangzy 2015/04/10 需求：XD150325024，集中作业扫描岗权限改造 end
%>
<%@page import="java.math.BigDecimal"%>
<emp:page>
<html>
<head>
<title>子任务关联页面</title>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	//子任务新增
	function doAddSubPspCheckTask(){
		var url = '<emp:url action="querySubPspCheckTaskListPop.do"/>?manager_id=<%=manager_id%>&batch_task_type=<%=batch_task_type%>&major_task_id=<%=major_task_id%>';
	    url = EMPTools.encodeURI(url);
	    var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		modifyWindow = window.open(url,'newWindow',param);
	};
	
	//子任务删除
	function doDelSubPspCheckTask(){
		var data = PspBatchTaskList._obj.getSelectedData();
		var num = PspBatchTaskList._obj.getSelectedData().length;
		if(data.length == 0){
			alert("请选择子任务项！");
			return false;
		}else {
			if(confirm("是否确认删除该子任务？")){
				var subTaskIdStr="";//子任务编号字符串，以","间隔
				var num = PspBatchTaskList._obj.getSelectedData().length;
				if (num != 0) {
					for(var i=0;i<num;i++){
						subTaskIdStr = subTaskIdStr+PspBatchTaskList._obj.getSelectedData()[i].sub_task_id._getValue()+",";
					}
				}
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
							var url = '<emp:url action="queryPspBatchTaskList.do"/>?major_task_id=<%=major_task_id%>&manager_id=<%=manager_id%>&batch_task_type=<%=batch_task_type%>&op=update';
							url = EMPTools.encodeURI(url);
							window.location=url;
						}else {
							alert("删除失败!");
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
				var url="<emp:url action='delPspBatchTaskRel.do'/>?manager_id=<%=manager_id%>"+"&subTaskIdStr="+subTaskIdStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		}
	};
	function doViewSubPspCheckTask() {
		var num = PspBatchTaskList._obj.getSelectedData().length;
		if (num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if (num == 1) {
			var sub_task_id = PspBatchTaskList._obj.getParamValue(['sub_task_id']);
			var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?op=view&task_id='+sub_task_id+"&batch_task_flag=1";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('只能选择一条记录！');
		}
	};
	
	//子任务检查
	function doGetUpdatePspCheckTaskPage() {
		var num = PspBatchTaskList._obj.getSelectedData().length;
		if(num == 0){
			alert('请先选择一条记录！');
			return;
		}	
		if(num ==1){
			var sub_task_id = PspBatchTaskList._obj.getParamValue(['sub_task_id']);
			var url = '<emp:url action="getPspCheckTaskUpdatePage.do"/>?op=update&task_id='+sub_task_id+"&batch_task_flag=1";
		    url = EMPTools.encodeURI(url);
		    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('只能选择一条记录！');
		}
	};

	/*** 影像部分操作按钮begin ***/
	function doImageScan(){
		ImageAction('Scan26');	//贷后资料扫描
	};
	function doImageView(){
		ImageAction('View27');	//贷后资料查看
	};
	function ImageAction(image_action){
		var num = PspBatchTaskList._obj.getSelectedData().length;
		if(num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if(num ==1){
		var data = new Array();
			data['serno'] = PspBatchTaskList._obj.getParamValue(['sub_task_id']);	//业务编号
			data['cus_id'] = PspBatchTaskList._obj.getParamValue(['cus_id']);	//客户编号
			data['prd_id'] = 'POSTLOAN';	//业务品种
			data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
			doPubImageAction(data);
		}else{
			alert('只能选择一条记录!');
		}
	};
	/*** 影像部分操作按钮end ***/

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>	
	
	<emp:gridLayout id="PspBatchTaskGroup" title="批量任务贷款金额" maxColumn="3">
			<emp:text id="PspBatchTask.loan_sum_amt" label="批量任务贷款总金额" dataType="Currency" readonly="true" defvalue="${context.loan_sum_amt}" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="PspBatchTask.loan_sum_balance" label="批量任务贷款总余额" dataType="Currency" readonly="true" defvalue="${context.loan_sum_balance}" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="PspBatchTask.sum_qnt" label="贷款总笔数" readonly="true" defvalue="${context.sum_qnt}"/>
	</emp:gridLayout> 
	<br><br>
	<div align="left">
	<%if(!"view".equals(op)){ %>
		<emp:button id="addSubPspCheckTask" label="新增" />
		<emp:button id="delSubPspCheckTask" label="删除" />
		<emp:button id="getUpdatePspCheckTaskPage" label="检查" />
	<%}%>
	<emp:button id="viewSubPspCheckTask" label="查看" />
	<%if(flagSM.equals("1")){ %>
		<%-- <emp:button id="ImageScan" label="影像扫描"/> --%>
	<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
	<emp:table icollName="PspBatchTaskList" pageMode="true" url="getPspBatchTaskList.do" reqParams="major_task_id=${context.major_task_id}" selectType="2">
		    <emp:text id="sub_task_id" label="子任务编号" />
		    <emp:text id="cus_id" label="客户码"/>
		    <emp:text id="cus_id_displayname" label="客户名称" />
			<emp:text id="check_type" label="检查类型" dictname="STD_PSP_CHECK_TYPE" hidden="true"/>
			<emp:text id="qnt" label="笔数" />
			<emp:text id="loan_totl_amt" label="贷款总金额" dataType="Currency"/>
			<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
			<emp:text id="task_create_date" label="任务生成日期"/>
			<emp:text id="task_request_time" label="要求完成时间"/>
			<emp:text id="check_freq" label="检查频率" hidden="true"/>
	 </emp:table>
</body>
</html>
</emp:page>
    