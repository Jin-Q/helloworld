<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function onReturn(data){
		//变更取值
		CusGrpInfoApply.parent_cus_id._setValue(data.parent_cus_id._getValue());
		CusGrpInfoApply.parent_cus_name._setValue(data.parent_cus_name._getValue());
		CusGrpInfoApply.parent_org_code._setValue(data.parent_org_code._getValue());
		CusGrpInfoApply.parent_loan_card._setValue(data.parent_loan_card._getValue());	
		CusGrpInfoApply.grp_no._setValue(data.grp_no._getValue());
		CusGrpInfoApply.grp_name._setValue(data.grp_name._getValue());
		CusGrpInfoApply.grp_finance_type._setValue(data.grp_finance_type._getValue());
		CusGrpInfoApply.grp_cus_type._setValue(data.grp_cus_type._getValue());		
		CusGrpInfoApply.grp_detail._setValue(data.grp_detail._getValue());
		CusGrpInfoApply.manager_br_id._setValue(data.manager_br_id._getValue());
		CusGrpInfoApply.manager_id._setValue(data.manager_id._getValue());
		CusGrpInfoApply.grp_detail._setValue(data.grp_detail._getValue());
		CusGrpInfoApply.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		CusGrpInfoApply.manager_id_displayname._setValue(data.manager_id_displayname._getValue());
		checkExistInGrpMember(data.parent_cus_id._getValue());
	}

	function checkExistInGrpMember(cus_id){
		var url = "<emp:url  action='checkExistInGrpMemberApply.do'/>&cus_id="+cus_id;
        var callback = {
            success : "doReturnMethod",
            isJSON : true
        };
        EMPTools.ajaxRequest('GET', url, callback);
	}
	
	function doReturnMethod(json,callback){
		var obj = eval(json);
	    var flag = obj.flag;
	    if(flag == "sq"){
			CusGrpInfoApply.parent_cus_id._setValue("");
			CusGrpInfoApply.parent_cus_name._setValue("");
			CusGrpInfoApply.parent_org_code._setValue("");
			CusGrpInfoApply.parent_loan_card._setValue("");	
			CusGrpInfoApply.grp_no._setValue("");
			CusGrpInfoApply.grp_name._setValue("");
			CusGrpInfoApply.grp_finance_type._setValue("");
			CusGrpInfoApply.grp_detail._setValue("");
			CusGrpInfoApply.manager_br_id._setValue("");
			CusGrpInfoApply.manager_id._setValue("");
			CusGrpInfoApply.grp_detail._setValue("");
			CusGrpInfoApply.manager_br_id_displayname._setValue("");
			CusGrpInfoApply.manager_id_displayname._setValue("");
	        alert("该客户已存在于变更申请中!");
		}
	}
	
	function doload(){
		CusGrpInfoApply.manager_id._obj.config.url=CusGrpInfoApply.manager_id._obj.config.url+"&queryCondition=state='1'&returnMethod=getCusManager";
		CusGrpInfoApply.grp_no._obj.config.url=CusGrpInfoApply.grp_no._obj.config.url+"&CusCom.cus_status=1&returnMethod=onReturn";
	}
	
	function mainBrId(data){
		CusGrpInfoApply.manager_br_id._setValue(data.organno._getValue());
	}
	
	function getCusManager(data){
		CusGrpInfoApply.manager_id._setValue(data.actorno._getValue());
	}
	
	function doReturn(){
		var stockURL = '<emp:url action="queryCusGrpInfoApplyList.do"/>?menuId=grpCognizChg';
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};
	
	function doSubmits(){
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
				var serno = jsonstr.serno;
				if(flag=='success'){
		            alert('新增成功!');
		            if(url == 'doReturn'){
		            	doReturn();
		            }else{
		            	var url = '<emp:url action="getCusGrpInfoApplyUpdatePage.do"/>?serno='+serno;
			    		url = EMPTools.encodeURI(url);
			    		window.location=url;
		            }		            
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = CusGrpInfoApply._checkAll();
		if(result){
			CusGrpInfoApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCusGrpInfoApplyRecord.do" method="POST">
		<emp:gridLayout id="CusGrpInfoApplyGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfoApply.serno" label="申请流水号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:pop id="CusGrpInfoApply.grp_no" label="关联(集团)编号"   required="true" url="selectCusGrpInfoApplyModPage.do"/>
			<emp:text id="CusGrpInfoApply.grp_name" label="关联(集团)名称" maxlength="60" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_cus_id" label="主申请关联(集团)客户码"   required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_cus_name" label="主申请关联(集团)名称" maxlength="60" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_org_code" label="主申请关联(集团)统一社会信用代码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_loan_card" label="主申请关联(集团)贷款卡编码" maxlength="16" required="true" readonly="true"/>
			<emp:select id="CusGrpInfoApply.grp_finance_type" label="关联(集团)融资形式" defvalue="02" readonly="true" required="true" dictname="STD_ZB_GRP_FIN_TYPE" />
			<emp:select id="CusGrpInfoApply.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfoApply.grp_detail" label="关联(集团)情况说明" maxlength="250" required="true" colSpan="2" readonly="true"/>
			<emp:text id="CusGrpInfoApply.is_change" label="是否变更标志" maxlength="20" hidden="true" required="true" defvalue="1" />
		</emp:gridLayout>
		
		<emp:gridLayout id="CusGrpInfoApplyGroup" maxColumn="2" title="登记信息">
			<emp:text id="CusGrpInfoApply.manager_id_displayname" label="责任人" required="true" readonly="true" />
			<emp:text id="CusGrpInfoApply.manager_br_id_displayname" label="管理机构"  required="true" readonly="true" />
			<emp:text id="CusGrpInfoApply.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusGrpInfoApply.input_br_id_displayname" label="登记机构" readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusGrpInfoApply.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusGrpInfoApply.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusGrpInfoApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_id" label="责任人" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_br_id" label="管理机构"  required="true" readonly="false" hidden="true"/>
			<emp:text id="CusGrpInfoApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="submits" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>