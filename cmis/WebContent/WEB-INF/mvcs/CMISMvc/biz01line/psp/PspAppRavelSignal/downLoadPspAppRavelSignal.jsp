<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>


<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<html>
<head>
<title>下载专区页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*XD140718027预警信息模块新增下载专区*/
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		file_name._toForm(form);
		PubDocumentInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.DocumentGroup.reset();
	};
	
	function doGetAddPubDocumentInfoPage() {
		var url = '<emp:url action="toAddPubDocumentInfoRecordOp.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//异步删除
	function doDeletePubDocumentInfo() {
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

		var paramStr = PubDocumentInfoList._obj.getParamStr(['file_pk','file_path']);
		if (paramStr != null) {
			if(confirm("是否确定要删除？该操作不能恢复。")){
				var url = '<emp:url action="deletePubDocumentInfoRecordOp.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		}else {
			alert('请选择一条记录！');
		}
	};

	//下载
	function doDownLoadPubDocumentInfo() {
		var paramStr = PubDocumentInfoList._obj.getParamStr(['file_pk','file_path','file_name']);
		if (paramStr != null) {
			var url = '<emp:url action="downLoadPubDocumentInfoRecordOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条记录！');
			//ymPrompt.alert({message:'请选择一条记录！',title:'系统提示',handler:null});
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body >
	
	<form  method="POST" action="#" id="queryForm"></form>
	<emp:gridLayout id="DocumentGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="file_name" label="文件名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
		
	<div align="left">
	<% if(!context.containsKey("help")){ %>
		<emp:button id="getAddPubDocumentInfoPage" label="上传文档" />
		<emp:button id="deletePubDocumentInfo" label="删除文档" />
	<%} %>
		<emp:button id="downLoadPubDocumentInfo" label="下载文档" />
	</div>
	<div class="info_table">
		<emp:table icollName="PubDocumentInfoList"  url="pagePubDocumentInfoList.do" >
			<emp:text id="file_pk" label="文件主键" hidden="true"/>
			<emp:text id="file_name" label="文件名称" />
			<emp:text id="file_path" label="文件路径" hidden="true"/>
			<emp:text id="task_id" label="任务ID" hidden="true"/>
			<emp:text id="file_size" label="文件大小(K)" dataType="Currency"/>
			<emp:text id="file_unit" label="文件单位" hidden="true"/>
			<emp:text id="file_memo" label="文件描述" />
			<emp:text id="create_date" label="上传时间" />
		</emp:table>
	</div>
	
</body>
</html>
</emp:page>

