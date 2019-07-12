<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	String menuId ="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if("view".equals(op)){
			request.setAttribute("canwrite","");
		}    
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if("cont".equals(cont)){   
			request.setAttribute("canwrite","");
		}
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetOrgRegi._checkAll()){
			IqpAssetOrgRegi._toForm(form);
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
						alert("保存成功!");
						var url = '<emp:url action="queryIqpAssetOrgRegiList.do"/>?serno='+IqpAssetOrgRegi.serno._getValue();
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("保存异常!");
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
		}else {
			return false;
		}
	};

	function returnOrg(data){
		IqpAssetOrgRegi.asset_org_id._setValue(data.asset_org_id._getValue());
		IqpAssetOrgRegi.asset_org_id_displayname._setValue(data.asset_org_name._getValue());
		IqpAssetOrgRegi.acct_no._setValue(data.acct_no._getValue());
		IqpAssetOrgRegi.acct_name._setValue(data.acct_name._getValue());
		IqpAssetOrgRegi.acctsvcr_no._setValue(data.acctsvcr_no._getValue());
		IqpAssetOrgRegi.acctsvcr_name._setValue(data.acctsvcr_name._getValue());

		var serno = IqpAssetOrgRegi.serno._getValue();
		var asset_org_id = data.asset_org_id._getValue();
		var url = '<emp:url action="checkIqpAssetOrgRegiApp.do"/>?serno='+serno+'&asset_org_id='+asset_org_id;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					
				}else {
					alert(msg);

					IqpAssetOrgRegi.asset_org_id._setValue("");
					IqpAssetOrgRegi.asset_org_id_displayname._setValue("");
					IqpAssetOrgRegi.acct_no._setValue("");
					IqpAssetOrgRegi.acct_name._setValue("");
					IqpAssetOrgRegi.acctsvcr_no._setValue("");
					IqpAssetOrgRegi.acctsvcr_name._setValue("");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAssetOrgRegiRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetOrgRegiGroup" maxColumn="2" title="机构登记">
			<emp:text id="IqpAssetOrgRegi.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="${context.serno}"/>
			<emp:pop id="IqpAssetOrgRegi.asset_org_id" label="机构代码" required="true" buttonLabel="选择" url="queryIqpAssetOrgPop.do?returnMethod=returnOrg&restrictUsed=false"/>
			<emp:text id="IqpAssetOrgRegi.asset_org_id_displayname" label="机构名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetOrgRegi.asset_org_type" label="机构类型" required="true" dictname="STD_ZB_ASSET_ORG_TYPE"/>
			<emp:text id="IqpAssetOrgRegi.acct_no" label="账号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acct_name" label="账户名" maxlength="80" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acctsvcr_no" label="开户行行号" maxlength="20" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acctsvcr_name" label="开户行行名" maxlength="100" required="false" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="update"/>
			<emp:button id="reset" label="取消" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>
