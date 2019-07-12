<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	/*XD140718027：检查页面添加返回按钮*/
	function doSub(){
    	var task_id = PspIostoreDoc.task_id._getValue();
    	var cus_id = PspIostoreDoc.cus_id._getValue();
		var form = document.getElementById("submitForm");
		if(PspIostoreDoc._checkAll()){
			PspIostoreDoc._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id;
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	function doReturn(){
		var task_id = PspIostoreDoc.task_id._getValue();
		var cus_id = PspIostoreDoc.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePspIostoreDocRecord.do" method="POST">
		<emp:gridLayout id="PspIostoreDocGroup" title="出入库清单" maxColumn="2">
			<emp:select id="PspIostoreDoc.doc_type" label="类别" required="true" dictname="STD_ZB_PSP_IOSTORE_TYPE"/>
			<emp:text id="PspIostoreDoc.goods_name" label="货物名称" maxlength="100" required="true" />
			<emp:select id="PspIostoreDoc.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/>
			<emp:text id="PspIostoreDoc.qnt" label="数量" maxlength="38" required="true" dataType="Int"/>
			<emp:text id="PspIostoreDoc.total_price" label="总价值" maxlength="16" required="true" dataType="Currency"/>
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspIostoreDoc.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" readonly="true" colSpan="2"/>
			<emp:textarea id="PspIostoreDoc.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspIostoreDoc.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspIostoreDoc.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspIostoreDoc.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspIostoreDoc.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspIostoreDoc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspIostoreDoc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspIostoreDoc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspIostoreDoc.task_id" label="任务编号" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" />
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<!--<emp:button id="reset" label="重置"/> -->
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>
