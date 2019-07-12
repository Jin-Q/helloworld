<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	String flag = "";
	String isHaveButton = "";
	String prd_id = "";
	String openNew = "";
	String dunCount = "";
	String isSpecialAcc = "";
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	}
	System.err.println(biz_type);
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	} 
	if(context.containsKey("isHaveButton")){
		isHaveButton = (String)context.getDataValue("isHaveButton");
	} 
	if(context.containsKey("AccLoan.prd_id")){
		prd_id = (String)context.getDataValue("AccLoan.prd_id");
	}
	if(context.containsKey("openNew")){
		openNew = (String)context.getDataValue("openNew");
	}
	if(context.containsKey("dunCount")){
		dunCount = (String)context.getDataValue("dunCount");
	}
	if(context.containsKey("isSpecialAcc")){
		isSpecialAcc = (String)context.getDataValue("isSpecialAcc");
	}
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
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

<script type="text/javascript">
	
	function doReturn() {
		var biz_type = '<%=biz_type%>';
		var flag = '<%=flag%>';
		if(biz_type!=''){
			if(menu_type == 'bad'){//不良资产模块共用
				var url = '<emp:url action="queryArpBadassetAccList.do"/>';
			}else if(flag!=''){
				var url = '<emp:url action="accLoanList.do"/>?menuId=${context.menuId}&flag=${context.flag}&biz_type='+'<%=biz_type%>';
			}else{
				var url = '<emp:url action="queryAccLoanList.do"/>?menuId=AccLoanList&biz_type='+'<%=biz_type%>';
			}			 
			url = EMPTools.encodeURI(url);
			window.location=url;
		}else{
			history.go(-1);
		}
	};

	function doClose(){
		window.close();
	}
	
	/*--user code begin--*/
    function onload(){
    	menu_type = "${context.menu_type}";
    	AccLoan.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
    	var prd_id = AccLoan.prd_id._getValue();
    	if(prd_id == "400020" || prd_id == "400021" || prd_id == "400023" || prd_id == "400022" || prd_id == "400024" || prd_id == "700020" || prd_id == "700021" || prd_id == "500032" ){
    		AccLoan.ruling_ir._obj._renderHidden(true);//基准利率
    		AccLoan.reality_ir_y._obj._renderHidden(true);//执行年利率
    		AccLoan.overdue_rate_y._obj._renderHidden(true);//逾期利率
    		AccLoan.default_rate_y._obj._renderHidden(true);//违约利率
    		AccLoan.inner_owe_int._obj._renderHidden(true);//表内欠息
    		AccLoan.out_owe_int._obj._renderHidden(true);//表外欠息
    		AccLoan.rec_int_accum._obj._renderHidden(true);//应收利息累计
    		AccLoan.recv_int_accum._obj._renderHidden(true);//实收利息累计
    		AccLoan.normal_balance._obj._renderHidden(true);//正常余额
    		AccLoan.overdue_balance._obj._renderHidden(true);//逾期余额
    		AccLoan.comp_int_balance._obj._renderHidden(true);//复利余额
    		AccLoan.slack_balance._obj._renderHidden(true);//呆滞余额
    		AccLoan.bad_dbt_balance._obj._renderHidden(true);//呆账余额
    		AccLoan.post_count._obj._renderHidden(true);//展期次数
    		AccLoan.overdue._obj._renderHidden(true);//逾期期数
    		AccLoan.overdue_date._obj._renderHidden(true);//逾期日期
    		AccLoan.ori_end_date._obj._renderHidden(true);//原到期日期
    		AccLoan.writeoff_date._obj._renderHidden(true);//核销日期
        }else{
        	AccLoan.paydate._obj._renderHidden(true);//转垫款日
        }
        //如果是贷款意向则不显示出账业务编号
    	if(prd_id == "400024"){
    		AccLoan.serno._obj._renderHidden(true);
        }
        //是否点击登记还款信息Tab页
		if('${context.menuId}' == "isTrustAccLoanList" && '${context.op}'=="update"){
			mainTab.tabs.isTrustAccDealDetail._clickLink();
		}else{
			mainTab.tabs.base_tab._clickLink();
		}
    };
    function getCusForm(){
		var cus_id = AccLoan.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccLoan.fount_serno._getValue();	//业务申请编号，不是出账流水号
		data['cus_id'] = AccLoan.cus_id._getValue();	//客户码
		data['prd_id'] = AccLoan.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =AccLoan.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
</script>
</head>
<body class="page_content" onload="onload()">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="台账信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="基本信息">
			<emp:text id="AccLoan.bill_no" label="借据编号" maxlength="40" required="false" colSpan="2"/>
			<%if(!"isTrustAcc".equals(flag)){ %>
			<emp:text id="AccLoan.serno" label="出账流水号" maxlength="40" required="true" readonly="true" />		
			<%} %>	
			<emp:text id="AccLoan.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.prd_id_displayname" label="产品名称" required="false" />
			<emp:text id="AccLoan.cus_id" label="客户码" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccLoan.cus_id_displayname" label="客户名称" required="false" cssElementClass="emp_field_text_long_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="金额信息">
			<emp:select id="AccLoan.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccLoan.loan_amt" label="贷款金额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.ruling_ir" label="基准利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.reality_ir_y" label="执行年利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.overdue_rate_y" label="逾期利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.default_rate_y" label="违约利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.inner_owe_int" label="表内欠息" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.out_owe_int" label="表外欠息" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.rec_int_accum" label="应收利息累计" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.recv_int_accum" label="实收利息累计" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.loan_balance" label="贷款余额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.normal_balance" label="正常余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.overdue_balance" label="逾期余额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.comp_int_balance" label="复利余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.slack_balance" label="呆滞余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.bad_dbt_balance" label="呆账余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="日期信息">	
			<emp:text id="AccLoan.post_count" label="展期次数" maxlength="38" required="false" />
			<emp:text id="AccLoan.overdue" label="逾期期数" maxlength="38" required="false" />
			<emp:date id="AccLoan.distr_date" label="起始日期" required="false" />
			<emp:date id="AccLoan.end_date" label="到期日期" required="false" />
			<emp:date id="AccLoan.ori_end_date" label="原到期日期" required="false" />
			<emp:date id="AccLoan.overdue_date" label="逾期日期" required="false" />
			<emp:date id="AccLoan.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccLoan.settl_date" label="结清日期" required="false" />
			<emp:date id="AccLoan.writeoff_date" label="核销日期" required="true" />			
			<emp:date id="AccLoan.paydate" label="转垫款日" required="true" />
			
			
			<emp:text id="AccLoan.acc_day" label="日期" maxlength="10" required="true" hidden="true"/>
			<emp:text id="AccLoan.acc_year" label="年份" maxlength="5" required="true" hidden="true"/>
			<emp:text id="AccLoan.acc_mon" label="月份" maxlength="5" required="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="风险分类信息">
		    <emp:select id="AccLoan.five_class" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccLoan.twelve_cls_flg" label="十二级分类标志" hidden="true" required="false" dictname="STD_ZB_TWELVE_CLASS"/>
		    <emp:date id="AccLoan.twelve_class_time" label="十二级分类时间" hidden="true" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="其他信息">		
			<emp:text id="AccLoan.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccLoan.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccLoan.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="AccLoan.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true" />
			<emp:select id="AccLoan.acc_status" label="台账状态" required="true" dictname="STD_ZB_ACC_TYPE"/> 
			<emp:text id="AccLoan.fount_serno" label="业务申请编号" hidden="true" />
		</emp:gridLayout>
	  </emp:tab>
	    <%if("AccAssetstrsf".equals(isSpecialAcc)){%>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if("AccAssetTrans".equals(isSpecialAcc)){ %>
	    <!-- modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造  begin-->
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrAssetProContViewPage.do?bill_no=${context.AccLoan.bill_no}&cont_no=${context.AccLoan.cont_no}&prd_id=${context.AccLoan.prd_id}&cont_flag=n&menuIdTab=zczqhxmcx&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <!-- modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造  end-->
	    <%}else if("8".equals(biz_type) && (prd_id.equals("300021")||prd_id.equals("300020"))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("8".equals(biz_type) && !prd_id.equals("300021") && !prd_id.equals("300020")){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if(prd_id.equals("300021")||prd_id.equals("300020")){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("isTrustAcc".equals(flag)){%>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have&flg=trust" initial="false" needFlush="true"/> 
	    <%}else{%>
	     <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}%>
      <emp:ExtActTab></emp:ExtActTab>
      
      <%if("Y".equals(dunCount)){ %>
      <emp:tab label="催收及回执" id="dunTab" url="queryPspDunningRecordList.do?bill_no=${context.AccLoan.bill_no}&menuIdTab=PspDunningTaskInfo&subMenuId=PspDunningRecord&op=view" initial="false" needFlush="true"/>
      <%} %>
    </emp:tabGroup>
	
	<div align="center">
		<br>
		<%if(!"not".equals(isHaveButton)){ %>
			<%if("Y".equals(openNew)){ %>
			<emp:button id="close" label="关闭"/>
			<%}else{ %>
			<emp:button id="return" label="返回列表"/>
			<%} %>
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
