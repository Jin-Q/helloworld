<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 600;
	height: 60;
};
.emp_field_text_readonly {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 240px;
};
</style>
<script type="text/javascript">

	/*--user code begin--*/
	
	function getOrganName(data){
		LmtIndusApply.belg_org._setValue(data[0]);
		LmtIndusApply.belg_org_displayname._setValue(data[1]);
	}
	function setconId(data){
		LmtIndusApply.manager_id_displayname._setValue(data.actorname._getValue());
		LmtIndusApply.manager_id._setValue(data.actorno._getValue());
		LmtIndusApply.manager_br_id._setValue(data.orgid._getValue());
		LmtIndusApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtIndusApply.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtIndusApply.manager_br_id._setValue(jsonstr.org);
					LmtIndusApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtIndusApply.manager_br_id._setValue("");
					LmtIndusApply.manager_br_id_displayname._setValue("");
					LmtIndusApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtIndusApply.manager_id._getValue();
					LmtIndusApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtIndusApply.manager_br_id._setValue("");
					LmtIndusApply.manager_br_id_displayname._setValue("");
					LmtIndusApply.manager_br_id_displayname._obj._renderReadonly(false);
					LmtIndusApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtIndusApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		LmtIndusApply.manager_br_id._setValue(data.organno._getValue());
		LmtIndusApply.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function getBlnOrg(data){
		LmtIndusApply.belg_org._setValue(data.organno._getValue());
		LmtIndusApply.belg_org_displayname._setValue(data.organname._getValue());
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/****** 单户限额与行业总额校验 *******/
	function checkAmt(obj){
		var indus_amt = LmtIndusApply.indus_amt._getValue();
		var single_amt = LmtIndusApply.single_amt._getValue();
		if( (indus_amt - single_amt < 0)  && indus_amt != "" && single_amt != ""){
			alert("单户限额不得大于行业总额");
			obj.value = "";
		}
	};
	/****** 共享范围与所属机构控制 *******/
	function controlOrg(obj){
		if(obj.value == 2){
			LmtIndusApply.belg_org_displayname._obj._renderHidden(false);
			LmtIndusApply.belg_org._obj._renderHidden(false);
			LmtIndusApply.belg_org._obj._renderRequired(true);
		}else{
			LmtIndusApply.belg_org_displayname._obj._renderHidden(true);
			LmtIndusApply.belg_org._obj._renderHidden(true);	
			LmtIndusApply.belg_org._obj._renderRequired(false);
			LmtIndusApply.belg_org_displayname._setValue('');
			LmtIndusApply.belg_org._setValue('');	
		}
	};
	
	function afterSucFun(){
        var url = '<emp:url action="getLmtIndusApplyUpdatePage.do"/>&serno=${context.LmtIndusApply.serno}&action=update';
        url = EMPTools.encodeURI(url);
		window.location = url;
    };

  //设置产品返回 
	function setProds(data){
		LmtIndusApply.suit_prd._setValue(data[0]);
		LmtIndusApply.suit_prd_displayname._setValue(data[1]);
	};
	//行业类型唯一校验
	function checkIndusType(obj){
		var indusType = obj.value;
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

				}else {
					alert("此行业已存在授信!");
					obj.value = '';
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
		var url="<emp:url action='checkUniqueType.do'/>&type=indusType&value="+indusType;
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	}

	function doAddLmtIndusApply(){
		var form = document.getElementById("submitForm");
		if(LmtIndusApply._checkAll()){
			LmtIndusApply._toForm(form);
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
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="getLmtIndusApplyUpdatePage.do"/>?op=update&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
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
			var agr_no = "${context.agr_no}";
			var url = '<emp:url action="addLmtIndusApplyRecord.do"/>?agr_no='+agr_no;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		}else {
			return false;
		}
	};
	function doLoad(){
		menuId = '${context.menuId}';
		if(menuId == 'indus_crd_change'){
			LmtIndusApply.indus_type._obj._renderReadonly(true);
			LmtIndusApply.change_list_flag._obj._renderHidden(false);
		}
		
		var obj = LmtIndusApply.shared_scope._obj;
		if(obj.value == 2){
			LmtIndusApply.belg_org_displayname._obj._renderHidden(false);
			LmtIndusApply.belg_org._obj._renderHidden(false);
			LmtIndusApply.belg_org._obj._renderRequired(true);
		}else{
			LmtIndusApply.belg_org_displayname._obj._renderHidden(true);
			LmtIndusApply.belg_org._obj._renderHidden(true);
			LmtIndusApply.belg_org._obj._renderRequired(false);
		}

		var change_list_flag = LmtIndusApply.change_list_flag._getValue();
		if(menuId == 'indus_crd_change' && change_list_flag == '1'){
			LmtIndusApply.shared_scope._obj._renderReadonly(true);
			LmtIndusApply.belg_org._obj._renderReadonly(true);
			LmtIndusApply.indus_amt._obj._renderReadonly(true);
			LmtIndusApply.single_amt._obj._renderReadonly(true);
			LmtIndusApply.crd_term_type._obj._renderReadonly(true);
			LmtIndusApply.crd_term._obj._renderReadonly(true);
			LmtIndusApply.is_list_mana._obj._renderReadonly(true);
			LmtIndusApply.manager_id_displayname._obj._renderReadonly(true);
			LmtIndusApply.manager_br_id_displayname._obj._renderReadonly(true);
			LmtIndusApply.memo._obj._renderReadonly(true);
		}

	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addLmtIndusApplyRecord.do" method="POST">
		<emp:gridLayout id="LmtIndusApplyGroup" title="行业授信申请信息" maxColumn="2">
			<emp:text id="LmtIndusApply.serno" label="业务编号" maxlength="40"   required="false"  readonly="true" cssElementClass="emp_field_common_serno" hidden="true"/>
			<emp:select id="LmtIndusApply.indus_type" label="行业分类" required="true" dictname="STD_ZB_INDUS_TYPE" onchange="checkIndusType(this)" />
			<emp:select id="LmtIndusApply.shared_scope" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="controlOrg(this)"/>
			<emp:pop id="LmtIndusApply.belg_org" label="所属机构"  
			url="queryMultiSOrgPop.do" returnMethod="getOrganName"  required="false"  hidden="true"/>
			<emp:textarea id="LmtIndusApply.belg_org_displayname" label="所属机构"  required="false"  readonly="true" hidden="true"/>
			<emp:pop id="LmtIndusApply.suit_prd" label="适用产品" url='showPrdCheckTreeDetails.do?bizline=BL100,BL200' 
			returnMethod="setProds" required="true" colSpan="2" />
			<emp:textarea id="LmtIndusApply.suit_prd_displayname" label="适用产品名称" readonly="true"  colSpan="2" />
			<emp:select id="LmtIndusApply.cur_type" label="授信币种" required="true" readonly="true"  dictname="STD_ZX_CUR_TYPE" defvalue="CNY" colSpan="2" />
			<emp:text id="LmtIndusApply.indus_amt" label="行业总额(元)" maxlength="18" required="true" dataType="Currency" defvalue="0.00"  onblur="checkAmt(this)"/>
			<emp:text id="LmtIndusApply.single_amt" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" defvalue="0.00"  onblur="checkAmt(this)"/>
			<emp:date id="LmtIndusApply.start_date" label="申请日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:date id="LmtIndusApply.end_date" label="办结日期" required="false" hidden="true"/>
			<emp:select id="LmtIndusApply.crd_term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtIndusApply.crd_term" label="授信期限" maxlength="5" required="true" dataType="Int"/>
			<emp:select id="LmtIndusApply.is_list_mana" label="是否名单制管理" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtIndusApply.change_list_flag" label="是否仅变更名单" readonly="true" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="LmtIndusApply.flow_type" label="流程类型" required="false" dictname="STD_ZB_FLOW_TYPE" defvalue="01" hidden="true"/>
			<emp:textarea id="LmtIndusApply.memo" label="备注" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)" />
			<emp:select id="LmtIndusApply.apply_type" label="申请类型" required="true" dictname="STD_ZB_LMT_APPLY_TYPE" hidden="true" defvalue="001"/>
		</emp:gridLayout>
				
		<emp:gridLayout id="LmtIndusApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="LmtIndusApply.manager_id_displayname" label="责任人" required="true" readonly="false"
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="LmtIndusApply.manager_br_id_displayname" label="责任机构"  required="true"  
			 url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="LmtIndusApply.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="LmtIndusApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true" defvalue="$organName" />
			<emp:text id="LmtIndusApply.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY" />	
			<emp:select id="LmtIndusApply.approve_status" label="审批状态" required="true" 
			dictname="WF_APP_STATUS" defvalue="000" readonly="true" />
			<emp:text id="LmtIndusApply.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="LmtIndusApply.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
			<emp:text id="LmtIndusApply.input_id" label="登记人" maxlength="20" readonly="true" required="true"  hidden="true" defvalue="$currentUserId"/>
			<emp:text id="LmtIndusApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" defvalue="$organNo" hidden="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addLmtIndusApply" label="确定"  />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>