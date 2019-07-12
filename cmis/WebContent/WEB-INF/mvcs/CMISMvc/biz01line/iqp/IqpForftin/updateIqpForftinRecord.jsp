<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	String reality_ir_y ="";
	String apply_amount ="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		} 
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	} 
	if(context.containsKey("reality_ir_y")){
		reality_ir_y = (String)context.getDataValue("reality_ir_y");
	}
	if(context.containsKey("apply_amount")){
		apply_amount = (String)context.getDataValue("apply_amount");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	
	/*--user code begin--*/
	
	function doOnLoad(){
		isReplace();
		var options = IqpForftin.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="01" || options[i].value=="02" || options[i].value=="03" || options[i].value=="04" || options[i].value=="06" ){
				options.remove(i);
			}
		}
	    checkMode();
	};
	function doSave(){
		if(!IqpForftin._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpForftin._toForm(form);
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
					alert("保存成功！");
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpForftinRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpForftin.is_replace._getValue();
		if(isPay == 1){
			IqpForftin.rpled_serno._obj._renderHidden(false);
			IqpForftin.rpled_serno._obj._renderRequired(true);
		}else{
			IqpForftin.rpled_serno._setValue("");
			IqpForftin.rpled_serno._obj._renderHidden(true);
			IqpForftin.rpled_serno._obj._renderRequired(false);
		}
	};

	function getSerno(data){
		IqpForftin.rpled_serno._setValue(data.bill_no._getValue());
	};			

	function checkMode(){
		var biz_settl_mode = IqpForftin.biz_settl_mode._getValue();
		if('${context.apply_term}' != null && '${context.apply_term}' != ""){
			IqpForftin.disc_day._setValue('${context.apply_term}');
		}
		IqpForftin.disc_day._obj._renderReadonly(true);
		if(biz_settl_mode == "05"){
			IqpForftin.disc_day._obj._renderReadonly(true);
			
			IqpForftin.porder_no._obj._renderHidden(false);
			IqpForftin.issue_date._obj._renderHidden(false);
			IqpForftin.drwr_name._obj._renderHidden(false);
			IqpForftin.disc_date._obj._renderHidden(false);
			IqpForftin.bill_end_date._obj._renderHidden(false);
			IqpForftin.porder_no._obj._renderRequired(true);
			IqpForftin.issue_date._obj._renderRequired(true);
			IqpForftin.drwr_name._obj._renderRequired(true);
			IqpForftin.disc_date._obj._renderRequired(true);
			IqpForftin.bill_end_date._obj._renderRequired(true);
			$(".emp_field_label:eq(11)").text("贴现天数");
			$(".emp_field_label:eq(4)").text("票据币种");
			$(".emp_field_label:eq(5)").text("票面金额");
			$(".emp_field_label:eq(7)").text("票据到期日");
			$(".emp_field_label:eq(9)").text("承兑人名称");
			$(".emp_field_label:eq(10)").text("贴现日期");
		}else if(biz_settl_mode == "07"){
			$(".emp_field_label:eq(11)").text("融资天数");
			$(".emp_field_label:eq(4)").text("应收款币种");
			$(".emp_field_label:eq(5)").text("应收款金额");
			$(".emp_field_label:eq(10)").text("应收款到期日");
			$(".emp_field_label:eq(9)").text("承诺付款人名称");

			IqpForftin.disc_day._obj._renderReadonly(true);

			IqpForftin.porder_no._obj._renderHidden(true);
			IqpForftin.issue_date._obj._renderHidden(true);
			IqpForftin.drwr_name._obj._renderHidden(true);
			IqpForftin.bill_end_date._obj._renderHidden(true);
			
			IqpForftin.porder_no._obj._renderRequired(false);
			IqpForftin.issue_date._obj._renderRequired(false);
			IqpForftin.drwr_name._obj._renderRequired(false);
			IqpForftin.bill_end_date._obj._renderRequired(false);

			IqpForftin.porder_no._setValue("");
			IqpForftin.issue_date._setValue("");
			IqpForftin.drwr_name._setValue("");
			IqpForftin.bill_end_date._setValue("");
		}
	};
	function caculatePayAmt(){
        var drft_amt = '${context.apply_amount}';//申请金额
        var dscnt_day = IqpForftin.disc_day._getValue();//贴现天数
        var arrangr_deduct = IqpForftin.arrangr_deduct_opt._getValue();//预扣款项
        var reality_ir_y = '<%=reality_ir_y%>';
        if(reality_ir_y == null || reality_ir_y == "" || reality_ir_y == "null"){
        	reality_ir_y = 0;
        }
        if(drft_amt != null && drft_amt != "" && dscnt_day != null && dscnt_day != "" && arrangr_deduct != null && arrangr_deduct != ""){
            var pay_amt = parseFloat(drft_amt)-(parseFloat(drft_amt)*parseFloat(reality_ir_y)/360*parseFloat(dscnt_day))-parseFloat(arrangr_deduct);
            if(pay_amt<0){
              alert("实付金额不能为负,请重新输入预扣款项");
              IqpForftin.pay_amt._setValue("");
              return;
            }
          //贸易融资贴现类产品币种为日元的实付金额计算值要四舍五入保留到个位
            if('${context.apply_cur_type}' == "JPY"){
            	IqpForftin.pay_amt._setValue(''+(pay_amt).toFixed(0)+'');
            }else{
            	IqpForftin.pay_amt._setValue(''+(pay_amt).toFixed(2)+'');
            }
            
        }
	};			

	function checkAmt(){
		var pay_amt = IqpForftin.pay_amt._getValue();
		var drft_amt = '${context.apply_amount}';
		if(pay_amt!=''&&drft_amt!=''){
			if(pay_amt-drft_amt>0){
				alert('实付金额不能大于申请金额！');
				IqpForftin.pay_amt._setValue('');
			}
		}
	};
	function caculateDiscDay(){
		sDate1 = '${context.end_date}';
		sDate2 = IqpForftin.disc_date._getValue();
		if(sDate1 != null  && sDate1 != "" && sDate2 != null  && sDate2 != ""){
			if(sDate2>sDate1){
               alert("业务到期日需大于贴现日");
               IqpForftin.disc_date._setValue("");
               return;
		    }
			var aDate,oDate1,oDate2,iDays;
	        aDate = sDate1.split('-');
	        oDate1 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        aDate = sDate2.split('-');
	        oDate2 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        iDays = parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);
	        IqpForftin.disc_day._setValue(iDays+"");
		}
    }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateIqpForftinRecord.do" method="POST">
		<emp:gridLayout id="IqpForftinGroup" maxColumn="2" title="福费廷信息">
			<emp:select id="IqpForftin.biz_settl_mode" label="原业务结算方式" required="true" onchange="checkMode()" dictname="STD_BIZ_SETTL_MODE" colSpan="2"/>
			<emp:select id="IqpForftin.is_replace" label="是否置换" required="true" onclick="isReplace();" dictname="STD_ZX_YES_NO"/>
			<emp:pop id="IqpForftin.rpled_serno" label="被置换业务编号"  url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="false" />
			<emp:text id="IqpForftin.porder_no" label="汇票号码" maxlength="40" colSpan="2" required="true" />
			<emp:select id="IqpForftin.bill_cur_type" label="票据币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpForftin.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="IqpForftin.issue_date" label="出票日期" required="true"/>
			<emp:date id="IqpForftin.bill_end_date" label="票据到期日" required="true" />
			<emp:text id="IqpForftin.drwr_name" label="出票人名称" maxlength="80" required="true" />
			<emp:text id="IqpForftin.accptr_name" label="承兑人名称" maxlength="80" required="true" />
			<emp:date id="IqpForftin.disc_date" label="贴现日期" required="true" />
			<emp:text id="IqpForftin.disc_day" label="贴现天数" maxlength="38" required="true" />
			<emp:text id="IqpForftin.arrangr_deduct_opt" label="预扣款项" maxlength="16" required="true" dataType="Currency" onblur="caculatePayAmt()"/>
			<emp:text id="IqpForftin.pay_amt" label="实付金额" maxlength="18" readonly="true" required="true" dataType="Currency" onchange="checkAmt()"/>
			<emp:select id="IqpForftin.is_internal_cert" label="是否国内证项下" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:text id="IqpForftin.serno" label="业务编号" defvalue="${context.serno}" colSpan="2" hidden="true" maxlength="40" required="true" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
