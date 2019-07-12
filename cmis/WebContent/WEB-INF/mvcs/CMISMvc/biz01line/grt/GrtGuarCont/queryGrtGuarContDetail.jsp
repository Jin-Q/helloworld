<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String flag ="";
if(context.containsKey("flag")){
	flag = (String)context.getDataValue("flag"); 
}
String rel = "";
if(context.containsKey("rel")){
	rel=(String)context.getDataValue("rel");
}
String isCreditChange = "";
if(context.containsKey("isCreditChange")){
	isCreditChange=(String)context.getDataValue("isCreditChange");
}
String canUpdate = "";
if(context.containsKey("canUpdate")){
	canUpdate=(String)context.getDataValue("canUpdate");
}
//担保变更签订时，所传的参数
String menu = "";
if(context.containsKey("menu")){
	menu=(String)context.getDataValue("menu");
}
String drfpo_no = "";
if(context.containsKey("drfpo_no")){
	drfpo_no=(String)context.getDataValue("drfpo_no");
}
String po_no = "";
if(context.containsKey("po_no")){
	po_no=(String)context.getDataValue("po_no");
}
String guaranty_no = "";
if(context.containsKey("guaranty_no")){
	guaranty_no=(String)context.getDataValue("guaranty_no");
}
String guarCusId ="";
if(context.containsKey("guar_cus_id")){
	guarCusId=(String)context.getDataValue("guar_cus_id");
}
//获取对公客户管理一键查询标识符
String one_key = "";
if(context.containsKey("OneKey")){
	one_key = (String)context.getDataValue("OneKey");
}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 450px;
	};
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var guar_cont_type = GrtGuarCont.guar_cont_type._getValue();
		var cus_id = GrtGuarCont.cus_id._getValue();
		var action="";
		if(guar_cont_type=="01"){
			action="zg";
		}else{
			action="yb";
		}
		<%if(flag.equals("queryGrtGuarCont4Tab")){%>
		var drfpo_no='<%=drfpo_no%>';
		if(drfpo_no==""){
			var url = '<emp:url action="queryGrtGuarCont4TabList.do"/>?po_no=<%=po_no %>';
		}else{
			var url = '<emp:url action="queryGrtGuarCont4TabList.do"/>?drfpo_no=<%=drfpo_no %>';
		}
		<%}else if(flag.equals("arp")){%>
			var url = '<emp:url action="queryGrtGuarContByMortGuarantyList.do"/>?menuIdTab=mort_maintain&guaranty_no=<%=guaranty_no%>';
		<%}else if(flag.equals("ybWh")){%>
		window.close();
		<%}else if(menu.equals("dbbg")){%>
		var url = '<emp:url action="QueryGuarChangeSignList.do"/>?&menuId=QueryGuarChangeSignList';
		<%}else{%>  
		var menuId = '${context.menuId}';
		var url = '<emp:url action="queryGrtGuarContList.do"/>?menuId='+menuId+'&action='+action;
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
       window.close();
	};

	function load(){
		GrtGuarCont.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//document.getElementById("main_tab").href="javascript:reLoad()";
		if (GrtGuarCont.lmt_grt_flag._obj.element.value == "1") {
			//GrtGuarCont.limit_code._obj._renderRequired(true);
			//GrtGuarCont.limit_code._obj._renderReadonly(true);
			//GrtGuarCont.limit_code._obj._renderHidden(false);
	/*		document.getElementById('RLmtGuarCont').style.display="";   
			RLmtGuar.guar_amt._obj._renderRequired(true);
			RLmtGuar.is_per_gur._obj._renderRequired(true); 
			RLmtGuar.is_add_guar._obj._renderRequired(true); */
			 
		}else{  
			//GrtGuarCont.limit_code._obj._renderRequired(false);
			//GrtGuarCont.limit_code._obj._renderReadonly(false);
			//GrtGuarCont.limit_code._obj._renderHidden(true);
	/*		document.getElementById('RLmtGuarCont').style.display="none";   
			RLmtGuar.guar_amt._obj._renderRequired(false);
			RLmtGuar.is_per_gur._obj._renderRequired(false); 
			RLmtGuar.is_add_guar._obj._renderRequired(false); */
		}   
		if(GrtGuarCont.guar_model._getValue()=="05"){//担保模式为联保时
			//GrtGuarCont.limit_code1._setValue(GrtGuarCont.limit_code._getValue());
		    //GrtGuarCont.limit_code1._obj._renderHidden(false);
		    //GrtGuarCont.limit_code._obj._renderHidden(true);
		
		}
	   /**ws添加--------start----*/   
		if('<%=rel%>' != "ywRel"){       
			/**隐藏业务担保合同相关信息 */ 
			document.getElementById('GrtLoanRGur').style.display="none";   
			GrtLoan.guar_amt._obj._renderRequired(false);
			GrtLoan.is_per_gur._obj._renderRequired(false); 
			GrtLoan.is_add_guar._obj._renderRequired(false);     
		}else {       
			GrtLoan.rel._setValue('<%=rel%>');    
			GrtLoan.isCreditChange._setValue('<%=isCreditChange%>');    
		} 
		/**如果是业务则隐藏授信担保合同相关信息 */
		if('<%=rel%>' == "ywRel"){ 	
	/*		document.getElementById('RLmtGuarCont').style.display="none";   
			RLmtGuar.guar_amt._obj._renderRequired(false);
			RLmtGuar.is_per_gur._obj._renderRequired(false); 
			RLmtGuar.is_add_guar._obj._renderRequired(false);  */
		} 
		
		if('<%=rel%>' == "sxRel"){         
	//		RLmtGuar.rel._setValue('<%=rel%>');          
		} 
		if('<%=canUpdate%>' == "canNot"){
			/**如果是引用进的担保合同，则不允许修改担保合同信息 */   
	 		GrtGuarCont.guar_cont_cn_no._obj._renderReadonly(true);
			GrtGuarCont.agr_no._obj._renderReadonly(true);
			GrtGuarCont.cur_type._obj._renderReadonly(true);
			GrtGuarCont.guar_amt._obj._renderReadonly(true);
			GrtGuarCont.guar_start_date._obj._renderReadonly(true);
			GrtGuarCont.guar_end_date._obj._renderReadonly(true);
			GrtGuarCont.memo._obj._renderReadonly(true);
			GrtGuarCont.manager_id_displayname._obj._renderReadonly(true);
			GrtGuarCont.manager_br_id_displayname._obj._renderReadonly(true);
	    }              
		var guar_cont_state = GrtGuarCont.guar_cont_state._getValue();
		if(guar_cont_state == "01"){
			var guar_amt = GrtGuarCont.guar_amt._getValue();
			var used_amt = GrtGuarCont.used_amt._getValue();
			if(parseFloat(guar_amt)>=parseFloat(used_amt)){
				var can_amt = parseFloat(guar_amt)-parseFloat(used_amt);
				can_amt = Math.round(can_amt*100)/100;
				GrtGuarCont.can_amt._setValue(''+can_amt+'');
			}else{
				GrtGuarCont.can_amt._setValue(''+0.00+'');
			}
		}else{
			GrtGuarCont.used_amt._obj._renderHidden(true);
			GrtGuarCont.can_amt._obj._renderHidden(true);
		}
		/**add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 begin **/
	    var guarWay = GrtGuarCont.guar_way._getValue();
	    if(guarWay!="00"){
	    	GrtGuarCont.actual_mort_rate._obj._renderHidden(true);
	    	GrtGuarCont.actual_mort_rate._obj._renderRequired(false);
	    }
	    /**add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 end **/
	}
	
	/*--user code begin--*/
	//查看客户信息
	function viewCusInfo(){
		var cus_id = GrtGuarCont.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	/*--user code end--*/
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var guarCusId ='<%=guarCusId%>'
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+guarCusId;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
</script>
</head>
<body class="page_content" onload="load()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="基本信息">
			<emp:text id="GrtGuarCont.cus_id" label="借款人客户码" required="false" readonly="true" />
			<emp:text id="GrtGuarCont.cus_id_displayname" label="借款人客户名称" required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2" />
		    <emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" maxlength="60" required="true" />
			<emp:select id="GrtGuarCont.guar_cont_type" label="担保合同类型" required="false" dictname="STD_GUAR_CONT_TYPE" readonly="true" colSpan="2"/>
			<emp:select id="GrtGuarCont.guar_model" label="担保模式" required="false" dictname="STD_GUAR_MODEL" hidden="false" readonly="true"/>
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" required="false" dictname="STD_GUAR_TYPE" readonly="true"/>
			<!-- **add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 begin -->
			<emp:text id="GrtGuarCont.actual_mort_rate" label="实际抵押率" dataType="Rate" maxlength="16" required="true" colSpan="2" readonly="true"/>
			<!-- **add by lisj 2015-6-3  需求编号：【XD150504034】信贷系统贷后管理改造 end -->
			<emp:select id="GrtGuarCont.lmt_grt_flag" label="是否授信项下" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:text id="GrtGuarCont.limit_code" label="授信额度编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.limit_code1" label="联保协议编号" maxlength="40" required="false" readonly="true" hidden="true" colSpan="2"/>
			<emp:select id="GrtGuarCont.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="GrtGuarCont.guar_amt" label="担保金额" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="GrtGuarCont.used_amt" label="已用担保金额" maxlength="18" dataType="Currency" readonly="true"/>
			<emp:text id="GrtGuarCont.can_amt" label="可用担保金额" maxlength="18" dataType="Currency" readonly="true"/>
			<emp:date id="GrtGuarCont.guar_start_date" label="担保起始日" required="false"/>
			<emp:date id="GrtGuarCont.guar_end_date" label="担保终止日" required="false"/>
			<emp:date id="GrtGuarCont.sign_date" label="签订日期" required="false" readonly="true"/>
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" required="false" dictname="STD_CONT_STATUS" readonly="true" defvalue="00" />
			<emp:textarea id="GrtGuarCont.memo" label="备注信息" maxlength="200" required="false" />
		
			<emp:text id="GrtGuarCont.guar_term" label="担保期限" maxlength="0" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.guar_term_type" label="担保期限类型" maxlength="2" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.drfpo_no" label="票据池编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.po_no" label="应收账款池编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.agr_no" label="联保协议编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.poType" label="标志位（1--应收账款，2--保理池）" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.bill_amt" label="担保合同项下担保品的价值总额（辅助字段）" maxlength="40" required="false" hidden="true" dataType="Currency" defvalue="0"/>
		</emp:gridLayout>
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="登记信息">
			<emp:pop id="GrtGuarCont.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="GrtGuarCont.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true"/>
			<emp:text id="GrtGuarCont.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false" /> 
			<emp:text id="GrtGuarCont.input_br_id_displayname" label="登记机构" required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarCont.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:text id="GrtGuarCont.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="GrtGuarCont.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="GrtGuarCont.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>                            
		<div id="GrtLoanRGur">
		 <emp:gridLayout id="GrtLoanRGurGroup" title="业务担保合同关联信息" maxColumn="2"> 
			<emp:text id="GrtLoan.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>   
			<emp:text id="GrtLoan.cont_no" label="合同编号" maxlength="40" defvalue="${context.cont_no}" required="false" hidden="true"/>    
			<emp:text id="GrtLoan.guar_cont_no" label="担保合同编号" defvalue="${context.GrtGuarCont.guar_cont_no}" readonly="true" hidden="true"/>       
			<emp:text id="GrtLoan.guar_amt" label="本次担保金额" maxlength="18" required="true" onblur="checkCur()" dataType="Currency" /> 
			<emp:select id="GrtLoan.is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO" required="true"/> 
			<emp:select id="GrtLoan.is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:text id="GrtLoan.rel" label="业务标识" required="false" hidden="true"/>    
			<emp:text id="GrtLoan.isCreditChange" label="是否担保变更" required="false" hidden="true"/>    
		 </emp:gridLayout>
		</div> 
		</emp:tab>
  <emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>

	<div align="center">
		<br>
		<%if(!"".equals(one_key) && one_key != null){ %>
			<emp:button id="returnByOneKey" label="返回" />
		<%}else if(flag.equals("loan")||flag.equals("ybWh")){%>
		   <emp:button id="close" label="关闭"/>
		<%}else{ %>
		  <emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
