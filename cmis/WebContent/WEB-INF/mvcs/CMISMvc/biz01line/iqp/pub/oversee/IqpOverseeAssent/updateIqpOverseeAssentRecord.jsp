<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% String type=(String)request.getParameter("type"); %>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdate(){
			if(IqpOverseeAssent._checkAll()){
				var form = document.getElementById("submitForm");
				IqpOverseeAssent._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						var jsonstr = eval("("+o.responseText+")");
						var flag = jsonstr.flag;
						if(flag == "success"){
                             alert("修改成功！");
                             window.close();
                             window.opener.location.reload(); 
						}else {
							alert("发生异常！");
						}
					}
				};
				var callback = {
					success:handleSuccess,
					failure:null
				};
				var postData = YAHOO.util.Connect.setForm(form);	
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
			}
		};

		function doLoad()
		{
			var type='<%=type%>'
			if(type=='main')
			{
				IqpOverseeAssent.util_term._obj._renderHidden(false);
				IqpOverseeAssent.util_term._obj._renderRequired(true);
				IqpOverseeAssent.util_case._obj._renderHidden(false);	
				IqpOverseeAssent.util_case._obj._renderRequired(true);
			}else
			{
				IqpOverseeAssent.util_term._obj._renderHidden(true);
				IqpOverseeAssent.util_term._obj._renderRequired(false);
				IqpOverseeAssent.util_case._obj._renderHidden(true);	
				IqpOverseeAssent.util_case._obj._renderRequired(false);		
			}
		}	
		
		function doClose()
		{
			window.close();
		}
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateIqpOverseeAssentRecord.do" method="POST">
		<emp:gridLayout id="IqpOverseeAssentGroup" maxColumn="2" title="资产明细">
			<emp:text id="IqpOverseeAssent.assent_name" label="资产名称" maxlength="100" required="true" />
			<emp:text id="IqpOverseeAssent.assent_qnt" label="资产数量" maxlength="10" required="true" dataType="Int"/>	
			<emp:text id="IqpOverseeAssent.fore_value" label="账面净值" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpOverseeAssent.reckon_value" label="估计现值" maxlength="18" required="true" dataType="Currency" />	
			<emp:select id="IqpOverseeAssent.util_case" label="目前使用情况" required="true" dictname="STD_ZX_FIELD_OWNER" />				
			<emp:text id="IqpOverseeAssent.util_term" label="已使用年限(年)" maxlength="10" required="true" hidden="true"/>
			<emp:text id="IqpOverseeAssent.wrr_proof" label="权利凭证" maxlength="40" required="true" />
			<emp:select id="IqpOverseeAssent.is_pldimn" label="是否已抵质押" required="true" dictname="STD_ZX_YES_NO" />					
			<emp:select id="IqpOverseeAssent.assent_type" label="资产类别" required="false" dictname="STD_ZB_ASSENT_TYPE" hidden="true"/>
			<emp:text id="IqpOverseeAssent.assent_id" label="资产编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeAssent.serno" label="业务流水号" maxlength="32" required="false" hidden="true"/>
			<emp:textarea id="IqpOverseeAssent.memo" label="备注" maxlength="500" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
