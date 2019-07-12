<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}    
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	} 
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>  
<script type="text/javascript">
	
	/*--user code begin--*/
	function onLoad(){
		isBankFormat();
		isShowByPrdId();
		doChangeForLoad();
		/**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 begin**/
		var op='<%=op%>';
		if(op="view"){
			IqpGuarantInfo.guarant_mode._obj._renderReadonly(true);
		}
		/**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 end**/
    };

    function isShowByPrdId(){
        var prd_id = '${context.prd_id}';
        if(prd_id == "400020"){//外汇保函
        	IqpGuarantInfo.item_name._obj._renderHidden(true);//项目名称
			IqpGuarantInfo.cont_no._obj._renderHidden(true);//商务合同编号
			IqpGuarantInfo.cont_name._obj._renderHidden(true);//商务合同名称
			IqpGuarantInfo.corre_busnes_cont_amt._obj._renderHidden(true);//商务合同金额
			IqpGuarantInfo.cur_type._obj._renderHidden(true);//商务合同币种
			IqpGuarantInfo.ben_addr._obj._renderHidden(true);//受益人地址
			IqpGuarantInfo.ben_acct_org_no._obj._renderHidden(true);//受益人开户行行号
			IqpGuarantInfo.ben_acct_org_name._obj._renderHidden(true);//受益人开户行行名
			IqpGuarantInfo.ben_acct_no._obj._renderHidden(true); //受益人账号

			IqpGuarantInfo.item_name._obj._renderRequired(false);//项目名称
			IqpGuarantInfo.cont_no._obj._renderRequired(false);//商务合同编号
			IqpGuarantInfo.cont_name._obj._renderRequired(false);//商务合同名称
			IqpGuarantInfo.corre_busnes_cont_amt._obj._renderRequired(false);//商务合同金额
			IqpGuarantInfo.cur_type._obj._renderRequired(false);//商务合同币种
			IqpGuarantInfo.ben_addr._obj._renderRequired(false);//受益人地址
			IqpGuarantInfo.ben_acct_org_no._obj._renderRequired(false);//受益人开户行行号
			IqpGuarantInfo.ben_acct_org_name._obj._renderRequired(false);//受益人开户行行名
			IqpGuarantInfo.ben_acct_no._obj._renderRequired(false); //受益人账号
        }
    };
	//是否转开代理行保函   
	function isBankFormat(){
		var is_bank_format = IqpGuarantInfo.is_agt_guarant._getValue();
        if(is_bank_format == "1"){
        	IqpGuarantInfo.agt_bank_no._obj._renderHidden(false);
        	IqpGuarantInfo.agt_bank_name._obj._renderHidden(false);
        	IqpGuarantInfo.agt_bank_no._obj._renderRequired(true);
        	IqpGuarantInfo.agt_bank_name._obj._renderRequired(true);
        }else{
        	IqpGuarantInfo.agt_bank_no._obj._renderHidden(true);
        	IqpGuarantInfo.agt_bank_name._obj._renderHidden(true);
        	
        	IqpGuarantInfo.agt_bank_no._obj._renderRequired(false);
        	IqpGuarantInfo.agt_bank_name._obj._renderRequired(false);

        	IqpGuarantInfo.agt_bank_no._setValue("");
        	IqpGuarantInfo.agt_bank_name._setValue("");
        }  
	};
	
	function doSave(){
		if(!IqpGuarantInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpGuarantInfo._toForm(form);
		//var serno = IqpBksyndic._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
					window.location.reload();
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpGuarantInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}				
    function doChange(){
       var guarant_type = IqpGuarantInfo.guarant_type._getValue();
       if("01"==guarant_type){//融资类
    	    //清空保函类型下拉选项
    		document.getElementById('IqpGuarantInfo.guarant_mode').innerHTML="";
    	    var itms1 = document.getElementById('IqpGuarantInfo.guarant_mode').options;
    		var op0 = document.createElement("OPTION");
    		var op1 = document.createElement("OPTION");
    		var op2 = document.createElement("OPTION");
    		var op3 = document.createElement("OPTION");
    		var op4 = document.createElement("OPTION");
    		var op5 = document.createElement("OPTION");
    		var op6 = document.createElement("OPTION");
    		var op7 = document.createElement("OPTION");
    		var op8 = document.createElement("OPTION");
    		var op9 = document.createElement("OPTION");
    		var op10 = document.createElement("OPTION");
    		var op11 = document.createElement("OPTION");
    		var op12 = document.createElement("OPTION");
    		var op13 = document.createElement("OPTION");
    		var op14 = document.createElement("OPTION");
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op0);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op1);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op2);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op3);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op4);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op5);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op6);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op7);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op8);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op9);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op10);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op11);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op12);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op13);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op14);
    		op0.innerText="-----请选择-----";
    		op1.innerText="特种保函";
    		op2.innerText="其他担保";
    		op3.innerText="银行资信证明";
    		op4.innerText="开工预付款保函";
    		op5.innerText="商业承兑汇票保兑函";
    		op6.innerText="租赁保函";
    		op7.innerText="备用信用证";

    		op8.innerText="借款保函";
    		op9.innerText="延期付款保函";
    		op10.innerText="商票保贴";
    		op11.innerText="商票保押保函";
    		op12.innerText="其他保函";
    		op13.innerText="透支保函";
    		op14.innerText="委托金融同业代开银承保函";

    		op0.value="";
    		op1.value="17";
    		op2.value="14";
    		op3.value="12";
    		op4.value="11";
    		op5.value="10";
    		op6.value="04";
    		op7.value="18";
    		
    		op8.value="03";
    		op9.value="08";
    		op10.value="15";
    		op11.value="16";
    		op12.value="13";
    		op13.value="19";
    		op14.value="20";
       }else if("02"==guarant_type){//非融资类
    	   //清空保函类型下拉选项
	   		document.getElementById('IqpGuarantInfo.guarant_mode').innerHTML="";
	   		/**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 begin**/
	   	    var itms1 = document.getElementById('IqpGuarantInfo.guarant_mode').options;//更改担保方式的字典项
	   		var op0 = document.createElement("OPTION");
	   		var op1 = document.createElement("OPTION");
	   		var op2 = document.createElement("OPTION");
	   		var op3 = document.createElement("OPTION");
	   		var op4 = document.createElement("OPTION");
	   		var op5 = document.createElement("OPTION");
	   		var op6 = document.createElement("OPTION");
	   		var op7 = document.createElement("OPTION");
	   		var op8 = document.createElement("OPTION");
	   		var op9 = document.createElement("OPTION");
	   		var op10 = document.createElement("OPTION");
	   		var op11 = document.createElement("OPTION");
	   		var op12 = document.createElement("OPTION");
	   		var op13 = document.createElement("OPTION");
	   		var op14 = document.createElement("OPTION");
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op0);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op1);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op2);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op3);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op4);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op5);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op6);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op7);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op8);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op9);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op10);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op11);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op12);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op13);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op14);
	   		
	   		op0.innerText="-----请选择-----";
	   		op1.innerText="特种保函";
	   		op2.innerText="其他担保";
	   		op3.innerText="银行资信证明";
	   		op4.innerText="开工预付款保函";
	   		op5.innerText="商业承兑汇票保兑函";
	   		op6.innerText="租赁保函";
	   		op7.innerText="备用信用证";
	
	   		op8.innerText="投标保函";
	   		op9.innerText="履约保函";
	   		op10.innerText="预付款保函";
	   		op11.innerText="付款保函";
	   		op12.innerText="质量及维修保函";
	   		op13.innerText="来料加工保函";
	   		op14.innerText="分离式保函";
	   		
	   		op0.value="";
	   		op1.value="17";
	   		op2.value="14";
	   		op3.value="12";
	   		op4.value="11";
	   		op5.value="10";
	   		op6.value="04";
	   		op7.value="18";
	   		
	   		op8.value="01";
	   		op9.value="02";
	   		op10.value="09";
	   		op11.value="05";
	   		op12.value="07";
	   		op13.value="06";
	   		op14.value="21";
	   		/**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 end**/
       }
       IqpGuarantInfo.guarant_mode._obj._renderReadonly(false);
    }	
    
    function doChangeForLoad(){
       var guarant_type = IqpGuarantInfo.guarant_type._getValue();
       var guarant_mode = IqpGuarantInfo.guarant_mode._getValue();
       if("01"==guarant_type){//融资类
    	    //清空保函类型下拉选项
    		document.getElementById('IqpGuarantInfo.guarant_mode').innerHTML="";
    	    var itms1 = document.getElementById('IqpGuarantInfo.guarant_mode').options;//更改担保方式的字典项
    		var op0 = document.createElement("OPTION");
    		var op1 = document.createElement("OPTION");
    		var op2 = document.createElement("OPTION");
    		var op3 = document.createElement("OPTION");
    		var op4 = document.createElement("OPTION");
    		var op5 = document.createElement("OPTION");
    		var op6 = document.createElement("OPTION");
    		var op7 = document.createElement("OPTION");
    		var op8 = document.createElement("OPTION");
    		var op9 = document.createElement("OPTION");
    		var op10 = document.createElement("OPTION");
    		var op11 = document.createElement("OPTION");
    		var op12 = document.createElement("OPTION");
    		var op13 = document.createElement("OPTION");
    		var op14 = document.createElement("OPTION");
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op0);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op1);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op2);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op3);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op4);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op5);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op6);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op7);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op8);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op9);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op10);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op11);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op12);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op13);
    		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op14);
    		op0.innerText="-----请选择-----";
    		op1.innerText="特种保函";
    		op2.innerText="其他担保";
    		op3.innerText="银行资信证明";
    		op4.innerText="开工预付款保函";
    		op5.innerText="商业承兑汇票保兑函";
    		op6.innerText="租赁保函";
    		op7.innerText="备用信用证";

    		op8.innerText="借款保函";
    		op9.innerText="延期付款保函";
    		op10.innerText="商票保贴";
    		op11.innerText="商票保押保函";
    		op12.innerText="其他保函";
    		op13.innerText="透支保函";
    		op14.innerText="委托金融同业代开银承保函";

    		op0.value="";
    		op1.value="17";
    		op2.value="14";
    		op3.value="12";
    		op4.value="11";
    		op5.value="10";
    		op6.value="04";
    		op7.value="18";
    		
    		op8.value="03";
    		op9.value="08";
    		op10.value="15";
    		op11.value="16";
    		op12.value="13";
    		op13.value="19";
    		op14.value="20";
    		IqpGuarantInfo.guarant_mode._setValue(guarant_mode);
       }else if("02"==guarant_type){//非融资类
    	   //清空保函类型下拉选项
    	   /**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 begin**/
	   		document.getElementById('IqpGuarantInfo.guarant_mode').innerHTML="";
	   	    var itms1 = document.getElementById('IqpGuarantInfo.guarant_mode').options;//更改担保方式的字典项
	   		var op0 = document.createElement("OPTION");
	   		var op1 = document.createElement("OPTION");
	   		var op2 = document.createElement("OPTION");
	   		var op3 = document.createElement("OPTION");
	   		var op4 = document.createElement("OPTION");
	   		var op5 = document.createElement("OPTION");
	   		var op6 = document.createElement("OPTION");
	   		var op7 = document.createElement("OPTION");
	   		var op8 = document.createElement("OPTION");
	   		var op9 = document.createElement("OPTION");
	   		var op10 = document.createElement("OPTION");
	   		var op11 = document.createElement("OPTION");
	   		var op12 = document.createElement("OPTION");
	   		var op13 = document.createElement("OPTION");
	   		var op14 = document.createElement("OPTION");
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op0);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op1);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op2);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op3);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op4);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op5);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op6);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op7);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op8);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op9);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op10);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op11);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op12);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op13);
	   		document.getElementById('IqpGuarantInfo.guarant_mode').options.add(op14);
	   		
	   		op0.innerText="-----请选择-----";
	   		op1.innerText="特种保函";
	   		op2.innerText="其他担保";
	   		op3.innerText="银行资信证明";
	   		op4.innerText="开工预付款保函";
	   		op5.innerText="商业承兑汇票保兑函";
	   		op6.innerText="租赁保函";
	   		op7.innerText="备用信用证";
	
	   		op8.innerText="投标保函";
	   		op9.innerText="履约保函";
	   		op10.innerText="预付款保函";
	   		op11.innerText="付款保函";
	   		op12.innerText="质量及维修保函";
	   		op13.innerText="来料加工保函";
	   		op14.innerText="分离式保函";
	   		
	   		op0.value="";
	   		op1.value="17";
	   		op2.value="14";
	   		op3.value="12";
	   		op4.value="11";
	   		op5.value="10";
	   		op6.value="04";
	   		op7.value="18";
	   		
	   		op8.value="01";
	   		op9.value="02";
	   		op10.value="09";
	   		op11.value="05";
	   		op12.value="07";
	   		op13.value="06";
	   		op14.value="21";
	   		IqpGuarantInfo.guarant_mode._setValue(guarant_mode);
	   		/**modified by lisj 2015-10-8 需求编号:XD150313021 关于保函类型的申请需求 end**/
       }
       IqpGuarantInfo.guarant_mode._obj._renderReadonly(false);
    }	

  //校验回购日期
	function dateCheck(){
		 var end_date = IqpGuarantInfo.end_date._getValue();//保函到期日
		 if(end_date){
			 var todayDate='${context.OPENDAY}';
			 var flag = CheckDate1BeforeDate2(end_date,todayDate);
             if(end_date==todayDate){
                 return true;
             }
             if(flag){
            	 alert("【保函到期日】不能小于当前日期！");
            	 IqpGuarantInfo.end_date._setValue("");
 				 return false;
             }
		 }
	};
	function getOrgNo(data){
		IqpGuarantInfo.ben_acct_org_no._setValue(data.bank_no._getValue());
		IqpGuarantInfo.ben_acct_org_name._setValue(data.bank_name._getValue());
	};
	//XD150209008 受益行行号控制 仅对福州和漳州分行进行控制 Edited by FCL 20150310
    function doCheckGuarantType(){
    	var guarantType = IqpGuarantInfo.guarant_mode._getValue();
    	if(guarantType==null || guarantType == ''){
			alert("请选择保函类型");
			IqpGuarantInfo.ben_acct_org_no._setValue("");
			IqpGuarantInfo.ben_acct_org_name._setValue("");
			return ;
		}else{
			checkTyhh();
		}
    }
    function checkTyhh(){
		var baon = IqpGuarantInfo.ben_acct_org_no._getValue();
		var guarantType = IqpGuarantInfo.guarant_mode._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					doSave();
				}else {
					alert("受益行行号有误，请重新选择!");
					IqpGuarantInfo.ben_acct_org_no._setValue("");
					IqpGuarantInfo.ben_acct_org_name._setValue("");
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
		var param = "&ben_acct_org_no="+baon+"&serno=${context.serno}&guarantType="+guarantType;
		var url = '<emp:url action="checkTyhh4IqpGuarantInfo.do"/>'+param;
		url = EMPTools.encodeURI(url);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null); 
    }
    /**add by lisj 2015年4月27日 校验开立类型，于2015-5-19上线 begin**/
    function doChangeOType(){
     var guarant_mode = IqpGuarantInfo.guarant_mode._getValue();
     var options = IqpGuarantInfo.guarant_mode._obj.element.options;
     if(guarant_mode =='20'){
    	 IqpGuarantInfo.open_type._setValue('02');
    	 IqpGuarantInfo.open_type._obj._renderReadonly(true);
     }else{
    	 //IqpGuarantInfo.open_type._setValue('');
    	 IqpGuarantInfo.open_type._obj._renderReadonly(false);
     }    
    }
    /**add by lisj 2015年4月27日 校验开立类型，于2015-5-19上线 end**/
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="updateIqpGuarantInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpGuarantInfoGroup" maxColumn="2" title="保函信息">
			<emp:select id="IqpGuarantInfo.guarant_type" label="保函种类" required="true" dictname="STD_ZB_GUARANT_MODEL" onchange="doChange()"/>
			<!-- add by lisj 2015年4月27日 校验开立类型，于2015-5-19上线 begin -->
			<emp:select id="IqpGuarantInfo.guarant_mode" label="保函类型" required="true" dictname="STD_ZB_GUARANT_TYPE" readonly="false" onchange="doChangeOType();"/> 
			<emp:select id="IqpGuarantInfo.open_type" label="开立类型" required="true" dictname="STD_ZB_OPEN_TYPE" />
			<!-- add by lisj 2015年4月27日 校验开立类型，于2015-5-19上线 end --> 
			<emp:select id="IqpGuarantInfo.guarant_pay_type" label="保函付款方式" required="true" dictname="STD_GUARANT_PAY_TYPE"/> 
			<emp:select id="IqpGuarantInfo.is_bank_format" label="是否我行标准格式" required="true"  dictname="STD_ZX_YES_NO" />
            <emp:date id="IqpGuarantInfo.end_date" label="保函到期日" required="true" onblur="dateCheck()"/>
 
			<emp:text id="IqpGuarantInfo.item_name" label="项目名称" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_long"/>
			<emp:text id="IqpGuarantInfo.cont_no" label="商务合同编号" maxlength="40" required="false" /> 
			<emp:text id="IqpGuarantInfo.cont_name" label="商务合同名称" maxlength="100" required="false" />
			<emp:text id="IqpGuarantInfo.corre_busnes_cont_amt" label="商务合同金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpGuarantInfo.cur_type" label="商务合同币种" required="false"  dictname="STD_ZX_CUR_TYPE" />
			 <emp:text id="IqpGuarantInfo.ben_name" label="受益人名称" maxlength="80" required="true" cssElementClass="emp_field_text_long" colSpan="2"/> 
			<emp:text id="IqpGuarantInfo.ben_addr" label="受益人地址" maxlength="150" required="true" cssElementClass="emp_field_text_long" colSpan="2"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpGuarantInfo.ben_acct_org_no" label="受益人开户行行号" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOrgNo" required="true" buttonLabel="选择" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpGuarantInfo.ben_acct_org_name" label="受益人开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly" />
			<emp:text id="IqpGuarantInfo.ben_acct_no" label="受益人账号" maxlength="40" required="true" />
			<emp:text id="IqpGuarantInfo.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true" />
		    
		    <emp:text id="IqpGuarantInfo.chrg_rate" label="手续费率" required="false" hidden="true" dataType="Rate"/> 
		    <emp:select id="IqpGuarantInfo.is_agt_guarant" label="是否转开代理行保函" required="false" hidden="true" onchange="isBankFormat()" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpGuarantInfo.agt_bank_no" label="代理行行号"  required="false" hidden="true"/>
			<emp:text id="IqpGuarantInfo.agt_bank_name" label="代理行名称" maxlength="100" required="false" hidden="true"/> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="checkGuarantType" label="保存" op="update"/>  
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
