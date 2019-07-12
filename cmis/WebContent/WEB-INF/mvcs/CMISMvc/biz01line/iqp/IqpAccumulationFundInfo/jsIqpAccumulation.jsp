<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------普通贷款业务申请调用JS----------------------------

function selectIrType(){
    var cur_type = IqpAccumulationFundInfo.apply_cur_type._getValue();
    var options = IqpAccumulationFundInfo.ir_type._obj.element.options;
    var ir_type = IqpAccumulationFundInfo.ir_type._getValue();
    for(var i=options.length-1;i>=0;i--){
			 options.remove(i); 
	}
    if(cur_type == "CNY"){
        var option1 = new Option("短期贷款6个月","13");
        var option2 = new Option("短期贷款6-12个月","14");
        options.add(option1);
        options.add(option2);
        if(ir_type != ""){
        	IqpAccumulationFundInfo.ir_type._setValue(ir_type);
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
        	IqpAccumulationFundInfo.ir_type._setValue(ir_type);
        }
    }
};

function _doKeypressDown() {
	try{
		if(IqpAccumulationFundInfo.security_rate._obj.element.focus){
			IqpAccumulationFundInfo.security_rate._obj.element.select();
	    }
	}catch(e){
		alert(e);
	}
}

//-------------------根据年利率同比换算月利率-----------------------
function getRulMounth(){
	var rulY = IqpAccumulationFundInfo.ruling_ir._getValue();
	if(rulY != null && rulY != ""){
		ruling_mounth._setValue(parseFloat(rulY)/1200);
		getRelYM();
	}
	var llyjfs = IqpAccumulationFundInfo.ir_accord_type._getValue();//利率依据方式
	if(llyjfs == "01"){//议价利率
        IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);
        IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);
    }
};

//-------------------加点、加百分比实时调整执行年、月利率-----------------------
function getRelYM(){
	var rulY = IqpAccumulationFundInfo.ruling_ir._obj.element.value;
	rulY = parseFloat(rulY)/100;
	var ir_accord_type  = IqpAccumulationFundInfo.ir_accord_type._getValue();//利率依据方式 
	if(rulY == null || rulY == ""){
		rulY = 0;
	}
	var rulM = ruling_mounth._getValue();
	var fRate = IqpAccumulationFundInfo.ir_float_rate._obj.element.value;
	var fPoint = IqpAccumulationFundInfo.ir_float_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpAccumulationFundInfo.ir_float_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*rulY;
		var relM = parseFloat(relY)/12;
		IqpAccumulationFundInfo.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpAccumulationFundInfo.ir_float_point._setValue("");
	        IqpAccumulationFundInfo.reality_ir_y._setValue(rulY);
			reality_mounth._setValue(parseFloat(rulY)/12);
	        return;
	    }
		IqpAccumulationFundInfo.ir_float_rate._setValue("");
		var relY = (parseFloat(rulY)*10000+parseFloat(fPoint))/10000;
		var relM = Math.round(relY*10000)/120000;
		IqpAccumulationFundInfo.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else {
		IqpAccumulationFundInfo.ir_float_rate._setValue("");
		IqpAccumulationFundInfo.ir_float_point._setValue("");
		//只有利率依据方式为牌告利率的时候,执行年利率为基准年利率
		if(ir_accord_type == "02"){
			IqpAccumulationFundInfo.reality_ir_y._setValue(parseFloat(rulY));
			reality_mounth._setValue(parseFloat(rulY)/12);
		}
	}
	if(ir_accord_type == "02" || ir_accord_type == "04"){
		getOverdueRateY();//更新逾期利率 
	    getDefaultRateY();//更新违约利率
	}
};
//-------------------根据贷款利率浮动方式同比调整显示-----------------------
function changeIrFloatType(){
	var floatType = IqpAccumulationFundInfo.ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		/** 显示控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
		
	}else if(floatType=='1'){//加点
		/** 显示控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.ir_float_rate._setValue("");//贷款利率浮动比
	}else {
		/** 显示控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.ir_float_rate._setValue("");//贷款利率浮动比
		IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
	}
};
//-------------------根据逾期利率浮动方式同比调整显示-----------------------
function changeOverdueFloatType(){
	var overdueFloatType = IqpAccumulationFundInfo.overdue_float_type._getValue();
	if(overdueFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
	}else if(overdueFloatType=='1'){//加点
		/** 显示控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
	}else {
		/** 显示控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
	}
};
//-------------------根据违约利率浮动方式同比调整显示-----------------------
function changeDefaultFloatType(){
	var defaultFloatType = IqpAccumulationFundInfo.default_float_type._getValue();
	if(defaultFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
	}else if(defaultFloatType=='1'){//加点
		/** 显示控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(false);//违约利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(true);//违约利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
	}else {
		/** 显示控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
		IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
	}
};
//----------------更新逾期利率-----------------
function getOverdueRateY(){
	var overdueY = IqpAccumulationFundInfo.reality_ir_y._getValue();
	if(overdueY == null || overdueY == ""){
		overdueY = 0;
	}
	var fRate = IqpAccumulationFundInfo.overdue_rate._obj.element.value;
	var fPoint = IqpAccumulationFundInfo.overdue_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpAccumulationFundInfo.overdue_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*overdueY; 
		IqpAccumulationFundInfo.overdue_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpAccumulationFundInfo.overdue_point._setValue("");
	        IqpAccumulationFundInfo.overdue_rate_y._setValue("");
	        return;
	    }
		IqpAccumulationFundInfo.overdue_rate._setValue("");
		var relY = (parseFloat(overdueY)*10000+parseFloat(fPoint))/10000;
		IqpAccumulationFundInfo.overdue_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpAccumulationFundInfo.overdue_rate._setValue("");
		IqpAccumulationFundInfo.overdue_point._setValue("");
		IqpAccumulationFundInfo.overdue_rate_y._setValue("");
	}
};

//---------------更新违约利率------------------
function getDefaultRateY(){
	var defaultY = IqpAccumulationFundInfo.reality_ir_y._getValue();
	if(defaultY == null || defaultY == ""){
		defaultY = 0;
	}
	var fRate = IqpAccumulationFundInfo.default_rate._obj.element.value;
	var fPoint = IqpAccumulationFundInfo.default_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpAccumulationFundInfo.default_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*defaultY; 
		IqpAccumulationFundInfo.default_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpAccumulationFundInfo.default_point._setValue("");
	        IqpAccumulationFundInfo.default_rate_y._setValue("");
	        return;
	    }
		IqpAccumulationFundInfo.default_rate._setValue("");
		var relY = (parseFloat(defaultY)*10000+parseFloat(fPoint))/10000;
		IqpAccumulationFundInfo.default_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpAccumulationFundInfo.default_rate._setValue("");
		IqpAccumulationFundInfo.default_point._setValue("");
		IqpAccumulationFundInfo.default_rate_y._setValue("");
	}
};
function showDifRateType(){
	  var type = IqpAccumulationFundInfo.ir_float_type._getValue();
	  if(type==0){
		  IqpAccumulationFundInfo.ir_float_point._setValue("");
		  IqpAccumulationFundInfo.overdue_point._setValue("");
		  IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);
		  IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);
		  IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(false);
		  IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(false);
	  }else if(type==1){
		  IqpAccumulationFundInfo.ir_float_rate._setValue("");
		  IqpAccumulationFundInfo.overdue_rate._setValue("");
		  IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);
		  IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);
		  IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(false);
		  IqpAccumulationFundInfo.overdue_point._obj._renderHidden(false);
	  }else if(type==2){

		  IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(false);
		  IqpAccumulationFundInfo.overdue_point._obj._renderHidden(false);
		  IqpAccumulationFundInfo.overdue_rate._setValue("");
		  IqpAccumulationFundInfo.ir_float_point._setValue("");
		  IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);
		  IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);
	  }
	  
  };

  function setRulingMounth(){
	  var year = IqpAccumulationFundInfo.ruling_ir._getValue();
	  ruling_mounth._setValue(year/12); 
  }; 

  function setreality_ir_yMounth(){
      var reality_ir_y = IqpAccumulationFundInfo.reality_ir_y._getValue();
      reality_mounth._setValue(reality_ir_y/12);
  };

	 

		
//-------------------利率依据方式下拉框响应方法-----------------------
function ir_accord_typeChange(data){
	var llyjfs = IqpAccumulationFundInfo.ir_accord_type._getValue();//利率依据方式
	var llfdfs = IqpAccumulationFundInfo.ir_float_type._getValue();//利率浮动方式
	if(llyjfs == "01"){//议价利率
		/** 显示控制 */
    	IqpAccumulationFundInfo.ir_type._obj._renderHidden(true);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpAccumulationFundInfo.ir_type._setValue("");//利率种类
			IqpAccumulationFundInfo.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpAccumulationFundInfo.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpAccumulationFundInfo.default_rate_y._setValue(""); //违约利率（年）
			IqpAccumulationFundInfo.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpAccumulationFundInfo.ir_adjust_type._setValue("0");//利率调整方式
			IqpAccumulationFundInfo.ir_float_type._setValue("");//利率浮动方式
			IqpAccumulationFundInfo.ir_float_rate._setValue("");//利率浮动比
			IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
			IqpAccumulationFundInfo.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
			IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
			IqpAccumulationFundInfo.default_float_type._setValue("");//违约利率浮动方式
			IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
			IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_type._obj._renderRequired(false);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpAccumulationFundInfo.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderReadonly(false);//违约利率（年）
		
    }else if(llyjfs == "02"){//牌告利率依据
		 /** 显示控制 */
    	IqpAccumulationFundInfo.ir_type._obj._renderHidden(false);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpAccumulationFundInfo.ir_type._setValue("");//利率种类
			IqpAccumulationFundInfo.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpAccumulationFundInfo.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpAccumulationFundInfo.default_rate_y._setValue(""); //违约利率（年）
			IqpAccumulationFundInfo.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpAccumulationFundInfo.ir_adjust_type._setValue("");//利率调整方式
			IqpAccumulationFundInfo.ir_float_type._setValue("");//利率浮动方式
			IqpAccumulationFundInfo.ir_float_rate._setValue("");//利率浮动比
			IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
			IqpAccumulationFundInfo.overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
			IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
			IqpAccumulationFundInfo.default_float_type._setValue("0");//违约利率浮动方式
			IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
			IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_type._obj._renderRequired(true);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpAccumulationFundInfo.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
		//getRate(); 
		changeOverdueFloatType();
		changeDefaultFloatType();
    }else if(llyjfs == "03"){//不计息
        /** 显示控制 */
    	IqpAccumulationFundInfo.ir_type._obj._renderHidden(true);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 赋值控制 */
		if(data != "init"){
			IqpAccumulationFundInfo.ir_type._setValue("");//利率种类
			IqpAccumulationFundInfo.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpAccumulationFundInfo.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpAccumulationFundInfo.default_rate_y._setValue(""); //违约利率（年）
			IqpAccumulationFundInfo.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpAccumulationFundInfo.ir_adjust_type._setValue("");//利率调整方式
			IqpAccumulationFundInfo.ir_float_type._setValue("");//利率浮动方式
			IqpAccumulationFundInfo.ir_float_rate._setValue("");//利率浮动比
			IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
			IqpAccumulationFundInfo.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
			IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
			IqpAccumulationFundInfo.default_float_type._setValue("");//违约利率浮动方式
			IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
			IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_type._obj._renderRequired(false);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpAccumulationFundInfo.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "04"){//正常利率
		 /** 显示控制 */
    	IqpAccumulationFundInfo.ir_type._obj._renderHidden(true);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		if(data != "init"){
			IqpAccumulationFundInfo.ir_type._setValue("");//利率种类
			IqpAccumulationFundInfo.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpAccumulationFundInfo.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpAccumulationFundInfo.default_rate_y._setValue(""); //违约利率（年）
			IqpAccumulationFundInfo.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpAccumulationFundInfo.ir_adjust_type._setValue("");//利率调整方式
			IqpAccumulationFundInfo.ir_float_type._setValue("");//利率浮动方式
			IqpAccumulationFundInfo.ir_float_rate._setValue("");//利率浮动比
			IqpAccumulationFundInfo.ir_float_point._setValue("");//贷款利率浮动点数
			IqpAccumulationFundInfo.overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpAccumulationFundInfo.overdue_rate._setValue("");//逾期利率浮动比
			IqpAccumulationFundInfo.overdue_point._setValue("");//逾期利率浮动点数
			IqpAccumulationFundInfo.default_float_type._setValue("0");//违约利率浮动方式
			IqpAccumulationFundInfo.default_rate._setValue("");//违约利率浮动比
			IqpAccumulationFundInfo.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpAccumulationFundInfo.ir_type._obj._renderRequired(false);//利率种类
		IqpAccumulationFundInfo.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpAccumulationFundInfo.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpAccumulationFundInfo.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(true); //执行利率（月）
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpAccumulationFundInfo.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpAccumulationFundInfo.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpAccumulationFundInfo.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpAccumulationFundInfo.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpAccumulationFundInfo.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpAccumulationFundInfo.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpAccumulationFundInfo.default_point._obj._renderRequired(false);//违约利率浮动点数

		/** 只读控制 */
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpAccumulationFundInfo.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

		changeOverdueFloatType();
		changeDefaultFloatType();
		/** 获取基准利率 */
    }
};

//-------------------年利率计算月利率-----------------------
function reality_ir_yChange(){
	//var reality_ir_y_Value = IqpAccumulationFundInfo.reality_ir_y._getValue();
	var reality_ir_y_Value = IqpAccumulationFundInfo.reality_ir_y._obj.element.value;
	var yll = parseFloat(reality_ir_y_Value)/1200;
	reality_mounth._setValue(yll);

	caculateOverdueRate();
	caculateDefaultRate();
};
//-------------------通过基准利率（年）获得执行利率（年）-----------------------
function getReality_ir_y(){
	alert("23e");
	var ir_y = IqpAccumulationFundInfo.ruling_ir._getValue();
	IqpAccumulationFundInfo.reality_ir_y._setValue(ir_y);
	reality_mounth._setValue(parseFloat(ir_y)/12);	
}; 

</script>