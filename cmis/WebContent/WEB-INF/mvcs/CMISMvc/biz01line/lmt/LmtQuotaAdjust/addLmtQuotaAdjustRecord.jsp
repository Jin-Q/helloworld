<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
	<html>
	<head>
	<title>新增页面</title>
	<jsp:include page="/include.jsp" flush="true" />
	<%
		String single_quota = request.getParameter("single_quota");
		String agr_no = request.getParameter("agr_no");
		String fin_totl_limit = request.getParameter("fin_totl_limit");
		String fin_totl_spac = request.getParameter("fin_totl_spac");
		String inure_date = request.getParameter("inure_date");
		String lmt_start_date = request.getParameter("lmt_start_date");
	%>
	<script type="text/javascript">

	/*--user code begin--*/

	function checkEndDate(){
		var inure_date = LmtQuotaAdjust.inure_date._getValue();
		var end_date = LmtQuotaAdjust.end_date._getValue();
		if(""!=inure_date && ""!=end_date && inure_date > end_date){
			alert("到期日期必须大于生效日期！");
			LmtQuotaAdjust.end_date._setValue("");
			return false;
		}
	}

	function checkTotl(){
		var fin_totl_limit = LmtQuotaAdjust.fin_totl_limit._getValue();//总额
		var fin_totl_spac = LmtQuotaAdjust.fin_totl_spac._getValue();//总额敞口
		var single_quota_new = LmtQuotaAdjust.single_quota_new._getValue();//单户限额(新增)
		var single_quota_his = LmtQuotaAdjust.single_quota_his._getValue();//单户限额(存量)
		if(parseFloat(fin_totl_limit)<parseFloat(fin_totl_spac)){
			alert("融资总额敞口应小于等于融资总额！");
			LmtQuotaAdjust.fin_totl_spac._setValue("");
			return;
		}
		if(parseFloat(fin_totl_limit)<parseFloat(single_quota_new)){
			alert("单户限额(新增)应小于等于融资总额！");
			LmtQuotaAdjust.single_quota_new._setValue("");
			return;
		}
		if(parseFloat(fin_totl_limit)<parseFloat(single_quota_his)){
			alert("单户限额(存量)应小于等于融资总额！");
			LmtQuotaAdjust.single_quota_his._setValue("");
			return;
		}
	}

	function doAdd() {
		var form = document.getElementById("submitForm");
		var result = LmtQuotaAdjust._checkAll();
		if(result){
			LmtQuotaAdjust._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit ='<%=fin_totl_limit%>';
		var fin_totl_spac ='<%=fin_totl_spac%>';
		var lmt_start_date ='<%=lmt_start_date%>';
		var url = '<emp:url action="addLmtQuotaAdjustRecord.do"/>';
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("新增成功！");
						var url = '<emp:url action="queryLmtQuotaAdjustList.do"/>?agr_no='+agr_no+'&single_quota='
						+single_quota+'&fin_totl_limit='+fin_totl_limit+'&lmt_start_date='+lmt_start_date+"&fin_totl_spac="+fin_totl_spac+"op=update";
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};

	function doReturn(){
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit = '<%=fin_totl_limit%>';
		var fin_totl_spac = '<%=fin_totl_spac%>';
		var fin_agr_no = '<%=agr_no%>';
		var lmt_start_date ='<%=lmt_start_date%>';
		var url = '<emp:url action="queryLmtQuotaAdjustList.do"/>?single_quota='+single_quota+'&agr_no='
		+agr_no+'&fin_totl_limit='+fin_totl_limit+'&fin_agr_no='+fin_agr_no+'&lmt_start_date='+lmt_start_date+"&fin_totl_spac="+fin_totl_spac+"op=update";
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content">

	<emp:form id="submitForm" action="addLmtQuotaAdjustRecord.do"
		method="POST">

		<emp:gridLayout id="LmtQuotaAdjustGroup" title="用信限额调整" maxColumn="2">
			<emp:text id="LmtQuotaAdjust.fin_agr_no" label="融资协议编号" maxlength="40" required="true" defvalue="<%=agr_no%>" readonly="true" colSpan="2"/>
			<emp:text id="LmtQuotaAdjust.fin_totl_limit" label="融资总额" maxlength="18" readonly="false" required="true" dataType="Currency" defvalue="<%=fin_totl_limit%>" onchange="checkTotl()" />
			<emp:text id="LmtQuotaAdjust.fin_totl_spac" label="融资总敞口" maxlength="18" readonly="false" required="true" dataType="Currency" defvalue="<%=fin_totl_spac%>" onchange="checkTotl()"/>
			<emp:text id="LmtQuotaAdjust.single_quota_his" label="单户限额(存量)" maxlength="18" defvalue="<%=single_quota%>" dataType="Currency" required="true" onchange="checkTotl()" readonly="false" />
			<emp:text id="LmtQuotaAdjust.single_quota_new" label="单户限额(新增)" maxlength="18" dataType="Currency" onchange="checkTotl()" required="true"/>
			<emp:date id="LmtQuotaAdjust.inure_date" label="生效日期" required="true" readonly="true" defvalue="${context.end_date}"/>
			<emp:date id="LmtQuotaAdjust.end_date" label="到期日期" required="true" onblur="checkEndDate()" />
			<emp:select id="LmtQuotaAdjust.approve_status" label="状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="true" />
			<emp:select id="LmtQuotaAdjust.status" label="状态" required="false" dictname="STD_ZB_STATUS" defvalue="2" readonly="true" hidden="true" />
			<emp:select id="LmtQuotaAdjust.serno" label="主键" required="false" hidden="true" />
		</emp:gridLayout>

		<div align="center"><br>
		<emp:button id="add" label="保存" /> 
		<emp:button id="return" label="返回" /></div>
	</emp:form>

	</body>
	</html>
</emp:page>

