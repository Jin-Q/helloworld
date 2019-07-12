<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doOnLoad(){
	var is_this_org_service = IqpAbsBatchMng.is_this_org_service._getValue();
	if(is_this_org_service =="1"){
		IqpAbsBatchMng.service_org_no._obj._renderHidden(false);
		IqpAbsBatchMng.service_org_name._obj._renderHidden(false);
		IqpAbsBatchMng.service_org_no._obj._renderRequired(true);
		IqpAbsBatchMng.service_org_name._obj._renderRequired(true);
	}else{
		IqpAbsBatchMng.service_org_no._obj._renderHidden(true);
		IqpAbsBatchMng.service_org_name._obj._renderHidden(true);
		IqpAbsBatchMng.service_org_no._obj._renderRequired(false);
		IqpAbsBatchMng.service_org_name._obj._renderRequired(false);
	}
};

function doSave(){
	var form = document.getElementById("submitForm");
	if(!IqpAbsBatchMng._checkAll()){
		return;
	}else{
		IqpAbsBatchMng._toForm(form);
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag =="success"){
					alert("保存成功!");
					doReturn();
				}else{
					alert("保存失败!");
				}
			}
		};
		var handleFailure = function(o) {
			alert("异步请求出错！");	
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action,callback,postData);
	}
};

function doChangeOS(){
	var is_this_org_service = IqpAbsBatchMng.is_this_org_service._getValue();
	if(is_this_org_service =="1"){
		IqpAbsBatchMng.service_org_no._obj._renderHidden(false);
		IqpAbsBatchMng.service_org_name._obj._renderHidden(false);
		IqpAbsBatchMng.service_org_no._obj._renderRequired(true);
		IqpAbsBatchMng.service_org_name._obj._renderRequired(true);
	}else{
		IqpAbsBatchMng.service_org_no._obj._renderHidden(true);
		IqpAbsBatchMng.service_org_name._obj._renderHidden(true);
		IqpAbsBatchMng.service_org_no._obj._renderRequired(false);
		IqpAbsBatchMng.service_org_name._obj._renderRequired(false);
		IqpAbsBatchMng.service_org_no._setValue('');
		IqpAbsBatchMng.service_org_name._setValue('');
	}
};
function doReturn(){
	var url = '<emp:url action="queryIqpAbsBatchMngList.do"/>';  
	url = EMPTools.encodeURI(url);
	window.location=url;
};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="updateIqpAbsBatchMngRecord.do" method="POST">
		<emp:gridLayout id="IqpAbsBatchMngGroup" maxColumn="2" title="修改证券化资产批次信息">
			<emp:text id="IqpAbsBatchMng.batch_no" label="批次号" maxlength="18" required="true" hidden="false" readonly="true"/>
			<emp:text id="IqpAbsBatchMng.batch_name" label="证券化批次名称" maxlength="80" required="true" colSpan="2" readonly="true"/>
			<emp:select id="IqpAbsBatchMng.trust_org_cert_type" label="受托机构证件类型" dictname="STD_ABS_CERT_TYPE" required="true" />
			<emp:text id="IqpAbsBatchMng.trust_org_cert_no" label="受托机构证件号码" maxlength="20" required="true" />
			<emp:text id="IqpAbsBatchMng.trust_org_no" label="受托机构名称" maxlength="80" required="true" />
			<emp:text id="IqpAbsBatchMng.manager_br_id" label="所属行社" maxlength="80" required="true" />
			<emp:text id="IqpAbsBatchMng.fund_acct_no" label="信托财产资金账号" maxlength="30" required="true" />
			<emp:text id="IqpAbsBatchMng.fund_acct_name" label="信托财产资金户名" maxlength="30" required="true" />
			<emp:text id="IqpAbsBatchMng.keep_org_no" label="资金保管机构行号" maxlength="20" required="true" />
			<emp:text id="IqpAbsBatchMng.keep_org_name" label="资金保管机构行名" maxlength="80" required="true" />
			<emp:select id="IqpAbsBatchMng.is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO" required="true" onchange="doChangeOS()"/>
			<emp:text id="IqpAbsBatchMng.service_org_no" label="贷款服务机构行号" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAbsBatchMng.service_org_name" label="贷款服务机构行名" maxlength="80" required="false" hidden="true"/>
			<emp:text id="IqpAbsBatchMng.service_fee_rate" label="服务费率"  required="true" dataType="Rate"/>
			<emp:text id="IqpAbsBatchMng.service_fee" label="总服务费" maxlength="11" required="true" dataType="Currency"/>
			<emp:date id="IqpAbsBatchMng.trust_date" label="信托成立日" required="true" />
			<emp:date id="IqpAbsBatchMng.cash_date" label="最后兑付日期" required="true" />
			<emp:text id="IqpAbsBatchMng.input_id" label="操作人员" maxlength="10" required="true" />
			<emp:text id="IqpAbsBatchMng.input_br_id" label="操作机构" maxlength="10" required="true" />
			<emp:date id="IqpAbsBatchMng.update_date" label="修改日期" required="false" />
			<emp:text id="IqpAbsBatchMng.mark" label="备注" maxlength="80" required="true" />
			<emp:select id="IqpAbsBatchMng.acct_status" label="账务状态" dictname="STD_ABS_ACCOUNT_STATUS" required="true" defvalue="01" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改"/>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
