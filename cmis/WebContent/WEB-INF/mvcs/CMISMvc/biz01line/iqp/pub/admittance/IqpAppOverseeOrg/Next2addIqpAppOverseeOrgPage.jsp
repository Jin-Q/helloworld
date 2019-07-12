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
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function returnCus(data){
		IqpAppOverseeOrg.oversee_org_id._setValue(data.cus_id._getValue());
		IqpAppOverseeOrg.oversee_org_id_displayname._setValue(data.cus_name._getValue());
		checkOverseeOrgId();//检查是否存在'在途'的申请
	}

	function checkOverseeOrgId(){
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
					returnCusDetail();//返回监管机构信息
				}else if(flag == "fail"){
					alert("此机构存在'在途'的申请！");
					IqpAppOverseeOrg.oversee_org_id._setValue("");
					IqpAppOverseeOrg.oversee_org_id_displayname._setValue("");
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
		var oversee_org_id = IqpAppOverseeOrg.oversee_org_id._getValue();
		var url = '<emp:url action="queryOverseeOrg.do"/>&oversee_org_id='+oversee_org_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}

	function returnCusDetail() {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						for(var i=0;i<cusList.length;i++){
	                      cusObj=cusList[i];
						}
						cusObj=cusList[0];
						returnDetail(cusObj);
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
			}
		}
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var oversee_org_id = IqpAppOverseeOrg.oversee_org_id._getValue();
		var url = '<emp:url action="getOverseeOrgDetail4Cus.do"/>?oversee_org_id='+oversee_org_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback)
	}

	function returnDetail(cusObj){
		//地址
		var oversee_org_addr = cusObj.oversee_org_addr;
		var oversee_org_addr_displayname = cusObj.oversee_org_addr_displayname;
		if (oversee_org_addr != null) {
			IqpAppOverseeOrg.oversee_org_addr._setValue(oversee_org_addr);
		}
		if (oversee_org_addr_displayname != null) {
			IqpAppOverseeOrg.oversee_org_addr_displayname._setValue(oversee_org_addr_displayname);
		}
		//街道
		var oversee_org_street = cusObj.oversee_org_street;
		if (oversee_org_street != null) {
			IqpAppOverseeOrg.oversee_org_street._setValue(oversee_org_street);
		}
		//组织形式
		var orgmodal = cusObj.orgmodal;
		if (orgmodal != null) {
			IqpAppOverseeOrg.orgmodal._setValue(orgmodal);
		}
		//所属集团名称
		var belg_grp = cusObj.belg_grp;
		if (belg_grp != null) {
			IqpAppOverseeOrg.belg_grp._setValue(belg_grp);
		}
		//注册资金
		var regi_cap = cusObj.regi_cap;
		if (regi_cap != null) {
			IqpAppOverseeOrg.regi_cap._setValue(regi_cap);
		}
		//成立日期
		var build_date = cusObj.build_date;
		if (build_date != null) {
			IqpAppOverseeOrg.build_date._setValue(build_date);
		}
		//企业规模
		var com_scale = cusObj.com_scale;
		if (com_scale != null) {
		//	IqpAppOverseeOrg.com_scale._setValue(com_scale);
		}
		//企业行业地位
		var con_trade_stats = cusObj.con_trade_stats;
		if (con_trade_stats != null) {
			IqpAppOverseeOrg.con_trade_stats._setValue(con_trade_stats);
		}
		//法人客户码
		var legal = cusObj.legal;
		var legal_displayname = cusObj.legal_displayname;
		if (legal != null) {
			IqpAppOverseeOrg.legal._setValue(legal);
		}
		if (legal_displayname != null) {
			IqpAppOverseeOrg.legal_displayname._setValue(legal_displayname);
		}
		if(orgmodal==null ||regi_cap==null||build_date==null||com_scale==null ||con_trade_stats==null
				||legal==null){
			alert("此监管机构信息不完整！");
			IqpAppOverseeOrg.oversee_org_addr._setValue("");//地址
			IqpAppOverseeOrg.oversee_org_street._setValue("");//街道
			IqpAppOverseeOrg.orgmodal._setValue("");//组织形式
			IqpAppOverseeOrg.regi_cap._setValue("");//注册资金
			IqpAppOverseeOrg.build_date._setValue("");//成立日期
			//IqpAppOverseeOrg.com_scale._setValue("");//企业规模
			IqpAppOverseeOrg.con_trade_stats._setValue("");//企业行业地位
			IqpAppOverseeOrg.legal._setValue("");//法人客户码
			IqpAppOverseeOrg.legal_displayname._setValue("");//法人客户名称
			IqpAppOverseeOrg.belg_grp._setValue("");//所属集团
			IqpAppOverseeOrg.oversee_org_id._setValue("");//监管机构编号
			IqpAppOverseeOrg.oversee_org_id_displayname._setValue("");//监管机构名称
			IqpAppOverseeOrg.oversee_org_addr._setValue("");//监管机构地址
			IqpAppOverseeOrg.oversee_org_addr_displayname._setValue("");//监管机构地址
			return false;
		}
	}

	function onReturnRegStateCode(date){
		IqpAppOverseeOrg.oversee_org_addr._obj.element.value=date.id;
    	IqpAppOverseeOrg.oversee_org_addr_displayname._obj.element.value=date.label;
	}

	function doReturn(){
		var oversee_org_id = IqpAppOverseeOrg.oversee_org_id._getValue();
		var url = '<emp:url action="queryIqpAppOverseeOrgList.do"/>&oversee_org_id='+oversee_org_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

    function doNextStep(){
		if(!IqpAppOverseeOrg._checkAll()){
           return;
		}
		var oversee_org_id = IqpAppOverseeOrg.oversee_org_id._getValue();
		var form = document.getElementById("submitForm");
		IqpAppOverseeOrg._toForm(form);
		form.submit();
    };  
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="getIqpAppOverseeOrgAddPage.do" method="POST">
		<emp:gridLayout id="IqpAppOverseeOrgGroup" title="监管机构准入申请" maxColumn="2">
			<emp:pop id="IqpAppOverseeOrg.oversee_org_id" label="监管机构编号" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus"
					 required="true" />
			<emp:text id="IqpAppOverseeOrg.oversee_org_id_displayname" label="监管机构名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" />
			<emp:text id="IqpAppOverseeOrg.oversee_org_addr" label="监管机构地址" required="false" hidden="true" readonly="true"/>
			<emp:text id="IqpAppOverseeOrg.oversee_org_addr_displayname" label="监管机构地址" cssElementClass="emp_field_text_input2" readonly="true" hidden="true"/>
			<emp:text id="IqpAppOverseeOrg.oversee_org_street" label="街道" required="false" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true" hidden="true"/>			
			<emp:text id="IqpAppOverseeOrg.legal" label="法人代表" maxlength="30" required="false" readonly="true" hidden="true" colSpan="2" />
			<emp:text id="IqpAppOverseeOrg.legal_displayname" label="法人代表"  required="true" readonly="true" />
			<emp:select id="IqpAppOverseeOrg.orgmodal" label="组织形式" required="true" dictname="STD_ZB_HOLD_TYPE" readonly="true"/>
			<emp:text id="IqpAppOverseeOrg.regi_cap" label="注册资金(万元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:date id="IqpAppOverseeOrg.build_date" label="成立日期" required="true" readonly="true" />
			<emp:select id="IqpAppOverseeOrg.con_trade_stats" label="企业行业地位" required="true" dictname="STD_ZB_COM_HD_ENTER" readonly="true" />
			<emp:text id="IqpAppOverseeOrg.belg_grp" label="所属集团" maxlength="80" readonly="true" hidden="true"/>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="nextStep" label="下一步" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

