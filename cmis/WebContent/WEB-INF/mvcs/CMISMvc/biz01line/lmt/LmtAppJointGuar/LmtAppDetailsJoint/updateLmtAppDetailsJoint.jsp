<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String belg_line = "";
if(context.containsKey("belg_line")){
	belg_line = (String)context.getDataValue("belg_line");
}
%>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doSave(){
		if(!LmtAppDetails._checkAll()){
			return false;
		}
		var form = document.getElementById("submitForm");
		LmtAppDetails._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("修改成功！");
					window.opener.location.reload();
					window.close();
				}else {
					alert("新增选择产品发生异常！");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	//关闭
	function doCloseAppDet(){
		window.close();
	}

	function doload(){
		//清除担保方式中的全额保证金、准全额保证金 
	/*	var options = LmtAppDetails.guar_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "500" || options[i].value == "510"){
				options.remove(i);
			}
		}*/
		var belg_line = '<%=belg_line%>';
		if(belg_line == 'BL300'){
			var urls = '&bizline=BL300';
		}else{
			var urls = '&bizline=BL100,BL200';
		}
		LmtAppDetails.prd_id._obj.config.url=LmtAppDetails.prd_id._obj.config.url+urls;
		LmtAppDetails.limit_name_displayname._obj.config.url=LmtAppDetails.limit_name_displayname._obj.config.url+urls;
	}

	//设置产品返回 
	function setProds(data){
		LmtAppDetails.prd_id._setValue(data[0]);
		LmtAppDetails.prd_id_displayname._setValue(data[1]);
		checkPrdConName('1');
	}

	//额度名称返回 
	function setLmtName(data){
		LmtAppDetails.limit_name._setValue(data.id);
		LmtAppDetails.limit_name_displayname._setValue(data.label);
		checkPrdConName('2');
	}
	//校验适用产品是否包含额度名称
	function checkPrdConName(flag){
		var lmtName = LmtAppDetails.limit_name._getValue();
		var prdId = LmtAppDetails.prd_id._getValue();
		if(lmtName!=null&&lmtName!=''&&prdId!=null&&prdId!=''){
			if(prdId.indexOf(lmtName)<0){
				alert("适用产品必须包含额度品种名称！");
				if(flag=='1'){
					LmtAppDetails.prd_id._setValue('');
					LmtAppDetails.prd_id_displayname._setValue('');
					LmtAppDetails.belg_line._setValue('');
				}else{
					LmtAppDetails.limit_name._setValue('');
					LmtAppDetails.limit_name_displayname._setValue('');
				}
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updateLmtAppDetailsJointRecord.do" method="POST">
		<emp:gridLayout id="LmtAppDetailsGroup" maxColumn="2" title="额度分项信息">
			<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:text id="LmtAppDetails.cus_id" label="客户码 " hidden="true" readonly="true"/>
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="true" dictname="STD_LMT_PROJ_TYPE" readonly="true"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="40" required="true" hidden="true" />
			<emp:pop id="LmtAppDetails.limit_name_displayname" label="额度品种名称" url='showPrdTreeDetails.do' required="true" returnMethod="setLmtName"/>
			<emp:pop id="LmtAppDetails.prd_id" label="适用产品编号" url='showPrdCheckTreeDetails.do' returnMethod="setProds" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppDetails.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="true" dataType="Currency" maxlength="18" />
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" defvalue="300" readonly="true"/>
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" defvalue="1" readonly="true"/>
			<emp:select id="LmtAppDetails.term_type" label="授信期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppDetails.term" label="授信期限" maxlength="5" required="false" dataType="Int"/>
			
			<emp:date id="LmtAppDetails.start_date" label="授信起始日" required="false" hidden="true"/>
			<emp:date id="LmtAppDetails.end_date" label="授信到期日" required="false" hidden="true"/>
			<emp:select id="LmtAppDetails.update_flag" label="修改类型" required="false" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true" hidden="true"/>
			
			<emp:text id="LmtAppDetails.ori_crd_amt" label="原有授信金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改" op="update"/>
			<emp:button id="closeAppDet" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
