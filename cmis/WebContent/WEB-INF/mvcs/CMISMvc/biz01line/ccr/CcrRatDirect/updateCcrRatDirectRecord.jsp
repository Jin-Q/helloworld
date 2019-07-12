<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String smallFlag = (String)request.getAttribute("smallFlag");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function returnCus(data){
		CcrAppInfo.cus_id._setValue(data.cus_id._getValue());
		CcrAppInfo.cus_name._setValue(data.cus_name._getValue());
		CcrAppInfo.cus_type._setValue(data.cus_type._getValue()); 
		//checkCusExist();
	}

//	function doLoad(){ 

//		CcrRatDirect.cus_id._obj.config.url=CcrRatDirect.cus_id._obj.config.url+"&returnMethod=returnCus";
		
//	}
	
	function CheckDt(){
		var start = CcrAppInfo.app_begin_date._getValue();
		var expiring = CcrAppDetail.congniz_fn_dt._getValue();
		if (start!=null && start!="" &&expiring!=null && expiring!=""){
			if(start>expiring){
				alert("到期日期要>认定日期！！");
				CcrAppDetail.congniz_fn_dt._setValue("");
				return false;
			}		
		} 
		return true;
	}
	
	function doReturn() {
		var url = '<emp:url action="queryCcrRatDirectList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function toSubmitForm(form){
	    var handleSuccess = function(o){
	        if(o.responseText !== undefined) {
	            try {
	              var jsonstr = eval("("+o.responseText+")");
	              var flag = jsonstr.flag;
	              if(flag == 'success'){
						alert("保存成功");
		          }
	            } catch(e) {
	              alert("保存失败！");
	              return;
	            }
	        }
	     };
	     var handleFailure = function(o){
	     };
	     var callback = {
	         success:handleSuccess,
	         failure:handleFailure
	     };
	     var postData = YAHOO.util.Connect.setForm(form);
	     var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	  };

	function doSubmitt(){
		var form = document.getElementById("submitForm");
	 	var result = CcrAppInfo._checkAll();
	 	var result1 = CcrAppDetail._checkAll();
	    if(result&&result1){
	    	CcrAppInfo._toForm(form);
	    	CcrAppDetail._toForm(form);
	        toSubmitForm(form);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}

    function doStartCcrDirectApp(){
	  	doSubmitt();
		var serno = CcrAppInfo.serno._getValue();
	    var approve_status = CcrAppInfo.approve_status._getValue();
	    if(approve_status!=''&&approve_status!= '000' &&approve_status!= '993'&&approve_status!= '992'){
		alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
	    }
		var cus_id = CcrAppInfo.cus_id._getValue();//客户码
		var cus_name = CcrAppInfo.cus_name._getValue();//客户名称
		WfiJoin.table_name._setValue("CcrAppInfo");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("640");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：ccr_app_info
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.prd_name._setValue("信用评级");
		initWFSubmit(false);
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
	function doChange(){
		var reason_show = CcrAppDetail.reason_show._getValue();
		if(""==reason_show||null==reason_show){
			CcrAppDetail.adjusted_grade._obj._renderReadonly(false);
		}else{
			CcrAppDetail.adjusted_grade._setValue("11");
			CcrAppDetail.adjusted_grade._obj._renderReadonly(true);
		}
	}
	function doLoad(){
		//doChange();
		CcrAppDetail.reason_show._obj._renderDisabled(true);
		CcrAppDetail.adjusted_grade._obj._renderReadonly(true);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="congniz" id="ccrAppTab">
	<emp:tab id="congniz" label="评级直接认定" initial="true" needFlush="true">
	<emp:form id="submitForm" action="updateCcrRatDirectRecord.do" method="POST">
		<emp:gridLayout id="CcrAppInfoGroup" title="评级直接认定" maxColumn="2">
			<emp:text id="CcrAppInfo.serno" label="业务申请编号" maxlength="40" required="false" colSpan="2" readonly="true" hidden="true"/>
			<emp:pop id="CcrAppInfo.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=Com" returnMethod="returnCus" required="true" readonly="true"/>
			<emp:text id="CcrAppInfo.cus_name" label="客户名称 " maxlength="60" required="true" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"   defvalue="${context.cus_name}"/>	
			<emp:select id="CcrAppInfo.cus_type" label="客户类型" required="false" dictname="STD_ZB_CUS_TYPE" readonly="true" defvalue="${context.cus_type}"/>
		    <emp:select id="CcrAppDetail.adjusted_grade" label="信用等级" required="true" dictname="STD_ZB_CREDIT_GRADE"/> 
		   	<emp:checkbox id="CcrAppDetail.reason_show" label=" " required="false" dictname="STD_ZB_COGNIZ" checkValue="1" onchange="doChange()"/>
		   	<emp:text id="CcrAppDetail.reason_show0" label=" " required="false" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show1" label=" " required="false" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show2" label=" " required="false" hidden="true"/>
			<emp:textarea id="CcrAppDetail.congniz_reason" label="认定理由" maxlength="1000" required="false" colSpan="2"/>
			<emp:text id="CcrAppInfo.app_begin_date" label="认定日期" maxlength="10" required="false" readonly="true" defvalue=""/>
			<emp:date id="CcrAppDetail.congniz_fn_dt" label="到期日期" required="true" onblur="CheckDt()" />
		    <emp:text id="CcrAppInfo.flag" label="申请类型" maxlength="40"  hidden="true" defvalue="2"/>
		    <emp:text id="CcrAppInfo.reportId" label="报表名" hidden="true" />
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
		</emp:form>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
		
		<div align="center">
			<br>
			<emp:button id="submitt" label="保存" op="update"/>
			<!--<emp:button id="startCcrDirectApp" label="放入流程" op="update"/>-->
			<emp:button id="return" label="返回"/>
		</div>

</body>
</html>
</emp:page>
