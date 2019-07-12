<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<%
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	request.setAttribute("canwrite","");
	String flow = "";//流程查看标识
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
	}
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpBailSubDisList.do"/>?flag=<%=flag%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	$(document).ready(function(){
		/** modified by yangzy 2015/07/20 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		<%if("1".equals(flag)){%>
		$(".emp_gridlayout_title:first").text("保证金追加申请");
		$(".emp_field_label:eq(16)").text("追加保证金金额");
		$(".emp_field_label:eq(13)").text("追加后保证金比率");
		$(".emp_field_label:eq(14)").text("追加后保证金汇率");
		$(".emp_field_label:eq(15)").text("追加后保证金金额");
		$(".emp_gridlayout_title:eq(2)").text("追加明细");
		<%}%>
		<%if("2".equals(flag)){%>
		$(".emp_gridlayout_title:first").text("保证金提取申请");
		$(".emp_field_label:eq(16)").text("提取保证金金额");
		$(".emp_field_label:eq(13)").text("提取后保证金比率");
		$(".emp_field_label:eq(14)").text("提取后保证金汇率");
		$(".emp_field_label:eq(15)").text("提取后保证金金额");
		$(".emp_gridlayout_title:eq(2)").text("提取明细");
		<%}%>
		/** modified by yangzy 2015/07/20 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	 });
	function doLoad(){
		var cont_no = IqpBailSubDis.cont_no._getValue();
		var serno = IqpBailSubDis.serno._getValue();
		var callback = {
    			success : "doSuccess",
    			isJSON : true,
    			form : ""
    		};	
		//异步获取原保证金金额和原保证金比例
		if(serno ==null || serno ==""){
			var url = '<emp:url action="queryBailAmtByContNo.do"/>?cont_no='+cont_no;
		}else{
			var url = '<emp:url action="queryBailAmtByContNo.do"/>?cont_no='+cont_no+'&serno='+serno;
		}
		url = EMPTools.encodeURI(url);
    	EMPTools.ajaxRequest('POST',url, callback);
		checkIsShowAddBail();
	};
	function doSuccess(data){
		IqpBailSubDis.cont_cur_type._setValue(data.cont_cur_type);
		IqpBailSubDis.exchange_rate._setValue(data.exchange_rate);
		IqpBailSubDis.cont_amt._setValue(data.cont_amt);
		IqpBailSubDis.cont_rmb_amount._setValue(data.cont_rmb_amount);

		IqpBailSubDis.security_cur_type._setValue(data.security_cur_type);
		IqpBailSubDis.security_exchange_rate._setValue(data.security_exchange_rate);
		IqpBailSubDis.ori_bail_amt._setValue(data.ori_bail_amt);
		IqpBailSubDis.ori_bail_perc._setValue(data.ori_bail_perc);
		IqpBailSubDis.security_rmb_amt._setValue(data.security_rmb_amt);
		
	};
	 //-------------判断是否显示新增保证金------------
    function checkIsShowAddBail(){
        var isShowAddBail = '${context.isShowAddBail}'; 
        if(isShowAddBail != "yes"){
        	document.getElementById('addpubbail').style.display="none";
        }else{
        	document.getElementById('IqpBailSubDisDetail').style.display="none";
        	getBailNo();
    		IqpBailSubDis.PubBailInfo.adjust_amt._setValue(IqpBailSubDis.adjust_bail_amt._getValue());
        }
    };
	//-------------通过账号获取在我行的保证金信息------------
    function getBailNo(){
  		 var acctNo = IqpBailSubDis.PubBailInfo.bail_acct_no._getValue();
  	        if(acctNo == null || acctNo == ""){
  				alert("请先输入保证金账号信息！");
  				return;
  	        }
  			var handleSuccess = function(o){
  				if(o.responseText !== undefined){
  					try {
  						var jsonstr = eval("("+o.responseText+")");
  					} catch(e) {
  						alert("Parse jsonstr1 define error!" + e.message);
  						return;
  					}
  					var flag = jsonstr.flag;
  					var retMsg = jsonstr.mes;
  					if(flag == "success"){
  						var GUARANTEE_ACCT_NO = jsonstr.BODY.AcctNoCrdNo;
  	  					var GUARANTEE_ACCT_NAME = jsonstr.BODY.AcctNm;
  	  					var CCY = jsonstr.BODY.Ccy;
  	  					var AMT = jsonstr.BODY.AcctBal;
  	  					var GUARANTEE_TYPE = jsonstr.BODY.PdTp;
  	  					var INT_RATE = jsonstr.BODY.BnkInnrIntRate;
  	  					var INTER_FLT_RATE = jsonstr.BODY.FltIntRate;
  	  					var TERM = jsonstr.BODY.Trm;
  	  					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.AcctBlngInstNo;
  	  					var OPEN_ACCT_BRANCH_ID_displayname = jsonstr.BODY.AcctBlngInstNo_displayname;
  	  					var ACCT_STATUS = jsonstr.BODY.AcctSt;
  	  				    var BAIL_ACCT_GL_CODE = jsonstr.BODY.AcctSeqNo;//增加科目号
  	  				    IqpBailSubDis.PubBailInfo.bail_acct_name._setValue(GUARANTEE_ACCT_NAME);
  	  				    IqpBailSubDis.PubBailInfo.bail_acct_gl_code._setValue(BAIL_ACCT_GL_CODE);
  	  			        IqpBailSubDis.PubBailInfo.cur_type._setValue(CCY);
  	  		            IqpBailSubDis.PubBailInfo.amt._setValue(AMT);
  						IqpBailSubDis.PubBailInfo.bail_type._setValue(GUARANTEE_TYPE);
  						IqpBailSubDis.PubBailInfo.dep_term._setValue(TERM);
  						IqpBailSubDis.PubBailInfo.open_org._setValue(OPEN_ACCT_BRANCH_ID);
  						IqpBailSubDis.PubBailInfo.open_org_displayname._setValue(OPEN_ACCT_BRANCH_ID_displayname);
  						if(INT_RATE == ""){
  							IqpBailSubDis.PubBailInfo.rate._setValue("");
  	  					}else{
  	  					    IqpBailSubDis.PubBailInfo.rate._setValue(parseFloat(INT_RATE)/100);
  	  	  	  			}
  						if(INTER_FLT_RATE == "0.0"){
  							IqpBailSubDis.PubBailInfo.up_rate._obj.element.value=0.00;
  	  					}else if(INTER_FLT_RATE == ""){
  	  					     IqpBailSubDis.PubBailInfo.up_rate._setValue("");
  	  	  				}else{
  	  	  				     IqpBailSubDis.PubBailInfo.up_rate._setValue(parseFloat(INTER_FLT_RATE)/100);
  	  	  	  			}
  					}else {
  						alert(retMsg); 
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
  			var url = '<emp:url action="getPubBailInfoForEsb.do"/>?bail_acct_no='+acctNo+'&service_code=30130001&sence_code=01';	
  			url = EMPTools.encodeURI(url);
  			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
	<emp:form id="submitForm" action="addIqpBailSubDisRecord.do" method="POST">
		<emp:gridLayout id="IqpBailSubDisGroup" title="保证金追加申请" maxColumn="2">
			<emp:text id="IqpBailSubDis.serno" label="业务编号" maxlength="60" required="false" readonly="true" hidden="false"/>
			<emp:text id="IqpBailSubDis.cont_no" label="合同编号" maxlength="60" required="true" readonly="true"/>
			<emp:text id="IqpBailSubDis.cus_id" label="客户码" maxlength="60" required="false" readonly="true"/>
			<emp:text id="IqpBailSubDis.cus_id_displayname" label="客户名称" required="false" cssElementClass="emp_field_text_readonly"/>
			
			<emp:select id="IqpBailSubDis.cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" required="false" readonly="true" hidden="false"/>
			<emp:text id="IqpBailSubDis.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true" hidden="false"/>
			<emp:text id="IqpBailSubDis.cont_amt" label="合同金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.cont_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:select id="IqpBailSubDis.security_cur_type" label="原保证金币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpBailSubDis.security_exchange_rate" label="原保证金汇率"  maxlength="16" readonly="true" required="false" />
			<emp:text id="IqpBailSubDis.ori_bail_perc" label="原保证金比例" maxlength="20" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.ori_bail_amt" label="原保证金金额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.security_rmb_amt" label="原保证金折算人民币金额" maxlength="18" readonly="true" required="false" colSpan="2" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			
			<emp:text id="IqpBailSubDis.adjusted_bail_perc" label="追加/提取后保证金比例" maxlength="20" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <!-- added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start-->
		    <emp:text id="IqpBailSubDis.adj_security_exchange_rate" label="追加/提取后保证金汇率"  maxlength="16" readonly="true" required="false" />
		    <!-- added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start-->
		    <emp:text id="IqpBailSubDis.adjusted_bail_amt" label="追加/提取后保证金金额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.adjust_bail_amt" label="追加/提取保证金金额" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>			
			
			<emp:text id="IqpBailSubDis.flag" label="申请类型（1--追加，2--提取）" maxlength="6" required="false" hidden="true"/>
			<emp:select id="IqpBailSubDis.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" maxColumn="2" title="登记信息">
			<emp:pop id="IqpBailSubDis.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpBailSubDis.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpBailSubDis.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName"/>
			<emp:text id="IqpBailSubDis.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName"/>
			<emp:text id="IqpBailSubDis.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			<emp:text id="IqpBailSubDis.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="IqpBailSubDis.manager_id" label="责任人" required="true" readonly="false" hidden="true"/>
			<emp:text id="IqpBailSubDis.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpBailSubDis.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
	</emp:form>
	   <div id=IqpBailSubDisDetail>
		<div class='emp_gridlayout_title'>追加明细</div><br>
		<emp:table icollName="IqpBailSubDisDetailList" pageMode="false" editable="false" url="" >
			<emp:text id="optType" label="操作方式" hidden="true" />
			<emp:text id="serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="false"/>
			<emp:text id="cont_no" label="合同编号" required="false" readonly="true"/>
			<emp:text id="bail_acct_no" label="保证金账号" maxlength="60" required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:text id="origi_bail_bal" label="原保证金金额 " required="false"  maxlength="18" readonly="true" dataType="Currency" />
			<emp:text id="adjust_amt" label="追加/提取金额 " required="true" readonly="false" maxlength="18" dataType="Currency" defvalue="0" onchange="onChange()"/>
		</emp:table>
		</div>
		<div id='addpubbail'>
		   <emp:gridLayout id="PubBailInfo" maxColumn="2" title="新增保证金">
				<emp:text id="IqpBailSubDis.PubBailInfo.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.cus_id" label="客户码" maxlength="40" required="false" defvalue="${context.cus_id}" hidden="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_no" label="保证金账号" maxlength="40" required="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" readonly="true" defvalue="123"/>
				<emp:select id="IqpBailSubDis.PubBailInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.amt" label="账户余额"  required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
				<emp:text id="IqpBailSubDis.PubBailInfo.rate" label="利率" maxlength="10" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly" />			
				<emp:text id="IqpBailSubDis.PubBailInfo.up_rate" label="上浮比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly" />
				<emp:select id="IqpBailSubDis.PubBailInfo.bail_type" label="保证金类型" required="false" dictname="STD_PUB_BAIL_TYPE" readonly="true"/>
				<emp:select id="IqpBailSubDis.PubBailInfo.dep_term" label="存期"  required="false" dictname="STD_BAIL_DEP_TERM" readonly="true" />
				<emp:text id="IqpBailSubDis.PubBailInfo.open_org_displayname" label="开户机构" required="false" readonly="true" />
				<emp:select id="IqpBailSubDis.PubBailInfo.bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS" readonly="true" required="false" defvalue="0" />
				<emp:text id="IqpBailSubDis.PubBailInfo.adjust_amt" label="追加金额 " required="true" readonly="true" dataType="Currency" maxlength="18"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/> 
		   	 	<emp:text id="IqpBailSubDis.PubBailInfo.open_org" label="开户机构" required="false" hidden="true"/>
		   	 	<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
		</emp:gridLayout>
		</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if(flow.equals("wf")){ %>
		<%}else{ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
