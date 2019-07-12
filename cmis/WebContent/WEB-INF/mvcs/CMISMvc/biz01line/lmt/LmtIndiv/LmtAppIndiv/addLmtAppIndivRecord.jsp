<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	//责任人
	function setconId(data){
		LmtAppIndiv.manager_id._setValue(data.actorno._getValue());
		LmtAppIndiv.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppIndiv.manager_br_id._setValue(data.orgid._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppIndiv.manager_br_id._setValue(jsonstr.org);
					LmtAppIndiv.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppIndiv.manager_id._getValue();
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppIndiv.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//责任机构
	function getOrgID(data){
		LmtAppIndiv.manager_br_id._setValue(data.organno._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.organname._getValue());
	}


	//设置产品返回 
	function setProds(data){
		LmtAppIndiv.prd_id._setValue(data[0]);
		LmtAppIndiv.prd_id_displayname._setValue(data[1]);
	}

	function doOnload(){
		LmtAppIndiv.cus_id._obj.addOneButton('view12','查看',viewCusInfo);

		checkOrgOrNot();  //控制变更时字段的显示

		//给主页签增加重载事件
		document.getElementById("main_tabs").href="javascript:reLoad()";

		//若不是‘待发起’状态，主管机构、主管客户经理不允许修改
		var app_status = LmtAppIndiv.approve_status._getValue();
		if(app_status!='000'){
			LmtAppIndiv.manager_id_displayname._obj._renderReadonly(true);
			LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(true);
		}
	}

	//控制变更时字段的显示
	function checkOrgOrNot(){
		var app_type = LmtAppIndiv.app_type._getValue();
		if("01"==app_type){    //如果是授信申请，隐藏原有额度情况   
			LmtAppIndiv.org_crd_totl_amt._obj._renderHidden(true);
		}else{
			//LmtAppIndiv.is_adj_term_totl._obj._renderRequired(true);
			//LmtAppIndiv.term_type._obj._renderHidden(true);
			//LmtAppIndiv.term._obj._renderHidden(true);
		}
	}
	
	//新增记录
	function doAddLmtAppIndiv(){
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
				if(flag=='success'){
					var serno = jsonstr.serno;
		            alert('保存成功！');
		            //var url = '<emp:url action="getLmtAppIndivUpdatePage.do"/>?serno='+serno+'&op=update';
					//url = EMPTools.encodeURI(url);
					//window.location = url;
				}else{
					alert(jsonstr.msg);
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
		var result = LmtAppIndiv._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}
	//返回
	function doReturn() {
		var url = '<emp:url action="queryLmtAppIndivList.do"/>?type=app&menuId=lmtindivapp';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//是否调整期限
	function showTerm(){
		var isAdjTermTotl = LmtAppIndiv.is_adj_term_totl._getValue();
		if(isAdjTermTotl=='1'){
			LmtAppIndiv.term_type._obj._renderHidden(false);
			LmtAppIndiv.term._obj._renderHidden(false);
			LmtAppIndiv.term_type._obj._renderRequired(true);
			LmtAppIndiv.term._obj._renderRequired(true);
		}else {
			LmtAppIndiv.term_type._obj._renderHidden(true);
			LmtAppIndiv.term._obj._renderHidden(true);
			LmtAppIndiv.term_type._obj._renderRequired(false);
			LmtAppIndiv.term._obj._renderRequired(false);
		}
	}
	function showSelfTerm(){
		var isAdjTermSelf = LmtAppIndiv.is_adj_term_self._getValue();
		if(isAdjTermSelf=='1'){
			LmtAppIndiv.self_term._obj._renderHidden(false);
			LmtAppIndiv.self_term._obj._renderRequired(true);
		}else{
			LmtAppIndiv.self_term._obj._renderHidden(true);
			LmtAppIndiv.self_term._obj._renderRequired(false);
		}
	}

	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppIndiv.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//重加载页面
	function reLoad(){
		//window.location.reload();
		var serno = LmtAppIndiv.serno._getValue();
		var cus_id = LmtAppIndiv.cus_id._getValue();
		var url = '<emp:url action="getLmtAppIndivUpdatePage.do"/>?menuId=lmtindivapp&serno='+serno+'&op=update&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location.href = url;
	}

</script>
</head>
<body class="page_content" onload="doOnload()" >
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updateLmtAppIndivRecord.do" method="POST">
		<emp:gridLayout id="LmtAppIndivGroup" title="授信信息" maxColumn="2">
			<emp:text id="LmtAppIndiv.serno" label="业务编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:select id="LmtAppIndiv.biz_type" label="授信业务类型 ：内部授信/公开授信" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" hidden="true"/>
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id_displayname" label="客户名称"   required="true" readonly="true"/>
			<emp:select id="LmtAppIndiv.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.app_type" label="申请类型" defvalue="01" required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppIndiv.org_crd_totl_amt" label="原授信总额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"  />
			<emp:text id="LmtAppIndiv.crd_totl_amt" label="授信总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" readonly="true"   />
			
			<emp:text id="LmtAppIndiv.totl_amt" label="非自助总额" maxlength="18" dataType="Currency" required="true" readonly="true"  cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.self_amt" label="自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"  readonly="true"  defvalue="0.00"/>
			<emp:text id="LmtAppIndiv.crd_cir_amt" label="非自助循环额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" readonly="true"  />
			<emp:text id="LmtAppIndiv.crd_one_amt" label="非自助一次性额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" readonly="true"  />
			
			<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:select id="LmtAppIndiv.is_overdue" label="逾期或欠息，垫款" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:date id="LmtAppIndiv.totl_start_date" label="授信起始日" readonly="true" hidden="true"/>
			<emp:date id="LmtAppIndiv.totl_end_date" label="授信到期日" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndiv.is_adj_term_totl" label="是否调整期限" dictname="STD_ZX_YES_NO" onchange="showTerm()" colSpan="2" hidden="true"/>
			<emp:select id="LmtAppIndiv.term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" defvalue="003" hidden="true"/>
			<emp:text id="LmtAppIndiv.term" label="期限" maxlength="3" dataType="Int" defvalue="0" hidden="true"/>
		</emp:gridLayout>

		<emp:gridLayout id="LmtAppIndivGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAppIndiv.inve_rst" label="调查人结论" maxlength="800" required="true" colSpan="2" />
			<emp:textarea id="LmtAppIndiv.memo" label="备注" maxlength="800" colSpan="2" />
			<emp:select id="LmtAppIndiv.belg_line" label="所属条线" required="false" dictname="STD_ZB_BUSILINE" hidden="true"/>
			<emp:select id="LmtAppIndiv.util_mode" label="提用方式" required="false" dictname="STD_ZB_UTIL_MODE" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppIndiv.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtAppIndiv.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 begin -->

			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:text id="LmtAppIndiv.input_id_displayname" label="登记人"   required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="LmtAppIndiv.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName" readonly="true"/>
			<emp:date id="LmtAppIndiv.input_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
		
			<emp:text id="LmtAppIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
	
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:text id="LmtAppIndiv.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="LmtAppIndiv.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:select id="LmtAppIndiv.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:date id="LmtAppIndiv.app_date" label="申请日期" required="true" hidden="true" defvalue="$OPENDAY"/>
			<emp:select id="LmtAppIndiv.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" hidden="true" defvalue="01"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addLmtAppIndiv" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

