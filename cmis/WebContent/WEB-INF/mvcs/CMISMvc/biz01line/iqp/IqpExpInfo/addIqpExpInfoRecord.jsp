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

	function doAdd(){
		var form = document.getElementById("submitForm");
		IqpExpInfo._checkAll();
		if(IqpExpInfo._checkAll()){
			var invc_date = IqpExpInfo.invc_date._getValue();
			var start_date = IqpExpInfo.start_date._getValue();
			var receive_date = IqpExpInfo.receive_date._getValue();
			if(invc_date>start_date){
				alert("快递发出日期早于开票日期！");
				return;
			}
			if(start_date>receive_date){
				alert("快递收到日期早于快递发出日期！");
				return;
			}
			IqpExpInfo._toForm(form);
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
						window.close();
					}else if(flag == "exist"){
						alert("已存在此记录！");
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

	function doOnLoad(){ 
		if('<%=poNo%>'!='null')IqpExpInfo.po_no._setValue('<%=poNo%>');
		IqpExpInfo.invc_no._obj.addOneButton("invc_no","选择",getCont);
	}
	function getCont(){
		var url = '<emp:url action="getIqpActrecbondDetailList.do"/>?po_no=<%=poNo%>&returnMethod=returnInvc';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	function returnInvc(data){
		IqpExpInfo.invc_no._setValue(data.invc_no._getValue());
		IqpExpInfo.invc_amt._setValue(data.bond_amt._getValue());
	};
	
	function doReturn (){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addIqpExpInfoRecord.do" method="POST">
		
		<emp:gridLayout id="IqpExpInfoGroup" title="快递信息" maxColumn="2">
			<emp:text id="IqpExpInfo.express_no" label="快递单号" maxlength="40" required="true" />
			<emp:text id="IqpExpInfo.express_cprt" label="快递公司" maxlength="80" required="true" />
			<emp:text id="IqpExpInfo.invc_no" label="发票号" maxlength="40" required="true" />
			<emp:text id="IqpExpInfo.invc_amt" label="发票金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="IqpExpInfo.invc_date" label="开票日期"   required="true" colSpan="2"/>
			<emp:date id="IqpExpInfo.start_date" label="快递发出日期"   required="true" />
			<emp:date id="IqpExpInfo.receive_date" label="快递接收日期"   required="true" />
			<emp:textarea id="IqpExpInfo.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			<emp:text id="IqpExpInfo.input_id" label="登记人" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.input_br_id" label="登记机构" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.po_no" label="池编号" maxlength="30" required="true"  hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="add" label="确定" />
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

