<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAddWfiCallBackDisc(){
		var result = WfiCallBackDisc._checkAll();
		if(result){
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
					var msg = jsonstr.msg;
					if("success" == flag){
						var form = document.getElementById('submitForm');
						WfiCallBackDisc._toForm(form);
				    	form.submit();
					}else {
						alert(msg);
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
			var enname=WfiCallBackDisc.cb_enname._getValue();
			var url = '<emp:url action="CheckWfiCallBackDiscByEnName.do"/>?cb_enname='+enname;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfiCallBackDiscRecord.do" method="POST">
		
		<emp:gridLayout id="WfiCallBackDiscGroup" title="打回标识配置信息" maxColumn="2">
			<emp:text id="WfiCallBackDisc.pk_id" label="主键" maxlength="36" colSpan="2" hidden="true" />
			<emp:text id="WfiCallBackDisc.cb_enname" label="打回标识号" required="true" />
			<emp:text id="WfiCallBackDisc.cb_cnname" label="打回标识"  required="true" />
			<emp:textarea id="WfiCallBackDisc.cb_memo" label="内容说明" maxlength="200" colSpan="2" required="false" />
			<emp:text id="WfiCallBackDisc.attr1" label="attr1" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.attr2" label="attr2" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.attr3" label="attr3" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.or_no" label="排序号" maxlength="50" required="true" />
			<emp:select id="WfiCallBackDisc.is_inuse" label="是否有效"  required="true" dictname="STD_ZX_YES_NO" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addWfiCallBackDisc" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

