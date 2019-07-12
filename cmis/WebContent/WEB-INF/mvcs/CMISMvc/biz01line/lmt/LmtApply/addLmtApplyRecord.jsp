<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>新增页面</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("LmtApply.app_type").toString();
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	/**选择POP框用户后自动赋值客户码及客户名称*/
	function setCusDatas(data){
		LmtApply.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
	}

	/**计算授信额度=循环额度+一次性额度 */
	function computeCrdAmt(){
		var crd_cir_amt = LmtApply.crd_cir_amt._getValue();
		var crd_one_amt = LmtApply.crd_one_amt._getValue();
		var total_amt = 0.00;
		if(null!=crd_cir_amt && ""!=crd_cir_amt){
			total_amt += parseFloat(crd_cir_amt);
		}
		if(null!=crd_one_amt && ""!=crd_one_amt){
			total_amt += parseFloat(crd_one_amt);
		}
		LmtApply.crd_totl_amt._setValue(total_amt+"");
	}

	//返回主管客户经理	
	function setconId(data){
		LmtApply.manager_id._setValue(data.actorno._getValue());
		LmtApply.manager_id_displayname._setValue(data.actorname._getValue());
		LmtApply.manager_br_id._setValue(data.orgid._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtApply.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtApply.manager_br_id._setValue(jsonstr.org);
					LmtApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtApply.manager_id._getValue();
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtApply.manager_br_id._setValue(data.organno._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doAddLmtApply(){
		var form = document.getElementById("submitForm");
		LmtApply._checkAll();
		if(LmtApply._checkAll()){
			LmtApply._toForm(form);
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
						var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?serno='+serno+'&op=update&isShow=N';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("保存失败！"+jsonstr.message);
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
	}

	function onLoad(){
		LmtApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//查看客户综合信息
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=600,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="addLmtApplyRecord.do" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="单一法人授信申请" maxColumn="2">
			<emp:text id="LmtApply.serno" label="业务编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:text id="LmtApply.grp_serno" label="集团授信编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="LmtApply.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" colSpan="2" readonly="true"/>
			<!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 begin-->
			<emp:select id="LmtApply.is_overdue" label="逾期或欠息，垫款" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:text id="LmtApply.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:select id="LmtApply.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01"/>
			<emp:text id="cus_name_displayname" label="客户名称" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly" defvalue="${context.LmtApply.cus_id_displayname}"/> 
			
			<emp:select id="LmtApply.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true" defvalue="20"/>
			<emp:select id="LmtApply.lmt_type" label="授信类别" required="true" dictname="STD_ZX_LMT_PRD" readonly="true"/>
			<emp:select id="LmtApply.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<% if("02".equals(app_type)){  //如果是授信变更，显示原有额度情况      %>
			<emp:text id="LmtApply.org_crd_totl_amt" label="原授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtApply.org_crd_cir_amt" label="原循环授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtApply.org_crd_one_amt" label="原一次性授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
			<% }%>
			<emp:text id="LmtApply.crd_totl_amt" label="授信总额" maxlength="16" required="true" colSpan="2" readonly="true" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtApply.crd_cir_amt" label="循环授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="LmtApply.crd_one_amt" label="一次性授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			
			<emp:select id="LmtApply.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true"/>
			<emp:date id="LmtApply.app_date" label="申请日期" required="false"  defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:text id="LmtApply.over_date" label="办结日期" maxlength="10" required="false" hidden="true"/>
			<emp:textarea id="LmtApply.memo" label="备注" maxlength="200" required="false" colSpan="3" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtApplyGroup" title="登记信息" maxColumn="2">
			<emp:pop id="LmtApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="LmtApply.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
			<emp:text id="LmtApply.manager_id" label="责任人" required="true" hidden="true"/>
			<emp:text id="LmtApply.manager_br_id" label="责任机构" required="true" hidden="true"/>
			<emp:text id="LmtApply.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="LmtApply.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="LmtApply.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="LmtApply.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:text id="LmtApply.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="LmtApply.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
			<emp:text id="LmtApply.agr_no" label="协议编号" hidden="true"/> 
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addLmtApply" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

