<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
function selAccInfo(data){
	var bill_no = data.bill_no._getValue();
	var cus_id = data.cus_id._getValue();
	var cus_id_displayname = data.cus_id_displayname._getValue();
	var cur_type = data.cur_type._getValue();
	var loan_amt = data.loan_amt._getValue();
	var loan_balance = data.loan_balance._getValue();
	var distr_date = data.distr_date._getValue();
	var end_date = data.end_date._getValue();

	var ir_accord_type = data.ir_accord_type._getValue();
	var ir_type = data.ir_type._getValue();
	var ruling_ir = data.ruling_ir._getValue();
	var pad_rate_y = data.pad_rate_y._getValue();
	var ir_adjust_type = data.ir_adjust_type._getValue();
	var ir_float_type = data.ir_float_type._getValue();
	var ir_float_rate = data.ir_float_rate._getValue();
	var ir_float_point = data.ir_float_point._getValue();
	var reality_ir_y = data.reality_ir_y._getValue();
	var overdue_float_type = data.overdue_float_type._getValue();
	var overdue_rate = data.overdue_rate._getValue();
	var overdue_point = data.overdue_point._getValue();
	var overdue_rate_y = data.overdue_rate_y._getValue();
	var default_float_type = data.default_float_type._getValue();
	var default_rate = data.default_rate._getValue();
	var default_point = data.default_point._getValue();
	var default_rate_y = data.default_rate_y._getValue();
	var ruling_ir_code = data.ruling_ir_code._getValue();
	
	var prd_id = data.prd_id._getValue();
	var term_type = data.term_type._getValue();
	var cont_term = data.cont_term._getValue();
	var manager_br_id = data.manager_br_id._getValue();
	var manager_br_id_displayname = data.manager_br_id_displayname._getValue();

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
				IqpRateChangeApp.bill_no._setValue(bill_no);
				IqpRateChangeApp.cus_id._setValue(cus_id);
				IqpRateChangeApp.cus_id_displayname._setValue(cus_id_displayname);
				IqpRateChangeApp.cur_type._setValue(cur_type);
				IqpRateChangeApp.loan_amt._setValue(loan_amt);
				IqpRateChangeApp.loan_balance._setValue(loan_balance);
				IqpRateChangeApp.distr_date._setValue(distr_date);
				IqpRateChangeApp.end_date._setValue(end_date);
                //原利率信息赋值
				IqpRateChangeApp.ir_accord_type._setValue(ir_accord_type);
				IqpRateChangeApp.ir_type._setValue(ir_type);
				IqpRateChangeApp.ruling_ir._setValue(ruling_ir);
				IqpRateChangeApp.pad_rate_y._setValue(pad_rate_y);
				IqpRateChangeApp.ir_adjust_type._setValue(ir_adjust_type);
				IqpRateChangeApp.ir_float_type._setValue(ir_float_type);
				IqpRateChangeApp.ir_float_rate._setValue(ir_float_rate);
				IqpRateChangeApp.ir_float_point._setValue(ir_float_point);
				IqpRateChangeApp.reality_ir_y._setValue(reality_ir_y);
				IqpRateChangeApp.overdue_float_type._setValue(overdue_float_type);
				IqpRateChangeApp.overdue_rate._setValue(overdue_rate);
				IqpRateChangeApp.overdue_point._setValue(overdue_point);
				IqpRateChangeApp.overdue_rate_y._setValue(overdue_rate_y);
				IqpRateChangeApp.default_float_type._setValue(default_float_type);
				IqpRateChangeApp.default_rate._setValue(default_rate);
				IqpRateChangeApp.default_point._setValue(default_point);
				IqpRateChangeApp.default_rate_y._setValue(default_rate_y);
				IqpRateChangeApp.ruling_ir_code._setValue(ruling_ir_code);
				//赋值给调整利率信息赋值
				IqpRateChangeApp.new_ir_accord_type._setValue(ir_accord_type);
				IqpRateChangeApp.new_ir_type._setValue(ir_type);
				IqpRateChangeApp.new_ruling_ir._setValue(ruling_ir);
				IqpRateChangeApp.new_pad_rate_y._setValue(pad_rate_y);
				IqpRateChangeApp.new_ir_adjust_type._setValue(ir_adjust_type);
				IqpRateChangeApp.new_ir_float_type._setValue(ir_float_type);
				IqpRateChangeApp.new_ir_float_rate._setValue(ir_float_rate);
				IqpRateChangeApp.new_ir_float_point._setValue(ir_float_point);
				IqpRateChangeApp.new_reality_ir_y._setValue(reality_ir_y);
				IqpRateChangeApp.new_overdue_float_type._setValue(overdue_float_type);
				IqpRateChangeApp.new_overdue_rate._setValue(overdue_rate);
				IqpRateChangeApp.new_overdue_point._setValue(overdue_point);
				IqpRateChangeApp.new_overdue_rate_y._setValue(overdue_rate_y);
				IqpRateChangeApp.new_default_float_type._setValue(default_float_type);
				IqpRateChangeApp.new_default_rate._setValue(default_rate);
				IqpRateChangeApp.new_default_point._setValue(default_point);
				IqpRateChangeApp.new_default_rate_y._setValue(default_rate_y);
				IqpRateChangeApp.new_ruling_ir_code._setValue(ruling_ir_code);
				
				IqpRateChangeApp.manager_br_id._setValue(manager_br_id);
				IqpRateChangeApp.manager_br_id_displayname._setValue(manager_br_id_displayname);

                //非利率调整中的字段，计算用
                IqpRateChangeApp.prd_id._setValue(prd_id);
                IqpRateChangeApp.term_type._setValue(term_type);
                IqpRateChangeApp.cont_term._setValue(cont_term);
				
				ir_accord_typeChange("init");
				getRulMounth();
				reality_ir_yChange();
				selectIrType();
				changeIrFloatTypeForOld();
				changeOverdueFloatTypeForOld();
				changeDefaultFloatTypeForOld();
				new_ir_accord_typeChange("init");
				new_reality_ir_yChange();
				newGetRulMounth();
				changeIrFloatType();
				ifRrAccordType();
				ifRrAccordType4New();

				//利率调整不可修改，利率调整方式  add by zhaozq 20150127 start
				IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(true);//利率调整方式
				//利率调整不可修改，利率调整方式  add by zhaozq 20150127 end
			}else if(flag == "have"){
				ir_accord_type = jsonstr.IqpRateChangeApp.new_ir_accord_type;
			    ir_type = jsonstr.IqpRateChangeApp.new_ir_type;
			    ruling_ir = jsonstr.IqpRateChangeApp.new_ruling_ir;
			    pad_rate_y = jsonstr.IqpRateChangeApp.new_pad_rate_y;
			    ir_adjust_type = jsonstr.IqpRateChangeApp.new_ir_adjust_type;
			    ir_float_type = jsonstr.IqpRateChangeApp.new_ir_float_type;
			    ir_float_rate = jsonstr.IqpRateChangeApp.new_ir_float_rate;
			    ir_float_point = jsonstr.IqpRateChangeApp.new_ir_float_point;
			    reality_ir_y = jsonstr.IqpRateChangeApp.new_reality_ir_y;
			    overdue_float_type = jsonstr.IqpRateChangeApp.new_overdue_float_type;
			    overdue_rate = jsonstr.IqpRateChangeApp.new_overdue_rate;
			    overdue_point = jsonstr.IqpRateChangeApp.new_overdue_point;
			    overdue_rate_y = jsonstr.IqpRateChangeApp.new_overdue_rate_y;
			    default_float_type = jsonstr.IqpRateChangeApp.new_default_float_type;
			    default_rate = jsonstr.IqpRateChangeApp.new_default_rate;
			    default_point = jsonstr.IqpRateChangeApp.new_default_point;
			    default_rate_y = jsonstr.IqpRateChangeApp.new_default_rate_y;
				ruling_ir_code = jsonstr.IqpRateChangeApp.new_ruling_ir_code;
				
				IqpRateChangeApp.bill_no._setValue(bill_no);
				IqpRateChangeApp.cus_id._setValue(cus_id);
				IqpRateChangeApp.cus_id_displayname._setValue(cus_id_displayname);
				IqpRateChangeApp.cur_type._setValue(cur_type);
				IqpRateChangeApp.loan_amt._setValue(loan_amt);
				IqpRateChangeApp.loan_balance._setValue(loan_balance);
				IqpRateChangeApp.distr_date._setValue(distr_date);
				IqpRateChangeApp.end_date._setValue(end_date);
                //原利率信息赋值
				IqpRateChangeApp.ir_accord_type._setValue(ir_accord_type);
				IqpRateChangeApp.ir_type._setValue(ir_type);
				IqpRateChangeApp.ruling_ir._setValue(ruling_ir);
				IqpRateChangeApp.pad_rate_y._setValue(pad_rate_y);
				IqpRateChangeApp.ir_adjust_type._setValue(ir_adjust_type);
				IqpRateChangeApp.ir_float_type._setValue(ir_float_type);
				IqpRateChangeApp.ir_float_rate._setValue(ir_float_rate);
				IqpRateChangeApp.ir_float_point._setValue(ir_float_point);
				IqpRateChangeApp.reality_ir_y._setValue(reality_ir_y);
				IqpRateChangeApp.overdue_float_type._setValue(overdue_float_type);
				IqpRateChangeApp.overdue_rate._setValue(overdue_rate);
				IqpRateChangeApp.overdue_point._setValue(overdue_point);
				IqpRateChangeApp.overdue_rate_y._setValue(overdue_rate_y);
				IqpRateChangeApp.default_float_type._setValue(default_float_type);
				IqpRateChangeApp.default_rate._setValue(default_rate);
				IqpRateChangeApp.default_point._setValue(default_point);
				IqpRateChangeApp.default_rate_y._setValue(default_rate_y);
				IqpRateChangeApp.ruling_ir_code._setValue(ruling_ir_code);
				//赋值给调整利率信息赋值
				IqpRateChangeApp.new_ir_accord_type._setValue(ir_accord_type);
				IqpRateChangeApp.new_ir_type._setValue(ir_type);
				IqpRateChangeApp.new_ruling_ir._setValue(ruling_ir);
				IqpRateChangeApp.new_pad_rate_y._setValue(pad_rate_y);
				IqpRateChangeApp.new_ir_adjust_type._setValue(ir_adjust_type);
				IqpRateChangeApp.new_ir_float_type._setValue(ir_float_type);
				IqpRateChangeApp.new_ir_float_rate._setValue(ir_float_rate);
				IqpRateChangeApp.new_ir_float_point._setValue(ir_float_point);
				IqpRateChangeApp.new_reality_ir_y._setValue(reality_ir_y);
				IqpRateChangeApp.new_overdue_float_type._setValue(overdue_float_type);
				IqpRateChangeApp.new_overdue_rate._setValue(overdue_rate);
				IqpRateChangeApp.new_overdue_point._setValue(overdue_point);
				IqpRateChangeApp.new_overdue_rate_y._setValue(overdue_rate_y);
				IqpRateChangeApp.new_default_float_type._setValue(default_float_type);
				IqpRateChangeApp.new_default_rate._setValue(default_rate);
				IqpRateChangeApp.new_default_point._setValue(default_point);
				IqpRateChangeApp.new_default_rate_y._setValue(default_rate_y);
				IqpRateChangeApp.new_ruling_ir_code._setValue(ruling_ir_code);
				
				IqpRateChangeApp.manager_br_id._setValue(manager_br_id);
				IqpRateChangeApp.manager_br_id_displayname._setValue(manager_br_id_displayname);

                //非利率调整中的字段，计算用
                IqpRateChangeApp.prd_id._setValue(prd_id);
                IqpRateChangeApp.term_type._setValue(term_type);
                IqpRateChangeApp.cont_term._setValue(cont_term);
				
				ir_accord_typeChange("init");
				getRulMounth();
				reality_ir_yChange();
				selectIrType();
				changeIrFloatTypeForOld();
				changeOverdueFloatTypeForOld();
				changeDefaultFloatTypeForOld();
				new_ir_accord_typeChange("init");
				new_reality_ir_yChange();
				newGetRulMounth();
				changeIrFloatType();
				ifRrAccordType();
				ifRrAccordType4New();
			}else {
				//XD150520037_信贷系统利率调整修改优化 start
				alert("该借据已存在在途利率调整申请！");
				//XD150520037_信贷系统利率调整修改优化 end
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
	var url = '<emp:url action="checkIqpRateChangeHaveBill.do"/>?bill_no='+bill_no;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
};

function ir_accord_typeChange(data){  
	var llyjfs = IqpRateChangeApp.ir_accord_type._getValue();//利率依据方式
	var llfdfs = IqpRateChangeApp.ir_float_type._getValue();//利率浮动方式
	if(llyjfs == "01"){//议价利率
		/** 显示控制 */
    	IqpRateChangeApp.ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.ir_type._setValue("");//利率种类
			IqpRateChangeApp.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.ir_adjust_type._setValue("0");//利率调整方式
			IqpRateChangeApp.ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 只读控制 */
		IqpRateChangeApp.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		//IqpRateChangeApp.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
		//IqpRateChangeApp.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
		//IqpRateChangeApp.default_rate_y._obj._renderReadonly(false);//违约利率（年）
		
		
		/** 获取基准利率 */
		//getRate();
    }else if(llyjfs == "02"){//牌告利率依据
		 /** 显示控制 */
    	IqpRateChangeApp.ir_type._obj._renderHidden(false);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.ir_type._setValue("");//利率种类
			IqpRateChangeApp.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.ir_type._obj._renderRequired(true);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 只读控制 */
		IqpRateChangeApp.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpRateChangeApp.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "03"){//不计息
        /** 显示控制 */
    	IqpRateChangeApp.ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.ir_type._setValue("");//利率种类
			IqpRateChangeApp.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpRateChangeApp.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "04"){//正常利率
		 /** 显示控制 */
    	IqpRateChangeApp.ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.ir_type._setValue("");//利率种类
			IqpRateChangeApp.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(true); //执行利率（月）
		IqpRateChangeApp.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpRateChangeApp.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpRateChangeApp.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

		/** 获取基准利率 */
		//getRate();
    }
};
//-------------------根据年利率同比换算月利率-----------------------
function getRulMounth(){
	var rulY = IqpRateChangeApp.ruling_ir._getValue();
	if(rulY != null && rulY != ""){
		ruling_mounth._setValue(parseFloat(rulY)/12);
		//getRelYM();
		
	}
	var llyjfs = IqpRateChangeApp.ir_accord_type._getValue();//利率依据方式
	if(llyjfs == "01"){//议价利率
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);
    }
};
//-------------------调整后根据年利率同比换算月利率-----------------------
function newGetRulMounth(){
	var rulY = IqpRateChangeApp.new_ruling_ir._getValue();
	if(rulY != null && rulY != ""){
		new_ruling_mounth._setValue(parseFloat(rulY)/12);
		//getRelYM();
		
	}
	var llyjfs = IqpRateChangeApp.new_ir_accord_type._getValue();//利率依据方式
	if(llyjfs == "01"){//议价利率
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);
    }
};

//-------------------年利率计算月利率-----------------------
function reality_ir_yChange(){
	var reality_ir_y_Value = IqpRateChangeApp.reality_ir_y._obj.element.value;
	var yll = parseFloat(reality_ir_y_Value)/1200;
	reality_mounth._setValue(yll);
};

//-------------------调整后年利率计算月利率-----------------------
function new_reality_ir_yChange(){
	var reality_ir_y_Value = IqpRateChangeApp.new_reality_ir_y._obj.element.value;
	var yll = parseFloat(reality_ir_y_Value)/1200;
	new_reality_mounth._setValue(yll);
};

//-------------------利率依据方式下拉框响应方法-----------------------
function new_ir_accord_typeChange(data){
	var llyjfs = IqpRateChangeApp.new_ir_accord_type._getValue();//利率依据方式
	var llfdfs = IqpRateChangeApp.new_ir_float_type._getValue();//利率浮动方式
	if(llyjfs == "01"){//议价利率
		/** 显示控制 */
    	IqpRateChangeApp.new_ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderHidden(true); //基准利率（年）
		new_ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderHidden(false); //执行利率（年）
		new_reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.new_ir_type._setValue("");//利率种类
			IqpRateChangeApp.new_ruling_ir._setValue(""); //基准利率（年）
			new_ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.new_overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.new_default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.new_pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.new_reality_ir_y._setValue(""); //执行利率（年）
			new_reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.new_ir_adjust_type._setValue("0");//利率调整方式
			IqpRateChangeApp.new_ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.new_ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.new_overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.new_default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.new_ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderRequired(false); //基准利率（年）
		new_ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderRequired(true); //执行利率（年）
		new_reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpRateChangeApp.new_reality_ir_y._obj._renderReadonly(false);//执行利率（年）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderReadonly(false);//违约利率（年）
		
		/** 获取基准利率 */
		//getRate();
    }else if(llyjfs == "02"){//牌告利率依据
		 /** 显示控制 */
    	IqpRateChangeApp.new_ir_type._obj._renderHidden(false);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderHidden(false); //基准利率（年）
		new_ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderHidden(false); //执行利率（年）
		new_reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.new_ir_type._setValue("");//利率种类
			IqpRateChangeApp.new_ruling_ir._setValue(""); //基准利率（年）
			new_ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.new_overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.new_default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.new_pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.new_reality_ir_y._setValue(""); //执行利率（年）
			new_reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.new_ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.new_ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.new_ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.new_overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.new_default_float_type._setValue("0");//违约利率浮动方式
			IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.new_ir_type._obj._renderRequired(true);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderRequired(true); //基准利率（年）
		new_ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderRequired(true); //执行利率（年）
		new_reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpRateChangeApp.new_reality_ir_y._obj._renderReadonly(true);//执行利率（年）
		//getRate(); 
		changeOverdueFloatType();
		changeDefaultFloatType();
    }else if(llyjfs == "03"){//不计息
        /** 显示控制 */
    	IqpRateChangeApp.new_ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderHidden(true); //基准利率（年）
		new_ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderHidden(true); //执行利率（年）
		new_reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.new_ir_type._setValue("");//利率种类
			IqpRateChangeApp.new_ruling_ir._setValue(""); //基准利率（年）
			new_ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.new_overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.new_default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.new_pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.new_reality_ir_y._setValue(""); //执行利率（年）
			new_reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.new_ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.new_ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.new_ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.new_overdue_float_type._setValue("");//逾期利率浮动方式
			IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.new_default_float_type._setValue("");//违约利率浮动方式
			IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.new_ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderRequired(false); //基准利率（年）
		new_ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderRequired(false); //执行利率（年）
		new_reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpRateChangeApp.new_reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "04"){//正常利率
		 /** 显示控制 */
    	IqpRateChangeApp.new_ir_type._obj._renderHidden(true);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderHidden(false); //基准利率（年）
		new_ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderHidden(false); //执行利率（年）
		new_reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpRateChangeApp.new_ir_type._setValue("");//利率种类
			IqpRateChangeApp.new_ruling_ir._setValue(""); //基准利率（年）
			new_ruling_mounth._setValue(""); //对应基准利率（月）
			IqpRateChangeApp.new_overdue_rate_y._setValue(""); //逾期利率（年）
			IqpRateChangeApp.new_default_rate_y._setValue(""); //违约利率（年）
			IqpRateChangeApp.new_pad_rate_y._setValue("");//垫款利率（年）
			IqpRateChangeApp.new_reality_ir_y._setValue(""); //执行利率（年）
			new_reality_mounth._setValue(""); //执行利率（月）
			IqpRateChangeApp.new_ir_adjust_type._setValue("");//利率调整方式
			IqpRateChangeApp.new_ir_float_type._setValue("");//利率浮动方式
			IqpRateChangeApp.new_ir_float_rate._setValue("");//利率浮动比
			IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
			IqpRateChangeApp.new_overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
			IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
			IqpRateChangeApp.new_default_float_type._setValue("0");//违约利率浮动方式
			IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
			IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpRateChangeApp.new_ir_type._obj._renderRequired(false);//利率种类
		IqpRateChangeApp.new_ruling_ir._obj._renderRequired(true); //基准利率（年）
		new_ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpRateChangeApp.new_overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpRateChangeApp.new_default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpRateChangeApp.new_pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpRateChangeApp.new_reality_ir_y._obj._renderRequired(true); //执行利率（年）
		new_reality_mounth._obj._renderRequired(true); //执行利率（月）
		IqpRateChangeApp.new_ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpRateChangeApp.new_ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpRateChangeApp.new_overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpRateChangeApp.new_default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpRateChangeApp.new_reality_ir_y._obj._renderReadonly(true);//执行利率（年）

		changeOverdueFloatType();
		changeDefaultFloatType();
		
		/** 获取基准利率 */
		getRateforIrAccordChange(); 
		
    }
	if(llyjfs!="04"){//当[利率依据方式]为"正常利率上浮动"时，才需要根据当前基准利率表中获取[基准利率代码]行自动赋值。
		IqpRateChangeApp.new_ruling_ir_code._setValue("");
	}
};


function getRateforIrAccordChange(){
 	var currType = IqpRateChangeApp.cur_type._getValue();//币种
	var prdId = IqpRateChangeApp.prd_id._getValue();//业务品种
	var termType = IqpRateChangeApp.term_type._getValue();//期限类型
	var term = IqpRateChangeApp.cont_term._getValue();//期限
	var llyjfs = IqpRateChangeApp.new_ir_accord_type._getValue();//利率依据方式
	if(currType == null || currType == ""){
		alert("请选择币种！");
		return;
	}
	if(termType == null || termType == ""){
		alert("请选择期限类型！");
		return;
	}
	if(term == null || term == ""){
		alert("请录入期限！");
		return;
	}
	if(term == "0"){
		alert("期限不能为0");
		return;
	}
	if(llyjfs == "04"){ 
		var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
		if(prdId != null && prdId != ""){
			var url = '<emp:url action="getRate.do"/>'+param;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					var rate = jsonstr.rate;
					var code = jsonstr.code;
					if(flag == "success"){
						if(llyjfs=="04"){//当[利率依据方式]为"正常利率上浮动"时，才需要根据当前基准利率表中获取[基准利率代码]行自动赋值。
							IqpRateChangeApp.new_ruling_ir_code._setValue(code);
						}else{
							IqpRateChangeApp.new_ruling_ir_code._setValue("");
						}
						if(IqpRateChangeApp.new_ruling_ir._getValue()==null || IqpRateChangeApp.new_ruling_ir._getValue() == ""){
							IqpRateChangeApp.new_ruling_ir._setValue(rate);
							new_ruling_mounth._setValue(Math.round(rate*1000000/12)/1000000);
						}
						getRelYM();
					}else {
						alert(msg);
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	}
};

//-------------------加点、加百分比实时调整执行年、月利率-----------------------
function getRelYM(){
	var rulY = IqpRateChangeApp.new_ruling_ir._getValue();
	var ir_accord_type  = IqpRateChangeApp.new_ir_accord_type._getValue();//利率依据方式 
	if(rulY == null || rulY == ""){
		rulY = 0;
	}
	var rulM = new_ruling_mounth._getValue();
	var fRate = IqpRateChangeApp.new_ir_float_rate._obj.element.value;
	var fPoint = IqpRateChangeApp.new_ir_float_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpRateChangeApp.new_ir_float_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*rulY;
		var relM = parseFloat(relY)/12;
		IqpRateChangeApp.new_reality_ir_y._setValue(relY);
		new_reality_mounth._setValue(relM);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpRateChangeApp.new_ir_float_point._setValue("");
	        IqpRateChangeApp.new_reality_ir_y._setValue(rulY);
			new_reality_mounth._setValue(parseFloat(rulY)/12);
	        return;
	    }
		IqpRateChangeApp.new_ir_float_rate._setValue("");
		var relY = (parseFloat(rulY)*10000+parseFloat(fPoint))/10000;
		var relM = Math.round(relY*10000)/120000;
		IqpRateChangeApp.new_reality_ir_y._setValue(relY);
		new_reality_mounth._setValue(relM);
	}else {
		IqpRateChangeApp.new_ir_float_rate._setValue("");
		IqpRateChangeApp.new_ir_float_point._setValue("");
		//只有利率依据方式为牌告利率的时候,执行年利率为基准年利率
		if(ir_accord_type == "02"){
			IqpRateChangeApp.new_reality_ir_y._setValue(rulY);
			new_reality_mounth._setValue(parseFloat(rulY)/12);
		}
	}
	if(ir_accord_type == "02" || ir_accord_type == "04"){
		getOverdueRateY();//更新逾期利率 
		getDefaultRateY();//更新违约利率
	}
};

//-------------------根据贷款利率浮动方式同比调整显示-----------------------
function changeIrFloatTypeForOld(){
	var floatType = IqpRateChangeApp.ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
		
	}else if(floatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.ir_float_rate._setValue("");//贷款利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.ir_float_rate._setValue("");//贷款利率浮动比
		IqpRateChangeApp.ir_float_point._setValue("");//贷款利率浮动点数
	}
};
//-------------------根据逾期利率浮动方式同比调整显示-----------------------
function changeOverdueFloatTypeForOld(){
	var overdueFloatType = IqpRateChangeApp.overdue_float_type._getValue();
	if(overdueFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
	}else if(overdueFloatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.overdue_rate._setValue("");//逾期利率浮动比
		IqpRateChangeApp.overdue_point._setValue("");//逾期利率浮动点数
	}
};

//-------------------根据违约利率浮动方式同比调整显示-----------------------
function changeDefaultFloatTypeForOld(){
	var defaultFloatType = IqpRateChangeApp.default_float_type._getValue();
	if(defaultFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.default_rate._obj._renderHidden(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.default_rate._obj._renderRequired(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
	}else if(defaultFloatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(false);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(true);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.default_rate._setValue("");//违约利率浮动比
		IqpRateChangeApp.default_point._setValue("");//违约利率浮动点数
	}
};
//-------------------根据贷款利率浮动方式同比调整显示-----------------------
function changeIrFloatType(){
	var floatType = IqpRateChangeApp.new_ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
		
	}else if(floatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_ir_float_rate._setValue("");//贷款利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_ir_float_rate._setValue("");//贷款利率浮动比
		IqpRateChangeApp.new_ir_float_point._setValue("");//贷款利率浮动点数
	}
};

//-------------------根据逾期利率浮动方式同比调整显示-----------------------
function changeOverdueFloatType(){
	var overdueFloatType = IqpRateChangeApp.new_overdue_float_type._getValue();
	if(overdueFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
	}else if(overdueFloatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(false);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(true);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_overdue_rate._setValue("");//逾期利率浮动比
		IqpRateChangeApp.new_overdue_point._setValue("");//逾期利率浮动点数
	}
};

//-------------------根据违约利率浮动方式同比调整显示-----------------------
function changeDefaultFloatType(){
	var defaultFloatType = IqpRateChangeApp.new_default_float_type._getValue();
	if(defaultFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpRateChangeApp.new_default_rate._obj._renderHidden(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_default_rate._obj._renderRequired(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
	}else if(defaultFloatType=='1'){//加点
		/** 显示控制 */
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(false);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(true);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
	}else {
		/** 显示控制 */
		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpRateChangeApp.new_default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpRateChangeApp.new_default_rate._setValue("");//违约利率浮动比
		IqpRateChangeApp.new_default_point._setValue("");//违约利率浮动点数
	}
};

//----------------更新逾期利率-----------------
function getOverdueRateY(){
	var overdueY = IqpRateChangeApp.new_reality_ir_y._getValue();
	if(overdueY == null || overdueY == ""){
		overdueY = 0;
	}
	var fRate = IqpRateChangeApp.new_overdue_rate._obj.element.value;
	var fPoint = IqpRateChangeApp.new_overdue_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpRateChangeApp.new_overdue_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*overdueY; 
		IqpRateChangeApp.new_overdue_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpRateChangeApp.new_overdue_point._setValue("");
	        IqpRateChangeApp.new_overdue_rate_y._setValue("");
	        return;
	    }
		IqpRateChangeApp.new_overdue_rate._setValue("");
		var relY = (parseFloat(overdueY)*10000+parseFloat(fPoint))/10000;
		IqpRateChangeApp.new_overdue_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpRateChangeApp.new_overdue_rate._setValue("");
		IqpRateChangeApp.new_overdue_point._setValue("");
		IqpRateChangeApp.new_overdue_rate_y._setValue("");
	}
};

//---------------更新违约利率------------------
function getDefaultRateY(){
	var defaultY = IqpRateChangeApp.new_reality_ir_y._getValue();
	if(defaultY == null || defaultY == ""){
		defaultY = 0;
	}
	var fRate = IqpRateChangeApp.new_default_rate._obj.element.value;
	var fPoint = IqpRateChangeApp.new_default_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpRateChangeApp.new_default_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*defaultY; 
		IqpRateChangeApp.new_default_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpRateChangeApp.new_default_point._setValue("");
	        IqpRateChangeApp.new_default_rate_y._setValue("");
	        return;
	    }
		IqpRateChangeApp.new_default_rate._setValue("");
		var relY = (parseFloat(defaultY)*10000+parseFloat(fPoint))/10000;
		IqpRateChangeApp.new_default_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpRateChangeApp.new_default_rate._setValue("");
		IqpRateChangeApp.new_default_point._setValue("");
		IqpRateChangeApp.new_default_rate_y._setValue("");
	}
};

//-------------------利率种类下拉框相应方法-----------------------
function ir_typeChange(){
	var ir_accord_type = IqpRateChangeApp.new_ir_accord_type._getValue();
	if(ir_accord_type=="03" || ir_accord_type=="01" || ir_accord_type=="04" || ir_accord_type==""){
       return;
    }
	var llyjfs = IqpRateChangeApp.new_ir_accord_type._getValue();//利率依据方式
    var rateType = IqpRateChangeApp.new_ir_type._getValue();//利率种类
    var bz = IqpRateChangeApp.cur_type._getValue();//币种
    if(bz=="" || bz == null){
         alert("请先选择【申请币种】！");
         return;
    }
    if(llyjfs=="" || llyjfs == null){
        alert("请先选择【利率依据方式】！");
        return;
   }
    if(rateType == null || rateType == ""){
		alert("请先选择【利率种类】");
		return;
    }
    getLiborRate(bz,rateType);
};

//-------------------通过libor利率类型获取牌告基准利率(curr 币种\irType 利率种类)-----------------------
function getLiborRate(curr,irType){
	if(curr != null && curr != "" && irType != null && irType != ""){
		var url = '<emp:url action="getLiborRate.do"/>&curr='+curr+'&irType='+irType;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var rateValue = jsonstr.rateValue;
				if(flag == "success"){
					IqpRateChangeApp.new_ruling_ir._setValue(rateValue);
					new_ruling_mounth._setValue(parseFloat(rateValue)/12);
					getReality_ir_y();
					getRelYM();
					getOverdueRateY();
					getDefaultRateY();
				}else {
					alert(msg);
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
function selectIrType(){
    var cur_type = IqpRateChangeApp.cur_type._getValue();
    var options = IqpRateChangeApp.new_ir_type._obj.element.options;
    var ir_type = IqpRateChangeApp.new_ir_type._getValue();
    for(var i=options.length-1;i>=0;i--){
			 options.remove(i); 
	}
    if(cur_type == "CNY"){
        var option1 = new Option("短期贷款6个月","13");
        var option2 = new Option("短期贷款6-12个月","14");
        options.add(option1);
        options.add(option2);
        if(ir_type != ""){
        	IqpRateChangeApp.new_ir_type._setValue(ir_type);
        }
    }else{
    	var option1 = new Option("外汇一个月LIBOR","01");
    	var option2 = new Option("外汇二个月LIBOR","02");
    	var option3 = new Option("外汇三个月LIBOR","03");
    	var option4 = new Option("外汇四个月LIBOR","04");
    	var option5 = new Option("外汇五个月LIBOR","05");
    	var option6 = new Option("外汇六个月LIBOR","06");
    	var option7 = new Option("外汇七个月LIBOR","07");
    	var option8 = new Option("外汇八个月LIBOR","08");
    	var option9 = new Option("外汇九个月LIBOR","09");
    	var option10 = new Option("外汇十个月LIBOR","10");
    	var option11 = new Option("外汇十一个月LIBOR","11");
    	var option12 = new Option("外汇一年LIBOR","12");
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        options.add(option5);
        options.add(option6);
        options.add(option7);
        options.add(option8);
        options.add(option9);
        options.add(option10);
        options.add(option11);
        options.add(option12);
        if(ir_type != ""){
        	IqpRateChangeApp.new_ir_type._setValue(ir_type);
        }
    }
};
//-------------------通过基准利率（年）获得基准利率（月）-----------------------
function getReality_ir_y(){
	var ir_y = IqpRateChangeApp.new_ruling_ir._getValue();
	IqpRateChangeApp.new_reality_ir_y._setValue(ir_y);
	new_reality_mounth._setValue(parseFloat(ir_y)/12);	
}; 
function checkDate(){
	var new_inure_date = IqpRateChangeApp.new_inure_date._getValue();
	var openDay = '${context.OPENDAY}';
	if(new_inure_date!='' && new_inure_date < openDay) {
		alert("调整后利率生效日期不能早于当前营业时间，请调整！");
		IqpRateChangeApp.new_inure_date._setValue('');
		return false;
	}
}; 

//反向计算利率浮动比
//01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
function caculateOverdueRate(){
	var ir_accord_type = IqpRateChangeApp.new_ir_accord_type._getValue();
	if(ir_accord_type == "01"){
		var reality_ir_y = IqpRateChangeApp.new_reality_ir_y._obj.element.value;//执行利率（年）
		if(parseFloat(reality_ir_y)>=0){
			var overdue_rate_y = IqpRateChangeApp.new_overdue_rate_y._obj.element.value;//逾期利率（年）
			var overdue_rate = parseFloat(overdue_rate_y)/parseFloat(reality_ir_y)-1;
			IqpRateChangeApp.new_overdue_rate._setValue(overdue_rate+"");
			IqpRateChangeApp.new_overdue_float_type._setValue("0");//加百分比
    	}else{
        	alert("请先输入执行利率!");
    		IqpRateChangeApp.new_overdue_rate_y._setValue("");
        }
    }
};
//反向计算利率浮动比
function caculateDefaultRate(){
	var ir_accord_type = IqpRateChangeApp.new_ir_accord_type._getValue();
	if(ir_accord_type == "01"){
		var reality_ir_y = IqpRateChangeApp.new_reality_ir_y._obj.element.value;//执行利率（年）
		if(parseFloat(reality_ir_y)>=0){
			var default_rate_y = IqpRateChangeApp.new_default_rate_y._obj.element.value;//违约利率（年）
			var default_rate = parseFloat(default_rate_y)/parseFloat(reality_ir_y)-1;
			IqpRateChangeApp.new_default_rate._setValue(default_rate+"");
			IqpRateChangeApp.new_default_float_type._setValue("0");//加百分比
    	}else{
        	alert("请先输入执行利率!");
        	IqpRateChangeApp.new_default_rate_y._setValue("");
        }
    }
};
//反向计算利率浮动比 并隐藏
function ifRrAccordType(){
	var ir_accord_type = IqpRateChangeApp.ir_accord_type._getValue();
	if(ir_accord_type == "01"){
		IqpRateChangeApp.overdue_rate._obj._renderHidden(true);
		IqpRateChangeApp.overdue_rate._obj._renderRequired(false);

		IqpRateChangeApp.default_rate._obj._renderHidden(true);
		IqpRateChangeApp.default_rate._obj._renderRequired(false);
	}
};
//反向计算利率浮动比 并隐藏
function ifRrAccordType4New(){
	var ir_accord_type = IqpRateChangeApp.new_ir_accord_type._getValue();
	if(ir_accord_type == "01"){
		IqpRateChangeApp.new_overdue_rate._obj._renderHidden(true);
		IqpRateChangeApp.new_overdue_rate._obj._renderRequired(false);

		IqpRateChangeApp.new_default_rate._obj._renderHidden(true);
		IqpRateChangeApp.new_default_rate._obj._renderRequired(false);
	}
};
</script>
