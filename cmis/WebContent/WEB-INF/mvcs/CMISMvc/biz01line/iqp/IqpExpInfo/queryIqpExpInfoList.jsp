<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
		IqpExpInfo._toForm(form);
		IqpExpInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExpInfoPage() {
		var paramStr = IqpExpInfoList._obj.getParamStr(['po_no','express_no','invc_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExpInfoUpdatePage.do"/>?' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:900px';
			window.showModalDialog(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExpInfo() {
		var paramStr = IqpExpInfoList._obj.getParamStr(['po_no','express_no','invc_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExpInfoViewPage.do"/>?' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:900px';
			window.showModalDialog(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExpInfoPage() {
		var url = '<emp:url action="getIqpExpInfoAddPage.do"/>?po_no=<%=po_no%>';
		url = EMPTools.encodeURI(url);
		var param = 'dialogWidth:900px';
		window.showModalDialog(url,'',param);
		window.location.reload();
	};
	
	function doDeleteIqpExpInfo(){
		var paramStr = IqpExpInfoList._obj.getParamStr(['po_no','express_no','invc_no']);
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
							var url = '<emp:url action="queryIqpExpInfoList.do"/>?po_no=<%=po_no%>&type=<%=type%>';
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
				
				var url = '<emp:url action="deleteIqpExpInfoRecord.do"/>?' + encodeURI(paramStr);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpExpInfoGroup.reset();
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
		<emp:button id="getAddIqpExpInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExpInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExpInfo" label="删除" op="remove"/>
		<% } %>
		<emp:button id="viewIqpExpInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpExpInfoList" pageMode="false" url="pageIqpExpInfoQuery.do">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="express_no" label="快递单号" />
		<emp:text id="express_cprt" label="快递公司" />
		<emp:text id="start_date" label="快递发出日期" />
		<emp:text id="receive_date" label="快递接收日期" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		
		
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    