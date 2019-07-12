<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isHaveButton = "";
	if(context.containsKey("isHaveButton")){
		isHaveButton = (String)context.getDataValue("isHaveButton");
	} 
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%> 
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function onload(){
		AccAssetstrsf.bill_no._obj.addOneButton("bill_no","查看",getBill);
		AccAssetstrsf.ori_bill_no._obj.addOneButton("bill_no","查看",getOriBill);
		AccAssetstrsf.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
    	ir_accord_typeChange("init");
	
		if(AccAssetstrsf.ir_accord_type._getValue()=='02'||AccAssetstrsf.ir_accord_type._getValue()=='04'){
			setRulingMounth();
		}else{
			getRealityMounth();
		}
		changeOverdueFloatType();
		changeDefaultFloatType();
		changeIrFloatType();
		ifRrAccordType();
	};
	function getBill(){
		var bill_no = AccAssetstrsf.bill_no._getValue();
		var url = '<emp:url action="getAccLoanViewPage.do"/>?bill_no='+bill_no+"&isHaveButton=not";
		
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	function getOriBill(){
		var bill_no = AccAssetstrsf.ori_bill_no._getValue();
		var url = '<emp:url action="getAccLoanViewPage.do"/>?bill_no='+bill_no+"&isHaveButton=not";
		
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	function getCusForm(){
		var cus_id = AccAssetstrsf.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function doReturn() {
		var url = '<emp:url action="queryAccAssetstrsfList.do"/>?menuId=${context.menuId}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//-------------------利率依据方式下拉框响应方法-----------------------
	function ir_accord_typeChange(data){
		var llyjfs = AccAssetstrsf.ir_accord_type._getValue();//利率依据方式
		var llfdfs = AccAssetstrsf.ir_float_type._getValue();//利率浮动方式
		if(llyjfs == "01"){//议价利率
			/** 显示控制 */
	    	AccAssetstrsf.ir_type._obj._renderHidden(true);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderHidden(true); //基准利率（年）
			ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderHidden(false); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderHidden(true);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderHidden(true);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderHidden(true);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				AccAssetstrsf.ir_type._setValue("");//利率种类
				AccAssetstrsf.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				AccAssetstrsf.overdue_rate_y._setValue(""); //逾期利率（年）
				AccAssetstrsf.default_rate_y._setValue(""); //违约利率（年）
				AccAssetstrsf.pad_rate_y._setValue("");//垫款利率（年）
				AccAssetstrsf.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				AccAssetstrsf.ir_adjust_type._setValue("0");//利率调整方式
				AccAssetstrsf.ir_float_type._setValue("");//利率浮动方式
				AccAssetstrsf.ir_float_rate._setValue("");//利率浮动比
				AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
				AccAssetstrsf.overdue_float_type._setValue("");//逾期利率浮动方式
				AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
				AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
				AccAssetstrsf.default_float_type._setValue("");//违约利率浮动方式
				AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
				AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			AccAssetstrsf.ir_type._obj._renderRequired(false);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderRequired(false); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderRequired(true); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderRequired(false);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderRequired(false);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderRequired(false);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			AccAssetstrsf.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
			//AccAssetstrsf.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
			//AccAssetstrsf.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
			//AccAssetstrsf.default_rate_y._obj._renderReadonly(false);//违约利率（年）
			
			/** 获取基准利率 */
			//getRate();
	    }else if(llyjfs == "02"){//牌告利率依据
			 /** 显示控制 */
	    	AccAssetstrsf.ir_type._obj._renderHidden(false);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderHidden(false); //基准利率（年）
			ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderHidden(false); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderHidden(false);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderHidden(false);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderHidden(false);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				AccAssetstrsf.ir_type._setValue("");//利率种类
				AccAssetstrsf.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				AccAssetstrsf.overdue_rate_y._setValue(""); //逾期利率（年）
				AccAssetstrsf.default_rate_y._setValue(""); //违约利率（年）
				AccAssetstrsf.pad_rate_y._setValue("");//垫款利率（年）
				AccAssetstrsf.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				AccAssetstrsf.ir_adjust_type._setValue("");//利率调整方式
				AccAssetstrsf.ir_float_type._setValue("");//利率浮动方式
				AccAssetstrsf.ir_float_rate._setValue("");//利率浮动比
				AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
				AccAssetstrsf.overdue_float_type._setValue("");//逾期利率浮动方式
				AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
				AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
				AccAssetstrsf.default_float_type._setValue("");//违约利率浮动方式
				AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
				AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			AccAssetstrsf.ir_type._obj._renderRequired(true);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderRequired(true); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderRequired(true); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderRequired(true);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderRequired(true);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderRequired(true);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			AccAssetstrsf.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
			AccAssetstrsf.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
	    }else if(llyjfs == "03"){//不计息
	        /** 显示控制 */
	    	AccAssetstrsf.ir_type._obj._renderHidden(true);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderHidden(true); //基准利率（年）
			ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderHidden(true); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderHidden(true); //执行利率（年）
			reality_mounth._obj._renderHidden(true); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderHidden(true);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderHidden(true);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderHidden(true);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				AccAssetstrsf.ir_type._setValue("");//利率种类
				AccAssetstrsf.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				AccAssetstrsf.overdue_rate_y._setValue(""); //逾期利率（年）
				AccAssetstrsf.default_rate_y._setValue(""); //违约利率（年）
				AccAssetstrsf.pad_rate_y._setValue("");//垫款利率（年）
				AccAssetstrsf.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				AccAssetstrsf.ir_adjust_type._setValue("");//利率调整方式
				AccAssetstrsf.ir_float_type._setValue("");//利率浮动方式
				AccAssetstrsf.ir_float_rate._setValue("");//利率浮动比
				AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
				AccAssetstrsf.overdue_float_type._setValue("");//逾期利率浮动方式
				AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
				AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
				AccAssetstrsf.default_float_type._setValue("");//违约利率浮动方式
				AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
				AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			AccAssetstrsf.ir_type._obj._renderRequired(false);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderRequired(false); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderRequired(false); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderRequired(false); //执行利率（年）
			reality_mounth._obj._renderRequired(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderRequired(false);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderRequired(false);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderRequired(false);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			AccAssetstrsf.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
			AccAssetstrsf.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
	    }else if(llyjfs == "04"){//正常利率
			 /** 显示控制 */
	    	AccAssetstrsf.ir_type._obj._renderHidden(true);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderHidden(false); //基准利率（年）
			ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderHidden(false); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderHidden(false); //执行利率（年）
			reality_mounth._obj._renderHidden(false); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderHidden(false);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderHidden(false);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderHidden(false);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数

			/** 赋值控制 */
			if(data != "init"){
				AccAssetstrsf.ir_type._setValue("");//利率种类
				AccAssetstrsf.ruling_ir._setValue(""); //基准利率（年）
				ruling_mounth._setValue(""); //对应基准利率（月）
				AccAssetstrsf.overdue_rate_y._setValue(""); //逾期利率（年）
				AccAssetstrsf.default_rate_y._setValue(""); //违约利率（年）
				AccAssetstrsf.pad_rate_y._setValue("");//垫款利率（年）
				AccAssetstrsf.reality_ir_y._setValue(""); //执行利率（年）
				reality_mounth._setValue(""); //执行利率（月）
				AccAssetstrsf.ir_adjust_type._setValue("");//利率调整方式
				AccAssetstrsf.ir_float_type._setValue("");//利率浮动方式
				AccAssetstrsf.ir_float_rate._setValue("");//利率浮动比
				AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
				AccAssetstrsf.overdue_float_type._setValue("");//逾期利率浮动方式
				AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
				AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
				AccAssetstrsf.default_float_type._setValue("");//违约利率浮动方式
				AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
				AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
			}
			/** 必输控制 */
			AccAssetstrsf.ir_type._obj._renderRequired(false);//利率种类
			AccAssetstrsf.ruling_ir._obj._renderRequired(true); //基准利率（年）
			ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
			AccAssetstrsf.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
			AccAssetstrsf.default_rate_y._obj._renderRequired(true); //违约利率（年）
			AccAssetstrsf.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
			AccAssetstrsf.reality_ir_y._obj._renderRequired(true); //执行利率（年）
			reality_mounth._obj._renderRequired(true); //执行利率（月）
			AccAssetstrsf.ir_adjust_type._obj._renderRequired(true);//利率调整方式
			AccAssetstrsf.ir_float_type._obj._renderRequired(true);//利率浮动方式
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			AccAssetstrsf.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			AccAssetstrsf.default_float_type._obj._renderRequired(true);//违约利率浮动方式
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数

			/** 只读控制 */
			AccAssetstrsf.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
			AccAssetstrsf.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

			/** 获取基准利率 */
			//getRate();
	    }
	};

	function setRulingMounth(){
		var rateValue = AccAssetstrsf.ruling_ir._obj.element.value;
		//alert(rateValue);
		rateValue = parseFloat(rateValue)/100;
		//alert(rateValue);
		ruling_mounth._setValue(rateValue/12);
		getReality_ir_y();
	}

	//-------------------通过基准利率（年）获得执行利率（年）-----------------------
	function getReality_ir_y(){
		var ir_y = AccAssetstrsf.reality_ir_y._getValue();
		reality_mounth._setValue(parseFloat(ir_y)/12);	
	};

	//-------------------通过基准利率（年）获得执行利率（年）-----------------------
	function getRealityMounth(){
		var ir_y = AccAssetstrsf.reality_ir_y._getValue();
		reality_mounth._setValue(parseFloat(ir_y)/12);	
	};

	//-----------------------利率调整方式js控制------------------------------------
	function ir_adjust_type_change(){
		var irAdjType = AccAssetstrsf.ir_adjust_type._getValue();
		if(irAdjType == 0){
			/** 显示控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderHidden(true);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderHidden(true);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderHidden(true);//第一次调整日
			/** 必输控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderRequired(false);//第一次调整日
			/** 值域控制 */
			AccAssetstrsf.ir_next_adjust_term._setValue("");//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._setValue("");//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._setValue("");//第一次调整日
		}else if(irAdjType == "FIX"){
			/** 显示控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderHidden(false);//第一次调整日
			/** 必输控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderRequired(true);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderRequired(true);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderRequired(true);//第一次调整日
			
		}else {
			/** 显示控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderHidden(false);//第一次调整日
			/** 必输控制 */
			AccAssetstrsf.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
			AccAssetstrsf.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
			AccAssetstrsf.fir_adjust_day._obj._renderRequired(false);//第一次调整日
		}
	};

	//-------------------根据贷款利率浮动方式同比调整显示-----------------------
	function changeIrFloatType(){
		var floatType = AccAssetstrsf.ir_float_type._getValue();
		if(floatType=='0'){//加百分比
			/** 显示控制 */
			AccAssetstrsf.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
			
		}else if(floatType=='1'){//加点
			/** 显示控制 */
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.ir_float_rate._setValue("");//贷款利率浮动比
		}else {
			/** 显示控制 */
			AccAssetstrsf.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
			AccAssetstrsf.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.ir_float_rate._setValue("");//贷款利率浮动比
			AccAssetstrsf.ir_float_point._setValue("");//贷款利率浮动点数
		}
	};
	//-------------------根据逾期利率浮动方式同比调整显示-----------------------
	function changeOverdueFloatType(){
		var overdueFloatType = AccAssetstrsf.overdue_float_type._getValue();
		if(overdueFloatType=='0'){//加百分比
			/** 显示控制 */
			AccAssetstrsf.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
		}else if(overdueFloatType=='1'){//加点
			/** 显示控制 */
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
		}else {
			/** 显示控制 */
			AccAssetstrsf.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
			AccAssetstrsf.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.overdue_rate._setValue("");//逾期利率浮动比
			AccAssetstrsf.overdue_point._setValue("");//逾期利率浮动点数
		}
	};

	//-------------------根据违约利率浮动方式同比调整显示-----------------------
	function changeDefaultFloatType(){
		var defaultFloatType = AccAssetstrsf.default_float_type._getValue();
		if(defaultFloatType=='0'){//加百分比
			/** 显示控制 */
			AccAssetstrsf.default_rate._obj._renderHidden(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.default_rate._obj._renderRequired(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
		}else if(defaultFloatType=='1'){//加点
			/** 显示控制 */
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(false);//违约利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(true);//违约利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
		}else {
			/** 显示控制 */
			AccAssetstrsf.default_rate._obj._renderHidden(true);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderHidden(true);//违约利率浮动点数
			/** 必输控制 */
			AccAssetstrsf.default_rate._obj._renderRequired(false);//违约利率浮动比
			AccAssetstrsf.default_point._obj._renderRequired(false);//违约利率浮动点数
			/** 值域控制 */
			AccAssetstrsf.default_rate._setValue("");//违约利率浮动比
			AccAssetstrsf.default_point._setValue("");//违约利率浮动点数
		}
	};

	function getRepayType(data){
		var repay_mode_id = data.repay_mode_id._getValue();
		AccAssetstrsf.repay_type._setValue(repay_mode_id);
		//var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?repay_mode_id='+repay_mode_id;
		//url = EMPTools.encodeURI(url);
		//PrdRepayPlanList._obj.ajaxQuery(url,null);
	};
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccAssetstrsf.fount_serno._getValue();	//业务申请编号，不是出账流水号
		data['cus_id'] = AccAssetstrsf.cus_id._getValue();	//客户码
		data['prd_id'] = AccAssetstrsf.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	
	function ifRrAccordType(){
    	var ir_accord_type = AccAssetstrsf.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		AccAssetstrsf.overdue_rate._obj._renderHidden(true);
    		AccAssetstrsf.overdue_rate._obj._renderRequired(false);

    		AccAssetstrsf.default_rate._obj._renderHidden(true);
    		AccAssetstrsf.default_rate._obj._renderRequired(false);
    	}
    };
    /**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
    function doReturnByOneKey() {
		var cus_id  =AccAssetstrsf.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="台账信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="AccAssetstrsfGroup" title="资产转让台账" maxColumn="2">
	        <emp:text id="AccAssetstrsf.bill_no" label="借据编号" maxlength="40" required="true" />
	        <emp:text id="AccAssetstrsf.cont_no" label="合同编号" maxlength="40" required="false" />			
			<emp:text id="AccAssetstrsf.asset_no" label="资产包编号" maxlength="40" required="false" colSpan="2"/>			
			<emp:text id="AccAssetstrsf.toorg_no" label="交易对手行号" />
			<emp:text id="AccAssetstrsf.toorg_name" label="交易对手行名" />		
			<emp:select id="AccAssetstrsf.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE"/>
			<emp:select id="AccAssetstrsf.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE"/>
			<emp:text id="AccAssetstrsf.takeover_date" label="转让日期" maxlength="10" required="false" />
			<emp:select id="AccAssetstrsf.is_risk_takeover" label="风险是否转移" hidden="true" required="false" dictname="STD_ZX_YES_NO"/>
		</emp:gridLayout>
		<emp:gridLayout id="" title="原借据信息" maxColumn="2">
			<emp:text id="AccAssetstrsf.ori_bill_no" label="原借据编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.ori_cont_no" label="原合同编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.cus_id_displayname" label="客户名称"  required="false" />
			<emp:text id="AccAssetstrsf.repay_type" label="还款方式" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccAssetstrsf.repay_type_displayname" label="还款方式"  readonly="true"/>
			<emp:select id="AccAssetstrsf.repay_term" label="还款间隔周期" required="false" dictname="STD_BACK_CYCLE"/>
			<emp:text id="AccAssetstrsf.repay_space" label="还款间隔" maxlength="10" required="false" />
			<emp:select id="AccAssetstrsf.ei_type" label="结息方式" required="false" dictname="STD_IQP_RATE_CYCLE"/>
			<emp:select id="AccAssetstrsf.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccAssetstrsf.guar_type" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS"/>
			<emp:textarea id="AccAssetstrsf.guar_desc" label="担保品说明" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="" title="金额信息" maxColumn="2">
			<emp:text id="AccAssetstrsf.loan_amt" label="贷款金额" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="AccAssetstrsf.loan_bal" label="贷款余额" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="AccAssetstrsf.takeover_amt" label="转让本金" maxlength="18" required="false" dataType="Currency"/>
			
			<emp:select id="AccAssetstrsf.afee_type" label="安排计费方式" required="false" dictname="STD_ZB_PREPARGET_MODE"/>
			<emp:text id="AccAssetstrsf.takeover_frate" label="转让费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="AccAssetstrsf.afee" label="安排费" maxlength="18" required="false" dataType="Currency"/>
			<emp:select id="AccAssetstrsf.afee_pay_type" label="安排费支付方式" required="false" dictname="STD_ZB_PREPARPAY_MODE"/>
			<emp:text id="AccAssetstrsf.mfee" label="管理费" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccAssetstrsf.int" label="应计利息" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="AccAssetstrsf.agent_asset_acct" label="结算账号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.takeover_rate" label="转让利率" maxlength="10" required="false" dataType="Rate"/>
			<emp:text id="AccAssetstrsf.takeover_int" label="转让利息" maxlength="18" required="false" dataType="Currency"/>
		</emp:gridLayout>
			
		<emp:gridLayout id="" title="利率信息" maxColumn="2">
			<emp:select id="AccAssetstrsf.ir_accord_type" label="利率依据方式"  onblur="ir_accord_typeChange('change');" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="AccAssetstrsf.ir_type" label="利率种类" hidden="true" required="false" dictname="STD_ZB_RATE_TYPE" />
			<emp:text id="AccAssetstrsf.ruling_ir" label="基准利率（年）" hidden="true" maxlength="16" readonly="false" required="false" dataType="Rate"/>
			<emp:text id="ruling_mounth" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="false"/>  
			<emp:text id="AccAssetstrsf.pad_rate_y" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2" readonly="false" required="false" dataType="Rate"/>
			<emp:select id="AccAssetstrsf.ir_adjust_type" label="利率调整方式" hidden="true" required="false" colSpan="1" readonly="true" dictname="STD_IR_ADJUST_TYPE" />
			<emp:select id="AccAssetstrsf.ir_next_adjust_term" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
			<emp:select id="AccAssetstrsf.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
			<emp:date id="AccAssetstrsf.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			<emp:select id="AccAssetstrsf.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="AccAssetstrsf.ir_float_rate" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getRelYM();" required="false" dataType="Percent2" />
			<emp:text id="AccAssetstrsf.ir_float_point" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />
			<emp:text id="AccAssetstrsf.reality_ir_y" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange()" readonly="true" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="AccAssetstrsf.overdue_float_type" label="逾期利率浮动方式" hidden="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="AccAssetstrsf.overdue_rate" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" required="false" dataType="Percent2" />
			<emp:text id="AccAssetstrsf.overdue_point" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" required="false" dataType="Int" />
			<emp:text id="AccAssetstrsf.overdue_rate_y" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			<emp:select id="AccAssetstrsf.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="AccAssetstrsf.default_rate" label="违约利率浮动比" maxlength="16" hidden="true" required="false" dataType="Percent2" />
			<emp:text id="AccAssetstrsf.default_point" label="违约利率浮动点数" maxlength="38" hidden="true" required="false" dataType="Int" />
			<emp:text id="AccAssetstrsf.default_rate_y" label="违约利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate" colSpan="2"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="" title="日期信息" maxColumn="2">
			<emp:text id="AccAssetstrsf.loan_start_date" label="原借款起始日期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.loan_end_date" label="原借款到期日期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.latest_repay" label="最近还款日" maxlength="10" required="false" />
			
			<emp:text id="AccAssetstrsf.acc_day" label="日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="AccAssetstrsf.acc_year" label="年份" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccAssetstrsf.acc_mon" label="月份" maxlength="5" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" title="登记信息" maxColumn="2">
			<emp:text id="AccAssetstrsf.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccAssetstrsf.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccAssetstrsf.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="AccAssetstrsf.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true"/>
			<emp:select id="AccAssetstrsf.acc_status" label="台账状态" required="false" dictname="STD_ZB_ACC_TYPE" />
			<emp:text id="AccAssetstrsf.fount_serno" label="业务申请编号" hidden="true" />
			<emp:text id="AccAssetstrsf.prd_id" label="产品编号" hidden="true" />
		</emp:gridLayout>
	</emp:tab>
	  <emp:tab label="合同信息" id="subTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.AccAssetstrsf.cont_no}&menuIdTab=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
    </emp:tabGroup>
	
	<div align="center">
		<br>
		<%if(!"not".equals(isHaveButton)){ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
