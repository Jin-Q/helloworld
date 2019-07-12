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

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript">
</script>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		history.go(-1);
	};
	
	/*--user code begin--*/
	function doOnLoad(){
		setReadonly();
		isUseLimt();
		var rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
		var rpay_amt = IqpRpddscnt.rpay_amt._getValue();
		 if(rpddscnt_type == "03" || rpddscnt_type == "04"){
         	IqpRpddscnt.rpay_amt_2._obj._renderHidden(false);
         	IqpRpddscnt.rpay_amt_2._setValue(rpay_amt);
         	IqpRpddscnt.rpay_amt_1._obj._renderHidden(true);
         }else{
         	IqpRpddscnt.rpay_amt_1._obj._renderHidden(false);
         	IqpRpddscnt.rpay_amt_1._setValue(rpay_amt);
         	IqpRpddscnt.rpay_amt_2._obj._renderHidden(true);
         }
		 cleanLimitInd();
	}
	function cleanLimitInd(){
    	var rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
    	//银票贴现,商票贴现 2014-03-15wangs添加
    	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
    	var bill_type = IqpRpddscnt.bill_type._getValue();
    	if(bill_type=='100'){//仅银票需控制
    		if(rpddscnt_type == "01"){//'01':'买入买断',
        		var limitIndOptions = IqpRpddscnt.limit_ind._obj.element.options;
        		for(var i=limitIndOptions.length-1;i>=0;i--){	
        			if(limitIndOptions[i].value=="4" || limitIndOptions[i].value=="5" || limitIndOptions[i].value=="6"){//
        				limitIndOptions.remove(i);
        			}
        		}
        		var varOption = new Option('使用承兑人额度','4');
        		limitIndOptions.add(varOption);
        		IqpRpddscnt.limit_ind._setValue("4");
        		IqpRpddscnt.limit_ind._obj._renderReadonly(true);
        	}
    	}else if(bill_type=='200'){//商票需控制
        	if(rpddscnt_type == "01" || rpddscnt_type == "02"){//'01':'买入买断', '02':'买入返售'
        		var limitIndOptions = IqpRpddscnt.limit_ind._obj.element.options;
        		for(var i=limitIndOptions.length-1;i>=0;i--){	
        			if(limitIndOptions[i].value=="4"){//
        				limitIndOptions.remove(i);
        			}
        		}
        		var varOption = new Option('使用第三方额度','4');
        		limitIndOptions.add(varOption);
        	}
        }
    };
	//设置页面要素只读
    function setReadonly(){
    	IqpRpddscnt.rpddscnt_type._obj._renderReadonly(true);
		IqpRpddscnt.bill_type._obj._renderReadonly(true);
		IqpRpddscnt.bill_curr._obj._renderReadonly(true);
		IqpRpddscnt.bill_qnt._obj._renderReadonly(true);
		IqpRpddscnt.bill_total_amt._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_date._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_rate._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_int._obj._renderReadonly(true);
		IqpRpddscnt.rpay_amt._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_date._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_rate._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_int._obj._renderReadonly(true);
		IqpRpddscnt.approve_status._obj._renderReadonly(true);
        if("04" == IqpRpddscnt.rpddscnt_type._getValue()||"02" == IqpRpddscnt.rpddscnt_type._getValue()){//如果转贴现方式为 卖出回购、买入返售
        	//非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	IqpRpddscnt.rebuy_date._obj._renderHidden(false);
        	IqpRpddscnt.rebuy_rate._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_int._obj._renderHidden(true);
        	$(".emp_field_label:eq(11)").text("总回购利息");
        	$(".emp_field_label:eq(15)").text("回购起始日");
			$(".emp_field_label:eq(14)").text("回购利率");
        }else{
            //非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	IqpRpddscnt.rebuy_date._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_rate._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_int._obj._renderHidden(true);
        	$(".emp_field_label:eq(11)").text("总贴现利息");
        	$(".emp_field_label:eq(15)").text("转贴现日期");
			$(".emp_field_label:eq(14)").text("转贴现利率");
        }
    }

    function isUseLimt(){
    	var prd_id = IqpRpddscnt.prd_id._getValue();
        var limit_ind = IqpRpddscnt.limit_ind._getValue();
        if(limit_ind=='1'||limit_ind==''){
        	IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
        	IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
        	IqpRpddscnt.limit_acc_no._obj.config.url='';
        	IqpRpddscnt.limit_credit_no._obj.config.url='';
        }else if(limit_ind=='4'){
        	var rpddscnt_type = '';
            var bill_type = IqpRpddscnt.bill_type._getValue();
        	if(prd_id=='300023' && bill_type=='100'){//外部转贴现 ,银票需控制
    			rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
    		}
        	IqpRpddscnt.limit_credit_no._obj._renderHidden(false);
        	IqpRpddscnt.limit_credit_no._obj._renderRequired(true);
        	
        	IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
        	if(rpddscnt_type == "01"){
        		IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
        		IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
            	
        		IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
        		IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
            }
        }else{
        	IqpRpddscnt.limit_acc_no._obj._renderHidden(false);
        	IqpRpddscnt.limit_acc_no._obj._renderRequired(true);
        	IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
        	var cus_id = IqpRpddscnt.toorg_no._getValue();
        	var lmt_type = "02";//02-同业客户
        	var prd_id = IqpRpddscnt.prd_id._getValue();
        	var outstnd_amt = IqpRpddscnt.rpay_amt._getValue();
        	IqpRpddscnt.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
        }
    }

    function getLmtAmt(data){
    	var lmtContNo = data[0];//授信协议编号
    	var lmtAmt = data[1];//授信余额暂时先去授信金额
    	IqpRpddscnt.limit_acc_no._setValue(lmtContNo);
    	//remain_amount._setValue(lmtAmt+"");//剩余额度
    }

    /*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpRpddscnt.serno._getValue();	//业务编号
		data['cus_id'] = IqpRpddscnt.toorg_no._getValue();	//客户码
		data['prd_id'] = IqpRpddscnt.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
   <emp:tabGroup mainTab="mainTab" id="mainTab">
   <emp:tab label="基本信息" id="mainTab">
	<emp:gridLayout id="IqpRpddscntGroup" title="转贴现申请信息" maxColumn="2">
					<emp:text id="IqpRpddscnt.serno" label="业务编号" maxlength="40" readonly="true" required="false" />
					<emp:text id="IqpRpddscnt.batch_no" label="批次号" readonly="true" required="true" />
					<emp:text id="IqpRpddscnt.prd_id" label="产品编码" maxlength="10" required="false" readonly="true"/>
					<emp:text id="IqpRpddscnt.prd_id_displayname" label="产品名称" readonly="true" />
					<emp:select id="IqpRpddscnt.rpddscnt_type" label="转贴现方式" required="false" dictname="STD_ZB_BUSI_TYPE" colSpan="2"/>
					<emp:text id="IqpRpddscnt.toorg_no" label="交易对手行号"  readonly="true"  required="true"  />
					<emp:text id="IqpRpddscnt.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				</emp:gridLayout>
				   <emp:text id="IqpRpddscnt.topp_acct_no" label="交易对手账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" hidden="true"/>
			        <emp:text id="IqpRpddscnt.topp_acct_name" label="交易对手户名" maxlength="40" required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.tooorg_no" label="交易对手开户行行号"  required="false"  readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" readonly="true" hidden="true"/>
			
			        <emp:text id="IqpRpddscnt.this_acct_no" label="本行账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" hidden="true"/>
			        <emp:text id="IqpRpddscnt.this_acct_name" label="本行账户名" maxlength="100" required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.acctsvcr_no" label="本行开户行行号"  required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.acctsvcr_name" label="本行开户行行名" maxlength="100" required="false" readonly="true" hidden="true"/>
				
				<emp:gridLayout id="IqpRpddscntGroup" title="票据信息" maxColumn="2">
					<emp:select id="IqpRpddscnt.bill_type" label="票据种类" required="false"  dictname="STD_DRFT_TYPE" readonly="true"/>
					<emp:select id="IqpRpddscnt.bill_curr" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
					<emp:text id="IqpRpddscnt.bill_total_amt" label="票据总金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpRpddscnt.bill_qnt" label="票据数量" maxlength="38" required="false" readonly="true"/>
					<emp:text id="IqpRpddscnt.rpddscnt_int" label="总贴现利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpRpddscnt.rpay_amt_1" label="总实付金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
		        	<emp:text id="IqpRpddscnt.rpay_amt_2" label="总实收金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpRpddscnt.rpddscnt_rate" label="转贴现利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
					<emp:date id="IqpRpddscnt.rpddscnt_date" label="转贴现日期" required="false" readonly="true"/>
					<emp:date id="IqpRpddscnt.rebuy_date" label="回购日期" required="false" readonly="true"/>
					<emp:text id="IqpRpddscnt.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
					<emp:text id="IqpRpddscnt.rebuy_int" label="总回购利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:textarea id="IqpRpddscnt.memo" label="备注" maxlength="250" required="false" colSpan="2" />
				    <emp:text id="IqpRpddscnt.rpay_amt" label="总实付金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="额度信息">
					<emp:select id="IqpRpddscnt.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    		<emp:pop id="IqpRpddscnt.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
				    <emp:pop id="IqpRpddscnt.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="3" title="登记信息">
					<emp:pop id="IqpRpddscnt.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
		    		<emp:select id="IqpRpddscnt.flow_type" label="流程类型"  required="false" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
		    		<emp:select id="IqpRpddscnt.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" defvalue="000"/>
		   			<emp:text id="IqpRpddscnt.input_id_displayname" label="登记人"   required="false"  readonly="true" />
					<emp:text id="IqpRpddscnt.input_br_id_displayname" label="登记机构"   required="false"  readonly="true" />
			
					<emp:date id="IqpRpddscnt.input_date" label="登记日期" required="false" readonly="true" />
					<emp:text id="IqpRpddscnt.manager_br_id" label="管理机构" hidden="true" />
					<emp:text id="IqpRpddscnt.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" />
					<emp:text id="IqpRpddscnt.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" />
				</emp:gridLayout>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
   </emp:tabGroup>
	<div align="center">
		<br>
		<%if(!"notHave".equals(flag)){%>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
	
	
</body>
</html>
</emp:page>
