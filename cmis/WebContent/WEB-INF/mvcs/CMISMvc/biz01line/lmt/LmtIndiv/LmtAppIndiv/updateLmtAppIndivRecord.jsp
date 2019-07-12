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
		LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(true);
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

	//返回共同债务人信息
	function returnDebt(data){
		var debId = data.cus_id._getValue();
		var cusId = LmtAppIndiv.cus_id._getValue();
		if(cusId==debId){
			alert('共同债务人与申请人不能相同，请重新选择！');
			return;
		}
		LmtAppIndiv.same_debtor_id_displayname._setValue(data.cus_name._getValue());
		LmtAppIndiv.same_debtor_id._setValue(debId);
	}
	
	//控制共同债务人
	function checkSameDeb(){
		var isSameDeb = LmtAppIndiv.is_same_debtor._getValue();
		if(isSameDeb=='1'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(true);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(false);
		}else{
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(false);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(true);
			LmtAppIndiv.same_debtor_id_displayname._setValue('');
			LmtAppIndiv.same_debtor_id._setValue('');
		}
	}

	//计算金额是否合规：授信总额=自助金额+额度金额
	function checkTheAmt(){
		var totlAmt = LmtAppIndiv.crd_totl_amt._getValue();
		var selfAmt = LmtAppIndiv.self_amt._getValue();
		if(totlAmt!=null&&totlAmt!=''&&selfAmt!=null&&selfAmt!=''){
			if(totlAmt<selfAmt){
				alert('授信总额要大于自助金额！');
				LmtAppIndiv.self_amt._setValue('');
				return;
			}
		}
	}

	//检查共同债务人是否合规
	function checkCusDeb(){
		var cusId = LmtAppIndiv.cus_id._getValue();
		var debId = LmtAppIndiv.same_debtor_id._getValue();
		if(cusId!=null&&cusId!=''&&debId!=null&&debId!=''){
			if(cusId==debId){
				alert('共同债务人与申请人不能相同，请重新选择！');
				LmtAppIndiv.same_debtor_id_displayname._setValue('');
				LmtAppIndiv.same_debtor_id._setValue('');
				return;
			}
		}
	}

	//设置产品返回 
	function setProds(data){
		LmtAppIndiv.prd_id._setValue(data[0]);
		LmtAppIndiv.prd_id_displayname._setValue(data[1]);
	}

	//设置高度
	function changeHeight(){
		var iframeid = document.getElementById("rightframeSupmk");
		iframeid.height = "80px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	}
	function dynamicHeightIE(frame){
		if(frame.readyState == "complete"){
			changeHeight();
		}
	}
	function dynamicHeightOn(frame){
		if(document.readyState != "loading"){
			changeHeight();
		}
	}

	function doOnload(){
		LmtAppIndiv.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//显示非自助额度信息
		var serno = LmtAppIndiv.serno._getValue();
		var cus_id = LmtAppIndiv.cus_id._getValue();
		var app_type = LmtAppIndiv.app_type._getValue(); 
		var rightframeSupmk = document.getElementById("rightframeSupmk");
		var url = "<emp:url action='queryLmtAppIndivDetailsList.do'/>?serno="+serno + "&cus_id="+cus_id +"&app_type="+app_type+"&op=update";
		url = EMPTools.encodeURI(url);
		rightframeSupmk.src = url;

		//控制共同债务人隐藏
		var isSameDeb = LmtAppIndiv.is_same_debtor._getValue();
		if(isSameDeb=='2'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(false);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(true);
		}else if(isSameDeb=='1'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(true);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(false);
		}

		checkOrgOrNot();
	}

	//控制变更时字段的显示
	function checkOrgOrNot(){
		var app_type = '${context.LmtAppIndiv.app_type}';
		if("01"==app_type){    //如果是授信变更，显示原有额度情况   
			LmtAppIndiv.org_crd_totl_amt._obj._renderHidden(true);
			LmtAppIndiv.totl_start_date._obj._renderHidden(true);
			LmtAppIndiv.totl_end_date._obj._renderHidden(true);
			LmtAppIndiv.is_adj_term_totl._obj._renderHidden(true);
			LmtAppIndiv.self_start_date._obj._renderHidden(true);
			LmtAppIndiv.self_end_date._obj._renderHidden(true);
			LmtAppIndiv.org_self_amt._obj._renderHidden(true);
			LmtAppIndiv.is_adj_term_self._obj._renderHidden(true);
			LmtAppIndiv.self_start_date._obj._renderHidden(true);
			LmtAppIndiv.self_end_date._obj._renderHidden(true);
		}else{
			LmtAppIndiv.is_adj_term_totl._obj._renderRequired(true);
			LmtAppIndiv.term_type._obj._renderHidden(true);
			LmtAppIndiv.term._obj._renderHidden(true);
			LmtAppIndiv.term_type._obj._renderRequired(false);
			LmtAppIndiv.term._obj._renderRequired(false);
			LmtAppIndiv.is_adj_term_self._obj._renderRequired(true);
			LmtAppIndiv.self_term._obj._renderHidden(true);
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
		            alert('保存成功！');
		            doReturn();
				}else{
					alert('保存失败！');
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
		var url = '<emp:url action="queryLmtAppIndivList.do"/>?type=app';
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()" >
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updateLmtAppIndivRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppIndivGroup" title="总额度" maxColumn="2">
			<emp:text id="LmtAppIndiv.serno" label="业务编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:select id="LmtAppIndiv.app_type" label="申请类型：新增/变更/冻结/解冻" defvalue="01" required="true" dictname="STD_ZB_APP_TYPE" hidden="true"/>
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id_displayname" label="客户名称"   required="true" readonly="true"/>
			<emp:select id="LmtAppIndiv.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.biz_type" label="授信业务类型 ：内部授信/公开授信" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" hidden="true"/>
			<emp:text id="LmtAppIndiv.org_crd_totl_amt" label="原授信总额(元)" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAppIndiv.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppIndiv.crd_totl_amt" label="授信总额(元)" maxlength="18" required="true" dataType="Currency" onchange="checkTheAmt()" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="LmtAppIndiv.totl_start_date" label="授信起始日" readonly="true"/>
			<emp:date id="LmtAppIndiv.totl_end_date" label="授信到期日" readonly="true"/>
			<emp:select id="LmtAppIndiv.is_adj_term_totl" label="是否调整期限" dictname="STD_ZX_YES_NO" onchange="showTerm()" colSpan="2"/>
			<emp:select id="LmtAppIndiv.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppIndiv.term" label="期限" maxlength="3" required="true" dataType="Int" />
			<emp:select id="LmtAppIndiv.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="LmtAppIndiv.guar_type_detail" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE"/>
			<emp:select id="LmtAppIndiv.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" colSpan="2"/>
			<emp:select id="LmtAppIndiv.is_same_debtor" label="是否有共同债务人" required="true" dictname="STD_ZX_YES_NO" onchange="checkSameDeb()" />
			<emp:pop id="LmtAppIndiv.same_debtor_id_displayname" label="共同债务人" url="queryAllCusPop.do?cusTypCondition=belg_line='BL300' and cus_status='20'&returnMethod=returnDebt" />
			<emp:select id="LmtAppIndiv.is_self_revolv" label="是否开通自助循环" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true"/>
			
			<emp:select id="LmtAppIndiv.is_open_pos" label="是否开通POS支付" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtAppIndiv.pos_pay_type" label="POS机支付方式" required="false" dictname="STD_ZB_POS_PAY_TYPE" />
			<emp:text id="LmtAppIndiv.limit_regi_id" label="额度注册账号" maxlength="40" required="false" />
			<emp:text id="LmtAppIndiv.limit_regi_name" label="额度注册账户名" maxlength="80" required="false" />
			<emp:text id="LmtAppIndiv.same_debtor_id" label="共同债务人" maxlength="30" required="false" hidden="true" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="自助额度" maxColumn="2">
			<emp:text id="LmtAppIndiv.org_self_amt" label="原自助金额(元)" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.self_amt" label="自助金额(元)" maxlength="18" required="true" dataType="Currency" onchange="checkTheAmt()"/>
			<emp:date id="LmtAppIndiv.self_start_date" label="授信起始日" readonly="true"/>
			<emp:date id="LmtAppIndiv.self_end_date" label="授信到期日" readonly="true"/>
			<emp:select id="LmtAppIndiv.is_adj_term_self" label="是否调整期限" required="false" dictname="STD_ZX_YES_NO" onchange="showSelfTerm()"/>
			<emp:text id="LmtAppIndiv.self_term" label="自助期限(月)" maxlength="3" required="true" dataType="Int" />
			<emp:text id="LmtAppIndiv.ir_float_rate_self" label="利率浮动比(自助)" maxlength="16" required="false" dataType="Percent" hidden="true"/>
			<emp:text id="LmtAppIndiv.overdue_rate" label="逾期利率浮动比例" maxlength="16" required="false" dataType="Percent" hidden="true"/>
			<emp:select id="LmtAppIndiv.ir_adjust_type_self" label="利率调整方式(自助)" required="false" dictname="STD_IR_ADJUST_TYPE" hidden="true"/>
			<emp:text id="LmtAppIndiv.repay_type_self" label="贷款还款方式(自助)" maxlength="5" required="false" hidden="true"/>
		</emp:gridLayout>

		<div class='emp_gridlayout_title'>非自助额度</div>
		<div align="left">
		<iframe id="rightframeSupmk" src="" frameborder="0" scrolling="no" frameborder="0" width="100%" onload="dynamicHeightOn(this)" onreadystatechange="dynamicHeightIE(this)"></iframe>
		</div>
		
		<emp:gridLayout id="LmtAppIndivGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAppIndiv.inve_rst" label="调查人结论" maxlength="800" required="true" colSpan="2" />
			<emp:textarea id="LmtAppIndiv.memo" label="备注" maxlength="800" required="true" colSpan="2" />
			<emp:select id="LmtAppIndiv.belg_line" label="所属条线" required="false" dictname="STD_ZB_BUSILINE" readonly="true"/>
			<emp:select id="LmtAppIndiv.util_mode" label="提用方式" required="false" dictname="STD_ZB_UTIL_MODE" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppIndiv.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtAppIndiv.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" readonly="true"/>
			<emp:text id="LmtAppIndiv.input_id_displayname" label="登记人"  required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="LmtAppIndiv.input_br_id_displayname" label="登记机构"  required="true" defvalue="$organName" readonly="true"/>
			<emp:date id="LmtAppIndiv.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
		
			<emp:text id="LmtAppIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
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
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	
</body>
</html>
</emp:page>

