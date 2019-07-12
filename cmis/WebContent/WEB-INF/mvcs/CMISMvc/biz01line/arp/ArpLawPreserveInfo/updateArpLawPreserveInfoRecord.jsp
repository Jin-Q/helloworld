<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		url = '<emp:url action="queryArpLawPreserveInfoList.do"/>?case_no='+ArpLawPreserveInfo.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doSubmits(){
		doPubUpdate(ArpLawPreserveInfo);
	};
	/***查封状态控制 ***/
	function checkseal(){
		seal_status = ArpLawPreserveInfo.seal_status._getValue();
		
		if(seal_status == '002'){
			ArpLawPreserveInfo.waiting_amt._obj._renderHidden(false);
			ArpLawPreserveInfo.waiting_amt._obj._renderRequired(true);
		}else{
			ArpLawPreserveInfo.waiting_amt._setValue('');
			ArpLawPreserveInfo.waiting_amt._obj._renderHidden(true);
			ArpLawPreserveInfo.waiting_amt._obj._renderRequired(false);
		}
	};

	/** 登记信息begin **/
	function setconId(data){
		ArpLawPreserveInfo.manager_id_displayname._setValue(data.actorname._getValue());
		ArpLawPreserveInfo.manager_id._setValue(data.actorno._getValue());
		ArpLawPreserveInfo.manager_br_id._setValue(data.orgid._getValue());
		ArpLawPreserveInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		ArpLawPreserveInfo.manager_br_id_displayname._obj._renderReadonly(true);
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
					ArpLawPreserveInfo.manager_br_id._setValue(jsonstr.org);
					ArpLawPreserveInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					ArpLawPreserveInfo.manager_br_id._setValue("");
					ArpLawPreserveInfo.manager_br_id_displayname._setValue("");
					ArpLawPreserveInfo.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpLawPreserveInfo.manager_id._getValue();
					ArpLawPreserveInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpLawPreserveInfo.manager_br_id._setValue("");
					ArpLawPreserveInfo.manager_br_id_displayname._setValue("");
					ArpLawPreserveInfo.manager_br_id_displayname._obj._renderReadonly(false);
					ArpLawPreserveInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpLawPreserveInfo.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		ArpLawPreserveInfo.manager_br_id._setValue(data.organno._getValue());
		ArpLawPreserveInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	
	/****** 各种日期校验 *******/
	function checkDate(obj){
		if(obj.value){
			var seal_start_date = ArpLawPreserveInfo.seal_start_date._getValue();	//查封起始日期
			var seal_end_date = ArpLawPreserveInfo.seal_end_date._getValue();	//查封到期日期

			if(obj.name == 'ArpLawPreserveInfo.seal_start_date'){
				if(seal_end_date <= seal_start_date && seal_end_date != ''){
					obj.value = '';
					alert("[查封起始日期]应该小于[查封到期日期]");
		   		}
			}
			if(obj.name == 'ArpLawPreserveInfo.seal_end_date'){
				if(seal_start_date != '' && seal_end_date <= seal_start_date ){
					obj.value = '';
					alert("[查封到期日期]应该大于[查封起始日期]");
		   		}
			}
		}		
	};
	function doLoad(){
		checkseal();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateArpLawPreserveInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpLawPreserveInfoGroup" title="保全信息" maxColumn="2">
			<emp:text id="ArpLawPreserveInfo.serno" label="流水号" maxlength="40" hidden="true" colSpan="2"/>
			<emp:text id="ArpLawPreserveInfo.case_no" label="案件编号" maxlength="40" required="true" readonly="true" />			
			<emp:text id="ArpLawPreserveInfo.preserve_writ_no" label="保全裁定文书号" maxlength="80" required="true" />
			<emp:select id="ArpLawPreserveInfo.preserve_asset_type" label="保全资产类型" required="true" dictname="STD_ZB_PRESERVE_TYPE" />
			<emp:text id="ArpLawPreserveInfo.preserve_asset_name" label="保全资产名称" maxlength="80" required="true" />			
			<emp:text id="ArpLawPreserveInfo.preserve_asset_unit" label="保全资产计量单位" maxlength="20" required="false" />
			<emp:text id="ArpLawPreserveInfo.preserve_asset_amount" label="保全资产数量" maxlength="16" required="false" dataType="Int" />
			<emp:select id="ArpLawPreserveInfo.preserve_asset_status" label="保全资产状态" required="true" dictname="STD_ZB_PRESERVE_STATUS" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawPreserveInfoGroup" maxColumn="2" title="查封信息">
			<emp:select id="ArpLawPreserveInfo.seal_type" label="查封类型" required="true" dictname="STD_ZB_SEAL_TYPE" colSpan="2"/>
			<emp:select id="ArpLawPreserveInfo.seal_status" label="查封状态" required="true" dictname="STD_ZB_SEAL_STATUS" onchange="checkseal()"/>
			<emp:text id="ArpLawPreserveInfo.waiting_amt" label="轮候金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawPreserveInfo.other_seal_case" label="其他债权人查封情况" maxlength="250" required="false" />
			<emp:text id="ArpLawPreserveInfo.deblocking_case" label="解封情况" maxlength="250" required="false" />
			<emp:date id="ArpLawPreserveInfo.seal_start_date" label="查封起始日期" required="true" onblur="checkDate(this)"/>
			<emp:date id="ArpLawPreserveInfo.seal_end_date" label="查封到期日期" required="true" onblur="checkDate(this)"/>
			<emp:text id="ArpLawPreserveInfo.seal_writ_signer" label="查封文书签发人" maxlength="20" required="true" />
			<emp:text id="ArpLawPreserveInfo.seal_writ_poster" label="查封文书送达人" maxlength="20" required="true" />
			<emp:textarea id="ArpLawPreserveInfo.seal_writ_content" label="查封文书内容" maxlength="250" required="true" colSpan="2" />
			<emp:text id="ArpLawPreserveInfo.seal_rcv_case" label="查封受理机构签收情况" maxlength="20" required="true" colSpan="2"/>
			<emp:text id="ArpLawPreserveInfo.seal_rcv_org" label="查封受理机构" maxlength="100" required="true" />
			<emp:text id="ArpLawPreserveInfo.seal_rcv_person" label="查封受理机构签收人" maxlength="20" required="true" />						
			<emp:textarea id="ArpLawPreserveInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawPreserveInfoGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpLawPreserveInfo.manager_id_displayname" label="主管客户经理" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpLawPreserveInfo.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="true"/>
			<emp:text id="ArpLawPreserveInfo.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpLawPreserveInfo.manager_id" label="主管客户经理" required="true" hidden="true"  />
			<emp:text id="ArpLawPreserveInfo.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpLawPreserveInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="ArpLawPreserveInfo.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpLawPreserveInfo.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpLawPreserveInfo.input_date" label="登记日期" required="true"   readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
