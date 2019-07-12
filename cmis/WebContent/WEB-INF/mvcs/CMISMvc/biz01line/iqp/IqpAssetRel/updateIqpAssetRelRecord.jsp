<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String takeover_type=""; 
	if(context.containsKey("takeover_type")){
		takeover_type = (String)context.getDataValue("takeover_type");
	}
	
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
    function _doKeypressDown() {
	    try{
		   if(IqpAssetRel.takeover_frate._obj.element.focus){
			   IqpAssetRel.takeover_frate._obj.element.select();
	       }
	    }catch(e){
		    alert(e);
	    }
    }
        
	function onload(){
		ir_accord_typeChange("init");
        if('<%=takeover_type%>'=="01"||'<%=takeover_type%>'=="02"){
        	IqpAssetRel.bill_no._obj.addOneButton("bill_no","选择",getBillForm);
        	readonly();
        }else{
        	IqpAssetRel.repay_type_displayname._obj._renderRequired(true);
        	IqpAssetRel.guar_type._obj._renderRequired(true);
        }
    	if(IqpAssetRel.ir_accord_type._getValue()=='02'||IqpAssetRel.ir_accord_type._getValue()=='04'){
			setRulingMounth();
		}else{
			getRealityMounth();
		}

    	changeIrFloatType();
		getRelYM();
		changeOverdueFloatType();
		//getOverdueRateY();
		changeDefaultFloatType();
		//getDefaultRateY();
		checkFromRepayType();

		var belg_line = "${context.IqpAssetRel.belg_line}";
		IqpAssetRel.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline='+belg_line;

		ifRrAccordType();
    }
		function getBillForm(){
			var cus_id = IqpAssetRel.cus_id._getValue();
			var url = "<emp:url action='queryIqpAverageAssetPop.do'/>&returnMethod=getBill&restrictUsed=flase";
			url=EMPTools.encodeURI(url);  
	      	window.open(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
		};
		//只读方法
		function readonly(){
			IqpAssetRel.cus_id._obj._renderReadonly(true);
			IqpAssetRel.cus_name._obj._renderReadonly(true);
			IqpAssetRel.cont_no._obj._renderReadonly(true);
			IqpAssetRel.repay_type._obj._renderReadonly(true);
			IqpAssetRel.repay_type_displayname._obj._renderReadonly(true);
			IqpAssetRel.repay_term._obj._renderReadonly(true);
			IqpAssetRel.repay_space._obj._renderReadonly(true);
			IqpAssetRel.ei_type._obj._renderReadonly(true);
			IqpAssetRel.five_class._obj._renderReadonly(true);
			IqpAssetRel.guar_type._obj._renderReadonly(true);	
			//IqpAssetRel.guar_desc._obj._renderReadonly(true);
			IqpAssetRel.asset_no._obj._renderReadonly(true);
			IqpAssetRel.loan_amt._obj._renderReadonly(true);
			IqpAssetRel.loan_bal._obj._renderReadonly(true);
			//IqpAssetRel.takeover_amt._obj._renderReadonly(true);
			//IqpAssetRel.takeover_frate._obj._renderReadonly(true);
			//IqpAssetRel.agent_asset_acct._obj._renderReadonly(true);
			//IqpAssetRel.takeover_rate._obj._renderReadonly(true);
			//IqpAssetRel.takeover_int._obj._renderReadonly(true);
		 	IqpAssetRel.loan_start_date._obj._renderReadonly(true);
			IqpAssetRel.loan_end_date._obj._renderReadonly(true);
			//IqpAssetRel.latest_repay._obj._renderReadonly(true);

			IqpAssetRel.ir_accord_type._obj._renderReadonly(true);
			IqpAssetRel.ir_type._obj._renderReadonly(true);
			IqpAssetRel.ruling_ir._obj._renderReadonly(true);
			ruling_mounth._obj._renderReadonly(true);
			IqpAssetRel.pad_rate_y._obj._renderReadonly(true);
			IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);
			IqpAssetRel.ir_next_adjust_term._obj._renderReadonly(true);
			IqpAssetRel.ir_next_adjust_unit._obj._renderReadonly(true);
			IqpAssetRel.fir_adjust_day._obj._renderReadonly(true);
			IqpAssetRel.ir_float_type._obj._renderReadonly(true);
			IqpAssetRel.ir_float_rate._obj._renderReadonly(true);
			IqpAssetRel.ir_float_point._obj._renderReadonly(true);
			IqpAssetRel.reality_ir_y._obj._renderReadonly(true);
			reality_mounth._obj._renderReadonly(true);
			IqpAssetRel.overdue_float_type._obj._renderReadonly(true);
			IqpAssetRel.overdue_rate._obj._renderReadonly(true);
			IqpAssetRel.overdue_point._obj._renderReadonly(true);
			IqpAssetRel.overdue_rate_y._obj._renderReadonly(true);
			IqpAssetRel.default_float_type._obj._renderReadonly(true);
			IqpAssetRel.default_rate._obj._renderReadonly(true);
			IqpAssetRel.default_point._obj._renderReadonly(true);
			IqpAssetRel.default_rate_y._obj._renderReadonly(true);
			IqpAssetRel.prd_id._obj._renderReadonly(true);

			IqpAssetRel.manager_br_id_displayname._obj._renderReadonly(true);
			IqpAssetRel.fina_br_id_displayname._obj._renderReadonly(true);
		};
	function returnCus(data){
		IqpAssetRel.cus_id._setValue(data.cus_id._getValue());
		IqpAssetRel.cus_name._setValue(data.cus_name._getValue());
		IqpAssetRel.fina_br_id._setValue(data.main_br_id._getValue());
		IqpAssetRel.fina_br_id_displayname._setValue(data.main_br_id_displayname._getValue());

		IqpAssetRel.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline='+data.belg_line._getValue();
		IqpAssetRel.prd_id._setValue("");
		IqpAssetRel.prd_name._setValue("");
	};	
	function doSub(){
		var asset_no = IqpAssetRel.asset_no._getValue();
		var form = document.getElementById("submitForm");
		IqpAssetRel._checkAll();
		if(IqpAssetRel._checkAll()){
			IqpAssetRel._toForm(form); 
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
						alert("修改成功!");
						var url = '<emp:url action="queryIqpAssetRelList.do"/>?asset_no='+IqpAssetRel.asset_no._getValue();
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("请重新选择客户!"); 
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
	//安排费的计算
	function afee(){
        var afee_type = IqpAssetRel.afee_type._getValue();
        if(afee_type=='02'){
            var amt = IqpAssetRel.takeover_amt._getValue();
            var frate = IqpAssetRel.takeover_frate._getValue();
            if(amt!="" && frate!=""){
            	IqpAssetRel.afee._obj._renderReadonly(true);
            	//未完成 
            }else{
                alert("请输入转让金额和转让费率");
                IqpAssetRel.afee_type._setValue("");
            }
        	
        	
        }
		
	};	

	function getBill(data){
		IqpAssetRel.bill_no._setValue(data.bill_no._getValue());
		IqpAssetRel.cus_id._setValue(data.cus_id._getValue());
		IqpAssetRel.cus_name._setValue(data.cus_id_displayname._getValue());
		IqpAssetRel.cont_no._setValue(data.cont_no._getValue());
		IqpAssetRel.repay_type._setValue(data.repay_type._getValue());
		IqpAssetRel.repay_type_displayname._setValue(data.repay_type_displayname._getValue());
		IqpAssetRel.repay_term._setValue(data.repay_term._getValue());
		IqpAssetRel.repay_space._setValue(data.repay_space._getValue());
		IqpAssetRel.ei_type._setValue(data.interest_term._getValue());
		IqpAssetRel.five_class._setValue(data.five_class._getValue());
		IqpAssetRel.guar_type._setValue(data.assure_main._getValue());
		IqpAssetRel.loan_amt._setValue(data.loan_amt._getValue());
		IqpAssetRel.loan_bal._setValue(data.loan_balance._getValue());
		IqpAssetRel.takeover_amt._setValue('');
		IqpAssetRel.takeover_frate._setValue('');
	    //IqpAssetRel.loan_rate._setValue(data.reality_ir_y._getValue());
		//IqpAssetRel.rate_adj_type._setValue('');
		//IqpAssetRel.rate_pefloat._setValue('');
		IqpAssetRel.takeover_rate._setValue('');
		IqpAssetRel.takeover_int._setValue('');
	 	IqpAssetRel.loan_start_date._setValue(data.distr_date._getValue());
		IqpAssetRel.loan_end_date._setValue(data.end_date._getValue());
		IqpAssetRel.latest_repay._setValue('');
		IqpAssetRel.afee_type._setValue('');
		IqpAssetRel.afee_pay_type._setValue('');
		IqpAssetRel.afee._setValue('');
		IqpAssetRel.mfee._setValue('');
		IqpAssetRel.int._setValue('');
		IqpAssetRel.agent_asset_acct._setValue('');

		IqpAssetRel.ir_accord_type._setValue(data.ir_accord_type._getValue());
		IqpAssetRel.ir_type._setValue(data.ir_type._getValue());
		IqpAssetRel.ruling_ir._setValue(data.ruling_ir._getValue());
		//ruling_mounth._setValue(data.repay_type._getValue());
		IqpAssetRel.pad_rate_y._setValue(data.pad_rate_y._getValue());
		IqpAssetRel.ir_adjust_type._setValue(data.ir_adjust_type._getValue());
		IqpAssetRel.ir_next_adjust_term._setValue(data.ir_next_adjust_term._getValue());
		IqpAssetRel.ir_next_adjust_unit._setValue(data.ir_next_adjust_unit._getValue());
		IqpAssetRel.fir_adjust_day._setValue(data.fir_adjust_day._getValue());
		IqpAssetRel.ir_float_type._setValue(data.ir_float_type._getValue());
		IqpAssetRel.ir_float_rate._setValue(data.ir_float_rate._getValue());
		IqpAssetRel.ir_float_point._setValue(data.ir_float_point._getValue());
		//alert(data.reality_ir_y._getValue());
		IqpAssetRel.reality_ir_y._setValue(data.reality_ir_y._getValue());
		//reality_mounth._setValue(data.repay_type._getValue());
		IqpAssetRel.overdue_float_type._setValue(data.overdue_float_type._getValue());
		IqpAssetRel.overdue_rate._setValue(data.overdue_rate._getValue());
		IqpAssetRel.overdue_point._setValue(data.overdue_point._getValue());
		IqpAssetRel.overdue_rate_y._setValue(data.overdue_rate_y._getValue());
		IqpAssetRel.default_float_type._setValue(data.default_float_type._getValue());
		IqpAssetRel.default_rate._setValue(data.default_rate._getValue());
		IqpAssetRel.default_point._setValue(data.default_point._getValue());
		IqpAssetRel.default_rate_y._setValue(data.default_rate_y._getValue());
		
		IqpAssetRel.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpAssetRel.fina_br_id_displayname._setValue(data.fina_br_id_displayname._getValue());
		IqpAssetRel.manager_br_id._setValue(data.manager_br_id._getValue());
		IqpAssetRel.fina_br_id._setValue(data.fina_br_id._getValue());

		if(data.ir_accord_type._getValue()=='02'||data.ir_accord_type._getValue()=='04'){
			setRulingMounth();
		}else{
			getRealityMounth();
		}
		ir_accord_typeChange("init");
		readonly();
		checkBillNo();
	};

	function checkBal(){
		var loan_bal = IqpAssetRel.loan_bal._getValue();
		var loan_amt = IqpAssetRel.loan_amt._getValue();
		if(loan_bal!=''&&loan_amt!=''){
			if(loan_amt-loan_bal<0){
				alert('贷款余额不能大于贷款金额！');
				IqpAssetRel.loan_bal._setValue('');
			}
		}
	}

	/**
	function checkTakeoverAmt(){
		var loan_bal = IqpAssetRel.loan_bal._getValue();
		if(loan_bal==''){
			alert('未获取贷款金额、贷款余额！');
			IqpAssetRel.takeover_amt._setValue('');
			return;
		}
		var takeover_amt = IqpAssetRel.takeover_amt._getValue();
		if(takeover_amt-loan_bal>0){
			alert('转让金额不能大于贷款余额！');
			IqpAssetRel.takeover_amt._setValue('');
			return;
		}
	}*/

	function countAfee(type){
		var afee_type = IqpAssetRel.afee_type._getValue();
		if(afee_type=='02'){
			IqpAssetRel.afee._obj._renderReadonly(true);
		    var takeover_amt=parseFloat(IqpAssetRel.takeover_amt._getValue());
		    var takeover_frate;
		    if(type == "rate"){
		    	takeover_frate=parseFloat(IqpAssetRel.takeover_frate._getValue()/100);
			}else{
				takeover_frate=parseFloat(IqpAssetRel.takeover_frate._getValue());
			}
			var value=(takeover_amt*takeover_frate).toFixed(2);
			if(!isNaN(takeover_amt)&&!isNaN(takeover_frate)&&!isNaN(value)){
				IqpAssetRel.afee._setValue(value+'');		
			}else{
				IqpAssetRel.afee._setValue('');	
			}
		}else if(afee_type='01'){
			IqpAssetRel.afee._obj._renderReadonly(false);
			IqpAssetRel.afee._setValue('');	
		}
	}

	function caculate(){
		var amt = IqpAssetRel.takeover_amt._getValue();//转让本金
		var reality_ir_y = IqpAssetRel.reality_ir_y._obj.element.value;//执行利率 
        if(reality_ir_y == null || reality_ir_y == "" || reality_ir_y == "null"){
        	reality_ir_y = 0;
        }
		
        var takeover_date = '${context.takeover_date}';//转让日期
        var latest_repay = IqpAssetRel.latest_repay._getValue();//最近还款日

        if(takeover_date != null  && takeover_date != "" && latest_repay != null  && latest_repay != "" && amt != null  && amt != ""){
			if(latest_repay>takeover_date){
               alert("最近还款日不能大于转让日期");
               IqpAssetRel.latest_repay._setValue("");
               return;
		    }
			var aDate,oDate1,oDate2,iDays;
	        aDate = takeover_date.split('-');
	        oDate1 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        aDate = latest_repay.split('-');
	        oDate2 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        iDays = parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);
	        var relInt = parseFloat(amt)*parseFloat(reality_ir_y)*parseFloat(iDays)/parseFloat(360)/parseFloat(100);
	        IqpAssetRel.int._setValue(''+(relInt).toFixed(2)+'');
		}
    };
	
	function checkDate(){
		var loan_start_date = IqpAssetRel.loan_start_date._getValue();
		var loan_end_date = IqpAssetRel.loan_end_date._getValue();
		if(loan_start_date!=''&&loan_end_date!=''){
			if(loan_start_date>loan_end_date){
				alert('起始日期不能大于到期日期！');
				IqpAssetRel.loan_end_date._setValue('');
			}
		}
		var date = '${context.OPENDAY}';
		if(loan_start_date!='' && loan_start_date!=''){
			if(loan_start_date>date){
				alert('起始日期不能大于当前系统时间!');
				IqpAssetRel.loan_start_date._setValue('');
			}
		}
	}

	function checkLatestDate(){
		var loan_start_date = IqpAssetRel.loan_start_date._getValue();
		var loan_end_date = IqpAssetRel.loan_end_date._getValue();
		if(loan_start_date==''||loan_end_date==''){
			alert('请先录入起始日期和到期日期！');
			IqpAssetRel.latest_repay._setValue('');
			return;
		}

		var latest_repay = IqpAssetRel.latest_repay._getValue();
		if(latest_repay != ''){
			if(latest_repay<loan_start_date){
				alert('最近还款日应在起始日期和到期日期之间！');
				IqpAssetRel.latest_repay._setValue('');
				return;
			}
			if(latest_repay>loan_end_date){
				alert('最近还款日应在起始日期和到期日期之间！');
				IqpAssetRel.latest_repay._setValue('');
				return;
			}
		}
	};

	//-------------------利率依据方式下拉框响应方法-----------------------
	function ir_accord_typeChange(data){
		var llyjfs = IqpAssetRel.ir_accord_type._getValue();//利率依据方式
		var llfdfs = IqpAssetRel.ir_float_type._getValue();//利率浮动方式
		if(llyjfs == "01"){//议价利率
			/** 显示控制 */
	    	IqpAssetRel.ir_type._obj._renderHidden(true);//利率种类
			IqpAssetRel.ruling_ir._obj._renderHidden(true); //基准利率（年）
			ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderHidden(false); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderHidden(true);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderHidden(true);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderHidden(true);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				IqpAssetRel.ir_type._setValue("");//利率种类
				IqpAssetRel.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				IqpAssetRel.overdue_rate_y._setValue(""); //逾期利率（年）
				IqpAssetRel.default_rate_y._setValue(""); //违约利率（年）
				IqpAssetRel.pad_rate_y._setValue("");//垫款利率（年）
				IqpAssetRel.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				IqpAssetRel.ir_adjust_type._setValue("0");//利率调整方式
				IqpAssetRel.ir_float_type._setValue("");//利率浮动方式
				IqpAssetRel.ir_float_rate._setValue("");//利率浮动比
				IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
				IqpAssetRel.overdue_float_type._setValue("");//逾期利率浮动方式
				IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
				IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
				IqpAssetRel.default_float_type._setValue("");//违约利率浮动方式
				IqpAssetRel.default_rate._setValue("");//违约利率浮动比
				IqpAssetRel.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			IqpAssetRel.ir_type._obj._renderRequired(false);//利率种类
			IqpAssetRel.ruling_ir._obj._renderRequired(false); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderRequired(true); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderRequired(false);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderRequired(false);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderRequired(false);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数
			

			/** 只读控制 */
			IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
			IqpAssetRel.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
			IqpAssetRel.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderReadonly(false);//违约利率（年）
			
			/** 获取基准利率 */
			//getRate();
	    }else if(llyjfs == "02"){//牌告利率依据
			 /** 显示控制 */
	    	IqpAssetRel.ir_type._obj._renderHidden(false);//利率种类
			IqpAssetRel.ruling_ir._obj._renderHidden(false); //基准利率（年）
			ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderHidden(false); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderHidden(false);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderHidden(false);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderHidden(false);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				IqpAssetRel.ir_type._setValue("");//利率种类
				IqpAssetRel.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				IqpAssetRel.overdue_rate_y._setValue(""); //逾期利率（年）
				IqpAssetRel.default_rate_y._setValue(""); //违约利率（年）
				IqpAssetRel.pad_rate_y._setValue("");//垫款利率（年）
				IqpAssetRel.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				IqpAssetRel.ir_adjust_type._setValue("");//利率调整方式
				IqpAssetRel.ir_float_type._setValue("");//利率浮动方式
				IqpAssetRel.ir_float_rate._setValue("");//利率浮动比
				IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
				IqpAssetRel.overdue_float_type._setValue("");//逾期利率浮动方式
				IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
				IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
				IqpAssetRel.default_float_type._setValue("");//违约利率浮动方式
				IqpAssetRel.default_rate._setValue("");//违约利率浮动比
				IqpAssetRel.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			IqpAssetRel.ir_type._obj._renderRequired(true);//利率种类
			IqpAssetRel.ruling_ir._obj._renderRequired(true); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderRequired(true); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderRequired(true);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderRequired(true);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderRequired(true);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
			IqpAssetRel.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
	    }else if(llyjfs == "03"){//不计息
	        /** 显示控制 */
	    	IqpAssetRel.ir_type._obj._renderHidden(true);//利率种类
			IqpAssetRel.ruling_ir._obj._renderHidden(true); //基准利率（年）
			ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderHidden(true); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderHidden(true); //执行利率（年）
			reality_mounth._obj._renderHidden(true); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderHidden(true);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderHidden(true);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderHidden(true);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数
			//IqpAssetRel.takeover_rate._obj._renderHidden(true);//转让利率
			//IqpAssetRel.takeover_int._obj._renderHidden(true);//转让利息
			

			/** 赋值控制 */
			if(data != "init"){
				IqpAssetRel.ir_type._setValue("");//利率种类
				IqpAssetRel.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				IqpAssetRel.overdue_rate_y._setValue(""); //逾期利率（年）
				IqpAssetRel.default_rate_y._setValue(""); //违约利率（年）
				IqpAssetRel.pad_rate_y._setValue("");//垫款利率（年）
				IqpAssetRel.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				IqpAssetRel.ir_adjust_type._setValue("");//利率调整方式
				IqpAssetRel.ir_float_type._setValue("");//利率浮动方式
				IqpAssetRel.ir_float_rate._setValue("");//利率浮动比
				IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
				IqpAssetRel.overdue_float_type._setValue("");//逾期利率浮动方式
				IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
				IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
				IqpAssetRel.default_float_type._setValue("");//违约利率浮动方式
				IqpAssetRel.default_rate._setValue("");//违约利率浮动比
				IqpAssetRel.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			IqpAssetRel.ir_type._obj._renderRequired(false);//利率种类
			IqpAssetRel.ruling_ir._obj._renderRequired(false); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderRequired(false); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderRequired(false); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderRequired(false);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderRequired(false);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderRequired(false);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数
			//IqpAssetRel.takeover_rate._obj._renderRequired(false);//转让利率
			//IqpAssetRel.takeover_int._obj._renderRequired(false);//转让利息

			/** 只读控制 */
			IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
			IqpAssetRel.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
	    }else if(llyjfs == "04"){//正常利率
			 /** 显示控制 */
	    	IqpAssetRel.ir_type._obj._renderHidden(true);//利率种类
			IqpAssetRel.ruling_ir._obj._renderHidden(false); //基准利率（年）
			ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderHidden(false); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderHidden(false);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderHidden(false);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderHidden(false);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				IqpAssetRel.ir_type._setValue("");//利率种类
				IqpAssetRel.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				IqpAssetRel.overdue_rate_y._setValue(""); //逾期利率（年）
				IqpAssetRel.default_rate_y._setValue(""); //违约利率（年）
				IqpAssetRel.pad_rate_y._setValue("");//垫款利率（年）
				IqpAssetRel.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				IqpAssetRel.ir_adjust_type._setValue("");//利率调整方式
				IqpAssetRel.ir_float_type._setValue("");//利率浮动方式
				IqpAssetRel.ir_float_rate._setValue("");//利率浮动比
				IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
				IqpAssetRel.overdue_float_type._setValue("");//逾期利率浮动方式
				IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
				IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
				IqpAssetRel.default_float_type._setValue("");//违约利率浮动方式
				IqpAssetRel.default_rate._setValue("");//违约利率浮动比
				IqpAssetRel.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			IqpAssetRel.ir_type._obj._renderRequired(false);//利率种类
			IqpAssetRel.ruling_ir._obj._renderRequired(true); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			IqpAssetRel.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			IqpAssetRel.default_rate_y._obj._renderRequired(true); //违约利率（年）
			IqpAssetRel.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			IqpAssetRel.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(true); //执行利率（月）
			IqpAssetRel.ir_adjust_type._obj._renderRequired(true);//利率调整方式
			IqpAssetRel.ir_float_type._obj._renderRequired(true);//利率浮动方式
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			IqpAssetRel.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			IqpAssetRel.default_float_type._obj._renderRequired(true);//违约利率浮动方式
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
			IqpAssetRel.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

			/** 获取基准利率 */
			//getRate();
	    }
	};

	function setRulingMounth(){
		var rateValue = IqpAssetRel.ruling_ir._obj.element.value;
		rateValue = parseFloat(rateValue)/100;
		ruling_mounth._setValue(rateValue/12);
		getReality_ir_y(rateValue);
		getRelYM();
	}

	//-------------------通过基准利率（年）获得执行利率（年）-----------------------
	function getReality_ir_y(rateValue){
		IqpAssetRel.reality_ir_y._setValue(rateValue);
		reality_mounth._setValue(parseFloat(rateValue)/12);	
	};

	//-------------------通过基准利率（月）获得执行利率（月）-----------------------
	function getRealityMounth(){
		var ir_y = IqpAssetRel.reality_ir_y._getValue();
		reality_mounth._setValue(parseFloat(ir_y)/12);	
	};

	//-----------------------利率调整方式js控制------------------------------------
	function ir_adjust_type_change(){
		var irAdjType = IqpAssetRel.ir_adjust_type._getValue();
		if(irAdjType == 0){
			/** 显示控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderHidden(true);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderHidden(true);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderHidden(true);//第一次调整日
			/** 必输控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderRequired(false);//第一次调整日
			/** 值域控制 */
			IqpAssetRel.ir_next_adjust_term._setValue("");//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._setValue("");//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._setValue("");//第一次调整日
		}else if(irAdjType == "FIX"){
			/** 显示控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderHidden(false);//第一次调整日
			/** 必输控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderRequired(true);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderRequired(true);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderRequired(true);//第一次调整日
			
		}else {
			/** 显示控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderHidden(false);//第一次调整日
			/** 必输控制 */
			IqpAssetRel.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
			IqpAssetRel.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
			IqpAssetRel.fir_adjust_day._obj._renderRequired(false);//第一次调整日
		}
	};

	//-------------------根据贷款利率浮动方式同比调整显示-----------------------
	function changeIrFloatType(){
		var floatType = IqpAssetRel.ir_float_type._getValue();
		if(floatType=='0'){//加百分比
			/** 显示控制 */
			IqpAssetRel.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			/** 必输控制 */
			IqpAssetRel.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			/** 值域控制 */
			IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
			
		}else if(floatType=='1'){//加点
			/** 显示控制 */
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
			/** 必输控制 */
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
			/** 值域控制 */
			IqpAssetRel.ir_float_rate._setValue("");//贷款利率浮动比
		}else {
			/** 显示控制 */
			IqpAssetRel.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			/** 必输控制 */
			IqpAssetRel.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
			IqpAssetRel.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			/** 值域控制 */
			IqpAssetRel.ir_float_rate._setValue("");//贷款利率浮动比
			IqpAssetRel.ir_float_point._setValue("");//贷款利率浮动点数
		}
	};
	//-------------------加点、加百分比实时调整执行年、月利率-----------------------
	function getRelYM(){
		var rulY = IqpAssetRel.ruling_ir._obj.element.value;
		rulY = parseFloat(rulY)/100;
		var ir_accord_type  = IqpAssetRel.ir_accord_type._getValue();//利率依据方式 
		if(rulY == null || rulY == ""){
			rulY = 0;
		}
		var rulM = ruling_mounth._getValue();
		var fRate = IqpAssetRel.ir_float_rate._obj.element.value;
		var fPoint = IqpAssetRel.ir_float_point._getValue();
		if(fRate !=null && fRate != ""){//加百分比
			IqpAssetRel.ir_float_point._setValue("");
			var relY =parseFloat(1+(parseFloat(fRate)/100))*rulY; 
			var relM = parseFloat(relY)/12;
			IqpAssetRel.reality_ir_y._setValue(relY);
			reality_mounth._setValue(relM);
		}else if(fPoint !=null && fPoint != ""){//加点
			if(fPoint.search("^[0-9|.|-]*$")!=0){
		        alert("请输入正确数据!");
		        IqpAssetRel.ir_float_point._setValue("");
		        IqpAssetRel.reality_ir_y._setValue(rulY);
				reality_mounth._setValue(parseFloat(rulY)/12);
		        return;
			}
			 IqpAssetRel.ir_float_rate._setValue("");
			 var relY = (parseFloat(rulY)*10000+parseFloat(fPoint))/10000;
			 var relM = Math.round(relY*10000)/120000;
			 IqpAssetRel.reality_ir_y._setValue(relY);
			 reality_mounth._setValue(relM);
		}else {
			IqpAssetRel.ir_float_rate._setValue("");
			IqpAssetRel.ir_float_point._setValue("");
			//只有利率依据方式为牌告利率的时候,执行年利率为基准年利率
			if(ir_accord_type == "02"){
				IqpAssetRel.reality_ir_y._setValue(rulY);
				reality_mounth._setValue(parseFloat(rulY)/12);
			}
		}
		if(ir_accord_type == "02" || ir_accord_type == "04"){
			getOverdueRateY();//更新逾期利率 
		    getDefaultRateY();//更新违约利率
		}
	};
	//-------------------根据逾期利率浮动方式同比调整显示-----------------------
	function changeOverdueFloatType(){
		var overdueFloatType = IqpAssetRel.overdue_float_type._getValue();
		if(overdueFloatType=='0'){//加百分比
			/** 显示控制 */
			IqpAssetRel.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			/** 必输控制 */
			IqpAssetRel.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			/** 值域控制 */
			IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
		}else if(overdueFloatType=='1'){//加点
			/** 显示控制 */
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
			/** 必输控制 */
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
			/** 值域控制 */
			IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
		}else {
			/** 显示控制 */
			IqpAssetRel.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			/** 必输控制 */
			IqpAssetRel.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			IqpAssetRel.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			/** 值域控制 */
			IqpAssetRel.overdue_rate._setValue("");//逾期利率浮动比
			IqpAssetRel.overdue_point._setValue("");//逾期利率浮动点数
		}
	};

	//----------------更新逾期利率-----------------
	function getOverdueRateY(){
		var overdueY = IqpAssetRel.reality_ir_y._getValue();
		if(overdueY == null || overdueY == ""){
			overdueY = 0;
		}
		var fRate = IqpAssetRel.overdue_rate._obj.element.value;
		var fPoint = IqpAssetRel.overdue_point._getValue();
		if(fRate !=null && fRate != ""){//加百分比
			IqpAssetRel.overdue_point._setValue("");
			var relY =parseFloat(1+(parseFloat(fRate)/100))*overdueY; 
			IqpAssetRel.overdue_rate_y._setValue(relY);
		}else if(fPoint !=null && fPoint != ""){//加点
			IqpAssetRel.overdue_rate._setValue("");
			var relY = (parseFloat(overdueY)*10000+parseFloat(fPoint))/10000;
			IqpAssetRel.overdue_rate_y._setValue(relY);
		}else {
			IqpAssetRel.overdue_rate._setValue("");
			IqpAssetRel.overdue_point._setValue("");
			IqpAssetRel.overdue_rate_y._setValue("");
		}
	};
	//-------------------根据违约利率浮动方式同比调整显示-----------------------
	function changeDefaultFloatType(){
		var defaultFloatType = IqpAssetRel.default_float_type._getValue();
		if(defaultFloatType=='0'){//加百分比
			/** 显示控制 */
			IqpAssetRel.default_rate._obj._renderHidden(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数
			/** 必输控制 */
			IqpAssetRel.default_rate._obj._renderRequired(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数
			/** 值域控制 */
			IqpAssetRel.default_point._setValue("");//违约利率浮动点数
		}else if(defaultFloatType=='1'){//加点
			/** 显示控制 */
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(false);//违约利率浮动点数
			/** 必输控制 */
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(true);//违约利率浮动点数
			/** 值域控制 */
			IqpAssetRel.default_rate._setValue("");//违约利率浮动比
		}else {
			/** 显示控制 */
			IqpAssetRel.default_rate._obj._renderHidden(true);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderHidden(true);//违约利率浮动点数
			/** 必输控制 */
			IqpAssetRel.default_rate._obj._renderRequired(false);//违约利率浮动比
			IqpAssetRel.default_point._obj._renderRequired(false);//违约利率浮动点数
			/** 值域控制 */
			IqpAssetRel.default_rate._setValue("");//违约利率浮动比
			IqpAssetRel.default_point._setValue("");//违约利率浮动点数
		}
	};
	//---------------更新违约利率------------------
	function getDefaultRateY(){
		var defaultY = IqpAssetRel.reality_ir_y._getValue();
		if(defaultY == null || defaultY == ""){
			defaultY = 0;
		}
		var fRate = IqpAssetRel.default_rate._obj.element.value;
		var fPoint = IqpAssetRel.default_point._getValue();
		if(fRate !=null && fRate != ""){//加百分比
			IqpAssetRel.default_point._setValue("");
			var relY =parseFloat(1+(parseFloat(fRate)/100))*defaultY; 
			IqpAssetRel.default_rate_y._setValue(relY);
		}else if(fPoint !=null && fPoint != ""){//加点
			IqpAssetRel.default_rate._setValue("");
			var relY = (parseFloat(defaultY)*10000+parseFloat(fPoint))/10000;
			IqpAssetRel.default_rate_y._setValue(relY);
		}else {
			IqpAssetRel.default_rate._setValue("");
			IqpAssetRel.default_point._setValue("");
			IqpAssetRel.default_rate_y._setValue("");
		}
	};

	function getRepayType(data){
		var repay_mode_id = data.repay_mode_id._getValue();
		IqpAssetRel.repay_type._setValue(repay_mode_id);
		IqpAssetRel.repay_type_displayname._setValue(data.repay_mode_dec._getValue());
		IqpAssetRel.repay_mode_type._setValue(data.repay_mode_type._getValue());
		checkFromRepayType();
	};

	//-------------------年利率计算月利率-----------------------
	function reality_ir_yChange(){
		//var reality_ir_y_Value = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
		var reality_ir_y_Value = IqpAssetRel.reality_ir_y._obj.element.value;
		var yll = parseFloat(reality_ir_y_Value)/1200;
		reality_mounth._setValue(yll);

		caculateOverdueRate();
		caculateDefaultRate();
	};

	//------------检查借据是否已存在清单表中------------
	function checkBillNo(){
		var bill_no = IqpAssetRel.bill_no._getValue();
		if(bill_no != ""){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						IqpAssetRel.bill_no._setValue('');
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "failed"){
						alert(msg);
						IqpAssetRel.bill_no._setValue('');
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
				IqpAssetRel.bill_no._setValue('');
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};

			var url = '<emp:url action="checkBillNoForIqpAssetRel.do"/>?bill_no='+bill_no;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		}
	};

	function setRepayTerm(){
		   var term = IqpAssetRel.ei_type._getValue();
	       //计息周期为按年  季 月
	       if(term == "0"){
	    	   IqpAssetRel.repay_term._setValue("Y");
	       }else if(term == "1"){
	    	   IqpAssetRel.repay_term._setValue("Q");
	       }else if(term == "2"){
	    	   IqpAssetRel.repay_term._setValue("M");
	       }
	};

	//通过还款方式判断还款方式信息 
	function checkFromRepayType(){
		var repay_mode_type = IqpAssetRel.repay_mode_type._getValue();
	    if(repay_mode_type == "05"){//还款方式种类为利随本清
	    	var options = IqpAssetRel.ei_type._obj.element.options;
	    	var option1 = new Option('利随本清','4');
	    	var option2 = new Option('放款日结息','5');
	    	options.add(option1);
	    	options.add(option2);
	        
	    	IqpAssetRel.ei_type._setValue("4");
	    	IqpAssetRel.ei_type._obj._renderReadonly(true);
	    	IqpAssetRel.ei_type._obj._renderRequired(true);

	    	IqpAssetRel.repay_term._setValue("");
	    	IqpAssetRel.repay_space._setValue("");

	    	IqpAssetRel.ei_type._obj._renderHidden(false);
	    	IqpAssetRel.repay_term._obj._renderHidden(true);
	    	IqpAssetRel.repay_space._obj._renderHidden(true);

	    	IqpAssetRel.repay_term._obj._renderRequired(false);   
	    	IqpAssetRel.repay_space._obj._renderRequired(false);   
	    }else if(repay_mode_type == "04" || repay_mode_type == "01" || repay_mode_type == "03"){//还款方式种类为按期结息 等额本息 等额本金
	    	removeInterestTerm();

	    	IqpAssetRel.ei_type._obj._renderHidden(false);
	    	IqpAssetRel.repay_term._obj._renderHidden(false);
	    	IqpAssetRel.repay_space._obj._renderHidden(false);

	    	IqpAssetRel.ei_type._obj._renderRequired(true);
	    	IqpAssetRel.repay_term._obj._renderRequired(true);   
	    	IqpAssetRel.repay_space._obj._renderRequired(true);   

	    	IqpAssetRel.ei_type._obj._renderReadonly(false);
	    	IqpAssetRel.repay_space._obj._renderReadonly(false);
	    }else if(repay_mode_type == "07" || repay_mode_type == "08"){//还款方式种类为弹性还款 气球贷
	    	IqpAssetRel.ei_type._setValue("");
	    	IqpAssetRel.repay_term._setValue("");
	    	IqpAssetRel.repay_space._setValue("");

	    	IqpAssetRel.ei_type._obj._renderHidden(true);
	    	IqpAssetRel.repay_term._obj._renderHidden(true);
	    	IqpAssetRel.repay_space._obj._renderHidden(true);

	    	IqpAssetRel.ei_type._obj._renderRequired(false);
	    	IqpAssetRel.repay_term._obj._renderRequired(false);   
	    	IqpAssetRel.repay_space._obj._renderRequired(false);   

	    }else if(repay_mode_type == "09"){//还款方式种类为一次付息到期还本
	    	var options = IqpAssetRel.ei_type._obj.element.options;
	    	var option1 = new Option('利随本清','4');
	    	var option2 = new Option('放款日结息','5');
	    	options.add(option1);
	    	options.add(option2);
	        
	    	IqpAssetRel.ei_type._setValue("5");
	    	IqpAssetRel.ei_type._obj._renderReadonly(true);
	    	IqpAssetRel.ei_type._obj._renderRequired(true);

	    	IqpAssetRel.repay_term._setValue("");
	    	IqpAssetRel.repay_space._setValue("");

	    	IqpAssetRel.ei_type._obj._renderHidden(false);
	    	IqpAssetRel.repay_term._obj._renderHidden(true);
	    	IqpAssetRel.repay_space._obj._renderHidden(true);

	    	IqpAssetRel.repay_term._obj._renderRequired(false);   
	    	IqpAssetRel.repay_space._obj._renderRequired(false);   

	    }
	};
	function removeInterestTerm(){
		var options = IqpAssetRel.ei_type._obj.element.options;
		for(var j=options.length-1;j>=0;j--){
	        if(options[j].value=="4" || options[j].value=="5"){
	        	options.remove(j);
	        }
		}
	};
	function returnPrdId(data){
		IqpAssetRel.prd_id._setValue(data.id);
		IqpAssetRel.prd_name._setValue(data.label);
	};
	//取基准利率
	function getRate(){
		var loan_start_date = IqpAssetRel.loan_start_date._getValue();
		var loan_end_date = IqpAssetRel.loan_end_date._getValue();
		var prdId = IqpAssetRel.prd_id._getValue();  //业务品种
		if(loan_start_date==''||loan_end_date==''){
			alert('请先录入起始日期和到期日期！');
			return;
		}
		if(prdId==''){
			alert('请先录入业务品种！');
			return;
		}
		
		var li_qs = getQs(loan_start_date,loan_end_date);
		getBaseRate(li_qs);
	};
	/** 根据给定的两个日期，求出它们之间的期数 **/
	function getQs(rqFrom,rqTo){
		var yearF,yearT,monF,monT,dayF,dayT,Qs,Qs1,Qs2,Qs3;
		yaarF = rqFrom.substring(0,4);
		yearT = rqTo.substring(0,4);
		monF = rqFrom.substring(5,7);
		monT = rqTo.substring(5,7);
		dayF = rqFrom.substring(8,10);
		dayT = rqTo.substring(8,10);
		Qs1 = (parseInt(yearT)-parseInt(yaarF))*12;
		Qs2 = parseInt(parseFloat(monT))-parseInt(parseFloat(monF));
		if(parseInt(parseFloat(dayT)) > parseInt(parseFloat(dayF)))
			Qs3 = 1;
		else
			Qs3 = 0;
		Qs = parseInt(Qs1)+parseInt(Qs2)+parseInt(Qs3);
		return Qs;
	};
	//取基准利率
	function getBaseRate(term,prdId){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var rate = jsonstr.rate;
				if(rate != ""){
					IqpAssetRel.ruling_ir._setValue(rate*12/10);
					ruling_mounth._setValue(rate/10);
					setRulingMounth();
				}else {

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
		
		var currType = IqpAssetRel.cur_type._getValue();  //币种
		var termType = "002"; //默认期限类型为 月
		var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
		var url = '<emp:url action="getRate.do"/>'+param;
		var postData = YAHOO.util.Connect.setForm();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};
	function selectIrType(){
	    var cur_type = IqpAssetRel.cur_type._getValue();
	    var options = IqpAssetRel.ir_type._obj.element.options;
	    var ir_type = IqpAssetRel.ir_type._getValue();
	    for(var i=options.length-1;i>0;i--){
				options.remove(i); 
		}
	    if(cur_type == "CNY"){
	        var option1 = new Option("短期贷款6个月","13");
	        var option2 = new Option("短期贷款6-12个月","14");
	        options.add(option1);
	        options.add(option2);
	        if(ir_type != ""){
	        	IqpAssetRel.ir_type._setValue(ir_type);
	        }
	    }
	};
	function ir_typeChange(){
		var ir_accord_type = IqpAssetRel.ir_accord_type._getValue();
		if(ir_accord_type=="03" || ir_accord_type=="01" || ir_accord_type=="04" || ir_accord_type==""){
	       return;
	    }
		var llyjfs = IqpAssetRel.ir_accord_type._getValue();//利率依据方式
	    var rateType = IqpAssetRel.ir_type._getValue();//利率种类
	    var bz = IqpAssetRel.cur_type._getValue();//币种
		var loan_start_date = IqpAssetRel.loan_start_date._getValue();	//起始日期
		var loan_end_date = IqpAssetRel.loan_end_date._getValue();	//到期日期
		var prdId = IqpAssetRel.prd_id._getValue();  //业务品种
		
		if(loan_start_date==''||loan_end_date==''){
			alert('请先录入起始日期和到期日期！');
			return;
		}
		if(prdId==''){
			alert('请先录入业务品种！');
			return;
		}
	    if(bz=="" || bz == null){
	         alert("请先选择【申请币种】！");
	         IqpAssetRel.apply_cur_type._obj.element.focus();
	         return;
	    }
	    if(llyjfs=="" || llyjfs == null){
	        alert("请先选择【利率依据方式】！");
	        IqpAssetRel.ir_accord_type._obj.element.focus();
	        return;
	   }
	    if(rateType == null || rateType == ""){
			alert("请先选择【利率种类】");
			return;
	    }
	    
		var li_qs = getQs(loan_start_date,loan_end_date);
		if(li_qs > 12){
			alert('短期贷款期限超出12个月');
			return;
		}
	    
	    if(rateType == "13" ){
	        var term = "6"
	        getBaseRate(term,prdId);
	    }else if(rateType == "14"){
	    	var term = "12"
	    	getBaseRate(term,prdId);
	    }
	};

	function getAcctOrgID(data){
		IqpAssetRel.fina_br_id._setValue(data.organno._getValue());
		IqpAssetRel.fina_br_id_displayname._setValue(data.organname._getValue());
	};
	function getOrgID(data){
		IqpAssetRel.manager_br_id._setValue(data.organno._getValue());
		IqpAssetRel.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	//01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
	function caculateOverdueRate(){
		var ir_accord_type = IqpAssetRel.ir_accord_type._getValue();
		if(ir_accord_type == "01"){
			var reality_ir_y = IqpAssetRel.reality_ir_y._obj.element.value;//执行利率（年）
			if(parseFloat(reality_ir_y)>=0){
				var overdue_rate_y = IqpAssetRel.overdue_rate_y._obj.element.value;//逾期利率（年）
				var overdue_rate = parseFloat(overdue_rate_y)/parseFloat(reality_ir_y)-1;
				IqpAssetRel.overdue_rate._setValue(overdue_rate+"");
				IqpAssetRel.overdue_float_type._setValue("0");//加百分比
	    	}else{
	        	alert("请先输入执行利率!");
	        	IqpAssetRel.overdue_rate_y._setValue("");
	        }
	    }
	};
	//01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
	function caculateDefaultRate(){
		var ir_accord_type = IqpAssetRel.ir_accord_type._getValue();
		if(ir_accord_type == "01"){
			var reality_ir_y = IqpAssetRel.reality_ir_y._obj.element.value;//执行利率（年）
			if(parseFloat(reality_ir_y)>=0){
				var default_rate_y = IqpAssetRel.default_rate_y._obj.element.value;//违约利率（年）
				var default_rate = parseFloat(default_rate_y)/parseFloat(reality_ir_y)-1;
				IqpAssetRel.default_rate._setValue(default_rate+"");
				IqpAssetRel.default_float_type._setValue("0");//加百分比
	    	}else{
	        	alert("请先输入执行利率!");
	        	IqpAssetRel.overdue_rate_y._setValue("");
	        }
	    }
	};

	function ifRrAccordType(){
    	var ir_accord_type = IqpAssetRel.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		IqpAssetRel.overdue_rate._obj._renderHidden(true);
    		IqpAssetRel.overdue_rate._obj._renderRequired(false);

    		IqpAssetRel.default_rate._obj._renderHidden(true);
    		IqpAssetRel.default_rate._obj._renderRequired(false);
    	}
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpAssetRelRecord.do" method="POST">
		 <emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:gridLayout id="IqpAssetRelGroup" title="资产信息" maxColumn="2">
			<emp:text id="IqpAssetRel.bill_no" label="借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpAssetRel.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:pop id="IqpAssetRel.cus_id" label="客户码" url="queryAllCusPop.do?returnMethod=returnCus" required="true" buttonLabel="选择"  />
			<emp:text id="IqpAssetRel.cus_name" label="客户名称" maxlength="100" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:pop id="IqpAssetRel.prd_id" label="产品编号" url="" returnMethod="returnPrdId" required="true" buttonLabel="选择" />
			<emp:text id="IqpAssetRel.prd_name" label="产品名称" maxlength="80" readonly="true" required="false" />
			<emp:select id="IqpAssetRel.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" />
			<emp:text id="IqpAssetRel.loan_amt" label="贷款金额" maxlength="18" required="true" dataType="Currency" onchange="checkBal()"/>
			<emp:text id="IqpAssetRel.loan_bal" label="贷款余额" maxlength="18" required="true" dataType="Currency" onchange="checkBal()"/>
			<emp:date id="IqpAssetRel.loan_start_date" label="起始日期" required="true" onblur="checkDate()"/>
			<emp:date id="IqpAssetRel.loan_end_date" label="到期日期" required="true" onblur="checkDate()"/>
			<emp:pop id="IqpAssetRel.repay_type_displayname" label="还款方式" url="queryPrdRepayModeList.do?prd_id=600020&returnMethod=getRepayType" buttonLabel="选择" required="false" />
			<emp:select id="IqpAssetRel.ei_type" label="计息周期" required="true"  dictname="STD_IQP_RATE_CYCLE" onblur="setRepayTerm();"/>
			<emp:select id="IqpAssetRel.repay_term" label="还款间隔周期" required="true" dictname="STD_BACK_CYCLE" readonly="true"/>
			<emp:text id="IqpAssetRel.repay_space" label="还款间隔" required="true" dataType="Int"/>
			<emp:date id="IqpAssetRel.latest_repay" label="最近还款日" required="true" colSpan="2" onblur="caculate()"/>
			<emp:select id="IqpAssetRel.ir_accord_type" label="利率依据方式"  onchange="ir_accord_typeChange('change');" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="IqpAssetRel.ir_type" label="利率种类" hidden="true" required="false" dictname="STD_ZB_RATE_TYPE" onchange="ir_typeChange();" />
			<emp:text id="IqpAssetRel.ruling_ir" label="基准利率（年）" hidden="true" maxlength="16" required="false" dataType="Rate" onchange="setRulingMounth()"/>
			<emp:text id="ruling_mounth" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>  
			<emp:text id="IqpAssetRel.pad_rate_y" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2" readonly="false" required="false" dataType="Rate"/>
			<emp:select id="IqpAssetRel.ir_adjust_type" label="利率调整方式" hidden="true" readonly="true" required="false" colSpan="1" dictname="STD_IR_ADJUST_TYPE" />
			<emp:select id="IqpAssetRel.ir_next_adjust_term" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
			<emp:select id="IqpAssetRel.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
			<emp:date id="IqpAssetRel.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			<emp:select id="IqpAssetRel.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
			<emp:text id="IqpAssetRel.ir_float_rate" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getRelYM();" required="false" dataType="Rate" />
			<emp:text id="IqpAssetRel.ir_float_point" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />
			<emp:text id="IqpAssetRel.reality_ir_y" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange()" readonly="true" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="IqpAssetRel.overdue_float_type" label="逾期利率浮动方式" hidden="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAssetRel.overdue_rate" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Rate" />
			<emp:text id="IqpAssetRel.overdue_point" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" />
			<emp:text id="IqpAssetRel.overdue_rate_y" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate" onblur="caculateOverdueRate()"/>
			<emp:select id="IqpAssetRel.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAssetRel.default_rate" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Rate" />
			<emp:text id="IqpAssetRel.default_point" label="违约利率浮动点数" maxlength="38" hidden="true" onchange="getDefaultRateY();" required="false" />
			<emp:text id="IqpAssetRel.default_rate_y" label="违约利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate" colSpan="2" onblur="caculateDefaultRate()"/>
			
			<emp:select id="IqpAssetRel.five_class" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpAssetRel.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" colSpan="2"/>
			<emp:textarea id="IqpAssetRel.guar_desc" label="担保说明" required="false" colSpan="2"/>	
			<emp:pop id="IqpAssetRel.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpAssetRel.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetRel.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetRel.fina_br_id" label="账务机构" hidden="true" />
			
			<emp:text id="IqpAssetRel.asset_no" label="资产包编号" maxlength="40" required="false" defvalue="${context.asset_no}" hidden="true"/>  
		    <emp:text id="IqpAssetRel.repay_type" label="还款方式" required="false"  colSpan="2" hidden="true"/>
		    <emp:text id="IqpAssetRel.repay_mode_type" label="还款方式类型" required="false"  colSpan="2" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="IqpAssetRelGroup" title="转让信息" maxColumn="2">	
			<emp:text id="IqpAssetRel.takeover_amt" label="转让本金" maxlength="18" required="true" dataType="Currency" onchange="countAfee();caculate()"/>
			<emp:text id="IqpAssetRel.takeover_frate" label="转让费率" maxlength="10" onfocus="_doKeypressDown()" required="true" dataType="Rate" onchange="countAfee('rate')" />
			<emp:select id="IqpAssetRel.afee_type" label="安排费计费方式" required="true" dictname="STD_ZB_PREPARGET_MODE" onchange="countAfee()"/>
			<emp:select id="IqpAssetRel.afee_pay_type" label="安排费支付方式" required="true" dictname="STD_ZB_PREPARPAY_MODE"/> 
			<emp:text id="IqpAssetRel.afee" label="安排费" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpAssetRel.mfee" label="管理费" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpAssetRel.int" label="应计利息" maxlength="18" required="true" dataType="Currency"/>
			<%if("01".equals(takeover_type)||"02".equals(takeover_type)){ %>
			<emp:text id="IqpAssetRel.agent_asset_acct" label="代理资产资金账号" maxlength="40" required="false" />
			<%}else{ %>
			<emp:text id="IqpAssetRel.agent_asset_acct" label="结算账号" maxlength="40" required="false" />
			<%} %>
			<emp:text id="IqpAssetRel.takeover_rate" label="转让利率" maxlength="10" required="true" dataType="Rate" />
			<emp:text id="IqpAssetRel.takeover_int" label="转让利息" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpAssetRel.pk_id" label="主键" hidden="true" />
		</emp:gridLayout>
		
		</emp:tabGroup>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确认" />    
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
