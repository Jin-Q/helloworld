<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>	
<%
	//request = (HttpServletRequest) pageContext.getRequest();
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String bill_no = request.getParameter("bill_no");
	String cont_no = request.getParameter("cont_no");
	String loan_balance = request.getParameter("loan_balance");
	String cur_type = request.getParameter("cur_type");
	String last_pay_date = (String)context.getDataValue("last_pay_date");
	context.put("bill_no",bill_no);
	context.put("cont_no",cont_no);
	context.put("loan_balance",loan_balance);
	context.put("cur_type",cur_type);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  begin **/
	function doOnLoad(){
		var optionJosn = "PP,PI,AM,LM";
		var options =AccTranTrustCompany.reclaim_mode._obj.element.options;		
		for ( var i = options.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options[i].value)<0){
				options.remove(i);
			}
		}
	};
	function checkAmt(){
		var tran_amt = AccTranTrustCompany.tran_amt._getValue();
		var list_type = AccTranTrustCompany.list_type._getValue();
		var loan_balance = '<%=loan_balance%>';
		if(tran_amt != "" && tran_amt != null){
            if(parseFloat(tran_amt)<=0){
                 alert("请输入正确的交易金额！");
                 AccTranTrustCompany.tran_amt._setValue("");
                 return;
            }
            if(list_type =="" || list_type == null){
            	 alert("请输入明细类型！"); 
            	 AccTranTrustCompany.tran_amt._setValue("");
                 return;
            }
            var handleSuccess = function(o){
    			if(o.responseText !== undefined) {
    				try {
    					var jsonstr = eval("("+o.responseText+")");
    				} catch(e) {
    					alert("Parse jsonstr1 define error!" + e.message);
    					return;
    				}
    				var amt = jsonstr.amt; 
    				if(parseFloat(amt)+parseFloat(tran_amt)>parseFloat(loan_balance)){
    					alert("输入的交易金额超过了贷款余额,可输入最高金额为:"+(parseFloat(loan_balance)-parseFloat(amt)));     
    					AccTranTrustCompany.tran_amt._setValue("");
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
    		var url = '<emp:url action="checkAccTranTrustAmt.do"/>?oprate=add&tran_amt='+tran_amt+'&list_type='+list_type+'&bill_no=${context.bill_no}';
    		url = EMPTools.encodeURI(url);
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		}
    };
    //当款项明细为【安排错合费】、【贷款管理费】时，交易金额自动计算
    function checkAutoCalInfo(msg){
	 	var rm_value = AccTranTrustCompany.reclaim_mode._getValue();
		var list_type = AccTranTrustCompany.list_type._getValue();
		if(list_type!="0"){
			if(rm_value !=null && rm_value!=""){
				if(rm_value == "AM" || rm_value == "LM"){
					var tran_date = AccTranTrustCompany.tran_date._getValue();
					if(tran_date!=null && tran_date!=""){
						//异步调用交易金额自动计算方法
						autoCalTranAmt(rm_value);
					}else if(msg =="click"){
						
					}else{
						 alert("请先录入付款日！");
						 AccTranTrustCompany.tran_amt._setValue("");
						 return;
					}
				}
			}else{
				 alert("请先选择款项明细！");
				 AccTranTrustCompany.tran_amt._setValue("");
				 return;
			}
		}else{//0:发放 
			AccTranTrustCompany.reclaim_mode._obj._renderHidden(true);
            AccTranTrustCompany.reclaim_mode._obj._renderRequired(false);
            AccTranTrustCompany.reclaim_mode._setValue("");
		}
    };

	function autoCalTranAmt(rm_value){
		var tran_date = AccTranTrustCompany.tran_date._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
					var tranAmt = jsonstr.tranAmt;
					var msg = jsonstr.msg;
					if(msg ="success"){
						AccTranTrustCompany.tran_amt._setValue(tranAmt);
					}else{
						alert("自动计算失败！");
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
		var url = '<emp:url action="autoCalTranAmt.do"/>?tran_date='+tran_date+"&last_pay_date=${context.last_pay_date}&bill_no=${context.bill_no}&cont_no=${context.cont_no}"+"&rm_value="+rm_value;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};
	
    function checkDate(){
    	start = AccTranTrustCompany.tran_date._getValue();
    	openDay = '${context.OPENDAY}';
    	if(start!=null && start!="" ){
    		var flag = CheckDate1BeforeDate2(openDay,start);
    		if(!flag){
    			alert("开始日期要大于当前日期！");
    			AccTranTrustCompany.tran_date._setValue("");
    			return false;
    		}
    	}
    	//录入付款日，自动计算
    	var msg ="click";
    	checkAutoCalInfo(msg);
    };

    function clearTranAmt(){
    	AccTranTrustCompany.tran_amt._setValue("");
    	var rm_value = AccTranTrustCompany.reclaim_mode._getValue();
    	if(rm_value =="PI"){
    		AccTranTrustCompany.trade_status._setValue("1");
        }
    };
        
	function checkReclaimMode(){
		var list_type = AccTranTrustCompany.list_type._getValue();
        if(list_type == "1"){//1:回收
             AccTranTrustCompany.reclaim_mode._obj._renderHidden(false);
             AccTranTrustCompany.reclaim_mode._obj._renderRequired(true);
        }else{//0:发放 
             AccTranTrustCompany.reclaim_mode._obj._renderHidden(true);
             AccTranTrustCompany.reclaim_mode._obj._renderRequired(false);
             AccTranTrustCompany.reclaim_mode._setValue("");
        }
	};
	
	function doSave(){
		var form = document.getElementById("submitForm");
		if(AccTranTrustCompany._checkAll()){
			AccTranTrustCompany._toForm(form);
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
						var url = '<emp:url action="queryAccTranTrustCompanyList.do"/>?bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("保存失败!");
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
			return;
		}
	};

	function doReturn(){
		var url = '<emp:url action="queryAccTranTrustCompanyList.do"/>?bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}&cur_type=${context.cur_type}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  end **/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addAccTranTrustCompanyRecord.do" method="POST">
		
		<emp:gridLayout id="AccTranTrustCompanyGroup" title="信托公司贷款台账交易流水" maxColumn="2">
			<emp:text id="AccTranTrustCompany.bill_no" label="借据号" defvalue="<%=bill_no%>" maxlength="40" required="true" readonly="true" />
			<emp:text id="AccTranTrustCompany.cont_no" label="合同号" defvalue="<%=cont_no%>" maxlength="40" required="true" readonly="true" />
			<emp:select id="AccTranTrustCompany.list_type" label="明细类型" required="true" dictname="STD_ACC_DETAIL_TYPE" onchange="checkReclaimMode()"/>
			<emp:select id="AccTranTrustCompany.cur_type" label="币种" defvalue="<%=cur_type%>" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccTranTrustCompany.tran_amt" label="交易金额" maxlength="16" required="true" dataType="Currency" onblur="checkAmt();checkAutoCalInfo();"/>
			<emp:select id="AccTranTrustCompany.reclaim_mode" label="款项明细" required="true" dictname="STD_ZB_RECYCLE_TYPE" onchange="clearTranAmt();"/>
			<emp:date id="AccTranTrustCompany.tran_date" label="付款日" required="true" onblur="checkDate();"/>
			<emp:text id="AccTranTrustCompany.last_pay_date" label="上一付款日" required="true" defvalue="<%=last_pay_date%>" readonly="true"/>	
			<emp:select id="AccTranTrustCompany.trade_status" label="交易状态" required="true"  dictname="STD_ZB_STATUS" readonly="true" defvalue="2"/>
			<emp:text id="AccTranTrustCompany.input_id" label="登记人" required="false" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="AccTranTrustCompany.input_br_id" label="登记机构" required="false" defvalue="${context.organNo}" hidden="true" />
			<emp:text id="AccTranTrustCompany.input_date" label="登记日期" required="false"  defvalue="${context.OPENDAY}" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="save" label="确定"/>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

