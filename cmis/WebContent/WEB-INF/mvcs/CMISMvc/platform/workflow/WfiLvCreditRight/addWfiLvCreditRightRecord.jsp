<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

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
		//剔除社区支行选项
		var optionJosn4OL = "1,2,3,4,5,6";
		var options4OL =WfiLvCreditRight.org_lvl._obj.element.options;		
		for ( var i = options4OL.length - 1; i >= 0; i--) {
			if(optionJosn4OL.indexOf(options4OL[i].value)<0){
				options4OL.remove(i);
			}
		}
	};

	function doSaveWLCRight(){
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
					if(msg=="success"){
			            alert("保存成功!");
			            doReturn();
					}else if(msg == "existSetting"){
						alert("已存在该担保方式的配置，不能再做新增操作！");
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
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	
	function doReturn(){
		var url = '<emp:url action="queryWfiLvCreditRightList.do"/>?right_type=01';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addWfiLvCreditRightRecord.do" method="POST">
		
		<emp:gridLayout id="WfiLvCreditRightGroup" title="授权等级授信审批权限配置" maxColumn="2">
			<emp:select id="WfiLvCreditRight.org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" required="true"/>
			<emp:select id="WfiLvCreditRight.right_type" label="权限类型" dictname="STD_ZB_RIGHT_TYPE" readonly="true" defvalue="01" />
			<emp:select id="WfiLvCreditRight.belg_line" label="机构条线" dictname="STD_ZB_BUSILINE" required="true"/>
			<emp:select id="WfiLvCreditRight.assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" />
			<emp:text id="WfiLvCreditRight.new_crd_amt" label="新增授信审批金额（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="WfiLvCreditRight.stock_crd_amt" label="存量授信审批金额（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="WfiLvCreditRight.pk_id" label="主键" maxlength="36" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="saveWLCRight" label="确定"/>
			<emp:button id="return" label="返回列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

