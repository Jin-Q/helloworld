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
		if('${context.apply_term}' != null && '${context.apply_term}' != ""){
			IqpDelayCreditPur.fin_day._setValue('${context.apply_term}');
		}
		IqpDelayCreditPur.fin_day._obj._renderReadonly(true);
	};

	function doSave(){
		if(!IqpDelayCreditPur._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpDelayCreditPur._toForm(form);
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

		var url = '<emp:url action="updateIqpDelayCreditPurRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}	
	//---------是否置换------------		
	function isReplace(){
		var isPay = IqpDelayCreditPur.is_replace._getValue();
		if(isPay == 1){
			IqpDelayCreditPur.rpled_serno._obj._renderHidden(false);
			IqpDelayCreditPur.rpled_serno._obj._renderRequired(true);
		}else {
			IqpDelayCreditPur.rpled_serno._setValue("");
			IqpDelayCreditPur.rpled_serno._obj._renderHidden(true);
			IqpDelayCreditPur.rpled_serno._obj._renderRequired(false);
		}
	};
	function getSerno(data){
		IqpDelayCreditPur.rpled_serno._setValue(data.bill_no._getValue());
	};						

	function checkAmt(){
		var pay_amt = IqpDelayCreditPur.pay_amt._getValue();
		var rece_amt = '${context.apply_amount}';//申请金额
		if(pay_amt!=''&&rece_amt!=''){
			if(pay_amt-rece_amt>0){
				alert('实付金额不能大于申请金额！');
				IqpDelayCreditPur.pay_amt._setValue('');
			}
		}
	};
	function caculatePayAmt(){
        var rece_amt = '${context.apply_amount}';//申请金额
        var fin_day = IqpDelayCreditPur.fin_day._getValue();//融资天数
        var arrangr_deduct = IqpDelayCreditPur.arrangr_deduct._getValue();//预扣款项
        var reality_ir_y = '<%=reality_ir_y%>'; 
        if(reality_ir_y == null || reality_ir_y == "" || reality_ir_y == "null"){
        	reality_ir_y = 0;
        }
        if(rece_amt != null && rece_amt != "" && fin_day != null && fin_day != "" && arrangr_deduct != null && arrangr_deduct != ""){
            var pay_amt = parseFloat(rece_amt)-(parseFloat(rece_amt)*parseFloat(reality_ir_y)/360*parseFloat(fin_day))-parseFloat(arrangr_deduct);
            if(pay_amt<0){
              alert("实付金额不能为负,请重新输入预扣款项");
              IqpDelayCreditPur.pay_amt._setValue("");
              return;
            }
          //贸易融资贴现类产品币种为日元的实付金额计算值要四舍五入保留到个位
            if('${context.apply_cur_type}' == "JPY"){
            	IqpDelayCreditPur.pay_amt._setValue(''+(pay_amt).toFixed(0)+'');
            }else{
            	IqpDelayCreditPur.pay_amt._setValue(''+(pay_amt).toFixed(2)+'');
            }
        }
	};		
	function caculateDiscDay(){
		sDate1 = '${context.end_date}';
		sDate2 = IqpDelayCreditPur.rece_end_date._getValue();
		if(sDate1 != null  && sDate1 != "" && sDate2 != null  && sDate2 != ""){
			if(sDate2>sDate1){
               alert("业务到期日需大于贴现日");
               IqpDelayCreditPur.rece_end_date._setValue("");
               return;
		    }
			var aDate,oDate1,oDate2,iDays;
	        aDate = sDate1.split('-');
	        oDate1 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        aDate = sDate2.split('-');
	        oDate2 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
	        iDays = parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);
	        IqpDelayCreditPur.fin_day._setValue(iDays+"");
		}
    }	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="updateIqpDelayCreditPurRecord.do" method="POST">
		<emp:gridLayout id="IqpDelayCreditPurGroup" maxColumn="2" title="应收款买入信息">
			<emp:text id="IqpDelayCreditPur.serno" label="业务编号" maxlength="40" colSpan="2" defvalue="${context.serno}" hidden="true" required="true" readonly="true" />
			<emp:select id="IqpDelayCreditPur.is_replace" label="是否置换" required="true" onclick="isReplace();" dictname="STD_ZX_YES_NO"/>
			<emp:pop id="IqpDelayCreditPur.rpled_serno" label="被置换业务编号" url="queryCtrListPop4Replace.do?cus_id=${context.cus_id}&prd_id=${context.prd_id}" returnMethod="getSerno" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="IqpDelayCreditPur.rece_cur_type" label="应收款币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpDelayCreditPur.rece_amt" label="应收款金额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="IqpDelayCreditPur.rece_end_date" label="应收款到期日" required="true" />
			<emp:text id="IqpDelayCreditPur.fin_day" label="融资天数" maxlength="38" required="true" readonly="true"/>
			<emp:text id="IqpDelayCreditPur.arrangr_deduct" label="预扣款项" maxlength="16" required="true" onchange="caculatePayAmt();checkAmt()"/>
			<emp:text id="IqpDelayCreditPur.pay_amt" label="实付金额" maxlength="18" readonly="true" required="true" dataType="Currency" onchange="checkAmt()"/>
			<emp:text id="IqpDelayCreditPur.promissory_pyr_name" label="承诺付款人名称" maxlength="80" required="true" />
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
