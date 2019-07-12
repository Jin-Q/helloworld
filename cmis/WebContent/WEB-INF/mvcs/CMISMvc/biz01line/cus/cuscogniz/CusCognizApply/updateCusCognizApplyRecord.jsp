<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusCognizApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function getOrgID(data){
		CusCognizApply.manager_br_id._setValue(data.organno._getValue());
		CusCognizApply.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function setconId(data){
		CusCognizApply.manager_id_displayname._setValue(data.actorname._getValue());
		CusCognizApply.manager_id._setValue(data.actorno._getValue());
		CusCognizApply.manager_br_id._setValue(data.orgid._getValue());
		CusCognizApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusCognizApply.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

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
					CusCognizApply.manager_br_id._setValue(jsonstr.org);
					CusCognizApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusCognizApply.manager_br_id._setValue("");
					CusCognizApply.manager_br_id_displayname._setValue("");
					CusCognizApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusCognizApply.manager_id._getValue();
					CusCognizApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusCognizApply.manager_br_id._setValue("");
					CusCognizApply.manager_br_id_displayname._setValue("");
					CusCognizApply.manager_br_id_displayname._obj._renderReadonly(false);
					CusCognizApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusCognizApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//修改
	function doUpdCusCognizApp(){
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
		            alert('保存成功!');
		            doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = CusCognizApply._checkAll();
		if(result){
			CusCognizApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	/*** 查看客户信息begin ***/
	function doOnLoad(){
		CusCognizApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	};
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusCognizApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*** 查看客户信息end ***/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="基本信息" id="main_tabs">
	<emp:form id="submitForm" action="updateCusCognizApplyRecord.do" method="POST">
		<emp:gridLayout id="CusCognizApplyGroup" maxColumn="2" title="条线认定申请">
			<emp:text id="CusCognizApply.serno" label="申请流水号" maxlength="40" required="true" colSpan="2" readonly="true" />
			<emp:text id="CusCognizApply.cus_id" label="客户码"  readonly="true" required="true"/>
			<emp:text id="CusCognizApply.cus_id_displayname" label="客户名称" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusCognizApply.scale_type" label="认定类别" required="true" dictname="STD_ZB_CUS_COGNIZ_TYPE"  />
			<emp:textarea id="CusCognizApply.scale_detail" label="认定说明" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)" />
		</emp:gridLayout>
		<emp:gridLayout id="CusCognizApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusCognizApply.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusCognizApply.manager_br_id_displayname" label="管理机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusCognizApply.input_id_displayname" label="登记人"  readonly="true" required="true"  defvalue="$currentUserId" />
			<emp:text id="CusCognizApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organNo" />
			<emp:text id="CusCognizApply.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusCognizApply.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusCognizApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>
			<emp:text id="CusCognizApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusCognizApply.manager_id" label="责任人" hidden="true"/>
			<emp:text id="CusCognizApply.manager_br_id" label="管理机构" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updCusCognizApp" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >
</body>
</html>
</emp:page>
