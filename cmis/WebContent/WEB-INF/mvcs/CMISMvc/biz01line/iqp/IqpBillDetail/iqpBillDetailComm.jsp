<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------票据明细调用JS----------------------------
//-----------------------票据种类控制操作------------------------------
	function chageBillType(){
		var bizType = IqpBillDetail.bill_type._getValue();
		if(bizType==100){
			IqpBillDetail.aaorg_type._setValue("");
			IqpBillDetail.aaorg_no._setValue("");
			IqpBillDetail.aaorg_name._setValue("");
			IqpBillDetail.accptr_cmon_code._setValue("");
			IqpBillDetail.aaorg_acct_no._setValue("");
			
			IqpBillDetail.aaorg_type._obj._renderHidden(true);
			IqpBillDetail.aaorg_type._obj._renderRequired(false);
			IqpBillDetail.aaorg_no._obj._renderHidden(true);
			IqpBillDetail.aaorg_no._obj._renderRequired(false);
			IqpBillDetail.aaorg_name._obj._renderHidden(true);
			IqpBillDetail.aaorg_name._obj._renderRequired(false);
			IqpBillDetail.accptr_cmon_code._obj._renderHidden(true);
			IqpBillDetail.accptr_cmon_code._obj._renderRequired(false);
			IqpBillDetail.aaorg_acct_no._obj._renderHidden(true);
			IqpBillDetail.aaorg_acct_no._obj._renderRequired(false);

			IqpBillDetail.aorg_type._obj._renderHidden(false);
			IqpBillDetail.aorg_type._obj._renderRequired(true);
			IqpBillDetail.aorg_no._obj._renderHidden(false);
			IqpBillDetail.aorg_no._obj._renderRequired(true);
			IqpBillDetail.aorg_name._obj._renderHidden(false);
			IqpBillDetail.aorg_name._obj._renderRequired(true);
		}else if(bizType==200){
			IqpBillDetail.aorg_type._setValue("");
			IqpBillDetail.aorg_no._setValue("");
			IqpBillDetail.aorg_name._setValue("");
			IqpBillDetail.aorg_type._obj._renderHidden(true);
			IqpBillDetail.aorg_type._obj._renderRequired(false);
			IqpBillDetail.aorg_no._obj._renderHidden(true);
			IqpBillDetail.aorg_no._obj._renderRequired(false);
			IqpBillDetail.aorg_name._obj._renderHidden(true);
			IqpBillDetail.aorg_name._obj._renderRequired(false);
			
			IqpBillDetail.aaorg_type._obj._renderHidden(false);
			IqpBillDetail.aaorg_type._obj._renderRequired(true);
			IqpBillDetail.aaorg_no._obj._renderHidden(false);
			IqpBillDetail.aaorg_no._obj._renderRequired(true);
			IqpBillDetail.aaorg_name._obj._renderHidden(false);
			IqpBillDetail.aaorg_name._obj._renderRequired(true);
			IqpBillDetail.accptr_cmon_code._obj._renderHidden(false);
			IqpBillDetail.accptr_cmon_code._obj._renderRequired(false);
			IqpBillDetail.aaorg_acct_no._obj._renderHidden(false);
			IqpBillDetail.aaorg_acct_no._obj._renderRequired(true);
		}
	};


	//票据签发日onchange事件响应方法。
	function startDateChange(){
		var str_date=IqpBillDetail.bill_isse_date._getValue();
		var openDay='${context.OPENDAY}';
		if(str_date){
			var flag = CheckDate1BeforeDate2(str_date,openDay);
			if(!flag){
				alert("【票据签发日】要小于等于当前日期！");
				IqpBillDetail.bill_isse_date._setValue("");
				return false;
			}
			var endDate = IqpBillDetail.porder_end_date._getValue();
			if(endDate){
				CheckBillDate();
			}
	     }
	}
	
    //票据到期日期onchange事件响应方法。
	function endDateChange(){
		var endDate=IqpBillDetail.porder_end_date._getValue();
		var openDay='${context.OPENDAY}';
		if(endDate){
			var flag = CheckDate1BeforeDate2(openDay,endDate);
			if(!flag){
				alert("当前日期要小于等于【汇票到期日】！");
				IqpBillDetail.porder_end_date._setValue("");
				return false;
			}
			var startDate = IqpBillDetail.bill_isse_date._getValue();
			if(startDate){
				CheckBillDate();
			}
	     }
	}
	
	//校验票据期限。
	function CheckBillDate(){
		var isEbillFlag = IqpBillDetail.is_ebill._obj.element.value;
		var start = IqpBillDetail.bill_isse_date._getValue();
		var end = IqpBillDetail.porder_end_date._getValue();
		//到期日期小于签发日期
		var flag = CheckDate1BeforeDate2(end,start);
		if(flag){
            alert("【汇票到期日】必须大于【票据签发日】,请重新输入！");
            IqpBillDetail.porder_end_date._setValue("");
            return false;
		}
		if(isEbillFlag=="1"){//是电子汇票时，汇票到期日不能大于签发日后延一年
			flag = CheckDate1BeforeDate2(end,DateAddMonth(start,12));
			if(flag){
				IqpBillDetail.porder_end_date._setValue(end);
			}else{
				alert("【汇票到期日】不能大于【票据签发日】后延一年,请重新输入！");
				IqpBillDetail.porder_end_date._setValue("");
				return false;
			}
		}else if(isEbillFlag=="2"){//不是电子汇票时，汇票到期日不能大于签发日后延六个月
			flag = CheckDate1BeforeDate2(end,DateAddMonth(start,6));
			if(flag){
				IqpBillDetail.porder_end_date._setValue(end);
			}else{
				alert("【汇票到期日】不能大于【票据签发日】后延六个月,请重新输入！");
				IqpBillDetail.porder_end_date._setValue("");
				return false;
			}
	    }
	}

	//是否电票下拉框onchange事件响应方法。
	function isEbillChange(){
		startDateChange();
		endDateChange();
	}

	//校验组织机构代码
	function checkCertCode(){
		var certCode =IqpBillDetail.drwr_org_code._getValue();
		if(certCode!=""){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				IqpBillDetail.drwr_org_code._setValue("");
				return false;
			}
		}
	};

	function getOrgCode(){

	}

	//贸易合同金额onchange事件响应方法，校验合同金额不能小于票面金额
	function contAmtChange(){
		var contAmt = IqpBillDetail.tcont_amt._getValue();//贸易合同金额
		var drftAmt = IqpBillDetail.drft_amt._getValue();//票面金额
		if(!drftAmt){
           alert("请先填写【票面金额】！");
           IqpBillDetail.tcont_amt._setValue("");
           return false;
		}
		if(contAmt){
           if(parseFloat(contAmt) < parseFloat(drftAmt)){
               alert("【贸易合同金额】不能小于【票面金额】,请重新输入！");
               IqpBillDetail.tcont_amt._setValue("");
               return false;
           }
	    }
	}
	//票面金额onchange事件响应方法，校验合同金额不能小于票面金额
	function tcontAmtChange(){
		var contAmt = IqpBillDetail.tcont_amt._getValue();//贸易合同金额
		var drftAmt = IqpBillDetail.drft_amt._getValue();//票面金额
		if(contAmt){
           if(parseFloat(contAmt) < parseFloat(drftAmt)){
               alert("【贸易合同金额】不能小于【票面金额】,请重新输入！");
               IqpBillDetail.tcont_amt._setValue("");
               return false;
           }
	    }
	}
//-----------------------------------------------------------

</script>