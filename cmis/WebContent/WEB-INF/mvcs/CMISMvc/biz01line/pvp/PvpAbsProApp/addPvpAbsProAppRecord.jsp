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

function doSave(){
	var form = document.getElementById("submitForm");
	if(!PvpAbsProApp._checkAll()){
		return;
	}else{
		PvpAbsProApp._toForm(form);
		var pre_package_serno = PvpAbsProApp.pre_package_serno._getValue();
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
					var url = '<emp:url action="getPvpAbsProAppUpdatePage.do"/>?pre_package_serno='+pre_package_serno+"&biz_type=1";  
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else if(flag =="exist"){
					alert("该批次号已存在在途的出账信息，不允许重复新增!");
					doReset();
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


function returnBtachInfo(data){
	var prc_status = data.prc_status._getValue();
	if(prc_status == "9"){
		PvpAbsProApp.batch_no._setValue(data.batch_no._getValue());
		PvpAbsProApp.batch_name._setValue(data.batch_name._getValue());
		PvpAbsProApp.trust_org_no._setValue(data.trust_org_no._getValue());
		PvpAbsProApp.is_this_org_service._setValue(data.is_this_org_service._getValue());
		PvpAbsProApp.pre_package_serno._setValue(data.pre_package_serno._getValue());
		PvpAbsProApp.pre_package_date._setValue(data.pre_package_date._getValue());
		PvpAbsProApp.pre_package_name._setValue(data.pre_package_name._getValue());
		PvpAbsProApp.input_id._setValue(data.input_id._getValue());
		PvpAbsProApp.input_br_id._setValue(data.input_br_id._getValue());
	}else{
      alert("只有预封包的批次才允许做出账申请！");
	}
};

function doReturn(){
	var url = '<emp:url action="queryPvpAbsProAppList.do"/>?biz_type=${param.biz_type}';  
	url = EMPTools.encodeURI(url);
	window.location=url;
};

function doReset(){
	page.dataGroups.PvpAbsProAppGroup.reset();
};
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPvpAbsProAppRecord.do" method="POST">
		<emp:gridLayout id="PvpAbsProAppGroup" title="证券化出账申请信息" maxColumn="2">
			<emp:pop id="PvpAbsProApp.batch_no" label="批次号" url="queryAllIqpAbsProAppListPop.do?returnMethod=returnBtachInfo" colSpan="2" />
			<emp:text id="PvpAbsProApp.pre_package_serno" label="预封包流水号" maxlength="80"  readonly="true" />
			<emp:text id="PvpAbsProApp.pre_package_name" label="预封包名称" maxlength="50" required="false" readonly="true" />
			<emp:text id="PvpAbsProApp.pre_package_date" label="封包日期" maxlength="10" readonly="true"/>
			<emp:text id="PvpAbsProApp.input_id" label="操作人员" maxlength="10" required="false" readonly="true"/>
			<emp:text id="PvpAbsProApp.input_br_id" label="操作机构" maxlength="10" required="false" readonly="true"/>
			<emp:text id="PvpAbsProApp.update_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:select id="PvpAbsProApp.prc_status" label="处理状态" required="false" dictname="PRC_STATUS" defvalue="1" readonly="true"/>
			<emp:text id="PvpAbsProApp.batch_name" label="批次名称" maxlength="80" required="false"  hidden="true" />
			<emp:text id="PvpAbsProApp.trust_org_no" label="受托机构名称" maxlength="80" hidden="true" required="false" />
			<emp:select id="PvpAbsProApp.is_this_org_service" label="是否本机构服务" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存"/>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

