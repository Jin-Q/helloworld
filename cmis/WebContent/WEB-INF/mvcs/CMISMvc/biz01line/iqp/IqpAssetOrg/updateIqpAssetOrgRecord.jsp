<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetOrg._checkAll()){
			IqpAssetOrg._toForm(form);
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
						alert("修改成功!");
						var url = '<emp:url action="queryIqpAssetOrgList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改异常!");
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

	function doReturn() {
		history.go(-1);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAssetOrgRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetOrgGroup" title="参与机构信息" maxColumn="2">
			<emp:text id="IqpAssetOrg.asset_org_id" label="机构代码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAssetOrg.asset_org_name" label="机构名称" maxlength="80" required="true" />
			<emp:select id="IqpAssetOrg.org_nature" label="机构性质" required="true" dictname="STD_ORG_QLTY"/>
			<emp:text id="IqpAssetOrg.org_address" label="注册地址" maxlength="200" required="true" />
			<emp:text id="IqpAssetOrg.org_repr" label="法人代表" maxlength="40" required="true" />
			<emp:text id="IqpAssetOrg.pcode" label="邮政编码" maxlength="20" required="true" />
			<emp:text id="IqpAssetOrg.phone" label="联系电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpAssetOrg.fax" label="传真" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpAssetOrg.acct_no" label="账号" maxlength="40" required="true" />
			<emp:text id="IqpAssetOrg.acct_name" label="账户名" maxlength="80" required="true" />
			<emp:text id="IqpAssetOrg.acctsvcr_no" label="开户行行号" maxlength="20" required="true" />
			<emp:text id="IqpAssetOrg.acctsvcr_name" label="开户行行名" maxlength="100" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="2">
			<emp:text id="IqpAssetOrg.input_id_displayname" label="登记人"  required="false" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_br_id_displayname" label="登记机构"  required="false" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_date" label="登记日期" maxlength="10" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_id" label="登记人" maxlength="40" required="false" hidden="true" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>
