<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String pvp = "";
	String op = "";
	if(context.containsKey("pvp")){
		pvp = (String)context.getDataValue("pvp");
	}
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
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
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">

	function doOnLoad(){
		isUseLimt();
		setReadonly();
		cleanLimitInd();
	}
	function cleanLimitInd(){
    	var rpddscnt_type = CtrRpddscntCont.rpddscnt_type._getValue();
    	//银票贴现,商票贴现 2014-03-15wangs添加
    	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
    	var bill_type = CtrRpddscntCont.bill_type._getValue();
    	if(bill_type=='100'){//仅银票需控制
    		if(rpddscnt_type == "01" ){//'01':'买入买断', '04':'卖出回购'
        		var limitIndOptions = CtrRpddscntCont.limit_ind._obj.element.options;
        		for(var i=limitIndOptions.length-1;i>=0;i--){	
        			if(limitIndOptions[i].value=="4" || limitIndOptions[i].value=="5" || limitIndOptions[i].value=="6"){//
        				limitIndOptions.remove(i);
        			}
        		}
        		var varOption = new Option('使用承兑人额度','4');
        		limitIndOptions.add(varOption);
        		CtrRpddscntCont.limit_ind._setValue("4");
        		CtrRpddscntCont.limit_ind._obj._renderReadonly(true);
        	}
    	}else if(bill_type=='200'){//商票需控制
        	if(rpddscnt_type == "01" || rpddscnt_type == "02"){//'01':'买入买断', '02':'买入返售'
        		var limitIndOptions = CtrRpddscntCont.limit_ind._obj.element.options;
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
	function doReturn() {
		if('${context.flag}' == "CtrRpddscntCont"){
			var url = '<emp:url action="queryCtrRpddscntContList.do"/>?menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=${context.op}';   
		}else{
			var url = '<emp:url action="queryCtrRpddscntContHistoryList.do"/>?menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=${context.op}'; 
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function doReturn1() {
		window.close();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	//设置页面要素只读
    function setReadonly(){
    	CtrRpddscntCont.rpddscnt_type._obj._renderReadonly(true);
    	CtrRpddscntCont.bill_type._obj._renderReadonly(true);
    	CtrRpddscntCont.bill_curr._obj._renderReadonly(true);
    	CtrRpddscntCont.bill_qnt._obj._renderReadonly(true);
    	CtrRpddscntCont.bill_total_amt._obj._renderReadonly(true);
    	CtrRpddscntCont.rpddscnt_date._obj._renderReadonly(true);
    	CtrRpddscntCont.rpddscnt_rate._obj._renderReadonly(true);
    	CtrRpddscntCont.rpddscnt_int._obj._renderReadonly(true);
    	CtrRpddscntCont.rpay_amt._obj._renderReadonly(true);
    	CtrRpddscntCont.rebuy_date._obj._renderReadonly(true);
    	CtrRpddscntCont.rebuy_rate._obj._renderReadonly(true);
    	CtrRpddscntCont.rebuy_int._obj._renderReadonly(true);
        if("04" == CtrRpddscntCont.rpddscnt_type._getValue()||"02" == CtrRpddscntCont.rpddscnt_type._getValue()){//如果转贴现方式为 卖出回购、买入返售
        	//非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	CtrRpddscntCont.rebuy_date._obj._renderHidden(false);
        	CtrRpddscntCont.rebuy_rate._obj._renderHidden(true);
        	CtrRpddscntCont.rebuy_int._obj._renderHidden(true);
        	$(".emp_field_label:eq(13)").text("总回购利息");
        	$(".emp_field_label:eq(16)").text("回购起始日");
			$(".emp_field_label:eq(15)").text("回购利率");
        }else{
            //非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	CtrRpddscntCont.rebuy_date._obj._renderHidden(true);
        	CtrRpddscntCont.rebuy_rate._obj._renderHidden(true);
        	CtrRpddscntCont.rebuy_int._obj._renderHidden(true);
        	//$(".emp_field_label:eq(21)").text("总贴现利息");
        	//$(".emp_field_label:eq(24)").text("转贴现日期");
			//$(".emp_field_label:eq(23)").text("转贴现利率");
        }
    };
	
	function isUseLimt(){
	    var limit_ind = CtrRpddscntCont.limit_ind._getValue();
	    var rpddscnt_type = CtrRpddscntCont.rpddscnt_type._getValue();
	    if(limit_ind=='1'||limit_ind==''){
	    	CtrRpddscntCont.limit_acc_no._obj._renderHidden(true);
	    	CtrRpddscntCont.limit_acc_no._obj._renderRequired(false);
	    	CtrRpddscntCont.limit_acc_no._obj.config.url='';
	    	CtrRpddscntCont.limit_credit_no._obj._renderHidden(true);
	    	CtrRpddscntCont.limit_credit_no._obj._renderRequired(false);
	    	CtrRpddscntCont.limit_acc_no._obj.config.url='';
	    }else if(limit_ind=='4'){
	    	var rpddscnt_type = '';
	        var bill_type = CtrRpddscntCont.bill_type._getValue();
	        var prd_id = CtrRpddscntCont.prd_id._getValue();
	    	if(prd_id=='300023' && bill_type=='100'){//外部转贴现 ,银票需控制
				rpddscnt_type = CtrRpddscntCont.rpddscnt_type._getValue();
			}
	    	  CtrRpddscntCont.limit_credit_no._obj._renderHidden(false);
 	    	  CtrRpddscntCont.limit_credit_no._obj._renderRequired(true);
 	    	
 	    	  CtrRpddscntCont.limit_acc_no._obj._renderHidden(true);
 	    	  CtrRpddscntCont.limit_acc_no._obj._renderRequired(false);
	    	 if(rpddscnt_type == "01"){
	         	CtrRpddscntCont.limit_credit_no._obj._renderHidden(true);
	         	CtrRpddscntCont.limit_credit_no._obj._renderRequired(false);
	         	
	         	CtrRpddscntCont.limit_acc_no._obj._renderHidden(true);
	         	CtrRpddscntCont.limit_acc_no._obj._renderRequired(false);
	         }
	    }else{
	    	CtrRpddscntCont.limit_acc_no._obj._renderHidden(false);
	    	CtrRpddscntCont.limit_acc_no._obj._renderRequired(true);
	    	CtrRpddscntCont.limit_credit_no._obj._renderHidden(true);
	    	CtrRpddscntCont.limit_credit_no._obj._renderRequired(false);
	    	CtrRpddscntCont.limit_acc_no._obj.config.url='';
	    	var cus_id = CtrRpddscntCont.toorg_no._getValue();
	    	var lmt_type = "02";//02-同业客户
	    	var prd_id = CtrRpddscntCont.prd_id._getValue();
	    	var outstnd_amt = CtrRpddscntCont.rpay_amt._getValue();
	    	CtrRpddscntCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
	    }
	}
	/*--user code begin--*/
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrRpddscntCont.serno._getValue();	//业务编号
		data['cus_id'] = CtrRpddscntCont.toorg_no._getValue();	//客户码
		data['prd_id'] = CtrRpddscntCont.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮begin ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:tabGroup mainTab="base_tab" id="mainTab" >
	 <emp:tab label="合同信息" id="base_tab" needFlush="true" initial="true" >
	<emp:gridLayout id="CtrRpddscntContGroup" title="转贴现申请信息" maxColumn="2">
					<emp:text id="CtrRpddscntCont.cont_no" label="合同编号" maxlength="40" readonly="true" required="false" />
					<emp:text id="CtrRpddscntCont.cn_cont_no" label="中文合同编号" maxlength="100" required="true"  />
					<emp:text id="CtrRpddscntCont.serno" label="业务编号" maxlength="40" readonly="true" required="false" />
					<emp:text id="CtrRpddscntCont.batch_no" label="批次号" readonly="true" required="true" />
					<emp:text id="CtrRpddscntCont.prd_id" label="产品编码" maxlength="10" required="false" readonly="true"/>
					<emp:text id="CtrRpddscntCont.prd_id_displayname" label="产品名称" readonly="true" />
					<emp:select id="CtrRpddscntCont.rpddscnt_type" label="转贴现方式" required="false" dictname="STD_ZB_BUSI_TYPE" colSpan="2" readonly="true"/>
					<emp:pop id="CtrRpddscntCont.toorg_no" label="交易对手行号" url="getPrdBankInfoPopList.do" readonly="true" returnMethod="getToorgNo" required="true" buttonLabel="选择" />
					<emp:text id="CtrRpddscntCont.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				</emp:gridLayout>
					<emp:text id="CtrRpddscntCont.topp_acct_no" label="交易对手账号" maxlength="40" required="false" hidden="true" readonly="true"/>
					<emp:text id="CtrRpddscntCont.topp_acct_name" label="交易对手户名" maxlength="40" required="false" hidden="true" readonly="true"/>
					<emp:pop id="CtrRpddscntCont.tooorg_no" label="交易对手开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getTooorgNo" readonly="true" required="false" hidden="true" buttonLabel="选择" />
					<emp:text id="CtrRpddscntCont.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" hidden="true" readonly="true"/>
			
					<emp:text id="CtrRpddscntCont.this_acct_no" label="本行账号" maxlength="40" required="false" hidden="true" readonly="true"/>
					<emp:text id="CtrRpddscntCont.this_acct_name" label="本行账户名" maxlength="100" required="false" hidden="true" readonly="true"/>
					<emp:pop id="CtrRpddscntCont.acctsvcr_no" label="本行开户行行号" url="getPrdBankInfoPopList.do" readonly="true" returnMethod="getAcctsvcrNo" required="false" hidden="true" buttonLabel="选择" />
					<emp:text id="CtrRpddscntCont.acctsvcr_name" label="本行开户行行名" maxlength="100" required="false" hidden="true" readonly="true"/>
				<emp:gridLayout id="CtrRpddscntContGroup" title="票据信息" maxColumn="2">
					<emp:select id="CtrRpddscntCont.bill_type" label="票据种类" required="false"  dictname="STD_DRFT_TYPE" readonly="true"/>
					<emp:select id="CtrRpddscntCont.bill_curr" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
					<emp:text id="CtrRpddscntCont.bill_total_amt" label="票据总金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="CtrRpddscntCont.bill_qnt" label="票据数量" maxlength="38" required="false" readonly="true"/>
					<emp:text id="CtrRpddscntCont.rpddscnt_int" label="总贴现利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="CtrRpddscntCont.rpay_amt" label="总实付金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="CtrRpddscntCont.rpddscnt_rate" label="转贴现利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
					<emp:date id="CtrRpddscntCont.rpddscnt_date" label="转贴现日期" required="false" readonly="true"/>
					<emp:date id="CtrRpddscntCont.rebuy_date" label="回购日期" required="false" readonly="true"/>
					<emp:text id="CtrRpddscntCont.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
					<emp:text id="CtrRpddscntCont.rebuy_int" label="总回购利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:textarea id="CtrRpddscntCont.memo" label="备注" maxlength="250" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="额度信息">
					<emp:select id="CtrRpddscntCont.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    		<emp:pop id="CtrRpddscntCont.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
				    <emp:pop id="CtrRpddscntCont.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" readonly="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="3" title="登记信息">   
		    		<emp:text id="CtrRpddscntCont.manager_br_id_displayname" label="管理机构"  required="false" readonly="true"/>
		    		<emp:select id="CtrRpddscntCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" required="false" hidden="true"/> 
					<emp:date id="CtrRpddscntCont.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
					<emp:text id="CtrRpddscntCont.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserId" readonly="true"/>
		    		<emp:text id="CtrRpddscntCont.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organNo" readonly="true"/>
		     		<emp:text id="CtrRpddscntCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		     		<emp:text id="CtrRpddscntCont.input_id" label="登记人" maxlength="20" hidden="true" required="false" defvalue="$currentUserId" readonly="true"/>
		    		<emp:text id="CtrRpddscntCont.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" defvalue="$organNo" readonly="true"/>
				</emp:gridLayout>
   </emp:tab>
   <emp:ExtActTab></emp:ExtActTab>
  </emp:tabGroup>
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
</body>
</html>
</emp:page>
