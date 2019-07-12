<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	String reality_ir_y ="";
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
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){
		isReplace();
		if('${context.apply_term}' != null && '${context.apply_term}' !=""){
			IqpFastCreditDscnt.dscnt_day._setValue('${context.apply_term}');
		}
		IqpFastCreditDscnt.dscnt_day._obj._renderReadonly(true);
		caculatePayAmt();
	};

	function doSave(){
		if(!IqpFastCreditDscnt._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpFastCreditDscnt._toForm(form);
		//var serno = IqpBksyndic._getValue();
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

		var url = '<emp:url action="updateIqpFastCreditDscntRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}	
	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpFastCreditDscnt.is_replace._getValue();
		if(isPay == 1){
			IqpFastCreditDscnt.rpled_serno._obj._renderHidden(false);
			IqpFastCreditDscnt.rpled_serno._obj._renderRequired(true);
		}else {
			IqpFastCreditDscnt.rpled_serno._setValue("");
			IqpFastCreditDscnt.rpled_serno._obj._renderHidden(true);
			IqpFastCreditDscnt.rpled_serno._obj._renderRequired(false);
		}
	};

	function getSerno(data){
		IqpFastCreditDscnt.rpled_serno._setValue(data.bill_no._getValue());
	};		

	function caculatePayAmt(){
        var drft_amt = '${context.apply_amount}';//申请金额
        var dscnt_day = IqpFastCreditDscnt.dscnt_day._getValue();//贴现天数
        var arrangr_deduct = IqpFastCreditDscnt.arrangr_deduct._getValue();//预扣款项
        var reality_ir_y = '<%=reality_ir_y%>';
        if(reality_ir_y == null || reality_ir_y == "" || reality_ir_y == "null"){
        	reality_ir_y = 0;
        }
        if(drft_amt != null && drft_amt != "" && dscnt_day != null && dscnt_day != "" && arrangr_deduct != null && arrangr_deduct != ""){
            var pay_amt = round(parseFloat(drft_amt)-(parseFloat(drft_amt)*parseFloat(reality_ir_y)/360*dscnt_day)-parseFloat(arrangr_deduct),2);
            if(pay_amt<0){
              alert("实付金额不能为负,请重新输入预扣款项");
              IqpFastCreditDscnt.pay_amt._setValue("");
              return;
            }
            //贸易融资贴现类产品币种为日元的实付金额计算值要四舍五入保留到个位
            if('${context.apply_cur_type}' == "JPY"){
            	IqpFastCreditDscnt.pay_amt._setValue(''+(pay_amt).toFixed(0)+'');
            }else{
            	IqpFastCreditDscnt.pay_amt._setValue(''+(pay_amt).toFixed(2)+'');
            }
        }
	};			

	function checkAmt(){
		var pay_amt = IqpFastCreditDscnt.pay_amt._getValue();
		var drft_amt = '${context.apply_amount}';//申请金额
		if(pay_amt!=''&&drft_amt!=''){
			if(pay_amt-drft_amt>0){
				alert('实付金额不能大于申请金额！');
				IqpFastCreditDscnt.pay_amt._setValue('');
			}
		}
	};

	function dateDiff(){
		var sDate1 = '${context.end_date}';
		var sDate2 = IqpFastCreditDscnt.dscnt_date._getValue();
		if(sDate2 != null && sDate2 != ""){
           var openDay = '${context.OPENDAY}';
           if(sDate2<openDay){
        	   alert("贴现日期不能小于当前系统时间");
        	   IqpFastCreditDscnt.dscnt_date._setValue("");
               return;
           }
		}
		if(sDate1 != null  && sDate1 != "" && sDate2 != null  && sDate2 != ""){
			if(sDate2>sDate1){
               alert("业务到期日需大于贴现日");
               IqpFastCreditDscnt.dscnt_date._setValue("");
               return;
		    }
			var aDate,oDate1,oDate2,iDays;
	        aDate = sDate1.split('-');
	        oDate1 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        aDate = sDate2.split('-');
	        oDate2 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        iDays = parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);
	        IqpFastCreditDscnt.dscnt_day._setValue(iDays+"");
		}
	};
	/*--user code end--*/
	
</script>

	
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="updateIqpFastCreditDscntRecord.do" method="POST">
		<emp:gridLayout id="IqpFastCreditDscntGroup" maxColumn="2" title="汇票贴现信息">
			<emp:text id="IqpFastCreditDscnt.serno" label="业务编号" colSpan="2" defvalue="${context.serno}" hidden="true" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpFastCreditDscnt.is_replace" label="是否置换" required="true" onclick="isReplace();" dictname="STD_ZX_YES_NO"/>
			<emp:pop id="IqpFastCreditDscnt.rpled_serno" label="被置换业务编号" url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="IqpFastCreditDscnt.post_order_no" label="汇票号码" maxlength="40" required="true" colSpan="2"/>
			<emp:select id="IqpFastCreditDscnt.bill_cur_type" label="票据币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpFastCreditDscnt.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="IqpFastCreditDscnt.issue_date" label="出票日期" required="true" />
			<emp:date id="IqpFastCreditDscnt.bill_end_date" label="票据到期日" required="true" />
			<emp:text id="IqpFastCreditDscnt.drwr_name" label="出票人名称" maxlength="80" required="true" />
			<emp:text id="IqpFastCreditDscnt.accptr_name" label="承兑人名称" maxlength="80" required="true" />   
			<emp:date id="IqpFastCreditDscnt.dscnt_date" label="贴现日期" required="true" onblur="dateDiff();caculatePayAmt()"/>
			<emp:text id="IqpFastCreditDscnt.dscnt_day" label="贴现天数" maxlength="38" required="true" readonly="true"/>
			<emp:text id="IqpFastCreditDscnt.arrangr_deduct" label="预扣款项" maxlength="18" onchange="caculatePayAmt()" required="true" dataType="Currency" />
			<emp:text id="IqpFastCreditDscnt.pay_amt" label="实付金额" maxlength="18" readonly="true" required="true" dataType="Currency" onchange="checkAmt()"/>
			<emp:select id="IqpFastCreditDscnt.is_internal_cert" label="是否国内证项下" required="true" dictname="STD_ZX_YES_NO"/>
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
