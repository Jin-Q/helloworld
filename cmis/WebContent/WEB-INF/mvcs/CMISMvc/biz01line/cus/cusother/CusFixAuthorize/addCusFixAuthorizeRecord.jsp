<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//选择授权人
	function setconId(data){
		CusFixAuthorize.auth_id._setValue(data.actorno._getValue());
		CusFixAuthorize.auth_id_displayname._setValue(data.actorname._getValue());
	}

	//主管机构
	function getOrgID(data){
		CusFixAuthorize.manager_br_id._setValue(data.organno._getValue());
		CusFixAuthorize.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	//确认授权码
	function checkCheckCode(){
		var checkCode = CusFixAuthorize.checkcode._getValue();
		var checkCodeChk = CusFixAuthorize.checkcodechk._getValue();
		if(checkCode!=null&&checkCode!=''&&checkCodeChk!=null&&checkCodeChk!=''){
			if(checkCode!=checkCodeChk){
				alert('两次密码不符，请重新输入！');
				CusFixAuthorize.checkcode._setValue('');
				CusFixAuthorize.checkcodechk._setValue('');
				return;
			}
		}
	}

	//校验起始日
	function checkStartDate(){
		var startDate = CusFixAuthorize.startdate._getValue();
		var endDate = CusFixAuthorize.enddate._getValue();
		if(startDate!=null&&startDate!=''&&endDate!=null&&endDate!=''){
			if(startDate>=endDate){
				alert('结束日期要大于开始日期！');
				CusFixAuthorize.startdate._setValue('');
				return;
			}
		}
	}
	
	//校验到期日
	function checkEndDate(){
		var openDay = '${context.OPENDAY}';
		var startDate = CusFixAuthorize.startdate._getValue();
		var endDate = CusFixAuthorize.enddate._getValue();
		if(endDate!=null&&endDate!=''){
			if(openDay>endDate){
				alert('结束日期不能小于当前日期！');
				CusFixAuthorize.enddate._setValue('');
				return;
			}
			if(startDate!=null&&startDate!=''){
				if(startDate>=endDate){
					alert('结束日期要大于开始日期！');
					CusFixAuthorize.enddate._setValue('');
					return;
				}
			}
		}
	}

	function doAddCusFixAuthorize(){
		var form = document.getElementById("submitForm");
		CusFixAuthorize._toForm(form);
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
					alert("新增成功！");
					var serno = CusFixAuthorize.serno._getValue();
					var url = '<emp:url action="queryCusFixAuthorizeList.do"/>?serno='+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("新增失败,当前授权人已存在一笔有效授权信息！");
					CusFixAuthorize.auth_id_displayname._setValue("");
					CusFixAuthorize.manager_br_id_displayname._setValue("");
					CusFixAuthorize.checkcode._setValue("");
					CusFixAuthorize.checkcodechk._setValue("");
					CusFixAuthorize.startdate._setValue("");
					CusFixAuthorize.enddate._setValue("");
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
	}

	function doReturn(){
		var url = '<emp:url action="queryCusFixAuthorizeList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusFixAuthorizeRecord.do" method="POST">
		<emp:gridLayout id="CusFixAuthorizeGroup" title="客户修改授权" maxColumn="2">
			<emp:pop id="CusFixAuthorize.auth_id_displayname" label="授权人" required="true" url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusFixAuthorize.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:password id="CusFixAuthorize.checkcode" label="授权码" maxlength="32" required="true" />
			<emp:password id="CusFixAuthorize.checkcodechk" label="确认授权码" maxlength="32" required="true" onblur="checkCheckCode()"/>
			<emp:date id="CusFixAuthorize.startdate" label="开始日期" required="true" onblur="checkStartDate()"/>
			<emp:date id="CusFixAuthorize.enddate" label="结束日期" required="true" onblur="checkEndDate()"/>
			<emp:text id="CusFixAuthorize.serno" label="流水号" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusFixAuthorize.auth_id" label="授权人" maxlength="20" required="true"  defvalue="${context.currentuserid}" hidden="true"/>
			<emp:text id="CusFixAuthorize.manager_br_id" label="管理机构" maxlength="20" required="true"  defvalue="${context.currentuserid}" hidden="true"/>
			<emp:text id="CusFixAuthorize.input_br_id" label="登记机构" maxlength="20" required="true"  defvalue="${context.organNo}" hidden="true"/>
			<emp:text id="CusFixAuthorize.input_id" label="登记人" maxlength="20" required="true"  defvalue="${context.currentUserId}" hidden="true"/>
			<emp:select id="CusFixAuthorize.status" label="状态" required="true" readonly="true" defvalue="2" dictname="STD_ZB_STATUS"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addCusFixAuthorize" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>