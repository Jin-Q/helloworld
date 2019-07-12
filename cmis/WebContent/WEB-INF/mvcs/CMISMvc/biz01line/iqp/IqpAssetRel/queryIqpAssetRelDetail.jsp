<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String takeover_type=""; 
	if(context.containsKey("takeover_type")){
		takeover_type = (String)context.getDataValue("takeover_type");
	}
%>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function onload(){
    	ir_accord_typeChange("init");
    	
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
		IqpAssetRel.ir_adjust_type._obj._renderReadonly(true);

		ifRrAccordType();
	}
	function doReturn() {
		var asset_no = IqpAssetRel.asset_no._getValue();
		var url = '<emp:url action="queryIqpAssetRelList.do"/>?asset_no='+asset_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
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
			//IqpAssetRel.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
			//IqpAssetRel.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
			//IqpAssetRel.default_rate_y._obj._renderReadonly(false);//违约利率（年）
			
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
		var rulY = IqpAssetRel.ruling_ir._getValue();
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

	function getAcctOrgID(data){
		IqpAssetRel.fina_br_id._setValue(data.organno._getValue());
		IqpAssetRel.fina_br_id_displayname._setValue(data.organname._getValue());
	};
	function getOrgID(data){
		IqpAssetRel.manager_br_id._setValue(data.organno._getValue());
		IqpAssetRel.manager_br_id_displayname._setValue(data.organname._getValue());
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
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:gridLayout id="IqpAssetRelGroup" title="资产信息" maxColumn="2">
			<emp:text id="IqpAssetRel.bill_no" label="借据编号" maxlength="40" required="true"/>  
			<emp:text id="IqpAssetRel.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:pop id="IqpAssetRel.cus_id" label="客户码" url="queryAllCusPop.do?returnMethod=returnCus" required="true" buttonLabel="选择"  />
			<emp:text id="IqpAssetRel.cus_name" label="客户名称" maxlength="100" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:pop id="IqpAssetRel.prd_id" label="产品编号" url="" returnMethod="returnPrdId" required="true" buttonLabel="选择" />
			<emp:text id="IqpAssetRel.prd_name" label="产品名称" maxlength="80" readonly="true" required="false" />
			<emp:select id="IqpAssetRel.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" readonly="true" defvalue="CNY"/>
			<emp:text id="IqpAssetRel.loan_amt" label="贷款金额" maxlength="18" required="true" dataType="Currency" onchange="checkBal()"/>
			<emp:text id="IqpAssetRel.loan_bal" label="贷款余额" maxlength="18" required="true" dataType="Currency" onchange="checkBal()"/>
			<emp:date id="IqpAssetRel.loan_start_date" label="起始日期" required="true" />
			<emp:date id="IqpAssetRel.loan_end_date" label="到期日期" required="true" />
			<emp:text id="IqpAssetRel.repay_type_displayname" label="还款方式"  required="true"/>
			<emp:select id="IqpAssetRel.ei_type" label="计息周期" required="true"  dictname="STD_IQP_RATE_CYCLE" />
			<emp:select id="IqpAssetRel.repay_term" label="还款间隔周期" required="false" dictname="STD_BACK_CYCLE" readonly="true"/>
			<emp:text id="IqpAssetRel.repay_space" label="还款间隔" required="false" dataType="Int"/>
			<emp:date id="IqpAssetRel.latest_repay" label="最近还款日" required="true"  colSpan="2"/>
			<emp:select id="IqpAssetRel.ir_accord_type" label="利率依据方式"   required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="IqpAssetRel.ir_type" label="利率种类" hidden="true" required="false" dictname="STD_ZB_RATE_TYPE" />
			<emp:text id="IqpAssetRel.ruling_ir" label="基准利率（年）" hidden="true" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="ruling_mounth" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>  
			<emp:text id="IqpAssetRel.pad_rate_y" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2"  required="false" dataType="Rate"/>
			<emp:select id="IqpAssetRel.ir_adjust_type" label="利率调整方式" hidden="true" readonly="true"  required="false" colSpan="1"  dictname="STD_IR_ADJUST_TYPE" />
			<emp:select id="IqpAssetRel.ir_next_adjust_term" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
			<emp:select id="IqpAssetRel.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
			<emp:date id="IqpAssetRel.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			<emp:select id="IqpAssetRel.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
			<emp:text id="IqpAssetRel.ir_float_rate" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getRelYM();" required="false" dataType="Percent2" />
			<emp:text id="IqpAssetRel.ir_float_point" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />
			<emp:text id="IqpAssetRel.reality_ir_y" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange()" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="IqpAssetRel.overdue_float_type" label="逾期利率浮动方式" hidden="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAssetRel.overdue_rate" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Percent2" />
			<emp:text id="IqpAssetRel.overdue_point" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" dataType="Int" />
			<emp:text id="IqpAssetRel.overdue_rate_y" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" required="false" dataType="Rate"/>
			<emp:select id="IqpAssetRel.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAssetRel.default_rate" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Percent2" />
			<emp:text id="IqpAssetRel.default_point" label="违约利率浮动点数" maxlength="38" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Int" />
			<emp:text id="IqpAssetRel.default_rate_y" label="违约利率（年）" hidden="true" maxlength="16" required="false" dataType="Rate" colSpan="2"/>
			
			<emp:select id="IqpAssetRel.five_class" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpAssetRel.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" colSpan="2"/>
			<emp:textarea id="IqpAssetRel.guar_desc" label="担保说明" required="false" colSpan="2"/>	
			<emp:pop id="IqpAssetRel.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpAssetRel.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true"/>
		    <emp:text id="IqpAssetRel.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetRel.fina_br_id" label="账务机构" hidden="true" />
			
			<emp:text id="IqpAssetRel.asset_no" label="资产包编号" maxlength="40" required="false" defvalue="${context.asset_no}" hidden="true"/>  
		    <emp:text id="IqpAssetRel.repay_type" label="还款方式" required="false"  colSpan="2" hidden="true"/>
		    <emp:text id="IqpAssetRel.repay_mode_type" label="还款方式类型" required="false"  colSpan="2" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="IqpAssetRelGroup" title="转让信息" maxColumn="2">
			<emp:text id="IqpAssetRel.takeover_amt" label="转让本金" maxlength="18" required="true" dataType="Currency" onchange="checkTakeoverAmt()"/>
			<emp:text id="IqpAssetRel.takeover_frate" label="转让费率" maxlength="10" required="true" dataType="Rate" />
			<emp:select id="IqpAssetRel.afee_type" label="安排费计费方式" required="true" dictname="STD_ZB_PREPARGET_MODE"/>
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
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
