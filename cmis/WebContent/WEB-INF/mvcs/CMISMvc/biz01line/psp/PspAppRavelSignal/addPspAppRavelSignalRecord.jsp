<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//返回主管客户经理	
	function setconId(data){
		PspAppRavelSignal.manager_id._setValue(data.actorno._getValue());
		PspAppRavelSignal.manager_id_displayname._setValue(data.actorname._getValue());
		PspAppRavelSignal.manager_br_id._setValue(data.orgid._getValue());
		PspAppRavelSignal.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(true);
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
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					PspAppRavelSignal.manager_br_id._setValue(jsonstr.org);
					PspAppRavelSignal.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					PspAppRavelSignal.manager_br_id._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspAppRavelSignal.manager_id._getValue();
					PspAppRavelSignal.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspAppRavelSignal.manager_br_id._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(false);
					PspAppRavelSignal.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspAppRavelSignal.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		PspAppRavelSignal.manager_br_id._setValue(data.organno._getValue());
		PspAppRavelSignal.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspAppRavelSignal._checkAll()){
			PspAppRavelSignal._toForm(form); 
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
						var serno = jsonstr.serno;
						alert("新增成功!");
						var url = '<emp:url action="getPspAppRavelSignalUpdatePage.do"/>?serno='+serno; 
						url = EMPTools.encodeURI(url);
						window.location = url; 
					}else {
						alert("新增异常!"); 
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
		}
	};

	//选择客户POP框返回方法
	function returnCus(data){
		PspAppRavelSignal.cus_id._setValue(data.cus_id._getValue());
		cus_id_displayname._setValue(data.cus_name._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPspAppRavelSignalRecord.do" method="POST">
		
		<emp:gridLayout id="PspAppRavelSignalGroup" title="预警信号解除申请" maxColumn="2">
			<emp:pop id="PspAppRavelSignal.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_id in (select cus_id from psp_alt_signal where signal_status='1')&returnMethod=returnCus" required="true"/>
			<emp:text id="cus_id_displayname" label="客户名称" readonly="true" required="false" />
			<emp:select id="PspAppRavelSignal.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
			<emp:textarea id="PspAppRavelSignal.memo" label="解除原因" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspAppRavelSignal.approve_status" label="申请类型" maxlength="3" required="false" defvalue="000" hidden="true"/>
			<emp:text id="PspAppRavelSignal.serno" label="业务编号" maxlength="32" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspAppRavelSignalGroup" title="登记信息" maxColumn="2">
			<emp:pop id="PspAppRavelSignal.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="PspAppRavelSignal.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="PspAppRavelSignal.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspAppRavelSignal.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspAppRavelSignal.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			
			<emp:text id="PspAppRavelSignal.manager_id" label="责任人" maxlength="20" hidden="true" />
			<emp:text id="PspAppRavelSignal.manager_br_id" label="责任机构" maxlength="20" hidden="true" />
			<emp:text id="PspAppRavelSignal.input_id" label="登记人" maxlength="20" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspAppRavelSignal.input_br_id" label="登记机构" maxlength="20" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

