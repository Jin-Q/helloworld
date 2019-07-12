<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%
	
	// 下方iframe中src的参数说明
	// allow-doc : 文件上传类型;
	// allow-size : 最大支持文件上传大小;
	// allow-num : 最大支持文件上传数量;
	// isSelection : 是否允许多文件选择;
	// isMultiple : 是否允许多文件上传;
	// afterUrl : 上传后续处理URL;
	// isModule : 是否根据模块划分存储路径;
	// isFunction : 是否根据功能划分存储路径;
	// returnMethod : 后续回调方法;

%>
<emp:page>
<html>
<head>
	<jsp:include page="/EUIInclude.jsp" flush="true" />
	<jsp:include page="/include.jsp" flush="true" />
</head>
<body id="managerFile" style="padding:0px;">
<!-- 用content 传递所需要的内容，excel导入的时候要用到 
WQJ  4.27
-->
	<emp:tabGroup id="fileUploadTabs" mainTab ="maintab" > 
		<emp:tab id="maintab" label="文件上传" >
			<iframe scrolling="auto" frameborder="0" style="width:100%;height: 98%"
			src="getUploadPage.do?EMP_SID=${context.EMP_SID}&allow-doc=xls&allow-size=10&allow-num=1&isSelection=false&isMultiple=false&afterUrl=${param.afterUrl}&rd=${param.rd}&content=${param.content}&isModule=false&isFunction=false&returnMethod=returnMethod&testtt=66"></iframe>
		</emp:tab>
	</emp:tabGroup>
 	<script type="text/javascript">

		function tabChange(title, index){

		}
		function returnMethod(fs, returnResponses){
			var flag = returnResponses[0].status;
			var message = returnResponses[0].message;
			var param = returnResponses[0].params;
			if(flag == "success"){
				alert(message); 
				window.location.reload();
			} else { 
				alert(message); 
			}
		}
		
	</script>
</body>
</html>
</emp:page>