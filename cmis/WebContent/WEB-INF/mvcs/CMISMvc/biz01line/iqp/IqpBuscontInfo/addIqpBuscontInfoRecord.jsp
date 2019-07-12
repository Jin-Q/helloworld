<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String poNo= request.getParameter("po_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){ 
		if('<%=poNo%>'!='null')IqpBuscontInfo.po_no._setValue('<%=poNo%>');
	}
	function doReturn(){
		window.close();
		}
	function doAdd(){
		var form = document.getElementById("submitForm");
		IqpBuscontInfo._checkAll();
		if(IqpBuscontInfo._checkAll()){
			var start_date = IqpBuscontInfo.start_date._getValue();
			var end_date = IqpBuscontInfo.end_date._getValue();
			if(end_date!=null&&end_date!=""){
			if(start_date>end_date){
					alert("贸易合同到期日早于贸易合同起始日！");
					return;
					}
				}
			IqpBuscontInfo._toForm(form);
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
						alert("保存成功！");
						window.opener.location.reload();
						window.close();
					}else if(flag=="exist") {
						alert("该贸易合同编号已存在！");
						return;
					}else {
						alert("保存失败！");
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
	}
	/* modified by wangj 需求编号【HS141110017】保理业务改造  begin*/
	//选择客户POP框返回方法
	function returnCus(data){
		IqpBuscontInfo.sup_mat_cprt._setValue(data.cus_name._getValue());
	}
	/* modified by wangj 需求编号【HS141110017】保理业务改造  end*/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addIqpBuscontInfoRecord.do" method="POST">
		
		<emp:gridLayout id="IqpBuscontInfoGroup" title="贸易合同信息" maxColumn="2">
			<emp:text id="IqpBuscontInfo.tcont_no" label="贸易合同编号" maxlength="40" required="true" hidden="false" />
			<emp:text id="IqpBuscontInfo.tcont_amt" label="贸易合同金额" maxlength="18" required="true" dataType="Currency"/>
			<!--   modified by wangj 需求编号【HS141110017】保理业务改造  begin -->
			<emp:pop id="IqpBuscontInfo.sup_mat_cprt" label="供货单位"  required="true"  url="queryAllCusPop.do?returnMethod=returnCus" cssElementClass="emp_field_text_long" colSpan="2"/>
			<!--   modified by wangj 需求编号【HS141110017】保理业务改造  end -->
			<emp:date id="IqpBuscontInfo.start_date" label="贸易合同起始日"  required="true" />
			<emp:date id="IqpBuscontInfo.end_date" label="贸易合同到期日"  required="false" />
			<emp:textarea id="IqpBuscontInfo.trade_detail" label="贸易合同交易内容" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="IqpBuscontInfo.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			
			<emp:text id="IqpBuscontInfo.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.po_no" label="池编号" maxlength="30" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="add" label="确定" />
			<emp:button id="return" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

