<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	
	/** 登记信息begin **/
	function setconId(data){
		ArpDbtCongnizApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpDbtCongnizApp.manager_id._setValue(data.actorno._getValue());
		ArpDbtCongnizApp.manager_br_id._setValue(data.orgid._getValue());
		ArpDbtCongnizApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//ArpDbtCongnizApp.manager_br_id_displayname._obj._renderReadonly(true);
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
				if("one" == flag){//客户经理只属于一个机构
					ArpDbtCongnizApp.manager_br_id._setValue(jsonstr.org);
					ArpDbtCongnizApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					ArpDbtCongnizApp.manager_br_id._setValue("");
					ArpDbtCongnizApp.manager_br_id_displayname._setValue("");
					ArpDbtCongnizApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpDbtCongnizApp.manager_id._getValue();
					ArpDbtCongnizApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpDbtCongnizApp.manager_br_id._setValue("");
					ArpDbtCongnizApp.manager_br_id_displayname._setValue("");
					ArpDbtCongnizApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpDbtCongnizApp.manager_id._getValue();
					ArpDbtCongnizApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpDbtCongnizApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		ArpDbtCongnizApp.manager_br_id._setValue(data.organno._getValue());
		ArpDbtCongnizApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	
	function doReturn() {
		var url = '<emp:url action="queryArpDbtCongnizAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){
		doPubUpdate(ArpDbtCongnizApp);
	};

	function reLoad(){
		var url = '<emp:url action="getArpDbtCongnizAppUpdatePage.do"/>?serno=${context.ArpDbtCongnizApp.serno}&op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doLoad(){
		document.getElementById("main_tabs").href="javascript:reLoad();";
		addCusForm(ArpDbtCongnizApp);
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="呆账认定申请" id="main_tabs">
	<emp:form id="submitForm" action="updateArpDbtCongnizAppRecord.do" method="POST">
		<emp:gridLayout id="ArpDbtCongnizAppGroup" title="呆账认定申请" maxColumn="2">
			<emp:text id="ArpDbtCongnizApp.serno" label="业务编号" maxlength="40" readonly="true" />
			<emp:text id="ArpDbtCongnizApp.cus_id" label="客户码" required="true"  colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtCongnizApp.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpDbtCongnizApp.qnt" label="笔数" maxlength="38" required="true" dataType="Int" readonly="true" />
			<emp:text id="ArpDbtCongnizApp.loan_balance" label="总贷款余额" maxlength="16" required="true" 
			dataType="Currency" readonly="true" />
			<emp:textarea id="ArpDbtCongnizApp.congniz_resn" label="认定理由" maxlength="250" required="true" colSpan="2" />
			<emp:textarea id="ArpDbtCongnizApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtCongnizApp.app_date" label="申请日期" required="false"  hidden="true"/>
			<emp:date id="ArpDbtCongnizApp.over_date" label="办结日期" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpDbtCongnizAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpDbtCongnizApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpDbtCongnizApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpDbtCongnizApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpDbtCongnizApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpDbtCongnizApp.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="ArpDbtCongnizApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpDbtCongnizApp.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpDbtCongnizApp.input_br_id" label="登记机构" required="true"  hidden="true" />
			<emp:date id="ArpDbtCongnizApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpDbtCongnizApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"   readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
