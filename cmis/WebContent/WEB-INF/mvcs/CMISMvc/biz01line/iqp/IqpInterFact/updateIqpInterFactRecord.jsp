<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
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
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	//国内保理 800021
	//国际保理 800020
	function onload(){
        if('${context.prd_id}' == "800021"){
        	IqpInterFact.fin_type._obj._renderHidden(false);
        	IqpInterFact.fact_type._obj._renderHidden(false);
        	IqpInterFact.biz_settl_mode._obj._renderHidden(true);
        	IqpInterFact.is_pay_int._obj._renderHidden(false);
        	
        	IqpInterFact.fin_type._obj._renderRequired(true);
        	IqpInterFact.fact_type._obj._renderRequired(true);
        	IqpInterFact.biz_settl_mode._obj._renderRequired(false);
        	IqpInterFact.is_pay_int._obj._renderRequired(true);
        }else if('${context.prd_id}' == "800020"){
        	IqpInterFact.fin_type._obj._renderHidden(true);
        	IqpInterFact.fact_type._obj._renderHidden(true);
        	IqpInterFact.biz_settl_mode._obj._renderHidden(false);
        	IqpInterFact.is_pay_int._obj._renderHidden(true);
        	
        	IqpInterFact.fin_type._obj._renderRequired(false);
        	IqpInterFact.fact_type._obj._renderRequired(false);
        	IqpInterFact.biz_settl_mode._obj._renderRequired(true);
        	IqpInterFact.is_pay_int._obj._renderRequired(false);
        }
        var options = IqpInterFact.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="05" || options[i].value=="06" || options[i].value=="07" ){
				options.remove(i);
			}
		}

	  	//HS141110017_保理业务改造  add by zhaozq start
	    setIntAcctNo();
	  	//HS141110017_保理业务改造  add by zhaozq end
	};
	
	function doSave(){
		if(!IqpInterFact._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpInterFact._toForm(form);
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

		var url = '<emp:url action="updateIqpInterFactRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
	function returnBuyCus(data){
		IqpInterFact.buy_cus_id._setValue(data.cus_id._getValue());
		IqpInterFact.buy_cus_id_displayname._setValue(data.cus_name._getValue());
    };
	function setIntAcctNo(){
		var is_pay_int = IqpInterFact.is_pay_int._getValue();
		if(is_pay_int=='1'){
			IqpInterFact.int_acct_no._obj._renderRequired(true);
			IqpInterFact.int_acct_no._obj._renderHidden(false);
		}else{
			IqpInterFact.int_acct_no._setValue('');
			IqpInterFact.int_acct_no._obj._renderRequired(false);
			IqpInterFact.int_acct_no._obj._renderHidden(true);
		}
	}
    /**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
    /**added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  begin**/
    function returnPoNoBl(data){
    	IqpInterFact.po_no._setValue(data.po_no._getValue());
	}
    /**added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  end**/
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpInterFactRecord.do" method="POST">
		<emp:gridLayout id="IqpInterFactGroup" maxColumn="2" title="保理信息">
			<!-- added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start -->
			<emp:pop id="IqpInterFact.po_no" label="保理池编号" url="queryIqpActrecpoManaPopList.do?PO_TYPE=2&cus_id=${context.cus_id}&returnMethod=returnPoNoBl" required="true" buttonLabel="引入" colSpan="2"/>
			<!-- added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end -->
			<emp:pop id="IqpInterFact.buy_cus_id_displayname" label="买方客户名称"  colSpan="2"  required="true"  url="queryAllCusPop.do?returnMethod=returnBuyCus"/>
			<emp:select id="IqpInterFact.biz_settl_mode" label="原业务结算方式" required="false" dictname="STD_BIZ_SETTL_MODE"/>
			<emp:select id="IqpInterFact.fin_type" label="融资类型" required="false" dictname="STD_ZB_FIN_TYPE" readonly="true"/>
			<emp:select id="IqpInterFact.is_pay_int" label="是否预收息" required="true" dictname="STD_ZX_YES_NO" onchange="setIntAcctNo()"/>
			<emp:text id="IqpInterFact.int_acct_no" label="预收息账号" required="false" maxlength="40" hidden="true"/>
			<emp:select id="IqpInterFact.fact_type" label="保理类型" required="false" dictname="STD_ZB_FACT_TYPE"/>
			<emp:select id="IqpInterFact.invc_cur_type" label="发票币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpInterFact.invc_totl_amt" label="发票总金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpInterFact.busnes_cont_no" label="贸易合同号" maxlength="40" required="true" />
			<emp:text id="IqpInterFact.agent_acct_no" label="待处理保理资金账号" maxlength="40" required="true" />
			<emp:textarea id="IqpInterFact.goods_dec" label="货物描述" maxlength="250" required="false" colSpan="2" />
		    <emp:text id="IqpInterFact.serno" label="业务编号" defvalue="${context.serno}" maxlength="40" hidden="true" required="false" readonly="true" />
		    <emp:text id="IqpInterFact.buy_cus_id" label="买方客户编号" maxlength="40" required="true" hidden="true"/>
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
