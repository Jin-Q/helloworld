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
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*XD140718027：公司客户和小微客户经营佐证信息的修改*/
	function doload(){
		var task_id = '${context.task_id}';
		this_task_id = '${context.task_id}';
		PspCheckAnaly.task_id._setValue(task_id);
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		PspPropertyAnaly._toForm(form);
		PspPropertyAnalyList._obj.ajaxQuery(null,form);
	};

	function doGetUpdatePspFeeDetailPage() {
		var paramStr = PspSitecheckComList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspSitecheckComList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspSitecheckComUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspSitecheckCom() {
		var paramStr = PspSitecheckComList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspSitecheckComViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspSitecheckComPage() {
		var task_id = '${context.task_id}';
		
		var url = '<emp:url action="getPspSitecheckComAddPage.do"/>?task_id='+task_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspSitecheckCom() {
		var paramStr = PspSitecheckComList._obj.getParamStr(['pk_id']);
		var task_id = PspSitecheckComList._obj.getParamValue(['task_id']);
//		var cus_id = PspSitecheckComList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspSitecheckComList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
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
							var url = '<emp:url action="queryPspPropertyAnalyListForVisit.do"/>?task_id='+task_id; 
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
				var url = '<emp:url action="deletePspSitecheckComRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doResetvalue(){
		PspCheckAnaly.visit_analy._obj.reset();
		
	};
	function doSaveanaly(){
		var form = document.getElementById("submitForm");
		if(PspCheckAnaly._checkAll()){
			PspCheckAnaly._toForm(form);
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
						alert("保存成功!");
					}else {
						alert("保存失败!");
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
			return false;
		}
	}

	function doGetUpdatePspSitecheckComPage() {
		var paramStr = PspSitecheckComList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspSitecheckComList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspSitecheckComUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doReturn() {
		var menuId = "${context.menuId}" ;
		if(menuId == 'expert_check_task_his' || menuId == 'period_check_task_his' || menuId == 'first_check_task_his'){
			url = '<emp:url action="queryPspCheckTaskHistoryList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}';
		}else{
			url = '<emp:url action="queryPspCheckTaskList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	/*--贷后管理系统改造（常规检查）    XD141222090  modefied by zhaoxp  2015-01-22--*/
	//查看担保品
	function doViewguar() {
		var paramStr = GrtMortList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看保证人信息
	function doViewee(){
		var paramStr = GrtGuaranteeList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+paramStr;
			url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doXccheck(){	
		var data = GrtMortList._obj.getSelectedData();
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = GrtMortList._obj.getSelectedData()[0].guaranty_no._getValue(); 
				var url = '<emp:url action="getPspSitecheckComAddPagedzy.do"/>?task_id='+task_id+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	

	}
	
	function doBzrxccheck(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		
		var param = GrtGuaranteeList._obj.getParamStr(['cus_name']);
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = GrtGuaranteeList._obj.getSelectedData()[0].cus_id._getValue();              
				var url = '<emp:url action="getPspSitecheckComAddPagedbqy.do"/>?'+param+'&task_id='+task_id+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	
	}

	function doXccheckview(){	
		var data = GrtMortList._obj.getSelectedData();
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = GrtMortList._obj.getSelectedData()[0].guaranty_no._getValue(); 
				var url = '<emp:url action="getPspSitecheckComAddPagedzyView.do"/>?task_id='+task_id+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	
	}

	function doBzrxccheckview(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		
		var param = GrtGuaranteeList._obj.getParamStr(['cus_name']);
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = GrtGuaranteeList._obj.getSelectedData()[0].cus_id._getValue();              
				var url = '<emp:url action="getPspSitecheckComAddPagedbqyView.do"/>?'+param+'&task_id='+task_id+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	
	}
	/*--贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2015-01-22--*/
</script>
</head>
<body class="page_content" onload="doload()">
	<div class="emp_gridLayout_title">现场检查 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspSitecheckComPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspSitecheckComPage" label="修改" op="update"/>
		<emp:button id="deletePspSitecheckCom" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspSitecheckCom" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspSitecheckComList" pageMode="false" url="pagePspSitecheckComQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true" />
		<emp:text id="task_id" label="任务号"  hidden="true"/>
		<emp:text id="check_time" label="检查时间" />
		<emp:text id="check_addr" label="检查地点" />
		<emp:text id="yjry" label="约见人员" />
		<emp:text id="visit_type" label="现场检查类型" dictname="STD_ZB_VISIT_TYPE"/>
	</emp:table>
	
			<div class='emp_gridlayout_title'>抵（质）押品检查</div>
	<emp:button id="viewguar" label="查看担保品" op="view"/>
	<%if(!op.equals("view")){ %>
	<emp:button id="xccheck" label="现场检查" op="view"/>
	<%} %>
	<%if(op.equals("view")){ %>
	<emp:button id="xccheckview" label="现场检查查看" op="view"/>
	<%} %>
	<emp:table icollName="GrtMortList" pageMode="false" url="">
		<emp:text id="guaranty_no" label="担保品编号" />
		<emp:text id="guaranty_name" label="担保品名称" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE"/>
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="cus_id_displayname" label="出质人客户名称" />
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		
	</emp:table>
	
	<div class='emp_gridlayout_title'>担保企业检查</div>
	<emp:button id="viewee" label="查看保证人" op="view"/>
	<%if(!op.equals("view")){ %>
	<emp:button id="bzrxccheck" label="现场检查" op="view"/>
	<%} %>
	<%if(op.equals("view")){ %>
	<emp:button id="bzrxccheckview" label="现场检查查看" op="view"/>
	<%} %>
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="">
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_name" label="保证人" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
		
	</emp:table>
	
	<emp:gridLayout id="PspGroup" title="现场检查结论" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.visit_analy" label="现场检查结论：" maxlength="2000" required="true" colSpan="2" />
	<emp:text id="PspCheckAnaly.task_id" label="任务编号"  required="false" hidden="true"/>
	</emp:gridLayout>
	<br>
	<br>
	<emp:form id="submitForm" action="updatePspCheckAnalyRecord.do" method="POST">
	<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="saveanaly" label="确定" />
			<emp:button id="resetvalue" label="重置"/>
<!--			<emp:button id="return" label="返回列表"/>-->
			<%} %>
	</div>
	</emp:form>
</body>
</html>
</emp:page>