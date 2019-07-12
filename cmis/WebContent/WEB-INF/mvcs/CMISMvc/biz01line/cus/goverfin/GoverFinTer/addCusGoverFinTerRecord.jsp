<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
.emp_field_select_select {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 210px;
}
</style>

<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusGoverFinTerList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function returnCus(data){
		CusGoverFinTer.cus_id._setValue(data.cus_id._getValue());
		CusGoverFinTer.cus_name._setValue(data.cus_name._getValue());
		checkCusid(data.cus_id._getValue());
	};
	function checkCusid(cus_id){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "false" ){
				CusGoverFinTer.cus_id._setValue("");
				CusGoverFinTer.cus_name._setValue("");
				alert("已经存该客户的信息记录，请重新选择：");	
			}else{
			}
		}
		var handleFailure = function(o){
		        alert("异步回调失败！");	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var url = '<emp:url action="checkCusidApplyed.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};

	function doOnLoad(){
		CusGoverFinTer.cash_cover_rate._obj.addOneButton('viewCus','测算',showCusDetail);
		changeMoveDate();
	};	
	function showCusDetail(){
		var url = '<emp:url action="calCashCoverRate.do"/>?returnMethod=getRate&serno='
			+CusGoverFinTer.serno._getValue()+'&cus_id='+CusGoverFinTer.cus_id._getValue();
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.45+',width='+window.screen.availWidth*0.5+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function doAddCusGoverFinTer(){
		var form = document.getElementById("submitForm");
		CusGoverFinTer._checkAll();
		if(CusGoverFinTer._checkAll()){
			CusGoverFinTer._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag != "false"){
						alert("保存成功！");
						var url = '<emp:url action="getCusGoverFinTerUpdatePage.do"/>?serno='+flag;
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
			return false;
		}
	};
	function getOrgID(data){
		CusGoverFinTer.manager_br_id._setValue(data.organno._getValue());
		CusGoverFinTer.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function setconId(data){
		CusGoverFinTer.manager_id_displayname._setValue(data.actorname._getValue());
		CusGoverFinTer.manager_id._setValue(data.actorno._getValue());
		CusGoverFinTer.manager_br_id._setValue(data.orgid._getValue());
		CusGoverFinTer.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusGoverFinTer.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};
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
					CusGoverFinTer.manager_br_id._setValue(jsonstr.org);
					CusGoverFinTer.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusGoverFinTer.manager_br_id._setValue("");
					CusGoverFinTer.manager_br_id_displayname._setValue("");
					CusGoverFinTer.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusGoverFinTer.manager_id._getValue();
					CusGoverFinTer.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusGoverFinTer.manager_br_id._setValue("");
					CusGoverFinTer.manager_br_id_displayname._setValue("");
					CusGoverFinTer.manager_br_id_displayname._obj._renderReadonly(false);
					CusGoverFinTer.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusGoverFinTer.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	
	function getRate(data){
		CusGoverFinTer.cash_cover_rate._setValue(data);
	};
	function getFinaGuarGap(){
		var crd_ln_guar_gap = CusGoverFinTer.crd_ln_guar_gap._getValue();
		var secu_ln_gap = CusGoverFinTer.secu_ln_gap._getValue();
		if(crd_ln_guar_gap != '' && secu_ln_gap != ''){
			a=parseFloat(crd_ln_guar_gap) + parseFloat(secu_ln_gap);
			CusGoverFinTer.fina_guar_gap._setValue(a.toString());
		}
	};
	
	function changeMoveDate(){
		var gover_ter_type = CusGoverFinTer.gover_ter_type._getValue();
		if(gover_ter_type == '001'){
			CusGoverFinTer.move_date._obj._renderRequired(true);
			CusGoverFinTer.move_date._obj._renderHidden(false);
		}else{
			CusGoverFinTer.move_date._setValue("");
			CusGoverFinTer.move_date._obj._renderHidden(true);
			CusGoverFinTer.move_date._obj._renderRequired(false);
		}
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="addCusGoverFinTerRecord.do" method="POST">
		<emp:gridLayout id="CusGoverFinTerGroup" title="基本信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.serno" label="申请流水号" maxlength="40" hidden="true" readonly="true"/>
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="CusGoverFinTer.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200')
			and cus_status='20' and cus_id in (select cus_id from cus_com where gover_fin_ter = '1') and cust_mgr = '${context.currentUserId}'&returnMethod=returnCus" required="true" colSpan="2" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CusGoverFinTer.cus_name" label="客户名称"  required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusGoverFinTer.gover_fin_loan_type" label="政府融资贷款类型" required="true" dictname="STD_ZB_GOVER_FIN_TYPE" colSpan="2" />
			<emp:select id="CusGoverFinTer.gover_ter_type" label="平台类别"  required="true" dictname="STD_ZB_TER_TYPE" onchange="changeMoveDate()" />
			<emp:date id="CusGoverFinTer.move_date" label="调出平台时间" required="true" />
			<emp:text id="CusGoverFinTer.cash_cover_rate" label="现金流覆盖率" maxlength="16" required="true" dataType="Rate" readonly="true"/>
			<emp:select id="CusGoverFinTer.cash_cover_degree" label="现金流覆盖程度"  required="true" dictname="STD_ZB_COVER_DEGREE" />
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" title="余额信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.secu_ln_bal" label="担保贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.crd_ln_bal" label="信用贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.land_property_usufruct_bal" label="土地收益权质押贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.land_mortgage_bal" label="其中土地抵押贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" title="缺口信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.guar_gap" label="担保缺口(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.crd_ln_guar_gap" label="对信用贷款的保证估值缺口(元)" maxlength="18" 
			required="true" dataType="Currency" onchange="getFinaGuarGap()"/>
			<emp:text id="CusGoverFinTer.secu_ln_gap" label="对担保贷款的缺口(元)" maxlength="18" required="true" 
			dataType="Currency" onchange="getFinaGuarGap()"/>
			<emp:text id="CusGoverFinTer.fina_guar_gap" label="其中增加财务政担保后的估值缺口(元)" maxlength="18" readonly="true" required="true" dataType="Currency" />
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusGoverFinTer.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusGoverFinTer.manager_br_id_displayname" label="管理机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusGoverFinTer.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusGoverFinTer.input_br_id_displayname" label="登记机构" readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusGoverFinTer.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusGoverFinTer.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusGoverFinTer.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>			
			<emp:text id="CusGoverFinTer.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusGoverFinTer.manager_id" label="责任人" hidden="true"/>
			<emp:text id="CusGoverFinTer.manager_br_id" label="管理机构" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusGoverFinTer" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

