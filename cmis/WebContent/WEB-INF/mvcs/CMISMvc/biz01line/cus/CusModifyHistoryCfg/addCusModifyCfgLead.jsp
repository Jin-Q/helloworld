<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

/*--user code begin--*/

    function doReturn() {
		var url = '<emp:url action="queryCusModifyHistoryCfgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doOnLoad(){
		CusModifyHistoryCfg.model_id._obj.addOneButton("loanform","选择",getAppForm);
	};

	//获得客户表模型Id
	function getAppForm(){
		var url = '<emp:url action="getAllFormForSelectPop.do"/>&returnMethod=getAppFormPop&flag=Cus';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};

	function getAppFormPop(data){
		CusModifyHistoryCfg.model_id._setValue(data.modelId._getValue());
	};

	//下一步操作
	function doGetNext(){
		var model_id = CusModifyHistoryCfg.model_id._getValue();
		if(model_id==null||model_id==''){
			alert('请先选择表模型！');
			return ;
		}
		var url = '<emp:url action="checkTableModelIsCfg.do"/>&model_id='+model_id;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("主担保金额校验失败!");
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="exist"){//存在配置
					//form.action = "getCusModifyHistoryCfgUpdatePage.do";
					var url = '<emp:url action="getCusModifyHistoryCfgUpdatePage.do"/>&model_id='+model_id;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{//不存在
					var form = document.getElementById("submitForm");
					CusModifyHistoryCfg._toForm(form);
					form.submit();
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("主担保金额校验失败，请联系管理员");
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
<body class="page_content"  onload="doOnLoad()">
	<emp:form id="submitForm" action="getCusModifyHistoryCfgAddPage.do" method="POST">
		<emp:gridLayout id="CusModifyHistoryCfgGroup" title="新增引导页" maxColumn="1">
			<emp:text id="CusModifyHistoryCfg.model_id" label="表模型ID" maxlength="60" required="true"/>
			<emp:text id="CusModifyHistoryCfg.input_id" label="登记人" maxlength="60" readonly="true" defvalue="$currentUserId" hidden="true"/> 
			<emp:text id="CusModifyHistoryCfg.input_br_id" label="登记机构" maxlength="60" readonly="true" defvalue="$organNo" hidden="true"/> 
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="getNext" label="下一步" op="add" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

