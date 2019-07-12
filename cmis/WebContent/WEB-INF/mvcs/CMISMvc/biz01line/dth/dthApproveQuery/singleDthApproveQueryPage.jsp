<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String submitType = "";
	String userid = "";
	if(context.containsKey("submitType")){
		submitType = (String)context.getDataValue("submitType");
	}
	if(context.containsKey("userid")){
		userid = (String)context.getDataValue("userid");
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
			var url = '<emp:url action="expSingleLmtToExcel.do"/>?submitType='+submitType+'&userid='+"<%=userid%>";
		}else{
			var url = '<emp:url action="expSingleCcrToExcel.do"/>?submitType='+submitType+'&userid='+"<%=userid%>";
		}    	
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doAllRecord(){
		var url = '<emp:url action="getDthApproveQueryPage.do"/>?submitType='+"<%=submitType%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doShowCus(){
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
		alertDetails(commentcontent,displayid,'775');
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
 <form  method="POST" action="#" id="queryForm" style=""><!-- 2014-5-22 -->
	<div align="left">
		<emp:button id="return" label="返回首页" />
		<emp:button id="portExcel" label="导出Excel" />
		<emp:button id="allRecord" label="全部记录" />		
	</div>
	
	<emp:table icollName="ApproveQueryList" pageMode="false" url=" " >
		<emp:text id="orgid_displayname" label="经办机构" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:link id="cus_name" label="客户名称" operation="ShowCus"/>
		<%
			if (submitType.equals("getDthApproveLmtLast") || submitType.equals("getDthApproveLmtThis")) {
		%>
		<emp:text id="crd_totl_amt" label="上报金额" dataType="Currency"/>
		<emp:text id="crd_totl_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="org_crd_totl_amt" label="原有授信金额" dataType="Currency"/>
		<emp:text id="change_amt" label="新增金额" dataType="Currency"/>
		<%
			} else {
		%>
		<emp:text id="flag" label="信用等级模型"/>
		<emp:text id="auto_score" label="机评得分"/>
		<emp:text id="auto_grade" label="机评信用等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<emp:text id="adjusted_grade" label="客户经理调整信用等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<%
			}
		%>
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