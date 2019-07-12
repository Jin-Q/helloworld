<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		file_name._toForm(form);
		file_date._toForm(form);
		CyberCusDocumentInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.DocumentGroup.reset();
	};

	//异步删除
	function doDeleteCyberCusDocumentInfo() {
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("删除成功！");
					window.location.reload();
				}else {
					alert(jsonstr.message);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var idx = CyberCusDocumentInfoList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length >= 1){
			if(confirm("已选择【"+idx.length+"】条记录,是否确定要删除？该操作不能恢复。")){
				var paramStr = CyberCusDocumentInfoList._obj.getParamStr(['file_path']);
				var url = '<emp:url action="deleteCyberCusDocumentInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		}else {
			alert('请至少选择一条记录！');
		}
	};


	
	//下载
	function doDownLoadCyberCusDocumentInfo() {
		var idx = CyberCusDocumentInfoList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length >= 1){
			var paramStr = CyberCusDocumentInfoList._obj.getParamStr(['file_path']);
			var url = '<emp:url action="downLoadCyberCusDocumentInfoRecordOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else {
			alert('请至少选择一条记录！');
		}
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
   <emp:gridLayout id="DocumentGroup" title="输入查询条件" maxColumn="3">
   		<emp:date id="file_date" label="文件日期" />
		<emp:text id="file_name" label="文件名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="deleteCyberCusDocumentInfo" label="删除文档" op="remove"/>
		<emp:button id="downLoadCyberCusDocumentInfo" label="下载文档" op="download"/>
	</div>

	<emp:table icollName="CyberCusDocumentInfoList" selectType="2" url="pageCyberCusDocumentInfoQuery.do">
		<emp:text id="file_name" label="文件名称" />
		<emp:text id="file_path2" label="文件路径" />
		<emp:text id="file_date" label="文件日期" />
		<emp:text id="file_path" label="文件路径" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    