<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>抵好贷调查表</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doBack(){
		var serno = MainInfo.serno._getValue();
		var url = '<emp:url action="setReportImportPage.do"/>&serno='+serno;
		url=encodeURI(url);
		window.location = url;
	}

	function doSave(){
		var form = document.getElementById("submitForm");

		MainInfo._toForm(form);
		MtgList._toForm(form);
		PartyList._toForm(form);
			
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("添加数据失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
					return;
				}else{
					alert("保存失败！");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	
</script>
</head>
<body class="page_content" >
<emp:tabGroup mainTab="mainInfo" id="抵好贷">
		<emp:tab label="抵好贷调查报告" id="mainInfo" >
		<emp:form id="submitForm" action="saveDiInvestigation.do" method="POST">
		<emp:gridLayout id="MainInfoGroup" title="基础信息" maxColumn="2">
				<emp:text id="MainInfo.pk_id" label="id" hidden="true"/>	
				<emp:text id="MainInfo.serno" label="流水号" hidden="true"/>	
				<emp:text id="MainInfo.charge_man" label="负责信贷员" />
				<emp:text id="MainInfo.chart_date" label="制表日期"/>
				
				<emp:text id="MainInfo.apply_amount" label="申请金额" />
				<emp:text id="MainInfo.apply_term" label="贷款期限"/>
				
				<emp:text id="MainInfo.repayment_mode" label="还款方式" />
				<emp:text id="MainInfo.apply_date" label="申请日期"/>
				
				<emp:text id="MainInfo.loan_aim" label="贷款用途" />
				<emp:text id="MainInfo.apply_name" label="借款人名称"/>
				
				<emp:text id="MainInfo.employee_num" label="雇员人数" />
				<emp:text id="MainInfo.gender" label="性别" />
				
				<emp:text id="MainInfo.education" label="文化程度" />
				<emp:text id="MainInfo.project" label="经营项目" />
				
				<emp:text id="MainInfo.prj_period" label="经营年限" />
				<emp:text id="MainInfo.house_info" label="房产情况" />
				<emp:text id="MainInfo.current_address" label="现住址" />
				<emp:text id="MainInfo.family_info" label="家庭情况" />
		</emp:gridLayout>
				
		<emp:gridLayout id="MainInfoGroup1" title="财务信息概览" maxColumn="2">	
				<emp:text id="MainInfo.current_deposit" label="现金及银行储蓄" />
				<emp:text id="MainInfo.account_payable" label="应付账款" />
				<emp:text id="MainInfo.accounts_receivable" label="应收账款" />
				<emp:text id="MainInfo.account_prereceivable" label="预收账款" />
				<emp:text id="MainInfo.account_prepayable" label="预付账款" />
				<emp:text id="MainInfo.short_loan" label="短期借款" />
				
				<emp:text id="MainInfo.stock" label="存货" />
				<emp:text id="MainInfo.total_flow_debt" label="流动负债合计" />
				<emp:text id="MainInfo.total_flow_assets" label="流动资产合计" />
				<emp:text id="MainInfo.long_loan" label="长期借款" />
				
				<emp:text id="MainInfo.fixed_assets" label="固定资产" />
				<emp:text id="MainInfo.total_debt" label="负债合计 " />
				<emp:text id="MainInfo.other_run_assets" label="其他经营资产" />
				<emp:text id="MainInfo.owner_equity" label="所有者权益 " />
				<emp:text id="MainInfo.total_assets" label="资产合计" />
				<emp:text id="MainInfo.debt_equity" label="负债加权益" />
				<emp:text id="MainInfo.other_nontable_assets" label="其他非表内资产" />
				<emp:text id="MainInfo.other_nontable_debt" label="其他非表内负债" />
		</emp:gridLayout>
		<emp:gridLayout id="MainInfoGroup1" title="损益情况分析（贷款前12个月情况）" maxColumn="2">	
				<emp:text id="MainInfo.mon_1" label="贷前第1月" />
				<emp:text id="MainInfo.mon_2" label="贷前第2月" />
				<emp:text id="MainInfo.mon_3" label="贷前第3月" />
				<emp:text id="MainInfo.mon_4" label="贷前第4月" />
				<emp:text id="MainInfo.mon_5" label="贷前第5月" />
				<emp:text id="MainInfo.mon_6" label="贷前第6月" />
				<emp:text id="MainInfo.mon_7" label="贷前第7月" />
				<emp:text id="MainInfo.mon_8" label="贷前第8月" />
				<emp:text id="MainInfo.mon_9" label="贷前第9月" />
				<emp:text id="MainInfo.mon_10" label="贷前第10月" />
				<emp:text id="MainInfo.mon_11" label="贷前第11月" />
				<emp:text id="MainInfo.mon_12" label="贷前第12月" />
				<emp:text id="MainInfo.mon_sum" label="总计" />
				<emp:text id="MainInfo.mon_avg" label="平均" />
				<emp:text id="MainInfo.gross_rate" label="毛利率" />
				<emp:text id="MainInfo.net_rate" label="净利率" />
		</emp:gridLayout>
				
		<emp:gridLayout id="MainInfoGroup1" title="费用" maxColumn="2">	
				<emp:text id="MainInfo.rent" label="租金" />
				<emp:text id="MainInfo.fee_sum" label="费用合计" />
				<emp:text id="MainInfo.other_income_analyse" label="其他收入来源分析" />
				<emp:text id="MainInfo.wage" label="工资" />
				<emp:text id="MainInfo.family_spending" label="家庭开支" />
				<emp:text id="MainInfo.traff" label="交通" />
				<emp:text id="MainInfo.other_income" label="其他收入" />
				<emp:text id="MainInfo.phone" label="电话" />
				<emp:text id="MainInfo.national_local_tax" label="国地税" />
				<emp:text id="MainInfo.other" label="其他" />
				<emp:text id="MainInfo.mon_dominate_income" label="月可支配收入" />
		</emp:gridLayout>
		<emp:gridLayout id="MainInfoGroup1" title="其他" maxColumn="2">	
				<emp:text id="MainInfo.history_status" label="经营历史" />
				<emp:text id="MainInfo.equity_check" label="权益的交叉检验" />
				<emp:text id="MainInfo.memo" label="备注" />
		</emp:gridLayout>	
	
		<br/>
		<div class='emp_gridlayout_title'>
			<emp:label text="抵押物描述"></emp:label>
		</div>
		<emp:table icollName="MtgList" pageMode="false" url='' >
				<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
				<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
				<emp:text id="owner" label="所有者" />
				<emp:text id="address" label="地址" />
				<emp:text id="current_status" label="目前使用状态" />
				<emp:text id="build_area" label="建筑面积" />
				<emp:text id="house_type" label="房屋性质" />
				<emp:text id="used_time" label="使用年限" />
				<emp:text id="market_value" label="市场价值" />
				<emp:text id="bank_evaluation" label="银行估价" />
		</emp:table>
		
		<br/>
		<div class='emp_gridlayout_title'>
			<emp:label text="贷款业务当事人"></emp:label>
		</div>
		<emp:table icollName="PartyList" pageMode="false" url='' >
				<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
				<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
				<emp:text id="name" label="姓名" />
				<emp:text id="relation_type" label="关系类型" />
				<emp:text id="company_position" label="工作单位及职务" />
				<emp:text id="income" label="收入" />
				<emp:text id="phone" label="联系电话" />
		</emp:table>
		</emp:form>
		
		<div align="center" >
			<emp:button id="save" label="保存"/>
			<emp:button id="back" label="返回"/>
		</div>
		</emp:tab>
		<emp:tab id="docInfo" label="调查报告" url="rqReport/reportShowPage.jsp" reqParams="reportId=dcbg_wx_dhd.raq&serno=$MainInfo.serno;&pageMark=no" initial="true" needFlush="true"/>	
	</emp:tabGroup> 	
</body>
</html>
</emp:page>
