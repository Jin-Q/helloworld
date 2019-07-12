<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
		LmtIndusListMana.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	};
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtIndusListMana.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function doSubmits(){
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
				if(flag=='success'){
		            alert('保存成功!');
		            //doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = LmtIndusListMana._checkAll();
		if(result){
			LmtIndusListMana._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusListManaList.do"/>?agr_no=${context.agr_no}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateLmtIndusListManaRecord.do" method="POST">
	<emp:gridLayout id="LmtIndusListManaGroup" title="行业名单管理表" maxColumn="2">
			<emp:text id="LmtIndusListMana.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="LmtIndusListMana.cus_id" label="客户码" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIndusListMana.cus_id_displayname" label="客户名称" colSpan="2" required="true"
			readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="LmtIndusListMana.is_do_limit" label="是否进行额度设置" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtIndusListMana.status" label="状态" required="true" dictname="STD_ZB_LIST_STATUS" readonly="true" />
			<emp:textarea id="LmtIndusListMana.memo" label="备注" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="修改" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
