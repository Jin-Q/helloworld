<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doOnLoad(){
		isUseLimt();
		checkTakeoverType();
		checkInterestType();
	}
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAssetstrsfList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	// 根据转让方式显示不同的字段
	function checkTakeoverType(){
		var takeover_type = IqpAssetstrsf.takeover_type._getValue();
		//'01':'转出卖断式', '02':'转出回购式', '03':'转入买断式', '04':'转入回购式'
		//转出买断式业务展示 本行账户、本行账户名、开户行行号、开户行行名
        if(takeover_type == "01"){
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(false);//开户行名
        	//IqpAssetstrsf.tooorg_city._obj._renderHidden(true);//交易对手所在城市
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(true);//交易对手开户行行名
        	IqpAssetstrsf.topp_acct_no._setValue('');//交易对手账号
        	IqpAssetstrsf.topp_acct_name._setValue('');;//交易对手户名
        	IqpAssetstrsf.tooorg_no._setValue('');;//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._setValue('');;//交易对手开户行行名
        	
        	IqpAssetstrsf.this_acct_no._obj._renderRequired(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(true);//开户行名
        	//IqpAssetstrsf.tooorg_city._obj._renderRequired(false);//交易对手所在城市
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(false);//交易对手开户行行名

        //转入买断式业务展示 交易对手账号、交易对手户名、交易对手开户行行号、交易对手开户行行名    
        }else if(takeover_type == "03"){
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(true);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(false);//交易对手开户行行名
        	//IqpAssetstrsf.tooorg_city._obj._renderHidden(false);//交易对手所在城市
        	IqpAssetstrsf.this_acct_no._setValue('');//本行账户
        	IqpAssetstrsf.this_acct_name._setValue('');//本行账户名
        	IqpAssetstrsf.acctsvcr_no._setValue('');//开户行号
        	IqpAssetstrsf.acctsvcr_name._setValue('');//开户行名
        	
        	IqpAssetstrsf.this_acct_no._obj._renderRequired(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(false);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        	//IqpAssetstrsf.tooorg_city._obj._renderRequired(true);//交易对手所在城市
        }else{
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(false);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(false);//交易对手开户行行名

        	IqpAssetstrsf.this_acct_no._obj._renderRequired(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(true);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        }
	};
	/*--user code begin--*/
	function isUseLimt(){
        var limit_ind = IqpAssetstrsf.limit_ind._getValue();
        if(limit_ind=='1'||limit_ind==''){
        	IqpAssetstrsf.limit_acc_no._obj._renderHidden(true);
        	IqpAssetstrsf.limit_acc_no._obj._renderRequired(false);

        	IqpAssetstrsf.limit_acc_no._obj.config.url='';
        }else{
        	IqpAssetstrsf.limit_acc_no._obj._renderHidden(false);
        	IqpAssetstrsf.limit_acc_no._obj._renderRequired(true);

        	var cus_id = IqpAssetstrsf.toorg_no._getValue();
        	var lmt_type = "02";//02-同业客户
        	var prd_id = IqpAssetstrsf.prd_id._getValue();
        	var outstnd_amt = IqpAssetstrsf.takeover_total_amt._getValue();
        	IqpAssetstrsf.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
        }
    }

    function getLmtAmt(data){
    	var lmtContNo = data[0];//授信协议编号
    	var lmtAmt = data[1];//授信余额暂时先去授信金额
    	IqpAssetstrsf.limit_acc_no._setValue(lmtContNo);
    };
    function checkInterestType(){
    	var interest_type = IqpAssetstrsf.interest_type._getValue();
    	if(interest_type =="1"){//自主 
    		IqpAssetstrsf.trust_rate._obj._renderHidden(true);
    		IqpAssetstrsf.trust_rate._obj._renderRequired(false);
        }else{
        	IqpAssetstrsf.trust_rate._obj._renderHidden(false);
    		IqpAssetstrsf.trust_rate._obj._renderRequired(true);
        }
    };

    /*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpAssetstrsf.serno._getValue();	//业务编号
		data['cus_id'] = IqpAssetstrsf.toorg_no._getValue();	//客户码
		data['prd_id'] = IqpAssetstrsf.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
</style>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:tabGroup mainTab="mainTab" id="mainTab">
   <emp:tab label="基本信息" id="mainTab">
		<emp:gridLayout id="IqpAssetstrsfGroup" title="基本信息" maxColumn="2">
					<emp:text id="IqpAssetstrsf.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
					<emp:pop id="IqpAssetstrsf.asset_no" label="资产包编号" url="queryIqpAssetMngListPop.do?returnMethod=getAssetMsg" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
					<emp:text id="IqpAssetstrsf.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产转受让" readonly="true"/>
					<emp:select id="IqpAssetstrsf.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE" colSpan="2" readonly="true"/>
					<emp:text id="IqpAssetstrsf.tooorg_city" label="交易对手所在城市" maxlength="50" required="true" colSpan="2"/>
					<emp:pop id="IqpAssetstrsf.toorg_no" label="交易对手行号" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="true" buttonLabel="选择" />
					<emp:text id="IqpAssetstrsf.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.topp_acct_no" label="交易对手账户" maxlength="40" dataType="Acct" required="false" cssElementClass="emp_field_input"/>
					<emp:text id="IqpAssetstrsf.topp_acct_name" label="交易对手户名" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long"/>
					<emp:pop id="IqpAssetstrsf.tooorg_no" label="交易对手开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getTooorgNo" required="true" buttonLabel="选择" />
					<emp:text id="IqpAssetstrsf.tooorg_name" label="交易对手开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.this_acct_no" label="本行账户" maxlength="40" dataType="Acct" required="true" cssElementClass="emp_field_input"/> 
					<emp:text id="IqpAssetstrsf.this_acct_name" label="本行账户名" maxlength="100" required="true" />
					<emp:pop id="IqpAssetstrsf.acctsvcr_no" label="开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getAcctsvcrNo" required="true" buttonLabel="选择" />
					<emp:text id="IqpAssetstrsf.acctsvcr_name" label="开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.takeover_qnt" label="转让笔数" maxlength="38" required="false" dataType="Int" readonly="true"/>
					<emp:select id="IqpAssetstrsf.acct_curr" label="转让币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
					<emp:text id="IqpAssetstrsf.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpAssetstrsf.takeover_total_amt" label="转让金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpAssetstrsf.takeover_int" label="转让利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:date id="IqpAssetstrsf.takeover_date" label="转让日期" required="true" />
					<emp:select id="IqpAssetstrsf.interest_type" label="收息方式" required="true" dictname="STD_RCV_INT_TYPE"/>
					<emp:text id="IqpAssetstrsf.trust_rate" label="委托费率" maxlength="10" required="true" dataType="Rate" />
					<emp:textarea id="IqpAssetstrsf.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
				    <emp:select id="IqpAssetstrsf.is_risk_takeover" label="风险是否转移" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="额度信息">
					<emp:select id="IqpAssetstrsf.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    		<emp:pop id="IqpAssetstrsf.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
		    		<emp:text id="remain_amount" label="剩余额度" maxlength="18" hidden="true" required="false" dataType="Currency" readonly="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="3" title="登记信息">
					<emp:pop id="IqpAssetstrsf.manager_br_id_displayname" label="管理机构" required="false"  buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
		    		<emp:select id="IqpAssetstrsf.flow_type" label="流程类型"  required="false" dictname="STD_ZB_FLOW_TYPE" />
		    		<emp:select id="IqpAssetstrsf.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" hidden="true" required="false" />
		   			<emp:text id="IqpAssetstrsf.input_id_displayname" label="登记人" required="false"  readonly="true" />
					<emp:text id="IqpAssetstrsf.input_br_id_displayname" label="登记机构" required="false"  readonly="true" />
			
					<emp:date id="IqpAssetstrsf.input_date" label="登记日期" required="false" readonly="true" />
					<emp:text id="IqpAssetstrsf.manager_br_id" label="管理机构" hidden="true" />
					<emp:text id="IqpAssetstrsf.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" />
					<emp:text id="IqpAssetstrsf.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" />
				</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"notHave".equals(flag)){%>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
   </emp:tab>
   <emp:ExtActTab></emp:ExtActTab>
  </emp:tabGroup>	
</body>
</html>
</emp:page>
