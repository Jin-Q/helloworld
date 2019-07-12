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
		ArpBondReducApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpBondReducApp.manager_id._setValue(data.actorno._getValue());
		ArpBondReducApp.manager_br_id._setValue(data.orgid._getValue());
		//ArpBondReducApp.manager_br_id_displayname._obj._renderReadonly(true);
		ArpBondReducApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
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
				var flag = jsonstr.flag;
				var org = jsonstr.org;
				if("one" == flag){//客户经理只属于一个机构
					ArpBondReducApp.manager_br_id._setValue(jsonstr.org);
					ArpBondReducApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					ArpBondReducApp.manager_br_id._setValue("");
					ArpBondReducApp.manager_br_id_displayname._setValue("");
					ArpBondReducApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpBondReducApp.manager_id._getValue();
					ArpBondReducApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){//客户经理为团队成员时
					ArpBondReducApp.manager_br_id._setValue("");
					ArpBondReducApp.manager_br_id_displayname._setValue("");
					ArpBondReducApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpBondReducApp.manager_id._getValue();
					ArpBondReducApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpBondReducApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		ArpBondReducApp.manager_br_id._setValue(data.organno._getValue());
		ArpBondReducApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	
	/** 选择客户码并校验begin **/
	function returnCus(data){
		cus_id = data.cus_id._getValue();
		cus_name = data.cus_name._getValue();
		
		var url="<emp:url action='checkAssetPreserve.do'/>&type=CusidBondReduc&value="+cus_id;
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpBondReducApp.cus_id._setValue(cus_id);
			ArpBondReducApp.cus_id_displayname._setValue(cus_name);
		}else{
			alert("此客户已有在途的债权减免申请!");
		}
	};
	/** 选择客户码并校验end **/
	
	function doReturn() {
		var url = '<emp:url action="queryArpBondReducAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){
		url = '<emp:url action="getArpBondReducAppUpdatePage.do"/>?restrictUsed=false&op=update&serno=';
		doPubAdd(url,ArpBondReducApp);
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addArpBondReducAppRecord.do" method="POST">
		<emp:gridLayout id="ArpBondReducAppGroup" title="债权减免申请" maxColumn="2">
			<emp:text id="ArpBondReducApp.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:select id="ArpBondReducApp.reduc_type" label="减免类型" required="true" dictname="STD_ZB_REDUC_TYPE" />
			<emp:pop id="ArpBondReducApp.cus_id" label="客户码" required="true"  colSpan="2"
			url="queryAllCusPop.do?flag=1&returnMethod=returnCus" />
			<emp:text id="ArpBondReducApp.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpBondReducApp.reduc_qnt" label="减免笔数" maxlength="38" required="true" 
			dataType="Int" defvalue="0" readonly="true" colSpan="2"/>
			<emp:text id="ArpBondReducApp.reduc_cap_sum" label="债权减免本金金额" maxlength="16" 
			required="true" dataType="Currency" defvalue="0.00" readonly="true"/>
			<emp:text id="ArpBondReducApp.reduc_int_sum" label="债权减免利息金额" maxlength="16" 
			required="true" dataType="Currency" defvalue="0.00" readonly="true"/>
			<emp:text id="ArpBondReducApp.reduc_accord_file" label="减免依据文件" maxlength="80" required="true" 
			colSpan="2" cssElementClass="emp_field_text_cusname" />
			<emp:textarea id="ArpBondReducApp.bond_reduc_reason" label="债权减免原因" maxlength="250" required="true" colSpan="2" />
			<emp:textarea id="ArpBondReducApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpBondReducApp.app_date" label="申请日期" required="false" defvalue="$OPENDAY" hidden="true" />
			<emp:date id="ArpBondReducApp.over_date" label="办结日期" required="false" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpBondReducAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpBondReducApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpBondReducApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpBondReducApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpBondReducApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpBondReducApp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpBondReducApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpBondReducApp.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpBondReducApp.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpBondReducApp.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
			<emp:select id="ArpBondReducApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"  defvalue="000" readonly="true" />
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