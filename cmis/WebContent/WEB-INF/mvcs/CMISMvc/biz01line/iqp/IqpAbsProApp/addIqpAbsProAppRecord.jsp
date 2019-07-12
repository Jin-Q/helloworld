<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>	
<%
	//request = (HttpServletRequest) pageContext.getRequest();
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function checkDate(){
	var pre_package_date = IqpAbsProApp.pre_package_date._getValue();
	 var openDay = '${context.OPENDAY}';
	if(pre_package_date!=null && pre_package_date!="" ){
		var flag = CheckDate1BeforeDate2(openDay,pre_package_date);
		if(!flag){
			alert("预封包日期要大于当前日期！");
			IqpAbsProApp.pre_package_date._setValue("");
			return false;
		}
	}
};

function returnBtachInfo(data){
	IqpAbsProApp.batch_no._setValue(data.batch_no._getValue());
	IqpAbsProApp.batch_name._setValue(data.batch_name._getValue());
	IqpAbsProApp.trust_org_no._setValue(data.trust_org_no._getValue());
	IqpAbsProApp.is_this_org_service._setValue(data.is_this_org_service._getValue());
};

function doReset(){
	page.dataGroups.IqpAbsProAppGroup.reset();
};

function doReturn(){
	var url = '<emp:url action="queryIqpAbsProAppList.do"/>?biz_type=${param.biz_type}';  
	url = EMPTools.encodeURI(url);
	window.location=url;
};


function doSave(){
	var form = document.getElementById("submitForm");
	if(!IqpAbsProApp._checkAll()){
		return;
	}else{
		IqpAbsProApp._toForm(form);
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
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAbsProAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAbsProAppGroup" title="证券化预封包申请" maxColumn="2">
			<emp:pop id="IqpAbsProApp.batch_no" label="批次号" url="queryAllAbsBatchInfoPop.do?returnMethod=returnBtachInfo" required="true"  colSpan="2"/>
			<emp:text id  ="IqpAbsProApp.batch_name" label="证券化批次名称" required="true" readonly="true"/>
			<emp:text id  ="IqpAbsProApp.trust_org_no" label="受托机构名称" required="true" readonly="true"/>
			<emp:text id  ="IqpAbsProApp.is_this_org_service" label="是否本机构服务" required="true" readonly="true" dictname="STD_ZX_YES_NO"/>
			<emp:text id="IqpAbsProApp.pre_package_name" label="预封包名称" maxlength="50" required="true" />
			<emp:date id="IqpAbsProApp.pre_package_date" label="预封包日期"  required="true" onblur="checkDate();"/>
			<emp:text id="IqpAbsProApp.input_id" label="操作人员" maxlength="10" required="false" />
			<emp:text id="IqpAbsProApp.input_br_id" label="操作机构" maxlength="10" required="false" />
			<emp:date id="IqpAbsProApp.update_date" label="修改日期" required="false" />
			<emp:select id="IqpAbsProApp.prc_status" label="处理状态" required="false"  dictname="STD_ABS_PRC_STATUS" defvalue="1" readonly="true"/>
			<emp:text id="IqpAbsProApp.pre_package_serno" label="预封包流水号" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

