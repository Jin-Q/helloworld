<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*****2019-01-23 jiangcuihua 附件上传页面  start******/
	function doUploadFileInfoPage(){
		var url = '<emp:url action="uploadFileInfoPage.do"/>?file_type=${context.file_type}&serno=${context.serno}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*****2019-01-23 jiangcuihua 附件上传页面  end******/
	
	//异步删除
	function doDeleteUploadFileInfo() {
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
	function doViewUploadFileInfo() {
		var paramStr = PubDocumentInfoList._obj.getParamStr(['file_pk','file_path','file_name']);
		if (paramStr != null) {
			var url = '<emp:url action="downLoadUploadFileInfoRecordOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条记录！');
		}
	};
	
	//在线预览
	function doOnlineUploadFileInfo(){
		var fileName = PubDocumentInfoList._obj.getParamValue(['file_name']);
		if(fileName!=null){
			var paramStr = PubDocumentInfoList._obj.getParamStr(['file_pk','file_path','file_name']);
			if(fileName.indexOf('.png')>0||fileName.indexOf('.jpg')>0){
				var url = '<emp:url action="viewFileOnline.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				var url = '<emp:url action="downLoadUploadFileInfoRecordOp.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			
		}else{
			alert('请选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<div id="operation" align="left">
		<emp:button id="uploadFileInfoPage" label="上传文档" />
		<emp:button id="viewUploadFileInfo" label="调阅文档" />
		<emp:button id="onlineUploadFileInfo" label="在线预览" />
		<emp:button id="deleteUploadFileInfo" label="删除文档" />
	</div>
	<emp:table icollName="PubDocumentInfoList" pageMode="true" url="pagePubDocumentInfoListQuery.do?file_type=${context.file_type}&serno=${context.serno}">
		<emp:text id="file_pk" label="文件编号" />
		<emp:text id="file_name" label="文档名称" />
		<emp:text id="file_type" label="文档类型" dictname="STD_ZB_IMAGE_TYPE"/>	
		<emp:text id="file_path" label="文档路径" hidden="true"/>
		<emp:text id="serno" label="业务流水" />		
		<emp:text id="create_man" label="创建人" hidden="true"/>
		<%-- <emp:text id="create_user_name" label="创建人" /> --%>
		<emp:text id="create_date" label="创建日期" />
	</emp:table>
</body>
</html>
</emp:page>
    