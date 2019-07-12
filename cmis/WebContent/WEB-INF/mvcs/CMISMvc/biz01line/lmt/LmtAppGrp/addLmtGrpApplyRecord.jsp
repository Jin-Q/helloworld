<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%><emp:page>
<html>
<head>
<title>修改页面</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("LmtAppGrp.app_type").toString();
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//返回主管客户经理	
	function setconId(data){
		LmtAppGrp.manager_id._setValue(data.actorno._getValue());
		LmtAppGrp.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppGrp.manager_br_id._setValue(data.orgid._getValue());
		LmtAppGrp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppGrp.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppGrp.manager_br_id._setValue(jsonstr.org);
					LmtAppGrp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppGrp.manager_br_id._setValue("");
					LmtAppGrp.manager_br_id_displayname._setValue("");
					LmtAppGrp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppGrp.manager_id._getValue();
					LmtAppGrp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppGrp.manager_br_id._setValue("");
					LmtAppGrp.manager_br_id_displayname._setValue("");
					LmtAppGrp.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppGrp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppGrp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtAppGrp.manager_br_id._setValue(data.organno._getValue());
		LmtAppGrp.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	
	function onLoad(){
		var action = '${context.operate}';   //新增跟修改共用同一个页面，从后台绑定action 
		var form = document.getElementById('submitForm');
		form.action = action;

		LmtAppGrp.grp_no._obj.addOneButton('view12','查看',viewGrpCusInfo);  //集团编号加查看按钮
	}

	function viewGrpCusInfo(){
		var url = "<emp:url action='queryCusGrpInfoPopDetial.do'/>&grp_no="+LmtAppGrp.grp_no._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//异步提交申请数据
	function doSubmitLmtGrpApp(){
		var form = document.getElementById("submitForm");
		LmtAppGrp._checkAll();
		if(LmtAppGrp._checkAll()){
			LmtAppGrp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="getLmtGrpApplyUpdatePage.do"/>?serno='+serno+'&op=update';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="addLmtGrpApplyRecord.do" method="POST">
		<emp:gridLayout id="LmtAppGrpGroup" maxColumn="2" title="集团授信申请">
			<emp:text id="LmtAppGrp.serno" label="业务编号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="true"/>
			<emp:text id="LmtAppGrp.grp_no" label="集团编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="LmtAppGrp.grp_name_displayname" label="集团名称"  required="true" defvalue="${context.LmtAppGrp.grp_no_displayname}" cssElementClass="emp_field_text_readonly"/>
			<emp:select id="LmtAppGrp.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:select id="LmtAppGrp.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" readonly="true"/>
			<emp:select id="LmtAppGrp.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<% if("02".equals(app_type)){  //如果是授信变更，显示原有额度情况      %>
			<emp:text id="LmtAppGrp.org_crd_totl_amt" label="原授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
			<% }%>
			<emp:text id="LmtAppGrp.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="LmtAppGrp.app_date" label="申请日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:date id="LmtAppGrp.over_date" label="办结日期" required="false" hidden="true"/>
			<emp:select id="LmtAppGrp.flow_type" label="流程类型" dictname="STD_ZB_FLOW_TYPE" hidden="true" defvalue="01"/>
			<emp:textarea id="LmtAppGrp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppGrpGroup" maxColumn="2" title="登记信息">
			<emp:pop id="LmtAppGrp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="LmtAppGrp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="LmtAppGrp.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="LmtAppGrp.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:date id="LmtAppGrp.input_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="LmtAppGrp.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:text id="LmtAppGrp.grp_agr_no" label="集团协议编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="LmtAppGrp.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppGrp.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppGrp.input_id" label="登记人" maxlength="20" required="true" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="LmtAppGrp.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="${context.organNo}" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitLmtGrpApp" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
