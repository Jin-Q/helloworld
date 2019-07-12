<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	} 
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/> 

<script type="text/javascript">

function doQuery(){
	var form = document.getElementById('queryForm');
	IqpLoanApp._toForm(form);
	IqpLoanAppList._obj.ajaxQuery(null,form);
};


function doViewIqpLoanApp() {
	var paramStr = IqpLoanAppList._obj.getParamStr(['serno','cus_id']);
	var prd_id = IqpLoanAppList._obj.getParamValue(['prd_id']);
	var appStatus = IqpLoanAppList._obj.getParamValue(['approve_status']);
	if (paramStr != null) {
		if(prd_id == 300021 || prd_id == 300020){
			var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?op=view&'+paramStr+'&flag=iqpLoanApp&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}else{
			var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?op=view&'+paramStr+'&flag=iqpLoanApp&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择一条记录！');
	}
};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <div  class='emp_gridlayout_title'>客户被拒绝历史列表</div>
	<emp:table icollName="IqpLoanAppList" pageMode="true" url="pageGetCusIsRfuListHis.do" reqParams="cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务流水号" />  		
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="apply_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="apply_amount" label="申请金额" dataType="Currency" />
		<emp:text id="apply_date" label="申请日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:select id="prd_id" label="产品编号" hidden="true"/>
	</emp:table>   
	
</body>
</html>
</emp:page>
    