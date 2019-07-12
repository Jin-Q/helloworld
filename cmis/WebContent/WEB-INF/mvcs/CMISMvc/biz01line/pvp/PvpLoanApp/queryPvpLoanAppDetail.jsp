<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="jsPvpComm.jsp" flush="true" /> 
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String flag = "";
    String biz_type = "";
    String flow = "";
    String prd_id = "";
    if(context.containsKey("flag")){
    	flag = (String)context.getDataValue("flag");
    }
    if(context.containsKey("biz_type")){
    	biz_type = (String)context.getDataValue("biz_type");
    }
    if(context.containsKey("flow")){
    	flow = (String)context.getDataValue("flow");
    }
    if(context.containsKey("prd_id")){
    	prd_id = (String)context.getDataValue("prd_id");
    }
    /**add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
    String wf_flag="";
    if(context.containsKey("wf_flag")){
    	wf_flag = (String)context.getDataValue("wf_flag");
    }
    String modify_rel_serno="";
    if(context.containsKey("modify_rel_serno")){
    	modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
    }
    String dutyNoList ="";
    if(context.containsKey("dutyNoList")){
    	dutyNoList = (String)context.getDataValue("dutyNoList");
    }
    /**add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
    request.setAttribute("canwrite","");
%>
<emp:page> 
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>
<script type="text/javascript">
	function doReturn1() {
        if('<%=flag%>'=="pvpLoan"){
        	var url = '<emp:url action="queryPvpLoanAppList.do"/>?menuId=${context.menuId}&biz_type='+'<%=biz_type%>';  
        }else{
        	var url = '<emp:url action="queryPvpLoanAppHistoryList.do"/>?menuId=${context.menuId}&biz_type='+'<%=biz_type%>';
        }
		url = EMPTools.encodeURI(url);  
		window.location=url;
	};

	function doReturn() {
		window.close();
	};
	
	function doOnLoad(){
	    /*
		doChangLimitInt();//额度使用标识 
		changeRmbAmt();//获取折合人民币金额、保证金比例、风险敞口比例
		showPromissory();//是否承诺函下
		showTrust();//是否信托贷款
		showPayType();//支付方式
		changeFloatType();//利率浮动方式
		getRulMounth();//--加载利率--
		show_net();//显示所属网络
		controlBizType();//业务模式控制
		*/
		document.getElementById("base_tab").href="javascript:reLoad();";
	}
	function reLoad(){
		var url = '<emp:url action="getPvpLoanAppViewPage.do"/>?flow=${context.flow}&flag=${context.flag}&menuId=${context.menuId}&serno=${context.PvpLoanApp.serno}&op=update&biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
		//window.location.reload();
	}; 
	/*--user code begin--*/
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	/**add by lisj 2014年10月29日   关于新信贷系统增加第三方授信客户影像查看功能   begin**/
	function doImageViewByThirdCusId(cusId){
		var data = new Array();
		data['serno'] =  cusId;	//客户资料的业务编号就填cus_id
		data['cus_id'] = cusId;	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View24'	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
		
	 function doImageView4Third(){
		 var form = document.getElementById("queryForm"); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
				    try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					} 
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					var cusId = jsonstr.cusId;//关联第三方授信额度信息的客户码
					if(flag == "success"){
						doImageViewByThirdCusId(cusId);
					}else{
						alert(msg);
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
			form.action = "<emp:url action='queryThirdCusIdByContNo.do'/>";
			PvpLoanApp._toForm(form);    
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};
	 /**add by lisj 2014年10月29日   关于新信贷系统增加影像查看功能   end**/
	
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = PvpLoanApp.fount_serno._getValue();	//业务申请编号，不是出账流水号
		data['cus_id'] = PvpLoanApp.cus_id._getValue();	//客户码
		data['prd_id'] = PvpLoanApp.prd_id._getValue();	//业务品种
		data['prd_stage'] = "<%=flow%>" =='wf'?'CZSH':'CZSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	function doShowBasic(){
		var url = '<emp:url action="getIqpAccAccpShowBasicPage.do"/>?serno=${context.PvpLoanApp.fount_serno}&cont_no=${context.PvpLoanApp.cont_no}&cus_id=${context.PvpLoanApp.cus_id}';
		//url = EMP.util.Tools.encodeURI(url);
		//window.location = url;
		//url=encodeURI(url); 
      	//window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		url = EMPTools.encodeURI(url);  
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param); 
	}
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
		<form  method="POST" action="" id="queryForm">
	     </form>
		  <emp:tabGroup mainTab="base_tab" id="mainTab" >
		 <emp:tab label="出账信息" id="base_tab" needFlush="true" initial="true" >
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="出账基本信息">
			<emp:text id="PvpLoanApp.serno" label="出账流水号" maxlength="40" required="true" readonly="true" hidden="false" colSpan="2"/>	
			<emp:text id="PvpLoanApp.prd_id" label="产品编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.prd_id_displayname" label="产品名称"   required="false" readonly="true"/>
			<%if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
			<emp:text id="PvpLoanApp.cus_id" label="对手行行号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.toorg_name" label="对手行行名称" maxlength="80" required="false" colSpan="2" readonly="true" cssElementClass="emp_input2"/>
			<emp:text id="PvpLoanApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<%}else{ %>
			<emp:text id="PvpLoanApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cus_id_displayname" label="客户名称"   required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="PvpLoanApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cn_cont_no" label="中文合同编号" maxlength="100" required="false" readonly="true"/>
			<%}%>
			<emp:text id="PvpLoanApp.bill_no" label="借据编号" maxlength="40" hidden="true" required="false" />
			<emp:date id="PvpLoanApp.first_pay_date" label="首次还款日" required="true" hidden="false"/>	
		  </emp:gridLayout>
          
          <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:text id="PvpLoanApp.cont_amt" label="合同金额" cssElementClass="emp_currency_text_readonly" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    <emp:select id="PvpLoanApp.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="PvpLoanApp.cont_balance" cssElementClass="emp_currency_text_readonly" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
		    <emp:text id="PvpLoanApp.pvp_amt" cssElementClass="emp_currency_text_readonly" label="出账金额" maxlength="18" required="true"  dataType="Currency"/>
		</emp:gridLayout>
				    
	  <emp:gridLayout id="" maxColumn="3" title="登记信息">   
	  	<emp:text id="PvpLoanApp.manager_br_id_displayname" label="管理机构"   required="false" readonly="true"/>
	    <emp:pop id="PvpLoanApp.in_acct_br_id_displayname" label="入账机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" required="true"/>
	    <emp:select id="PvpLoanApp.flow_type" label="流程类型" dictname="STD_ZB_FLOW_TYPE" defvalue="01" required="false"/>    
	    <emp:text id="PvpLoanApp.input_id_displayname" label="登记人"   required="false" readonly="true"/>
		<emp:text id="PvpLoanApp.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
		<emp:date id="PvpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
		<emp:select id="PvpLoanApp.approve_status" label="出账状态" required="false" hidden="true" dictname="STD_ZB_PVP_STATUS" defvalue="1"/>
	    <emp:text id="PvpLoanApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
	    <emp:pop id="PvpLoanApp.in_acct_br_id" label="入账机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" required="true"/>
	     <emp:text id="PvpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false" readonly="true"/>
		<emp:text id="PvpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false" readonly="true"/>
		<emp:text id="PvpLoanApp.fount_serno" label="业务编号" hidden="true" />
	 </emp:gridLayout>
	 	<div align="center">      
		  <br>
		  <%if(!"wf".equals(flow)){%>
		  	<emp:button id="return" label="返回到列表页面"/>
		  <%}else{%>
		  	<emp:button id="ShowBasic" label="基本信息展示"/> 
		  <%}%>
		  <emp:button id="ImageView" label="影像查看"/>
		  <emp:button id="ImageView4Third" label="第三方影像查看"/>
	    </div>
		</emp:tab>
		<%if("600020".equals(prd_id)){ %>
	    <emp:tab label="资产转受让合同信息" id="assetContSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrAssetstrsfContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
		<emp:tab label="转贴现合同信息" id="rpContsubTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrRpddscntContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else if("8".equals(biz_type) && (prd_id.equals("300021")||prd_id.equals("300020"))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("8".equals(biz_type) && !prd_id.equals("300021") && !prd_id.equals("300020")){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if(prd_id.equals("300021")||prd_id.equals("300020")){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else{%>
	     <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}%>
		<%if(flag.equals("pvpLoanHistory")){ %>
		<emp:tab label="出账审批历史意见" id="pvpFlowHisTab" url="getIqpFlowHis.do" reqParams="instanceId=${context.instanceIdPvp}" needFlush="true"></emp:tab> 
	    <%}%>
	    <!-- add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
	    <%if(((dutyNoList.indexOf("S0118")>=0) || (dutyNoList.indexOf("S0200")>=0) ||(dutyNoList.indexOf("S0226")>=0))&&!"".equals(modify_rel_serno)){ %>
			<emp:tab id="CompareHis" label="与上一次修改值对比" url="getModifyHisViewPage.do" reqParams="cont_no=${context.PvpLoanApp.cont_no}&prd_id=${context.PvpLoanApp.prd_id}&modify_rel_serno=${context.modify_rel_serno}&op=his&wf_flag=${context.wf_flag}"/>
		<%} %>
		<!-- add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
	</emp:tabGroup>  
	

</body>
</html>
</emp:page>
