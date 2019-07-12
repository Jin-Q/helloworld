<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
String guaranty_no = request.getParameter("guaranty_no");
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
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
		var result = MortGuarantyInsurInfo._checkAll();
	    if(result){
	    	MortGuarantyInsurInfo._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
	    	var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};		
	function checkInsurStartDate(){
		if(MortGuarantyInsurInfo.insur_start_date._obj.element.value!=''){
			var e = MortGuarantyInsurInfo.inure_date._obj.element.value;
			var s = MortGuarantyInsurInfo.insur_start_date._obj.element.value;
			var d = MortGuarantyInsurInfo.insur_end_date._obj.element.value;
			//if(CheckDateAfterToday(s)){
        		//alert('起始日期不能大于当前日期！');
        		//GuarantyInsurInfo.insur_start_date._obj.element.value="";
        		//return;
        	//}
			if(e!=''){
				if(s>e){
            		alert('起始日期必须小于或等于生效日期！');
            		MortGuarantyInsurInfo.insur_start_date._obj.element.value="";
            		return;
            	}
			}
			if(d!=''){
				if(s>=d){
            		alert('起始日期必须小于到期日期！');
            		MortGuarantyInsurInfo.insur_start_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortGuarantyInsurInfo.insur_end_date._obj.element.value!=''){
			var e = MortGuarantyInsurInfo.inure_date._obj.element.value;
			var s = MortGuarantyInsurInfo.insur_start_date._obj.element.value;
			var d = MortGuarantyInsurInfo.insur_end_date._obj.element.value;
			if(e!=''){
				if(d<=e){
					alert('到期日期必须大于生效日期！');
					MortGuarantyInsurInfo.insur_end_date._obj.element.value="";
        			return;
				}
			}
			if(s!=''){
				if(d<=s){
            		alert('到期日期必须大于起始日期！');
            		MortGuarantyInsurInfo.insur_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkEffectiveDate(){
		if(MortGuarantyInsurInfo.inure_date._obj.element.value!=''){
			var s = MortGuarantyInsurInfo.insur_start_date._obj.element.value;
			var e = MortGuarantyInsurInfo.inure_date._obj.element.value;
			var d = MortGuarantyInsurInfo.insur_end_date._obj.element.value;
			if(s!=''){
				if(e<s){
					alert('生效日期必须大于或等于起始日期！');
					MortGuarantyInsurInfo.inure_date._obj.element.value="";
            		return;
				}
			}
			if(d!=''){
				if(e>=d){
            		alert('生效日期必须小于到期日期！');
            		MortGuarantyInsurInfo.inure_date._obj.element.value="";
            		return;
            	}
			}
		}
	}	
	function doReturn(){
		var guaranty_no = MortGuarantyInsurInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantyInsurInfoList.do"/>?menuIdTab=mort_maintain&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateMortGuarantyInsurInfoRecord.do" method="POST">
		<emp:gridLayout id="MortGuarantyInsurInfoGroup" title="记录抵质押物保险信息" maxColumn="2">
			<emp:text id="MortGuarantyInsurInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="MortGuarantyInsurInfo.insu_org_type" label="保险机构类型" required="false" dictname="STD_INSU_ORG_TYPE" hidden="true"/>
			<emp:text id="MortGuarantyInsurInfo.insu_org_no" label="保险机构编号" maxlength="40" />
			<emp:text id="MortGuarantyInsurInfo.insu_org_name" label="保险机构名称" maxlength="100" required="true" />
			<emp:text id="MortGuarantyInsurInfo.insuarance_no" label="保险单编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortGuarantyInsurInfo.insurant" label="被保险人" maxlength="100" required="true" />
			<emp:text id="MortGuarantyInsurInfo.beneficiar" label="受益人" maxlength="100" required="true" />
			<emp:select id="MortGuarantyInsurInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="MortGuarantyInsurInfo.insure_amt" label="投保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="MortGuarantyInsurInfo.insur_start_date" label="起始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortGuarantyInsurInfo.inure_date" label="生效日期" required="true" onblur="checkEffectiveDate()"/>
			<emp:date id="MortGuarantyInsurInfo.insur_end_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:select id="MortGuarantyInsurInfo.insu_type" label="保险险种" required="true" dictname="STD_ZB_INSU_TYPE" />
			<emp:textarea id="MortGuarantyInsurInfo.other_memo" label="其他说明" maxlength="600" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="修改"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
