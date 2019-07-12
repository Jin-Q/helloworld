<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAdd(){
		if(!PrdBasicinfo._checkAll()){
			return false;
		}
		var form = document.getElementById("submitForm");
		PrdBasicinfo._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var prdId = jsonstr.prdid;
				if(flag == "success" & prdId != null){
					var url = '<emp:url action="getPrdBasicinfoUpdatePage.do"/>?prdid='+prdId;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("新增选择产品发生异常！");
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
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};

	function returnManager(data){
		PrdBasicinfo.prdmanager._setValue(data.actorno._getValue());
	}
	function returnMsg(data){
		PrdBasicinfo.supcatalog._setValue(data.id);
		PrdBasicinfo.supcatalog_displayname._setValue(data.label);
	
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdBasicinfoRecord.do" method="POST">
		<emp:gridLayout id="" title="产品配置选择信息" maxColumn="2">
			<emp:select id="PrdBasicinfo.prdkind" label="产品类别" required="true" dictname="STD_ZB_PRD_TYPE"/>
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="40" required="true"/>
			<emp:pop id="PrdBasicinfo.supcatalog_displayname" label="上级目录" url="showDicTree_PRD.do" returnMethod="returnMsg"  required="true" buttonLabel="选择"/>
			<emp:pop id="PrdBasicinfo.prdmanager" label="产品经理" url="getPrdManagerPage.do?returnMethod=returnManager" required="true" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" buttonLabel="选择"/>
			<emp:select id="PrdBasicinfo.prdstatus" label="产品状态" required="false" hidden="true" dictname="STD_PRD_STATE" />
			<emp:text id="PrdBasicinfo.inputid" label="登记人员" maxlength="30" hidden="true" required="false" defvalue="${context.currentUserId}" />
			<emp:text id="PrdBasicinfo.inputdate" label="登记日期" maxlength="10" hidden="true" required="false" defvalue="${context.OPENDAY}" />
			<emp:text id="PrdBasicinfo.orgid" label="登记机构" maxlength="20" hidden="true" required="false" defvalue="${context.organNo}" />
		    <emp:text id="PrdBasicinfo.supcatalog" label="上级目录" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="add" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

