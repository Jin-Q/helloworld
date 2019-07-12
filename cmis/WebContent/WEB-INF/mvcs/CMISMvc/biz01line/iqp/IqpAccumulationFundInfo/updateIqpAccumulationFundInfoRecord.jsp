<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}
	}
	
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsIqpAccumulation.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function onload(){
		selectIrType();//判断显示哪些利率种类
		ir_accord_typeChange("init");
		reality_ir_yChange();//计算执行利率月
		setRulingMounth();//计算基准利率月
		changeIrFloatType();//利率浮动方式显示

		<%if(op.equals("view")){ %>
		IqpAccumulationFundInfo.ir_adjust_type._obj._renderReadonly(true);
		IqpAccumulationFundInfo.reality_ir_y._obj._renderReadonly(true);
		IqpAccumulationFundInfo.overdue_rate_y._obj._renderReadonly(true);
		IqpAccumulationFundInfo.default_rate_y._obj._renderReadonly(true);
		IqpAccumulationFundInfo.overdue_float_type._obj._renderReadonly(true);
		IqpAccumulationFundInfo.default_float_type._obj._renderReadonly(true);
		<%} %>

		ifRrAccordType();
    };
	
	function doSub(){
		var form = document.getElementById("submitForm");
		IqpAccumulationFundInfo._checkAll();
		if(IqpAccumulationFundInfo._checkAll()){
			IqpAccumulationFundInfo._toForm(form); 
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
						alert("保存成功!");
						window.location.reload();
					}else {
						alert("发生异常!");
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

	//01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
	function caculateOverdueRate(){
		var ir_accord_type = IqpAccumulationFundInfo.ir_accord_type._getValue();
		if(ir_accord_type == "01"){
			var reality_ir_y = IqpAccumulationFundInfo.reality_ir_y._obj.element.value;//执行利率（年）
			if(parseFloat(reality_ir_y)>=0){
				var overdue_rate_y = IqpAccumulationFundInfo.overdue_rate_y._obj.element.value;//逾期利率（年）
				var overdue_rate = parseFloat(overdue_rate_y)/parseFloat(reality_ir_y)-1;
				IqpAccumulationFundInfo.overdue_rate._setValue(overdue_rate+"");
				IqpAccumulationFundInfo.overdue_float_type._setValue("0");//加百分比
	    	}else{
	        	alert("请先输入执行利率!");
	        	IqpAccumulationFundInfo.overdue_rate_y._setValue("");
	        }
	    }
	};
	//01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
	function caculateDefaultRate(){
		var ir_accord_type = IqpAccumulationFundInfo.ir_accord_type._getValue();
		if(ir_accord_type == "01"){
			var reality_ir_y = IqpAccumulationFundInfo.reality_ir_y._obj.element.value;//执行利率（年）
			if(parseFloat(reality_ir_y)>=0){
				var default_rate_y = IqpAccumulationFundInfo.default_rate_y._obj.element.value;//违约利率（年）
				var default_rate = parseFloat(default_rate_y)/parseFloat(reality_ir_y)-1;
				IqpAccumulationFundInfo.default_rate._setValue(default_rate+"");
				IqpAccumulationFundInfo.default_float_type._setValue("0");//加百分比
	    	}else{
	        	alert("请先输入执行利率!");
	        	IqpAccumulationFundInfo.overdue_rate_y._setValue("");
	        }
	    }
	};

	function ifRrAccordType(){
    	var ir_accord_type = IqpAccumulationFundInfo.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		IqpAccumulationFundInfo.overdue_rate._obj._renderHidden(true);
    		IqpAccumulationFundInfo.overdue_rate._obj._renderRequired(false);

    		IqpAccumulationFundInfo.default_rate._obj._renderHidden(true);
    		IqpAccumulationFundInfo.default_rate._obj._renderRequired(false);
    	}
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpAccumulationFundInfoRecord.do" method="POST">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="公积金贷款" id="base_tab" needFlush="true" initial="true" >
			<emp:gridLayout id="" maxColumn="2" title="金额信息">
			    <emp:select id="IqpAccumulationFundInfo.apply_cur_type" label="申请币种" required="true" onchange="selectIrType()" dictname="STD_ZX_CUR_TYPE" />
			    <emp:text id="IqpAccumulationFundInfo.apply_amount" label="申请金额" maxlength="16" required="true" dataType="Currency" />
			</emp:gridLayout>
			<emp:gridLayout id="" maxColumn="2" title="利率信息">
			    <emp:select id="IqpAccumulationFundInfo.ir_accord_type" label="利率依据方式" onchange="ir_accord_typeChange('change');" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			    <emp:select id="IqpAccumulationFundInfo.ir_type" label="利率种类" hidden="true" required="false" dictname="STD_ZB_RATE_TYPE" />
			    <emp:text id="IqpAccumulationFundInfo.ruling_ir" label="基准利率（年）" hidden="true" maxlength="16" onblur="getRulMounth()" required="false" dataType="Rate" />
			    <emp:text id="ruling_mounth" label="对应基准利率(月)" hidden="true" maxlength="16" required="false" dataType="Rate4Month" readonly="true"/>
			    
			    <emp:select id="IqpAccumulationFundInfo.ir_adjust_type" label="利率调整方式" hidden="true" required="false" dictname="STD_IR_ADJUST_TYPE" />
			    
			    <emp:select id="IqpAccumulationFundInfo.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
			    <emp:text id="IqpAccumulationFundInfo.ir_float_rate" label="利率浮动比" hidden="true" colSpan="2" onblur="getRelYM();" maxlength="16" required="false" dataType="Rate" />
			    <emp:text id="IqpAccumulationFundInfo.ir_float_point" label="利率浮动点数" hidden="true" colSpan="2" onblur="getRelYM();" maxlength="10" required="false" />
			    <emp:text id="IqpAccumulationFundInfo.reality_ir_y" label="执行利率（年）" maxlength="16" hidden="true" onchange="reality_ir_yChange()" required="false" dataType="Rate" />
			    <emp:text id="reality_mounth" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>
			    
			    <emp:select id="IqpAccumulationFundInfo.overdue_float_type" label="逾期利率浮动方式" hidden="true" readonly="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			    <emp:text id="IqpAccumulationFundInfo.overdue_rate" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Rate" />
			    <emp:text id="IqpAccumulationFundInfo.overdue_point" label="逾期利率浮动点" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" />
			    <emp:text id="IqpAccumulationFundInfo.overdue_rate_y" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate" onblur="caculateOverdueRate()"/>
			    
			    <emp:select id="IqpAccumulationFundInfo.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" readonly="true" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			    <emp:text id="IqpAccumulationFundInfo.default_rate" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Rate" />
			    <emp:text id="IqpAccumulationFundInfo.default_point" label="违约利率浮动点" maxlength="10" hidden="true" onchange="getDefaultRateY();" required="false" />
			    <emp:text id="IqpAccumulationFundInfo.default_rate_y" label="违约利率（年）" hidden="true" maxlength="16" required="false" dataType="Rate" onblur="caculateDefaultRate()"/>
		        <emp:text id="IqpAccumulationFundInfo.serno" label="业务流水号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true" readonly="true" />
		</emp:gridLayout>
		</emp:tab>
		</emp:tabGroup>
		<%if(!op.equals("view")){ %>
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" />
		</div>
		<%} %>
	</emp:form>
</body>
</html>
</emp:page>
