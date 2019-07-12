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
		if(IqpAssetIssueResult._checkAll()){
			IqpAssetIssueResult._toForm(form);
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
						var url = '<emp:url action="queryIqpAssetIssueResultList.do"/>';
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
	
	<emp:form id="submitForm" action="addIqpAssetIssueResultRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetIssueResultGroup" title="发行结果管理" maxColumn="2">
			<emp:text id="IqpAssetIssueResult.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetIssueResult.prd_id" label="产品代码" maxlength="40" required="true" />
			<emp:text id="IqpAssetIssueResult.bidding_dest" label="招标标的" maxlength="5" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_totl_amt" label="招标总量（万元）" maxlength="80" required="false" />
			<emp:text id="IqpAssetIssueResult.act_issue_date" label="实际发行首日" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.end_date" label="法定到期日" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.base_date" label="基准日（起息日）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.resp_bid_mqnt" label="应投家数（家）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.act_issue_amt" label="实际发行总量（万元）" maxlength="16" required="false" />
			<emp:text id="IqpAssetIssueResult.paid_totl_amt" label="缴款总金额（万元）" maxlength="16" required="false" />
			<emp:text id="IqpAssetIssueResult.issue_type" label="发行方式" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.sale_type" label="销售方式" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.tran_market" label="交易市场" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_mode" label="招标方式" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_book_no" label="招标书编号" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_vld_amt" label="有效投标总量（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bid_amt" label="中标总量（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_mqnt" label="投标家数（家）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bid_mqnt" label="中标家数（家）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bidding_qnt" label="投标笔数（笔）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.vld_qnt" label="有效笔数（笔）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.no_vld_qnt" label="无效笔数（笔）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.bid_qnt" label="中标笔数（笔）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.max_bidding_price" label="最高投标价位" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.min_bidding_price" label="最低投标价位" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.max_bid_price" label="最高中标价位" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.min_bid_price" label="最低中标价位" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.side_bidding_totl_amt" label="边际中标价位投标总量（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.side_bid_totl_amt" label="边际中标价位中标总量（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.final_bid_rate" label="最终中标利率" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.final_bid_sprd" label="最终中标利差" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.add_value" label="增加值" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.base_rate" label="基础利率" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.base_rate_mome" label="基础利率选择说明" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.base_sprd" label="基本利差" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.drft_rate" label="票面利率" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.issue_price" label="发行价格(面值)（元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.dealer" label="承销商" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.deal_amt" label="承销金额（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.deal_perc" label="承销比例" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.invest_person" label="投资人" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.desbuy_amt" label="认购总额（万元）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.desbuy_perc" label="认购比例" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueResult.cont_no" label="合同编号" maxlength="40" required="true" />
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

