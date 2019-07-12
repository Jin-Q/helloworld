<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	String serno = request.getParameter("serno");
%>
<script type="text/javascript">

		function doSub(){
			if(IqpOverseeCusinfo._checkAll()){
				var form = document.getElementById("submitForm");
				IqpOverseeCusinfo._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						var jsonstr = eval("("+o.responseText+")");
						var flag = jsonstr.flag;
						if(flag == "success"){
                             alert("保存成功！");
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

		function doClose(){
			window.close();
		}
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpOverseeCusinfoRecord.do" method="POST">		
		<emp:gridLayout id="IqpOverseeCusinfoGroup" title="监管客户信息" maxColumn="2">			
			<emp:text id="IqpOverseeCusinfo.cus_name" label="客户名称" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.goods_name" label="储运货物名称" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.transfer_qnt" label="年吞吐量(吨)" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.avg_storage" label="平均仓储量(吨)" maxlength="20" required="true" />
			<emp:text id="IqpOverseeCusinfo.biz_yearn" label="年均业务收入(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpOverseeCusinfo.coop_bank" label="合作银行" maxlength="32" required="false" />
			<emp:text id="IqpOverseeCusinfo.cusinfo_id" label="客户信息编号" maxlength="32" required="false" hidden="true" />
			<emp:text id="IqpOverseeCusinfo.serno" label="流水主键" maxlength="32" required="false" hidden="true" defvalue="<%=serno%>"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

