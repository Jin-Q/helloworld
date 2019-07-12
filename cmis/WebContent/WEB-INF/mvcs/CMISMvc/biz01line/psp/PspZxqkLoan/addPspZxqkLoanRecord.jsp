<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspZxqkLoan.task_id._setValue(task_id);
		var cus_type = PspZxqkLoan.cus_type._getValue();
		if(cus_type == "01"){
			changeVisit();
			changeVisitType();
			}
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspZxqkLoan._checkAll()){
			PspZxqkLoan._toForm(form); 
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
						doReturn();
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};	

	function doReturn(){
		var dataFrom = '<%=dataFrom%>';
		var cus_id = PspZxqkLoan.cus_id._getValue();
		var task_id = PspZxqkLoan.task_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForZx.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function changeVisit(){
		
		PspZxqkLoan.jqgr_name._setValue("");
		PspZxqkLoan.jqgr_type._setValue("");
		PspZxqkLoan.loan_amt._setValue("");
		PspZxqkLoan.jqgr_sfyqb._setValue("");
		PspZxqkLoan.jqgr_sfyqb_sm._setValue("");
		PspZxqkLoan.bzr_name._setValue("");
		PspZxqkLoan.dwrz_amt._setValue("");
		PspZxqkLoan.dwrz_sfyqb._setValue("");
		PspZxqkLoan.dwrz_sfyqb_sm._setValue("");
		PspZxqkLoan.credit_bank_num._setValue("");
		PspZxqkLoan.dwdb_amt._setValue("");
		PspZxqkLoan.dwdb_jsqzje._setValue("");
		PspZxqkLoan.dwdb_sfyqb._setValue("");
		PspZxqkLoan.dwdb_sfyqb_sm._setValue("");
		PspZxqkLoan.lawsuit_enp._setValue("");
	
	}

	
	function changeVisitType(){
		var cus_type = PspZxqkLoan.cus_type._getValue();
		if(cus_type=="01"){
			document.getElementById('qkqegjgr').style.display="";
			document.getElementById('bzr').style.display="none";

			PspZxqkLoan.jqgr_name._obj._renderRequired(true);
			PspZxqkLoan.jqgr_type._obj._renderRequired(true);
			PspZxqkLoan.loan_amt._obj._renderRequired(true);
			PspZxqkLoan.jqgr_jsqzje._obj._renderRequired(true);			
			PspZxqkLoan.jqgr_sfyqb._obj._renderRequired(true);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(true);
			PspZxqkLoan.bzr_name._obj._renderRequired(false);
			PspZxqkLoan.dwrz_amt._obj._renderRequired(false);
			PspZxqkLoan.dwrz_jsqzje._obj._renderRequired(false);			
			PspZxqkLoan.dwrz_sfyqb._obj._renderRequired(false);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.credit_bank_num._obj._renderRequired(false);
			PspZxqkLoan.dwdb_amt._obj._renderRequired(false);
			PspZxqkLoan.dwdb_jsqzje._obj._renderRequired(false);
			PspZxqkLoan.dwdb_sfyqb._obj._renderRequired(false);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.lawsuit_enp._obj._renderRequired(false);
			
		}else if (cus_type=="02"){
			document.getElementById('bzr').style.display="";	
			document.getElementById('qkqegjgr').style.display="none";

			PspZxqkLoan.jqgr_name._obj._renderRequired(false);
			PspZxqkLoan.jqgr_type._obj._renderRequired(false);
			PspZxqkLoan.loan_amt._obj._renderRequired(false);
			PspZxqkLoan.jqgr_jsqzje._obj._renderRequired(false);	
			PspZxqkLoan.jqgr_sfyqb._obj._renderRequired(false);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.bzr_name._obj._renderRequired(true);
			PspZxqkLoan.dwrz_amt._obj._renderRequired(true);
			PspZxqkLoan.dwrz_jsqzje._obj._renderRequired(true);	
			PspZxqkLoan.dwrz_sfyqb._obj._renderRequired(true);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(true);
			PspZxqkLoan.credit_bank_num._obj._renderRequired(true);
			PspZxqkLoan.dwdb_amt._obj._renderRequired(true);
			PspZxqkLoan.dwdb_jsqzje._obj._renderRequired(true);
			PspZxqkLoan.dwdb_sfyqb._obj._renderRequired(true);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(true);
			PspZxqkLoan.lawsuit_enp._obj._renderRequired(true);

		}else {
			document.getElementById('qkqegjgr').style.display="none";
			document.getElementById('bzr').style.display="none";
			
		}
	}
	function convert_jqgr(){
		if(PspZxqkLoan.jqgr_sfyqb._getValue()=="1"){
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.jqgr_sfyqb_sm._setValue("");
		}
	}
	function convert_dwrz(){
		if(PspZxqkLoan.dwrz_sfyqb._getValue()=="1"){
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.dwrz_sfyqb_sm._setValue("");
		}
	}
	function convert_dwdb(){
		if(PspZxqkLoan.dwdb_sfyqb._getValue()=="1"){
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.dwdb_sfyqb_sm._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspZxqkLoanRecord.do" method="POST">
			<emp:text id="PspZxqkLoan.task_id" label="任务编码"  defvalue="${context.task_id}" hidden="true"/>
			<emp:text id="PspZxqkLoan.cus_id" label="客户编码"  defvalue="${context.cus_id}" hidden="true"/>
		<emp:gridLayout id="PspZxqkLoanGroup" title="征信情况" maxColumn="2">
			<emp:select id="PspZxqkLoan.cus_type" label="客户类型"  required="true" defvalue="01" readonly="true" dictname="STD_ZB_JBR_TYPE" onchange="changeVisitType();changeVisit()"/>	
		</emp:gridLayout>
		
		<div id="qkqegjgr" style="display:none" >
		<emp:gridLayout id="PspZxqkLoanGroup" title="借款企业关键个人" maxColumn="2"  >
			<emp:text id="PspZxqkLoan.jqgr_name" label="姓名" required="true" />
			<emp:select id="PspZxqkLoan.jqgr_type" label="类别"  required="true"  dictname="STD_ZX_JQGG_TYP" />	
			<emp:text id="PspZxqkLoan.loan_amt" label="贷款总额（万元）" required="true" dataType="Currency" />
			<emp:text id="PspZxqkLoan.jqgr_jsqzje" label="较上期增加（或减少）额" required="true" dataType="Currency" />
			<emp:radio id="PspZxqkLoan.jqgr_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jqgr()"/>
			<emp:textarea id="PspZxqkLoan.jqgr_sfyqb_sm" label="如有请说明原因：" required="false" hidden="true" colSpan="2"/>
			
		</emp:gridLayout>
		</div>
		
		<div id="bzr" style="display:none" >
		<emp:gridLayout id="PspZxqkLoanGroup" title="保证人" maxColumn="2"  >
			<emp:text id="PspZxqkLoan.bzr_name" label="保证人姓名" required="true" />
			<emp:text id="PspZxqkLoan.dwrz_amt" label="对外融资总额（万元）"  required="true" dataType="Currency" />	
			<emp:text id="PspZxqkLoan.dwrz_jsqzje" label="较上期增加（或减少）额" required="true" dataType="Currency" />
			<emp:radio id="PspZxqkLoan.dwrz_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" onclick="convert_dwrz()"/>
			<emp:textarea id="PspZxqkLoan.dwrz_sfyqb_sm" label="如有请说明原因：" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspZxqkLoan.credit_bank_num" label="授信合作银行数量" required="true"  dataType="Int"/>
			<emp:text id="PspZxqkLoan.dwdb_amt" label="对外担保总额（万元）" required="true" dataType="Currency" />
			<emp:text id="PspZxqkLoan.dwdb_jsqzje" label="较上期增加（或减少）额"  required="true" dataType="Currency" />
			<emp:radio id="PspZxqkLoan.dwdb_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO"  layout="false" colSpan="2" onclick="convert_dwdb()"/>
			<emp:textarea id="PspZxqkLoan.dwdb_sfyqb_sm" label="如有请说明原因：" required="false" hidden="true" colSpan="2"/>
			<emp:textarea id="PspZxqkLoan.lawsuit_enp" label="诉讼环保情况" required="true" colSpan="2"/>
		</emp:gridLayout>
		</div>

		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

