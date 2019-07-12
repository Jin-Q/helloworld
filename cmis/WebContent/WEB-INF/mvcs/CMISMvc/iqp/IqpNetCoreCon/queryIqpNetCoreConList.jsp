<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpNetCoreCon._toForm(form);
		IqpNetCoreConList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpNetCoreConPage() {
		var paramStr = IqpNetCoreConList._obj.getParamStr(['net_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpNetCoreCon() {
		var paramStr = IqpNetCoreConList._obj.getParamStr(['net_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpNetCoreConViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpNetCoreConPage() {
		var url = '<emp:url action="getIqpNetCoreConAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpNetCoreCon(){
		var paramStr = IqpNetCoreConList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var _status = IqpNetCoreConList._obj.getParamValue(['approve_status']);
	        if(_status!= '000'){
				alert('该申请所处状态不是【待发起】不能删除！');
				return;
			}
			var url = '<emp:url action="deleteIqpNetCoreConRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			if(!confirm("确认删除?") )return ;
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
				  try {
					var jsonstr = eval("("+o.responseText+")");
				  } catch(e) {
					alert("数据库操作失败!");
					return;
				  }
				  var flag = jsonstr.flag;
				  if(flag == 'success'){
					 alert("删除成功！");
					 window.location.reload();
					}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpNetCoreConGroup.reset();
	};
	
	/*--user code begin--*/
	function doCheckSub(){
		var paramStr = IqpNetCoreConList._obj.getParamStr(['net_agr_no']);
		if (paramStr != null){
		var handleSuccess = function(o){ 		
            var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
				if(flag == "success" ){
					alert("该网络中不存在待入网和待退网成员！")
				}else{
					doIqpNetMagInfo();//提交流程
				}
			}
				var url = '<emp:url action="checkMemIsInNet.do"/>?'+paramStr;
				var callback = {
				success:handleSuccess,
				failure:null
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		}else{
			alert('请先选择一条记录！');
		}
	}
	//提交流程
	function doIqpNetMagInfo(){
		var paramStr = IqpNetCoreConList._obj.getParamValue(['serno']);
		var approve_status = IqpNetCoreConList._obj.getParamValue(['approve_status']);
		var cus_id = IqpNetCoreConList._obj.getParamValue(['cus_id']);
		var cus_name = IqpNetCoreConList._obj.getParamValue(['cus_id_displayname']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("IqpNetCoreCon");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("513");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_name);//客户名称
			WfiJoin.prd_name._setValue("网络管理入网/退网申请");//产品名称
			initWFSubmit(false);
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="IqpNetCoreConGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpNetCoreCon.cus_id" label="客户码" />
			<emp:text id="IqpNetCoreCon.cus_id_displayname" label="客户名称" />
			<emp:select id="IqpNetCoreCon.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		</emp:gridLayout>
	</form>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpNetCoreConPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpNetCoreConPage" label="修改" op="update"/>
		<emp:button id="deleteIqpNetCoreCon" label="删除" op="remove"/>
		<emp:button id="viewIqpNetCoreCon" label="查看" op="view"/>
		<emp:button id="checkSub" label="提交" op="view"/>
	</div>

	<emp:table icollName="IqpNetCoreConList" pageMode="false" url="pageIqpNetCoreConQuery.do">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="net_agr_no" label="网络编号" />
		<emp:select id="app_flag" label="申请标识" dictname="STD_ZB_NET_APP_TYPE"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true" />
		<emp:text id="input_id" label="登记人" hidden="true" />
		<emp:text id="input_br_id" label="登记机构" hidden="true" />
		<emp:text id="input_date" label="登记日期" />
		<emp:select id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    