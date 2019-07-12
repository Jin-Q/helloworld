<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<emp:page>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String source = (String)context.getDataValue("CusBlkListTemp.source");
System.out.println(source);
%>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	function doOnload(){
		//去掉数据来源中，系统自动生成的。
		var options = CusBlkListTemp.data_source._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '11'){
				options.remove(i);
			}
		}
		changeFromType();
		//
		<%if("1".equals(source)){%>
		CusBlkListTemp.cert_type._obj._renderReadonly(true);
		CusBlkListTemp.cert_code._obj._renderReadonly(true);
		<%}else{%>
		CusBlkListTemp.cert_type._obj._renderReadonly(false);
		CusBlkListTemp.cert_code._obj._renderReadonly(false);
		<%}%>
	}
	function doReturn() {
		var url = '<emp:url action="queryCusBlkListTempList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doUpdateCusBlkList(){
		var form = document.getElementById("submitForm");
		var result = CusBlkListTemp._checkAll();
		if(result){
			CusBlkListTemp._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
	}

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
			/*     }else if(flag=="fail"){
				   alert("该客户已存在,修改失败！");
				   return;*/
			     }else{
				     alert("修改失败！");
				 }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
	//	var url= '<emp:url action="updateCusBlkListRecord.do"/>';
	//	url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};
	
	function returnCus(data){
		CusBlkListTemp.cus_id._setValue(data.cus_id._getValue());
		CusBlkListTemp.cus_name._setValue(data.cus_name._getValue());
		CusBlkListTemp.cert_type._setValue(data.cert_type._getValue());
		CusBlkListTemp.cert_code._setValue(data.cert_code._getValue());
	}

	function setconId(data){
		CusBlkListTemp.manager_id_displayname._setValue(data.actorname._getValue());
		CusBlkListTemp.manager_id._setValue(data.actorno._getValue());
		CusBlkListTemp.manager_br_id._setValue(data.orgid._getValue());
		CusBlkListTemp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(true);
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
					CusBlkListTemp.manager_br_id._setValue(jsonstr.org);
					CusBlkListTemp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusBlkListTemp.manager_br_id._setValue("");
					CusBlkListTemp.manager_br_id_displayname._setValue("");
					CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBlkListTemp.manager_id._getValue();
					CusBlkListTemp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusBlkListTemp.manager_br_id._setValue("");
					CusBlkListTemp.manager_br_id_displayname._setValue("");
					CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(false);
					CusBlkListTemp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusBlkListTemp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		CusBlkListTemp.manager_br_id._setValue(data.organno._getValue());
		CusBlkListTemp.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function onReturnRegStateCode(date){
		CusBlkListTemp.legal_addr._obj.element.value=date.id;
		CusBlkListTemp.legal_addr_displayname._obj.element.value=date.label;
	}

	//根据数据来源显示不同信息
	function changeFromType(){
		var dataSource = CusBlkListTemp.data_source._getValue();//数据来源
		if(dataSource=='20'){//系统外
			CusBlkListTemp.cus_id._obj._renderHidden(true);
			CusBlkListTemp.cus_id._obj._renderRequired(false);
		}else{
			CusBlkListTemp.cus_id._obj._renderHidden(false);
			CusBlkListTemp.cus_id._obj._renderRequired(true);
		}
	}
	
	function onReturnRegStateCode(date){
		CusBlkListTemp.legal_addr._obj.element.value=date.id;
    	CusBlkListTemp.legal_addr_displayname._obj.element.value=date.label;
	}
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="updateCusBlkListRecord.do" method="POST">
		<emp:gridLayout id="CusBlkListGroup" maxColumn="2" title="共享客户信息">
			<emp:text id="CusBlkListTemp.serno" label="登记流水号" maxlength="40" required="true" readonly="true" colSpan="2" hidden="true"/>	
			<emp:select id="CusBlkListTemp.data_source" label="数据来源" required="true" dictname="STD_ZB_DATA_SOURCE" colSpan="2" readonly="true"/>
			<emp:pop id="CusBlkListTemp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=returnCus" required="true"  readonly="true"/>
			<emp:text id="CusBlkListTemp.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:select id="CusBlkListTemp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="CusBlkListTemp.cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:select id="CusBlkListTemp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:select id="CusBlkListTemp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkListTemp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkListTemp.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone"/>
			<emp:text id="CusBlkListTemp.legal_addr" label="通讯地址" required="true" hidden="true"/>
			<emp:pop id="CusBlkListTemp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>	
			<emp:text id="CusBlkListTemp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>	
			<emp:date id="CusBlkListTemp.black_date" label="列入日期" required="false" colSpan="2" 
				onblur="CheckDate(CusBlkListTemp.black_date,'列入日期不能大于当前日期')" hidden="true"/>
			<emp:textarea id="CusBlkListTemp.black_reason" label="客户描述" maxlength="250" 
				required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusBlkListGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusBlkListTemp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusBlkListTemp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />	
			<emp:text id="CusBlkListTemp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName"/>
			<emp:text id="CusBlkListTemp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName"/>
			<emp:date id="CusBlkListTemp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusBlkListTemp.manager_id" label="责任人" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.manager_br_id" label="管理机构" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			
			<emp:textarea id="CusBlkListTemp.logout_reason" label="注销原因" maxlength="250" colSpan="2" hidden="true"/>
			<emp:date id="CusBlkListTemp.logout_date" label="注销日期" required="false" hidden="true"/>
			
			<emp:select id="CusBlkListTemp.status" label="状态" required="false" readonly="true" dictname="STD_CUS_BLK_STATUS" defvalue="001" hidden="true"/>
			<emp:text id="CusBlkListTemp.logout_id" label="注销人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.logout_br_id" label="注销机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.appr_id" label="审批人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.appr_br_id" label="审批机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateCusBlkList" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
