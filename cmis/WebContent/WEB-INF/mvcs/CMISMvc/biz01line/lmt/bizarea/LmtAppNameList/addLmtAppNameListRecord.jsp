<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	String serno = request.getParameter("serno");//申请编号
	String agr_no = request.getParameter("agr_no");//圈商编号
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doOnload(){

	}
	//选择客户
	function returnCus(data){
		LmtAppNameList.cus_name._setValue(data.cus_name._getValue());
		LmtAppNameList.cus_id._setValue(data.cus_id._getValue());
	}
	function doClose(){
		window.close();
	}
	//保存信息
	function doSave(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("保存成功!");
					window.opener.location.reload();
					window.close();
				}else{
					alert("保存失败!");
				}
			}
		};
		var handleFailure = function(o){};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		//设置form
		var form = document.getElementById("submitForm");
		LmtAppNameList._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}

	/**保存前先校验:1.是否已存在该条入圈申请中
				   2.是否已存在该圈商中
				   3.是否存在有效圈商名单中
				   4.是否存在在途的入/退圈申请中*/
	function doCheckBeforeSave(){
		if(!LmtAppNameList._checkAll()){
			return;
		}
		var cus_id = LmtAppNameList.cus_id._getValue();
		var url = '<emp:url action="chkBeforeAddLmtAppNameRecord.do"/>&cus_id='+cus_id+"&serno=<%=serno%>&agr_no=<%=agr_no%>";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("入圈新增校验失败!");
					return;
				}
				var flag=jsonstr.flag;
				var returnMsg = jsonstr.returnMsg;
				if(flag=="success"){
					doSave();
				}else if(flag=="existThis"){//该客户已存在该笔申请中
					alert(returnMsg);
				}else{
					if(confirm(returnMsg)){
						doSave();
					}
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("入圈新增校验失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="addLmtAppNameListRecord.do" method="POST">
		<emp:gridLayout id="LmtAppNameListGroup" maxColumn="2" title="名单表">
			<emp:text id="LmtAppNameList.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="<%=serno%>"/>
			<emp:select id="LmtAppNameList.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE" defvalue="02" readonly="true"/>
			<emp:pop id="LmtAppNameList.cus_id" label="客户码" readonly="false" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and belg_line='BL300'&returnMethod=returnCus" required="true" />
			<emp:text id="LmtAppNameList.cus_name" label="客户名称" readonly="true" />
			<emp:text id="LmtAppNameList.bail_rate" label="保证金比例" dataType="Percent" required="true"/>
			<emp:textarea id="LmtAppNameList.memo" label="备注" maxlength="400" required="false" colSpan="2" />
			
			<emp:select id="LmtAppNameList.is_limit_set" label="是否进行额度设置" required="true" dictname="STD_ZX_YES_NO" defvalue="2" hidden="true" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="checkBeforeSave" label="确定" />
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

