<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op= "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if("view".equals(op)||"to_storage".equals(op)){
		request.setAttribute("canwrite","");
	}
	String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//选择机构信息返回方法
	function getOrgID(data){
		MortGuarantyEvalValue.eval_cmon_code._setValue(data.extr_eval_org._getValue());
		MortGuarantyEvalValue.eval_org_name._setValue(data.cus_id._getValue());
		MortGuarantyEvalValue.eval_name._setValue(data.cus_name._getValue());
		MortGuarantyEvalValue.eval_end_date._setValue(data.end_date._getValue());
	};	
  	function doNext(){
		if(checkDt('eval_end_date')){
	  		//added by yangzy 2015/05/28  提交改造  begin
			var buttonObj = document.getElementById('button_next');
			buttonObj.disabled=true;
			buttonObj.innerHTML='请稍等..';
			//added by yangzy 2015/05/28  提交改造  end
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						//added by yangzy 2015/05/28  提交改造  begin
						buttonObj.disabled=false;
						buttonObj.innerHTML='确定';
						//added by yangzy 2015/05/28  提交改造  end
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						//added by yangzy 2015/05/28  提交改造  begin
						buttonObj.disabled=false;
						buttonObj.innerHTML='确定';
						//added by yangzy 2015/05/28  提交改造  end
						alert("保存成功！");
					    window.location.reload();
					}else{
						//added by yangzy 2015/05/28  提交改造  begin
						buttonObj.disabled=false;
						buttonObj.innerHTML='确定';
						//added by yangzy 2015/05/28  提交改造  end
						alert("保存失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("保存失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			var result = MortGuarantyBaseInfo._checkAll();
			var result1 = MortGuarantyEvalValue._checkAll();
		    if(result&&result1){
				MortGuarantyBaseInfo._toForm(form);
				MortGuarantyEvalValue._toForm(form);
				page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
		    	var postData = YAHOO.util.Connect.setForm(form);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		    }else {
		    	//added by yangzy 2015/05/28  提交改造  begin
				buttonObj.disabled=false;
				buttonObj.innerHTML='确定';
				//added by yangzy 2015/05/28  提交改造  end
			    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
			}
		}		
	};
	function doLoad(){
		doChangeOnload();//外层的评估类型校验		
		checkOnload();//内层的评估方式校验
		<%if("view".equals(op)){%>
		<%}else{%>
		   doCheck();
		<%}%>	
		
	}
	function checkOnload(){
		var flag = MortGuarantyEvalValue.eval_mode._getValue();
		if(flag=="3"||flag==""||flag=="2"){
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_name._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_name._obj._renderRequired(false);
		//	MortGuarantyEvalValue.eval_org_name._setValue("");
		//	MortGuarantyEvalValue.eval_cmon_code._setValue("");
		}else{
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_name._obj._renderHidden(false);
		//	MortGuarantyEvalValue.eval_org_name._obj._renderRequired(true);
		//	MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(true);
		}
	}
	function check(){
		var flag = MortGuarantyEvalValue.eval_mode._getValue();
		if(flag=="3"||flag==""||flag=="2"){
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_name._obj._renderHidden(true);
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_name._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_org_name._setValue("");
			MortGuarantyEvalValue.eval_cmon_code._setValue("");
			MortGuarantyEvalValue.eval_end_date._setValue("");
			MortGuarantyEvalValue.eval_name._setValue("");
		}else{
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_name._obj._renderHidden(false);
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_name._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_org_name._setValue("");
			MortGuarantyEvalValue.eval_cmon_code._setValue("");
			MortGuarantyEvalValue.eval_end_date._setValue("");
			MortGuarantyEvalValue.eval_name._setValue("");
		}
	}

	function doChangeOnload(){
		var flag = MortGuarantyEvalValue.eval_type._getValue();
		if(flag=="0400"){//尚未评估
			
			MortGuarantyEvalValue.cur_type._obj._renderHidden(true);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderHidden(true);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderHidden(true);    
			MortGuarantyEvalValue.guar_amt._obj._renderHidden(true);        
			MortGuarantyEvalValue.wrr_amt._obj._renderHidden(true);         
			MortGuarantyEvalValue.eval_amt._obj._renderHidden(true);        
			MortGuarantyEvalValue.eval_mode._obj._renderHidden(true);       
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(true);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(true); 
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(true); 
			MortGuarantyEvalValue.eval_name._obj._renderHidden(true); 
			MortGuarantyEvalValue.eval_date._obj._renderHidden(true); 

			MortGuarantyEvalValue.cur_type._obj._renderRequired(false);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderRequired(false);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderRequired(false);    
			MortGuarantyEvalValue.guar_amt._obj._renderRequired(false);        
			MortGuarantyEvalValue.wrr_amt._obj._renderRequired(false);         
			MortGuarantyEvalValue.eval_amt._obj._renderRequired(false);        
			MortGuarantyEvalValue.eval_mode._obj._renderRequired(false);       
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(false);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(false); 
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(false); 
			MortGuarantyEvalValue.eval_name._obj._renderRequired(false); 
			MortGuarantyEvalValue.eval_date._obj._renderRequired(false); 

			MortGuarantyEvalValue.cur_type._setValue("");     
			MortGuarantyEvalValue.pldimn_rate._setValue("");       
			MortGuarantyEvalValue.high_pld_amt._setValue("");      
			MortGuarantyEvalValue.guar_amt._setValue("");          
			MortGuarantyEvalValue.wrr_amt._setValue("");           
			MortGuarantyEvalValue.eval_amt._setValue("");          
			MortGuarantyEvalValue.eval_mode._setValue("");         
			MortGuarantyEvalValue.eval_org_name._setValue("");     
			MortGuarantyEvalValue.eval_cmon_code._setValue("");
			MortGuarantyEvalValue.eval_end_date._setValue("");
			MortGuarantyEvalValue.eval_name._setValue("");    
			MortGuarantyEvalValue.eval_date._setValue("");         
			MortGuarantyEvalValue.is_valid._setValue("");          
			MortGuarantyEvalValue.sys_update_time._setValue("");   
		}else{
			MortGuarantyEvalValue.cur_type._obj._renderHidden(false);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderHidden(false);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderHidden(false);    
			MortGuarantyEvalValue.guar_amt._obj._renderHidden(false);        
			MortGuarantyEvalValue.wrr_amt._obj._renderHidden(false);         
			MortGuarantyEvalValue.eval_amt._obj._renderHidden(false);        
			MortGuarantyEvalValue.eval_mode._obj._renderHidden(false);       
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(false);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(false);  
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(false);  
			MortGuarantyEvalValue.eval_name._obj._renderHidden(false);  
			MortGuarantyEvalValue.eval_date._obj._renderHidden(false); 

			MortGuarantyEvalValue.cur_type._obj._renderRequired(true);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderRequired(true);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderRequired(true);    
			MortGuarantyEvalValue.guar_amt._obj._renderRequired(true);        
			MortGuarantyEvalValue.wrr_amt._obj._renderRequired(true);         
			MortGuarantyEvalValue.eval_amt._obj._renderRequired(true);        
			MortGuarantyEvalValue.eval_mode._obj._renderRequired(true);       
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(true);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(true);
			MortGuarantyEvalValue.eval_name._obj._renderRequired(true);  
			MortGuarantyEvalValue.eval_date._obj._renderRequired(true);  
		}
	}
	function doChange(){
		var flag = MortGuarantyEvalValue.eval_type._getValue();
		if(flag=="0400"){
			MortGuarantyEvalValue.cur_type._obj._renderHidden(true);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderHidden(true);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderHidden(true);    
			MortGuarantyEvalValue.guar_amt._obj._renderHidden(true);        
			MortGuarantyEvalValue.wrr_amt._obj._renderHidden(true);         
			MortGuarantyEvalValue.eval_amt._obj._renderHidden(true);        
			MortGuarantyEvalValue.eval_mode._obj._renderHidden(true);       
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(true);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(true);  
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(true);  
			MortGuarantyEvalValue.eval_name._obj._renderHidden(true);  
			MortGuarantyEvalValue.eval_date._obj._renderHidden(true); 

			MortGuarantyEvalValue.cur_type._obj._renderRequired(false);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderRequired(false);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderRequired(false);    
			MortGuarantyEvalValue.guar_amt._obj._renderRequired(false);        
			MortGuarantyEvalValue.wrr_amt._obj._renderRequired(false);         
			MortGuarantyEvalValue.eval_amt._obj._renderRequired(false);        
			MortGuarantyEvalValue.eval_mode._obj._renderRequired(false);       
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(false);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(false);  
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(false);  
			MortGuarantyEvalValue.eval_name._obj._renderRequired(false);
			MortGuarantyEvalValue.eval_date._obj._renderRequired(false); 

			MortGuarantyEvalValue.cur_type._setValue("");     
			MortGuarantyEvalValue.pldimn_rate._setValue("");       
			MortGuarantyEvalValue.high_pld_amt._setValue("");      
			MortGuarantyEvalValue.guar_amt._setValue("");          
			MortGuarantyEvalValue.wrr_amt._setValue("");           
			MortGuarantyEvalValue.eval_amt._setValue("");          
			MortGuarantyEvalValue.eval_mode._setValue("");         
			MortGuarantyEvalValue.eval_org_name._setValue("");     
			MortGuarantyEvalValue.eval_cmon_code._setValue("");   
			MortGuarantyEvalValue.eval_end_date._setValue("");   
			MortGuarantyEvalValue.eval_name._setValue("");    
			MortGuarantyEvalValue.eval_date._setValue("");         
			MortGuarantyEvalValue.is_valid._setValue("");          
			MortGuarantyEvalValue.sys_update_time._setValue("");   
		}else{
			MortGuarantyEvalValue.cur_type._obj._renderHidden(false);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderHidden(false);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderHidden(false);    
			MortGuarantyEvalValue.guar_amt._obj._renderHidden(false);        
			MortGuarantyEvalValue.wrr_amt._obj._renderHidden(false);         
			MortGuarantyEvalValue.eval_amt._obj._renderHidden(false);        
			MortGuarantyEvalValue.eval_mode._obj._renderHidden(false);       
			MortGuarantyEvalValue.eval_org_name._obj._renderHidden(false);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderHidden(false); 
			MortGuarantyEvalValue.eval_end_date._obj._renderHidden(false); 
			MortGuarantyEvalValue.eval_name._obj._renderHidden(false);  
			MortGuarantyEvalValue.eval_date._obj._renderHidden(false); 

			MortGuarantyEvalValue.cur_type._obj._renderRequired(true);        
			MortGuarantyEvalValue.pldimn_rate._obj._renderRequired(true);     
			MortGuarantyEvalValue.high_pld_amt._obj._renderRequired(true);    
			MortGuarantyEvalValue.guar_amt._obj._renderRequired(true);        
			MortGuarantyEvalValue.wrr_amt._obj._renderRequired(true);         
			MortGuarantyEvalValue.eval_amt._obj._renderRequired(true);        
			MortGuarantyEvalValue.eval_mode._obj._renderRequired(true);       
			MortGuarantyEvalValue.eval_org_name._obj._renderRequired(true);   
			MortGuarantyEvalValue.eval_cmon_code._obj._renderRequired(true); 
			MortGuarantyEvalValue.eval_end_date._obj._renderRequired(true); 
			MortGuarantyEvalValue.eval_name._obj._renderRequired(true);  
			MortGuarantyEvalValue.eval_date._obj._renderRequired(true);  
			check();     
		}
	}
	//异步验证有没有录入押品详细信息
	function doCheck(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("fail" == flag){
					alert("押品详细信息还没有录入，不能做押品价值评估！");
					var button = document.getElementById('button_next');
					button.disabled = "true";
				}else{
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var guaranty_no = '${context.guaranty_no}';
		var guaranty_type = '${context.guaranty_type}';
		var url = '<emp:url action="checkGuarantyIsFinish.do"/>?guaranty_no='+guaranty_no+'&guaranty_type='+guaranty_type;
		url = EMPTools.encodeURI(url);
	 	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};

	/*** 校验：评估日期 < 当前日期 < 评估机构有效日期   ***/
	function checkDt(str){
		var eval_mode = MortGuarantyEvalValue.eval_mode._getValue();	//评估方式
		if(eval_mode == '1'){
			var eval_date = MortGuarantyEvalValue.eval_date._getValue();	//评估日期
			var eval_end_date = MortGuarantyEvalValue.eval_end_date._getValue();	//评估机构有效日期
			var openDay='${context.OPENDAY}';	//当前日期
			
			if(eval_date!='' && str == 'eval_date'){
				if(eval_date > eval_end_date || eval_date > openDay ){
		    		alert('[评估日期]应该小于[评估机构有效日期]与[当前日期]！');
		    		MortGuarantyEvalValue.eval_date._obj.element.value="";
		    		return false;
		    	}
	    	}
	    	
			if(str == 'eval_end_date'){
				if(eval_date > eval_end_date || openDay > eval_end_date ){
		    		alert('[评估机构有效日期]应该大于[当前日期]与[评估日期]！');
		    		return false;
		    	}else{
		    		return true;
		    	}
	    	}
		}else{
			var openDay='${context.OPENDAY}';	//当前日期
			var eval_date = MortGuarantyEvalValue.eval_date._getValue();	//评估日期
			
			if(eval_date!=''){
				if( eval_date > openDay ){
		    		alert('[评估日期]应该小于[当前日期]！');
		    		MortGuarantyEvalValue.eval_date._obj.element.value="";
		    		return false;
		    	}else{
		    		return true;
		    	}
	    	}else{
	    		return true;
	    	}
		}
		
	}	
	function doPldimnRate(){
		MortGuarantyEvalValue.pldimn_rate._setValue('');
		high_pld_amt=parseFloat(MortGuarantyEvalValue.high_pld_amt._getValue());
		guar_amt=parseFloat(MortGuarantyEvalValue.guar_amt._getValue());
		if(isNaN(high_pld_amt) || high_pld_amt <= 0){
			return;
		}
		if(isNaN(guar_amt) || guar_amt <= 0){
			return;
		}
		pldimn_rate = parseFloat(guar_amt/high_pld_amt);
		pldimn_rate = round(pldimn_rate,4);
		MortGuarantyEvalValue.pldimn_rate._setValue(''+pldimn_rate+'');
		if(pldimn_rate>1.2){
			alert("抵质押率不得超过120%");
			MortGuarantyEvalValue.pldimn_rate._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortGuarantyEvalValueRecord.do" method="POST">
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="原价值信息" maxColumn="2">
			<emp:select id="MortGuarantyBaseInfo.guaranty_cur_type" label="押品原值币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_score" label="押品原值" maxlength="18" required="true" dataType="Currency" />
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyEvalValueGroup" title="评估价值信息" maxColumn="2">
			<emp:select id="MortGuarantyEvalValue.eval_type" label="评估类型" required="true" dictname="STD_EVAL_TYPE"  onchange="doChange()"/>
			<emp:select id="MortGuarantyEvalValue.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortGuarantyEvalValue.high_pld_amt" label="最高可担保金额（元）" maxlength="18" required="true" dataType="Currency" onchange="doPldimnRate()"/>
			<emp:text id="MortGuarantyEvalValue.guar_amt" label="担保金额（元）" maxlength="18" required="true" dataType="Currency" onchange="doPldimnRate()"/>
			<emp:text id="MortGuarantyEvalValue.pldimn_rate" label="抵质押率" maxlength="16" readonly="true" dataType="Percent" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortGuarantyEvalValue.wrr_amt" label="权利金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortGuarantyEvalValue.eval_amt" label="评估金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="MortGuarantyEvalValue.eval_mode" label="评估方式" required="true" dictname="STD_ZB_EVAL_TYPE" onchange="check()" defvalue="1"/>
			<emp:pop id="MortGuarantyEvalValue.eval_name" label="评估机构名称" required="true" url="queryCusOrgAppMngPopList.do?restrictUsed=false&returnMethod=getOrgID" cssElementClass="emp_pop_common_org" colSpan="2"/>
			<emp:text id="MortGuarantyEvalValue.eval_org_name" label="评估机构客户码" required="true" readonly="true" hidden="true"/>
			<emp:text id="MortGuarantyEvalValue.eval_cmon_code" label="评估机构组织机构代码" maxlength="20" required="true" readonly="true" />
			<emp:date id="MortGuarantyEvalValue.eval_end_date" label="评估机构有效日期" required="true" readonly="true"/>
			<emp:date id="MortGuarantyEvalValue.eval_date" label="评估日期" required="true" onblur="checkDt('eval_date')"/>
			
			<emp:text id="MortGuarantyEvalValue.is_valid" label="IS_VALID" maxlength="1" required="false" hidden="true"/>
			<emp:date id="MortGuarantyEvalValue.sys_update_time" label="系统更新日期" required="false" hidden="true"/>
			<emp:text id="MortGuarantyEvalValue.eval_id" label="评估信息编码" maxlength="30" hidden="true"/>
			<emp:text id="MortGuarantyEvalValue.guaranty_no" label="押品编号" maxlength="40" required="true" defvalue="<%=guaranty_no%>" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="next" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<% } %>			
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>押品价格调整历史信息</div>
	<emp:table icollName="PspGuarantyValueReevalList" pageMode="false" url="">
		<emp:text id="guaranty_no" label="押品编号"/>
		<emp:text id="last_reeval_value" label="上期认定价值 " dataType="Currency"/>
		<emp:text id="batch_reeval_value" label="本期批量重估押品价值" dataType="Currency"/>
		<emp:text id="reeval_value" label="本期建议押品价值" dataType="Currency"/>
	</emp:table>
</body>
</html>
</emp:page>