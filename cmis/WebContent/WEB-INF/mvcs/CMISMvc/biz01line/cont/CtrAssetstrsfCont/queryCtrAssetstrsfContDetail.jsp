<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String pvp = "";
	if(context.containsKey("pvp")){
		pvp = (String)context.getDataValue("pvp");
	}
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	String viewtype="";
	if(context.containsKey("viewtype")){
		viewtype = (String)context.getDataValue("viewtype");
	}
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
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
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
</style>
<script type="text/javascript">
	function doOnLoad(){
		isUseLimt();
		checkTakeoverType();
		checkInterestType();
	}
	// 根据转让方式显示不同的字段
	function checkTakeoverType(){
		var takeover_type = CtrAssetstrsfCont.takeover_type._getValue();
		//转出买断式业务展示 本行账户、本行账户名、开户行行号、开户行行名
		if(takeover_type == "01"){
        	CtrAssetstrsfCont.this_acct_no._obj._renderHidden(false);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderHidden(false);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderHidden(false);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderHidden(false);//开户行名
        	//CtrAssetstrsfCont.tooorg_city._obj._renderHidden(true);//交易对手所在城市
        	CtrAssetstrsfCont.topp_acct_no._obj._renderHidden(true);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderHidden(true);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderHidden(true);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderHidden(true);//交易对手开户行行名
        	CtrAssetstrsfCont.topp_acct_no._setValue('');//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._setValue('');;//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._setValue('');;//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._setValue('');;//交易对手开户行行名
        	
        	CtrAssetstrsfCont.this_acct_no._obj._renderRequired(true);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderRequired(true);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderRequired(true);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderRequired(true);//开户行名
        	//CtrAssetstrsfCont.tooorg_city._obj._renderRequired(false);//交易对手所在城市
        	CtrAssetstrsfCont.topp_acct_no._obj._renderRequired(false);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderRequired(false);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderRequired(false);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderRequired(false);//交易对手开户行行名

        //转入买断式业务展示 交易对手账号、交易对手户名、交易对手开户行行号、交易对手开户行行名    
        }else if(takeover_type == "03"){
        	CtrAssetstrsfCont.this_acct_no._obj._renderHidden(true);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderHidden(true);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderHidden(true);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderHidden(true);//开户行名
        	CtrAssetstrsfCont.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderHidden(false);//交易对手开户行行名
        	//CtrAssetstrsfCont.tooorg_city._obj._renderHidden(false);//交易对手所在城市
        	CtrAssetstrsfCont.this_acct_no._setValue('');//本行账户
	        CtrAssetstrsfCont.this_acct_name._setValue('');//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._setValue('');//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._setValue('');//开户行名

        	CtrAssetstrsfCont.this_acct_no._obj._renderRequired(false);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderRequired(false);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderRequired(false);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderRequired(false);//开户行名
        	CtrAssetstrsfCont.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        	//CtrAssetstrsfCont.tooorg_city._obj._renderRequired(true);//交易对手所在城市
        }else{
        	CtrAssetstrsfCont.this_acct_no._obj._renderHidden(false);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderHidden(false);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderHidden(false);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderHidden(false);//开户行名
        	CtrAssetstrsfCont.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderHidden(false);//交易对手开户行行名

        	CtrAssetstrsfCont.this_acct_no._obj._renderRequired(true);//本行账户
        	CtrAssetstrsfCont.this_acct_name._obj._renderRequired(true);//本行账户名
        	CtrAssetstrsfCont.acctsvcr_no._obj._renderRequired(true);//开户行号
        	CtrAssetstrsfCont.acctsvcr_name._obj._renderRequired(true);//开户行名
        	CtrAssetstrsfCont.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	CtrAssetstrsfCont.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	CtrAssetstrsfCont.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	CtrAssetstrsfCont.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        }
		};
	function doReturn() {
		var url = '<emp:url action="queryCtrAssetstrsfContList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function doReturn1() {
		window.close();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	function isUseLimt(){
        var limit_ind = CtrAssetstrsfCont.limit_ind._getValue();
        if(limit_ind=='1'||limit_ind==''){
        	CtrAssetstrsfCont.limit_acc_no._obj._renderHidden(true);
        	//remain_amount._obj._renderHidden(true);
        	CtrAssetstrsfCont.limit_acc_no._obj._renderRequired(false);
        	//remain_amount._obj._renderRequired(false);

        	CtrAssetstrsfCont.limit_acc_no._obj.config.url='';
        }else{
        	CtrAssetstrsfCont.limit_acc_no._obj._renderHidden(false);
        	//remain_amount._obj._renderHidden(false);
        	CtrAssetstrsfCont.limit_acc_no._obj._renderRequired(true);
        	//remain_amount._obj._renderRequired(true);

        	var cus_id = CtrAssetstrsfCont.toorg_no._getValue();
        	var lmt_type = "02";//02-同业客户
        	var prd_id = CtrAssetstrsfCont.prd_id._getValue();
        	var outstnd_amt = CtrAssetstrsfCont.takeover_total_amt._getValue();
        	CtrAssetstrsfCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
        }
    };
  //收息方式展示委托费率
	function checkInterestType(){
    	var interest_type = CtrAssetstrsfCont.interest_type._getValue();
    	if(interest_type =="1"){//自主 
    		CtrAssetstrsfCont.trust_rate._obj._renderHidden(true);
    		CtrAssetstrsfCont.trust_rate._obj._renderRequired(false);
        }else{
        	CtrAssetstrsfCont.trust_rate._obj._renderHidden(false);
        	CtrAssetstrsfCont.trust_rate._obj._renderRequired(true);
        }
    };
	
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrAssetstrsfCont.serno._getValue();	//业务编号
		data['cus_id'] = CtrAssetstrsfCont.toorg_no._getValue();	//客户码
		data['prd_id'] = CtrAssetstrsfCont.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code begin--*/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:tabGroup mainTab="mainTab" id="mainTab">
   <emp:tab label="基本信息" id="mainTab">
	<emp:gridLayout id="CtrAssetstrsfContGroup" maxColumn="2" title="资产转受让合同">
		  <emp:text id="CtrAssetstrsfCont.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.cn_cont_no" label="中文合同编号" maxlength="100" required="true"  />
		  <emp:text id="CtrAssetstrsfCont.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
		  <emp:pop id="CtrAssetstrsfCont.asset_no" label="资产包编号" url="queryIqpAssetMngListPop.do?returnMethod=getAssetMsg" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		  <emp:text id="CtrAssetstrsfCont.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产转受让" readonly="true"/>
		  <emp:select id="CtrAssetstrsfCont.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE" colSpan="2" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.tooorg_city" label="交易对手所在城市" maxlength="50" required="false" readonly="true" colSpan="2"/>
		  <emp:pop id="CtrAssetstrsfCont.toorg_no" label="交易对手行号" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="true" buttonLabel="选择" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		  <emp:text id="CtrAssetstrsfCont.topp_acct_no" label="交易对手账户" maxlength="40" dataType="Acct" readonly="true" required="false" cssElementClass="emp_field_input"/>
		  <emp:text id="CtrAssetstrsfCont.topp_acct_name" label="交易对手户名" maxlength="40" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long"/>
		  <emp:pop id="CtrAssetstrsfCont.tooorg_no" label="交易对手开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getTooorgNo" required="true" buttonLabel="选择" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
	      <emp:text id="CtrAssetstrsfCont.this_acct_no" label="本行账户" maxlength="40" required="false" readonly="true"/>
	  	  <emp:text id="CtrAssetstrsfCont.this_acct_name" label="本行账户名" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		  <emp:pop id="CtrAssetstrsfCont.acctsvcr_no" label="开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getAcctsvcrNo" required="true" buttonLabel="选择" readonly="true"/>
	  	  <emp:text id="CtrAssetstrsfCont.acctsvcr_name" label="开户行行名" maxlength="100" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		  <emp:text id="CtrAssetstrsfCont.takeover_qnt" label="转让笔数" maxlength="38" required="false" dataType="Int" readonly="true"/>
		  <emp:select id="CtrAssetstrsfCont.acct_curr" label="转让币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.takeover_total_amt" label="转让金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		  <emp:date id="CtrAssetstrsfCont.takeover_date" label="转让日期" required="false" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.takeover_int" label="转让利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		  <emp:select id="CtrAssetstrsfCont.interest_type" label="收息方式" required="false" dictname="STD_RCV_INT_TYPE" readonly="true"/>
		  <emp:text id="CtrAssetstrsfCont.trust_rate" label="委托费率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
		  <emp:select id="CtrAssetstrsfCont.is_risk_takeover" label="风险是否转移" required="false" dictname="STD_ZX_YES_NO" readonly="true" hidden="true"/>
		  <emp:textarea id="CtrAssetstrsfCont.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="CtrAssetstrsfCont.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    <emp:pop id="CtrAssetstrsfCont.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">   
		   <emp:text id="CtrAssetstrsfCont.manager_br_id_displayname" label="管理机构"  required="false" readonly="true"/>
		    	<emp:text id="CtrAssetstrsfCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
				<emp:select id="CtrAssetstrsfCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" required="false" hidden="true"/>
				<emp:date id="CtrAssetstrsfCont.input_date" label="登记日期" required="false"  readonly="true"/>
				<emp:text id="CtrAssetstrsfCont.input_id_displayname" label="登记人"  required="false"  readonly="true"/>
		    	<emp:text id="CtrAssetstrsfCont.input_br_id_displayname" label="登记机构"  required="false"  readonly="true"/>
		     	<emp:text id="CtrAssetstrsfCont.input_id" label="登记人" maxlength="20" hidden="true" required="false"  readonly="true"/>
		    	<emp:text id="CtrAssetstrsfCont.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false"  readonly="true"/>
		  </emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!pvp.equals("pvp")&&!"out".equals(viewtype)){ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%}%>
		<%if(!"pvp".equals(pvp)&&"out".equals(viewtype)){ %>
		    <emp:button id="return1" label="关闭"/>
		<%}%>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
   </emp:tab>
   
     <emp:ExtActTab></emp:ExtActTab>
     
 </emp:tabGroup>	
</body>
</html>
</emp:page>
