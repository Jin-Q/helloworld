<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	} 
%> 
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  begin **/
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccTranTrustCompany._toForm(form);
		AccTranTrustCompanyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccTranTrustCompanyPage() {
		var paramStr = AccTranTrustCompanyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = AccTranTrustCompanyList._obj.getSelectedData();
			var trade_status = data[0].trade_status._getValue();
			if(trade_status =="1"){
				alert("交易状态已为【生效】！");
				return;
			}else{
				var url = '<emp:url action="getAccTranTrustCompanyUpdatePage.do"/>?'+paramStr+'&bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}&cur_type=${context.cur_type}';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccTranTrustCompany() {
		var paramStr = AccTranTrustCompanyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccTranTrustCompanyViewPage.do"/>?'+paramStr+'&bill_no=${context.bill_no}&cont_no=${context.cont_no}&cur_type=${context.cur_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccTranTrustCompanyPage() {
		var url = '<emp:url action="getAccTranTrustCompanyAddPage.do"/>?bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}&cur_type=${context.cur_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccTranTrustCompany() {
		var paramStr = AccTranTrustCompanyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = AccTranTrustCompanyList._obj.getSelectedData();
			var trade_status = data[0].trade_status._getValue();
			var reclaim_mode = data[0].reclaim_mode._getValue();
			var tran_date = data[0].tran_date._getValue();
			var tran_amt = data[0].tran_amt._getValue();
			if(confirm("是否确认要删除？")){
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
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("发生异常！"); 
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
				var url = '<emp:url action="deleteAccTranTrustCompanyRecord.do"/>?'+paramStr+"&trade_status="+trade_status+"&bill_no=${context.bill_no}&cont_no=${context.cont_no}"+"&reclaim_mode="+reclaim_mode+"&tran_date="+tran_date+"&tran_amt="+tran_amt;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}     
		} else {
			alert('请先选择一条记录！');
		}
	};
	//生效按钮
	function doEffect(){
		var paramStr = AccTranTrustCompanyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = AccTranTrustCompanyList._obj.getSelectedData();
			var trade_status = data[0].trade_status._getValue();
			if(trade_status =="1"){
				alert("交易状态已为【生效】！");
				return;
			}else{
				var url = '<emp:url action="getAccTranTrustCompanyUpdatePage.do"/>?'+paramStr+'&bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}&cur_type=${context.cur_type}'+"&eff_flag=1";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//失效按钮
	function doLoseEffect(){
		var paramStr = AccTranTrustCompanyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var data = AccTranTrustCompanyList._obj.getSelectedData();
			var trade_status = data[0].trade_status._getValue();
			if(trade_status =="2"){
				alert("交易状态为【未生效】，不能做失效操作！");
				return;
			}else if(trade_status =="0"){
				alert("交易状态已为【失效】！");
				return;
			}else{
				var url = '<emp:url action="getAccTranTrustCompanyUpdatePage.do"/>?'+paramStr+'&bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}&cur_type=${context.cur_type}'+"&eff_flag=0";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.AccTranTrustCompanyGroup.reset();
	};
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  end **/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
	 <%if("update".equals(op)){%>
		<emp:button id="getAddAccTranTrustCompanyPage" label="新增" />
		<emp:button id="getUpdateAccTranTrustCompanyPage" label="修改" />
	<%}else if("update4m".equals(op)){%>
		<emp:button id="effect" label="生效" />
		<emp:button id="loseEffect" label="失效" />
		<emp:button id="deleteAccTranTrustCompany" label="删除" />
	<%}%>
		<emp:button id="viewAccTranTrustCompany" label="查看" />
	</div>

	<emp:table icollName="AccTranTrustCompanyList" pageMode="true" url="pageAccTranTrustCompanyQuery.do?bill_no=${context.bill_no}&cont_no=${context.cont_no}">
		<emp:text id="serno" label="交易明细编号" hidden="false"/>
		<emp:text id="bill_no" label="借据号" />
		<emp:text id="cont_no" label="合同号" />
		<emp:text id="list_type" label="明细类型" dictname="STD_ACC_DETAIL_TYPE"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="tran_amt" label="交易金额" dataType="Currency"/>
		<emp:text id="reclaim_mode" label="款项明细" dictname="STD_ZB_RECYCLE_TYPE" />
		<emp:text id="tran_date" label="付款日"  />
		<emp:text id="trade_status" label="交易状态" dictname="STD_ZB_STATUS" />
		<emp:text id="input_id_displayname" label="登记人"  />
	</emp:table>
	
</body>
</html>
</emp:page>
    