<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String roles = "";
	if(context.containsKey("roles")){
		roles = (String)context.getDataValue("roles");
	} 
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateCusSubmitInfoPage() {
		var paramStr = CusSubmitInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusSubmitInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusSubmitInfo() {
		var paramStr = CusSubmitInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusSubmitInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
		//	window.location = url;
			window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusSubmitInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doHandOverPop(){
		//选择一个集中录入岗用户
		var data = CusSubmitInfoList._obj.getSelectedData();
		if(data != null && data != ""){
			var cusId = data[0].cus_id._getValue();
			var url = '<emp:url action="handOverCusSubmitInfoPage.do"/>?' + "cus_id=" + cusId;
		    url = EMPTools.encodeURI(url);
		    window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}

	//将当前任务置为完成状态
	function doHandEndFlag(){
		var paramStr = CusSubmitInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="handEndFlagRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.mask();
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("完成失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert('当前任务已完成!');
						window.location.reload();								
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("完成失败，请联系管理员!");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<!--<emp:button id="getUpdateCusSubmitInfoPage" label="修改" op="update"/>-->
		<emp:button id="viewCusSubmitInfo" label="查看" op="view"/>
		<emp:button id="handOverPop" label="移交" op="handOver"/>
		<emp:button id="handEndFlag" label="完成" op="handEnd"/>
	</div>

	<emp:table icollName="CusSubmitInfoList" pageMode="false" url="pageCusSubmitInfoQuery.do">
		<emp:text id="serno" label="流水号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="submit_id" label="提交人" hidden="true"/>
		<emp:text id="rcv_id" label="接收人" hidden="true"/>
		<emp:text id="submit_id_displayname" label="提交人" />
		<%if(!roles.contains("3003")){%>
		 <emp:text id="rcv_id_displayname" label="接收人" />
		<%} %>
		<emp:text id="memo" label="提示信息" />
		<emp:text id="input_date" label="录入日期" />
		<emp:text id="end_flag" label="完成标志(0.完成 1.未完成)" hidden="true"/>
		<emp:text id="opr_time" label="具体时间" hidden="true"/>
		<emp:text id="opr_type" label="操作类型(1.提交 2.打回 3.移交)" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    