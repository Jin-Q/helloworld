<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryFncDetailBaseList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewFncAccPayable() {
		var paramStr = FncDetailBase.FncAccPayable._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncAccPayableDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncAccReceivable() {
		var paramStr = FncDetailBase.FncAccReceivable._obj.getParamStr(['seq','cus_id','fnc_ym','fnc_con_cus_name']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncAccReceivableDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncAssure() {
		var paramStr = FncDetailBase.FncAssure._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncAssureDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncFixedAsset() {
		var paramStr = FncDetailBase.FncFixedAsset._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncFixedAssetDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncInventory() {
		var paramStr = FncDetailBase.FncInventory._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncInventoryDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncInvestment() {
		var paramStr = FncDetailBase.FncInvestment._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncInvestmentDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncLoan() {
		var paramStr = FncDetailBase.FncLoan._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncLoanDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncOtherPayable() {
		var paramStr = FncDetailBase.FncOtherPayable._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncOtherPayableDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncOtherRecv() {
		var paramStr = FncDetailBase.FncOtherRecv._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncOtherRecvDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewFncProject() {
		var paramStr = FncDetailBase.FncProject._obj.getParamStr(['seq','cus_id','fnc_ym']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryFncDetailBaseFncProjectDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};



	function doReturn(){
        var paramStr="FncDetailBase.cus_id="+FncDetailBase.cus_id._obj.element.value;
		var url = '<emp:url action="queryFncDetailBaseList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;		
	}

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="FncDetailBaseGroup" title="报表明细基表" maxColumn="2">
			<emp:text id="FncDetailBase.pk" label="PK" maxlength="40" required="true" hidden="true" readonly="true" />
			<emp:text id="FncDetailBase.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="FncDetailBase.fnc_ym" label="年月" maxlength="6" required="true" />
			<emp:text id="FncDetailBase.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="FncDetailBase.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:date id="FncDetailBase.input_date" label="登记日期" required="false" />
			<emp:text id="FncDetailBase.last_upd_id" label="更新人" maxlength="20" required="false" />
			<emp:date id="FncDetailBase.last_upd_date" label="更新日期" required="false" />
	</emp:gridLayout>
	<div align="center">
			<br>
			<emp:button id="return" label="返回"/>
		</div>
	<br>

	<emp:tabGroup id="FncDetailBase_tabs" mainTab="FncAccPayable_tab">
		<emp:tab id="FncAccPayable_tab" label="应付账款">
			<div align="left">
				<emp:button id="viewFncAccPayable" label="查看" op="view_FncAccPayable"/>
			</div>
			<emp:table icollName="FncDetailBase.FncAccPayable" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_con_cus_name" label="对方客户名称" />
		<emp:text id="fnc_cur_typ" label="币种" />
		<emp:text id="fnc_sum_amt" label="应付款余额合计" />
		<emp:text id="fnc_imm_trm_amt" label="账龄6个月(含)以内金额" dataType="Currency" />
		<emp:text id="fnc_short_trm_amt" label="账龄6个月-1年(含1年)金额" dataType="Currency" />
		<emp:text id="fnc_inter_trm_amt" label="账龄1-2年(含2年)金额" dataType="Currency" />
		<emp:text id="fnc_long_trm_amt" label="账龄2年以上金额 " dataType="Currency" />
		<emp:text id="rel_flg" label="是否关联企业" />   
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
		<emp:tab id="FncAccReceivable_tab" label="应收账款">
			<div align="left">
				<emp:button id="viewFncAccReceivable" label="查看" op="view_FncAccReceivable"/>
			</div>
			<emp:table icollName="FncDetailBase.FncAccReceivable" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_con_cus_name" label="对方客户名称" />
		<emp:text id="fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="fnc_sum_amt" label="应付款余额合计" dataType="Currency" />
		<emp:text id="fnc_imm_trm_amt" label="账龄6个月(含)以内金额" dataType="Currency" />
		<emp:text id="fnc_short_trm_amt" label="账龄6个月-1年(含1年)金额" dataType="Currency" />
		<emp:text id="fnc_inter_trm_amt" label="账龄1-2年(含2年)金额" dataType="Currency" />
		<emp:text id="fnc_long_trm_amt" label="账龄2年以上金额" dataType="Currency" />
		<emp:text id="rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>

		<emp:tab id="FncInventory_tab" label="主要存货">
			<div align="left">
				<emp:button id="viewFncInventory" label="查看" op="view_FncInventory"/>
			</div>
			<emp:table icollName="FncDetailBase.FncInventory" pageMode="false" url="">
		<emp:text id="seq" label="序号" />
		<emp:text id="pk" label="PK" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_invy_name" label="存货名称" />
		<emp:text id="fnc_prc_typ" label="计价方式" />
		<emp:text id="fnc_invy_typ" label="存货种类" />
		<emp:text id="fnc_invy_amt" label="数量" />
		<emp:text id="fnc_invy_val" label="价值" dataType="Currency" />
		<emp:text id="fnc_invy_dt" label="库存日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
		<emp:tab id="FncInvestment_tab" label="主要投资">
			<div align="left">
				<emp:button id="viewFncInvestment" label="查看" op="view_FncInvestment"/>
			</div>
			<emp:table icollName="FncDetailBase.FncInvestment" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_invt_toward" label="投资资产名称" />
		<emp:text id="fnc_invt_amt" label="投资金额" dataType="Currency" />
		<emp:text id="fnc_cur_val" label="现价值"  dataType="Currency"/>
		<emp:text id="fnc_last_invt_incm" label="上年投资收益" dataType="Currency" />
		<emp:text id="fnc_avg_invt_incm" label="平均投资收益" dataType="Currency" />
		<emp:text id="fnc_price_typ" label="后续计量方法" />
		<emp:text id="fnc_invt_dt" label="投资日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
	
		<emp:tab id="FncOtherPayable_tab" label="其它应付款">
			<div align="left">
				<emp:button id="viewFncOtherPayable" label="查看" op="view_FncOtherPayable"/>
			</div>
			<emp:table icollName="FncDetailBase.FncOtherPayable" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_con_cus_name" label="对方客户名称" />
		<emp:text id="fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="fnc_sum_amt" label="应付款余额合计" dataType="Currency" />
		<emp:text id="fnc_imm_trm_amt" label="账龄6个月(含)以内金额" dataType="Currency" />
		<emp:text id="fnc_short_trm_amt" label="账龄6个月-1年(含1年)金额" dataType="Currency" />
		<emp:text id="fnc_inter_trm_amt" label="账龄1-2年(含2年)金额 " dataType="Currency" />
		<emp:text id="fnc_long_trm_amt" label="账龄2年以上金额 " dataType="Currency" />  
		<emp:text id="rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
		<emp:tab id="FncOtherRecv_tab" label="其它应收款">
			<div align="left">
				<emp:button id="viewFncOtherRecv" label="查看" op="view_FncOtherRecv"/>
			</div>
			<emp:table icollName="FncDetailBase.FncOtherRecv" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_con_cus_name" label="对方客户名称" />
		<emp:text id="fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="fnc_sum_amt" label="应付款余额合计" dataType="Currency" />
		<emp:text id="fnc_imm_trm_amt" label="账龄6个月(含)以内金额" dataType="Currency" />
		<emp:text id="fnc_short_trm_amt" label="账龄6个月-1年(含1年)金额" dataType="Currency" />
		<emp:text id="fnc_inter_trm_amt" label="账龄1-2年(含2年)金额 " dataType="Currency" />
		<emp:text id="fnc_long_trm_amt" label="账龄2年以上金额 " dataType="Currency" />
		<emp:text id="rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
		<emp:tab id="FncProject_tab" label="在建工程">
			<div align="left">
				<emp:button id="viewFncProject" label="查看" op="view_FncProject"/>
			</div>
			<emp:table icollName="FncDetailBase.FncProject" pageMode="false" url="">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="pk" label="PK" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="fnc_prj_name" label="项目名称" />
		<emp:text id="fnc_const_loc" label="施工地点" />
		<emp:text id="fnc_const_depnt" label="施工单位" />
		<emp:text id="fnc_invt_amt" label="投入金额" dataType="Currency" />
		<emp:text id="fnc_invt_amted" label="已完成投资额" dataType="Currency" />
		<emp:text id="fnc_prg" label="项目进度" />
		<emp:text id="remark" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="last_upd_id" label="更新人" hidden="true"/>
		<emp:text id="last_upd_date" label="更新日期" hidden="true"/>
			</emp:table>
		</emp:tab>
		
		<emp:tab id="FncOrDebt_tab" label="或有负债">
			<div align="left">
				<emp:button id="viewFncOrDebt" label="查看" op="view_FncOrDebt"/>
			</div>
			<emp:table icollName="FncDetailBase.FncOrDebt" pageMode="false" url="">
				<emp:text id="seq" label="序号" hidden="true" />
				<emp:text id="pk" label="PK" hidden="true" />
				<emp:text id="cus_id" label="客户码" />
				<emp:text id="fnc_ym" label="年月" />
				<emp:select id="fnc_or_debt_typ" label="或有负债类型" />
				<emp:textarea id="fnc_or_debt_des" label="或有负债描述" cols="2" hidden="true"/>
				<emp:text id="fnc_amt" label="金额" dataType="Currency" />
				<emp:text id="input_id" label="干系人"  />
				<emp:date id="input_date" label="生产日期" />
			</emp:table>
		</emp:tab>
		
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
	