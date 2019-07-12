<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	//返回主管客户经理	
	function setconId(data){
		IqpAppOverseeOrg.manager_id._setValue(data.actorno._getValue());
		IqpAppOverseeOrg.manager_id_displayname._setValue(data.actorname._getValue());
		IqpAppOverseeOrg.manager_br_id._setValue(data.orgid._getValue());
		IqpAppOverseeOrg.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpAppOverseeOrg.manager_br_id_displayname._obj._renderReadonly(true);
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
					IqpAppOverseeOrg.manager_br_id._setValue(jsonstr.org);
					IqpAppOverseeOrg.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpAppOverseeOrg.manager_br_id._setValue("");
					IqpAppOverseeOrg.manager_br_id_displayname._setValue("");
					IqpAppOverseeOrg.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpAppOverseeOrg.manager_id._getValue();
					IqpAppOverseeOrg.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpAppOverseeOrg.manager_br_id._setValue("");
					IqpAppOverseeOrg.manager_br_id_displayname._setValue("");
					IqpAppOverseeOrg.manager_br_id_displayname._obj._renderReadonly(false);
					IqpAppOverseeOrg.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpAppOverseeOrg.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		IqpAppOverseeOrg.manager_br_id._setValue(data.organno._getValue());
		IqpAppOverseeOrg.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	function returnCus(data){
		IqpAppOverseeOrg.oversee_org_id._setValue(data.cus_id._getValue());
		IqpAppOverseeOrg.oversee_org_id_displayname._setValue(data.cus_name._getValue());
	}
	
	function doUpdateIqpAppOverseeOrg(){
		var form = document.getElementById("submitForm");
		IqpAppOverseeOrg._checkAll();
		if(IqpAppOverseeOrg._checkAll()){
			IqpAppOverseeOrg._toForm(form);
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
						alert("修改成功！");
						var url = '<emp:url action="queryIqpAppOverseeOrgList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改失败！");
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

	function doReturn(){
		var oversee_org_id = IqpAppOverseeOrg.oversee_org_id._getValue();
		var url = '<emp:url action="queryIqpAppOverseeOrgList.do"/>&oversee_org_id='+oversee_org_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function doLoad(){
		document.getElementById("main_tabs").href="javascript:reLoad()";
		changeOslimit();
		Change();
		IqpAppOverseeOrg.com_scale._obj.addOneButton('uniquCheck', '测算',doMeasure);
	}
	function doMeasure(){
		var cus_id = IqpAppOverseeOrg.oversee_org_id._obj.element.value;
		var url = '<emp:url action="getOverseeOrgSizeOp.do"/>&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var out_flag = jsonstr.out_flag;
				var out_msg = jsonstr.out_msg;
				var com_scale = jsonstr.com_scale;
				if("N"==out_flag || "n"==out_flag){
					alert(out_msg);
					IqpAppOverseeOrg.com_scale._setValue('30');
				}else{
					alert("测算成功！");
					IqpAppOverseeOrg.com_scale._setValue(com_scale);
				}
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	function reLoad(){
		window.location.reload();
	}

	function changeOslimit(){
		var is_build_oslimit = IqpAppOverseeOrg.is_build_oslimit._getValue();
		if(is_build_oslimit=="1"){
			IqpAppOverseeOrg.defray_limit._obj._renderHidden(false);
			IqpAppOverseeOrg.limit_start_date._obj._renderHidden(false);
			IqpAppOverseeOrg.limit_over_date._obj._renderHidden(false);
			IqpAppOverseeOrg.defray_limit._obj._renderRequired(true);
			IqpAppOverseeOrg.limit_start_date._obj._renderRequired(true);
			IqpAppOverseeOrg.limit_over_date._obj._renderRequired(true);
		}else{
			IqpAppOverseeOrg.defray_limit._obj._renderHidden(true);
			IqpAppOverseeOrg.limit_start_date._obj._renderHidden(true);
			IqpAppOverseeOrg.limit_over_date._obj._renderHidden(true);
			IqpAppOverseeOrg.defray_limit._obj._renderRequired(false);
			IqpAppOverseeOrg.limit_start_date._obj._renderRequired(false);
			IqpAppOverseeOrg.limit_over_date._obj._renderRequired(false);
		}
	}

	function CheckEndDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		if(end!=null && end!="" ){
			var ff = CheckDate1BeforeDate2(end,start);
			if(ff){
				alert("合作到期日大于等于合作起始日！");
				date2._obj.element.value="";
			}
		}
	}

	function CheckOverDate(date1,date2){
		var start = date1._obj.element.value;
		var end = date2._obj.element.value;
		if(end!=null && end!="" ){
			var ff = CheckDate1BeforeDate2(end,start);
			if(ff){
				alert("额度结束日期大于等于额度开始日期！");
				date2._obj.element.value="";
			}
		}
	}

	function checkBeginDate(){
		var Begin = IqpAppOverseeOrg.begin_date._getValue();//合作起始日
		var openDay='${context.OPENDAY}';
		if(Begin!=null && Begin!="" ){
			var flag = CheckDate1BeforeDate2(Begin,openDay);
			if(!flag){
				alert("合作起始日期要小于等于当前日期！");
				IqpAppOverseeOrg.begin_date._setValue("");
			}
		}
	}
	function Change(){
		var flag = IqpAppOverseeOrg.oversee_way._getValue();//监管方式
		if("01"==flag){//动态监管时，显示最低核准价值
			IqpAppOverseeOrg.low_auth_value._obj._renderHidden(false);
		    IqpAppOverseeOrg.low_auth_value._obj._renderRequired(true);
		}else if("02"==flag){
			IqpAppOverseeOrg.low_auth_value._obj._renderHidden(true);
		    IqpAppOverseeOrg.low_auth_value._obj._renderRequired(false);
		    IqpAppOverseeOrg.low_auth_value._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateIqpAppOverseeOrgRecord.do" method="POST">
				<emp:gridLayout id="IqpAppOverseeOrgGroup" title="监管机构管理" maxColumn="2">
					<emp:text id="IqpAppOverseeOrg.serno" label="业务流水号" maxlength="40" required="false" colSpan="2" hidden="true"/>
					<emp:pop id="IqpAppOverseeOrg.oversee_org_id" label="监管机构编号" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus"
								popParam="width=700px,height=650px" required="true" readonly="true" />
					<emp:text id="IqpAppOverseeOrg.oversee_org_id_displayname" label="监管机构名称"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" />
					<emp:text id="IqpAppOverseeOrg.oversee_org_addr" label="监管机构地址" required="false" hidden="true" readonly="true" />
					<emp:text id="IqpAppOverseeOrg.oversee_org_addr_displayname" label="监管机构地址" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:text id="IqpAppOverseeOrg.oversee_org_street" label="街道" required="false" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true" />			
					<emp:text id="IqpAppOverseeOrg.legal" label="法人代表" maxlength="30" required="false" readonly="true" hidden="true" colSpan="2" />
					<emp:text id="IqpAppOverseeOrg.legal_displayname" label="法人代表"   required="true" readonly="true" />
					<emp:select id="IqpAppOverseeOrg.orgmodal" label="组织形式" required="true" dictname="STD_ZB_HOLD_TYPE" readonly="true" />
					<emp:text id="IqpAppOverseeOrg.belg_grp" label="所属集团" maxlength="80" readonly="true" />
					<emp:text id="IqpAppOverseeOrg.regi_cap" label="注册资金(万元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
					<emp:date id="IqpAppOverseeOrg.build_date" label="成立日期" required="true" readonly="true" />
					<emp:select id="IqpAppOverseeOrg.com_scale" label="监管规模" required="true" dictname="STD_ZB_ENTERPRISE" readonly="true" />
					<emp:select id="IqpAppOverseeOrg.is_busnes_con" label="是否为贸易企业" required="true" dictname="STD_ZX_YES_NO" />
					<emp:text id="IqpAppOverseeOrg.other_oper_pro" label="其他经营项目" maxlength="10" required="true" />
					<emp:select id="IqpAppOverseeOrg.is_parmachin" label="是否附带加工" required="true" dictname="STD_ZX_YES_NO" />
					<emp:text id="IqpAppOverseeOrg.storage_volume" label="仓库容量(吨)" maxlength="10" required="true" dataType="Int" />
					<emp:text id="IqpAppOverseeOrg.transfer_abi_y" label="年吞吐量(吨)" maxlength="10" required="true" dataType="Int" />			
					<emp:text id="IqpAppOverseeOrg.openairsqu" label="露天面积(m²)" maxlength="20" required="true" dataType="Int" />
					<emp:text id="IqpAppOverseeOrg.storage_chrg_normal" label="仓储收费标准" maxlength="18" required="true" />
					<emp:select id="IqpAppOverseeOrg.place_util_case" label="场地使用权情况" required="true" dictname="STD_ZB_STORE_CHA" />
					<emp:select id="IqpAppOverseeOrg.con_trade_stats" label="企业行业地位" required="true" dictname="STD_ZB_COM_HD_ENTER" readonly="true" />
					<emp:select id="IqpAppOverseeOrg.trade_stand_status" label="行业信誉状况" required="true" dictname="STD_MAINTAIN_STATUS" />
					<emp:text id="IqpAppOverseeOrg.oversee_biz_chief" label="监管业务负责人" maxlength="10" required="true" />
					<emp:text id="IqpAppOverseeOrg.link_mode" label="联系方式" maxlength="20" required="true" dataType="Phone" />
					<emp:textarea id="IqpAppOverseeOrg.oper_range" label="经营范围" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.now_main_biz_oper" label="现主要业务及经营状况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.store_occlude_modal" label="仓库封闭形式" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.person_iostore_proce" label="人员出入库手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.goods_iostore_proce" label="货物出入库手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.goods_test_proce" label="货物检、化验手续" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.imn_is_espec_flow" label="质押监管融资是否有特别流程" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.bkloan_outguar_case" label="银行借款及对外担保情况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.his_record" label="历史资信记录" maxlength="60" required="false" colSpan="2"/>
					<emp:textarea id="IqpAppOverseeOrg.alcoopbk_cpyear" label="已合作货押监管的银行及合作年限记录" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.his_coop_case" label="我行历史合作情况" maxlength="60" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.cpwish_advice" label="仓储单位监管能力、与我行的监管合作意愿及对我行动产融资业务的建议" required="false" colSpan="2" />
					<emp:textarea id="IqpAppOverseeOrg.other_indgt_case" label="其他调查情况" maxlength="60" required="false" colSpan="2" />
					<emp:text id="IqpAppOverseeOrg.cdt_eval" label="信用评级" maxlength="3" required="false" disabled="true" hidden="true"/>
					<emp:date id="IqpAppOverseeOrg.eval_time" label="评估日期" required="false" disabled="true" hidden="true"/>			
					<emp:checkbox id="IqpAppOverseeOrg.oversee_mode" label="监管模式" required="true" dictname="STD_ZB_OVERSEE_MODE" />
					<emp:checkbox id="IqpAppOverseeOrg.opr_mode" label="操作模式" required="true" dictname="STD_ZB_OPR_MODE" />
					<emp:select id="IqpAppOverseeOrg.is_build_oslimit" label="是否设立监管额度" required="true" dictname="STD_ZX_YES_NO" onchange="changeOslimit()" />
					<emp:text id="IqpAppOverseeOrg.defray_limit" label="最高监管额度" maxlength="18" dataType="Currency" hidden="true" />
					<emp:date id="IqpAppOverseeOrg.limit_start_date" label="额度开始日期" defvalue="$OPENDAY" readonly="true" hidden="true" />
					<emp:date id="IqpAppOverseeOrg.limit_over_date" label="额度结束日期" onblur="checkOverDate(IqpAppOverseeOrg.limit_start_date,IqpAppOverseeOrg.limit_over_date)" hidden="true" />
					<emp:select id="IqpAppOverseeOrg.oversee_way" label="监管方式" required="true" dictname="STD_ZB_OVERSEE_TYPE" onchange="Change()"/>
					<emp:text id="IqpAppOverseeOrg.low_auth_value" label="最低核准价值" maxlength="18" required="true" dataType="Currency" />			
					<emp:date id="IqpAppOverseeOrg.begin_date" label="合作起始日" required="false" defvalue="${context.OPENDAY}" readonly="false" onblur="checkBeginDate()"/>
					<emp:date id="IqpAppOverseeOrg.end_date" label="合作到期日" required="true" onblur="CheckEndDate(IqpAppOverseeOrg.begin_date,IqpAppOverseeOrg.end_date)"/>
					<emp:select id="IqpAppOverseeOrg.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"  hidden="true"/>			
					<emp:select id="IqpAppOverseeOrg.flow_type" label="流程类型" required="false" dictname="STD_ZB_FLOW_TYPE" defvalue="01" hidden="true" />			
				</emp:gridLayout>
				<emp:gridLayout id="IqpAppOverseeOrgGroup" title="登记信息" maxColumn="2">
					<emp:pop id="IqpAppOverseeOrg.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
					<emp:pop id="IqpAppOverseeOrg.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
					<emp:text id="IqpAppOverseeOrg.manager_id" label="责任人" required="false" hidden="true" />
					<emp:text id="IqpAppOverseeOrg.manager_br_id" label="责任机构" required="false" hidden="true" />
					<emp:text id="IqpAppOverseeOrg.input_id_displayname" label="登记人"   required="true" readonly="true" defvalue="$currentUserName" />
					<emp:text id="IqpAppOverseeOrg.input_br_id_displayname" label="登记机构"   required="true" readonly="true" defvalue="$organName" />
					<emp:text id="IqpAppOverseeOrg.input_id" label="登记人" maxlength="10" required="false" hidden="true" defvalue="$currentUserId" />
					<emp:text id="IqpAppOverseeOrg.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
					<emp:date id="IqpAppOverseeOrg.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true" />			
				</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateIqpAppOverseeOrg" label="修改" op="update"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
