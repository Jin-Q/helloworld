<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doOnLoad(){
		//剔除条线
		var optionJosn4BL = "BL100,BL200,BL300";
		var options4BL =WfiLvCreditRight.belg_line._obj.element.options;		
		for ( var i = options4BL.length - 1; i >= 0; i--) {
			if(optionJosn4BL.indexOf(options4BL[i].value)<0){
				options4BL.remove(i);
			}
		}
		//剔除担保方式
		var optionJosn4AM = "100,200,300,400,210,220";
		var options4AM =WfiLvCreditRight.assure_main._obj.element.options;		
		for ( var i = options4AM.length - 1; i >= 0; i--) {
			if(optionJosn4AM.indexOf(options4AM[i].value)<0){
				options4AM.remove(i);
			}
		}
	};
	function doUpdateWLCRight(){
		if(!WfiLvCreditRight._checkAll()){
			return false;
		}else{
			var form = document.getElementById("submitForm");
			WfiLvCreditRight._toForm(form);
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var msg = jsonstr.flag;
					if(msg=='success'){
			            alert('修改成功!');
			            window.opener.location.reload();
			            doReturn();
					}else if(msg=='fail'){
						alert('修改失败!');
					}else{
						alert('修改成功，但写入操作记录异常！');
					}
				}
			};
			var handleFailure = function(o) {
				alert("修改失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	
	function doReturn(){
		var cb_org_id  = WfiLvCreditRight.org_id._getValue();
		var url = '<emp:url action="getCommBranchCreditRightUpdatePage.do"/>?cb_org_id='+cb_org_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="updateWfiLvCreditRightRecord.do" method="POST">
		<emp:gridLayout id="WfiLvCreditRightGroup" title="社区支行授信审批权限配置" maxColumn="2"> 	
			<emp:select id="WfiLvCreditRight.right_type" label="权限类型" dictname="STD_ZB_RIGHT_TYPE" readonly="true" colSpan="2"/>
			<emp:select id="WfiLvCreditRight.belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" required="true" readonly="true"/>
			<emp:select id="WfiLvCreditRight.assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" readonly="true"/>
			<emp:text id="WfiLvCreditRight.new_crd_amt" label="新增授信审批金额（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="WfiLvCreditRight.stock_crd_amt" label="存量授信审批金额（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="WfiLvCreditRight.pk_id" label="主键" maxlength="36" readonly="true" hidden="true"/>
			<emp:select id="WfiLvCreditRight.org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" hidden="true"/>
			<emp:text id="WfiLvCreditRight.org_id" label="社区支行机构码" maxlength="16" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateWLCRight" label="修改" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
