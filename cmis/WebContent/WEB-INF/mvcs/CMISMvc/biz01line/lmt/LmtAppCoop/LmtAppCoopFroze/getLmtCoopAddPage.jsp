<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
		
	//返回主管客户经理	
	function setconId(data){
		LmtAppJointCoop.manager_id._setValue(data.actorno._getValue());
		LmtAppJointCoop.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppJointCoop.manager_br_id._setValue(data.orgid._getValue());
		LmtAppJointCoop.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppJointCoop.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppJointCoop.manager_br_id._setValue(jsonstr.org);
					LmtAppJointCoop.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppJointCoop.manager_br_id._setValue("");
					LmtAppJointCoop.manager_br_id_displayname._setValue("");
					LmtAppJointCoop.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppJointCoop.manager_id._getValue();
					LmtAppJointCoop.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppJointCoop.manager_br_id._setValue("");
					LmtAppJointCoop.manager_br_id_displayname._setValue("");
					LmtAppJointCoop.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppJointCoop.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppJointCoop.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtAppJointCoop.manager_br_id._setValue(data.organno._getValue());
		LmtAppJointCoop.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	
	function onLoad(){
		//根据共享范围 设置所属机构的显示跟隐藏 
		var share_range = LmtAppJointCoop.share_range._getValue();
		if(share_range == 2){
			LmtAppJointCoop.belg_org._obj._renderHidden(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(false);
			LmtAppJointCoop.belg_org._obj._renderRequired(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(true);
		}else{
			LmtAppJointCoop.belg_org._obj._renderHidden(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(true);
			LmtAppJointCoop.belg_org._obj._renderRequired(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(false);
		}
		//客户信息查看
		LmtAppJointCoop.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		
		setFrozeHiddenOrNot();
	}

	//冻结解冻字段显示隐藏
	function setFrozeHiddenOrNot(){
		var app_type = LmtAppJointCoop.app_type._getValue();
		if(app_type=="03"){
			LmtAppJointCoop.unfroze_amt._obj._renderHidden(true);
		}else if(app_type=="04"){
			LmtAppJointCoop.froze_amt._obj._renderRequired(false);
			LmtAppJointCoop.froze_amt._obj._renderHidden(true);
			LmtAppJointCoop.unfroze_amt._obj._renderRequired(true);
		}
	}
	
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppJointCoop.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//检查冻结金额<=授信总额-已冻结金额
	function checkFrozeAmt(){
		var totl_amt = LmtAppJointCoop.lmt_totl_amt._getValue();
		var al_froze_amt = LmtAppJointCoop.al_froze_amt._getValue();
		var froze_amt = LmtAppJointCoop.froze_amt._getValue();
		if(totl_amt!=null&&totl_amt!=""&&froze_amt!=null&&froze_amt!=""&&al_froze_amt!=null&&al_froze_amt!=""){
			if((parseFloat(totl_amt)-parseFloat(al_froze_amt))<parseFloat(froze_amt)){
				alert('冻结金额不能大于授信总额减已冻结金额!');
				LmtAppJointCoop.froze_amt._setValue('');
			}
		}
	}

	//检查解冻金额不能大于冻结金额
	function checkUnFrozeAmt(){
		var unFroze_amt = LmtAppJointCoop.unfroze_amt._getValue();
		var al_froze_amt = LmtAppJointCoop.al_froze_amt._getValue();
		if(unFroze_amt!=null&&unFroze_amt!=""&&al_froze_amt!=null&&al_froze_amt!=""){
			if(parseFloat(unFroze_amt)>parseFloat(al_froze_amt)){
				alert('解冻金额不能大于已冻结金额!');
				LmtAppJointCoop.unfroze_amt._setValue('');
			}
		}
	}

	//异步提交申请数据
	function doSubmitLmtCoopFroze(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if("Y"==flag){
					alert("保存成功！");
					var url = '<emp:url action="getLmtCoopFrozenUpdatePage.do"/>?serno='+serno+"&op=view&type=froze&menuId=LmtCoopFrozen";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert("保存失败，失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = LmtAppJointCoop._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			document.getElementById("button_submitLmtCoopFroze").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	function doReturn() {
		var url = '<emp:url action="queryLmtAgrCoopList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="addLmtAppJointCoopRecord.do" method="POST">
		<emp:gridLayout id="LmtAppJointCoopGroup" maxColumn="2" title="合作方授信申请">
			<emp:text id="LmtAppJointCoop.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="LmtAppJointCoop.app_type" label="申请类型 " required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:text id="LmtAppJointCoop.serno" label="业务编号" maxlength="40" hidden="true" readonly="true" />
			<emp:text id="LmtAppJointCoop.cus_id" label="客户码" maxlength="30" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppJointCoop.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/>
			<emp:select id="LmtAppJointCoop.coop_type" label="合作方类型 " required="true" dictname="STD_ZB_COOP_TYPE" readonly="true"/>
			<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" readonly="true"/>
			<emp:text id="LmtAppJointCoop.belg_org" label="所属机构" cssElementClass="emp_field_text_readonly" required="false" hidden="true" colSpan="2"/>
			<emp:textarea id="LmtAppJointCoop.belg_org_displayname" label="所属机构"  required="false" readonly="true" hidden="true" colSpan="2"/> 
			<emp:select id="LmtAppJointCoop.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppJointCoop.al_froze_amt" label="已冻结金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppJointCoop.froze_amt" label="冻结金额" maxlength="18" required="true" dataType="Currency" onblur="checkFrozeAmt()"/>
			<emp:text id="LmtAppJointCoop.unfroze_amt" label="解冻金额" maxlength="18" dataType="Currency" onblur="checkUnFrozeAmt()"/>
			
			<emp:date id="LmtAppJointCoop.start_date" label="起始日期" readonly="true"/>
			<emp:date id="LmtAppJointCoop.end_date" label="到期日期" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppJointCoopGroup" maxColumn="2" title="登记信息">
			<emp:pop id="LmtAppJointCoop.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="LmtAppJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="LmtAppJointCoop.manager_id" label="责任人" required="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.manager_br_id" label="责任机构" required="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.input_id" label="登记人" maxlength="20" required="true" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="LmtAppJointCoop.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="${context.organNo}" hidden="true" />
			<emp:text id="LmtAppJointCoop.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="LmtAppJointCoop.input_br_id_displayname" label="登记机构" required="true" readonly="true" />
			<emp:date id="LmtAppJointCoop.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" readonly="true"/>
			<emp:date id="LmtAppJointCoop.app_date" label="申请日期" required="true" readonly="true" defvalue="${context.OPENDAY}" hidden="true"/>
			<emp:date id="LmtAppJointCoop.over_date" label="办结日期" required="false" hidden="true"/>
			<emp:text id="LmtAppJointCoop.flow_type" label="流程类型" required="false" hidden="true" defvalue="01" maxlength="20"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitLmtCoopFroze" label="保存"/>
			<emp:button id="reset" label="取消"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
