<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String dutys = "";
	if(context.containsKey("dutys")){
		dutys = (String)context.getDataValue("dutys");
	} 
	String organNo = "";
	if(context.containsKey("organNo")){
		organNo = (String)context.getDataValue("organNo");
	}
	String flagHOrg = "";
	if(dutys.contains("S0240") || dutys.contains("S0241") || dutys.contains("D0029") || dutys.contains("S0002") || "9350500009".equals(organNo) || "9350500012".equals(organNo)){
		flagHOrg = "1";
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function selCusId(data){
		CusBlkCheckinapp.cus_id._setValue(data.cus_id._getValue());
		CusBlkCheckinapp.cus_name._setValue(data.cus_name._getValue());
		CusBlkCheckinapp.cert_type._setValue(data.cert_type._getValue());
		CusBlkCheckinapp.cert_code._setValue(data.cert_code._getValue());
	}
	/*--user code end--*/
		function doReturn() {
		var url = '<emp:url action="queryCusBlkCheckinappList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onReturnRegStateCode(date){
    	CusBlkCheckinapp.legal_addr._obj.element.value=date.id;
    	CusBlkCheckinapp.legal_addr_displayname._obj.element.value=date.label;
	}

	function getOrgID(data){
    	CusBlkCheckinapp.manager_br_id._setValue(data.organno._getValue());
    	CusBlkCheckinapp.manager_br_id_displayname._setValue(data.organname._getValue());
	}

    function setconId(data){
    	CusBlkCheckinapp.manager_id_displayname._setValue(data.actorname._getValue());
    	CusBlkCheckinapp.manager_id._setValue(data.actorno._getValue());
    	CusBlkCheckinapp.manager_br_id._setValue(data.orgid._getValue());
    	CusBlkCheckinapp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
    	//CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(true);	
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
					CusBlkCheckinapp.manager_br_id._setValue(jsonstr.org);
					CusBlkCheckinapp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusBlkCheckinapp.manager_br_id._setValue("");
					CusBlkCheckinapp.manager_br_id_displayname._setValue("");
					CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBlkCheckinapp.manager_id._getValue();
					CusBlkCheckinapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}else if("yteam"==flag){
   					CusBlkCheckinapp.manager_br_id._setValue("");
   					CusBlkCheckinapp.manager_br_id_displayname._setValue("");
   					CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(false);
   					CusBlkCheckinapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
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
   		var manager_id = CusBlkCheckinapp.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
    	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
   	}
   	
	function doUpdateCusBlkCheckinapp() {
		var form = document.getElementById("submitForm");
		var result = CusBlkCheckinapp._checkAll();
		if(result){
			CusBlkCheckinapp._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
	};
	
	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("修改成功！");
					doReturn();
			     }else if(flag == "fail"){
						//var options = CusBlkCheckinapp.black_level._obj.element.options;
						//var level = CusBlkCheckinapp.black_level._getValue();
						//var levelName ;
						//for ( var i = options.length - 1; i >= 0; i--) {
					   	//    if(options[i].value == level){
					   	//    	levelName = options[i].text;
					   	//    }
					    //}
						alert("此客户已进入共享客户管理,不能修改！");
					   return;
			     }else{
				     alert("修改失败！")
				 }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	//若不是‘待发起’状态，主管机构、主管客户经理不允许修改
	function doLoad(){
		var app_status = CusBlkCheckinapp.approve_status._getValue();
		if(app_status!=null && app_status!='000'){
			CusBlkCheckinapp.manager_id_displayname._obj._renderReadonly(true);
			CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(true);
		}else{
			var dutyList = '${context.dutyNoList}';
			if(dutyList.indexOf("D0005")>=0 || dutyList.indexOf("D0006")>=0){//授信管理部综合管理岗 或者 风险部经办岗
				CusBlkCheckinapp.manager_id_displayname._setValue('${context.currentUserName}');
				CusBlkCheckinapp.manager_br_id_displayname._setValue('${context.organName}');
				CusBlkCheckinapp.manager_id._setValue('${context.currentUserId}');
				CusBlkCheckinapp.manager_br_id._setValue('${context.organNo}');
				CusBlkCheckinapp.manager_id_displayname._obj._renderReadonly(true);
				CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(true);
			}
		}
		var flagHOrg = '<%=flagHOrg%>';
		if(flagHOrg != "1"){
			var options = CusBlkCheckinapp.black_type._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
				if(options[i].value == "14"){
					options.remove(i);
				}
			}
		}
	}
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateCusBlkCheckinappRecord.do" method="POST">
		<emp:gridLayout id="CusBlkCheckinappGroup" maxColumn="2" title="共享客户进入申请">
			<emp:text id="CusBlkCheckinapp.serno" label="业务流水号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="true"/>
			<emp:pop id="CusBlkCheckinapp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=setCurPageDatas" required="true" readonly="true" defvalue="" />
			<emp:text id="CusBlkCheckinapp.cus_id_displayname" label="客户名称"  required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusBlkCheckinapp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:select id="CusBlkCheckinapp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusBlkCheckinapp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkCheckinapp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkCheckinapp.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone"/>
			<emp:text id="CusBlkCheckinapp.legal_addr" label="通讯地址" required="true" hidden="true"/>
			<emp:pop id="CusBlkCheckinapp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" cssElementClass="emp_field_text_input2" required="true"/>	
			<emp:text id="CusBlkCheckinapp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:textarea id="CusBlkCheckinapp.black_reason" label="客户描述" maxlength="250" required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusBlkCheckinappGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkCheckinapp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusBlkCheckinapp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="CusBlkCheckinapp.manager_id" label="责任人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_id_displayname" label="登记人" required="true" defvalue="$currentUserId" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id_displayname" label="登记机构" required="true" defvalue="$organNo"  readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo"  readonly="true" hidden="true"/>
			<emp:date id="CusBlkCheckinapp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.approve_status" label="审批状态" hidden="true" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateCusBlkCheckinapp" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
