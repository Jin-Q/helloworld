<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function onReturn(data){
		//这个POP是直接从cus_com表中查询出来的，而不是从信贷关系表中查询出来的POP
		CusGrpInfo.parent_cus_id._setValue(data.cus_id._getValue());
		CusGrpInfo.parent_cus_name._setValue(data.cus_name._getValue());
		CusGrpInfo.parent_org_code._setValue(data.cert_code._getValue());
		CusGrpInfo.parent_loan_card._setValue(data.loan_card_id._getValue());	
		CusGrpInfo.manager_br_id._setValue(data.main_br_id._getValue());
		CusGrpInfo.manager_id._setValue(data.cust_mgr._getValue());
		checkExistInGrpMember(data.cus_id._getValue());
	}
	function getCusManager(data){

		CusGrpInfo.manager_br_id._setValue(data.actorno._getValue());
	}
	function checkExistInGrpMember(cus_id){
		var url = "<emp:url  action='checkExistInGrpMember.do'/>&cus_id="+cus_id;
        var callback = {
                success : "doReturnMethod",
                isJSON : true
        };
        EMPTools.ajaxRequest('GET', url, callback);
	}
	function doReturnMethod(json,callback){
		var obj = eval(json);
	    var flag = obj.flag;
	    if(flag=="true"){
	        CusGrpInfo.parent_cus_id._setValue("");
	        CusGrpInfo.parent_cus_name._setValue("");
	        CusGrpInfo.parent_org_code._setValue("");
	        CusGrpInfo.parent_loan_card._setValue("");   
	        alert("该客户已存在于别的集团中,不允许作为集团母公司!");
		}
	}
	function doload(){

		CusGrpInfo.manager_id._obj.config.url=CusGrpInfo.manager_id._obj.config.url+"&queryCondition=state='1'&returnMethod=getCusManager";
		CusGrpInfo.parent_cus_id._obj.config.url=CusGrpInfo.parent_cus_id._obj.config.url+"&CusCom.cus_status=1&returnMethod=onReturn&actorright=A";
		
	}
	function mainBrId(data){
		CusGrpInfo.main_br_id._setValue(data.organno._getValue());
	}

	function doReturn(){
		var paramStr="CusGrpInfo.cus_manager="+CusGrpInfo.cus_manager._obj.element.value;
		var stockURL = '<emp:url action="queryCusGrpInfoList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCusGrpInfoRecord.do" method="POST">
		<emp:gridLayout id="CusGrpInfoGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" maxlength="60" required="true" />
			<emp:pop id="CusGrpInfo.parent_cus_id" label="主关联(集团)客户码" url="queryCusComPopListforGrp.do?CusCom.cus_status=20"  required="true" />
			<emp:text id="CusGrpInfo.parent_cus_name" label="主关联(集团)客户名称" maxlength="60" required="true"  readonly="true"/>
			<emp:text id="CusGrpInfo.parent_org_code" label="主关联(集团)组织机构代码" maxlength="10" required="true"  readonly="true"/>
			<emp:text id="CusGrpInfo.parent_loan_card" label="主关联(集团)贷款卡编码" maxlength="16" required="false"  readonly="true"/>
			<emp:select id="CusGrpInfo.grp_finance_type" label="关联(集团)融资形式" required="false" hidden="true" dictname="STD_ZB_GRP_FIN_TYPE"/>
			<emp:textarea id="CusGrpInfo.grp_detail" label="关联(集团)情况说明" maxlength="250" required="false" colSpan="2" />
			<emp:pop id="CusGrpInfo.manager_br_id" label="主办行" url="getValuequerySOrgList.do" returnMethod="mainBrId" hidden="false" readonly="true"/>
			<emp:pop id="CusGrpInfo.manager_id" label="主办客户经理" url="querySUserPopList.do"  hidden="false" readonly="true"/>
			<emp:text id="CusGrpInfo.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="false"/>
			<emp:text id="CusGrpInfo.input_date" label="登记日期" maxlength="10" required="true"  defvalue="$OPENDAY" hidden="false"/>
			<emp:text id="CusGrpInfo.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="false"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
