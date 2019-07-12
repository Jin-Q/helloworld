<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
String guaranty_no = request.getParameter("guaranty_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function convert(){
		if(MortGuarantyCertiInfo.warrant_cls._getValue()=="1"){
			MortGuarantyCertiInfo.warrant_type_other._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_type_other._obj._renderRequired(false);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderHidden(false);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderRequired(true);
			MortGuarantyCertiInfo.warrant_type_other._setValue("");
		}else{
			MortGuarantyCertiInfo.warrant_type_other._obj._renderHidden(false);
			MortGuarantyCertiInfo.warrant_type_other._obj._renderRequired(true);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderRequired(false);
			MortGuarantyCertiInfo.warrant_type_con._setValue("");
		}
	}
	function doset(){
		MortGuarantyCertiInfo.warrant_type._setValue(MortGuarantyCertiInfo.warrant_type_other._getValue());
		doCheck();
	}
	function doset1(){
		MortGuarantyCertiInfo.warrant_type._setValue(MortGuarantyCertiInfo.warrant_type_con._getValue());
		doCheck();
	}
	function doLoad(){
		convert();
	}	
	//选择保管机构信息返回方法
	function getOrgIDKeep(data){
		MortGuarantyCertiInfo.keep_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.keep_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择经办机构信息返回方法
	function getOrgIDHand(data){
		MortGuarantyCertiInfo.hand_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.hand_org_no_displayname._setValue(data.organname._getValue());
	};		
	function doNext(){
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
				if("success" == flag){
					alert("保存成功！");
					doReturn();
				}else{
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById('submitForm');
		var result = MortGuarantyCertiInfo._checkAll();
		 if(result){
			MortGuarantyCertiInfo._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
	    	var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};
	function doNext1(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var check = jsonstr.check;
				if("false" == check){
					MortGuarantyCertiInfo.warrant_no._setValue("");
				}else{
					doNext();
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var warrant_no = MortGuarantyCertiInfo.warrant_no._getValue();
		var warrant_type = MortGuarantyCertiInfo.warrant_type._getValue();

		//权证编号中文传输会乱码，所以使用编码传输
		warrant_no = encodeURIComponent(warrant_no);
		
		var url = '<emp:url action="checkWarrantNo.do"/>?warrant_no='+warrant_no+'&warrant_type='+warrant_type;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}

	function trim(str)
	{
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	//异步校验权证编号是否唯一
	function doCheck(){
		var warrant_no = MortGuarantyCertiInfo.warrant_no._getValue();
		MortGuarantyCertiInfo.warrant_no._setValue(trim(warrant_no));
		
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var check = jsonstr.check;
				if("false" == check){
					alert("此权证类型下的权证编号已经存在，请重新录入!");
					MortGuarantyCertiInfo.warrant_no._setValue("");
				}else{
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var warrant_type = MortGuarantyCertiInfo.warrant_type._getValue();

		//权证编号中文传输会乱码，所以使用编码传输
		warrant_no = encodeURIComponent(warrant_no);
		
		var url = '<emp:url action="checkWarrantNo.do"/>?warrant_no='+warrant_no+'&warrant_type='+warrant_type;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	//日期校验
	function checkDt(date){
		var start = date._obj.element.value;
		//alert(start);
		var openDay='${context.OPENDAY}';
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(start,openDay);
			if(!flag){
				alert("输入的日期要小于等于当前日期！");
				MortGuarantyCertiInfo.warrant_appro_date._setValue("");
			}else{
		    }
		}
	}
	function doReturn() {
		var guaranty_no = MortGuarantyCertiInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantyCertiInfoList.do"/>?menuIdTab=mort_maintain&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
<style type="text/css">

/************************ 下拉框(empext:select)的样式 **************************/
	/************ 下拉框(select)普通状态下的样式 ****************/
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:250px;
};
.emp_field_select_longinput { 
	display: inline;
	border-width: 1px;
	width: 250px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_readonly .emp_field_select_longinput {
	display: none;
}
.emp_field_readonly .emp_field_select_longinput {
	display: inline;
	width: 250px;
	border-color: #b7b7b7;
}
</style>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortGuarantyCertiInfoRecord.do" method="POST">
		
		<emp:gridLayout id="MortGuarantyCertiInfoGroup" title="抵质押物权证信息" maxColumn="2">
			<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true" defvalue="<%=guaranty_no %>"/>
			<emp:radio id="MortGuarantyCertiInfo.warrant_cls" label="权证类别" required="true" dictname="STD_WARRANT_TYPE" layout="false" onclick="convert()" defvalue="1"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_type" label="权证类型" required="true" hidden="true" onchange="doCheck()"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type_other" label="其他权证类型" dictname="STD_OTHER_WARRANT_TYPE" onchange="doset();doCheck()" colSpan="2" cssElementClass="emp_field_input"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type_con" label="权利证明类型" dictname="STD_WRR_PROVE_TYPE" onchange="doset1();doCheck()" colSpan="2" cssElementClass="emp_field_input"/>
			<emp:select id="MortGuarantyCertiInfo.is_main_warrant" label="是否主权证" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" readonly="true"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" maxlength="100" required="true" onchange="doCheck()"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_name" label="权证名称" maxlength="40" required="true" />
			<emp:text id="MortGuarantyCertiInfo.warrant_appro_unit" label="权利凭证核发单位" maxlength="100" required="false" />
			<emp:date id="MortGuarantyCertiInfo.warrant_appro_date" label="权利凭证核发日期" required="false" onblur="checkDt(MortGuarantyCertiInfo.warrant_appro_date)"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_trem" label="权利凭证期限" maxlength="10" />
			<emp:select id="MortGuarantyCertiInfo.warrant_state" label="权证状态" required="true" dictname="STD_WARRANT_STATUS" readonly="true" defvalue="1" colSpan="2"/>
			<emp:pop id="MortGuarantyCertiInfo.keep_org_no_displayname" label="保管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDKeep" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:pop id="MortGuarantyCertiInfo.hand_org_no_displayname" label="经办机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDHand" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:text id="MortGuarantyCertiInfo.keep_org_no" label="保管机构" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortGuarantyCertiInfo.hand_org_no" label="保管机构" maxlength="10" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next1" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

