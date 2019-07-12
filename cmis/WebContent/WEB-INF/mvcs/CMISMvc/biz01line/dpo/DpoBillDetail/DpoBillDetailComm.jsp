<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------票据明细调用JS----------------------------
//-----------------------票据种类控制操作------------------------------
	function chageBillType(){
		var bizType = IqpBillDetailInfo.bill_type._getValue();
		if(bizType==100){
			IqpBillDetailInfo.aaorg_type._setValue("");
			IqpBillDetailInfo.aaorg_no._setValue("");
			IqpBillDetailInfo.aaorg_name._setValue("");
			IqpBillDetailInfo.accptr_cmon_code._setValue("");
			IqpBillDetailInfo.aaorg_acct_no._setValue("");
			
			IqpBillDetailInfo.aaorg_type._obj._renderHidden(true);
			IqpBillDetailInfo.aaorg_type._obj._renderRequired(false);
			IqpBillDetailInfo.aaorg_no._obj._renderHidden(true);
			IqpBillDetailInfo.aaorg_no._obj._renderRequired(false);
			IqpBillDetailInfo.aaorg_name._obj._renderHidden(true);
			IqpBillDetailInfo.aaorg_name._obj._renderRequired(false);
			IqpBillDetailInfo.accptr_cmon_code._obj._renderHidden(true);
			IqpBillDetailInfo.accptr_cmon_code._obj._renderRequired(false);
			IqpBillDetailInfo.aaorg_acct_no._obj._renderHidden(true);
			IqpBillDetailInfo.aaorg_acct_no._obj._renderRequired(false);

			IqpBillDetailInfo.aorg_type._obj._renderHidden(false);
			IqpBillDetailInfo.aorg_type._obj._renderRequired(true);
			IqpBillDetailInfo.aorg_no._obj._renderHidden(false);
			IqpBillDetailInfo.aorg_no._obj._renderRequired(true);
			IqpBillDetailInfo.aorg_name._obj._renderHidden(false);
			IqpBillDetailInfo.aorg_name._obj._renderRequired(true);

			IqpBillDetailInfo.drwr_org_code._obj._renderRequired(false);
			IqpBillDetailInfo.tcont_no._obj._renderRequired(false);
			IqpBillDetailInfo.tcont_amt._obj._renderRequired(false);
			
		}else if(bizType==200){
			IqpBillDetailInfo.aorg_type._setValue("");
			IqpBillDetailInfo.aorg_no._setValue("");
			IqpBillDetailInfo.aorg_name._setValue("");
			IqpBillDetailInfo.aorg_type._obj._renderHidden(true);
			IqpBillDetailInfo.aorg_type._obj._renderRequired(false);
			IqpBillDetailInfo.aorg_no._obj._renderHidden(true);
			IqpBillDetailInfo.aorg_no._obj._renderRequired(false);
			IqpBillDetailInfo.aorg_name._obj._renderHidden(true);
			IqpBillDetailInfo.aorg_name._obj._renderRequired(false);
			
			IqpBillDetailInfo.aaorg_type._obj._renderHidden(false);
			IqpBillDetailInfo.aaorg_type._obj._renderRequired(true);
			IqpBillDetailInfo.aaorg_no._obj._renderHidden(false);
			IqpBillDetailInfo.aaorg_no._obj._renderRequired(true);
			IqpBillDetailInfo.aaorg_name._obj._renderHidden(false);
			IqpBillDetailInfo.aaorg_name._obj._renderRequired(true);
			IqpBillDetailInfo.accptr_cmon_code._obj._renderHidden(false);
			IqpBillDetailInfo.accptr_cmon_code._obj._renderRequired(true);
			IqpBillDetailInfo.aaorg_acct_no._obj._renderHidden(false);
			IqpBillDetailInfo.aaorg_acct_no._obj._renderRequired(true);

			IqpBillDetailInfo.drwr_org_code._obj._renderRequired(true);
			IqpBillDetailInfo.tcont_no._obj._renderRequired(true);
			IqpBillDetailInfo.tcont_amt._obj._renderRequired(true);
		}
	};
//-----------------------------------------------------------

</script>