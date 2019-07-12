<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
//-----------------------票据批次管理调用JS----------------------------
//-----------------------直贴业务隐藏操作------------------------------
	function chageBizType(){
		var bizType = IqpBatchMng.biz_type._getValue();
		if(bizType==07){//直贴
			IqpBatchMng.opp_org_type._setValue("");
			IqpBatchMng.opp_org_no._setValue("");
			IqpBatchMng.opp_org_name._setValue("");
			IqpBatchMng.rate._setValue("");
			IqpBatchMng.rebuy_date._setValue("");
			IqpBatchMng.rebuy_int._setValue("");
			IqpBatchMng.rebuy_int._obj._renderHidden(true);
			IqpBatchMng.rebuy_int._obj._renderRequired(false);
			IqpBatchMng.opp_org_type._obj._renderHidden(true);
			IqpBatchMng.opp_org_type._obj._renderRequired(false);
			IqpBatchMng.opp_org_no._obj._renderHidden(true);
			IqpBatchMng.opp_org_no._obj._renderRequired(false);
			IqpBatchMng.opp_org_name._obj._renderHidden(true);
			IqpBatchMng.opp_org_name._obj._renderRequired(false);
			IqpBatchMng.rate._obj._renderHidden(true);
			IqpBatchMng.rate._obj._renderRequired(false);
			IqpBatchMng.rebuy_date._obj._renderHidden(true);
			IqpBatchMng.rebuy_date._obj._renderRequired(false);
			IqpBatchMng.rebuy_rate._obj._renderHidden(true);//回购利率
			IqpBatchMng.rebuy_rate._obj._renderRequired(false);
			IqpBatchMng.due_rebuy_rate._obj._renderHidden(true);//逾期回购利率
			IqpBatchMng.due_rebuy_rate._obj._renderRequired(false);

			$(".emp_field_label:eq(3)").text("转/贴现日期");
			$(".emp_field_label:eq(4)").text("转/再贴现利率");
			$(".emp_field_label:eq(5)").text("回购日期");
		}else if(bizType==04 || bizType==02){//卖出回购,买入返售
			//IqpBatchMng.int_amt._obj._renderHidden(true);
			IqpBatchMng.rebuy_int._obj._renderHidden(true);
			IqpBatchMng.rebuy_int._obj._renderRequired(false);
			IqpBatchMng.rebuy_date._obj._renderHidden(false);
			IqpBatchMng.rebuy_date._obj._renderRequired(true);
			IqpBatchMng.opp_org_type._obj._renderHidden(false);
			IqpBatchMng.opp_org_type._obj._renderRequired(true);
			IqpBatchMng.opp_org_no._obj._renderHidden(false);
			IqpBatchMng.opp_org_no._obj._renderRequired(true);
			IqpBatchMng.opp_org_name._obj._renderHidden(false);
			IqpBatchMng.opp_org_name._obj._renderRequired(true);
			IqpBatchMng.rate._obj._renderHidden(false);
			IqpBatchMng.rate._obj._renderRequired(true);
			IqpBatchMng.rebuy_rate._obj._renderHidden(true);//回购利率
			IqpBatchMng.rebuy_rate._obj._renderRequired(false);
			IqpBatchMng.due_rebuy_rate._obj._renderHidden(true);//逾期回购利率
			IqpBatchMng.due_rebuy_rate._obj._renderRequired(false);

			$(".emp_field_label:eq(3)").text("回购起始日");
			$(".emp_field_label:eq(4)").text("回购利率");
			$(".emp_field_label:eq(5)").text("回购到期日");
			
		}else {
			IqpBatchMng.rebuy_int._obj._renderHidden(true);
			IqpBatchMng.rebuy_int._obj._renderRequired(false);
			IqpBatchMng.rebuy_date._obj._renderHidden(true);
			IqpBatchMng.rebuy_date._obj._renderRequired(false);
			IqpBatchMng.opp_org_type._obj._renderHidden(false);
			IqpBatchMng.opp_org_type._obj._renderRequired(true);
			IqpBatchMng.opp_org_no._obj._renderHidden(false);
			IqpBatchMng.opp_org_no._obj._renderRequired(true);
			IqpBatchMng.opp_org_name._obj._renderHidden(false);
			IqpBatchMng.opp_org_name._obj._renderRequired(true);
			IqpBatchMng.rate._obj._renderHidden(false);
			IqpBatchMng.rate._obj._renderRequired(true);
			IqpBatchMng.rebuy_rate._obj._renderHidden(true);//回购利率
			IqpBatchMng.rebuy_rate._obj._renderRequired(false);
			IqpBatchMng.due_rebuy_rate._obj._renderHidden(true);//逾期回购利率
			IqpBatchMng.due_rebuy_rate._obj._renderRequired(false);

			$(".emp_field_label:eq(3)").text("转/贴现日期");
			$(".emp_field_label:eq(4)").text("转/再贴现利率");
			$(".emp_field_label:eq(5)").text("回购日期");
		}
	};
	//校验 转/贴现日期
	function discDateCheck(){
		 var fore_disc_date = IqpBatchMng.fore_disc_date._getValue();//转/贴现日期
		 var biz_type = IqpBatchMng.biz_type._getValue();
		 if(fore_disc_date){
			 /*var flag = CheckDateAfterToday(fore_disc_date);
			 if(!flag){
				alert("【转/贴现日期】要大于当前日期！");
				IqpBatchMng.fore_disc_date._setValue("");
				return false;
			 }*/
			 //var todayDate=getCurrentDate();
			 var todayDate='${context.OPENDAY}';
			 var flag = CheckDate1BeforeDate2(fore_disc_date,todayDate);
             if(fore_disc_date==todayDate){
                 return true;
             }
             if(flag){
                 if(biz_type == '02' || biz_type == '04'){
                	 alert("【回购起始日】不能小于当前日期！");
                 }else{
                	 alert("【转/贴现日期】不能小于当前日期！");
                 }
 				 IqpBatchMng.fore_disc_date._setValue("");
 				 return false;
             }
             //买入反售 02
             if(biz_type == '02'){
            	 var rebuy_date = IqpBatchMng.rebuy_date._getValue();//回购日期
            	 if(rebuy_date){
            		 var flag = CheckDate1BeforeDate2(rebuy_date,fore_disc_date);
                     if(rebuy_date==todayDate){
                         return true;
                     }
                     if(flag){
                    	 alert("【回购起始日】不能超过【回购到日期】");
                    	 IqpBatchMng.fore_disc_date._setValue("");
         				 return false;
                     }
                 }
             }
		 }
	}

	//校验回购日期
	function rebuyDateCheck(){
		 var rebuy_date = IqpBatchMng.rebuy_date._getValue();//回购日期
		 var biz_type = IqpBatchMng.biz_type._getValue();
		 if(rebuy_date){
			 //var todayDate=getCurrentDate();
			 var todayDate='${context.OPENDAY}';
			 var flag = CheckDate1BeforeDate2(rebuy_date,todayDate);
             if(rebuy_date==todayDate){
                 return true;
             }
             if(flag){
            	 alert("【回购到期日】不能小于当前日期！");
 				 IqpBatchMng.rebuy_date._setValue("");
 				 return false;
             }
             //买入反售 02
             if(biz_type == '02'){
            	 var fore_disc_date = IqpBatchMng.fore_disc_date._getValue();//转/贴现日期
            	 if(fore_disc_date){
            		 var flag = CheckDate1BeforeDate2(rebuy_date,fore_disc_date);
                     if(rebuy_date==todayDate){
                         return true;
                     }
                     if(flag){
                    	 alert("【回购起始日】不能超过【回购到期日】");
         				 IqpBatchMng.rebuy_date._setValue("");
         				 return false;
                     }
                 }
             }
			 discAndRebuyDateCheck();
		 }
	}

	//校验转贴现日期和回购日期
	function discAndRebuyDateCheck(){
		var biz_type = IqpBatchMng.biz_type._getValue();//业务类型 07：直贴  04：卖出回购
		//如果是卖出回购，那么回购日期一定要大于转贴现日期。
		if(biz_type=="04"){
            var fore_disc_date = IqpBatchMng.fore_disc_date._getValue();//转/贴现日期
            var rebuyDate = IqpBatchMng.rebuy_date._getValue();//回购日期
            if(fore_disc_date && rebuyDate){
            	var flag = CheckDate1BeforeDate2(rebuyDate,fore_disc_date);
            	if(flag){
                    alert("【回购到期日】一定要大于【回购起始日】，请重新输入！");
                    IqpBatchMng.rebuy_date._setValue("");
                    return false;
                }
            }
		}
		return true;	
	}
	
//-----------------------------------------------------------

</script>