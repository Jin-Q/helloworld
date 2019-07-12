<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String cont_no="";
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("cont_no")){
		cont_no = (String)context.getDataValue("cont_no");
	}
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	$(document).ready(function(){
		/** modified by yangzy 2015/07/20 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		<%if("1".equals(flag)){%>
		$(".emp_gridlayout_title:first").text("保证金追加申请");
		$(".emp_field_label:eq(16)").text("追加保证金金额");
		$(".emp_field_label:eq(13)").text("追加后保证金比例");
		$(".emp_field_label:eq(14)").text("追加后保证金汇率");
		$(".emp_field_label:eq(15)").text("追加后保证金金额");
		$(".emp_gridlayout_title:eq(2)").text("追加明细");
		<%}%>
		<%if("2".equals(flag)){%>
		$(".emp_gridlayout_title:first").text("保证金提取申请");
		$(".emp_field_label:eq(16)").text("提取保证金金额");
		$(".emp_field_label:eq(13)").text("提取后保证金比例");
		$(".emp_field_label:eq(14)").text("提取后保证金汇率");
		$(".emp_field_label:eq(15)").text("提取后保证金金额");
		$(".emp_gridlayout_title:eq(2)").text("提取明细");
		<%}%>
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	 });
	//保存按钮操作事件
	function doNext(){
		if(!IqpBailSubDis._checkAll()){
			return;
		}
		var adjust_bail_amt = IqpBailSubDis.adjust_bail_amt._getValue();
		if(parseFloat(adjust_bail_amt)<=0){
           alert("请输入追加/提取保证金比例!");
           return;
		}
		var form = document.getElementById("submitForm");
		IqpBailSubDis._toForm(form);
		IqpBailSubDisDetailList._toForm(form);
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
					alert("保存成功!");
					var url = '<emp:url action="queryIqpBailSubDisList.do"/>?flag=<%=flag%>';
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else {
					alert("此笔申请信息，未对保证金进行追加或者提取操作，新增失败!"); 
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
		var postData = YAHOO.util.Connect.setForm(form);
		var url = EMPTools.encodeURI(form.action);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}
	//返回按钮操作事件
	function doReturn(){
		var url = '<emp:url action="queryIqpBailSubDisList.do"/>?flag=<%=flag%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	
	function doLoad(){
	    /** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		// var flag=<%=flag%>;
		/** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
		var cont_no='<%=cont_no%>';
		var serno = IqpBailSubDis.serno._getValue();
		var callback = {
    			success : "doSuccess",
    			isJSON : true,
    			form : ""
    		};	
		//异步获取原保证金金额和原保证金比例
		if(serno ==null || serno ==""){
			var url = '<emp:url action="queryBailAmtByContNo.do"/>?cont_no='+cont_no;
		}else{
			var url = '<emp:url action="queryBailAmtByContNo.do"/>?cont_no='+cont_no+'&serno='+serno;
		}
		url = EMPTools.encodeURI(url);
    	EMPTools.ajaxRequest('POST',url, callback);
    	/** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
   		//if("1"==flag){
		//	IqpBailSubDis.flag._setValue("1");
		//}else if("2"==flag){
		//	IqpBailSubDis.flag._setValue("2");
		//}
   		//checkIsShowAddBail(flag);
    	/** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	};
    //-------------判断是否显示新增保证金------------
    function checkIsShowAddBail(flag){
        var isShowAddBail = '${context.isShowAddBail}'; 
        if(isShowAddBail != "yes" || flag == "2"){
        	document.getElementById('addpubbail').style.display="none";
        	IqpBailSubDis.PubBailInfo.bail_acct_no._obj._renderRequired(false);
        	IqpBailSubDis.PubBailInfo.bail_acct_name._obj._renderRequired(false);
        	IqpBailSubDis.PubBailInfo.adjust_amt._obj._renderRequired(false);
        	IqpBailSubDis.addflag._setValue("2");
        }else{
        	document.getElementById('IqpBailSubDisDetail').style.display="none";
        	IqpBailSubDis.PubBailInfo.bail_acct_no._obj.addOneButton('uniquCheck','获 取', getBailNo);
        	IqpBailSubDis.addflag._setValue("1");
        	setAdjustAmt();
        	getBailNo();
        }
    };

  //-------------通过账号获取在我行的保证金信息------------
    function getBailNo(){
  		 var acctNo = IqpBailSubDis.PubBailInfo.bail_acct_no._getValue();
  	        if(acctNo == null || acctNo == ""){
  				return;
  	        }
  			var handleSuccess = function(o){
  				if(o.responseText !== undefined){
  					try {
  						var jsonstr = eval("("+o.responseText+")");
  					} catch(e) {
  						alert("Parse jsonstr1 define error!" + e.message);
  						return;
  					}
  					var flag = jsonstr.flag;
  					var retMsg = jsonstr.mes;
  					if(flag == "success"){
  						var GUARANTEE_ACCT_NO = jsonstr.BODY.AcctNoCrdNo;//账号
  	  					var GUARANTEE_ACCT_NAME = jsonstr.BODY.AcctNm;//账户名称
  	  					var CCY = jsonstr.BODY.Ccy;//币种
  	  					var AMT = jsonstr.BODY.AcctBal;//账户余额
  	  					var GUARANTEE_TYPE = jsonstr.BODY.PdTp;//保证金类型:产品类型
  	  					var INT_RATE = jsonstr.BODY.BnkInnrIntRate;//行内利率
  	  					var INTER_FLT_RATE = jsonstr.BODY.FltIntRate;//浮动利率
  	  					var TERM = jsonstr.BODY.Trm;//期限
  	  					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.AcctBlngInstNo;//机构ID
  	  					var OPEN_ACCT_BRANCH_ID_displayname = jsonstr.BODY.AcctBlngInstNo_displayname;//机构名称
  	  					var ACCT_STATUS = jsonstr.BODY.AcctSt;//账户状态
  	  				    var BAIL_ACCT_GL_CODE = jsonstr.BODY.AcctSeqNo;//增加科目号
  	  				    IqpBailSubDis.PubBailInfo.bail_acct_name._setValue(GUARANTEE_ACCT_NAME);
  	  				    IqpBailSubDis.PubBailInfo.bail_acct_gl_code._setValue(BAIL_ACCT_GL_CODE);
  	  			        IqpBailSubDis.PubBailInfo.cur_type._setValue(CCY);
  	  		            IqpBailSubDis.PubBailInfo.amt._setValue(AMT);
  	  		        	IqpBailSubDis.PubBailInfo.bail_type._setValue(GUARANTEE_TYPE);
						IqpBailSubDis.PubBailInfo.dep_term._setValue(TERM);
						IqpBailSubDis.PubBailInfo.open_org._setValue(OPEN_ACCT_BRANCH_ID);
						IqpBailSubDis.PubBailInfo.open_org_displayname._setValue(OPEN_ACCT_BRANCH_ID_displayname);
  						if(INT_RATE == ""){
  							IqpBailSubDis.PubBailInfo.rate._setValue("");
  	  					}else{
  	  					    IqpBailSubDis.PubBailInfo.rate._setValue(parseFloat(INT_RATE)/100);
  	  	  	  			}
  						if(INTER_FLT_RATE == "0.0"){
  							IqpBailSubDis.PubBailInfo.up_rate._obj.element.value=0.00;
  	  					}else if(INTER_FLT_RATE == ""){
  	  					     IqpBailSubDis.PubBailInfo.up_rate._setValue("");
  	  	  				}else{
  	  	  				     IqpBailSubDis.PubBailInfo.up_rate._setValue(parseFloat(INTER_FLT_RATE)/100);
  	  	  	  			}
  					}else {
  						alert(retMsg); 
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
  			var url = '<emp:url action="getPubBailInfoForEsb.do"/>?bail_acct_no='+acctNo+'&service_code=30130001&sence_code=01';	
  			url = EMPTools.encodeURI(url);
  			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };
  //-------------新增保证金后修改页面信息------------
    function changeAmt(){
        /** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
        var adjust_amt = IqpBailSubDis.PubBailInfo.adjust_amt._getValue();
		IqpBailSubDis.adjust_bail_amt._setValue(adjust_amt);//追加保证金金额
		IqpBailSubDis.adjusted_bail_amt._setValue(adjust_amt);//追加后保证金金额
		var cont_rmb_amount = IqpBailSubDis.cont_rmb_amount._getValue();//合同折合成人民币金额
		var adjusted_bail_amt = IqpBailSubDis.adjusted_bail_amt._getValue();
	    var security_exchange_rate = IqpBailSubDis.adj_security_exchange_rate._getValue();//保证金汇率
		adjusted_bail_perc = parseFloat(adjusted_bail_amt)*parseFloat(security_exchange_rate)/parseFloat(cont_rmb_amount);
		IqpBailSubDis.adjusted_bail_amt._setValue(adjust_amt);//追加后保证金金额
		IqpBailSubDis.adjusted_bail_perc._setValue(adjusted_bail_perc);//追加后保证金比例
		/** deleted by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	};	
	
	function doSuccess(data){
		IqpBailSubDis.cont_cur_type._setValue(data.cont_cur_type);
		IqpBailSubDis.exchange_rate._setValue(data.exchange_rate);
		IqpBailSubDis.cont_amt._setValue(data.cont_amt);
		IqpBailSubDis.cont_rmb_amount._setValue(data.cont_rmb_amount);

		IqpBailSubDis.security_cur_type._setValue(data.security_cur_type);
		IqpBailSubDis.security_exchange_rate._setValue(data.security_exchange_rate);
		IqpBailSubDis.ori_bail_amt._setValue(data.ori_bail_amt);
		IqpBailSubDis.ori_bail_perc._setValue(data.ori_bail_perc);
		IqpBailSubDis.security_rmb_amt._setValue(data.security_rmb_amt);
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		IqpBailSubDis.adj_security_exchange_rate._setValue(data.adj_security_exchange_rate);
		var isShowAddBail = '${context.isShowAddBail}';
        if(isShowAddBail != "yes"){
        	onChange();
        }
        changeRmbAmt4SecurityChange4OnLoad();
		var flag=<%=flag%>;
  		if("1"==flag){
			IqpBailSubDis.flag._setValue("1");
		}else if("2"==flag){
			IqpBailSubDis.flag._setValue("2");
		}
		checkIsShowAddBail(flag);  		
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	};

	function setAdjustAmt(){
		IqpBailSubDis.PubBailInfo.adjust_amt._setValue(IqpBailSubDis.adjust_bail_amt._getValue());
	};
	
	function changeHeight(){
		var iframeid = document.getElementById("rightframe");
		iframeid.height = "80px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	};
	//选择责任人返回方法
	function setconId(data){
		IqpBailSubDis.manager_id_displayname._setValue(data.actorname._getValue());
		IqpBailSubDis.manager_id._setValue(data.actorno._getValue());
		IqpBailSubDis.manager_br_id._setValue(data.orgid._getValue());
		IqpBailSubDis.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpBailSubDis.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					IqpBailSubDis.manager_br_id._setValue(jsonstr.org);
					IqpBailSubDis.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					IqpBailSubDis.manager_br_id._setValue("");
					IqpBailSubDis.manager_br_id_displayname._setValue("");
					IqpBailSubDis.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpBailSubDis.manager_id._getValue();
					IqpBailSubDis.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpBailSubDis.manager_br_id._setValue("");
					IqpBailSubDis.manager_br_id_displayname._setValue("");
					IqpBailSubDis.manager_br_id_displayname._obj._renderReadonly(false);
					IqpBailSubDis.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpBailSubDis.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//选择机构信息返回方法
	function getOrgID(data){
		IqpBailSubDis.manager_br_id._setValue(data.organno._getValue());
		IqpBailSubDis.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function onChange(){
		var num = IqpBailSubDisDetailList._getSize();
		var totel=0;
		for(var i=0;i<num;i++){
			var amt = IqpBailSubDisDetailList[i].adjust_amt._getValue();
			totel = parseFloat(totel)+parseFloat(amt);
		}
		//IqpBailSubDis.adjust_bail_amt._setValue(totel.toString());//追加或提取保证金金额
		var cont_rmb_amount = IqpBailSubDis.cont_rmb_amount._getValue();//合同折合人民币金额
		var security_exchange_rate = IqpBailSubDis.security_exchange_rate._getValue();//保证金汇率
		var ori_bail_amt = IqpBailSubDis.ori_bail_amt._getValue();//原保证金金额
		var flag=<%=flag%>;
		var adjust_bail_amt = IqpBailSubDis.adjust_bail_amt._getValue();
		if("1"==flag){//追加
			if(totel<adjust_bail_amt){
                alert("追加的保证金金额不能小于追加保证金金额");
                for(var i=0;i<num;i++){
        			var amt = IqpBailSubDisDetailList[i].adjust_amt._setValue("");
        		}
			}
			//adjusted_bail_amt = parseFloat(totel)+parseFloat(ori_bail_amt);
		}else if("2"==flag){//提取
			if(totel>adjust_bail_amt){
                alert("提取的保证金金额不能大于提取保证金金额");
                for(var i=0;i<num;i++){
        			var amt = IqpBailSubDisDetailList[i].adjust_amt._setValue("");
        		}
			}
		}
		/** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		for(var i=0;i<num;i++){
			var adjust_amt = IqpBailSubDisDetailList[i].adjust_amt._getValue();
			if(adjust_amt != null && adjust_amt != "" && adjust_amt != '0'){
				IqpBailSubDis.adjust_bail_amt._setValue(adjust_amt);//追加保证金金额
				var cont_rmb_amount = IqpBailSubDis.cont_rmb_amount._getValue();//合同折合成人民币金额
				var adjust_bail_amt = IqpBailSubDis.adjust_bail_amt._getValue();
				var security_rmb_amt = IqpBailSubDis.security_rmb_amt._getValue();
			    var security_exchange_rate = IqpBailSubDis.adj_security_exchange_rate._getValue();//保证金汇率
				if("1"==flag){
					adjusted_bail_perc = (parseFloat(adjust_bail_amt)+parseFloat(security_rmb_amt))*parseFloat(security_exchange_rate)/parseFloat(cont_rmb_amount);
				    var adjusted_bail_amt = parseFloat(security_rmb_amt)+parseFloat(adjust_bail_amt);
				}else if("2"==flag){
					adjusted_bail_perc = (parseFloat(security_rmb_amt)-parseFloat(adjust_bail_amt))*parseFloat(security_exchange_rate)/parseFloat(cont_rmb_amount);
				    var adjusted_bail_amt = parseFloat(security_rmb_amt)-parseFloat(adjust_bail_amt);
				}
				IqpBailSubDis.adjusted_bail_amt._setValue(''+adjusted_bail_amt+'');//追加后保证金金额
				IqpBailSubDis.adjusted_bail_perc._setValue(''+adjusted_bail_perc+'');//追加后保证金比例
			}
		}
		/** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
		//adjusted_bail_perc = parseFloat(adjusted_bail_amt)*parseFloat(security_exchange_rate)/parseFloat(cont_rmb_amount);
		//IqpBailSubDis.adjusted_bail_amt._setValue(adjusted_bail_amt.toString());//追加/提取后保证金金额
		//IqpBailSubDis.adjusted_bail_perc._setValue(adjusted_bail_perc.toString());//追加/提取后保证金比例
	};	
	
	function doSubmitFlow(){
		if(!IqpBailSubDis._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpBailSubDis._toForm(form);
		IqpBailSubDisDetailList._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var cont_no = IqpBailSubDis.cont_no._getValue();
				serno = jsonstr.serno;
				if(flag == "success"){
					IqpBailSubDis.serno._setValue(serno);
					submitFlow();
				}else {
					alert("此笔申请信息，未对保证金进行追加或者提取操作，新增失败!"); 
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
		var postData = YAHOO.util.Connect.setForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = EMPTools.encodeURI(form.action);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}
	var serno="";//全局变量
	//流程提交信息
	function submitFlow(){
		var approve_status = IqpBailSubDis.approve_status._getValue();
		var cus_id = IqpBailSubDis.cus_id._getValue();
		var cus_name = IqpBailSubDis.cus_id_displayname._getValue();
		var adjust_bail_amt = IqpBailSubDis.adjust_bail_amt._getValue();
		
		WfiJoin.table_name._setValue("IqpBailSubDis");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		
		WfiJoin.amt._setValue(adjust_bail_amt);
		
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		var flag=<%=flag%>;
		if("1"==flag){//追加（ 流程申请类型，对应字典项[ZB_BIZ_CATE]）
			WfiJoin.appl_type._setValue("1001");
			WfiJoin.prd_name._setValue("保证金追加");
		}else if("2"==flag){//提取
			WfiJoin.appl_type._setValue("1002");
			WfiJoin.prd_name._setValue("保证金提取");
		}
		initWFSubmit(false);
	};
	function _doKeypressDown() {
		try{
			if(IqpBailSubDis.adjusted_bail_perc._obj.element.focus){
				IqpBailSubDis.adjusted_bail_perc._obj.element.select();
		    }
		}catch(e){
			alert(e);
		}
	};
	//-------------------计算保证金金额(修改保证金比例的时候)-----------------------
	function changeRmbAmt4SecurityChange(){
        var flag ='${context.flag}';
		var prd_id = '${context.prd_id}';
		var appAmt = IqpBailSubDis.cont_amt._getValue();//合同金额
		var security_cur_type = IqpBailSubDis.security_cur_type._getValue();//原保证金币种
		if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
			var rate = IqpBailSubDis.exchange_rate._getValue();//汇率
			//var setRate =IqpLoanApp.security_rate._obj.element.value;//保证金比例
			var setRate =IqpBailSubDis.adjusted_bail_perc._getValue();//追加/提取后保证金比例
			var ori_bail_perc =IqpBailSubDis.ori_bail_perc._getValue();//原保证金比例
			if(flag == "1"){
	            if((parseFloat(setRate)/100) <= parseFloat(ori_bail_perc)){
                    alert("追加后保证金比例应大于原保证金比例!");
                    IqpBailSubDis.adjusted_bail_perc._setValue("");
                    return;
		        }
	        }else if(flag == "2"){
	        	if((parseFloat(setRate)/100) > parseFloat(ori_bail_perc)){
                    alert("提取后保证金比例应小于原保证金比例!");
                    IqpBailSubDis.adjusted_bail_perc._setValue("");
                    return;
		        }
	        }
	        /** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			var security_exchange_rate = IqpBailSubDis.adj_security_exchange_rate._getValue();//调整保证金汇率
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			if(setRate == null || setRate == ""){
				setRate = 0;
			}
			//如果是贸易融资业务
			var security_amt;
			if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
				if(prd_id == "700020" || prd_id =="700021"){
					var floodact_perc = '${context.floodact_perc}';
	           	    if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
	           	    	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
	           	    	security_amt = Math.ceil((parseFloat(appAmt*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
	           	    	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
	           	    }else{
	           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
	           	    	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
	               	}
	            }else{
	            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
	            	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
	            }
			}else{
			    security_amt = Math.round((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)),2)/100;//保证金金额
			    IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
		    }
			var ori_bail_amt = IqpBailSubDis.ori_bail_amt._getValue();
			if(flag == "1"){
				IqpBailSubDis.adjust_bail_amt._setValue(''+(security_amt - ori_bail_amt)+'');
	        }else if(flag == "2"){
	        	IqpBailSubDis.adjust_bail_amt._setValue(''+(ori_bail_amt- security_amt)+'');
	        }
	        /** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			var isShowAddBail = '${context.isShowAddBail}'; 
	        if(isShowAddBail != "yes" || flag == "2"){
	        }else{
	        	setAdjustAmt();
	        }
	        /** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
		}
	};
	
	/** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
	function changeRmbAmt4SecurityChange4OnLoad(){
        var flag ='${context.flag}';
		var prd_id = '${context.prd_id}';
		var appAmt = IqpBailSubDis.cont_amt._getValue();//合同金额
		var security_cur_type = IqpBailSubDis.security_cur_type._getValue();//原保证金币种
		if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
			var rate = IqpBailSubDis.exchange_rate._getValue();//汇率
			//var setRate =IqpLoanApp.security_rate._obj.element.value;//保证金比例
			var setRate =IqpBailSubDis.adjusted_bail_perc._getValue();//追加/提取后保证金比例
			if(setRate != null && setRate != ""){
				setRate = parseFloat(setRate)*100;
				var ori_bail_perc =IqpBailSubDis.ori_bail_perc._getValue();//原保证金比例
				if(flag == "1"){
		            if((parseFloat(setRate)/100) <= parseFloat(ori_bail_perc)){
	                    alert("追加后保证金比例应大于原保证金比例!");
	                    IqpBailSubDis.adjusted_bail_perc._setValue("");
	                    return;
			        }
		        }else if(flag == "2"){
		        	if((parseFloat(setRate)/100) > parseFloat(ori_bail_perc)){
	                    alert("提取后保证金比例应小于原保证金比例!");
	                    IqpBailSubDis.adjusted_bail_perc._setValue("");
	                    return;
			        }
		        }
				var security_exchange_rate = IqpBailSubDis.adj_security_exchange_rate._getValue();//调整保证金汇率
				if(setRate == null || setRate == ""){
					setRate = 0;
				}
				//如果是贸易融资业务
				var security_amt;
				if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
					if(prd_id == "700020" || prd_id =="700021"){
						var floodact_perc = '${context.floodact_perc}';
		           	    if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
		           	    	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
		           	    	security_amt = Math.ceil((parseFloat(appAmt*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
		           	    	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
		           	    }else{
		           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
		           	    	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
		               	}
		            }else{
		            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
		            	IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
		            }
				}else{
				    security_amt = Math.round((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)),2)/100;//保证金金额
				    IqpBailSubDis.adjusted_bail_amt._setValue(''+security_amt+'');
			    }
				var ori_bail_amt = IqpBailSubDis.ori_bail_amt._getValue();
				if(flag == "1"){
					IqpBailSubDis.adjust_bail_amt._setValue(''+(security_amt - ori_bail_amt)+'');
		        }else if(flag == "2"){
		        	IqpBailSubDis.adjust_bail_amt._setValue(''+(ori_bail_amt- security_amt)+'');
		        }
			}
		}
	};
	/** added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:form id="submitForm" action="addIqpBailSubDisRecord.do?op=<%=op %>" method="POST">
		<emp:gridLayout id="IqpBailSubDisGroup" title="保证金追加申请" maxColumn="2">
			<emp:text id="IqpBailSubDis.serno" label="业务编号" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpBailSubDis.cont_no" label="合同编号" maxlength="60" required="true" defvalue="${context.cont_no}" readonly="true"/>
			<emp:text id="IqpBailSubDis.cus_id" label="客户码" maxlength="60" required="false" defvalue="${context.cus_id}" readonly="true"/>
			<emp:text id="IqpBailSubDis.cus_id_displayname" label="客户名称"  required="false" defvalue="${context.cus_id_displayname}" cssElementClass="emp_field_text_readonly"/>
			
			<emp:select id="IqpBailSubDis.cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" required="false" readonly="true" hidden="false"/>
			<emp:text id="IqpBailSubDis.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true" hidden="false"/>
			<emp:text id="IqpBailSubDis.cont_amt" label="合同金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.cont_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:select id="IqpBailSubDis.security_cur_type" label="原保证金币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpBailSubDis.security_exchange_rate" label="原保证金汇率"  maxlength="16" readonly="true" required="false" />
			<emp:text id="IqpBailSubDis.ori_bail_perc" label="原保证金比例" maxlength="20" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.ori_bail_amt" label="原保证金金额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBailSubDis.security_rmb_amt" label="原保证金折算人民币金额" maxlength="18" readonly="true" required="false" colSpan="2" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="IqpBailSubDis.adjusted_bail_perc" label="追加/提取后保证金比例" maxlength="20" required="true" dataType="Rate" readonly="false" defvalue="${context.IqpBailSubDis.ori_bail_perc}" onfocus="_doKeypressDown()" onchange="changeRmbAmt4SecurityChange()"/>
			<!-- added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start-->
			<emp:text id="IqpBailSubDis.adj_security_exchange_rate" label="追加/提取后保证金汇率"  maxlength="16" readonly="true" required="false" />
			<!-- added by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end-->
			<emp:text id="IqpBailSubDis.adjusted_bail_amt" label="追加/提取后保证金金额" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="${context.IqpBailSubDis.ori_bail_amt}" cssElementClass="emp_currency_text_readonly"/>
            <emp:text id="IqpBailSubDis.adjust_bail_amt" label="追加/提取保证金金额" maxlength="18" required="false" dataType="Currency"  readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="IqpBailSubDis.flag" label="申请类型（1--追加，2--提取）" maxlength="6" required="false" hidden="true"/>
			<emp:text id="IqpBailSubDis.addflag" label="申请类型（1--新增，2--追加）" maxlength="6" required="false" hidden="true"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" maxColumn="2" title="登记信息">
			<!-- modified by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin -->
			<emp:pop id="IqpBailSubDis.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  defvalue="${context.manager_id_displayname}"/>
			<emp:pop id="IqpBailSubDis.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?manager_id=${context.manager_id}&restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org"  defvalue="${context.manager_br_id_displayname}"/>
			<!-- modified by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end -->
			<emp:text id="IqpBailSubDis.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName"/>
			<emp:text id="IqpBailSubDis.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName"/>
			<emp:text id="IqpBailSubDis.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			<!-- modified by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin -->
			<emp:text id="IqpBailSubDis.manager_br_id" label="管理机构"  required="true" hidden="true" defvalue="${context.manager_br_id}"/>
			<emp:text id="IqpBailSubDis.manager_id" label="责任人" required="true" readonly="false" hidden="true" defvalue="${context.manager_id}"/>
			<!-- modified by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end -->
			<emp:text id="IqpBailSubDis.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpBailSubDis.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true" defvalue="$organNo"/>
		    <emp:select id="IqpBailSubDis.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000" readonly="true"/>
		</emp:gridLayout>
	</emp:form>
	 <div id=IqpBailSubDisDetail>
		<div class='emp_gridlayout_title'>追加明细</div>
		<emp:table icollName="IqpBailSubDisDetailList" pageMode="false" editable="true" url="" >
			<emp:text id="optType" label="操作方式" hidden="true" />
			<emp:text id="serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="cont_no" label="合同编号" required="false" readonly="true"/>
			<emp:text id="bail_acct_no" label="保证金账号" maxlength="60" required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:text id="origi_bail_bal" label="原保证金金额 " required="false"  dictname="STD_ZB_CERT_TYP" maxlength="18" readonly="true" dataType="Currency"/>
			<emp:text id="adjust_amt" label="追加/提取金额 " required="true" readonly="false" dataType="Currency" maxlength="18" defvalue="0" onchange="onChange()"/>
		</emp:table>
		</div>
		<br>
		<div id='addpubbail'>
		   <emp:gridLayout id="PubBailInfo" maxColumn="2" title="新增保证金">
				<emp:text id="IqpBailSubDis.PubBailInfo.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.cus_id" label="客户码" maxlength="40" required="false" defvalue="${context.cus_id}" hidden="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_no" label="保证金账号" maxlength="40" required="true"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" readonly="true" defvalue="123"/>
				<emp:select id="IqpBailSubDis.PubBailInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.amt" label="账户余额" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
				<emp:text id="IqpBailSubDis.PubBailInfo.rate" label="利率" maxlength="10" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly" />			
				<emp:text id="IqpBailSubDis.PubBailInfo.up_rate" label="上浮比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly" />
				<emp:select id="IqpBailSubDis.PubBailInfo.bail_type" label="保证金类型" required="false" dictname="STD_PUB_BAIL_TYPE" readonly="true"/>
				<emp:select id="IqpBailSubDis.PubBailInfo.dep_term" label="存期"  required="false" dictname="STD_BAIL_DEP_TERM" readonly="true" />
				<emp:text id="IqpBailSubDis.PubBailInfo.open_org_displayname" label="开户机构" required="false" readonly="true" />
				<emp:select id="IqpBailSubDis.PubBailInfo.bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS" readonly="true" required="false" defvalue="0" />
				<emp:text id="IqpBailSubDis.PubBailInfo.adjust_amt" label="追加金额 " required="true" readonly="false" dataType="Currency" maxlength="18" onchange="changeAmt()"/>
				<emp:text id="IqpBailSubDis.PubBailInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/> 
		   	 	<emp:text id="IqpBailSubDis.PubBailInfo.open_org" label="开户机构" required="false" hidden="true"/>
		   	 	<emp:text id="IqpBailSubDis.PubBailInfo.bail_acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
		</emp:gridLayout>
		</div>
		
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="next" label="保存" op="add"/>
			<emp:button id="submitFlow" label="放入流程"/>
			<emp:button id="return" label="返回"/>
		</div>
</body>
</html>
</emp:page>

