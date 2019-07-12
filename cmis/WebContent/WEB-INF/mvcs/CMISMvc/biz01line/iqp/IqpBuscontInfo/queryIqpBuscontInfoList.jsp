<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String po_no = context.getDataValue("po_no").toString();
	String type = context.getDataValue("type").toString();
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBuscontInfo._toForm(form);
		IqpBuscontInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBuscontInfoPage() {
		var paramStr = IqpBuscontInfoList._obj.getParamStr(['tcont_no','po_no']);
		if (paramStr != null) {		
			var url = '<emp:url action="getIqpBuscontInfoUpdatePage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'Update',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBuscontInfo() {
		var paramStr = IqpBuscontInfoList._obj.getParamStr(['tcont_no','po_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBuscontInfoViewPage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'View',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBuscontInfoPage() {
		var url = '<emp:url action="getIqpBuscontInfoAddPage.do"/>?po_no=<%=po_no%>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'Add',param);
	};
	
	function doDeleteIqpBuscontInfo(){
		var paramStr = IqpBuscontInfoList._obj.getParamStr(['tcont_no','po_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功！");
							var url = '<emp:url action="queryIqpBuscontInfoList.do"/>?po_no=<%=po_no%>&type=<%=type%>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除失败！");
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
				
				var url = '<emp:url action="deleteIqpBuscontInfoRecord.do"/>?' + encodeURI(paramStr);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpBuscontInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
	<%if(!type.equals("view")){ %>
		<emp:button id="getAddIqpBuscontInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBuscontInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBuscontInfo" label="删除" op="remove"/>
	<% } %>
		<emp:button id="viewIqpBuscontInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBuscontInfoList" pageMode="false" url="pageIqpBuscontInfoQuery.do">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="tcont_no" label="贸易合同编号" hidden="false"/>
		<emp:text id="sup_mat_cprt" label="供货单位" />
		<emp:text id="tcont_amt" label="贸易合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="贸易合同起始日" />
		<emp:text id="end_date" label="贸易合同到期日" />
		
		<emp:text id="trade_detail" label="贸易交易内容" hidden="true"/>
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    