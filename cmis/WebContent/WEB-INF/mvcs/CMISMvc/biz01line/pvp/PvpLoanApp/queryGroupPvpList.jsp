<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border: 1px solid #b7b7b7;
width:80px;
}
</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CtrLoanCont._toForm(form);
		CtrLoanContList._obj.ajaxQuery(null,form);
	};
	
	//放款操作
	function doOutPvp(){
		//added by yagnzy 20140829 放款改造  begin
		var buttonObjWait = document.getElementById('button_outPvp');
		buttonObjWait.disabled=true;
		buttonObjWait.innerHTML='请稍等..';
		//added by yagnzy 20140829 放款改造  end
		
		var recordCount = CtrLoanContList._obj.recordCount;//取总的记录数
		var count = 0;
		var totPvpAmt = 0;
		for(var i=0;i<recordCount;i++){
			var box = CtrLoanContList._obj.data[i].box._getValue();
			if(box=="on"){
				 count ++;
				 var pvp_amt = CtrLoanContList._obj.data[i].pvp_amt._getValue();
				 totPvpAmt = parseFloat(pvp_amt)+parseFloat(totPvpAmt);
				 var cont_balance = CtrLoanContList._obj.data[i].cont_balance._getValue(); 
				 if((pvp_amt == null || cont_balance == null) || (pvp_amt ==0 || cont_balance==0)){
					 alert("请检查勾选的第"+ count +"条记录【放款余额】和【合同余额】");    
					 //added by yangzy 20140829 放款改造  begin
					 buttonObjWait.disabled=false;
					 buttonObjWait.innerHTML='放款';
					 //added by yangzy 20140829 放款改造  end 
					 return; 
				 }
				 if(parseFloat(cont_balance)-parseFloat(pvp_amt)<0){  
					 alert("警告:勾选的第"+ count +"条记录【出账金额】应小于等于【合同余额】");
					 //added by yangzy 20140829 放款改造  begin
					 buttonObjWait.disabled=false;
					 buttonObjWait.innerHTML='放款';
					 //added by yangzy 20140829 放款改造  end
					 return; 
				 }
			}
		}
		//判断此次放款额度+当日放款额度是否<=当日放款总额度
		var limit_amt = PvpLimitSet.limit_amt._getValue();
		if(limit_amt == null || limit_amt ==""){
          alert("请设置当日放款总额度!");
          //added by yangzy 20140829 放款改造  begin
		  buttonObjWait.disabled=false;
		  buttonObjWait.innerHTML='放款';
		  //added by yangzy 20140829 放款改造  end
          return;
		}
		var out_limit_amt = PvpLimitSet.out_limit_amt._getValue();
        if((totPvpAmt+parseFloat(out_limit_amt))>parseFloat(limit_amt)){
        	alert("当日放款金额已到上限");
        	//added by yangzy 20140829 放款改造  begin
			buttonObjWait.disabled=false;
			buttonObjWait.innerHTML='放款';
			//added by yangzy 20140829 放款改造  end
			return;
        }
		if(count>0){
		var form = document.getElementById("queryForm"); 
		var handleSuccess = function(o){
		if(o.responseText !== undefined) {
		    try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				//added by yangzy 20140829 放款改造  begin
				buttonObjWait.disabled=false;
				buttonObjWait.innerHTML='放款';
				//added by yangzy 20140829 放款改造  end
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			} 
			var flag = jsonstr.flag;
			if(flag == "success"){
				//added by yangzy 20140829 放款改造  begin
				buttonObjWait.disabled=false;
				buttonObjWait.innerHTML='放款';
				//added by yangzy 20140829 放款改造  end
				alert("出账成功!");
				window.location.reload();
			}else {
				//added by yangzy 20140829 放款改造  begin
				buttonObjWait.disabled=false;
				buttonObjWait.innerHTML='放款';
				//added by yangzy 20140829 放款改造  end
				alert("出账失败!");
			}
		}
	    };
	    var handleFailure = function(o){
	    	//added by yangzy 20140829 放款改造  begin
			buttonObjWait.disabled=false;
			buttonObjWait.innerHTML='放款';
			//added by yangzy 20140829 放款改造  end
			alert("异步请求出错！");	
		};
		var callback = { 
			success:handleSuccess,
			failure:handleFailure
		};
		form.action = "<emp:url action='outPvpLoanAppRecord.do'/>";
		CtrLoanContList._toForm(form);    
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
		}else{
			//added by yangzy 20140829 放款改造  begin
			buttonObjWait.disabled=false;
			buttonObjWait.innerHTML='放款';
			//added by yangzy 20140829 放款改造  end
            alert("请勾选放款项!");
		}
	};	

    //挂起操作
	function doGetOut(){
		var recordCount = CtrLoanContList._obj.recordCount;//取总的记录数
		var count = 0;
		for(var i=0;i<recordCount;i++){
			var box = CtrLoanContList._obj.data[i].box._getValue();
			if(box=="on"){
				 count ++;
			}
		}
		if(count>0){
			var form = document.getElementById("queryForm"); 
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
						alert("挂起成功!");
						window.location.reload();
					}else {
						alert("挂起失败!");
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
			form.action = "<emp:url action='getOutPvpRecoed.do'/>";
			CtrLoanContList._toForm(form);    
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
		}else{
            alert("请勾选挂起项!");
		}
    };
	
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};
	
	function doOnLoad(){
		PvpLimitSet.limit_amt._obj.addOneButton("limit_amt","设置",getLimitAmt);
	};

	function getLimitAmt(){
		var limitAmt = PvpLimitSet.limit_amt._getValue();
		if(limitAmt == null || limitAmt == ""){
			alert("请先输入需要设置的当日放款总额度！");
			return;
		}
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
					alert("设置成功！");
					window.location.reload();
				}else {
					alert("设置失败!");
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
		var url = "<emp:url action='updatePvpLimitSetRecord.do'/>?limit_amt="+limitAmt;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		
	};

	//查看的js
	function doOutCtrLoanContview(){
		var url;
		var selObj = CtrLoanContList._obj.getSelectedData()[0];
		var cont_no = selObj.cont_no._getValue();
		var prd_id = selObj.prd_id._getValue();
		var biz_type = selObj.biz_type._getValue();
		if(biz_type == "8"){
			if(prd_id == "300021" || prd_id == "300020"){
			 	url="<emp:url action='getCtrLoanContForDiscViewPage.do'/>?cont_no="+cont_no+"&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
		    }else{
		    	url="<emp:url action='getCtrLoanContViewPage.do'/>?cont_no="+cont_no+"&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
			}
		}else{
			if(prd_id == "300021" || prd_id == "300020"){
			 	url="<emp:url action='getCtrLoanContForDiscViewPage.do'/>?cont_no="+cont_no+"&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
		    }else{
		    	url="<emp:url action='getCtrLoanContViewPage.do'/>?cont_no="+cont_no+"&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
			}
		}
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}; 

	//出账队列放到合同签订之前，用于确定合同是否可以签订  2014-10-09 唐顺岩 
	function doConfirm(){
		
	}
	function doChange(){
		var recordCount = CtrLoanContList._obj.recordCount;//取总的记录数
		var count = 0;
		var totPvpAmt = 0;
		for(var i=0;i<recordCount;i++){
			var box = CtrLoanContList._obj.data[i].box._getValue();
			if(box=="on"){
				 count ++;
				 var pvp_amt = CtrLoanContList._obj.data[i].pvp_amt._getValue();
				 totPvpAmt = parseFloat(pvp_amt)+parseFloat(totPvpAmt);
				 var cont_balance = CtrLoanContList._obj.data[i].cont_balance._getValue(); 
				 if((pvp_amt == null || cont_balance == null) || (pvp_amt ==0 || cont_balance==0)){
					 alert("请检查勾选的第"+ count +"条记录【放款余额】和【合同余额】");    
				 }
				 if(parseFloat(cont_balance)-parseFloat(pvp_amt)<0){  
					 alert("警告:勾选的第"+ count +"条记录【出账金额】应小于等于【合同余额】");
					 return; 
				 }
			}
		}
		PvpLimitSet.select_limit_amt._setValue(''+totPvpAmt+'');
	}
	
		
	/** add by lisj 2014年10月31日  需求:【XD140818051】出账页面导出功能  begin**/
	function doExcel(){
		var recordCount = CtrLoanContList._obj.recordCount;//取总的记录数
		var count = 0;
		var contNo ="";//合同编号字符串
		for(var i=0;i<recordCount;i++){
			var box = CtrLoanContList._obj.data[i].box._getValue();
			if(box=="on"){
				var cont_no = CtrLoanContList._obj.data[i].cont_no._getValue();
				 count ++;
				 contNo += cont_no +",";
			}
		}
		if(count>0){                                                                                          
			var form = document.getElementById("queryForm");
			var url = "<emp:url action='ctrLoanContExpBatchToExcel.do'/>?contNo="+contNo;
			url = EMPTools.encodeURI(url);
			window.location=url;
		}else{
			alert("请勾选需导出的队列项!");
			}
	};
	/** add by lisj 2014年10月31日  需求:【XD140818051】出账页面导出功能  end**/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
		<emp:pop id="CtrLoanCont.cus_name" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
		<emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>    
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
		
	<emp:gridLayout id="PvpLimitSetGroup" title="放款额度控制" maxColumn="3">
			<emp:text id="PvpLimitSet.limit_amt" label="当日放款总额度" dataType="Currency"/>
			<emp:text id="PvpLimitSet.out_limit_amt" readonly="true" label="当日已放额度" dataType="Currency"/>
			<emp:text id="PvpLimitSet.select_limit_amt" readonly="true" label="当前选中额度" dataType="Currency"/>
	</emp:gridLayout>  
	
	<div align="left">
		<emp:button id="outPvp" label="放款" op="update"/>  
		<emp:button id="getOut" label="挂起" op="remove"/>
		<emp:button id="confirm" label="确定" op="confirm"/>
		<emp:button id="excel" label="导出" op="excel"/>
	</div>

	<emp:table icollName="CtrLoanContList" pageMode="false" url="pageGroupPvpQuery.do"> 
		<emp:checkbox id="box" label="选择" flat="false" onclick="doChange()"/>
		<emp:link id="cont_no" label="合同编号" operation="outCtrLoanContview"/>
		<emp:text id="cn_cont_no" label="中文合同编号" hidden="true" />
		<emp:text id="cus_id" label="客户码" hidden="true" /> 
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="is_close_loan" label="续贷标志" dictname="STD_ZX_YES_NO" />
		
		<emp:text id="dep_ln_rate" label="支行存贷比" />
		<emp:text id="cont_number" label="评分" />
		<emp:text id="cont_term" label="期限" />
		<emp:text id="joint_guar_flag" label="是否联保" dictname="STD_ZX_YES_NO" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_balance" label="合同余额" dataType="Currency" />     
		<emp:text id="pvp_amt" label="放款金额" flat="false" dataType="Currency" cssElementClass="emp_input" readonly="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="机构名称" />
		<emp:text id="input_id" label="登记人" hidden="true" />
		<emp:text id="manager_br_id" label="管理机构" required="false" readonly="true" hidden="true" />
		<emp:text id="input_br_id" label="登记机构" required="false" hidden="true" />
		<emp:text id="input_date" label="登记日期" required="false" hidden="true" />
		<emp:text id="biz_type" label="业务模式" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>