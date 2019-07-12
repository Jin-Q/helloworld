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
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppJointCoop._toForm(form);
		LmtAppJointCoop_jointList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAppJointCoopGroup.reset();
	};
	
	/*--user code begin--*/
	function doView() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?'+paramStr+"&op=view&type=frozen";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doAdd(){
		var url = '<emp:url action="getLmtJointFrozenLeadPage.do"/>&op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
	function doDelete() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
	        if(_status!=''&&_status!= '000'){
				alert('该申请所处状态不是【待发起】不能删除');
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAppJointCoop_jointRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);				
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	//提交流程
	function doSubm(){
		var paramStr = LmtAppJointCoop_jointList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
			var cus_id = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id_displayname']);//客户名称
			var amt = LmtAppJointCoop_jointList._obj.getParamValue(['lmt_totl_amt']);//授信总额
			WfiJoin.table_name._setValue("LmtAppJointCoop");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3242");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("联保小组授信申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppJointCoopGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAppJointCoop.serno" label="业务编号" />
		<emp:text id="LmtAppJointCoop.cus_id" label="组长客户码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	<!-- <emp:button id="add" label="新增" op="add"/> -->
		<emp:button id="delete" label="删除" op="remove"/>
		<emp:button id="view" label="查看" op="view"/>
		<emp:button id="subm" label="提交" op="subm"/>
	</div>

	<emp:table icollName="LmtAppJointCoop_jointList" pageMode="true" url="pageLmtAppJointCoop_jointQuery.do" reqParams="process=${context.process}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="类别" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency" hidden="true"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    