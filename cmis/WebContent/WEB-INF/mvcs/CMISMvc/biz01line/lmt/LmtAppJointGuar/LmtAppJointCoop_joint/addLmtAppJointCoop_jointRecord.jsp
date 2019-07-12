<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doOnload(){
		controlOrg();  //初始化机构信息显示
	}
	//选择客户POP框返回方法
	function returnCus(data){
		var cus_crd_grade = data.cus_crd_grade._getValue();
		var belg_line = data.belg_line._getValue();
        if(belg_line != "BL300"){
        	if(cus_crd_grade=='11'||cus_crd_grade=='12'||cus_crd_grade=='13'){
    			var cus_id = data.cus_id._getValue();
    			LmtAppJointCoop.cus_id._setValue(cus_id);
    			LmtAppJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
    			LmtAppJointCoop.manager_id._setValue(data.cust_mgr._getValue());
    			LmtAppJointCoop.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
    			LmtAppJointCoop.manager_br_id._setValue(data.main_br_id._getValue());
    			LmtAppJointCoop.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
    		}else{
    			alert('联保小组成员(对公客户)评级应达到我行信用等级A级（含）以上!');
    		}
        }else{
        	var cus_id = data.cus_id._getValue();
			LmtAppJointCoop.cus_id._setValue(cus_id);
			LmtAppJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
			LmtAppJointCoop.manager_id._setValue(data.cust_mgr._getValue());
			LmtAppJointCoop.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
			LmtAppJointCoop.manager_br_id._setValue(data.main_br_id._getValue());
			LmtAppJointCoop.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
        }
	}
	
	//设置共享范围机构
	function getBelgOrg(data){
		LmtAppJointCoop.belg_org._setValue(data[0]);
		LmtAppJointCoop.belg_org_displayname._setValue(data[1]);
	}

	/****** 共享范围与所属机构控制 *******/
	function controlOrg(){
		var _value = LmtAppJointCoop.share_range._getValue();
		if(_value == 2){
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
	};
	
	//单户限额  控制
	function singleMaxAmt(obj){
		var _totl = LmtAppJointCoop.lmt_totl_amt._getValue();
		var _single = LmtAppJointCoop.single_max_amt._getValue();
		if(_totl!=null&&_totl!=''&&_single!=null&&_single!=''){
			if(parseFloat(_single)>parseFloat(_totl)){
				alert('单户限额不能大于授信总额！');
				obj._setValue('');
			}
		}
	}
	
	//责任人
	function setManagerId(data){
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
	
	//返回责任机构
	function getOrganName(data){
		LmtAppJointCoop.manager_br_id._setValue(data.organno._getValue());
		LmtAppJointCoop.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	//异步提交申请数据
	function doSubmitLmtAppJointCoop(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("保存成功！");
					var paramStr = jsonstr.serno;
					var url = '<emp:url action="getLmtAppJointCoop_jointUpdatePage.do"/>?serno='+paramStr+'&menuId=unit_team_crd_apply&op=add';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
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
			document.getElementById("button_checkBeforeSave").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	/**保存前先校验:
	   1.是否存在有效联保小组名单中
	   2.是否存在在途的联保申请中*/
	function doCheckBeforeSave(){
		if(!LmtAppJointCoop._checkAll()){
			return ;
		}
		var cus_id = LmtAppJointCoop.cus_id._getValue();
		var url = '<emp:url action="chkBeforeAddJointCoopNameRecord.do"/>&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("联保新增校验失败!");
					return;
				}
				var flag=jsonstr.flag;
				var returnMsg = jsonstr.returnMsg;
				if(flag=="success"){
					doSubmitLmtAppJointCoop();
				}else if(flag=="existThis"){//该客户已存在该笔申请中
					alert(returnMsg);
				}else{
					if(confirm(returnMsg)){
						doSubmitLmtAppJointCoop();
					}
				}
			}	
		};
		var handleFailure = function(o){
			alert("联保新增校验失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="addLmtAppJointCoop_jointRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppJointCoopGroup" title="联保小组信息" maxColumn="2">
			<emp:pop id="LmtAppJointCoop.cus_id" label="组长客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cus_type!='A2'&returnMethod=returnCus" required="true"/>
			<emp:text id="LmtAppJointCoop.cus_id_displayname" label="组长客户名称" required="false" readonly="true"/>
			<emp:select id="LmtAppJointCoop.coop_type" label="类别" required="true" dictname="STD_ZB_COOP_TYPE" colSpan="2" readonly="true" defvalue="010"/>
			<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" onchange="controlOrg()" hidden="true"/>
			<emp:pop id="LmtAppJointCoop.belg_org" label="所属机构码"  cssElementClass="emp_pop_common_org" url="queryMultiSOrgPop.do" returnMethod="getBelgOrg"  required="false"  hidden="true" colSpan="2" popParam="height=600px;width:800px"/>
			<emp:textarea id="LmtAppJointCoop.belg_org_displayname" label="所属机构名称" readonly="true" hidden="true" colSpan="2"/>
			<emp:date id="LmtAppJointCoop.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="LmtAppJointCoop.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" defvalue="01" readonly="true"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:select id="LmtAppJointCoop.is_overdue" label="额度是否逾期" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:date id="LmtAppJointCoop.over_date" label="办结日期" required="false" hidden="true"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppJointCoopGroupM" title="小组额度信息" maxColumn="2">
			<emp:select id="LmtAppJointCoop.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" onblur="singleMaxAmt(LmtAppJointCoop.lmt_totl_amt)"/>
			<emp:text id="LmtAppJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" onblur="singleMaxAmt(LmtAppJointCoop.single_max_amt)"/>
			<emp:select id="LmtAppJointCoop.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppJointCoop.term" label="期限" maxlength="3" required="true" dataType="Int" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppJointCoopGroupM" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppJointCoop.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
			<emp:text id="LmtAppJointCoop.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
			
			<emp:pop id="LmtAppJointCoop.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="LmtAppJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="LmtAppJointCoop.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="LmtAppJointCoop.input_br_id_displayname" label="登记机构"  required="false" readonly="true" defvalue="$organName"/>
			<emp:date id="LmtAppJointCoop.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY" />
			
			<emp:select id="LmtAppJointCoop.flow_type" label="流程类型" required="false" readonly="true" dictname="STD_ZB_FLOW_TYPE" defvalue="01" hidden="true"/>
			<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" required="false" readonly="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:text id="LmtAppJointCoop.app_serno" label="入/退圈申请编号" maxlength="40" hidden="true" readonly="true" defvalue="${context.app_serno}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="checkBeforeSave" label="保存"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

