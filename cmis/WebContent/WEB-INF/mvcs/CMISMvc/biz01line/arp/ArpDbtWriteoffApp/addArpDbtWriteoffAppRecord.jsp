<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	
	/** 登记信息begin **/
	function setconId(data){
		ArpDbtWriteoffApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpDbtWriteoffApp.manager_id._setValue(data.actorno._getValue());
		ArpDbtWriteoffApp.manager_br_id._setValue(data.orgid._getValue());
		ArpDbtWriteoffApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(true);
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
					ArpDbtWriteoffApp.manager_br_id._setValue(jsonstr.org);
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					ArpDbtWriteoffApp.manager_br_id._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpDbtWriteoffApp.manager_id._getValue();
					ArpDbtWriteoffApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpDbtWriteoffApp.manager_br_id._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(false);
					ArpDbtWriteoffApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
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
		var manager_id = ArpDbtWriteoffApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		ArpDbtWriteoffApp.manager_br_id._setValue(data.organno._getValue());
		ArpDbtWriteoffApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	
	/** 选择客户码并校验begin **/
	function returnCus(data){
		cus_id = data.cus_id._getValue();
		cus_name = data.cus_name._getValue();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		ArpDbtWriteoffApp.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		ArpDbtWriteoffApp.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		ArpDbtWriteoffApp.manager_id._setValue(data.cust_mgr._getValue());
		ArpDbtWriteoffApp.manager_br_id._setValue(data.main_br_id._getValue());
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
		var url="<emp:url action='checkAssetPreserve.do'/>&type=CusidDbtWriteoff&value="+cus_id;
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpDbtWriteoffApp.cus_id._setValue(cus_id);
			ArpDbtWriteoffApp.cus_id_displayname._setValue(cus_name);
		}else{
			alert("此客户已有在途的呆账核销!");
		}
	};
	/** 选择客户码并校验end **/
	
	function doSubmits(){
		url = '<emp:url action="getArpDbtWriteoffAppUpdatePage.do"/>?restrictUsed=false&op=update&serno=';
		doPubAdd(url,ArpDbtWriteoffApp);
	};

	function doReturn() {
		var url = '<emp:url action="queryArpDbtWriteoffAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addArpDbtWriteoffAppRecord.do" method="POST">
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="呆账核销申请" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:pop id="ArpDbtWriteoffApp.cus_id" label="客户码" required="true"  colSpan="2"
			url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cust_mgr='${context.currentUserId}'&returnMethod=returnCus" />
			<emp:text id="ArpDbtWriteoffApp.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_qnt" label="核销笔数" maxlength="38" 
			required="true" dataType="Int" defvalue="0" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.loan_balance" label="贷款余额" maxlength="16" 
			required="true" dataType="Currency" defvalue="0.00" readonly="true"/>			
			<emp:textarea id="ArpDbtWriteoffApp.writeoff_resn" label="核销理由" maxlength="250" required="true" colSpan="2" />
			<emp:select id="ArpDbtWriteoffApp.whether_appx_appeal" label="是否保留追诉权" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpDbtWriteoffApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtWriteoffApp.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:date id="ArpDbtWriteoffApp.over_date" label="办结日期" required="false" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpDbtWriteoffApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpDbtWriteoffApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpDbtWriteoffApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpDbtWriteoffApp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpDbtWriteoffApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpDbtWriteoffApp.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpDbtWriteoffApp.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
			<emp:select id="ArpDbtWriteoffApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"  defvalue="000" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>