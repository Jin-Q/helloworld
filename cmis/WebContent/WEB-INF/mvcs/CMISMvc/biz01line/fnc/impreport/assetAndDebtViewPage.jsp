<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>资产负债表</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doBack(){
		var serno = TableInfo.serno._getValue();
		var url = '<emp:url action="setReportImportPage.do"/>&serno='+serno;
		url=encodeURI(url);
		window.location = url;
		// window.history.go(-1);
	}

	function doSave(){
		var form = document.getElementById("submitForm");

		CurrentAndDepositList._toForm(form);
		AccountReceivableList._toForm(form);
		AccountPrePayableList._toForm(form);
		StockList._toForm(form);
		FixedAssetList._toForm(form);
		OtherAssetList._toForm(form);
		OtherAssetAndNontableAssetList._toForm(form);
		HouseList._toForm(form);
		MotorCarList._toForm(form);
		BankAccountList._toForm(form);
		PayableVendorAccountList._toForm(form);
		PrereceiveableAccountList._toForm(form);
		BankDebtList._toForm(form);
		PayableOtherAccountList._toForm(form);
		Other._toForm(form);
		TableInfo._toForm(form);
			
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
<style type="text/css">
	.emp_table {
	width: 70%;
	border-collapse: collapse;
	border-spacing: 0;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
}
</style>
</head>
<body class="page_content" >
	<emp:tabGroup mainTab="mainInfo" id="资产负债表">
		<emp:tab label="资产负债表" id="mainInfo" >
	<emp:form id="submitForm" action="saveAssetAndDebt.do" method="POST">
	<div class='emp_gridlayout_title'>
		<emp:label text="现金和银行账款"></emp:label>
	</div>
	<emp:table icollName="CurrentAndDepositList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="remark" label="备注" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="应收账款"></emp:label>
	</div>
	<emp:table icollName="AccountReceivableList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="begin_date" label="发生日期" flat="false" />
			<emp:text id="end_date" label="结束日期" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="预付账款"></emp:label>
	</div>
	<emp:table icollName="AccountPrePayableList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="remark" label="备注" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="存货"></emp:label>
	</div>
	<emp:table icollName="StockList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"  flat="false" />
			<emp:text id="num" label="数量" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="固定资产"></emp:label>
	</div>
	<emp:table icollName="FixedAssetList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="fix_asset_details" label="明细" flat="false"/>
			<emp:text id="fix_asset_buy_date" label="购置日"  flat="false" />
			<emp:text id="fix_asset_primary_value" label="原值" flat="false" dataType="Currency"/>
			<emp:text id="fix_asset_depreciation" label="折旧" flat="false" dataType="Currency"/>
			<emp:text id="fix_asset_current_value" label="现值" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="其他资产"></emp:label>
	</div>
	<emp:table icollName="OtherAssetList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="begin_date" label="发生日期" flat="false" />
			<emp:text id="end_date" label="结束日期" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="其他非经营资产或表外资产"></emp:label>
	</div>
	<emp:table icollName="OtherAssetAndNontableAssetList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="remark" label="备注" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="财产情况——房产"></emp:label>
	</div>
	<emp:table icollName="HouseList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="code_num" label="序号"   flat="false" />
			<emp:text id="owner_name" label="抵质押人姓名" flat="false" />
			<emp:text id="hou_num" label="房产证号" flat="false" />
			<emp:text id="hou_addr" label="房产地址" flat="false" />
			<emp:text id="hou_area" label="房屋面积" flat="false" dataType="Number"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="财产情况——车辆"></emp:label>
	</div>
	<emp:table icollName="MotorCarList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="code_num" label="序号"   flat="false" />
			<emp:text id="owner_name" label="抵质押人姓名" flat="false" />
			<emp:text id="car_flag_no" label="车牌号/车架号" flat="false" />
			<emp:text id="buy_money" label="购置价格" flat="false" dataType="Currency"/>
			<emp:text id="cus_id_car" label="车辆登记证书编号" flat="false" />
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="财产情况——银行账户"></emp:label>
	</div>
	<emp:table icollName="BankAccountList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="code_num" label="序号"   flat="false" />
			<emp:text id="accout_name" label="账户名称" flat="false" />
			<emp:text id="bank_name" label="开户行" flat="false" />
			<emp:text id="accout_no" label="账户号" flat="false" />
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	<!-- 负债 -->
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="应付供货商账款"></emp:label>
	</div>
	<emp:table icollName="PayableVendorAccountList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="begin_date" label="发生日期" flat="false" />
			<emp:text id="end_date" label="结束日期" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="预收账款"></emp:label>
	</div>
	<emp:table icollName="PrereceiveableAccountList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="remark" label="备注" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="银行借款"></emp:label>
	</div>
	<emp:table icollName="BankDebtList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="bank_name" label="银行"   flat="false" />
			<emp:text id="begin_date" label="发生日期" flat="false" />
			<emp:text id="end_date" label="结束日期" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="应付其他账款"></emp:label>
	</div>
	<emp:table icollName="PayableOtherAccountList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="pro_num" label="项目"   flat="false" />
			<emp:text id="begin_date" label="发生日期" flat="false" />
			<emp:text id="end_date" label="结束日期" flat="false" />
			<emp:text id="amt" label="金额" flat="false" dataType="Currency"/>
			<emp:text id="fnc_type" label="资产类型" readonly="true" hidden="true"/>
	</emp:table>
	
	<emp:gridLayout id="OtherGroup" title="其他" maxColumn="2">
			<emp:text id="Other.pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="Other.serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="Other.fnc_flag" label="科目" readonly="true" hidden="true"/>
			<emp:text id="Other.apply_money" label="申请金额" dataType="Currency"/>
			<emp:text id="Other.amount_money" label="对外担保金额"  dataType="Currency" />
	</emp:gridLayout>	
	<emp:gridLayout id="TableInfoGroup" title="表信息" maxColumn="2">
			<emp:text id="TableInfo.pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="TableInfo.serno" label="业务流水号" hidden="true"/>
			<emp:text id="TableInfo.fnc_type" label="类型" readonly="true" hidden="true" colSpan="2"/>
			<emp:text id="TableInfo.inser_user" label="制表人" />
			<emp:text id="TableInfo.inser_date" label="日期"/>
			<emp:text id="TableInfo.application" label="申请人"/>
			<emp:text id="TableInfo.apply_date" label="申请日期"/>
	</emp:gridLayout>	
	</emp:form>
	<div align="center" >
		<emp:button id="save" label="保存"/>
		<emp:button id="back" label="返回"/>
	</div> 	
	</emp:tab>
	<emp:tab id="docInfo" label="调查报告" url="rqReport/reportShowPage.jsp" reqParams="reportId=dcbg_wx_zcfzb.raq&serno=$TableInfo.serno;&pageMark=no" initial="true" needFlush="true"/>	
</emp:tabGroup>
</body>
</html>
</emp:page>
