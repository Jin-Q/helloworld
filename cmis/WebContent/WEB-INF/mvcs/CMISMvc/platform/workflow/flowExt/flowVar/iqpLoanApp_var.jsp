<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
/*
String serno = request.getParameter("serno");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
com.ecc.emp.jdbc.JNDIDataSource jndiDataSource = (com.ecc.emp.jdbc.JNDIDataSource)context.getService("dataSource");
Connection connection = jndiDataSource.getConnection();
IqpLoanAppComponent ladc = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("IqpLoanApp", context, connection);
IqpLoanApp iqpLoanApp = ladc.queryIqpLoanApp(serno);
*/
%>

<emp:page>

<script type="text/javascript">
	
   function onVarSubmit(){
     
      /** 在提交后台之前*/
      
      /*将显示值放入对应的 WfiVarShow 之中*/
      
      //WfiVarDisp.crdTotalCrd._setValue(WfiBizVarRecord.crdTotalCrd._obj.getDisplayValue());
      //WfiVarDisp.term._setValue(WfiBizVarRecord.term._obj.getDisplayValue());
      
      /*
      WfiVarDisp.rate_count_type._setValue(WfiBizVarRecord.rate_count_type._obj.getDisplayValue());
	   var apply_amt_check = WfiVarFlag.apply_amount._getValue();
	   var term_check = WfiVarFlag.apply_term._getValue();
	      if(apply_amt_check!=null&&apply_amt_check!=''){
	    	  var apply_amt_cg = WfiBizVarRecord.apply_amount._getValue();
	          var apply_amt = '1';
	          if(apply_amt_cg!=null&&apply_amt_cg!=''&&(apply_amt_cg-apply_amt)>0){
	              alert('变更后的申请金额不能大于原申请金额！');
	              return false;
	          }
	      }
	      if(term_check!=null&&term_check!=''){
	          var term_cg = WfiBizVarRecord.apply_term._getValue();
          	  var term = '1';
	          if(term_cg!=null&&term_cg!=''&&(term_cg-term)>0){
	              alert('变更后的贷款期限不能大于原贷款期限！');
	              return false;
	          }
	      }
	      */
   }
</script>
		
	<DIV style="margin: 0px 0px 0px 10px;">
		<emp:gridLayout id="bizGroup" title="可修改的申请要素" maxColumn="2" >
			<emp:text id="WfiBizVarRecord.apply_amount" label="申请金额(元)"  maxlength="16"  dataType="Currency"  />
			<emp:checkbox id="WfiVarFlag.apply_amount" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->
			
			<emp:select id="WfiBizVarRecord.rate_count_type" label="利率计算方式" dictname="STD_ZB_ACCOUNT_TYPE"/>
			<emp:checkbox id="WfiVarFlag.rate_count_type" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->
						
			<emp:text id="WfiBizVarRecord.reality_ir_y" label="执行年利率" dataType="Rate" />
			<emp:checkbox id="WfiVarFlag.reality_ir_y" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->
						
			<emp:text id="WfiBizVarRecord.apply_term" label="贷款期限(月)" dataType="Int" />
			<emp:checkbox id="WfiVarFlag.apply_term" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->
			
			<emp:text id="WfiBizVarRecord.floating_rate" label="利率浮动比" dataType="Percent"/>
			<emp:checkbox id="WfiVarFlag.floating_rate" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->

		<%/*	<emp:select id="WfiBizVarRecord.ir_adjust_mode" label="利率调整方式" dictname="STD_ZB_IR_ADJ_MODE1"/>
			<emp:checkbox id="WfiVarFlag.ir_adjust_mode" label="是否修改" />
			
		 	<emp:select id="WfiBizVarRecord.interest_acc_mode" label="结息方式" dictname="STD_ZB_CAL_INTEREST"/>
			<emp:checkbox id="WfiVarFlag.interest_acc_mode" label="是否修改" />
		 */%>
		</emp:gridLayout>
		<!-- 字段名称-->
		<emp:text id="WfiVarName.apply_amount" label="" hidden="true" defvalue="申请金额(元)"/>
		<emp:text id="WfiVarName.rate_count_type" label="" hidden="true" defvalue="利率计算方式"/>
		<emp:text id="WfiVarName.reality_ir_y" label="" hidden="true" defvalue="执行年利率"/>
		<emp:text id="WfiVarName.apply_term" label="" hidden="true" defvalue="贷款期限(月)"/>
		<emp:text id="WfiVarName.floating_rate" label="" hidden="true" defvalue="利率浮动比"/> 
		<%/*<emp:text id="WfiVarName.ir_adjust_mode" label="" hidden="true" defvalue="利率调整方式"/> 
		 <emp:text id="WfiVarName.interest_acc_mode" label="" hidden="true" defvalue="结息方式"/> */%>
		
		<!-- 类型 -->
		<emp:text id="WfiVarType.apply_amount" label="" hidden="true" defvalue="double"/>
		<emp:text id="WfiVarType.rate_count_type" label="" hidden="true" defvalue="string"/>
		<emp:text id="WfiVarType.reality_ir_y" label="" hidden="true" defvalue="double"/>
		<emp:text id="WfiVarType.apply_term" label="" hidden="true" defvalue="int"/>
		<emp:text id="WfiVarType.floating_rate" label="" hidden="true" defvalue="double"/>
		<%/*<emp:text id="WfiVarType.ir_adjust_mode" label="" hidden="true" defvalue="string"/>
		<emp:text id="WfiVarType.interest_acc_mode" label="" hidden="true" defvalue="string"/> */%>
	 
		<!-- 显示值 -->	
		<emp:text id="WfiVarDisp.apply_amount" label="" hidden="true" />
		<emp:text id="WfiVarDisp.rate_count_type" label="" hidden="true" />
		<emp:text id="WfiVarDisp.reality_ir_y" label="" hidden="true" />
		<emp:text id="WfiVarDisp.apply_term" label="" hidden="true" />
		<emp:text id="WfiVarDisp.floating_rate" label="" hidden="true" />
		<%/*<emp:text id="WfiVarDisp.ir_adjust_mode" label="" hidden="true" />
		<emp:text id="WfiVarDisp.interest_acc_mode" label="" hidden="true" /> */%>
	</DIV>
	
 </emp:page>