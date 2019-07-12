<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_field_cus_addr_input {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 395px;
};
</style>
<script type="text/javascript">

	/*--user code begin--*/
	//同业机构成立日期的检查
	function CheckDate(date){
		var str_date=date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(str_date!=null && str_date!="" ){
			var flag = CheckDate1BeforeDate2(str_date,openDay);
			if(!flag){
				alert("输入日期要小于等于当前日期！");
				date._obj.element.value="";
			}
     	}
	}

	//评级到期日期的检查
	function CheckExpDate(date1){
		var end = date1._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(end!=null && end!="" ){
			var flag = CheckDate1BeforeDate2(openDay,end);
			if(!flag){
				alert("当前日期要小于等于输入的日期！");
				date1._obj.element.value="";
			}
		}
	}
	function doOnLoad(){
		//上市标志事件
		EMPTools.addEvent(CusSameOrg.mrk_flag._obj.element, "change", cheakMrk);
		cheakMrk();
	}
	//上市标志事件
	function cheakMrk(){
		var mrkFlag = CusSameOrg.mrk_flag._obj.element.value;
		if(mrkFlag=='1'){
			//当选择为上市时，上市地和股票代码必输
			//上市地
			CusSameOrg.mrl_area._obj._renderRequired(true);
			CusSameOrg.mrl_area._obj._renderReadonly(false);
			CusSameOrg.mrl_area._obj._renderHidden(false);
			//股票代码
			CusSameOrg.stock_no._obj._renderRequired(true);
			CusSameOrg.stock_no._obj._renderReadonly(false);
			CusSameOrg.stock_no._obj._renderHidden(false);
		}else if(mrkFlag=='2'){
			//当选择为未上市时，上市地和股票代码非必输且隐藏
			CusSameOrg.mrl_area._setValue("");
			CusSameOrg.mrl_area._obj._renderRequired(false);
			CusSameOrg.mrl_area._obj._renderReadonly(false);
			CusSameOrg.mrl_area._obj._renderHidden(true);

			CusSameOrg.stock_no._obj.element.value="";
			CusSameOrg.stock_no._obj._renderRequired(false);
			CusSameOrg.stock_no._obj._renderReadonly(false);
			CusSameOrg.stock_no._obj._renderHidden(true);
		}
	}

	function giveValueToCusNo(){
		CusSameOrg.cus_id._obj.element.value = CusSameOrg.same_org_no._obj.element.value;
		CusSameOrg.reg_no._obj.element.value = CusSameOrg.same_org_no._obj.element.value;
	}
	
	/*--user code end--*/
	function checkMrk(){
		var mrkFlag = CusSameOrg.mrk_flag._getValue();
		if(mrkFlag == 1){
			CusSameOrg.mrl_area._obj._renderHidden(false);
			CusSameOrg.stock_no._obj._renderHidden(false);
		}else{
			CusSameOrg.mrl_area._obj._renderHidden(true);
			CusSameOrg.stock_no._obj._renderHidden(true);
		}
	}

	//地址街道隐藏显示
	function checkCountry(){
		var comCountry = CusSameOrg.country._getValue();
		if(comCountry == 'CHN'){
			CusSameOrg.address_displayname._obj._renderRequired(true);
			CusSameOrg.street._obj._renderRequired(true);
			CusSameOrg.address_displayname._obj._renderHidden(false);
			CusSameOrg.street._obj._renderHidden(false);
		}else{
			CusSameOrg.address_displayname._obj._renderHidden(true);
			CusSameOrg.street._obj._renderHidden(true);
			CusSameOrg.address_displayname._obj._renderRequired(false);
			CusSameOrg.street._obj._renderRequired(false);
		}
	}

	function onReturnRegStateCode(date){
		CusSameOrg.address._obj.element.value=date.id;
		CusSameOrg.address_displayname._obj.element.value=date.label;
	}
	//主管客户经理
	function setconId(data){
		CusSameOrg.manager_id._setValue(data.actorno._getValue());
		CusSameOrg.manager_id_displayname._setValue(data.actorname._getValue());
		CusSameOrg.manager_br_id._setValue(data.orgid._getValue());
		CusSameOrg.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		CusSameOrg.manager_br_id_displayname._obj._renderReadonly(true);
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
					CusSameOrg.manager_br_id._setValue(jsonstr.org);
					CusSameOrg.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusSameOrg.manager_br_id._setValue("");
					CusSameOrg.manager_br_id_displayname._setValue("");
					CusSameOrg.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusSameOrg.manager_id._getValue();
					CusSameOrg.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusSameOrg.manager_br_id._setValue("");
					CusSameOrg.manager_br_id_displayname._setValue("");
					CusSameOrg.manager_br_id_displayname._obj._renderReadonly(false);
					CusSameOrg.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusSameOrg.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//主管机构
	function getOrgId(data){
		CusSameOrg.manager_br_id._setValue(data.organno._getValue());
		CusSameOrg.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function setUpOrgEst(data){
		CusSameOrg.up_org_no._setValue(data.bank_no._getValue());
		checkOrgNo();
	}
	function setHeadOrgEst(data){
		CusSameOrg.head_org_no._setValue(data.bank_no._getValue());
	}
	
	function setSameOrgNo(data){
		CusSameOrg.same_org_no._setValue(data.bank_no._getValue());
		CusSameOrg.same_org_cnname._setValue(data.bank_name._getValue());
		checkOrgNo();
	}

	function checkOrgNo(){
		var same_org_no = CusSameOrg.same_org_no._getValue();
		var up_org_no = CusSameOrg.up_org_no._getValue();
		if(same_org_no==up_org_no){
			alert("同业机构(行)号与上级行号相同！");
			CusSameOrg.same_org_no._setValue("");
			CusSameOrg.up_org_no._setValue("");
			CusSameOrg.same_org_cnname._setValue("");
		}
	}

	//校验组织机构代码
	function CheckComInsCode(){
		var certCode = CusSameOrg.com_ins_code._getValue();
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				CusSameOrg.com_ins_code._obj.element.value="";
				return false;
			}
		}
	};
	
	//开户并保存
	function doOpenCusSame(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.flag;
				if(operMsg=='success'){
		            alert('新增成功！');
		            doReturn();
				}else if(operMsg=='fail'){
					alert('该客户已经在本系统开户！');
					return;
				}else if(operMsg=='error'){
					alert('开户失败，请联系管理员！');
					return;
				}
			}
		};
		var handleFailure = function(o) {
			alert("开户失败，请联系管理员！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = CusSameOrg._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}

	//校验注册资金与实际到位资金
	function checkCapAmt(){
		var regCapAmt = CusSameOrg.reg_cap_amt._getValue();
		var paidCapAmt = CusSameOrg.paid_cap_amt._getValue();
		if(regCapAmt!=null&&regCapAmt!=''&&paidCapAmt!=null&&paidCapAmt!=''){
			if(parseFloat(paidCapAmt)-parseFloat(regCapAmt)>0){
				alert('实际到位资金不能大于注册/开办资金！');
				CusSameOrg.paid_cap_amt._setValue('');
			}
		}
	}
	//返回列表
	function doReturn() {
		var url = '<emp:url action="queryCusSameOrgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	//同业机构类型
	function onReturnOrgType(date){
		CusSameOrg.same_org_type._obj.element.value=date.id;
		CusSameOrg.same_org_type_displayname._obj.element.value=date.label;
	}
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="addCusSameOrgRecord.do" method="POST">
		<emp:gridLayout id="CusSameOrgGroup" title="金融同业客户" maxColumn="2">
		<%//    <emp:select id="CusSameOrg.same_org_type" label="同业机构类型" required="true" dictname="STD_ZB_INTER_BANK_ORG" /> %>
			<emp:pop id="CusSameOrg.same_org_type_displayname" label="同业机构类型" required="true" colSpan="2" cssElementClass="emp_field_cus_addr_input" url="showDicTree.do?dicTreeTypeId=STD_ZB_INTER_BANK_ORG" returnMethod="onReturnOrgType"/>
		    <emp:text id="CusSameOrg.same_org_type" label="同业机构类型" colSpan="2" hidden="true"/>
			<emp:select id="CusSameOrg.country" label="国别" required="true" dictname="STD_GB_2659-2000" onchange="checkCountry()" defvalue="CHN"/>
			<emp:text id="CusSameOrg.cus_id" label="客户码" readonly="true" hidden="true" />
			<emp:text id="CusSameOrg.com_ins_code" label="组织机构代码" maxlength="10" required="true" onchange="CheckComInsCode()"/>
			<emp:text id="CusSameOrg.swift_no" label="SWIFT编号" maxlength="35" required="false" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="CusSameOrg.same_org_no" label="同业机构(行)号" required="true" url="getPrdBankInfoPopList.do?restrictUsed=false&flag=add&status=1" returnMethod="setSameOrgNo"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			<emp:text id="CusSameOrg.same_org_enname" label="同业机构(行)英文名称" maxlength="40" required="false" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			<emp:text id="CusSameOrg.org_site" label="同业机构(行)网址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			
			<emp:text id="CusSameOrg.address" label="地址" required="false" hidden="true"/>
			<emp:pop id="CusSameOrg.address_displayname" label="地址" required="true" colSpan="2" cssElementClass="emp_field_cus_addr_input" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode"/>
			<emp:text id="CusSameOrg.street" label="街道" required="true" cssElementClass="emp_field_cus_addr_input" colSpan="2"/>
			<emp:date id="CusSameOrg.same_org_est" label="同业机构(行)成立日" required="false" onblur="CheckDate(CusSameOrg.same_org_est)"/>
			<emp:text id="CusSameOrg.bank_pro_lic" label="金融业务许可证" maxlength="80" required="true" />
			<emp:text id="CusSameOrg.com_ins_no" label="营业执照号码" maxlength="80" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="CusSameOrg.up_org_no" label="上级行号" required="false" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="setUpOrgEst" />
			<emp:pop id="CusSameOrg.head_org_no" label="总行行号" required="false" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="setHeadOrgEst" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:select id="CusSameOrg.reg_cur_type" label="注册/开办资金币种" required="false" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="CusSameOrg.reg_cap_amt" label="注册/开办资金(万元)" maxlength="18" required="false" dataType="Currency" onblur="checkCapAmt()" defvalue="0"/>
			<emp:text id="CusSameOrg.paid_cap_amt" label="实际到位资金(万元)" maxlength="18" required="true" dataType="Currency" onblur="checkCapAmt()" defvalue="0"/>
			<emp:text id="CusSameOrg.assets" label="总资产(万元)" maxlength="18" required="true" dataType="Currency" defvalue="0"/>
			<emp:select id="CusSameOrg.crd_grade" label="信用等级" required="false" dictname="STD_ZB_FINA_GRADE" readonly="true" defvalue="Z"/>
			<emp:date id="CusSameOrg.eval_maturity" label="评级到期日期" required="false" onblur="CheckExpDate(CusSameOrg.eval_maturity)" readonly="true"/>
			<emp:select id="CusSameOrg.cust_level" label="监管评级" required="true" dictname="STD_ZB_CUSTD_RATE"/>
			<emp:select id="CusSameOrg.mrk_flag" label="上市标志" required="true" dictname="STD_ZX_YES_NO" onchange="checkMrk()"/>
			<emp:select id="CusSameOrg.mrl_area" label="上市地" required="false" hidden="true" dictname="STD_ZX_LISTED"/>
			<emp:text id="CusSameOrg.stock_no" label="股票代码" maxlength="32" hidden="true" required="false" />
			<emp:text id="CusSameOrg.linkman_name" label="主联系人姓名" maxlength="40" required="true" />
			<emp:select id="CusSameOrg.linkman_duty" label="主联系人职务" required="true" dictname="STD_ZB_MANAGER_TYPE"/>
			<emp:text id="CusSameOrg.linkman_phone" label="主联系人电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="CusSameOrg.linkman_mobile_no" label="主联系人手机号" maxlength="20" required="true" dataType="Mobile"/>
			<emp:text id="CusSameOrg.linkman_email" label="电子邮箱" maxlength="80" required="true" dataType="Email"/>
			<emp:text id="CusSameOrg.linkman_fax" label="传真" maxlength="35" required="true" dataType="Phone"/>
			<emp:select id="CusSameOrg.rel_dgr" label="与我行合作关系" required="true" dictname="STD_ZB_CUS_BANK_CO"/>
		</emp:gridLayout>	
		<emp:gridLayout id="CusSameOrgGroup2" title="登记信息" maxColumn="2">
			<emp:pop id="CusSameOrg.manager_id_displayname" label="主管客户经理" required="false" hidden="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusSameOrg.manager_br_id_displayname" label="主管机构" required="false" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgId" readonly="true"/>
			<emp:text id="CusSameOrg.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
			<emp:text id="CusSameOrg.manager_id" label="主管客户经理" required="false" hidden="true"/>
			<emp:text id="CusSameOrg.manager_br_id" label="主管机构" required="false" hidden="true"/>
			<emp:text id="CusSameOrg.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true" readonly="true"/>
			<emp:text id="CusSameOrg.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" hidden="false" readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="openCusSame" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

