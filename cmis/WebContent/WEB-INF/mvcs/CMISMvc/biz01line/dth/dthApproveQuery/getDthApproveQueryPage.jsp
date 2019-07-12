<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String submitType = "";
	if(context.containsKey("submitType")){
		submitType = (String)context.getDataValue("submitType");
	} 
%> 
<emp:page>

<html>
<head>
<title>审批报备</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>
<script type="text/javascript">
	function doPortExcel(){
		var submitType = "<%=submitType%>";
		if(submitType == 'getDthApproveLmtLast' || submitType == 'getDthApproveLmtThis'){
			var url = '<emp:url action="expAllLmtToExcel.do"/>?submitType='+submitType;
		}else{
			var url = '<emp:url action="expAllCcrToExcel.do"/>?submitType='+submitType;
		}    	
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doShowCus(){	//客户信息链接
		var selObj = ApproveQueryList._obj.getSelectedData()[0];
		var cus_id=selObj.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
		windowName = Math.ceil(Math.random()*50000000);
		EMPTools.openWindow(url,windowName+"",'height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function doShowComment(){	//审批意见详情
		var selObj = ApproveQueryList._obj.getSelectedData()[0];
		var commentcontent=selObj.commentcontent._getValue();
		var displayid = selObj.displayid._getValue();
		alertDetails(commentcontent,displayid,'1614');
	};

	function doShowPrdid(){	//业务品种详情
		var selObj = ApproveQueryList._obj.getSelectedData()[0];
		var prd_id_displayname=selObj.prd_id_displayname._getValue();
		var displayid = selObj.displayid._getValue();
		alertDetails(prd_id_displayname,displayid,'460');
	};

	function alertDetails(data,displayid,x){	//弹出详细信息
	    var win = new Ext.Window({
	        title: '详细信息',
	        layout: 'fit',    //设置布局模式为fit，能让frame自适应窗体大小
	        modal: false,    //打开遮罩层
	        border: 0,    //无边框
	        frame: true,    //去除窗体的panel框架
	        html: data
	    });

	    win.setSize(300, 200);    //w为设置的宽度，h为设置的高度
	    win.setPosition(x, 37+displayid*25);    //x为设置的x坐标，y为设置的y坐标。这个37+25x是调出来的
	    win.show();    //显示窗口
	};

	function doReturn(){
		var url = '<emp:url action="queryDthApproveQueryPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

</script>
</head>

<body class="page_content">
	<form  method="POST" action="#" id="queryForm" style="width: 2000">
	<div align="left">
		<emp:button id="return" label="返回首页" />
		<emp:button id="portExcel" label="导出Excel" />		
	</div>
	
	<emp:table icollName="ApproveQueryList" pageMode="false" url=" " >
		<emp:text id="orgid_displayname" label="经办机构" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:link id="cus_name" label="客户名称" operation="ShowCus"/>
		<emp:text id="belg_line" label="条线" dictname="STD_ZB_BIZ_BELG" />
		<emp:text id="com_cll_door_type" label="行业门类" />
		<emp:text id="com_cll_big_type" label="行业大类" />
		<emp:text id="com_cll_type" label="行业编号" />
		<%
			if (submitType.equals("getDthApproveLmtLast") || submitType.equals("getDthApproveLmtThis")) {
		%>
		<emp:text id="prd_id_displayname" label="业务品种" hidden="true"/>
		<emp:link id="prd_id_show" label="业务品种" operation="ShowPrdid"/>
		<emp:text id="guar_type_displayname" label="担保方式" />
		<emp:text id="crd_totl_amt" label="上报金额" dataType="Currency"/>
		<emp:text id="crd_totl_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="org_crd_totl_amt" label="原有授信金额" dataType="Currency"/>
		<emp:text id="change_amt" label="新增金额" dataType="Currency"/>
		<emp:text id="report_type" label="上报类型" dictname="STD_APPROVE_APP_TYPE"/>
		<%
			} else {
		%>
		<emp:text id="flag" label="信用等级模型"/>
		<emp:text id="app_type" label="申请类型" dictname="STD_APPROVE_APP_TYPE"/>
		<emp:text id="auto_score" label="机评得分"/>
		<emp:text id="auto_grade" label="机评信用等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<emp:text id="adjusted_grade" label="客户经理调整信用等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<%
			}
		%>
		<emp:text id="approve_status" label="审批结果" dictname="WF_APP_STATUS"/>
		<emp:text id="username" label="审批人" />
		<emp:text id="num" label="审批顺序" />
		<emp:text id="nodeendtime" label="审批时间" />
		<emp:text id="commentcontent" label="审批意见" hidden="true"/>
		<emp:link id="commentcontent_show" label="审批意见" operation="ShowComment"/>
	</emp:table>
	</form>	
</body>
</html>
</emp:page>