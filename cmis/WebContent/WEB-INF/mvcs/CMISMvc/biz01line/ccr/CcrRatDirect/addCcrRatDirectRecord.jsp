<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.yucheng.cmis.pub.PUBConstant"%><emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function returnCus(data){
		CcrAppInfo.cus_id._setValue(data.cus_id._getValue());
		CcrAppInfo.cus_name._setValue(data.cus_name._getValue());
		CcrAppInfo.cus_type._setValue(data.cus_type._getValue());
		CcrAppInfo.manager_id._setValue(data.cust_mgr._getValue());
		CcrAppInfo.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		CcrAppInfo.manager_br_id._setValue(data.main_br_id._getValue());
		CcrAppInfo.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		checkCusExist();
		checkCusComHdEnterprise();
	}
	//检查此客户是否是省级以上龙头企业
	function checkCusComHdEnterprise(){
		var url = "<emp:url action='checkCusComHdEnterprise.do'/>&cus_id="+CcrAppInfo.cus_id._getValue();	
		var callback = {
			success : "doReturnMethodCus",
			isJSON : true
		};
		EMPTools.ajaxRequest('GET', url, callback);
	}
	function doReturnMethodCus(json, callback){
		if('success'==json.flag){
			CcrAppDetail.reason_show._setValue("");
			//此客户为省级以上龙头企业
		}else if('fail'==json.flag){
			changeDic();
		}else{
			alert('查询出错！');
		}	
	}
	//为非省级以上龙头企业时，第二个选项为不可选
	function changeDic(){
		CcrAppDetail.reason_show._setValue("");
		var reason_show = document.getElementsByName('CcrAppDetail.reason_show');
		reason_show[1].disabled="true";
	}
	function setconId(data){
		CcrAppInfo.manager_id_displayname._setValue(data.actorname._getValue());
		CcrAppInfo.manager_id._setValue(data.actorno._getValue());
		CcrAppInfo.manager_br_id._setValue(data.orgid._getValue());
		CcrAppInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(true);
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
					CcrAppInfo.manager_br_id._setValue(jsonstr.org);
					CcrAppInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CcrAppInfo.manager_br_id._setValue("");
					CcrAppInfo.manager_br_id_displayname._setValue("");
					CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CcrAppInfo.manager_id._getValue();
					CcrAppInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CcrAppInfo.manager_br_id._setValue("");
					CcrAppInfo.manager_br_id_displayname._setValue("");
					CcrAppInfo.manager_br_id_displayname._obj._renderReadonly(false);
					CcrAppInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CcrAppInfo.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		CcrAppInfo.manager_br_id._setValue(data.organno._getValue());
		CcrAppInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	
	function doload(){ 
		CcrAppInfo.cus_id._obj.config.url=CcrAppInfo.cus_id._obj.config.url+"&returnMethod=returnCus";
		var itms1 = document.getElementById('CcrAppDetail.adjusted_grade').options;//更改客户经理调整等级的字典项
	    itms1.remove("01");
	} 	
	function CheckDt(){
		start = CcrAppInfo.app_begin_date._getValue();
		expiring = CcrAppDetail.congniz_fn_dt._getValue();
		/*if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(openDay,start);
			if(!flag){
				alert("开始日期要大于当前日期！");
				CcrAppInfo.start_date._setValue("");
				return false;
			}
		}*/
		if (start!=null && start!="" &&expiring!=null && expiring!=""){
			var flag = CheckDate1BeforeDate2(start,expiring);
			
			if(!flag){
				alert("到期日期要大于认定日期！");
				CcrAppDetail.congniz_fn_dt._setValue("");
				return false;
			}			
		} 
		return true;
		
	}

	function riskInspect(){
		var _applType = "992";
		var _wfSign  = "";
		var _modelId = "CusBase";
		var _pkCol   = "cus_id";
		var _pkVal   = CcrRatDirect.cus_id._getValue();
		var _preventIdLst = "FFFBC88600C4FB5C801C0955D3A2E9A5";
	    var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&wfsign="+_wfSign + "&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
	    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
		return _retObj;	
	}
		
	function doReturn() {
		var url = '<emp:url action="queryCcrRatDirectList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}	

	function doNext(){
		var form = document.getElementById('submitForm');
		var result = CcrAppInfo._checkAll();
	 	var result1 = CcrAppDetail._checkAll();
	    if(result&&result1){
	    	CcrAppInfo._toForm(form);
	    	CcrAppDetail._toForm(form);
	    	form.submit();
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};	
	function doChange(){
		var reason_show = CcrAppDetail.reason_show._getValue();
		if(""==reason_show||null==reason_show){
			CcrAppDetail.adjusted_grade._obj._renderReadonly(false);
		}else{
			CcrAppDetail.adjusted_grade._setValue("11");
			CcrAppDetail.adjusted_grade._obj._renderReadonly(true);
		}
	}
	//检查此客户有没有正在进行的评级
	function checkCusExist(){
		
		var url = "<emp:url  action='checkAppExist.do'/>&cus_id="+CcrAppInfo.cus_id._getValue();	
		var callback = {
			success : "doReturnMethod",
			isJSON : true
		};
		EMPTools.ajaxRequest('GET', url, callback);
	}
	
	function doReturnMethod(json, callback){
		
		if('false'==json.ccr_result){
			//未发现重名客户
		}else if('true'==json.ccr_result){
			alert("该客户已经开始信用评级,在评级结束之前不允许重复评级");
			CcrAppInfo.cus_id._setValue("");
			CcrAppInfo.cus_name._setValue("");
			CcrAppInfo.cus_type._setValue("");
		}else{
			alert('查询信用评级明细表出错了');
		}	
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCcrRatDirectRecord.do" method="POST" >
		
		<emp:gridLayout id="CcrAppInfoGroup" title="评级直接认定" maxColumn="2">
			<emp:text id="CcrAppInfo.serno" label="业务申请编号" maxlength="40" required="false" colSpan="2" readonly="true" hidden="true"/>
			<emp:pop id="CcrAppInfo.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=((BELG_LINE in ('BL100','BL200')and cus_type!='A2' and cus_status='20' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')) or cus_id in (select cus_id from lmt_ent_self_cus_info))" returnMethod="returnCus" required="true" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="CcrAppInfo.cus_name" label="客户名称 " maxlength="60" required="false" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
			<emp:select id="CcrAppInfo.cus_type" label="客户类型" required="false" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
			<emp:select id="CcrAppDetail.adjusted_grade" label="信用等级" required="true" dictname="STD_ZB_CREDIT_GRADE"/> 
			<emp:checkbox id="CcrAppDetail.reason_show" label=" " required="false" dictname="STD_ZB_COGNIZ" onchange="doChange()" />
			<emp:text id="CcrAppDetail.reason_show0" label=" " required="false" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show1" label=" " required="false" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show2" label=" " required="false" hidden="true"/>
			<emp:textarea id="CcrAppDetail.congniz_reason" label="认定理由" maxlength="1000" required="false" colSpan="2"/>
			<emp:text id="CcrAppInfo.app_begin_date" label="认定日期" maxlength="10" required="false" readonly="true" defvalue="$OPENDAY"/>
			<emp:date id="CcrAppDetail.congniz_fn_dt" label="到期日期" required="true" onblur="CheckDt()" />
			<emp:text id="CcrAppInfo.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="2"/>
			<emp:text id="CcrAppInfo.reportId" label="报表名" hidden="true"/>
			<emp:text id="CcrAppInfo.check_value" label="检查值" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CcrAppInfo.manager_id_displayname" label="主管客户经理" required="true" readonly="false" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CcrAppInfo.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="CcrAppInfo.input_id_displayname" label="登记人"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="CcrAppInfo.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="false"/>
			<emp:text id="CcrAppInfo.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="CcrAppInfo.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="CcrAppInfo.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CcrAppInfo.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

