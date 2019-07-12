<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
text-align:left;
width:450px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	//选择客户POP框返回方法
	function returnCus(data){
		PspAppCusVisit.cus_id._setValue(data.cus_id._getValue());
		PspAppCusVisit.cus_id_displayname._setValue(data.cus_name._getValue());
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		PspAppCusVisit.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		PspAppCusVisit.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		PspAppCusVisit.manager_id._setValue(data.cust_mgr._getValue());
		PspAppCusVisit.manager_br_id._setValue(data.main_br_id._getValue());
		doChangeOrgUrl();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	}
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
	function doChangeOrgUrl(){
		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("belg2team" == flag){//客户经理只属于团队
					var manager_id = PspAppCusVisit.manager_id._getValue();
					PspAppCusVisit.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};

   		var manager_id = PspAppCusVisit.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	//校验访客时间不能大于openDay
	function checkVtime(){
		var openDay = '${context.OPENDAY}';
		var visitTime = PspAppCusVisit.visit_time._getValue();
		if(visitTime!=null&&visitTime!=''){
			if(visitTime>openDay){
				alert('访客时间不能大于当前日期！');
				PspAppCusVisit.visit_time._setValue('');
			}
		}
	}
	//地址信息
	function onReturnAddr(date){
		PspAppCusVisit.visit_addr._obj.element.value=date.id;
		PspAppCusVisit.visit_addr_displayname._obj.element.value=date.label;
	}
	//新增客户走访登记
	function doAddPspAppCusVisit(){
		var form = document.getElementById("submitForm");
		if(PspAppCusVisit._checkAll()){
			PspAppCusVisit._toForm(form);
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
						var url = '<emp:url action="queryPspAppCusVisitList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("保存失败！");
					}
				}
			};
			var handleFailure = function(o){
				alert("保存失败！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
			return false;
		}
	}
	/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
	//返回主管客户经理	
	function setconId(data){
		PspAppCusVisit.manager_id._setValue(data.actorno._getValue());
		PspAppCusVisit.manager_id_displayname._setValue(data.actorname._getValue());
		PspAppCusVisit.manager_br_id._setValue(data.orgid._getValue());
		PspAppCusVisit.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//PspAppCusVisit.manager_br_id_displayname._obj._renderReadonly(true);
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
					PspAppCusVisit.manager_br_id._setValue(jsonstr.org);
					PspAppCusVisit.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					PspAppCusVisit.manager_br_id._setValue("");
					PspAppCusVisit.manager_br_id_displayname._setValue("");
					PspAppCusVisit.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspAppCusVisit.manager_id._getValue();
					PspAppCusVisit.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspAppCusVisit.manager_br_id._setValue("");
					PspAppCusVisit.manager_br_id_displayname._setValue("");
					PspAppCusVisit.manager_br_id_displayname._obj._renderReadonly(false);
					PspAppCusVisit.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspAppCusVisit.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		PspAppCusVisit.manager_br_id._setValue(data.organno._getValue());
		PspAppCusVisit.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPspAppCusVisitRecord.do" method="POST">
		
		<emp:gridLayout id="PspAppCusVisitGroup" title="客户走访登记" maxColumn="2">
			<emp:text id="PspAppCusVisit.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:pop id="PspAppCusVisit.cus_id" label="受访客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cust_mgr = '${context.currentUserId}' &returnMethod=returnCus" required="true"/>
			<emp:text id="PspAppCusVisit.cus_id_displayname" label="受访客户名称" readonly="true" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:date id="PspAppCusVisit.visit_time" label="访客时间" required="true" onblur="checkVtime()"/>
			<emp:text id="PspAppCusVisit.visit_addr" label="访客地点" required="false" maxlength="80" hidden="true"/>
			<emp:pop id="PspAppCusVisit.visit_addr_displayname" label="访客地点" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnAddr" cssElementClass="emp_field_text_input2"/>
			<emp:text id="PspAppCusVisit.visit_street" label="街道" maxlength="80" required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="PspAppCusVisit.visit_obj" label="访谈对象" maxlength="80" required="true" />
			<emp:text id="PspAppCusVisit.visit_obj_phone" label="访谈对象电话" maxlength="80" required="false" dataType="Phone" />
			<emp:select id="PspAppCusVisit.visit_obj_duty" label="访谈对象职务" cssElementClass="emp_field_text_input2" dictname="STD_ZX_DUTY" required="true" colSpan="2"/>
			<emp:select id="PspAppCusVisit.is_cret_need" label="是否存在信贷需求" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspAppCusVisit.is_advice_sale" label="是否建议再次营销" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="PspAppCusVisit.visit_dest" label="本次访客目的" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.visit_record" label="本次访谈记录" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.visit_res" label="本次访谈结论" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<!-- add by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
			<emp:pop id="PspAppCusVisit.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="PspAppCusVisit.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
			<emp:text id="PspAppCusVisit.manager_id" label="责任人" required="true" hidden="true" />
			<emp:text id="PspAppCusVisit.manager_br_id" label="责任机构" required="true" hidden="true" />
			<!-- add by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
			<emp:text id="PspAppCusVisit.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspAppCusVisit.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName"  readonly="true"/>
			<emp:text id="PspAppCusVisit.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="PspAppCusVisit.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="PspAppCusVisit.input_date" label="登记日期" maxlength="10" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="PspAppCusVisit.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addPspAppCusVisit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

