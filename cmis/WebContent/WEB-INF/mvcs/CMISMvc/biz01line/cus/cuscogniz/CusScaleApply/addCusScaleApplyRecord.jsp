<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
	<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusScaleApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function returnCus(data){
		CusScaleApply.cus_id._setValue(data.cus_id._getValue());
		CusScaleApply.cus_name._setValue(data.cus_name._getValue());
		checkCusid(data.cus_id._getValue());
		var belg_line = data.belg_line._getValue();
		//原业务条线为对公则只能认定为小微，反之亦然
		if(belg_line=='BL100'){
			CusScaleApply.scale_type._setValue('01');
			CusScaleApply.scale_type._obj._renderReadonly(true);
		}else {
			CusScaleApply.scale_type._setValue('02');
			CusScaleApply.scale_type._obj._renderReadonly(true);
		}
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		CusScaleApply.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		CusScaleApply.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		CusScaleApply.manager_id._setValue(data.cust_mgr._getValue());
		CusScaleApply.manager_br_id._setValue(data.main_br_id._getValue());
		doChangeOrgUrl();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
	function doChangeOrgUrl(){
		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("belg2team" == flag){//客户经理只属于团队
					var manager_id = CusScaleApply.manager_id._getValue();
					CusScaleApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};

   		var manager_id = CusScaleApply.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	function checkCusid(cus_id){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "false" ){
				CusScaleApply.cus_id._setValue("");
				CusScaleApply.cus_name._setValue("");
				alert("该客户已经存在于申请中，请重新选择：");	
			}
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="checkCusidApplyed.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};
	//新增
	function doAddCusScaleApp(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
		            alert('新增成功!');
		            doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = CusScaleApply._checkAll();
		if(result){
			CusScaleApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	}
	function getOrgID(data){
		CusScaleApply.manager_br_id._setValue(data.organno._getValue());
		CusScaleApply.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function setconId(data){
		CusScaleApply.manager_id_displayname._setValue(data.actorname._getValue());
		CusScaleApply.manager_id._setValue(data.actorno._getValue());
		CusScaleApply.manager_br_id._setValue(data.orgid._getValue());
		CusScaleApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusScaleApply.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusScaleApply.manager_br_id._setValue(jsonstr.org);
					CusScaleApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusScaleApply.manager_br_id._setValue("");
					CusScaleApply.manager_br_id_displayname._setValue("");
					CusScaleApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusScaleApply.manager_id._getValue();
					CusScaleApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusScaleApply.manager_br_id._setValue("");
					CusScaleApply.manager_br_id_displayname._setValue("");
					CusScaleApply.manager_br_id_displayname._obj._renderReadonly(false);
					CusScaleApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusScaleApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusScaleApplyRecord.do" method="POST">
		
		<emp:gridLayout id="CusScaleApplyGroup" title="条线认定申请" maxColumn="2">
			<emp:text id="CusScaleApply.serno" label="申请流水号" maxlength="40" colSpan="2" readonly="true" hidden="true" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="CusScaleApply.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20' and cust_mgr = '${context.currentUserId}'&returnMethod=returnCus" required="true" colSpan="2" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CusScaleApply.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusScaleApply.scale_type" label="认定类别" required="true" dictname="STD_ZB_COGNIZ_TYPE"  />
			<emp:textarea id="CusScaleApply.scale_detail" label="认定说明" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)" />
		</emp:gridLayout>
		<emp:gridLayout id="CusScaleApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusScaleApply.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusScaleApply.manager_br_id_displayname" label="管理机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusScaleApply.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusScaleApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusScaleApply.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusScaleApply.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusScaleApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>			
			<emp:text id="CusScaleApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusScaleApply.manager_id" label="责任人" hidden="true"/>
			<emp:text id="CusScaleApply.manager_br_id" label="管理机构" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusScaleApp" label="保存" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

