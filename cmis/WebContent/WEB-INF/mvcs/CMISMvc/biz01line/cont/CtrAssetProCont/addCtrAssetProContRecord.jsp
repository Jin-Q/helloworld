<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(CtrAssetProCont._checkAll()){
			CtrAssetProCont._toForm(form);
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
						alert("新增成功!");
						var url = '<emp:url action="queryCtrAssetProContList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("新增异常!"); 
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCtrAssetProContRecord.do" method="POST">
		
		<emp:gridLayout id="CtrAssetProContGroup" title="资产项目管理" maxColumn="2">
			<emp:text id="CtrAssetProCont.cont_no" label="项目编号" maxlength="40" required="true" />
			<emp:text id="CtrAssetProCont.serno" label="业务编号" maxlength="40" required="false" />
			<emp:text id="CtrAssetProCont.pro_name" label="项目名称" maxlength="80" required="false" />
			<emp:text id="CtrAssetProCont.pro_short_name" label="项目简称" maxlength="80" required="false" />
			<emp:text id="CtrAssetProCont.pro_type" label="项目类型" maxlength="5" required="false" />
			<emp:text id="CtrAssetProCont.pro_org" label="资产所属机构" maxlength="20" required="false" />
			<emp:text id="CtrAssetProCont.pro_short_memo" label="项目简介" maxlength="200" required="false" />
			<emp:text id="CtrAssetProCont.pro_amt" label="项目金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="CtrAssetProCont.pro_qnt" label="笔数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="CtrAssetProCont.pack_date" label="封包日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.issue_qnt" label="发行总量" maxlength="16" required="false" dataType="Int" />
			<emp:text id="CtrAssetProCont.issue_date" label="发行日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.int_start_date" label="起息日" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.end_date" label="法定到期日" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.final_date" label="终结日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.cont_status" label="项目状态" maxlength="5" required="false" />
			<emp:text id="CtrAssetProCont.approve_date" label="审批通过日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.input_id" label="登记人" maxlength="40" required="false" />
			<emp:text id="CtrAssetProCont.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="CtrAssetProCont.input_date" label="登记日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="CtrAssetProCont.prd_id" label="产品编码" maxlength="6" required="false" />
			<emp:text id="CtrAssetProCont.cur_type" label="币种" maxlength="5" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

