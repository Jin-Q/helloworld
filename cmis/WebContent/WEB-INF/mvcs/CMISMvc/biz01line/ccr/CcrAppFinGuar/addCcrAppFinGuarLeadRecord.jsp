<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String smallFlag = (String)request.getAttribute("smallFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String lmt_serno = "";
	if(context.containsKey("CcrAddInf.lmt_serno")){
		lmt_serno = (String)context.getDataValue("CcrAddInf.lmt_serno");
	}
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

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
	function setconId(data){
		CcrAddInf.manager_id_displayname._setValue(data.actorname._getValue());
		CcrAddInf.manager_id._setValue(data.actorno._getValue());
		CcrAddInf.manager_br_id._setValue(data.orgid._getValue());
		CcrAddInf.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CcrAddInf.manager_br_id_displayname._obj._renderReadonly(true);
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
					CcrAddInf.manager_br_id._setValue(jsonstr.org);
					CcrAddInf.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CcrAddInf.manager_br_id._setValue("");
					CcrAddInf.manager_br_id_displayname._setValue("");
					CcrAddInf.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CcrAddInf.manager_id._getValue();
					CcrAddInf.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CcrAddInf.manager_br_id._setValue("");
					CcrAddInf.manager_br_id_displayname._setValue("");
					CcrAddInf.manager_br_id_displayname._obj._renderReadonly(false);
					CcrAddInf.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CcrAddInf.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		CcrAddInf.manager_br_id._setValue(data.organno._getValue());
		CcrAddInf.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	function doload(){
		CcrAddInf.cus_id._obj.addOneButton('view12','查看',viewCusInfo);   //在客户码后面新增 查看 按钮
	}
	//查看客户详细信息
	function viewCusInfo(){
		var cus_id = CcrAddInf.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	
	function doNext(){
		var result = CcrAddInf._checkAll();
		if(!result){
			return;
		}
		var form = document.getElementById("submitForm");
		CcrAddInf._toForm(form);
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
				var cus_id = jsonstr.cus_id;
				var fnc_year = jsonstr.fnc_year;
				if(flag == "success"){
					var url = '<emp:url action="getCcrAppFinGuarUpdatePage.do"/>&serno='+serno+'&cus_id='+cus_id+'&fnc_year='+fnc_year+'&flag=${context.flag}';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("发生异常！");
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
		
	function doReturn() {
		var flag = CcrAddInf.flag._getValue();
		var url = '<emp:url action="queryCcrAppFinGuarList.do"/>?flag='+flag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCcrAppFinGuarRecord.do" method="POST">
		
		<emp:gridLayout id="CcrAddInfGroup" maxColumn="2" title="信用评级申请主表">
			<emp:text id="CcrAddInf.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="CcrAddInf.model_no_displayname" label="评级模型"  required="false" readonly="true" defvalue="${context.model_name}"/>
			<emp:text id="CcrAddInf.cus_id" label="客户码" maxlength="30" required="true" readonly="true" defvalue="${context.cus_id}"/>
			<emp:text id="CcrAddInf.cus_id_displayname" label="客户名称"  cssElementClass="emp_field_text_input_user_name" colSpan = "2" required="true" readonly="true" defvalue="${context.cus_name}"/>
			<emp:select id="CcrAddInf.cus_type" label="客户类型 " required="true"  dictname="STD_ZB_CUS_TYPE" defvalue="${context.cus_type}" readonly="true"/>
			<emp:text id="CcrAddInf.app_begin_date" label="申请日期" maxlength="10" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:select id="CcrAddInf.is_authorize" label="是否授信" required="false"  readonly="true" dictname="STD_ZX_YES_NO" defvalue="${context.is_authorize}"/>
			<emp:text id="CcrAddInf.model_no" label="评级模型Id" maxlength="60" required="false" hidden="true" defvalue="${context.model_no}"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusComGroup" maxColumn="2" title="信用评级申请附表">
			<emp:select id="CcrAddInf.last_adjusted_grade" label="上次信用等级" required="false"  readonly="true"  dictname="STD_ZB_FINA_GRADE" defvalue="${context.cus_crd_grade}"/>
			<emp:text id="CcrAddInf.lat_app_end_date" label="上次评级日期" maxlength="10" required="false"  readonly="true" defvalue="${context.com_crd_dt}"/>
			<emp:text id="CcrAddInf.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="${context.flag}"/>
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CcrAddInf.manager_id_displayname" label="主管客户经理" required="true" readonly="false" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CcrAddInf.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="CcrAddInf.input_id_displayname" label="登记人"  required="false" readonly="true" hidden="false" defvalue="$currentUserName"/>
			<emp:text id="CcrAddInf.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" defvalue="$organName"/>
			<emp:text id="CcrAddInf.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="CcrAddInf.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="true"/>
			<emp:text id="CcrAddInf.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="CcrAddInf.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="CcrAddInf.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CcrAddInf.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

