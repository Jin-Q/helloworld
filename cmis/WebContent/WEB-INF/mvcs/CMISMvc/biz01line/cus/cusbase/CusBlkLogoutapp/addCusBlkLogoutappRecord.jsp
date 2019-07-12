<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryCusBlkLogoutappList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	/*--user code end--*/
	function doOnload(){
		CusBlkLogoutapp.cus_id._obj.config.url=CusBlkLogoutapp.cus_id._obj.config.url+"&returnMethod=retCusBlk"

		var dutyList = '${context.dutyNoList}';
		if(dutyList.indexOf("D0005")>=0 || dutyList.indexOf("D0006")>=0){//授信管理部综合管理岗 或者 风险部经办岗
			CusBlkLogoutapp.manager_id_displayname._setValue('${context.currentUserName}');
			CusBlkLogoutapp.manager_br_id_displayname._setValue('${context.organName}');
			CusBlkLogoutapp.manager_id._setValue('${context.currentUserId}');
			CusBlkLogoutapp.manager_br_id._setValue('${context.organNo}');
			CusBlkLogoutapp.manager_id_displayname._obj._renderReadonly(true);
			CusBlkLogoutapp.manager_br_id_displayname._obj._renderReadonly(true);
		}
	}
	function retCusBlk(data){
	//	CusBlkLogoutapp.old_serno._setValue(data.serno._getValue());
		CusBlkLogoutapp.black_date._setValue(data.black_date._getValue());
		CusBlkLogoutapp.cus_id._setValue(data.cus_id._getValue());
		CusBlkLogoutapp.cus_name._setValue(data.cus_name._getValue());
		CusBlkLogoutapp.cert_type._setValue(data.cert_type._getValue());
		CusBlkLogoutapp.cert_code._setValue(data.cert_code._getValue());
		CusBlkLogoutapp.black_type._setValue(data.black_type._getValue());
		CusBlkLogoutapp.black_level._setValue(data.black_level._getValue());
		CusBlkLogoutapp.legal_name._setValue(data.legal_name._getValue());
		CusBlkLogoutapp.legal_phone._setValue(data.legal_phone._getValue());
		CusBlkLogoutapp.legal_addr._setValue(data.legal_addr._getValue());
		CusBlkLogoutapp.black_reason._setValue(data.black_reason._getValue());
		CusBlkLogoutapp.legal_addr_displayname._setValue(data.legal_addr_displayname._getValue());
		CusBlkLogoutapp.street._setValue(data.street._getValue());
		checkCusBlk4Logout();
	}

	function checkCusBlk4Logout(){
		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "success" ){
				alert("该客户已经存在一笔申请信息！");
		//		CusBlkLogoutapp.old_serno._setValue("");
				CusBlkLogoutapp.black_date._setValue("");
				CusBlkLogoutapp.cus_id._setValue("");
				CusBlkLogoutapp.cus_name._setValue("");
				CusBlkLogoutapp.cert_type._setValue("");
				CusBlkLogoutapp.cert_code._setValue("");
				CusBlkLogoutapp.black_type._setValue("");
				CusBlkLogoutapp.black_level._setValue("");
				CusBlkLogoutapp.legal_name._setValue("");
				CusBlkLogoutapp.legal_phone._setValue("");
				CusBlkLogoutapp.legal_addr._setValue("");
				CusBlkLogoutapp.black_reason._setValue("");
				CusBlkLogoutapp.legal_addr_displayname._setValue("");
				CusBlkLogoutapp.street._setValue("");
			}
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var cus_id = CusBlkLogoutapp.cus_id._getValue();
		var url = '<emp:url action="checkCusBlk4Logout.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	}

	function getOrgID(data){
		CusBlkLogoutapp.manager_br_id._setValue(data.organno._getValue());
		CusBlkLogoutapp.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function setconId(data){
		CusBlkLogoutapp.manager_id_displayname._setValue(data.actorname._getValue());
		CusBlkLogoutapp.manager_id._setValue(data.actorno._getValue());
		CusBlkLogoutapp.manager_br_id._setValue(data.orgid._getValue());
		CusBlkLogoutapp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusBlkLogoutapp.manager_br_id_displayname._obj._renderReadonly(true);
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
					CusBlkLogoutapp.manager_br_id._setValue(jsonstr.org);
					CusBlkLogoutapp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusBlkLogoutapp.manager_br_id._setValue("");
					CusBlkLogoutapp.manager_br_id_displayname._setValue("");
					CusBlkLogoutapp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBlkLogoutapp.manager_id._getValue();
					CusBlkLogoutapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusBlkLogoutapp.manager_br_id._setValue("");
					CusBlkLogoutapp.manager_br_id_displayname._setValue("");
					CusBlkLogoutapp.manager_br_id_displayname._obj._renderReadonly(false);
					CusBlkLogoutapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
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
		var manager_id = CusBlkLogoutapp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function doAddCusBlkLogoutapp(){
		var form = document.getElementById("submitForm");
		if(CusBlkLogoutapp._checkAll()){
			CusBlkLogoutapp._toForm(form);
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
						alert("保存成功！");
						var url = '<emp:url action="queryCusBlkLogoutappList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("保存失败！");
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
			var serno = CusBlkLogoutapp.serno._getValue();
			var url = '<emp:url action="addCusBlkLogoutappRecord.do"/>?serno='+serno;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	}

	function onReturnRegStateCode(date){
    	CusBlkLogoutapp.legal_addr._obj.element.value=date.id;
    	CusBlkLogoutapp.legal_addr_displayname._obj.element.value=date.label;
	}
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="addCusBlkLogoutappRecord.do" method="POST">
		<emp:gridLayout id="CusBlkLogoutappGroup" title="共享客户注销申请" maxColumn="2">
			<emp:text id="CusBlkLogoutapp.serno" label="业务流水号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="true"/>
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="CusBlkLogoutapp.cus_id" label="客户码" required="true" url="queryCusBlkListPopList.do?cusTypCondition=manager_id = '${context.currentUserId}'" colSpan="2" reqParams="CusBlkList.status=002"/>
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CusBlkLogoutapp.cus_name" label="客户名称" maxlength="60" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:date id="CusBlkLogoutapp.black_date" label="列入日期" required="true" readonly="true"/>
			<emp:select id="CusBlkLogoutapp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" readonly="true" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="CusBlkLogoutapp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="CusBlkLogoutapp.cert_code" label="证件号码" maxlength="20" required="true" onblur="checkCertCode();" readonly="true"/>
			<emp:select id="CusBlkLogoutapp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" readonly="true"/>
			<emp:text id="CusBlkLogoutapp.legal_name" label="法定代表人" maxlength="30" required="false" readonly="true"/>
			<emp:text id="CusBlkLogoutapp.legal_phone" label="联系电话" maxlength="35" required="false" readonly="true"/>
			<emp:text id="CusBlkLogoutapp.legal_addr" label="通讯地址" required="false" hidden="true"/>
			<emp:pop id="CusBlkLogoutapp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"  readonly="true"/>	
			<emp:text id="CusBlkLogoutapp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"  readonly="true"/>
			<emp:textarea id="CusBlkLogoutapp.black_reason" label="客户描述" maxlength="250" colSpan="2" readonly="true"/>
			<emp:textarea id="CusBlkLogoutapp.logout_reason" label="注销原因" maxlength="250" required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
			<emp:select id="CusBlkLogoutapp.approve_status" label="审批状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusBlkLogoutappGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkLogoutapp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusBlkLogoutapp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="CusBlkLogoutapp.manager_id" label="责任人" hidden="true"/>
			<emp:text id="CusBlkLogoutapp.manager_br_id" label="管理机构" hidden="true" required="true"/>
			<emp:text id="CusBlkLogoutapp.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="CusBlkLogoutapp.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:text id="CusBlkLogoutapp.input_id" label="登记人" maxlength="20" required="true" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBlkLogoutapp.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="CusBlkLogoutapp.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusBlkLogoutapp" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>