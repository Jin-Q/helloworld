<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = "";
	if(context.containsKey("app_type")){
		app_type = (String)context.getDataValue("app_type");
	} 
	if("01".equals(app_type)){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input_user_name { /****** 长度固定 ******/
	width: 451px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

</style>
<script type="text/javascript">
<%
	String type = request.getParameter("type");
%>
	/*--user code begin--*/
	function returnCus(data){
		LmtAppFinGuar.cus_id._setValue(data.cus_id._getValue());//客户码
		LmtAppFinGuar.cus_id_displayname._setValue(data.cus_name._getValue());//客户名称
		LmtAppFinGuar.guar_cls._setValue(data.guar_cls._getValue());//担保类别
		LmtAppFinGuar.eval_rst._setValue(data.guar_crd_grade._getValue());	//评级结果
		LmtAppFinGuar.guar_bail_multiple._setValue(data.guar_bail_multiple._getValue());//担保倍数
	}

	function checkAppDate(){ 
		var openDay = '${context.OPENDAY}';
		var appDate = LmtAppFinGuar.app_date._getValue();
			if(appDate<openDay){
				alert("申请日期必须大于等于当前日期！");
				LmtAppFinGuar.app_date._setValue("");
			}
	}

	function checkEndDate(){
		var appDate = LmtAppFinGuar.app_date._getValue();
		var endDate = LmtAppFinGuar.end_date._getValue();
			if(endDate<appDate){
				alert("办结日期必须大于等于申请日期！");
				LmtAppFinGuar.end_date._setValue("");
			}
	}

	function setconId(data){
		LmtAppFinGuar.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppFinGuar.manager_id._setValue(data.actorno._getValue());
		LmtAppFinGuar.manager_br_id._setValue(data.orgid._getValue());
		LmtAppFinGuar.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppFinGuar.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppFinGuar.manager_br_id._setValue(jsonstr.org);
					LmtAppFinGuar.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppFinGuar.manager_br_id._setValue("");
					LmtAppFinGuar.manager_br_id_displayname._setValue("");
					LmtAppFinGuar.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppFinGuar.manager_id._getValue();
					LmtAppFinGuar.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppFinGuar.manager_br_id._setValue("");
					LmtAppFinGuar.manager_br_id_displayname._setValue("");
					LmtAppFinGuar.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppFinGuar.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppFinGuar.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		LmtAppFinGuar.manager_br_id._setValue(data.organno._getValue());
		LmtAppFinGuar.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function checkMoney(){
		var fin_totl_limit = LmtAppFinGuar.fin_totl_limit._getValue();//融资总额
		var single_quota = LmtAppFinGuar.single_quota._getValue();//单户限额
		var spac = LmtAppFinGuar.fin_totl_spac._getValue();//融资总敞口
		if(parseFloat(fin_totl_limit) < parseFloat(single_quota)){
			alert("单户限额必须小于等于融资总额！");
			LmtAppFinGuar.single_quota._setValue("");
		}
		if(parseFloat(fin_totl_limit)<parseFloat(spac)){
			alert("融资总额应大于于等于融资总敞口！");
			LmtAppFinGuar.fin_totl_limit._setValue("");
		}
	}

	function doUpdate(form){
		var form = document.getElementById("submitForm");
		var type = '<%=type%>';
		LmtAppFinGuar._checkAll();
		if(LmtAppFinGuar._checkAll()){
			LmtAppFinGuar._toForm(form);
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
						var url = '<emp:url action="queryLmtAppFinGuarList.do"/>?type='+type;
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
	}

	function doReturnPage(){
		var type='<%=type%>';
		var url = '<emp:url action="queryLmtAppFinGuarList.do"/>?type='+type;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function checkSpac(){
		var spac = LmtAppFinGuar.fin_totl_spac._getValue();//融资总敞口
		var totl = LmtAppFinGuar.fin_totl_limit._getValue();//融资总额
		if(parseFloat(spac)>parseFloat(totl)){
			alert("融资总敞口应小于等于融资总额！");
			LmtAppFinGuar.fin_totl_spac._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="授信基本信息" id="main_tabs">
			<emp:form id="submitForm" action="updateLmtAppFinGuarRecord.do" method="POST">
				<emp:gridLayout id="LmtAppFinGuarGroup" title="担保公司信息" maxColumn="2">
					<emp:text id="LmtAppFinGuar.serno" label="业务编号" maxlength="40" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly"/>
					<emp:pop id="LmtAppFinGuar.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=FinGuar&returnMethod=returnCus" required="true" colSpan="2" readonly="true"/>
					<emp:text id="LmtAppFinGuar.cus_id_displayname" label="客户名称"  colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
					<emp:text id="LmtAppFinGuar.fin_cls" label="融资类别" maxlength="20" required="false" defvalue="融资性担保公司" readonly="true"/>
					<emp:select id="LmtAppFinGuar.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" defvalue="1" readonly="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="LmtAppFinGuarGroup" title="融资额度信息" maxColumn="2">
					<emp:select id="LmtAppFinGuar.guar_cls" label="担保类别" required="true" dictname="STD_ZB_GUAR_TYPE" readonly="true"/>
					<emp:select id="LmtAppFinGuar.eval_rst" label="评级结果" required="true" dictname="STD_ZB_FINA_GRADE" readonly="true"/>
					<emp:text id="LmtAppFinGuar.guar_bail_multiple" label="担保放大倍数" maxlength="10" required="true" readonly="true"/>
					<emp:select id="LmtAppFinGuar.fin_cur_type" label="融资币种" dictname="STD_ZX_CUR_TYPE" required="true" readonly="true"/>
					<emp:text id="LmtAppFinGuar.fin_totl_limit" label="融资总额" maxlength="18" required="true" dataType="Currency" onchange="checkMoney()"/>
					<emp:text id="LmtAppFinGuar.single_quota" label="单户限额" maxlength="18" required="true" dataType="Currency" onchange="checkMoney()"/>
					<emp:text id="LmtAppFinGuar.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" onblur="checkSpac()" colSpan="2"/>
					<emp:select id="LmtAppFinGuar.lmt_term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" required="true"/>
					<emp:text id="LmtAppFinGuar.term" label="授信期限" maxlength="2" required="true"/>
					<emp:date id="LmtAppFinGuar.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:date id="LmtAppFinGuar.end_date" label="办结日期" required="false" hidden="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="LmtAppFinGuarGroup" title="登记信息" maxColumn="2">
					<emp:pop id="LmtAppFinGuar.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
					<emp:pop id="LmtAppFinGuar.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
					<emp:text id="LmtAppFinGuar.input_id_displayname" label="登记人" required="false" defvalue="$currentUserId" readonly="true"/>
					<emp:text id="LmtAppFinGuar.input_br_id_displayname" label="登记机构" required="false" defvalue="$organNo" readonly="true"/>
					<emp:text id="LmtAppFinGuar.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
					<emp:text id="LmtAppFinGuar.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
					<emp:text id="LmtAppFinGuar.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
					<emp:text id="LmtAppFinGuar.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
					<emp:text id="LmtAppFinGuar.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
					<emp:select id="LmtAppFinGuar.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
					<emp:text id="LmtAppFinGuar.app_type" label="申请类型" required="false" readonly="true" hidden="true"/>
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="update" label="修改" op="update"/>
					<emp:button id="returnPage" label="返回"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
