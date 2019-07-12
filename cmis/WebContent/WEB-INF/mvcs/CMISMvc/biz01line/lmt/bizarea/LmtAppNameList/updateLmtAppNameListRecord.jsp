<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnload(){
		LmtAppNameList.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppNameList.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow2','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	function doClose(){
		window.close();
	}
	
	function doSure(){
		if(LmtAppNameList._checkAll()){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "dayError"){
						alert("请调整期限!");
					}else
					if(flag=="suc"){
						alert("保存成功!");
						window.opener.location.reload();
						window.close();
					 }else{
						alert("客户已存在!");
					 }
				}
			};
			var handleFailure = function(o){
			};
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
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="updateLmtAppNameListRecord.do" method="POST">
			<emp:gridLayout id="LmtAppNameListGroup" maxColumn="2" title="名单表">
			<emp:text id="LmtAppNameList.serno" label="流水号" maxlength="40" required="true" readonly="true" />
			<emp:select id="LmtAppNameList.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE" defvalue="02" readonly="true"/>
			<emp:text id="LmtAppNameList.cus_id" label="客户码" readonly="true" required="true" />
			<emp:text id="LmtAppNameList.cus_id_displayname" label="客户名称" readonly="true" />
			<emp:text id="LmtAppNameList.bail_rate" label="保证金比例" dataType="Percent" required="true"/>
			
			<emp:textarea id="LmtAppNameList.memo" label="备注" maxlength="400" required="false" colSpan="2" />
			<emp:select id="LmtAppNameList.is_limit_set" label="是否进行额度设置" required="true" dictname="STD_ZX_YES_NO" hidden="true" defvalue="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sure" label="确定" />
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
